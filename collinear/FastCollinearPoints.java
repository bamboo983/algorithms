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
        if (points.length >= MINIMUM_SEGMENT_POINTS) {
            List<LineSegment> segmentsList = new ArrayList<LineSegment>();
            for (int i = 0; i < points.length; i++) {
                Point[] sortPoints = points.clone();
                Arrays.sort(sortPoints, sortPoints[i].slopeOrder());
                List<Point> pointsList = new ArrayList<Point>();
                double slope = sortPoints[0].slopeTo(sortPoints[1]);
                pointsList.add(sortPoints[0]);
                pointsList.add(sortPoints[1]);
                for (int j = 2; j < sortPoints.length; j++) {
                    double adjSlope = sortPoints[0].slopeTo(sortPoints[j]);
                    if (adjSlope == slope) {
                        pointsList.add(sortPoints[j]);
                    } else {
                        checkPointsToSegment(pointsList, segmentsList);
                        pointsList.clear();
                        pointsList.add(sortPoints[0]);
                        pointsList.add(sortPoints[j]);
                        slope = adjSlope;
                    }
                }
                checkPointsToSegment(pointsList, segmentsList);
            }
            segments = segmentsList.toArray(new LineSegment[0]);
        }
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

    private LineSegment getSegment(Point[] points) {
        Arrays.sort(points);
        return new LineSegment(points[0], points[points.length - 1]);
    }

    private boolean checkSegments(List<LineSegment> segmentsList,
        LineSegment segment) {
        for (LineSegment s : segmentsList)
            if (s.toString().equals(segment.toString()))
                return true;
        return false;
    }

    private void checkPointsToSegment(List<Point> pointsList,
                                List<LineSegment> segmentsList) {
        if (pointsList.size() >= MINIMUM_SEGMENT_POINTS) {
            LineSegment segment = getSegment(pointsList.toArray(new Point[0]));
            if (!checkSegments(segmentsList, segment))
                segmentsList.add(segment);
        }
    }

    private boolean checkDuplicatePoints(Point[] points) {
        for (int i = 0; i < points.length; i++)
            for (int j = i + 1; j < points.length; j++)
                if (points[i].slopeTo(points[j]) == Double.NEGATIVE_INFINITY)
                    return true;
        return false;
    }
}
