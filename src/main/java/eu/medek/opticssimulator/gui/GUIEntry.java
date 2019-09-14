package eu.medek.opticssimulator.gui;

import eu.medek.opticssimulator.gui.elements.*;
import eu.medek.opticssimulator.gui.visualization.VisController;
import processing.core.PApplet;
import processing.core.PVector;
import processing.event.MouseEvent;

import static eu.medek.opticssimulator.gui.Constants.*;

public class GUIEntry extends PApplet {

    private UIController uiController;
    private ColorScheme colorScheme;

    private VisController visController;

    private static final int TEXT_SIZE = 18;
    private static final int LINE_HEIGHT = 30;

    @Override
    public void settings() {
        size(1000, 800);
    }

    @Override
    public void setup() {
        surface.setTitle("Optics Simulator");

        setupColorScheme();

        setupVisualization();

        setupGUI();
    }

    private void setupColorScheme() {
        int textColor = color(255);
        int normalColor = color(80);
        int highlightedColor = color(50);
        colorScheme = new ColorScheme(textColor, normalColor, normalColor, textColor, highlightedColor, highlightedColor);
    }

    private void setupGUI() {
        uiController = new UIController(this);
        uiController.setupFactory(new PVector(10, 10), LINE_HEIGHT, 5, TEXT_SIZE, colorScheme);


        UIDropdown fileDropdown = uiController.addDropdown(FILE_DROPDOWN_LABEL, "File");
        fileDropdown.addButton(RESET_BUTTON_LABEL, "Reset").addOnClickListener(visController);

        uiController.addExtraSpace(2);

        UIDropdown sourcesDropdown = uiController.addDropdown(LIGHT_SOURCES_DROPDOWN_LABEL, "Light sources");
        sourcesDropdown.addButton(RAY_BUTTON_LABEL, "Ray").addOnClickListener(visController);
        sourcesDropdown.addButton(BEAM_BUTTON_LABEL, "Beam").addOnClickListener(visController);
        sourcesDropdown.addButton(POINT_SOURCE_BUTTON_LABEL, "Point source").addOnClickListener(visController);

        UIDropdown mirrorsDropdown = uiController.addDropdown(MIRRORS_DROPDOWN_LABEL, "Mirrors");
        mirrorsDropdown.addButton(FLAT_MIRROR_BUTTON_LABEL, "Flat").addOnClickListener(visController);
        mirrorsDropdown.addButton(IDEAL_CURVED_MIRROR_BUTTON_LABEL, "Ideal curved").addOnClickListener(visController);

        UIDropdown lensDropdown = uiController.addDropdown(LENS_DROPDOWN_LABEL, "Lens");
        lensDropdown.addButton(CIRCLE_LENS_BUTTON_LABEL, "Circle").addOnClickListener(visController);
        lensDropdown.addButton(IDEAL_LENS_BUTTON_LABEL, "Ideal").addOnClickListener(visController);

        uiController.addButton(BLOCKER_BUTTON_LABEL, "Blocker").addOnClickListener(visController);

        uiController.addExtraSpace(2);

        uiController.addButton(MOVE_BUTTON_LABEL, "Move").addOnClickListener(visController);
        uiController.addButton(DELETE_BUTTON_LABEL, "Delete").addOnClickListener(visController);

        uiController.addExtraSpace(2);

        UISlider densitySlider = uiController.addSlider(DENSITY_SLIDER_LABEL, "Ray density slider:", 200);
        densitySlider.addOnChangeListener(visController);
        densitySlider.setValue(0.5f);
    }

    private void setupVisualization() {
        visController = new VisController(this, new PVector(10, 50), TEXT_SIZE, LINE_HEIGHT, colorScheme);
    }

    @Override
    public void draw() {
        background(0);
        visController.draw();
        noStroke();
        fill(30);
        rect(0,0,width,50);
        uiController.draw();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == LEFT) {
            if (!uiController.mousePressed()) {
                visController.mousePressed();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == LEFT) {
            if (!uiController.mouseReleased()) {
                visController.mouseReleased();
            }
        }
    }

    @Override
    public void mouseWheel(MouseEvent e) {
        visController.mouseWheel(e);
    }

    public static void main(String[] args) {
        PApplet.main(GUIEntry.class.getName());
    }
}
