package jsrunner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

/**
 * This class takes mocha's test results as JSON string and
 * notifies whatever happened to JUnit's @link{RunNotifier}.
 * @author mefernandez
 */
public class MochaJUnitNotifierAdapter {

	public void notify(String mochaResults, RunNotifier notifier) {
		
		JSONObject results = (JSONObject) JSONValue.parse(mochaResults);
		if (results != null) {
			JSONArray passes = (JSONArray) results.get("passes");
			if (passes != null) {
				for (int i = 0; i < passes.size(); i++) {
					String title = (String) ((JSONObject)passes.get(i)).get("fullTitle");
					Description description = Description.createTestDescription(this.getClass(), title);
					notifier.fireTestStarted(description);
					notifier.fireTestFinished(description);
				}
				
			}
			JSONArray failures = (JSONArray) results.get("failures");
			if (failures != null) {
				for (int i = 0; i < failures.size(); i++) {
					String title = (String) ((JSONObject)failures.get(i)).get("fullTitle");
					Description description = Description.createTestDescription(this.getClass(), title);
					notifier.fireTestStarted(description);
					Failure failure = new Failure(description, new Exception("A test failed apparently"));
					notifier.fireTestFailure(failure);
					notifier.fireTestFinished(description);
				}
				
			}
				
			
		}


	}

}
