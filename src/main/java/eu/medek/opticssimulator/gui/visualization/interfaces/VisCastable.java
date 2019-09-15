/*
 * OpticsSimulator
 * Jakub Medek, I. ročník, IOI MFF UK
 * letní semestr 2018/19
 * Programování II (NPRG031)
 */

package eu.medek.opticssimulator.gui.visualization.interfaces;

import eu.medek.opticssimulator.logic.reflectables.Reflectable;
import processing.core.PVector;

import java.util.List;

public interface VisCastable extends MouseListeners {
    void updateScreenEdges(PVector screenTopLeft, PVector screenBottomRight);
    void update(List<Reflectable> reflectables, PVector mouse);
}
