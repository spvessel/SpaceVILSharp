package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceMouseMethodState;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.Orientation;

public class HorizontalScrollBar extends HorizontalStack {
    private static int count = 0;

    public ButtonCore upArrow = new ButtonCore();
    public ButtonCore downArrow = new ButtonCore();
    public HorizontalSlider slider = new HorizontalSlider();

    /**
     * Constructs a HorizontalScrollBar
     */
    public HorizontalScrollBar() {
        setItemName("HorizontalScrollBar_" + count);
        count++;

        slider.handler.direction = Orientation.HORIZONTAL;
        
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

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.HorizontalScrollBar"));
        setStyle(DefaultsService.getDefaultStyle(HorizontalScrollBar.class));
    }

    /**
     * Initialization and adding of all elements in the HorizontalScrollBar
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
     * Set Left and right arrows of the HorizontalScrollBar visible
     */
    public void setArrowsVisible(boolean value) {
        upArrow.setVisible(value);
        downArrow.setVisible(value);
    }

    /**
     * Set style of the HorizontalScrollBar
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