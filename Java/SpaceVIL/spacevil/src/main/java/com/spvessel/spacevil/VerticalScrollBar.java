package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceMouseMethodState;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.Orientation;

public class VerticalScrollBar extends VerticalStack {
    private static int count = 0;

    /**
     * VerticalScrollBar's upArrow
     */
    public ButtonCore upArrow = new ButtonCore();
    /**
     * VerticalScrollBar's downArrow
     */
    public ButtonCore downArrow = new ButtonCore();
    /**
     * VerticalScrollBar's slider
     */
    public VerticalSlider slider = new VerticalSlider();

    /**
     * Constructs a VerticalScrollBar
     */
    public VerticalScrollBar() {
        setItemName("VerticalScrollBar_" + count);
        count++;

        // Slider
        slider.handler.direction = Orientation.VERTICAL;

        // Arrows
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

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.VerticalScrollBar"));
        setStyle(DefaultsService.getDefaultStyle(VerticalScrollBar.class));
    }

    /**
     * Initialization and adding of all elements in the VerticalScrollBar
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
     * Is up and down arrows of the VerticalScrollBar visible
     */
    public void setArrowsVisible(boolean value) {
        upArrow.setVisible(value);
        downArrow.setVisible(value);
    }

    /**
     * Set style of the VerticalScrollBar
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