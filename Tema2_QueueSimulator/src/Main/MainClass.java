package Main;

import controller.SimulationManager;

public class MainClass {
		public static void main(String[] args) {
		
			SimulationManager gen;
			try {
				gen = new SimulationManager(args);
				Thread t = new Thread(gen);
				t.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
