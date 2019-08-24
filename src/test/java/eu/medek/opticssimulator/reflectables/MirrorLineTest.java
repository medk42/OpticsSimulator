package eu.medek.opticssimulator.reflectables;

import eu.medek.opticssimulator.Ray;
import eu.medek.opticssimulator.Response;
import org.junit.Test;

import static eu.medek.opticssimulator.Checks.assertAngle;
import static org.junit.Assert.*;

public class MirrorLineTest {

    @Test
    public void getImpactResult() {
        MirrorLine mirrorLine = new MirrorLine(2,1,2,3);

        Ray ray = new Ray(1,2,Math.PI/3d,1);
        Response response = mirrorLine.getImpactResult(ray);
        assertFalse(response.getImpact());

        ray.setAngle(Math.PI/4d);
        response = mirrorLine.getImpactResult(ray);
        assertTrue(response.getImpact());
        assertNotNull(response.getResultingRays());
        assertEquals(response.getResultingRays().size(), 1);
        assertAngle(response.getResultingRays().get(0).getAngle(), Math.PI*3d/4d);

        ray.setAngle(Math.PI/6d);
        response = mirrorLine.getImpactResult(ray);
        assertTrue(response.getImpact());
        assertNotNull(response.getResultingRays());
        assertEquals(response.getResultingRays().size(), 1);
        assertAngle(response.getResultingRays().get(0).getAngle(), Math.PI*5d/6d);



        mirrorLine = new MirrorLine(2,3,2,1);

        ray = new Ray(1,2,Math.PI/3d,1);
        response = mirrorLine.getImpactResult(ray);
        assertFalse(response.getImpact());

        ray.setAngle(Math.PI/4d);
        response = mirrorLine.getImpactResult(ray);
        assertTrue(response.getImpact());
        assertNotNull(response.getResultingRays());
        assertEquals(response.getResultingRays().size(), 1);
        assertAngle(response.getResultingRays().get(0).getAngle(), Math.PI*3d/4d);

        ray.setAngle(Math.PI/6d);
        response = mirrorLine.getImpactResult(ray);
        assertTrue(response.getImpact());
        assertNotNull(response.getResultingRays());
        assertEquals(response.getResultingRays().size(), 1);
        assertAngle(response.getResultingRays().get(0).getAngle(), Math.PI*5d/6d);



        ray = new Ray(3,2,Math.PI*2d/3d,1);
        response = mirrorLine.getImpactResult(ray);
        assertFalse(response.getImpact());

        ray.setAngle(Math.PI*3d/4d);
        response = mirrorLine.getImpactResult(ray);
        assertTrue(response.getImpact());
        assertNotNull(response.getResultingRays());
        assertEquals(response.getResultingRays().size(), 1);
        assertAngle(response.getResultingRays().get(0).getAngle(), Math.PI*1d/4d);

        ray.setAngle(Math.PI*5d/6d);
        response = mirrorLine.getImpactResult(ray);
        assertTrue(response.getImpact());
        assertNotNull(response.getResultingRays());
        assertEquals(response.getResultingRays().size(), 1);
        assertAngle(response.getResultingRays().get(0).getAngle(), Math.PI*1d/6d);



        mirrorLine = new MirrorLine(1,1,2,2);

        ray = new Ray(1,2,Math.PI/2d,1);
        response = mirrorLine.getImpactResult(ray);
        assertTrue(response.getImpact());
        assertNotNull(response.getResultingRays());
        assertEquals(response.getResultingRays().size(), 1);
        assertAngle(response.getResultingRays().get(0).getAngle(), Math.PI);

        ray = new Ray(1,2,Math.PI/4d,1);
        response = mirrorLine.getImpactResult(ray);
        assertTrue(response.getImpact());
        assertNotNull(response.getResultingRays());
        assertEquals(response.getResultingRays().size(), 1);
        assertAngle(response.getResultingRays().get(0).getAngle(), Math.PI*5d/4d);

        ray = new Ray(1,2,0,1);
        response = mirrorLine.getImpactResult(ray);
        assertTrue(response.getImpact());
        assertNotNull(response.getResultingRays());
        assertEquals(response.getResultingRays().size(), 1);
        assertAngle(response.getResultingRays().get(0).getAngle(), -Math.PI/2d);

        ray = new Ray(1,2,Math.PI/3d,1);
        response = mirrorLine.getImpactResult(ray);
        assertTrue(response.getImpact());
        assertNotNull(response.getResultingRays());
        assertEquals(response.getResultingRays().size(), 1);
        assertAngle(response.getResultingRays().get(0).getAngle(), Math.PI*7d/6d);

    }
}