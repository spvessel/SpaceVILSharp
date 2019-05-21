package com.spvessel.spacevil.Core;

import java.awt.Color;

public interface InterfaceImageItem {
    byte[] getPixMapImage();
    int getImageWidth();
    int getImageHeight();
    boolean isColorOverlay();
    Color getColorOverlay();
    float getRotationAngle();
    RectangleBounds getRectangleBounds();
}