package com.spvessel.spacevil.Common;

import java.util.HashSet;
import java.util.Set;

import com.spvessel.spacevil.CoreWindow;
import com.spvessel.spacevil.WindowsBox;
import com.spvessel.spacevil.Core.IOpenGLLayer;
import com.spvessel.spacevil.Flags.BenchmarkIndicator;

/**
 * RenderService is static class providing methods to managing OpenGL attributes
 * such as settings and viewport used by SpaceVIL.
 * <p>
 * Tip: RenderService is usualy used with
 * com.spvessel.spacevil.Core.IOpenGLLayer.
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
     * com.spvessel.spacevil.Core.IOpenGLLayer.
     * 
     * @param window A window as com.spvessel.spacevil.CoreWindow.
     * @param layer  An item that extends Prototype and implements
     *               com.spvessel.spacevil.Core.IOpenGLLayer.
     */
    public static void setGLLayerViewport(CoreWindow window, IOpenGLLayer layer) {
        WindowsBox.setGLLayerViewport(window, layer);
    }

    public static void enableMonitoring(BenchmarkIndicator... indicators) {
        if (indicators == null) {
            return;
        }
        indicatorList = new HashSet<>();
        for (BenchmarkIndicator benchmarkIndicator : indicators) {
            indicatorList.add(benchmarkIndicator);
        }
    }

    public static void disableMonitoring() {
        indicatorList = null;
    }

    public static Set<BenchmarkIndicator> getMonitoringIndicators() {
        if (indicatorList == null) {
            return null;
        }
        return new HashSet<>(indicatorList);
    }

    private static Set<BenchmarkIndicator> indicatorList = null;
}