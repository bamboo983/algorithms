import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class FastCollinearPoints {
    private static final int MINIMUM_SEGMENT_POINTS = 4;
    private LineSegment[] segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new NullPointerException("the argument is null");
        if (Arrays.asList(points).contains(null))
            throw new NullPointerException("the point is null");
        if (checkDuplicatePoints(points))
            throw new IllegalArgumentException("the argument contains a repeated point");

        segments = new LineSegment[0];
        List<LineSegment> segmentsList = new ArrayList<LineSegment>();
        Arrays.sort(points);
        for (int i = 0; i < points.length; i++) {
            Point[] aux = points.clone();
            Arrays.sort(aux, i + 1, aux.length,  aux[i].slopeOrder());

            List<Double> leftSlopes = new ArrayList<Double>();
            List<Double> rightSlopes = new ArrayList<Double>();
            for (int j = 0; j < aux.length; j++) {
                if (j < i)
                    leftSlopes.add(aux[i].slopeTo(aux[j]));
                else if (j > i)
                    rightSlopes.add(aux[i].slopeTo(aux[j]));
            }

            Double current = Double.NEGATIVE_INFINITY;
            int count = 0;
            for (int j = 0; j < rightSlopes.size(); j++) {
                if (Double.compare(rightSlopes.get(j), current) != 0) {
                    if (count >= MINIMUM_SEGMENT_POINTS - 1) {
                        if (!leftSlopes.contains(current))
                            segmentsList.add(new LineSegment(aux[i],
                                                    aux[i + j]));
                    }
                    current = rightSlopes.get(j);
                    count = 1;
                } else {
                    count++;
                }
            }
            if (count >= MINIMUM_SEGMENT_POINTS - 1) {
                if (!leftSlopes.contains(current))
                    segmentsList.add(new LineSegment(aux[i],
                                            aux[i + rightSlopes.size()]));
            }
        }
        segments = segmentsList.toArray(new LineSegment[0]);
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.clone();
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

    private boolean checkDuplicatePoints(Point[] points) {
        for (int i = 0; i < points.length; i++)
            for (int j = i + 1; j < points.length; j++)
                if (points[i].slopeTo(points[j]) == Double.NEGATIVE_INFINITY)
                    return true;
        return false;
    }
}
