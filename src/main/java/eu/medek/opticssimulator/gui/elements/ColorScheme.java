/*
 * OpticsSimulator
 * Jakub Medek, I. ročník, IOI MFF UK
 * letní semestr 2018/19
 * Programování II (NPRG031)
 */

package eu.medek.opticssimulator.gui.elements;

public class ColorScheme {

    // Variables
    private int textColor, backgroundColor, strokeColor;
    private int hoverTextColor, hoverBackgroundColor, hoverStrokeColor;


    // Constructors
    public ColorScheme(int textColor, int backgroundColor, int strokeColor,
                       int hoverTextColor, int hoverBackgroundColor, int hoverStrokeColor) {
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.strokeColor = strokeColor;

        this.hoverTextColor = hoverTextColor;
        this.hoverBackgroundColor = hoverBackgroundColor;
        this.hoverStrokeColor = hoverStrokeColor;
    }


    // Getters
    public int getTextColor() {
        return textColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public int getHoverTextColor() {
        return hoverTextColor;
    }

    public int getHoverBackgroundColor() {
        return hoverBackgroundColor;
    }

    public int getHoverStrokeColor() {
        return hoverStrokeColor;
    }


    // Setters
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public void setHoverTextColor(int hoverTextColor) {
        this.hoverTextColor = hoverTextColor;
    }

    public void setHoverBackgroundColor(int hoverBackgroundColor) {
        this.hoverBackgroundColor = hoverBackgroundColor;
    }

    public void setHoverStrokeColor(int hoverStrokeColor) {
        this.hoverStrokeColor = hoverStrokeColor;
    }
}
