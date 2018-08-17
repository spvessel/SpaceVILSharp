using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    public class ListArea : VisualItem, IVLayout, IHLayout
    {
        public EventCommonMethod SelectionChanged;
        public EventCommonMethod ItemListChanged;

        private int _step = 15;
        public void SetStep(int value)
        {
            _step = value;
        }
        public int GetStep()
        {
            return _step;
        }

        private bool _show_selection = true;
        private int _selection = -1;
        public int GetSelection()
        {
            return _selection;
        }
        public void SetSelection(int index)
        {
            _selection = index;
            SelectionChanged?.Invoke();
            UpdateLayout();
        }
        public void Unselect()
        {
            _selection = -1;
            _substrate.IsVisible = false;
        }
        public void SetSelectionVisibility(bool visibility)
        {
            _show_selection = visibility;
            UpdateLayout();
        }
        public bool GetSelectionVisibility()
        {
            return _show_selection;
        }

        private CustomShape _substrate = new CustomShape();
        public CustomShape GetSubstrate()
        {
            return _substrate;
        }
        public void SetSubstrate(CustomShape shape)
        {
            _substrate = shape;
            UpdateLayout();
        }
        public ListPosition AreaPosition = ListPosition.No;
        public int FirstVisibleItem = 0;
        public int LastVisibleItem = 0;

        static int count = 0;
        public ListArea()
        {
            SetItemName("ListArea_" + count);
            count++;
            //Border.Radius = 50;

            EventMouseClick += OnMouseClick;
        }

        //overrides
        public override void InitElements()
        {
            //substrate
            _substrate.SetBackground(121, 223, 152);
            _substrate.SetAlignment(ItemAlignment.Left | ItemAlignment.Top);
            _substrate.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            _substrate.IsVisible = false;
            base.AddItem(_substrate);
        }

        public void OnMouseClick(IItem sender, MouseArgs args)
        {
            for (int i = FirstVisibleItem; i <= LastVisibleItem; i++)//?????? fuck!!!
            {
                if (i == GetItems().Count)
                    break;

                if (GetItems().ElementAt(i).Equals(_substrate))
                    continue;

                int y = GetItems().ElementAt(i).GetY();
                int h = GetItems().ElementAt(i).GetHeight();
                if (_mouse_ptr.Y > y && _mouse_ptr.Y < (y + h))
                {
                    SetSelection(i - 1);
                    break;
                }
            }
            SelectionChanged?.Invoke();
        }
        public override void InvokePoolEvents()
        {
            //EventMouseClick?.Invoke(this);
        }
        public override void AddItem(BaseItem item)
        {
            item.IsVisible = false;
            base.AddItem(item);
            UpdateLayout();
        }
        public override void RemoveItem(BaseItem item)
        {
            Unselect();
            base.RemoveItem(item);
            UpdateLayout();
            ItemListChanged?.Invoke();
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
            AreaPosition = ListPosition.No;

            Int64 offset = (-1) * GetVScrollOffset();
            int startY = GetY() + GetPadding().Top;
            int index = 0;
            foreach (var child in GetItems())
            {
                if (child.Equals(_substrate))
                    continue;

                child.SetX((-1) * (int)_xOffset + GetX() + GetPadding().Left + child.GetMargin().Left);

                Int64 child_Y = startY + offset + child.GetMargin().Top;
                offset += child.GetHeight() + GetSpacing().Vertical;

                //top checking
                if (child_Y < startY)
                {
                    AreaPosition |= ListPosition.Top;
                    if (child_Y + child.GetHeight() <= startY)
                    {
                        child.IsVisible = false;
                        if (_selection == index)
                            _substrate.IsVisible = false;
                    }
                    else
                    {
                        child.SetY((int)child_Y);
                        child.IsVisible = true;
                        FirstVisibleItem = index + 1;
                        if (_selection == index)
                            SetSubstrate(child);
                    }
                    index++;
                    continue;
                }

                //bottom checking
                if (child_Y + child.GetHeight() + child.GetMargin().Bottom > GetY() + GetHeight() - GetPadding().Bottom)
                {
                    AreaPosition |= ListPosition.Bottom;
                    if (child_Y >= GetY() + GetHeight() - GetPadding().Bottom)
                    {
                        child.IsVisible = false;
                        if (_selection == index)
                            _substrate.IsVisible = false;
                    }
                    else
                    {
                        child.SetY((int)child_Y);
                        child.IsVisible = true;
                        LastVisibleItem = index + 1;
                        if (_selection == index)
                            SetSubstrate(child);
                    }
                    index++;
                    continue;
                }

                child.SetY((int)child_Y);
                child.IsVisible = true;
                LastVisibleItem = index + 1;
                if (_selection == index)
                    SetSubstrate(child);
                index++;

                //refactor
                child.SetConfines();
            }
        }
        private void SetSubstrate(BaseItem child)
        {
            if (!_show_selection)
            {
                _substrate.IsVisible = false;
                return;
            }

            _substrate.IsVisible = true;
            _substrate.SetHeight(child.GetHeight() + 2);
            _substrate.SetTriangles(GraphicsMathService.GetRoundSquare(_substrate.GetWidth(), _substrate.GetHeight(), (child as VisualItem).Border.Radius, _substrate.GetX(), _substrate.GetY()));
            _substrate.SetY(child.GetY() - 1);
        }
    }
}
