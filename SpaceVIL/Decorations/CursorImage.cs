using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Common;
using System.Drawing.Imaging;
using System.Runtime.InteropServices;

namespace SpaceVIL.Decorations
{
    /// <summary>
    /// Class CursorImage provides features for creating custom cursors. It can also 
    /// be used with several standards types of cursor images (Arrow, IBeam, Crosshair, Hand and etc.).
    /// </summary>
    public sealed class CursorImage
    {
        private Glfw3.Glfw.Cursor _cursor;

        internal Glfw3.Glfw.Cursor GetCursor()
        {
            return _cursor;
        }

        private byte[] _bitmap;

        /// <summary>
        /// Constructor for creating cursor with standards types of cursor images (Arrow, IBeam, Crosshair, Hand and etc.).
        /// </summary>
        /// <param name="type">Cursor image as SpaceVIL.Core.EmbeddedCursor enum.</param>
        public CursorImage(EmbeddedCursor type)
        {
            switch (type)
            {
                case EmbeddedCursor.Arrow:
                    _cursor = CommonService.CursorArrow;
                    break;
                case EmbeddedCursor.IBeam:
                    _cursor = CommonService.CursorInput;
                    break;
                case EmbeddedCursor.Crosshair:
                    _cursor = CommonService.CursorResizeAll;
                    break;
                case EmbeddedCursor.Hand:
                    _cursor = CommonService.CursorHand;
                    break;
                case EmbeddedCursor.ResizeX:
                    _cursor = CommonService.CursorResizeH;
                    break;
                case EmbeddedCursor.ResizeY:
                    _cursor = CommonService.CursorResizeV;
                    break;
                default:
                    _cursor = CommonService.CursorArrow;
                    break;
            }

            _imageHeight = 25;
            _imageWidth = 25;
        }

        /// <summary>
        /// Constructor for creating cursor with custom bitmap image.
        /// </summary>
        /// <param name="bitmap">Cursor image as System.Drawing.Bitmap class.</param>
        public CursorImage(Bitmap bitmap)
        {
            if (bitmap == null)
            {
                return;
            }

            _bitmap = GetImagePixels(bitmap);
            CreateCursor(bitmap);
        }

        /// <summary>
        /// Constructor for creating cursor with custom bitmap image with the specified size.
        /// </summary>
        /// <param name="bitmap">Cursor image as System.Drawing.Bitmap class.</param>
        /// <param name="width">Desired width.</param>
        /// <param name="height">Desired height.</param>
        public CursorImage(Bitmap bitmap, int width, int height)
        {
            if (bitmap == null)
            {
                return;
            }
            Bitmap scaled = GraphicsMathService.ScaleBitmap(bitmap, width, height);
            _bitmap = GetImagePixels(scaled);
            CreateCursor(scaled);
            scaled.Dispose();
        }

        private void CreateCursor(Bitmap bitmap)
        {
            Glfw3.Glfw.Image image;
            image.Width = bitmap.Width;
            image.Height = bitmap.Height;
            image.Pixels = _bitmap;
            _cursor = Glfw3.Glfw.CreateCursor(image, 0, 0);
        }

        private int _imageWidth;
        private int _imageHeight;

        /// <summary>
        /// Getting cursor image width.
        /// </summary>
        /// <returns>The width of the image.</returns>
        public int GetCursorWidth()
        {
            return _imageWidth;
        }

        /// <summary>
        /// Getting cursor image height.
        /// </summary>
        /// <returns>The height of the image.</returns>
        public int GetCursorHeight()
        {
            return _imageHeight;
        }

        /// <summary>
        /// Setting new image for cursor.
        /// </summary>
        /// <param name="image">Cursor image as System.Drawing.Bitmap.</param>
        public void SetImage(Bitmap image)
        {
            if (image == null)
            {
                return;
            }
            _bitmap = GetImagePixels(image);
            CreateCursor(image);
        }

        private byte[] GetImagePixels(Bitmap bitmap)
        {
            if (bitmap == null)
                return null;

            _imageWidth = bitmap.Width;
            _imageHeight = bitmap.Height;
            int size = _imageWidth * _imageHeight;
            BitmapData bitData = bitmap.LockBits(
                new System.Drawing.Rectangle(System.Drawing.Point.Empty, bitmap.Size), ImageLockMode.ReadOnly, PixelFormat.Format32bppArgb);
            int[] pixels = new int[size];
            Marshal.Copy(bitData.Scan0, pixels, 0, size);
            bitmap.UnlockBits(bitData);
            byte[] result = new byte[size * 4];
            int index = 0;
            foreach (int pixel in pixels)
            {
                byte a = (byte)((pixel & 0xFF000000) >> 24);
                byte r = (byte)((pixel & 0x00FF0000) >> 16);
                byte g = (byte)((pixel & 0x0000FF00) >> 8);
                byte b = (byte)(pixel & 0x000000FF);
                result[index++] = r;
                result[index++] = g;
                result[index++] = b;
                result[index++] = a;
            }
            return result;
        }
    }
}