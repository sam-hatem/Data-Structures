package bearmaps;

//@Source Josh Hug

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class KDTreeTest {
    private static Random q = new Random(500);


    private static void buildTreeDuplicates() {
        Point p1 = new Point(4, 2);
        Point p2 = new Point(4, 2);
        KDTree kd = new KDTree(List.of(p1, p2));
    }

    private static KDTree buildLectureTree() {
        Point p1 = new Point(2, 3); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(4, 2);
        Point p3 = new Point(4, 2);
        Point p4 = new Point(4, 5);
        Point p5 = new Point(3, 3);
        Point p6 = new Point(1, 5);
        Point p7 = new Point(4, 4);

        KDTree kd = new KDTree(List.of(p1, p2, p3, p4, p5, p6, p7));
        return kd;


    }


    @Test
    public void testDemo() {
        KDTree kd = buildLectureTree();
        Point actual = kd.nearest(0, 7);
        Point expected = new Point(1, 5);
        assertEquals(expected, actual);

    }

    //generate random pts

    private Point rndPt() {
        double a = q.nextDouble();
        double b = q.nextDouble();
        return new Point(a, b);
    }

    private List<Point> random(int N) {
        List<Point> rndPts = new ArrayList<>();
        for (int i = 0; i < N; i += 1) {
            rndPts.add(rndPt());
        }
        return rndPts;
    }

    @Test
    public void points1000() {

        List<Point> thousand = random(1000);
        NaivePointSet naive = new NaivePointSet(thousand);
        KDTree kd = new KDTree(thousand);
        List<Point> test150 = random(150);

        for (Point p : test150) {
            Point expected = naive.nearest(p.getX(), p.getY());
            Point actual = kd.nearest(p.getX(), p.getY());
            assertEquals(expected, actual);
        }
    }


    @Test
    public void timewith100000Pointsand10000Queries() {
        List<Point> hundredThousand = random(100000);
        KDTree kd = new KDTree(hundredThousand);
        NaivePointSet naive = new NaivePointSet(hundredThousand);

        List<Point> query = random(10000);


        Stopwatch stopwatch = new Stopwatch();


        for (Point p : query) {
            naive.nearest(p.getX(), p.getY());

        }
        double timePassed = stopwatch.elapsedTime();
        System.out.println("100000 with 10000, naive " + timePassed);


        Stopwatch stopwatch2 = new Stopwatch();


        for (Point p : query) {
            kd.nearest(p.getX(), p.getY());
        }

        double timeKd = stopwatch2.elapsedTime();
        System.out.println("100000 with 10000, KD " + timeKd);
    }
}
