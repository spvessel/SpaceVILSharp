using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Timers;
namespace SpaceVIL
{
    public class PopUpMessage : VisualItem
    {
        static int count = 0;
        private TextLine _text_object;
        private ButtonCore _btn_close;
        internal Timer _stop;
        private int _timeout = 2000;
        internal bool _holded = false;
        public void SetTimeOut(int milliseconds)
        {
            _timeout = milliseconds;
        }
        public int GetTimeOut()
        {
            return _timeout;
        }
        public PopUpMessage(String message, WindowLayout handler)
        {
            SetItemName("PopUpMessage_" + count);

            _btn_close = new ButtonCore();
            _btn_close.SetItemName("ClosePopUp");
            _text_object = new TextLine();
            _text_object.SetItemText(message);
            count++;


            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.PopUpMessage)));
            handler.GetWindow().AddItem(this);
        }
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _text_object.SetTextAlignment(alignment);
        }
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
        public void SetText(String text)
        {
            _text_object.SetItemText(text);
        }
        public String GetText()
        {
            return _text_object.GetItemText();
        }
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

        public override void InitElements()
        {
            _btn_close.EventMouseClick += (sender, args) => RemoveSelf();
            //adding
            AddItems(_text_object, _btn_close);
        }

        public void Show()
        {
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
            GetParent().RemoveItem(this);
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