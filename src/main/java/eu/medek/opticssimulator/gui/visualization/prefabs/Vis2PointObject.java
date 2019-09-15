/*
 * OpticsSimulator
 * Jakub Medek, I. ročník, IOI MFF UK
 * letní semestr 2018/19
 * Programování II (NPRG031)
 */

package eu.medek.opticssimulator.gui.visualization.prefabs;

import eu.medek.opticssimulator.gui.visualization.interfaces.MouseListeners;
import processing.core.PApplet;
import processing.core.PVector;

import static eu.medek.opticssimulator.gui.Constants.GRAB_RADIUS;

public class Vis2PointObject implements MouseListeners {

    // Variables
    protected PVector start;
    protected PVector end;

    protected PApplet pApplet;
    private boolean startMoving = false;
    private boolean endMoving = false;
    private PVector offset;


    // Constructors
    public Vis2PointObject(PVector start, PVector end, PApplet pApplet) {
        this.start = start;
        this.end = end;
        this.pApplet = pApplet;
    }


    // Getters
    public PVector getStart() {
        return start;
    }

    public PVector getEnd() {
        return end;
    }


    // Methods
    @Override
    public boolean mousePressed(PVector mouse) {
        if (closeEnough(start, mouse)) {
            startMoving = true;
            offset = PVector.sub(start, mouse);
        }
        else if (closeEnough(end, mouse)) {
            endMoving = true;
            offset = PVector.sub(end, mouse);
        }

        return startMoving || endMoving;
    }

    @Override
    public boolean mouseReleased() {
        startMoving = false;
        endMoving = false;

        return false;
    }

    protected void update(PVector mouse) {
        if (startMoving) {
            this.start.x = mouse.x + offset.x;
            this.start.y = mouse.y + offset.y;
        }

        if (endMoving) {
            this.end.x = mouse.x + offset.x;
            this.end.y = mouse.y + offset.y;
        }
    }

    protected void drawEndpoints() {
        pApplet.noStroke();
        pApplet.fill(255, 0, 0);
        pApplet.ellipse(start.x, start.y, GRAB_RADIUS*2, GRAB_RADIUS*2);
        pApplet.ellipse(end.x, end.y, GRAB_RADIUS*2, GRAB_RADIUS*2);
    }

    /**
     * @return true if point is close enough to button (closer than GRAB_RADIUS)
     */
    public boolean closeEnough(PVector button, PVector point) {
        return PVector.dist(button, point) <= GRAB_RADIUS;
    }
}
