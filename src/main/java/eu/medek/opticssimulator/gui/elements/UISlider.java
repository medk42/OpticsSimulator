package eu.medek.opticssimulator.gui.elements;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.LinkedList;
import java.util.List;

public class UISlider extends UIElement {

    // Variables
    private List<OnChangeListener> onChangeListeners;
    private double value;

    private boolean holding = false;
    private int sliderDeltaX;

    private static final double PRECISION_ERROR = 0.00001;


    // Constructors
    public UISlider(int x, int y, int width, int height,
                    String label, String text,
                    int textColor, int backgroundColor, int strokeColor,
                    int hoverTextColor, int hoverBackgroundColor, int hoverStrokeColor,
                    int textSize, PApplet pApplet) {
        super(x, y, width, height,
                label, text,
                textColor, backgroundColor, strokeColor,
                hoverTextColor, hoverBackgroundColor, hoverStrokeColor,
                textSize, pApplet);

        pApplet.textSize(textSize);
        sliderDeltaX = (int) pApplet.textWidth(text) + 7;
        onChangeListeners = new LinkedList<>();
    }

    public UISlider(int x, int y, int width, int height, String label, String text, int textSize,
                    PApplet pApplet, ColorScheme colorScheme) {
        super(x, y, width, height, label, text, textSize, pApplet, colorScheme);

        pApplet.textSize(textSize);
        sliderDeltaX = (int) pApplet.textWidth(text) + 7;
        onChangeListeners = new LinkedList<>();
    }


    // Methods
    public void addOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListeners.add(onChangeListener);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
        this.onChangeListeners.forEach(listener -> listener.onChange(this, this.value));
    }

    @Override
    public boolean mousePressed() {
        PVector mouse = new PVector(pApplet.mouseX, pApplet.mouseY);
        if (this.checkHover(mouse)) {
            calculateNewValueCallListeners(mouse);
            if (PVector.dist(mouse, getValueCenter()) <= super.getDimensions().y / 2f) holding = true;
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseReleased() {
        holding = false;
        return false;
    }

    @Override
    public void draw() {
        PVector mouse = new PVector(pApplet.mouseX, pApplet.mouseY);

        if (holding) calculateNewValueCallListeners(mouse);

        PVector valueCenter = this.getValueCenter();
        boolean hover = PVector.dist(mouse, valueCenter) <= super.getDimensions().y / 2f;

        pApplet.fill(super.getBackgroundColor());
        pApplet.stroke(super.getStrokeColor());

        float x = super.getPosition().x + sliderDeltaX + super.getDimensions().y / 2f;
        float y = super.getPosition().y + super.getDimensions().y * 3f / 8f;
        float width = super.getDimensions().x - sliderDeltaX - super.getDimensions().y;
        float height = super.getDimensions().y / 4f;
        float curvature = super.getDimensions().y / 4f;

        pApplet.rect(x, y, width, height, curvature);

        if (hover) {
            pApplet.fill(super.getHoverBackgroundColor());
            pApplet.stroke(super.getHoverStrokeColor());
        } else {
            pApplet.fill(super.getBackgroundColor());
            pApplet.stroke(super.getStrokeColor());
        }
        pApplet.ellipseMode(PApplet.CENTER);
        pApplet.ellipse(valueCenter.x, valueCenter.y, super.getDimensions().y, super.getDimensions().y);


        pApplet.textAlign(PApplet.LEFT, PApplet.CENTER);
        pApplet.textSize(super.getTextSize());
        if (hover) {
            pApplet.fill(super.getHoverTextColor());
            pApplet.stroke(super.getHoverTextColor());
        } else {
            pApplet.fill(super.getTextColor());
            pApplet.stroke(super.getTextColor());
        }

        PVector middleLeft = new PVector(super.getPosition().x, super.getPosition().y + super.getDimensions().y / 2f);
        pApplet.text(super.getText(), middleLeft.x, middleLeft.y);



    }

    private PVector getValueCenter() {
        return new PVector(super.getPosition().x + sliderDeltaX + super.getDimensions().y/2f + (super.getDimensions().x - sliderDeltaX - super.getDimensions().y) * (float) this.value,
                          super.getPosition().y + super.getDimensions().y / 2f);
    }

    private double calculateNewValue(PVector mouse) {
        return (double) (mouse.x - (super.getPosition().x + sliderDeltaX + super.getDimensions().y/2f)) / (super.getDimensions().x - sliderDeltaX - super.getDimensions().y);
    }

    private void calculateNewValueCallListeners(PVector mouse) {
        double newValue = calculateNewValue(mouse);
        if (!compareDouble(newValue, this.value)) {
            this.value = newValue;
            if (this.value < 0) this.value = 0;
            else if (this.value > 1) this.value = 1;
            this.onChangeListeners.forEach(listener -> listener.onChange(this, this.value));
        }
    }

    private boolean compareDouble(double a, double b) {
        return (Math.abs(a - b) < PRECISION_ERROR);
    }
}
