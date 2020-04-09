package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.EventCommonMethodState;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.Orientation;

/**
 * VerticalSlider is the basic implementation of a user interface slider
 * (vertical version).
 * <p>
 * Contains track, handler.
 * <p>
 * Supports all events including drag and drop (internal handler
 * (com.spvessel.spacevil.ScrollHandler) supports drag and drop events).
 */
public class VerticalSlider extends Prototype {
    private static int count = 0;

    /**
     * Slider track.
     */
    public Rectangle track = new Rectangle();

    /**
     * Slider handler.
     */
    public ScrollHandler handler = new ScrollHandler();

    private float _step = 1.0f;

    /**
     * Setting slider movement step.
     * 
     * @param value Slider step.
     */
    public void setStep(float value) {
        _step = value;
    }

    /**
     * Getting slider movement step.
     * 
     * @return Slider step.
     */
    public float getStep() {
        return _step;
    }

    /**
     * Event that is invoked when value of the slider is changed.
     */
    public EventCommonMethodState eventValueChanged = new EventCommonMethodState();

    /**
     * Disposing all resources if the item was removed.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void release() {
        eventValueChanged.clear();
    }

    private float _currentValue = 0;

    boolean _ignoreStep = true;

    /**
     * Ignoring slider step (affects only on animation). Set False if you want the
     * slider to move strictly in steps.
     * <p>
     * Default: True.
     * 
     * @param value
     */
    public void setIgnoreStep(boolean value) {
        _ignoreStep = value;
    }

    /**
     * Returns True if slider movement ignore steps otherwise returns False.
     * 
     * @return True: if movement step is ignored. False: if movement step is not
     *         ignored.
     */
    public boolean isIgnoreStep() {
        return _ignoreStep;
    }

    /**
     * Setting the current slider value. If the value is greater/less than the
     * maximum/minimum slider value, then the slider value becomes equal to the
     * maximum/minimum value.
     * 
     * @param value Slider value.
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

    private int getSumOfVerticalIndents() {
        Indents marginHandler = handler.getMargin();
        Indents paddingSlider = getPadding();
        int margin = marginHandler.top + marginHandler.bottom;
        int padding = paddingSlider.top + paddingSlider.bottom;
        return margin + padding;
    }

    void updateHandler() {
        float offset = ((float) getHeight() - getSumOfVerticalIndents() - handler.getHeight()) / (_maxValue - _minValue)
                * (_currentValue - _minValue);
        handler.setOffset((int) offset + getPadding().top + handler.getMargin().top);
    }

    /**
     * Getting the current slider value.
     * 
     * @return Slider value.
     */
    public float getCurrentValue() {
        return _currentValue;
    }

    private float _minValue = 0;

    /**
     * Setting the minimum slider value limit. Slider value cannot be less than this
     * limit.
     * 
     * @param value Minimum slider value limit.
     */
    public void setMinValue(float value) {
        _minValue = value;
    }

    /**
     * Getting the current minimum slider value limit.
     * 
     * @return Minimum slider value limit.
     */
    public float getMinValue() {
        return _minValue;
    }

    private float _maxValue = 100;

    /**
     * Setting the maximum slider value limit. Slider value cannot be greater than
     * this limit.
     * 
     * @param value Maximum slider value limit.
     */
    public void setMaxValue(float value) {
        _maxValue = value;
    }

    /**
     * Getting the current maximum slider value limit.
     * 
     * @return Maximum slider value limit.
     */
    public float getMaxValue() {
        return _maxValue;
    }

    /**
     * Default VerticalSlider constructor.
     */
    public VerticalSlider() {
        setItemName("VerticalSlider_" + count);
        eventMouseClick.add(this::onTrackClick);
        count++;

        // Handler
        handler.orientation = Orientation.VERTICAL;
        setStyle(DefaultsService.getDefaultStyle(VerticalSlider.class));
    }

    /**
     * Initializing all elements in the VerticalSlider.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
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

    /**
     * Slider drop event
     */
    protected void onDragHandler(InterfaceItem sender, MouseArgs args)// что-то с тобой не так
    {
        _dragging = true;
        // иногда число NAN
        float result = (float) (handler.getY() - getY()) * (_maxValue - _minValue)
                / ((float) getHeight() - getSumOfVerticalIndents() - handler.getHeight()) + _minValue;
        if (!Float.isNaN(result))
            setCurrentValue(result);
    }

    /**
     * Click on the sliders track (outside the slider handler)
     */
    protected void onTrackClick(InterfaceItem sender, MouseArgs args) {
        // Compute CurrentValue
        if (!_dragging)
            setCurrentValue((float) (args.position.getY() - getY() - handler.getHeight() / 2) * (_maxValue - _minValue)
                    / ((float) getHeight() - getSumOfVerticalIndents() - handler.getHeight()));
        _dragging = false;
    }

    /**
     * Setting Y coordinate of the left-top corner of the VerticalSlider.
     * 
     * @param y Y position of the left-top corner.
     */
    @Override
    public void setY(int y) {
        super.setY(y);
        updateHandler();
    }

    /**
     * Setting style of the VerticalSlider.
     * <p>
     * Inner styles: "track", "handler".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
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