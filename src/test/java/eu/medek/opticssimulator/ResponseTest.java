package eu.medek.opticssimulator;

import eu.medek.opticssimulator.rays.Ray;
import eu.medek.opticssimulator.reflectables.BlockerLine;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class ResponseTest {

    @Test
    public void createNoImpactResponse() {
        Response response = new Response();
        assertFalse(response.getImpact());
        assertNull(response.getPointOfImpact());
        assertNull(response.getHitObject());
        assertNull(response.getResultingRays());
    }

    @Test
    public void createImpactResponse() {
        Vector pointOfImpact = new Vector(3,4);
        BlockerLine hitObject = new BlockerLine(1,1,2,2);
        List<Ray> resultingRays = new LinkedList<>();
        resultingRays.add(new Ray(1,1,1,1));
        resultingRays.add(new Ray(2,2,2,2));

        Response response = new Response(pointOfImpact, hitObject, resultingRays);

        assertTrue(response.getImpact());
        assertSame(pointOfImpact, response.getPointOfImpact());
        assertSame(hitObject, response.getHitObject());
        assertSame(resultingRays, response.getResultingRays());
    }
}