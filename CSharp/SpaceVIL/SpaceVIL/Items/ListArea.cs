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
        private Object _lock = new Object();
        public EventCommonMethod SelectionChanged;
        public EventCommonMethod ItemListChanged;

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

        /// <summary>
        /// Set selected item by index
        /// </summary>
        public void SetSelection(int index)
        {
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
        public void SetSelectionVisibility(bool visibility)
        {
            _isSelectionVisible = visibility;
            UpdateLayout();
        }
        public bool GetSelectionVisibility()
        {
            return _isSelectionVisible;
        }

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
            EventKeyPress += OnKeyPress;
            // SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.ListArea)));
        }

        void OnMouseClick(IItem sender, MouseArgs args) { }
        void OnMouseDoubleClick(IItem sender, MouseArgs args) { }
        void OnMouseHover(IItem sender, MouseArgs args) { }

        void OnKeyPress(IItem sender, KeyArgs args)
        {
            int index = _selection;
            switch (args.Key)
            {
                case KeyCode.Up:
                    index--;
                    if (index < 0)
                        break;
                    SetSelection(index);
                    break;
                case KeyCode.Down:
                    index++;
                    if (index >= base.GetItems().Count)
                        break;
                    SetSelection(index);
                    break;
                case KeyCode.Escape:
                    Unselect();
                    break;
                default:
                    break;
            }
        }

        /// <summary>
        /// If something changes when mouse hovered
        /// </summary>
        public override void SetMouseHover(bool value)
        {
            base.SetMouseHover(value);
        }

        internal Dictionary<IBaseItem, SelectionItem> _mapContent = new Dictionary<IBaseItem, SelectionItem>();

        private SelectionItem GetWrapper(IBaseItem item)
        {
            SelectionItem wrapper = new SelectionItem(item);
            // wrapper.setStyle(_selectedStyle);
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
        /// Insert item into the ListArea by index
        /// </summary>
        public override void InsertItem(IBaseItem item, Int32 index)
        {
            SelectionItem wrapper = GetWrapper(item);
            base.InsertItem(wrapper, index);
            _mapContent.Add(item, wrapper);
            UpdateLayout();
        }

        private Style _selectedStyle;

        /// <summary>
        /// Add item to the ListArea
        /// </summary>
        public override void AddItem(IBaseItem item)
        {
            SelectionItem wrapper = GetWrapper(item);
            base.AddItem(wrapper);
            _mapContent.Add(item, wrapper);
            UpdateLayout();
        }

        /// <summary>
        /// Remove item from the ListArea
        /// </summary>
        public override void RemoveItem(IBaseItem item)
        {
            base.RemoveItem(_mapContent[item]);
            _mapContent.Remove(item);
            UpdateLayout();
            ItemListChanged?.Invoke();
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
            foreach (var child in base.GetItems())
            {
                index++;
                if (!child.IsVisible())
                    continue;

                child.SetX(child_X + child.GetMargin().Left);

                Int64 child_Y = startY + offset + child.GetMargin().Top;
                offset += child.GetHeight() + GetSpacing().Vertical;

                //top checking
                if (child_Y < startY)
                {
                    child.SetY((int)child_Y);
                    if (child_Y + child.GetHeight() <= startY)
                    {
                        child.SetDrawable(false);
                    }
                    else
                    {
                        child.SetDrawable(true);
                        _list_of_visible_items.Add(index);
                    }
                    child.SetConfines();
                    continue;
                }

                //bottom checking
                if (child_Y + child.GetHeight() + child.GetMargin().Bottom > GetY() + GetHeight() - GetPadding().Bottom)
                {
                    child.SetY((int)child_Y);
                    if (child_Y >= GetY() + GetHeight() - GetPadding().Bottom)
                    {
                        child.SetDrawable(false);
                    }
                    else
                    {
                        child.SetDrawable(true);
                        _list_of_visible_items.Add(index);
                    }
                    child.SetConfines();
                    continue;
                }
                child.SetY((int)child_Y);
                child.SetDrawable(true);
                _list_of_visible_items.Add(index);

                //refactor
                child.SetConfines();
            }
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

            Style inner_style = style.GetInnerStyle("selecteditem");
            if (inner_style != null)
            {
                _selectedStyle = inner_style;
            }
        }
    }
}
