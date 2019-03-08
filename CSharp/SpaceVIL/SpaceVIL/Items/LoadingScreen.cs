using System;
using System.Drawing;
using System.Threading;
using System.Threading.Tasks;
using SpaceVIL;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    class LoadingScreen : Prototype
    {
        static int count = 0;
        private ImageItem _loadIcon;
        public void SetImage(Bitmap image)
        {
            _loadIcon = new ImageItem(image);
        }
        private Label _text_object;
        public void SetValueVisibility(bool visibility)
        {
            _text_object.SetVisible(visibility);
        }
        public bool IsValueVisible()
        {
            return _text_object.IsVisible();
        }
        
        private WindowLayout _handler = null;

        public LoadingScreen()
        {
            SetItemName("LoadingScreen_" + count++);
            SetPassEvents(false);
            _loadIcon = new ImageItem();
            if (_loadIcon.GetImage() == null)
                _loadIcon.SetImage(DefaultsService.GetDefaultImage(EmbeddedImage.LoadCircle, EmbeddedImageSize.Size64x64));
            _text_object = new Label("0%");
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.LoadingScreen)));

            // EventKeyPress += (s, a) =>
            // {
            //     if (a.Key == KeyCode.NumpadAdd)
            //         SetValue(GetValue() + 1);
            //     else if (a.Key == KeyCode.NumpadSubtract)
            //         SetValue(GetValue() - 1);
            //     else if (a.Key == KeyCode.Escape)
            //         SetToClose();
            // };
        }

        public override void InitElements()
        {
            _loadIcon.IsHover = false;
            base.AddItems(_loadIcon, _text_object);
        }

        private int _percent = 0;
        public void SetValue(int value)
        {
            if (value == _percent)
                return;
            _percent = value;
            if (_percent > 100)
                _percent = 100;
            if (_percent < 0)
                _percent = 0;
            _text_object.SetText(_percent.ToString() + "%");
        }

        public int GetValue()
        {
            return _percent;
        }


        Object _locker = new Object();
        private bool _isShouldClose = false;
        public void SetToClose()
        {
            Monitor.Enter(_locker);
            try
            {
                _isShouldClose = true;
            }
            catch (Exception ex)
            {
                Console.WriteLine("Method - SetToClose");
                Console.WriteLine(ex.StackTrace);
                _isShouldClose = true;
            }
            finally
            {
                Monitor.Exit(_locker);
            }
        }

        private bool IsOnClose()
        {
            Monitor.Enter(_locker);
            try
            {
                return _isShouldClose;
            }
            catch (Exception ex)
            {
                Console.WriteLine("Method - IsOnClose");
                Console.WriteLine(ex.StackTrace);
                return true;
            }
            finally
            {
                Monitor.Exit(_locker);
            }
        }

        public void Show(WindowLayout handler)
        {
            _handler = handler;
            _handler.AddItem(this);
            _handler.SetFocusedItem(this);
            RedrawFrequency tmp = _handler.GetRenderFrequency();
            if (tmp != RedrawFrequency.High)
                _handler.SetRenderFrequency(RedrawFrequency.High);
            Task thread = new Task(() =>
            {
                int alpha = 360;
                while (!IsOnClose())
                {
                    _loadIcon.SetRotationAngle(alpha);
                    alpha--;
                    if (alpha == 0)
                        alpha = 360;
                    Thread.Sleep(2);
                }
                Close();
                if (tmp != RedrawFrequency.High)
                    _handler.SetRenderFrequency(tmp);
            });
            thread.Start();
        }

        private void Close()
        {
            _handler.GetWindow().RemoveItem(this);
        }

        /// <summary>
        /// Text font parameters in the LoadingScreen
        /// </summary>
        public void SetFont(Font font)
        {
            _text_object.SetFont(font);
        }
        public void SetFontSize(int size)
        {
            _text_object.SetFontSize(size);
        }
        public void SetFontStyle(FontStyle style)
        {
            _text_object.SetFontStyle(style);
        }
        public void SetFontFamily(FontFamily font_family)
        {
            _text_object.SetFontFamily(font_family);
        }
        public Font GetFont()
        {
            return _text_object.GetFont();
        }

        /// <summary>
        /// Text color in the LoadingScreen
        /// </summary>
        public void SetForeground(Color color)
        {
            _text_object.SetForeground(color);
        }
        public void SetForeground(int r, int g, int b)
        {
            _text_object.SetForeground(r, g, b);
        }
        public void SetForeground(int r, int g, int b, int a)
        {
            _text_object.SetForeground(r, g, b, a);
        }
        public void SetForeground(float r, float g, float b)
        {
            _text_object.SetForeground(r, g, b);
        }
        public void SetForeground(float r, float g, float b, float a)
        {
            _text_object.SetForeground(r, g, b, a);
        }
        public Color GetForeground()
        {
            return _text_object.GetForeground();
        }

        //style
        /// <summary>
        /// Set style of the LoadingScreen
        /// </summary>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
            //common
            SetForeground(style.Foreground);
            SetFont(style.Font);
            //parts
            _text_object.SetStyle(style.GetInnerStyle("text"));
            _loadIcon.SetStyle(style.GetInnerStyle("image"));
        }
    }
}