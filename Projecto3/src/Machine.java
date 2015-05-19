import desmoj.core.simulator.*;
import desmoj.core.dist.*;
import java.util.concurrent.*;

public class Machine extends SimProcess{
	MainModel model;
	Station station;
	
	public Machine(MainModel model, Station station){
		super(model, "Machine", false, false);
		this.model = model;
		this.station = station;	
	}
	public void init(){
	}

	public void lifeCycle(){
		while(true){
			if(station.isQueueEmpty())
				passivate();

			Job job = station.popFromQueue();
			int jobType = job.getJobType();
			int curStage = job.getCurrentStage();
			hold(new TimeSpan(model.getServiceTime(jobType, curStage), TimeUnit.MINUTES) );

			if(!job.isLastStation())
				model.getAvg().pushToQueue(job);

		}
	}
}