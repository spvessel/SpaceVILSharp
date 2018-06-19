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
        private String _url;

        public ImageItem()
        {
            SetItemName("Image" + count);
            count++;
        }
        public ImageItem(Image picture) : this()
        {
            _image = picture;
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
            Bitmap bmp = new Bitmap(_image);
            byte[] pixel_buffer = new byte[_image.Width * _image.Height * 4];
            int skew = 0;
            for (int i = 0; i < _image.Width; i++)
            {
                for (int j = 0; j < _image.Height; j++)
                {
                    Color pixel = bmp.GetPixel(i, j);
                    pixel_buffer[0 + skew] = pixel.R;
                    pixel_buffer[1 + skew] = pixel.G;
                    pixel_buffer[2 + skew] = pixel.B;
                    pixel_buffer[3 + skew] = pixel.A;
                    skew += 4;
                }
            }
            return pixel_buffer;
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
