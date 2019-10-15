package com.spvessel.spacevil.Core;

import java.util.List;

import com.spvessel.spacevil.Decorations.CustomFigure;
import com.spvessel.spacevil.Flags.ItemAlignment;

public interface InterfaceSubtractFigure {
    public void setSubtractFigure(CustomFigure figure);
    public CustomFigure getSubtractFigure();
    public void setPositionOffset(int x, int y);
    public void setSizeScale(float wScale, float hScale);
    public int getXOffset();
    public int getYOffset();
    public float getWidthScale();
    public float getHeightScale();
    public void setAlignment(ItemAlignment... alignment);
    public List<ItemAlignment> getAlignment();
}
