import desmoj.core.simulator.*;
import desmoj.core.dist.*;
import java.util.concurrent.*;

public class Projecto3{
    public static void main (String[] args) {
        //create model and experiment
        Model model = new MainModel();
        Experiment exp = new Experiment("MyExperiment", TimeUnit.SECONDS, TimeUnit.MINUTES, null);
        //and link them
        model.connectToExperiment(exp);
        //initialize experiments parameters
        exp.setShowProgressBar(false);
        exp.tracePeriod(new TimeInstant(0.0), new TimeInstant(10000));
        exp.debugPeriod(new TimeInstant(0.0), new TimeInstant(10000));
        exp.stop(new TimeInstant(10000 * 60, TimeUnit.MINUTES));
        //start experiment
        exp.start();
        //generate report and shut everything down
        exp.report();
        exp.finish();
    }
}