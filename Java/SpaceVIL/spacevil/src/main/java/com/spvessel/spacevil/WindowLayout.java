package com.spvessel.spacevil;

import static org.lwjgl.system.MemoryUtil.NULL;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.spvessel.spacevil.Core.InterfaceOpenGLLayer;

final class WindowLayout {

    void restoreView() {
        if (_engine != null) {
            _engine.restoreView();
        }
    }

    void restoreCommonGLSettings() {
        if (_engine != null) {
            _engine.restoreCommonGLSettings();
        }
    }

    void setGLLayerViewport(InterfaceOpenGLLayer layer) {
        if (_engine != null) {
            _engine.setGLLayerViewport(layer);
        }
    }

    Lock engineLocker = new ReentrantLock();
    private Lock wndLock = new ReentrantLock();

    private UUID _id;

    UUID getId() {
        return _id;
    }

    void setId(UUID value) {
        _id = value;
    }

    private CoreWindow _coreWindow;

    CoreWindow getCoreWindow() {
        return _coreWindow;
    }

    void setCoreWindow() {
        _id = _coreWindow.getWindowGuid();

        wndLock.lock();
        try {
            WindowsBox.initWindow(_coreWindow);
            setFocusedItem(_window);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        } finally {
            wndLock.unlock();
        }
    }

    private DrawEngine _engine;

    private Thread _threadActionManager;
    private ActionManager _actionManager;

    WindowLayout(CoreWindow window) {
        _coreWindow = window;
        _actionManager = new ActionManager(_coreWindow);
        _engine = new DrawEngine(_coreWindow);
        _coreWindow.eventClose.add(this::close);
    }

    private WContainer _window;

    WContainer getContainer() {
        return _window;
    }

    void setWindow(WContainer window) {
        _window = window;
    }

    final void show() {
        if (!WindowManager.isRunning())
            WindowManager.startWith(_coreWindow);
        else
            WindowManager.addWindow(_coreWindow);
    }

    void close() {
        WindowManager.closeWindow(_coreWindow);
    }

    void setFocusable(boolean value) {
        _engine.glwHandler.focusable = value;
    }

    void minimize() {
        _engine.minimizeRequest = true;
    }

    void maximize() {
        _engine.maximizeRequest = true;
    }

    void toggleFullScreen() {
        _engine.fullScreenRequest = true;
    }

    void updatePosition() {
        _engine.updatePositionRequest = true;
    }

    void updateSize() {
        _engine.updateSizeRequest = true;
    }

    void isFixed(Boolean flag) {
    }

    void setEventTask(EventTask task) {
        _actionManager.addTask(task);
    }

    void executePollActions() {
        _actionManager.execute.set();
    }

    Prototype getFocusedItem() {
        return _engine.getFocusedItem();
    }

    void setFocusedItem(Prototype item) {
        _engine.setFocusedItem(item);
    }

    void resetItems() {
        _engine.resetItems();
    }

    void resetFocus() {
        _engine.resetFocus();
    }

    void setIcon(BufferedImage icon_big, BufferedImage icon_small) {
        _engine.setBigIcon(icon_big);
        _engine.setSmallIcon(icon_small);
    }

    void setHidden(Boolean value) {
        _engine.glwHandler.setHidden(value);
    }

    long getGLWID() {
        return _engine.glwHandler.getWindowId();
    }

    boolean isGLWIDValid() {
        if (getGLWID() == NULL)
            return false;
        return true;
    }

    boolean initEngine() {
        if (_coreWindow.isHidden)
            setHidden(false);
        _coreWindow.isClosed = false;

        _engine.glwHandler.transparent = _coreWindow.isTransparent;
        _engine.glwHandler.maximized = _coreWindow.isMaximized;
        _engine.glwHandler.visible = !_coreWindow.isHidden;
        _engine.glwHandler.resizeble = _coreWindow.isResizable;
        _engine.glwHandler.borderHidden = _coreWindow.isBorderHidden;
        _engine.glwHandler.appearInCenter = _coreWindow.isCentered;
        _engine.glwHandler.focusable = _coreWindow.isFocusable;
        _engine.glwHandler.alwaysOnTop = _coreWindow.isAlwaysOnTop;

        _engine.glwHandler.getPointer().setX(_coreWindow.getX());
        _engine.glwHandler.getPointer().setY(_coreWindow.getY());

        _threadActionManager = new Thread(() -> {
            _actionManager.startManager();
        });
        _threadActionManager.setDaemon(true);
        _threadActionManager.start();

        createWindowsPair();

        return _engine.init();
    }

    void createWindowsPair() {
        WindowsBox.createWindowsPair(_coreWindow);
        if (_coreWindow.isDialog) {
            WindowsBox.getWindowPair(_coreWindow).setFocusable(false);
        }
    }

    void dispose() {
        CoreWindow currentPair = getPairForCurrentWindow();
        destroyWindowsPair();
        if (_threadActionManager != null && _threadActionManager.isAlive()) {
            _actionManager.stopManager();
            _actionManager.execute.set();
        }
        _engine.freeOnClose();
        if (currentPair != null && !currentPair.isClosed)
            currentPair.setWindowFocused();
    }

    void destroyWindowsPair() {
        if (_coreWindow.isDialog) {
            CoreWindow pair = WindowsBox.getWindowPair(_coreWindow);
            if (pair != null)
                pair.setFocusable(true);
            WindowsBox.removeWindow(_coreWindow);
        }
        WindowsBox.removeFromWindowDispatcher(_coreWindow);
    }

    CoreWindow getPairForCurrentWindow() {
        return WindowsBox.getWindowPair(_coreWindow);
    }

    void updateScene() {
        _engine.drawScene();
    }

    void render() {
        _engine.render();
    }

    void setFocus() {
        setFocusable(true);
        _engine.setWindowFocused();
    }

    private Color _shadeColor = new Color(0, 0, 0, 200);

    void setShadeColor(Color color) {
        wndLock.lock();
        try {
            _shadeColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        } finally {
            wndLock.unlock();
        }
    }

    void setShadeColor(int r, int g, int b) {
        setShadeColor(GraphicsMathService.colorTransform(r, g, b));
    }

    void setShadeColor(int r, int g, int b, int a) {
        setShadeColor(GraphicsMathService.colorTransform(r, g, b, a));
    }

    void setShadeColor(float r, float g, float b) {
        setShadeColor(GraphicsMathService.colorTransform(r, g, b));
    }

    void setShadeColor(float r, float g, float b, float a) {
        setShadeColor(GraphicsMathService.colorTransform(r, g, b, a));
    }

    Color getShadeColor() {
        wndLock.lock();
        try {
            return _shadeColor;
        } finally {
            wndLock.unlock();
        }
    }

    <T> void freeVRAMResource(T resource) {
        _engine.freeVRAMResource(resource);
    }

    ToolTipItem getToolTip() {
        return _engine.getToolTip();
    }
}