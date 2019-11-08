package com.spvessel.spacevil;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

class CustomSelector extends Primitive {
    private static int count = 0;

    /**
     * Constructs a CustomSelector
     */
    public CustomSelector() {
        super("CustomSelector_" + count);
        count++;
    }

    /**
     * Make CustomSelector's rectangles with left top and right bottom points
     */
    public void setRectangles(List<Point> points) {
        List<float[]> triangles = new LinkedList<>();
        int w1 = 0, w2 = 0;
        int h1 = 0, h2 = 0;
        int rectX = 0, rectY = 0;
        if (points != null && points.size() > 0) {
            w1 = points.get(0).x;
            w2 = w1;
            h1 = points.get(0).y;
            h2 = h1;
            rectX = points.get(0).x;
            rectY = points.get(1).y;
            for (int i = 0; i < points.size() / 2; i++) {
                Point p1 = points.get(i * 2 + 0);
                Point p2 = points.get(i * 2 + 1);
                triangles.addAll(
                        GraphicsMathService.getRectangle((p2.x - p1.x), (p2.y - p1.y), p1.x - rectX, p2.y - rectY));
                w1 = (p1.x < w1) ? p1.x : w1;
                w2 = (p2.x > w2) ? p2.x : w2;
                h1 = (p1.y < h1) ? p1.y : h1;
                h2 = (p2.y > h2) ? p2.y : h2;
            }
        }
        setX(rectX);
        setY(rectY);
        setWidth(w2 - w1);
        setHeight(h2 - h1);
        setTriangles(triangles);
        ItemsRefreshManager.setRefreshShape(this);
    }

    /**
     * Shift selector on Y direction
     */
    public void shiftAreaY(int yShift) {
        List<float[]> triangles = getTriangles();
        if (triangles == null || triangles.size() == 0)
            return;

        for (float[] xyz : triangles) {
            xyz[1] += yShift;
        }

        setTriangles(triangles);
    }

    /**
     * Shift selector on X direction
     */
    public void shiftAreaX(int xShift) {
        List<float[]> triangles = getTriangles();
        if (triangles == null || triangles.size() == 0)
            return;

        for (float[] xyz : triangles) {
            xyz[0] += xShift;
        }

        setTriangles(triangles);
    }
}