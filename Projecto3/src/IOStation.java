import desmoj.core.simulator.*;
import desmoj.core.dist.*;
import java.util.concurrent.*;

public class IOStation extends SimProcess{
	MainModel model;
	Station ioStation;
	ContDistConstant jobTypeDist;

	public IOStation(MainModel model){
		super(model, "IOStation", false, false);
		this.model = model;
		
	}
	public void init(){
		jobTypeDist = new ContDistConstant(model, "jobTypeDist", 1, false, false);
	}
	public void lifeCycle(){
		while(true){	

			int type;
			double r = jobTypeDist.sample();
			if(r < 0.3)
				type = 0;
			else if(r < 0.8)
				type = 2;
			else
				type = 3;

			Job job = new Job(model, type);

			Double t = model.newJobDistTime();
			System.out.println("New Job " + t);
			hold(new TimeSpan(t, TimeUnit.MINUTES));
			
			model.getAvg().pushToQueue(job);

		}
	}
}