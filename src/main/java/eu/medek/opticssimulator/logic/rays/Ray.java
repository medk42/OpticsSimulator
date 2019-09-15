/*
 * OpticsSimulator
 * Jakub Medek, I. ročník, IOI MFF UK
 * letní semestr 2018/19
 * Programování II (NPRG031)
 */

package eu.medek.opticssimulator.logic.rays;

import eu.medek.opticssimulator.logic.reflectables.Reflectable;
import eu.medek.opticssimulator.logic.Response;
import eu.medek.opticssimulator.logic.Vector;

import java.util.LinkedList;
import java.util.List;

public class Ray {

    // Variables
    private Vector position;
    /**
     * Angle in radians (cartesian coordinates)
     */
    private double angle;
    private double strength;
    final private List<Reflectable> ignoredReflectables;


    // Constructors
    public Ray(Vector position, double angle, double strength) {
        this.position = position;
        this.angle = angle;
        this.strength = strength;
        ignoredReflectables = new LinkedList<>();
    }

    public Ray(double x, double y, double angle, double strength) {
        this(new Vector(x,y), angle, strength);
    }

    public Ray() {
        this(0,0,0,1);
    }



    // Setters
    public void setPosition(Vector position) {
        this.position = position;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setStrength(double strength) {
        if (strength < 0 || strength > 1) throw new RuntimeException("Strength needs to be between 0 and 1");
        this.strength = strength;
    }



    // Getters
    public Vector getPosition() {
        return position;
    }

    public double getAngle() {
        return angle;
    }

    public double getStrength() {
        return strength;
    }

    public List<Reflectable> getIgnoredReflectables() {
        return ignoredReflectables;
    }



    // Methods
    public void lookAt(Vector v) {
        this.angle = Vector.sub(v, this.position).heading();
    }

    /**
     * Checks all Reflectable objects and returns Response object containing the closest point of impact
     * @param reflectables Reflectable objects to check
     * @return Response object containing the closest point of impact
     */
    public Response solveReflectables(List<Reflectable> reflectables) {
        Response closest = null;
        reflectables = new LinkedList<>(reflectables);
        reflectables.removeAll(ignoredReflectables);
        for (Reflectable reflectable : reflectables) {
            Response response = reflectable.getImpactResult(this);
            if (response.getImpact()) {
                if (closest == null || Vector.dist(response.getPointOfImpact(), this.position) < Vector.dist(closest.getPointOfImpact(), this.position)) {
                    closest = response;
                }
            }
        }

        if (closest == null) return new Response();
        return closest;
    }
}
