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
    internal class DefaultFont
    {
        private static DefaultFont _instance;
        private DefaultFont() { }
        public static DefaultFont GetInstance()
        {
            if (_instance == null)
                _instance = new DefaultFont();
            return _instance;
        }

        private PrivateFontCollection privateFontCollection = null;
        private Font _default_font;
        public void SetDefaultFont(Font font)
        {
            _default_font = font;
        }
        // public void SetDefaultFont(String font_path)
        // {
        //     Stream fontStream = Assembly.GetExecutingAssembly().GetManifestResourceStream(font_path);
        //     SetDefaultFont(fontStream);
        // }
        // public void SetDefaultFont(Stream font_stream)
        // {
        //     privateFontCollection = new PrivateFontCollection();

        //     System.IntPtr data = Marshal.AllocCoTaskMem((int)font_stream.Length);
        //     byte[] fontdata = new byte[font_stream.Length];
        //     font_stream.Read(fontdata, 0, (int)font_stream.Length);
        //     Marshal.Copy(fontdata, 0, data, (int)font_stream.Length);
        //     privateFontCollection.AddMemoryFont(data, (int)font_stream.Length);
        //     font_stream.Close();
        //     Marshal.FreeCoTaskMem(data);
        //     _default_font = new Font(privateFontCollection.Families[0], 10, FontStyle.Regular);
        // }
        // public Font GetDefaultFont() //объекты не инициализируются почему-то, выяснить
        // {
        //     if (_default_font == null)
        //     {
        //         AddFontFromMemory();
        //         _default_font = new Font(privateFontCollection.Families[0], 10, FontStyle.Regular);
        //     }
        //     return new Font(_default_font.FontFamily, 10, _default_font.Style);
        //     // return new Font(new FontFamily("Ubuntu"), 10, FontStyle.Regular);
        // }
        public Font GetDefaultFont(int size = 12) //объекты не инициализируются почему-то, выяснить
        {
            // Console.WriteLine(size);
            if (size == 0)
            {
                size = 12;
            }
            if (_default_font == null)
            {
                AddFontFromMemory();
                _default_font = new Font(privateFontCollection.Families[0], 12, FontStyle.Regular);
            }
            return new Font(_default_font.FontFamily, size, _default_font.Style);
            // return new Font(new FontFamily("Ubuntu"), size, FontStyle.Regular);
        }
        public Font GetDefaultFont(FontStyle style, int size)
        {
            // Console.WriteLine(size);
            if (size == 0)
            {
                size = 10;
            }
            if (_default_font == null)
            {
                AddFontFromMemory();
                _default_font = new Font(privateFontCollection.Families[0], size, style);
            }
            return new Font(_default_font.FontFamily, size, style);
        }
        // public Font GetEmbeddedFont(EmbeddedFont font, int size, FontStyle style)
        // {
        //     PrivateFontCollection tmp = new PrivateFontCollection();
        //     Stream fontStream = Assembly.GetExecutingAssembly().GetManifestResourceStream(GetPathEmbeddedFont(font));
        //     System.IntPtr data = Marshal.AllocCoTaskMem((int)fontStream.Length);
        //     byte[] fontdata = new byte[fontStream.Length];
        //     fontStream.Read(fontdata, 0, (int)fontStream.Length);
        //     Marshal.Copy(fontdata, 0, data, (int)fontStream.Length);
        //     tmp.AddMemoryFont(data, (int)fontStream.Length);
        //     fontStream.Close();
        //     Marshal.FreeCoTaskMem(data);
        //     return new Font(tmp.Families[0], size, style);
        // }
        private void AddFontFromMemory()
        {
            if (_embedded_font == null || _embedded_font == String.Empty)
                SetDefaultEmbeddedFont(EmbeddedFont.Ubuntu);

            privateFontCollection = new PrivateFontCollection();

            Stream fontStream = Assembly.GetExecutingAssembly().GetManifestResourceStream(_embedded_font);
            System.IntPtr data = Marshal.AllocCoTaskMem((int)fontStream.Length);
            byte[] fontdata = new byte[fontStream.Length];
            fontStream.Read(fontdata, 0, (int)fontStream.Length);
            Marshal.Copy(fontdata, 0, data, (int)fontStream.Length);
            privateFontCollection.AddMemoryFont(data, (int)fontStream.Length);
            fontStream.Close();
            Marshal.FreeCoTaskMem(data);
        }
        private String _embedded_font = null;
        public void SetDefaultEmbeddedFont(EmbeddedFont font)
        {
            switch (font)
            {
                case EmbeddedFont.Ubuntu:
                    _embedded_font = "SpaceVIL.Fonts.Ubuntu-Regular.ttf";
                    break;

                default:
                    _embedded_font = "SpaceVIL.Fonts.Ubuntu-Regular.ttf";
                    break;
            }
        }
        // private String GetPathEmbeddedFont(EmbeddedFont font)
        // {
        //     String path;
        //     switch (font)
        //     {
        //         case EmbeddedFont.Ubuntu:
        //             path = "SpaceVIL.Fonts.Ubuntu-Regular.ttf";
        //             break;

        //         default:
        //             path = "SpaceVIL.Fonts.Ubuntu-Regular.ttf";
        //             break;
        //     }
        //     return path;
        // }
    }

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
        public static Font GetDefaultFont(FontStyle style, int size)
        {
            return _default_font.GetDefaultFont(style, size);
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