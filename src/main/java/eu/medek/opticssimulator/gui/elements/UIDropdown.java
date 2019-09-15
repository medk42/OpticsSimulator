/*
 * OpticsSimulator
 * Jakub Medek, I. ročník, IOI MFF UK
 * letní semestr 2018/19
 * Programování II (NPRG031)
 */

package eu.medek.opticssimulator.gui.elements;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.LinkedList;
import java.util.List;

public class UIDropdown extends UIElement {

    // Variables
    private List<UIButton> buttons;
    private PVector nextStartingPoint;
    private boolean hover = false;
    private int defaultWidth;


    // Constructors
    public UIDropdown(int x, int y, int width, int height,
                      String label, String text,
                      int textColor, int backgroundColor, int strokeColor,
                      int hoverTextColor, int hoverBackgroundColor, int hoverStrokeColor,
                      int textSize, PApplet pApplet) {
        super(x, y, width, height,
                label, text,
                textColor, backgroundColor, strokeColor,
                hoverTextColor, hoverBackgroundColor, hoverStrokeColor,
                textSize, pApplet);

        nextStartingPoint = new PVector(x, y + height);
        buttons = new LinkedList<>();
        defaultWidth = width;
    }

    public UIDropdown(int x, int y, int width, int height, String label, String text, int textSize,
                      PApplet pApplet, ColorScheme colorScheme) {
        super(x, y, width, height, label, text, textSize, pApplet, colorScheme);

        nextStartingPoint = new PVector(x, y + height);
        buttons = new LinkedList<>();
        defaultWidth = width;
    }


    // Methods

    /**
     * Add button with specified label and text to the dropdown and return it
     */
    public UIButton addButton(String label, String text) {
        pApplet.textSize(super.getTextSize());
        int width = (int) pApplet.textWidth(text) + 15;
        if (width > defaultWidth) {
            defaultWidth = width;
            buttons.forEach(button -> button.setDimensions(new PVector(defaultWidth, button.getDimensions().y)));
        }

        UIButton newButton = new UIButton((int) nextStartingPoint.x, (int) nextStartingPoint.y,
                    defaultWidth, (int) super.getDimensions().y,
                    label, text, super.getTextSize(), pApplet, super.getColorScheme());
        nextStartingPoint.y += super.getDimensions().y;
        this.buttons.add(newButton);

        return newButton;
    }

    @Override
    public boolean mousePressed() {
        boolean uiUsed = false;

        if (hover) {
            for (UIButton button : buttons) if (button.mousePressed()) uiUsed = true;
        }

        return uiUsed;
    }

    @Override
    public boolean mouseReleased() {
        hover = false;
        return false;
    }

    @Override
    public void draw() {
        PVector mouse = new PVector(pApplet.mouseX, pApplet.mouseY);
        boolean actHover = this.checkHover(mouse);
        if (actHover) hover = true;
        for (UIButton button : buttons) if (button.checkHover(mouse)) actHover = true;
        if (!actHover) hover = false;

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

        if (hover) buttons.forEach(UIButton::draw);
    }
}
