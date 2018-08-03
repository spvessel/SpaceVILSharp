using System.Drawing;
using System;
using System.Collections.Generic;

namespace SpaceVIL
{
    public static class DefaultsService
    {
        public static Style DefaultStyle = new Style();

        public static Color GetDefaultBackground()
        {
            return DefaultStyle.Background;
        }
        public static Color GetDefaultForeground()
        {
            return DefaultStyle.Foreground;
        }
    }
}