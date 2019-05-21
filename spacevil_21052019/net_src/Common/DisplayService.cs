namespace SpaceVIL.Common
{
    public static class DisplayService
    {
        //sizes
        private static int _displayWidth = 0;
        private static int _displayHeight = 0;
        public static int GetDisplayWidth()
        {
            return _displayWidth;
        }
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
        private static float _dpi = 1.0f;
        public static float GetDisplayDpiScale()
        {
            return _dpi;
        }
        internal static void SetDisplayDpiScale(float scale)
        {
            _dpi = scale;
        }
    }
}
