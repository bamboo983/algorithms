import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Subset {
    public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        String[] input = StdIn.readAllStrings();
        int k = Integer.parseInt(args[0]);

        StdRandom.shuffle(input);
        for (int i = 0; i < k; i++)
            q.enqueue(input[i]);
        for (int i = 0; i < k; i++)
            StdOut.println(q.dequeue());
    }
}
