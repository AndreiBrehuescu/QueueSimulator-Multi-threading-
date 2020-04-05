package model;

public class Task implements Comparable{
	private int id;
	private int arrivalTime;
	private int finishTime;
	private int processingTime;
	
	public Task() {
		
	}

	public Task(int id, int arrivalTime, int finishTime, int processingTime) {
		super();
		this.id = id;
		this.arrivalTime = arrivalTime;
		this.finishTime = finishTime;
		this.processingTime = processingTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(int finishTime) {
		this.finishTime = finishTime;
	}

	public int getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(int processingTime) {
		this.processingTime = processingTime;
	}

	@Override
	public String toString() {
		String s ="("+this.id +","+this.arrivalTime+","+this.processingTime+");";
		return s;
	}

	@Override
	public int compareTo(Object o) {
		Task q = (Task)o;
		if (this.arrivalTime > q.getArrivalTime() )
			return 1;
		else if ( this.arrivalTime < q.getArrivalTime() )
			return -1;
		else
			return 0;
	}
	
}
