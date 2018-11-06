package com.spvessel;

public class HorizontalSlider extends VisualItem {
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
    public int direction = 0;

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

    void updateHandler() {
        float offset = ((float) getWidth() - handler.getWidth()) / (_max_value - _min_value) * _current_value;
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

    public HorizontalSlider() {
        setItemName("HorizontalSlider_" + count);
        InterfaceMouseMethodState t_click = (sender, args) -> onTrackClick(sender, args);
        eventMouseClick.add(t_click);
        count++;

        handler.direction = Orientation.HORIZONTAL;

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.HorizontalSlider"));
        setStyle(DefaultsService.getDefaultStyle(HorizontalSlider.class));
    }

    @Override
    public void initElements() {
        // Adding
        addItems(track, handler);

        // Event connections
        InterfaceMouseMethodState h_drop = (sender, args) -> onDrophandler(sender, args);
        eventMouseDrop.add(h_drop);
        InterfaceMouseMethodState m_dragg = (sender, args) -> eventMouseDrop.execute(sender, args);
        handler.eventMouseDrag.add(m_dragg);
    }

    public void onDrophandler(InterfaceItem sender, MouseArgs args)// что-то с тобой не так
    {
        // иногда число NAN
        float result = (float) (handler.getX() - getX()) * (_max_value - _min_value)
                / ((float) getWidth() - handler.getWidth());
        if (!Float.isNaN(result))
            setCurrentValue(result);
    }

    public void onTrackClick(InterfaceItem sender, MouseArgs args) {
        if (handler.isMouseHover())
            return;

        // Compute CurrentValue
        setCurrentValue((float) (args.position.getX() - getX() - handler.getWidth() / 2) * (_max_value - _min_value)
                / ((float) getWidth() - handler.getWidth()));
    }

    @Override
    public void setX(int _x) {
        super.setX(_x);
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