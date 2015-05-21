import desmoj.core.simulator.*;
import desmoj.core.dist.*;
import java.util.concurrent.*;

public class AVG extends SimProcess {
	MainModel model;
	Queue<Job> avgQueue;
	int currentPos = 0;
	int totalMovingTime = 0;
	int[][] distances ={{0, 135,135,90,50 ,50, 0},
						{135, 0  ,45 ,50,90 ,100},
						{135, 45 ,0  ,50,100,90 },
						{90, 50 ,50 ,0 ,50 ,50 },
						{50, 90 ,100,50,0  ,45 },
						{50, 100,90 ,50,45 ,0  }};
	boolean onHold = false;

	public AVG(MainModel model) {
		super(model, "AVG", true, true);
		this.model = model;

		avgQueue = new Queue<Job>(model, "AVG Queue", true, true);
	}

	private void moveTo(int end){
		double moveTime = distances[currentPos][end] / 150;
		totalMovingTime += moveTime;

		if(moveTime > 0){
			sendTraceNote("hold");
			onHold = true;
			hold(new TimeSpan(moveTime, TimeUnit.MINUTES) );
			onHold = false;
		}


		System.out.println("Move from " + currentPos + " to " + end + ", distance " + distances[currentPos][end]);
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
				if(!job.isLastStation()){
					Station s = model.getStation(job.getCurrentStation());
					s.pushToQueue(job);
					sendTraceNote("Queue " + s.name + " size: " + s.queue.size());
				}else{
					System.out.println(">>>>>>>>>>>>>>>> Finished job");
				}
				

			}else{
				passivate();
			}


		}
	}

	public void pushToQueue(Job job){
		avgQueue.insert(job);
		if(!onHold)
			activate(new TimeSpan(0));
	}

}
