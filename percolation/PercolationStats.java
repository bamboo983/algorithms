import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        } else {
            Percolation perc;
            int[] threshold = new int[trials];

            // start percolation experiments
            for (int i = 0; i < trials; i++) {
                perc = new Percolation(n);
                threshold[i] = run(perc, n);
            }

            // calculate the statistics result
            mean = StdStats.mean(threshold) / (n * n);
            stddev = StdStats.stddev(threshold) / (n * n);
            double confidence = (1.96 * stddev) / Math.sqrt(trials);
            confidenceLo = mean - confidence;
            confidenceHi = mean + confidence;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }
    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }

    // test client (described below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats p = new PercolationStats(n, trials);

        StdOut.println("mean                    = " + p.mean());
        StdOut.println("stddev                  = " + p.stddev());
        StdOut.println("95% confidence interval = "
            + p.confidenceLo() + ", " + p.confidenceHi());
   }

   private int run(Percolation perc, int n) {
       int count = 0;
       int row, col;

       while (!perc.percolates()) {
           row = StdRandom.uniform(n) + 1;
           col = StdRandom.uniform(n) + 1;
           if (!perc.isOpen(row, col)) {
               perc.open(row, col);
               count++;
           }
       }
       return count;
   }
}
