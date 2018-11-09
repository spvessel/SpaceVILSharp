package com.spvessel.Decorations;

public class CornerRadius {
    public float leftTop;
    public float rightTop;
    public float leftBottom;
    public float rightBottom;

    public CornerRadius(float lt, float rt, float lb, float rb) {
        leftTop = lt;
        rightTop = rt;
        leftBottom = lb;
        rightBottom = rb;
    }

    public CornerRadius(CornerRadius radius) {
        leftTop = radius.leftTop;
        rightTop = radius.rightTop;
        leftBottom = radius.leftBottom;
        rightBottom = radius.rightBottom;
    }

    public CornerRadius() {
        this(0);
    }

    public CornerRadius(float radius) {
        leftTop = radius;
        rightTop = radius;
        leftBottom = radius;
        rightBottom = radius;
    }

}
