using System;
using System.Drawing;
using System.Drawing.Text;
using System.IO;
using System.Reflection;
using System.Runtime.InteropServices;
using SpaceVIL.Core;

namespace SpaceVIL.Common
{
    internal sealed class DefaultFont
    {
        private DefaultFont() { }

        private PrivateFontCollection _privateFontCollection = null;

        private String _embeddedFont = null;

        private Font _defaultFont;

        private const int _defFontSize = 12;
        
        private static DefaultFont _instance;

        internal static DefaultFont GetInstance()
        {
            if (_instance == null)
                _instance = new DefaultFont();
            return _instance;
        }

        internal void SetDefaultFont(Font font)
        {
            _defaultFont = font;
        }

        internal Font GetDefaultFont(int size = _defFontSize)
        {
            if (size == 0)
            {
                size = _defFontSize;
            }
            if (_defaultFont == null)
            {
                AddFontFromMemory();
                _defaultFont = new Font(_privateFontCollection.Families[0], _defFontSize, FontStyle.Regular);
            }
            return GraphicsMathService.ChangeFontSize(size, _defaultFont);
        }
        internal Font GetDefaultFont(FontStyle style, int size)
        {
            if (size == 0)
            {
                size = _defFontSize;
            }
            if (_defaultFont == null)
            {
                AddFontFromMemory();
                _defaultFont = new Font(_privateFontCollection.Families[0], size, style);
            }
            return new Font(_defaultFont.FontFamily, size, style);
        }

        private void AddFontFromMemory()
        {
            if (String.IsNullOrEmpty(_embeddedFont))
                SetDefaultEmbeddedFont(EmbeddedFont.Ubuntu);

            _privateFontCollection = new PrivateFontCollection();

            Stream fontStream = Assembly.GetExecutingAssembly().GetManifestResourceStream(_embeddedFont);
            System.IntPtr data = Marshal.AllocCoTaskMem((int)fontStream.Length);
            byte[] fontdata = new byte[fontStream.Length];
            fontStream.Read(fontdata, 0, (int)fontStream.Length);
            Marshal.Copy(fontdata, 0, data, (int)fontStream.Length);
            _privateFontCollection.AddMemoryFont(data, (int)fontStream.Length);
            fontStream.Close();
            Marshal.FreeCoTaskMem(data);
        }
        
        internal void SetDefaultEmbeddedFont(EmbeddedFont font)
        {
            switch (font)
            {
                case EmbeddedFont.Ubuntu:
                    _embeddedFont = "SpaceVIL.Fonts.Ubuntu-Regular.ttf";
                    break;

                default:
                    _embeddedFont = "SpaceVIL.Fonts.Ubuntu-Regular.ttf";
                    break;
            }
        }
    }
}