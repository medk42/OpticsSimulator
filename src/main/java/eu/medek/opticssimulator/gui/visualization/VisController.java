/*
 * OpticsSimulator
 * Jakub Medek, I. ročník, IOI MFF UK
 * letní semestr 2018/19
 * Programování II (NPRG031)
 */

package eu.medek.opticssimulator.gui.visualization;

import eu.medek.opticssimulator.gui.elements.*;
import eu.medek.opticssimulator.gui.visualization.interfaces.VisCastable;
import eu.medek.opticssimulator.gui.visualization.interfaces.VisReflactable;
import eu.medek.opticssimulator.gui.visualization.prefabs.Vis1PointObject;
import eu.medek.opticssimulator.gui.visualization.prefabs.Vis2PointObject;
import eu.medek.opticssimulator.gui.visualization.rays.VisBeam;
import eu.medek.opticssimulator.gui.visualization.rays.VisPointSource;
import eu.medek.opticssimulator.gui.visualization.rays.VisRay;
import eu.medek.opticssimulator.gui.visualization.reflactables.*;
import eu.medek.opticssimulator.logic.reflectables.Reflactable;
import processing.core.PApplet;
import processing.core.PVector;
import processing.event.MouseEvent;

import java.util.LinkedList;

import static eu.medek.opticssimulator.gui.Constants.*;

public class VisController implements OnClickListener, OnChangeListener {

    // Variables
    private LinkedList<VisReflactable> reflactables;
    private LinkedList<VisCastable> rays;
    private LinkedList<Reflactable> reflactablesLogic;

    private PApplet pApplet;
    private String actActionLabel;
    private String actActionText;
    private boolean released = true;
    private double density = 0.1d;

    private float viewZoom;
    private PVector viewCenter;
    private PVector lastMousePosition;

    private PVector infoLocation;
    private int textSize;
    private int lineHeight;
    /**
     * Used for example for setting focus distance and refractive index
     */
    private UISlider settingsSlider;
    private ColorScheme colorScheme;


    private static final int SETTINGS_SLIDER_WIDTH = 400;

    private static final String REFRACTIVE_INDEX_TEXT = "Refractive index:";
    private static final double REFRACTIVE_INDEX_MIN = 1;
    private static final double REFRACTIVE_INDEX_MAX = 5;
    private static final double REFRACTIVE_INDEX_DEFAULT = 1.5d;

    private static final String FOCUS_DISTANCE_TEXT = "Focus distance:";
    private static final double FOCUS_DISTANCE_MIN= -300;
    private static final double FOCUS_DISTANCE_MAX = 300;
    private static final double FOCUS_DISTANCE_DEFAULT = 100;

    private static final float ZOOM_CONSTANT = 1.1f;
    private static final float ZOOM_MAX = 3f;
    private static final float ZOOM_MIN = .3f;


    //Constructors
    public VisController(PApplet pApplet, PVector infoLocation, int textSize, int lineHeight, ColorScheme colorScheme) {
        this.pApplet = pApplet;

        this.infoLocation = infoLocation;
        this.textSize = textSize;
        this.lineHeight = lineHeight;
        this.colorScheme = colorScheme;

        reset();
    }


    // Methods
    private void reset() {
        reflactables = new LinkedList<>();
        rays = new LinkedList<>();
        reflactablesLogic = new LinkedList<>();

        settingsSlider = null;

        viewZoom = 1;
        viewCenter = new PVector(pApplet.width/2f, pApplet.height/2f);

        actActionLabel = MOVE_BUTTON_LABEL;
        actActionText = "Move";
    }

    public void draw() {
        PVector mouseWorld = screenToWorld(new PVector(pApplet.mouseX, pApplet.mouseY));

        switch (actActionLabel) {
            case RAY_BUTTON_LABEL:
            case BEAM_BUTTON_LABEL:
                if (!released) ((Vis2PointObject) rays.getLast()).getEnd().set(mouseWorld);
                break;

            case POINT_SOURCE_BUTTON_LABEL:
                if (!released) ((Vis1PointObject) rays.getLast()).getCenter().set(mouseWorld);
                break;

            case IDEAL_LENS_BUTTON_LABEL:
            case IDEAL_CURVED_MIRROR_BUTTON_LABEL:
            case FLAT_MIRROR_BUTTON_LABEL:
            case BLOCKER_BUTTON_LABEL:
            case CIRCLE_LENS_BUTTON_LABEL:
                if (!released) ((Vis2PointObject) reflactables.getLast()).getEnd().set(mouseWorld);
                break;

            case MOVE_BUTTON_LABEL:
                if (!released) {
                    this.viewCenter.x += (pApplet.mouseX - lastMousePosition.x) / viewZoom;
                    this.viewCenter.y += (pApplet.mouseY - lastMousePosition.y) / viewZoom;
                    lastMousePosition.x = pApplet.mouseX;
                    lastMousePosition.y = pApplet.mouseY;

                    this.rays.forEach(ray -> ray.updateScreenEdges(screenToWorld(new PVector(0, 0)), screenToWorld(new PVector(pApplet.width, pApplet.height))));
                }
                break;
        }

        pApplet.pushMatrix();
            pApplet.scale(this.viewZoom);
            pApplet.strokeWeight(1/this.viewZoom);
            pApplet.translate(this.viewCenter.x, this.viewCenter.y);

            this.reflactables.forEach(reflactable -> reflactable.update(mouseWorld));
            this.rays.forEach(ray -> ray.update(reflactablesLogic, mouseWorld));
        pApplet.popMatrix();

        drawInfo();
    }

    /**
     * Draws info table based on infoLocation, textSize, lineHeight and actActionText
     */
    private void drawInfo() {
        pApplet.textSize(textSize);
        pApplet.textAlign(PApplet.LEFT, PApplet.CENTER);
        pApplet.fill(colorScheme.getTextColor());
        pApplet.text("Action: " + actActionText, infoLocation.x, infoLocation.y + lineHeight/2f);

        if (settingsSlider != null) {
            double value = 0;

            VisReflactable reflactable = reflactables.get(Integer.parseInt(settingsSlider.getLabel()));
            if (reflactable instanceof VisCircleGlass) {
                value = settingsSlider.getValue() * (REFRACTIVE_INDEX_MAX - REFRACTIVE_INDEX_MIN) + REFRACTIVE_INDEX_MIN;
                ((VisCircleGlass) reflactable).setRefractiveIndex(value);
            }
            else if (reflactable instanceof VisIdealLens) {
                value = settingsSlider.getValue() * (FOCUS_DISTANCE_MAX - FOCUS_DISTANCE_MIN) + FOCUS_DISTANCE_MIN;
                ((VisIdealLens) reflactable).setFocusDistance(value);
            }
            else if (reflactable instanceof VisIdealCurvedMirror) {
                value = settingsSlider.getValue() * (FOCUS_DISTANCE_MAX - FOCUS_DISTANCE_MIN) + FOCUS_DISTANCE_MIN;
                ((VisIdealCurvedMirror) reflactable).setFocusDistance(value);
            }

            settingsSlider.draw();
            pApplet.text("Value: " + String.format("%.2f", value), infoLocation.x, infoLocation.y + 2*lineHeight + lineHeight/2f);
        }
    }

    public void mousePressed() {
        PVector mouseWorld = screenToWorld(new PVector(pApplet.mouseX, pApplet.mouseY));
        if (settingsSlider != null && settingsSlider.mousePressed()) return;
        else settingsSlider = null;

        if (actActionLabel.equals(DELETE_BUTTON_LABEL)) {
            for (VisCastable ray : rays) {
                if (ray instanceof Vis2PointObject) {
                    Vis2PointObject vis2PointObject = (Vis2PointObject) ray;
                    if (vis2PointObject.closeEnough(vis2PointObject.getStart(), mouseWorld) ||
                        vis2PointObject.closeEnough(vis2PointObject.getEnd(), mouseWorld)) {
                        rays.remove(ray);
                        return;
                    }
                } else if (ray instanceof Vis1PointObject) {
                    Vis1PointObject vis1PointObject = (Vis1PointObject) ray;
                    if (vis1PointObject.closeEnough(vis1PointObject.getCenter(), mouseWorld)) {
                        rays.remove(ray);
                        return;
                    }
                }
            }

            for (VisReflactable reflactable : reflactables) {
                if (reflactable instanceof Vis2PointObject) {
                    Vis2PointObject vis2PointObject = (Vis2PointObject) reflactable;
                    if (vis2PointObject.closeEnough(vis2PointObject.getStart(), mouseWorld) ||
                            vis2PointObject.closeEnough(vis2PointObject.getEnd(), mouseWorld)) {
                        reflactables.remove(reflactable);
                        reflactablesLogic.remove(reflactable.getReflactable());
                        return;
                    }
                }
            }

            return;
        }

        for (VisCastable ray : rays) if (ray.mousePressed(mouseWorld)) return;
        for (VisReflactable reflactable: reflactables) {
            if (reflactable.mousePressed(mouseWorld)) {
                if (reflactable instanceof VisCircleGlass) {
                    settingsSlider = new UISlider((int) infoLocation.x, (int) (infoLocation.y + lineHeight), SETTINGS_SLIDER_WIDTH, lineHeight, Integer.toString(reflactables.indexOf(reflactable)), REFRACTIVE_INDEX_TEXT, textSize, pApplet, colorScheme);
                    double refractiveIndexPercentage = (((VisCircleGlass) reflactable).getRefractiveIndex() - REFRACTIVE_INDEX_MIN) / (REFRACTIVE_INDEX_MAX - REFRACTIVE_INDEX_MIN);
                    settingsSlider.setValue(refractiveIndexPercentage);
                } else if (reflactable instanceof VisIdealLens) {
                    settingsSlider = new UISlider((int) infoLocation.x, (int) (infoLocation.y + lineHeight), SETTINGS_SLIDER_WIDTH, lineHeight, Integer.toString(reflactables.indexOf(reflactable)), FOCUS_DISTANCE_TEXT, textSize, pApplet, colorScheme);
                    double focusDistancePercentage = (((VisIdealLens) reflactable).getFocusDistance() - FOCUS_DISTANCE_MIN) / (FOCUS_DISTANCE_MAX - FOCUS_DISTANCE_MIN);
                    settingsSlider.setValue(focusDistancePercentage);
                } else if (reflactable instanceof VisIdealCurvedMirror) {
                    settingsSlider = new UISlider((int) infoLocation.x, (int) (infoLocation.y + lineHeight), SETTINGS_SLIDER_WIDTH, lineHeight, Integer.toString(reflactables.indexOf(reflactable)), FOCUS_DISTANCE_TEXT, textSize, pApplet, colorScheme);
                    double focusDistancePercentage = (((VisIdealCurvedMirror) reflactable).getFocusDistance() - FOCUS_DISTANCE_MIN) / (FOCUS_DISTANCE_MAX - FOCUS_DISTANCE_MIN);
                    settingsSlider.setValue(focusDistancePercentage);
                }
                return;
            }
        }

        switch (actActionLabel) {
            case RAY_BUTTON_LABEL:
                this.rays.add(new VisRay(mouseWorld.copy(), mouseWorld.copy(), pApplet, screenToWorld(new PVector(0, 0)), screenToWorld(new PVector(pApplet.width, pApplet.height))));
                break;
            case BEAM_BUTTON_LABEL:
                this.rays.add(new VisBeam(mouseWorld.copy(), mouseWorld.copy(), pApplet, density, screenToWorld(new PVector(0, 0)), screenToWorld(new PVector(pApplet.width, pApplet.height))));
                break;
            case POINT_SOURCE_BUTTON_LABEL:
                this.rays.add(new VisPointSource(mouseWorld.copy(), pApplet, density, screenToWorld(new PVector(0, 0)), screenToWorld(new PVector(pApplet.width, pApplet.height))));
                break;
            case CIRCLE_LENS_BUTTON_LABEL:
                VisCircleGlass newCircleGlass = new VisCircleGlass(mouseWorld.copy(), mouseWorld.copy(), pApplet, REFRACTIVE_INDEX_DEFAULT);
                this.reflactables.add(newCircleGlass);
                this.reflactablesLogic.add(newCircleGlass.getReflactable());

                settingsSlider = new UISlider((int) infoLocation.x, (int) (infoLocation.y + lineHeight), SETTINGS_SLIDER_WIDTH, lineHeight, Integer.toString(reflactables.indexOf(newCircleGlass)), REFRACTIVE_INDEX_TEXT, textSize, pApplet, colorScheme);
                settingsSlider.setValue((newCircleGlass.getRefractiveIndex() - REFRACTIVE_INDEX_MIN) / (REFRACTIVE_INDEX_MAX - REFRACTIVE_INDEX_MIN));
                break;
            case IDEAL_LENS_BUTTON_LABEL:
                VisIdealLens newIdealLens = new VisIdealLens(mouseWorld.copy(), mouseWorld.copy(), pApplet, FOCUS_DISTANCE_DEFAULT);
                this.reflactables.add(newIdealLens);
                this.reflactablesLogic.add(newIdealLens.getReflactable());

                settingsSlider = new UISlider((int) infoLocation.x, (int) (infoLocation.y + lineHeight), SETTINGS_SLIDER_WIDTH, lineHeight, Integer.toString(reflactables.indexOf(newIdealLens)), FOCUS_DISTANCE_TEXT, textSize, pApplet, colorScheme);
                settingsSlider.setValue((newIdealLens.getFocusDistance() - FOCUS_DISTANCE_MIN) / (FOCUS_DISTANCE_MAX - FOCUS_DISTANCE_MIN));
                break;
            case IDEAL_CURVED_MIRROR_BUTTON_LABEL:
                VisIdealCurvedMirror newIdealCurvedMirror = new VisIdealCurvedMirror(mouseWorld.copy(), mouseWorld.copy(), pApplet, FOCUS_DISTANCE_DEFAULT);
                this.reflactables.add(newIdealCurvedMirror);
                this.reflactablesLogic.add(newIdealCurvedMirror.getReflactable());

                settingsSlider = new UISlider((int) infoLocation.x, (int) (infoLocation.y + lineHeight), SETTINGS_SLIDER_WIDTH, lineHeight, Integer.toString(reflactables.indexOf(newIdealCurvedMirror)), FOCUS_DISTANCE_TEXT, textSize, pApplet, colorScheme);
                settingsSlider.setValue((newIdealCurvedMirror.getFocusDistance() - FOCUS_DISTANCE_MIN) / (FOCUS_DISTANCE_MAX - FOCUS_DISTANCE_MIN));
                break;
            case FLAT_MIRROR_BUTTON_LABEL:
                VisMirror newMirror = new VisMirror(mouseWorld.copy(), mouseWorld.copy(), pApplet);
                this.reflactables.add(newMirror);
                this.reflactablesLogic.add(newMirror.getReflactable());
                break;
            case BLOCKER_BUTTON_LABEL:
                VisBlockerLine newBlocker = new VisBlockerLine(mouseWorld.copy(), mouseWorld.copy(), pApplet);
                this.reflactables.add(newBlocker);
                this.reflactablesLogic.add(newBlocker.getReflactable());
                break;
            case MOVE_BUTTON_LABEL:
                lastMousePosition = new PVector(pApplet.mouseX, pApplet.mouseY);
        }

        released = false;
    }

    public void mouseReleased() {
        if (settingsSlider != null && settingsSlider.mouseReleased()) return;

        for (VisCastable ray : rays) if (ray.mouseReleased()) return;
        for (VisReflactable reflactable: reflactables) if (reflactable.mouseReleased()) return;

        released = true;
    }

    public void mouseWheel(MouseEvent event) {
        PVector mouse = new PVector(event.getX(), event.getY());
        PVector origMouseWorld = screenToWorld(mouse);
        this.viewZoom *= Math.pow(ZOOM_CONSTANT, -event.getCount());
        if (this.viewZoom < ZOOM_MIN) this.viewZoom = ZOOM_MIN;
        else if (this.viewZoom > ZOOM_MAX) this.viewZoom = ZOOM_MAX;
        PVector zoomedMouseWorld = screenToWorld(mouse);
        PVector difference = PVector.sub(zoomedMouseWorld, origMouseWorld);
        this.viewCenter.add(difference);

        this.rays.forEach(ray -> ray.updateScreenEdges(screenToWorld(new PVector(0, 0)), screenToWorld(new PVector(pApplet.width, pApplet.height))));
    }

    private PVector screenToWorld(PVector screenVector) {
        PVector worldVector = new PVector(screenVector.x, screenVector.y);
        worldVector.div(this.viewZoom);
        worldVector.sub(this.viewCenter);
        return worldVector;
    }

    private PVector  worldToScreen(PVector worldVector) {
        PVector screenVector = new PVector(worldVector.x, worldVector.y);
        screenVector.add(this.viewCenter);
        screenVector.mult(this.viewZoom);
        return screenVector;
    }

    @Override
    public void onClick(UIElement element) {
        actActionLabel = element.getLabel();
        actActionText = element.getText();

        if (actActionLabel.equals(RESET_BUTTON_LABEL)) this.reset();
    }

    @Override
    public void onChange(UIElement element, double newValue) {
        if (element.getLabel().equals(DENSITY_SLIDER_LABEL)) {
            density = Math.pow(Math.E, (newValue - 1) * 4.6);
            for (VisCastable ray : rays) {
                if (ray instanceof VisBeam) ((VisBeam) ray).setDensity(density);
                if (ray instanceof VisPointSource) ((VisPointSource) ray).setDensity(density);
            }
        }
    }
}
