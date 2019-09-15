/*
 * OpticsSimulator
 * Jakub Medek, I. ročník, IOI MFF UK
 * letní semestr 2018/19
 * Programování II (NPRG031)
 */

package eu.medek.opticssimulator.gui.visualization.reflectables;

import eu.medek.opticssimulator.gui.visualization.interfaces.VisReflectable;
import eu.medek.opticssimulator.gui.visualization.prefabs.Vis2PointObject;
import eu.medek.opticssimulator.logic.Vector;
import eu.medek.opticssimulator.logic.reflectables.IdealCurvedMirror;
import eu.medek.opticssimulator.logic.reflectables.Reflectable;
import processing.core.PApplet;
import processing.core.PVector;

public class VisIdealCurvedMirror extends Vis2PointObject implements VisReflectable {

    // Variables
    private IdealCurvedMirror idealCurvedMirror;


    // Constructors
    public VisIdealCurvedMirror(PVector start, PVector end, PApplet pApplet, double focusDistance) {
        super(start, end, pApplet);
        this.idealCurvedMirror = new IdealCurvedMirror(new Vector(start.x, start.y), new Vector(end.x, end.y), focusDistance);
    }


    // Getters/Setters
    public double getFocusDistance() {
        return idealCurvedMirror.getFocusDistance();
    }

    public void setFocusDistance(double focusDistance) {
        if (focusDistance != 0) idealCurvedMirror.setFocusDistance(focusDistance);
    }


    // Methods
    @Override
    public void update(PVector mouse) {
        super.update(mouse);

        idealCurvedMirror.getPointA().x = start.x;
        idealCurvedMirror.getPointA().y = start.y;
        idealCurvedMirror.getPointB().x = end.x;
        idealCurvedMirror.getPointB().y = end.y;

        this.draw();
    }

    private void draw() {
        pApplet.stroke(100, 100, 255);
        pApplet.line(start.x, start.y, end.x, end.y);

        PVector direction = PVector.sub(end, start);
        PVector center = PVector.add(start, PVector.div(direction, 2));

        direction.normalize();
        direction.rotate(PApplet.HALF_PI);
        direction.mult((float) idealCurvedMirror.getFocusDistance());

        PVector focusA = PVector.add(center, direction);
        PVector focusB = PVector.sub(center, direction);

        pApplet.noStroke();
        pApplet.fill(40, 40, 200);
        pApplet.ellipseMode(PApplet.CENTER);
        pApplet.ellipse(focusA.x, focusA.y, 5, 5);
        pApplet.ellipse(focusB.x, focusB.y, 5, 5);

        super.drawEndpoints();
    }

    @Override
    public Reflectable getReflectable() {
        return idealCurvedMirror;
    }
}
