using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using System.Threading;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class WrapArea : Prototype, IGrid
    {
        internal Dictionary<IBaseItem, SelectionItem> _mapContent = new Dictionary<IBaseItem, SelectionItem>();
        private Object _lock = new Object();
        public EventCommonMethod SelectionChanged;
        public EventCommonMethod ItemListChanged;

        internal int _rows = 0;
        internal int _columns = 0;

        private int _step = 30;

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

        private int _selection = -1;

        /// <returns> Number of the selected item </returns>
        public int GetSelection()
        {
            return _selection;
        }

        private SelectionItem _selectionItem;

        /// <returns> selected item </returns>
        public IBaseItem GetSelectionItem()
        {
            if (_selectionItem != null)
                return _selectionItem.GetContent();
            return null;
        }
        internal SelectionItem GetTrueSelection()
        {
            return _selectionItem;
        }
        public void SetSelection(int index)
        {
            if (!_isSelectionVisible)
                return;
            _selection = index;
            _selectionItem = GetItems().ElementAt(index) as SelectionItem;
            _selectionItem.SetToggled(true);
            UnselectOthers(_selectionItem);
            Prototype tmp = _selectionItem.GetContent() as Prototype;
            if (tmp != null)
            {
                tmp.SetFocus();
            }
            SelectionChanged?.Invoke();
        }

        private void UnselectOthers(SelectionItem sender)
        {
            List<IBaseItem> items = GetItems();
            foreach (IBaseItem item in items)
            {
                if (!item.Equals(sender))
                {
                    ((SelectionItem)item).SetToggled(false);
                }
            }
        }

        public void Unselect()
        {
            _selection = -1;
            if (_selectionItem != null)
            {
                _selectionItem.SetToggled(false);
                _selectionItem = null;
            }
        }

        private bool _isSelectionVisible = true;
        /// <summary>
        /// Is selection changes view of the item or not
        /// </summary>
        public void SetSelectionVisible(bool value)
        {
            _isSelectionVisible = value;
            if (!_isSelectionVisible)
                Unselect();
            foreach (SelectionItem item in _mapContent.Values)
            {
                item.SetToggleVisible(_isSelectionVisible);
            }
        }
        public bool IsSelectionVisible()
        {
            return _isSelectionVisible;
        }

        static int count = 0;
        internal int _cellWidth = 0;
        internal int _cellHeight = 0;
        internal void SetCellSize(int cellWidth, int cellHeight)
        {
            _cellWidth = cellWidth;
            _cellHeight = cellHeight;
            UpdateLayout();
        }

        /// <summary>
        /// Constructs a WrapArea
        /// </summary>
        public WrapArea(int cellWidth, int cellHeight, Orientation orientation)
        {
            SetItemName("WrapArea_" + count);
            count++;
            _orientation = orientation;
            _cellWidth = cellWidth;
            _cellHeight = cellHeight;
            EventMouseClick += OnMouseClick;
            EventMouseDoubleClick += OnMouseDoubleClick;
            EventMouseHover += OnMouseHover;
            EventKeyPress += OnKeyPress;
        }

        void OnMouseClick(IItem sender, MouseArgs args) { }
        void OnMouseDoubleClick(IItem sender, MouseArgs args) { }
        void OnMouseHover(IItem sender, MouseArgs args) { }
        void OnKeyPress(IItem sender, KeyArgs args)
        {
            int index = _selection;
            int x, y;
            if (_orientation == Orientation.Horizontal)
            {
                x = _selection % _columns;
                y = _selection / _columns;
            }
            else
            {
                x = _selection / _rows;
                y = _selection % _rows;
            }

            switch (args.Key)
            {
                case KeyCode.Up:
                    y--;
                    if (y < 0)
                        y = 0;
                    index = GetIndexByCoord(x, y);
                    if (index != _selection)
                        SetSelection(index);
                    break;
                case KeyCode.Down:
                    y++;
                    if (y >= _rows)
                        y = _rows - 1;
                    index = GetIndexByCoord(x, y);
                    if (index >= GetItems().Count)
                        index = GetItems().Count - 1;
                    if (index != _selection)
                        SetSelection(index);
                    break;

                case KeyCode.Left:
                    x--;
                    if (x < 0)
                        x = 0;
                    index = GetIndexByCoord(x, y);
                    if (index != _selection)
                        SetSelection(index);
                    break;
                case KeyCode.Right:
                    x++;
                    if (x >= _columns)
                        x = _columns - 1;
                    index = GetIndexByCoord(x, y);
                    if (index >= GetItems().Count)
                        index = GetItems().Count - 1;
                    if (index != _selection)
                        SetSelection(index);
                    break;
                case KeyCode.Escape:
                    Unselect();
                    break;
                default:
                    break;
            }
        }

        private int GetIndexByCoord(int x, int y)
        {
            if (_orientation == Orientation.Horizontal)
            {
                return (x + y * _columns);
            }
            else
            {
                return (y + x * _rows);
            }
        }

        private SelectionItem GetWrapper(IBaseItem item)
        {
            SelectionItem wrapper = new SelectionItem(item);
            wrapper.SetStyle(_selectedStyle);
            
            wrapper.SetToggleVisible(_isSelectionVisible);
            wrapper.SetSize(_cellWidth, _cellHeight);
            wrapper.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            wrapper.EventMouseClick += (sender, args) =>
            {
                int index = 0;
                _selectionItem = _mapContent[item];
                foreach (IBaseItem tmp in base.GetItems())
                {
                    if (tmp.Equals(_selectionItem))
                    {
                        _selection = index;
                        SelectionChanged?.Invoke();
                        return;
                    }
                    index++;
                }
            };
            return wrapper;
        }

        /// <summary>
        /// Insert item to the WrapArea by row and column number
        /// </summary>
        public override void InsertItem(IBaseItem item, int index)
        {
            SelectionItem wrapper = GetWrapper(item);
            base.InsertItem(wrapper, index);
            _mapContent.Add(item, wrapper);
            UpdateLayout();
        }

        /// <summary>
        /// Add item to the WrapArea
        /// </summary>
        public override void AddItem(IBaseItem item)
        {
            SelectionItem wrapper = GetWrapper(item);
            base.AddItem(wrapper);
            _mapContent.Add(item, wrapper);
            UpdateLayout();
        }

        /// <summary>
        /// Remove item from the WrapArea
        /// </summary>
        public override void RemoveItem(IBaseItem item)
        {
            base.RemoveItem(_mapContent[item]);
            _mapContent.Remove(item);
            UpdateLayout();
            ItemListChanged?.Invoke();
        }

        public override void Clear()
        {
            RemoveAllItems();
        }

        internal void RemoveAllItems()
        {
            Monitor.Enter(_lock);
            try
            {
                Unselect();
                List<IBaseItem> list = new List<IBaseItem>(GetItems());

                if (list == null || list.Count == 0)
                    return;

                while (list.Count != 0)
                {
                    base.RemoveItem(list.ElementAt(0));
                    list.RemoveAt(0);
                }
                _mapContent.Clear();
                UpdateLayout();
                ItemListChanged?.Invoke();
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.StackTrace);
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }

        /// <summary>
        /// Set X position of the WrapArea
        /// </summary>
        public override void SetX(int _x)
        {
            base.SetX(_x);
            UpdateLayout();
        }

        /// <summary>
        /// Set Y position of the WrapArea
        /// </summary>
        public override void SetY(int _y)
        {
            base.SetY(_y);
            UpdateLayout();
        }

        //update content position
        internal Orientation _orientation = Orientation.Horizontal;
        private Int64 _yOffset = 0;
        private Int64 _xOffset = 0;

        /// <summary>
        /// Vertical scroll offset in the WrapArea
        /// </summary>
        public Int64 GetScrollOffset()
        {
            if (_orientation == Orientation.Horizontal)
                return _yOffset;
            else
                return _xOffset;
        }
        public void SetScrollOffset(Int64 value)
        {
            if (_orientation == Orientation.Horizontal)
                _yOffset = value;
            else
                _xOffset = value;
            UpdateLayout();
        }

        private bool _isUpdating = false;
        /// <summary>
        /// Update all children and WrapArea sizes and positions
        /// according to confines
        /// </summary>
        //Update Layout
        public void UpdateLayout()
        {
            if (GetItems().Count == 0 || _isUpdating)
                return;
            _isUpdating = true;

            Int64 offset = (-1) * GetScrollOffset();
            Int64 x = GetX() + GetPadding().Left;
            Int64 y = GetY() + GetPadding().Top;
            if (_orientation == Orientation.Horizontal)
            {
                //update
                Int64 globalY = y + offset;
                int w = GetWidth() - GetPadding().Left - GetPadding().Right;
                int itemCount = (w + GetSpacing().Horizontal) / (_cellWidth + GetSpacing().Horizontal);
                int column = 0;
                int row = 0;
                foreach (IBaseItem item in GetItems())
                {
                    if (!item.IsVisible())
                        continue;

                    // Console.WriteLine(item.GetItemName());
                    item.SetX((int)(x + (_cellWidth + GetSpacing().Horizontal) * column));
                    int itemY = (int)(globalY + (_cellHeight + GetSpacing().Vertical) * row);
                    item.SetY(itemY);
                    item.SetConfines();
                    column++;
                    if (column == itemCount)
                    {
                        column = 0;
                        row++;
                    }
                    //top check
                    if (itemY < y)
                    {
                        if (itemY + _cellHeight <= y)
                            item.SetDrawable(false);
                        else
                            item.SetDrawable(true);
                        continue;
                    }
                    //bottom check
                    if (itemY + _cellHeight > GetY() + GetHeight() - GetPadding().Bottom)
                    {
                        if (itemY >= GetY() + GetHeight() - GetPadding().Bottom)
                            item.SetDrawable(false);
                        else
                            item.SetDrawable(true);
                        continue;
                    }
                    item.SetDrawable(true);
                }
                if (GetItems().Count % itemCount == 0)
                    row--;
                _rows = row + 1;
                _columns = (itemCount > GetItems().Count) ? GetItems().Count : itemCount;
            }
            else if (_orientation == Orientation.Vertical)
            {
                //update
                Int64 globalX = x + offset;
                int h = GetHeight() - GetPadding().Top - GetPadding().Bottom;
                int itemCount = (h + GetSpacing().Vertical) / (_cellHeight + GetSpacing().Vertical);
                int column = 0;
                int row = 0;
                foreach (IBaseItem item in GetItems())
                {
                    item.SetY((int)(y + (_cellHeight + GetSpacing().Vertical) * row));

                    int itemX = (int)(globalX + (_cellWidth + GetSpacing().Horizontal) * column);
                    item.SetX(itemX);
                    item.SetConfines();
                    row++;
                    if (row == itemCount)
                    {
                        row = 0;
                        column++;
                    }
                    //left check
                    if (itemX < x)
                    {
                        if (itemX + _cellWidth <= x)
                            item.SetDrawable(false);
                        else
                            item.SetDrawable(true);
                        continue;
                    }
                    //right check
                    if (itemX + _cellWidth > GetX() + GetWidth() - GetPadding().Left)
                    {
                        if (itemX >= GetX() + GetWidth() - GetPadding().Left)
                            item.SetDrawable(false);
                        else
                            item.SetDrawable(true);
                        continue;
                    }
                    item.SetDrawable(true);
                }
                if (GetItems().Count % itemCount == 0)
                    column--;
                _columns = column + 1;
                _rows = (itemCount > GetItems().Count) ? GetItems().Count : itemCount;
            }
            _isUpdating = false;
        }

        private Style _selectedStyle;
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);

            Style inner_style = style.GetInnerStyle("selection");
            if (inner_style != null)
            {
                _selectedStyle = inner_style.Clone();
                List<IBaseItem> list = new List<IBaseItem>(GetItems());
                foreach (IBaseItem item in list)
                {
                    item.SetStyle(_selectedStyle);
                }
            }
        }
    }
}
