import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final byte BLOCKED = 1;
    private static final byte OPEN = 2;
    private static final byte TOP_CONNECTED = 4;
    private static final byte BOTTOM_CONNECTED = 8;
    private int n;
    private WeightedQuickUnionUF wquuf;
    private byte[] status;
    private boolean percolate;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        } else {
            percolate = false;
            status = new byte[n * n];
            this.n = n;
            wquuf = new WeightedQuickUnionUF(n * n);
            for (int i = 0; i < n * n; i++) {
                status[i] = BLOCKED;
            }
        }
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        checkRange(row, col);
        if (!isOpen(row, col)) {
            status[getIndex(row, col)] = OPEN;
            unionNeighbors(row, col);
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkRange(row, col);
        return (status[getIndex(row, col)] & OPEN) > 0;
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        checkRange(row, col);
        return (status[wquuf.find(getIndex(row, col))] & TOP_CONNECTED) > 0;
    }

    // does the system percolate?
    public boolean percolates() {
        return percolate;
    }

    // test client (optional)
    public static void main(String[] args) {
    }

    private int getIndex(int row, int col) {
        if (row <= 0 || row > n || col <= 0 || col > n) {
            return -1;
        } else {
            return (row - 1) * n + col - 1;
        }
    }

    private void checkRange(int row, int col) {
        if (row <= 0 || row > n || col <= 0 || col > n) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void unionNeighbors(int row, int col) {
        int openIdx = getIndex(row, col);
        byte newStatus = OPEN;

        if (row == 1) {
            newStatus |= TOP_CONNECTED;
        }

        if (row == n) {
            newStatus |= BOTTOM_CONNECTED;
        }

        // upper site
        int neighborIdx = getIndex(row - 1, col);
        if (neighborIdx >= 0 && isOpen(row - 1, col)) {
            newStatus |= status[wquuf.find(neighborIdx)];
            wquuf.union(openIdx, neighborIdx);
        }

        // right site
        neighborIdx = getIndex(row, col + 1);
        if (neighborIdx >= 0 && isOpen(row, col + 1)) {
            newStatus |= status[wquuf.find(neighborIdx)];
            wquuf.union(openIdx, neighborIdx);
        }

        // down site
        neighborIdx = getIndex(row + 1, col);
        if (neighborIdx >= 0 && isOpen(row + 1, col)) {
            newStatus |= status[wquuf.find(neighborIdx)];
            wquuf.union(openIdx, neighborIdx);
        }

        // left site
        neighborIdx = getIndex(row, col - 1);
        if (neighborIdx >= 0 && isOpen(row, col - 1)) {
            newStatus |= status[wquuf.find(neighborIdx)];
            wquuf.union(openIdx, neighborIdx);
        }

        status[wquuf.find(openIdx)] = newStatus;
        if ((newStatus & TOP_CONNECTED) > 0 && (newStatus & BOTTOM_CONNECTED) > 0)
            percolate = true;
    }
}
