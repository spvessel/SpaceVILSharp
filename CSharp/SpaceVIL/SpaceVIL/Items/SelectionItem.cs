using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class SelectionItem : Prototype
    {
        private static int count = 0;
        IBaseItem _item;
        private bool _visibility = true;

        internal void SetToggleVisible(bool value)
        {
            _visibility = value;
        }

        private SelectionItem()
        {
            IsFocusable = false;
            SetItemName("SelectionItem_" + count);
            count++;
            // SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.SelectionItem)));
        }
        public SelectionItem(IBaseItem content) : this()
        {
            _item = content;
            EventMouseClick += (sender, args) =>
            {
                if (IsToggled() || !_visibility)
                    return;
                SetToggled(true);
                UncheckOthers(sender);
            };
        }

        public IBaseItem GetContent()
        {
            return _item;
        }

        public override void InitElements()
        {
            SetVisible(_item.IsVisible());
            AddItem(_item);
        }
        public void UpdateSize()
        {
            SetSize(_item.GetWidth() + _item.GetMargin().Left + _item.GetMargin().Right,
                    _item.GetHeight() + _item.GetMargin().Bottom + _item.GetMargin().Top + 2);
            SetMinSize(_item.GetMinWidth() + _item.GetMargin().Left + _item.GetMargin().Right,
                    _item.GetMinHeight() + _item.GetMargin().Bottom + _item.GetMargin().Top);
            // _item.SetParent(GetParent());
        }

        // private for class
        private bool _toggled = false;

        /**
         * Is ButtonToggle toggled (boolean)
         */
        public bool IsToggled()
        {
            return _toggled;
        }

        public void SetToggled(bool value)
        {
            _toggled = value;
            if (value == true)
                SetState(ItemStateType.Toggled);
            else
                SetState(ItemStateType.Base);
        }

        private void UncheckOthers(IItem sender)
        {
            List<IBaseItem> items = GetParent().GetItems();
            foreach (IBaseItem item in items)
            {
                SelectionItem tmp = item as SelectionItem;
                if (tmp != null && !tmp.Equals(this))
                {
                    tmp.SetToggled(false);
                }
            }
        }

        public override void RemoveItem(IBaseItem item)
        {
            GetParent().RemoveItem(item);
        }

        public override void SetMouseHover(bool value)
        {
            if (_visibility)
                base.SetMouseHover(value);
        }
    }
}