/*
 * OpticsSimulator
 * Jakub Medek, I. ročník, IOI MFF UK
 * letní semestr 2018/19
 * Programování II (NPRG031)
 */

package eu.medek.opticssimulator.gui.visualization.interfaces;

import processing.core.PVector;

public interface MouseListeners {
    boolean mousePressed(PVector mouse);
    boolean mouseReleased();
}
