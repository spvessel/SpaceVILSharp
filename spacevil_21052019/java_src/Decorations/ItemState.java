package com.spvessel.spacevil.Decorations;

import java.awt.*;

public class ItemState {
    /**
     * A class that describes state of the item and its changes according
     * to state value
     */
    public Boolean value = true;
    public Color background;
    public Border border = new Border();
    // public Color Foreground = Color.Black;
    // public string Text = null;
    // public string ImageUri = null;
    // public bool IsVisible = true;
    public CustomFigure shape = null;

    /**
     * Constructs an empty ItemState
     */
    public ItemState() {

    }

    /**
     * Constructs an ItemState with background color
     */
    public ItemState(Color background) {
        this.background = background;
    }
}
