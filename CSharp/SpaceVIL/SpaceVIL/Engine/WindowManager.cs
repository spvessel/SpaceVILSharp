using System;
using System.Collections.Generic;
using System.Threading;
using SpaceVIL.Core;
using Glfw3;

namespace SpaceVIL
{
    /// <summary>
    /// WindowManager is a static class that is designed to manage instances of a window and entire application. 
    /// Provides control for changing render frequency, render type, vertical sync, adding/closing windows, exiting the app and more.
    /// </summary>
    public static class WindowManager
    {
        private static Object _lock = new Object();
        private static readonly float _intervalVeryLow = 1.0f;
        private static readonly float _intervalLow = 1.0f / 10.0f;
        private static readonly float _intervalMedium = 1.0f / 30.0f;
        private static readonly float _intervalHigh = 1.0f / 60.0f;
        private static readonly float _intervalUltra = 1.0f / 120.0f;
        private static float _intervalAssigned = 1.0f / 10.0f;
        private static RedrawFrequency _frequency = RedrawFrequency.Low;

        /// <summary>
        /// Setting the frequency of redrawing scene in idle state. 
        /// The higher the level, the more computer resources are used. 
        /// Default: SpaceVIL.Core.RedrawFrequency.Low
        /// <para/>Can be: 
        /// <para/>VeryLow - 1 frame per second, 
        /// <para/>Low - up to 10 frames per second, 
        /// <para/>Medium - up to 30 frames per second,
        /// <para/>High - up to 60 frames per second,
        /// <para/>Ultra - up to 120 frames per second,
        /// </summary>
        /// <param name="level">A frequency level as SpaceVIL.Core.RedrawFrequency</param>
        public static void SetRenderFrequency(RedrawFrequency level)
        {
            Monitor.Enter(_lock);
            try
            {
                _frequency = level;
                if (level == RedrawFrequency.VeryLow)
                {
                    _intervalAssigned = _intervalVeryLow;
                }
                if (level == RedrawFrequency.Low)
                {
                    _intervalAssigned = _intervalLow;
                }
                else if (level == RedrawFrequency.Medium)
                {
                    _intervalAssigned = _intervalMedium;
                }
                else if (level == RedrawFrequency.High)
                {
                    _intervalAssigned = _intervalHigh;
                }
                else if (level == RedrawFrequency.Ultra)
                {
                    _intervalAssigned = _intervalUltra;
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Method - SetFrequency");
                Console.WriteLine(ex.StackTrace);
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }
        
        private static float GetCurrentFrequency()
        {
            Monitor.Enter(_lock);
            try
            {
                return _intervalAssigned;
            }
            catch (Exception ex)
            {
                Console.WriteLine("Method - GetCurrentFrequency");
                Console.WriteLine(ex.StackTrace);
                return _intervalLow;
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }

        /// <summary>
        /// Getting the current render frequency.
        /// </summary>
        /// <returns>The current render frequency as SpaceVIL.Core.RedrawFrequency.</returns>
        public static RedrawFrequency GetRenderFrequency()
        {
            Monitor.Enter(_lock);
            try
            {
                return _frequency;
            }
            catch (Exception ex)
            {
                Console.WriteLine("Method - SetFrequency");
                Console.WriteLine(ex.StackTrace);
                _frequency = RedrawFrequency.Low;
                return _frequency;
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }

        private static int _vsync = 1;

        /// <summary>
        /// Setting the vsync value. If value is 0 - vsync is OFF, if other value - vsync is ON. 
        /// The total amount of FPS calculated by the formula: 1.0 / Math.Abs(value) * DisplayRefreshRate, 
        /// so if value is 2 (or -2) and dysplay refresh rate is 60 then 1.0 / 2 * 60 = 30 fps.
        /// Default: 1 - ENABLE.
        /// </summary>
        /// <param name="value">Value of vsync.</param>
        public static void EnableVSync(int value)
        {
            if (_isRunning)
            {
                return;
            }
            _vsync = value;
        }

        /// <summary>
        /// Getting the current vsync value. If value is 0 - vsync is OFF, if other value - vsync is ON. 
        /// The total amount of FPS calculated by the formula: 1.0 / Math.Abs(value) * DisplayRefreshRate, 
        /// so if value is 2 (or -2) and dysplay refresh rate is 60 then 1.0 / 2 * 60 = 30 fps.
        /// Default: 1 - ENABLE.
        /// </summary>
        /// <returns>The current vsync value</returns>
        public static int GetVSyncValue()
        {
            return _vsync;
        }

        /// <summary>
        /// Setting the common render type. Default: SpaceVIL.Core.RenderType.Periodic.
        /// <para/>Can be:
        /// <para/>IfNeeded - the scene is redrawn only if any input event occurs (mouse move, mouse click, 
        /// keyboard key press, window resizing and etc.), 
        /// <para/>Periodic - the scene is redrawn according to the current render frequency type 
        /// (See SetRenderFrequency(type)) in idle and every time when any input event occurs, 
        /// <para/>Always - the scene is constantly being redrawn.
        /// </summary>
        /// <param name="type">A render type as SpaceVIL.Core.RenderType.</param>
        public static void SetRenderType(RenderType type)
        {
            Monitor.Enter(_lock);
            try
            {
                waitfunc = null;
                if (type == RenderType.IfNeeded)
                {
                    waitfunc += () => Glfw.WaitEvents();
                }
                else if (type == RenderType.Periodic)
                {
                    waitfunc += () => Glfw.WaitEventsTimeout(GetCurrentFrequency());
                }
                else if (type == RenderType.Always)
                {
                    waitfunc += () => Glfw.PollEvents();
                }

            }
            catch (Exception ex)
            {
                Console.WriteLine("Method - SetRenderType");
                Console.WriteLine(ex.StackTrace);
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }

        private static Dictionary<CoreWindow, bool> _initializedWindows = new Dictionary<CoreWindow, bool>();
        private static List<CoreWindow> _windows = new List<CoreWindow>();
        private static Queue<CoreWindow> _listWaitigForInit = new Queue<CoreWindow>();
        private static Queue<CoreWindow> _listWaitigForClose = new Queue<CoreWindow>();
        private static bool _isRunning = false;

        internal static bool IsRunning()
        {
            return _isRunning;
        }

        private static bool _isEmpty = true;

        /// <summary>
        /// Adding a window to rendering queue. After adding the window shows up immediately.
        /// </summary>
        /// <param name="wnd">Any CoreWindow instance.</param>
        public static void AddWindow(CoreWindow wnd)
        {
            Monitor.Enter(_lock);
            try
            {
                if (!_windows.Contains(wnd))
                {
                    if (_isRunning)
                    {
                        _listWaitigForInit.Enqueue(wnd);
                    }
                    else
                    {
                        _windows.Add(wnd);
                        _isEmpty = (_windows.Count == 0);
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }

        private static List<CoreWindow> GetStoredWindows()
        {
            Monitor.Enter(_lock);
            try
            {
                return new List<CoreWindow>(_windows);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
                return new List<CoreWindow>();
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }

        /// <summary>
        /// Closing the specified window if it exist in render queue.
        /// </summary>
        /// <param name="wnd">Any CoreWindow instance.</param>
        public static void CloseWindow(CoreWindow wnd)
        {
            Monitor.Enter(_lock);
            try
            {
                if (_initializedWindows.ContainsKey(wnd))
                {
                    _listWaitigForClose.Enqueue(wnd);
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }

        private static EventCommonMethod waitfunc;

        private static void Run()
        {
            foreach (CoreWindow wnd in _windows)
            {
                InitWindow(wnd);
            }

            if (waitfunc == null)
            {
                waitfunc += () => Glfw.WaitEventsTimeout(GetCurrentFrequency());
            }

            _isRunning = true;
            while (!_isEmpty)
            {
                List<CoreWindow> list = GetStoredWindows();
                waitfunc.Invoke();
                foreach (CoreWindow window in list)
                {
                    SetContextCurrent(window);
                    window.UpdateScene();
                }
                CoreWindow wnd = WindowsBox.GetCurrentFocusedWindow();
                if (wnd != null && _initializedWindows.ContainsKey(wnd))
                {
                    SetContextCurrent(wnd);
                }
                VerifyToCloseWindows();
                VerifyToInitWindows();
            }
        }

        private static void VerifyToCloseWindows()
        {
            while (_listWaitigForClose.Count > 0)
            {
                CoreWindow wnd = _listWaitigForClose.Dequeue();
                _windows.Remove(wnd);
                _initializedWindows.Remove(wnd);
                _isEmpty = (_windows.Count == 0);
                SetContextCurrent(wnd);
                wnd.Dispose();
                wnd.IsClosed = true;
            }
        }

        private static void VerifyToInitWindows()
        {
            while (_listWaitigForInit.Count > 0)
            {
                CoreWindow wnd = _listWaitigForInit.Dequeue();
                InitWindow(wnd);
                _windows.Add(wnd);
            }
        }

        private static void InitWindow(CoreWindow wnd)
        {
            if (!_initializedWindows.ContainsKey(wnd))
            {
                if (wnd.InitEngine())
                {
                    _initializedWindows.Add(wnd, true);
                    wnd.EventOnStart?.Invoke();
                }
            }
        }

        /// <summary>
        /// Launching the applications and showing all specified windows.
        /// </summary>
        /// <param name="windows">A sequence of any amount of CoreWindow instances.</param>
        public static void StartWith(params CoreWindow[] windows)
        {
            foreach (var wnd in windows)
            {
                AddWindow(wnd);
            }
            Run();
        }

        /// <summary>
        /// Exiting the current application. All windows will be closed and all their EventClose will be executed.
        /// </summary>
        public static void AppExit()
        {
            Monitor.Enter(_lock);
            try
            {
                _listWaitigForClose = new Queue<CoreWindow>(_windows);
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }

        private static CoreWindow _currentContextedWindow = null;
        
        internal static void SetContextCurrent(CoreWindow window)
        {
            Monitor.Enter(_lock);
            try
            {
                Glfw.MakeContextCurrent(window.GetGLWID());
                _currentContextedWindow = window;
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }

        internal static CoreWindow GetWindowContextCurrent()
        {
            Monitor.Enter(_lock);
            try
            {
                return _currentContextedWindow;
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
                return null;
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }
    }
}