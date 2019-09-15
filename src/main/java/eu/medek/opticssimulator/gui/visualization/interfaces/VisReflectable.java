/*
 * OpticsSimulator
 * Jakub Medek, I. ročník, IOI MFF UK
 * letní semestr 2018/19
 * Programování II (NPRG031)
 */

package eu.medek.opticssimulator.gui.visualization.interfaces;

import eu.medek.opticssimulator.logic.reflectables.Reflectable;
import processing.core.PVector;

public interface VisReflectable extends MouseListeners {
    void update(PVector mouse);
    Reflectable getReflectable();
}
