package model;
import java.util.ArrayList;
import java.util.List;

import model.Strategy.SelectionPolicy;


public class Scheduler {
	
	private List<Server> servers;
	private int maxNoServers;
	private Strategy strategy;
	public static double totalTime = 0.0f;
	
	public Scheduler(int maxNoServers) {
		this.maxNoServers = maxNoServers;
		servers = new ArrayList<Server>();
		
		for( int i = 0; i < maxNoServers; i++) {
			Server newServer = new Server(i+1);   // id
			servers.add(newServer);
			
			Thread t = new Thread(newServer);
			t.setName( "Queue " + i +":");
			t.start();
		}
	}
	
	public void changeStrategy(SelectionPolicy policy) {
		//apply strategy patter to instantiate the strategy with the concrete
		//strategy corresponding to policy
		if( policy == SelectionPolicy.SHORTEST_QUEUE ) {
			strategy = new ConcreteStrategyQueue();
		}
		if( policy == SelectionPolicy.SHORTEST_TIME ) {
			strategy = new ConcreteStrategyTime();
		}
		
	}
	
	public void dispatchTask(Task t) throws Exception{
		// call the stategy addTask method
		strategy.addTask(servers, t);

	}
	
	public List<Server> getServers(){
		return servers;
	}
	
	public int maxWaiting() {
		
		int max = -1;
		for (Server server : servers) {
			if( server.getWaitingPeriod().intValue() > max)
				max = server.getWaitingPeriod().intValue();		
		}
		
		return max;
	}


}
