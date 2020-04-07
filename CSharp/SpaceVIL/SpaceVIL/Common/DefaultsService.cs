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
    }
}