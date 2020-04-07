using System.Collections.Generic;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// ContextMenu is a menu for selecting one of the available options 
    /// from the list that perform the assigned action. ContextMenu is a floating 
    /// item (see SpaceVIL.Core.IFloating and  enum SpaceVIL.Core.LayoutType) and 
    /// closes when mouse click outside the ContextMenu area.
    /// <para/> Contains ListBox. 
    /// <para/> Supports all events except drag and drop.
    /// <para/> Notice: All floating items render above all others items.
    /// <para/> ContextMenu does not pass any input events and invisible by default.
    /// </summary>
    public class ContextMenu : Prototype, IFloating
    {
        /// <summary>
        /// Property that allows to specify what item will be focused after ContextMenu is closed.
        /// </summary>
        public Prototype ReturnFocus = null;
        /// <summary>
        /// ListBox for storing a list of options (SpaceVIL.MenuItem).
        /// </summary>
        public ListBox ItemList = new ListBox();
        private Queue<IBaseItem> _queue = new Queue<IBaseItem>();
        private Prototype _sender = null;
        /// <summary>
        /// Getting the item that invokes ContextMenu.
        /// </summary>
        /// <returns>Item as SpaceVIL.Prototype.</returns>
        public Prototype GetSender()
        {
            return _sender;
        }
        private static int count = 0;
        /// <summary>
        /// You can specify mouse button (see SpaceVIL.Core.MouseButton) 
        /// that is used to open ContextMenu.
        /// <para/> Default: SpaceVIL.Core.MouseButton.ButtonRight.
        /// </summary>
        public MouseButton ActiveButton = MouseButton.ButtonRight;

        private bool _init = false;
        private bool _ouside = true;

        /// <summary>
        /// Returns True if ContextMenu (see SpaceVIL.Core.IFloating)
        /// should closes when mouse click outside the area of ContextMenu otherwise returns False.
        /// </summary>
        /// <returns>True: if ContextMenu closes when mouse click outside the area.
        /// False: if ContextMenu stays opened when mouse click outside the area.</returns>
        public bool IsOutsideClickClosable()
        {
            return _ouside;
        }
        /// <summary>
        /// Setting boolean value of item's behavior when mouse click occurs outside the ContextMenu.
        /// </summary>
        /// <param name="value">True: ContextMenu should become invisible if mouse click occurs outside the item.
        /// False: an item should stay visible if mouse click occurs outside the item.</param>
        public void SetOutsideClickClosable(bool value)
        {
            _ouside = value;
        }

        /// <summary>
        /// Constructs a ContextMenu and attaches it to the specified window 
        /// (see SpaceVIL.CoreWindow, SpaceVIL.ActiveWindow, SpaceVIL.DialogWindow).
        /// ContextMenu does not pass any input events and invisible by default.
        /// </summary>
        /// <param name="handler">Window for attaching ContextMenu.</param>
        public ContextMenu(CoreWindow handler)
        {
            ItemsLayoutBox.AddItem(handler, this, LayoutType.Floating);
            SetItemName("ContextMenu_" + count++);
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.ContextMenu)));
            SetPassEvents(false);
            SetVisible(false);
        }
        /// <summary>
        /// Constructs a ContextMenu with specified options and attaches it to the specified window 
        /// (see SpaceVIL.CoreWindow, SpaceVIL.ActiveWindow, SpaceVIL.DialogWindow).
        /// </summary>
        /// <param name="handler">Window for attaching ContextMenu.</param>
        /// <param name="items">Sequence of options as SpaceVIL.MenuItem.</param>
        public ContextMenu(CoreWindow handler, params MenuItem[] items) : this(handler)
        {
            foreach (MenuItem item in items)
                AddItem(item);
        }

        /// <summary>
        /// Initializing all elements in the ContextMenu.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            SetConfines();
            ItemList.SetMenuDisabled(true);
            ItemList.SetVScrollBarPolicy(VisibilityPolicy.Never);
            ItemList.SetHScrollBarPolicy(VisibilityPolicy.Never);
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

            foreach (IBaseItem item in _queue)
            {
                ItemList.AddItem(item);
            }

            _init = true;
        }

        private void HideDependentMenus()
        {
            //тут находит еще одно меню, у ItemList
            foreach (var context_menu in ItemsLayoutBox.GetLayoutFloatItems(GetHandler().GetWindowGuid()))
            {
                ContextMenu menu = context_menu as ContextMenu;
                if (menu != null && !menu.Equals(this))
                    menu.Hide();
            }
        }

        private void OnSelectionChanged(MenuItem sender)
        {
            if (sender != null)
                if (sender.IsActionItem)
                    return;

            HideDependentMenus();
            Hide();
        }

        /// <summary>
        /// Getting number of options in the list.
        /// </summary>
        /// <returns>Number of options in the list.</returns>
        public int GetListCount()
        {
            return ItemList.GetListContent().Count;
        }

        /// <summary>
        /// Getting all existing options (list of SpaceVIL.MenuItem objects).
        /// </summary>
        /// <returns>Options as List&lt;SpaceVIL.MenuItem&gt;</returns>
        public List<IBaseItem> GetListContent()
        {
            return ItemList.GetListContent();
        }

        /// <summary>
        /// Adding option (or any SpaceVIL.Core.IBaseItem implementation) to the ComboBoxDropDown. 
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        public override void AddItem(IBaseItem item)
        {
            MenuItem tmp = (item as MenuItem);
            if (tmp != null)
            {
                tmp.ContextMenu = this;
                tmp.EventMouseClick += (sender, args) =>
                {
                    OnSelectionChanged(tmp);
                };
            }
            if (_init)
            {
                ItemList.AddItem(item);
            }
            else
            {
                _queue.Enqueue(item);
            }
            _added = false;
        }

        /// <summary>
        /// Removing option (or any SpaceVIL.Core.IBaseItem implementation) from the ComboBoxDropDown.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        /// <returns>True: if the removal was successful. 
        /// False: if the removal was unsuccessful.</returns>
        public override bool RemoveItem(IBaseItem item)
        {
            return ItemList.RemoveItem(item);
        }

        private void UpdateSize()
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
        /// Shows the ContextMenu at the proper position.
        /// </summary>
        /// <param name="sender"> The item from which the show request is sent. </param>
        /// <param name="args"> Mouse click arguments (cursor position, mouse button,
        /// mouse button press/release, etc.). </param>
        public void Show(IItem sender, MouseArgs args)
        {
            if (args.Button == ActiveButton)
            {
                if (!_init)
                    InitElements();
                if (!_added)
                {
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
        /// <summary>
        /// Shows the ContextMenu at the position (0, 0).
        /// </summary>
        public void Show()
        {
            MouseArgs margs = new MouseArgs();
            margs.Button = MouseButton.ButtonRight;
            Show(this, margs);
        }

        private bool _added = false;
        /// <summary>
        /// Remove all content in the ContextMenu.
        /// </summary>
        public override void Clear()
        {
            ItemList.Clear();
            _queue.Clear();
            _added = false;
        }

        /// <summary>
        /// Hide the ContextMenu without destroying.
        /// </summary>
        public void Hide()
        {
            ItemList.Unselect();
            SetVisible(false);
            SetX(-GetWidth());

            if (ReturnFocus != null)
                ReturnFocus.SetFocus();
        }
        /// <summary>
        /// Hide the ContextMenu without destroying with using specified mouse arguments.
        /// </summary>
        /// <param name="args">Arguments as SpaceVIL.Core.MouseArgs.</param>
        public void Hide(MouseArgs args)
        {
            Hide();
        }

        /// <summary>
        /// Overridden method for setting confines according 
        /// to position and size of the ContextMenu (see Prototype.SetConfines()).
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

        /// <summary>
        /// Setting style of the ContextMenu.
        /// <para/> Inner styles: "itemlist".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);

            Style innerStyle = style.GetInnerStyle("itemlist");
            if (innerStyle != null)
            {
                ItemList.SetStyle(innerStyle);
            }
        }
    }
}
