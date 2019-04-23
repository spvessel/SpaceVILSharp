package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.CommonService;
import com.spvessel.spacevil.Common.DisplayService;
import com.spvessel.spacevil.Core.Pointer;
import com.spvessel.spacevil.Exceptions.SpaceVILException;
import com.spvessel.spacevil.Flags.EmbeddedCursor;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.IntBuffer;

final class GLWHandler {

    // private float _scaleWidth = 1.0f;
    // private float _scaleHeight = 1.0f;

    // float[] getDpiScale() {
    // return new float[] { _scaleWidth, _scaleHeight };
    // }

    private void setDpiScale(float w, float h) {
        // _scaleWidth = w * 2;
        // _scaleHeight = h * 2;
        // System.out.println(w + " " + h);
        DisplayService.SetDisplayDpiScale(w);
        _coreWindow.setDpiScale(w, h);
    }

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
    private GLFWFramebufferSizeCallback framebufferCallback;
    private GLFWWindowRefreshCallback windowRefreshCallback;
    private GLFWDropCallback dropCallback;
    ///////////////////////////////////////////////

    Boolean borderHidden;
    Boolean appearInCenter;
    Boolean focusable;
    Boolean focused = true;
    Boolean resizeble;
    Boolean visible;
    Boolean alwaysOnTop;
    Boolean maximized;
    Boolean transparent;
    private Pointer wPosition = new Pointer();

    Pointer getPointer() {
        return wPosition;
    }
    ///////////////////////////////////////////////

    private CoreWindow _coreWindow;

    CoreWindow getCoreWindow() {
        return _coreWindow;
    }

    private long _window = NULL;

    long getWindowId() {
        return _window;
    }

    int gVAO = 0;

    GLWHandler(CoreWindow handler) {
        _coreWindow = handler;
        getPointer().setX(0);
        getPointer().setY(0);
    }

    void createWindow() throws SpaceVILException {
        // important!!! may be the best combination of WINDOW HINTS!!!

        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_SAMPLES, _coreWindow._msaa.getValue());
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        // glfwWindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW_FALSE);

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

        if (transparent)
            glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_TRUE);
        else
            glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_FALSE);

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        _window = glfwCreateWindow(_coreWindow.getWidth(), _coreWindow.getHeight(), _coreWindow.getWindowTitle(), NULL,
                NULL);

        if (_window == NULL) {
            System.out.println("glfwCreateWindow fail");
            throw new SpaceVILException("Create window fails - " + getCoreWindow().getWindowTitle());
        }
        glfwMakeContextCurrent(_window);

        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode vidmode = glfwGetVideoMode(monitor);
        int width = vidmode.width();
        int height = vidmode.height();
        // System.out.println("VIDSIZE: " + width + " " + height);
        // System.out.println("WSIZE: " + _coreWindow.getWidth() + " " +
        // _coreWindow.getHeight());

        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        glfwGetFramebufferSize(_window, w, h);
        // System.out.println("FBSIZE: " + w.get(0) + " " + h.get(0));

        setDpiScale((float) w.get(0) / (float) _coreWindow.getWidth(),
                (float) h.get(0) / (float) _coreWindow.getHeight());

        if (appearInCenter) {
            getPointer().setX((width - _coreWindow.getWidth()) / 2);
            getPointer().setY((height - _coreWindow.getHeight()) / 2);

        } else {
            _coreWindow.setX(200);
            _coreWindow.setY(50);
            getPointer().setX(200);
            getPointer().setY(50);
        }
        glfwSetWindowSizeLimits(_window, _coreWindow.getMinWidth(), _coreWindow.getMinHeight(),
                _coreWindow.getMaxWidth(), _coreWindow.getMaxHeight());
        glfwSetWindowPos(_window, getPointer().getX(), getPointer().getY());

        if (_coreWindow.isKeepAspectRatio)
            glfwSetWindowAspectRatio(_window, _coreWindow.ratioW, _coreWindow.ratioH);

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
        framebufferCallback = null;
        windowRefreshCallback = null;
        dropCallback = null;
    }

    void setCursorType(int type) {
        switch (type) {
        case EmbeddedCursor.ARROW:
            glfwSetCursor(_window, CommonService.cursorArrow);
            break;
        case EmbeddedCursor.IBEAM:
            glfwSetCursor(_window, CommonService.cursorInput);
            break;
        case EmbeddedCursor.CROSSHAIR:
            glfwSetCursor(_window, CommonService.cursorResizeAll);
            break;
        case EmbeddedCursor.HAND:
            glfwSetCursor(_window, CommonService.cursorHand);
            break;
        case EmbeddedCursor.RESIZE_X:
            glfwSetCursor(_window, CommonService.cursorResizeH);
            break;
        case EmbeddedCursor.RESIZE_Y:
            glfwSetCursor(_window, CommonService.cursorResizeV);
            break;
        default:
            glfwSetCursor(_window, CommonService.cursorArrow);
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

    void setCallbackFramebuffer(GLFWFramebufferSizeCallback function) {
        framebufferCallback = function;
        glfwSetFramebufferSizeCallback(_window, framebufferCallback);
    }

    void setCallbackRefresh(GLFWWindowRefreshCallback function) {
        windowRefreshCallback = function;
        glfwSetWindowRefreshCallback(_window, windowRefreshCallback);
    }

    void setCallbackDrop(GLFWDropCallback function) {
        dropCallback = function;
        glfwSetDropCallback(_window, dropCallback);
    }

    void setHidden(Boolean value) {
        if (value)
            glfwHideWindow(_window);
        else
            glfwShowWindow(_window);
    }
}