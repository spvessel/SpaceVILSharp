package com.spvessel.spacevil;

import java.util.Map;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.spvessel.spacevil.Core.EventCommonMethod;
import com.spvessel.spacevil.Flags.RedrawFrequency;
import com.spvessel.spacevil.Flags.RenderType;

import com.spvessel.spacevil.internal.Wrapper.*;

/**
 * WindowManager is a static class that is designed to manage instances of a
 * window and entire application. Provides control for changing render
 * frequency, render type, vertical sync, adding/closing windows, exiting the
 * app and more.
 */
public final class WindowManager {

    private static GlfwWrapper glfw = null;
    static {
        glfw = GlfwWrapper.get();
    }

    private WindowManager() {
        waitfunc.add(() -> glfw.WaitEventsTimeout(getCurrentFrequency()));
    }

    private static float _intervalVeryLow = 1.0f;
    private static float _intervalLow = 1.0f / 10.0f;
    private static float _intervalMedium = 1.0f / 30.0f;
    private static float _intervalHigh = 1.0f / 60.0f;
    private static float _intervalUltra = 1.0f / 120.0f;
    private static float _intervalAssigned = 1.0f / 10.0f;
    private static RedrawFrequency _frequency = RedrawFrequency.Low;

    /**
     * Setting the frequency of redrawing scene in idle state. The higher the level,
     * the more computer resources are used. Default:
     * SpaceVIL.Core.RedrawFrequency.Low
     * <p>
     * Can be:
     * <p>
     * VERY_LOW - 1 frame per second,
     * <p>
     * LOW - up to 10 frames per second,
     * <p>
     * MEDIUM - up to 30 frames per second,
     * <p>
     * HIGH - up to 60 frames per second,
     * <p>
     * ULTRA - up to 120 frames per second,
     * 
     * @param value A frequency level as com.spvessel.spacevil.Flags.RedrawFrequency
     */
    public static void setRenderFrequency(RedrawFrequency value) {
        _lock.lock();
        try {
            _frequency = value;
            if (value == RedrawFrequency.VeryLow) {
                _intervalAssigned = _intervalVeryLow;
            } else if (value == RedrawFrequency.Low) {
                _intervalAssigned = _intervalLow;
            } else if (value == RedrawFrequency.Medium) {
                _intervalAssigned = _intervalMedium;
            } else if (value == RedrawFrequency.High) {
                _intervalAssigned = _intervalHigh;
            } else if (value == RedrawFrequency.Ultra) {
                _intervalAssigned = _intervalUltra;
            }
        } catch (Exception ex) {
            System.out.println("Method - SetFrequency");
            ex.printStackTrace();
        } finally {
            _lock.unlock();
        }
    }

    private static float getCurrentFrequency() {
        _lock.lock();
        try {
            return _intervalAssigned;
        } catch (Exception ex) {
            System.out.println("Method - SetFrequency");
            ex.printStackTrace();
            return _intervalLow;
        } finally {
            _lock.unlock();
        }
    }

    /**
     * Getting the current render frequency.
     * 
     * @return The current render frequency as
     *         com.spvessel.spacevil.Flags.RedrawFrequency.
     */
    public static RedrawFrequency getRenderFrequency() {
        _lock.lock();
        try {
            return _frequency;
        } catch (Exception ex) {
            System.out.println("Method - SetFrequency");
            ex.printStackTrace();
            _frequency = RedrawFrequency.Low;
            return _frequency;
        } finally {
            _lock.unlock();
        }
    }

    private static int _vsync = 1;

    /**
     * Setting the vsync value. If value is 0 - vsync is OFF, if other value - vsync
     * is ON. The total amount of FPS calculated by the formula: 1.0 /
     * Math.Abs(value) * DisplayRefreshRate, so if value is 2 (or -2) and dysplay
     * refresh rate is 60 then 1.0 / 2 * 60 = 30 fps. Default: 1 - ENABLE.
     * 
     * @param value Value of vsync.
     */
    public static void enableVSync(int value) {
        if (_isRunning) {
            return;
        }
        _vsync = value;
    }

    /**
     * Getting the current vsync value. If value is 0 - vsync is OFF, if other value
     * - vsync is ON. The total amount of FPS calculated by the formula: 1.0 /
     * Math.Abs(value) * DisplayRefreshRate, so if value is 2 (or -2) and dysplay
     * refresh rate is 60 then 1.0 / 2 * 60 = 30 fps. Default: 1 - ENABLE.
     * 
     * @return The current vsync value
     */
    public static int getVSyncValue() {
        return _vsync;
    }

    /**
     * Setting the common render type. Default: SpaceVIL.Core.RenderType.Periodic.
     * <p>
     * Can be:
     * <p>
     * IfNeeded - the scene is redrawn only if any input event occurs (mouse move,
     * mouse click, keyboard key press, window resizing and etc.),
     * <p>
     * Periodic - the scene is redrawn according to the current render frequency
     * type (See SetRenderFrequency(type)) in idle and every time when any input
     * event occurs,
     * <p>
     * Always - the scene is constantly being redrawn.
     * 
     * @param value A render type as com.spvessel.spacevil.Flags.RenderType.
     */
    public static void setRenderType(RenderType value) {
        _lock.lock();
        try {
            waitfunc.clear();
            if (value == RenderType.IfNeeded) {
                waitfunc.add(() -> glfw.WaitEvents());
            } else if (value == RenderType.Periodic) {
                waitfunc.add(() -> glfw.WaitEventsTimeout(getCurrentFrequency()));
            } else if (value == RenderType.Always) {
                waitfunc.add(() -> glfw.PollEvents());
            }

        } catch (Exception ex) {
            System.out.println("Method - setRenderType");
            ex.printStackTrace();
        } finally {
            _lock.unlock();
        }
    }

    private static Lock _lock = new ReentrantLock();
    private static Map<CoreWindow, Boolean> _initializedWindows = new LinkedHashMap<>();
    private static List<CoreWindow> _windows = new LinkedList<>();
    private static Deque<CoreWindow> _listWaitigForInit = new ArrayDeque<>();
    private static Deque<CoreWindow> _listWaitigForClose = new ArrayDeque<>();
    private static boolean _isRunning = false;

    static boolean isRunning() {
        return _isRunning;
    }

    private static boolean _isEmpty = true;

    /**
     * Adding a window to rendering queue. After adding the window shows up
     * immediately.
     * 
     * @param wnd Any CoreWindow instance.
     */
    public static void addWindow(CoreWindow wnd) {
        _lock.lock();
        try {
            if (!_windows.contains(wnd)) {
                if (_isRunning) {
                    _listWaitigForInit.add(wnd);
                } else {
                    _windows.add(wnd);
                    _isEmpty = _windows.isEmpty();
                }
            }
        } catch (Exception e) {
        } finally {
            _lock.unlock();
        }
    }

    private static List<CoreWindow> getStoredWindows() {
        _lock.lock();
        try {
            return new LinkedList<>(_windows);
        } catch (Exception e) {
            e.printStackTrace();
            return new LinkedList<>();
        } finally {
            _lock.unlock();
        }
    }

    /**
     * Closing the specified window if it exist in render queue.
     * 
     * @param wnd Any CoreWindow instance.
     */
    public static void closeWindow(CoreWindow wnd) {
        _lock.lock();
        try {
            if (_initializedWindows.containsKey(wnd)) {
                _listWaitigForClose.add(wnd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            _lock.unlock();
        }
    }

    private static EventCommonMethod waitfunc = new EventCommonMethod();

    private static void run() {
        List<CoreWindow> initFailed = new ArrayList<>();
        for (CoreWindow wnd : _windows) {
            if (!initWindow(wnd))
                initFailed.add(wnd);
        }
        for (CoreWindow wnd : initFailed) {
            _windows.remove(wnd);
        }
        _isEmpty = _windows.isEmpty();

        if (waitfunc.size() == 0) {
            waitfunc.add(() -> glfw.WaitEventsTimeout(getCurrentFrequency()));
        }

        _isRunning = true;
        while (!_isEmpty) {
            List<CoreWindow> list = getStoredWindows();
            waitfunc.execute();
            for (CoreWindow window : list) {
                setContextCurrent(window);
                window.updateScene();
            }

            CoreWindow wnd = WindowsBox.getCurrentFocusedWindow();
            if (wnd != null && _initializedWindows.containsKey(wnd)) {
                setContextCurrent(wnd);
            }
            verifyToCloseWindows();
            verifyToInitWindows();
        }
    }

    private static void verifyToCloseWindows() {
        while (!_listWaitigForClose.isEmpty()) {
            CoreWindow wnd = _listWaitigForClose.pollFirst();
            _windows.remove(wnd);
            _initializedWindows.remove(wnd);
            _isEmpty = _windows.isEmpty();
            setContextCurrent(wnd);
            wnd.dispose();
            wnd.isClosed = true;
        }
    }

    private static void verifyToInitWindows() {
        while (!_listWaitigForInit.isEmpty()) {
            CoreWindow wnd = _listWaitigForInit.pollFirst();
            if (initWindow(wnd)) {
                _windows.add(wnd);
            }
        }
    }

    private static boolean initWindow(CoreWindow wnd) {
        if (!_initializedWindows.containsKey(wnd)) {
            if (wnd.initEngine()) {
                _initializedWindows.put(wnd, true);
                wnd.eventOnStart.execute();
                return true;
            }
        }
        return false;
    }

    /**
     * Launching the applications and showing all specified windows.
     * 
     * @param windows A sequence of any amount of CoreWindow instances.
     */
    public static void startWith(CoreWindow... windows) {
        for (CoreWindow wnd : windows) {
            addWindow(wnd);
        }
        run();
    }

    /**
     * Exiting the current application. All windows will be closed and all their
     * eventClose will be executed.
     */
    public static void appExit() {
        _lock.lock();
        try {
            _listWaitigForClose = new ArrayDeque<>(_windows);
        } finally {
            _lock.unlock();
        }
    }

    private static CoreWindow _currentContextedWindow = null;

    static void setContextCurrent(CoreWindow window) {
        _lock.lock();
        try {
            glfw.MakeContextCurrent(window.getGLWID());
            _currentContextedWindow = window;
        } finally {
            _lock.unlock();
        }
    }

    static CoreWindow getWindowContextCurrent() {
        _lock.lock();
        try {
            return _currentContextedWindow;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            _lock.unlock();
        }
    }
}