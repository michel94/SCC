/*
Author: Fernando J. Barros
University of Coimbra
Department of Informatics Enginnering
3030 Coimbra, Portugal
Date: 20/2/2015
 */
package eventS;

import java.util.*;

class Token {
	private double arrivalTick;
	private double serviceTick;
	private double endTick;
	public int id;
	public Token(double arrivalTick) {
		Random r = new Random();
		id = Math.abs(r.nextInt() % 1000);
		this.arrivalTick = this.serviceTick = arrivalTick;
	}
	public double waitTime() {return serviceTick - arrivalTick;}
	public double cycleTime(double time) {return time - arrivalTick;}
	public double cycleTime() {return endTick - arrivalTick;}
	public void arrivalTick(double arrivalTick) {this.arrivalTick = arrivalTick;}
	public double arrivalTick() {return arrivalTick;}
	public double serviceTick() {return serviceTick;}
	public void serviceTick(double serviceTick) {this.serviceTick = serviceTick;}
	public void endTick(double endTick) {this.endTick = endTick;}
	@Override
	public String toString() {return String.format("[%.2f]", arrivalTick);}
}

final class DrinksDeparture extends Event{
	private Token client;
	private Server model;
	public DrinksDeparture(Server model, Token client){
		super();
		this.model = model;
		this.client = client;
	}
	@Override
	public void execute() {
		System.out.println("DrinksDeparture at " + time + " client: " + client.id);
		client.serviceTick(time);

	}
}

final class HotFoodDeparture extends Event{
	private Token client;
	private Server model;
	public HotFoodDeparture(Server model, Token client){
		super();
		this.model = model;
		this.client = client;
	}
	@Override
	public void execute() {
		
		model.schedule(new DrinksDeparture(model, client), model.drinksDist.next());
		client.serviceTick(time);
		System.out.println("HotFoodDeparture at " + time + " client: " + client.id + " queue size " + model.hotFoodQueue.value() );

		if (model.hotFoodQueue.value() > 0) {
			model.hotFoodQueue.inc(-1, time);
			client = model.hotFood.remove(0);
			model.schedule(this, model.hotFoodDist.next());
		}
		else {
			model.restHotFood.inc(-1, time);
		}
		
	}
}

final class SandwichesDeparture extends Event{
	private Token client;
	private Server model;
	public SandwichesDeparture(Server model, Token client){
		super();
		this.model = model;
		this.client = client;
	}
	@Override
	public void execute() {

		model.schedule(new DrinksDeparture(model, client), model.drinksDist.next());
		client.serviceTick(time);
		System.out.println("Sandwich Departure " + time + " client: " + client.id + " queue size " + model.hotFoodQueue.value() );
		
		if (model.sandwichesQueue.value() > 0) {
			model.sandwichesQueue.inc(-1, time);
			client = model.sandwiches.remove(0);
			model.schedule(this, model.sandwichesDist.next());
		}
		else {
			model.restSandwiches.inc(-1, time);
		}
		
	}
}

final class Arrival extends Event {
	private final Server model;
	public Arrival(Server model) {
		super();
		this.model = model;
	}
	@Override
	public void execute() {
		Token client = new Token(time);
		
		double q = model.choiceDist.next();
		
		if(q == 0.0){
			System.out.println("Arrival at " + q);
			if(model.restHotFood.value() > 0){ // hotfood queue empty
				model.restHotFood.inc(-1, time);
				model.schedule(new HotFoodDeparture(model, client), model.hotFoodDist.next());
			}else{
				model.hotFoodQueue.inc(1, time);
				model.hotFood.add(client);
			}
		}
		else if(q == 1.0){
			System.out.println("Arrival at " + q);
			if(model.restSandwiches.value() > 0){
				model.restSandwiches.inc(-1, time);
				model.schedule(new SandwichesDeparture(model, client), model.sandwichesDist.next());
			}else{
				model.sandwichesQueue.inc(1, time);
				model.sandwiches.add(client);
			}

		}else if(q == 2.0){
			System.out.println("Arrival at " + q);
			model.schedule(new DrinksDeparture(model, client), model.drinksDist.next());
			
		}else{
			System.out.println("WTF");
		}
		

		double t = model.arrivalDist.next();
		//System.out.println("new arrival in " + t);
		model.schedule(this, t);
		

	}
}

final class Stop extends Event {
	private final Server model;
	public Stop(Server model) {
		super();
		this.model = model;
	}
	@Override
	public void execute() {
		System.out.println("End");
		model.clear();
	}
}

final class Server extends Model {
	final Accumulate hotFoodQueue, sandwichesQueue, queue;
	final Accumulate restSandwiches, restHotFood, rest;
	public final List<Token> sandwiches, hotFood;
	public final List<Token>[] cashiers;
	final Uniform hotFoodDist, sandwichesDist, drinksDist;
	final Discrete choiceDist;
	final Exponential arrivalDist;
	public Server(int n) {
		super();

		hotFoodQueue = new Accumulate(0);
		sandwichesQueue = new Accumulate(0);
		this.queue = new Accumulate(0);

		this.rest = new Accumulate(n);
		restSandwiches = new Accumulate(1);
		restHotFood = new Accumulate(1);

		sandwiches = new ArrayList<>();
		hotFood = new ArrayList<>();
		cashiers = new ArrayList[3]; //TODO: fix this warning
		
		arrivalDist = new Exponential((int)new Date().getTime(), 30.0);
		hotFoodDist = new Uniform((int)new Date().getTime(), 50.0, 120.0);
		sandwichesDist = new Uniform((int)new Date().getTime(), 60.0, 180.0);
		drinksDist = new Uniform((int)new Date().getTime(), 5.0, 20.0);
		double[] a = {0, 1, 2};
		double[] p = {0.8, 0.15, 0.05};
		choiceDist = new Discrete((int)new Date().getTime(), a, p);
	}
	@Override
	protected void init() {
		schedule(new Arrival(this), arrivalDist.next());
		schedule(new Stop(this), 1000);
	}
	@Override
	public String toString() {return "" + queue.value() + " " + rest.value();}
}



