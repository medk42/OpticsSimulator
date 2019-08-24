package eu.medek.opticssimulator.reflectables;

import eu.medek.opticssimulator.Ray;
import eu.medek.opticssimulator.Response;
import eu.medek.opticssimulator.Vector;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlockerLineTest {

    private static final double THRESHOLD = 0.00001;

    private static void assertDouble(double expected, double actual) {
        if (Math.abs(expected-actual) < THRESHOLD) assert true;
        else {
            System.out.println("Wrong value!");
            System.out.println("Expected: " + expected + " Actual: " + actual);
            assert false;
        }
    }

    private static void assertVector(Vector expected, Vector actual) {
        assertDouble(expected.x, actual.x);
        assertDouble(expected.y, actual.y);
    }

    @Test
    public void getImpactResult() {
        BlockerLine blocker = new BlockerLine(new Vector(1,1), new Vector(2,2));

        Ray ray = new Ray(1,2,Math.PI/4d,1);
        Response response = blocker.getImpactResult(ray);
        assertTrue(response.getImpact());
        assertSame(blocker, response.getHitObject());
        assertVector(new Vector(1.5d, 1.5d), response.getPointOfImpact());
        assertNotNull(response.getResultingRays());
        assertEquals(0, response.getResultingRays().size());
    }
}