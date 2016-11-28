import java.util.Deque;
import java.util.ArrayDeque;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;

public class Solver {
    private Deque<Board> solution;
    private int moves;
    private boolean isSolvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        solution = new ArrayDeque<Board>();
        MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>();
        MinPQ<SearchNode> minPQTwin = new MinPQ<SearchNode>();
        moves = 0;
        isSolvable = false;

        Board board = initial;
        Board boardTwin = initial.twin();
        SearchNode node = new SearchNode(board, 0, null);
        SearchNode nodeTwin = new SearchNode(boardTwin, 0, null);
        minPQ.insert(node);
        minPQTwin.insert(nodeTwin);

        // boards are divided into two equivalence classes with respect to reachability
        // (i) those that lead to the goal board and
        // (ii) those that lead to the goal board if we modify the initial board by swapping any pair of blocks
        // exactly one of the two will lead to the goal board.
        while (true) {
            node = minPQ.delMin();
            nodeTwin = minPQTwin.delMin();
            board = node.board;
            boardTwin = nodeTwin.board;
            if (board.isGoal()) {
                isSolvable = true;
                solution.push(board);
                while (node.previous != null) {
                    node = node.previous;
                    solution.push(node.board);
                }
                return;
            } else if (boardTwin.isGoal()) {
                isSolvable = false;
                return;
            }

            Iterable<Board> neighbors = board.neighbors();
            for (Board neighbor : neighbors) {
                // reduce unnecessary exploration of useless search nodes
                if (node.previous != null && neighbor.equals(node.previous.board))
                    continue;
                SearchNode newNode = new SearchNode(neighbor, node.moves + 1, node);
                minPQ.insert(newNode);
            }

            Iterable<Board> neighborsTwin = boardTwin.neighbors();
            for (Board neighbor : neighborsTwin) {
                // reduce unnecessary exploration of useless search nodes
                if (nodeTwin.previous != null && neighbor.equals(nodeTwin.previous.board))
                    continue;
                SearchNode newNode = new SearchNode(neighbor, nodeTwin.moves + 1, nodeTwin);
                minPQTwin.insert(newNode);
            }
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return solution.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return isSolvable ? solution : null;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves;
        private int priority;
        private SearchNode previous;

        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.priority = moves + board.manhattan();
            this.previous = previous;
        }

        private int priority() {
            return moves + board.manhattan();
        }

        public int compareTo(SearchNode that) {
            if (this.priority < that.priority)
                return -1;
            if (this.priority > that.priority)
                return +1;
            return 0;
        }
    }
}