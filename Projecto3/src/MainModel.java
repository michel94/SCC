import desmoj.core.simulator.*;


public class MainModel extends Model {
	Truck truck;

	public MainModel(){
		super(null, "Main", true, true);
		truck = new Truck(this);

	}
	public String description(){
		return "";
	}
	public void init(){

	}
	public void doInitialSchedules(){

	}
}