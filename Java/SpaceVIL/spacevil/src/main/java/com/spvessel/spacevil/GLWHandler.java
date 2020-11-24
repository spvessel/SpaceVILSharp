package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.CommonService;
import com.spvessel.spacevil.Core.Area;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Exceptions.SpaceVILException;
import com.spvessel.spacevil.Flags.EmbeddedCursor;
import com.spvessel.spacevil.Flags.OSType;

import com.spvessel.spacevil.internal.Wrapper.*;

import java.nio.FloatBuffer;

final class GLWHandler {

    private GlfwWrapper glfw = null;

    ///////////////////////////////////////////////
    private GLFWCursorPosCallback mouseMoveCallback;
    private GLFWMouseButtonCallback mouseClickCallback;
    private GLFWScrollCallback mouseScrollCallback;

    private GLFWKeyCallback keyPressCallback;
    private GLFWCharModsCallback keyInputTextCallback;

    private GLFWWindowSizeCallback windowResizeCallback;
    private GLFWWindowCloseCallback windowCloseCallback;
    private GLFWWindowPosCallback windowPosCallback;
    private GLFWWindowFocusCallback windowFocusCallback;
    private GLFWWindowRefreshCallback windowRefreshCallback;
    private GLFWWindowIconifyCallback windowIconifyCallback;

    private GLFWDropCallback dropCallback;
    private GLFWFramebufferSizeCallback framebufferCallback;
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
    private Position wPosition = new Position();

    Position getPointer() {
        return wPosition;
    }
    ///////////////////////////////////////////////

    private CoreWindow _coreWindow;

    CoreWindow getCoreWindow() {
        return _coreWindow;
    }

    private long _window = 0;

    long getWindowId() {
        return _window;
    }

    int gVAO = 0;

    GLWHandler(CoreWindow handler) {
        glfw = GlfwWrapper.get();
        _coreWindow = handler;
        getPointer().setX(0);
        getPointer().setY(0);
    }

    void createWindow() throws SpaceVILException {
        // important!!! may be the best combination of WINDOW HINTS!!!
        // glfw.DefaultWindowHinst();
        
        glfw.WindowHint(Hint.OpenglForwardCompat, 1);
        glfw.WindowHint(Hint.OpenglProfile, OpenGLProfile.Core);
        glfw.WindowHint(Hint.Samples, _coreWindow._msaa.getValue());
        glfw.WindowHint(Hint.ContextVersionMajor, 3);
        glfw.WindowHint(Hint.ContextVersionMinor, 3);

        // scaling window's content (does not affect on Mac OS)
        glfw.WindowHint(Hint.ScaleToMonitor, 1);

        if (resizeble)
            glfw.WindowHint(Hint.Resizable, 1);
        else
            glfw.WindowHint(Hint.Resizable, 0);

        if (!borderHidden)
            glfw.WindowHint(Hint.Decorated, 1);
        else
            glfw.WindowHint(Hint.Decorated, 0);

        if (focused)
            glfw.WindowHint(Hint.Focused, 1);
        else
            glfw.WindowHint(Hint.Focused, 0);

        if (alwaysOnTop)
            glfw.WindowHint(Hint.Floating, 1);
        else
            glfw.WindowHint(Hint.Floating, 0);

        // if (maximized)
        // glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        // else
        // glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        if (transparent)
            glfw.WindowHint(Hint.TranspatentFramebuffer, 1);
        else
        glfw.WindowHint(Hint.TranspatentFramebuffer, 0);

        glfw.WindowHint(Hint.Visible, 0);

        _window = glfw.CreateWindow(_coreWindow.getWidth(), _coreWindow.getHeight(), _coreWindow.getWindowTitle(), 0, 0);

        if (_window == 0) {
            System.out.println("glfwCreateWindow fails");
            throw new SpaceVILException("Create window fails - " + getCoreWindow().getWindowTitle());
        }
        WindowManager.setContextCurrent(_coreWindow);

        float[] scale = glfw.GetWindowContentScale(_window);
        _coreWindow.setWindowScale(scale[0], scale[1]);

        // System.out.println(_coreWindow.getDpiScale().toString());

        int actualWndWidth = _coreWindow.getWidth();
        int actualWndHeight = _coreWindow.getHeight();
        float xActualScale = 1f;
        float yActualScale = 1f;

        Area workArea = _coreWindow.getWorkArea();

        if (appearInCenter) {
            if (CommonService.getOSType() != OSType.Mac) {
                actualWndWidth = (int) (_coreWindow.getWidth() * _coreWindow.getDpiScale().getXScale());
                actualWndHeight = (int) (_coreWindow.getHeight() * _coreWindow.getDpiScale().getYScale());
            }
            getPointer().setX(workArea.getX() + (workArea.getWidth() - actualWndWidth) / 2);
            getPointer().setY(workArea.getY() + (workArea.getHeight() - actualWndHeight) / 2);

        } else {

            if (CommonService.getOSType() != OSType.Mac) {
                xActualScale = _coreWindow.getDpiScale().getXScale();
                yActualScale = _coreWindow.getDpiScale().getYScale();
            }

            _coreWindow.setXDirect((int) (_coreWindow.getX() * xActualScale));
            _coreWindow.setYDirect((int) (_coreWindow.getY() * yActualScale));

            getPointer().setX(_coreWindow.getX() + workArea.getX());
            getPointer().setY(_coreWindow.getY() + workArea.getY());
        }
        glfw.SetWindowPos(_window, getPointer().getX(), getPointer().getY());

        glfw.SetWindowSizeLimits(_window, (int) (_coreWindow.getMinWidth() * xActualScale),
                (int) (_coreWindow.getMinHeight() * yActualScale), (int) (_coreWindow.getMaxWidth() * xActualScale),
                (int) (_coreWindow.getMaxHeight() * yActualScale));

        if (_coreWindow.isKeepAspectRatio)
            glfw.SetWindowAspectRatio(_window, _coreWindow.ratioW, _coreWindow.ratioH);

        if (visible) {
            glfw.ShowWindow(_window);
        }
    }

    void switchContext() {
        glfw.MakeContextCurrent(0);
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
        windowResizeCallback = null;
        windowRefreshCallback = null;
        windowIconifyCallback = null;

        framebufferCallback = null;
        dropCallback = null;
        contentScaleCallback = null;
    }

    void setCursorType(int type) {
        EmbeddedCursor cursor = EmbeddedCursor.valueOf(type);
        switch (cursor) {
            case Arrow:
                glfw.SetCursor(_window, CursorImage.cursorArrow);
                break;
            case IBeam:
                glfw.SetCursor(_window, CursorImage.cursorInput);
                break;
            case Crosshair:
                glfw.SetCursor(_window, CursorImage.cursorResizeAll);
                break;
            case Hand:
                glfw.SetCursor(_window, CursorImage.cursorHand);
                break;
            case ResizeX:
                glfw.SetCursor(_window, CursorImage.cursorResizeH);
                break;
            case ResizeY:
                glfw.SetCursor(_window, CursorImage.cursorResizeV);
                break;
            default:
                glfw.SetCursor(_window, CursorImage.cursorArrow);
                break;
        }
    }

    Boolean isClosing() {
        return glfw.WindowShouldClose(_window);
    }

    void destroy() {
        glfw.DestroyWindow(_window);
    }

    void swap() {
        glfw.SwapBuffers(_window);
    }

    void setToClose() {
        if (_window != 0)
            glfw.SetWindowShouldClose(_window, 1);
    }

    void setCallbackMouseMove(GLFWCursorPosCallback function) {
        mouseMoveCallback = function;
        glfw.SetCursorPosCallback(_window, mouseMoveCallback);
    }

    void setCallbackMouseClick(GLFWMouseButtonCallback function) {
        mouseClickCallback = function;
        glfw.SetMouseButtonCallback(_window, mouseClickCallback);
    }

    void setCallbackMouseScroll(GLFWScrollCallback function) {
        mouseScrollCallback = function;
        glfw.SetScrollCallback(_window, mouseScrollCallback);
    }

    void setCallbackKeyPress(GLFWKeyCallback function) {
        keyPressCallback = function;
        glfw.SetKeyCallback(_window, keyPressCallback);
    }

    void setCallbackTextInput(GLFWCharModsCallback function) {
        keyInputTextCallback = function;
        glfw.SetCharModsCallback(_window, keyInputTextCallback);
    }

    void setCallbackClose(GLFWWindowCloseCallback function) {
        windowCloseCallback = function;
        glfw.SetWindowCloseCallback(_window, windowCloseCallback);
    }

    void setCallbackPosition(GLFWWindowPosCallback function) {
        windowPosCallback = function;
        glfw.SetWindowPosCallback(_window, windowPosCallback);
    }

    void setCallbackFocus(GLFWWindowFocusCallback function) {
        windowFocusCallback = function;
        glfw.SetWindowFocusCallback(_window, windowFocusCallback);
    }

    void setCallbackResize(GLFWWindowSizeCallback function) {
        windowResizeCallback = function;
        glfw.SetWindowSizeCallback(_window, windowResizeCallback);
    }

    void setCallbackIconify(GLFWWindowIconifyCallback function) {
        windowIconifyCallback = function;
        glfw.SetWindowIconifyCallback(_window, windowIconifyCallback);
    }

    void setCallbackFramebuffer(GLFWFramebufferSizeCallback function) {
        framebufferCallback = function;
        glfw.SetFramebufferSizeCallback(_window, framebufferCallback);
    }

    void setCallbackRefresh(GLFWWindowRefreshCallback function) {
        windowRefreshCallback = function;
        glfw.SetWindowRefreshCallback(_window, windowRefreshCallback);
    }

    void setCallbackDrop(GLFWDropCallback function) {
        dropCallback = function;
        glfw.SetDropCallback(_window, dropCallback);
    }

    void setCallbackContentScale(GLFWWindowContentScaleCallback function) {
        contentScaleCallback = function;
        glfw.SetWindowContentScaleCallback(_window, contentScaleCallback);
    }

    void setHidden(Boolean value) {
        if (value)
            glfw.HideWindow(_window);
        else
            glfw.ShowWindow(_window);
    }
}