using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using System.Threading;
using SpaceVIL.Common;
using SpaceVIL.Core;

namespace SpaceVIL
{
    public class ImageItem : Prototype, IImageItem
    {
        internal class ImageBounds : Geometry, IPosition
        {
            private int _x, _y;
            public void SetX(int x)
            {
                _x = x;
            }
            public void SetY(int y)
            {
                _y = y;
            }
            public int GetX()
            {
                return _x;
            }
            public int GetY()
            {
                return _y;
            }
        }

        internal ImageBounds Area = new ImageBounds();

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

        public bool IsHover = true;

        internal override bool GetHoverVerification(float xpos, float ypos)
        {
            if (IsHover)
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
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.ImageItem)));
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
            IsHover = hover;
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

        private bool _isKeepAspectRatio = false;
        public void KeepAspectRatio(bool value)
        {
            _isKeepAspectRatio = value;
        }
        public bool IsAspectRatio()
        {
            return _isKeepAspectRatio;
        }

        public override void SetSize(int width, int height)
        {
            this.SetWidth(width);
            this.SetHeight(height);
        }
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            Area.SetHeight(height);
            if (_isKeepAspectRatio && _image != null)
                ApplyAspectRatio();
            UpdateLayout();
        }

        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            Area.SetWidth(width);
            if (_isKeepAspectRatio && _image != null)
                ApplyAspectRatio();
            UpdateLayout();
        }

        public override void SetX(int _x)
        {
            base.SetX(_x);
            UpdateLayout();
        }

        public override void SetY(int _y)
        {
            base.SetY(_y);
            UpdateLayout();
        }

        private void ApplyAspectRatio()
        {
            int w, h;
            float ratioW = (float)_image.Width / (float)_image.Height;
            float ratioH = (float)_image.Height / (float)_image.Width;
            if (GetWidth() > GetHeight())
            {
                h = GetHeight();
                w = (int)((float)h * ratioW);
                Area.SetWidth(w);
                Area.SetHeight(h);
            }
            else
            {
                w = GetWidth();
                h = (int)((float)w * ratioH);
                Area.SetWidth(w);
                Area.SetHeight(h);
            }
        }

        //self update
        public void UpdateLayout()
        {
            UpdateVerticalPosition();
            UpdateHorizontalPosition();
        }

        private void UpdateHorizontalPosition()
        {
            if (GetAlignment().HasFlag(ItemAlignment.Left))
            {
                Area.SetX(GetX());
            }
            else if (GetAlignment().HasFlag(ItemAlignment.Right))
            {
                Area.SetX(GetX() + GetWidth() - Area.GetWidth());
            }
            else if (GetAlignment().HasFlag(ItemAlignment.HCenter))
            {
                int x = GetX();
                int w = Area.GetWidth();
                Area.SetX((GetWidth() - w) / 2 + x);
            }
        }
        private void UpdateVerticalPosition()
        {
            if (GetAlignment().HasFlag(ItemAlignment.Top))
            {
                Area.SetY(GetY());
            }
            else if (GetAlignment().HasFlag(ItemAlignment.Bottom))
            {
                Area.SetY(GetY() + GetHeight() - Area.GetHeight());
            }
            else if (GetAlignment().HasFlag(ItemAlignment.VCenter))
            {
                int y = GetY();
                int h = Area.GetHeight();
                Area.SetY((GetHeight() - h) / 2 + y);
            }
        }
    }
}
