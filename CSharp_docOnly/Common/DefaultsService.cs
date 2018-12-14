using System.Drawing;
using System;
using System.Collections.Generic;
using System.Drawing.Text;
using System.Reflection;
using System.Runtime.InteropServices;
using System.IO;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL.Common
{
    public static class DefaultsService
    {
        private static ThemeStyle _default_theme; // = ThemeStyle.GetInstance();
        private static DefaultFont _default_font; // = ThemeStyle.GetInstance();

        static DefaultsService()
        {
            _default_font = DefaultFont.GetInstance();
            // _default_theme = ThemeStyle.GetInstance();
        }

        public static ThemeStyle GetDefaultTheme()
        {
            if (_default_theme == null)
                _default_theme = new ThemeStyle();
            return _default_theme;
        }
        public static void SetDefaultTheme(ThemeStyle theme)
        {
            _default_theme = theme;
        }
        public static Style GetDefaultStyle(Type type)
        {
            if (_default_theme == null)
                _default_theme = new ThemeStyle();
            return _default_theme.GetThemeStyle(type);
        }
        public static Font GetDefaultFont()
        {
            return _default_font.GetDefaultFont();
        }
        public static Font GetDefaultFont(int size)
        {
            return _default_font.GetDefaultFont(size);
        }
        // public static void SetDefaultEmbeddedFont(EmbeddedFont font)
        // {
        //     _default_font.SetDefaultEmbeddedFont(font);
        // }
        // public static void SetDefaultFont(String font_path)
        // {
        //     _default_font.SetDefaultFont(font_path);
        // }
        public static void SetDefaultFont(Font font)
        {
            _default_font.SetDefaultFont(font);
        }
        // public static void SetDefaultFont(Stream font_stream)
        // {
        //     _default_font.SetDefaultFont(font_stream);
        // }
        // public static Font GetEmbeddedFont(EmbeddedFont font, int size, FontStyle style)
        // {
        //     return _default_font.GetEmbeddedFont(font, size, style);
        // }
    }
}