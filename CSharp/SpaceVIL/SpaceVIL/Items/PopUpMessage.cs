using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Timers;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class PopUpMessage : Prototype
    {
        static int count = 0;
        private Label _text_object;
        private ButtonCore _btn_close;
        internal Timer _stop;
        private int _timeout = 2000;
        internal bool _holded = false;

        /// <summary>
        /// PopUpMessage timeout in milliseconds
        /// </summary>
        public void SetTimeOut(int milliseconds)
        {
            _timeout = milliseconds;
        }
        public int GetTimeOut()
        {
            return _timeout;
        }

        /// <summary>
        /// Constructs a PopUpMessage with message and parent window (handler)
        /// </summary>
        public PopUpMessage(String message)
        {
            SetItemName("PopUpMessage_" + count);

            _btn_close = new ButtonCore();
            _btn_close.SetItemName("ClosePopUp");
            _text_object = new Label();
            _text_object.SetText(message);
            count++;
            SetShadow(10, 3, 3, Color.FromArgb(140, 0, 0, 0));
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.PopUpMessage)));
            SetPassEvents(false);
        }

        /// <summary>
        /// Text alignment in the PopUpMessage
        /// </summary>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _text_object.SetTextAlignment(alignment);
        }
        public void SetTextAlignment(params ItemAlignment[] alignment)
        {
            _text_object.SetTextAlignment(alignment);
        }

        /// <summary>
        /// Text margin in the PopUpMessage
        /// </summary>
        public void SetTextMargin(Indents margin)
        {
            _text_object.SetMargin(margin);
        }

        /// <summary>
        /// Text font parameters in the PopUpMessage
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
        /// Set text in the PopUpMessage
        /// </summary>
        public void SetText(String text)
        {
            _text_object.SetText(text);
        }

        /// <summary>
        /// Get the PopUpMessage text
        /// </summary>
        public String GetText()
        {
            return _text_object.GetText();
        }

        /// <summary>
        /// Text color in the PopUpMessage
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

        /// <summary>
        /// Initialization and adding of all elements in the PopUpMessage
        /// </summary>
        public override void InitElements()
        {
            _btn_close.EventMouseClick += (sender, args) => RemoveSelf();
            //adding
            AddItems(_text_object, _btn_close);
        }

        private CoreWindow _handler = null;
        /// <summary>
        /// Show the PopUpMessage
        /// </summary>
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
        /// Set style of the PopUpMessage
        /// </summary>
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
                _btn_close.SetStyle(inner_style);
            }
        }
    }
}