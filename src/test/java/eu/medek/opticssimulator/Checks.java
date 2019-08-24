package eu.medek.opticssimulator;

public class Checks {
    private static final double THRESHOLD = 0.00001;

    public static void assertDouble(double expected, double actual) {
        if (Math.abs(expected-actual) < THRESHOLD) assert true;
        else {
            System.out.println("Wrong value!");
            System.out.println("Expected: " + expected + " Actual: " + actual);
            assert false;
        }
    }

    public static void assertVector(Vector expected, Vector actual) {
        assertDouble(expected.x, actual.x);
        assertDouble(expected.y, actual.y);
    }

    public static void assertAngle(double a, double b) {
        double TWO_PI = Math.PI * 2;
        if (a < 0) a = a % TWO_PI + TWO_PI;
        else a = a % TWO_PI;
        if (b < 0) b = b % TWO_PI + TWO_PI;
        else b = b % TWO_PI;
        assertDouble(a, b);
    }
}
