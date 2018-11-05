﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    public class ImageItem : VisualItem, IImageItem
    {
        static int count = 0;
        public Image _image;
        private List<byte> _bitmap;
        private String _url;

        public ImageItem()
        {
            SetItemName("Image_" + count);
            count++;
        }
        public ImageItem(Image picture) : this()
        {
            SetImage(picture);
        }

        //IImage implements
        public Image GetImage()
        {
            return _image;
        }
        internal bool IsChanged = false;
        public void SetImage(Image picture)
        {
            if (picture == null)
                return;
            _image = picture;

            _bitmap = new List<byte>();
            Bitmap bmp = new Bitmap(_image);
            for (int j = _image.Height - 1; j >= 0; j--)
            // for (int j = 0; j < _image.Height; j++)
            {
                for (int i = 0; i < _image.Width; i++)
                {
                    Color pixel = bmp.GetPixel(i, j);
                    _bitmap.Add(pixel.R);
                    _bitmap.Add(pixel.G);
                    _bitmap.Add(pixel.B);
                    _bitmap.Add(pixel.A);
                }
            }
        }
        public String GetImageUrl()
        {
            return _url;
        }
        public void SetImageUrl(String url)
        {
            _url = url;
        }

        public byte[] GetPixMapImage()
        {
            /*if (_image == null)
                return new byte[] { 0 };*/

            if (_image == null)
                return null;

            if (_bitmap == null)
            {
                _bitmap = new List<byte>();
                Bitmap bmp = new Bitmap(_image);
                for (int j = _image.Height - 1; j >= 0; j--)
                {
                    for (int i = 0; i < _image.Width; i++)
                    {
                        Color pixel = bmp.GetPixel(i, j);
                        _bitmap.Add(pixel.R);
                        _bitmap.Add(pixel.G);
                        _bitmap.Add(pixel.B);
                        _bitmap.Add(pixel.A);
                    }
                }
            }
            return _bitmap.ToArray();
        }
        public int GetImageWidth()
        {
            return _image.Width;
        }
        public int GetImageHeight()
        {
            return _image.Height;
        }
    }
}
