package com.spvessel.spacevil.Core;

/**
 * An interface that defines free type of a container.
 */
public interface IFreeLayout {
    /**
     * Method for describing how to update the size and position of children within a container.
     */
    public void updateLayout();
}