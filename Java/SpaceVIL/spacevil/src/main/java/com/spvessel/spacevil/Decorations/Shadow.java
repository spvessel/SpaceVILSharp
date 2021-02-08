package com.spvessel.spacevil.Decorations;

import java.awt.Color;

import com.spvessel.spacevil.GraphicsMathService;
import com.spvessel.spacevil.Core.IShadow;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Core.Size;

/**
 * Shadow is visual effect for applying to item's shape. Implements
 * com.spvessel.spacevil.Core.IShadow and com.spvessel.spacevil.Core.IEffect.
 * <p>
 * This visual effect drops shadow under item's shape.
 */
public final class Shadow implements IShadow {

    /**
     * Getting the effect name.
     * 
     * @return Returns name Shadow effect as System.String.
     */
    public String getEffectName() {
        return this.getClass().getName();
    }

    private int _radius = 5;
    private int _maxAvailableRadius = 10;

    /**
     * Setting the specified blur radius of the shadow.
     * <p>
     * Default: 0.
     * 
     * @param value Blur radius of the shadow. Min value: 0. Max value: 10.
     */
    public void setRadius(int value) {
        if (value < 0 || value > _maxAvailableRadius) {
            return;
        }
        _radius = value;
    }

    /**
     * Getting the shadow blur raduis.
     * 
     * @return The blur radius of the shadow. Min value: 0. Max value: 10.
     */
    public int getRadius() {
        return _radius;
    }

    private Position _offset = new Position();

    /**
     * Setting X shift of the shadow.
     * 
     * @param value Shift by X-axis.
     */
    public void setXOffset(int value) {
        _offset.setX(value);
    }

    /**
     * Getting X shift of the shadow.
     * 
     * @return Shift by X-axis.
     */
    public int getXOffset() {
        return _offset.getX();
    }

    /**
     * Setting Y shift of the shadow.
     * 
     * @param value Shift by Y-axis.
     */
    public void setYOffset(int value) {
        _offset.setY(value);
    }

    /**
     * Getting the offset of the shadow relative to the position of the item.
     * 
     * @return Shadow offset as com.spvessel.spacevil.Core.Position.
     */
    public Position getOffset() {
        return _offset;
    }

    private Color _color = Color.BLACK;

    /**
     * Setting shadow color.
     * 
     * @param color Shadow color as java.awt.Color.
     */
    public void setColor(Color color) {
        _color = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    /**
     * Setting shadow color in RGB format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setColor(int r, int g, int b) {
        _color = GraphicsMathService.colorTransform(r, g, b);
    }

    /**
     * Setting shadow color in byte RGBA format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     * @param a Alpha color component. Range: (0 - 255)
     */
    public void setColor(int r, int g, int b, int a) {
        _color = GraphicsMathService.colorTransform(r, g, b, a);
    }

    /**
     * Setting shadow color in float RGB format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setColor(float r, float g, float b) {
        _color = GraphicsMathService.colorTransform(r, g, b);
    }

    /**
     * Setting shadow color in float RGBA format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     * @param a Alpha color component. Range: (0.0f - 1.0f)
     */
    public void setColor(float r, float g, float b, float a) {
        _color = GraphicsMathService.colorTransform(r, g, b, a);
    }

    /**
     * Getting shadow color.
     * 
     * @return Returns the shadow color as java.awt.Color.
     */
    public Color getColor() {
        return new Color(_color.getRed(), _color.getGreen(), _color.getBlue(), _color.getAlpha());
    }

    private boolean _isApplied;

    /**
     * Setting drop shadow status.
     * 
     * @param value True: allow shadow dropping. False: not allow shadow dropping.
     */
    public void setApplied(boolean value) {
        _isApplied = value;
    }

    /**
     * Getting shadow drop status.
     * 
     * @return True: allow shadow dropping. False: not allow shadow dropping.
     */
    public boolean isApplied() {
        return _isApplied;
    }

    private Size _extension = new Size();

    /**
     * Getting the values of shadow extensions in pixels.
     * 
     * @return The values of shadow extensions as com.spvessel.spacevil.Core.Size
     */
    public Size getExtension() {
        return _extension;
    }

    /**
     * Default Shadow class constructor. Allow shadow dropping.
     */
    public Shadow() {
        _isApplied = true;
    }

    /**
     * Shadow class constructor with specified blur radius. Allow shadow dropping.
     * 
     * @param radius A blur radius of the shadow.
     */
    public Shadow(int radius) {
        this();
        _radius = radius;
    }

    /**
     * Shadow class constructor with specified blur radius, shadow color. Allow
     * shadow dropping.
     * 
     * @param radius A blur radius of the shadow.
     * @param color  A shadow color as java.awt.Color.
     */
    public Shadow(int radius, Color color) {
        this();
        _radius = radius;
        _color = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    /**
     * Shadow class constructor with specified blur radius, axis shifts, shadow
     * color. Allow shadow dropping.
     * 
     * @param radius A blur radius of the shadow.
     * @param offset Shift of the shadow.
     * @param color  A shadow color as java.awt.Color.
     */
    public Shadow(int radius, Position offset, Color color) {
        this();
        _radius = radius;
        _offset.setPosition(offset.getX(), offset.getY());
        _color = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    /**
     * Shadow class constructor with specified blur radius, size extensions, shadow
     * color. Allow shadow dropping.
     * 
     * @param radius    A blur radius of the shadow.
     * @param extension Size extension of the shadow.
     * @param color     A shadow color as java.awt.Color.
     */
    public Shadow(int radius, Size extension, Color color) {
        this();
        _radius = radius;
        _extension.setSize(extension.getWidth(), extension.getHeight());
        _color = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    /**
     * Shadow class constructor with specified blur radius, axis shifts, size
     * extensions and shadow color. Allow shadow dropping.
     * 
     * @param radius    A blur radius of the shadow.
     * @param offset    Shift of the shadow.
     * @param extension Size extension of the shadow.
     * @param color     A shadow color as java.awt.Color.
     */
    public Shadow(int radius, Position offset, Size extension, Color color) {
        this();
        _radius = radius;
        _offset.setPosition(offset.getX(), offset.getY());
        _extension.setSize(extension.getWidth(), extension.getHeight());
        _color = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    /**
     * Clones current Shadow class instance.
     * 
     * @return Copy of current Shadow.
     */
    public Shadow clone() {
        Shadow clone = new Shadow(
            getRadius(),
            new Position(getOffset().getX(), getOffset().getY()),
            new Size(getExtension().getWidth(), getExtension().getHeight()),
            new Color(_color.getRed(), _color.getGreen(), _color.getBlue(), _color.getAlpha()));

        clone.setApplied(isApplied());

        return clone;
    }
}