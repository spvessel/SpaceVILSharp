package com.spvessel.spacevil.Decorations;

import java.util.LinkedList;
import java.util.List;

public class CustomFigure {
    private List<float[]> _figure;

    /**
     * @return CustomFigure points list
     */
    public List<float[]> getFigure() {
        return _figure;
    }

    private boolean _is_fixed = false;

    /**
     * Is CustomFigure fixed
     */
    public boolean isFixed() {
        return _is_fixed;
    }

    /**
     * Constructs a CustomFigure
     * @param isFixed is CustomFigure fixed
     * @param triangles Triangles list of the CustomFigure's shape
     */
    public CustomFigure(boolean isFixed, List<float[]> triangles) {
        synchronized (this) {
            _is_fixed = isFixed;
            _figure = new LinkedList<>(triangles);
        }
    }

    /**
     * @return new CustomFugure points list changed according to the new position (x, y)
     */
    public List<float[]> updatePosition(int _x, int _y) {
        List<float[]> result = new LinkedList<>();
        for (float[] item : _figure) {
            result.add(new float[] { item[0] + _x, item[1] + _y, 0.0f });
        }
        return result;
    }
}