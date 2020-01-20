package com.spvessel.spacevil.Common;

import com.spvessel.spacevil.CoreWindow;
import com.spvessel.spacevil.WindowsBox;
import com.spvessel.spacevil.Core.InterfaceOpenGLLayer;

public final class RenderService {

    private RenderService() {
    }

    public static void restoreCommonGLSettings(CoreWindow window) {
        WindowsBox.restoreCommonGLSettings(window);
    }

    public static void restoreViewport(CoreWindow window) {
        WindowsBox.restoreViewport(window);
    }

    public static void setGLLayerViewport(CoreWindow window, InterfaceOpenGLLayer layer) {
        WindowsBox.setGLLayerViewport(window, layer);
    }
}