using System;
using System.Collections.Generic;
using System.Linq;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// WrapArea is a scrollable container for other elements with ability of selection. 
    /// WrapArea groups elements in cells of a certain size. 
    /// It can be oriented vertically or horizontally.
    /// WrapArea is part of SpaceVIL.WrapGrid which controls scrolling, resizing and etc.
    /// <para/> Supports all events except drag and drop.
    /// </summary>
    public class WrapArea : Prototype, IFreeLayout
    {
        internal Dictionary<IBaseItem, SelectionItem> _mapContent = new Dictionary<IBaseItem, SelectionItem>();
        private Object _lock = new Object();
        /// <summary>
        /// Event that is invoked when one of the element is selected.
        /// </summary>
        public EventCommonMethod SelectionChanged;
        /// <summary>
        /// Event that is invoked when one of the set of elements is changed.
        /// </summary>
        public EventCommonMethod ItemListChanged;
        /// <summary>
        /// Disposing ComboBoxDropDown resources if it was removed.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void Release()
        {
            SelectionChanged = null;
            ItemListChanged = null;
        }

        internal int Rows = 0;
        internal int Columns = 0;

        private int _step = 30;

        /// <summary>
        /// Setting scroll movement step.
        /// </summary>
        /// <param name="value">Scroll step.</param>
        public void SetStep(int value)
        {
            _step = value;
        }
        /// <summary>
        /// Getting scroll movement step.
        /// </summary>
        /// <returns>Scroll step.</returns>
        public int GetStep()
        {
            return _step;
        }

        private bool _isStretch = false;
        /// <summary>
        /// Returns True if WrapArea allocates all available space between cells 
        /// to achieve smooth streching, otherwise returns False.
        /// </summary>
        /// <returns>True: if WrapArea allocates all available space between cells.
        /// False: if space between cells is fixed.</returns>
        public bool IsStretch()
        {
            return _isStretch;
        }
        /// <summary>
        /// Setting strech mode for WrapArea. WrapArea can allocates all available 
        /// space between cells or uses fixed space between cells.
        /// </summary>
        /// <param name="value">True: if you want to WrapArea allocates 
        /// all available space between cells.
        /// False: if you want space between cells to be fixed.</param>
        public void SetStretch(bool value)
        {
            if (value == _isStretch)
                return;
            _isStretch = value;
            UpdateLayout();
        }

        private int _selection = -1;

        /// <summary>
        /// Getting index of selected item.
        /// </summary>
        /// <returns>Index of selected item.</returns>
        public int GetSelection()
        {
            return _selection;
        }

        private SelectionItem _selectionItem;

        /// <summary>
        /// Getting selected item.
        /// </summary>
        /// <returns>Selected item as SpaceVIL.Core.IBaseItem</returns>
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
        /// <summary>
        /// Select item by index.
        /// </summary>
        /// <param name="index">Index of selection.</param>
        public void SetSelection(int index)
        {
            if (!_isSelectionVisible)
                return;
            _selection = index;
            _selectionItem = GetItems().ElementAt(index) as SelectionItem;
            _selectionItem.SetSelected(true);
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
                    ((SelectionItem)item).SetSelected(false);
                }
            }
        }
        /// <summary>
        /// Unselect selected item.
        /// </summary>
        public void Unselect()
        {
            _selection = -1;
            if (_selectionItem != null)
            {
                _selectionItem.SetSelected(false);
                _selectionItem = null;
            }
        }

        private bool _isSelectionVisible = true;
        /// <summary>
        /// Enable or disable selection ability of WrapArea.
        /// </summary>
        /// <param name="value">True: if you want selection ability of WrapArea to be enabled. 
        /// False: if you want selection ability of WrapArea to be disabled.</param>
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
        /// <summary>
        /// Returns True if selection ability of WrapArea is enabled otherwise returns False.
        /// </summary>
        /// <returns>True: selection ability of WrapArea is enabled. 
        /// False: selection ability of WrapArea is disabled.</returns>
        public bool IsSelectionVisible()
        {
            return _isSelectionVisible;
        }

        static int count = 0;
        internal int CellWidth = 0;
        internal int CellHeight = 0;
        internal void SetCellSize(int cellWidth, int cellHeight)
        {
            CellWidth = cellWidth;
            CellHeight = cellHeight;
            UpdateLayout();
        }

        /// <summary>
        /// Default WrapArea constructor.
        /// </summary>
        public WrapArea(int cellWidth, int cellHeight, Orientation orientation)
        {
            SetItemName("WrapArea_" + count);
            count++;
            Orientation = orientation;
            CellWidth = cellWidth;
            CellHeight = cellHeight;
            EventMouseClick += OnMouseClick;
            EventMouseDoubleClick += OnMouseDoubleClick;
            EventMouseHover += OnMouseHover;
            EventKeyPress += OnKeyPress;
        }

        private void OnMouseClick(IItem sender, MouseArgs args) { }
        private void OnMouseDoubleClick(IItem sender, MouseArgs args) { }
        private void OnMouseHover(IItem sender, MouseArgs args) { }
        private void OnKeyPress(IItem sender, KeyArgs args)
        {
            int index = _selection;
            int x, y;
            if (Orientation == Orientation.Horizontal)
            {
                x = _selection % Columns;
                y = _selection / Columns;
            }
            else
            {
                x = _selection / Rows;
                y = _selection % Rows;
            }
            List<IBaseItem> list = GetItems();
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
                    if (y >= Rows)
                        y = Rows - 1;
                    index = GetIndexByCoord(x, y);
                    if (index >= list.Count)
                        index = list.Count - 1;
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
                    if (x >= Columns)
                        x = Columns - 1;
                    index = GetIndexByCoord(x, y);
                    if (index >= list.Count)
                        index = list.Count - 1;
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
            if (Orientation == Orientation.Horizontal)
            {
                return (x + y * Columns);
            }
            else
            {
                return (y + x * Rows);
            }
        }

        private SelectionItem GetWrapper(IBaseItem item)
        {
            SelectionItem wrapper = new SelectionItem(item);
            wrapper.SetStyle(_selectedStyle);

            wrapper.SetToggleVisible(_isSelectionVisible);
            wrapper.SetSize(CellWidth, CellHeight);
            wrapper.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            wrapper.EventMouseClick += (sender, args) =>
            {
                int index = 0;
                _selectionItem = _mapContent[item];
                foreach (IBaseItem tmp in GetItems())
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
        /// Insert item into the WrapArea by index.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        /// <param name="index">Index of insertion.</param>
        public override void InsertItem(IBaseItem item, int index)
        {
            SelectionItem wrapper = GetWrapper(item);
            base.InsertItem(wrapper, index);
            _mapContent.Add(item, wrapper);
            UpdateLayout();

            if (index <= _selection)
                SetSelection(_selection + 1);
        }

        /// <summary>
        /// Add item to the WrapArea.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        public override void AddItem(IBaseItem item)
        {
            SelectionItem wrapper = GetWrapper(item);
            base.AddItem(wrapper);
            _mapContent.Add(item, wrapper);
            UpdateLayout();
        }
        /// <summary>
        /// Adding all elements in the WrapArea from the given list.
        /// </summary>
        /// <param name="content">List of items as 
        /// System.Collections.Generic.IEnumerable&lt;IBaseItem&gt;</param>
        public virtual void SetListContent(IEnumerable<IBaseItem> content)
        {
            RemoveAllItems();
            foreach (IBaseItem item in content)
            {
                SelectionItem wrapper = GetWrapper(item);
                base.AddItem(wrapper);
                _mapContent.Add(item, wrapper);
            }
            UpdateLayout();
            ItemListChanged?.Invoke();
        }

        /// <summary>
        /// Removing the specified item from the WrapArea.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        /// <returns>True: if the removal was successful. 
        /// False: if the removal was unsuccessful.</returns>
        public override bool RemoveItem(IBaseItem item)
        {
            bool restore = false;
            SelectionItem currentSelection = null;

            bool b;
            SelectionItem tmp = item as SelectionItem;
            if (tmp != null)
            {
                if (GetTrueSelection() != null)
                {
                    currentSelection = GetTrueSelection();
                    restore = !currentSelection.Equals(tmp);
                }
                Unselect();
                _mapContent.Remove(tmp.GetContent());
                tmp.ClearContent();
                b = base.RemoveItem(tmp);
            }
            else
            {
                if (GetTrueSelection() != null)
                {
                    currentSelection = GetTrueSelection();
                    restore = !currentSelection.GetContent().Equals(item);
                }
                Unselect();
                tmp = _mapContent[item];
                _mapContent.Remove(item);
                tmp.ClearContent();
                b = base.RemoveItem(tmp);
            }

            UpdateLayout();

            if (restore)
                SetSelection(GetItems().IndexOf(currentSelection));

            ItemListChanged?.Invoke();
            return b;
        }
        /// <summary>
        /// Removing all items from the WrapArea.
        /// </summary>
        public override void Clear()
        {
            RemoveAllItems();
            UpdateLayout();
            ItemListChanged?.Invoke();
        }

        private void RemoveAllItems()
        {
            Unselect();
            List<IBaseItem> list = GetItems();

            if (list == null || list.Count == 0)
                return;

            while (list.Count != 0)
            {
                (list.ElementAt(0) as SelectionItem).ClearContent();
                base.RemoveItem(list.ElementAt(0));
                list.RemoveAt(0);
            }
            _mapContent.Clear();
        }

        /// <summary>
        /// Setting X coordinate of the left-top corner of the WrapArea.
        /// </summary>
        /// <param name="x">Y position of the left-top corner.</param>
        public override void SetX(int x)
        {
            base.SetX(x);
            UpdateLayout();
        }

        /// <summary>
        /// Setting Y coordinate of the left-top corner of the WrapArea.
        /// </summary>
        /// <param name="y">Y position of the left-top corner.</param>
        public override void SetY(int y)
        {
            base.SetY(y);
            UpdateLayout();
        }

        //update content position
        internal Orientation Orientation = Orientation.Horizontal;
        private Int64 _yOffset = 0;
        private Int64 _xOffset = 0;

        /// <summary>
        /// Getting vertical scroll offset in the WrapArea.
        /// </summary>
        /// <returns>Vertical scroll offset.</returns>
        public Int64 GetScrollOffset()
        {
            if (Orientation == Orientation.Horizontal)
                return _yOffset;
            else
                return _xOffset;
        }
        /// <summary>
        /// Setting vertical scroll offset of the WrapArea.
        /// </summary>
        /// <param name="value">Vertical scroll offset.</param>
        public void SetScrollOffset(Int64 value)
        {
            if (Orientation == Orientation.Horizontal)
                _yOffset = value;
            else
                _xOffset = value;
            UpdateLayout();
        }

        private bool _isUpdating = false;
        /// <summary>
        /// Updating all children positions (implementation of SpaceVIL.Core.IFreeLayout).
        /// </summary>
        public void UpdateLayout()
        {
            List<IBaseItem> list = GetItems();
            if (list == null || list.Count == 0 || _isUpdating)
                return;
            _isUpdating = true;

            Int64 offset = (-1) * GetScrollOffset();
            Int64 x = GetX() + GetPadding().Left;
            Int64 y = GetY() + GetPadding().Top;
            if (Orientation == Orientation.Horizontal)
            {
                //update
                Int64 globalY = y + offset;
                int w = GetWidth() - GetPadding().Left - GetPadding().Right;
                int itemCount = (w + GetSpacing().Horizontal) / (CellWidth + GetSpacing().Horizontal);
                int column = 0;
                int row = 0;
                Columns = (itemCount > list.Count) ? list.Count : itemCount;
                if (Columns == 0)
                {
                    Columns = 1;
                    itemCount = 1;
                }

                // stretch
                int xOffset = 0;
                if (_isStretch && itemCount < list.Count)
                {
                    int freeSpace = w - ((CellWidth + GetSpacing().Horizontal) * Columns) - GetSpacing().Horizontal;
                    xOffset = freeSpace / Columns;
                    if (Columns > 1)
                        xOffset = freeSpace / (Columns - 1);
                }

                foreach (IBaseItem item in list)
                {
                    if (!item.IsVisible())
                        continue;

                    item.SetSize(CellWidth, CellHeight);

                    item.SetX((int)(x + (CellWidth + GetSpacing().Horizontal + xOffset) * column));
                    int itemY = (int)(globalY + (CellHeight + GetSpacing().Vertical) * row);
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
                        if (itemY + CellHeight <= y)
                            item.SetDrawable(false);
                        else
                            item.SetDrawable(true);
                        continue;
                    }
                    //bottom check
                    if (itemY + CellHeight > GetY() + GetHeight() - GetPadding().Bottom)
                    {
                        if (itemY >= GetY() + GetHeight() - GetPadding().Bottom)
                            item.SetDrawable(false);
                        else
                            item.SetDrawable(true);
                        continue;
                    }
                    item.SetDrawable(true);
                }
                if (list.Count % itemCount == 0)
                    row--;
                Rows = row + 1;
            }
            else if (Orientation == Orientation.Vertical)
            {
                //update
                Int64 globalX = x + offset;
                int h = GetHeight() - GetPadding().Top - GetPadding().Bottom;
                int itemCount = (h + GetSpacing().Vertical) / (CellHeight + GetSpacing().Vertical);
                int column = 0;
                int row = 0;
                Rows = (itemCount > list.Count) ? list.Count : itemCount;
                if (Rows == 0)
                {
                    Rows = 1;
                    itemCount = 1;
                }

                // stretch
                int yOffset = 0;
                if (_isStretch && itemCount < list.Count)
                {
                    int freeSpace = h - ((CellHeight + GetSpacing().Vertical + yOffset) * Rows) - GetSpacing().Vertical;
                    yOffset = freeSpace / Rows;
                    if (Rows > 1)
                        yOffset = freeSpace / (Rows - 1);
                }

                foreach (IBaseItem item in list)
                {
                    if (!item.IsVisible())
                        continue;

                    item.SetSize(CellWidth, CellHeight);

                    item.SetY((int)(y + (CellHeight + GetSpacing().Vertical + yOffset) * row));
                    int itemX = (int)(globalX + (CellWidth + GetSpacing().Horizontal) * column);
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
                        if (itemX + CellWidth <= x)
                            item.SetDrawable(false);
                        else
                            item.SetDrawable(true);
                        continue;
                    }
                    //right check
                    if (itemX + CellWidth > GetX() + GetWidth() - GetPadding().Left)
                    {
                        if (itemX >= GetX() + GetWidth() - GetPadding().Left)
                            item.SetDrawable(false);
                        else
                            item.SetDrawable(true);
                        continue;
                    }
                    item.SetDrawable(true);
                }
                if (list.Count % itemCount == 0)
                    column--;
                Columns = column + 1;
            }
            _isUpdating = false;
        }

        private Style _selectedStyle;
        /// <summary>
        /// Setting style of the WrapArea. 
        /// <para/> Inner styles: "selection".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);

            Style inner_style = style.GetInnerStyle("selection");
            if (inner_style != null)
            {
                _selectedStyle = inner_style.Clone();
                List<IBaseItem> list = GetItems();
                foreach (IBaseItem item in list)
                {
                    item.SetStyle(_selectedStyle);
                }
            }
        }
    }
}
