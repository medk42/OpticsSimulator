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
import eu.medek.opticssimulator.logic.reflectables.BlockerLine;
import eu.medek.opticssimulator.logic.reflectables.Reflactable;
import processing.core.PApplet;
import processing.core.PVector;

public class VisBlockerLine extends Vis2PointObject implements VisReflactable {

    // Variables
    private BlockerLine blockerLine;


    // Constructors
    public VisBlockerLine(PVector start, PVector end, PApplet pApplet) {
        super(start, end, pApplet);
        this.blockerLine = new BlockerLine(new Vector(start.x, start.y), new Vector(end.x, end.y));
    }


    // Methods
    @Override
    public void update(PVector mouse) {
        super.update(mouse);

        blockerLine.getPointA().x = start.x;
        blockerLine.getPointA().y = start.y;
        blockerLine.getPointB().x = end.x;
        blockerLine.getPointB().y = end.y;

        this.draw();
    }

    private void draw() {
        pApplet.stroke(200, 50, 50);
        pApplet.line(start.x, start.y, end.x, end.y);

        super.drawEndpoints();
    }

    @Override
    public Reflactable getReflactable() {
        return blockerLine;
    }
}
