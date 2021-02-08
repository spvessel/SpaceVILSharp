using System;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;

namespace SpaceVIL
{
    /// <summary>
    /// ImageItem is class for rendering loaded images. 
    /// <para/> Supports all events except drag and drop.
    /// </summary>
    public class ImageItem : Prototype, IImageItem
    {
        private Area Area = new Area();
        /// <summary>
        /// Getting bounds for an image (for example: to keep aspect ratio).
        /// </summary>
        /// <returns>Bounds as SpaceVIL.Core.Area.</returns>
        public Area GetAreaBounds()
        {
            return Area;
        }

        static int count = 0;
        private Bitmap _image = null;
        /// <summary>
        /// Getting a bitmap image in the form as System.Drawing.Bitmap.
        /// </summary>
        /// <returns>Image as System.Drawing.Bitmap.</returns>
        public Bitmap GetImage()
        {
            return _image;
        }
        private String _url;
        private float _angleRotation = 0.0f;
        /// <summary>
        /// Setting rotation angle for image.
        /// <para/> Default: 0.
        /// </summary>
        /// <param name="angle">Rotation angle.</param>
        public void SetRotationAngle(float angle)
        {
            _angleRotation = angle * (float)Math.PI / 180.0f;
        }
        /// <summary>
        /// Getting rotation angle for image.
        /// </summary>
        /// <returns>Rotation angle.</returns>
        public float GetRotationAngle()
        {
            return _angleRotation;
        }
        /// <summary>
        /// Property to enable or disable mouse events (hover, click, press, scroll).
        /// <para/> True: ImageItem can receive mouse events. False: cannot receive mouse events.
        /// <para/> Default: False.
        /// </summary>
        public bool IsHover = false;

        protected internal override bool GetHoverVerification(float xpos, float ypos)
        {
            if (IsHover)
                return base.GetHoverVerification(xpos, ypos);
            return false;
        }

        /// <summary>
        /// Default ImageItem constructor. Does not contains any image.
        /// </summary>
        public ImageItem()
        {
            SetItemName("Image_" + count);
            count++;
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.ImageItem)));
        }

        /// <summary>
        /// Constructs an ImageItem with an bitmap image.
        /// </summary>
        /// <param name="picture">Bitmap image as System.Drawing.Bitmap.</param>
        public ImageItem(Bitmap picture) : this()
        {
            if (picture == null)
                return;
            SetImage(picture);
        }
        /// <summary>
        /// Constructs an ImageItem with an bitmap image 
        /// with the ability to enable or disable mouse events.
        /// </summary>
        /// <param name="picture">Bitmap image as System.Drawing.Bitmap.</param>
        /// <param name="hover">True: ImageItem can receive mouse events. 
        /// False: cannot receive mouse events.</param>
        public ImageItem(Bitmap picture, bool hover) : this(picture)
        {
            IsHover = hover;
        }


        // public byte[] GetPixMapImage()
        // {
        //     // return _bitmap;
        //     return null;
        // }

        // protected virtual byte[] CreateByteImage(Bitmap picture)
        // {
        //     try
        //     {
        //         _imageWidth = picture.Width;
        //         _imageHeight = picture.Height;
        //         List<byte> result = new List<byte>();
        //         for (int j = picture.Height - 1; j >= 0; j--)
        //         {
        //             for (int i = 0; i < picture.Width; i++)
        //             {
        //                 Color pixel = picture.GetPixel(i, j);
        //                 result.Add(pixel.R);
        //                 result.Add(pixel.G);
        //                 result.Add(pixel.B);
        //                 result.Add(pixel.A);
        //             }
        //         }
        //         picture.Dispose();
        //         return result.ToArray();
        //     }
        //     catch (System.Exception ex)
        //     {
        //         Console.WriteLine("Create byte image" + ex.StackTrace);
        //         return null;
        //     }
        // }

        private int _imageWidth;
        private int _imageHeight;

        /// <summary>
        /// Getting an image width.
        /// </summary>
        /// <returns>Image width.</returns>
        public int GetImageWidth()
        {
            return _imageWidth;
        }
        /// <summary>
        /// Getting an image height.
        /// </summary>
        /// <returns>Image height.</returns>
        public int GetImageHeight()
        {
            return _imageHeight;
        }

        /// <summary>
        /// Setting new bitmap image of ImageItem.
        /// </summary>
        /// <param name="image">New bitmap image as System.Drawing.Bitmap.</param>
        public void SetImage(Bitmap image)
        {
            if (image == null)
                return;

            _image = (Bitmap)image.Clone();
            _imageWidth = image.Width;
            _imageHeight = image.Height;
            if (_isKeepAspectRatio && _image != null)
                ApplyAspectRatio();
            UpdateLayout();
        }

        private bool _isOverlay = false;
        private Color _colorOverlay;
        /// <summary>
        /// Getting color overlay (useful in images that have alpha channel).
        /// </summary>
        /// <returns>Color overlay as System.Drawing.Color.</returns>
        public Color GetColorOverlay()
        {
            return _colorOverlay;
        }
        /// <summary>
        /// Setting color overlay (useful in images that have alpha channel).
        /// </summary>
        /// <param name="color">Color overlay as System.Drawing.Color.</param>
        public void SetColorOverlay(Color color)
        {
            _colorOverlay = color;
            _isOverlay = true;
        }
        /// <summary>
        /// Setting color overlay (useful in images that have alpha channel) 
        /// with ability to specify overlay status.
        /// </summary>
        /// <param name="color">Color overlay as System.Drawing.Color.</param>
        /// <param name="overlay">True: if color overlay is using. 
        /// False: if color overlay is not using.</param>
        public void SetColorOverlay(Color color, bool overlay)
        {
            _colorOverlay = color;
            _isOverlay = overlay;
        }
        /// <summary>
        /// Setting color overlay status.
        /// </summary>
        /// <param name="overlay">True: if color overlay is using. 
        /// False: if color overlay is not using.</param>
        public void SetColorOverlay(bool overlay)
        {
            _isOverlay = overlay;
        }
        /// <summary>
        /// Getting color overlay status.
        /// </summary>
        /// <returns>True: if color overlay is using. 
        /// False: if color overlay is not using.</returns>
        public bool IsColorOverlay()
        {
            return _isOverlay;
        }

        private bool _isKeepAspectRatio = false;
        /// <summary>
        /// Setting value to keep or not to keep еру aspect ratio of the image.
        /// </summary>
        /// <param name="value">True: to keep aspect ratio of the image. 
        /// False: to not keep aspect ratio of the image.</param>
        public void KeepAspectRatio(bool value)
        {
            _isKeepAspectRatio = value;
        }
        /// <summary>
        /// Returns True if aspect ratio of the image is kept otherwise returns False.
        /// </summary>
        /// <returns>True: if aspect ratio of the image is kept. 
        /// False: if aspect ratio of the image is not kept.</returns>
        public bool IsAspectRatio()
        {
            return _isKeepAspectRatio;
        }
        /// <summary>
        /// Setting ImageItem size (width and height).
        /// </summary>
        /// <param name="width"> Width of the ImageItem. </param>
        /// <param name="height"> Height of the ImageItem. </param>
        public override void SetSize(int width, int height)
        {
            this.SetWidth(width);
            this.SetHeight(height);
        }
        /// <summary>
        /// Setting ImageItem height. If the value is greater/less than the maximum/minimum 
        /// value of the height, then the height becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="height"> Height of the ImageItem. </param>
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            Area.SetHeight(height);
            if (_isKeepAspectRatio && _image != null)
                ApplyAspectRatio();
            UpdateLayout();
        }
        /// <summary>
        /// Setting ImageItem width. If the value is greater/less than the maximum/minimum 
        /// value of the width, then the width becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="width"> Width of the ImageItem. </param>
        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            Area.SetWidth(width);
            if (_isKeepAspectRatio && _image != null)
                ApplyAspectRatio();
            UpdateLayout();
        }
        /// <summary>
        /// Setting X coordinate of the left-top corner of the ImageItem.
        /// </summary>
        /// <param name="x">X position of the left-top corner.</param>
        public override void SetX(int x)
        {
            base.SetX(x);
            UpdateLayout();
        }
        /// <summary>
        /// Setting Y coordinate of the left-top corner of the ImageItem.
        /// </summary>
        /// <param name="y">Y position of the left-top corner.</param>
        public override void SetY(int y)
        {
            base.SetY(y);
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
        internal void UpdateLayout()
        {
            UpdateVerticalPosition();
            UpdateHorizontalPosition();
            ItemsRefreshManager.SetRefreshImage(this);
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
        /// <summary>
        /// Disposing bitmap resources if the item was removed.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void Release()
        {
            if (_image != null)
                _image.Dispose();
        }

        private ImageQuality _filter = ImageQuality.Smooth;
        /// <summary>
        /// Getting an image filtering quality.
        /// </summary>
        /// <returns>Image filtering quality as SpaceVIL.Core.ImageQuality.</returns>
        public ImageQuality GetImageQuality()
        {
            return _filter;
        }

        /// <summary>
        /// Setting an image filtering quality.
        /// </summary>
        /// <param name="quality">Image filtering quality as SpaceVIL.Core.ImageQuality.</param>
        public void SetImageQuality(ImageQuality quality)
        {
            _filter = quality;
        }
    }
}
