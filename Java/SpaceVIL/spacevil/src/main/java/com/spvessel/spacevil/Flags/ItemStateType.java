package com.spvessel.spacevil.Flags;

/**
 * Item state types enum.
 * <p> Values: Base, Hovered, Pressed, Toggled, Focused, Disabled.
 */
public enum ItemStateType {
    /**
     * Base static item's condition. 
     * <p> Where Item is class extended from com.spvessel.spacevil.Prototype.
     */
    BASE,

    /**
     * Item's condition when mouse cursor inside items area. 
     * <p> Where Item is class extended from com.spvessel.spacevil.Prototype.
     */
    HOVERED,

    /**
     * Item's condition when mouse cursor inside items area and any mouse button is pressed.
     * <p> Where Item is class extended from com.spvessel.spacevil.Prototype.
     */
    PRESSED,

    /**
     * Item's condition when it is toggled.
     * <p> Where Item is class extended from com.spvessel.spacevil.Prototype.
     */
    TOGGLED,

    /**
     * Item's condition when it is focused.
     * <p> Where Item is class extended from com.spvessel.spacevil.Prototype.
     */
    FOCUSED,

    /**
     * Item's condition when it is disabled.
     * <p> Where Item is class extended from com.spvessel.spacevil.Prototype.
     */
    DISABLED
}