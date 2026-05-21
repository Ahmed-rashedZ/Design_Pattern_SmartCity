package com.smartgrid.observer;

/**
 * PATTERN 1: Observer
 * This is the listener interface.
 */
public interface GridObserver {
    void onGridUpdate(String message);
}
