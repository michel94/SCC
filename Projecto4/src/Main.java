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

  public double coisa(int k){
    return 2.0 / (9.0 * k);
  }

  public double crit(double value){
    return -1.645;
  }

  public double chiSquareDist(int k, double alpha){
    return k * Math.pow( 1 - coisa(k) + crit(1-alpha) * Math.sqrt(coisa(k)), 3);
  }

  public double chiSquareTest(double data[], double e){
    double sum=0;
    for(int i=0; i<data.length; i++){
      sum += Math.pow(data[i] - e, 2) / e;
    }

    return sum;

  }

  public double TwoLevelTest(double d[]){
    double data[] = Arrays.copyOf(d, d.length);
    int quads[][] = new int[k][k];
    for(int i=0; i<n; i+=2){
      quads[(int)(data[i]*k) ][(int)(data[i+1]*k) ]++;
    }
    double count[] = new double[k * k];
    for(int i=0; i<k; i++){
      for(int j=0; j<k; j++){
        count[i + j*k] = quads[i][j];
      }
    }
    double result = chiSquareTest(count, (n/2.0) / (k*k) );
    double limit = chiSquareDist(k*k-1, 1-0.05);
    System.out.println("TwoLevelTest: " + result + " " + limit);

    return result;
  }

  public double ThreeLevelTest(double d[]){
    double data[] = Arrays.copyOf(d, d.length);
    int quads[][][] = new int[k][k][k];
    for(int i=0; i<n; i+=3){
      quads[(int)(data[i]*k) ][(int)(data[i+1]*k) ][(int)(data[i+2]*k) ]++;
    }
    double count[] = new double[k*k*k];
    for(int i=0; i<k; i++){
      for(int j=0; j<k; j++){
        for(int l=0; l<k; l++){
          count[i + j*k + l*k*k] = quads[i][j][l];
        }
      }
    }
    double result = chiSquareTest(count, (n/3.0) / (k*k*k) );
    double limit = chiSquareDist(k*k*k-1, 1-0.05);
    System.out.println("ThreeLevelTest: " + result + " " + limit);

    return result;
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
    TwoLevelTest(data);
    ThreeLevelTest(data);

    /*double trTest = TwoLevelTest(data);
    double limit = chiSquare(30*30-1, 1-0.05);
    System.out.println("TwoLevelTest " + trTest + " " + limit);
*/
  }

  public static void main(String[] args) {
    new Main();
  }
}
