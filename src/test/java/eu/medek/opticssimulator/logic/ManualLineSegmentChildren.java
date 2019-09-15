/*
 * OpticsSimulator
 * Jakub Medek, I. ročník, IOI MFF UK
 * letní semestr 2018/19
 * Programování II (NPRG031)
 */

package eu.medek.opticssimulator.logic;

import eu.medek.opticssimulator.logic.rays.Ray;
import eu.medek.opticssimulator.logic.reflectables.IdealCurvedMirror;
import eu.medek.opticssimulator.logic.reflectables.IdealLens;
import eu.medek.opticssimulator.logic.reflectables.Mirror;
import eu.medek.opticssimulator.logic.reflectables.Reflactable;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class ManualLineSegmentChildren extends PApplet {

    private Vector rayStart, rayEnd;

    private Vector objStart, objEnd;

    private Ray ray;
    private List<Reflactable> reflactables;

    private boolean keyReleased = true;

    public void settings() {
        size(640, 480);
    }

    public void setup() {
        rayStart = new Vector(0,0);
        rayEnd = new Vector(1, 1);

        objStart = new Vector(0,0);
        objEnd = new Vector(100,100);

        ray = new Ray(rayStart, -Math.PI/4d, 1d);
        reflactables = new ArrayList<>();

        //CHANGE FOR TESTING DIFFERENT COMPONENTS
        reflactables.add(new IdealCurvedMirror(objStart, objEnd, 50));
        //END CHANGE

        reflactables.add(new Mirror(0,0,width,0));
        reflactables.add(new Mirror(0,0,0,height));
        reflactables.add(new Mirror(width,height,width,0));
        reflactables.add(new Mirror(width,height,0,height));
    }

    public void draw() {
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

        strokeWeight(3);
        stroke(255);
        if (!keyReleased) {
            objEnd.x = mouseX;
            objEnd.y = mouseY;
        }
        line((float) objStart.x, (float) objStart.y, (float) objEnd.x, (float) objEnd.y);

        solveRay(ray, 2);
    }

    private void solveRay(Ray ray, int limit) {
        if (limit == 0) return;
        Response response = ray.solveReflactables(reflactables);
        if (response.getImpact()) {
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
            keyReleased = true;
        }
    }

    public void mouseWheel(MouseEvent event) {
        for (Reflactable reflactable : reflactables) {
            if (reflactable instanceof IdealLens) {
                IdealLens converted = (IdealLens) reflactable;
                if (converted.getFocusDistance()+event.getCount() != 0) converted.setFocusDistance(converted.getFocusDistance()+event.getCount());
                else converted.setFocusDistance(converted.getFocusDistance()+event.getCount()*2);
            }
        }
    }



    public static void main(String[] args) {
        PApplet.main("eu.medek.opticssimulator.logic.ManualLineSegmentChildren");
    }
}
