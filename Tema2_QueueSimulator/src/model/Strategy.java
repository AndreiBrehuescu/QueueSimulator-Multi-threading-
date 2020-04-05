package model;
import java.util.List;

public interface Strategy {
	public enum SelectionPolicy {

		SHORTEST_TIME, SHORTEST_QUEUE

	}
	public void addTask(List<Server> servers, Task t) throws Exception;
}
