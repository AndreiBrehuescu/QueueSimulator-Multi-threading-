package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import model.Scheduler;
import model.Strategy.SelectionPolicy;
import model.Task;
import model.Server;

public class SimulationManager implements Runnable{
	
	public int timeLimit ; //maximum processing time
	public int maxProcessingTime ;
	public int minProcessingTime ;
	public int numberOfServers ;
	public int numberOfClients ;
	public int minArrivalTime;
	public int maxArrivalTime;
	private static File outputFile;
	
	public static boolean threadRunning = true;
	
	public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;
	
	//entity responsible with queue management and client distribution
	private Scheduler scheduler;
	
	//pool of tasks(clients shopping in the store)
	private List<Task> generatedTasks = new ArrayList();
	
	
	public SimulationManager(String[] arg) throws Exception {
		this.readFromFile(arg[0]);
		outputFile = new File(arg[1]);
		if( outputFile.exists() )
		{
			outputFile.delete();
			outputFile = new File(arg[1]);
		}
		
		
		this.scheduler = new Scheduler(this.numberOfServers);
		this.scheduler.changeStrategy( this.selectionPolicy);
		
		
		generateNRandomTasks();
	}
	
	@SuppressWarnings("unchecked")
	private void generateNRandomTasks() throws Exception {
		
		int randTaskProcessingTime;
		int randArrivalTime;

		for( int i = 0; i < this.numberOfClients; i++) {
			
			if( this.maxProcessingTime < this.minProcessingTime )
					throw new Exception("Invalid Processing time Max < Min");
			// minProcTime <  randTaskPT  < maxProcTime 
			randTaskProcessingTime = new Random().nextInt(this.maxProcessingTime - this.minProcessingTime + 1) + this.minProcessingTime;
			
			
			if( this.maxArrivalTime < this.minArrivalTime )
				throw new Exception("Invalid Arrival time Max < Min ");
			randArrivalTime = new Random().nextInt( this.maxArrivalTime - this.minArrivalTime + 1) + this.minArrivalTime;
			
			generatedTasks.add(new Task( i+1, randArrivalTime, 0, randTaskProcessingTime));
		} 
		Collections.sort(generatedTasks); //sort in the ascending order of arrival TImee
		
	}
	
	@Override
	public void run() {
		
		int currentTime = 0;
		int tasksDone = 0;
		
		
		while( currentTime < timeLimit ) {
			
			writeToFile(SimulationManager.outputFile,currentTime, scheduler.getServers(), 0);  //currentTime
			
			Iterator<Task> i = this.generatedTasks.iterator();
			while( i.hasNext() ) {
				
				try {
					Task t = i.next();
					
					if( t.getArrivalTime() == currentTime ) {
						this.scheduler.dispatchTask(t);
						tasksDone++;
						i.remove();
					}
					
					if( tasksDone == this.numberOfClients ) {
						//this.timeLimit = currentTime + scheduler.maxWaiting();
						this.timeLimit = Math.min(currentTime + scheduler.maxWaiting(), this.timeLimit);
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			writeToFile(SimulationManager.outputFile,currentTime, scheduler.getServers(), 1); //waiting clients
			writeToFile(SimulationManager.outputFile,currentTime, scheduler.getServers(),2); // queues and their clients
		
			try {// wait an interval of 1 second	
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("Thread interrupted");
			}
			
			currentTime++;
					
		}
		writeToFile(SimulationManager.outputFile,this.timeLimit, scheduler.getServers(),3); // all data again to show the queues empty + avarage waiting time
		SimulationManager.threadRunning = false; //kill all threads if exceed the timeLimit
	}
	
	
	public void readFromFile(String filePath) {
		
		String s;
		String[] line;
		int[] fileInt = new int[7];
		
		int i = 0;
		File f = new File(filePath);
		
		try {
			Scanner scan = new Scanner(f);
		
			while( scan.hasNext() ) {
				s = scan.nextLine();
				line = s.split(",");
				for (String word : line) {
					fileInt[i++] = Integer.parseInt(word);
				}
			}
			
			this.numberOfClients = fileInt[0];
			this.numberOfServers = fileInt[1];
			this.timeLimit = fileInt[2];
			this.minArrivalTime = fileInt[3];
			this.maxArrivalTime = fileInt[4];
			this.minProcessingTime = fileInt[5];
			this.maxProcessingTime = fileInt[6];
	
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		//System.out.println(fileInt[0] + " " + fileInt[1]  + " " + fileInt[2]  + " " + fileInt[3]  + " " + fileInt[4]  + " " + fileInt[5]  + " " + fileInt[6] );
		
	}
	
	public void writeToFile(File f, int currTime,List<Server> serverList,int write) {

		try {
			FileWriter buffer = new FileWriter(f, true);
			if( write == 0 || write == 3)
				buffer.write("Time:" + currTime +"\n");
			
			if( write == 1 || write == 3 ){
				buffer.write("Waiting clients: ");
				for (Task task : this.generatedTasks) {
					buffer.write(task.toString());
				}
				buffer.write("\n");
			}
			
			if(write == 2 || write == 3) {
				for (Server server : serverList) {
					if( server.getSizeTask() == 0 ) {
						buffer.write(server.toString() + " closed\n");
					}else {
						buffer.write(server.toString());
						buffer.write("\n");
					}
				}
				buffer.write("\n");
			}
			
			if( write == 3) {
				buffer.write("Average waiting time: " + Scheduler.totalTime / this.numberOfClients);
				
			}
			buffer.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}

}
