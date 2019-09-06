package eu.medek.opticssimulator.logic.reflectables;

import eu.medek.opticssimulator.logic.rays.Ray;
import eu.medek.opticssimulator.logic.Response;
import eu.medek.opticssimulator.logic.Vector;
import org.junit.Test;

import static eu.medek.opticssimulator.Checks.assertVector;
import static org.junit.Assert.*;

public class BlockerLineTest {

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