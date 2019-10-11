package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Decorations.CornerRadius;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public final class GraphicsMathService {
    /**
     * Class with some functions for constructing custom figures
     */

    private GraphicsMathService() {
    }

    /**
     * Mix two or more colors
     */
    public static Color mixColors(Color... colors) {
        if (colors.length == 0)
            return new Color(255, 255, 255);
        if (colors.length == 1)
            return colors[0];

        float r = 0, g = 0, b = 0, a = 0.0f;

        for (Color item : colors) {
            if (item == null || item.getAlpha() == 0)
                continue;

            if (item.getAlpha() == 255) // exchange
            {
                r = item.getRed();
                g = item.getGreen();
                b = item.getBlue();
                a = 255.0f;
            } else { // mixing
                float alpha = item.getAlpha() / 255.0f;
                float notAlpha = 1.0f - alpha;

                if (r == 0.0f)
                    r = item.getRed();
                else
                    r = r * notAlpha + item.getRed() * alpha;

                if (g == 0.0f)
                    g = item.getGreen();
                else
                    g = g * notAlpha + item.getGreen() * alpha;

                if (b == 0.0f)
                    b = item.getBlue();
                else
                    b = b * notAlpha + item.getBlue() * alpha;

                if (a == 0.0f)
                    a = item.getAlpha();
                else if (a < 255.0f)
                    a = a * notAlpha + item.getAlpha() * alpha;
            }
        }
        return new Color((int) r, (int) g, (int) b, (int) a);
    }

    public static Color cloneColor(Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    static List<float[]> toGL(InterfaceBaseItem item, CoreWindow handler) // where TLayout : VisualItem
    {
        if (item.getTriangles() == null)
            return null;

        List<float[]> result = new LinkedList<>();

        for (float[] vector : item.getTriangles()) {
            float x = (vector[0] / (float) handler.getWidth()) * 2.0f - 1.0f;
            float y = (vector[1] / (float) handler.getHeight() * 2.0f - 1.0f) * (-1.0f);
            result.add(new float[] { x, y, vector[2] });
        }
        return result;
    }

    static List<float[]> toGL(List<float[]> triangles, CoreWindow handler) // where TLayout : VisualItem
    {
        List<float[]> result = new LinkedList<>();

        for (float[] vector : triangles) {
            float x = (vector[0] / (float) handler.getWidth()) * 2.0f - 1.0f;
            float y = (vector[1] / (float) handler.getHeight() * 2.0f - 1.0f) * (-1.0f);
            result.add(new float[] { x, y, vector[2] });
        }
        return result;
    }

    static float[] toGL(float[] triangles, CoreWindow handler) // where TLayout : VisualItem
    {
        for (int i = 0; i < triangles.length / 2; i++) {
            triangles[i * 2 + 0] /= (((float) handler.getWidth()) * 2.0f - 1.0f);
            triangles[i * 2 + 1] /= (((float) handler.getHeight() * 2.0f - 1.0f) * (-1.0f));
        }
        return triangles;
    }

    /**
     * Make a rectangle with roundness corners
     * 
     * @param cornerRadius radius values for all corners
     * @param width        rectangle width
     * @param height       rectangle height
     * @param x            X position (left top corner) of the result object
     * @param y            Y position (left top corner) of the result object
     * @return Points list of the rectangle with roundness corners
     */
    public static List<float[]> getRoundSquare(CornerRadius cornerRadius, float width, float height, int x, int y) {
        if (width <= 0 || height <= 0)
            return null;

        if (cornerRadius == null)
            cornerRadius = new CornerRadius();
        else {
            if (cornerRadius.leftTop < 0)
                cornerRadius.leftTop = 0;
            if (cornerRadius.rightTop < 0)
                cornerRadius.rightTop = 0;
            if (cornerRadius.leftBottom < 0)
                cornerRadius.leftBottom = 0;
            if (cornerRadius.rightBottom < 0)
                cornerRadius.rightBottom = 0;
        }

        if (cornerRadius.isCornersZero()) {
            return getRectangle(width, height, x, y);
        }

        List<float[]> triangles = new LinkedList<>();
        // Начало координат в левом углу

        // 1
        triangles.addAll(RectToTri(cornerRadius.leftTop + x, y, width / 2f + x, height / 2f + y));
        // triangles.addAll(RectToTri(0.0f + x, cornerRadius.leftTop + y, cornerRadius.leftTop + x, height / 2f + y));

        // 2
        triangles.addAll(RectToTri(width / 2f + x, y, width - cornerRadius.rightTop + x, height / 2f + y));
        // triangles.addAll(
        //         RectToTri(width - cornerRadius.rightTop + x, cornerRadius.rightTop + y, width + x, height / 2f + y));

        // 3
        triangles.addAll(RectToTri(cornerRadius.leftBottom + x, height / 2f + y, width / 2f + x, height + y));
        // triangles.addAll(
        // RectToTri(0 + x, height / 2f + y, cornerRadius.leftBottom + x, height -
        // cornerRadius.leftBottom + y));

        // 4
        triangles.addAll(RectToTri(width / 2f + x, height / 2f + y, width - cornerRadius.rightBottom + x, height + y));
        // triangles.addAll(RectToTri(width - cornerRadius.rightBottom + x, height / 2f + y, width + x,
        //         height - cornerRadius.rightBottom + y));

        // if (radius < 1)
        // return triangles;

        float x0, y0;
        int quadrantInd;

        if (cornerRadius.rightBottom >= 1) {
            triangles.addAll(RectToTri(width - cornerRadius.rightBottom + x, height / 2f + y, width + x,
                    height - cornerRadius.rightBottom + y));
            x0 = width - cornerRadius.rightBottom + x;
            y0 = height - cornerRadius.rightBottom + y;
            quadrantInd = 1;
            triangles.addAll(countCircleSector(quadrantInd, x0, y0, cornerRadius.rightBottom));
        }

        if (cornerRadius.rightTop >= 1) {
            triangles.addAll(RectToTri(width - cornerRadius.rightTop + x, cornerRadius.rightTop + y, width + x,
                    height / 2f + y));
            x0 = width - cornerRadius.rightTop + x;
            y0 = cornerRadius.rightTop + y;
            quadrantInd = 4;
            triangles.addAll(countCircleSector(quadrantInd, x0, y0, cornerRadius.rightTop));
        }

        if (cornerRadius.leftTop >= 1) {
            triangles.addAll(RectToTri(0.0f + x, cornerRadius.leftTop + y, cornerRadius.leftTop + x, height / 2f + y));
            x0 = cornerRadius.leftTop + x;
            y0 = cornerRadius.leftTop + y;
            quadrantInd = 3;
            triangles.addAll(countCircleSector(quadrantInd, x0, y0, cornerRadius.leftTop));
        }

        if (cornerRadius.leftBottom >= 1) {
            triangles.addAll(RectToTri(0 + x, height / 2f + y, cornerRadius.leftBottom + x,
                    height - cornerRadius.leftBottom + y));
            x0 = cornerRadius.leftBottom + x;
            y0 = height - cornerRadius.leftBottom + y;
            quadrantInd = 2;
            triangles.addAll(countCircleSector(quadrantInd, x0, y0, cornerRadius.leftBottom));
        }

        return triangles;
    }

    private static List<float[]> RectToTri(float leftTopX, float leftTopY, float rightBottomX, float rightBottomY) {
        // Начало координат в левом верхнем углу
        List<float[]> tri = new LinkedList<>();

        tri.add(new float[] { leftTopX, leftTopY });
        tri.add(new float[] { leftTopX, rightBottomY });
        tri.add(new float[] { rightBottomX, rightBottomY });

        tri.add(new float[] { rightBottomX, rightBottomY });
        tri.add(new float[] { rightBottomX, leftTopY });
        tri.add(new float[] { leftTopX, leftTopY });

        return tri;
    }

    /**
     * Make a rectangle with roundness corners
     * 
     * @param width  rectangle width
     * @param height rectangle height
     * @param radius same radius value for each corner
     * @param x      X position (left top corner) of the result object
     * @param y      Y position (left top corner) of the result object
     * @return Points list of the rectangle with roundness corners
     */
    public static List<float[]> getRoundSquare(float width, float height, float radius, int x, int y) {
        return getRoundSquare(new CornerRadius(radius), width, height, x, y);
    }

    static private List<float[]> countCircleSector(int quadrantInd, float x0, float y0, float radius) {
        int[][] quadrantAngles = new int[][] { { 0, 90 }, { 90, 180 }, { 180, 270 }, { 270, 360 } };

        int firstAngle = quadrantAngles[quadrantInd - 1][0];
        int lastAngle = quadrantAngles[quadrantInd - 1][1];

        List<float[]> circleSect = new LinkedList<>();
        float x1, y1, x2, y2;
        x1 = radius * (float) cosGrad(firstAngle) + x0;
        y1 = radius * (float) sinGrad(firstAngle) + y0;

        for (int alf = firstAngle + 1; alf <= lastAngle; alf += 5) {
            x2 = radius * (float) cosGrad(alf) + x0;
            y2 = radius * (float) sinGrad(alf) + y0;
            circleSect.add(new float[] { x0, y0 });
            circleSect.add(new float[] { x2, y2 });
            circleSect.add(new float[] { x1, y1 }); // против часовой стрелки
            x1 = x2;
            y1 = y2;
        }
        x2 = radius * (float) cosGrad(lastAngle) + x0;
        y2 = radius * (float) sinGrad(lastAngle) + y0;
        circleSect.add(new float[] { x0, y0 });
        circleSect.add(new float[] { x2, y2 });
        circleSect.add(new float[] { x1, y1 });

        return circleSect;
    }

    /**
     * Make a triangle with corners in (x + w/2, y), (x, y + h), (x + w, y + h),
     * rotated on angle degrees
     * 
     * @param angle rotation angle for the triangle in degrees
     */
    public static List<float[]> getTriangle(float w, float h, int x, int y, int angle) {
        float x0 = x + w / 2;
        float y0 = y + h / 2;

        List<float[]> figure = new LinkedList<>();

        figure.add(new float[] { x + w / 2, y });
        figure.add(new float[] { x, y + h });
        figure.add(new float[] { x + w, y + h });

        for (float[] crd : figure) {
            float x_crd = crd[0];
            float y_crd = crd[1];
            crd[0] = x0 + (x_crd - x0) * (float) cosGrad(angle) - (y_crd - y0) * (float) sinGrad(angle);
            crd[1] = y0 + (y_crd - y0) * (float) cosGrad(angle) + (x_crd - x0) * (float) sinGrad(angle);
        }
        return figure;
    }

    /**
     * Make an ellipse with two equal radii (i. e. circle)
     * 
     * @param n points count on the ellipse border
     */
    static public List<float[]> getEllipse(float r, int n) {
        float x_center = r;
        float y_center = r;

        List<float[]> triangles = new LinkedList<>();

        float alpha = 0;
        for (int i = 0; i < n; i++) {
            triangles.add(new float[] { x_center, y_center });

            triangles.add(
                    new float[] { (float) (x_center + r * cosGrad(alpha)), (float) (y_center - r * sinGrad(alpha)) });

            alpha = alpha + 360.0f / n;

            triangles.add(
                    new float[] { (float) (x_center + r * cosGrad(alpha)), (float) (y_center - r * sinGrad(alpha)) });
        }

        return triangles;
    }

    /**
     * Make an ellipse
     * 
     * @param w ellipse width
     * @param h ellipse height
     * @param x X position of the left top corner (ellipse center in x + w/2)
     * @param y Y position of the left top corner (ellipse center in y + h/2)
     * @param n points count on the ellipse border
     */
    static public List<float[]> getEllipse(float w, float h, int x, int y, int n) {
        float rX = w / 2;
        float rY = h / 2;
        float x_center = x + rX;
        float y_center = y + rY;
        List<float[]> triangles = new LinkedList<>();

        float alpha = 0;
        for (int i = 0; i < n; i++) {
            triangles.add(new float[] { x_center, y_center });

            triangles.add(
                    new float[] { (float) (x_center + rX * cosGrad(alpha)), (float) (y_center - rY * sinGrad(alpha)) });

            alpha = alpha + 360.0f / n;

            triangles.add(
                    new float[] { (float) (x_center + rX * cosGrad(alpha)), (float) (y_center - rY * sinGrad(alpha)) });
        }

        return triangles;
    }

    /**
     * Make cross figure
     * 
     * @param w         cross width
     * @param h         cross height
     * @param thickness cross parts thickness
     * @param alpha     cross rotation angle in degrees
     */
    static public List<float[]> getCross(float w, float h, float thickness, int alpha) {
        List<float[]> figure = new LinkedList<>();

        float x0 = (w - thickness) / 2;
        float y0 = (h - thickness) / 2;

        // center
        figure.add(new float[] { x0, y0 });
        figure.add(new float[] { x0, y0 + thickness });
        figure.add(new float[] { x0 + thickness, y0 + thickness });

        figure.add(new float[] { x0 + thickness, y0 + thickness });
        figure.add(new float[] { x0 + thickness, y0 });
        figure.add(new float[] { x0, y0 });

        // top
        figure.add(new float[] { x0, 0 });
        figure.add(new float[] { x0, y0 });
        figure.add(new float[] { x0 + thickness, y0 });

        figure.add(new float[] { x0 + thickness, y0 });
        figure.add(new float[] { x0 + thickness, 0 });
        figure.add(new float[] { x0, 0 });

        // bottom
        figure.add(new float[] { x0, y0 + thickness });
        figure.add(new float[] { x0, h });
        figure.add(new float[] { x0 + thickness, h });

        figure.add(new float[] { x0 + thickness, h });
        figure.add(new float[] { x0 + thickness, y0 + thickness });
        figure.add(new float[] { x0, y0 + thickness });

        // left
        figure.add(new float[] { 0, y0 });
        figure.add(new float[] { 0, y0 + thickness });
        figure.add(new float[] { x0, y0 + thickness });

        figure.add(new float[] { x0, y0 + thickness, });
        figure.add(new float[] { x0, y0 });
        figure.add(new float[] { 0, y0 });

        // right
        figure.add(new float[] { x0 + thickness, y0 });
        figure.add(new float[] { x0 + thickness, y0 + thickness });
        figure.add(new float[] { h, y0 + thickness });

        figure.add(new float[] { h, y0 + thickness });
        figure.add(new float[] { h, y0 });
        figure.add(new float[] { x0 + thickness, y0 });

        // rotate
        x0 = w / 2;
        y0 = h / 2;
        for (float[] crd : figure) {
            float x_crd = crd[0];
            float y_crd = crd[1];
            crd[0] = x0 + (x_crd - x0) * (float) cosGrad(alpha) - (y_crd - y0) * (float) sinGrad(alpha);
            crd[1] = y0 + (y_crd - y0) * (float) cosGrad(alpha) + (x_crd - x0) * (float) sinGrad(alpha);
        }
        return figure;
    }

    public static List<float[]> rotateShape(float w, float h, float angle, List<float[]> triangles) {
        // rotate
        float x0 = w / 2.0f;
        float y0 = h / 2.0f;
        for (float[] crd : triangles) {
            float x_crd = crd[0];
            float y_crd = crd[1];
            crd[0] = x0 + (x_crd - x0) * (float) cosGrad(angle) - (y_crd - y0) * (float) sinGrad(angle);
            crd[1] = y0 + (y_crd - y0) * (float) cosGrad(angle) + (x_crd - x0) * (float) sinGrad(angle);
        }
        return triangles;
    }

    public static List<float[]> getArrow(float w, float h, float thickness, int alpha) {
        List<float[]> figure = new LinkedList<float[]>();

        float x0 = (w - thickness) / 2;
        float y0 = (h - thickness) / 2;

        // center
        figure.add(new float[] { x0, y0 });
        figure.add(new float[] { x0, y0 + thickness });
        figure.add(new float[] { x0 + thickness, y0 + thickness });

        figure.add(new float[] { x0 + thickness, y0 + thickness });
        figure.add(new float[] { x0 + thickness, y0 });
        figure.add(new float[] { x0, y0 });

        // top
        figure.add(new float[] { x0, 0 });
        figure.add(new float[] { x0, y0 });
        figure.add(new float[] { x0 + thickness, y0 });

        figure.add(new float[] { x0 + thickness, y0 });
        figure.add(new float[] { x0 + thickness, 0 });
        figure.add(new float[] { x0, 0 });

        // left
        figure.add(new float[] { 0, y0 });
        figure.add(new float[] { 0, y0 + thickness });
        figure.add(new float[] { x0, y0 + thickness });

        figure.add(new float[] { x0, y0 + thickness });
        figure.add(new float[] { x0, y0 });
        figure.add(new float[] { 0, y0 });

        // rotate
        x0 = w / 2;
        y0 = h / 2;
        for (float[] crd : figure) {
            float x_crd = crd[0];
            float y_crd = crd[1];
            crd[0] = x0 + (x_crd - x0) * (float) cosGrad(alpha) - (y_crd - y0) * (float) sinGrad(alpha);
            crd[1] = y0 + (y_crd - y0) * (float) cosGrad(alpha) + (x_crd - x0) * (float) sinGrad(alpha);
        }
        return figure;
    }

    /**
     * Make rectangle as two triangles by its width, height and top left corner
     * position (x, y)
     */
    public static List<float[]> getRectangle(float w, float h, float x, float y) {
        List<float[]> figure = new LinkedList<>();
        figure.add(new float[] { x, y });
        figure.add(new float[] { x, y + h });
        figure.add(new float[] { x + w, y + h });

        figure.add(new float[] { x + w, y + h });
        figure.add(new float[] { x + w, y });
        figure.add(new float[] { x, y });
        return figure;
    }

    /**
     * Move shape by X or/and Y direction
     */
    public static List<float[]> moveShape(List<float[]> shape, float x, float y) {
        if (shape.size() == 0)
            return null;

        // clone triangles
        List<float[]> result = new LinkedList<>();
        for (int i = 0; i < shape.size(); i++) {
            result.add(new float[] { shape.get(i)[0], shape.get(i)[1] });
        }
        // to the left top corner
        for (float[] point : result) {
            point[0] += x;
            point[1] += y;
        }

        return result;
    }

    /**
     * Make folder icon shape as three rectangles
     */
    public static List<float[]> getFolderIconShape(float w, float h, float x, float y) {
        List<float[]> triangles = new LinkedList<>();
        triangles.addAll(getRectangle(w / 3, h, 0, 0));
        triangles.addAll(getRectangle(2 * w / 3, h - h / 4, w / 3, h / 4));
        triangles.addAll(getRectangle(w / 4, h / 8, w / 3 + 2, 0));
        return triangles;
    }

    /**
     * Make a star figure
     * 
     * @param R Circumscribed circle radius
     * @param r Incircle radius
     * @param n vertices count
     */
    static public List<float[]> getStar(float R, float r, int n) {
        float x_center = r;
        float y_center = r;

        List<float[]> triangles = new LinkedList<>();

        float alpha = 0.0f;
        for (int i = 0; i < n; i++) {
            triangles.add(new float[] { x_center, y_center });

            triangles.add(new float[] { (float) (x_center + r / 2 * cosGrad(alpha)),
                    (float) (y_center - r / 2 * sinGrad(alpha)) });

            alpha = alpha + 360.0f / n;

            triangles.add(new float[] { (float) (x_center + r / 2 * cosGrad(alpha)),
                    (float) (y_center - r / 2 * sinGrad(alpha)) });
        }

        alpha = 0.0f;
        int count = 1;
        for (int i = 1; i < n * 2 + 2; i++) {
            if ((i % 2) != 0) {
                triangles.add(new float[] { (float) (x_center + r / 2 * cosGrad(alpha)),
                        (float) (y_center - r / 2 * sinGrad(alpha)) });
                if (count % 3 == 0) {
                    triangles.add(new float[] { (float) (x_center + r / 2 * cosGrad(alpha)),
                            (float) (y_center - r / 2 * sinGrad(alpha)) });
                    count = 1;
                }
            } else {
                triangles.add(new float[] { (float) (x_center + R / 2 * cosGrad(alpha)),
                        (float) (y_center - R / 2 * sinGrad(alpha)) });
                if (count % 3 == 0) {
                    triangles.add(new float[] { (float) (x_center + R / 2 * cosGrad(alpha)),
                            (float) (y_center - R / 2 * sinGrad(alpha)) });
                    count = 1;
                }
            }
            alpha = alpha + 180.0f / n;
            count++;
        }
        triangles.remove(triangles.size() - 1);
        return triangles;
    }

    /**
     * Make a regular polygon
     */
    static public List<float[]> getRegularPolygon(float r, int n) {
        float x_center = r;
        float y_center = r;

        List<float[]> triangles = new LinkedList<>();

        float alpha = 0;
        for (int i = 0; i < n; i++) {
            triangles.add(new float[] { x_center, y_center });

            triangles.add(new float[] { (float) (x_center + r / 2 * cosGrad(alpha)),
                    (float) (y_center - r / 2 * sinGrad(alpha)) });

            alpha = alpha + 360.0f / n;

            triangles.add(new float[] { (float) (x_center + r / 2 * cosGrad(alpha)),
                    (float) (y_center - r / 2 * sinGrad(alpha)) });
        }

        return triangles;
    }

    /**
     * Make a rectangle border with roundness corners
     * 
     * @param width     rectangle border width
     * @param height    rectangle border height
     * @param radius    same radius value for each corner
     * @param thickness border thickness
     * @param x         X position (left top corner) of the result object
     * @param y         Y position (left top corner) of the result object
     * @return Points list of the rectangle border with roundness corners
     */
    public static List<float[]> getRoundSquareBorder(float width, float height, float radius, float thickness, int x,
            int y) {
        if (radius < 0)
            radius = 0;

        List<BorderSection> border = new LinkedList<>();
        // Начало координат в углу

        border.add(new BorderSection(width - radius + x, y, radius + x, y, width / 2f + x, height + 1 + y));
        border.add(new BorderSection(width - radius + x, height + y, radius + x, height + y, width / 2f + x, y - 1));
        border.add(new BorderSection(width + x, height - radius + y, width + x, radius + y, x - 1, height / 2f + y));
        border.add(new BorderSection(x, height - radius + y, x, radius + y, width + 1 + x, height / 2f + y));

        if (radius < 1)
            return makeBorder(border, thickness);

        List<float[]> tmpList;
        float x0, y0;
        int quadrantInd;
        x0 = width - radius + x;
        y0 = height - radius + y;
        quadrantInd = 1;
        tmpList = countCircleSector(quadrantInd, x0, y0, radius);

        x0 = width - radius + x;
        y0 = radius + y;
        quadrantInd = 4;
        tmpList.addAll(countCircleSector(quadrantInd, x0, y0, radius));

        x0 = radius + x;
        y0 = radius + y;
        quadrantInd = 3;
        tmpList.addAll(countCircleSector(quadrantInd, x0, y0, radius));

        x0 = radius + x;
        y0 = height - radius + y;
        quadrantInd = 2;
        tmpList.addAll(countCircleSector(quadrantInd, x0, y0, radius));

        for (int i = 0; i < tmpList.size() / 3; i++) {
            border.add(
                    new BorderSection(tmpList.get(i * 3 + 1)[0], tmpList.get(i * 3 + 1)[1], tmpList.get(i * 3 + 2)[0],
                            tmpList.get(i * 3 + 2)[1], tmpList.get(i * 3)[0], tmpList.get(i * 3)[1]));
        }

        return makeBorder(border, thickness);
    }

    /**
     * Make a rectangle border with roundness corners
     * 
     * @param cornerRadius radius values for all corners
     * @param width        rectangle border width
     * @param height       rectangle border height
     * @param thickness    border thickness
     * @param x            X position (left top corner) of the result object
     * @param y            Y position (left top corner) of the result object
     * @return Points list of the rectangle border with roundness corners
     */
    public static List<float[]> getRoundSquareBorder(CornerRadius cornerRadius, float width, float height,
            float thickness, int x, int y) {

        if (width <= 0 || height <= 0)
            return null;

        if (cornerRadius == null)
            cornerRadius = new CornerRadius();
        else {
            if (cornerRadius.leftTop < 0)
                cornerRadius.leftTop = 0;
            if (cornerRadius.rightTop < 0)
                cornerRadius.rightTop = 0;
            if (cornerRadius.leftBottom < 0)
                cornerRadius.leftBottom = 0;
            if (cornerRadius.rightBottom < 0)
                cornerRadius.rightBottom = 0;
        }

        List<BorderSection> border = new LinkedList<>();
        // Начало координат в левом углу

        // 1
        border.add(new BorderSection(cornerRadius.leftTop + x, y, width / 2f + x, y, width / 2f + x, height + y + 1));
        border.add(new BorderSection(x, cornerRadius.leftTop + y, x, height / 2f + y, width + 1 + x, height / 2f + y));

        // 2
        border.add(new BorderSection(width / 2f + x, y, width - cornerRadius.rightTop + x, y, width / 2f + x,
                height + y + 1));
        border.add(new BorderSection(width + x, height / 2f + y, width + x, cornerRadius.rightTop + y, x - 1,
                height / 2f + y));

        // 3
        border.add(new BorderSection(width / 2f + x, height + y, cornerRadius.leftBottom + x, height + y, width / 2f,
                y - 1));
        border.add(new BorderSection(x, height / 2f + y, x, height - cornerRadius.leftBottom + y, width + 1 + x,
                height / 2f + y));

        // 4
        border.add(new BorderSection(width - cornerRadius.rightBottom + x, height + y, width / 2f + x, height + y,
                width / 2f, y - 1));
        border.add(new BorderSection(width + x, height - cornerRadius.rightBottom + y, width + x, height / 2f + y,
                x - 1, height / 2f + y));

        // if (radius < 1)
        // return triangles;

        List<float[]> tmpList = new LinkedList<>();
        float x0, y0;
        int quadrantInd;

        if (cornerRadius.rightBottom >= 1) {
            x0 = width - cornerRadius.rightBottom + x;
            y0 = height - cornerRadius.rightBottom + y;
            quadrantInd = 1;
            tmpList.addAll(countCircleSector(quadrantInd, x0, y0, cornerRadius.rightBottom));
        }

        if (cornerRadius.rightTop >= 1) {
            x0 = width - cornerRadius.rightTop + x;
            y0 = cornerRadius.rightTop + y;
            quadrantInd = 4;
            tmpList.addAll(countCircleSector(quadrantInd, x0, y0, cornerRadius.rightTop));
        }

        if (cornerRadius.leftTop >= 1) {
            x0 = cornerRadius.leftTop + x;
            y0 = cornerRadius.leftTop + y;
            quadrantInd = 3;
            tmpList.addAll(countCircleSector(quadrantInd, x0, y0, cornerRadius.leftTop));
        }

        if (cornerRadius.leftBottom >= 1) {
            x0 = cornerRadius.leftBottom + x;
            y0 = height - cornerRadius.leftBottom + y;
            quadrantInd = 2;
            tmpList.addAll(countCircleSector(quadrantInd, x0, y0, cornerRadius.leftBottom));
        }

        for (int i = 0; i < tmpList.size() / 3; i++) {
            border.add(
                    new BorderSection(tmpList.get(i * 3 + 1)[0], tmpList.get(i * 3 + 1)[1], tmpList.get(i * 3 + 2)[0],
                            tmpList.get(i * 3 + 2)[1], tmpList.get(i * 3)[0], tmpList.get(i * 3)[1]));
        }

        return makeBorder(border, thickness);
    }

    private static List<float[]> makeBorder(List<BorderSection> borders, float thickness) {
        List<float[]> borderTris = new LinkedList<>();

        float bw = thickness; // border width
        float x3, y3, x4, y4;
        float x1, y1, x2, y2;
        for (int i = 0; i < borders.size(); i++) {
            x1 = borders.get(i).x1;
            x2 = borders.get(i).x2;
            y1 = borders.get(i).y1;
            y2 = borders.get(i).y2;

            x3 = x1 + borders.get(i).nx * bw;
            y3 = y1 + borders.get(i).ny * bw;
            x4 = x2 + borders.get(i).nx * bw;
            y4 = y2 + borders.get(i).ny * bw;

            borderTris.addAll(wherePoint(x1, y1, x2, y2, x4, y4));
            borderTris.addAll(wherePoint(x1, y1, x4, y4, x3, y3));

        }

        return borderTris;
    }

    private static List<float[]> wherePoint(float x1, float y1, float x2, float y2, float x3, float y3) {
        List<float[]> clockwise = new LinkedList<>();
        float f = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1);
        if (f < 0) {
            clockwise.add(new float[] { x1, y1 });
            clockwise.add(new float[] { x2, y2 });
            clockwise.add(new float[] { x3, y3 });
        } else {
            clockwise.add(new float[] { x2, y2 });
            clockwise.add(new float[] { x1, y1 });
            clockwise.add(new float[] { x3, y3 });
        }
        return clockwise;
    }

    public static BufferedImage scaleBitmap(BufferedImage img, int w, int h) {
        float boundW = w;
        float boundH = h;

        float ratioX = (boundW / img.getWidth());
        float ratioY = (boundH / img.getHeight());
        float ratio = ratioX < ratioY ? ratioX : ratioY;

        int resH = (int) (img.getHeight() * ratio);
        int resW = (int) (img.getWidth() * ratio);

        BufferedImage bmp = new BufferedImage(resW, resH, BufferedImage.TYPE_INT_ARGB);
        // System.out.println(resW + " " + resH);
        Graphics2D graphic = bmp.createGraphics();
        graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        graphic.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphic.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        graphic.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // graphic.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        // graphic.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

        graphic.drawImage(img, 0, 0, resW, resH, null);
        graphic.dispose();

        return bmp;
    }

    public static Color colorTransform(int r, int g, int b) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 255)
            r = 255;
        if (g < 0)
            g = Math.abs(g);
        if (g > 255)
            g = 255;
        if (b < 0)
            b = Math.abs(b);
        if (b > 255)
            b = 255;
        return new Color(r, g, b, 255);
    }

    public static Color colorTransform(int r, int g, int b, int a) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 255)
            r = 255;
        if (g < 0)
            g = Math.abs(g);
        if (g > 255)
            g = 255;
        if (b < 0)
            b = Math.abs(b);
        if (b > 255)
            b = 255;
        return new Color(r, g, b, a);
    }

    public static Color colorTransform(float r, float g, float b) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 1.0f)
            r = 1.0f;
        if (g < 0)
            g = Math.abs(g);
        if (g > 1.0f)
            g = 1.0f;
        if (b < 0)
            b = Math.abs(b);
        if (b > 1.0f)
            b = 1.0f;
        return new Color((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), 255);
    }

    public static Color colorTransform(float r, float g, float b, float a) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 1.0f)
            r = 1.0f;
        if (g < 0)
            g = Math.abs(g);
        if (g > 1.0f)
            g = 1.0f;
        if (b < 0)
            b = Math.abs(b);
        if (b > 1.0f)
            b = 1.0f;
        return new Color((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), (int) (a * 255.0f));
    }

    public static Font changeFontSize(int size, Font oldFont) {
        return new Font(oldFont.getFamily(), oldFont.getStyle(), size);// oldFont.getName(), oldFont.getStyle(), size);
    }

    public static Font changeFontStyle(int style, Font oldFont) {
        return new Font(oldFont.getFamily(), style, oldFont.getSize());
    }

    public static Font changeFontFamily(String fontFamily, Font oldFont) {
        return new Font(fontFamily, oldFont.getStyle(), oldFont.getSize());
    }

    private static double grad2Radian(double angleGrad) {
        return (angleGrad * Math.PI / 180.0f);
    }

    private static double cosGrad(double angleGrad) {
        return Math.cos(grad2Radian(angleGrad));
    }

    private static double sinGrad(double angleGrad) {
        return Math.sin(grad2Radian(angleGrad));
    }
}

class BorderSection {
    float x1;
    float y1;
    float x2;
    float y2;
    float nx;
    float ny;

    BorderSection(float x1, float y1, float x2, float y2, float x3, float y3) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        checkNormDir(x3, y3);
    }

    private void checkNormDir(float x3, float y3) {
        nx = y1 - y2;
        ny = x2 - x1;

        float k = Math.signum((x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1));
        /*
         * if (nx == 0 || ny == 0) { System.out.println(); System.out.println("nx " + nx
         * + " ny " + ny); System.out.println("x1 " + x1 + " y1 " + y1);
         * System.out.println("x2 " + x2 + " y2 " + y2); System.out.println("k " + k); }
         */
        if (k != 0) {
            nx *= k;
            ny *= k;
        }

        float d = (float) Math.sqrt(Math.pow(nx, 2.0) + Math.pow(ny, 2.0));
        if (d != 0) {
            nx /= d;
            ny /= d;
        }
        /*
         * if (nx == 0 || ny == 0) {
         * 
         * System.out.println("after nx " + nx + " ny " + ny); System.out.println(); }
         */
    }
}
