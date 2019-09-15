/*
 * OpticsSimulator
 * Jakub Medek, I. ročník, IOI MFF UK
 * letní semestr 2018/19
 * Programování II (NPRG031)
 */

package eu.medek.opticssimulator.gui.visualization.reflactables;

import eu.medek.opticssimulator.gui.visualization.interfaces.VisReflactable;
import eu.medek.opticssimulator.gui.visualization.prefabs.Vis2PointObject;
import eu.medek.opticssimulator.logic.Vector;
import eu.medek.opticssimulator.logic.reflectables.Mirror;
import eu.medek.opticssimulator.logic.reflectables.Reflactable;
import processing.core.PApplet;
import processing.core.PVector;

public class VisMirror extends Vis2PointObject implements VisReflactable {

    // Variables
    private Mirror mirror;


    // Constructors
    public VisMirror(PVector start, PVector end, PApplet pApplet) {
        super(start, end, pApplet);
        this.mirror = new Mirror(new Vector(start.x, start.y), new Vector(end.x, end.y));
    }


    // Methods
    @Override
    public void update(PVector mouse) {
        super.update(mouse);

        mirror.getPointA().x = start.x;
        mirror.getPointA().y = start.y;
        mirror.getPointB().x = end.x;
        mirror.getPointB().y = end.y;

        this.draw();
    }

    private void draw() {
        pApplet.stroke(100, 100, 255);
        pApplet.line(start.x, start.y, end.x, end.y);

        super.drawEndpoints();
    }

    @Override
    public Reflactable getReflactable() {
        return mirror;
    }
}
