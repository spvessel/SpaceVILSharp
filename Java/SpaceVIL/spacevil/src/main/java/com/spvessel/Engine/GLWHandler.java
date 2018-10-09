package com.spvessel.Engine;

import java.util.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import com.spvessel.Common.*;
import com.spvessel.Cores.*;
import com.spvessel.Windows.*;
//import org.lwjgl.glfw.;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.system.MemoryUtil.*;
import java.nio.ByteBuffer;

public class GLWHandler {
    // cursors

    long _arrow;
    long _input;
    long _hand;
    long _resize_h;
    long _resize_v;
    long _resize_all;
    ///////////////////////////////////////////////

    GLFWWindowSizeCallback resizeCallback;
    GLFWCursorPosCallback mouseMoveCallback;
    GLFWMouseButtonCallback mouseClickCallback;
    GLFWScrollCallback mouseScrollCallback;
    GLFWWindowCloseCallback windowCloseCallback;
    GLFWWindowPosCallback windowPosCallback;
    GLFWKeyCallback keyPressCallback;
    GLFWCharModsCallback keyInputTextCallback;
    GLFWWindowFocusCallback windowFocusCallback;
    ///////////////////////////////////////////////

    public Boolean borderHidden;
    public Boolean appearInCenter;
    public Boolean focusable;
    public Boolean focused = true;
    public Boolean resizeble;
    public Boolean visible;
    public Boolean alwaysOnTop;
    public Boolean maximized;
    public com.spvessel.Cores.Pointer wPosition = new com.spvessel.Cores.Pointer();
    ///////////////////////////////////////////////

    private WindowLayout _w_layout;

    protected WindowLayout getLayout() {
        return _w_layout;
    }

    long _window;

    public long getWindowId() {
        return _window;
    }

    protected int gVAO = 0;

    protected GLWHandler(WindowLayout handler) {
        _w_layout = handler;
        wPosition.X = 0;
        wPosition.Y = 0;
    }

    protected void initGlfw() {
        // path to c++ glfw3.dll (x32 or x64)
        // glfwConfigureNativesDirectory(AppDomain.CurrentDomain.BaseDirectory);
        if (!glfwInit()) {
            System.out.println("Init GLFW fail - " + getLayout().getWindowTitle());
            System.err.println("GLFW initialization failed!");
        }

        // cursors
        _arrow = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        _input = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
        _hand = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
        _resize_h = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
        _resize_v = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
        _resize_all = glfwCreateStandardCursor(GLFW_CROSSHAIR_CURSOR);
    }

    protected void createWindow() {

        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_SAMPLES, 8);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);

        if (resizeble)
            glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        else
            glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        if (!borderHidden)
            glfwWindowHint(GLFW_DECORATED, GLFW_TRUE);
        else
            glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);

        if (focused)
            glfwWindowHint(GLFW_FOCUSED, GLFW_TRUE);
        else
            glfwWindowHint(GLFW_FOCUSED, GLFW_FALSE);

        if (alwaysOnTop)
            glfwWindowHint(GLFW_FLOATING, GLFW_TRUE);
        else
            glfwWindowHint(GLFW_FLOATING, GLFW_FALSE);

        if (maximized)
            glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        else
            glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        // if (visible)
        // glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        // else

        _window = glfwCreateWindow(_w_layout.getWidth(), _w_layout.getHeight(), _w_layout.getWindowTitle(), NULL, NULL);

        if (_window == NULL) {
            // LogService.Log().LogText("Create window fail - " +
            // getLayout().getWindowTitle());
            glfwTerminate();
        }
        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode vidmode = glfwGetVideoMode(monitor);
        int width = vidmode.width();
        int height = vidmode.height();

        if (appearInCenter) {
            wPosition.X = (width - _w_layout.getWidth()) / 2;
            wPosition.Y = (height - _w_layout.getHeight()) / 2;
        } else {
            wPosition.X = _w_layout.getX();
            wPosition.Y = _w_layout.getY();
        }
        glfwMakeContextCurrent(_window);
        GL.createCapabilities();
        glfwSetWindowSizeLimits(_window, _w_layout.getMinWidth(), _w_layout.getMinHeight(), _w_layout.getMaxWidth(),
                _w_layout.getMaxHeight());
        glfwSetWindowPos(_window, wPosition.X, wPosition.Y);
        // Console.WriteLine(
        // _w_layout.getMinWidth() + " " +
        // _w_layout.getMinHeight() + " " +
        // _w_layout.getMaxWidth() + " " +
        // _w_layout.getMaxHeight() + " "
        // );
        // LogService.Log().LogWindow(getLayout(), LogProps.AllGeometry);
        if (visible)
            glfwShowWindow(_window);
    }

    protected void switchContext() {
        glfwMakeContextCurrent(0);
        glfwMakeContextCurrent(_window);
    }
    protected void setContext() {
        glfwMakeContextCurrent(_window);
    }

    protected void clearEventsCallbacks() {
        mouseMoveCallback = null;
        mouseClickCallback = null;
        mouseScrollCallback = null;
        keyPressCallback = null;
        keyInputTextCallback = null;
        windowCloseCallback = null;
        windowPosCallback = null;
        windowFocusCallback = null;
        resizeCallback = null;
    }

    protected void setCursorType(int type) {
        switch (type) {
        case GLFW_ARROW_CURSOR:
            glfwSetCursor(_window, _arrow);
            break;
        case GLFW_IBEAM_CURSOR:
            glfwSetCursor(_window, _input);
            break;
        case GLFW_CROSSHAIR_CURSOR:
            glfwSetCursor(_window, _resize_all);
            break;
        case GLFW_HAND_CURSOR:
            glfwSetCursor(_window, _hand);
            break;
        case GLFW_HRESIZE_CURSOR:
            glfwSetCursor(_window, _resize_h);
            break;
        case GLFW_VRESIZE_CURSOR:
            glfwSetCursor(_window, _resize_v);
            break;
        default:
            glfwSetCursor(_window, _arrow);
            break;
        }
    }

    protected Boolean isClosing() {
        return glfwWindowShouldClose(_window);
    }

    protected void destroy() {
        glfwDestroyWindow(_window);
    }

    protected void swap() {
        glfwSwapBuffers(_window);
    }

    protected void setToClose() {
        if (_window != 0)
            glfwSetWindowShouldClose(_window, true);
        // try
        // {
        // glfwSetWindowShouldClose(_window, true);
        // }
        // catch(Exception ex)
        // {
        // System.out.println(ex.toString());
        // }
    }

    protected void setCallbackMouseMove(GLFWCursorPosCallback function) {
        mouseMoveCallback = function;
        glfwSetCursorPosCallback(_window, mouseMoveCallback);
    }

    protected void setCallbackMouseClick(GLFWMouseButtonCallback function) {
        mouseClickCallback = function;
        glfwSetMouseButtonCallback(_window, mouseClickCallback);
    }

    protected void setCallbackMouseScroll(GLFWScrollCallback function) {
        mouseScrollCallback = function;
        glfwSetScrollCallback(_window, mouseScrollCallback);
    }

    protected void setCallbackKeyPress(GLFWKeyCallback function) {
        keyPressCallback = function;
        glfwSetKeyCallback(_window, keyPressCallback);
    }

    protected void setCallbackTextInput(GLFWCharModsCallback function) {
        keyInputTextCallback = function;
        glfwSetCharModsCallback(_window, keyInputTextCallback);
    }

    protected void setCallbackClose(GLFWWindowCloseCallback function) {
        windowCloseCallback = function;
        glfwSetWindowCloseCallback(_window, windowCloseCallback);
    }

    protected void setCallbackPosition(GLFWWindowPosCallback function) {
        windowPosCallback = function;
        glfwSetWindowPosCallback(_window, windowPosCallback);
    }

    protected void setCallbackFocus(GLFWWindowFocusCallback function) {
        windowFocusCallback = function;
        glfwSetWindowFocusCallback(_window, windowFocusCallback);
    }

    protected void setCallbackResize(GLFWWindowSizeCallback function) {
        resizeCallback = function;
        glfwSetWindowSizeCallback(_window, resizeCallback);
    }

    public void setHidden(Boolean value) {
        if (value)
            glfwHideWindow(_window);
        else
            glfwShowWindow(_window);
    }
}