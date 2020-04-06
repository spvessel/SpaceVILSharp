using System;
using System.Collections.Generic;
using System.Linq;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// ListArea is a scrollable container for other elements with ability of selection. 
    /// ListArea is part of SpaceVIL.ListBox which controls scrolling, resizing and etc.
    /// <para/> Supports all events except drag and drop.
    /// </summary>
    public class ListArea : Prototype, IVLayout
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
        /// Disposing ListArea resources if it was removed.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void Release()
        {
            SelectionChanged = null;
            ItemListChanged = null;
        }

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
        public IBaseItem GetSelectedItem()
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
        /// Enable or disable selection ability of ListArea.
        /// </summary>
        /// <param name="value">True: if you want selection ability of ListArea to be enabled. 
        /// False: if you want selection ability of ListArea to be disabled.</param>
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
        /// Returns True if selection ability of ListArea is enabled otherwise returns False.
        /// </summary>
        /// <returns>True: selection ability of ListArea is enabled. 
        /// False: selection ability of ListArea is disabled.</returns>
        public bool IsSelectionVisible()
        {
            return _isSelectionVisible;
        }

        static int count = 0;

        /// <summary>
        /// Default ListArea constructor.
        /// </summary>
        public ListArea()
        {
            SetItemName("ListArea_" + count);
            count++;
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
            bool changed = false;
            List<IBaseItem> list = GetItems();
            switch (args.Key)
            {
                case KeyCode.Up:
                    while (index > 0)
                    {
                        index--;
                        _selectionItem = list.ElementAt(index) as SelectionItem;
                        if (_selectionItem.IsVisible())
                        {
                            changed = true;
                            break;
                        }
                    }
                    if (changed)
                        SetSelection(index);
                    break;
                case KeyCode.Down:
                    while (index < list.Count - 1)
                    {
                        index++;
                        _selectionItem = list.ElementAt(index) as SelectionItem;
                        if (_selectionItem.IsVisible())
                        {
                            changed = true;
                            break;
                        }
                    }
                    if (changed)
                        SetSelection(index);
                    break;
                case KeyCode.Escape:
                    Unselect();
                    break;
                default:
                    break;
            }
        }

        private SelectionItem GetWrapper(IBaseItem item)
        {
            SelectionItem wrapper = new SelectionItem(item);
            wrapper.SetStyle(_selectedStyle);
            wrapper.SetToggleVisible(_isSelectionVisible);
            wrapper.EventMouseClick += (sender, args) =>
            {
                int index = 0;
                if (_mapContent.ContainsKey(item))
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
        /// Insert item into the ListArea by index.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        /// <param name="index">Index of insertion.</param>
        public override void InsertItem(IBaseItem item, Int32 index)
        {
            SelectionItem wrapper = GetWrapper(item);
            base.InsertItem(wrapper, index);
            wrapper.UpdateHeight();
            _mapContent.Add(item, wrapper);
            UpdateLayout();

            if (index <= _selection)
                SetSelection(_selection + 1);
        }

        /// <summary>
        /// Add item to the ListArea.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        public override void AddItem(IBaseItem item)
        {
            SelectionItem wrapper = GetWrapper(item);
            base.AddItem(wrapper);
            wrapper.UpdateHeight();
            _mapContent.Add(item, wrapper);
            UpdateLayout();
        }
        /// <summary>
        /// Adding all elements in the ListArea from the given list.
        /// </summary>
        /// <param name="content">List of items as 
        /// System.Collections.Generic.IEnumerable&lt;IBaseItem&gt;</param>
        public virtual void SetListContent(IEnumerable<IBaseItem> content)
        {
            RemoveAllItems();
            IEnumerator<IBaseItem> node = content.GetEnumerator();
            while (node.MoveNext())
            {
                SelectionItem wrapper = GetWrapper(node.Current);
                base.AddItem(wrapper);
                wrapper.UpdateSize();
                _mapContent.Add(node.Current, wrapper);
            }
        }

        /// <summary>
        /// Removing the specified item from the ListArea.
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
        /// Removing all items from the ListArea.
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

            while (list.Count > 0)
            {
                (list.ElementAt(0) as SelectionItem).ClearContent();
                base.RemoveItem(list.ElementAt(0));
                list.RemoveAt(0);
            }

            _mapContent.Clear();
        }

        /// <summary>
        /// Setting Y coordinate of the left-top corner of the ListArea.
        /// </summary>
        /// <param name="y">Y position of the left-top corner.</param>
        public override void SetY(int y)
        {
            base.SetY(y);
            UpdateLayout();
        }

        //update content position
        private Int64 _yOffset = 0;
        private Int64 _xOffset = 0;

        /// <summary>
        /// Getting vertical scroll offset in the ListArea.
        /// </summary>
        /// <returns>Vertical scroll offset.</returns>
        public Int64 GetVScrollOffset()
        {
            return _yOffset;
        }
        /// <summary>
        /// Setting vertical scroll offset of the ListArea.
        /// </summary>
        /// <param name="value">Vertical scroll offset.</param>
        public void SetVScrollOffset(Int64 value)
        {
            _yOffset = value;
            UpdateLayout();
        }

        /// <summary>
        /// Getting horizontal scroll offset in the ListArea.
        /// </summary>
        /// <returns>Horizontal scroll offset.</returns>
        public Int64 GetHScrollOffset()
        {
            return _xOffset;
        }
        /// <summary>
        /// Setting horizontal scroll offset of the ListArea.
        /// </summary>
        /// <param name="value">Horizontal scroll offset.</param>
        public void SetHScrollOffset(Int64 value)
        {
            _xOffset = value;
            UpdateLayout();
        }

        private bool _isUpdating = false;
        /// <summary>
        /// Updating all children positions (implementation of SpaceVIL.Core.IVLayout).
        /// </summary>
        public void UpdateLayout()
        {
            List<IBaseItem> list = GetItems();

            if (list == null || list.Count == 0 || _isUpdating)
                return;
            _isUpdating = true;

            Int64 offset = (-1) * GetVScrollOffset();
            int startY = GetY() + GetPadding().Top;
            int child_X = (-1) * (int)_xOffset + GetX() + GetPadding().Left;
            foreach (var child in list)
            {
                if (!child.IsVisible())
                    continue;

                child.SetX(child_X + child.GetMargin().Left);

                Int64 child_Y = startY + offset + child.GetMargin().Top;
                offset += child.GetHeight() + GetSpacing().Vertical;
                child.SetY((int)child_Y);
                child.SetConfines();

                //top checking
                if (child_Y < startY)
                {
                    if (child_Y + child.GetHeight() <= startY)
                        child.SetDrawable(false);
                    else
                        child.SetDrawable(true);
                    continue;
                }

                //bottom checking
                if (child_Y + child.GetHeight() + child.GetMargin().Bottom > GetY() + GetHeight() - GetPadding().Bottom)
                {
                    if (child_Y >= GetY() + GetHeight() - GetPadding().Bottom)
                        child.SetDrawable(false);
                    else
                        child.SetDrawable(true);
                    continue;
                }
                child.SetDrawable(true);
            }
            _isUpdating = false;
        }

        private Style _selectedStyle;

        /// <summary>
        /// Setting style of the ListArea. 
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
