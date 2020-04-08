package com.spvessel.spacevil.Decorations;

import java.util.LinkedList;
import java.util.List;

/**
 * Figure class represents any triangulated shape. It is used to draw any types of custom shapes.
 */
public class Figure {
    private List<float[]> _figure;

    /**
     * Getting list of pairs - [x, y] coordinates of a shape.
     * @return Figure points list as List of float[2] array.
     */
    public List<float[]> getFigure() {
        return _figure;
    }

    private boolean _is_fixed = false;

    /**
     * Is Figure fixed.
     * @return True: if shape can not be resized. False: if shape can be resised.
     */
    public boolean isFixed() {
        return _is_fixed;
    }

    /**
     * Constructs a Figure with specified triangles and specified fixed flag.
     * @param isFixed True: if shape can not be resized. False: if shape can be resised.
     * @param triangles Triangles list of the Figure's shape.
     */
    public Figure(boolean isFixed, List<float[]> triangles) {
            _is_fixed = isFixed;
            _figure = new LinkedList<>(triangles);
    }

    /**
     * Constructs a Figure with specified triangles.
     * @param triangles Triangles list of the Figure's shape.
     */
    public Figure(List<float[]> triangles) {
            _figure = new LinkedList<>(triangles);
    }

    /**
     * Updating the coordinates of triangles with specified shifts along the X and Y axis.
     * @param x Shift by X-axis.
     * @param y Shift by Y-axis.
     * @return Updated points list changed according to the new shift by (x, y)
     */
    public List<float[]> updatePosition(int x, int y) {
        List<float[]> result = new LinkedList<>();
        for (float[] item : _figure) {
            result.add(new float[] { item[0] + x, item[1] + y, 0.0f });
        }
        return result;
    }
}