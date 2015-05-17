import desmoj.core.simulator.*;
import desmoj.core.dist.*;
import java.util.concurrent.*;

public class IOStation extends SimProcess{
	MainModel model;

	public IOStation(MainModel model){
		super(model, "IOStation", true);
		this.model = model;
	}
	public void init(){
		
		System.out.println("IOStation");
		
	}
	public void lifeCycle(){
		
		while(true){

			Job job = new Job(model);
			job.activate(new TimeSpan(0));

			Double t = model.newJobDistTime();
			System.out.println("New Job " + t);
			hold(new TimeSpan(t, TimeUnit.MINUTES));
			
		}

	}

}