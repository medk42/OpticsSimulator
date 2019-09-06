package eu.medek.opticssimulator.logic;

import eu.medek.opticssimulator.logic.rays.Ray;
import eu.medek.opticssimulator.logic.reflectables.*;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * For testing CircleGlass
 */
public class ManualCircleGlass extends PApplet {

    private Vector rayStart, rayEnd;

    private Vector objStart, objEnd;

    private Ray ray;
    private List<Reflactable> reflactables;

    private static final double CHANGE_AMOUNT = 0.9d;

    private boolean keyReleased = true;

    public void setup() {
        size(640, 480);
        //frameRate(2);
        rayStart = new Vector(0,0);
        rayEnd = new Vector(1, 1);

        objStart = new Vector(0,0);
        objEnd = new Vector(100,100);

        ray = new Ray(rayStart, -Math.PI/4d, 1d);
        reflactables = new ArrayList<>();

        reflactables.add(new CircleGlass(objStart, Vector.dist(objStart, objEnd), 1.2d));

        reflactables.add(new Mirror(0,0,width,0));
        reflactables.add(new Mirror(0,0,0,height));
        reflactables.add(new Mirror(width,height,width,0));
        reflactables.add(new Mirror(width,height,0,height));
    }

    public void draw() {
        System.out.println(frameRate);

        background(0);
        fill(255);

        for (Reflactable reflactable : reflactables) {
            if (reflactable instanceof IdealLens) {
                Vector pointA = ((IdealLens) reflactable).getPointA();
                Vector pointB = ((IdealLens) reflactable).getPointB();
                fill(255);
                ellipse((float) pointA.x, (float) pointA.y, 30, 30);
                ellipse((float) pointB.x, (float) pointB.y, 30, 30);
                Vector center = Vector.add(pointA, Vector.sub(pointB, pointA).div(2));
                Vector dir = Vector.sub(pointB, pointA).normalize().rotate(-Math.PI / 2).mult(((IdealLens) reflactable).getFocusDistance());
                Vector focus = Vector.add(center, dir);
                Vector focus2 = Vector.sub(center, dir);
                ellipse((float) focus.x, (float) focus.y, 8, 8);
                fill(0);
                ellipse((float) focus2.x, (float) focus2.y, 5, 5);
            }
        }


        rayEnd.x = mouseX;
        rayEnd.y = mouseY;
        Vector direction = Vector.sub(rayEnd, rayStart);
        ray.setAngle(direction.heading());
        direction.normalize();
        direction.mult(1000);
        rayEnd = Vector.add(rayStart, direction);

        strokeWeight(1);
        stroke(100);
        line((float) rayStart.x, (float) rayStart.y, (float) rayEnd.x, (float) rayEnd.y);

        strokeWeight(1);
        stroke(255);
        fill(80,40,230, (float) ((1d-1d/((CircleGlass) reflactables.get(0)).getRefractiveIndex()) * 255d));
        if (!keyReleased) {
            objEnd.x = mouseX;
            objEnd.y = mouseY;
            ((CircleGlass) reflactables.get(0)).setRadius(Vector.dist(objStart, objEnd));
        }
        double radius = ((CircleGlass) reflactables.get(0)).getRadius();
        ellipse((float) objStart.x, (float) objStart.y, (float) radius*2, (float) radius*2);

        solveRay(ray, 5);
    }

    private void solveRay(Ray ray, int limit) {
        if (limit == 0) return;
        Response response = ray.solveReflactables(reflactables);
        if (response.getImpact()) {
            strokeWeight(1);
            stroke(255, 50);
            line((float) objStart.x, (float) objStart.y, (float) response.getPointOfImpact().x, (float) response.getPointOfImpact().y);
            strokeWeight(2);
            stroke(200,100,100,(float) ray.getStrength()*255);
            line((float) ray.getPosition().x, (float) ray.getPosition().y, (float) response.getPointOfImpact().x, (float) response.getPointOfImpact().y);
            for (Ray resultingRay : response.getResultingRays()) {
                solveRay(resultingRay, limit - 1);
            }
        } else {
            System.out.println("got no impact");
        }
    }

    public void mousePressed() {
        rayStart.x = mouseX;
        rayStart.y = mouseY;
    }

    public void keyPressed(KeyEvent event) {
        if (event.getKey() == ' ' && keyReleased) {
            objStart.x = mouseX;
            objStart.y = mouseY;
            keyReleased = false;
        }
    }

    public void keyReleased(KeyEvent event) {
        if (event.getKey() == ' ') {
            objEnd.x = mouseX;
            objEnd.y = mouseY;
            ((CircleGlass) reflactables.get(0)).setRadius(Vector.dist(objStart, objEnd));
            keyReleased = true;
        }
    }

    public void mouseWheel(MouseEvent event) {
        for (Reflactable reflactable : reflactables) {
            if (reflactable instanceof IdealLens) {
                IdealLens converted = (IdealLens) reflactable;
                if (converted.getFocusDistance()+event.getCount() != 0) converted.setFocusDistance(converted.getFocusDistance()+event.getCount());
                else converted.setFocusDistance(converted.getFocusDistance()+event.getCount()*2);
            } else if (reflactable instanceof  CircleGlass) {
                CircleGlass converted = (CircleGlass) reflactable;
                double resultingValue = (converted.getRefractiveIndex() - 1) * Math.pow(CHANGE_AMOUNT, event.getCount()) + 1;
                converted.setRefractiveIndex(resultingValue);
                System.out.println(resultingValue);
            }
        }


    }



    public static void main(String[] args) {
        PApplet.main("eu.medek.opticssimulator.logic.ManualCircleGlass");
    }
}
