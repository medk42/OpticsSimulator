/*
 * OpticsSimulator
 * Jakub Medek, I. ročník, IOI MFF UK
 * letní semestr 2018/19
 * Programování II (NPRG031)
 */

package eu.medek.opticssimulator.gui.visualization.reflectables;

import eu.medek.opticssimulator.gui.visualization.prefabs.Vis2PointObject;
import eu.medek.opticssimulator.gui.visualization.interfaces.VisReflectable;
import eu.medek.opticssimulator.logic.Vector;
import eu.medek.opticssimulator.logic.reflectables.IdealLens;
import eu.medek.opticssimulator.logic.reflectables.Reflectable;
import processing.core.PApplet;
import processing.core.PVector;

public class VisIdealLens extends Vis2PointObject implements VisReflectable {

    // Variables
    private IdealLens idealLens;


    // Constructors
    public VisIdealLens(PVector start, PVector end, PApplet pApplet, double focusDistance) {
        super(start, end, pApplet);
        this.idealLens = new IdealLens(new Vector(start.x, start.y), new Vector(end.x, end.y), focusDistance);
    }


    // Getters/Setters
    public double getFocusDistance() {
        return idealLens.getFocusDistance();
    }

    public void setFocusDistance(double focusDistance) {
        if (focusDistance != 0) idealLens.setFocusDistance(focusDistance);
    }


    // Methods
    @Override
    public void update(PVector mouse) {
        super.update(mouse);

        idealLens.getPointA().x = start.x;
        idealLens.getPointA().y = start.y;
        idealLens.getPointB().x = end.x;
        idealLens.getPointB().y = end.y;

        this.draw();
    }

    private void draw() {
        pApplet.stroke(200);
        pApplet.line(start.x, start.y, end.x, end.y);

        PVector direction = PVector.sub(end, start);
        PVector center = PVector.add(start, PVector.div(direction, 2));

        direction.normalize();
        direction.rotate(PApplet.HALF_PI);
        direction.mult((float) idealLens.getFocusDistance());

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
        return idealLens;
    }
}
