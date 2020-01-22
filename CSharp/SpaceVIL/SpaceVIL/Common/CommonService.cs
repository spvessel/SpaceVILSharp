using System;
using Glfw3;
using SpaceVIL.Core;
using System.Threading;
using System.Runtime.InteropServices;
using System.ComponentModel;
using static Glfw3.Glfw;

namespace SpaceVIL.Common
{
    public static class CommonService
    {
        private static String _version = "0.3.1.4-ALPHA - February 2019";

#if STANDARD
    private static String _platform = "Standard";
#else
        private static String _platform = "Core";
#endif

        public static String GetSpaceVILInfo()
        {
            if (!_isOSSet)
                setOSType();

            return "SpaceVIL version: " + _version + "\n"
                + "Platform: .Net " + _platform + "\n"
                + "OS type: " + _osType + "\n";
        }

        private static String ClipboardTextStorage = String.Empty;
        internal static readonly object GlobalLocker = new object();

        private static SpaceVIL.Core.OSType _osType;

        public static SpaceVIL.Core.OSType GetOSType()
        {
            return _osType;
        }

        //cursors 
        internal static CursorImage CurrentCursor = null;
        internal static Glfw3.Glfw.Cursor CursorArrow;
        internal static Glfw3.Glfw.Cursor CursorInput;
        internal static Glfw3.Glfw.Cursor CursorHand;
        internal static Glfw3.Glfw.Cursor CursorResizeH;
        internal static Glfw3.Glfw.Cursor CursorResizeV;
        internal static Glfw3.Glfw.Cursor CursorResizeAll;

        private static bool _isOSSet = false;
        private static void setOSType()
        {
            if (RuntimeInformation.IsOSPlatform(OSPlatform.Windows))
            {
                _osType = SpaceVIL.Core.OSType.Windows;
                OpenGL.OpenGLWrapper.GL = new OpenGL.WindowsGL();
            }
            if (RuntimeInformation.IsOSPlatform(OSPlatform.Linux))
            {
                _osType = SpaceVIL.Core.OSType.Linux;
                OpenGL.OpenGLWrapper.GL = new OpenGL.UnixGL();
            }
            if (RuntimeInformation.IsOSPlatform(OSPlatform.OSX))
            {
                _osType = SpaceVIL.Core.OSType.Mac;
                OpenGL.OpenGLWrapper.GL = new OpenGL.MacGL();
            }
            _isOSSet = true;
        }

        public static bool InitSpaceVILComponents()
        {
            if (!_isOSSet)
                setOSType();

            if (GetOSType() == SpaceVIL.Core.OSType.Mac)
            {
                _controlRight = KeyCode.RightSuper;
                _controlLeft = KeyCode.LeftSuper;
                _controlMod = KeyMods.Super;
            }

            try
            {
                if (!Glfw.Init())
                {
                    Console.WriteLine("Init SpaceVIL framework failed. Abort.\nReason: Init GLFW failed.");
                    return false;
                }
            }
            catch
            {
                Console.WriteLine("Can not load/find library: " +
                ((GetOSType() == OSType.Linux || GetOSType() == OSType.Mac) ? "lib" : "") +
                "glfw" +
                ((GetOSType() == OSType.Linux) ? ".so" : ((GetOSType() == OSType.Mac) ? ".dylib" : ".dll")) +
                ". Failed to init GLFW. Abort function."
                );
                // System.Environment.Exit(-1);
                return false;
            }

            //cursors
            CursorArrow = Glfw.CreateStandardCursor(EmbeddedCursor.Arrow);
            CursorInput = Glfw.CreateStandardCursor(EmbeddedCursor.IBeam);
            CursorHand = Glfw.CreateStandardCursor(EmbeddedCursor.Hand);
            CursorResizeH = Glfw.CreateStandardCursor(EmbeddedCursor.ResizeX);
            CursorResizeV = Glfw.CreateStandardCursor(EmbeddedCursor.ResizeY);
            CursorResizeAll = Glfw.CreateStandardCursor(EmbeddedCursor.Crosshair);

            DisplayService.SetDisplaySize(Glfw.GetVideoMode(Glfw.GetPrimaryMonitor()).Width,
                        Glfw.GetVideoMode(Glfw.GetPrimaryMonitor()).Height);

            Glfw.Monitor monitor = Glfw.GetPrimaryMonitor();
            float x, y;
            Glfw.GetMonitorContentScale(monitor, out x, out y);
            DisplayService.SetDisplayScale(x, y);
            Console.WriteLine(DisplayService.GetDisplayDpiScale());

            DefaultsService.InitImages();
            DefaultsService.InitDefaultTheme();

            // RunCG();

            return true;
        }

        private static void RunCG()
        {
            Thread freeMemoryThread = new Thread(() =>
            {
                while (true)
                {
                    Thread.Sleep(5000);
                    Console.WriteLine("Run Garbage Collector");
                    System.GC.Collect();
                    GC.WaitForPendingFinalizers();
                }
            });
            freeMemoryThread.IsBackground = true;
            freeMemoryThread.Start();
        }

        public static String GetClipboardString()
        {
            CoreWindow window = WindowsBox.GetCurrentFocusedWindow();
            if (window == null)
                return "";
            Int64 id = window.GetGLWID();
            if (id == 0)
                return String.Empty;
            return Glfw.GetClipboardString(id);
        }

        public static void SetClipboardString(String text)
        {
            CoreWindow window = WindowsBox.GetCurrentFocusedWindow();
            if (window == null)
                return;
            Int64 id = window.GetGLWID();
            if (id == 0)
                return;
            Glfw.SetClipboardString(id, text);
        }

        private static KeyCode _controlRight = KeyCode.RightControl;
        private static KeyCode _controlLeft = KeyCode.LeftControl;
        private static KeyMods _controlMod = KeyMods.Control;

        public static KeyCode GetOsControlKeyRight()
        {
            return _controlRight;
        }

        public static KeyCode GetOsControlKeyLeft()
        {
            return _controlLeft;
        }

        public static KeyMods GetOsControlMod()
        {
            return _controlMod;
        }
    }
}
