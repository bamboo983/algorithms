import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class BruteCollinearPoints {
    private int numberOfSegments;
    private LineSegment[] segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new NullPointerException("the argument is null");
        if (Arrays.asList(points).contains(null))
            throw new NullPointerException("the point is null");
        if (checkDuplicatePoints(points))
            throw new IllegalArgumentException("the argument contains a repeated point");

        numberOfSegments = 0;
        segments = null;
        List<LineSegment> segmentsList = new ArrayList<LineSegment>();
        for (int i = 0; i < points.length - 3; i++)
            for (int j = i + 1; j < points.length - 2; j++)
                for (int k = j + 1; k < points.length - 1; k++)
                    for (int l = k + 1; l < points.length; l++)
                        if (points[i].slopeTo(points[j])
                                == points[i].slopeTo(points[k])
                                && points[i].slopeTo(points[j])
                                == points[i].slopeTo(points[l])) {
                            numberOfSegments++;
                            Point[] segmentPoints = new Point[4];
                            segmentPoints[0] = points[i];
                            segmentPoints[1] = points[j];
                            segmentPoints[2] = points[k];
                            segmentPoints[3] = points[l];
                            segmentsList.add(getSegment(segmentPoints));
                        }
        segments = segmentsList.toArray(new LineSegment[0]);
    }

    // the number of line segments
    public int numberOfSegments() {
        return numberOfSegments;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

    private LineSegment getSegment(Point[] points) {
        Arrays.sort(points);
        return new LineSegment(points[0], points[3]);
    }

    private boolean checkDuplicatePoints(Point[] points) {
        for (int i = 0; i < points.length; i++)
            for (int j = i + 1; j < points.length; j++)
                if (points[i].slopeTo(points[j]) == Double.NEGATIVE_INFINITY)
                    return true;
        return false;
    }
}
