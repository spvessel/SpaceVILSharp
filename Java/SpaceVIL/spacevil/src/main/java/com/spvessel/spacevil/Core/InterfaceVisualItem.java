package com.spvessel.spacevil.Core;

import java.awt.Color;
import java.util.List;

import com.spvessel.spacevil.Decorations.Border;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.Figure;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Spacing;
import com.spvessel.spacevil.Flags.GeometryEventType;
import com.spvessel.spacevil.Flags.InputEventType;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;

/**
 * Not implemented!!!
 */
public interface InterfaceVisualItem extends InterfaceBaseItem {
    public String getToolTip();

    public void setToolTip(String text);

    public Spacing getSpacing();

    public void setSpacing(Spacing spacing);

    public void setSpacing(int horizontal, int vertical);

    public Indents getPadding();

    public void setPadding(Indents padding);

    public void setPadding(int left, int top, int right, int bottom);

    public void setBorder(Border border);

    public void setBorderFill(Color fill);

    public Color getBorderFill();

    public void setBorderFill(int r, int g, int b);

    public void setBorderFill(int r, int g, int b, int a);

    public void setBorderFill(float r, float g, float b);

    public void setBorderFill(float r, float g, float b, float a);

    public void setBorderRadius(CornerRadius radius);

    public CornerRadius getBorderRadius();

    public void setBorderThickness(int thickness);

    public int getBorderThickness();

    public void setBackground(int r, int g, int b);

    public void setBackground(int r, int g, int b, int a);

    public void setBackground(float r, float g, float b);

    public void setBackground(float r, float g, float b, float a);

    public void setMinSize(int width, int height);

    public int[] getMinSize();

    public void setMaxSize(int width, int height);

    public int[] getMaxSize();

    public void setAlignment(List<ItemAlignment> alignment);

    public void setSizePolicy(SizePolicy width, SizePolicy height);

    public void setShadowExtension(int wExtension, int hExtension);

    public void addItemState(ItemStateType type, ItemState state);

    public void removeItemState(ItemStateType type);

    public void removeAllItemStates();

    public ItemState getState(ItemStateType type);

    public void updateState();

    public void insertItem(InterfaceBaseItem item, int index);

    public void addItem(InterfaceBaseItem item);

    public boolean isPassEvents();

    public boolean isPassEvents(InputEventType e);

    public List<InputEventType> getPassEvents();

    public List<InputEventType> getNonPassEvents();

    public void setPassEvents(boolean value);

    public void setPassEvents(boolean value, InputEventType e);

    public void setPassEvents(boolean value, List<InputEventType> events_set);

    public void setPassEvents(boolean value, InputEventType... events_set);

    public boolean isDisabled();

    public void setDisabled(boolean value);

    public boolean isMouseHover();

    public void setMouseHover(boolean value);

    public boolean isMousePressed();

    public void setMousePressed(boolean value);

    public boolean isFocused();

    public void setFocused(boolean value);

    public boolean getHoverVerification(float xpos, float ypos);

    public List<InterfaceBaseItem> getItems();

    public boolean removeItem(InterfaceBaseItem item);

    public void clear();

    public void addEventListener(GeometryEventType type, InterfaceBaseItem listener);

    public void removeEventListener(GeometryEventType type, InterfaceBaseItem listener);

    public ItemStateType getCurrentStateType();

    public void setState(ItemStateType state);

    public void setContent(List<InterfaceBaseItem> content);

    public Figure isCustomFigure();

    public void setCustomFigure(Figure figure);
}
