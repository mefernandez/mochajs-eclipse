package jsrunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;

public class RunNotifierReplayer extends RunNotifier {
	
	interface ReplayCommand {
		public void execute(RunNotifier notifier);
	}
	
	class FireTestStarted implements ReplayCommand {
		
		Description description;

		@Override
		public void execute(RunNotifier notifier) {
			notifier.fireTestStarted(description);
		}
		
	}
	
	class FireTestFinished implements ReplayCommand {
		
		Description description;

		@Override
		public void execute(RunNotifier notifier) {
			notifier.fireTestFinished(description);
		}
		
	}
	
	class FireTestFailure implements ReplayCommand {
		
		Failure failure;

		@Override
		public void execute(RunNotifier notifier) {
			notifier.fireTestFailure(failure);
		}
		
	}
	List<ReplayCommand> commands = new ArrayList<RunNotifierReplayer.ReplayCommand>();

	@Override
	public void fireTestFailure(Failure failure) {
		FireTestFailure cmd = new FireTestFailure();
		cmd.failure = failure;
		commands.add(cmd);
	}

	@Override
	public void fireTestFinished(Description description) {
		FireTestFinished cmd = new FireTestFinished();
		cmd.description = description;
		commands.add(cmd);
	}

	@Override
	public void fireTestStarted(Description description)
			throws StoppedByUserException {
		FireTestStarted cmd = new FireTestStarted();
		cmd.description = description;
		commands.add(cmd);
	}

	/**
	 * Replay all methods called on this object up until now, but applied on the notifier passed by parameter.
	 * @param notifier, the object to replay all methods called up until now.
	 */
	public void replay(RunNotifier notifier) {
		Iterator<ReplayCommand> it = commands.iterator();
		while (it.hasNext()) {
			ReplayCommand replayCommand = (ReplayCommand) it.next();
			replayCommand.execute(notifier);
		}
	}

}
