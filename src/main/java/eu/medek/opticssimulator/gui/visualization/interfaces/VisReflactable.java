/*
 * OpticsSimulator
 * Jakub Medek, I. ročník, IOI MFF UK
 * letní semestr 2018/19
 * Programování II (NPRG031)
 */

package eu.medek.opticssimulator.gui.visualization.interfaces;

import eu.medek.opticssimulator.logic.reflectables.Reflactable;
import processing.core.PVector;

public interface VisReflactable extends MouseListeners {
    void update(PVector mouse);
    Reflactable getReflactable();
}
