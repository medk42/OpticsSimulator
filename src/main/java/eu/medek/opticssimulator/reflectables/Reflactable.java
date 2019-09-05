package eu.medek.opticssimulator.reflectables;

import eu.medek.opticssimulator.Response;
import eu.medek.opticssimulator.Vector;
import eu.medek.opticssimulator.rays.Ray;

public interface Reflactable {
    Vector getIntersection(Ray ray);

    Response getImpactResult(Ray ray);
}
