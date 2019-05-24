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
        private Font _defaultFont;
        public void SetDefaultFont(Font font)
        {
            _defaultFont = font;
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
        //     _defaultFont = new Font(privateFontCollection.Families[0], 10, FontStyle.Regular);
        // }
        // public Font GetDefaultFont() //объекты не инициализируются почему-то, выяснить
        // {
        //     if (_defaultFont == null)
        //     {
        //         AddFontFromMemory();
        //         _defaultFont = new Font(privateFontCollection.Families[0], 10, FontStyle.Regular);
        //     }
        //     return new Font(_defaultFont.FontFamily, 10, _defaultFont.Style);
        //     // return new Font(new FontFamily("Ubuntu"), 10, FontStyle.Regular);
        // }

        private const int defFontSize = 12;
        public Font GetDefaultFont(int size = defFontSize) //объекты не инициализируются почему-то, выяснить
        {
            if (size == 0)
            {
                size = defFontSize;
            }
            if (_defaultFont == null)
            {
                AddFontFromMemory();
                _defaultFont = new Font(privateFontCollection.Families[0], defFontSize, FontStyle.Regular);
            }
            return GraphicsMathService.ChangeFontSize(size, _defaultFont); //new Font(_defaultFont.FontFamily, size, _defaultFont.Style);
            // return new Font(new FontFamily("Ubuntu"), size, FontStyle.Regular);
        }
        public Font GetDefaultFont(FontStyle style, int size)
        {
            if (size == 0)
            {
                size = defFontSize;
            }
            if (_defaultFont == null)
            {
                AddFontFromMemory();
                _defaultFont = new Font(privateFontCollection.Families[0], size, style);
            }
            return new Font(_defaultFont.FontFamily, size, style);
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
            if (String.IsNullOrEmpty(_embeddedFont)) // _embeddedFont == null || _embeddedFont == String.Empty)
                SetDefaultEmbeddedFont(EmbeddedFont.Ubuntu);

            privateFontCollection = new PrivateFontCollection();

            Stream fontStream = Assembly.GetExecutingAssembly().GetManifestResourceStream(_embeddedFont);
            System.IntPtr data = Marshal.AllocCoTaskMem((int)fontStream.Length);
            byte[] fontdata = new byte[fontStream.Length];
            fontStream.Read(fontdata, 0, (int)fontStream.Length);
            Marshal.Copy(fontdata, 0, data, (int)fontStream.Length);
            privateFontCollection.AddMemoryFont(data, (int)fontStream.Length);
            fontStream.Close();
            Marshal.FreeCoTaskMem(data);
        }
        private String _embeddedFont = null;
        public void SetDefaultEmbeddedFont(EmbeddedFont font)
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
        private static CursorImage _defaultCursor = new CursorImage(EmbeddedCursor.Arrow);

        public static void SetDefaultCursor(CursorImage cursor)
        {
            if (cursor == null)
                return;
            _defaultCursor = cursor;
        }

        public static CursorImage GetDefaultCursor()
        {
            return _defaultCursor;
        }

        private static ThemeStyle _defaultTheme; // = ThemeStyle.GetInstance();
        private static DefaultFont _defaultFont; // = ThemeStyle.GetInstance();

        static DefaultsService()
        {
            _defaultFont = DefaultFont.GetInstance();
            // _defaultTheme = ThemeStyle.GetInstance();
        }

        internal static void InitDefaultTheme()
        {
            _defaultTheme = new ThemeStyle();
        }

        public static ThemeStyle GetDefaultTheme()
        {
            if (_defaultTheme == null)
                _defaultTheme = new ThemeStyle();
            return _defaultTheme;
        }
        public static void SetDefaultTheme(ThemeStyle theme)
        {
            _defaultTheme = theme;
        }
        public static Style GetDefaultStyle(Type type)
        {
            if (_defaultTheme == null)
                _defaultTheme = new ThemeStyle();
            return _defaultTheme.GetThemeStyle(type);
        }
        public static Font GetDefaultFont()
        {
            return _defaultFont.GetDefaultFont();
        }
        public static Font GetDefaultFont(int size)
        {
            return _defaultFont.GetDefaultFont(size);
        }
        public static Font GetDefaultFont(FontStyle style, int size)
        {
            return _defaultFont.GetDefaultFont(style, size);
        }
        // public static void SetDefaultEmbeddedFont(EmbeddedFont font)
        // {
        //     _defaultFont.SetDefaultEmbeddedFont(font);
        // }
        // public static void SetDefaultFont(String font_path)
        // {
        //     _defaultFont.SetDefaultFont(font_path);
        // }
        public static void SetDefaultFont(Font font)
        {
            _defaultFont.SetDefaultFont(font);
        }
        // public static void SetDefaultFont(Stream font_stream)
        // {
        //     _defaultFont.SetDefaultFont(font_stream);
        // }
        // public static Font GetEmbeddedFont(EmbeddedFont font, int size, FontStyle style)
        // {
        //     return _defaultFont.GetEmbeddedFont(font, size, style);
        // }

        private static Dictionary<EmbeddedImage, Bitmap> images_32 = new Dictionary<EmbeddedImage, Bitmap>();
        private static Dictionary<EmbeddedImage, Bitmap> images_64 = new Dictionary<EmbeddedImage, Bitmap>();

        internal static void InitImages()
        {
            ///////////////32
            Bitmap stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.add32.png"));
            AddImage(stream, images_32, EmbeddedImage.Add);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.arrowleft32.png"));
            AddImage(stream, images_32, EmbeddedImage.ArrowLeft);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.arrowup32.png"));
            AddImage(stream, images_32, EmbeddedImage.ArrowUp);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.eye32.png"));
            AddImage(stream, images_32, EmbeddedImage.Eye);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.file32.png"));
            AddImage(stream, images_32, EmbeddedImage.File);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.folder32.png"));
            AddImage(stream, images_32, EmbeddedImage.Folder);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.folderplus32.png"));
            AddImage(stream, images_32, EmbeddedImage.FolderPlus);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.gear32.png"));
            AddImage(stream, images_32, EmbeddedImage.Gear);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.import32.png"));
            AddImage(stream, images_32, EmbeddedImage.Import);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.lines32.png"));
            AddImage(stream, images_32, EmbeddedImage.Lines);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.loupe32.png"));
            AddImage(stream, images_32, EmbeddedImage.Loupe);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.recyclebin32.png"));
            AddImage(stream, images_32, EmbeddedImage.RecycleBin);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.refresh32.png"));
            AddImage(stream, images_32, EmbeddedImage.Refresh);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.pencil32.png"));
            AddImage(stream, images_32, EmbeddedImage.Pencil);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.diskette32.png"));
            AddImage(stream, images_32, EmbeddedImage.Diskette);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.eraser32.png"));
            AddImage(stream, images_32, EmbeddedImage.Eraser);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.home32.png"));
            AddImage(stream, images_32, EmbeddedImage.Home);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.user32.png"));
            AddImage(stream, images_32, EmbeddedImage.User);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.drive32.png"));
            AddImage(stream, images_32, EmbeddedImage.Drive);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.filter32.png"));
            AddImage(stream, images_32, EmbeddedImage.Filter);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.loadcircle32.png"));
            AddImage(stream, images_32, EmbeddedImage.LoadCircle);

            //////////////////64
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.add64.png"));
            AddImage(stream, images_64, EmbeddedImage.Add);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.arrowleft64.png"));
            AddImage(stream, images_64, EmbeddedImage.ArrowLeft);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.arrowup64.png"));
            AddImage(stream, images_64, EmbeddedImage.ArrowUp);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.eye64.png"));
            AddImage(stream, images_64, EmbeddedImage.Eye);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.file64.png"));
            AddImage(stream, images_64, EmbeddedImage.File);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.folder64.png"));
            AddImage(stream, images_64, EmbeddedImage.Folder);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.folderplus64.png"));
            AddImage(stream, images_64, EmbeddedImage.FolderPlus);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.gear64.png"));
            AddImage(stream, images_64, EmbeddedImage.Gear);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.import64.png"));
            AddImage(stream, images_64, EmbeddedImage.Import);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.lines64.png"));
            AddImage(stream, images_64, EmbeddedImage.Lines);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.loupe64.png"));
            AddImage(stream, images_64, EmbeddedImage.Loupe);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.recyclebin64.png"));
            AddImage(stream, images_64, EmbeddedImage.RecycleBin);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.refresh64.png"));
            AddImage(stream, images_64, EmbeddedImage.Refresh);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.pencil64.png"));
            AddImage(stream, images_64, EmbeddedImage.Pencil);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.diskette64.png"));
            AddImage(stream, images_64, EmbeddedImage.Diskette);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.eraser64.png"));
            AddImage(stream, images_64, EmbeddedImage.Eraser);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.home64.png"));
            AddImage(stream, images_64, EmbeddedImage.Home);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.user64.png"));
            AddImage(stream, images_64, EmbeddedImage.User);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.drive64.png"));
            AddImage(stream, images_64, EmbeddedImage.Drive);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.filter64.png"));
            AddImage(stream, images_64, EmbeddedImage.Filter);
            stream = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Images.loadcircle64.png"));
            AddImage(stream, images_64, EmbeddedImage.LoadCircle);
        }

        static void AddImage(Bitmap stream, Dictionary<EmbeddedImage, Bitmap> map, EmbeddedImage key)
        {
            if (stream != null)
                map.Add(key, stream);
        }

        public static Bitmap GetDefaultImage(EmbeddedImage image, EmbeddedImageSize size)
        {
            if (size == EmbeddedImageSize.Size32x32 && images_32.ContainsKey(image))
            {
                return new Bitmap(images_32[image]);
            }
            else if (size == EmbeddedImageSize.Size64x64 && images_64.ContainsKey(image))
            {
                return new Bitmap(images_64[image]);
            }
            return null;
        }
    }
}