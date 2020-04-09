package com.spvessel.spacevil.View;

import java.awt.Color;
import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.*;

// InterfaceDraggable to recieve mouse drag events
// InterfaceHLayout to disable auto position by X axis.
public class MyWidget extends Prototype implements InterfaceDraggable, InterfaceHLayout {
    private Rectangle _handler = null;

    public MyWidget() {
        // appearance of MyWidget
        setStyle(Style.getDefaultCommonStyle());
        setWidthPolicy(SizePolicy.EXPAND);
        setSize(100, 30);
        setBackground(121, 223, 152);
        // appearance of handler
        _handler = new Rectangle();
        _handler.setStyle(Style.getDefaultCommonStyle());
        _handler.setHeightPolicy(SizePolicy.EXPAND);
        _handler.setBackground(Color.BLACK);
        _handler.setWidth(20);
    }

    @Override
    public void initElements() {
        // add handler
        addItem(_handler);
        //
        eventMouseClick.add(this::onRelease);
        eventMouseDrag.add(this::onDrag);
        eventMousePress.add(this::onPress);
        
        updateLayout();
    }

    // all overrides are necessary
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        updateLayout();
    }

    // InterfaceHLayout implementation
    @Override
    public void updateLayout() {

        _handler.setX(valueToXPos(_percent));
    }

    float _percent = 0; // percent

    private void onPress(InterfaceItem sender, MouseArgs args) {
        System.out.println("Press");
        _percent = 100.0f / (float) getWidth() * getActualXPos(args.position);
        updateLayout();
    }

    private void onDrag(InterfaceItem sender, MouseArgs args) {
        System.out.println("Drag");
        _percent = 100.0f / (float) getWidth() * getActualXPos(args.position);
        updateLayout();
    }

    private void onRelease(InterfaceItem sender, MouseArgs args) {
        System.out.println("Release");
        _percent = 100.0f / (float) getWidth() * getActualXPos(args.position);
        updateLayout();
    }

    // limit our handler by width and position of MyWidget
    private int getActualXPos(Position pos) {
        int actualX = pos.getX() - _handler.getWidth() / 2;
        if (actualX < getX())
            actualX = getX();
        if (actualX > getX() + getWidth() - _handler.getWidth())
            actualX = getX() + getWidth() - _handler.getWidth();
        return actualX;
    }

    private int valueToXPos(float value) {
        return (int) ((float) getWidth() / 100.f * value);
    }
}