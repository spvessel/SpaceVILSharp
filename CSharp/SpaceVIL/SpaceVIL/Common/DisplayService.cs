using SpaceVIL.Core;
using Glfw3;
using static Glfw3.Glfw;

namespace SpaceVIL.Common
{
    /// <summary>
    /// DisplayService is static class providing methods to getting display attributes such as size and dpi scale.
    /// </summary>
    public static class DisplayService
    {
        //sizes
        private static int _displayWidth = 0;
        private static int _displayHeight = 0;

        /// <summary>
        /// Getting the current display width.
        /// </summary>
        /// <returns>The current display width as System.Int32.</returns>
        public static int GetDisplayWidth()
        {
            return _displayWidth;
        }
        /// <summary>
        /// Getting the current display height.
        /// </summary>
        /// <returns>The current display height as System.Int32.</returns>
        public static int GetDisplayHeight()
        {
            return _displayHeight;
        }
        internal static void SetDisplaySize(int w, int h)
        {
            _displayWidth = w;
            _displayHeight = h;
        }

        //dpi
        static Scale _displayScale = new Scale();

        /// <summary>
        /// Getting the current display scale.
        /// </summary>
        /// <returns>The current display scale as SpaceVIL.Core.Scale.</returns>
        public static Scale GetDisplayDpiScale()
        {
            // Monitor monitor = Glfw.GetPrimaryMonitor();
            // float x, y;
            // Glfw.GetMonitorContentScale(monitor, out x, out y);
            // return new Scale(x, y);
            return new Scale(_displayScale.GetXScale(), _displayScale.GetYScale());
        }

        internal static void SetDisplayScale(float x, float y)
        {
            _displayScale.SetScale(x, y);
        }
        
        /// <summary>
        /// Getting the current window scale.
        /// </summary>
        /// <param name="window">A window as SpaceVIL.CoreWindow.</param>
        /// <returns>The current window scale as SpaceVIL.Core.Scale.</returns>
        public static Scale GetWindowDpiScale(CoreWindow window)
        {
            return window.GetDpiScale();
        }
    }
}
