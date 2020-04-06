using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// MenuItem is designed to be an option in menu type items 
    /// such as SpaceVIL.ContextMenu and ComboBoxDropDown.
    /// <para/> Contains text and arrow.
    /// <para/> Supports all events except drag and drop.
    /// </summary>
    public class MenuItem : Prototype
    {
        /// <summary>
        /// Property to mark this MenuItem as active type (such MenuItem can show another SpaceVIL.ContextMenu). 
        /// True this MenuItem is active otherwise False.
        /// <para/> Default: False.
        /// </summary>
        public bool IsActionItem = false;
        static int count = 0;
        private Label _textObject;
        internal ContextMenu ContextMenu;
        private ContextMenu _subContextMenu;
        /// <summary>
        /// Getting the item that invokes ContextMenu of this MenuItem.
        /// </summary>
        /// <returns>Item as SpaceVIL.Prototype.</returns>
        public Prototype GetSender()
        {
            return ContextMenu.GetSender();
        }
        /// <summary>
        /// Getting the assigned SpaceVIL.ContextMenu. 
        /// If MenuItem is active type it can invoke assigned SpaceVIL.ContextMenu.
        /// </summary>
        /// <returns></returns>
        public ContextMenu GetSubContextMenu()
        {
            return _subContextMenu;
        }

        /// <summary>
        /// Is MenuItem ready to close
        /// </summary>
        internal bool IsReadyToClose(MouseArgs args)
        {
            if (_subContextMenu != null)
            {
                if (!_subContextMenu.GetHoverVerification(args.Position.GetX(), args.Position.GetY())
                    && _subContextMenu.CloseDependencies(args))
                    return true;
            }
            return false;
        }

        CustomShape _arrow;

        public CustomShape GetArrow()
        {
            return _arrow;
        }

        /// <summary>
        /// Assigning SpaceVIL.ContextMenu to this MenuItem. 
        /// In this case MenuItem becomes active type and can invoke assigned SpaceVIL.ContextMenu.
        /// </summary>
        /// <param name="contextMenu">Assigned context menu as SpaceVIL.ContextMenu.</param>
        public void AssignContextMenu(ContextMenu contextMenu)
        {
            IsActionItem = true;
            _subContextMenu = contextMenu;
            _subContextMenu.SetOutsideClickClosable(false);
        }

        /// <summary>
        /// Default MenuItem constructor.
        /// </summary>
        public MenuItem()
        {
            SetItemName("MenuItem_" + count);
            count++;
            EventKeyPress += OnKeyPress;
            EventMousePress += (sender, args) => OnMouseAction();

            _textObject = new Label();
            _textObject.IsHover = false;

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.MenuItem)));
        }

        /// <summary>
        /// Constructs a MenuItem with text.
        /// </summary>
        /// <param name="text">Text for MenuItem.</param>
        public MenuItem(String text = "") : this()
        {
            SetText(text);
        }

        /// <summary>
        /// Constructs a MenuItem with text and assigns context menu.
        /// </summary>
        /// <param name="contextMenu">Assigned context menu as SpaceVIL.ContextMenu.</param>
        /// <param name="text">Text for MenuItem.</param>
        public MenuItem(ContextMenu contextMenu, String text = "") : this()
        {
            AssignContextMenu(contextMenu);
            SetText(text);
        }

        protected virtual void OnKeyPress(object sender, KeyArgs args)
        {
            if (args.Key == KeyCode.Enter)
                EventMouseClick?.Invoke(this, new MouseArgs());
        }

        /// <summary>
        /// Getting the text width (useful when you need resize item by text width).
        /// </summary>
        /// <returns>Text width.</returns>
        public int GetTextWidth()
        {
            return _textObject.GetTextWidth();
        }

        /// <summary>
        /// Getting the text height (useful when you need resize item by text height).
        /// </summary>
        /// <returns>Text height.</returns>
        public int GetTextHeight()
        {
            return _textObject.GetTextHeight();
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
        /// Getting indents of the text.
        /// </summary>
        /// <returns>Indents as SpaceVIL.Decorations.Indents.</returns>
        public Indents GetTextMargin()
        {
            return _textObject.GetMargin();
        }

        /// <summary>
        /// Setting indents for the text to offset text relative to this MenuItem.
        /// </summary>
        /// <param name="margin">Indents as SpaceVIL.Decorations.Indents.</param>
        public void SetTextMargin(Indents margin)
        {
            _textObject.SetMargin(margin);
        }
        /// <summary>
        /// Setting indents for the text to offset text relative to MenuItem.
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
        /// Getting the current text of the MenuItem.
        /// </summary>
        /// <returns>Text as System.String.</returns>
        public virtual String GetText()
        {
            return _textObject.GetText();
        }

        /// <summary>
        /// Setting text color of a MenuItem.
        /// </summary>
        /// <param name="color">Color as System.Drawing.Color.</param>
        public void SetForeground(Color color)
        {
            _textObject.SetForeground(color);
        }
        /// <summary>
        /// Setting text color of a MenuItem in byte RGB format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0 - 255)</param>
        /// <param name="g">Green bits of a color. Range: (0 - 255)</param>
        /// <param name="b">Blue bits of a color. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b)
        {
            _textObject.SetForeground(r, g, b);
        }
        /// <summary>
        /// Setting text color of a MenuItem in byte RGBA format.
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
        /// Setting text color of a MenuItem in float RGB format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue bits of a color. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b)
        {
            _textObject.SetForeground(r, g, b);
        }
        /// <summary>
        /// Setting text color of a MenuItem in float RGBA format.
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
        /// Initializing all elements in the MenuItem.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            //adding
            AddItem(_textObject);
            if (IsActionItem)
                AddItem(_arrow);
            foreach (IBaseItem item in _queue)
            {
                base.AddItem(item);
            }
        }

        private List<IBaseItem> _queue = new List<IBaseItem>();
        /// <summary>
        /// Adding item into the container (this).
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        public override void AddItem(IBaseItem item)
        {
            _queue.Add(item);
        }

        /// <summary>
        /// Setting style of the MenuItem.
        /// <para/> Inner styles: "arrow", "text".
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

            Style inner_style = style.GetInnerStyle("arrow");
            if (inner_style != null)
            {
                if (_arrow == null)
                    _arrow = new CustomShape();
                _arrow.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("text");
            if (inner_style != null)
            {
                _textObject.SetMargin(inner_style.Margin);
            }
        }

        /// <summary>
        /// Adding custom arrow shape.
        /// </summary>
        /// <param name="arrow">Arrow shape as SpaceVIL.CustomShape.</param>
        public void AddArrow(CustomShape arrow)
        {
            _arrow = arrow;
        }

        /// <summary>
        /// Shows the assigned ContextMenu at the proper position. Only if this MeniItem is active type.
        /// </summary>
        public void Show()
        {
            if (_subContextMenu == null)
                return;

            MouseArgs args = new MouseArgs();
            args.Button = MouseButton.ButtonRight;

            //проверка справа
            args.Position.SetX(ContextMenu.GetX() + ContextMenu.GetWidth() + 2);
            if (args.Position.GetX() + _subContextMenu.GetWidth() > GetHandler().GetWidth())
            {
                args.Position.SetX(ContextMenu.GetX() - _subContextMenu.GetWidth() - 2);
            }
            //проверка снизу
            args.Position.SetY(GetY());
            if (args.Position.GetY() + _subContextMenu.GetHeight() > GetHandler().GetHeight())
            {
                args.Position.SetY(ContextMenu.GetY() + ContextMenu.GetHeight() - _subContextMenu.GetHeight());
            }

            _subContextMenu.Show(this, args);
        }

        /// <summary>
        /// Hides the assigned ContextMenu. Only if this MeniItem is active type.
        /// </summary>
        public void Hide()
        {
            _subContextMenu?.Hide();
        }

        private void OnMouseAction()
        {
            if (_subContextMenu != null)
            {
                if (_subContextMenu.IsVisible())
                {
                    Hide();
                    MouseArgs args = new MouseArgs();
                    args.Button = MouseButton.ButtonRight;
                    args.Position.SetPosition(GetX(), GetY());
                    _subContextMenu.CloseDependencies(args);
                }
                else
                    Show();
            }
        }
    }
}