package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.EventCommonMethodState;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.Orientation;

public class HorizontalSlider extends Prototype {
    /**
     * Part of HorizontalScrollBar
     */
    static int count = 0;

    public Rectangle track = new Rectangle();
    public ScrollHandler handler = new ScrollHandler();

    // Values
    private float _step = 1.0f;

    /**
     * HorizontalSlider moving step when HorizontalScrollBar arrows pressed
     */
    public void setStep(float value) {
        _step = value;
    }

    public float getStep() {
        return _step;
    }

    public EventCommonMethodState eventValueChanged = new EventCommonMethodState();

    @Override
    public void release() {
        eventValueChanged.clear();
    }

    private float _currentValue = 0;

    boolean _ignoreStep = true;

    public void setIgnoreStep(boolean value) {
        _ignoreStep = value;
    }

    public boolean isIgnoreStep() {
        return _ignoreStep;
    }

    /**
     * Position value of the HorizontalSlider
     */
    public void setCurrentValue(float value) {

        _currentValue = value;
        
        if (!_ignoreStep)
            _currentValue = (float) Math.round(_currentValue / _step) * _step;

        if (_currentValue < _minValue)
            _currentValue = _minValue;
        if (_currentValue > _maxValue)
            _currentValue = _maxValue;

        updateHandler(); // refactor!!

        if (eventValueChanged != null)
            eventValueChanged.execute(this);
    }

    public float getCurrentValue() {
        return _currentValue;
    }

    private int getSumOfHorizontalIndents() {
        Indents marginHandler = handler.getMargin();
        Indents paddingSlider = getPadding();
        int margin = marginHandler.left + marginHandler.right;
        int padding = paddingSlider.left + paddingSlider.right;
        return margin + padding;
    }

    void updateHandler() {
        float offset = ((float) getWidth() - getSumOfHorizontalIndents() - handler.getWidth())
                / (_maxValue - _minValue) * (_currentValue - _minValue);
        handler.setOffset((int) offset + getPadding().left + handler.getMargin().left);
    }

    private float _minValue = 0;

    /**
     * Minimum value of the HorizontalSlider
     */
    public void setMinValue(float value) {
        _minValue = value;
    }

    public float getMinValue() {
        return _minValue;
    }

    private float _maxValue = 100;

    /**
     * Maximum value of the HorizontalSlider
     */
    public void setMaxValue(float value) {
        _maxValue = value;
    }

    public float getMaxValue() {
        return _maxValue;
    }

    /**
     * Constructs a HorizontalSlider
     */
    public HorizontalSlider() {
        setItemName("HorizontalSlider_" + count);
        eventMouseClick.add(this::onTrackClick);
        count++;

        handler.direction = Orientation.HORIZONTAL;

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.HorizontalSlider"));
        setStyle(DefaultsService.getDefaultStyle(HorizontalSlider.class));
    }

    /**
     * Initialization and adding of all elements in the HorizontalSlider
     */
    @Override
    public void initElements() {
        // Adding
        addItems(track, handler);

        // Event connections
        eventMouseDrop.add(this::onDragHandler);
        handler.eventMouseDrag.add(eventMouseDrop::execute);
    }

    private boolean _dragging = false;

    protected void onDragHandler(InterfaceItem sender, MouseArgs args)// что-то с тобой не так
    {
        _dragging = true;
        // иногда число NAN
        float result = (float) (handler.getX() - getX()) * (_maxValue - _minValue)
                / ((float) getWidth() - getSumOfHorizontalIndents() - handler.getWidth()) + _minValue;
        if (!Float.isNaN(result))
            setCurrentValue(result);
    }

    protected void onTrackClick(InterfaceItem sender, MouseArgs args) {
        // Compute CurrentValue
        if (!_dragging)
            setCurrentValue((float) (args.position.getX() - getX() - handler.getWidth() / 2) * (_maxValue - _minValue)
                    / ((float) getWidth() - getSumOfHorizontalIndents() - handler.getWidth()));
        _dragging = false;
    }

    /**
     * Set X position of the HorizontalSlider
     */
    @Override
    public void setX(int x) {
        super.setX(x);
        updateHandler();
    }

    /**
     * Set style of the HorizontalSlider
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);

        Style inner_style = style.getInnerStyle("track");
        if (inner_style != null) {
            track.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("handler");
        if (inner_style != null) {
            handler.setStyle(inner_style);
        }
    }
}