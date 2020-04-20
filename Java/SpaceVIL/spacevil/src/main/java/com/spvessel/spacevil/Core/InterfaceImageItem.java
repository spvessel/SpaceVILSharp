package com.spvessel.spacevil.Core;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.spvessel.spacevil.Flags.ImageQuality;

/**
 * An interface that discribes such type of items that are images (for
 * texturing).
 */
public interface InterfaceImageItem {
    /**
     * Method for describing how to get a bitmap in the form of
     * java.awt.image.BufferedImage.
     * 
     * @return Image as java.awt.image.BufferedImage.
     */
    public BufferedImage getImage();

    /**
     * Method for describing how to get an image width.
     * 
     * @return Image width.
     */
    public int getImageWidth();

    /**
     * Method for describing how to get an image height.
     * 
     * @return Image height.
     */
    public int getImageHeight();

    /**
     * Method for getting color overlay (useful in images that have alpha channel).
     * 
     * @return Color overlay as System.Drawing.Color.
     */
    public Color getColorOverlay();

    /**
     * Method for getting color overlay status.
     * 
     * @return True: if color overlay is using. False: if color overlay is not
     *         using.
     */
    public boolean isColorOverlay();

    /**
     * Method for getting rotation angle of an image.
     * 
     * @return Rotation angle.
     */
    public float getRotationAngle();

    /**
     * Method for getting bounds for an image (for example: to keep aspect ratio).
     * 
     * @return Bounds as com.spvessel.spacevil.Core.Area.
     */
    public Area getAreaBounds();

    /**
     * Method for getting an image filtering quality.
     * 
     * @return Image filtering quality as com.spvessel.spacevil.Flags.ImageQuality.
     */
    ImageQuality getImageQuality();
}