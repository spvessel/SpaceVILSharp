using System;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// TitleBar is the basic implementation of a user interface tittle bar of window.
    /// <para/> Extended from SpaceVIL.WindowAnchor. WindowAnchor is class representing the draggable window type of an item.
    /// <para/> Contains icon, text, minimize button, maximize button, close button.
    /// <para/> Supports all events except drag and drop despite that this class is draggable type.
    /// </summary>
    public class TitleBar : WindowAnchor
    {
        static int count = 0;
        private HorizontalStack _layout;

        /// <summary>
        /// Direction of title bar (in Mac OS is HorizontalDirection.FromRightToLeft, 
        /// in others OS HorizontalDirection.FromLeftToRight).
        /// </summary>
        public HorizontalDirection Direction = HorizontalDirection.FromLeftToRight;
        private Label _textLabel;
        private ImageItem _icon;

        /// <summary>
        /// Getting icon of title bar.
        /// </summary>
        /// <returns>Icon of title bar as SpaceVIL.ImageItem.</returns>
        public ImageItem GetIcon()
        {
            return _icon;
        }

        private ButtonCore _close;

        /// <summary>
        /// Getting close button of title bar. This button closes the current window.
        /// </summary>
        /// <returns>Close button of title bar as SpaceVIL.TitleBar.</returns>
        public ButtonCore GetCloseButton()
        {
            return _close;
        }

        private ButtonCore _minimize;

        /// <summary>
        /// Getting minimize button of title bar. This button minimizes the window to the taskbar.
        /// </summary>
        /// <returns>Minimize button of title bar as SpaceVIL.TitleBar.</returns>
        public ButtonCore GetMinimizeButton()
        {
            return _minimize;
        }

        private ButtonCore _maximize;

        /// <summary>
        /// Getting maximize button of title bar. This button maximizes/restores the 
        /// window to all available space of the current display 
        /// (display size without task bar and other OS elements).
        /// </summary>
        /// <returns>Maximize button of title bar as SpaceVIL.TitleBar.</returns>
        public ButtonCore GetMaximizeButton()
        {
            return _maximize;
        }

        /// <summary>
        /// Default TitleBar constructor.
        /// </summary>
        public TitleBar()
        {
            SetItemName("TitleBar_" + count);
            count++;

            _layout = new HorizontalStack();
            _textLabel = new Label();
            _textLabel.IsFocusable = false;
            _minimize = new ButtonCore();
            _minimize.IsFocusable = false;
            _maximize = new ButtonCore();
            _maximize.IsFocusable = false;
            _close = new ButtonCore();
            _close.IsFocusable = false;
            _icon = new ImageItem();
            _icon.IsFocusable = false;

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.TitleBar)));

            EventMouseDoubleClick += (sender, args) =>
            {
                GetHandler().Maximize();
            };
        }

        /// <summary>
        /// Constructs TitleBar with the specified title text.
        /// </summary>
        /// <param name="text">Title text</param>
        public TitleBar(String text) : this()
        {
            SetText(text);
        }

        /// <summary>
        /// Setting image icon for title bar. The image is scaled to the specified width and height.
        /// </summary>
        /// <param name="icon">Image icon as System.Drawing.Bitmap.</param>
        /// <param name="width">New width of the bitmap.</param>
        /// <param name="height">New height of the bitmap.</param>
        public void SetIcon(Bitmap icon, int width, int height)
        {
            _icon.SetSize(width, height);
            _icon.SetImage(icon);
            _icon.SetVisible(true);
        }

        /// <summary>
        /// Setting alignment of a TitleBar text. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Text alignment as SpaceVIL.Core.ItemAlignment.</param>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _textLabel.SetTextAlignment(alignment);
        }

        /// <summary>
        /// Setting alignment of a TitleBar text. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Text alignment as sequence of SpaceVIL.Core.ItemAlignment.</param>
        public void SetTextAlignment(params ItemAlignment[] alignment)
        {
            _textLabel.SetTextAlignment(alignment);
        }

        /// <summary>
        /// Setting indents for the text to offset text relative to TitleBar.
        /// </summary>
        /// <param name="margin">Indents as SpaceVIL.Decorations.Indents.</param>
        public void SetTextMargin(Indents margin)
        {
            _textLabel.SetMargin(margin);
        }

        /// <summary>
        /// Setting indents for the text to offset text relative to TitleBar.
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
        }

        /// <summary>
        /// Getting the current text of the TitleBar.
        /// </summary>
        /// <returns>Text as System.String.</returns>
        public virtual String GetText()
        {
            return _textLabel.GetText();
        }

        /// <summary>
        /// Getting the text width (useful when you need resize TitleBar by text width).
        /// </summary>
        /// <returns>Text width.</returns>
        public int GetTextWidth()
        {
            return _textLabel.GetWidth();
        }

        /// <summary>
        /// Getting the text height (useful when you need resize TitleBar by text height).
        /// </summary>
        /// <returns>Text height.</returns>
        public int GetTextHeight()
        {
            return _textLabel.GetHeight();
        }

        /// <summary>
        /// Setting text color of a TitleBar.
        /// </summary>
        /// <param name="color">Text color as System.Drawing.Color.</param>
        public void SetForeground(Color color)
        {
            _textLabel.SetForeground(color);
        }

        /// <summary>
        /// Setting text color of a TitleBar in byte RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b)
        {
            _textLabel.SetForeground(r, g, b);
        }

        /// <summary>
        /// Setting text color of a TitleBar in byte RGBA format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        /// <param name="a">Alpha color component. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b, int a)
        {
            _textLabel.SetForeground(r, g, b, a);
        }

        /// <summary>
        /// Setting text color of a TitleBar in float RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b)
        {
            _textLabel.SetForeground(r, g, b);
        }

        /// <summary>
        /// Setting text color of a TitleBar in float RGBA format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        /// <param name="a">Alpha color component. Range: (0.0f - 1.0f)</param>
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

        /// <summary>
        /// Initializing all elements in the TitleBar.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            AddItem(_layout);

            //_close
            _close.EventMouseClick += (sender, args) =>
            {
                GetHandler().EventClose?.Invoke();
            };

            //_minimize
            _minimize.EventMouseClick += (sender, args) =>
            {
                GetHandler().Minimize();
            };

            //_maximize
            _maximize.EventMouseClick += (sender, args) =>
            {
                GetHandler().Maximize();
            };

            //adding
            switch (Direction)
            {
                case HorizontalDirection.FromLeftToRight:
                    _layout.AddItems(_icon, _textLabel, _minimize, _maximize, _close);
                    break;
                case HorizontalDirection.FromRightToLeft:
                    _textLabel.SetTextAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);
                    _layout.AddItems(_close, _minimize, _maximize, _icon, _textLabel);
                    break;
                default:
                    _layout.AddItems(_icon, _textLabel, _minimize, _maximize, _close);
                    break;
            }
        }

        /// <summary>
        /// Setting style of the TitleBar. 
        /// <para/> Inner styles: "closebutton", "minimizebutton", "maximizebutton", "title".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;

            base.SetStyle(style);
            SetForeground(style.Foreground);
            SetFont(style.Font);
            SetTextAlignment(style.TextAlignment);
            _layout.SetSpacing(style.Spacing);

            Style innerStyle = style.GetInnerStyle("closebutton");
            if (innerStyle != null)
            {
                _close.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("minimizebutton");
            if (innerStyle != null)
            {
                _minimize.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("maximizebutton");
            if (innerStyle != null)
            {
                _maximize.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("title");
            if (innerStyle != null)
            {
                SetTextMargin(innerStyle.Margin);
            }

            //icon
            _icon.SetVisible(false);
            _icon.SetBackground(Color.Transparent);
            _icon.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            _icon.SetAlignment(ItemAlignment.VCenter | ItemAlignment.Left);
        }
    }
}
