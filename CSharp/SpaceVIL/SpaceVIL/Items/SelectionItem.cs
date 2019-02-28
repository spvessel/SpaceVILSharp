using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    class SelectionItem : Prototype
    {
        private static int count = 0;
        IBaseItem _item;

        public SelectionItem(IBaseItem content)
        {
            _item = content;
            IsFocusable = false;
            SetItemName("SelectionItem_" + count);
            count++;

            SetPadding(0, 1, 0, 1);
            SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            SetBackground(0, 0, 0, 0);
            AddItemState(ItemStateType.Toggled, new ItemState(Color.FromArgb(50, 255, 255, 255)));

            EventMouseClick += (sender, args) =>
            {
                if (IsToggled())
                    return;
                SetToggled(true);
                UncheckOthers(sender);
            };
        }

        internal IBaseItem GetContent()
        {
            return _item;
        }

        public override void InitElements()
        {
            AddItem(_item);
            SetSize(_item.GetWidth() + _item.GetMargin().Left + _item.GetMargin().Right,
                    _item.GetHeight() + _item.GetMargin().Bottom + _item.GetMargin().Top + 2);
            SetMinSize(_item.GetMinWidth() + _item.GetMargin().Left + _item.GetMargin().Right,
                    _item.GetMinHeight() + _item.GetMargin().Bottom + _item.GetMargin().Top);
            _item.SetParent(GetParent());
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
                if (tmp != null && !tmp.Equals(this)) {
                    tmp.SetToggled(false);
                }
            }
        }

        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
        }
    }
}