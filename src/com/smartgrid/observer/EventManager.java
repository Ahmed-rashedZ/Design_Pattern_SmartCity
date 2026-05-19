package com.smartgrid.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * PATTERN 1: Observer
 * This is the Event Manager (Publisher) that sends messages to all observers.
 */
public class EventManager {
    private List<GridObserver> observers = new ArrayList<>();

    public void subscribe(GridObserver observer) {
        observers.add(observer);
    }

    public void notify(String message) {
        for (GridObserver obs : observers) {
            obs.onGridUpdate(message);
        }
    }
}
