import desmoj.core.simulator.*;
import desmoj.core.dist.*;
import java.util.concurrent.*;

public class Machine extends SimProcess{
	MainModel model;
	Station station;
	String name;
	
	public Machine(MainModel model, Station station, String name){
		super(model, name, true, true);
		this.model = model;
		this.station = station;	
		this.name = name;
	}
	public void init(){
	}

	public void lifeCycle(){
		while(true){
			while(station.isQueueEmpty())
				passivate();

			Job job = station.popFromQueue();
			int jobType = job.getJobType();
			int curStage = job.getCurrentStage();
			System.out.println("on " + station.getName() + ", " + name + ", job " + jobType + ", " + curStage);

			hold(new TimeSpan(model.getServiceTime(jobType, curStage), TimeUnit.MINUTES) );

			job.setMachine(this);
			if(!job.isLastStation())
				model.getAvg().pushToQueue(job);


		}
	}
}