package com.example.demo.widget.colorpicker;

import java.util.ArrayList;
import java.util.List;

class ColorObservableEmitter implements ColorObservable {

    private List<ColorObserver> observers = new ArrayList<>();
    private int color;

    @Override
    public void subscribe(ColorObserver observer) {
        if (observer == null) return;
        observers.add(observer);
    }

    @Override
    public void unsubscribe(ColorObserver observer) {
        if (observer == null) return;
        observers.remove(observer);
    }

    @Override
    public int getColor() {
        return color;
    }

    void onColor(int color, boolean fromUser, boolean shouldPropagate) {
        if (color == 0) return;
        this.color = color;
        for (ColorObserver observer : observers) {
            observer.onColor(color, fromUser, shouldPropagate);
        }
    }

}