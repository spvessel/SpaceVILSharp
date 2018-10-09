package com.spvessel.Windows;

import com.spvessel.Common.*;
import com.spvessel.Cores.*;
import com.spvessel.Decorations.*;
import com.spvessel.Items.*;
import com.spvessel.Engine.*;

import java.util.*;
import java.util.List;
import java.awt.*;
import java.util.stream.Collectors;

public class WindowLayout {

    class DrawThread extends Thread {
        DrawEngine engine;

        DrawThread(String name, DrawEngine engine) {
            super(name);
            this.engine = engine;
        }

        public void run() {
            engine.init();
        }
    }

    class TaskThread extends Thread {
        ActionManager manager;

        TaskThread(String name, ActionManager manager) {
            super(name);
            this.manager = manager;
        }

        public void run() {
            manager.startManager();
        }
    }

    public InterfaceCommonMethod eventClose;
    public InterfaceCommonMethod eventMinimize;
    public InterfaceCommonMethod eventHide;

    private UUID _id;

    public UUID getId() {
        return _id;
    }

    public void setId(UUID value) {
        _id = value;
    }

    public Object engine_locker = new Object();

    private CoreWindow handler;

    public CoreWindow getHandler() {
        return handler;
    }

    private UUID ParentGUID;

    private DrawThread thread_engine;
    public DrawEngine engine;

    private TaskThread thread_manager;
    private ActionManager manager;

    public WindowLayout(CoreWindow window, String name, String title) {
        handler = window;
        _id = handler.getWindowGuid();
        setWindowName(name);
        setWindowTitle(title);

        isDialog = false;
        isBorderHidden = false;
        isClosed = true;
        isHidden = false;
        isResizable = true;
        isCentered = true;
        isFocusable = true;
        isAlwaysOnTop = false;
        isOutsideClickClosable = false;
        isMaximized = false;

        manager = new ActionManager(this);
        engine = new DrawEngine(this);

        synchronized (CommonService.GlobalLocker) {
            WindowLayoutBox.initWindow(this);
        }
        setFocusedItem(_window);
    }

    public WindowLayout(CoreWindow window, String name, String title, int width, int height, Boolean border_hidden) {
        handler = window;
        _id = handler.getWindowGuid();
        setWindowName(name);
        setWindowTitle(title);

        setWidth(width);
        setHeight(height);

        isDialog = false;
        isBorderHidden = border_hidden;
        isClosed = true;
        isHidden = false;
        isResizable = true;
        isCentered = true;
        isFocusable = true;
        isAlwaysOnTop = false;
        isOutsideClickClosable = false;
        isMaximized = false;

        manager = new ActionManager(this);
        engine = new DrawEngine(this);

        synchronized (CommonService.GlobalLocker) {
            WindowLayoutBox.initWindow(this);
        }
        setFocusedItem(_window);
    }

    public void updatePosition() {
        if (engine != null)
            engine.setWindowPos();
    }

    public void updateSize() {
        if (engine != null)
            engine.setWindowSize();
    }

    private WContainer _window;

    public WContainer getWindow() {
        return _window;
    }

    public void setWindow(WContainer window) {
        _window = window;
    }

    public void setBackground(Color color) {
        _window.setBackground(color);
    }

    public void setBackground(int r, int g, int b) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 255)
            r = 255;
        if (g < 0)
            g = Math.abs(g);
        if (g > 255)
            g = 255;
        if (b < 0)
            b = Math.abs(b);
        if (b > 255)
            b = 255;
        _window.setBackground(new Color(r, g, b, 255));
    }

    public void setBackground(int r, int g, int b, int a) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 255)
            r = 255;
        if (g < 0)
            g = Math.abs(g);
        if (g > 255)
            g = 255;
        if (b < 0)
            b = Math.abs(b);
        if (b > 255)
            b = 255;
        _window.setBackground(new Color(r, g, b, a));
    }

    public void setBackground(float r, float g, float b) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 1.0f)
            r = 1.0f;
        if (g < 0)
            g = Math.abs(g);
        if (g > 1.0f)
            g = 1.0f;
        if (b < 0)
            b = Math.abs(b);
        if (b > 1.0f)
            b = 1.0f;
        _window.setBackground(new Color((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f)));
    }

    public void setBackground(float r, float g, float b, float a) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 1.0f)
            r = 1.0f;
        if (g < 0)
            g = Math.abs(g);
        if (g > 1.0f)
            g = 1.0f;
        if (b < 0)
            b = Math.abs(b);
        if (b > 1.0f)
            b = 1.0f;
        _window.setBackground(
                new Color((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), (int) (a * 255.0f)));
    }

    public Color getBackground() {
        return _window.getBackground();
    }

    public void setPadding(Indents padding) {
        _window.setPadding(padding);
    }

    public void setPadding(int left, int top, int right, int bottom) {
        _window.setPadding(left, top, right, bottom);
    }

    public void addItem(BaseItem item) {
        _window.addItem(item);
    }

    public void addItems(BaseItem... items) {
        List<BaseItem> list = Arrays.stream(items).collect(Collectors.toList());
        for (BaseItem item : list) {
            _window.addItem(item);
        }
    }

    private String _name;

    public void setWindowName(String value) {
        _name = value;
    }

    public String getWindowName() {
        return _name;
    }

    private String _title;

    public void setWindowTitle(String title) {
        _title = title;
    }

    public String getWindowTitle() {
        return _title;
    }

    // geometry
    private Geometry _itemGeometry = new Geometry();

    public void setMinWidth(int width) {
        _itemGeometry.setMinWidth(width);
        if (_window != null)
            _window.setMinWidth(width);
    }

    public void setWidth(int width) {
        _itemGeometry.setWidth(width);
        if (_window != null)
            _window.setWidth(width);
    }

    public void setMaxWidth(int width) {
        _itemGeometry.setMaxWidth(width);
        if (_window != null)
            _window.setMaxWidth(width);
    }

    public void setMinHeight(int height) {
        _itemGeometry.setMinHeight(height);
        if (_window != null)
            _window.setMinHeight(height);
    }

    public void setHeight(int height) {
        _itemGeometry.setHeight(height);
        if (_window != null)
            _window.setHeight(height);
    }

    public void setMaxHeight(int height) {
        _itemGeometry.setMaxHeight(height);
        if (_window != null)
            _window.setMaxHeight(height);
    }

    public int getMinWidth() {
        return _itemGeometry.getMinWidth();
    }

    public int getWidth() {
        return _itemGeometry.getWidth();
    }

    public int getMaxWidth() {
        return _itemGeometry.getMaxWidth();
    }

    public int getMinHeight() {
        return _itemGeometry.getMinHeight();
    }

    public int getHeight() {
        return _itemGeometry.getHeight();
    }

    public int getMaxHeight() {
        return _itemGeometry.getMaxHeight();
    }

    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public void setMinSize(int width, int height) {
        setMinWidth(width);
        setMinHeight(height);
    }

    public void setMaxSize(int width, int height) {
        setMaxWidth(width);
        setMaxHeight(height);
    }

    public int[] getSize() {
        return _itemGeometry.getSize();
    }

    // position
    private Position _itemPosition = new Position();

    public void setX(int x) {
        _itemPosition.setX(x);
    }

    public int getX() {
        return _itemPosition.getX();
    }

    public void setY(int y) {
        _itemPosition.setY(y);
    }

    public int getY() {
        return _itemPosition.getY();
    }

    public void show() {
        if (isHidden)
            setHidden(false);
        isClosed = false;

        engine._handler.maximized = isMaximized;
        engine._handler.visible = !isHidden;
        engine._handler.resizeble = isResizable;
        engine._handler.borderHidden = isBorderHidden;
        engine._handler.appearInCenter = isCentered;
        engine._handler.focusable = isFocusable;
        engine._handler.alwaysOnTop = isAlwaysOnTop;
        engine._handler.wPosition.X = getX();
        engine._handler.wPosition.Y = getY();

        if (thread_engine != null && thread_engine.isAlive())
            return;

        thread_manager = new TaskThread(getWindowName() + "_" + "actions", manager);
        thread_manager.start();

        thread_engine = new DrawThread(getWindowName() + "_" + "engine", engine);

        if (isDialog) {
            synchronized (CommonService.GlobalLocker) {
                ParentGUID = WindowLayoutBox.lastFocusedWindow.getId();
                WindowLayoutBox.addToWindowDispatcher(this);
                WindowLayoutBox.getWindowInstance(ParentGUID).engine._handler.focusable = false;
                // WindowLayoutBox.getWindowInstance(ParentGUID).engine.update();
            }
            thread_engine.start();
            try {
                thread_engine.join();
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }
            // engine.init();
        }
        else {
            thread_engine.start();
            // engine.init();
        }
    }

    public void close() {
        if (isDialog) {
            engine.close();
            setWindowFocused();
            synchronized (CommonService.GlobalLocker) {
                WindowLayoutBox.removeWindow(this);
                WindowLayoutBox.removeFromWindowDispatcher(this);
            }
            if (thread_manager != null && thread_manager.isAlive()) {
                manager.stopManager();
                manager.execute.set();
            }
        } else {
            if (thread_engine != null && thread_engine.isAlive()) {
                engine.close();
            }
            if (thread_manager != null && thread_manager.isAlive()) {
                manager.stopManager();
                manager.execute.set();
            }
            isClosed = true;
        }
        if (eventClose != null)
            eventClose.execute();
    }

    public boolean isDialog;
    public boolean isClosed;
    public boolean isHidden;
    public boolean isResizable;
    public boolean isAlwaysOnTop;
    public boolean isBorderHidden;
    public boolean isCentered;
    public boolean isFocusable;
    public boolean isOutsideClickClosable;
    public boolean isFocused;
    public boolean isMaximized = false;

    public void setFocus(Boolean value) {
        if (isFocused == value)
            return;
        isFocused = value;
        if (value)
            setWindowFocused();
    }

    void setWindowFocused() {
        synchronized (CommonService.GlobalLocker) {
            if(WindowLayoutBox.getWindowInstance(ParentGUID) != null)
            WindowLayoutBox.getWindowInstance(ParentGUID).engine._handler.focusable = true;
            // WindowLayoutBox.getWindowInstance(ParentGUID).engine
            //         .focus(WindowLayoutBox.getWindowInstance(ParentGUID).engine._handler.getWindowId(), true);
        }
    }

    public void minimize() {
        engine.minimizeWindow();
    }

    public void maximize() {
        engine.maximizeWindow();
    }

    public void IsFixed(Boolean flag) {
    }

    public void setEventTask(EventTask task) {
        manager.stackEvents.add(task);
    }

    volatile Boolean set = true;

    public void executePollActions() {
//        manager.execute.release();
//        manager.notify();
        manager.execute.set();
    }

    public void setFocusedItem(VisualItem item) {
        engine.setFocusedItem(item);
    }

    public void resetItems() {
        engine.resetItems();
    }

    public void setIcon(Image icon_big, Image icon_small) {
        // engine.setBigIcon(icon_big);
        // engine.setSmallIcon(icon_small);
    }

    public void setHidden(Boolean value) {
        engine._handler.setHidden(value);
        isHidden = value;
    }
}