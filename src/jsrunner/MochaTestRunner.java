package jsrunner;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

/**
 * Based on a solution I've seen at:
 * http://js-testrunner.codehaus.org/index.html
 * Setup this runner on JUnit class inside an Eclipse's Java Project.
 * Then configure a TestFolder class annotation to tell it where your
 * Mocha tests really are.
 * Run the JUnit class inside Eclipse et voilˆ.
 * <code>
 * @RunWith(MochaTestRunner.class)
 * @TestsFolder("mocha-tests")
 * public class MochaTestSuiteWithAnotation {
 * 
 * }
 * </code>
 * @author mariano
 */
public class MochaTestRunner extends ParentRunner<File> {
	
	File[] testFolders;
	MochaProcess mocha;
	MochaJUnitNotifierAdapter adapter;
	
	/**
	 * Describes where are the tests in relation to the project home folder.
	 * This can have multiple values. 
	 * The default value is "test" given Mocha's convention.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@Inherited
	public @interface TestsFolder {
		String[] value();
	}

	public MochaTestRunner(Class<?> testClass) throws InitializationError {
		super(testClass);

		// Set up our resource bases.
		TestsFolder resourceBaseAnnotation = testClass.getAnnotation(TestsFolder.class);
		if (resourceBaseAnnotation == null) {
			testFolders = new File[] { new File("test") };
		} else {
			String[] anotations = resourceBaseAnnotation.value();
			testFolders = new File[anotations.length];
			for (int i = 0; i < anotations.length; i++) {
				testFolders[i] = new File(anotations[i]);
			} 
		}
		
	}

	@Override
	protected Description describeChild(File test) {
		Description description = Description.createSuiteDescription(test.getName());
		return description;
	}

	@Override
	protected List<File> getChildren() {
		List<File> children = Arrays.asList(testFolders[0].listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".js");
			}
		}));
		return children;
	}

	@Override
	protected void runChild(File test, RunNotifier notifier) {
		try {
			mocha = new MochaProcess();
			String mochaResults = mocha.run(test);
			adapter = new MochaJUnitNotifierAdapter();
			adapter.notify(mochaResults, notifier);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
