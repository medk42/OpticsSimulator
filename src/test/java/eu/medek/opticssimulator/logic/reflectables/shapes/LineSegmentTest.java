package eu.medek.opticssimulator.logic.reflectables.shapes;

import eu.medek.opticssimulator.logic.rays.Ray;
import eu.medek.opticssimulator.logic.Vector;
import eu.medek.opticssimulator.logic.reflectables.BlockerLine;
import org.junit.Test;

import static org.junit.Assert.*;

public class LineSegmentTest {

    private static final double THRESHOLD = 0.01;

    private static boolean isSimilarDouble(double a, double b) {
        return Math.abs(a-b) < THRESHOLD;
    }

    private static boolean isSimilarVector(Vector u, Vector v) {
        return isSimilarDouble(u.x, v.x) && isSimilarDouble(u.y, v.y);
    }

    @Test
    public void getIntersection() {
        LineSegment segment = new BlockerLine(1,1,2,2);

        Ray ray = new Ray(1,2,Math.PI/2d,1);
        Vector intersection = segment.getIntersection(ray);
        Vector expected = new Vector(1,1);
        assertTrue(isSimilarVector(intersection, expected));

        ray.setAngle(0);
        intersection = segment.getIntersection(ray);
        expected = new Vector(2,2);
        assertTrue(isSimilarVector(intersection, expected));

        ray.setAngle(Math.PI/4d);
        intersection = segment.getIntersection(ray);
        expected = new Vector(1.5d,1.5d);
        assertTrue(isSimilarVector(intersection, expected));

        ray.setAngle(Math.PI/8d);
        intersection = segment.getIntersection(ray);
        expected = new Vector(1.71d,1.71d);
        assertTrue(isSimilarVector(intersection, expected));

        ray.setAngle(Math.PI*3d/8d);
        intersection = segment.getIntersection(ray);
        expected = new Vector(1.29d,1.29d);
        assertTrue(isSimilarVector(intersection, expected));
    }
}