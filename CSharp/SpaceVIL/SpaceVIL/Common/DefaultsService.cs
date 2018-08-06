using System.Drawing;
using System;
using System.Collections.Generic;

namespace SpaceVIL
{
    public static class DefaultsService
    {
        private static ThemeStyle DefaultTheme = new ThemeStyle();
        public static void SetDefaultTheme(ThemeStyle theme)
        {
            DefaultTheme = theme;
        }
    }
}