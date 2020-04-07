package com.spvessel.spacevil.Flags;

/**
 * Enum of event types directly related to events that change the size and position of an item.
 * <p> Where Item is class extended from com.spvessel.spacevil.Prototype.
 * <p> Values: Focused, MovedX, MovedY, ResizeWidth, ResizeHeight.
 */
public enum GeometryEventType {
    FOCUSED, MOVED_X, MOVED_Y, RESIZE_WIDTH, RESIZE_HEIGHT
}