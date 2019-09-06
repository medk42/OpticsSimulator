package eu.medek.opticssimulator.reflectables;

import eu.medek.opticssimulator.rays.Ray;
import eu.medek.opticssimulator.Response;
import eu.medek.opticssimulator.Vector;
import eu.medek.opticssimulator.reflectables.shapes.LineSegment;

import java.util.LinkedList;
import java.util.List;

public class IdealLens extends LineSegment {

    // Variables
    private static final double HALF_PI = Math.PI / 2;
    private static final double TWO_PI = Math.PI * 2;

    private double focusDistance;


    // Constructors
    /**
     * Create a new IdealLens using vectors passed as references
     * @exception RuntimeException throws runtime exception if focusDistance is set to 0
     */
    public IdealLens(Vector pointA, Vector pointB, double focusDistance) {
        super(pointA, pointB);
        if (focusDistance == 0) throw new RuntimeException("Focus distance cannot be 0!");
        this.focusDistance = focusDistance;
    }

    /**
     * Create a new IdealLens with the specified coordinates used as endpoints.
     * @exception RuntimeException throws runtime exception if focusDistance is set to 0
     */
    public IdealLens(double x1, double y1, double x2, double y2, double focusDistance) {
        super(x1, y1, x2, y2);
        if (focusDistance == 0) throw new RuntimeException("Focus distance cannot be 0!");
        this.focusDistance = focusDistance;
    }


    // Getters/Setters
    public double getFocusDistance() {
        return focusDistance;
    }

    /**
     * Set the focus distance.
     * @exception RuntimeException throws runtime exception if focusDistance is set to 0
     */
    public void setFocusDistance(double focusDistance) {
        if (focusDistance == 0) throw new RuntimeException("Focus distance cannot be 0!");
        this.focusDistance = focusDistance;
    }


    // Methods
    /**
     * Make the angle in range -PI to +PI
     */
    double normalizeAngle(double angle) {
        double res = angle % TWO_PI;
        if (res > Math.PI) res -= TWO_PI;
        else if (res < -Math.PI) res += TWO_PI;
        return res;
    }

    /**
     * Calculates the resulting ray using ideal lens equation (https://www.sciencedirect.com/science/article/pii/S0030402615000364#eq0010)
     */
    @Override
    public Response getImpactResult(Ray ray) {
        Vector intersection = getIntersection(ray);
        if (intersection == null) return new Response();
        else {
            Vector directionVector = Vector.sub(pointB, pointA);
            double lensAngle = directionVector.heading();
            double rayAngle = ray.getAngle();

            double alpha = normalizeAngle(HALF_PI - lensAngle + rayAngle);
            double tanAlpha = Math.tan(alpha);

            Vector lensCenter = Vector.add(pointA, new Vector(directionVector).div(2));
            double intersectionLensCenterDistance = Vector.dist(lensCenter, intersection);
            if (Vector.dist(pointA, intersection) < Vector.dist(pointA, lensCenter)) intersectionLensCenterDistance = -intersectionLensCenterDistance;

            double tanGamma = intersectionLensCenterDistance / focusDistance;

            double beta;

            if (alpha > HALF_PI || alpha < -HALF_PI) {
                beta = Math.atan(-tanAlpha - tanGamma);
                beta = Math.PI - beta;
                beta += lensAngle - HALF_PI;

//TODO: to be removed if not used later
//                beta = Math.atan(tanAlpha - (-tanGamma));
//                beta += lensAngle + HALF_PI;
            } else {
                beta = Math.atan(tanAlpha - tanGamma);
                beta += lensAngle - HALF_PI;
            }

            Ray resultingRay = new Ray(intersection, beta, ray.getStrength());
            resultingRay.getIgnoredReflactables().add(this);
            List<Ray> resultingRays = new LinkedList<>();
            resultingRays.add(resultingRay);
            return new Response(intersection, this, resultingRays);
        }
    }
}
