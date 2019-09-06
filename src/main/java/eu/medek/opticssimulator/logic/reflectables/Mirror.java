package eu.medek.opticssimulator.logic.reflectables;

import eu.medek.opticssimulator.logic.rays.Ray;
import eu.medek.opticssimulator.logic.Response;
import eu.medek.opticssimulator.logic.Vector;
import eu.medek.opticssimulator.logic.reflectables.shapes.LineSegment;

import java.util.LinkedList;
import java.util.List;

public class Mirror extends LineSegment {

    /**
     * Creates a new Mirror using vectors passed as references
     */
    public Mirror(Vector pointA, Vector pointB) {
        super(pointA, pointB);
    }

    /**
     * Creates a new Mirror with the specified coordinates used as endpoints.
     */
    public Mirror(double x1, double y1, double x2, double y2) {
        super(x1, y1, x2, y2);
    }

    @Override
    public Response getImpactResult(Ray ray) {
        Vector intersection = getIntersection(ray);
        if (intersection == null) return new Response();
        else {
            double segmentAngle = Vector.sub(pointB, pointA).heading(); // heading from pointA to pointB
            double resultingAngle = 2*segmentAngle - ray.getAngle();

            List<Ray> resultingRays = new LinkedList<>();
            Ray resultingRay = new Ray(intersection, resultingAngle, ray.getStrength());
            resultingRay.getIgnoredReflactables().add(this);
            resultingRays.add(resultingRay);
            return new Response(intersection, this, resultingRays);
        }
    }
}
