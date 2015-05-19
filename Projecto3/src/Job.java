import desmoj.core.simulator.*;
import desmoj.core.dist.*;

public class Job extends SimProcess{
	Model model;
	int jobType;
	int curTarget;
	int[][] jobs = {{0, 3, 1, 2, 5}, 
				{0, 4, 1, 3},
				{0, 2, 5, 1, 4, 3}};

	public Job(Model model, int jobType) {
		super(model, "Job", true);

		this.model = model;
		this.jobType = jobType;
		curTarget = 1;

	}
	
	public Motion getNextMotion() {
		return new Motion(jobs[jobType][curTarget-1], jobs[jobType][curTarget-1]);
	}
	public void advance(){
		curTarget++;
	}
	public boolean isLastStation(){
		return curTarget >= jobs[jobType].length;
	}
	public int getJobType(){
		return jobType;
	}
	public int getCurrentStage(){
		return curTarget - 1;
	}
	public int getCurrentStation(){
		return jobs[jobType][curTarget - 1];
	}

	public void init(){

	}
	public void lifeCycle(){

	}

}
