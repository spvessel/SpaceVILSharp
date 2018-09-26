package com.spvessel.Engine;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Windows.*;
import com.spvessel.Items.*;

public final class GraphicsMathService {

    private GraphicsMathService() {
    }

    public static Color MixColors(Color... colors) {
        List<Color> m_colors = Arrays.stream(colors).collect(Collectors.toList());
        if (m_colors.size() == 0)
            return new Color(255, 255, 255);
        if (m_colors.size() == 1)
            return m_colors.get(0);

        float r = 0, g = 0, b = 0, a = 255.0f;

        for (Color item : m_colors) {
            if (item.getAlpha() == 255) // exchange
            {
                r = item.getRed();
                g = item.getGreen();
                b = item.getBlue();
            } else // mixing
            {
                r = r * (1.0f - item.getAlpha() / 255.0f) + item.getRed() * (item.getAlpha() / 255.0f);
                g = g * (1.0f - item.getAlpha() / 255.0f) + item.getGreen() * (item.getAlpha() / 255.0f);
                b = b * (1.0f - item.getAlpha() / 255.0f) + item.getBlue() * (item.getAlpha() / 255.0f);
            }
        }
        return new Color((int)r, (int)g , (int)b , (int)a);
    }

    static public List<float[]> toGL(BaseItem item, WindowLayout handler) // where TLayout : VisualItem
    {
        List<float[]> result = new LinkedList<float[]>();

        for (float[] vector : item.getTriangles()) {
            float x = (vector[0] / (float) handler.getWidth()) * 2.0f - 1.0f;
            float y = (vector[1] / (float) handler.getHeight() * 2.0f - 1.0f) * (-1.0f);
            result.add(new float[] { x, y, vector[2] });
        }
        return result;
    }

    static public List<float[]> toGL(List<float[]> triangles, WindowLayout handler) // where TLayout : VisualItem
    {
        List<float[]> result = new LinkedList<float[]>();

        for (float[] vector : triangles) {
            float x = (vector[0] / (float) handler.getWidth()) * 2.0f - 1.0f;
            float y = (vector[1] / (float) handler.getHeight() * 2.0f - 1.0f) * (-1.0f);
            result.add(new float[] { x, y, vector[2] });
        }
        return result;
    }

    public static List<float[]> getRoundSquare(float width, float height, float radius, int x, int y) {
        if (radius < 0)
            radius = 0;

        List<float[]> triangles = new LinkedList<float[]>();
        // Начало координат в углу

        triangles.add(new float[] { radius + x, height + y, 0.0f });
        triangles.add(new float[] { width - radius + x, y, 0.0f });
        triangles.add(new float[] { radius + x, y, 0.0f });
        // 1

        triangles.add(new float[] { radius + x, height + y, 0.0f });
        triangles.add(new float[] { width - radius + x, height + y, 0.0f });
        triangles.add(new float[] { width - radius + x, y, 0.0f });
        // 2

        triangles.add(new float[] { width - radius + x, height - radius + y, 0.0f });
        triangles.add(new float[] { width + x, radius + y, 0.0f });
        triangles.add(new float[] { width - radius + x, radius + y, 0.0f });
        // 3

        triangles.add(new float[] { width - radius + x, height - radius + y, 0.0f });
        triangles.add(new float[] { width + x, height - radius + y, 0.0f });
        triangles.add(new float[] { width + x, radius + y, 0.0f });
        // 4

        triangles.add(new float[] { x, height - radius + y, 0.0f });
        triangles.add(new float[] { radius + x, radius + y, 0.0f });
        triangles.add(new float[] { x, radius + y, 0.0f });
        // 5

        triangles.add(new float[] { x, height - radius + y, 0.0f });
        triangles.add(new float[] { radius + x, height - radius + y, 0.0f });
        triangles.add(new float[] { radius + x, radius + y, 0.0f });
        // 6

        if (radius < 1)
            return triangles;

        float x0, y0;
        x0 = width - radius + x;
        y0 = height - radius + y;
        triangles.addAll(countCircleSector(0, 90, x0, y0, radius)); // 7

        x0 = width - radius + x;
        y0 = radius + y;
        triangles.addAll(countCircleSector(270, 360, x0, y0, radius)); // 8

        x0 = radius + x;
        y0 = radius + y;
        triangles.addAll(countCircleSector(180, 270, x0, y0, radius)); // 9

        x0 = radius + x;
        y0 = height - radius + y;
        triangles.addAll(countCircleSector(90, 180, x0, y0, radius)); // 10

        return triangles;
    }

    static private List<float[]> countCircleSector(int alph1, int alph2, float x0, float y0, float radius) {
        List<float[]> circleSect = new LinkedList<float[]>();
        float x1, y1, x2, y2;
        x1 = radius * (float) Math.cos(alph1 * Math.PI / 180.0f) + x0;
        y1 = radius * (float) Math.sin(alph1 * Math.PI / 180.0f) + y0;

        for (int alf = alph1 + 1; alf <= alph2; alf += 5) { // Шаг можно сделать больше 1 градуса, нужны тестирования
            x2 = radius * (float) Math.cos(alf * Math.PI / 180.0f) + x0;
            y2 = radius * (float) Math.sin(alf * Math.PI / 180.0f) + y0;
            circleSect.add(new float[] { x0, y0, 0.0f });
            circleSect.add(new float[] { x2, y2, 0.0f });
            circleSect.add(new float[] { x1, y1, 0.0f }); // против часовой стрелки
            x1 = x2;
            y1 = y2;
        }
        x2 = radius * (float) Math.cos(alph2 * Math.PI / 180.0f) + x0;
        y2 = radius * (float) Math.sin(alph2 * Math.PI / 180.0f) + y0;
        circleSect.add(new float[] { x0, y0, 0.0f });
        circleSect.add(new float[] { x2, y2, 0.0f });
        circleSect.add(new float[] { x1, y1, 0.0f });

        return circleSect;
    }

    public static List<float[]> getTriangle(float w, float h, int x, int y, int a) {
        float x0 = x + w / 2;
        float y0 = y + h / 2;

        List<float[]> figure = new LinkedList<float[]>();

        figure.add(new float[] { x + w / 2, y, 0.0f });
        figure.add(new float[] { x, y + h, 0.0f });
        figure.add(new float[] { x + w, y + h, 0.0f });

        for (float[] crd : figure) {
            float x_crd = crd[0];
            float y_crd = crd[1];
            crd[0] = x0 + (x_crd - x0) * (float) Math.cos(a * Math.PI / 180.0f)
                    - (y_crd - y0) * (float) Math.sin(a * Math.PI / 180.0f);
            crd[1] = y0 + (y_crd - y0) * (float) Math.cos(a * Math.PI / 180.0f)
                    + (x_crd - x0) * (float) Math.sin(a * Math.PI / 180.0f);
        }
        return figure;
    }

    static public List<float[]> getEllipse(float r, int n) {
        float x_center = r;
        float y_center = r;

        List<float[]> triangles = new LinkedList<float[]>();

        float alpha = 0;
        for (int i = 0; i < n; i++) {
            triangles.add(new float[] { x_center, y_center, 0.0f });

            triangles.add(new float[] { (float) (x_center + r * Math.cos(alpha * Math.PI / 180.0f)),
                    (float) (y_center - r * Math.sin(alpha * Math.PI / 180.0f)), 0.0f });

            alpha = alpha + 360.0f / n;

            triangles.add(new float[] { (float) (x_center + r * Math.cos(alpha * Math.PI / 180.0f)),
                    (float) (y_center - r * Math.sin(alpha * Math.PI / 180.0f)), 0.0f });
        }

        return triangles;
    }

    static public List<float[]> getEllipse(float w, float h, int x, int y, int n) {
        float rX = w / 2;
        float rY = h / 2;
        float x_center = x + rX;
        float y_center = y + rY;
        List<float[]> triangles = new LinkedList<float[]>();

        float alpha = 0;
        for (int i = 0; i < n; i++) {
            triangles.add(new float[] { x_center, y_center, 0.0f });

            triangles.add(new float[] { (float) (x_center + rX * Math.cos(alpha * Math.PI / 180.0f)),
                    (float) (y_center - rY * Math.sin(alpha * Math.PI / 180.0f)), 0.0f });

            alpha = alpha + 360.0f / n;

            triangles.add(new float[] { (float) (x_center + rX * Math.cos(alpha * Math.PI / 180.0f)),
                    (float) (y_center - rY * Math.sin(alpha * Math.PI / 180.0f)), 0.0f });
        }

        return triangles;
    }

    static public List<float[]> getCross(float w, float h, float thickness, int alpha) {
        List<float[]> figure = new LinkedList<float[]>();

        float x0 = (w - thickness) / 2;
        float y0 = (h - thickness) / 2;

        // center
        figure.add(new float[] { x0, y0, 0.0f });
        figure.add(new float[] { x0, y0 + thickness, 0.0f });
        figure.add(new float[] { x0 + thickness, y0 + thickness, 0.0f });

        figure.add(new float[] { x0 + thickness, y0 + thickness, 0.0f });
        figure.add(new float[] { x0 + thickness, y0, 0.0f });
        figure.add(new float[] { x0, y0, 0.0f });

        // top
        figure.add(new float[] { x0, 0, 0.0f });
        figure.add(new float[] { x0, y0, 0.0f });
        figure.add(new float[] { x0 + thickness, y0, 0.0f });

        figure.add(new float[] { x0 + thickness, y0, 0.0f });
        figure.add(new float[] { x0 + thickness, 0, 0.0f });
        figure.add(new float[] { x0, 0, 0.0f });

        // bottom
        figure.add(new float[] { x0, y0 + thickness, 0.0f });
        figure.add(new float[] { x0, h, 0.0f });
        figure.add(new float[] { x0 + thickness, h, 0.0f });

        figure.add(new float[] { x0 + thickness, h, 0.0f });
        figure.add(new float[] { x0 + thickness, y0 + thickness, 0.0f });
        figure.add(new float[] { x0, y0 + thickness, 0.0f });

        // left
        figure.add(new float[] { 0, y0, 0.0f });
        figure.add(new float[] { 0, y0 + thickness, 0.0f });
        figure.add(new float[] { x0, y0 + thickness, 0.0f });

        figure.add(new float[] { x0, y0 + thickness, 0.0f });
        figure.add(new float[] { x0, y0, 0.0f });
        figure.add(new float[] { 0, y0, 0.0f });

        // right
        figure.add(new float[] { x0 + thickness, y0, 0.0f });
        figure.add(new float[] { x0 + thickness, y0 + thickness, 0.0f });
        figure.add(new float[] { h, y0 + thickness, 0.0f });

        figure.add(new float[] { h, y0 + thickness, 0.0f });
        figure.add(new float[] { h, y0, 0.0f });
        figure.add(new float[] { x0 + thickness, y0, 0.0f });

        // rotate
        x0 = w / 2;
        y0 = h / 2;
        for (float[] crd : figure) {
            float x_crd = crd[0];
            float y_crd = crd[1];
            crd[0] = x0 + (x_crd - x0) * (float) Math.cos(alpha * Math.PI / 180.0f)
                    - (y_crd - y0) * (float) Math.sin(alpha * Math.PI / 180.0f);
            crd[1] = y0 + (y_crd - y0) * (float) Math.cos(alpha * Math.PI / 180.0f)
                    + (x_crd - x0) * (float) Math.sin(alpha * Math.PI / 180.0f);
        }
        return figure;
    }

    public static List<float[]> getRectangle(float w, float h, float x, float y) {
        List<float[]> figure = new LinkedList<float[]>();
        figure.add(new float[] { x, y, 0.0f });
        figure.add(new float[] { x, y + h, 0.0f });
        figure.add(new float[] { x + w, y + h, 0.0f });

        figure.add(new float[] { x + w, y + h, 0.0f });
        figure.add(new float[] { x + w, y, 0.0f });
        figure.add(new float[] { x, y, 0.0f });
        return figure;
    }

    public static List<float[]> moveShape(List<float[]> shape, float x, float y) {
        if (shape.size() == 0)
            return null;

        // clone triangles
        List<float[]> result = new LinkedList<float[]>();
        for (int i = 0; i < shape.size(); i++) {
            result.add(new float[] { shape.get(i)[0], shape.get(i)[1], shape.get(i)[2] });
        }
        // to the left top corner
        for (float[] point : result) {
            point[0] += x;
            point[1] += y;
        }

        return result;
    }

    public static List<float[]> getFolderIconShape(float w, float h, float x, float y) {
        List<float[]> triangles = new LinkedList<float[]>();
        triangles.addAll(getRectangle(w / 3, h, 0, 0));
        triangles.addAll(getRectangle(2 * w / 3, h - h / 4, w / 3, h / 4));
        triangles.addAll(getRectangle(w / 4, h / 8, w / 3 + 2, 0));
        return triangles;
    }
}