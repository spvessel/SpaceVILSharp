package com.spvessel.spacevil;

import java.awt.Color;
import java.awt.Font;

import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Decorations.Border;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Decorations.Style;

/**
 * ToolTip is a static class for com.spvessel.spacevil.ToolTipItem managing.
 * <p>
 * Every window has its own ToolTipItem.
 */
public final class ToolTip {

    private ToolTip() {
    }

    private static ToolTipItem getToolTip(CoreWindow window) {
        return window.getLayout().getToolTip();
    }

    /**
     * Setting style for ToolTipItem of the specified window.
     * <p>
     * Every window has its own ToolTipItem.
     * 
     * @param window Window as com.spvessel.spacevil.CoreWindow.
     * @param style  Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static void setStyle(CoreWindow window, Style style) {
        if (window == null)
            return;
        ToolTipItem toolTip = getToolTip(window);
        toolTip.setStyle(style);
    }

    /**
     * Setting waiting time in milliseconds after which ToolTipItem appears.
     * <p>
     * Every window has its own ToolTipItem.
     * <p>
     * Default: 500 milliseconds (0.5 seconds).
     * 
     * @param window Window as com.spvessel.spacevil.CoreWindow.
     * @param ms     Waiting time in milliseconds.
     */
    public static void setTimeOut(CoreWindow window, int ms) {
        if (window == null)
            return;
        ToolTipItem toolTip = getToolTip(window);
        toolTip.setTimeOut(ms);
    }

    /**
     * Getting current waiting time in milliseconds after which ToolTipItem appears.
     * <p>
     * Every window has its own ToolTipItem.
     * 
     * @param window Window as com.spvessel.spacevil.CoreWindow.
     * @return Current waiting time in milliseconds.
     */
    public static int getTimeOut(CoreWindow window) {
        if (window == null)
            return -1;
        ToolTipItem toolTip = getToolTip(window);
        return toolTip.getTimeOut();
    }

    /**
     * Setting background color of ToolTipItem.
     * <p>
     * Every window has its own ToolTipItem.
     * 
     * @param window Window as com.spvessel.spacevil.CoreWindow.
     * @param color Background color as java.awt.Color.
     */
    public static void setBackground(CoreWindow window, Color color) {
        if (window == null)
            return;
        ToolTipItem toolTip = getToolTip(window);
        toolTip.setBackground(color);
    }

    /**
     * Setting text color of ToolTipItem.
     * <p>
     * Every window has its own ToolTipItem.
     * 
     * @param window Window as com.spvessel.spacevil.CoreWindow.
     * @param color Text color as java.awt.Color.
     */
    public static void setForeground(CoreWindow window, Color color) {
        if (window == null)
            return;
        ToolTipItem toolTip = getToolTip(window);
        toolTip.setForeground(color);
    }

    /**
     * Setting text font of ToolTipItem.
     * <p>
     * Every window has its own ToolTipItem.
     * 
     * @param window Window as com.spvessel.spacevil.CoreWindow.
     * @param font Font as java.awt.Font.
     */
    public static void setFont(CoreWindow window, Font font) {
        if (window == null)
            return;
        ToolTipItem toolTip = getToolTip(window);
        toolTip.setFont(font);
    }

    /**
     * Setting border for ToolTipItem.
     * <p>
     * Every window has its own ToolTipItem.
     * 
     * @param window Window as com.spvessel.spacevil.CoreWindow.
     * @param border Border as com.spvessel.spacevil.Decorations.Border.
     */
    public static void setBorder(CoreWindow window, Border border) {
        if (window == null)
            return;
        ToolTipItem toolTip = getToolTip(window);
        toolTip.setBorder(border);
    }

    /**
     * Setting shadow for ToolTipItem.
     * <p>
     * Every window has its own ToolTipItem.
     * 
     * @param window Window as com.spvessel.spacevil.CoreWindow.
     * @param shadow Shadow as com.spvessel.spacevil.Decorations.Shadow.
     */
    public static void setShadow(CoreWindow window, Shadow shadow) {
        if (window == null)
            return;
        ToolTipItem toolTip = getToolTip(window);
        toolTip.setShadow(shadow.getRadius(), shadow.getXOffset(), shadow.getYOffset(), shadow.getColor());
    }

    /**
     * Setting ToolTipItem shadow visibility.
     * <p>
     * Every window has its own ToolTipItem.
     * <p>
     * True: shadow is visible. False: shadow is invisible.
     * 
     * @param window Window as com.spvessel.spacevil.CoreWindow.
     * @param value  True: shadow is visible. False: shadow is invisible.
     */
    public static void setShadowDrop(CoreWindow window, boolean value) {
        if (window == null)
            return;
        ToolTipItem toolTip = getToolTip(window);
        toolTip.setShadowDrop(value);
    }

    /**
     * Adding item to the ToolTipItem for decoration or extension.
     * <p>
     * Every window has its own ToolTipItem.
     * 
     * @param window Window as com.spvessel.spacevil.CoreWindow.
     * @param items  Sequence of items as
     *               com.spvessel.spacevil.Core.InterfaceBaseItem.
     */
    public static void addItems(CoreWindow window, InterfaceBaseItem... items) {
        if (window == null)
            return;
        ToolTipItem toolTip = getToolTip(window);
        toolTip.addItems(items);
    }
}
