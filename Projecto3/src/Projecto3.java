import desmoj.core.simulator.*;
import desmoj.core.dist.*;

public class Projecto3{
    public static void main (String[] args) {
        //create model and experiment
        Model model = new MainModel();
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
