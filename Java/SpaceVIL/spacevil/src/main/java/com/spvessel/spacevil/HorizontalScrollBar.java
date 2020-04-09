package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceMouseMethodState;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.Orientation;

/**
 * HorizontalScrollBar is the basic implementation of a user interface scroll
 * bar (horizontal version).
 * <p>
 * Contains arrow buttons, slider.
 * <p>
 * By default ability to get focus is disabled.
 * <p>
 * Supports all events except drag and drop.
 */
public class HorizontalScrollBar extends HorizontalStack {
    private static int count = 0;
    /**
     * Button to scroll up.
     */
    public ButtonCore upArrow = new ButtonCore();
    /**
     * Button to scroll down.
     */
    public ButtonCore downArrow = new ButtonCore();
    /**
     * Slider for scrolling with mouse drag and drop ivents or mouse wheel.
     */
    public HorizontalSlider slider = new HorizontalSlider();

    /**
     * Default HorizontalScrollBar constructor.
     */
    public HorizontalScrollBar() {
        isFocusable = false;
        setItemName("HorizontalScrollBar_" + count);
        count++;

        slider.handler.orientation = Orientation.HORIZONTAL;

        InterfaceMouseMethodState up_click = (sender, args) -> {
            float value = slider.getCurrentValue();
            value -= slider.getStep();
            if (value < slider.getMinValue())
                value = slider.getMinValue();
            slider.setCurrentValue(value);
        };
        upArrow.eventMouseClick.add(up_click);
        eventScrollUp.add(up_click);

        InterfaceMouseMethodState down_click = (sender, args) -> {
            float value = slider.getCurrentValue();
            value += slider.getStep();
            if (value > slider.getMaxValue())
                value = slider.getMaxValue();
            slider.setCurrentValue(value);
        };
        downArrow.eventMouseClick.add(down_click);
        eventScrollDown.add(down_click);

        setStyle(DefaultsService.getDefaultStyle(HorizontalScrollBar.class));
    }

    /**
     * Initializing all elements in the HorizontalScrollBar.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        upArrow.isFocusable = false;
        downArrow.isFocusable = false;
        slider.isFocusable = false;
        slider.handler.isFocusable = false;
        addItems(upArrow, slider, downArrow);
    }

    /**
     * Setting Up and Down arrow buttons visibility of the HorizontalScrollBar.
     * 
     * @param value True: if you want buttons visible. False: if you want buttons
     *              invisible.
     */
    public void setArrowsVisible(boolean value) {
        upArrow.setVisible(value);
        downArrow.setVisible(value);
    }

    /**
     * Setting style of the ButtonCore.
     * <p>
     * Inner styles: "uparrow", "downarrow", "slider".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);

        Style inner_style = style.getInnerStyle("uparrow");
        if (inner_style != null) {
            upArrow.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("downarrow");
        if (inner_style != null) {
            downArrow.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("slider");
        if (inner_style != null) {
            slider.setStyle(inner_style);
        }
    }
}