using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    class ListArea : VisualItem, IVLayout
    {
        private int _step = 15;
        public void SetStep(int value)
        {
            _step = value;
        }
        public int GetStep()
        {
            return _step;
        }

        public ListPosition AreaPosition = ListPosition.No;
        public int FirstVisibleItem = 0;
        public int LastVisibleItem = 0;

        static int count = 0;
        public ListArea()
        {
            SetItemName("ListArea_" + count);
            count++;
        }

        //overrides
        protected internal override bool GetHoverVerification(float xpos, float ypos)
        {
            return false;
        }

        public override void InvokePoolEvents()
        {

        }
        public override void AddItem(BaseItem item)
        {
            item.IsVisible = false;
            base.AddItem(item);
            UpdateLayout();
        }
        public override void RemoveItem(BaseItem item)
        {
            base.RemoveItem(item);
            UpdateLayout();
            (GetParent() as ListBox)?.UpdateElements();//хрееееееень
        }
        public override void SetY(int _y)
        {
            base.SetY(_y);
            UpdateLayout();
        }

        //update content position
        private Int64 _yOffset = 0;
        private Int64 _xOffset = 0;
        public Int64 GetVScrollOffset()
        {
            return _yOffset;
        }
        public void SetVScrollOffset(Int64 value)
        {
            _yOffset = value;
            UpdateLayout();
        }
        public Int64 GetHScrollOffset()
        {
            return _xOffset;
        }
        public void SetHScrollOffset(Int64 value)
        {
            _xOffset = value;
            UpdateLayout();
        }

        public void UpdateLayout()
        {
            if (GetItems().Count == 0)
                return;

            AreaPosition = ListPosition.No;

            Int64 offset = (-1) * GetVScrollOffset();
            int startY = GetY() + GetPadding().Top;

            foreach (var child in GetItems())
            {
                child.SetX((-1) * (int)_xOffset + GetX() + GetPadding().Left);

                Int64 child_Y = startY + offset;
                offset += child.GetHeight() + GetSpacing().Vertical;

                //top checking
                if (child_Y < startY)
                {
                    AreaPosition |= ListPosition.Top;
                    if (child_Y + child.GetHeight() <= startY)
                    {
                        child.IsVisible = false;
                    }
                    else
                    {
                        child.SetY((int)child_Y);
                        child.IsVisible = true;
                    }
                    continue;
                }

                //bottom checking
                if (child_Y + child.GetHeight() > GetY() + GetHeight() - GetPadding().Bottom)
                {
                    AreaPosition |= ListPosition.Bottom;
                    if (child_Y >= GetY() + GetHeight() - GetPadding().Bottom)
                    {
                        child.IsVisible = false;
                    }
                    else
                    {
                        child.SetY((int)child_Y);
                        child.IsVisible = true;
                    }
                    continue;
                }

                child.SetY((int)child_Y);
                child.IsVisible = true;
            }
        }
    }
}
