package eu.medek.opticssimulator.gui.elements;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.LinkedList;
import java.util.List;

public class UIController {

    // Variables
    private List<UIElement> elements;
    private PApplet pApplet;

    private int defaultHeight;
    private int defaultTextSize;
    private int defaultSpace;
    private ColorScheme defaultColorScheme;
    private PVector defaultStartingPoint;
    private PVector nextStartingPoint = null;


    // Constructors
    public UIController(PApplet pApplet) {
        this.elements = new LinkedList<>();
        this.pApplet = pApplet;
    }


    // Getters
    public List<UIElement> getElements() {
        return elements;
    }


    // Methods
    public void addElement(UIElement element) {
        this.elements.add(element);
    }

    public boolean mousePressed() {
        boolean uiUsed = false;
        for (UIElement element : elements) if (element.mousePressed()) uiUsed = true;
        return uiUsed;
    }

    public boolean mouseReleased() {
        boolean uiUsed = false;
        for (UIElement element : elements) if (element.mouseReleased()) uiUsed = true;
        return uiUsed;
    }

    public void draw() {
        elements.forEach(UIElement::draw);
    }

    /**
     * Set default values for creating UI Elements using UIController
     */
    public void setupFactory(PVector defaultStartingPoint, int defaultHeight, int defaultSpace, int defaultTextSize, ColorScheme defaultColorScheme) {
        this.defaultStartingPoint = new PVector(defaultStartingPoint.x, defaultStartingPoint.y);
        this.nextStartingPoint = new PVector(defaultStartingPoint.x, defaultStartingPoint.y);
        this.defaultColorScheme = defaultColorScheme;
        this.defaultHeight = defaultHeight;
        this.defaultSpace = defaultSpace;
        this.defaultTextSize = defaultTextSize;
    }

    /**
     * Creates button, so it fits nicely next to the last element
     * @return the newly created button
     * @throws RuntimeException if function is called before calling {@link UIController#setupFactory(PVector, int, int, int, ColorScheme)} method
     */
    public UIButton addButton(String label, String text) {
        if (nextStartingPoint == null) throw new RuntimeException("You first need to set the factory up using setupFactory function!");

        pApplet.textSize(defaultTextSize);
        int width = (int) pApplet.textWidth(text) + 15;

        if (nextStartingPoint.x + width + defaultSpace > pApplet.width) {
            nextStartingPoint.x = defaultStartingPoint.x;
            nextStartingPoint.y = defaultStartingPoint.y + defaultHeight + defaultSpace;
        }

        UIButton newButton = new UIButton((int) nextStartingPoint.x, (int) nextStartingPoint.y, width, defaultHeight, label, text, defaultTextSize, pApplet, defaultColorScheme);
        this.addElement(newButton);
        nextStartingPoint.x += width + defaultSpace;
        return newButton;
    }

    public UIDropdown addDropdown(String label, String text) {
        if (nextStartingPoint == null) throw new RuntimeException("You first need to set the factory up using setupFactory function!");

        pApplet.textSize(defaultTextSize);
        int width = (int) pApplet.textWidth(text) + 15;

        if (nextStartingPoint.x + width + defaultSpace > pApplet.width) {
            nextStartingPoint.x = defaultStartingPoint.x;
            nextStartingPoint.y = defaultStartingPoint.y + defaultHeight + defaultSpace;
        }

        UIDropdown newDropdown = new UIDropdown((int) nextStartingPoint.x, (int) nextStartingPoint.y, width, defaultHeight, label, text, defaultTextSize, pApplet, defaultColorScheme);
        this.addElement(newDropdown);
        nextStartingPoint.x += width + defaultSpace;
        return newDropdown;
    }

    public UISlider addSlider(String label, String text, int width) {
        pApplet.textSize(defaultTextSize);
        width += (int) pApplet.textWidth(text) + 7;

        if (nextStartingPoint == null) throw new RuntimeException("You first need to set the factory up using setupFactory function!");

        if (nextStartingPoint.x + width + defaultSpace > pApplet.width) {
            nextStartingPoint.x = defaultStartingPoint.x;
            nextStartingPoint.y = defaultStartingPoint.y + defaultHeight + defaultSpace;
        }

        UISlider newSlider = new UISlider((int) nextStartingPoint.x, (int) nextStartingPoint.y, width, defaultHeight, label, text, defaultTextSize, pApplet, defaultColorScheme);
        this.addElement(newSlider);
        nextStartingPoint.x += width + defaultSpace;
        return newSlider;
    }

    /**
     * Adds extra defaultSpace
     * @param howMany how many spaces will be added
     */
    public void addExtraSpace(int howMany) {
        nextStartingPoint.x += defaultSpace * howMany;
    }
}
