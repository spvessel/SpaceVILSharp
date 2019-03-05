using System;
using System.Linq;
using System.Drawing;
using System.Collections.Generic;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using SpaceVIL.Common;

namespace SpaceVIL
{
    public class WrapGrid : Prototype
    {
        static int count = 0;

        public int GetRowCount()
        {
            return _area._rows;
        }
        public int GetColumnCount()
        {
            return _area._columns;
        }
        public int GetCellWidth()
        {
            return _area._cellWidth;
        }
        public int GetCellHeight()
        {
            return _area._cellHeight;
        }
        public void SetCellWidth(int cellWidth)
        {
            _area._cellWidth = cellWidth;
            _area.UpdateLayout();
            UpdateSlider();
        }
        public void SetCellHeight(int cellHeight)
        {
            _area._cellHeight = cellHeight;
            _area.UpdateLayout();
            UpdateSlider();
        }
        public void SetCellSize(int cellWidth, int cellHeight)
        {
            _area.SetCellSize(cellWidth, cellHeight);
            UpdateSlider();
        }
        /// <summary>
        /// ScrollBar moving step
        /// </summary>
        public void SetScrollStep(int step)
        {
            _area.SetStep(step);
        }
        public int GetScrollStep()
        {
            return _area.GetStep();
        }

        /// <summary>
        /// Selection item
        /// </summary>
        public int GetSelection()
        {
            return _area.GetSelection();
        }

        /// <summary>
        /// Set selected item of the WrapGrid by index
        /// </summary>
        public void SetSelection(int index)
        {
            _area.SetSelection(index);
        }

        /// <summary>
        /// Unselect all items
        /// </summary>
        public void Unselect()
        {
            _area.Unselect();
        }

        /// <summary>
        /// Is selection changes view of the item or not
        /// </summary>
        public void SetSelectionVisibility(bool visibility)
        {
            _area.SetSelectionVisibility(visibility);
        }
        public bool GetSelectionVisibility()
        {
            return _area.GetSelectionVisibility();
        }

        private VerticalStack _vlayout;
        private HorizontalStack _hlayout;
        private WrapArea _area;

        /// <returns> WrapArea </returns>
        public WrapArea GetArea()
        {
            return _area;
        }

        private ContextMenu _menu;
        public VerticalScrollBar VScrollBar;
        public HorizontalScrollBar HScrollBar;
        private ScrollBarVisibility _scrollBarPolicy = ScrollBarVisibility.AsNeeded;

        public Orientation GetOrientation()
        {
            return _area._orientation;
        }

        /// <summary>
        /// Is vertical scroll bar visible
        /// </summary>
        public ScrollBarVisibility GetScrollBarVisible()
        {
            return _scrollBarPolicy;
        }
        public void SetScrollBarVisible(ScrollBarVisibility policy)
        {
            _scrollBarPolicy = policy;

            if (GetOrientation() == Orientation.Horizontal)
            {
                if (policy == ScrollBarVisibility.Never)
                    VScrollBar.SetDrawable(false);
                else if (policy == ScrollBarVisibility.AsNeeded)
                    VScrollBar.SetDrawable(false);
                else if (policy == ScrollBarVisibility.Always)
                    VScrollBar.SetDrawable(true);
                _hlayout.UpdateLayout();
            }
            else
            {
                if (policy == ScrollBarVisibility.Never)
                    HScrollBar.SetDrawable(false);
                else if (policy == ScrollBarVisibility.AsNeeded)
                    HScrollBar.SetDrawable(false);
                else if (policy == ScrollBarVisibility.Always)
                    HScrollBar.SetDrawable(true);
                _vlayout.UpdateLayout();
            }
        }

        /// <summary>
        /// Constructs a WrapGrid
        /// </summary>
        public WrapGrid(int cellWidth, int cellHeight, Orientation orientation)
        {
            SetItemName("WrapGrid_" + count);
            count++;

            _area = new WrapArea(cellWidth, cellHeight, orientation);
            if (GetOrientation() == Orientation.Horizontal)
            {
                _hlayout = new HorizontalStack();
                VScrollBar = new VerticalScrollBar();
                VScrollBar.SetDrawable(true);
            }
            else
            {
                _vlayout = new VerticalStack();
                HScrollBar = new HorizontalScrollBar();
                HScrollBar.SetDrawable(true);
            }

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.WrapGrid)));

            //events
            EventMouseClick += (sender, args) =>
            {
                if (_area.GetSelection() >= 0)
                    _area.SetSelection(_area.GetSelection());
                else
                    _area.SetFocus();
            };
            EventKeyPress += OnKeyPress;
        }

        private bool IsOutsideArea(SelectionItem selection)
        {
            if (GetOrientation() == Orientation.Horizontal)
            {
                int startY = _area.GetY() + GetPadding().Top;
                int endY = _area.GetY() + GetHeight() - GetPadding().Bottom;
                if (selection.GetY() + selection.GetHeight() < startY || selection.GetY() > endY)
                    return true;
                return false;
            }
            else
            {
                int startX = _area.GetX() + GetPadding().Left;
                int endX = _area.GetX() + GetWidth() - GetPadding().Right;
                if (selection.GetX() + selection.GetWidth() < startX || selection.GetX() > endX)
                    return true;
                return false;
            }
        }

        private void OnKeyPress(IItem sender, KeyArgs args)
        {
            SelectionItem selection = _area.GetTrueSelection();
            if (selection == null)
                return;
            long offset = _area.GetScrollOffset();
            int startY = _area.GetY() + GetPadding().Top;
            int startX = _area.GetX() + GetPadding().Left;
            long selection_Y = selection.GetY() + selection.GetMargin().Top;
            long selection_X = selection.GetX() + selection.GetMargin().Left;

            switch (args.Key)
            {
                case KeyCode.Up:
                    if (IsOutsideArea(selection) || (selection_Y < startY))
                    {
                        _area.SetScrollOffset(offset - (startY - selection_Y));
                        UpdateSlider();
                    }
                    break;
                case KeyCode.Down:
                    if (IsOutsideArea(selection))
                    {
                        _area.SetScrollOffset(offset - (startY - selection_Y));
                        UpdateSlider();
                        break;
                    }
                    if (selection_Y + selection.GetHeight() + selection.GetMargin().Bottom > GetY() + GetHeight() - GetPadding().Bottom)
                    {
                        _area.SetScrollOffset(offset + ((selection_Y + selection.GetHeight() + selection.GetMargin().Bottom
                                + _area.GetSpacing().Vertical) - (GetY() + GetHeight() - GetPadding().Bottom)));
                        UpdateSlider();
                    }
                    break;

                case KeyCode.Left:
                    if (IsOutsideArea(selection) || (selection_X < startX))
                    {
                        _area.SetScrollOffset(offset - (startX - selection_X));
                        UpdateSlider();
                    }
                    break;
                case KeyCode.Right:
                    if (IsOutsideArea(selection))
                    {
                        _area.SetScrollOffset(offset - (startX - selection_X));
                        UpdateSlider();
                        break;
                    }
                    if (selection_X + selection.GetWidth() + selection.GetMargin().Right > GetX() + GetWidth() - GetPadding().Right)
                    {
                        _area.SetScrollOffset(offset + ((selection_X + selection.GetWidth() + selection.GetMargin().Right
                                + _area.GetSpacing().Horizontal) - (GetX() + GetWidth() - GetPadding().Right)));
                        UpdateSlider();
                    }
                    break;

                case KeyCode.Escape:
                    Unselect();
                    break;
                default:
                    break;
            }
        }

        private Int64 v_size = 0;
        private Int64 h_size = 0;

        private void UpdateWrapArea()
        {
            if (GetOrientation() == Orientation.Horizontal)
            {
                //vertical slider
                float v_value = VScrollBar.Slider.GetCurrentValue();
                int v_offset = (int)Math.Round((float)(v_size * v_value) / 100.0f);
                _area.SetScrollOffset(v_offset);
            }
            else
            {
                //horizontal slider
                float h_value = HScrollBar.Slider.GetCurrentValue();
                int h_offset = (int)Math.Round((float)(h_size * h_value) / 100.0f);
                _area.SetScrollOffset(h_offset);
            }
        }

        private void UpdateSlider()//vertical slider
        {
            int total_invisible_size = 0;
            if (GetOrientation() == Orientation.Horizontal)
            {
                int visible_area = _area.GetHeight() - _area.GetPadding().Top - _area.GetPadding().Bottom;
                if (visible_area < 0)
                    visible_area = 0;
                int total = (_area._cellHeight + _area.GetSpacing().Vertical) * _area._rows - _area.GetSpacing().Vertical;
                if (total <= visible_area)
                {
                    VScrollBar.Slider.Handler.SetDrawable(false);
                    VScrollBar.Slider.SetStep(VScrollBar.Slider.GetMaxValue());
                    v_size = 0;
                    VScrollBar.Slider.SetCurrentValue(0);
                    if (GetScrollBarVisible() == ScrollBarVisibility.AsNeeded)
                    {
                        VScrollBar.SetDrawable(false);
                        Update();
                    }
                    return;
                }
                if (GetScrollBarVisible() == ScrollBarVisibility.AsNeeded)
                {
                    VScrollBar.SetDrawable(true);
                    Update();
                }
                VScrollBar.Slider.Handler.SetDrawable(true);
                total_invisible_size = total - visible_area;
                v_size = total_invisible_size;
                if (total_invisible_size > 0)
                {
                    float size_handler = (float)(visible_area) / (float)total * 100.0f;
                    size_handler = (float)VScrollBar.Slider.GetHeight() / 100.0f * size_handler;
                    //size of handler
                    VScrollBar.Slider.Handler.SetHeight((int)size_handler);
                }
                //step of slider
                float step_count = total_invisible_size / _area.GetStep();
                VScrollBar.Slider.SetStep((VScrollBar.Slider.GetMaxValue() - VScrollBar.Slider.GetMinValue()) / step_count);
                VScrollBar.Slider.SetCurrentValue((100.0f / total_invisible_size) * _area.GetScrollOffset());
            }
            else
            {
                int visible_area = _area.GetWidth() - _area.GetPadding().Left - _area.GetPadding().Right;
                if (visible_area < 0)
                    visible_area = 0;
                int total = (_area._cellWidth + _area.GetSpacing().Horizontal) * _area._columns - _area.GetSpacing().Horizontal;
                if (total <= visible_area)
                {
                    HScrollBar.Slider.Handler.SetDrawable(false);
                    HScrollBar.Slider.SetStep(HScrollBar.Slider.GetMaxValue());
                    h_size = 0;
                    HScrollBar.Slider.SetCurrentValue(0);
                    if (GetScrollBarVisible() == ScrollBarVisibility.AsNeeded)
                    {
                        HScrollBar.SetDrawable(false);
                        Update();
                    }
                    return;
                }
                if (GetScrollBarVisible() == ScrollBarVisibility.AsNeeded)
                {
                    HScrollBar.SetDrawable(true);
                    Update();
                }
                HScrollBar.Slider.Handler.SetDrawable(true);
                total_invisible_size = total - visible_area;
                h_size = total_invisible_size;
                if (total_invisible_size > 0)
                {
                    float size_handler = (float)(visible_area) / (float)total * 100.0f;
                    size_handler = (float)HScrollBar.Slider.GetWidth() / 100.0f * size_handler;
                    //size of handler
                    HScrollBar.Slider.Handler.SetWidth((int)size_handler);
                }
                //step of slider
                float step_count = total_invisible_size / _area.GetStep();
                HScrollBar.Slider.SetStep((HScrollBar.Slider.GetMaxValue() - HScrollBar.Slider.GetMinValue()) / step_count);
                HScrollBar.Slider.SetCurrentValue((100.0f / total_invisible_size) * _area.GetScrollOffset());
            }
        }

        private void Update()
        {
            if (GetOrientation() == Orientation.Horizontal)
                _hlayout.UpdateLayout();
            else
                _vlayout.UpdateLayout();
        }

        /// <summary>
        /// Set width of the ListBox
        /// </summary>
        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            UpdateSlider();
        }

        /// <summary>
        /// Set height of the ListBox
        /// </summary>
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            UpdateSlider();
        }

        /// <summary>
        /// Add item to the WrapGrid
        /// </summary>
        public override void AddItem(IBaseItem item)
        {
            _area.AddItem(item);
            UpdateSlider();
        }

        /// <summary>
        /// Insert item to the WrapGrid by index
        /// </summary>
        public override void InsertItem(IBaseItem item, Int32 index)
        {
            _area.InsertItem(item, index);
            UpdateSlider();
        }

        /// <summary>
        /// Remove item from the WrapGrid
        /// </summary>
        public override void RemoveItem(IBaseItem item)
        {
            _area.RemoveItem(item);
            UpdateSlider();
        }
        public virtual void Clear()
        {
            _area.RemoveAllItems();
            UpdateSlider();
        }

        /// <summary>
        /// Initialization and adding of all elements in the WrapGrid
        /// </summary>
        public override void InitElements()
        {
            //Adding
            _area.ItemListChanged += () => { UpdateSlider(); };

            if (GetOrientation() == Orientation.Horizontal)
            {
                base.AddItem(_hlayout);
                _hlayout.AddItems(_area, VScrollBar);
                EventScrollUp += VScrollBar.EventScrollUp.Invoke;
                EventScrollDown += VScrollBar.EventScrollDown.Invoke;
                VScrollBar.Slider.EventValueChanged += (sender) => { UpdateWrapArea(); };
            }
            else
            {
                base.AddItem(_vlayout);
                _vlayout.AddItems(_area, HScrollBar);
                EventScrollUp += HScrollBar.EventScrollUp.Invoke;
                EventScrollDown += HScrollBar.EventScrollDown.Invoke;
                HScrollBar.Slider.EventValueChanged += (sender) => { UpdateWrapArea(); };
            }
        }

        /// <returns> list of all WrapGrid items </returns>
        public List<IBaseItem> GetListContent()
        {
            List<IBaseItem> result = new List<IBaseItem>();
            List<IBaseItem> list = new List<IBaseItem>(_area.GetItems());

            foreach (IBaseItem item in list)
            {
                SelectionItem tmp = item as SelectionItem;
                result.Add(tmp.GetContent());
            }
            return result;
        }

        /// <summary>
        /// Set list of items
        /// </summary>
        public virtual void SetListContent(List<IBaseItem> content)
        {
            _area.RemoveAllItems();
            foreach (IBaseItem item in content)
                AddItem(item);
        }

        public IBaseItem GetWrapper(IBaseItem item)
        {
            return GetArea()._mapContent[item];
        }

        /// <returns> selection item </returns>
        public IBaseItem GetSelectionItem()
        {
            return _area.GetSelectionItem();
        }

        /// <summary>
        /// Set style of the WrapGrid
        /// </summary>
        //style
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);

            Style inner_style = style.GetInnerStyle("vscrollbar");
            if (inner_style != null && VScrollBar != null)
            {
                VScrollBar.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("hscrollbar");
            if (inner_style != null && HScrollBar != null)
            {
                HScrollBar.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("area");
            if (inner_style != null)
            {
                _area.SetStyle(inner_style);
            }
        }
    }
}
