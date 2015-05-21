import desmoj.core.simulator.*;
import desmoj.core.dist.*;
import java.util.concurrent.*;

public class IOStation extends SimProcess{
	MainModel model;
	Station ioStation;
	ContDistUniform jobTypeDist;

	public IOStation(MainModel model){
		super(model, "IOStation", false, false);
		this.model = model;

		jobTypeDist = new ContDistUniform(model, "jobTypeDist", 0, 1, false, false);
		System.out.println("jobTypeDist");
	}
	public void init(){
	}
	public void lifeCycle(){
		System.out.println("lifeCycle");
		while(true){

			int type;
			double r = jobTypeDist.sample();
			if(r < 0.3)
				type = 0;
			else if(r < 0.8)
				type = 1;
			else
				type = 2;

			Job job = new Job(model, type);

			Double t = model.newJobDistTime();
			//System.out.println("New Job " + t+ " type: " + type);
			hold(new TimeSpan(t, TimeUnit.MINUTES));

			model.getAvg().pushToQueue(job);
		}
	}
}