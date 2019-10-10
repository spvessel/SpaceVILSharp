package com.spvessel.spacevil.Core;

import java.awt.Color;
import java.awt.image.BufferedImage;

public interface InterfaceImageItem {
    // byte[] getPixMapImage();
    BufferedImage getImage();
    int getImageWidth();
    int getImageHeight();
    boolean isColorOverlay();
    Color getColorOverlay();
    float getRotationAngle();
    RectangleBounds getRectangleBounds();
}