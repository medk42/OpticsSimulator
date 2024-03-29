/*
 * OpticsSimulator
 * Jakub Medek, I. ročník, IOI MFF UK
 * letní semestr 2018/19
 * Programování II (NPRG031)
 */

package eu.medek.opticssimulator.logic;

import eu.medek.opticssimulator.logic.rays.Beam;
import eu.medek.opticssimulator.logic.rays.DensityListener;
import eu.medek.opticssimulator.logic.rays.PointSource;
import eu.medek.opticssimulator.logic.rays.Ray;
import eu.medek.opticssimulator.logic.reflectables.*;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * For testing beam and point source
 */
public class ManualRaysPackage extends PApplet {

    private Vector objStart, objEnd;

    private Beam beam;
    private PointSource pointSource;
    private List<Reflectable> reflectables;
    private double density = 0.1d;

    private List<DensityListener> densityListeners;

    private boolean keyReleased = true, mouseLeftReleased = true, mouseRightReleased = true;

    private static final double ZOOM_AMOUNT = 0.9d;

    public void settings() {
        size(640, 480);
    }

    public void setup() {
        reflectables = new ArrayList<>();
        densityListeners = new LinkedList<>();

        objStart = new Vector(0,0);
        objEnd = new Vector(100,100);

        beam = new Beam();
        pointSource = new PointSource();
        densityListeners.add(beam);
        densityListeners.add(pointSource);


        //CHANGE FOR TESTING DIFFERENT COMPONENTS
        reflectables.add(new IdealLens(objStart, objEnd, 50));
        //END CHANGE

        reflectables.add(new BlockerLine(0,0,width,0));
        reflectables.add(new BlockerLine(10,0,10,height));
        reflectables.add(new BlockerLine(width,height,width,0));
        reflectables.add(new BlockerLine(width,height,0,height));
    }

    public void draw() {
        background(0);
        fill(255);



        for (Reflectable reflectable : reflectables) {
            if (reflectable instanceof IdealLens) {
                Vector pointA = ((IdealLens) reflectable).getPointA();
                Vector pointB = ((IdealLens) reflectable).getPointB();
                fill(255);
                ellipse((float) pointA.x, (float) pointA.y, 30, 30);
                ellipse((float) pointB.x, (float) pointB.y, 30, 30);
                Vector center = Vector.add(pointA, Vector.sub(pointB, pointA).div(2));
                Vector dir = Vector.sub(pointB, pointA).normalize().rotate(-Math.PI / 2).mult(((IdealLens) reflectable).getFocusDistance());
                Vector focus = Vector.add(center, dir);
                Vector focus2 = Vector.sub(center, dir);
                ellipse((float) focus.x, (float) focus.y, 8, 8);
                fill(0);
                ellipse((float) focus2.x, (float) focus2.y, 5, 5);
            }
        }

        stroke(100, 100, 200);
        strokeWeight(2);
        line((float) beam.getPointA().x, (float) beam.getPointA().y, (float) beam.getPointB().x, (float) beam.getPointB().y);

        noStroke();
        fill(100, 200, 200);
        ellipse((float) pointSource.getPosition().x, (float) pointSource.getPosition().y, 30, 30);


        strokeWeight(3);
        stroke(255);
        if (!keyReleased) {
            objEnd.x = mouseX;
            objEnd.y = mouseY;
        }
        line((float) objStart.x, (float) objStart.y, (float) objEnd.x, (float) objEnd.y);

        if (!mouseLeftReleased) {
            beam.setPointA(new Vector(mouseX, mouseY));
        }
        if (!mouseRightReleased) {
            pointSource.getPosition().x = mouseX;
            pointSource.getPosition().y = mouseY;
        }

        int limit = 2;
        for (Ray ray : beam.getRays()) solveRay(ray, limit);
        for (Ray ray : pointSource.getRays()) solveRay(ray, limit);
    }

    private void solveRay(Ray ray, int limit) {
        if (limit == 0) return;
        Response response = ray.solveReflectables(reflectables);
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
        if (mouseButton == LEFT) {
            if (mouseLeftReleased) {
                beam.setPointB(new Vector(mouseX, mouseY));
                mouseLeftReleased = false;
            }
        } else if (mouseButton == RIGHT) {
            mouseRightReleased = false;
        }
    }

    public void mouseReleased() {
        if (mouseButton == LEFT) {
            beam.setPointA(new Vector(mouseX, mouseY));
            mouseLeftReleased = true;
        } else if (mouseButton == RIGHT) {
            mouseRightReleased = true;
        }
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
        for (Reflectable reflectable : reflectables) {
            if (reflectable instanceof IdealLens) {
                IdealLens converted = (IdealLens) reflectable;
                if (converted.getFocusDistance()+event.getCount() != 0) converted.setFocusDistance(converted.getFocusDistance()+event.getCount());
                else converted.setFocusDistance(converted.getFocusDistance()+event.getCount()*2);
            }
        }

        density *= Math.pow(ZOOM_AMOUNT, event.getCount());
        for (DensityListener listener : densityListeners) {
            listener.setDensity(density);
        }
    }



    public static void main(String[] args) {
        PApplet.main("eu.medek.opticssimulator.logic.ManualRaysPackage");
    }
}
