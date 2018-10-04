package com.spvessel.Items;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Cores.InterfaceMouseMethodState;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.Orientation;

public class HorizontalScrollBar extends HorizontalStack {
    private static int count = 0;

    public ButtonCore upArrow = new ButtonCore();
    public ButtonCore downArrow = new ButtonCore();
    public HorizontalSlider slider = new HorizontalSlider();

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

        setStyle(DefaultsService.getDefaultStyle("SpaceVIL.HorizontalScrollBar"));
    }

    @Override
    public void initElements() {
        addItems(upArrow, slider, downArrow);
    }

    public void setArrowsVisible(boolean value) {
        upArrow.setVisible(value);
        downArrow.setVisible(value);
    }

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