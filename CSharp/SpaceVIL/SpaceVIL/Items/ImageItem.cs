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
        private RectangleBounds Area = new RectangleBounds();

        public RectangleBounds GetRectangleBounds()
        {
            return Area;
        }

        static int count = 0;
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

        public bool IsHover = false;

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
            _bitmap = CreateByteImage(picture);
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
            return _bitmap;
        }

        private byte[] CreateByteImage(Bitmap picture)
        {
            try
            {
                _imageWidth = picture.Width;
                _imageHeight = picture.Height;
                List<byte> result = new List<byte>();
                for (int j = picture.Height - 1; j >= 0; j--)
                {
                    for (int i = 0; i < picture.Width; i++)
                    {
                        Color pixel = picture.GetPixel(i, j);
                        result.Add(pixel.R);
                        result.Add(pixel.G);
                        result.Add(pixel.B);
                        result.Add(pixel.A);
                    }
                }
                picture.Dispose();
                SetNew(true);
                return result.ToArray();
            }
            catch (System.Exception ex)
            {
                Console.WriteLine("Create byte image" + ex.StackTrace);
                return null;
            }
        }

        private int _imageWidth;
        private int _imageHeight;

        /// <returns> width of the image in the ImageItem </returns>
        public int GetImageWidth()
        {
            return _imageWidth;
        }

        /// <returns> height of the image in the ImageItem </returns>
        public int GetImageHeight()
        {
            return _imageHeight;
        }

        /// <summary>
        /// Set an image into the ImageItem
        /// </summary>
        public void SetImage(Bitmap image)
        {
            if (image == null)
                return;
            _bitmap = CreateByteImage(image);
            if (_isKeepAspectRatio && _bitmap != null)
                ApplyAspectRatio();
            UpdateLayout();
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
        public void SetColorOverlay(Color color, bool overlay)
        {
            _colorOverlay = color;
            _isOverlay = overlay;
        }
        public void SetColorOverlay(bool overlay)
        {
            _isOverlay = overlay;
        }
        public bool IsColorOverlay()
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
            if (_isKeepAspectRatio && _bitmap != null)
                ApplyAspectRatio();
            UpdateLayout();
        }

        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            Area.SetWidth(width);
            if (_isKeepAspectRatio && _bitmap != null)
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
            float boundW = GetWidth();
            float boundH = GetHeight();

            var ratioX = (boundW / _imageWidth);
            var ratioY = (boundH / _imageHeight);
            float ratio = ratioX < ratioY ? ratioX : ratioY;

            int resH = (int)(_imageHeight * ratio);
            int resW = (int)(_imageWidth * ratio);
            Area.SetWidth(resW);
            Area.SetHeight(resH);
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

        public override void Release()
        {
            VRAMStorage.AddToDelete(this);
        }

        //подумать над общим решением
        private Object _lock = new Object();
        private bool _isNew = true;
        internal bool IsNew()
        {
            Monitor.Enter(_lock);
            try
            {
                return _isNew;
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }
        internal void SetNew(bool value)
        {
            Monitor.Enter(_lock);
            try
            {
                _isNew = value;
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }
    }
}
