using System;
using System.Collections.Generic;
using System.Linq;
using System.Drawing;
using SpaceVIL;
using SpaceVIL.Core;
using SpaceVIL.Common;

namespace SpaceVIL.Core
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
            List<byte> _map = new List<byte>();
            Bitmap bmp = new Bitmap(bitmap);
            for (int j = 0; j < bitmap.Height; j++)
            {
                for (int i = 0; i < bitmap.Width; i++)
                {
                    Color pixel = bmp.GetPixel(i, j);
                    _map.Add(pixel.R);
                    _map.Add(pixel.G);
                    _map.Add(pixel.B);
                    _map.Add(pixel.A);
                }
            }
            return _map.ToArray();
        }
    }
}