package com.spvessel.spacevil;

import java.awt.*;

/**
 * FontService is a static class with static methods for working with fonts.
 */
public final class FontService {
    /**
     * Changing font size.
     * @param size New size of the font.
     * @param oldFont Font as java.awt.Font.
     * @return New sized font as java.awt.Font.
     */
    public static Font changeFontSize(int size, Font oldFont) {
        return oldFont.deriveFont(oldFont.getStyle(), size); // oldFont.getName(), oldFont.getStyle(), size);
    }

    /**
     * Changing font style.
     * @param style New style of the font.
     * @param oldFont Font as java.awt.Font.
     * @return New styled font as java.awt.Font.
     */
    public static Font changeFontStyle(int style, Font oldFont) {
        return oldFont.deriveFont(style);
    }

    /**
     * Changing font family.
     * @param fontFamily New font family of the font.
     * @param oldFont Font as java.awt.Font.
     * @return New font as java.awt.Font.
     */
    public static Font changeFontFamily(String fontFamily, Font oldFont) {
        return new Font(fontFamily, oldFont.getStyle(), oldFont.getSize());
    }
}