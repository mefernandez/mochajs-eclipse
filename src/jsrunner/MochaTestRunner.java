package jsrunner;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

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
public class MochaTestRunner extends Runner {
	
	File testFolders[];
	MochaResultsDescriptionBuilder descriptionBuilder;
	MochaProcess mochaProcess;
	List<String> savedResults;
	RunNotifierReplayer notifierReplayer;
	
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

	public MochaTestRunner(Class testClass) {
		setupTestFolder(testClass);
	}

	void setupTestFolder(Class testClass) {
		// Set up our resource bases.
		TestsFolder resourceBaseAnnotation = (TestsFolder)testClass.getAnnotation(TestsFolder.class);
		if (resourceBaseAnnotation == null) {
			testFolders = new File[] { new File("test") };
		} else {
			String[] anotations = resourceBaseAnnotation.value();
			testFolders = new File[anotations.length];
			for (int i = 0; i < anotations.length; i++) {
				testFolders[i] = new File(anotations[i]);
			} 
		}
		
		descriptionBuilder = new MochaResultsDescriptionBuilder();
		mochaProcess = new MochaProcess();
		savedResults = new ArrayList<String>();
	}

	@Override
	public Description getDescription() {
		notifierReplayer = new RunNotifierReplayer();
		Description topSuite = Description.createSuiteDescription("All tests inside");
		for (int i = 0; i < testFolders.length; i++) {
			File folder = testFolders[i];
			Description folderDescription = Description.createSuiteDescription(folder.getAbsolutePath());
			topSuite.addChild(folderDescription);
			File[] testFiles = folder.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".js");
				}
			});
			
			for (int j = 0; j < testFiles.length; j++) {
				File test = testFiles[j];
				String mochaResults;
				try {
					mochaResults = mochaProcess.run(test);
					savedResults.add(mochaResults);
					String suiteName = test.getName();
					Description testDescription = descriptionBuilder.buildDescription(suiteName, mochaResults, notifierReplayer);
					folderDescription.addChild(testDescription);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return topSuite;
	}

	@Override
	public void run(RunNotifier notifier) {
		notifierReplayer.replay(notifier);
		notifierReplayer = null;
	}
	
}
