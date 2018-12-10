package com.spvessel.spacevil;

// import java.util.*;
// import java.io.FileReader;
// import java.io.BufferedReader;
// import java.util.ArrayList;
// import java.util.List;

// import Common.*;
// import Cores.*;
//import org.lwjgl.glfw.;
import com.spvessel.spacevil.Core.Pointer;
import com.spvessel.spacevil.Exceptions.SpaceVILException;
import org.lwjgl.glfw.*;
// import org.lwjgl.opengl.GL;
// import org.lwjgl.system.*;

import static org.lwjgl.glfw.GLFW.*;
// import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.system.MemoryUtil.*;
// import java.nio.ByteBuffer;


final class GLWHandler {
    // cursors

    private long _arrow;
    private long _input;
    private long _hand;
    private long _resize_h;
    private long _resize_v;
    private long _resize_all;
    ///////////////////////////////////////////////

    private GLFWWindowSizeCallback resizeCallback;
    private GLFWCursorPosCallback mouseMoveCallback;
    private GLFWMouseButtonCallback mouseClickCallback;
    private GLFWScrollCallback mouseScrollCallback;
    private GLFWWindowCloseCallback windowCloseCallback;
    private GLFWWindowPosCallback windowPosCallback;
    private GLFWKeyCallback keyPressCallback;
    private GLFWCharModsCallback keyInputTextCallback;
    private GLFWWindowFocusCallback windowFocusCallback;
    ///////////////////////////////////////////////

    Boolean borderHidden;
    Boolean appearInCenter;
    Boolean focusable;
    Boolean focused = true;
    Boolean resizeble;
    Boolean visible;
    Boolean alwaysOnTop;
    Boolean maximized;
    private Pointer wPosition = new Pointer();

    Pointer getPointer() {
        return wPosition;
    }
    ///////////////////////////////////////////////

    private WindowLayout _w_layout;

    protected WindowLayout getLayout() {
        return _w_layout;
    }

    private long _window = NULL;

    long getWindowId() {
        return _window;
    }

    int gVAO = 0;

    GLWHandler(WindowLayout handler) {
        _w_layout = handler;
        getPointer().setX(0);
        getPointer().setY(0);
    }

    void initGlfw() throws SpaceVILException {
        // path to c++ glfw3.dll (x32 or x64)
        // glfwConfigureNativesDirectory(AppDomain.CurrentDomain.BaseDirectory);
        if (!glfwInit()) {
            // System.err.println("GLFW initialization failed!");
            // System.exit(-1);
            throw new SpaceVILException("Init GLFW fail - " + getLayout().getWindowTitle());
        }

        // cursors
        _arrow = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        _input = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
        _hand = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
        _resize_h = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
        _resize_v = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
        _resize_all = glfwCreateStandardCursor(GLFW_CROSSHAIR_CURSOR);
    }

    void createWindow() throws SpaceVILException {
        // important!!! may be the best combination of WINDOW HINTS!!!
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE);
        glfwWindowHint(GLFW_SAMPLES, 8);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

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

        _window = glfwCreateWindow(_w_layout.getWidth(), _w_layout.getHeight(), _w_layout.getWindowTitle(), NULL, NULL);

        if (_window == NULL) {
            System.out.println("glfwCreateWindow fail");
            throw new SpaceVILException("Create window fails - " + getLayout().getWindowTitle());
        }
        glfwMakeContextCurrent(_window);

        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode vidmode = glfwGetVideoMode(monitor);
        int width = vidmode.width();
        int height = vidmode.height();

        if (appearInCenter) {
            getPointer().setX((width - _w_layout.getWidth()) / 2);
            getPointer().setY((height - _w_layout.getHeight()) / 2);
        } else {
            getPointer().setX(_w_layout.getX());
            getPointer().setY(_w_layout.getY());
        }
        glfwSetWindowSizeLimits(_window, _w_layout.getMinWidth(), _w_layout.getMinHeight(), _w_layout.getMaxWidth(),
                _w_layout.getMaxHeight());
        glfwSetWindowPos(_window, getPointer().getX(), getPointer().getY());
        if (visible)
            glfwShowWindow(_window);
    }

    void switchContext() {
        glfwMakeContextCurrent(0);
        glfwMakeContextCurrent(_window);
    }

    void clearEventsCallbacks() {
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

    void setCursorType(int type) {
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

    Boolean isClosing() {
        return glfwWindowShouldClose(_window);
    }

    void destroy() {
        glfwDestroyWindow(_window);
    }

    void swap() {
        glfwSwapBuffers(_window);
    }

    void setToClose() {
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

    void setCallbackMouseMove(GLFWCursorPosCallback function) {
        mouseMoveCallback = function;
        glfwSetCursorPosCallback(_window, mouseMoveCallback);
    }

    void setCallbackMouseClick(GLFWMouseButtonCallback function) {
        mouseClickCallback = function;
        glfwSetMouseButtonCallback(_window, mouseClickCallback);
    }

    void setCallbackMouseScroll(GLFWScrollCallback function) {
        mouseScrollCallback = function;
        glfwSetScrollCallback(_window, mouseScrollCallback);
    }

    void setCallbackKeyPress(GLFWKeyCallback function) {
        keyPressCallback = function;
        glfwSetKeyCallback(_window, keyPressCallback);
    }

    void setCallbackTextInput(GLFWCharModsCallback function) {
        keyInputTextCallback = function;
        glfwSetCharModsCallback(_window, keyInputTextCallback);
    }

    void setCallbackClose(GLFWWindowCloseCallback function) {
        windowCloseCallback = function;
        glfwSetWindowCloseCallback(_window, windowCloseCallback);
    }

    void setCallbackPosition(GLFWWindowPosCallback function) {
        windowPosCallback = function;
        glfwSetWindowPosCallback(_window, windowPosCallback);
    }

    void setCallbackFocus(GLFWWindowFocusCallback function) {
        windowFocusCallback = function;
        glfwSetWindowFocusCallback(_window, windowFocusCallback);
    }

    void setCallbackResize(GLFWWindowSizeCallback function) {
        resizeCallback = function;
        glfwSetWindowSizeCallback(_window, resizeCallback);
    }

    void setHidden(Boolean value) {
        if (value)
            glfwHideWindow(_window);
        else
            glfwShowWindow(_window);
    }
}