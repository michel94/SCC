import desmoj.core.simulator.*;
import desmoj.core.dist.*;

public class Job extends SimProcess {
	Model model;

	public Job(Model model) {
		super(model, "Job", true);

		this.model = model;
	}

	public void lifeCycle(){

	}

	public void init() {
		

	}
}