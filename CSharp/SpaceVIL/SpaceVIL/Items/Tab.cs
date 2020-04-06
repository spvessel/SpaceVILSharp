using System;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// Tab is used in SpaceVIL.TabView. Represents tab of one page.
    /// <para/> Contains close button.
    /// <para/> Supports all including drag and drop.
    /// </summary>
    public class Tab : Prototype, IDraggable
    {
        static int count = 0;
        internal Frame View;
        private Label _textLabel;
        private Tab _close;
        /// <summary>
        /// Getting close button.
        /// </summary>
        /// <returns>Close button as SpaceVIL.Tab.</returns>
        public Tab GetCloseButton()
        {
            return _close;
        }

        private bool _isClosable = false;
        /// <summary>
        /// Setting tab to support closing or not support closing.
        /// </summary>
        /// <param name="value">True: if you want to tab support closing.
        /// False: if you want to tab do not support closing.</param>
        public void SetClosable(bool value)
        {
            if (_isClosable == value)
                return;
            _isClosable = value;
            _close.SetVisible(_isClosable);
            UpdateTabWidth();
        }
        /// <summary>
        /// Returns True if tab support closing otherwise returns False.
        /// </summary>
        /// <returns>True: if tab support closing.
        /// False: if tab do not support closing.</returns>
        public bool IsClosable()
        {
            return _isClosable;
        }
        /// <summary>
        /// Constructs Tab with the specified text.
        /// </summary>
        /// <param name="text">Text of Tab.</param>
        public Tab(String text) : this(text, text) { }
        /// <summary>
        /// Constructs Tab with specified text and name of the Tab.
        /// </summary>
        /// <param name="text">Text of the Tab.</param>
        /// <param name="name">Name of the Tab.</param>
        public Tab(String text, String name) : this()
        {
            SetItemName(name);
            SetText(text);
        }
        /// <summary>
        /// Default Tab constructor.
        /// </summary>
        public Tab() : base()
        {
            SetItemName("Tab_" + count++);
            _close = new Tab();
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
        /// <summary>
        /// Setting tab to support drag and drop or not.
        /// </summary>
        /// <param name="value">True: if you want to tab support drag and drop.
        /// False: if you want to tab do not support drag and drop.</param>
        public void SetDraggable(bool value)
        {
            _isDraggable = value;
        }
        /// <summary>
        /// Returns True if tab support drag and drop otherwise returns False.
        /// </summary>
        /// <returns>True: if tab support drag and drop.
        /// False: if tab do not support drag and drop.</returns>
        public bool IsDraggable()
        {
            return _isDraggable;
        }

        private void OnMousePress(IItem sender, MouseArgs args)
        {
            if (!IsDraggable())
                return;
            _xClick = args.Position.GetX();
            _xDiff = args.Position.GetX() - GetX();
        }

        private void OnDragging(IItem sender, MouseArgs args)
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
        /// Returns True if Tab is selected otherwise returns False.
        /// </summary>
        /// <returns>True: Tab is selected. 
        /// False: Tab is unselected.</returns>
        public bool IsSelected()
        {
            return _toggled;
        }
        /// <summary>
        /// Setting Tab selected or unselected.
        /// </summary>
        /// <param name="value">True: if you want Tab to be selected. 
        /// False: if you want Tab to be unselected.</param>
        public void SetSelected(bool value)
        {
            _toggled = value;
            if (value)
                SetState(ItemStateType.Toggled);
            else
                SetState(ItemStateType.Base);
        }
        /// <summary>
        /// Event that is invoked when Tab become selected.
        /// </summary>
        public EventMouseMethodState EventOnSelect;
        /// <summary>
        /// Event that is invoked when Tab is closed.
        /// </summary>
        public EventCommonMethod EventOnClose;
        /// <summary>
        /// Event that is invoked when Tab is removed.
        /// </summary>
        internal EventCommonMethod EventTabRemove;
        /// <summary>
        /// Disposing Tab resources if the Tab was removed.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void Release()
        {
            EventOnSelect = null;
            EventOnClose = null;
            EventTabRemove = null;
        }

        /// <summary>
        /// Setting alignment of Tab text. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Text alignment as SpaceVIL.Core.ItemAlignment.</param>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _textLabel.SetTextAlignment(alignment);
        }
        /// <summary>
        /// Setting alignment of Tab text. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Text alignment as sequence of SpaceVIL.Core.ItemAlignment.</param>
        public void SetTextAlignment(params ItemAlignment[] alignment)
        {
            _textLabel.SetTextAlignment(alignment);
        }
        /// <summary>
        /// Setting indents for the text to offset text relative to Tab.
        /// </summary>
        /// <param name="margin">Indents as SpaceVIL.Decorations.Indents.</param>
        public void SetTextMargin(Indents margin)
        {
            _textLabel.SetMargin(margin);
        }
        /// <summary>
        /// Setting indents for the text to offset text relative to Tab.
        /// </summary>
        /// <param name="left">Indent on the left.</param>
        /// <param name="top">Indent on the top.</param>
        /// <param name="right">Indent on the right.</param>
        /// <param name="bottom">Indent on the bottom.</param>
        public void SetTextMargin(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            _textLabel.SetMargin(left, top, right, bottom);
        }
        /// <summary>
        /// Getting indents of the text.
        /// </summary>
        /// <returns>Indents as SpaceVIL.Decorations.Indents.</returns>
        public Indents GetTextMargin()
        {
            return _textLabel.GetMargin();
        }
        /// <summary>
        /// Setting font of the text.
        /// </summary>
        /// <param name="font">Font as System.Drawing.Font.</param>
        public void SetFont(Font font)
        {
            _textLabel.SetFont(font);
        }
        /// <summary>
        /// Setting font size of the text.
        /// </summary>
        /// <param name="size">New size of the font.</param>
        public void SetFontSize(int size)
        {
            _textLabel.SetFontSize(size);
        }
        /// <summary>
        /// Setting font style of the text.
        /// </summary>
        /// <param name="style">New font style as System.Drawing.FontStyle.</param>
        public void SetFontStyle(FontStyle style)
        {
            _textLabel.SetFontStyle(style);
        }
        /// <summary>
        /// Setting new font family of the text.
        /// </summary>
        /// <param name="fontFamily">New font family as System.Drawing.FontFamily.</param>
        public void SetFontFamily(FontFamily fontFamily)
        {
            _textLabel.SetFontFamily(fontFamily);
        }
        /// <summary>
        /// Getting the current font of the text.
        /// </summary>
        /// <returns>Font as System.Drawing.Font.</returns>
        public Font GetFont()
        {
            return _textLabel.GetFont();
        }
        /// <summary>
        /// Setting the text.
        /// </summary>
        /// <param name="text">Text as System.String.</param>
        public virtual void SetText(String text)
        {
            _textLabel.SetText(text);
            UpdateTabWidth();
        }
        /// <summary>
        /// Getting the current text of the Tab.
        /// </summary>
        /// <returns>Text as System.String.</returns>
        public virtual String GetText()
        {
            return _textLabel.GetText();
        }
        /// <summary>
        /// Getting the text width (useful when you need resize Tab by text width).
        /// </summary>
        /// <returns>Text width.</returns>
        public int GetTextWidth()
        {
            return _textLabel.GetWidth();
        }
        /// <summary>
        /// Getting the text height (useful when you need resize Tab by text height).
        /// </summary>
        /// <returns>Text height.</returns>
        public int GetTextHeight()
        {
            return _textLabel.GetHeight();
        }
        /// <summary>
        /// Setting text color of a Tab.
        /// </summary>
        /// <param name="color">Color as System.Drawing.Color.</param>
        public void SetForeground(Color color)
        {
            _textLabel.SetForeground(color);
        }
        /// <summary>
        /// Setting text color of a Tab in byte RGB format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0 - 255)</param>
        /// <param name="g">Green bits of a color. Range: (0 - 255)</param>
        /// <param name="b">Blue bits of a color. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b)
        {
            _textLabel.SetForeground(r, g, b);
        }
        /// <summary>
        /// Setting text color of a Tab in byte RGBA format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0 - 255)</param>
        /// <param name="g">Green bits of a color. Range: (0 - 255)</param>
        /// <param name="b">Blue bits of a color. Range: (0 - 255)</param>
        /// <param name="a">Alpha bits of a color. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b, int a)
        {
            _textLabel.SetForeground(r, g, b, a);
        }
        /// <summary>
        /// Setting text color of a Tab in float RGB format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue bits of a color. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b)
        {
            _textLabel.SetForeground(r, g, b);
        }
        /// <summary>
        /// Setting text color of a Tab in float RGBA format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="a">Alpha bits of a color. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b, float a)
        {
            _textLabel.SetForeground(r, g, b, a);
        }
        /// <summary>
        /// Getting current text color.
        /// </summary>
        /// <returns>Text color as System.Drawing.Color.</returns>
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
        /// <summary>
        /// Initializing all elements in the Tab.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            base.InitElements();
            _close.SetVisible(_isClosable);
            AddItems(_textLabel, _close);
            EventOnSelect = null;
            EventOnSelect += (sender, args) =>
            {
                if (IsSelected())
                    return;
                SetSelected(true);
            };

            _close.IsFocusable = false;
            _close.EventMouseClick += (sender, args) =>
            {
                RemoveTab();
            };
        }
        /// <summary>
        /// Removing Tab.
        /// </summary>
        public void RemoveTab()
        {
            EventOnClose?.Invoke();
            EventTabRemove?.Invoke();
        }
        /// <summary>
        /// Seting style of the Tab.
        /// <para/> Inner styles: "closebutton", "view", "text".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
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