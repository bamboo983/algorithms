import java.util.Stack;

public class Board {
    private static final int BLANK = 0;
    private int n;
    private int[][] tiles;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        n = blocks.length;
        tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = blocks[i][j];
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        int count = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (tiles[i][j] != BLANK && (tiles[i][j] != i * n + j + 1))
                    count++;
        return count;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int distance = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {

                if (tiles[i][j] != 0) {
                    int row = tiles[i][j] / n;
                    int col = (tiles[i][j] + n - 1) % n;
                    distance += Math.abs((row - i) + (col - j));
                }
            }
        return distance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        Board board = new Board(tiles);

        // swap the first non-zero tile with its adjacency at the same row
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1; j++)
                if (tiles[i][j] != BLANK && tiles[i][j + 1] != 0)
                    board.swap(i, j, i, j + 1);
        }
        return board;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this)
            return true;

        if (y == null)
            return false;

        if (y.getClass() != this.getClass())
            return false;

        Board that = (Board) y;
        if (this.dimension() != that.dimension())
            return false;

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (this.tiles[i][j] != that.tiles[i][j])
                    return false;
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Index index = searchBlankIndex();
        Stack<Board> neighbors = new Stack<Board>();

        // upper tile
        Board board = new Board(tiles);
        boolean isNeighbor = board.swap(index.row, index.col, index.row - 1, index.col);
        if (isNeighbor)
            neighbors.push(board);

        // right tile
        board = new Board(tiles);
        isNeighbor = board.swap(index.row, index.col, index.row, index.col + 1);
        if (isNeighbor)
            neighbors.push(board);

        // down tile
        board = new Board(tiles);
        isNeighbor = board.swap(index.row, index.col, index.row + 1, index.col);
        if (isNeighbor)
            neighbors.push(board);

        // left tile
        board = new Board(tiles);
        isNeighbor = board.swap(index.row, index.col, index.row, index.col - 1);
        if (isNeighbor)
            neighbors.push(board);

        return neighbors;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // unit tests
    public static void main(String[] args) {

    }

    private boolean swap(int srcRow, int srcCol, int dstRow, int dstCol) {
        if (dstRow < 0 || dstRow >= n || dstCol < 0 || dstCol >= n)
            return false;

        int tmp = tiles[srcRow][srcCol];
        tiles[srcRow][srcCol] = tiles[dstRow][dstCol];
        tiles[dstRow][dstCol] = tmp;
        return true;
    }

    private Index searchBlankIndex() {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (tiles[i][j] == BLANK)
                    return new Index(i, j);
        return new Index(0, 0);
    }

    private class Index {
        private int row;
        private int col;

        public Index(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}