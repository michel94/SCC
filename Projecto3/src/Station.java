import desmoj.core.simulator.*;
import desmoj.core.dist.*;
import java.util.concurrent.*;

public class Station{
	MainModel model;
	Machine[] machines;
	public String name;
	public Queue<Job> queue;

	public Station(MainModel model, String name, int nMachines){
		this.model = model;
		this.name = name;
		machines = new Machine[nMachines];
		for(int i=0; i<nMachines; i++)
			machines[i] = new Machine(model, this, "Machine " + i);

		System.out.println("Station");
		queue = new Queue<Job>(model, name + " Queue", true, true);
	}

	public Job popFromQueue(){
		Job job = queue.first();
		queue.remove(job);
		System.out.println("Queue " + name + " size: " + queue.size());
		return job;
	}

	public void pushToQueue(Job job){
		queue.insert(job);
		for(int i=0; i<machines.length; i++)
			if(!machines[i].isWorking){
				machines[i].activate(new TimeSpan(0));
				break;
			}
		System.out.println("Queue " + name + " size: " + queue.size());
	}

	public boolean isQueueEmpty(){
		return queue.isEmpty();
	}
	public String getName(){
		return name;
	}

}
