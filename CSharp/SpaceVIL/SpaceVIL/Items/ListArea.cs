using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class ListArea : Prototype, IVLayout
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
        public IBaseItem GetSelectionItem()
        {
            return GetItems().ElementAt(_selection + 1);
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
            _substrate.SetVisible(false);
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

        private bool _show_hover = true;

        public void SetHoverVisibility(bool visibility)
        {
            _show_hover = visibility;
            UpdateLayout();
        }

        public bool GetHoverVisibility()
        {
            return _show_hover;
        }

        private Rectangle _hover_substrate = new Rectangle();

        public Rectangle GetHoverSubstrate()
        {
            return _hover_substrate;
        }

        public ListPosition AreaPosition = ListPosition.No;
        public int FirstVisibleItem = 0;
        public int LastVisibleItem = 0;

        static int count = 0;
        public ListArea()
        {
            SetItemName("ListArea_" + count);
            count++;
            EventMouseClick += OnMouseClick;
            EventMouseHover += OnMouseHover;

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.ListArea)));

            _hover_substrate.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            _hover_substrate.SetBackground(255, 255, 255, 30);
        }

        //overrides
        public override void InitElements()
        {
            //substrate
            // _substrate.SetBackground(111, 181, 255);
            // _substrate.SetAlignment(ItemAlignment.Left | ItemAlignment.Top);
            // _substrate.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            _substrate.SetVisible(false);
            base.AddItems(_substrate, _hover_substrate);
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
                if (args.Position.GetY() > y && args.Position.GetY() < (y + h))
                {
                    SetSelection(i - 1);
                    break;
                }
            }
            //SelectionChanged?.Invoke();
        }
        public void OnMouseHover(IItem sender, MouseArgs args)
        {
            if (!GetHoverVisibility())
                return;
            foreach (IBaseItem item in GetItems())
            {
                if (item.Equals(_substrate) || item.Equals(_hover_substrate) || !item.IsVisible() || !item.IsDrawable())
                    continue;
                if (args.Position.GetY() > item.GetY() && args.Position.GetY() < item.GetY() + item.GetHeight())
                {
                    _hover_substrate.SetHeight(item.GetHeight());
                    _hover_substrate.SetPosition(GetX() + GetPadding().Left, item.GetY());
                    _hover_substrate.SetDrawable(true);
                    _hover_substrate.SetVisible(true);
                    break;
                }
            }
            // Console.WriteLine(
            //     _hover_substrate.GetHeight() + " " +
            //     _hover_substrate.GetWidth() + " " +
            //     _hover_substrate.GetX() + " " +
            //     _hover_substrate.GetY() + " " +
            //     _hover_substrate.IsDrawable() + " " +
            //     _hover_substrate.IsVisible()
            // );
        }
        public override void SetMouseHover(bool value)
        {
            base.SetMouseHover(value);
            if (!value)
                _hover_substrate.SetDrawable(false);
        }

        public override void InsertItem(IBaseItem item, Int32 index)
        {
            item.SetDrawable(false);
            base.InsertItem(item, index);
            UpdateLayout();
        }
        public override void AddItem(IBaseItem item)
        {
            item.SetDrawable(false);
            base.AddItem(item);
            UpdateLayout();
        }
        public override void RemoveItem(IBaseItem item)
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
                if (child.Equals(_substrate) || child.Equals(_hover_substrate) || !child.IsVisible())
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
                        child.SetDrawable(false);
                        if (_selection == index)
                            _substrate.SetDrawable(false);
                    }
                    else
                    {
                        child.SetY((int)child_Y);
                        child.SetDrawable(true);
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
                        child.SetDrawable(false);
                        if (_selection == index)
                            _substrate.SetDrawable(false);
                    }
                    else
                    {
                        child.SetY((int)child_Y);
                        child.SetDrawable(true);
                        LastVisibleItem = index + 1;
                        if (_selection == index)
                            SetSubstrate(child);
                    }
                    index++;
                    continue;
                }

                child.SetY((int)child_Y);
                child.SetDrawable(true);
                LastVisibleItem = index + 1;
                if (_selection == index)
                    SetSubstrate(child);
                index++;

                //refactor
                child.SetConfines();
            }
        }
        private void SetSubstrate(IBaseItem child)
        {
            if (!_show_selection)
            {
                _substrate.SetVisible(false);
                return;
            }

            _substrate.SetVisible(true);
            _substrate.SetHeight(child.GetHeight() + 2);
            _substrate.SetMargin(
                -GetParent().GetPadding().Left,
                -GetParent().GetPadding().Top,
                -GetParent().GetPadding().Right,
                -GetParent().GetPadding().Bottom
                );
            _substrate.SetTriangles(GraphicsMathService.GetRoundSquare(child.GetWidth(), _substrate.GetHeight(), 0, _substrate.GetX(), _substrate.GetY()));
            _substrate.SetY(child.GetY() - 1);
        }

        //style
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);

            Style inner_style = style.GetInnerStyle("substrate");
            if (inner_style != null)
            {
                _substrate.SetStyle(inner_style);
            }
        }
    }
}
