package com.spvessel.spacevil.Flags;

/**
 * Visibility types of item enum. Used in such items as scroll bars.
 * <p> Values: ALWAYS, AS_NEEDED, NEVER.
 */
public enum VisibilityPolicy {
    /**
     * Item is always visible.
     */
    ALWAYS,
    
    /**
     * Item can be visible in some circumstances.
     */
    AS_NEEDED,
    
    /**
     * Item is always invisible.
     */
    NEVER
}