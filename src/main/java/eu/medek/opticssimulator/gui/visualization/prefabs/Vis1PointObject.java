package eu.medek.opticssimulator.gui.visualization.prefabs;

import eu.medek.opticssimulator.gui.visualization.interfaces.MouseListeners;
import processing.core.PApplet;
import processing.core.PVector;

import static eu.medek.opticssimulator.gui.Constants.GRAB_RADIUS;

public class Vis1PointObject implements MouseListeners {

    // Variables
    protected PVector center;

    protected PApplet pApplet;
    private boolean centerMoving = false;
    private PVector offset;


    // Constructors
    public Vis1PointObject(PVector center, PApplet pApplet) {
        this.center = center;
        this.pApplet = pApplet;
    }


    // Getters
    public PVector getCenter() {
        return center;
    }

    // Methods
    @Override
    public boolean mousePressed(PVector mouse) {
        if (closeEnough(center, mouse)) {
            centerMoving = true;
            offset = PVector.sub(center, mouse);
        }
        return centerMoving;
    }

    @Override
    public boolean mouseReleased() {
        centerMoving = false;

        return false;
    }

    protected void update(PVector mouse) {
        if (centerMoving) {
            this.center.x = mouse.x + offset.x;
            this.center.y = mouse.y + offset.y;
        }
    }

    protected void drawCenter() {
        pApplet.noStroke();
        pApplet.fill(255, 0, 0);
        pApplet.ellipse(center.x, center.y, GRAB_RADIUS*2, GRAB_RADIUS*2);
    }

    /**
     * @return true if point is close enough to button (closer than GRAB_RADIUS)
     */
    public boolean closeEnough(PVector button, PVector point) {
        return PVector.dist(button, point) <= GRAB_RADIUS;
    }
}
