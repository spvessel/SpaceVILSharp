package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfacePoints;

import java.util.List;
import java.util.LinkedList;
import java.awt.Color;

/**
 * PointsContainer is class for rendering points in graph.
 */
public class PointsContainer extends Primitive implements InterfacePoints {
    private static int count = 0;

    /**
     * Default PointsContainer constructor.
     */
    public PointsContainer() {
        setItemName("PointsContainer_" + count);
        setBackground(0, 0, 0, 0);
        count++;
    }

    private float _thickness = 1.0f;

    /**
     * Setting thickness of points.
     * 
     * @param thickness Point thickness.
     */
    public void setPointThickness(float thickness) {
        _thickness = thickness;
    }

    /**
     * Getting points thickness.
     * 
     * @return Point thickness.
     */
    public float getPointThickness() {
        return _thickness;
    }

    private Color _color = new Color(255, 255, 255);

    /**
     * Setting points color. Default: WHITE.
     * 
     * @param color Points color.
     */
    public void setPointColor(Color color) {
        _color = color;
    }

    /**
     * Getting points color.
     * 
     * @return Points color.
     */
    public Color getPointColor() {
        return _color;
    }

    private List<float[]> _pointShape;

    /**
     * Setting custom shape for points (if one want to use other shape than circle).
     * 
     * @param shape Points list of the shape as List of float[2] array.
     */
    public void setPointShape(List<float[]> shape) {
        if (shape == null)
            return;
        _pointShape = shape;

    }

    /**
     * Getting current shape of points. Default: circle shape.
     * 
     * @return Points list as List of float[2] array.
     */
    public List<float[]> getPointShape() {
        if (_pointShape == null)
            _pointShape = GraphicsMathService.getEllipse(getPointThickness() / 2.0f, 16);
        return _pointShape;
    }

    /**
     * Setting points coordinates.
     * 
     * @param coord Points list as List of float[2] array.
     */
    public void setPoints(List<float[]> coord) {
        List<float[]> tmp = new LinkedList<>(coord);
        setTriangles(tmp);
    }

    /**
     * Overridden method for stretching the points position relative to the current
     * size of the item. Use in conjunction with getTriangles() and setTriangles()
     * methods.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void makeShape() {
        if (getTriangles() != null)
            setTriangles(updateShape());
    }

    /**
     * Getting points coordinates.
     * 
     * @return Points list as List of float[2] array.
     */
    @Override
    public List<float[]> getPoints() {
        if (getTriangles() == null || getTriangles().size() < 2)
            return null;
        return getTriangles();
    }
}