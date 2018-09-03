using System.Drawing;
using System;
using System.Collections.Generic;

namespace SpaceVIL
{
    public static class DefaultsService
    {
        private static ThemeStyle DefaultTheme = ThemeStyle.GetInstance();

        public static void SetDefaultTheme(ThemeStyle theme)
        {
            DefaultTheme = theme;
        }
        public static Style GetDefaultStyle(Type type)
        {
            return DefaultTheme.GetThemeStyle(type);
        }
    }
}