package com.spvessel.spacevil.Flags;

/**
 * Size policy types enum.
 * <p> Values: FIXED, EXPAND.
 */
public enum SizePolicy {
    /**
     * Size is fixed and cannot be changed.
     */
    Fixed,//размер фиксированный, никаких изменений фигуры, только выравнивание

    /**
     * The form will be stretched inside the container to all available space.
     */
    Expand,//размер плавающий, размер фигуры подстраивается под все доступное пространство, выравнивание работает корректно
}