package eu.medek.opticssimulator.gui.visualization.rays;

import eu.medek.opticssimulator.logic.Response;
import eu.medek.opticssimulator.logic.Vector;
import eu.medek.opticssimulator.logic.rays.Ray;
import eu.medek.opticssimulator.logic.reflectables.BlockerLine;
import eu.medek.opticssimulator.logic.reflectables.Reflactable;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.LinkedList;
import java.util.List;

class RaySolver {

    // Variables
    private List<BlockerLine> screenEdgeBlockers;
    private PApplet pApplet;


    // Constructors
    RaySolver(PApplet pApplet, PVector screenTopLeft, PVector screenBottomRight) {
        this.pApplet = pApplet;

        this.screenEdgeBlockers = new LinkedList<>();
        this.screenEdgeBlockers.add(new BlockerLine(screenTopLeft.x,screenTopLeft.y, screenBottomRight.x,screenTopLeft.y));
        this.screenEdgeBlockers.add(new BlockerLine(screenTopLeft.x,screenTopLeft.y,screenTopLeft.x, screenBottomRight.y));
        this.screenEdgeBlockers.add(new BlockerLine(screenBottomRight.x, screenBottomRight.y, screenBottomRight.x,screenTopLeft.y));
        this.screenEdgeBlockers.add(new BlockerLine(screenBottomRight.x, screenBottomRight.y,screenTopLeft.x, screenBottomRight.y));
    }


    // Methods
    void updateScreenEdges(PVector screenTopLeft, PVector screenBottomRight) {
        screenEdgeBlockers.get(0).getPointA().x = screenTopLeft.x;
        screenEdgeBlockers.get(0).getPointA().y = screenTopLeft.y;
        screenEdgeBlockers.get(0).getPointB().x = screenBottomRight.x;
        screenEdgeBlockers.get(0).getPointB().y = screenTopLeft.y;

        screenEdgeBlockers.get(1).getPointA().x = screenTopLeft.x;
        screenEdgeBlockers.get(1).getPointA().y = screenTopLeft.y;
        screenEdgeBlockers.get(1).getPointB().x = screenTopLeft.x;
        screenEdgeBlockers.get(1).getPointB().y = screenBottomRight.y;

        screenEdgeBlockers.get(2).getPointA().x = screenBottomRight.x;
        screenEdgeBlockers.get(2).getPointA().y = screenBottomRight.y;
        screenEdgeBlockers.get(2).getPointB().x = screenBottomRight.x;
        screenEdgeBlockers.get(2).getPointB().y = screenTopLeft.y;

        screenEdgeBlockers.get(3).getPointA().x = screenBottomRight.x;
        screenEdgeBlockers.get(3).getPointA().y = screenBottomRight.y;
        screenEdgeBlockers.get(3).getPointB().x = screenTopLeft.x;
        screenEdgeBlockers.get(3).getPointB().y = screenBottomRight.y;
    }

    void solveRay(Ray ray, List<Reflactable> reflactables, int limit) {
        if (limit == 0) return;
        Response response = ray.solveReflactables(reflactables);
        if (response.getImpact()) {
            drawRay((float) ray.getPosition().x, (float) ray.getPosition().y, (float) response.getPointOfImpact().x, (float) response.getPointOfImpact().y, (float) ray.getStrength() * 180);
            response.getResultingRays().forEach(resultingRay -> solveRay(resultingRay, reflactables, limit - 1));
        } else {
            Vector farthestIntersection = null;
            for (BlockerLine blocker : screenEdgeBlockers) {
                Vector newIntersection = blocker.getIntersection(ray);
                if (newIntersection != null) {
                    if (farthestIntersection == null) farthestIntersection = newIntersection;
                    else if (Vector.dist(newIntersection, ray.getPosition()) > Vector.dist(farthestIntersection, ray.getPosition())) {
                        farthestIntersection = newIntersection;
                    }
                }
            }

            if (farthestIntersection != null) {
                drawRay((float) ray.getPosition().x, (float) ray.getPosition().y, (float) farthestIntersection.x, (float) farthestIntersection.y, (float) ray.getStrength() * 180);
            }
        }
    }

    private void drawRay(float x1, float y1, float x2, float y2, float opacity) {
        pApplet.strokeWeight(2);
        pApplet.stroke(255,170,0, opacity);
        pApplet.line(x1, y1, x2, y2);
    }
}
