package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.CommonService;
import com.spvessel.spacevil.Core.Area;
import com.spvessel.spacevil.Core.Pointer;
import com.spvessel.spacevil.Exceptions.SpaceVILException;
import com.spvessel.spacevil.Flags.EmbeddedCursor;
import com.spvessel.spacevil.Flags.OSType;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.FloatBuffer;

final class GLWHandler {

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
    private GLFWWindowContentScaleCallback contentScaleCallback;
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

        // scaling window's content (does not affect on Mac OS)
        glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE);

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
            System.out.println("glfwCreateWindow fails");
            throw new SpaceVILException("Create window fails - " + getCoreWindow().getWindowTitle());
        }

        WindowManager.setContextCurrent(_coreWindow);

        FloatBuffer xScale = BufferUtils.createFloatBuffer(1);
        FloatBuffer yScale = BufferUtils.createFloatBuffer(1);
        glfwGetWindowContentScale(_window, xScale, yScale);
        _coreWindow.setWindowScale(xScale.get(0), yScale.get(0));

        // System.out.println(_coreWindow.getDpiScale().toString());

        int actualWndWidth = _coreWindow.getWidth();
        int actualWndHeight = _coreWindow.getHeight();
        float xActualScale = 1f;
        float yActualScale = 1f;

        Area workArea  = _coreWindow.getWorkArea();
        
        if (appearInCenter) {
            if (CommonService.getOSType() != OSType.MAC) {
                actualWndWidth = (int) (_coreWindow.getWidth() * _coreWindow.getDpiScale().getX());
                actualWndHeight = (int) (_coreWindow.getHeight() * _coreWindow.getDpiScale().getY());
            }
            getPointer().setX(workArea.getX() + (workArea.getWidth() - actualWndWidth) / 2);
            getPointer().setY(workArea.getY() + (workArea.getHeight() - actualWndHeight) / 2);

        } else {

            if (CommonService.getOSType() != OSType.MAC) {
                xActualScale = _coreWindow.getDpiScale().getX();
                yActualScale = _coreWindow.getDpiScale().getY();
            }

            _coreWindow.setXDirect((int) (_coreWindow.getX() * xActualScale));
            _coreWindow.setYDirect((int) (_coreWindow.getY() * yActualScale));

            getPointer().setX(_coreWindow.getX());
            getPointer().setY(_coreWindow.getY());
        }
        glfwSetWindowPos(_window, getPointer().getX(), getPointer().getY());

        glfwSetWindowSizeLimits(_window, 
                (int) (_coreWindow.getMinWidth() * xActualScale),
                (int) (_coreWindow.getMinHeight() * yActualScale), 
                (int) (_coreWindow.getMaxWidth() * xActualScale),
                (int) (_coreWindow.getMaxHeight() * yActualScale));

        if (_coreWindow.isKeepAspectRatio)
            glfwSetWindowAspectRatio(_window, _coreWindow.ratioW, _coreWindow.ratioH);

        if (visible)
            glfwShowWindow(_window);
    }

    void switchContext() {
        glfwMakeContextCurrent(0);
        WindowManager.setContextCurrent(_coreWindow);
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
        contentScaleCallback = null;
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

    void setCallbackContentScale(GLFWWindowContentScaleCallback function) {
        contentScaleCallback = function;
        glfwSetWindowContentScaleCallback(_window, contentScaleCallback);
    }

    void setHidden(Boolean value) {
        if (value)
            glfwHideWindow(_window);
        else
            glfwShowWindow(_window);
    }
}