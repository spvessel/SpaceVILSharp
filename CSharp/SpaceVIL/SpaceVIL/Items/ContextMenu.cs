using System;
using System.Drawing;
using System.Collections.Generic;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class ContextMenu : Prototype, IFloating
    {
        public Prototype _returnFocus = null;
        public void SetReturnFocus(Prototype item)
        {
            _returnFocus = item;
        }
        public Prototype GetReturnFocusItem()
        {
            return _returnFocus;
        }

        public ListBox ItemList = new ListBox();
        private Queue<IBaseItem> _queue = new Queue<IBaseItem>();
        private Prototype _sender = null;
        public Prototype GetSender()
        {
            return _sender;
        }

        private static int count = 0;
        public MouseButton ActiveButton = MouseButton.ButtonRight;

        private bool _init = false;
        private bool _ouside = true;

        /// <summary>
        /// Close the ContextMenu it mouse click is outside (true or false)
        /// </summary>
        public bool IsOutsideClickClosable()
        {
            return _ouside;
        }
        public void SetOutsideClickClosable(bool value)
        {
            _ouside = value;
        }

        /// <summary>
        /// Constructs a ContextMenu
        /// </summary>
        /// <param name="handler"> parent window for the ContextMenu </param>
        public ContextMenu(WindowLayout handler)
        {
            SetPassEvents(false);
            SetVisible(false);
            SetHandler(handler);
            SetItemName("ContextMenu_" + count);
            count++;
            ItemsLayoutBox.AddItem(GetHandler(), this, LayoutType.Floating);
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.ContextMenu)));
        }

        public ContextMenu(WindowLayout handler, params MenuItem[] items) : this(handler)
        {
            foreach (MenuItem item in items)
                AddItem(item);
        }

        /// <summary>
        /// Initialization and adding of all elements in the ContextMenu
        /// </summary>
        public override void InitElements()
        {
            SetConfines();
            ItemList.DisableMenu(true);
            ItemList.SetVScrollBarVisible(ScrollBarVisibility.Never);
            ItemList.SetHScrollBarVisible(ScrollBarVisibility.Never);
            base.AddItem(ItemList);
            ItemList.EventScrollUp = null;
            ItemList.EventScrollDown = null;
            ItemList.EventMouseClick = null;
            ItemList.EventKeyPress = null;
            ItemList.GetArea().EventKeyPress += (sender, args) =>
            {
                if (args.Key == KeyCode.Escape)
                {
                    HideDependentMenus();
                    Hide();
                }
                if (args.Key == KeyCode.Enter)
                {
                    Prototype selected = ItemList.GetSelectedItem() as Prototype;
                    if (selected != null)
                    {
                        selected.EventMousePress.Invoke(selected, null);
                        selected.EventMouseClick.Invoke(selected, null);
                    }
                }
            };
            _init = true;
        }

        private void HideDependentMenus()
        {
            //тут находит еще одно меню, у ItemList
            foreach (var context_menu in ItemsLayoutBox.GetLayoutFloatItems(GetHandler().Id))
            {
                ContextMenu menu = context_menu as ContextMenu;
                if (menu != null && !menu.Equals(this))
                    menu.Hide();
            }
        }

        void OnSelectionChanged(MenuItem sender)
        {
            if (sender != null)
                if (sender.IsActionItem)
                    return;

            HideDependentMenus();
            Hide();
        }

        /// <summary>
        /// Returns count of the ContextMenu lines
        /// </summary>
        public int GetListCount()
        {
            return ItemList.GetListContent().Count;
        }

        /// <summary>
        /// Returns ContextMenu items list
        /// </summary>
        public List<IBaseItem> GetListContent()
        {
            return ItemList.GetListContent();
        }

        /// <summary>
        /// Add item to the ContextMenu
        /// </summary>
        public override void AddItem(IBaseItem item)
        {
            MenuItem tmp = (item as MenuItem);
            if (tmp != null)
            {
                tmp._context_menu = this;
                tmp.EventMouseClick += (sender, args) =>
                {
                    OnSelectionChanged(tmp);
                };
            }
            _queue.Enqueue(item);
        }

        /// <summary>
        /// Remove item from the ContextMenu
        /// </summary>
        public override bool RemoveItem(IBaseItem item)
        {
            return ItemList.RemoveItem(item);
        }

        void UpdateSize()
        {
            int height = 0;
            int width = GetWidth();
            List<IBaseItem> list = ItemList.GetListContent();
            foreach (var item in list)
            {
                IBaseItem wrapper = ItemList.GetWrapper(item);
                height += (wrapper.GetHeight() + ItemList.GetArea().GetSpacing().Vertical);

                int tmp = GetPadding().Left + GetPadding().Right + item.GetMargin().Left + item.GetMargin().Right;

                MenuItem m = item as MenuItem;
                if (m != null)
                {
                    tmp += m.GetTextWidth() + m.GetPadding().Left + m.GetPadding().Right + m.GetTextMargin().Left
                        + m.GetTextMargin().Right;
                    if (m.IsActionItem)
                        tmp += m.GetArrow().GetWidth() + m.GetArrow().GetMargin().Left + m.GetArrow().GetMargin().Right;
                }
                else
                    tmp = tmp + item.GetWidth() + item.GetMargin().Left + item.GetMargin().Right;

                if (width < tmp)
                    width = tmp;
            }
            if (height == 0)
                height = GetHeight();
            else
                height -= ItemList.GetArea().GetSpacing().Vertical;

            SetSize(width, height);
        }

        /// <summary>
        /// Show the ContextMenu
        /// </summary>
        /// <param name="sender"> the item from which the show request is sent </param>
        /// <param name="args"> mouse click arguments (cursor position, mouse button,
        /// mouse button press/release, etc.) </param>
        public void Show(IItem sender, MouseArgs args)
        {
            if (args.Button == ActiveButton)
            {
                if (!_init)
                    InitElements();
                if (!_added)
                {
                    foreach (IBaseItem item in _queue)
                    {
                        ItemList.AddItem(item);
                    }
                    UpdateSize();
                    _added = true;
                }
                Prototype tmp = sender as Prototype;
                if (tmp != null)
                    _sender = tmp;

                //проверка снизу
                if (args.Position.GetY() + GetHeight() > GetHandler().GetHeight())
                {
                    SetY(args.Position.GetY() - GetHeight());
                }
                else
                {
                    SetY(args.Position.GetY());
                }
                //проверка справа
                if (args.Position.GetX() + GetWidth() > GetHandler().GetWidth())
                {
                    SetX(args.Position.GetX() - GetWidth());
                }
                else
                {
                    SetX(args.Position.GetX());
                }
                SetConfines();
                SetVisible(true);
                ItemList.GetArea().SetFocus();
            }
        }
        public void Show()
        {
            Show(this, new MouseArgs());
        }

        private bool _added = false;

        public override void Clear()
        {
            ItemList.Clear();
            _queue.Clear();
            _added = false;
        }

        /// <summary>
        /// Hide the ContextMenu without destroying
        /// </summary>
        public void Hide()
        {
            ItemList.Unselect();
            SetVisible(false);
            SetX(-GetWidth());

            if (_returnFocus != null)
                _returnFocus.SetFocus();
        }

        /// <summary>
        /// Set confines according to position and size of the ContextMenu
        /// </summary>
        public override void SetConfines()
        {
            base.SetConfines(
                GetX(),
                GetX() + GetWidth(),
                GetY(),
                GetY() + GetHeight()
            );
        }

        internal bool CloseDependencies(MouseArgs args)
        {
            foreach (var item in GetListContent())
            {
                MenuItem menu_item = item as MenuItem;
                if (menu_item != null)
                {
                    if (menu_item.IsActionItem)
                    {
                        if (menu_item.IsReadyToClose(args))
                        {
                            menu_item.Hide();
                        }
                        else
                            return false;
                    }
                }
            }
            return true;
        }

        //style
        /// <summary>
        /// Set style of the ContextMenu
        /// </summary>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            SetPadding(style.Padding);
            SetSizePolicy(style.WidthPolicy, style.HeightPolicy);
            SetBackground(style.Background);

            Style inner_style = style.GetInnerStyle("itemlist");
            if (inner_style != null)
            {
                ItemList.SetBackground(inner_style.Background);
                ItemList.SetAlignment(inner_style.Alignment);
                ItemList.SetPadding(inner_style.Padding);
            }
            inner_style = style.GetInnerStyle("listarea");
            if (inner_style != null)
            {
                ItemList.GetArea().SetStyle(inner_style);
            }
        }
    }
}
