package com.spvessel.spacevil;

import java.awt.Color;
import java.util.List;

import com.spvessel.spacevil.Flags.SizePolicy;

/**
 * Graph is class representing custom graphs with lines and points.
 * <p>
 * Contains com.spvessel.spacevil.PointsContainer and
 * com.spvessel.spacevil.LinesContainer.
 * <p>
 * Supports all events except drag and drop.
 */
public class Graph extends Prototype {
    private static int count = 0;

    PointsContainer points;
    LinesContainer lines;

    /**
     * Property to enable or disable mouse events (hover, click, press, scroll).
     * <p>
     * True: Graph can receive mouse events. False: cannot receive mouse events.
     * <p>
     * Default: False.
     */
    public boolean isHover = false;

    @Override
    protected boolean getHoverVerification(float xpos, float ypos) {
        if (isHover)
            return super.getHoverVerification(xpos, ypos);
        return false;
    }

    /**
     * Default Graph constructor. The ability to get focus is disabled by default.
     */
    public Graph() {
        setItemName("Graph_" + count);
        setBackground(0, 0, 0, 0);
        count++;

        points = new PointsContainer();
        lines = new LinesContainer();

        isFocusable = false;
    }

    /**
     * Constructs Graph with the ability to enable or disable mouse events.
     * 
     * @param hover True: Graph can receive mouse events. False: cannot receive
     *              mouse events.
     */
    public Graph(boolean hover) {
        this();
        isHover = hover;
    }

    /**
     * Initializing all elements in the Graph.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        lines.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        points.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        addItems(lines, points);
    }

    /**
     * Setting thickness of Graph points.
     * 
     * @param thickness Point thickness.
     */
    public void setPointThickness(float thickness) {
        points.setPointThickness(thickness);
    }

    /**
     * Getting Graph points thickness.
     * 
     * @return Point thickness.
     */
    public float getPointThickness() {
        return points.getPointThickness();
    }

    /**
     * Setting Graph points color. Default: WHITE.
     * 
     * @param color Points color.
     */
    public void setPointColor(Color color) {
        points.setPointColor(color);
    }

    /**
     * Getting Graph points color.
     * 
     * @return Points color.
     */
    public Color getPointColor() {
        return points.getPointColor();
    }

    /**
     * Setting custom shape for points (if one want to use other shape than circle).
     * 
     * @return Points list of the shape as List of float[2] array.
     */
    public void setPointShape(List<float[]> shape) {
        points.setPointShape(shape);
    }

    /**
     * Getting Graph points coordinates.
     * 
     * @return Points list as List of float[2] array.
     */
    public List<float[]> getPointShape() {
        return points.getPointShape();
    }

    /**
     * Setting Graph points coordinates.
     * 
     * @return Points list as List of float[2] array.
     */
    public void setPointsCoord(List<float[]> coord) {
        points.setPoints(coord);
        lines.setPoints(coord);
    }

    /**
     * Getting Graph points coordinates.
     * 
     * @return Points list as List of float[2] array.
     */
    public List<float[]> getPointsCoord() {
        return points.getTriangles();
    }

    /**
     * Setting Graph thickness of lines.
     * 
     * @return Line thickness.
     */
    public void setLineThickness(float thickness) {
        lines.setLineThickness(thickness);
    }

    /**
     * Getting Graph lines thickness.
     * 
     * @return Lines thickness.
     */
    public float getLineThickness() {
        return lines.getLineThickness();
    }

    /**
     * Setting Graph lines color.
     * 
     * @return Line color.
     */
    public void setLineColor(Color color) {
        lines.setLineColor(color);
    }

    /**
     * Getting Graph lines color.
     * 
     * @return Lines color.
     */
    public Color getLineColor() {
        return lines.getLineColor();
    }
}