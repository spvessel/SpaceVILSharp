package com.spvessel.spacevil;

import java.awt.Color;
import java.awt.Font;

import com.spvessel.spacevil.ToolTipItem;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Decorations.Border;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Decorations.Style;

public final class ToolTip {

    private ToolTip() {
    }

    private static ToolTipItem getToolTip(CoreWindow window) {
        return window.getLayout().getToolTip();
    }

    public static void setStyle(CoreWindow window, Style style) {
        if (window == null)
            return;
        ToolTipItem toolTip = getToolTip(window);
        toolTip.setStyle(style);
    }

    public static void setTimeOut(CoreWindow window, int ms) {
        if (window == null)
            return;
        ToolTipItem toolTip = getToolTip(window);
        toolTip.setTimeOut(ms);
    }

    public static int getTimeOut(CoreWindow window) {
        if (window == null)
            return -1;
        ToolTipItem toolTip = getToolTip(window);
        return toolTip.getTimeOut();
    }

    public static void setBackground(CoreWindow window, Color color) {
        if (window == null)
            return;
        ToolTipItem toolTip = getToolTip(window);
        toolTip.setBackground(color);
    }

    public static void setForeground(CoreWindow window, Color color) {
        if (window == null)
            return;
        ToolTipItem toolTip = getToolTip(window);
        toolTip.setForeground(color);
    }

    public static void setFont(CoreWindow window, Font font) {
        if (window == null)
            return;
        ToolTipItem toolTip = getToolTip(window);
        toolTip.setFont(font);
    }

    public static void setBorder(CoreWindow window, Border border) {
        if (window == null)
            return;
        ToolTipItem toolTip = getToolTip(window);
        toolTip.setBorder(border);
    }

    public static void setShadow(CoreWindow window, Shadow shadow) {
        if (window == null)
            return;
        ToolTipItem toolTip = getToolTip(window);
        toolTip.setShadow(shadow.getRadius(), shadow.getXOffset(), shadow.getYOffset(), shadow.getColor());
    }

    public static void setShadowDrop(CoreWindow window, boolean value) {
        if (window == null)
            return;
        ToolTipItem toolTip = getToolTip(window);
        toolTip.setShadowDrop(value);
    }

    public static void addItems(CoreWindow window, InterfaceBaseItem... items) {
        if (window == null)
            return;
        ToolTipItem toolTip = getToolTip(window);
        toolTip.addItems(items);
    }
}
