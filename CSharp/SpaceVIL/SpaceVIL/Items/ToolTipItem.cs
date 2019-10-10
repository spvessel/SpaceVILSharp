using System;
using System.Drawing;
using System.Timers;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;
using System.Collections.Generic;

namespace SpaceVIL
{
    public sealed class ToolTipItem : Prototype, IFloating
    {
        private Label _textObject;
        internal Label GetTextLabel()
        {
            return _textObject;
        }
        internal Timer _stop;
        private int _timeout = 500;
        internal void SetTimeOut(int milliseconds)
        {
            _timeout = milliseconds;
        }
        internal int GetTimeOut()
        {
            return _timeout;
        }

        // private static ToolTip _instance = null;

        internal ToolTipItem()
        {
            SetVisible(false);
            _textObject = new Label();
            SetItemName("ToolTip");
            IsFocusable = false;
            _queue = new Queue<IBaseItem>();
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.ToolTipItem)));
        }

        internal bool IsInit = false;

        public override void InitElements()
        {
            ItemsLayoutBox.AddItem(GetHandler(), this, LayoutType.Floating);
            AddItem(_textObject);
            if (_queue.Count != 0)
            {
                foreach (IBaseItem item in _queue)
                {
                    base.AddItem(item);
                }
            }
            IsInit = true;
        }

        private Queue<IBaseItem> _queue = null;

        public override void AddItems(params IBaseItem[] items)
        {
            if (IsInit)
                return;
            foreach (IBaseItem item in items)
            {
                _queue.Enqueue(item);
            }
        }

        internal void InitTimer(bool value)
        {
            if (value)
            {
                if (_stop != null)
                    return;

                if (_timeout == 0)
                    SetVisible(true);
                else
                {

                    _stop = new System.Timers.Timer(_timeout);
                    _stop.Elapsed += (sender, e) => VisibleSelf();
                    _stop.Start();
                }
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
        }

        internal void SetTextAlignment(ItemAlignment alignment)
        {
            _textObject.SetTextAlignment(alignment);
        }
        internal void SetTextAlignment(params ItemAlignment[] alignment)
        {
            _textObject.SetTextAlignment(alignment);
        }
        internal void SetFont(Font font)
        {
            _textObject.SetFont(font);
        }
        internal void SetFontSize(int size)
        {
            _textObject.SetFontSize(size);
        }
        internal void SetFontStyle(FontStyle style)
        {
            _textObject.SetFontStyle(style);
        }
        internal void SetFontFamily(FontFamily font_family)
        {
            _textObject.SetFontFamily(font_family);
        }
        internal Font GetFont()
        {
            return _textObject.GetFont();
        }
        internal void SetText(String text)
        {
            _textObject.SetText(text);
        }
        internal String GetText()
        {
            return _textObject.GetText();
        }
        internal int GetTextWidth()
        {
            return _textObject.GetTextWidth();
        }
        internal int GetTextHeight()
        {
            return _textObject.GetTextHeight();
        }
        internal void SetForeground(Color color)
        {
            _textObject.SetForeground(color);
        }
        internal Color GetForeground()
        {
            return _textObject.GetForeground();
        }

        //style
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);

            Style innerStyle = style.GetInnerStyle("text");
            if (innerStyle != null)
                _textObject.SetStyle(innerStyle);

            SetForeground(style.Foreground);
            SetFont(style.Font);
            SetTextAlignment(style.TextAlignment);
        }

        public void Show()
        {
        }

        public void Show(IItem sender, MouseArgs args)
        {
        }

        public void Hide()
        {
        }

        public void Hide(MouseArgs args)
        {
        }

        public bool IsOutsideClickClosable()
        {
            return false;
        }

        public void SetOutsideClickClosable(bool value)
        {
        }
    }
}