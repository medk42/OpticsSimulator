package eu.medek.opticssimulator.gui.elements;

import processing.core.PApplet;
import processing.core.PVector;

public abstract class UIElement {

    // Variables
    private PVector position, dimensions;
    private String label, text;
    private int textColor, backgroundColor, strokeColor;
    private int hoverTextColor, hoverBackgroundColor, hoverStrokeColor;
    private int textSize;
    private ColorScheme colorScheme;
    PApplet pApplet;

    
    // Constructors
    public UIElement(int x, int y, int width, int height, String label, String text,
                     int textColor, int backgroundColor, int strokeColor,
                     int hoverTextColor, int hoverBackgroundColor, int hoverStrokeColor,
                     int textSize, PApplet pApplet) {
        this.position = new PVector(x, y);
        this.dimensions = new PVector(width, height);
        this.label = label;
        this.text = text;

        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.strokeColor = strokeColor;

        this.hoverTextColor = hoverTextColor;
        this.hoverBackgroundColor = hoverBackgroundColor;
        this.hoverStrokeColor = hoverStrokeColor;

        this.textSize = textSize;
        this.pApplet = pApplet;

        this.colorScheme = null;
    }

    public UIElement(int x, int y, int width, int height, String label, String text, int textSize,
                     PApplet pApplet, ColorScheme colorScheme) {
        this.position = new PVector(x, y);
        this.dimensions = new PVector(width, height);
        this.label = label;
        this.text = text;

        this.textSize = textSize;
        this.pApplet = pApplet;

        this.colorScheme = colorScheme;
    }


    // Getters
    public PVector getPosition() {
        return new PVector(position.x, position.y);
    }

    public PVector getDimensions() {
        return new PVector(dimensions.x, dimensions.y);
    }

    public String getLabel() {
        return label;
    }

    public String getText() {
        return text;
    }

    public int getTextColor() {
        if (colorScheme != null) return colorScheme.getTextColor();
        return textColor;
    }

    public int getBackgroundColor() {
        if (colorScheme != null) return colorScheme.getBackgroundColor();
        return backgroundColor;
    }

    public int getStrokeColor() {
        if (colorScheme != null) return colorScheme.getStrokeColor();
        return strokeColor;
    }

    public int getHoverTextColor() {
        if (colorScheme != null) return colorScheme.getHoverTextColor();
        return hoverTextColor;
    }

    public int getHoverBackgroundColor() {
        if (colorScheme != null) return colorScheme.getHoverBackgroundColor();
        return hoverBackgroundColor;
    }

    public int getHoverStrokeColor() {
        if (colorScheme != null) return colorScheme.getHoverStrokeColor();
        return hoverStrokeColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public PApplet getpApplet() {
        return pApplet;
    }

    public ColorScheme getColorScheme() {
        return colorScheme;
    }


    // Setters
    public void setDimensions(PVector dimensions) {
        this.dimensions = dimensions;
    }


    // Methods
    boolean checkHover(PVector mouse) {
        return mouse.x >= this.position.x &&
                mouse.x < this.position.x + this.dimensions.x &&
                mouse.y >= this.position.y &&
                mouse.y < this.position.y + this.dimensions.y;
    }

    /**
     * @return true if some UI has been triggered
     */
    public abstract boolean mousePressed();

    public abstract boolean mouseReleased();

    public abstract void draw();
}
