package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.CommonService;
import com.spvessel.spacevil.Core.Pointer;
import com.spvessel.spacevil.Exceptions.SpaceVILException;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.IntBuffer;

final class GLWHandler {

    // private float _scaleWidth = 1.0f;
    // private float _scaleHeight = 1.0f;

    // float[] getDpiScale() {
    //     return new float[] { _scaleWidth, _scaleHeight };
    // }

    private void setDpiScale(float w, float h) {
        // _scaleWidth = w * 2;
        // _scaleHeight = h * 2;
        // System.out.println(w + " " + h);
        _w_layout.setDpiScale(w, h);
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

    void createWindow() throws SpaceVILException {
        // important!!! may be the best combination of WINDOW HINTS!!!

        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_SAMPLES, _w_layout._msaa.getValue());
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
        // System.out.println("VIDSIZE: " + width + " " + height);
        // System.out.println("WSIZE: " + _w_layout.getWidth() + " " + _w_layout.getHeight());

        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        glfwGetFramebufferSize(_window, w, h);
        // System.out.println("FBSIZE: " + w.get(0) + " " + h.get(0));

        setDpiScale((float) w.get(0) / (float) _w_layout.getWidth(), (float) h.get(0) / (float) _w_layout.getHeight());

        if (appearInCenter) {
            getPointer().setX((width - _w_layout.getWidth()) / 2);
            getPointer().setY((height - _w_layout.getHeight()) / 2);

        } else {
            _w_layout.setX(200);
            _w_layout.setY(200);
            getPointer().setX(200);
            getPointer().setY(200);
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
        framebufferCallback = null;
        windowRefreshCallback = null;
    }

    void setCursorType(int type) {
        switch (type) {
        case GLFW_ARROW_CURSOR:
            glfwSetCursor(_window, CommonService.cursorArrow);
            break;
        case GLFW_IBEAM_CURSOR:
            glfwSetCursor(_window, CommonService.cursorInput);
            break;
        case GLFW_CROSSHAIR_CURSOR:
            glfwSetCursor(_window, CommonService.cursorResizeAll);
            break;
        case GLFW_HAND_CURSOR:
            glfwSetCursor(_window, CommonService.cursorHand);
            break;
        case GLFW_HRESIZE_CURSOR:
            glfwSetCursor(_window, CommonService.cursorResizeH);
            break;
        case GLFW_VRESIZE_CURSOR:
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

    void setHidden(Boolean value) {
        if (value)
            glfwHideWindow(_window);
        else
            glfwShowWindow(_window);
    }
}