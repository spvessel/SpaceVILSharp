using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class Tab : Prototype, IDraggable
    {
        static int count = 0;
        internal Frame View;
        private Label _textLabel;
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
            _textLabel = new Label();
            _textLabel.IsHover = false;
            View = new Frame();
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.Tab)));

            //draggable tabs
            SetPassEvents(false,
                InputEventType.MouseDoubleClick,
                InputEventType.MousePress,
                InputEventType.MouseRelease);
            IsFocusable = false;

            EventMousePress += OnMousePress;
            EventMouseDrag += OnDragging;
        }

        private int _xClick = 0;
        private int _xDiff = 0;
        internal bool Dragging = false;

        private bool _isDraggable = true;

        public void SetDraggable(bool value)
        {
            _isDraggable = value;
        }

        public bool IsDraggable()
        {
            return _isDraggable;
        }

        void OnMousePress(IItem sender, MouseArgs args)
        {
            if (!IsDraggable())
                return;
            _xClick = args.Position.GetX();
            _xDiff = args.Position.GetX() - GetX();
        }

        void OnDragging(IItem sender, MouseArgs args)
        {
            if (!IsDraggable())
                return;
            if (Dragging)
            {
                Prototype parent = GetParent();
                int offset = args.Position.GetX() - parent.GetX() - _xDiff;
                int x = offset + parent.GetX();
                if (x <= parent.GetX())
                {
                    x = parent.GetX();
                }
                if (x >= parent.GetX() + parent.GetWidth() - GetWidth())
                {
                    x = parent.GetX() + parent.GetWidth() - GetWidth();
                }
                SetX(x);
            }
            else
            {
                if (Math.Abs(_xClick - args.Position.GetX()) <= 20)
                    return;
                Dragging = true;
            }
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
            if (value)
                SetState(ItemStateType.Toggled);
            else
                SetState(ItemStateType.Base);
        }

        public EventMouseMethodState EventOnSelect;
        public EventCommonMethod EventOnClose;
        internal EventCommonMethod EventTabRemove;
        public override void Release()
        {
            EventOnSelect = null;
            EventOnClose = null;
            EventTabRemove = null;
        }

        //text init
        /// <summary>
        /// Text alignment in the ButtonToggle
        /// </summary>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _textLabel.SetTextAlignment(alignment);
        }
        public void SetTextAlignment(params ItemAlignment[] alignment)
        {
            _textLabel.SetTextAlignment(alignment);
        }

        /// <summary>
        /// Text margin in the ButtonToggle
        /// </summary>
        public void SetTextMargin(Indents margin)
        {
            _textLabel.SetMargin(margin);
        }
        public Indents GetTextMargin()
        {
            return _textLabel.GetMargin();
        }

        /// <summary>
        /// Text font parameters in the ButtonToggle
        /// </summary>
        public void SetFont(Font font)
        {
            _textLabel.SetFont(font);
        }
        public void SetFontSize(int size)
        {
            _textLabel.SetFontSize(size);
        }
        public void SetFontStyle(FontStyle style)
        {
            _textLabel.SetFontStyle(style);
        }
        public void SetFontFamily(FontFamily font_family)
        {
            _textLabel.SetFontFamily(font_family);
        }
        public Font GetFont()
        {
            return _textLabel.GetFont();
        }
        public virtual void SetText(String text)
        {
            _textLabel.SetText(text);
            UpdateTabWidth();
        }
        public virtual String GetText()
        {
            return _textLabel.GetText();
        }
        public int GetTextWidth()
        {
            return _textLabel.GetTextWidth();
        }
        public int GetTextHeight()
        {
            return _textLabel.GetTextHeight();
        }
        /// <summary>
        /// Text color in the ButtonToggle
        /// </summary>
        public void SetForeground(Color color)
        {
            _textLabel.SetForeground(color);
        }
        public void SetForeground(int r, int g, int b)
        {
            _textLabel.SetForeground(r, g, b);
        }
        public void SetForeground(int r, int g, int b, int a)
        {
            _textLabel.SetForeground(r, g, b, a);
        }
        public void SetForeground(float r, float g, float b)
        {
            _textLabel.SetForeground(r, g, b);
        }
        public void SetForeground(float r, float g, float b, float a)
        {
            _textLabel.SetForeground(r, g, b, a);
        }
        public Color GetForeground()
        {
            return _textLabel.GetForeground();
        }

        private int _labelRightMargin = 0;

        internal void UpdateTabWidth()
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
            _textLabel.SetMargin(
                _textLabel.GetMargin().Left,
                _textLabel.GetMargin().Top,
                value,
                _textLabel.GetMargin().Bottom
                );
        }

        public override void InitElements()
        {
            base.InitElements();
            _close.SetVisible(_isClosable);
            AddItems(_textLabel, _close);
            EventOnSelect = null;
            EventOnSelect += (sender, args) =>
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
            EventOnClose?.Invoke();
            EventTabRemove?.Invoke();
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
                _textLabel.SetStyle(inner_style);
                _labelRightMargin = _textLabel.GetMargin().Right;
            }

            SetForeground(style.Foreground);
            SetFont(style.Font);
            SetTextAlignment(style.TextAlignment);
        }
    }
}