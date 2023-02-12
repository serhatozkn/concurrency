package tr.com.serhat.concurrency.forkjoin;

import java.util.concurrent.ForkJoinPool;

public class ForkJoinExample {
	
	public static void main(String[] args) {
		
		ForkJoinPool pool = ForkJoinPool.commonPool();
		CustomRecursiveAction action1 = new CustomRecursiveAction("Serhat Kübra Özkan");
		
		// execute method requires join for wait
		System.out.println("execute");
		pool.execute(action1);
		action1.join();
		
		// invoke method automatically joins
		System.out.println("invoke");
		CustomRecursiveAction action2 = new CustomRecursiveAction("Kübra Serhat Özkan");
		pool.invoke(action2);
		
		
	}

}
