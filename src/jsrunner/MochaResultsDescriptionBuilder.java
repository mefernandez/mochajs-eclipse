package jsrunner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

public class MochaResultsDescriptionBuilder {

	public Description buildDescription(String suiteName, String mochaResults, RunNotifier notifier) {
		Description suiteDescription = Description.createSuiteDescription(suiteName);
		
		JSONObject results = (JSONObject) JSONValue.parse(mochaResults);
		if (results != null) {
			JSONArray tests = (JSONArray) results.get("tests");
			if (tests != null) {
				for (int i = 0; i < tests.size(); i++) {
					String title = (String) ((JSONObject)tests.get(i)).get("fullTitle");
					Description testDescription = Description.createTestDescription(MochaResultsDescriptionBuilder.class, title);
					suiteDescription.addChild(testDescription);
					
					notifier.fireTestStarted(testDescription);
					// FIXME The structure of the JSON makes it difficult to find build JUnit Description's
					if (failed(results, title)) {
						// FIXME Currently, Mocha's json reporter does NOT include error message. Might have to write my own reporter.
						String errorMsg = "A test failed apparently";
						Failure failure = new Failure(testDescription, new Exception(errorMsg));
						notifier.fireTestFailure(failure);					
					}
					notifier.fireTestFinished(testDescription);
				}
				
			}
		}
		return suiteDescription;		
	}

	boolean failed(JSONObject results, String title) {
		// FIXME Instead of going through the failed tests Array every time, results can be cached and managed by a collaborator object to check for failed/passed in O(1)
		JSONArray failures = (JSONArray) results.get("failures");
		if (failures != null) {
			for (int i = 0; i < failures.size(); i++) {
				String failedTitle = (String) ((JSONObject)failures.get(i)).get("fullTitle");
				if (title.equalsIgnoreCase(failedTitle))
					return true;
			}
		}
		return false;
	}

}
