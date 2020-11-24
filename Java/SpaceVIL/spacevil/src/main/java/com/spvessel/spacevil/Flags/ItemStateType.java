package com.spvessel.spacevil.Flags;

/**
 * Item state types enum.
 * <p> Values: BASE, HOVERED, PRESSED, TOGGLED, FOCUSED, DISABLED.
 */
public enum ItemStateType {
    /**
     * Base static item's condition. 
     * <p> Where Item is class extended from com.spvessel.spacevil.Prototype.
     */
    Base,

    /**
     * Item's condition when mouse cursor inside items area. 
     * <p> Where Item is class extended from com.spvessel.spacevil.Prototype.
     */
    Hovered,

    /**
     * Item's condition when mouse cursor inside items area and any mouse button is pressed.
     * <p> Where Item is class extended from com.spvessel.spacevil.Prototype.
     */
    Pressed,

    /**
     * Item's condition when it is toggled.
     * <p> Where Item is class extended from com.spvessel.spacevil.Prototype.
     */
    Toggled,

    /**
     * Item's condition when it is focused.
     * <p> Where Item is class extended from com.spvessel.spacevil.Prototype.
     */
    Focused,

    /**
     * Item's condition when it is disabled.
     * <p> Where Item is class extended from com.spvessel.spacevil.Prototype.
     */
    Disabled
}