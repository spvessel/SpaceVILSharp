using System;
using System.Drawing;
using System.Collections.Generic;
using System.Reflection;

using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL.Common
{
    /// <summary>
    /// DefaultsService is static class providing methods to getting SpaceVIL default common values such as font, mouse cursor, icon images. 
    /// </summary>
    public static class DefaultsService
    {
        static DefaultsService()
        {
            _defaultFont = DefaultFont.GetInstance();
        }

        internal static void InitDefaultTheme()
        {
            _defaultTheme = new ThemeStyle();
        }

        private static Dictionary<EmbeddedImage, Bitmap> images_32 = new Dictionary<EmbeddedImage, Bitmap>();

        private static Dictionary<EmbeddedImage, Bitmap> images_64 = new Dictionary<EmbeddedImage, Bitmap>();

        private static CursorImage _defaultCursor = new CursorImage(EmbeddedCursor.Arrow);

        private static ThemeStyle _defaultTheme;

        private static DefaultFont _defaultFont;

        /// <summary>
        /// Setting default cursor image the current application.
        /// </summary>
        /// <param name="cursor">The mouse cursor image as SpaceVIL.Decorations.CursorImage</param>
        public static void SetDefaultCursor(CursorImage cursor)
        {
            if (cursor == null)
            {
                return;
            }
            _defaultCursor = cursor;
        }

        /// <summary>
        /// Getting the current default mouse cursor image.
        /// </summary>
        /// <returns>The mouse cursor image as SpaceVIL.Decorations.CursorImage</returns>
        public static CursorImage GetDefaultCursor()
        {
            return _defaultCursor;
        }

        /// <summary>
        /// Getting the default theme.
        /// </summary>
        /// <returns>The theme as SpaceVIL.Decorations.ThemeStyle</returns>
        public static ThemeStyle GetDefaultTheme()
        {
            if (_defaultTheme == null)
            {
                _defaultTheme = new ThemeStyle();
            }
            return _defaultTheme;
        }

        /// <summary>
        /// Setting the default theme for the current application.
        /// </summary>
        /// <param name="theme">The theme as SpaceVIL.Decorations.ThemeStyle</param>
        public static void SetDefaultTheme(ThemeStyle theme)
        {
            _defaultTheme = theme;
        }

        /// <summary>
        /// Getting the default item style from the current default theme by its type.
        /// </summary>
        /// <param name="type">Item type as System.Type
        /// <para/>Example: typeof(SpaceVIL.ButtonCore)
        /// </param>
        /// <returns>Style as SpaceVIL.Decorations.Style</returns>
        public static Style GetDefaultStyle(Type type)
        {
            if (_defaultTheme == null)
            {
                _defaultTheme = new ThemeStyle();
            }
            return _defaultTheme.GetThemeStyle(type);
        }

        /// <summary>
        /// Getting the current default font for the current application.
        /// </summary>
        /// <returns>The current default font as System.Drawing.Font</returns>
        public static Font GetDefaultFont()
        {
            return _defaultFont.GetDefaultFont();
        }
        /// <summary>
        /// Getting the current default font with the specified font size for the current application.
        /// </summary>
        /// <param name="size">A font size as System.Int32</param>
        /// <returns>The current default font with changed font size as System.Drawing.Font</returns>
        public static Font GetDefaultFont(int size)
        {
            return _defaultFont.GetDefaultFont(size);
        }
        /// <summary>
        /// Getting the current default font with the specified font size and font style for the current application.
        /// </summary>
        /// <param name="style">A font style as System.Drawing.FontStyle</param>
        /// <param name="size">A font size as System.Int32</param>
        /// <returns>The current default font with changed font size and font style as System.Drawing.Font</returns>
        public static Font GetDefaultFont(FontStyle style, int size)
        {
            return _defaultFont.GetDefaultFont(style, size);
        }
        /// <summary>
        /// Setting the default font for the current application.
        /// </summary>
        /// <param name="font">A font as System.Drawing.Font</param>
        public static void SetDefaultFont(Font font)
        {
            _defaultFont.SetDefaultFont(font);
        }

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

        private static void AddImage(Bitmap stream, Dictionary<EmbeddedImage, Bitmap> map, EmbeddedImage key)
        {
            if (stream != null)
                map.Add(key, stream);
        }

        /// <summary>
        /// Getting the specified image by the type and size of the image, which is stored in the SpaceVIL framework.
        /// </summary>
        /// <param name="image">
        /// An image type as SpaceVIL.Core.EmbeddedImage
        /// <para/>Example: SpaceVIL.Core.EmbeddedImage.Gear (to get gear icon)
        /// </param>
        /// <param name="size">
        /// An image size as SpaceVIL.Core.EmbeddedImageSize (only 32x32 or 64x64)
        /// <para/>Example: SpaceVIL.Core.EmbeddedImage.Size32x32 (to get an image in 32x32 pixels)
        /// </param>
        /// <returns>Copy of image icon as System.Drawing.Bitmap</returns>
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

        private static Dictionary<int, KeyCode> _defaultLinuxKeyCodes = new Dictionary<int, KeyCode>()
        {
            { 65, KeyCode.Space },
            { 48, KeyCode.Apostrophe },
            { 59, KeyCode.Comma },
            { 20, KeyCode.Minus },
            { 60, KeyCode.Period },
            { 61, KeyCode.Slash },
            { 19, KeyCode.Alpha0 },
            { 10, KeyCode.Alpha1 },
            { 11, KeyCode.Alpha2 },
            { 12, KeyCode.Alpha3 },
            { 13, KeyCode.Alpha4 },
            { 14, KeyCode.Alpha5 },
            { 15, KeyCode.Alpha6 },
            { 16, KeyCode.Alpha7 },
            { 17, KeyCode.Alpha8 },
            { 18, KeyCode.Alpha9 },
            { 47, KeyCode.SemiColon },
            { 21, KeyCode.Equal },
            { 38, KeyCode.A },
            { 56, KeyCode.B },
            { 54, KeyCode.C },
            { 40, KeyCode.D },
            { 26, KeyCode.E },
            { 41, KeyCode.F },
            { 42, KeyCode.G },
            { 43, KeyCode.H },
            { 31, KeyCode.I },
            { 44, KeyCode.J },
            { 45, KeyCode.K },
            { 46, KeyCode.L },
            { 58, KeyCode.M },
            { 57, KeyCode.N },
            { 32, KeyCode.O },
            { 33, KeyCode.P },
            { 24, KeyCode.Q },
            { 27, KeyCode.R },
            { 39, KeyCode.S },
            { 28, KeyCode.T },
            { 30, KeyCode.U },
            { 55, KeyCode.V },
            { 25, KeyCode.W },
            { 53, KeyCode.X },
            { 29, KeyCode.Y },
            { 52, KeyCode.Z },
            { 34, KeyCode.LeftBracket },
            { 51, KeyCode.Backslash },
            { 35, KeyCode.RightBracket },
            { 49, KeyCode.GraveAccent },
            { 94, KeyCode.World1 },
            { 9, KeyCode.Escape },
            { 36, KeyCode.Enter },
            { 23, KeyCode.Tab },
            { 22, KeyCode.Backspace },
            { 118, KeyCode.Insert },
            { 119, KeyCode.Delete },
            { 114, KeyCode.Right },
            { 113, KeyCode.Left },
            { 116, KeyCode.Down },
            { 111, KeyCode.Up },
            { 112, KeyCode.PageUp },
            { 117, KeyCode.PageDown },
            { 110, KeyCode.Home },
            { 115, KeyCode.End },
            { 66, KeyCode.CapsLock },
            { 78, KeyCode.ScrollLock },
            { 77, KeyCode.NumLock },
            { 218, KeyCode.PrintScreen },
            { 127, KeyCode.Pause },
            { 67, KeyCode.F1 },
            { 68, KeyCode.F2 },
            { 69, KeyCode.F3 },
            { 70, KeyCode.F4 },
            { 71, KeyCode.F5 },
            { 72, KeyCode.F6 },
            { 73, KeyCode.F7 },
            { 74, KeyCode.F8 },
            { 75, KeyCode.F9 },
            { 76, KeyCode.F10 },
            { 95, KeyCode.F11 },
            { 96, KeyCode.F12 },
            { 90, KeyCode.Numpad0 },
            { 87, KeyCode.Numpad1 },
            { 88, KeyCode.Numpad2 },
            { 89, KeyCode.Numpad3 },
            { 83, KeyCode.Numpad4 },
            { 84, KeyCode.Numpad5 },
            { 85, KeyCode.Numpad6 },
            { 79, KeyCode.Numpad7 },
            { 80, KeyCode.Numpad8 },
            { 81, KeyCode.Numpad9 },
            { 129, KeyCode.NumpadDecimal },
            { 106, KeyCode.NumpadDivide },
            { 63, KeyCode.NumpadMultiply },
            { 82, KeyCode.NumpadSubtract },
            { 86, KeyCode.NumpadAdd },
            { 104, KeyCode.NumpadEnter },
            { 125, KeyCode.NumpadEqual },
            { 50, KeyCode.LeftShift },
            { 37, KeyCode.LeftControl },
            { 64, KeyCode.LeftAlt },
            { 133, KeyCode.LeftSuper },
            { 62, KeyCode.RightShift },
            { 105, KeyCode.RightControl },
            { 203, KeyCode.RightAlt },
            { 134, KeyCode.RightSuper },
            { 135, KeyCode.Menu }
        };

        private static Dictionary<int, KeyCode> _defaultWindowsKeyCodes = new Dictionary<int, KeyCode>()
        {
            { 57, KeyCode.Space },
            { 40, KeyCode.Apostrophe },
            { 51, KeyCode.Comma },
            { 12, KeyCode.Minus },
            { 52, KeyCode.Period },
            { 53, KeyCode.Slash },
            { 11, KeyCode.Alpha0 },
            { 2, KeyCode.Alpha1 },
            { 3, KeyCode.Alpha2 },
            { 4, KeyCode.Alpha3 },
            { 5, KeyCode.Alpha4 },
            { 6, KeyCode.Alpha5 },
            { 7, KeyCode.Alpha6 },
            { 8, KeyCode.Alpha7 },
            { 9, KeyCode.Alpha8 },
            { 10, KeyCode.Alpha9 },
            { 39, KeyCode.SemiColon },
            { 13, KeyCode.Equal },
            { 30, KeyCode.A },
            { 48, KeyCode.B },
            { 46, KeyCode.C },
            { 32, KeyCode.D },
            { 18, KeyCode.E },
            { 33, KeyCode.F },
            { 34, KeyCode.G },
            { 35, KeyCode.H },
            { 23, KeyCode.I },
            { 36, KeyCode.J },
            { 37, KeyCode.K },
            { 38, KeyCode.L },
            { 50, KeyCode.M },
            { 49, KeyCode.N },
            { 24, KeyCode.O },
            { 25, KeyCode.P },
            { 16, KeyCode.Q },
            { 19, KeyCode.R },
            { 31, KeyCode.S },
            { 20, KeyCode.T },
            { 22, KeyCode.U },
            { 47, KeyCode.V },
            { 17, KeyCode.W },
            { 45, KeyCode.X },
            { 21, KeyCode.Y },
            { 44, KeyCode.Z },
            { 26, KeyCode.LeftBracket },
            { 43, KeyCode.Backslash },
            { 27, KeyCode.RightBracket },
            { 41, KeyCode.GraveAccent },
            { 86, KeyCode.World2 },
            { 1, KeyCode.Escape },
            { 28, KeyCode.Enter },
            { 15, KeyCode.Tab },
            { 14, KeyCode.Backspace },
            { 338, KeyCode.Insert },
            { 339, KeyCode.Delete },
            { 333, KeyCode.Right },
            { 331, KeyCode.Left },
            { 336, KeyCode.Down },
            { 328, KeyCode.Up },
            { 329, KeyCode.PageUp },
            { 337, KeyCode.PageDown },
            { 327, KeyCode.Home },
            { 335, KeyCode.End },
            { 58, KeyCode.CapsLock },
            { 70, KeyCode.ScrollLock },
            { 325, KeyCode.NumLock },
            { 311, KeyCode.PrintScreen },
            { 326, KeyCode.Pause },
            { 59, KeyCode.F1 },
            { 60, KeyCode.F2 },
            { 61, KeyCode.F3 },
            { 62, KeyCode.F4 },
            { 63, KeyCode.F5 },
            { 64, KeyCode.F6 },
            { 65, KeyCode.F7 },
            { 66, KeyCode.F8 },
            { 67, KeyCode.F9 },
            { 68, KeyCode.F10 },
            { 87, KeyCode.F11 },
            { 88, KeyCode.F12 },
            { 100, KeyCode.F13 },
            { 101, KeyCode.F14 },
            { 102, KeyCode.F15 },
            { 103, KeyCode.F16 },
            { 104, KeyCode.F17 },
            { 105, KeyCode.F18 },
            { 106, KeyCode.F19 },
            { 107, KeyCode.F20 },
            { 108, KeyCode.F21 },
            { 109, KeyCode.F22 },
            { 110, KeyCode.F23 },
            { 118, KeyCode.F24 },
            { 82, KeyCode.Numpad0 },
            { 79, KeyCode.Numpad1 },
            { 80, KeyCode.Numpad2 },
            { 81, KeyCode.Numpad3 },
            { 75, KeyCode.Numpad4 },
            { 76, KeyCode.Numpad5 },
            { 77, KeyCode.Numpad6 },
            { 71, KeyCode.Numpad7 },
            { 72, KeyCode.Numpad8 },
            { 73, KeyCode.Numpad9 },
            { 83, KeyCode.NumpadDecimal },
            { 309, KeyCode.NumpadDivide },
            { 55, KeyCode.NumpadMultiply },
            { 74, KeyCode.NumpadSubtract },
            { 78, KeyCode.NumpadAdd },
            { 284, KeyCode.NumpadEnter },
            { 89, KeyCode.NumpadEqual },
            { 42, KeyCode.LeftShift },
            { 29, KeyCode.LeftControl },
            { 56, KeyCode.LeftAlt },
            { 347, KeyCode.LeftSuper },
            { 54, KeyCode.RightShift },
            { 285, KeyCode.RightControl },
            { 312, KeyCode.RightAlt },
            { 348, KeyCode.RightSuper },
            { 349, KeyCode.Menu }
        };

        /// <summary>
        /// Returns KeyCode of specified scancode key of keyboard.
        /// </summary>
        /// <param name="scancode">Scancode key of keyboard.</param>
        /// <returns>The key as com.spvessel.spacevil.Flags.KeyCode.</returns>
        public static KeyCode GetKeyCodeByScancode(int scancode)
        {
            if (CommonService.GetOSType() == OSType.Linux)
            {
                if (_defaultLinuxKeyCodes.ContainsKey(scancode))
                {
                    return _defaultLinuxKeyCodes[scancode];
                }
            }
            else if (CommonService.GetOSType() == OSType.Windows)
            {
                if (_defaultWindowsKeyCodes.ContainsKey(scancode))
                {
                    return _defaultWindowsKeyCodes[scancode];
                }
            }
            return KeyCode.Unknown;
        }
    }
}