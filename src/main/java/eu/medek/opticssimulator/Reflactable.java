package eu.medek.opticssimulator;

public interface Reflactable {
    Vector getIntersection(Ray ray);

    Response getImpactResult(Ray ray);
}
