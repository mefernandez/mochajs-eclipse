package jsrunner;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import jsrunner.MochaTestRunner.TestsFolder;

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
public class MochaFullTestRunner extends Runner {
	
	File testFolders[];
	MochaResultsDescriptionBuilder descriptionBuilder;
	MochaProcess mochaProcess;
	MochaJUnitNotifierAdapter notifierAdapter;
	List<String> savedResults;
	RunNotifierReplayer notifierReplayer;

	public MochaFullTestRunner(Class testClass) {
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
		notifierAdapter = new MochaJUnitNotifierAdapter();
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
