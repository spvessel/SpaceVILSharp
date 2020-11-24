package com.spvessel.spacevil.Common;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.spvessel.spacevil.CoreWindow;
import com.spvessel.spacevil.Core.Scale;

/**
 * DisplayService is static class providing methods to getting display attributes such as size and dpi scale.
 */
public final class DisplayService {

    private DisplayService() {
    }

    // sizes
    private static int _displayWidth = 0;
    private static int _displayHeight = 0;

    /**
     * Getting the current display width.
     * @return The current display width as int.
     */
    public static int getDisplayWidth() {
        return _displayWidth;
    }

    /**
     * Getting the current display height.
     * @return The current display height as int.
     */
    public static int getDisplayHeight() {
        return _displayHeight;
    }

    static void setDisplaySize(int w, int h) {
        _displayWidth = w;
        _displayHeight = h;
    }

    // dpi
    static Scale _displayScale = new Scale();

    /**
     * Getting the current display scale.
     * @return The current display scale as com.spvessel.spacevil.Core.Scale.
     */
    public static Scale getDisplayDpiScale() {
        
        // long monitor = glfwGetPrimaryMonitor();
        // FloatBuffer x = BufferUtils.createFloatBuffer(1);
        // FloatBuffer y = BufferUtils.createFloatBuffer(1);
        // glfwGetMonitorContentScale(monitor, x, y);
        // return new Scale(x.get(0), y.get(0));

        return new Scale(_displayScale.getXScale(), _displayScale.getYScale());
    }

    public static void setDisplayScale(float x, float y)
    {
        _displayScale.setScale(x, y);
    }

    /**
     * Getting the current window scale.
     * @param window A window as com.spvessel.spacevil.CoreWindow.
     * @return The current window scale as com.spvessel.spacevil.Core.Scale.
     */
    public static Scale getWindowDpiScale(CoreWindow window) {
        return window.getDpiScale();
    }
}