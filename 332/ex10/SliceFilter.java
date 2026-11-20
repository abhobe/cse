import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class SliceFilter {
    static ForkJoinPool POOL = ForkJoinPool.commonPool();
    static int CUTOFF;

    // Behavior should match Sequential.takeSlice.
    // Ignoring the initialization of arrays, your implementation must have linear
    // work and log(n) span
    public static double[][] takeSlice(double[][] arr, double[] point, double[] normal, int cutoff) {
        SliceFilter.CUTOFF = cutoff;
        double pointNormal = dotProduct(point, normal);
        double[] dotProducts = new double[arr.length];
        int[] mask = new int[arr.length];

        POOL.invoke(new DotProductAction(arr, dotProducts, normal, 0, arr.length));
        POOL.invoke(new FilterAction(arr, dotProducts, pointNormal, mask, 0, arr.length));
        int[] maskPrefixSum = ParallelPrefix.prefixSum(mask, CUTOFF);

        double[][] result = new double[maskPrefixSum[maskPrefixSum.length - 1]][point.length];

        POOL.invoke(new PopulateAction(arr, result, mask, maskPrefixSum, 0, arr.length));
        // TODO: Implement to match the filter/pack procedure discussed in class.
        // Reminder: the main steps are:
        // 1) do a map on the arr of "vectors"
        
        // 2) do prefix sum on the map result (implementation provided for you in
        // ParallelPrefix.java)
        // 3) initialize and array whose length matches the last value in the prefix sum
        // result
        // 4) do a map to populate that new array.
        return result;
    }

    private static double dotProduct(double[] a, double[] b) {
        return DotProduct.dotProduct(a, b, SliceFilter.CUTOFF);
    }

    private static boolean approxEquals(double a, double b) {
        final double EPSILON = 1e-5;
        return Math.abs(a - b) < EPSILON;
    }

    private static class DotProductAction extends RecursiveAction {
        private double[][] arr;
        private double[] dotProducts;
        private double[] normal;
        private int start;
        private int end;

        public DotProductAction(double[][] arr, double[] dotProducts, double[] normal, int start, int end) {
            this.arr = arr;
            this.dotProducts = dotProducts;
            this.normal = normal;
            this.start = start;
            this.end = end;
        }

        public void compute() {
            if (end - start <= CUTOFF) {
                for (int i = start; i < end; i++) {
                    dotProducts[i] = dotProduct(arr[i], normal);
                }
            } else {
                int mid = (start + end) / 2;
                DotProductAction left = new DotProductAction(arr, dotProducts, normal, start, mid);
                DotProductAction right = new DotProductAction(arr, dotProducts, normal, mid, end);
                left.fork();
                right.compute();
                left.join();
            }
        }
    }

    private static class FilterAction extends RecursiveAction {
        private double[][] arr;
        private double[] dotProducts;
        private double pointNormal;
        private int[] mask;
        private int start;
        private int end;

        public FilterAction(double[][] arr, double[] dotProducts, double pointNormal, int[] mask, int start, int end) {
            this.arr = arr;
            this.dotProducts = dotProducts;
            this.pointNormal = pointNormal;
            this.mask = mask;
            this.start = start;
            this.end = end;
        }

        public void compute() {
            if (end - start <= CUTOFF) {
                for (int i = start; i < end; i++) {
                    mask[i] = approxEquals(dotProducts[i], pointNormal) ? 1 : 0;
                }
            } else {
                int mid = (start + end) / 2;
                FilterAction left = new FilterAction(arr, dotProducts, pointNormal, mask, start, mid);
                FilterAction right = new FilterAction(arr, dotProducts, pointNormal, mask, mid, end);
                left.fork();
                right.compute();
                left.join();
            }
        }
    }


    private static class PopulateAction extends RecursiveAction {
        private double[][] arr;
        private double[][] result;
        private int[] mask;
        private int[] maskPrefixSum;
        private int start;
        private int end;

        public PopulateAction(double[][] arr, double[][] result, int[] mask, int[] maskPrefixSum, int start, int end) {
            this.arr = arr;
            this.result = result;
            this.mask = mask;
            this.maskPrefixSum = maskPrefixSum;
            this.start = start;
            this.end = end;
        }

        public void compute() {
            if (end - start <= CUTOFF) {
                for (int i = start; i < end; i++) {
                    if (mask[i] == 1) {
                        result[maskPrefixSum[i] - 1] = arr[i];
                    }
                }
            } else {
                int mid = (start + end) / 2;
                PopulateAction left = new PopulateAction(arr, result, mask, maskPrefixSum, start, mid);
                PopulateAction right = new PopulateAction(arr, result, mask, maskPrefixSum, mid, end);
                left.fork();
                right.compute();
                left.join();
            }
        }
    }
}
