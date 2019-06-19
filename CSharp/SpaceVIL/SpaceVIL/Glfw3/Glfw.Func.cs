namespace Glfw3
{
    using System;
    using System.Runtime.InteropServices;
    using System.Security;
    using SpaceVIL;
    using SpaceVIL.Core;

    internal static partial class Glfw
    {
        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwInit"), SuppressUnmanagedCodeSecurity]
        [return: MarshalAs(UnmanagedType.Bool)]
        internal static extern bool Init();


        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwTerminate"), SuppressUnmanagedCodeSecurity]
        internal static extern void Terminate();

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwGetVersion"), SuppressUnmanagedCodeSecurity]
        internal static extern void GetVersion(out int major, out int minor, out int rev);

        internal static unsafe string GetVersionString() => FromUTF8(glfwGetVersionString());

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        public static extern IntPtr glfwGetVersionString();

        internal static void SetErrorCallback(ErrorFunc callback) => glfwSetErrorCallback(Marshal.GetFunctionPointerForDelegate(callback));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern IntPtr glfwSetErrorCallback(IntPtr callback);

        internal static unsafe Monitor[] GetMonitors()
        {
            int count;
            var array = glfwGetMonitors(&count);

            if (count == 0)
                return null;

            var monitors = new Monitor[count];
            int size = Marshal.SizeOf(typeof(IntPtr));

            for (int i = 0; i < count; i++)
            {
                var ptr = Marshal.ReadIntPtr(array, i * size);
                monitors[i] = new Monitor(ptr);
            }

            return monitors;
        }

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern unsafe IntPtr glfwGetMonitors(int* count);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwGetPrimaryMonitor"), SuppressUnmanagedCodeSecurity]
        internal static extern Monitor GetPrimaryMonitor();

        internal static unsafe void GetMonitorPos(Monitor monitor, out int xpos, out int ypos)
        {
            int xx, yy;
            glfwGetMonitorPos(monitor.Ptr, &xx, &yy);
            xpos = xx; ypos = yy;
        }

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern unsafe void glfwGetMonitorPos(IntPtr monitor, int* xpos, int* ypos);

        internal static unsafe void GetMonitorPhysicalSize(Monitor monitor, out int widthMM, out int heightMM)
        {
            int ww, hh;
            glfwGetMonitorPhysicalSize(monitor.Ptr, &ww, &hh);
            widthMM = ww; heightMM = hh;
        }

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern unsafe void glfwGetMonitorPhysicalSize(IntPtr monitor, int* widthMM, int* heightMM);

        internal static unsafe string GetMonitorName(Monitor monitor) => FromUTF8(glfwGetMonitorName(monitor.Ptr));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern IntPtr glfwGetMonitorName(IntPtr monitor);

        internal static void SetMonitorCallback(MonitorFunc callback) => glfwSetMonitorCallback(Marshal.GetFunctionPointerForDelegate(callback));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern IntPtr glfwSetMonitorCallback(IntPtr callback);

        internal static unsafe VideoMode[] GetVideoModes(Monitor monitor)
        {
            int count;
            var array = glfwGetVideoModes(monitor.Ptr, &count);

            if (count == 0)
                return null;

            var result = new VideoMode[count];

            for (int i = 0; i < count; i++)
                result[i] = array[i];

            return result;
        }

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern unsafe VideoMode* glfwGetVideoModes(IntPtr monitor, int* count);

        internal static VideoMode GetVideoMode(Monitor monitor)
        {
            var ptr = glfwGetVideoMode(monitor.Ptr);
            return (VideoMode)Marshal.PtrToStructure(ptr, typeof(VideoMode));
        }

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern IntPtr glfwGetVideoMode(IntPtr monitor);

        internal static void SetGamma(Monitor monitor, float gamma) => glfwSetGamma(monitor.Ptr, gamma);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern void glfwSetGamma(IntPtr monitor, float gamma);

        internal static unsafe GammaRamp GetGammaRamp(Monitor monitor)
        {
            var internalRamp = glfwGetGammaRamp(monitor.Ptr);

            var ramp = new GammaRamp
            {
                Red = new ushort[internalRamp->Size],
                Green = new ushort[internalRamp->Size],
                Blue = new ushort[internalRamp->Size]
            };

            for (uint i = 0; i < ramp.Size; i++)
            {
                ramp.Red[i] = internalRamp->Red[i];
                ramp.Green[i] = internalRamp->Green[i];
                ramp.Blue[i] = internalRamp->Blue[i];
            }

            return ramp;
        }

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern unsafe InternalGammaRamp* glfwGetGammaRamp(IntPtr monitor);

        internal static unsafe void SetGammaRamp(Monitor monitor, GammaRamp ramp)
        {
            fixed (ushort* rampRed = ramp.Red, rampBlue = ramp.Blue, rampGreen = ramp.Green)
            {
                var internalRamp = new InternalGammaRamp
                {
                    Red = rampRed,
                    Blue = rampBlue,
                    Green = rampGreen,
                    Size = ramp.Size
                };

                glfwSetGammaRamp(monitor.Ptr, internalRamp);
            }
        }

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern void glfwSetGammaRamp(IntPtr monitor, InternalGammaRamp ramp);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwDefaultWindowHints"), SuppressUnmanagedCodeSecurity]
        internal static extern void DefaultWindowHints();

        internal static void WindowHint(Hint hint, bool value) => glfwWindowHint((int)hint, value ? 1 : 0);

        internal static void WindowHint(Hint hint, int value)
        {
            if (value < 0)
                value = DontCare;

            glfwWindowHint((int)hint, value);
        }

        internal static void WindowHint(Hint hint, Enum value) => glfwWindowHint((int)hint, Convert.ToInt32(value));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern void glfwWindowHint(int target, int hint);

        internal static Int64 CreateWindow(int width, int height, string title, Monitor? monitor = null, Int64 share = 0)
            => glfwCreateWindow(
                width,
                height,
                ToUTF8(title),
                monitor.HasValue ? monitor.Value.Ptr : IntPtr.Zero,
                share
               );

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern Int64 glfwCreateWindow(int width, int height, IntPtr title, IntPtr monitor, Int64 share);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwDestroyWindow"), SuppressUnmanagedCodeSecurity]
        internal static extern void DestroyWindow(Int64 window);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwWindowShouldClose"), SuppressUnmanagedCodeSecurity]
        [return: MarshalAs(UnmanagedType.Bool)]
        internal static extern bool WindowShouldClose(Int64 window);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwSetWindowShouldClose"), SuppressUnmanagedCodeSecurity]
        internal static extern void SetWindowShouldClose(Int64 window, [MarshalAs(UnmanagedType.Bool)] bool value);

        internal static void SetWindowTitle(Int64 window, string title) => glfwSetWindowTitle(window, ToUTF8(title));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern void glfwSetWindowTitle(Int64 window, IntPtr title);

        internal static void SetWindowIcon(Int64 window, Image image) => SetWindowIcon(window, new Image[] { image });

        internal static unsafe void SetWindowIcon(Int64 window, Image[] images)
        {
            if (images == null)
            {
                glfwSetWindowIcon(window, 0, IntPtr.Zero);
                return;
            }

            var imgs = new InternalImage[images.Length];

            for (int i = 0; i < imgs.Length; i++)
            {
                int size = images[i].Width * images[i].Width * 4;

                imgs[i] = new InternalImage
                {
                    Width = images[i].Width,
                    Height = images[i].Width,
                    Pixels = Marshal.AllocHGlobal(size)
                };

                Marshal.Copy(images[i].Pixels, 0, imgs[i].Pixels, Math.Min(size, images[i].Pixels.Length));
            }

            IntPtr ptr;
            fixed (InternalImage* array = imgs)
                ptr = new IntPtr((void*)array);

            glfwSetWindowIcon(window, images.Length, ptr);

            for (int i = 0; i < imgs.Length; i++)
                Marshal.FreeHGlobal(imgs[i].Pixels);
        }

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern void glfwSetWindowIcon(Int64 window, int count, IntPtr images);

        internal static unsafe void GetWindowPos(Int64 window, out int xpos, out int ypos)
        {
            int xx, yy;
            glfwGetWindowPos(window, &xx, &yy);
            xpos = xx; ypos = yy;
        }

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern unsafe void glfwGetWindowPos(Int64 window, int* xpos, int* ypos);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwSetWindowPos"), SuppressUnmanagedCodeSecurity]
        internal static extern void SetWindowPos(Int64 window, int xpos, int ypos);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwSetWindowOpacity"), SuppressUnmanagedCodeSecurity]
        internal static extern void SetWindowOpacity(Int64 window, float opacity);

        internal static unsafe void GetWindowSize(Int64 window, out int width, out int height)
        {
            int w, h;
            glfwGetWindowSize(window, &w, &h);
            width = w; height = h;
        }

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern unsafe void glfwGetWindowSize(Int64 window, int* width, int* height);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwSetWindowSizeLimits"), SuppressUnmanagedCodeSecurity]
        internal static extern void SetWindowSizeLimits(Int64 window, int minwidth, int minheight, int maxwidth, int maxheight);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwSetWindowAspectRatio"), SuppressUnmanagedCodeSecurity]
        internal static extern void SetWindowAspectRatio(Int64 window, int numer, int denom);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwSetWindowSize"), SuppressUnmanagedCodeSecurity]
        internal static extern void SetWindowSize(Int64 window, int width, int height);

        internal static unsafe void GetFramebufferSize(Int64 window, out int width, out int height)
        {
            int w, h;
            glfwGetFramebufferSize(window, &w, &h);
            width = w; height = h;
        }

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern unsafe void glfwGetFramebufferSize(Int64 window, int* width, int* height);

        internal static unsafe void GetWindowFrameSize(Int64 window, out int left, out int top, out int right, out int bottom)
        {
            int l, t, r, b;
            glfwGetWindowFrameSize(window, &l, &t, &r, &b);
            left = l; top = t; right = r; bottom = b;
        }

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern unsafe void glfwGetWindowFrameSize(Int64 window, int* left, int* top, int* right, int* bottom);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwIconifyWindow"), SuppressUnmanagedCodeSecurity]
        internal static extern void IconifyWindow(Int64 window);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwRestoreWindow"), SuppressUnmanagedCodeSecurity]
        internal static extern void RestoreWindow(Int64 window);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwMaximizeWindow"), SuppressUnmanagedCodeSecurity]
        internal static extern void MaximizeWindow(Int64 window);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwShowWindow"), SuppressUnmanagedCodeSecurity]
        internal static extern void ShowWindow(Int64 window);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwHideWindow"), SuppressUnmanagedCodeSecurity]
        internal static extern void HideWindow(Int64 window);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwFocusWindow"), SuppressUnmanagedCodeSecurity]
        internal static extern void FocusWindow(Int64 window);

        internal static Monitor GetWindowMonitor(Int64 window)
        {
            var ptr = glfwGetWindowMonitor(window);
            return new Monitor(ptr);
        }

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern IntPtr glfwGetWindowMonitor(Int64 window);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwSetWindowMonitor"), SuppressUnmanagedCodeSecurity]
        internal static extern void SetWindowMonitor(Int64 window, Monitor monitor, int xpos, int ypos, int width, int height, int refreshRate);

        internal static bool GetWindowAttrib(Int64 window, WindowAttrib attrib) => glfwGetWindowAttrib(window, Convert.ToInt32(attrib)) != 0;

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern int glfwGetWindowAttrib(Int64 window, int attrib);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwSetWindowUserPointer"), SuppressUnmanagedCodeSecurity]
        internal static extern void SetWindowUserPointer(Int64 window, IntPtr ptr);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwGetWindowUserPointer"), SuppressUnmanagedCodeSecurity]
        internal static extern IntPtr GetWindowUserPointer(Int64 window);

        internal static void SetWindowPosCallback(Int64 window, WindowPosFunc callback) => glfwSetWindowPosCallback(window, Marshal.GetFunctionPointerForDelegate(callback));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern IntPtr glfwSetWindowPosCallback(Int64 window, IntPtr callback);

        internal static void SetWindowSizeCallback(Int64 window, WindowSizeFunc callback) => glfwSetWindowSizeCallback(window, Marshal.GetFunctionPointerForDelegate(callback));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern IntPtr glfwSetWindowSizeCallback(Int64 window, IntPtr callback);

        internal static void SetWindowCloseCallback(Int64 window, WindowCloseFunc callback) => glfwSetWindowCloseCallback(window, Marshal.GetFunctionPointerForDelegate(callback));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern IntPtr glfwSetWindowCloseCallback(Int64 window, IntPtr callback);

        internal static void SetWindowRefreshCallback(Int64 window, WindowRefreshFunc callback) => glfwSetWindowRefreshCallback(window, Marshal.GetFunctionPointerForDelegate(callback));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern IntPtr glfwSetWindowRefreshCallback(Int64 window, IntPtr callback);

        internal static void SetWindowFocusCallback(Int64 window, WindowFocusFunc callback) => glfwSetWindowFocusCallback(window, Marshal.GetFunctionPointerForDelegate(callback));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern IntPtr glfwSetWindowFocusCallback(Int64 window, IntPtr callback);

        internal static void SetWindowIconifyCallback(Int64 window, WindowIconifyFunc callback) => glfwSetWindowIconifyCallback(window, Marshal.GetFunctionPointerForDelegate(callback));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern IntPtr glfwSetWindowIconifyCallback(Int64 window, IntPtr callback);

        internal static void SetFramebufferSizeCallback(Int64 window, FramebufferSizeFunc callback) => glfwSetFramebufferSizeCallback(window, Marshal.GetFunctionPointerForDelegate(callback));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern IntPtr glfwSetFramebufferSizeCallback(Int64 window, IntPtr callback);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwPollEvents"), SuppressUnmanagedCodeSecurity]
        internal static extern void PollEvents();

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwWaitEvents"), SuppressUnmanagedCodeSecurity]
        internal static extern void WaitEvents();

        /// <summary>
        /// <para>This function puts the calling thread to sleep until at least one event is
        /// available in the event queue, or until the specified timeout is reached. Once one or
        /// more events are available, it behaves exactly like <see cref="PollEvents"/>, i.e. the
        /// events in the queue are processed and the function then returns immediately. Processing
        /// events will cause the window and input callbacks associated with those events to be
        /// called.</para>
        /// <para>Since not all events are associated with callbacks, this function may return
        /// without a callback having been called even if you are monitoring all callbacks.</para>
        /// <para>On some platforms, a window move, resize or menu operation will cause event
        /// processing to block. This is due to how event processing is designed on those platforms.
        /// You can use the window refresh callback to redraw the contents of your window when
        /// necessary during such operations.</para>
        /// <para>On some platforms, certain callbacks may be called outside of a call to one of the
        /// event processing functions.</para>
        /// <para>If no windows exist, this function returns immediately. For synchronization of
        /// threads in applications that do not create windows, use your threading library of
        /// choice.</para>
        /// <para>Event processing is not required for joystick input to work.</para>
        /// </summary>
        /// <param name="timeout">The maximum amount of time, in seconds, to wait.</param>
        /// <seealso cref="PollEvents"/>
        /// <seealso cref="WaitEvents"/>
        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwWaitEventsTimeout"), SuppressUnmanagedCodeSecurity]
        internal static extern void WaitEventsTimeout(double timeout);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwPostEmptyEvent"), SuppressUnmanagedCodeSecurity]
        internal static extern void PostEmptyEvent();

        internal static int GetInputMode(Int64 window, InputMode mode) => glfwGetInputMode(window, Convert.ToInt32(mode));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern int glfwGetInputMode(Int64 window, int mode);

        internal static void SetInputMode(Int64 window, InputMode mode, CursorMode value) => glfwSetInputMode(window, Convert.ToInt32(mode), Convert.ToInt32(value));

        internal static void SetInputMode(Int64 window, InputMode mode, bool value) => glfwSetInputMode(window, Convert.ToInt32(mode), value ? 1 : 0);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern void glfwSetInputMode(Int64 window, int mode, int value);

        internal static unsafe string GetKeyName(KeyCode key, int scancode) => Marshal.PtrToStringAnsi(glfwGetKeyName(Convert.ToInt32(key), scancode));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern IntPtr glfwGetKeyName(int key, int scancode);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwGetKey"), SuppressUnmanagedCodeSecurity]
        [return: MarshalAs(UnmanagedType.Bool)]
        internal static extern bool GetKey(Int64 window, int key);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwGetMouseButton"), SuppressUnmanagedCodeSecurity]
        [return: MarshalAs(UnmanagedType.Bool)]
        internal static extern bool GetMouseButton(Int64 window, MouseButton button);

        internal static unsafe void GetCursorPos(Int64 window, out double xpos, out double ypos)
        {
            double xx, yy;
            glfwGetCursorPos(window, &xx, &yy);
            xpos = xx; ypos = yy;
        }

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern unsafe void glfwGetCursorPos(Int64 window, double* xpos, double* ypos);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwSetCursorPos"), SuppressUnmanagedCodeSecurity]
        internal static extern void SetCursorPos(Int64 window, double xpos, double ypos);

        internal static unsafe Cursor CreateCursor(Image image, int xhot, int yhot)
        {
            int size = image.Width * image.Width * 4;

            var img = new InternalImage
            {
                Width = image.Width,
                Height = image.Width,
                Pixels = Marshal.AllocHGlobal(size)
            };

            Marshal.Copy(image.Pixels, 0, img.Pixels, Math.Min(size, image.Pixels.Length));

            var ptr = new IntPtr(&img);
            ptr = glfwCreateCursor(ptr, xhot, yhot);

            Marshal.FreeHGlobal(img.Pixels);

            return new Cursor(ptr);
        }

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern IntPtr glfwCreateCursor(IntPtr image, int xhot, int yhot);


        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwCreateStandardCursor"), SuppressUnmanagedCodeSecurity]
        internal static extern Cursor CreateStandardCursor(EmbeddedCursor cursor);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwDestroyCursor"), SuppressUnmanagedCodeSecurity]
        internal static extern void DestroyCursor(Cursor cursor);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwSetCursor"), SuppressUnmanagedCodeSecurity]
        internal static extern void SetCursor(Int64 window, Cursor cursor);

        internal static void SetKeyCallback(Int64 window, KeyFunc callback) => glfwSetKeyCallback(window, Marshal.GetFunctionPointerForDelegate(callback));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern IntPtr glfwSetKeyCallback(Int64 window, IntPtr callback);

        internal static void SetCharCallback(Int64 window, CharFunc callback) => glfwSetCharCallback(window, Marshal.GetFunctionPointerForDelegate(callback));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern IntPtr glfwSetCharCallback(Int64 window, IntPtr callback);

        internal static void SetCharModsCallback(Int64 window, CharModsFunc callback) => glfwSetCharModsCallback(window, Marshal.GetFunctionPointerForDelegate(callback));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern IntPtr glfwSetCharModsCallback(Int64 window, IntPtr callback);

        internal static void SetMouseButtonCallback(Int64 window, MouseButtonFunc callback) => glfwSetMouseButtonCallback(window, Marshal.GetFunctionPointerForDelegate(callback));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern IntPtr glfwSetMouseButtonCallback(Int64 window, IntPtr callback);

        internal static void SetCursorPosCallback(Int64 window, CursorPosFunc callback) => glfwSetCursorPosCallback(window, Marshal.GetFunctionPointerForDelegate(callback));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern IntPtr glfwSetCursorPosCallback(Int64 window, IntPtr callback);

        internal static void SetCursorEnterCallback(Int64 window, CursorEnterFunc callback) => glfwSetCursorEnterCallback(window, Marshal.GetFunctionPointerForDelegate(callback));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern IntPtr glfwSetCursorEnterCallback(Int64 window, IntPtr callback);

        internal static void SetScrollCallback(Int64 window, ScrollFunc callback) => glfwSetScrollCallback(window, Marshal.GetFunctionPointerForDelegate(callback));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern IntPtr glfwSetScrollCallback(Int64 window, IntPtr callback);

        internal static void SetDropCallback(Int64 window, DropFunc callback) => glfwSetDropCallback(window, Marshal.GetFunctionPointerForDelegate(callback));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern IntPtr glfwSetDropCallback(Int64 window, IntPtr callback);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwJoystickPresent"), SuppressUnmanagedCodeSecurity]
        [return: MarshalAs(UnmanagedType.Bool)]
        internal static extern bool JoystickPresent(Joystick joy);

        internal static unsafe float[] GetJoystickAxes(Joystick joy)
        {
            int n;
            var array = glfwGetJoystickAxes((int)joy, &n);

            if (n == 0 || array == IntPtr.Zero)
                return null;

            var axes = new float[n];
            Marshal.Copy(array, axes, 0, n);
            return axes;
        }

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern unsafe IntPtr glfwGetJoystickAxes(int joy, int* count);

        internal static unsafe bool[] GetJoystickButtons(Joystick joy)
        {
            int n;
            var array = glfwGetJoystickButtons((int)joy, &n);

            if (n == 0 || array == IntPtr.Zero)
                return null;

            var b = new byte[n];
            Marshal.Copy(array, b, 0, n);

            var buttons = new bool[n];
            for (int i = 0; i < n; i++)
                buttons[i] = b[i] > 0;

            return buttons;
        }

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern unsafe IntPtr glfwGetJoystickButtons(int joy, int* count);

        internal static string GetJoystickName(Joystick joy) => FromUTF8(glfwGetJoystickName((int)joy));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern unsafe IntPtr glfwGetJoystickName(int joy);

        internal static void SetJoystickCallback(JoystickFunc callback) => glfwSetJoystickCallback(Marshal.GetFunctionPointerForDelegate(callback));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern IntPtr glfwSetJoystickCallback(IntPtr callback);

        internal static void SetClipboardString(Int64 window, string value) => glfwSetClipboardString(window, ToUTF8(value));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern void glfwSetClipboardString(Int64 window, IntPtr value);

        internal static string GetClipboardString(Int64 window) => FromUTF8(glfwGetClipboardString(window));

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl), SuppressUnmanagedCodeSecurity]
        static extern unsafe IntPtr glfwGetClipboardString(Int64 window);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwGetTime"), SuppressUnmanagedCodeSecurity]
        internal static extern double GetTime();

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwSetTime"), SuppressUnmanagedCodeSecurity]
        internal static extern void SetTime(double time);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwGetTimerValue"), SuppressUnmanagedCodeSecurity]
        internal static extern ulong GetTimerValue();

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwGetTimerFrequency"), SuppressUnmanagedCodeSecurity]
        internal static extern ulong GetTimerFrequency();

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwMakeContextCurrent"), SuppressUnmanagedCodeSecurity]
        internal static extern void MakeContextCurrent(Int64 window);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwGetCurrentContext"), SuppressUnmanagedCodeSecurity]
        internal static extern Int64 GetCurrentContext();

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwSwapBuffers"), SuppressUnmanagedCodeSecurity]
        internal static extern void SwapBuffers(Int64 window);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwSwapInterval"), SuppressUnmanagedCodeSecurity]
        internal static extern void SwapInterval(int interval);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, CharSet = CharSet.Ansi, EntryPoint = "glfwExtensionSupported"), SuppressUnmanagedCodeSecurity]
        [return: MarshalAs(UnmanagedType.Bool)]
        internal static extern bool ExtensionSupported(string extension);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, CharSet = CharSet.Ansi, EntryPoint = "glfwGetProcAddress"), SuppressUnmanagedCodeSecurity]
        internal static extern IntPtr GetProcAddress(string procname);

        [DllImport(kLibrary, CallingConvention = CallingConvention.Cdecl, EntryPoint = "glfwVulkanSupported"), SuppressUnmanagedCodeSecurity]
        [return: MarshalAs(UnmanagedType.Bool)]
        internal static extern bool VulkanSupported();
    }
}
