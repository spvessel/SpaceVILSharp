using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class ListArea : Prototype, IVLayout
    {
        internal Dictionary<IBaseItem, SelectionItem> _mapContent = new Dictionary<IBaseItem, SelectionItem>();
        private Object _lock = new Object();
        public EventCommonMethod SelectionChanged;
        public EventCommonMethod ItemListChanged;

        public override void Release()
        {
            SelectionChanged = null;
            ItemListChanged = null;
        }

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
        /// Set selected item by index
        /// </summary>
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

        /// <summary>
        /// Unselect all items
        /// </summary>
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
            EventKeyPress += OnKeyPress;
        }

        void OnMouseClick(IItem sender, MouseArgs args) { }
        void OnMouseDoubleClick(IItem sender, MouseArgs args) { }
        void OnMouseHover(IItem sender, MouseArgs args) { }

        void OnKeyPress(IItem sender, KeyArgs args)
        {
            int index = _selection;
            bool changed = false;

            switch (args.Key)
            {
                case KeyCode.Up:
                    while (index > 0)
                    {
                        index--;
                        _selectionItem = GetItems().ElementAt(index) as SelectionItem;
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
                    while (index < base.GetItems().Count - 1)
                    {
                        index++;
                        _selectionItem = GetItems().ElementAt(index) as SelectionItem;
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
        /// Insert item into the ListArea by index
        /// </summary>
        public override void InsertItem(IBaseItem item, Int32 index)
        {
            SelectionItem wrapper = GetWrapper(item);
            base.InsertItem(wrapper, index);
            wrapper.UpdateSize();
            _mapContent.Add(item, wrapper);
            UpdateLayout();
        }

        /// <summary>
        /// Add item to the ListArea
        /// </summary>
        public override void AddItem(IBaseItem item)
        {
            SelectionItem wrapper = GetWrapper(item);
            base.AddItem(wrapper);
            wrapper.UpdateSize();
            _mapContent.Add(item, wrapper);
            UpdateLayout();
        }

        /// <summary>
        /// Remove item from the ListArea
        /// </summary>
        public override bool RemoveItem(IBaseItem item)
        {
            Unselect();
            bool b;
            SelectionItem tmp = item as SelectionItem;
            if (tmp != null)
            {
                _mapContent.Remove(tmp.GetContent());
                tmp.ClearContent();
                b = base.RemoveItem(tmp);
            }
            else
            {
                tmp = _mapContent[item];
                _mapContent.Remove(item);
                tmp.ClearContent();
                b = base.RemoveItem(tmp);
            }

            UpdateLayout();
            ItemListChanged?.Invoke();
            return b;
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
                    (list.ElementAt(0) as SelectionItem).ClearContent();
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

        private bool _isUpdating = false;
        /// <summary>
        /// Update all children and ListArea sizes and positions
        /// according to confines
        /// </summary>
        public void UpdateLayout()
        {
            if (GetItems().Count == 0 || _isUpdating)
                return;
            _isUpdating = true;

            Int64 offset = (-1) * GetVScrollOffset();
            int startY = GetY() + GetPadding().Top;
            int child_X = (-1) * (int)_xOffset + GetX() + GetPadding().Left;
            foreach (var child in base.GetItems())
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
