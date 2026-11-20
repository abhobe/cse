import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class DotProduct {
    static ForkJoinPool POOL = ForkJoinPool.commonPool();
    static int CUTOFF;

    // Behavior should match Sequential.dotProduct
    // Your implementation must have linear work and log(n) span
    public static double dotProduct(double[] a, double[]b, int cutoff){
        DotProduct.CUTOFF = cutoff;
        return POOL.invoke(new DotProductTask(a, b, 0, a.length));
    }

    private static class DotProductTask extends RecursiveTask<Double>{
        private double[] a;
        private double[] b;
        private int start;
        private int end;

        public DotProductTask(double[] a, double[] b, int start, int end){
            this.a = a;
            this.b = b;
            this.start = start;
            this.end = end;
        }

        public Double compute(){
            if (end - start <= CUTOFF) {
                double sum = 0.0;
                for (int i = start; i < end; i++) {
                    sum += a[i] * b[i];
                }
                return sum;
            } else {
                int mid = (start + end) / 2;
                DotProductTask leftTask = new DotProductTask(a, b, start, mid);
                DotProductTask rightTask = new DotProductTask(a, b, mid, end);
                leftTask.fork();
                double rightResult = rightTask.compute();
                double leftResult = leftTask.join();
                return leftResult + rightResult;
            }
        }
    }
    
}
