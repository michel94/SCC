import desmoj.core.simulator.*;
import desmoj.core.dist.*;
import java.util.concurrent.*;

public class Machine extends SimProcess{
	MainModel model;
	Station station;
	String name;
	public boolean isWorking = false;
	public boolean waiting = false;
	double totalWorkingTime = 0;
	double totalBlockedTime = 0;
	double totalIdleTime = 0;
	double time = 0;

	public Machine(MainModel model, Station station, String name){
		super(model, name, true, true);
		this.model = model;
		this.station = station;
		this.name = name;
	}
	public void init(){
	}

	public void fetchTime(){
		time = model.getExperiment().getSimClock().getTime().getTimeAsDouble(TimeUnit.MINUTES);
	}
	public void lifeCycle(){
		while(true){
			isWorking = false;
			while(station.isQueueEmpty())
				passivate();
			isWorking = true;

			Job job = station.popFromQueue();
			int jobType = job.getJobType();
			int curStage = job.getCurrentStage();
			System.out.println("On " + station.getName() + ", " + name + ", job " + jobType + ", " + curStage);

			double t = model.getServiceTime(jobType, curStage);
			totalWorkingTime += t;
			hold(new TimeSpan(t, TimeUnit.MINUTES) );
			job.setMachine(this);


			if(!job.isLastStation()){
				model.getAvg().pushToQueue(job);
				//System.out.println("pushed to other queue");
				fetchTime();
				passivate();
				totalBlockedTime += model.getExperiment().getSimClock().getTime().getTimeAsDouble(TimeUnit.MINUTES) - time;
			}
		}
	}
}
