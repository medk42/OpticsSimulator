/*
 * OpticsSimulator
 * Jakub Medek, I. ročník, IOI MFF UK
 * letní semestr 2018/19
 * Programování II (NPRG031)
 */

package eu.medek.opticssimulator.logic.reflectables.shapes;

import eu.medek.opticssimulator.logic.rays.Ray;
import eu.medek.opticssimulator.logic.Vector;
import eu.medek.opticssimulator.logic.reflectables.Reflactable;

public abstract class Circle implements Reflactable {

    // Variables
    final protected Vector center;
    protected double radius;

    private static final double MIN_LIMIT = .00001d;


    // Constructors
    /**
     * Create a new Circle using center vector passed as references and radius
     */
    protected Circle(Vector center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    /**
     * Create a new Circle with specified center coordinates and radius.
     */
    protected Circle(double x, double y, double radius) {
        this(new Vector(x, y), radius);
    }


    // Getters/Setters
    public Vector getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }


    // Methods
    /**
     * Get the point of intersection between the ray and this object using circle-line intersection (http://mathworld.wolfram.com/Circle-LineIntersection.html)
     * @param ray ray to be used
     * @return Point of intersection or null if there isn't one.
     */
    @Override
    public Vector getIntersection(Ray ray) {
        Vector delta = Vector.fromAngle(ray.getAngle());
        Vector rayRelativeStart = Vector.sub(ray.getPosition(), center);
        Vector rayRelativeEnd = Vector.add(rayRelativeStart, delta);

        double deltaRadius = delta.mag();
        double D = rayRelativeStart.x*rayRelativeEnd.y - rayRelativeEnd.x*rayRelativeStart.y;

        double discriminant = square(radius) * square(deltaRadius) - square(D);

        // If there is an intersection, find the point
        if (discriminant > 0) {
            // Some fancy math to find the two points of intersection
            double discriminantSqrt = Math.sqrt(discriminant);
            double deltaRadiusSquared = square(deltaRadius);

            double topX1 = D * delta.y;
            double topX2 = sign(delta.y) * delta.x * discriminantSqrt;
            double topY1 = -D * delta.x;
            double topY2 = Math.abs(delta.y) * discriminantSqrt;

            double x1 = (topX1 + topX2) / deltaRadiusSquared;
            double x2 = (topX1 - topX2) / deltaRadiusSquared;
            double y1 = (topY1 + topY2) / deltaRadiusSquared;
            double y2 = (topY1 - topY2) / deltaRadiusSquared;

            // Here are the resulting points of intersection
            Vector p1 = new Vector(x1, y1);
            Vector p2 = new Vector(x2, y2);

            Vector p1Delta = Vector.sub(p1, rayRelativeStart);
            Vector p2Delta = Vector.sub(p2, rayRelativeStart);

            p1.add(center);
            p2.add(center);

            // Only keep the closer one which is in front of the ray
            if (sign(p1Delta.x) != sign(delta.x) && Math.abs(delta.x) > MIN_LIMIT) p1 = null;
            if (sign(p1Delta.y) != sign(delta.y) && Math.abs(delta.y) > MIN_LIMIT) p1 = null;

            if (sign(p2Delta.x) != sign(delta.x) && Math.abs(delta.x) > MIN_LIMIT) p2 = null;
            if (sign(p2Delta.y) != sign(delta.y) && Math.abs(delta.y) > MIN_LIMIT) p2 = null;

            if (p1Delta.mag() <= MIN_LIMIT) p1 = null; // Might be problems with MIN_LIMIT if zoomed too close
            if (p2Delta.mag() <= MIN_LIMIT) p2 = null;

            if (p1 != null && p2 != null) {
                if (p1Delta.mag() < p2Delta.mag()) p2 = null;
                else p1 = null;
            }

            // Return result
            if (p1 != null) return p1;
            if (p2 != null) return p2;
        }

        // If there is no intersection, return null
        return null;
    }

    private double square(double num) {
        return num * num;
    }

    private int sign(double val) {
        return (val >= 0)?1:-1;
    }
}
