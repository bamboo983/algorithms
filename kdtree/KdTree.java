import java.util.Stack;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private int size;
    private Node root;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }

    /**
     * Creates an empty 2d-tree of points.
     */
    public KdTree() {
        size = 0;
        root = null;
    }

    /**
     * Returns true if the 2d-tree is empty.
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the number of points in the set
     */
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        root = insert(root, p, 0.0, 0.0, 1.0, 1.0, true);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return contains(root, p, true);
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, true);
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        Stack<Point2D> s = new Stack<Point2D>();
        range(root, rect, s);
        return s;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        return nearest(root, p, root.p, true);
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }

    private Node insert(Node node, Point2D p, double x0, double y0,
                        double x1, double y1, boolean xcmp) {
        if (node == null) {
            size++;
            RectHV rect = new RectHV(x0, y0, x1, y1);
            return new Node(p, rect);
        }
        if (node.p.equals(p))
            return node;
        if (xcmp) {
            double cmp = p.x() - node.p.x();
            if (cmp < 0)
                node.lb = insert(node.lb, p, x0, y0, node.p.x(), y1, !xcmp);
            else
                node.rt = insert(node.rt, p, node.p.x(), y0, x1, y1, !xcmp);
        } else {
            double cmp = p.y() - node.p.y();
            if (cmp < 0)
                node.lb = insert(node.lb, p, x0, y0, x1, node.p.y(), !xcmp);
            else
                node.rt = insert(node.rt, p, x0, node.p.y(), x1, y1, !xcmp);
        }
        return node;
    }

    private boolean contains(Node node, Point2D p, boolean xcmp) {
        if (node == null)
            return false;
        if (node.p.equals(p))
            return true;
        double cmp = xcmp ? p.x() - node.p.x() : p.y() - node.p.y();
        if (cmp < 0)
            return contains(node.lb, p, !xcmp);
        else
            return contains(node.rt, p, !xcmp);
    }

    private void draw(Node node, boolean vertical) {
        if (node == null)
            return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.p.draw();
        if (vertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
        }
        draw(node.lb, !vertical);
        draw(node.rt, !vertical);
    }

    private void range(Node node, RectHV rect, Stack<Point2D> s) {
        if (node == null)
            return;
        if (rect.contains(node.p))
            s.push(node.p);
        if (rect.intersects(node.rect)) {
            range(node.lb, rect, s);
            range(node.rt, rect, s);
        }
    }

    private Point2D nearest(Node node, Point2D p,
                            Point2D cp, boolean xcmp) {
        Point2D closest = cp;
        if (node == null)
            return closest;
        if (node.p.distanceSquaredTo(p) < closest.distanceSquaredTo(p))
            closest = node.p;
        if (node.rect.distanceSquaredTo(p) < closest.distanceSquaredTo(p)) {
            Node sameSide, otherSide;
            double cmp = xcmp ? p.x() - node.p.x() : p.y() - node.p.y();
            if (cmp < 0) {
                sameSide = node.lb;
                otherSide = node.rt;
            } else {
                sameSide = node.rt;
                otherSide = node.lb;
            }
            closest = nearest(sameSide, p, closest, !xcmp);
            closest = nearest(otherSide, p, closest, !xcmp);
        }
        return closest;
    }
}
