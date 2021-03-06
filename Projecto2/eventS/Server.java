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
	private double foodTick;
	private double drinksTick;
	private double endTick;
	private double cashierTime;
	private double type;
	public int id;
	public Token(double arrivalTick) {
		Random r = new Random();
		id = Math.abs(r.nextInt() % 1000);
		this.arrivalTick = this.foodTick = arrivalTick;
	}
	public void addCashierTime(double t){
		cashierTime += t;
	}
	public double cashierTime(){
		return cashierTime;
	}
	public double waitFoodTime() {return foodTick - arrivalTick;}
	public double waitCashierTime() {return endTick - drinksTick;}
	public double cycleTime(double time) {return time - arrivalTick;}
	public double cycleTime() {return endTick - arrivalTick;}
	public void arrivalTick(double arrivalTick) {this.arrivalTick = arrivalTick;}
	public double arrivalTick() {return arrivalTick;}
	public double foodTick() {return foodTick;}
	public void foodTick(double foodTick) {this.foodTick = foodTick;}
	public double drinksTick() {return drinksTick;}
	public void drinksTick(double drinksTick) {this.drinksTick = drinksTick;}
	public void endTick(double endTick) {this.endTick = endTick;}
	public double endTick() {return endTick;}
	public void type(double type) {this.type = type;}
	public double type() {return type;}
	@Override
	public String toString() {return String.format("[%.2f]", arrivalTick);}
}

final class CashiersDeparture extends Event{
	private Token client;
	private Server model;
	private int cashier;
	public CashiersDeparture(Server model, Token client, int cashier){
		super();
		this.model = model;
		this.client = client;
		this.cashier = cashier;
	}
	@Override
	public void execute(){
		//System.out.println("CashiersDeparture at " + time + " client: " + client.id);

		client.endTick(time);
		model.cashierMeanDelayTime.add(client.waitCashierTime());
		model.cashierMaxDelayTime.add(client.waitCashierTime());

		if(client.type() == 0.0) //hotfood
		{
			model.clientAMeanDelayTime.add(client.waitCashierTime());
			model.clientAMaxDelayTime.add(client.waitCashierTime());
		}
		else if(client.type() == 1.0)
		{
			model.clientBMeanDelayTime.add(client.waitCashierTime());
			model.clientBMaxDelayTime.add(client.waitCashierTime());
		}

		else if(client.type() == 2.0)
		{
			model.clientCMeanDelayTime.add(client.waitCashierTime());
			model.clientCMaxDelayTime.add(client.waitCashierTime());
		}

		model.overallMeanTime.add(client.cycleTime());

		if(model.cashiersQueue[cashier].value() > 0){
			model.cashiersQueue[cashier].inc(-1, time);
			client = model.cashiers[cashier].remove(0);
			model.schedule(this, client.cashierTime());
			//System.out.println("Being serviced by CASHIER " + cashier + " at time " + (time + client.cashierTime()) );
		}
		else{
			model.restCashiers[cashier].inc(1, time);
		}
	}

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
		//System.out.println("DrinksDeparture at " + time + " client: " + client.id);
		//System.out.println("Accumulated Time " + client.cashierTime());

		int bestCashier = 0;
		client.drinksTick(time);

		for(int i = 1; i < model.cashiers.length; i++){
			if(model.cashiersQueue[i].value() < model.cashiersQueue[bestCashier].value())
			bestCashier = i;
		}

		if(model.restCashiers[bestCashier].value() > 0){
			model.restCashiers[bestCashier].inc(-1, time);
			model.schedule(new CashiersDeparture(model, client, bestCashier), /*time +*/ client.cashierTime());
			//System.out.println("Being serviced by CASHIER " + bestCashier + " at time " + (time + client.cashierTime()) + " queue size " + model.cashiersQueue[bestCashier].value() );
		}
		else{
			model.cashiersQueue[bestCashier].inc(1, time);
			model.cashiers[bestCashier].add(client);
			//System.out.println("On CASHIER queue " + bestCashier + " with size " + model.cashiersQueue[bestCashier].value() + " at time " + (time + client.cashierTime()) );
		}
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

		client.foodTick(time);
		model.hotFoodMaxDelayTime.add(client.waitFoodTime());
		model.hotFoodMeanDelayTime.add(client.waitFoodTime());

		model.clientAMeanDelayTime.add(client.waitFoodTime());
		model.clientAMaxDelayTime.add(client.waitFoodTime());

		client.addCashierTime(model.drinksExtraDist.next());
		model.schedule(new DrinksDeparture(model, client), model.drinksDist.next());
		//System.out.println("HotFoodDeparture at " + time + " client: " + client.id + " queue size " + model.hotFoodQueue.value() );

		if (model.hotFoodQueue.value() > 0) {
			model.hotFoodQueue.inc(-1, time);
			client = model.hotFood.remove(0);
			client.addCashierTime(model.hotFoodExtraDist.next());
			model.schedule(this, model.hotFoodDist.next());
		}
		else {
			model.restHotFood.inc(1, time);
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

		client.foodTick(time);
		model.sandwichesMaxDelaytime.add(client.waitFoodTime());
		model.sandwichesMeanDelaytime.add(client.waitFoodTime());

		model.clientBMeanDelayTime.add(client.waitFoodTime());
		model.clientBMaxDelayTime.add(client.waitFoodTime());

		client.addCashierTime(model.drinksExtraDist.next());
		model.schedule(new DrinksDeparture(model, client), model.drinksDist.next());
		//System.out.println("Sandwich Departure " + time + " client: " + client.id + " queue size " + model.hotFoodQueue.value() );

		if (model.sandwichesQueue.value() > 0) {
			model.sandwichesQueue.inc(-1, time);
			client = model.sandwiches.remove(0);
			client.addCashierTime(model.sandwichesExtraDist.next());
			model.schedule(this, model.sandwichesDist.next());
		}
		else {
			model.restSandwiches.inc(1, time);
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

		double q, g = model.groupsDist.next();
		//System.out.println("Group with " + g + " elements");

		for(int i=0; i<g; i++){
			q = model.choiceDist.next();
			client.type(q);

			if(q == 0.0){
				//System.out.println("Arrival at " + q + " time " + time + " queue " + model.hotFoodQueue.value());
				if(model.restHotFood.value() > 0){ // hotfood not working
					model.restHotFood.inc(-1, time);
					client.addCashierTime(model.hotFoodExtraDist.next());
					model.schedule(new HotFoodDeparture(model, client), model.hotFoodDist.next());
				}else{
					model.hotFoodQueue.inc(1, time);
					model.hotFood.add(client);
				}
			}
			else if(q == 1.0){
				//System.out.println("Arrival at " + q + " time " + time + " queue " + model.sandwichesQueue.value());
				if(model.restSandwiches.value() > 0){
					model.restSandwiches.inc(-1, time);
					client.addCashierTime(model.hotFoodExtraDist.next());
					model.schedule(new SandwichesDeparture(model, client), model.sandwichesDist.next());
				}else{
					model.sandwichesQueue.inc(1, time);
					model.sandwiches.add(client);
				}
			}else if(q == 2.0){
				//System.out.println("Arrival at " + q + " time " + time);
				client.addCashierTime(model.drinksExtraDist.next());
				model.schedule(new DrinksDeparture(model, client), model.drinksDist.next());
			}

		}
		model.schedule(this, model.arrivalDist.next());

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
		System.out.println("STATISTICS");

		/* FIRST POINT STATISTICS */
		System.out.println("1 - Sandwiches mean delay time: " + model.sandwichesMeanDelaytime.mean());
		System.out.println("1 - HotFood mean delay time: " + model.hotFoodMeanDelayTime.mean());
		System.out.println("1 - Cashier mean delay time: " + model.cashierMeanDelayTime.mean());
		System.out.println("1 - Sandwiches max delay time: " + model.sandwichesMaxDelaytime.value());
		System.out.println("1 - HotFood max delay time: " + model.hotFoodMaxDelayTime.value());
		System.out.println("1 - Cashier max delay time: " + model.cashierMaxDelayTime.value());

		/* SECOND POINT STATISTICS */
		System.out.println("2 - time-average sandwichesQueue: " + model.sandwichesQueue.mean(time));
		System.out.println("2 - time-average hotFoodQueue: " + model.hotFoodQueue.mean(time));
		double timeAverageCashier = 0;
		for(int i=0;i<model.cashiersQueue.length;i++)
		{
			timeAverageCashier += model.cashiersQueue[i].mean(time);
		}
		timeAverageCashier = timeAverageCashier/model.cashiersQueue.length;
		System.out.println("2 - time-average cashiers: " + timeAverageCashier);
		System.out.println("2 - max number sandwichesQueue: " + model.sandwichesQueue.max());
		System.out.println("2 - max number hotFoodQueue: " + model.hotFoodQueue.max());
		double maxNumberCashier = 0;
		for(int i=0;i<model.cashiersQueue.length;i++)
		{
			if(maxNumberCashier < model.cashiersQueue[i].max())
				maxNumberCashier = model.cashiersQueue[i].max();
		}
		System.out.println("2 - max number cashiers: " + maxNumberCashier);

		/* THIRD POINT STATISTICS */
		System.out.println("3 - Client A mean delay time: " + model.clientAMeanDelayTime.mean());
		System.out.println("3 - Client B mean delay time: " + model.clientBMeanDelayTime.mean());
		System.out.println("3 - Client C mean delay time: " + model.clientCMeanDelayTime.mean());
		System.out.println("3 - Client A max delay time: " + model.clientAMaxDelayTime.value());
		System.out.println("3 - Client B max delay time: " + model.clientBMaxDelayTime.value());
		System.out.println("3 - Client C max delay time: " + model.clientCMaxDelayTime.value());

		/* FORTH POINT STATISTICS */
		System.out.println("4 - Overall mean delay time (weighted): " + (model.clientAMeanDelayTime.mean()*0.8 + model.clientBMeanDelayTime.mean()*0.15 + model.clientCMeanDelayTime.mean()*0.05));
		System.out.println("4 - Overall max delay time (weighted): " + (model.clientAMaxDelayTime.value()*0.8 + model.clientBMaxDelayTime.value()*0.15 + model.clientCMaxDelayTime.value()*0.05));

		/* FIFTH POINT STATISTICS */
		System.out.println("5 - Mean time per user: " + model.overallMeanTime.mean());
		System.out.println("5 - Total users: " + model.overallMeanTime.count());
		model.clear();
	}
}

final class Server extends Model {
	final Accumulate hotFoodQueue, sandwichesQueue;
	final Accumulate restSandwiches, restHotFood;
	public final Average sandwichesMeanDelaytime, hotFoodMeanDelayTime, cashierMeanDelayTime, clientAMeanDelayTime, clientBMeanDelayTime, clientCMeanDelayTime, overallMeanTime;
	public final Max sandwichesMaxDelaytime, hotFoodMaxDelayTime, cashierMaxDelayTime, clientAMaxDelayTime, clientBMaxDelayTime, clientCMaxDelayTime;
	public final List<Token> sandwiches, hotFood;
	public final ArrayList<Token>[] cashiers;
	public final Accumulate[] cashiersQueue, restCashiers;
	final Uniform hotFoodDist, sandwichesDist, drinksDist, hotFoodExtraDist, sandwichesExtraDist, drinksExtraDist;
	final Discrete choiceDist, groupsDist;
	final Exponential arrivalDist;
	public Server(int hf, int sd, int cs) {
		super();

		hotFoodQueue = new Accumulate(0);
		sandwichesQueue = new Accumulate(0);
		cashiersQueue = new Accumulate[2+cs];
		for(int i = 0; i < cashiersQueue.length; i++)
			cashiersQueue[i] = new Accumulate(0);

		restSandwiches = new Accumulate(1);
		restHotFood = new Accumulate(1);
		restCashiers = new Accumulate[2+cs];
		for(int i = 0; i < restCashiers.length; i++)
			restCashiers[i] = new Accumulate(1);

		sandwichesMeanDelaytime = new Average();
		hotFoodMeanDelayTime = new Average();
		cashierMeanDelayTime = new Average();
		clientAMeanDelayTime = new Average();
		clientBMeanDelayTime = new Average();
		clientCMeanDelayTime = new Average();
		overallMeanTime = new Average();

		sandwichesMaxDelaytime = new Max();
		hotFoodMaxDelayTime = new Max();
		cashierMaxDelayTime = new Max();
		clientAMaxDelayTime = new Max();
		clientBMaxDelayTime = new Max();
		clientCMaxDelayTime = new Max();

		sandwiches = new ArrayList<>();
		hotFood = new ArrayList<>();
		cashiers = new ArrayList[2+cs];
		for(int i = 0; i < cashiers.length; i++)
			cashiers[i] = new ArrayList<Token>();

		arrivalDist = new Exponential((int)new Date().getTime(), 30.0);
		hotFoodDist = new Uniform((int)new Date().getTime(), 50.0/hf, 120.0/hf);
		sandwichesDist = new Uniform((int)new Date().getTime(), 60.0/sd, 180.0/sd);
		drinksDist = new Uniform((int)new Date().getTime(), 5.0, 20.0);

		hotFoodExtraDist = new Uniform((int)new Date().getTime(), 20.0, 40.0);
		sandwichesExtraDist = new Uniform((int)new Date().getTime(), 5.0, 15.0);
		drinksExtraDist = new Uniform((int)new Date().getTime(), 5.0, 10.0);

		choiceDist = new Discrete((int)new Date().getTime(), new double[]{0, 1, 2}, new double[]{0.8, 0.15, 0.05});
		groupsDist = new Discrete((int)new Date().getTime(), new double[]{1, 2, 3, 4}, new double[]{0.5, 0.3, 0.1, 0.1});


	}
	@Override
	protected void init() {
		schedule(new Arrival(this), arrivalDist.next());
		schedule(new Stop(this), 9000);
	}
}
