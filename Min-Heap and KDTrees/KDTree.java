package bearmaps;

import java.util.List;


//@Source josh Hug
public class KDTree implements PointSet {

    private static final boolean HORIZONTAL = false;
    private static final boolean VERTICAL = true;
    private Node root;

    public KDTree(List<Point> points) {
        for (Point p : points) {
            root = add(p, root, HORIZONTAL);
        }

    }

    @Override
    public Point nearest(double x, double y) {
        Node best = root;
        Point goal = new Point(x, y);


        return nearest(root, goal, best, true).p;


    }

    private Node nearest(Node n, Point goal, Node best, boolean orien) {


        if (n == null) {
            return best;
        }
        if (Point.distance(n.p, goal) < Point.distance(best.p, goal)) {
            best = n;
        }

        double toPartition = compare(goal, n, orien);

        if (toPartition < 0) {
            best = nearest(n.leftChild, goal, best, !orien);

            if (Point.distance(best.p, goal) >= toPartition * toPartition) {
                best = nearest(n.rightChild, goal, best, !orien);
            }
        } else {
            best = nearest(n.rightChild, goal, best, !orien);

            if (Point.distance(best.p, goal) >= toPartition * toPartition) {
                best = nearest(n.leftChild, goal, best, !orien);
            }
        }


        return best;
    }

    private double compare(Point p, Node n, boolean orien) {
        if (orien) {
            return p.getX() - n.p.getX();
        } else {
            return p.getY() - n.p.getY();
        }

    }

    private Node add(Point p, Node n, boolean orientation) {
        if (n == null) {
            return new Node(p, orientation);
        }
        if (p.equals(n.p)) {
            return n;
        }

        int compare = comparePoints(p, n.p, orientation);
        if (compare < 0) {
            n.leftChild = add(p, n.leftChild, !orientation);

        } else if (compare >= 0) {
            n.rightChild = add(p, n.rightChild, !orientation);
        }
        return n;

    }

    private int comparePoints(Point p1, Point p2, boolean orientation) {
        if (orientation == HORIZONTAL) {
            return Double.compare(p1.getX(), p2.getX());
        } else {
            return Double.compare(p1.getY(), p2.getY());
        }

    }

    private class Node {
        private Point p;
        private Node leftChild; //also refers to  down  child
        private Node rightChild; //also refers to up child
        private boolean orientation;

        Node(Point givenP, boolean orien) {
            p = givenP;
            orientation = orien;


        }

    }


}
