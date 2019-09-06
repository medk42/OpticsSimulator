package eu.medek.opticssimulator.logic.reflectables;

import eu.medek.opticssimulator.logic.rays.Ray;
import eu.medek.opticssimulator.logic.Response;
import eu.medek.opticssimulator.logic.Vector;

public class IdealCurvedMirror extends IdealLens {

    /**
     * Creates a new Mirror using vectors passed as references
     * @exception RuntimeException throws runtime exception if focusDistance is set to 0
     */
    public IdealCurvedMirror(Vector pointA, Vector pointB, double focusDistance) {
        super(pointA, pointB, focusDistance);
    }

    /**
     * Creates a new Mirror with the specified coordinates used as endpoints.
     * @exception RuntimeException throws runtime exception if focusDistance is set to 0
     */
    public IdealCurvedMirror(double x1, double y1, double x2, double y2, double focusDistance) {
        super(x1, y1, x2, y2, focusDistance);
    }

    @Override
    public Response getImpactResult(Ray ray) {
        Response response = super.getImpactResult(ray);
        if (response.getImpact()) {
            double segmentAngle = Vector.sub(pointB, pointA).heading(); // heading from pointA to pointB
            for (Ray resultingRay : response.getResultingRays()) {
                double resultingAngle = normalizeAngle(2*segmentAngle - resultingRay.getAngle());
                resultingRay.setAngle(resultingAngle);
            }
            response = new Response(response.getPointOfImpact(), this, response.getResultingRays());
        }
        return response;
    }
}
