import desmoj.core.simulator.*;
import desmoj.core.dist.*;
import java.util.concurrent.*;
import java.util.*;
import java.nio.file.*;
import java.io.*;

public class Main{
  double data[];
  double dataAux[];
  int n = 600000, k = 30;

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
      double x = data[i];
      maxDiff = max( ((i+1.0)/n - x ), maxDiff );
      minDiff = max( (x - (i)/n ), minDiff );
      //minDiff = max( Math.sqrt(n) * data[i] * (i/n - data[i]), minDiff );
    }
    double diff;
    if(maxDiff > minDiff)
      diff = maxDiff;
    else
      diff = minDiff;
    //diff = (Math.sqrt(n) + 0.12 + 0.11/Math.sqrt(n)) * diff;
    System.out.println("1 - Kolmogorov: " + diff + " " + 1.358);
  }

  public double coisa(int k){
    return 2.0 / (9.0 * k);
  }

  public double crit(double value){
    return 1.645;
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


  public void KSTestTriangular(double d1[], double d2[]){
    double data[] = new double[d1.length];
    int n = data.length;

    for(int i=0; i<n; i++){
      data[i] = max(d1[i], d2[i]);
    }

    Arrays.sort(data);
    double maxDiff = 0.0, minDiff = 0.0;

    System.out.println(n);
    for(int i=0; i<n; i++){
      //System.out.println(data[i] + " " + (i+1)/(double)n);
      double maxRandomNumber = data[i];
      double e = Math.pow(maxRandomNumber, 2);
      maxDiff = max( ( (i+1.0)/n - e ), maxDiff );
      minDiff = max( (e - i/(double)n ), minDiff );
      //minDiff = max( Math.sqrt(n) * data[i] * (i/n - data[i]), minDiff );
    }

    double diff;
    if(maxDiff > minDiff)
      diff = maxDiff;
    else
      diff = minDiff;

    System.out.println("2 - Kolmogorov-Smirnov Triangular Test: " + minDiff + " " + 1.358);
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
    System.out.println("  - TwoLevelTest: " + result + " " + limit);

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
    System.out.println("  - ThreeLevelTest: " + result + " " + limit);

    return result;
  }

  public int RunsTest(double[] d){
    double data[] = Arrays.copyOf(d, d.length),
         sorted[] = Arrays.copyOf(d, d.length);
    boolean plus[] = new boolean[d.length];

    Arrays.sort(sorted);
    double med = sorted[sorted.length / 2];

    int runs = 0;
    for(int i=1; i<data.length; i++){
      if( (data[i-1] > med) != (data[i] > med) )
        runs++;
    }

    System.out.println("  - RunsTest: " + runs);

    return runs;

  }

  class WeightRank{
    private int count=0;
    private double rank=0;
    public WeightRank() {
    }
    public void addRank(int rank){
      this.rank += rank;
      count++;
    }
    public double getRank(){
      return rank / count;
    }

  }

  public double WeibullEstim(double data[]){
    double k = 1, l, sum=0;
    int n = data.length;

    double dif=10000;
    for(double kx=0; kx<3; kx+=0.01){
      double s=0, s2=0, s3=0;
      for(int i=0; i<data.length; i++){
        s += Math.log(data[i]) / Math.log(Math.E) * Math.pow(data[i], kx);
        s2+= Math.pow(data[i], kx);
        s3+= Math.log(data[i]) / Math.log(Math.E);
      }
      double t = 1 / kx - s/s2 + s3/n;
      if( Math.abs(t) < Math.abs(dif) ){
        dif = t;
        k = kx;
      }
    }

    for(int i=0; i<n; i++){
      sum += Math.pow(data[i], k);
    }
    l = Math.pow(sum / n, 1/k);

    System.out.println("3b- Weibull: alpha = " + k + ", beta = " + l);

    return l;

  }

  public double Kruskal(double[] d1, double[] d2){
    int n1 = d1.length, n2 = d2.length;
    int N = n1 + n2;

    double sorted[] = new double[N];
    System.arraycopy(d1, 0, sorted, 0, n1);
    System.arraycopy(d2, 0, sorted, n1, n2);
    Arrays.sort(sorted);

    Hashtable<Double, WeightRank> dict = new Hashtable<Double, WeightRank>();

    for(int i=0; i<sorted.length; i++){
      if(!dict.containsKey(sorted[i]))
        dict.put(sorted[i], new WeightRank() );
      dict.get(sorted[i]).addRank(i+1);
    }

    double rank1[], rank2[];
    rank1 = new double[n1];
    rank2 = new double[n2];

    double r1_avg=0, r2_avg=0;

    for(int i=0; i<rank1.length; i++){
      rank1[i] = dict.get(d1[i]).getRank();
      r1_avg += rank1[i];
    }
    r1_avg /= n1;

    for(int i=0; i<rank2.length; i++){
      rank2[i] = dict.get(d2[i]).getRank();
      r2_avg += rank2[i];
    }
    r2_avg /= n2;

    double r_avg = 0.5 * (N + 1);

    double usum = n1 * Math.pow(r1_avg - r_avg, 2) +
                  n2 * Math.pow(r2_avg - r_avg, 2);

    double dsum = 0;
    for(int i=0; i<n1; i++){
      dsum += Math.pow(rank1[i] - r_avg, 2);
    }
    for(int i=0; i<n2; i++){
      dsum += Math.pow(rank2[i] - r_avg, 2);
    }

    double K = (N-1) * usum / dsum;

    return K;

  }

  public Main(){
    data = new double[n];
    dataAux = new double[n];
    EmptyModel emptyModel = new EmptyModel();
    Experiment exp = new Experiment("MyExperiment", TimeUnit.SECONDS, TimeUnit.MINUTES, null);
    emptyModel.connectToExperiment(exp);

    ContDistUniform dist = new ContDistUniform(
        emptyModel, "test",
        0, 1, false, false);

    for(int i=0; i<n; i++){
      data[i] = dist.sample();
      dataAux[i] = dist.sample();
    }

    KSTest(data);
    TwoLevelTest(data);
    ThreeLevelTest(data);
    RunsTest(data);

    //double kruskal = Kruskal(new double[]{1, 2, 3, 4}, new double[]{1, 2, 3, 4, 5});

    Path file1 = Paths.get("kruskal1.txt"), file2 = Paths.get("kruskal2.txt");
    double d1[], d2[];

    try {
      Scanner sc = new Scanner(file1);
      int n1 = sc.nextInt();
      d1 = new double[n1];
      for(int i=0; i<n1; i++){
        d1[i] = sc.nextDouble();
      }

      sc = new Scanner(file2);
      int n2 = sc.nextInt();
      d2 = new double[n2];
      for(int i=0; i<n2; i++){
        d2[i] = sc.nextDouble();
      }
    } catch (IOException e) {
      System.out.println("Reading error " + e);
      return;
    }

    KSTestTriangular(data, dataAux);

    double kruskal = Kruskal(d1, d2);
    double crit = 3.8415;
    System.out.println("3a- kruskal-Wallis: " + kruskal + " " + crit);


    WeibullEstim(d1);

    /*double trTest = TwoLevelTest(data);
    double limit = chiSquare(30*30-1, 1-0.05);
    System.out.println("TwoLevelTest " + trTest + " " + limit);
*/
  }

  public static void main(String[] args) {
    new Main();
  }
}
