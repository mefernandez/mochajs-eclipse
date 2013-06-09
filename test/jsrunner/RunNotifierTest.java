package jsrunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

public class RunNotifierTest {

	@Test
	public void replayFireTestStarted() {
		
		RunNotifierReplayer replayer = new RunNotifierReplayer();
		String title = "A dummy test";
		Description description = Description.createTestDescription(this.getClass(), title);
		replayer.fireTestStarted(description);
		RunNotifier notifier = mock(RunNotifier.class); 
		replayer.replay(notifier);
		verify(notifier).fireTestStarted(description);
	}
	
	@Test
	public void replayFireTestFinished() {
		
		RunNotifierReplayer replayer = new RunNotifierReplayer();
		String title = "A dummy test";
		Description description = Description.createTestDescription(this.getClass(), title);
		replayer.fireTestFinished(description);
		RunNotifier notifier = mock(RunNotifier.class); 
		replayer.replay(notifier);
		verify(notifier).fireTestFinished(description);
	}

	
	@Test
	public void replayFireTestFailure() {
		
		RunNotifierReplayer replayer = new RunNotifierReplayer();
		String title = "A dummy test";
		Description description = Description.createTestDescription(this.getClass(), title);
		Failure failure = new Failure(description, new Exception("A test failed"));
		replayer.fireTestFailure(failure);
		RunNotifier notifier = mock(RunNotifier.class); 
		replayer.replay(notifier);
		verify(notifier).fireTestFailure(failure);
	}

}
