using System;
using Glfw3;
using SpaceVIL.Core;
using System.Threading;
using System.Runtime.InteropServices;
using SpaceVIL.Decorations;

namespace SpaceVIL.Common
{
    /// <summary>
    /// CommonService is static class providing methods to getting basic information 
    /// about the SpaceVIL framework, OS attributes, 
    /// initializing the SpaceVIL framework and working with clipboard.
    /// </summary>
    public static class CommonService
    {
        private static String _version = "0.3.1.4-ALPHA - February 2019";

#if STANDARD
    private static String _platform = "Standard";
#else
        private static String _platform = "Core";
#endif

        /// <summary>
        /// Getting basic information about SpaceVIL such as version, date, platform and OS.
        /// </summary>
        /// <returns>The information as System.String.</returns>
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

        /// <summary>
        /// Getting the current OS type.
        /// </summary>
        /// <returns>The OS type as SpaceVIL.Core.OSType.</returns>
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

        /// <summary>
        /// Initializing the mandatory SpaceVIL common components (GLFW, default values and etc.).
        /// </summary>
        /// <returns>True: if initialization is successful. False: if initialization is unsuccessful.</returns>
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

            // Console.WriteLine(DisplayService.GetDisplayDpiScale());

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

        /// <summary>
        /// Getting stored text in clipboard.
        /// </summary>
        /// <returns>The text as System.String.</returns>
        public static String GetClipboardString()
        {
            CoreWindow window = WindowsBox.GetCurrentFocusedWindow();
            if (window == null)
            {
                return "";
            }
            Int64 id = window.GetGLWID();
            if (id == 0)
            {
                return String.Empty;
            }
            return Glfw.GetClipboardString(id);
        }

        /// <summary>
        /// Placing the specified text to the clipboard.
        /// </summary>
        /// <param name="text">The text as System.String.</param>
        public static void SetClipboardString(String text)
        {
            CoreWindow window = WindowsBox.GetCurrentFocusedWindow();
            if (window == null)
            {
                return;
            }
            Int64 id = window.GetGLWID();
            if (id == 0)
            {
                return;
            }
            Glfw.SetClipboardString(id, text);
        }

        private static KeyCode _controlRight = KeyCode.RightControl;
        private static KeyCode _controlLeft = KeyCode.LeftControl;
        private static KeyMods _controlMod = KeyMods.Control;

        /// <summary>
        /// Getting the default right "control" key KeyCode of current OS (for Mac OS - Command key)
        /// </summary>
        /// <returns>The keyboard key as SpaceVIL.Core.KeyCode</returns>
        public static KeyCode GetOsControlKeyRight()
        {
            return _controlRight;
        }
        /// <summary>
        /// Getting the default left "control" key KeyCode of current OS (for Mac OS - Command key)
        /// </summary>
        /// <returns>The keyboard key as SpaceVIL.Core.KeyCode</returns>
        public static KeyCode GetOsControlKeyLeft()
        {
            return _controlLeft;
        }
        /// <summary>
        /// Getting the default "control" modifier KeyCode of current OS (for Mac OS - Command key)
        /// </summary>
        /// <returns>The keyboard modifier as SpaceVIL.Core.KeyMods</returns>
        public static KeyMods GetOsControlMod()
        {
            return _controlMod;
        }
    }
}
