package jsrunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Test;
import org.junit.runner.notification.RunNotifier;

public class MochaJSONTestsResultsTest {
	
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
	public void parseJSON() {
		
		JSONObject results = (JSONObject) JSONValue.parse(mochaResults);
		assertNotNull(results);

		JSONArray passes = (JSONArray) results.get("passes");
		assertEquals("with a test that passes", ((JSONObject)passes.get(0)).get("title"));

		JSONArray failures = (JSONArray) results.get("failures");
		assertEquals("with a test that fails", ((JSONObject)failures.get(0)).get("title"));
				
	}
	
	@Test
	public void defaultTestFolderExists() throws Exception {
		MochaTestRunner runner = new MochaTestRunner(MochaTestSuite.class);
		File[] testFolders = runner.testFolders;
		assertNotNull(testFolders);
		assertEquals(1, testFolders.length);
		File expectedTestFolder = new File("test");
		assertEquals(expectedTestFolder, testFolders[0]);
		assertTrue(testFolders[0].exists());
		assertTrue(testFolders[0].isDirectory());
	}

	@Test
	public void runTest() throws Exception {
		MochaTestRunner runner = new MochaTestRunner(MochaTestSuite.class);
		File test = new File("test/mochatest.js");
		RunNotifier notifier = mock(RunNotifier.class);
		runner.runChild(test, notifier);
		
	}

}
