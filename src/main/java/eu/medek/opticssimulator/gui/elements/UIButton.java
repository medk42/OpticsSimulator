package eu.medek.opticssimulator.gui.elements;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.LinkedList;
import java.util.List;

public class UIButton extends UIElement {

    // Variables
    private List<OnClickListener> onClickListeners;


    // Constructors
    public UIButton(int x, int y, int width, int height,
                    String label, String text,
                    int textColor, int backgroundColor, int strokeColor,
                    int hoverTextColor, int hoverBackgroundColor, int hoverStrokeColor,
                    int textSize, PApplet pApplet) {
        super(x, y, width, height,
                label, text,
                textColor, backgroundColor, strokeColor,
                hoverTextColor, hoverBackgroundColor, hoverStrokeColor,
                textSize, pApplet);

        onClickListeners = new LinkedList<>();
    }

    public UIButton(int x, int y, int width, int height, String label, String text, int textSize,
                     PApplet pApplet, ColorScheme colorScheme) {
        super(x, y, width, height, label, text, textSize, pApplet, colorScheme);

        onClickListeners = new LinkedList<>();
    }


    // Methods
    public void addOnClickListener(OnClickListener onClickListener) {
        this.onClickListeners.add(onClickListener);
    }

    @Override
    public boolean mousePressed() {
        PVector mouse = new PVector(pApplet.mouseX, pApplet.mouseY);
        if (this.checkHover(mouse)) {
            onClickListeners.forEach(listener -> listener.onClick(this));
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased() {
        return false;
    }

    public void draw() {
        PVector mouse = new PVector(pApplet.mouseX, pApplet.mouseY);
        boolean hover = this.checkHover(mouse);

        if (hover) {
            pApplet.fill(super.getHoverBackgroundColor());
            pApplet.stroke(super.getHoverStrokeColor());
        } else {
            pApplet.fill(super.getBackgroundColor());
            pApplet.stroke(super.getStrokeColor());
        }
        pApplet.rect(super.getPosition().x, super.getPosition().y, super.getDimensions().x, super.getDimensions().y);

        pApplet.textAlign(PApplet.CENTER, PApplet.CENTER);
        pApplet.textSize(super.getTextSize());
        if (hover) {
            pApplet.fill(super.getHoverTextColor());
            pApplet.stroke(super.getHoverTextColor());
        } else {
            pApplet.fill(super.getTextColor());
            pApplet.stroke(super.getTextColor());
        }

        PVector middle = PVector.add(super.getPosition(), PVector.div(super.getDimensions(), 2));
        pApplet.text(super.getText(), middle.x, middle.y);
    }
}
