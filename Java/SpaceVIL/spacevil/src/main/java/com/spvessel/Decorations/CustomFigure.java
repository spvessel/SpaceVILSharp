package com.spvessel.Decorations;

import java.util.LinkedList;
import java.util.List;

public class CustomFigure {
    private List<float[]> _figure;

    public List<float[]> getFigure() {
        return _figure;
    }

    private boolean _is_fixed = false;

    public boolean isFixed() {
        return _is_fixed;
    }

    public CustomFigure(boolean isFixed, List<float[]> triangles) {
        _is_fixed = isFixed;
        _figure = triangles;
    }

    public List<float[]> updatePosition(int _x, int _y) {
        List<float[]> result = new LinkedList<>();
        for (float[] item : _figure) {
            result.add(new float[] { item[0] + _x, item[1] + _y, 0.0f });
        }
        return result;
    }
}