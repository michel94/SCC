package eventS;

import eventS.*;

public class Projecto2{
	static int sum = 0;
	static double meanSum = 0;

	public Projecto2(String scenario){
		Model model;
		switch(scenario){
				case "ai":	model = new Server(1, 1, 1);
							break;
				case "aii":	model = new Server(2, 1, 0);
							break;
				case "aiii":model = new Server(1, 2, 0);
							break;
				case "bi":	model = new Server(2, 2, 0);
							break;
				case "bii":	model = new Server(2, 1, 1);
							break;
				case "biii":model = new Server(1, 2, 1);
							break;
				case "c":	model = new Server(2, 2, 1);
							break;
				default:	model = new Server(1, 1, 0);
							break;
		}
		Simulator simulator = new Simulator(model);
		model.simulator(simulator);
		simulator.run();
		sum += ((Server)model).overallMeanTime.count();
		meanSum += ((Server)model).overallMeanTime.mean();
	}
	public static void main(String args[]){
		if(args.length == 0)
			new Projecto2("default");
		else{
			int n=Integer.parseInt(args[0]);
			for(int i = 0; i < n; i++){
				if(args.length == 2)
					new Projecto2(args[1]);
				else
					new Projecto2("default");
			}
			System.out.println("Sum: " + sum*1.0/n);
			System.out.println("MeanSum: " + meanSum*1.0/n);
		}
	}
}
