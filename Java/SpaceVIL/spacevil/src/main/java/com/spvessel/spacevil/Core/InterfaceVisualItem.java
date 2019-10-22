package com.spvessel.spacevil.Core;

import java.awt.Color;
import java.util.List;

import com.spvessel.spacevil.Decorations.Border;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.CustomFigure;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Spacing;
import com.spvessel.spacevil.Flags.GeometryEventType;
import com.spvessel.spacevil.Flags.InputEventType;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;

public interface InterfaceVisualItem extends InterfaceBaseItem {
    String getToolTip();

    void setToolTip(String text);

    Spacing getSpacing();

    void setSpacing(Spacing spacing);

    void setSpacing(int horizontal, int vertical);

    Indents getPadding();

    void setPadding(Indents padding);

    void setPadding(int left, int top, int right, int bottom);

    void setBorder(Border border);

    void setBorderFill(Color fill);

    Color getBorderFill();

    void setBorderFill(int r, int g, int b);

    void setBorderFill(int r, int g, int b, int a);

    void setBorderFill(float r, float g, float b);

    void setBorderFill(float r, float g, float b, float a);

    void setBorderRadius(CornerRadius radius);

    CornerRadius getBorderRadius();

    void setBorderThickness(int thickness);

    int getBorderThickness();

    void setBackground(int r, int g, int b);

    void setBackground(int r, int g, int b, int a);

    void setBackground(float r, float g, float b);

    void setBackground(float r, float g, float b, float a);

    void setMinSize(int width, int height);

    int[] getMinSize();

    void setMaxSize(int width, int height);

    int[] getMaxSize();

    void setAlignment(List<ItemAlignment> alignment);

    void setSizePolicy(SizePolicy width, SizePolicy height);

    void setShadowExtension(int wExtension, int hExtension);

    void addItemState(ItemStateType type, ItemState state);

    void removeItemState(ItemStateType type);

    void removeAllItemStates();

    ItemState getState(ItemStateType type);

    void updateState();

    void insertItem(InterfaceBaseItem item, int index);

    void addItem(InterfaceBaseItem item);

    boolean isPassEvents();

    boolean isPassEvents(InputEventType e);

    List<InputEventType> getPassEvents();

    List<InputEventType> getNonPassEvents();

    void setPassEvents(boolean value);

    void setPassEvents(boolean value, InputEventType e);

    void setPassEvents(boolean value, List<InputEventType> events_set);

    void setPassEvents(boolean value, InputEventType... events_set);

    boolean isDisabled();

    void setDisabled(boolean value);

    boolean isMouseHover();

    void setMouseHover(boolean value);

    boolean isMousePressed();

    void setMousePressed(boolean value);

    boolean isFocused();

    void setFocused(boolean value);

    boolean getHoverVerification(float xpos, float ypos);

    List<InterfaceBaseItem> getItems();

    boolean removeItem(InterfaceBaseItem item);

    void clear();

    void addEventListener(GeometryEventType type, InterfaceBaseItem listener);

    void removeEventListener(GeometryEventType type, InterfaceBaseItem listener);

    ItemStateType getCurrentStateType();

    void setState(ItemStateType state);

    void setContent(List<InterfaceBaseItem> content);

    CustomFigure isCustomFigure();

    void setCustomFigure(CustomFigure figure);
}
