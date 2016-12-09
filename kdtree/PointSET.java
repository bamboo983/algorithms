import java.util.Stack;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
    private SET<Point2D> pointSET;

    // construct an empty set of points
    public PointSET() {
        pointSET = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSET.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointSET.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        pointSET.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return pointSET.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : pointSET)
            p.draw();
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        Stack<Point2D> inRectPoints = new Stack<Point2D>();
        for (Point2D p : pointSET)
            if (rect.contains(p))
                inRectPoints.push(p);
        return inRectPoints;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        Point2D nearestPoint = null;
        double nearestDist = 2;
        for (Point2D point : pointSET) {
            if (nearestPoint == null) {
                nearestPoint = point;
                nearestDist = p.distanceTo(point);
            } else {
                double dist = p.distanceTo(point);
                if (dist < nearestDist) {
                    nearestPoint = point;
                    nearestDist = dist;
                }
            }
        }
        return nearestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
    }
}
