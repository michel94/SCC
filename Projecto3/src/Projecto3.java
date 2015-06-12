import desmoj.core.simulator.*;
import desmoj.core.dist.*;
import java.util.concurrent.*;

public class Projecto3{
    public static void main (String[] args) {
        //create model and experiment
        MainModel model = new MainModel();
        System.out.println(args[0]);
        model.extra = Integer.parseInt(args[0]);
        Experiment exp = new Experiment("MyExperiment", TimeUnit.SECONDS, TimeUnit.MINUTES, null);
        //and link them
        model.connectToExperiment(exp);
        //initialize experiments parameters
        exp.setShowProgressBar(false);
        exp.tracePeriod(new TimeInstant(0.0), new TimeInstant(10000));
        exp.debugPeriod(new TimeInstant(0.0), new TimeInstant(10000));
        exp.stop(new TimeInstant(2900 * 60, TimeUnit.MINUTES));
        //start experiment
        exp.start();
        //generate report and shut everything down
        exp.report();
        exp.finish();
        
        System.out.println(model.getAvg().totalMovingTime);

        for(int i=0; i<3; i++)
            System.out.println("Job Type:" + i + " AvgMeanWaitTime " + model.avgWaitTime[i] / model.finished + " QueueMeanWaitTime " + model.queueWaitTime[i] / model.finished );
        System.out.println("Mean Cycle Time: " + model.cycleTime / model.finished);

        for(int i=0; i<model.stations.length;i++)
        {
            for(int k=0; k<model.stations[i].machines.length;k++)
            {
                System.out.println("Station: " + i + " Machine: " + k + " Working Time:" + model.stations[i].machines[k].totalWorkingTime + " Blocking Time:" + model.stations[i].machines[k].totalBlockedTime + " Idle Time:" + (2900*60.0 - model.stations[i].machines[k].totalWorkingTime - model.stations[i].machines[k].totalBlockedTime));
            }
        }
    }
}
