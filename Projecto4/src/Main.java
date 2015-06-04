import desmoj.core.simulator.*;
import desmoj.core.dist.*;
import java.util.concurrent.*;
import java.util.Arrays;

public class Main{
  double data[];
  int n = 60000, k = 30;

  public double max(double a, double b){
    if(a > b)
      return a;
    return b;
  }

  public void KSTest(double d[]){
    double data[] = Arrays.copyOf(d, d.length);
    Arrays.sort(data);

    double maxDiff = 0.0, minDiff = 0.0;

    for(int i=0; i<n; i++){
      //System.out.println(data[i] + " " + (i+1)/(double)n);
      maxDiff = max( Math.sqrt(n) * data[i] * (data[i] - (i+1)/(double)n ), maxDiff );
      minDiff = max( Math.sqrt(n) * data[i] * ((i+1)/(double)n - data[i] ), minDiff );
      //minDiff = max( Math.sqrt(n) * data[i] * (i/n - data[i]), minDiff );
    }
    System.out.println("Kolmogorov: " + minDiff + " " + maxDiff);
  }

  public void TwoLevelTest(double d[]){
    double data[] = Arrays.copyOf(d, d.length);
    int quads[][] = new int[k][k];
    for(int i=0; i<n; i+=2){
      quads[round(data[i]*k)][round(data[i+1]*k)]++;
    }

  }

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
    }

    KSTest(data);



  }

  public static void main(String[] args) {
    new Main();
  }
}
