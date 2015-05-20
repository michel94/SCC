import desmoj.core.simulator.*;
import desmoj.core.dist.*;

public class MainModel extends Model {
	private ContDistExponential newJobDist;
	private AVG avg;
	double[][] meanServiceTimes = {{30, 36, 51, 30}, {66, 48, 45}, {72, 15, 42, 54, 60}};
	ContDistErlang[][] serviceTimesDist;
	Station[] stations;
	int[] nMachines = {3, 3, 4, 4, 1};

	public MainModel(){
		super(null, "Main", true, true);
		System.out.println("MainModel");

	}
	public String description(){
		return "";
	}
	public void init(){
		newJobDist = new ContDistExponential(this, "newJobDist", 15, true, true);
		newJobDist.setNonNegative(true);

		serviceTimesDist = new ContDistErlang[4][5];
		for(int t = 0; t<meanServiceTimes.length; t++)
			for(int i = 0; i<meanServiceTimes[t].length; i++)
				serviceTimesDist[t][i] = new ContDistErlang(this, "serviceTimesDist " + t + ", " + i, 1, meanServiceTimes[t][i], false, false);

		stations = new Station[5];
		for(int i=0; i<stations.length; i++)
			stations[i] = new Station(this, "station " + (i+1), nMachines[i]);
		
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

	public double getServiceTime(int jobType, int curStage){
		return serviceTimesDist[jobType][curStage-1].sample();
	}
	public Station getStation(int n){
		return stations[n];
	}
}
