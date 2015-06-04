import desmoj.core.simulator.*;
import desmoj.core.dist.*;
import java.util.concurrent.*;

public class Main{
  double data[];
  int n = 60000;

  public Main(){
    data = new double[n];
    EmptyModel emptyModel = new EmptyModel();
    Experiment exp = new Experiment("MyExperiment", TimeUnit.SECONDS, TimeUnit.MINUTES, null);
    emptyModel.connectToExperiment(exp);

    ContDistUniform dist = new ContDistUniform(
        emptyModel, "test",
        0, 1, false, false);

    for(int i=0; i<n; i++){
      data[i] = dist.sample();
      System.out.println(data[i]);
    }

  }

  public static void main(String[] args) {
    new Main();
  }
}
