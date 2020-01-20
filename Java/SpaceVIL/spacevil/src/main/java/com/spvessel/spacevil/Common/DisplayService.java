package com.spvessel.spacevil.Common;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.spvessel.spacevil.CoreWindow;
import com.spvessel.spacevil.Core.Scale;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

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
    private static boolean _dpiSet = false;
    private static float _dpi = 1.0f;

    public static Scale getDisplayDpiScale() {
        long monitor = glfwGetPrimaryMonitor();
        FloatBuffer x = BufferUtils.createFloatBuffer(1);
        FloatBuffer y = BufferUtils.createFloatBuffer(1);
        glfwGetMonitorContentScale(monitor, x, y);
        return new Scale(x.get(0), y.get(0));
    }

    public static Scale getWindowDpiScale(CoreWindow window) {
        return window.getDpiScale();
    }
}