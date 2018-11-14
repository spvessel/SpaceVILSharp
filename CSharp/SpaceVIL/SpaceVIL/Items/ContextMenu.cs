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
        public ListBox ItemList = new ListBox();
        private Queue<IBaseItem> _queue = new Queue<IBaseItem>();

        private static int count = 0;
        public MouseButton ActiveButton = MouseButton.ButtonRight;

        private bool _init = false;
        private bool _ouside = true;
        public bool IsOutsideClickClosable()
        {
            return _ouside;
        }
        public void SetOutsideClickClosable(bool value)
        {
            _ouside = value;
        }

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

        public override void InitElements()
        {
            SetConfines();
            ItemList.SetSelectionVisibility(false);
            ItemList.SetVScrollBarVisible(ScrollBarVisibility.Never);
            ItemList.SetHScrollBarVisible(ScrollBarVisibility.Never);
            ItemList.GetArea().SelectionChanged += OnSelectionChanged;

            base.AddItem(ItemList);

            ItemList.EventScrollUp = null;
            ItemList.EventScrollDown = null;

            foreach (var item in _queue)
                ItemList.AddItem(item);
            _queue = null;
            _init = true;
        }

        protected virtual void OnSelectionChanged()
        {
            MenuItem item = ItemList.GetSelectionItem() as MenuItem;
            if (item != null)
            {
                if (item.IsActionItem)
                {
                    return;
                }
            }
            Hide();
            MouseArgs args = new MouseArgs();
            args.Position.SetPosition(-100, -100);
            foreach (var context_menu in ItemsLayoutBox.GetLayoutFloatItems(GetHandler().Id))
            {
                ContextMenu menu = context_menu as ContextMenu;
                if (menu != null)
                    menu.Hide();
            }
        }
        public int GetListCount()
        {
            return ItemList.GetListContent().Count;
        }
        public List<IBaseItem> GetListContent()
        {
            return ItemList.GetListContent();
        }

        public override void AddItem(IBaseItem item)
        {
            // (item as MenuItem)._invoked_menu = this;
            MenuItem tmp = (item as MenuItem);
            if (tmp != null)
                tmp._context_menu = this;
            _queue.Enqueue(item);
        }
        public override void RemoveItem(IBaseItem item)
        {
            ItemList.RemoveItem(item);
        }

        void UpdateSize()
        {
            int height = 0;
            int width = GetWidth();
            List<IBaseItem> list = ItemList.GetListContent();
            foreach (var item in list)
            {
                height += (item.GetHeight() + ItemList.GetArea().GetSpacing().Vertical);

                int tmp = GetPadding().Left + GetPadding().Right + item.GetMargin().Left + item.GetMargin().Right;

                MenuItem m = item as MenuItem;
                if (item != null)
                {
                    tmp += m.GetTextWidth() + m.GetMargin().Left + m.GetMargin().Right + m.GetPadding().Left + m.GetPadding().Right;
                }
                else
                    tmp = tmp + item.GetWidth() + item.GetMargin().Left + item.GetMargin().Right;

                if (width < tmp)
                    width = tmp;
            }
            // SetSize(width, height);
            SetWidth(width);
            SetHeight(height);
        }

        public void Show(IItem sender, MouseArgs args)
        {
            if (args.Button == ActiveButton)
            {
                if (!_init)
                {
                    InitElements();
                    UpdateSize();
                }

                SetVisible(true);

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
                // SetX(args.Position.X);
                // SetY(args.Position.Y);
                SetConfines();
            }
        }
        public void Hide()
        {
            SetX(-GetWidth());
            SetVisible(false);
        }

        public override void SetConfines()
        {
            base.SetConfines(
                GetX(),
                GetX() + GetWidth(),
                GetY(),
                GetY() + GetHeight()
            );
        }

        public bool CloseDependencies(MouseArgs args)
        {
            // Console.WriteLine(GetItemName() + " " + args.Position.X);
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
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            SetPadding(style.Padding);
            SetSizePolicy(style.WidthPolicy, style.HeightPolicy);
            SetBackground(style.Background);

            Style itemlist_style = style.GetInnerStyle("itemlist");
            if (itemlist_style != null)
            {
                ItemList.SetBackground(itemlist_style.Background);
                ItemList.SetAlignment(itemlist_style.Alignment);
                ItemList.SetPadding(itemlist_style.Padding);
            }
        }
    }
}