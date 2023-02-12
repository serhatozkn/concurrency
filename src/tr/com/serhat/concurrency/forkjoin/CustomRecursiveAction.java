package tr.com.serhat.concurrency.forkjoin;

import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

@SuppressWarnings("serial")
public class CustomRecursiveAction extends RecursiveAction {

	private String workload = "";
    private static final int THRESHOLD = 4;
	
    CustomRecursiveAction(final String workload) {
    	this.workload = workload;
    }
   
	@Override
	protected void compute() {
		if (workload.length() > 4) {
			ForkJoinTask.invokeAll(createSubTasks());
		} else {
			processing(workload);
		}
	}
	
	private List<CustomRecursiveAction> createSubTasks() {
		String part1 = workload.substring(0, workload.length() / 2);
		String part2 = workload.substring(workload.length() / 2, workload.length());
		return List.of(new CustomRecursiveAction(part1), new CustomRecursiveAction(part2));
	}
	
	private void processing(String work) {
        String result = work.toUpperCase();
        System.out.println("This result - (" + result + ") - was processed by " 
          + Thread.currentThread().getName());
    }

}
