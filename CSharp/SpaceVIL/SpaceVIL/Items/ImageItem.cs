using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL.Core;
using System.Threading;

namespace SpaceVIL
{
    public class ImageItem : Prototype, IImageItem
    {
        static int count = 0;
        public Bitmap _image;
        private byte[] _bitmap;
        private String _url;
        private float _angleRotation = 0.0f;
        public void SetRotationAngle(float angle)
        {
            _angleRotation = angle * (float)Math.PI / 180.0f;
        }

        public float GetRotationAngle()
        {
            return _angleRotation;
        }

        public bool isHover = true;

        internal override bool GetHoverVerification(float xpos, float ypos)
        {
            if (isHover)
                return base.GetHoverVerification(xpos, ypos);
            return false;
        }

        /// <summary>
        /// Constructs an ImageItem
        /// </summary>
        public ImageItem()
        {
            SetItemName("Image_" + count);
            count++;
        }

        /// <summary>
        /// Constructs an ImageItem with an image
        /// </summary>
        public ImageItem(Bitmap picture) : this()
        {
            if (picture == null)
                return;
            _image = picture;
            _bitmap = CreateByteImage();
        }

        public ImageItem(Bitmap picture, bool hover) : this(picture)
        {
            isHover = hover;
        }

        /// <summary>
        /// Returns the image as byte array
        /// </summary>
        public byte[] GetPixMapImage()
        {
            if (_image == null)
                return null;

            if (_bitmap == null)
                _bitmap = CreateByteImage();

            return _bitmap;
        }

        private byte[] CreateByteImage()
        {
            try
            {
                List<byte> result = new List<byte>();
                Bitmap bmp = new Bitmap(_image);
                for (int j = _image.Height - 1; j >= 0; j--)
                {
                    for (int i = 0; i < _image.Width; i++)
                    {
                        Color pixel = bmp.GetPixel(i, j);
                        result.Add(pixel.R);
                        result.Add(pixel.G);
                        result.Add(pixel.B);
                        result.Add(pixel.A);
                    }
                }
                return result.ToArray();
            }
            catch (System.Exception ex)
            {
                Console.WriteLine("Create byte image" + ex.StackTrace);
                return null;
            }
        }

        /// <returns> width of the image in the ImageItem </returns>
        public int GetImageWidth()
        {
            try
            {
                if (_image == null)
                    return -1;
                return _image.Width;
            }
            catch (System.Exception ex)
            {
                Console.WriteLine("Getting width " + ex.StackTrace);
                return -1;
            }
        }

        /// <returns> height of the image in the ImageItem </returns>
        public int GetImageHeight()
        {
            try
            {
                if (_image == null)
                    return -1;
                return _image.Height;
            }
            catch (System.Exception ex)
            {
                Console.WriteLine("Getting heigh " + ex.StackTrace);
                return -1;
            }
        }

        //IImage implements
        /// <returns> BufferedImage of the ImageItem </returns>
        public Bitmap GetImage()
        {
            return _image;
        }
        //internal bool IsChanged = false;

        /// <summary>
        /// Set an image into the ImageItem
        /// </summary>
        public void SetImage(Bitmap image)
        {
            if (image == null)
                return;
            _image = image;
            _bitmap = CreateByteImage();
        }

        /// <summary>
        /// Image location
        /// </summary>
        public String GetImageUrl()
        {
            return _url;
        }
        public void SetImageUrl(String url)
        {
            _url = url;
        }

        private bool _isOverlay = false;
        private Color _colorOverlay;

        public Color GetColorOverlay()
        {
            return _colorOverlay;
        }

        public void SetColorOverlay(Color color)
        {
            _colorOverlay = color;
            _isOverlay = true;
        }
        public void SetColorOverlay(bool overlay)
        {
            _isOverlay = overlay;
        }

        public bool IsColorOverLay()
        {
            return _isOverlay;
        }
    }
}
