package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceMouseMethodState;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.Orientation;

/**
 * VerticalScrollBar is the basic implementation of a user interface scroll bar
 * (vertical version).
 * <p>
 * Contains arrow buttons, slider.
 * <p>
 * By default ability to get focus is disabled.
 * <p>
 * Supports all events except drag and drop.
 */
public class VerticalScrollBar extends VerticalStack {
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
    public VerticalSlider slider = new VerticalSlider();

    /**
     * Default VerticalScrollBar constructor.
     */
    public VerticalScrollBar() {
        isFocusable = false;
        setItemName("VerticalScrollBar_" + count);
        count++;

        // Slider
        slider.handler.orientation = Orientation.VERTICAL;

        setStyle(DefaultsService.getDefaultStyle(VerticalScrollBar.class));
    }

    /**
     * Initializing all elements in the VerticalScrollBar.
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

        // Arrows
        InterfaceMouseMethodState upScroll = (sender, args) -> {
            float value = slider.getCurrentValue();
            value -= slider.getStep();
            if (value < slider.getMinValue())
                value = slider.getMinValue();
            slider.setCurrentValue(value);
        };
        upArrow.eventMouseClick.add(upScroll);
        eventScrollUp.add(upScroll);

        InterfaceMouseMethodState downScroll = (sender, args) -> {
            float value = slider.getCurrentValue();
            value += slider.getStep();
            if (value > slider.getMaxValue())
                value = slider.getMaxValue();
            slider.setCurrentValue(value);
        };
        downArrow.eventMouseClick.add(downScroll);
        eventScrollDown.add(downScroll);
    }

    /**
     * Setting Up and Down arrow buttons visibility of the VerticalScrollBar.
     * 
     * @param value True: if you want buttons visible. False: if you want buttons
     *              invisible.
     */
    public void setArrowsVisible(boolean value) {
        upArrow.setVisible(value);
        downArrow.setVisible(value);
    }

    /**
     * Seting style of the VerticalScrollBar.
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

        Style innerStyle = style.getInnerStyle("uparrow");
        if (innerStyle != null) {
            upArrow.setStyle(innerStyle);
        }
        innerStyle = style.getInnerStyle("downarrow");
        if (innerStyle != null) {
            downArrow.setStyle(innerStyle);
        }
        innerStyle = style.getInnerStyle("slider");
        if (innerStyle != null) {
            slider.setStyle(innerStyle);
        }
    }
}