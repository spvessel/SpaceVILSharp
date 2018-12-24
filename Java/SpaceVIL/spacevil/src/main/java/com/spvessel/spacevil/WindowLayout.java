package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.Geometry;
import com.spvessel.spacevil.Core.InterfaceCommonMethod;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Decorations.Indents;

import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.stream.Collectors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class WindowLayout {
    Lock engineLocker = new ReentrantLock();
    private Lock wndLock = new ReentrantLock();

    // class DrawThread extends Thread {
    //     DrawEngine engine;

    //     DrawThread(String name, DrawEngine engine) {
    //         super(name);
    //         this.engine = engine;
    //     }

    //     public void run() {
    //         engine.init();
    //     }
    // }

    // class TaskThread extends Thread {
    //     ActionManager manager;

    //     TaskThread(String name, ActionManager manager) {
    //         super(name);
    //         this.manager = manager;
    //     }

    //     public void run() {
    //         manager.startManager();
    //     }
    // }

    public InterfaceCommonMethod eventClose;
    public InterfaceCommonMethod eventMinimize;
    public InterfaceCommonMethod eventHide;

    private boolean isMain = false;

    public void setMainThread() {
        isMain = true;
    }

    private UUID _id;

    public UUID getId() {
        return _id;
    }

    public void setId(UUID value) {
        _id = value;
    }

    private CoreWindow handler;

    public CoreWindow getCoreWindow() {
        return handler;
    }
    void setCoreWindow(CoreWindow window) {
        if (handler != null && handler.equals(window)) return;
        handler = window;
        _id = handler.getWindowGuid();

        wndLock.lock();
        try {
            WindowLayoutBox.initWindow(this);
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
    protected DrawEngine engine;

    private Thread thread_manager;
    private ActionManager manager;

    public WindowLayout(String name, String title) {

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
    }

    public WindowLayout(String name, String title, int width, int height, Boolean border_hidden) {
        this(name, title);

        setWidth(width);
        setHeight(height);

        isBorderHidden = border_hidden;
    }

    protected void updatePosition() {
        // if (engine != null)
        // engine.setWindowPos();
    }

    protected void updateSize() {
        // if (engine != null)
        // engine.setWindowSize();
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

    public void addItem(InterfaceBaseItem item) {
        _window.addItem(item);
    }

    public void addItems(InterfaceBaseItem... items) {
        List<InterfaceBaseItem> list = Arrays.stream(items).collect(Collectors.toList());
        for (InterfaceBaseItem item : list) {
            _window.addItem(item);
        }
    }

    private String _name;

    void setWindowName(String value) {
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
        engine._handler.getPointer().setX(getX());
        engine._handler.getPointer().setY(getY());

        if (thread_engine != null && thread_engine.isAlive())
            return;

            thread_manager = new Thread(()-> {
                manager.startManager();
            });
        // thread_manager = new TaskThread(getWindowName() + "_" + "actions", manager);
        // thread_manager.setPriority(Thread.MAX_PRIORITY);
        thread_manager.setDaemon(true);
        thread_manager.start();

        if (!isMain) {
            thread_engine = new Thread(() -> {
                engine.init();
            });
            // thread_engine = new DrawThread(getWindowName() + "_" + "engine", engine);
            // thread_engine.setPriority(Thread.MAX_PRIORITY);
        }

        if (isDialog) {
            wndLock.lock();
            try {
                ParentGUID = WindowLayoutBox.lastFocusedWindow.getId();
                WindowLayoutBox.addToWindowDispatcher(this);
                WindowLayoutBox.getWindowInstance(ParentGUID).engine._handler.focusable = false;
                // WindowLayoutBox.getWindowInstance(ParentGUID).engine.update();
            } finally {
                wndLock.unlock();
            }
            thread_engine.setDaemon(false);
            thread_engine.start();
            try {
                thread_engine.join();
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }
        } else {

            if (!isMain) {
                thread_engine.setDaemon(false);
                thread_engine.start();
            } else
                engine.init();
        }
    }

    public void close() {
        if (isDialog) {
            engine.close();
            setWindowFocused();
            wndLock.lock();
            try {
                WindowLayoutBox.removeWindow(this);
                WindowLayoutBox.removeFromWindowDispatcher(this);
            } finally {
                wndLock.unlock();
            }
            if (thread_manager != null && thread_manager.isAlive()) {
                manager.stopManager();
                manager.execute.set();
            }
        } else {
            if (!isMain) {
                if (thread_engine != null && thread_engine.isAlive()) {
                    engine.close();
                }
            } else
                engine.close();

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

    public void setWindowFocused() {
        wndLock.lock();
        try {
            if (WindowLayoutBox.getWindowInstance(ParentGUID) != null)
                WindowLayoutBox.getWindowInstance(ParentGUID).engine._handler.focusable = true;
        } finally {
            wndLock.unlock();
        }
    }

    public void minimize() {
        engine.minimizeWindow();
    }

    public void maximize() {
        engine.maximizeWindow();
    }

    protected void isFixed(Boolean flag) {
    }

    protected void setEventTask(EventTask task) {
        // manager.stackEvents.add(task);
        manager.addTask(task);
    }

    // private volatile Boolean set = true;

    protected void executePollActions() {
        // manager.execute.release();
        // manager.notify();
        manager.execute.set();
    }

    public Prototype getFocusedItem() {
        return engine.getFocusedItem();
    }
    public void setFocusedItem(Prototype item) {
        engine.setFocusedItem(item);
    }

    public void resetItems() {
        engine.resetItems();
    }

    public void setIcon(BufferedImage icon_big, BufferedImage icon_small) {
        engine.setBigIcon(icon_big);
        engine.setSmallIcon(icon_small);
    }

    public void setHidden(Boolean value) {
        engine._handler.setHidden(value);
        isHidden = value;
    }
}