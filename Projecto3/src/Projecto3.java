import desmoj.core.simulator.*;
import desmoj.core.dist.*;

class MyModelClass extends Model {
    /**
    * ProcessesExample constructor.
    *
    * Creates a new ProcessesExample model via calling
    * the constructor of the superclass.
    *
    * @param owner the model this model is part of (set to null when there is
    *              no such model)
    * @param modelName this model's name
    * @param showInReport flag to indicate if this model shall produce output
    *                     to the report file
    * @param showInTrace flag to indicate if this model shall produce output
    *                    to the trace file
    */
    public MyModelClass(Model owner, String modelName, boolean showInReport, boolean showInTrace) {
        super(owner, modelName, showInReport, showInTrace);
    }
    /**
    * Returns a description of the model to be used in the report.
    * @return model description as a string
    */
    public String description() {
        return "This model describes a queueing system located at a "+
        "container terminal. Trucks will arrive and "+
        "require the loading of a container. A van carrier (VC) is "+
        "on duty and will head off to find the required container "+
        "in the storage. It will then load the container onto the "+
        "truck. Afterwards, the truck leaves the terminal. "+
        "In case the VC is busy, the truck waits "+
        "for its turn on the parking-lot. "+
        "If the VC is idle, it waits on its own parking spot for the "+
        "truck to come.";
    }
    /**
    * Activates dynamic model components (simulation processes).
    *
    * This method is used to place all events or processes on the
    * internal event list of the simulator which are necessary to start
    * the simulation.
    *
    * In this case, the truck generator and the van carrier(s) have to be
    * created and activated.
    */
    public void doInitialSchedules() {}
    /**
    * Initialises static model components like distributions and queues.
    */
    public void init() {}
}
public class Projecto3{
    public static void main (String[] args) {
        //create model and experiment
        MyModelClass model = new MyModelClass(null, "a", true, true);
        Experiment exp = new Experiment("MyExperiment");
        //and link them
        model.connectToExperiment(exp);
        //initialize experiments parameters
        exp.setShowProgressBar(true);
        exp.tracePeriod(new TimeInstant(0.0), new TimeInstant(100.0));
        exp.debugPeriod(new TimeInstant(10.0), new TimeInstant(50.0));
        exp.stop(new TimeInstant(480.0));
        //start experiment
        exp.start();
        //generate report and shut everything down
        exp.report();
        exp.finish();
    }
}
