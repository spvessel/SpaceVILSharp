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

    private float _current_value = 0;
    public int direction = 0;

    /**
     * Position value of the HorizontalSlider
     */
    public void setCurrentValue(float value) {
        if (_current_value > value)
            direction = -1; // up
        else
            direction = 1; // down

        _current_value = value;

        if (_current_value < _min_value)
            _current_value = _min_value;
        if (_current_value > _max_value)
            _current_value = _max_value;

        updateHandler(); // refactor!!

        if (eventValueChanged != null)
            eventValueChanged.execute(this);
    }

    public float getCurrentValue() {
        return _current_value;
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
                / (_max_value - _min_value) * _current_value;
        handler.setOffset((int) offset + getPadding().left + handler.getMargin().left);
    }

    private float _min_value = 0;

    /**
     * Minimum value of the HorizontalSlider
     */
    public void setMinValue(float value) {
        _min_value = value;
    }

    public float getMinValue() {
        return _min_value;
    }

    private float _max_value = 100;

    /**
     * Maximum value of the HorizontalSlider
     */
    public void setMaxValue(float value) {
        _max_value = value;
    }

    public float getMaxValue() {
        return _max_value;
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
        float result = (float) (handler.getX() - getX()) * (_max_value - _min_value)
                / ((float) getWidth() - getSumOfHorizontalIndents() - handler.getWidth());
        if (!Float.isNaN(result))
            setCurrentValue(result);
    }

    protected void onTrackClick(InterfaceItem sender, MouseArgs args) {
        // Compute CurrentValue
        if (!_dragging)
            setCurrentValue((float) (args.position.getX() - getX() - handler.getWidth() / 2) * (_max_value - _min_value)
                    / ((float) getWidth() - getSumOfHorizontalIndents() - handler.getWidth()));
        _dragging = false;
    }

    /**
     * Set X position of the HorizontalSlider
     */
    @Override
    public void setX(int _x) {
        super.setX(_x);
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