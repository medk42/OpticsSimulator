package eu.medek.opticssimulator.reflectables;

import eu.medek.opticssimulator.Ray;
import eu.medek.opticssimulator.Response;
import eu.medek.opticssimulator.Vector;
import eu.medek.opticssimulator.reflectables.shapes.LineSegment;

import java.util.LinkedList;

public class BlockerLine extends LineSegment {

    /**
     * Creates a new Blocker using vectors passed as references
     */
    public BlockerLine(Vector pointA, Vector pointB) {
        super(pointA, pointB);
    }

    /**
     * Creates a new BlockerLine with the specified coordinates used as endpoints.
     */
    public BlockerLine(double x1, double y1, double x2, double y2) {
        super(x1,y1,x2,y2);
    }


    @Override
    public Response getImpactResult(Ray ray) {
        Vector intersection = getIntersection(ray);
        if (intersection == null) return new Response();
        else return new Response(intersection, this, new LinkedList<>());
    }
}
