package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.CommonService;
import com.spvessel.spacevil.Flags.OSType;
import com.spvessel.spacevil.Flags.RedrawFrequency;

import static org.lwjgl.system.MemoryUtil.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

final class WindowLayout {
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
            System.out.println(ex.toString());
            return;
        } finally {
            wndLock.unlock();
        }
    }

    private UUID ParentGUID;

    private Thread thread_engine;
    private DrawEngine engine;

    private Thread thread_manager;
    private ActionManager manager;

    WindowLayout(CoreWindow cWindow) {
        _coreWindow = cWindow;
        manager = new ActionManager(_coreWindow);
        engine = new DrawEngine(_coreWindow);
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
        if (_coreWindow.isHidden)
            setHidden(false);
        _coreWindow.isClosed = false;

        engine._handler.transparent = _coreWindow.isTransparent;
        engine._handler.maximized = _coreWindow.isMaximized;
        engine._handler.visible = !_coreWindow.isHidden;
        engine._handler.resizeble = _coreWindow.isResizable;
        engine._handler.borderHidden = _coreWindow.isBorderHidden;
        engine._handler.appearInCenter = _coreWindow.isCentered;
        engine._handler.focusable = _coreWindow.isFocusable;
        engine._handler.alwaysOnTop = _coreWindow.isAlwaysOnTop;
        
        engine._handler.getPointer().setX(_coreWindow.getX());
        engine._handler.getPointer().setY(_coreWindow.getY());

        thread_manager = new Thread(() -> {
            manager.startManager();
        });
        thread_manager.setDaemon(true);
        thread_manager.start();

        if (CommonService.getOSType() == OSType.MAC) {
            if (!WindowsBox.isAnyWindowRunning()) {
                WindowsBox.setWindowRunning(_coreWindow);
                engine.init();
            }
        } else {
            showInsideNewThread();
        }
    }

    private void showInsideNewThread() {
        if (thread_engine != null && thread_engine.isAlive())
            return;
        thread_engine = new Thread(() -> {
            engine.init();
        });

        if (_coreWindow.isDialog) {
            wndLock.lock();
            try {
                ParentGUID = WindowsBox.lastFocusedWindow.getWindowGuid();
                WindowsBox.addToWindowDispatcher(_coreWindow);
                WindowsBox.getWindowInstance(ParentGUID).setFocusable(false);
            } finally {
                wndLock.unlock();
            }
            thread_engine.setDaemon(false);
            thread_engine.start();
            try {
                thread_engine.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            thread_engine.setDaemon(false);
            thread_engine.start();
        }
    }

    void close() {
        if (CommonService.getOSType() == OSType.MAC) {
            engine._handler.setToClose();
            WindowsBox.setWindowRunning(null);
        } else {
            closeInsideNewThread();
        }

        if (thread_manager != null && thread_manager.isAlive()) {
            manager.stopManager();
            manager.execute.set();
        }
        _coreWindow.isClosed = true;
    }

    private void closeInsideNewThread() {
        if (_coreWindow.isDialog) {
            engine._handler.setToClose();
            setWindowFocused();
            wndLock.lock();
            try {
                WindowsBox.removeWindow(_coreWindow);
                WindowsBox.removeFromWindowDispatcher(_coreWindow);
            } finally {
                wndLock.unlock();
            }
        } else {
            if (thread_engine != null && thread_engine.isAlive())
                engine._handler.setToClose();
        }
    }

    void setWindowFocused() {
        wndLock.lock();
        try {
            if (WindowsBox.getWindowInstance(ParentGUID) != null)
                WindowsBox.getWindowInstance(ParentGUID).setFocusable(true);
        } finally {
            wndLock.unlock();
        }
    }

    void setFocusable(boolean value) {
        engine._handler.focusable = value;
    }

    void minimize() {
        engine.minimizeWindow();
    }

    void maximize() {
        engine.maximizeRequest = true;
    }

    void isFixed(Boolean flag) {
    }

    void setEventTask(EventTask task) {
        manager.addTask(task);
    }

    void executePollActions() {
        manager.execute.set();
    }

    Prototype getFocusedItem() {
        return engine.getFocusedItem();
    }

    void setFocusedItem(Prototype item) {
        engine.setFocusedItem(item);
    }

    void resetItems() {
        engine.resetItems();
    }

    void resetFocus() {
        engine.resetFocus();
    }

    void setIcon(BufferedImage icon_big, BufferedImage icon_small) {
        engine.setBigIcon(icon_big);
        engine.setSmallIcon(icon_small);
    }

    void setHidden(Boolean value) {
        engine._handler.setHidden(value);
    }

    private float _scaleWidth = 1.0f;
    private float _scaleHeight = 1.0f;

    float[] getDpiScale() {
        return new float[] { _scaleWidth, _scaleHeight };
    }

    void setDpiScale(float w, float h) {
        _scaleWidth = w;
        _scaleHeight = h;
        // _scaleWidth = w * 2;
        // _scaleHeight = h * 2;
    }

    void setRenderFrequency(RedrawFrequency value) {
        engineLocker.lock();
        try {
            engine.setFrequency(value);
        } finally {
            engineLocker.unlock();
        }
    }

    RedrawFrequency getRenderFrequency() {
        engineLocker.lock();
        try {
            return engine.getRedrawFrequency();
        } finally {
            engineLocker.unlock();
        }
    }

    long getGLWID() {
        if (engine == null)
            return NULL;
        return engine._handler.getWindowId();
    }
}