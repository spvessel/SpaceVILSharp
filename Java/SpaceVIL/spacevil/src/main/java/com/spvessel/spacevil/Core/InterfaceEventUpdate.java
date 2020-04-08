package com.spvessel.spacevil.Core;

import com.spvessel.spacevil.Flags.GeometryEventType;

/**
 * An interface that describes update events by type of event.
 * <p> This interface is part of com.spvessel.spacevil.Core.InterfaceBaseItem.
 */
public interface InterfaceEventUpdate {
    /**
     * Method for updating an item size or/and position.
     * @param type Type of event as com.spvessel.spacevil.Flags.GeometryEventType.
     * @param value Value of a property that was changed.
     */
    public void update(GeometryEventType type, int value);
}