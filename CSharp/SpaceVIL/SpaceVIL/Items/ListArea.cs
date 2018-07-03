using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    class ListArea : VisualItem, IVLayout
    {
        private int _step = 10;
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
        //internal int _height_last_visible_item = 0;

        static int count = 0;
        public ListArea()
        {
            SetItemName("ListArea" + count);
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
            //item.SetParent(GetParent());//проверить это все, так как тут заменен хендлер-владелец
            UpdateLayout();
        }
        public override void RemoveItem(BaseItem item)
        {
            base.RemoveItem(item);
            UpdateLayout();
            (GetParent() as ListBox)?.UpdateElements();//хрееееееень
        }
        public override void SetHeight(int height)
        {
            int value = height - GetHeight();

            base.SetHeight(height);

            if (AreaPosition == ListPosition.No)
                SetScrollOffset(0);
            else if (AreaPosition == ListPosition.Top)
                SetScrollOffset(GetScrollOffset() + value);
        }
        public override void SetY(int _y)
        {
            base.SetY(_y);
            UpdateLayout();
        }

        //update content position
        private Int64 _scrollOffset = 0;
        public Int64 GetScrollOffset()
        {
            return _scrollOffset;
        }
        public void SetScrollOffset(Int64 value)
        {
            _scrollOffset = value;
            UpdateLayout();
        }

        public void UpdateLayout()
        {
            if (GetItems().Count == 0)
                return;

            AreaPosition = ListPosition.No;

            Int64 offset = GetScrollOffset();
            int startY = GetY() + GetPadding().Top;

            foreach (var child in GetItems())
            {
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

        public void UpdateLayout_deprecated() //refactor!!!
        {
            if (GetItems().Count == 0)
                return;

            AreaPosition = ListPosition.No;

            Int64 offset = GetScrollOffset();
            int startY = GetY() + GetPadding().Top;

            List<BaseItem> childs = GetItems();

            //check previous item for visibility
            if (FirstVisibleItem > 0)
            {
                if (GetSpacing().Vertical < (startY + offset - GetY() - GetPadding().Top))
                {
                    BaseItem previous = childs[FirstVisibleItem - 1];
                    previous.IsVisible = true;
                    _scrollOffset = childs[FirstVisibleItem].GetY() - GetSpacing().Vertical - previous.GetHeight();
                    offset = _scrollOffset;
                    FirstVisibleItem--;
                }
            }

            //check others
            int start_index = FirstVisibleItem;
            for (int i = start_index; i < childs.Count; i++)
            {
                childs[i].SetY(startY + (int)offset);
                offset += childs[i].GetHeight() + GetSpacing().Vertical;

                //top checking
                if (childs[i].GetY() < startY)
                {
                    AreaPosition |= ListPosition.Top;
                    if (childs[i].GetY() + childs[i].GetHeight() <= startY)
                        childs[i].IsVisible = false;
                    else
                    {
                        childs[i].IsVisible = true;
                    }
                    continue;
                }

                //bottom checking
                if (childs[i].GetY() + childs[i].GetHeight() > GetY() + GetHeight() - GetPadding().Bottom)
                {
                    AreaPosition |= ListPosition.Bottom;
                    if (childs[i].GetY() >= GetY() + GetHeight() - GetPadding().Bottom)
                    {
                        childs[i].IsVisible = false;
                        break;
                    }
                    else
                        childs[i].IsVisible = true;
                    LastVisibleItem = i;
                    continue;
                }

                childs[i].IsVisible = true;
            }

            if (AreaPosition == ListPosition.No)
                LastVisibleItem = 0;
        }
    }
}
