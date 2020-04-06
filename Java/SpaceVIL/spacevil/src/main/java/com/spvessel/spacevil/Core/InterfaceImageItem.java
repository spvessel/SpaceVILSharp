package com.spvessel.spacevil.Core;

import java.awt.Color;
import java.awt.image.BufferedImage;

public interface InterfaceImageItem {
    public BufferedImage getImage();
    public int getImageWidth();
    public int getImageHeight();
    public boolean isColorOverlay();
    public Color getColorOverlay();
    public float getRotationAngle();
    public Area getAreaBounds();
}