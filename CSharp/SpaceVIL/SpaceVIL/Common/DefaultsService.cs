using System.Drawing;
using System;
using System.Collections.Generic;

namespace SpaceVIL
{
    public static class DefaultsService
    {
        private static ThemeStyle _default_theme = ThemeStyle.GetInstance();
        private static Font _default_font;// = new Font(new FontFamily("Courier New"), 14, FontStyle.Regular);
        // private static string _font_family = "Courier New";
        // private static float _font_size = 14;
        // private static FontStyle _font_style = FontStyle.Regular;

        static DefaultsService()
        {
            // _default_font = new Font(new FontFamily("Courier New"), 14, FontStyle.Regular);
            // _font_family = "Courier New";
            // _font_size = 14;
            // _font_style = FontStyle.Regular;
        }

        public static void SetDefaultTheme(ThemeStyle theme)
        {
            _default_theme = theme;
        }
        public static Style GetDefaultStyle(Type type)
        {
            return _default_theme.GetThemeStyle(type);
        }
        public static void SetDefaultFont(Font font)
        {
            _default_font = font;
        }
        public static Font GetDefaultFont()//объекты не инициализируются почему-то, выяснить
        {
            if (_default_font == null)
                _default_font = new Font(new FontFamily("Arial"), 14, FontStyle.Regular);

            return _default_font;
        }
    }
}