using System.Collections.Generic;
using SpaceVIL.Core;

namespace SpaceVIL
{
    /// <summary>
    /// SelectionItem is designed to be a wrapper (selection showing) of items in 
    /// special containers that supports item selection such as 
    /// SpaceVIL.ListBox, SpaceVIL.TreeView, SpaceVIL.WrapGrid.
    /// <para/> Can resize by size of wrapped item.
    /// <para/> Supports all events except drag and drop.
    /// </summary>
    public class SelectionItem : Prototype
    {
        private static int count = 0;
        IBaseItem _item;
        private bool _visibility = true;

        internal void SetToggleVisible(bool value)
        {
            _visibility = value;
        }
        /// <summary>
        /// Default SelectionItem constructor.
        /// </summary>
        private SelectionItem()
        {
            IsFocusable = false;
            SetItemName("SelectionItem_" + count);
            count++;
        }
        /// <summary>
        /// Constructs SelectionItem with given item for wrapping.
        /// </summary>
        /// <param name="content"></param>
        public SelectionItem(IBaseItem content) : this()
        {
            _item = content;
            EventMouseClick += (sender, args) =>
            {
                if (IsSelected() || !_visibility)
                    return;
                SetSelected(true);
                UnselectOthers(sender);
            };
        }
        /// <summary>
        /// Getting wrapped item of SelectionItem.
        /// </summary>
        /// <returns>Wrapped item as SpaceVIL.Core.IBaseItem.</returns>
        public IBaseItem GetContent()
        {
            return _item;
        }
        /// <summary>
        /// Initializing all elements in the SelectionItem.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            SetVisible(_item.IsVisible());
            AddItem(_item);
        }
        /// <summary>
        /// Updating size of SelectionItem according to wrapped item size.
        /// </summary>
        public void UpdateSize()
        {
            SetSize(_item.GetWidth() + _item.GetMargin().Left + _item.GetMargin().Right,
                    _item.GetHeight() + _item.GetMargin().Bottom + _item.GetMargin().Top + 2);
            SetMinSize(_item.GetMinWidth() + _item.GetMargin().Left + _item.GetMargin().Right,
                    _item.GetMinHeight() + _item.GetMargin().Bottom + _item.GetMargin().Top);
        }
        /// <summary>
        /// Updating width of SelectionItem according to wrapped item width.
        /// </summary>
        public void UpdateWidth()
        {
            SetWidth(_item.GetWidth() + _item.GetMargin().Left + _item.GetMargin().Right);
            SetMinWidth(_item.GetMinWidth() + _item.GetMargin().Left + _item.GetMargin().Right);
        }
        /// <summary>
        /// Updating height of SelectionItem according to wrapped item height.
        /// </summary>
        public void UpdateHeight()
        {
            SetHeight(_item.GetHeight() + _item.GetMargin().Bottom + _item.GetMargin().Top + 2);
            SetMinHeight(_item.GetMinHeight() + _item.GetMargin().Bottom + _item.GetMargin().Top);
        }

        private bool _toggled = false;

        /// <summary>
        /// Returns True if SelectionItem is selected otherwise returns False.
        /// </summary>
        /// <returns>True: SelectionItem is selected. 
        /// False: SelectionItem is unselected.</returns>
        public bool IsSelected()
        {
            return _toggled;
        }
        /// <summary>
        /// Setting SelectionItem selected or unselected.
        /// </summary>
        /// <param name="value">True: if you want SelectionItem to be selected. 
        /// False: if you want SelectionItem to be unselected.</param>
        public void SetSelected(bool value)
        {
            _toggled = value;
            if (value)
                SetState(ItemStateType.Toggled);
            else
                SetState(ItemStateType.Base);
        }

        private void UnselectOthers(IItem sender)
        {
            List<IBaseItem> items = GetParent().GetItems();
            foreach (IBaseItem item in items)
            {
                SelectionItem tmp = item as SelectionItem;
                if (tmp != null && !tmp.Equals(this))
                {
                    tmp.SetSelected(false);
                }
            }
        }
        /// <summary>
        /// Removing the specified item from SelectionItem.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        /// <returns>True: if the removal was successful. 
        /// False: if the removal was unsuccessful.</returns>
        public override bool RemoveItem(IBaseItem item)
        {
            if (_item != null)
                return GetParent().RemoveItem(item);
            else return false;
        }
        /// <summary>
        /// Remove wrapped item from SelectionItem.
        /// </summary>
        public void ClearContent()
        {
            base.RemoveItem(_item);
            _item = null;
        }
        /// <summary>
        /// Setting this item hovered (mouse cursor located within item's shape).
        /// </summary>
        /// <param name="value">True: if you want this item be hovered. 
        /// False: if you want this item be not hovered.</param>
        public override void SetMouseHover(bool value)
        {
            if (_visibility)
                base.SetMouseHover(value);
        }
    }
}