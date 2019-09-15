/*
 * OpticsSimulator
 * Jakub Medek, I. ročník, IOI MFF UK
 * letní semestr 2018/19
 * Programování II (NPRG031)
 */

package eu.medek.opticssimulator.gui.visualization.rays;

import eu.medek.opticssimulator.gui.visualization.prefabs.Vis2PointObject;
import eu.medek.opticssimulator.gui.visualization.interfaces.VisCastable;
import eu.medek.opticssimulator.logic.Vector;
import eu.medek.opticssimulator.logic.rays.Ray;
import eu.medek.opticssimulator.logic.reflectables.Reflactable;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.List;

import static eu.medek.opticssimulator.gui.Constants.REFLECT_LIMIT;

public class VisRay extends Vis2PointObject implements VisCastable {

    // Variables
    private Ray ray;
    private RaySolver raySolver;


    // Constructors
    public VisRay(PVector start, PVector end, PApplet pApplet, PVector screenTopLeft, PVector screenBottomRight) {
        super(start, end, pApplet);
        this.ray = new Ray(new Vector(start.x, start.y), -PVector.sub(end, start).heading(), 1d);
        this.raySolver = new RaySolver(pApplet, screenTopLeft, screenBottomRight);
    }


    // Methods
    @Override
    public void updateScreenEdges(PVector screenTopLeft, PVector screenBottomRight) {
        raySolver.updateScreenEdges(screenTopLeft, screenBottomRight);
    }

    @Override
    public void update(List<Reflactable> reflactables, PVector mouse) {
        super.update(mouse);

        ray.getPosition().x = start.x;
        ray.getPosition().y = start.y;
        ray.setAngle(-PVector.sub(end, start).heading());

        raySolver.solveRay(ray, reflactables, REFLECT_LIMIT);

        super.drawEndpoints();
    }
}
