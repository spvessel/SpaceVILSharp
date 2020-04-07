package com.spvessel.spacevil;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Gradient {
    private List<Color> colors;
    private List<Integer> positions;
    private double angle = 0;
    private GradientType gradientType = GradientType.Linear;

//    public Gradient(List<Color> colors) {
//        this.colors = colors;
//        //positions equally
//    }


    public Gradient(List<Color> colors, List<Integer> positions) {
        this(colors, positions, 0);
    }

    public Gradient(List<Color> colors, List<Integer> positions, double angle) {
        this(colors, positions, angle, GradientType.Linear);
    }

    public Gradient(List<Color> colors, List<Integer> positions, double angle, GradientType gradientType) {
        this.colors = colors;
        this.positions = positions;
        this.angle = angle;
        this.gradientType = gradientType;
    }

    public void setColors(List<Color> colors) {
        this.colors = colors;
    }

    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setGradientType(GradientType gradientType) {
        this.gradientType = gradientType;
    }
}
