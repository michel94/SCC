import desmoj.core.simulator.*;
import desmoj.core.dist.*;
import java.util.concurrent.*;

public class AVG extends SimProcess {
	MainModel model;
	ProcessQueue<Job> avgQueue;
	int currentPos = 0;
	int[][] distances ={{0, 135,135,90,50 ,50, 0},
						{135, 0  ,45 ,50,90 ,100},
						{135, 45 ,0  ,50,100,90 },
						{90, 50 ,50 ,0 ,50 ,50 },
						{50, 90 ,100,50,0  ,45 },
						{50, 100,90 ,50,45 ,0  }};

	public AVG(MainModel model) {
		super(model, "Truck", true);
		this.model = model;
	}

	public void init() {
		avgQueue = new ProcessQueue<Job>(model, "AVG Queue", true, true);
	}

	private void moveTo(int end){
		hold(new TimeSpan(distances[currentPos][end] / 2.5, TimeUnit.MINUTES) );
		currentPos = end;
	}

	public void lifeCycle(){
		while(true){
			if(!avgQueue.isEmpty()){
				Job job = avgQueue.first();
				avgQueue.remove(job);

				Motion m = job.getNextMotion();
				moveTo(m.start);
				moveTo(m.end);

				job.advance();
				model.getStation(job.getCurrentStation()).pushToQueue(job);
			}else{
				passivate();
			}


		}
	}

	public void pushToQueue(Job job){
		avgQueue.insert(job);
		activate(new TimeSpan(0));
	}

}