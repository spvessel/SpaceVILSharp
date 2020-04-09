package com.spvessel.spacevil.Common;

import com.spvessel.spacevil.CoreWindow;
import com.spvessel.spacevil.WindowsBox;
import com.spvessel.spacevil.Core.InterfaceOpenGLLayer;

/**
 * RenderService is static class providing methods to managing OpenGL attributes
 * such as settings and viewport used by SpaceVIL.
 * <p>
 * Tip: RenderService is usualy used with
 * com.spvessel.spacevil.Core.InterfaceOpenGLLayer.
 */
public final class RenderService {

    private RenderService() {
    }

    /**
     * Restoring initial SpaceVIL OpenGL settings for the specified window (if they
     * have been changed).
     * 
     * @param window A window as com.spvessel.spacevil.CoreWindow.
     */
    public static void restoreCommonGLSettings(CoreWindow window) {
        WindowsBox.restoreCommonGLSettings(window);
    }

    /**
     * Restoring initial OpenGL viewport of SpaceVIL environment for the specified
     * window (if it was changed).
     * 
     * @param window A window as com.spvessel.spacevil.CoreWindow.
     */
    public static void restoreViewport(CoreWindow window) {
        WindowsBox.restoreViewport(window);
    }

    /**
     * Setting custom viewport by the specified window and
     * com.spvessel.spacevil.Core.InterfaceOpenGLLayer.
     * 
     * @param window A window as com.spvessel.spacevil.CoreWindow.
     * @param layer  An item that extends Prototype and implements
     *               com.spvessel.spacevil.Core.InterfaceOpenGLLayer.
     */
    public static void setGLLayerViewport(CoreWindow window, InterfaceOpenGLLayer layer) {
        WindowsBox.setGLLayerViewport(window, layer);
    }
}