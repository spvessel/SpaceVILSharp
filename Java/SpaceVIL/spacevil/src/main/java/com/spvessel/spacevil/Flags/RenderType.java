package com.spvessel.spacevil.Flags;

/**
 * Enum of types render.
 * <p> Values: IF_NEEDED, PERIODIC, ALWAYS.
 */
public enum RenderType {
    /**
     * The scene is redrawn only if any input event occurs (mouse move, mouse click, 
     * keyboard key press, window resizing and etc.).
     */
    IF_NEEDED,
    
    /**
     * The scene is redrawn according to the current render frequency type 
     * (See setRenderFrequency(type)) in idle and every time when any input event occurs.
     */
    PERIODIC,
    
    /**
     * The scene is constantly being redrawn.
     */
    ALWAYS
}