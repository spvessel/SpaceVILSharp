using System;
using System.Collections.Generic;
using System.Linq;
using System.Drawing;
using SpaceVIL;
using SpaceVIL.Core;
using SpaceVIL.Common;
using System.Drawing.Imaging;
using System.Runtime.InteropServices;

namespace SpaceVIL
{
    public sealed class CursorImage
    {
        private Glfw3.Glfw.Cursor _cursor;
        internal Glfw3.Glfw.Cursor GetCursor()
        {
            return _cursor;
        }

        private byte[] _bitmap;

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

        public CursorImage(Bitmap bitmap)
        {
            if (bitmap == null)
                return;

            _bitmap = GetImagePixels(bitmap);
            CreateCursor(bitmap);
        }
        public CursorImage(Bitmap bitmap, int width, int height)
        {
            if (bitmap == null)
                return;
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

        public int GetCursorWidth()
        {
            return _imageWidth;
        }

        public int GetCursorHeight()
        {
            return _imageHeight;
        }

        public void SetImage(Bitmap image)
        {
            if (image == null)
                return;
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
            
            // Bitmap bmp = new Bitmap(bitmap);
            // for (int j = 0; j < bitmap.Height; j++)
            // {
            //     for (int i = 0; i < bitmap.Width; i++)
            //     {
            //         Color pixel = bmp.GetPixel(i, j);
            //         _map.Add(pixel.R);
            //         _map.Add(pixel.G);
            //         _map.Add(pixel.B);
            //         _map.Add(pixel.A);
            //     }
            // }
            // return _map.ToArray();
        }
    }
}