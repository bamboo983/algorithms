import java.util.NoSuchElementException;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private int N;

    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[]) new Object[1];
        N = 0;
    }

    // is the queue empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the number of items on the queue
    public int size() {
        return N;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new NullPointerException("attempts to add a null item");
        if (N == queue.length)
            resize(2 * queue.length);
        queue[N++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();
        Item item;
        int i = StdRandom.uniform(N);
        item = queue[i];
        queue[i] = queue[--N];
        queue[N] = null;
        if (N > 0 && N == queue.length / 4)
            resize(queue.length / 2);
        return item;
    }

    // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException();
        return queue[StdRandom.uniform(N)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {
        private int current = 0;
        private int[] shuffledIdx = new int[N];

        public boolean hasNext() {
            if (current == 0)
                shuffle();
            return current < N;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (isEmpty() || current == N)
                throw new NoSuchElementException("there are no more items to return");
            if (current == 0)
                shuffle();
            return queue[shuffledIdx[current++]];
        }

        private void shuffle() {
            for (int i = 0; i < N; i++)
                shuffledIdx[i] = i;
            StdRandom.shuffle(shuffledIdx);
        }
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++)
            copy[i] = queue[i];
        queue = copy;
    }

    // unit testing
    public static void main(String[] args) {
    }
}
