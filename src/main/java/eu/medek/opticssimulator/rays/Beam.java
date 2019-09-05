package eu.medek.opticssimulator.rays;

import eu.medek.opticssimulator.Vector;

import java.util.LinkedList;
import java.util.List;

public class Beam implements DensityListener {

    // Variables
    private Vector pointA, pointB;
    private double density;
    private double strength;
    private List<Ray> rays;

    private static final double DEFAULT_DENSITY = .1d;
    private static final double DEFAULT_STRENGTH = .5d;
    private static final double HALF_PI = Math.PI/2;


    // Constructors
    /**
     * Create a beam shooting rays to the right of the vector pointA -> pointB with specified density.
     */
    public Beam(Vector pointA, Vector pointB, double density, double strength) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.density = density;
        this.strength = strength;

        updateRays(this.strength);
    }

    public Beam(double x1, double y1, double x2, double y2, double density, double strength) {
        this(new Vector(x1, y1), new Vector(x2, y2), density, strength);
    }

    public Beam(Vector pointA, Vector pointB) {
        this(pointA, pointB, DEFAULT_DENSITY, DEFAULT_STRENGTH);
    }

    public Beam(double x1, double y1, double x2, double y2) {
        this(x1, y1, x2, y2, DEFAULT_DENSITY, DEFAULT_STRENGTH);
    }

    public Beam() {
        this(0, 0, 0, 0, DEFAULT_DENSITY, DEFAULT_STRENGTH);
    }


    // Getters/Setters
    public void setPointA(Vector pointA) {
        this.pointA = pointA;
        updateRays(this.strength);
    }

    public void setPointB(Vector pointB) {
        this.pointB = pointB;
        updateRays(this.strength);
    }

    public Vector getPointA() {
        return new Vector(pointA);
    }

    public Vector getPointB() {
        return new Vector(pointB);
    }

    public double getDensity() {
        return density;
    }

    public List<Ray> getRays() {
        return rays;
    }

    public void setDensity(double density) {
        this.density = density;
        updateRays(this.strength);
    }

    public void setStrength(double strength) {
        this.strength = strength;
        for (Ray ray : rays) ray.setStrength(strength);
    }


    // Methods
    private void updateRays(double strength) {
        //TODO: POSSIBLE FUTURE STEPS - improve memory by using already existing rays

        this.rays = new LinkedList<>();
        double dist = Vector.dist(pointA, pointB);
        int count = (int) Math.floor(dist * density);

        Vector delta = Vector.sub(pointB, pointA);
        double angle = delta.heading();

        delta.div(count-1);
        Vector actPos = new Vector(pointA);
        for (int i = 0; i < count; i++, actPos.add(delta)) {
            rays.add(new Ray(new Vector(actPos), angle - HALF_PI, strength));
        }
    }
}
