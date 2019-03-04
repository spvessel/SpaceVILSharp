package com.spvessel.spacevil.Common;

public final class DisplayService {

    private DisplayService() {
    }

    // sizes
    private static int _displayWidth = 0;
    private static int _displayHeight = 0;

    public static int GetDisplayWidth() {
        return _displayWidth;
    }

    public static int GetDisplayHeight() {
        return _displayHeight;
    }

    static void SetDisplaySize(int w, int h) {
        _displayWidth = w;
        _displayHeight = h;
    }

    // dpi
    private static boolean _dpiSet = false;
    private static float _dpi = 1.0f;

    public static float GetDisplayDpiScale() {
        return _dpi;
    }

    public static void SetDisplayDpiScale(float scale) {
        if (!_dpiSet)
            _dpi = scale;
        _dpiSet = true;
    }
}