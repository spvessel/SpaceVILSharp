package com.spvessel.spacevil.Core;

import java.awt.Color;

/**
 * An interface that describes texture for text rendering.
 */
public interface InterfaceTextContainer {
    /**
     * Method for getting text texture.
     * @return Texture as com.spvessel.spacevil.Core.InterfaceTextImage.
     */
    public InterfaceTextImage getTexture();

    /**
     * Method for getting text color.
     * @return Text color as java.awt.Color.
     */
    public Color getForeground();
}