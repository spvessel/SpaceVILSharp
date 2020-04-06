package com.spvessel.spacevil.Decorations;

import java.util.LinkedList;
import java.util.List;

public class Figure {
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
    public Figure(boolean isFixed, List<float[]> triangles) {
            _is_fixed = isFixed;
            _figure = new LinkedList<>(triangles);
    }

    public Figure(List<float[]> triangles) {
            _figure = new LinkedList<>(triangles);
    }

    /**
     * @return new CustomFugure points list changed according to the new position (x, y)
     */
    public List<float[]> updatePosition(int x, int y) {
        List<float[]> result = new LinkedList<>();
        for (float[] item : _figure) {
            result.add(new float[] { item[0] + x, item[1] + y, 0.0f });
        }
        return result;
    }
}