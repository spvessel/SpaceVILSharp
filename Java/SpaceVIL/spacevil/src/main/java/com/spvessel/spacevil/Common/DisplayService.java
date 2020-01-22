package com.spvessel.spacevil.Common;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.spvessel.spacevil.CoreWindow;
import com.spvessel.spacevil.Core.Scale;

import org.lwjgl.BufferUtils;
import static org.lwjgl.glfw.GLFW.*;

public final class DisplayService {

    private DisplayService() {
    }

    // sizes
    private static int _displayWidth = 0;
    private static int _displayHeight = 0;

    public static int getDisplayWidth() {
        return _displayWidth;
    }

    public static int getDisplayHeight() {
        return _displayHeight;
    }

    static void setDisplaySize(int w, int h) {
        _displayWidth = w;
        _displayHeight = h;
    }

    // dpi
    static Scale _displayScale = new Scale();
    public static Scale getDisplayDpiScale() {
        // long monitor = glfwGetPrimaryMonitor();
        // FloatBuffer x = BufferUtils.createFloatBuffer(1);
        // FloatBuffer y = BufferUtils.createFloatBuffer(1);
        // glfwGetMonitorContentScale(monitor, x, y);
        // return new Scale(x.get(0), y.get(0));
        return new Scale(_displayScale.getX(), _displayScale.getY());
    }
    public static void setDisplayScale(float x, float y)
    {
        _displayScale.setScale(x, y);
    }

    public static Scale getWindowDpiScale(CoreWindow window) {
        return window.getDpiScale();
    }
}