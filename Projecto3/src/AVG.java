import desmoj.core.simulator.*;
import desmoj.core.dist.*;

public class AVG extends SimProcess {
	Model model;
	ProcessQueue<Job> avgQueue;
	int currentPos = 0;
	int[][] distances ={{0  ,45 ,50,90 ,100,135},
						{45 ,0  ,50,100,90 ,135},
						{50 ,50 ,0 ,50 ,50 ,90 },
						{90 ,100,50,0  ,45 ,50 },
						{100,90 ,50,45 ,0  ,50 },
						{135,135,90,50 ,50 , 0 }};

	public AVG(Model model) {
		super(model, "Truck", true);
		this.model = model;
	}

	public void init() {
		avgQueue = new ProcessQueue<Job>(this, "AVG Queue", true, true);
	}

	private void moveTo(int end){
		hold(new TimeSpan(distances[currentPos][end], TimeUnit.MINUTES) );
		currentPos = end;
	}

	public void lifeCycle(){
		while(true){
			if(!avgQueue.empty()){
				Job job = avgQueue.first();
				avgQueue.remove(job);

				Motion m = job.getNextMotion();
				moveTo(m.start);
				moveTo(m.end);
			}else{
				passivate();
			}


		}
	}

	public void addToQueue(Job job){
		avgQueue.insert(job);
	}

}