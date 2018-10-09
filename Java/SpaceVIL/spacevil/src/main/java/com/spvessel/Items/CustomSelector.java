package com.spvessel.Items;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import com.spvessel.Engine.GraphicsMathService;

public class CustomSelector extends Primitive {
    static int count = 0;

    public CustomSelector() {
        super("CustomSelector_" + count);
        count++;
    }

    @Override
    public List<float[]> makeShape() {
        return GraphicsMathService.toGL(this, getHandler());
    }

    public void setRectangles(List<Point> points) {
        List<float[]> triangles = new LinkedList<>();
        int w1 = 0, w2 = 0;
        int h1 = 0, h2 = 0;
        if (points != null) {
            w1 = points.get(0).x;
            w2 = w1;
            h1 = points.get(0).y;
            h2 = h1;
            for (int i = 0; i < points.size() / 2; i++) {
                Point p1 = points.get(i * 2 + 0);
                Point p2 = points.get(i * 2 + 1);
                //System.out.println((p2.x - p1.x) + " " + (p2.y - p1.y) + " " + p1.x + " " + p2.y);
                triangles.addAll(GraphicsMathService.getRectangle((p2.x - p1.x), (p2.y - p1.y), p1.x, p2.y));
                w1 = (p1.x < w1) ? p1.x : w1;
                w2 = (p2.x > w2) ? p2.x : w2;
                h1 = (p1.y < h1) ? p1.y : h1;
                h2 = (p2.y > h2) ? p2.y : h2;
            }
        }
        setTriangles(triangles);
        setWidth(w2 - w1);
        setHeight(h2 - h1);
    }
}