import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class MatrixMultiply {
    static ForkJoinPool POOL = ForkJoinPool.commonPool();
    static int CUTOFF;

    // Behavior should match Sequential.multiply.
    // Ignoring the initialization of arrays, your implementation should have n^3
    // work and log(n) span
    public static double[][] multiply(double[][] a, double[][] b, int cutoff) {
        MatrixMultiply.CUTOFF = cutoff;
        double[][] product = new double[a.length][b[0].length];
        POOL.invoke(new MatrixMultiplyAction(a, b, product, 0, product.length, 0, product[0].length)); // TODO: add parameters to match your constructor
        return product;
    }

    // Behavior should match the 2d version of Sequential.dotProduct.
    // Your implementation must have linear work and log(n) span
    public static double dotProduct(double[][] a, double[][] b, int row, int col, int cutoff) {
        MatrixMultiply.CUTOFF = cutoff;
        return POOL.invoke(new DotProductTask(a, b, row, col, 0,  a.length)); // TODO: add parameters to match your constructor
    }

    private static class MatrixMultiplyAction extends RecursiveAction {
        private double[][] A;
        private double[][] B;
        private double[][] product;
        private int rowStart;
        private int rowEnd;
        private int colStart;
        private int colEnd;

        public MatrixMultiplyAction(double[][] A, double[][] B, double[][] product, int rowStart, int rowEnd, int colStart, int colEnd) {
            this.A = A;
            this.B = B;
            this.product = product;
            this.rowStart = rowStart;
            this.rowEnd = rowEnd;
            this.colStart = colStart;
            this.colEnd = colEnd;
        }

        public void compute() {
            if (colEnd - colStart <= CUTOFF || rowEnd - rowStart <= CUTOFF ) {
                for (int row = rowStart; row < rowEnd; row++) {
                    for (int col = colStart; col < colEnd; col++) {
                        product[row][col] = dotProduct(A, B, row, col, CUTOFF);
                    }
                }
                return;
            }

            int rowMid = (rowStart + rowEnd) / 2;
            int colMid = (colStart + colEnd) / 2;

            MatrixMultiplyAction tl =  new MatrixMultiplyAction(A, B, product, rowStart, rowMid, colStart, colMid);
            MatrixMultiplyAction tr =  new MatrixMultiplyAction(A, B, product, rowStart, rowMid, colMid, colEnd);
            MatrixMultiplyAction bl =  new MatrixMultiplyAction(A, B, product, rowMid, rowEnd, colStart, colMid);
            MatrixMultiplyAction br =  new MatrixMultiplyAction(A, B, product, rowMid, rowEnd, colMid, colEnd);

            tl.fork();
            tr.fork();
            bl.fork();
            br.compute();
            tl.join();
            tr.join();
            bl.join();


        }

    }

    private static class DotProductTask extends RecursiveTask<Double> {
        private double[][] A;
        private double[][] B;
        private int row;
        private int col;
        private int start;
        private int end;

        public DotProductTask(double[][] A, double[][] B, int row, int col, int start, int end){
            this.A = A;
            this.B = B;
            this.row = row;
            this.col = col;
            this.start = start;
            this.end = end;
        }

        public Double compute() {
            if (end - start <= CUTOFF) {
                double sum = 0.0;
                for (int i = start; i < end; i++) {
                    sum += A[row][i] * B[i][col];
                }
                return sum;
            } else {
                int mid = (start + end) / 2;
                DotProductTask leftTask = new DotProductTask(A, B, row, col, start, mid);
                DotProductTask rightTask = new DotProductTask(A, B, row, col, mid, end);
                leftTask.fork();
                double rightResult = rightTask.compute();
                double leftResult = leftTask.join();
                return leftResult + rightResult;
            }
        }
    }
}
