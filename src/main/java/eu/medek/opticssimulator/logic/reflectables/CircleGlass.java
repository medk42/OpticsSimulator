package eu.medek.opticssimulator.logic.reflectables;

import eu.medek.opticssimulator.logic.Response;
import eu.medek.opticssimulator.logic.Vector;
import eu.medek.opticssimulator.logic.rays.Ray;
import eu.medek.opticssimulator.logic.reflectables.shapes.Circle;

import java.util.LinkedList;
import java.util.List;

public class CircleGlass extends Circle {

    //Variables
    private double refractiveIndex;

    private static final double TWO_PI = Math.PI * 2;
    private static final double HALF_PI = Math.PI / 2;


    // Constructors
    /**
     * Create a new CircleGlass using vector passed as reference and radius
     * @exception RuntimeException throws runtime exception if refractiveIndex is set to value <= 0
     */
    public CircleGlass(Vector center, double radius, double refractiveIndex) {
        super(center, radius);
        if (refractiveIndex < 1) throw new RuntimeException("Refractive index must be greater or equal to 1");
        this.refractiveIndex = refractiveIndex;
    }

    /**
     * Create a new CircleGlass with specified center coordinates and radius
     * @exception RuntimeException throws runtime exception if focusDistance is set to 0
     */
    public CircleGlass(double x, double y, double radius, double refractiveIndex) {
        super(x, y, radius);
        if (refractiveIndex < 1) throw new RuntimeException("Refractive index must be greater or equal to 1");
        this.refractiveIndex = refractiveIndex;
    }


    // Getters/Setters
    public double getRefractiveIndex() {
        return refractiveIndex;
    }

    public void setRefractiveIndex(double refractiveIndex) {
        if (refractiveIndex < 1) throw new RuntimeException("Refractive index must be greater or equal to 1");
        this.refractiveIndex = refractiveIndex;
    }


    // Methods
    /**
     * Make the angle in range -PI to +PI
     */
    private double normalizeAngle(double angle) {
        double res = angle % TWO_PI;
        if (res > Math.PI) res -= TWO_PI;
        else if (res < -Math.PI) res += TWO_PI;
        return res;
    }

    /**
     * Calculates the resulting ray using Snell's law (https://en.wikipedia.org/wiki/Snell%27s_law)
     */
    @Override
    public Response getImpactResult(Ray ray) {
        Vector intersection = getIntersection(ray);

        if (intersection != null) {
            double angleCenterToIntersection = Vector.sub(intersection, center).heading();
            double rayAngle =  normalizeAngle(ray.getAngle() + Math.PI);
            double incomingAngleDelta =  normalizeAngle(angleCenterToIntersection - rayAngle);
            double rayRefractiveIndex = 1;  // expecting the ray to come from empty space rather than other Circle or other non-ideal optical object

            double resultingAngle;

            boolean rayOutside = incomingAngleDelta < HALF_PI && incomingAngleDelta > -HALF_PI;
            double resultingDeltaSin;
            if (rayOutside) resultingDeltaSin = rayRefractiveIndex / this.refractiveIndex * Math.sin(incomingAngleDelta);
            else resultingDeltaSin = this.refractiveIndex / rayRefractiveIndex * Math.sin(normalizeAngle(Math.PI - incomingAngleDelta));

            if (resultingDeltaSin > 1 || resultingDeltaSin < -1) {
                resultingAngle = 2 * angleCenterToIntersection - rayAngle;
            } else {
                double resultingDelta = Math.asin(resultingDeltaSin);
                if (rayOutside) resultingAngle = normalizeAngle(angleCenterToIntersection + Math.PI - resultingDelta);
                else resultingAngle = normalizeAngle(angleCenterToIntersection + resultingDelta);
            }

            Ray resultingRay = new Ray(intersection, resultingAngle, ray.getStrength());

            List<Ray> resultingRays = new LinkedList<>();
            resultingRays.add(resultingRay);

            return new Response(intersection, this, resultingRays);
        }

        return new Response();
    }
}
