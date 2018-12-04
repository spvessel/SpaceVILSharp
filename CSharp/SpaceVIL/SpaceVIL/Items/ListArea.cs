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

        /// <summary>
        /// ScrollBar moving step
        /// </summary>
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

        /// <returns> Number of the selected item </returns>
        public int GetSelection()
        {
            return _selection;
        }

        /// <returns> selected item </returns>
        public IBaseItem GetSelectionItem()
        {
            return GetItems().ElementAt(_selection + 2);
        }

        /// <summary>
        /// Set selected item by index
        /// </summary>
        public void SetSelection(int index)
        {
            _selection = index;
            if (index < 0)
                _selection = 0;
            if (index >= GetItems().Count - 2)
                _selection = GetItems().Count - 3;
            UpdateLayout();
        }

        /// <summary>
        /// Unselect all items
        /// </summary>
        public void Unselect()
        {
            _selection = -1;
            _substrate.SetVisible(false);
            _hover_substrate.SetVisible(false);
        }

        /// <summary>
        /// Is selection changes view of the item or not
        /// </summary>
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

        /// <summary>
        /// Substrate under the selected item
        /// </summary>
        public Rectangle GetSubstrate()
        {
            return _substrate;
        }
//        public void SetSubstrate(Rectangle shape)
//        {
//            _substrate = shape;
//            UpdateLayout();
//        }

        private bool _show_hover = true;

        /// <summary>
        /// Is hovering changes view of the item or not
        /// </summary>
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

        /// <summary>
        /// Substrate under the hovered item
        /// </summary>
        public Rectangle GetHoverSubstrate()
        {
            return _hover_substrate;
        }

        // public int FirstVisibleItem = 0;
        // public int LastVisibleItem = 0;

        List<int> _list_of_visible_items = new List<int>();

        static int count = 0;

        /// <summary>
        /// Constructs a ListArea
        /// </summary>
        public ListArea()
        {
            SetItemName("ListArea_" + count);
            count++;
            EventMouseClick += OnMouseClick;
            EventMouseDoubleClick += OnMouseDoubleClick;
            EventMouseHover += OnMouseHover;
            //events
            EventKeyPress += OnKeyPress;
        }

        void OnMouseClick(IItem sender, MouseArgs args)
        {
            Unselect();
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
        void OnMouseDoubleClick(IItem sender, MouseArgs args)
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

        void OnMouseHover(IItem sender, MouseArgs args)
        {
            if (!GetHoverVisibility())
                return;
            bool found = false;
            foreach (IBaseItem item in GetItems())
            {
                if (item.Equals(_substrate) || item.Equals(_hover_substrate) || !item.IsVisible() || !item.IsDrawable())
                    continue;

                if (args.Position.GetY() > item.GetY() && args.Position.GetY() < item.GetY() + item.GetHeight())
                {
                    _hover_substrate.SetHeight(item.GetHeight());
                    _hover_substrate.SetPosition(GetX() + GetPadding().Left, item.GetY());
                    _hover_substrate.SetVisible(true);
                    _hover_substrate.SetDrawable(true);
                    found = true;
                    break;
                }
            }
            if (!found)
            {
                _hover_substrate.SetVisible(false);
                _hover_substrate.SetDrawable(false);
            }
        }

        void OnKeyPress(IItem sender, KeyArgs args)
        {
            Prototype tmp = GetSelectionItem() as Prototype;
            int index = GetSelection();
            switch (args.Key)
            {
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

        //overrides
        /// <summary>
        /// Initialization and adding of all elements in the ListArea
        /// </summary>
        public override void InitElements()
        {
            _substrate.SetVisible(false);
            base.AddItems(_substrate, _hover_substrate);
        }

        /// <summary>
        /// If something changes when mouse hovered
        /// </summary>
        public override void SetMouseHover(bool value)
        {
            base.SetMouseHover(value);
            if (!value)
                _hover_substrate.SetDrawable(false);
        }

        /// <summary>
        /// Insert item into the ListArea by index
        /// </summary>
        public override void InsertItem(IBaseItem item, Int32 index)
        {
            Prototype tmp = item as Prototype;
            if (tmp != null)
                tmp.IsFocusable = false;

            item.SetDrawable(false);
            base.InsertItem(item, index);
            UpdateLayout();
        }

        /// <summary>
        /// Add item to the ListArea
        /// </summary>
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

        /// <summary>
        /// Remove item from the ListArea
        /// </summary>
        public override void RemoveItem(IBaseItem item)
        {
            Unselect();
            base.RemoveItem(item);
            UpdateLayout();
            ItemListChanged?.Invoke();
        }

        /// <summary>
        /// Set Y position of the ListArea
        /// </summary>
        public override void SetY(int _y)
        {
            base.SetY(_y);
            UpdateLayout();
        }

        //update content position
        private Int64 _yOffset = 0;
        private Int64 _xOffset = 0;

        /// <summary>
        /// Vertical scroll offset in the ListArea
        /// </summary>
        public Int64 GetVScrollOffset()
        {
            return _yOffset;
        }
        public void SetVScrollOffset(Int64 value)
        {
            _yOffset = value;
            _hover_substrate.SetDrawable(false);
            UpdateLayout();
        }

        /// <summary>
        /// Horizontal scroll offset in the ListArea
        /// </summary>
        public Int64 GetHScrollOffset()
        {
            return _xOffset;
        }
        public void SetHScrollOffset(Int64 value)
        {
            _xOffset = value;
            UpdateLayout();
        }

        /// <summary>
        /// Update all children and ListArea sizes and positions
        /// according to confines
        /// </summary>
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

        /// <summary>
        /// Set style of the ListArea
        /// </summary>
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
