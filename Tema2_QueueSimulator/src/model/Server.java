package model;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import controller.SimulationManager;

import static java.lang.Thread.sleep;
public class Server implements Runnable{
	private int id;
	private LinkedBlockingQueue<Task> tasks;
	private AtomicInteger waitingPeriod;
	
	public Server() {
	 //initializa queue and waitingPeriod
	}
	
	public Server(int id) {
		this.id = id;
		this.waitingPeriod = new AtomicInteger(0);
		this.tasks = new LinkedBlockingQueue<Task>();
	}
	
	public void addTask(Task newTask) {
		//add task to queue  + set task details (finish time) + change waitingPeriod
		//increment the waitingPEriod
		boolean emp = false;
		if( tasks.isEmpty() ) {
			emp = true;
		}
		try {
			tasks.put(newTask);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newTask.setFinishTime( newTask.getArrivalTime() + newTask.getProcessingTime() + this.waitingPeriod.intValue() );
		this.waitingPeriod.set(this.waitingPeriod.intValue() +  newTask.getProcessingTime());
		if( emp == true ) {
			Thread t = new Thread(this);
			t.start();
		}
	}
	
	@Override
	public void run() {

		Task t;
		
		while(tasks.isEmpty() == false) {  //threadul moare cand ramane fara task-uri
		
			t = this.tasks.peek(); //get the head of the queue without deleting it or null if empty
			if( t != null ) {
				try {
	
					while(t.getProcessingTime() != 0 ) {
						sleep(1000);
						t.setProcessingTime(t.getProcessingTime()-1);
						this.waitingPeriod.set(this.waitingPeriod.get() - 1);
					}
					
				
				 
				this.waitingPeriod.set( this.waitingPeriod.intValue() - t.getProcessingTime());
				this.tasks.poll();// remove head from queue
				}catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			}
		
			if( !SimulationManager.threadRunning ) //kill thread
				break;
		}
		
	}

	public ArrayList<Task> getTasks() { //conversie la ArrayList
		ArrayList<Task> newTasks = new ArrayList<Task>(this.tasks);
		return newTasks;
	}
	public int getSizeTask() {
		return this.tasks.size();
	}

	public AtomicInteger getWaitingPeriod() {
		return waitingPeriod;
	}

	@Override
	public String toString() {
		String s="";
		s += "Queue "+ this.id + ":";
		for (Task task : tasks) {
			s += task.toString();
		}
		
		return s;
	}

}
