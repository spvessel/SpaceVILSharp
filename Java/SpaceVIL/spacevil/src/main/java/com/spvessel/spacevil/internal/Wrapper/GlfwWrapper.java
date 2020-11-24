package com.spvessel.spacevil.internal.Wrapper;

/**
 * The minimum required Glfw constants and functions for SpaceVIL framework to
 * work.
 */
public final class GlfwWrapper {
    private GlfwWrapper() {
    }

    private static GlfwWrapper instance = null;

    public static GlfwWrapper get() {
        if (instance == null) {
            instance = new GlfwWrapper();
        }
        return instance;
    }

    // glfw: common
    public native int Init();

    public native void Terminate();

    public native void SwapInterval(int interval);

    public native void SwapBuffers(long window);

    public native String GetClipboardString(long window);

    public native void SetClipboardString(long window, String string);

    public native void WaitEventsTimeout(double timeout);

    public native void WaitEvents();

    public native void PollEvents();

    // glfw: callbacks
    public native void SetCursorPosCallback(long window, GLFWCursorPosCallback callback);

    public native void SetMouseButtonCallback(long window, GLFWMouseButtonCallback callback);

    public native void SetScrollCallback(long window, GLFWScrollCallback callback);

    public native void SetKeyCallback(long window, GLFWKeyCallback callback);

    public native void SetCharModsCallback(long window, GLFWCharModsCallback callback);

    public native void SetWindowCloseCallback(long window, GLFWWindowCloseCallback callback);

    public native void SetWindowPosCallback(long window, GLFWWindowPosCallback callback);

    public native void SetWindowFocusCallback(long window, GLFWWindowFocusCallback callback);

    public native void SetWindowSizeCallback(long window, GLFWWindowSizeCallback callback);

    public native void SetWindowIconifyCallback(long window, GLFWWindowIconifyCallback callback);

    public native void SetWindowRefreshCallback(long window, GLFWWindowRefreshCallback callback);

    public native void SetFramebufferSizeCallback(long window, GLFWFramebufferSizeCallback callback);

    public native void SetWindowContentScaleCallback(long window, GLFWWindowContentScaleCallback callback);

    public native void SetDropCallback(long window, GLFWDropCallback callback);

    // glfw: window

    public native void DefaultWindowHinst();

    public native void WindowHint(int hint, int value);

    public native long CreateWindow(int width, int height, String title, long monitor, long share);

    public native void SetWindowShouldClose(long window, int value);

    public native boolean WindowShouldClose(long window);

    public native void MakeContextCurrent(long window);

    public native void SetWindowPos(long window, int x, int y);

    public native int[] GetWindowPos(long window);

    public native void SetWindowSize(long window, int width, int height);

    public native int[] GetWindowSize(long window);

    public native void SetWindowSizeLimits(long window, int minWidth, int minHeight, int maxWidth, int maxHeight);

    public native void SetWindowAspectRatio(long window, int numer, int denom);

    public native void ShowWindow(long window);

    public native void HideWindow(long window);

    public native float[] GetWindowContentScale(long window);

    public native void MaximizeWindow(long window);

    public native void RestoreWindow(long window);

    public native void FocusWindow(long window);

    public native void IconifyWindow(long window);

    public native void DestroyWindow(long window);

    public native void SetWindowIcon(long window, int count, GLFWImage[] images);

    // glfw: display
    public native long GetPrimaryMonitor();

    public native int[] GetMonitorWorkarea(long monitor);

    public native void SetWindowMonitor(long window, long monitor, int xpos, int ypos, int width, int height,
            int refreshrate);

    public native int[] GetFramebufferSize(long window);

    public native GLFWVidMode GetVideoMode(long monitor);

    public native float[] GetMonitorContentScale(long monitor);

    // glfw: cursor
    public native long CreateStandardCursor(int shape);

    public native long CreateCursor(GLFWImage image, int xhot, int yhot);

    public native void SetCursor(long window, long cursor);

    public native double[] GetCursorPos(long window);
}
