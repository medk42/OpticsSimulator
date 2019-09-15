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
import eu.medek.opticssimulator.logic.rays.Beam;
import eu.medek.opticssimulator.logic.reflectables.Reflactable;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.List;

import static eu.medek.opticssimulator.gui.Constants.REFLECT_LIMIT;

public class VisBeam extends Vis2PointObject implements VisCastable {

    // Variables
    private Beam beam;
    private RaySolver raySolver;


    // Constructors
    public VisBeam(PVector start, PVector end, PApplet pApplet, double density, PVector screenTopLeft, PVector screenBottomRight) {
        super(start, end, pApplet);
        this.beam = new Beam(new Vector(end.x, end.y), new Vector(start.x, start.y), density, 1d);
        this.raySolver = new RaySolver(pApplet, screenTopLeft, screenBottomRight);
    }


    // Methods
    @Override
    public void updateScreenEdges(PVector screenTopLeft, PVector screenBottomRight) {
        raySolver.updateScreenEdges(screenTopLeft, screenBottomRight);
    }

    public void setDensity(double density) {
        beam.setDensity(density);
    }

    @Override
    public void update(List<Reflactable> reflactables, PVector mouse) {
        super.update(mouse);

        beam.setPointA(new Vector(end.x, end.y));
        beam.setPointB(new Vector(start.x, start.y));

        beam.getRays().forEach(ray -> raySolver.solveRay(ray, reflactables, REFLECT_LIMIT));

        this.draw();
    }

    private void draw() {
        pApplet.stroke(100, 255, 150);
        pApplet.line(start.x, start.y, end.x, end.y);

        super.drawEndpoints();
    }
}
