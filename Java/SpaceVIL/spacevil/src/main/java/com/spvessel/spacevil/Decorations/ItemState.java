package com.spvessel.spacevil.Decorations;

import java.awt.*;

/**
 * A class that describes state of the item and its changes according
 * to state value.
 */
public class ItemState {
    public Boolean value = true;

    /**
     * Getting backgroud color of the item of current state as java.awt.Color.
     */
    public Color background;

    /**
     * Getting border of the item of current state as com.spvessel.spacevil.Decorations.Border.
     */
    public Border border = new Border();
    
    /**
     * Getting shape of the item of current state as com.spvessel.spacevil.Decorations.Figure.
     */
    public Figure shape = null;

    /**
     * Constructs an empty ItemState
     */
    public ItemState() {

    }

    /**
     * Constructs an ItemState with the specified background color.
     * @param background A color of item as java.awt.Color.
     */
    public ItemState(Color background) {
        this.background = background;
    }
}
