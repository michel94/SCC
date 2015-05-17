import desmoj.core.simulator.*;
import desmoj.core.dist.*;

public class MainModel extends Model {
	private ContDistExponential newJobDist;
	private AVG avg;

	public MainModel(){
		super(null, "Main", true, true);
		System.out.println("MainModel");

	}
	public String description(){
		return "";
	}
	public void init(){
		newJobDist = new ContDistExponential(this, "newJobDist", 15, true, false);
		newJobDist.setNonNegative(true);
	}
	public void doInitialSchedules() {
		IOStation ioStation = new IOStation(this);
		ioStation.activate(new TimeSpan(0));

		avg = new AVG(this);
		avg.activate(new TimeSpan(0));
		
	}
	public AVG getAvg(){
		return avg;
	}
	public Double newJobDistTime(){
		return newJobDist.sample();
	}
}