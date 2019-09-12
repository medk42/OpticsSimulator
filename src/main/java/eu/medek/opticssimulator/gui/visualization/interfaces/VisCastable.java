package eu.medek.opticssimulator.gui.visualization.interfaces;

import eu.medek.opticssimulator.logic.reflectables.Reflactable;
import processing.core.PVector;

import java.util.List;

public interface VisCastable extends MouseListeners {
    void updateScreenEdges(PVector screenTopLeft, PVector screenBottomRight);
    void update(List<Reflactable> reflactables, PVector mouse);
}