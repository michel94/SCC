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
	public Token(double arrivalTick) {this.arrivalTick = this.serviceTick = arrivalTick;}
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
		Token client;

		if (model.hotFoodQueue.value() > 0) {
			model.hotFoodQueue.inc(-1, time);
			client = model.hotFood.remove(0);
			client.serviceTick(time);
			model.delayTime.add(client.waitTime());
			model.schedule(this, model.hotFoodDist.next());
			System.out.println("HotFood Departure " + time + " queue size " + model.hotFoodQueue.value() );
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
		System.out.println("Sandwich Departure");
		Token client;

		if (model.sandwichesQueue.value() > 0) {
			model.sandwichesQueue.inc(-1, time);
			client = model.sandwiches.remove(0);
			client.serviceTick(time);
			model.delayTime.add(client.waitTime());
			model.schedule(this, model.sandwichesDist.next());
			System.out.println("Sandwich Departure " + time + " queue size " + model.hotFoodQueue.value() );
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
		/*if (model.rest.value() > 0) {
			model.rest.inc(-1, time);
			model.schedule(new Departure(model, client), model.service.next());
		}
		else {
			model.queue.inc(1, time);
			model.line.add(client);
		}*/
		
		double q = model.choiceDist.next();
		System.out.println("Arrival at " + q);
		
		if(q == 0){
			if(model.restHotFood.value() > 0){
				model.restHotFood.inc(-1, time);
				model.schedule(new HotFoodDeparture(model, client), model.hotFoodDist.next());
			}else{
				model.hotFoodQueue.inc(1, time);
				model.hotFood.add(client);
			}
		}
		else if(q == 1){
			if(model.restSandwiches.value() > 0){
				model.restSandwiches.inc(-1, time);
				model.schedule(new SandwichesDeparture(model, client), model.sandwichesDist.next());
			}else{
				model.sandwichesQueue.inc(1, time);
				model.sandwiches.add(client);
			}

		}
		/*else
			drinks here
			
		*/
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
		System.out.format("%.2f\t%.2f\t%.2f\n",
			model.queue.mean(time),
			model.rest.mean(time), model.delayTime.mean()
		);
		model.clear();
	}
}

final class Server extends Model {
	final Accumulate hotFoodQueue, sandwichesQueue, queue;
	final Accumulate restSandwiches, restHotFood, rest;
	final RandomStream service;
	public final List<Token> sandwiches, hotFood;
	public final List<Token>[] cashiers;
	final Average delayTime;
	final Uniform hotFoodDist, sandwichesDist;
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
		cashiers = new ArrayList[3];

		this.delayTime = new Average();
		
		arrivalDist = new Exponential((int)new Date().getTime(), 30.0);
		service = new Uniform((int)new Date().getTime(), 20.0, 40.0);
		hotFoodDist = new Uniform((int)new Date().getTime(), 50.0, 120.0);
		sandwichesDist = new Uniform((int)new Date().getTime(), 60.0, 180.0);
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




