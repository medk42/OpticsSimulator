package eu.medek.opticssimulator.reflectables;

import eu.medek.opticssimulator.Ray;
import eu.medek.opticssimulator.Response;
import eu.medek.opticssimulator.Vector;
import eu.medek.opticssimulator.reflectables.shapes.LineSegment;

import java.util.LinkedList;
import java.util.List;

public class MirrorLine extends LineSegment {

    /**
     * Creates a new MirrorLine using vectors passed as references
     */
    public MirrorLine(Vector pointA, Vector pointB) {
        super(pointA, pointB);
    }

    /**
     * Creates a new MirrorLine with the specified coordinates used as endpoints.
     */
    public MirrorLine(double x1, double y1, double x2, double y2) {
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
            resultingRays.add(new Ray(intersection, resultingAngle, 1));
            return new Response(intersection, this, resultingRays);
        }
    }
}
