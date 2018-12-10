package com.spacevil.Common;

import com.spacevil.Decorations.Style;

import java.awt.Font;

public final class DefaultsService {

    private static com.spacevil.Decorations.ThemeStyle _default_theme; // = ThemeStyle.GetInstance();
    private static DefaultFont _default_font = DefaultFont.getInstance();;

    private DefaultsService() {
        // _default_font = DefaultFont.getInstance();
    }

    public static com.spacevil.Decorations.ThemeStyle getDefaultTheme() {
        if (_default_theme == null)
            _default_theme = new com.spacevil.Decorations.ThemeStyle();
        return _default_theme;
    }

    public static void setDefaultTheme(com.spacevil.Decorations.ThemeStyle theme) {
        _default_theme = theme;
    }

    // public static Style getDefaultStyle(String type) {
    public static Style getDefaultStyle(Class<?> type) {
        if (_default_theme == null)
            _default_theme = new com.spacevil.Decorations.ThemeStyle();
        return _default_theme.getThemeStyle(type);
    }

    public static void setDefaultFont(Font font) {
        _default_font.setDefaultFont(font);
    }
    public static Font getDefaultFont() {
        return _default_font.getDefaultFont();
    }

    public static Font getDefaultFont(int size) {
        return _default_font.getDefaultFont(size);
    }

    public static Font getDefaultFont(int style, int size) {
        return _default_font.getDefaultFont(style, size);
    }
}
