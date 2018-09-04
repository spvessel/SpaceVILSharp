using System;
using System.Drawing;
using System.Collections.Generic;

namespace SpaceVIL
{
    public class ContextMenu : VisualItem, IFloating
    {
        public ListBox ItemList = new ListBox();
        private Queue<BaseItem> _queue = new Queue<BaseItem>();

        private static int count = 0;

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
            IsVisible = false;
            SetHandler(handler);
            SetItemName("ContextMenu_" + count);
            // SetPadding(2, 2, 2, 2);
            // SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            // SetBackground(Color.FromArgb(255, 210, 210, 210));
            count++;
            ItemsLayoutBox.AddItem(GetHandler(), this, LayoutType.Floating);

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.ButtonCore)));
        }

        public override void InitElements()
        {
            SetConfines();

            ItemList.SetSelectionVisibility(false);
            // ItemList.GetArea().SetSpacing(0, 0);
            // ItemList.GetArea().SetPadding(0);
            // ItemList.SetBackground(Color.Transparent);
            // ItemList.SetAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);
            ItemList.SetVScrollBarVisible(ScrollBarVisibility.Never);
            ItemList.SetHScrollBarVisible(ScrollBarVisibility.Never);
            ItemList.GetArea().SelectionChanged += OnSelectionChanged;

            base.AddItem(ItemList);

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
        public List<BaseItem> GetListContent()
        {
            return ItemList.GetListContent();
        }

        public override void AddItem(BaseItem item)
        {
            // (item as MenuItem)._invoked_menu = this;
            _queue.Enqueue(item);
        }
        public override void RemoveItem(BaseItem item)
        {
            ItemList.RemoveItem(item);
        }

        public void Show(IItem sender, MouseArgs args)
        {
            if (args.Button == MouseButton.ButtonRight)
            {
                if (!_init)
                    InitElements();

                IsVisible = true;
                SetX(args.Position.X);
                SetY(args.Position.Y);
                SetConfines();
            }
        }
        public void Hide()
        {
            SetX(-GetWidth());
            IsVisible = false;
        }

        public override void SetConfines()
        {
            _confines_x_0 = GetX();
            _confines_x_1 = GetX() + GetWidth();
            _confines_y_0 = GetY();
            _confines_y_1 = GetY() + GetHeight();
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
            }
        }
    }
}