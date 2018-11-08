package com.spvessel.Decorations;

import com.spvessel.Decorations.Border;
import com.spvessel.Decorations.CustomFigure;

import java.awt.*;

public class ItemState {
    public Boolean value = true;
    public Color background;
    public Border border = new Border();
    // public Color Foreground = Color.Black;
    // public string Text = null;
    // public string ImageUri = null;
    // public bool IsVisible = true;
    public CustomFigure shape = null;

    public ItemState() {

    }

    public ItemState(Color background) {
        this.background = background;
    }
}
