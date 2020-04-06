using System;
using System.Drawing;
using System.Threading;
using System.Threading.Tasks;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// LoadingScreen is designed to lock the entire window 
    /// to prevent all input events during the execution of any long time task.
    /// <para/> Contains image and text.
    /// <para/> Supports all events except drag and drop.
    /// </summary>
    public class LoadingScreen : Prototype
    {
        static int count = 0;
        private ImageItem _loadIcon;
        /// <summary>
        /// Setting an image that should let the user know that 
        /// another task is not yet complete, and the user must wait.
        /// </summary>
        /// <param name="image">Image as System.Drawing.Bitmap.</param>
        public void SetImage(Bitmap image)
        {
            _loadIcon = new ImageItem(image);
        }
        private Label _textObject;
        /// <summary>
        /// Setting the text that represents the progress of the unfinished task, 
        /// visible or invisible.
        /// </summary>
        /// <param name="value">True: if text should be visible. 
        /// False: if text should be invisible.</param>
        public void SetValueVisible(bool value)
        {
            _textObject.SetVisible(value);
        }
        /// <summary>
        /// Returns True if text that represents the progress of 
        /// the unfinished task is visible, otherwise returns False.
        /// </summary>
        /// <returns>True: if text is visible. 
        /// False: if text is invisible.</returns>
        public bool IsValueVisible()
        {
            return _textObject.IsVisible();
        }

        private CoreWindow _handler = null;
        /// <summary>
        /// Default LoadingScreen constructor.
        /// </summary>
        public LoadingScreen()
        {
            SetItemName("LoadingScreen_" + count++);
            SetPassEvents(false);
            _loadIcon = new ImageItem();
            if (_loadIcon.GetImage() == null)
                _loadIcon.SetImage(DefaultsService.GetDefaultImage(EmbeddedImage.LoadCircle, EmbeddedImageSize.Size64x64));
            _textObject = new Label("0%");
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.LoadingScreen)));

            EventKeyPress += (s, a) =>
            {
                if (a.Key == KeyCode.Escape && a.Mods == KeyMods.Shift)
                    SetToClose();
            };
        }
        /// <summary>
        /// Initializing all elements in the LoadingScreen. 
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            _loadIcon.IsHover = false;
            base.AddItems(_loadIcon, _textObject);
        }

        private int _percent = 0;
        /// <summary>
        /// Setting the progress value of the unfinished task.
        /// </summary>
        /// <param name="value">Progress value of the unfinished task.</param>
        public void SetCurrentValue(int value)
        {
            if (value == _percent)
                return;
            _percent = value;
            if (_percent > 100)
                _percent = 100;
            if (_percent < 0)
                _percent = 0;
            _textObject.SetText(_percent.ToString() + "%");
        }
        /// <summary>
        /// Getting the progress value of the unfinished task.
        /// </summary>
        /// <returns>Progress value of the unfinished task.</returns>
        public int GetCurrentValue()
        {
            return _percent;
        }

        private Object _locker = new Object();
        private bool _isShouldClose = false;

        /// <summary>
        /// Informing of LoadingScreen to closes.
        /// </summary>
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
        /// <summary>
        /// Shows LoadingScreen and attaches it to the specified window 
        /// (see SpaceVIL.CoreWindow, SpaceVIL.ActiveWindow, SpaceVIL.DialogWindow).
        /// </summary>
        /// <param name="handler">Window for attaching LoadingScreen.</param>
        public void Show(CoreWindow handler)
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
        /// <summary>
        /// Closes LoadingScreen.
        /// </summary>
        private void Close()
        {
            _handler.RemoveItem(this);
        }

        /// <summary>
        /// Setting font of the text.
        /// </summary>
        /// <param name="font">Font as System.Drawing.Font.</param>
        public void SetFont(Font font)
        {
            _textObject.SetFont(font);
        }
        /// <summary>
        /// Setting font size of the text.
        /// </summary>
        /// <param name="size">New size of the font.</param>
        public void SetFontSize(int size)
        {
            _textObject.SetFontSize(size);
        }
        /// <summary>
        /// Setting font style of the text.
        /// </summary>
        /// <param name="style">New font style as System.Drawing.FontStyle.</param>
        public void SetFontStyle(FontStyle style)
        {
            _textObject.SetFontStyle(style);
        }
        /// <summary>
        /// Setting new font family of the text.
        /// </summary>
        /// <param name="fontFamily">New font family as System.Drawing.FontFamily.</param>
        public void SetFontFamily(FontFamily fontFamily)
        {
            _textObject.SetFontFamily(fontFamily);
        }
        /// <summary>
        /// Getting the current font of the text.
        /// </summary>
        /// <returns>Font as System.Drawing.Font.</returns>
        public Font GetFont()
        {
            return _textObject.GetFont();
        }

        /// <summary>
        /// Setting text color of a ButtonCore.
        /// </summary>
        /// <param name="color">Color as System.Drawing.Color.</param>
        public void SetForeground(Color color)
        {
            _textObject.SetForeground(color);
        }
        /// <summary>
        /// Setting text color of a ButtonCore in byte RGB format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0 - 255)</param>
        /// <param name="g">Green bits of a color. Range: (0 - 255)</param>
        /// <param name="b">Blue bits of a color. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b)
        {
            _textObject.SetForeground(r, g, b);
        }
        /// <summary>
        /// Setting text color of a ButtonCore in byte RGBA format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0 - 255)</param>
        /// <param name="g">Green bits of a color. Range: (0 - 255)</param>
        /// <param name="b">Blue bits of a color. Range: (0 - 255)</param>
        /// <param name="a">Alpha bits of a color. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b, int a)
        {
            _textObject.SetForeground(r, g, b, a);
        }
        /// <summary>
        /// Setting text color of a ButtonCore in float RGB format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue bits of a color. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b)
        {
            _textObject.SetForeground(r, g, b);
        }
        /// <summary>
        /// Setting text color of a ButtonCore in float RGBA format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="a">Alpha bits of a color. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b, float a)
        {
            _textObject.SetForeground(r, g, b, a);
        }
        /// <summary>
        /// Getting current text color.
        /// </summary>
        /// <returns>Text color as System.Drawing.Color.</returns>
        public Color GetForeground()
        {
            return _textObject.GetForeground();
        }

        /// <summary>
        /// Setting style of the LoadingScreen. 
        /// <para/> Inner styles: "text", "image".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
            //common
            SetForeground(style.Foreground);
            SetFont(style.Font);
            //parts
            _textObject.SetStyle(style.GetInnerStyle("text"));
            _loadIcon.SetStyle(style.GetInnerStyle("image"));
        }
    }
}