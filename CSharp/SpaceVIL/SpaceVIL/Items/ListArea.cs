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
            return GetItems().ElementAt(_selection + 2);
        }
        public void SetSelection(int index)
        {
            _selection = index;
            if (index < 0)
                _selection = 0;
            if (index >= GetItems().Count - 2)
                _selection = GetItems().Count - 3;
            UpdateLayout();
        }
        public void Unselect()
        {
            _selection = -1;
            _substrate.SetVisible(false);
            _hover_substrate.SetVisible(false);
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

        private Rectangle _substrate = new Rectangle();
        public Rectangle GetSubstrate()
        {
            return _substrate;
        }
        public void SetSubstrate(Rectangle shape)
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

        // public int FirstVisibleItem = 0;
        // public int LastVisibleItem = 0;

        List<int> _list_of_visible_items = new List<int>();

        static int count = 0;
        public ListArea()
        {
            SetItemName("ListArea_" + count);
            count++;
            EventMouseClick += OnMouseClick;
            EventMouseDoubleClick += OnMouseDoubleClick;
            EventMouseHover += OnMouseHover;

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.ListArea)));

            //events
            EventKeyPress += OnKeyPress;
        }

        //overrides
        public override void InitElements()
        {
            _substrate.SetVisible(false);
            base.AddItems(_substrate, _hover_substrate);
        }

        public void OnMouseClick(IItem sender, MouseArgs args)
        {
            foreach (var index in _list_of_visible_items)
            {
                IBaseItem item = GetItems().ElementAt(index);
                int y = item.GetY();
                int h = item.GetHeight();
                if (args.Position.GetY() > y && args.Position.GetY() < (y + h))
                {
                    SetSelection(index - 2);
                    UpdateSubstrate();
                    SelectionChanged?.Invoke();
                    break;
                }
            }
        }
        public void OnMouseDoubleClick(IItem sender, MouseArgs args)
        {
            Prototype tmp = GetSelectionItem() as Prototype;
            if (tmp != null)
            {
                tmp.EventMouseDoubleClick?.Invoke(tmp, args);
            }
        }
        private void UpdateSubstrate()
        {
            if (_show_selection && GetSelection() >= 0)
            {
                IBaseItem item = GetItems().ElementAt(GetSelection() + 2);
                _substrate.SetHeight(item.GetHeight() + 4);
                _substrate.SetPosition(GetX() + GetPadding().Left, item.GetY() - 2);
                _substrate.SetVisible(true);
            }
        }

        protected void OnMouseHover(IItem sender, MouseArgs args)
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
                    break;
                }
            }
        }

        protected virtual void OnKeyPress(IItem sender, KeyArgs args)
        {
            Prototype tmp = GetSelectionItem() as Prototype;
            int index = GetSelection();
            switch (args.Key)
            {
                // case KeyCode.Backspace:
                //     foreach (var _ in GetItems())
                //     {
                //         Console.WriteLine(_.GetItemName() + " " + _.IsVisible() + " " + _.IsDrawable() + " " + GetItems().IndexOf(_));
                //     }
                //     break;

                case KeyCode.Up:
                    index--;
                    if (index < 0)
                        break;
                    if (!GetItems().ElementAt(index + 2).IsVisible())
                        while (index >= 0 && !GetItems().ElementAt(index + 2).IsVisible())
                            index--;
                    if (index >= 0)
                        SetSelection(index);
                    break;

                case KeyCode.Down:
                    index++;
                    if (index >= GetItems().Count - 2)
                        break;
                    if (!GetItems().ElementAt(index + 2).IsVisible())
                        while (index < (GetItems().Count - 2) && !GetItems().ElementAt(index + 2).IsVisible())
                            index++;
                    if (index < GetItems().Count - 2)
                        SetSelection(index);
                    break;

                case KeyCode.Escape:
                    Unselect();
                    break;
                case KeyCode.Enter:
                    if (tmp != null)
                        tmp.EventKeyPress?.Invoke(sender, args);
                    SelectionChanged?.Invoke();
                    break;
                default:
                    if (tmp != null)
                        tmp.EventKeyPress?.Invoke(sender, args);
                    break;
            }
        }

        public override void SetMouseHover(bool value)
        {
            base.SetMouseHover(value);
            if (!value)
                _hover_substrate.SetDrawable(false);
        }

        public override void InsertItem(IBaseItem item, Int32 index)
        {
            Prototype tmp = item as Prototype;
            if (tmp != null)
                tmp.IsFocusable = false;

            item.SetDrawable(false);
            base.InsertItem(item, index);
            UpdateLayout();
        }
        public override void AddItem(IBaseItem item)
        {
            Prototype tmp = item as Prototype;
            if (tmp != null)
            {
                if (!(tmp is ITextEditable))
                    tmp.IsFocusable = false;
            }

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
            _list_of_visible_items.Clear();

            Int64 offset = (-1) * GetVScrollOffset();
            int startY = GetY() + GetPadding().Top;
            int index = -1;
            int child_X = (-1) * (int)_xOffset + GetX() + GetPadding().Left;
            foreach (var child in GetItems())
            {
                index++;
                if (child.Equals(_substrate) || child.Equals(_hover_substrate) || !child.IsVisible())
                    continue;

                child.SetX(child_X + child.GetMargin().Left);

                Int64 child_Y = startY + offset + child.GetMargin().Top;
                offset += child.GetHeight() + GetSpacing().Vertical;

                //top checking
                if (child_Y < startY)
                {
                    if (child_Y + child.GetHeight() <= startY)
                    {
                        child.SetDrawable(false);
                        if (_selection == index - 2)
                            _substrate.SetDrawable(false);
                    }
                    else
                    {
                        child.SetY((int)child_Y);
                        child.SetDrawable(true);
                        _list_of_visible_items.Add(index);
                        if (_selection == index - 2)
                            _substrate.SetDrawable(true);
                    }
                    child.SetConfines();
                    continue;
                }

                //bottom checking
                if (child_Y + child.GetHeight() + child.GetMargin().Bottom > GetY() + GetHeight() - GetPadding().Bottom)
                {
                    if (child_Y >= GetY() + GetHeight() - GetPadding().Bottom)
                    {
                        child.SetDrawable(false);
                        if (_selection == index - 2)
                            _substrate.SetDrawable(false);
                    }
                    else
                    {
                        child.SetY((int)child_Y);
                        child.SetDrawable(true);
                        _list_of_visible_items.Add(index);
                        if (_selection == index - 2)
                            _substrate.SetDrawable(true);
                    }
                    child.SetConfines();
                    continue;
                }
                child.SetY((int)child_Y);
                child.SetDrawable(true);
                _list_of_visible_items.Add(index);
                if (_selection == index - 2)
                    _substrate.SetDrawable(true);

                //refactor
                child.SetConfines();
            }
            UpdateSubstrate();
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
            inner_style = style.GetInnerStyle("hovercover");
            if (inner_style != null)
            {
                _hover_substrate.SetStyle(inner_style);
            }
        }
    }
}
