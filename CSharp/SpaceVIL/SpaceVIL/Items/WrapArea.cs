using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using System.Threading;

namespace SpaceVIL
{
    public class WrapArea : Prototype, IGrid
    {
        internal Dictionary<IBaseItem, SelectionItem> _mapContent = new Dictionary<IBaseItem, SelectionItem>();
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
        public void SetSelectionVisibility(bool visibility)
        {
            _isSelectionVisible = visibility;
            if (!_isSelectionVisible)
                Unselect();
            foreach (SelectionItem item in _mapContent.Values)
            {
                item.SetToggleVisibility(_isSelectionVisible);
            }
        }
        public bool GetSelectionVisibility()
        {
            return _isSelectionVisible;
        }

        List<int> _list_of_visible_items = new List<int>();

        static int count = 0;

        /// <summary>
        /// Constructs a WrapArea
        /// </summary>
        private WrapArea()
        {
            SetItemName("WrapArea_" + count);
            count++;
            // SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.WrapArea)));
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

        }

        private SelectionItem GetWrapper(IBaseItem item)
        {
            SelectionItem wrapper = new SelectionItem(item);
            wrapper.SetToggleVisibility(_isSelectionVisible);
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
        /// Set width of the WrapArea
        /// </summary>
        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            UpdateLayout();
        }

        /// <summary>
        /// Set height of the WrapArea
        /// </summary>
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            UpdateLayout();
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
        internal Orientation WrapOrientation = Orientation.Horizontal;
        private Int64 _yOffset = 0;
        private Int64 _xOffset = 0;

        /// <summary>
        /// Vertical scroll offset in the WrapArea
        /// </summary>
        public Int64 GetScrollOffset()
        {
            if (WrapOrientation == Orientation.Horizontal)
                return _yOffset;
            else
                return _xOffset;
        }
        public void SetScrollOffset(Int64 value)
        {
            if (WrapOrientation == Orientation.Horizontal)
                _yOffset = value;
            else
                _xOffset = value;
            UpdateLayout();
        }

        /// <summary>
        /// Update all children and WrapArea sizes and positions
        /// according to confines
        /// </summary>
        //Update Layout
        public void UpdateLayout()
        {
            if (WrapOrientation == Orientation.Horizontal)
            {

            }
            else if(WrapOrientation == Orientation.Vertical)
            {

            }
        }
    }
}
