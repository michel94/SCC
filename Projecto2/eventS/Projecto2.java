package eventS;

import eventS.*;

public class Projecto2{
	public Projecto2(){
		Model model = new Server(1);
		Simulator simulator = new Simulator(model);
		model.simulator(simulator);
		simulator.run();
	}
	public static void main(String args[]){
		new Projecto2();
	}
}
