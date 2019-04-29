using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class Tab : Prototype
    {
        static int count = 0;
        internal Frame View;
        private Label _text_object;
        private ButtonCore _close;
        public ButtonCore GetCloseButton()
        {
            return _close;
        }

        private bool _isClosable = false;

        public void SetClosable(bool value)
        {
            if (_isClosable == value)
                return;
            _isClosable = value;
            _close.SetVisible(_isClosable);
            UpdateTabWidth();
        }

        public bool IsClosable()
        {
            return _isClosable;
        }

        public Tab(String text) : this(text, text) { }
        public Tab(String text, String name) : this()
        {
            SetItemName(name);
            SetText(text);
        }
        public Tab() : base()
        {
            SetItemName("Tab_" + count++);
            _close = new ButtonCore();
            _text_object = new Label();
            View = new Frame();
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.Tab)));
        }

        //private for class
        private bool _toggled;

        /// <summary>
        /// Is ButtonToggle toggled (boolean)
        /// </summary>
        public bool IsToggled()
        {
            return _toggled;
        }
        public void SetToggled(bool value)
        {
            _toggled = value;
            if (value == true)
                SetState(ItemStateType.Toggled);
            else
                SetState(ItemStateType.Base);
        }

        public EventMouseMethodState EventToggle;
        internal EventCommonMethod EventRemoved;
        public override void Release()
        {
            EventToggle = null;
            EventRemoved = null;
        }

        //text init
        /// <summary>
        /// Text alignment in the ButtonToggle
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
        /// Text margin in the ButtonToggle
        /// </summary>
        public void SetTextMargin(Indents margin)
        {
            _text_object.SetMargin(margin);
        }
        public Indents GetTextMargin()
        {
            return _text_object.GetMargin();
        }

        /// <summary>
        /// Text font parameters in the ButtonToggle
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
        public virtual void SetText(String text)
        {
            _text_object.SetText(text);
            UpdateTabWidth();
        }
        public virtual String GetText()
        {
            return _text_object.GetText();
        }
        public int GetTextWidth()
        {
            return _text_object.GetTextWidth();
        }
        public int GetTextHeight()
        {
            return _text_object.GetTextHeight();
        }
        /// <summary>
        /// Text color in the ButtonToggle
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

        private int _labelRightMargin = 0;

        private void UpdateTabWidth()
        {
            if (GetWidthPolicy() == SizePolicy.Fixed)
            {
                int w = GetPadding().Left + GetTextWidth() + GetPadding().Right;
                if (_isClosable)
                {
                    w += GetSpacing().Horizontal + _close.GetWidth();
                    ApplyRightTextMargin(GetSpacing().Horizontal + _close.GetWidth());
                }
                else
                {
                    ApplyRightTextMargin(_labelRightMargin);
                }
                SetWidth(w);
            }
            else
            {
                if (_isClosable)
                    ApplyRightTextMargin(GetSpacing().Horizontal + _close.GetWidth());
                else
                    ApplyRightTextMargin(_labelRightMargin);
            }
        }

        private void ApplyRightTextMargin(int value)
        {
            _text_object.SetMargin(
                _text_object.GetMargin().Left,
                _text_object.GetMargin().Top,
                value,
                _text_object.GetMargin().Bottom
                );
        }

        public override void InitElements()
        {
            base.InitElements();
            _close.SetVisible(_isClosable);
            AddItems(_text_object, _close);
            EventToggle = null;
            EventToggle += (sender, args) =>
            {
                if (IsToggled())
                    return;
                SetToggled(true);
            };

            _close.IsFocusable = false;
            _close.EventMouseClick += (sender, args) =>
            {
                RemoveTab();
            };
        }

        public void RemoveTab()
        {
            EventRemoved?.Invoke();
        }

        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);

            Style inner_style = style.GetInnerStyle("closebutton");
            if (inner_style != null)
                _close.SetStyle(inner_style);
            inner_style = style.GetInnerStyle("view");
            if (inner_style != null)
                View.SetStyle(inner_style);
            inner_style = style.GetInnerStyle("text");
            if (inner_style != null)
            {
                _text_object.SetStyle(inner_style);
                _labelRightMargin = _text_object.GetMargin().Right;
            }

            SetForeground(style.Foreground);
            SetFont(style.Font);
            SetTextAlignment(style.TextAlignment);
        }
    }
}