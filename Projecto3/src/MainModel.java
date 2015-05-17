import desmoj.core.simulator.*;
import desmoj.core.dist.*;

public class MainModel extends Model {
	private ContDistExponential newTruckDist;

	public MainModel(){
		super(null, "Main", true, true);
		System.out.println("MainModel");

	}
	public String description(){
		return "";
	}
	public void init(){
		newTruckDist = new ContDistExponential(this, "newTruckDist", 15, true, false);
		newTruckDist.setNonNegative(true);
	}
	public void doInitialSchedules() {
		IOStation ioStation = new IOStation(this);
		ioStation.activate(new TimeSpan(0));
		
	}
	public Double newTruckDistTime(){
		return newTruckDist.sample();
	}
}