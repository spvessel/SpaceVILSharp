using System.Drawing;
using System;
using System.Collections.Generic;
using System.Drawing.Text;
using System.Reflection;
using System.Runtime.InteropServices;
using System.IO;

namespace SpaceVIL
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
        public void SetDefaultFont(String font_path)
        {
            Stream fontStream = Assembly.GetExecutingAssembly().GetManifestResourceStream(font_path);
            SetDefaultFont(fontStream);
        }
        public void SetDefaultFont(Stream font_stream)
        {
            privateFontCollection = new PrivateFontCollection();

            System.IntPtr data = Marshal.AllocCoTaskMem((int)font_stream.Length);
            byte[] fontdata = new byte[font_stream.Length];
            font_stream.Read(fontdata, 0, (int)font_stream.Length);
            Marshal.Copy(fontdata, 0, data, (int)font_stream.Length);
            privateFontCollection.AddMemoryFont(data, (int)font_stream.Length);
            font_stream.Close();
            Marshal.FreeCoTaskMem(data);
            _default_font = new Font(privateFontCollection.Families[0], 13, FontStyle.Regular);
        }
        public Font GetDefaultFont() //объекты не инициализируются почему-то, выяснить
        {
            if (_default_font == null)
            {
                AddFontFromMemory();
                _default_font = new Font(privateFontCollection.Families[0], 12, FontStyle.Regular);
            }
            return new Font(_default_font.FontFamily, _default_font.Size, _default_font.Style);
        }
        public Font GetEmbeddedFont(EmbeddedFont font, int size, FontStyle style)
        {
            PrivateFontCollection tmp = new PrivateFontCollection();
            Stream fontStream = Assembly.GetExecutingAssembly().GetManifestResourceStream(GetPathEmbeddedFont(font));
            System.IntPtr data = Marshal.AllocCoTaskMem((int)fontStream.Length);
            byte[] fontdata = new byte[fontStream.Length];
            fontStream.Read(fontdata, 0, (int)fontStream.Length);
            Marshal.Copy(fontdata, 0, data, (int)fontStream.Length);
            tmp.AddMemoryFont(data, (int)fontStream.Length);
            fontStream.Close();
            Marshal.FreeCoTaskMem(data);
            return new Font(tmp.Families[0], size, style);
        }
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
                case EmbeddedFont.Verdana:
                    _embedded_font = "SpaceVIL.Fonts.Verdana.ttf";
                    break;
                case EmbeddedFont.Exo2:
                    _embedded_font = "SpaceVIL.Fonts.Exo2-Regular.ttf";
                    break;
                case EmbeddedFont.TitilliumWeb:
                    _embedded_font = "SpaceVIL.Fonts.TitilliumWeb-Regular.ttf";
                    break;
                case EmbeddedFont.OpenSans:
                    _embedded_font = "SpaceVIL.Fonts.OpenSans-Regular.ttf";
                    break;
                case EmbeddedFont.SansLight:
                    _embedded_font = "SpaceVIL.Fonts.Sans-Light.ttf";
                    break;
                case EmbeddedFont.Muli:
                    _embedded_font = "SpaceVIL.Fonts.Muli-Regular.ttf";
                    break;
                case EmbeddedFont.Nunito:
                    _embedded_font = "SpaceVIL.Fonts.Nunito-Regular.ttf";
                    break;
                case EmbeddedFont.Quicksand:
                    _embedded_font = "SpaceVIL.Fonts.Quicksand-Regular.ttf";
                    break;
                case EmbeddedFont.Ubuntu:
                    _embedded_font = "SpaceVIL.Fonts.Ubuntu-Regular.ttf";
                    break;
                case EmbeddedFont.GlacialIndifference:
                    _embedded_font = "SpaceVIL.Fonts.GlacialIndifference-Regular.otf";
                    break;
                case EmbeddedFont.RobotoMono:
                    _embedded_font = "SpaceVIL.Fonts.RobotoMono-Regular.ttf";
                    break;
                case EmbeddedFont.OpenGostTypeA:
                    _embedded_font = "SpaceVIL.Fonts.OpenGostTypeA-Regular.ttf";
                    break;

                default:
                    _embedded_font = "SpaceVIL.Fonts.Verdana.ttf";
                    break;
            }
        }
        private String GetPathEmbeddedFont(EmbeddedFont font)
        {
            String path;
            switch (font)
            {
                case EmbeddedFont.Verdana:
                    path = "SpaceVIL.Fonts.Verdana.ttf";
                    break;
                case EmbeddedFont.Exo2:
                    path = "SpaceVIL.Fonts.Exo2-Regular.ttf";
                    break;
                case EmbeddedFont.TitilliumWeb:
                    path = "SpaceVIL.Fonts.TitilliumWeb-Regular.ttf";
                    break;
                case EmbeddedFont.OpenSans:
                    path = "SpaceVIL.Fonts.OpenSans-Regular.ttf";
                    break;
                case EmbeddedFont.SansLight:
                    path = "SpaceVIL.Fonts.Sans-Light.ttf";
                    break;
                case EmbeddedFont.Muli:
                    path = "SpaceVIL.Fonts.Muli-Regular.ttf";
                    break;
                case EmbeddedFont.Nunito:
                    path = "SpaceVIL.Fonts.Nunito-Regular.ttf";
                    break;
                case EmbeddedFont.Quicksand:
                    path = "SpaceVIL.Fonts.Quicksand-Regular.ttf";
                    break;
                case EmbeddedFont.Ubuntu:
                    path = "SpaceVIL.Fonts.Ubuntu-Regular.ttf";
                    break;
                case EmbeddedFont.GlacialIndifference:
                    path = "SpaceVIL.Fonts.GlacialIndifference-Regular.otf";
                    break;
                case EmbeddedFont.RobotoMono:
                    path = "SpaceVIL.Fonts.RobotoMono-Regular.ttf";
                    break;
                case EmbeddedFont.OpenGostTypeA:
                    path = "SpaceVIL.Fonts.OpenGostTypeA-Regular.ttf";
                    break;

                default:
                    path = "SpaceVIL.Fonts.Verdana.ttf";
                    break;
            }
            return path;
        }
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
        public static void SetDefaultEmbeddedFont(EmbeddedFont font)
        {
            _default_font.SetDefaultEmbeddedFont(font);
        }
        public static void SetDefaultFont(String font_path)
        {
            _default_font.SetDefaultFont(font_path);
        }
        public static void SetDefaultFont(Stream font_stream)
        {
            _default_font.SetDefaultFont(font_stream);
        }
        public static Font GetEmbeddedFont(EmbeddedFont font, int size, FontStyle style)
        {
            return _default_font.GetEmbeddedFont(font, size, style);
        }
    }
}