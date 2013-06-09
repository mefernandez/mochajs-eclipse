package jsrunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

public class MochaFullTestRunnerTest {
	
	String mochaResults = "{\n" + 
			"  \"stats\": {\n" + 
			"    \"suites\": 1,\n" + 
			"    \"tests\": 2,\n" + 
			"    \"passes\": 1,\n" + 
			"    \"pending\": 0,\n" + 
			"    \"failures\": 1,\n" + 
			"    \"start\": \"2013-05-25T16:22:42.807Z\",\n" + 
			"    \"end\": \"2013-05-25T16:22:42.808Z\",\n" + 
			"    \"duration\": 1\n" + 
			"  },\n" + 
			"  \"tests\": [\n" + 
			"    {\n" + 
			"      \"title\": \"with a test that fails\",\n" + 
			"      \"fullTitle\": \"A Mocha Test with a test that fails\",\n" + 
			"      \"duration\": 0\n" + 
			"    },\n" + 
			"    {\n" + 
			"      \"title\": \"with a test that passes\",\n" + 
			"      \"fullTitle\": \"A Mocha Test with a test that passes\",\n" + 
			"      \"duration\": 0\n" + 
			"    }\n" + 
			"  ],\n" + 
			"  \"failures\": [\n" + 
			"    {\n" + 
			"      \"title\": \"with a test that fails\",\n" + 
			"      \"fullTitle\": \"A Mocha Test with a test that fails\",\n" + 
			"      \"duration\": 0\n" + 
			"    }\n" + 
			"  ],\n" + 
			"  \"passes\": [\n" + 
			"    {\n" + 
			"      \"title\": \"with a test that passes\",\n" + 
			"      \"fullTitle\": \"A Mocha Test with a test that passes\",\n" + 
			"      \"duration\": 0\n" + 
			"    }\n" + 
			"  ]\n" + 
			"}";

	@Test
	public void constructorSetsUpTestFolderFromAnnotationInJavaClass() {
		Class testClass = MochaFullTestSuite.class;
		MochaFullTestRunner runner = new MochaFullTestRunner(testClass);
		File expected = new File("test");
		assertEquals(expected.getAbsolutePath(), runner.testFolders[0].getAbsolutePath());
	}

	@Test
	public void buildSuiteDescription() {
		MochaResultsDescriptionBuilder builder = new MochaResultsDescriptionBuilder();
		String suiteName = "mochatest.js";
		RunNotifierReplayer notifier = new RunNotifierReplayer();
		Description suiteDescription = builder.buildDescription(suiteName, mochaResults, notifier);
		assertTrue(suiteDescription.isSuite());
		assertEquals(suiteName, suiteDescription.getDisplayName());
		ArrayList<Description> children = suiteDescription.getChildren();
		assertEquals(2, children.size());
		assertEquals("A Mocha Test with a test that fails", children.get(0).getMethodName());
		assertEquals("A Mocha Test with a test that passes", children.get(1).getMethodName());
	}

	@Test
	public void notifyPassedAndFailedTests() {
		MochaResultsDescriptionBuilder builder = new MochaResultsDescriptionBuilder();
		String suiteName = "mochatest.js";
		RunNotifier notifier = mock(RunNotifier.class);
		Description suiteDescription = builder.buildDescription(suiteName, mochaResults, notifier);
		verify(notifier, times(2)).fireTestStarted(any(Description.class));
		verify(notifier, times(2)).fireTestFinished(any(Description.class));
		verify(notifier).fireTestFailure(any(Failure.class));		
	}

}
