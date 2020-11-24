package com.spvessel.spacevil.Core;

public final class ScrollValue {

    public double dX = 0;
    public double dY = 0;

    public void setValues(double dx, double dy) {
        dX = dx;
        dY = dy;
    }

    @Override
    public String toString() {
        return dX + " " + dY;
    }
}