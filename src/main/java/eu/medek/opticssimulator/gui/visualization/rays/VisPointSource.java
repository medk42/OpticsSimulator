/*
 * OpticsSimulator
 * Jakub Medek, I. ročník, IOI MFF UK
 * letní semestr 2018/19
 * Programování II (NPRG031)
 */

package eu.medek.opticssimulator.gui.visualization.rays;

import eu.medek.opticssimulator.gui.visualization.prefabs.Vis1PointObject;
import eu.medek.opticssimulator.gui.visualization.interfaces.VisCastable;
import eu.medek.opticssimulator.logic.Vector;
import eu.medek.opticssimulator.logic.rays.PointSource;
import eu.medek.opticssimulator.logic.reflectables.Reflectable;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.List;

import static eu.medek.opticssimulator.gui.Constants.REFLECT_LIMIT;

public class VisPointSource extends Vis1PointObject implements VisCastable {

    // Variables
    private PointSource pointSource;
    private RaySolver raySolver;


    // Constructors
    public VisPointSource(PVector center, PApplet pApplet, double density, PVector screenTopLeft, PVector screenBottomRight) {
        super(center, pApplet);
        this.pointSource = new PointSource(new Vector(center.x, center.y), density, 1d);
        this.raySolver = new RaySolver(pApplet, screenTopLeft, screenBottomRight);
    }


    // Methods
    @Override
    public void updateScreenEdges(PVector screenTopLeft, PVector screenBottomRight) {
        raySolver.updateScreenEdges(screenTopLeft, screenBottomRight);
    }

    public void setDensity(double density) {
        pointSource.setDensity(density);
    }

    @Override
    public void update(List<Reflectable> reflectables, PVector mouse) {
        super.update(mouse);

        pointSource.getPosition().x = center.x;
        pointSource.getPosition().y = center.y;

        pointSource.getRays().forEach(ray -> raySolver.solveRay(ray, reflectables, REFLECT_LIMIT));

        super.drawCenter();
    }
}
