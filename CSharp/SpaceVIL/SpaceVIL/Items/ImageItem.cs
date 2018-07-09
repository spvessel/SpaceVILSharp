using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    class ImageItem : VisualItem, IImageItem
    {
        static int count = 0;
        private Image _image;
        private List<byte> _bitmap;
        private String _url;

        public ImageItem()
        {
            SetItemName("Image_" + count);
            count++;
        }
        public ImageItem(Image picture) : this()
        {
            _image = picture;

            _bitmap = new List<byte>();
            Bitmap bmp = new Bitmap(_image);
            for (int i = 0; i < _image.Width; i++)
            {
                for (int j = 0; j < _image.Height; j++)
                {
                    Color pixel = bmp.GetPixel(i, j);
                    _bitmap.Add(pixel.R);
                    _bitmap.Add(pixel.G);
                    _bitmap.Add(pixel.B);
                    _bitmap.Add(pixel.A);
                }
            }
        }

        //IImage implements
        public Image GetImage()
        {
            return _image;
        }
        public void SetImage(Image picture)
        {
            _image = picture;
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
            if (_bitmap == null)
            {
                _bitmap = new List<byte>();
                Bitmap bmp = new Bitmap(_image);
                for (int i = 0; i < _image.Width; i++)
                {
                    for (int j = 0; j < _image.Height; j++)
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

        public override void InvokePoolEvents() { }
    }
}
