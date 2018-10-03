package com.spvessel.Common;

import com.spvessel.Decorations.ThemeStyle;
import java.awt.Font;

public final class DefaultsService {

    private DefaultsService() {
    }

    public static void setDefaultTheme(ThemeStyle theme)
    {

    }

    public static Font getDefaultFont()
    {
        return new Font("Verdana", Font.PLAIN, 12);
    }
    public static Font getDefaultFont(int size)
    {
        return new Font("Ubuntu", Font.PLAIN, size);
    }
    public static Font getDefaultFont(int style, int size)
    {
        return new Font("Ubuntu", style, size);
    }
}