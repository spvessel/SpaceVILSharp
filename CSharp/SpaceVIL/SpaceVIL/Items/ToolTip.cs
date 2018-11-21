using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Timers;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class ToolTip : Prototype, IToolTip
    {
        private TextLine _text_object;
        internal TextLine GetTextLine()
        {
            return _text_object;
        }
        internal Timer _stop;
        private int _timeout = 300;
        public void SetTimeOut(int milliseconds)
        {
            _timeout = milliseconds;
        }
        public int GetTimeOut()
        {
            return _timeout;
        }
        public ToolTip()
        {
            SetVisible(false);
            _text_object = new TextLine();
            SetItemName("ToolTip");

            // SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.ToolTip)));
            SetStyle(Style.GetToolTipStyle());
            IsFocusable = false;
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
        public int GetTextWidth()
        {
            return _text_object.GetWidth();
        }
        public void SetForeground(Color color)
        {
            _text_object.SetForeground(color);
        }
        public Color GetForeground()
        {
            return _text_object.GetForeground();
        }

        public override void InitElements()
        {
            //text
            // _text_object.SetTextAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
            // _text_object.SetFont(new Font(new FontFamily("Courier New"), 14, FontStyle.Regular));
        }

        internal void InitTimer(bool value)
        {
            if (value)
            {
                if (_stop != null)
                    return;

                _stop = new System.Timers.Timer(_timeout);
                _stop.Elapsed += (sender, e) => VisibleSelf();
                _stop.Start();
            }
            else
            {
                SetVisible(false);

                if (_stop == null)
                    return;

                _stop.Stop();
                _stop.Dispose();
                _stop = null;
            }
        }

        private void VisibleSelf()
        {
            SetVisible(true);

            _stop.Stop();
            _stop.Dispose();
            _stop = null;

            //GetHandler().UpdateScene();
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
        }
    }
}