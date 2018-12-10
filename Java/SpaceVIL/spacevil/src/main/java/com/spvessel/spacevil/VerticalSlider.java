package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.EventCommonMethodState;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.Orientation;

public class VerticalSlider extends Prototype {
    private static int count = 0;

    /**
     * Path where slider moves. Can be styled
     */
    public Rectangle track = new Rectangle();
    /**
     * Slider itself
     */
    public ScrollHandler handler = new ScrollHandler();

    // Values
    private float _step = 1.0f;

    /**
     * VerticalSlider step
     */
    public void setStep(float value) {
        _step = value;
    }
    public float getStep() {
        return _step;
    }

    public EventCommonMethodState eventValueChanged = new EventCommonMethodState();
    private float _current_value = 0;
    /**
     * Slider direction(1 - down direction, -1 - up direction)
     */
    public int Direction = 0;

    /**
     * Sets current value of the VerticalSlider
     */
    public void setCurrentValue(float value) {
        if (_current_value > value)
            Direction = -1; // up
        else
            Direction = 1; // down

        _current_value = value;

        if (_current_value < _min_value)
            _current_value = _min_value;
        if (_current_value > _max_value)
            _current_value = _max_value;

        updateHandler(); // refactor!!

        if (eventValueChanged != null)
            eventValueChanged.execute(this);
    }

    void updateHandler() {
        float offset = ((float) getHeight() - handler.getHeight()) / (_max_value - _min_value) * _current_value;
        handler.setOffset((int) offset);
    }

    /**
     * @return current value of the VerticalSlider
     */
    public float getCurrentValue() {
        return _current_value;
    }

    private float _min_value = 0;

    /**
     * VerticalSlider's minimum value
     */
    public void setMinValue(float value) {
        _min_value = value;
    }
    public float getMinValue() {
        return _min_value;
    }

    private float _max_value = 100;

    /**
     * VerticalSlider's maximum value
     */
    public void setMaxValue(float value) {
        _max_value = value;
    }
    public float getMaxValue() {
        return _max_value;
    }

    /**
     * Constructs a VerticalSlider
     */
    public VerticalSlider() {
        setItemName("VerticalSlider_" + count);
        eventMouseClick.add(this::onTrackClick);
        count++;

        // Handler
        handler.direction = Orientation.VERTICAL;
        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.VerticalSlider"));
        setStyle(DefaultsService.getDefaultStyle(VerticalSlider.class));
    }

    /**
     * Initialization and adding of all elements in the VerticalSlider
     */
    @Override
    public void initElements() {
        // Adding
        addItems(track, handler);

        // Event connections
        eventMouseDrop.add(this::onDropHandler);
        handler.eventMouseDrag.add(eventMouseDrop::execute);
    }

    /**
     * Slider drop event
     */
    public void onDropHandler(InterfaceItem sender, MouseArgs args)// что-то с тобой не так
    {
        // иногда число NAN
        float result = (float) (handler.getY() - getY()) * (_max_value - _min_value)
                / ((float) getHeight() - handler.getHeight());
        if (!Float.isNaN(result))
            setCurrentValue(result);
    }

    /**
     * Click on the sliders track (outside the slider handler)
     */
    public void onTrackClick(InterfaceItem sender, MouseArgs args) {
        // Compute CurrentValue
        setCurrentValue((float) (args.position.getY() - getY() - handler.getHeight() / 2) * (_max_value - _min_value)
                / ((float) getHeight() - handler.getHeight()));
    }

    /**
     * Set Y position of the VerticalSlider
     */
    @Override
    public void setY(int _y) {
        super.setY(_y);
        updateHandler();
    }

    /**
     * Set style of the VerticalSlider
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