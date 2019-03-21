using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class MenuItem : Prototype //not finished
    {
        public bool IsActionItem = false;
        static int count = 0;
        private TextLine _text_object;
        internal ContextMenu _context_menu;
        private ContextMenu _sub_context_menu;

        public Prototype GetSender()
        {
            return _context_menu.GetSender();
        }

        /// <returns> sub context menu </returns>
        public ContextMenu GetSubContextMenu()
        {
            return _sub_context_menu;
        }

        /// <summary>
        /// Is MenuItem ready to close
        /// </summary>
        internal bool IsReadyToClose(MouseArgs args)
        {
            if (_sub_context_menu != null)
            {
                if (!_sub_context_menu.GetHoverVerification(args.Position.GetX(), args.Position.GetY())
                    && _sub_context_menu.CloseDependencies(args))
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
        /// Assign the context menu
        /// </summary>
        public void AssignContextMenu(ContextMenu context_menu)
        {
            _sub_context_menu = context_menu;
            _sub_context_menu.SetOutsideClickClosable(false);
            IsActionItem = true;
        }

        /// <summary>
        /// Constructs a MenuItem
        /// </summary>
        public MenuItem()
        {
            SetItemName("MenuItem_" + count);
            count++;
            EventKeyPress += OnKeyPress;
            EventMousePress += (sender, args) => OnMouseAction();

            _text_object = new TextLine();

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.MenuItem)));
        }

        /// <summary>
        /// Constructs a MenuItem with text
        /// </summary>
        public MenuItem(String text = "") : this()
        {
            SetText(text);
        }

        /// <summary>
        /// Constructs a MenuItem with assigned context menu and text
        /// </summary>
        public MenuItem(ContextMenu context_menu, String text = "") : this()
        {
            AssignContextMenu(context_menu);
            SetText(text);
        }

        protected virtual void OnKeyPress(object sender, KeyArgs args)
        {
            if (args.Key == KeyCode.Enter)
                EventMouseClick?.Invoke(this, new MouseArgs());
        }

        //text init
        /// <returns> text width in the MenuItem </returns>
        public int GetTextWidth()
        {
            return _text_object.GetWidth();
        }

        /// <returns> text height in the MenuItem </returns>
        public int GetTextHeight()
        {
            return _text_object.GetHeight();
        }

        /// <summary>
        /// Text alignment in the MenuItem
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
        /// Text margin in the MenuItem
        /// </summary>
        public Indents GetTextMargin()
        {
            return _text_object.GetMargin();
        }

        public void SetTextMargin(Indents margin)
        {
            _text_object.SetMargin(margin);
        }

        /// <summary>
        /// Text font in the MenuItem
        /// </summary>
        public void SetFont(Font font)
        {
            _text_object.SetFont(font);
        }
        public Font GetFont()
        {
            return _text_object.GetFont();
        }

        /// <summary>
        /// MenuItem text
        /// </summary>
        public void SetText(String text)
        {
            _text_object.SetItemText(text);
        }
        public String GetText()
        {
            return _text_object.GetItemText();
        }

        /// <summary>
        /// Text color in the MenuItem
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
        /// Initialization and adding of all elements in the MenuItem
        /// </summary>
        public override void InitElements()
        {
            //adding
            AddItem(_text_object);
            if (IsActionItem)
                AddItem(_arrow);
            foreach (IBaseItem item in _queue)
            {
                base.AddItem(item);
            }
        }

        private List<IBaseItem> _queue = new List<IBaseItem>();

        public override void AddItem(IBaseItem item)
        {
            _queue.Add(item);
        }

        /// <summary>
        /// Set style of the MenuItem
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
                _text_object.SetMargin(inner_style.Margin);
            }
        }

        /// <summary>
        /// Customize shape of the MenuItem's arrow
        /// </summary>
        public void AddArrow(CustomShape arrow)
        {
            _arrow = arrow;
        }

        /// <summary>
        /// Show the MenuItem
        /// </summary>
        public void Show()
        {
            if (_sub_context_menu == null)
                return;

            MouseArgs args = new MouseArgs();
            args.Button = MouseButton.ButtonRight;

            //проверка справа
            args.Position.SetX(_context_menu.GetX() + _context_menu.GetWidth() + 2);
            if (args.Position.GetX() + _sub_context_menu.GetWidth() > GetHandler().GetWidth())
            {
                args.Position.SetX(_context_menu.GetX() - _sub_context_menu.GetWidth() - 2);
            }
            //проверка снизу
            args.Position.SetY(GetY());
            if (args.Position.GetY() + _sub_context_menu.GetHeight() > GetHandler().GetHeight())
            {
                args.Position.SetY(_context_menu.GetY() + _context_menu.GetHeight() - _sub_context_menu.GetHeight());
            }

            _sub_context_menu.Show(this, args);
        }

        /// <summary>
        /// Hide the MenuItem
        /// </summary>
        public void Hide()
        {
            _sub_context_menu?.Hide();
        }

        private void OnMouseAction()
        {
            if (_sub_context_menu != null)
            {
                if (_sub_context_menu.IsVisible())
                {
                    Hide();
                    MouseArgs args = new MouseArgs();
                    args.Button = MouseButton.ButtonRight;
                    args.Position.SetPosition(GetX(), GetY());
                    _sub_context_menu.CloseDependencies(args);
                }
                else
                    Show();
            }
        }
    }
}