package eu.medek.opticssimulator.reflectables.shapes;

import eu.medek.opticssimulator.rays.Ray;
import eu.medek.opticssimulator.Vector;
import eu.medek.opticssimulator.reflectables.Reflactable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class Circle implements Reflactable {

    // Variables
    final protected Vector center;
    protected double radius;


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
//        Vector rayPosition = ray.getPosition();
//        Vector headingVector = Vector.fromAngle(ray.getAngle()).add(rayPosition);
//
//        double x1 = rayPosition.x;
//        double y1 = rayPosition.y;
//        double x2 = headingVector.x;
//        double y2 = headingVector.y;
//
//        double x3 = pointA.x;
//        double y3 = pointA.y;
//        double x4 = pointB.x;
//        double y4 = pointB.y;
//
//        double denominator = (x1-x2)*(y3-y4)-(y1-y2)*(x3-x4);
//
//        if (denominator == 0) return null;
//
//        double t = ((x1-x3)*(y3-y4)-(y1-y3)*(x3-x4))/denominator;
//        double u = ((y1-y2)*(x1-x3)-(x1-x2)*(y1-y3))/denominator;
//
//        if (t < 0 || u < 0 || u > 1) return null;
//
//        return new Vector(x1+t*(x2-x1), y1+t*(y2-y1));
        throw new NotImplementedException();
    }
}
