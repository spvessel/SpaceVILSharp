using System;
using System.Drawing;
using System.Timers;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// PopUpMessage is designed to display short quick messages to the user. 
    /// PopUpMessage disappears after a specified period of time (default: 2 seconds), 
    /// or you can prevent this by moving the cursor over PopUpMessage and closing 
    /// it later manually.
    /// <para/> Contains text, close button.
    /// <para/> Supports all events except drag and drop.
    /// <para/> By default PopUpMessage do not pass further 
    /// any input events (mouse, keyboard and etc.).
    /// </summary>
    public class PopUpMessage : Prototype
    {
        static int count = 0;
        private Label _textObject;
        private ButtonCore _btnClose;
        internal Timer _stop;
        private int _timeout = 2000;
        internal bool _holded = false;

        /// <summary>
        /// Setting waiting time in milliseconds after which PopUpMessage will be closed.
        /// <para/>Default: 2000 milliseconds (2 seconds).
        /// </summary>
        /// <param name="milliseconds">Waiting time in milliseconds.</param>
        public void SetTimeOut(int milliseconds)
        {
            _timeout = milliseconds;
        }
        /// <summary>
        /// Getting current waiting time in milliseconds after which PopUpMessage will be closed.
        /// </summary>
        /// <returns>Current waiting time in milliseconds.</returns>
        public int GetTimeOut()
        {
            return _timeout;
        }

        /// <summary>
        /// Constructs a PopUpMessage with text message.
        /// </summary>
        public PopUpMessage(String message)
        {
            SetItemName("PopUpMessage_" + count);

            _btnClose = new ButtonCore();
            _btnClose.SetItemName("ClosePopUp");
            _textObject = new Label();
            _textObject.SetText(message);
            count++;
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.PopUpMessage)));
            SetPassEvents(false);
        }

        /// <summary>
        /// Setting alignment of the text. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Text alignment as SpaceVIL.Core.ItemAlignment.</param>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _textObject.SetTextAlignment(alignment);
        }
        /// <summary>
        /// Setting alignment of the text. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Text alignment as sequence of SpaceVIL.Core.ItemAlignment.</param>
        public void SetTextAlignment(params ItemAlignment[] alignment)
        {
            _textObject.SetTextAlignment(alignment);
        }
        /// <summary>
        /// Setting indents for the text to offset text relative to this PopUpMessage.
        /// </summary>
        /// <param name="margin">Indents as SpaceVIL.Decorations.Indents.</param>
        public void SetTextMargin(Indents margin)
        {
            _textObject.SetMargin(margin);
        }
        /// <summary>
        /// Setting indents for the text to offset text relative to PopUpMessage.
        /// </summary>
        /// <param name="left">Indent on the left.</param>
        /// <param name="top">Indent on the top.</param>
        /// <param name="right">Indent on the right.</param>
        /// <param name="bottom">Indent on the bottom.</param>
        public void SetTextMargin(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            _textObject.SetMargin(left, top, right, bottom);
        }
        /// <summary>
        /// Getting indents of the text.
        /// </summary>
        /// <returns>Indents as SpaceVIL.Decorations.Indents.</returns>
        public Indents GetTextMargin()
        {
            return _textObject.GetMargin();
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
        /// Setting the text.
        /// </summary>
        /// <param name="text">Text as System.String.</param>
        public virtual void SetText(String text)
        {
            _textObject.SetText(text);
        }
        /// <summary>
        /// Getting the current text of the PopUpMessage.
        /// </summary>
        /// <returns>Text as System.String.</returns>
        public virtual String GetText()
        {
            return _textObject.GetText();
        }
        /// <summary>
        /// Getting the text width (useful when you need resize PopUpMessage by text width).
        /// </summary>
        /// <returns>Text width.</returns>
        public int GetTextWidth()
        {
            return _textObject.GetWidth();
        }
        /// <summary>
        /// Getting the text height (useful when you need resize PopUpMessage by text height).
        /// </summary>
        /// <returns>Text height.</returns>
        public int GetTextHeight()
        {
            return _textObject.GetHeight();
        }
        /// <summary>
        /// Setting text color of a PopUpMessage.
        /// </summary>
        /// <param name="color">Color as System.Drawing.Color.</param>
        public void SetForeground(Color color)
        {
            _textObject.SetForeground(color);
        }
        /// <summary>
        /// Setting text color of a PopUpMessage in byte RGB format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0 - 255)</param>
        /// <param name="g">Green bits of a color. Range: (0 - 255)</param>
        /// <param name="b">Blue bits of a color. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b)
        {
            _textObject.SetForeground(r, g, b);
        }
        /// <summary>
        /// Setting text color of a PopUpMessage in byte RGBA format.
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
        /// Setting text color of a PopUpMessage in float RGB format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue bits of a color. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b)
        {
            _textObject.SetForeground(r, g, b);
        }
        /// <summary>
        /// Setting text color of a PopUpMessage in float RGBA format.
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
        /// Initializing indicator and text in the PopUpMessage. 
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            _btnClose.EventMouseClick += (sender, args) => RemoveSelf();
            //adding
            AddItems(_textObject, _btnClose);
        }

        private CoreWindow _handler = null;
        /// <summary>
        /// Shows PopUpMessage and attaches it to the specified window 
        /// (see SpaceVIL.CoreWindow, SpaceVIL.ActiveWindow, SpaceVIL.DialogWindow).
        /// </summary>
        /// <param name="handler">Window for attaching PopUpMessage.</param>
        public void Show(CoreWindow handler)
        {
            _handler = handler;
            _handler.AddItem(this);
            InitTimer();
        }

        internal void InitTimer()
        {
            if (_stop != null)
                return;

            _stop = new System.Timers.Timer(_timeout);
            _stop.Elapsed += (sender, e) =>
            {
                RemoveSelf();
            };
            _stop.Start();
        }

        private void RemoveSelf()
        {
            if (_stop != null)
            {
                _stop.Stop();
                _stop.Dispose();
                _stop = null;
            }
            _handler.RemoveItem(this);
        }

        internal void HoldSelf(bool ok)
        {
            _holded = ok;
            if (_stop != null)
            {
                _stop.Stop();
                _stop.Dispose();
                _stop = null;
            }
        }

        /// <summary>
        /// Setting style of the PopUpMessage.
        /// <para/> Inner styles: "closebutton".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        //style
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
            SetForeground(style.Foreground);
            SetFont(style.Font);
            SetTextAlignment(style.TextAlignment);

            Style inner_style = style.GetInnerStyle("closebutton");
            if (inner_style != null)
            {
                _btnClose.SetStyle(inner_style);
            }
        }
    }
}