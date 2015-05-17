import desmoj.core.simulator.*;
import desmoj.core.dist.*;

public class Job extends SimProcess {
	Model model;
	int jobType;
	int curTarget;
	int jobs = {{0, 3, 1, 2, 5}, 
				{0, 4, 1, 3},
				{0, 2, 5, 1, 4, 3}}

	public Job(Model model) {
		super(model, "Job", true);

		this.model = model;
	}
	
	public getNextMotion() {
		
	}

	public void lifeCycle() {

	}

	public void init() {
		
	}
}