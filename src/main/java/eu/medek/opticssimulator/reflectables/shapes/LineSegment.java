package eu.medek.opticssimulator.reflectables.shapes;

import eu.medek.opticssimulator.Ray;
import eu.medek.opticssimulator.Reflactable;
import eu.medek.opticssimulator.Vector;

public abstract class LineSegment implements Reflactable {

    protected Vector pointA, pointB;

    /**
     * Creates a new Blocker using vectors passed as references
     */
    public LineSegment(Vector pointA, Vector pointB) {
        this.pointA = pointA;
        this.pointB = pointB;
    }

    /**
     * Creates a new BlockerLine with the specified coordinates used as endpoints.
     */
    public LineSegment(double x1, double y1, double x2, double y2) {
        this(new Vector(x1, y1), new Vector(x2, y2));
    }

    /**
     * Get the point of intersection between the ray and this object using line-line intersection (https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection)
     * @param ray ray to be used
     * @return Point of intersection or null if there isn't one.
     */
    @Override
    public Vector getIntersection(Ray ray) {
        Vector rayPosition = ray.getPosition();
        Vector headingVector = Vector.fromAngle(ray.getAngle()).add(rayPosition);

        double x1 = rayPosition.x;
        double y1 = rayPosition.y;
        double x2 = headingVector.x;
        double y2 = headingVector.y;

        double x3 = pointA.x;
        double y3 = pointA.y;
        double x4 = pointB.x;
        double y4 = pointB.y;

        double denominator = (x1-x2)*(y3-y4)-(y1-y2)*(x3-x4);

        if (denominator == 0) return null;

        double t = ((x1-x3)*(y3-y4)-(y1-y3)*(x3-x4))/denominator;
        double u = ((y1-y2)*(x1-x3)-(x1-x2)*(y1-y3))/denominator;

        if (t < 0 || u < 0 || u > 1) return null;

        return new Vector(x1+t*(x2-x1), y1+t*(y2-y1));
    }
}