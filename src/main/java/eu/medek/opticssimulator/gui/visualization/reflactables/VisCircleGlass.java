package eu.medek.opticssimulator.gui.visualization.reflactables;

import eu.medek.opticssimulator.gui.visualization.interfaces.VisReflactable;
import eu.medek.opticssimulator.gui.visualization.prefabs.Vis2PointObject;
import eu.medek.opticssimulator.logic.Vector;
import eu.medek.opticssimulator.logic.reflectables.CircleGlass;
import eu.medek.opticssimulator.logic.reflectables.Reflactable;
import processing.core.PApplet;
import processing.core.PVector;

public class VisCircleGlass extends Vis2PointObject implements VisReflactable {

    // Variables
    private CircleGlass circleGlass;


    // Constructors
    public VisCircleGlass(PVector start, PVector end, PApplet pApplet, double refractiveIndex) {
        super(start, end, pApplet);
        this.circleGlass = new CircleGlass(new Vector(start.x, start.y), PVector.dist(start, end), refractiveIndex);
    }


    // Getters/Setters
    public void setRefractiveIndex(double refractiveIndex) {
        circleGlass.setRefractiveIndex(refractiveIndex);
    }

    public double getRefractiveIndex() {
        return circleGlass.getRefractiveIndex();
    }


    // Methods
    @Override
    public void update(PVector mouse) {
        super.update(mouse);

        circleGlass.getCenter().x = start.x;
        circleGlass.getCenter().y = start.y;
        circleGlass.setRadius(PVector.dist(start, end));

        this.draw();
    }

    private void draw() {
        pApplet.stroke(80,40,230);
        pApplet.fill(80,40,230, (float) ((1d - 1d/circleGlass.getRefractiveIndex()) * 255d));

        float diameter = PVector.dist(start, end) * 2;
        pApplet.ellipse(start.x, start.y, diameter, diameter);

        super.drawEndpoints();
    }

    @Override
    public Reflactable getReflactable() {
        return circleGlass;
    }
}
