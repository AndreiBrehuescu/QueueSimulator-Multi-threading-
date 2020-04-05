package model;
import java.util.List;

public class ConcreteStrategyTime implements Strategy {

	@Override
	public void addTask(List<Server> servers, Task t) throws Exception {
		int min = Integer.MAX_VALUE;
		Server minTime = null;
		for (Server server : servers) {
			if( server.getWaitingPeriod().get() < min ) {
				minTime = server;
				min = server.getWaitingPeriod().get();
			}
		}
		Scheduler.totalTime += min + t.getProcessingTime() ;
		
		if( min != Integer.MAX_VALUE) {
			minTime.addTask(t);
		}
	}

}
