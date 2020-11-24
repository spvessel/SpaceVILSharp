package com.spvessel.glfwwrapper;

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
            NativeLibraryManager.ExtractEmbeddedLibrary();
            instance = new GlfwWrapper();
        }
        return instance;
    }

    // glfw: common
    public native int init();

    public native void terminate();

    public native void swapInterval(int interval);

    public native void swapBuffers(long window);

    public native String getClipboardString(long window);

    public native void setClipboardString(long window, String string);

    public native void waitEventsTimeout(double timeout);

    public native void waitEvents();

    public native void pollEvents();

    // glfw: callbacks
    public native void setCursorPosCallback(long window, GLFWCursorPosCallback callback);

    public native void setMouseButtonCallback(long window, GLFWMouseButtonCallback callback);

    public native void setScrollCallback(long window, GLFWScrollCallback callback);

    public native void setKeyCallback(long window, GLFWKeyCallback callback);

    public native void setCharModsCallback(long window, GLFWCharModsCallback callback);

    public native void setWindowCloseCallback(long window, GLFWWindowCloseCallback callback);

    public native void setWindowPosCallback(long window, GLFWWindowPosCallback callback);

    public native void setWindowFocusCallback(long window, GLFWWindowFocusCallback callback);

    public native void setWindowSizeCallback(long window, GLFWWindowSizeCallback callback);

    public native void setWindowIconifyCallback(long window, GLFWWindowIconifyCallback callback);

    public native void setWindowRefreshCallback(long window, GLFWWindowRefreshCallback callback);

    public native void setFramebufferSizeCallback(long window, GLFWFramebufferSizeCallback callback);

    public native void setWindowContentScaleCallback(long window, GLFWWindowContentScaleCallback callback);

    public native void setDropCallback(long window, GLFWDropCallback callback);

    // glfw: window
    public native void windowHint(int hint, int value);

    public native long createWindow(int width, int height, String title, long monitor, long share);

    public native void setWindowShouldClose(long window, int value);

    public native boolean windowShouldClose(long window);

    public native void makeContextCurrent(long window);

    public native void setWindowPos(long window, int x, int y);

    public native int[] getWindowPos(long window);

    public native void setWindowSize(long window, int width, int height);

    public native int[] getWindowSize(long window);

    public native void setWindowSizeLimits(long window, int minWidth, int minHeight, int maxWidth, int maxHeight);

    public native void setWindowAspectRatio(long window, int numer, int denom);

    public native void showWindow(long window);

    public native void hideWindow(long window);

    public native float[] getWindowContentScale(long window);

    public native void maximizeWindow(long window);

    public native void restoreWindow(long window);

    public native void focusWindow(long window);

    public native void iconifyWindow(long window);

    public native void destroyWindow(long window);

    public native void setWindowIcon(long window, int count, GLFWImage[] images);

    // glfw: display
    public native long getPrimaryMonitor();

    public native int[] getMonitorWorkarea(long monitor);

    public native void setWindowMonitor(long window, long monitor, int xpos, int ypos, int width, int height,
            int refreshrate);

    public native int[] getFramebufferSize(long window);

    public native GLFWVidMode getVideoMode(long monitor);

    public native float[] getMonitorContentScale(long monitor);

    // glfw: cursor
    public native long createStandardCursor(int shape);

    public native long createCursor(GLFWImage image, int xhot, int yhot);

    public native void setCursor(long window, long cursor);

    public native double[] getCursorPos(long window);

}
