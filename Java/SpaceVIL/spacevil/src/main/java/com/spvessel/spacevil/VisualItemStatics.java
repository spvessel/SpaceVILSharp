package com.spvessel.spacevil;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.spvessel.spacevil.Decorations.Border;
import com.spvessel.spacevil.Decorations.Figure;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Spacing;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemStateType;

final class VisualItemStatics {
    private VisualItemStatics() {
    }

    static void updateState(VisualItem item) {
        ItemState baseState = item.getState(ItemStateType.Base);
        ItemState currentState = item.getState(item.getCurrentStateType());
        
        item.setBackgroundDirect(currentState.background);

        if (currentState.shape != null) {
            if (item.isCustomFigure() != currentState.shape)
                item.setCustomFigure(currentState.shape);
        }

        ItemState disabledState = item.getState(ItemStateType.Disabled);
        if (item.isDisabled() && disabledState != null) {
            updateVisualProperties(item, disabledState, baseState);
            return;
        }

        ItemState focusedState = item.getState(ItemStateType.Focused);
        if (item.isFocused() && focusedState != null) {
            updateVisualProperties(item, focusedState, baseState);
            baseState = focusedState;
        }
        ItemState hoverState = item.getState(ItemStateType.Hovered);
        if (item.isMouseHover() && hoverState != null) {
            updateVisualProperties(item, hoverState, baseState);
            baseState = hoverState;
        }
        ItemState pressedState = item.getState(ItemStateType.Pressed);
        if (item.isMousePressed() && pressedState != null) {
            updateVisualProperties(item, pressedState, baseState);
            baseState = pressedState;
        }

        if (baseState == currentState) {
            Border border = currentState.border.clone();
            if (!item.getBorder().getRadius().equals(baseState.border.getRadius())) {
                ItemsRefreshManager.setRefreshShape(item.prototype);
            }
            item.border = border;
        }
    }

    static void updateVisualProperties(VisualItem item, ItemState state, ItemState prevState) {
        ItemState currentState = item.getState(item.getCurrentStateType());
        item.setBackgroundDirect(
                GraphicsMathService.mixColors(currentState.background, item.getBackground(), state.background));

        Border borderCurrentState = currentState.border.clone();
        Border borderState = state.border.clone();

        //CASE 1: item has CornerRadius(!=0) but other states has CornerRadius(0)
        if (!borderCurrentState.getRadius().isCornersZero()) {
            if (!borderState.getRadius().isCornersZero()) {
                if (!borderCurrentState.getRadius().equals(borderState.getRadius())) {
                    item.border.setRadius(borderState.getRadius());
                    ItemsRefreshManager.setRefreshShape(item.prototype);
                }
            } else {
                if (!prevState.border.getRadius().isCornersZero()) {
                    if (!borderCurrentState.getRadius().equals(prevState.border.getRadius())) {
                        item.border.setRadius(prevState.border.getRadius());
                        ItemsRefreshManager.setRefreshShape(item.prototype);
                    }
                }
            }
        }
        if (borderState.getThickness() >= 0) {
            item.border.setThickness(borderState.getThickness());
        } else {
            if (prevState.border.getThickness() >= 0)
                item.border.setThickness(prevState.border.getThickness());
        }

        if (borderState.getColor().getAlpha() > 0) {
            item.border.setColor(borderState.getColor());
        } else {
            if (prevState.border.getColor().getAlpha() > 0)
                item.border.setColor(prevState.border.getColor());
        }
        if (state.shape != null)
            item.setCustomFigure(state.shape);
    }

    static boolean getHoverVerification(VisualItem item, float xpos, float ypos) {
        switch (item.HoverRule) {
        case Lazy:
            return lazyHoverVerification(item, xpos, ypos);
        case Strict:
            return strictHoverVerification(item, xpos, ypos);
        default:
            return false;
        }
    }

    static boolean strictHoverVerification(VisualItem item, float xpos, float ypos) {
        List<float[]> tmp = BaseItemStatics.updateShape(item);
        if (tmp == null)
            return false;

        float Ax, Ay, Bx, By, Cx, Cy, Px, Py, case1, case2, case3;
        Px = xpos - item.getX();
        Py = ypos - item.getY();

        for (int point = 0; point < tmp.size(); point += 3) {

            Ax = tmp.get(point)[0];
            Ay = tmp.get(point)[1];

            Bx = tmp.get(point + 1)[0];
            By = tmp.get(point + 1)[1];

            Cx = tmp.get(point + 2)[0];
            Cy = tmp.get(point + 2)[1];

            case1 = (Ax - Px) * (By - Ay) - (Bx - Ax) * (Ay - Py);
            case2 = (Bx - Px) * (Cy - By) - (Cx - Bx) * (By - Py);
            case3 = (Cx - Px) * (Ay - Cy) - (Ax - Cx) * (Cy - Py);

            if ((case1 >= 0 && case2 >= 0 && case3 >= 0) || (case1 <= 0 && case2 <= 0 && case3 <= 0))
                return true;
        }

        return false;
    }

    static boolean lazyHoverVerification(VisualItem item, float xpos, float ypos) {

        boolean result = false;
        float minx = item.getX();
        float maxx = item.getX() + item.getWidth();
        float miny = item.getY();
        float maxy = item.getY() + item.getHeight();

        if (item._confinesX0 > minx)
            minx = item._confinesX0;

        if (item._confinesX1 < maxx)
            maxx = item._confinesX1;

        if (item._confinesY0 > miny)
            miny = item._confinesY0;

        if (item._confinesY1 < maxy)
            maxy = item._confinesY1;

        // if (xpos >= minx && xpos <= maxx && ypos >= miny && ypos <= maxy) {
        if (xpos > minx && xpos < maxx && ypos > miny && ypos < maxy) {
            result = true;
        }
        return result;
    }

    static void setStyle(VisualItem item, Style style) {
        if (style == null)
            return;

        item.setPosition(style.x, style.y);
        item.setSize(style.width, style.height);
        item.setSizePolicy(style.widthPolicy, style.heightPolicy);
        item.setPadding(style.padding);
        item.setMargin(style.margin);
        item.setAlignment(style.alignment);
        item.setSpacing(style.spacing);
        item.setMinSize(style.minWidth, style.minHeight);
        item.setMaxSize(style.maxWidth, style.maxHeight);
        item.setBackground(style.background);
        item.setBorder(style.border);
        item.effects().add(style.shadow);
        item.setVisible(style.isVisible);
        item.removeAllItemStates();

        ItemState coreState = new ItemState(style.background);
        coreState.border = style.border.clone();

        for (Map.Entry<ItemStateType, ItemState> state : style.getAllStates().entrySet()) {
            item.addItemState(state.getKey(), state.getValue());
        }

        if (style.shape != null) {
            item.setCustomFigure(new Figure(style.isFixedShape, style.shape));
            coreState.shape = item.isCustomFigure();
        }
        item.addItemState(ItemStateType.Base, coreState);
    }

    static Style extractCoreStyle(VisualItem item) {
        Style style = new Style();
        style.setSize(item.getWidth(), item.getHeight());
        style.setSizePolicy(item.getWidthPolicy(), item.getHeightPolicy());
        style.background = item.getBackground();
        style.minWidth = item.getMinWidth();
        style.minHeight = item.getMinHeight();
        style.maxWidth = item.getMaxWidth();
        style.maxHeight = item.getMaxHeight();
        style.x = item.getX();
        style.y = item.getY();
        style.padding = new Indents(item.getPadding().left, item.getPadding().top, item.getPadding().right,
                item.getPadding().bottom);
        style.margin = new Indents(item.getMargin().left, item.getMargin().top, item.getMargin().right,
                item.getMargin().bottom);
        style.spacing = new Spacing(item.getSpacing().horizontal, item.getSpacing().vertical);
        style.alignment = new LinkedList<>(item.getAlignment());
        style.border = item.border.clone();
        style.isVisible = item.isVisible();

        if (item.isCustomFigure() != null) {
            style.shape = item.isCustomFigure().getFigure();
            style.isFixedShape = item.isCustomFigure().isFixed();
        }
        for (Map.Entry<ItemStateType, ItemState> state : item.states.entrySet()) {
            style.addItemState(state.getKey(), state.getValue());
        }

        return style;
    }
}