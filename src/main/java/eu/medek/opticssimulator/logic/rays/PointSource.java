package eu.medek.opticssimulator.logic.rays;

import eu.medek.opticssimulator.logic.Vector;

import java.util.LinkedList;
import java.util.List;

public class PointSource implements DensityListener {
    // Variables
    private Vector position;
    private double density;
    private double strength;
    private List<Ray> rays;

    private static final double DEFAULT_DENSITY = .1d;
    private static final double DEFAULT_STRENGTH = .5d;
    private static final double TWO_PI = Math.PI*2;
    private static final double MAGIC_CONSTANT = 100;


    // Constructors
    /**
     * Create a point source shooting rays to all directions with specified density.
     */
    public PointSource(Vector position, double density, double strength) {
        this.position = position;
        this.density = density;
        this.strength = strength;

        updateRays(this.strength);
    }

    public PointSource(double x, double y, double density, double strength) {
        this(new Vector(x, y), density, strength);
    }

    public PointSource(Vector position) {
        this(position, DEFAULT_DENSITY, DEFAULT_STRENGTH);
    }

    public PointSource(double x, double y) {
        this(x, y, DEFAULT_DENSITY, DEFAULT_STRENGTH);
    }

    public PointSource() {
        this(0, 0, DEFAULT_DENSITY, DEFAULT_STRENGTH);
    }


    // Getters/Setters

    /**
     * Warning: also sets the new position to all rays (slow) - use getPosition and set x and y speedup
     */
    public void setPosition(Vector position) {
        this.position = position;
        for (Ray ray : rays) ray.setPosition(position);
    }

    public Vector getPosition() {
        return position;
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
        this.rays = new LinkedList<>();
        double angle = TWO_PI;
        int count = (int) Math.floor(angle * density * MAGIC_CONSTANT);

        angle /= count;

        double actAngle = 0;
        for (int i = 0; i < count; i++, actAngle += angle) {
            rays.add(new Ray(this.position, actAngle, strength));
        }
    }
}
