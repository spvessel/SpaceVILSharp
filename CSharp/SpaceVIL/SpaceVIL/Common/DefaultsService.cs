using System.Drawing;
using System.Drawing.Text;
using System;
using System.Collections.Generic;
using System.Reflection;
using System.IO;

namespace SpaceVIL
{
    public enum EmbeddedFont
    {
        OpenSans,
        Moire,
        Verdana,
        Muli,
        Nunito,
        Quicksand,
        Ubuntu
    }
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
        private static String _embedded_font;
        public static void SetDefaultEmbeddedFont(EmbeddedFont font)
        {
            switch (font)
            {
                case EmbeddedFont.Verdana:
                    _embedded_font = "SpaceVIL.Fonts.verdana.ttf";
                    break;
                case EmbeddedFont.Moire:
                    _embedded_font = "SpaceVIL.Fonts.moireregular.ttf";
                    break;
                case EmbeddedFont.OpenSans:
                    _embedded_font = "SpaceVIL.Fonts.opensans.ttf";
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

                default:
                    _embedded_font = "SpaceVIL.Fonts.verdana.ttf";
                    break;
            }
        }
        public static void SetDefaultFont(Font font)
        {
            _default_font = font;
        }
        public static Font GetDefaultFont()//объекты не инициализируются почему-то, выяснить
        {
            if (_default_font == null)
            {
                FontFamily[] fontFamilies;
                PrivateFontCollection privateFontCollection = AddFontFromMemory();
                fontFamilies = privateFontCollection.Families;
                int count = fontFamilies.Length;
                string familyName = fontFamilies[0].Name;
                _default_font = new Font(familyName, 14);
            }

            return _default_font;
        }
        private static PrivateFontCollection AddFontFromMemory()
        {
            if (_embedded_font == null || _embedded_font == String.Empty)
                SetDefaultEmbeddedFont(EmbeddedFont.Moire);

            PrivateFontCollection privateFontCollection = new PrivateFontCollection();
            Stream fontStream = Assembly.GetExecutingAssembly().GetManifestResourceStream(_embedded_font);

            byte[] fontdata = new byte[fontStream.Length];
            fontStream.Read(fontdata, 0, (int)fontStream.Length);
            fontStream.Close();
            unsafe
            {
                fixed (byte* pFontData = fontdata)
                {
                    privateFontCollection.AddMemoryFont((System.IntPtr)pFontData, fontdata.Length);
                }
            }
            return privateFontCollection;
        }
    }
}