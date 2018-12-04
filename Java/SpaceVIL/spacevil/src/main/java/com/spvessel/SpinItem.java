package com.spvessel;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Core.MouseArgs;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.InputRestriction;
import com.spvessel.Flags.SizePolicy;

public class SpinItem extends Prototype {
    private static int count = 0;
    private HorizontalStack _horzStack = new HorizontalStack();
    private VerticalStack _vertStack = new VerticalStack();
    public ButtonCore upButton = new ButtonCore();
    public ButtonCore downButton = new ButtonCore();
    public TextEditRestricted textInput = new TextEditRestricted();

    /**
     * Constructs a SpinItem
     */
    public SpinItem() {
        setItemName("SpinItem_" + count);
        count++;
        _horzStack.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        textInput.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);

        setStyle(DefaultsService.getDefaultStyle(SpinItem.class));

        upButton.isFocusable = false;
        upButton.eventMouseClick.add(this::onUpClick);

        downButton.isFocusable = false;
        downButton.eventMouseClick.add(this::onDownClick);

        eventScrollUp.add(this::onUpClick);
        eventScrollDown.add(this::onDownClick);
    }

    private void onUpClick(Object sender, MouseArgs args) {
        textInput.increaseValue();
    }

    private void onDownClick(Object sender, MouseArgs args) {
        textInput.decreaseValue();
    }

    /**
     * Set SpinItem's parameters
     * @param currentValue SpinItem current value
     * @param minValue minimum available value
     * @param maxValue maximum available value
     * @param step SpinItem step
     */
    public void setParameters(int currentValue, int minValue, int maxValue, int step) {
        textInput.setParameters(currentValue, minValue, maxValue, step);
    }
    public void setParameters(double currentValue, double minValue, double maxValue, double step) {
        textInput.setParameters(currentValue, minValue, maxValue, step);
    }

    /**
     * Values accuracy (decimal places)
     */
    public void setAccuracy(int accuracy) {
        textInput.setAccuracy(accuracy);
    }

    /**
     * Initialization and adding of all elements in the SpinItem
     */
    @Override
    public void initElements() {
        addItem(_horzStack);
        _horzStack.addItems(textInput, _vertStack);
        _vertStack.addItems(upButton, downButton);
    }

    /**
     * Set style of the SpinItem
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);

        Style inner_style = style.getInnerStyle("buttonsarea");
        if (inner_style != null) {
            _vertStack.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("uparrow");
        if (inner_style != null) {
            upButton.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("downarrow");
        if (inner_style != null) {
            downButton.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("textedit");
        if (inner_style != null) {
            textInput.setStyle(inner_style);
        }
    }
}
