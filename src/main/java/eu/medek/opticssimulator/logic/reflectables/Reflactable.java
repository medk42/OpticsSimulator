/*
 * OpticsSimulator
 * Jakub Medek, I. ročník, IOI MFF UK
 * letní semestr 2018/19
 * Programování II (NPRG031)
 */

package eu.medek.opticssimulator.logic.reflectables;

import eu.medek.opticssimulator.logic.Response;
import eu.medek.opticssimulator.logic.Vector;
import eu.medek.opticssimulator.logic.rays.Ray;

public interface Reflactable {
    Vector getIntersection(Ray ray);

    Response getImpactResult(Ray ray);
}
