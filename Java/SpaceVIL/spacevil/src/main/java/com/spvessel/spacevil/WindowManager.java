package com.spvessel.spacevil;

import java.util.Map;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.spvessel.spacevil.Core.EventCommonMethod;
import com.spvessel.spacevil.Flags.RedrawFrequency;
import com.spvessel.spacevil.Flags.RenderType;

import static org.lwjgl.glfw.GLFW.*;

public final class WindowManager {

    private WindowManager() {
        waitfunc.add(() -> glfwWaitEventsTimeout(getCurrentFrequency()));
    }

    private static float _intervalVeryLow = 1.0f;
    private static float _intervalLow = 1.0f / 10.0f;
    private static float _intervalMedium = 1.0f / 30.0f;
    private static float _intervalHigh = 1.0f / 60.0f;
    private static float _intervalUltra = 1.0f / 120.0f;
    private static float _intervalAssigned = 1.0f / 10.0f;
    private static RedrawFrequency _frequency = RedrawFrequency.LOW;

    public static void setRenderFrequency(RedrawFrequency value) {
        _lock.lock();
        try {
            _frequency = value;
            if (value == RedrawFrequency.VERY_LOW) {
                _intervalAssigned = _intervalVeryLow;
            } else if (value == RedrawFrequency.LOW) {
                _intervalAssigned = _intervalLow;
            } else if (value == RedrawFrequency.MEDIUM) {
                _intervalAssigned = _intervalMedium;
            } else if (value == RedrawFrequency.HIGH) {
                _intervalAssigned = _intervalHigh;
            } else if (value == RedrawFrequency.ULTRA) {
                _intervalAssigned = _intervalUltra;
            }
        } catch (Exception ex) {
            System.out.println("Method - SetFrequency");
            ex.printStackTrace();
        } finally {
            _lock.unlock();
        }
    }

    private static int _vsync = 1;

    public static void enableVSync(int value) {
        if (_isRunning)
            return;
        _vsync = value;
    }

    public static int getVSyncValue() {
        return _vsync;
    }

    public static void setRenderType(RenderType value) {
        _lock.lock();
        try {
            waitfunc.clear();
            if (value == RenderType.IF_NEEDED) {
                waitfunc.add(() -> glfwWaitEvents());
            } else if (value == RenderType.PERIODIC) {
                waitfunc.add(() -> glfwWaitEventsTimeout(getCurrentFrequency()));
            } else if (value == RenderType.ALWAYS) {
                waitfunc.add(() -> glfwPollEvents());
            }

        } catch (Exception ex) {
            System.out.println("Method - setRenderType");
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

    public static RedrawFrequency getRenderFrequency() {
        _lock.lock();
        try {
            return _frequency;
        } catch (Exception ex) {
            System.out.println("Method - SetFrequency");
            ex.printStackTrace();
            _frequency = RedrawFrequency.LOW;
            return _frequency;
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
        } finally {
            _lock.unlock();
        }
    }

    public static void closeWindow(CoreWindow wnd) {
        _lock.lock();
        try {
            if (_initializedWindows.containsKey(wnd)) {
                _listWaitigForClose.add(wnd);
            }
        } catch (Exception e) {
        } finally {
            _lock.unlock();
        }
    }

    private static EventCommonMethod waitfunc = new EventCommonMethod();

    private static void run() {
        for (CoreWindow wnd : _windows) {
            initWindow(wnd);
        }
        if (waitfunc.size() == 0)
            waitfunc.add(() -> glfwWaitEventsTimeout(getCurrentFrequency()));

        _isRunning = true;
        while (!_isEmpty) {
            List<CoreWindow> list = getStoredWindows();
            waitfunc.execute();
            for (CoreWindow window : list) {
                glfwMakeContextCurrent(window.getGLWID());
                window.updateScene();
            }

            CoreWindow wnd = WindowsBox.getCurrentFocusedWindow();
            if (wnd != null && _initializedWindows.containsKey(wnd)) {
                glfwMakeContextCurrent(wnd.getGLWID());
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
            glfwMakeContextCurrent(wnd.getGLWID());
            wnd.dispose();
            wnd.isClosed = true;
        }
    }

    private static void verifyToInitWindows() {
        while (!_listWaitigForInit.isEmpty()) {
            CoreWindow wnd = _listWaitigForInit.pollFirst();
            initWindow(wnd);
            _windows.add(wnd);
        }
    }

    private static void initWindow(CoreWindow wnd) {
        if (!_initializedWindows.containsKey(wnd)) {
            if (wnd.initEngine()) {
                _initializedWindows.put(wnd, true);
                wnd.eventOnStart.execute();
            }
        }
    }

    public static void startWith(CoreWindow... windows) {
        for (CoreWindow wnd : windows) {
            addWindow(wnd);
        }
        run();
    }

    public static void appExit() {
        _lock.lock();
        try {
            _listWaitigForClose = new ArrayDeque<>(_windows);
        } finally {
            _lock.unlock();
        }
    }
}