/*
 * OpticsSimulator
 * Jakub Medek, I. ročník, IOI MFF UK
 * letní semestr 2018/19
 * Programování II (NPRG031)
 */

package eu.medek.opticssimulator.logic;

import org.junit.Test;

import static org.junit.Assert.*;

public class VectorTest {

    private static final double THRESHOLD = 0.00001;

    private static boolean isSimilar(double a, double b) {
        return Math.abs(a-b) < THRESHOLD;
    }

    @Test
    public void Vector() {
        Vector v = new Vector();
        Vector expected = new Vector(0,0);
        assertTrue("expected: " + expected + " actual" + v, isSimilar(v.x, expected.x) && isSimilar(v.y, expected.y));
    }

    @Test
    public void add() {
        Vector v = new Vector(5,2);
        v.add(new Vector(-7,4));
        Vector expected = new Vector(-2,6);
        assertTrue(isSimilar(expected.x, v.x) && isSimilar(expected.y, v.y));
    }

    @Test
    public void sub() {
        Vector v = new Vector(5,2);
        v.sub(new Vector(-7,4));
        Vector expected = new Vector(12,-2);
        assertTrue(isSimilar(expected.x, v.x) && isSimilar(expected.y, v.y));
    }

    @Test
    public void mult() {
        Vector v = new Vector(10,5);
        v.mult(0.1);
        Vector expected = new Vector(1,0.5);
        assertTrue(isSimilar(expected.x, v.x) && isSimilar(expected.y, v.y));

        v = new Vector(10,5);
        v.mult(0);
        expected = new Vector(0,0);
        assertTrue(isSimilar(expected.x, v.x) && isSimilar(expected.y, v.y));

        v = new Vector(10,5);
        v.mult(-1);
        expected = new Vector(-10,-5);
        assertTrue(isSimilar(expected.x, v.x) && isSimilar(expected.y, v.y));

        v = new Vector(10,5);
        v.mult(2);
        expected = new Vector(20,10);
        assertTrue(isSimilar(expected.x, v.x) && isSimilar(expected.y, v.y));
    }

    @Test
    public void div() {
        Vector v = new Vector(10,5);
        v.div(0.1);
        Vector expected = new Vector(100,50);
        assertTrue(isSimilar(expected.x, v.x) && isSimilar(expected.y, v.y));

        v = new Vector(10,5);
        v.div(2);
        expected = new Vector(5,2.5);
        assertTrue(isSimilar(expected.x, v.x) && isSimilar(expected.y, v.y));

        v = new Vector(10,5);
        v.div(1);
        expected = new Vector(10,5);
        assertTrue(isSimilar(expected.x, v.x) && isSimilar(expected.y, v.y));

        v = new Vector(10,5);
        v.div(0);
        assertTrue(Double.isInfinite(v.x) && Double.isInfinite(v.y));
    }

    @Test
    public void mag() {
        Vector v = new Vector(3,4);
        double expected = 5;
        assertTrue(isSimilar(v.mag(), expected));

    }

    @Test
    public void normalize() {
        Vector v = new Vector(3,4);
        v.normalize();
        Vector expected = new Vector(3d/5d,4d/5d);
        assertTrue("expected: " + expected + " actual: " + v, isSimilar(expected.x, v.x) && isSimilar(expected.y, v.y));
    }

    @Test
    public void heading() {
        Vector v = new Vector(1,1);
        double expected = -1d/4d*Math.PI;
        assertTrue(v.heading() + " != " + expected,isSimilar(v.heading(), expected));

        v = new Vector(1,0);
        expected = 0;
        assertTrue(v.heading() + " != " + expected,isSimilar(v.heading(), expected));

        v = new Vector(1,-1);
        expected = Math.PI/4d;
        assertTrue(v.heading() + " != " + expected,isSimilar(v.heading(), expected));

        v = new Vector(0,-1);
        expected = Math.PI/2d;
        assertTrue(v.heading() + " != " + expected,isSimilar(v.heading(), expected));

        v = new Vector(-1,-1);
        expected = 3d/4d*Math.PI;
        assertTrue(v.heading() + " != " + expected,isSimilar(v.heading(), expected));

        v = new Vector(-1,0);
        expected = -Math.PI;
        assertTrue(v.heading() + " != " + expected,isSimilar(v.heading(), expected));

        v = new Vector(-1,1);
        expected = -3d/4d*Math.PI;
        assertTrue(v.heading() + " != " + expected,isSimilar(v.heading(), expected));

        v = new Vector(0,1);
        expected = -1d/2d*Math.PI;
        assertTrue(v.heading() + " != " + expected,isSimilar(v.heading(), expected));
    }

    @Test
    public void rotate() {
        Vector v = new Vector(1,-1).rotate(Math.PI/2d);
        double expected = 3d/4d*Math.PI;
        assertTrue(v.toString() + " heading: " + v.heading(), isSimilar(v.heading(), expected));

        v = new Vector(1,-1).rotate(-Math.PI/2d);
        expected = -1d/4d*Math.PI;
        assertTrue(v.toString() + " heading: " + v.heading(), isSimilar(v.heading(), expected));

        v = new Vector(1,-1).rotate(-Math.PI/2d).rotate(Math.PI/4d).rotate(Math.PI/4d);
        expected = 1d/4d*Math.PI;
        assertTrue(v.toString() + " heading: " + v.heading(), isSimilar(v.heading(), expected));

        v = new Vector(1,-1).rotate(-Math.PI);
        expected = -3d/4d*Math.PI;
        assertTrue(v.toString() + " heading: " + v.heading(), isSimilar(v.heading(), expected));

        v = new Vector(5,2);
        double expectedHeading = v.heading() + Math.PI/3d;
        double expectedMag = v.mag();
        v.rotate(Math.PI/3d);
        assertTrue(v.toString() + " heading: " + v.heading(), isSimilar(v.heading(), expectedHeading)
                && isSimilar(v.mag(), expectedMag));
    }

    @Test
    public void addStatic() {
        Vector res = Vector.add(new Vector(2,5), new Vector(-4,8));
        Vector expected = new Vector(-2,13);
        assertTrue(isSimilar(expected.x, res.x) && isSimilar(expected.y, res.y));
    }

    @Test
    public void subStatic() {
        Vector res = Vector.sub(new Vector(2,5), new Vector(-4,8));
        Vector expected = new Vector(6,-3);
        assertTrue(isSimilar(expected.x, res.x) && isSimilar(expected.y, res.y));
    }

    @Test
    public void fromAngleStatic() {
        Vector res = Vector.fromAngle(Math.PI/4d);
        Vector expected = new Vector(1,-1).normalize();
        assertTrue(res + " : " + expected, isSimilar(expected.x, res.x) && isSimilar(expected.y, res.y));

        res = Vector.fromAngle(0);
        expected = new Vector(1,0).normalize();
        assertTrue(res + " : " + expected, isSimilar(expected.x, res.x) && isSimilar(expected.y, res.y));

        res = Vector.fromAngle(Math.PI/2d);
        expected = new Vector(0,-1).normalize();
        assertTrue(res + " : " + expected, isSimilar(expected.x, res.x) && isSimilar(expected.y, res.y));

        res = Vector.fromAngle(5d/4d*Math.PI);
        expected = new Vector(-1,1).normalize();
        assertTrue(res + " : " + expected, isSimilar(expected.x, res.x) && isSimilar(expected.y, res.y));

        res = Vector.fromAngle(7d/4d*Math.PI);
        expected = new Vector(1,1).normalize();
        assertTrue(res + " : " + expected, isSimilar(expected.x, res.x) && isSimilar(expected.y, res.y));
    }

    @Test
    public void distStatic() {
        Vector a = new Vector(3,5);
        Vector b = new Vector(6,9);
        double expected = 5;
        assertTrue(isSimilar(Vector.dist(a,b), expected));
        assertTrue(isSimilar(Vector.dist(b,a), expected));
    }
}