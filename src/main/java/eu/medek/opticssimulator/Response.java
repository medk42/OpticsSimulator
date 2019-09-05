package eu.medek.opticssimulator;

import eu.medek.opticssimulator.rays.Ray;
import eu.medek.opticssimulator.reflectables.Reflactable;

import java.util.List;

public class Response {

    // Variables
    final private boolean impact;
    final private Vector pointOfImpact;
    final private Reflactable objectHit;
    final private List<Ray> resultingRays;



    //Constructors
    /**
     * Creates no-impact Response objectHit (impact set to false, everything else to null)
     */
    public Response() {
        impact = false;
        pointOfImpact = null;
        objectHit = null;
        resultingRays = null;
    }

    /**
     * Creates impact Response objectHit (impact set to true, everything else according to parameters)
     * @param pointOfImpact point of the impact of the ray
     * @param objectHit object that has been hit by the ray
     * @param resultingRays rays that have been created by the impact
     */
    public Response(Vector pointOfImpact, Reflactable objectHit, List<Ray> resultingRays) {
        this.impact = true;
        this.pointOfImpact = pointOfImpact;
        this.objectHit = objectHit;
        this.resultingRays = resultingRays;
    }



    //Getters

    /**
     * Gets impact result.
     * @return true if there has been an impact, false otherwise
     */
    public boolean getImpact() {
        return impact;
    }

    /**
     * Gets point of the impact.
     */
    public Vector getPointOfImpact() {
        return pointOfImpact;
    }

    /**
     * Gets reference to the object that has been hit.
     */
    public Reflactable getHitObject() {
        return objectHit;
    }

    /**
     * Gets rays created by the collision.
     */
    public List<Ray> getResultingRays() {
        return resultingRays;
    }
}
