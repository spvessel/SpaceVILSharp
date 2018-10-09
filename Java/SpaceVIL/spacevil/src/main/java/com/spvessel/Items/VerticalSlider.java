package com.spvessel.Items;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Cores.EventCommonMethodState;
import com.spvessel.Cores.InterfaceItem;
import com.spvessel.Cores.MouseArgs;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.Orientation;

public class VerticalSlider extends VisualItem {
    static int count = 0;

    public Rectangle track = new Rectangle();
    public ScrollHandler handler = new ScrollHandler();

    // Values
    private float _step = 1.0f;

    public void setStep(float value) {
        _step = value;
    }

    public float getStep() {
        return _step;
    }

    public EventCommonMethodState eventValueChanged = new EventCommonMethodState();
    private float _current_value = 0;
    public int Direction = 0;

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

    public void updateHandler() {
        float offset = ((float) getHeight() - handler.getHeight()) / (_max_value - _min_value) * _current_value;
        handler.setOffset((int) offset);
    }

    public float getCurrentValue() {
        return _current_value;
    }

    private float _min_value = 0;

    public void setMinValue(float value) {
        _min_value = value;
    }

    public float getMinValue() {
        return _min_value;
    }

    private float _max_value = 100;

    public void setMaxValue(float value) {
        _max_value = value;
    }

    public float getMaxValue() {
        return _max_value;
    }

    public VerticalSlider() {
        setItemName("VerticalSlider_" + count);
        //InterfaceMouseMethodState t_click = (sender, args) -> onTrackClick(sender, args);
        eventMouseClick.add(this::onTrackClick); //t_click);
        count++;

        // Handler
        handler.direction = Orientation.VERTICAL;
        setStyle(DefaultsService.getDefaultStyle("SpaceVIL.VerticalSlider"));
    }

    @Override
    public void initElements() {
        // Adding
        addItems(track, handler);

        // Event connections
        //InterfaceMouseMethodState h_drop = (sender, args) -> onDropHandler(sender, args);
        eventMouseDrop.add(this::onDropHandler); //h_drop);
        //InterfaceMouseMethodState m_dragg = (sender, args) -> eventMouseDrop.execute(sender, args);
        handler.eventMouseDrag.add(eventMouseDrop::execute); //m_dragg);
    }

    public void onDropHandler(InterfaceItem sender, MouseArgs args)// что-то с тобой не так
    {
        // иногда число NAN
        float result = (float) (handler.getY() - getY()) * (_max_value - _min_value)
                / ((float) getHeight() - handler.getHeight());
        if (!Float.isNaN(result))
            setCurrentValue(result);
    }

    public void onTrackClick(InterfaceItem sender, MouseArgs args) {
        // Compute CurrentValue
        setCurrentValue((float) (_mouse_ptr.Y - getY() - handler.getHeight() / 2) * (_max_value - _min_value)
                / ((float) getHeight() - handler.getHeight()));
    }

    @Override
    public void setY(int _y) {
        super.setY(_y);
        updateHandler();
    }

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