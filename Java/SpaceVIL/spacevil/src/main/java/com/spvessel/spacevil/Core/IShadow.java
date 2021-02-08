package com.spvessel.spacevil.Core;

import java.awt.Color;

/**
 * IShadow is an interface for creating classes that decorates item's shape with
 * shadow.
 */
public interface IShadow extends IEffect {
    /**
     * Getting the shadow blur raduis.
     * 
     * @return The blur radius of the shadow. Min value: 0. Max value: 10.
     */
    int getRadius();

    /**
     * Getting the offset of the shadow relative to the position of the item.
     * 
     * @return Shadow offset as com.spvessel.spacevil.Core.Position.
     */
    Position getOffset();

    /**
     * Getting shadow color.
     * 
     * @return Returns the shadow color as java.awt.Color.
     */
    Color getColor();

    /**
     * Getting the values of shadow extensions in pixels.
     * 
     * @return The values of shadow extensions as com.spvessel.spacevil.Core.Size.
     */
    Size getExtension();
}
