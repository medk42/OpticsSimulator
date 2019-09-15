/*
 * OpticsSimulator
 * Jakub Medek, I. ročník, IOI MFF UK
 * letní semestr 2018/19
 * Programování II (NPRG031)
 */

package eu.medek.opticssimulator.logic;

import eu.medek.opticssimulator.logic.rays.Ray;
import org.junit.Test;

import static org.junit.Assert.*;

public class RayTest {

    private static final double THRESHOLD = 0.00001;

    private static boolean isSimilarDouble(double a, double b) {
        return Math.abs(a-b) < THRESHOLD;
    }

    private static boolean isSimilarRay(Ray a, Ray b) {
        return  isSimilarDouble(a.getAngle(), b.getAngle()) &&
                isSimilarDouble(a.getStrength(), b.getStrength()) &&
                isSimilarDouble(a.getPosition().x, b.getPosition().x) &&
                isSimilarDouble(a.getPosition().y, b.getPosition().y);
    }

    @Test
    public void constructors() {
        Ray expected, ray = new Ray(3,4,5,6);
        assertTrue(isSimilarDouble(ray.getPosition().x, 3) && isSimilarDouble(ray.getPosition().y, 4) &&
                isSimilarDouble(ray.getAngle(), 5) && isSimilarDouble(ray.getStrength(), 6));

        ray = new Ray();
        expected = new Ray(0,0,0,1);
        assertTrue(isSimilarRay(ray, expected));

        Vector position = new Vector(1, 2);
        ray = new Ray(position, 3, 4);
        expected = new Ray(1,2,3,4);
        assertTrue(isSimilarRay(ray, expected));
        position.x = 10;
        expected = new Ray(10,2,3,4);
        assertTrue(isSimilarRay(ray, expected));
    }

    @Test
    public void setPosition() {
        Ray ray, expected;

        Vector position = new Vector(1, 2);
        ray = new Ray(0,0, 3, 4);
        expected = new Ray(0,0,3,4);
        assertTrue(isSimilarRay(ray, expected));
        ray.setPosition(position);
        expected = new Ray(1,2,3,4);
        assertTrue(isSimilarRay(ray, expected));

        position.x = 10;
        expected = new Ray(10,2,3,4);
        assertTrue(isSimilarRay(ray, expected));
    }

    @Test
    public void setStrength() {
        Ray ray;

        ray = new Ray(1,2,3,4);

        ray.setStrength(0);
        ray.setStrength(1);

        try {
            ray.setStrength(-0.01);
            assert false;
        } catch (RuntimeException e) {
            assert true;
        }

        try {
            ray.setStrength(1.01);
            assert false;
        } catch (RuntimeException e) {
            assert true;
        }

        double random_num = 0.702835d;
        ray.setStrength(random_num);
        assertTrue(isSimilarDouble(random_num, ray.getStrength()));
    }

    @Test
    public void getPosition() {
        Ray expected, ray;

        ray = new Ray();
        expected = new Ray(0,0,0,1);
        assertTrue(isSimilarRay(ray, expected));

        Vector position = ray.getPosition();
        position.x = 5;
        position.y = 10;
        expected = new Ray(5,10,0,1);
        assertTrue(isSimilarRay(ray, expected));
    }

    @Test
    public void lookAt() {
        double x = 53425.43968d;
        double y = 83290.12348d;
        double angle = 2.23535;
        Ray ray = new Ray(x,y,0,0);
        ray.setAngle(angle);
        double expected = 2.23535;
        assertTrue(isSimilarDouble(expected, ray.getAngle()));


        ray.lookAt(new Vector(x + 1, y - 1));
        expected = 1d/4d*Math.PI;
        assertTrue(isSimilarDouble(expected, ray.getAngle()));
    }
}