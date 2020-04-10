using System;
using System.Collections.Generic;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using SpaceVIL.Common;

namespace SpaceVIL
{
    /// <summary>
    /// WrapGrid is a container and manager of SpaceVIL.WrapArea 
    /// (scrollable container for other elements with ability of selection, 
    /// groups elements in cells of a certain size)
    /// and scroll bars. WrapGrid controls scrolling, resizing and other actions of SpaceVIL.WrapArea.
    /// <para/> Contains list area, scroll bars, scroll menu.
    /// <para/> Supports all events except drag and drop.
    /// </summary>
    public class WrapGrid : Prototype
    {
        static int count = 0;

        /// <summary>
        /// Returns True if internal SpaceVIL.WrapArea allocates all available space between cells 
        /// to achieve smooth streching, otherwise returns False.
        /// </summary>
        /// <returns>True: if SpaceVIL.WrapArea allocates all available space between cells.
        /// False: if space between cells is fixed.</returns>
        public bool IsStretch()
        {
            return _area.IsStretch();
        }

        /// <summary>
        /// Setting strech mode for internal SpaceVIL.WrapArea. 
        /// SpaceVIL.WrapArea can allocates all available 
        /// space between cells or uses fixed space between cells.
        /// </summary>
        /// <param name="value">True: if you want to SpaceVIL.WrapArea allocates 
        /// all available space between cells.
        /// False: if you want space between cells to be fixed.</param>
        public void SetStretch(bool value)
        {
            _area.SetStretch(value);
        }

        /// <summary>
        /// Getting current row count.
        /// </summary>
        /// <returns>Row count.</returns>
        public int GetRowCount()
        {
            return _area.Rows;
        }

        /// <summary>
        /// Getting current column count.
        /// </summary>
        /// <returns>Column count.</returns>
        public int GetColumnCount()
        {
            return _area.Columns;
        }

        /// <summary>
        /// Getting current cell width.
        /// </summary>
        /// <returns>Cell width.</returns>
        public int GetCellWidth()
        {
            return _area.CellWidth;
        }

        /// <summary>
        /// Getting current cell height.
        /// </summary>
        /// <returns>Cell height.</returns>
        public int GetCellHeight()
        {
            return _area.CellHeight;
        }

        /// <summary>
        /// Setting cell width.
        /// </summary>
        /// <param name="cellWidth">Cell width.</param>
        public void SetCellWidth(int cellWidth)
        {
            _area.CellWidth = cellWidth;
            _area.UpdateLayout();
            UpdateSlider();
        }

        /// <summary>
        /// Setting cell height.
        /// </summary>
        /// <param name="cellHeight">Cell height.</param>
        public void SetCellHeight(int cellHeight)
        {
            _area.CellHeight = cellHeight;
            _area.UpdateLayout();
            UpdateSlider();
        }

        /// <summary>
        /// Setting cell size.
        /// </summary>
        /// <param name="cellWidth">Cell width.</param>
        /// <param name="cellHeight">Cell height.</param>
        public void SetCellSize(int cellWidth, int cellHeight)
        {
            _area.SetCellSize(cellWidth, cellHeight);
            UpdateSlider();
        }

        /// <summary>
        /// Setting scroll movement step.
        /// </summary>
        /// <param name="step">Scroll step.</param>
        public void SetScrollStep(int step)
        {
            _area.SetStep(step);
        }

        /// <summary>
        /// Getting scroll movement step.
        /// </summary>
        /// <returns>Scroll step.</returns>
        public int GetScrollStep()
        {
            return _area.GetStep();
        }

        /// <summary>
        /// Getting index of selected item.
        /// </summary>
        /// <returns>Index of selected item.</returns>
        public int GetSelection()
        {
            return _area.GetSelection();
        }

        /// <summary>
        /// Select item by index.
        /// </summary>
        /// <param name="index">Index of selection.</param>
        public void SetSelection(int index)
        {
            _area.SetSelection(index);
        }

        /// <summary>
        /// Unselect selected item.
        /// </summary>
        public void Unselect()
        {
            _area.Unselect();
        }

        /// <summary>
        /// Enable or disable selection ability of SpaceVIL.WrapArea.
        /// </summary>
        /// <param name="value">True: if you want selection ability of SpaceVIL.WrapArea to be enabled. 
        /// False: if you want selection ability of SpaceVIL.WrapArea to be disabled.</param>
        public void SetSelectionVisible(bool value)
        {
            _area.SetSelectionVisible(value);
        }

        /// <summary>
        /// Returns True if selection ability of SpaceVIL.WrapArea is enabled otherwise returns False.
        /// </summary>
        /// <returns>True: selection ability of SpaceVIL.WrapArea is enabled. 
        /// False: selection ability of SpaceVIL.WrapArea is disabled.</returns>
        public bool IsSelectionVisible()
        {
            return _area.IsSelectionVisible();
        }

        private VerticalStack _vlayout;
        private HorizontalStack _hlayout;
        private WrapArea _area;

        /// <summary>
        /// Getting list area of WrapGrid.
        /// </summary>
        /// <returns>List area as SpaceVIL.WrapArea.</returns>
        public WrapArea GetArea()
        {
            return _area;
        }

        /// <summary>
        /// Vertical scroll bar of WrapGrid.
        /// </summary>
        public VerticalScrollBar VScrollBar;

        /// <summary>
        /// Horizontal scroll bar of WrapGrid.
        /// </summary>
        public HorizontalScrollBar HScrollBar;

        private VisibilityPolicy _scrollBarPolicy = VisibilityPolicy.AsNeeded;

        /// <summary>
        /// Getting internal SpaceVIL.WrapArea orientation.
        /// <para/> Orientation can be Orientation.Horizontal 
        /// or Orientation.Vertical.
        /// </summary>
        /// <returns>Current SpaceVIL.WrapArea orientation.</returns>
        public Orientation GetOrientation()
        {
            return _area.Orientation;
        }

        /// <summary>
        /// Getting scroll bar visibility policy.
        /// </summary>
        /// <returns>Visibility policy as SpaceVIL.Core.VisibilityPolicy.</returns>
        public VisibilityPolicy GetScrollBarPolicy()
        {
            return _scrollBarPolicy;
        }

        /// <summary>
        /// Setting scroll bar visibility policy.
        /// <para/> Default: SpaceVIL.Core.VisibilityPolicy.AsNeeded.
        /// </summary>
        /// <param name="policy">Visibility policy as SpaceVIL.Core.VisibilityPolicy.</param>
        public void SetScrollBarPolicy(VisibilityPolicy policy)
        {
            _scrollBarPolicy = policy;

            if (GetOrientation() == Orientation.Horizontal)
            {
                if (policy == VisibilityPolicy.Never)
                    VScrollBar.SetDrawable(false);
                else if (policy == VisibilityPolicy.AsNeeded)
                    VScrollBar.SetDrawable(false);
                else if (policy == VisibilityPolicy.Always)
                    VScrollBar.SetDrawable(true);
                _hlayout.UpdateLayout();
            }
            else
            {
                if (policy == VisibilityPolicy.Never)
                    HScrollBar.SetDrawable(false);
                else if (policy == VisibilityPolicy.AsNeeded)
                    HScrollBar.SetDrawable(false);
                else if (policy == VisibilityPolicy.Always)
                    HScrollBar.SetDrawable(true);
                _vlayout.UpdateLayout();
            }
        }

        /// <summary>
        /// Constructs a WrapGrid with specified cell width, height and orientation.
        /// </summary>
        /// <param name="cellWidth">Width of each cell.</param>
        /// <param name="cellHeight">Height of each cell.</param>
        /// <param name="orientation">Orientation of layout as SpaceVIL.Core.Orientation.</param>
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
                int total = (_area.CellHeight + _area.GetSpacing().Vertical) * _area.Rows - _area.GetSpacing().Vertical;
                if (total <= visible_area)
                {
                    VScrollBar.Slider.Handler.SetDrawable(false);
                    VScrollBar.Slider.SetStep(VScrollBar.Slider.GetMaxValue());
                    v_size = 0;
                    VScrollBar.Slider.SetCurrentValue(0);
                    if (GetScrollBarPolicy() == VisibilityPolicy.AsNeeded)
                    {
                        VScrollBar.SetDrawable(false);
                        Update();
                    }
                    return;
                }
                if (GetScrollBarPolicy() == VisibilityPolicy.AsNeeded)
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
                int total = (_area.CellWidth + _area.GetSpacing().Horizontal) * _area.Columns - _area.GetSpacing().Horizontal;
                if (total <= visible_area)
                {
                    HScrollBar.Slider.Handler.SetDrawable(false);
                    HScrollBar.Slider.SetStep(HScrollBar.Slider.GetMaxValue());
                    h_size = 0;
                    HScrollBar.Slider.SetCurrentValue(0);
                    if (GetScrollBarPolicy() == VisibilityPolicy.AsNeeded)
                    {
                        HScrollBar.SetDrawable(false);
                        Update();
                    }
                    return;
                }
                if (GetScrollBarPolicy() == VisibilityPolicy.AsNeeded)
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
        /// Setting item width. If the value is greater/less than the maximum/minimum 
        /// value of the width, then the width becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="width"> Width of the item. </param>
        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            UpdateSlider();
        }

        /// <summary>
        /// Setting item height. If the value is greater/less than the maximum/minimum 
        /// value of the height, then the height becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="height"> Рeight of the item. </param>
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            UpdateSlider();
        }

        /// <summary>
        /// Adding item to the list area of WrapGrid.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        public override void AddItem(IBaseItem item)
        {
            _area.AddItem(item);
            UpdateSlider();
        }

        /// <summary>
        /// Insert item into the list area of WrapGrid by index.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        /// <param name="index">Index of insertion.</param>
        public override void InsertItem(IBaseItem item, Int32 index)
        {
            _area.InsertItem(item, index);
            UpdateSlider();
        }

        /// <summary>
        /// Removing the specified item from the list area of WrapGrid.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        /// <returns>True: if the removal was successful. 
        /// False: if the removal was unsuccessful.</returns>
        public override bool RemoveItem(IBaseItem item)
        {
            List<IBaseItem> list = GetItems();
            if (list.Contains(item))
            {
                return base.RemoveItem(item);
            }
            bool b = _area.RemoveItem(item);
            UpdateSlider();
            return b;
        }

        /// <summary>
        /// Removing all items from the list area of WrapGrid.
        /// </summary>
        public override void Clear()
        {
            _area.Clear();
        }

        /// <summary>
        /// Initializing all elements in the WrapGrid. 
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            //Adding
            _area.ItemListChanged += () => { UpdateSlider(); };

            if (GetOrientation() == Orientation.Horizontal)
            {
                base.AddItem(_hlayout);
                _hlayout.AddItems(_area, VScrollBar);

                EventScrollUp += (sender, args) =>
                {
                    if (args.Mods == 0)
                        VScrollBar.EventScrollUp.Invoke(sender, args);
                };

                EventScrollDown += (sender, args) =>
                {
                    if (args.Mods == 0)
                        VScrollBar.EventScrollDown.Invoke(sender, args);
                };

                VScrollBar.Slider.EventValueChanged += (sender) => { UpdateWrapArea(); };
            }
            else
            {
                base.AddItem(_vlayout);
                _vlayout.AddItems(_area, HScrollBar);

                EventScrollUp += (sender, args) =>
                {
                    if (args.Mods == 0)
                        HScrollBar.EventScrollUp.Invoke(sender, args);
                };

                EventScrollDown += (sender, args) =>
                {
                    if (args.Mods == 0)
                        HScrollBar.EventScrollDown.Invoke(sender, args);
                };
                HScrollBar.Slider.EventValueChanged += (sender) => { UpdateWrapArea(); };
            }
        }

        /// <summary>
        /// Getting content of the list area of WrapGrid.
        /// </summary>
        /// <returns>Content of the list area as List&lt;IBaseItem&gt;</returns>
        public List<IBaseItem> GetListContent()
        {
            List<IBaseItem> result = new List<IBaseItem>();
            List<IBaseItem> list = _area.GetItems();

            foreach (IBaseItem item in list)
            {
                SelectionItem tmp = item as SelectionItem;
                result.Add(tmp.GetContent());
            }
            return result;
        }

        /// <summary>
        /// Adding all elements in the list area of WrapGrid from the given list.
        /// </summary>
        /// <param name="content">List of items as List&lt;IBaseItem&gt;</param>
        public virtual void SetListContent(List<IBaseItem> content)
        {
            _area.SetListContent(content);
            // UpdateSlider();
        }

        /// <summary>
        /// Getting wrapper of item.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        /// <returns>Wrapper of given item as SpaceVIL.SelectionItem.</returns>
        public IBaseItem GetWrapper(IBaseItem item)
        {
            return GetArea()._mapContent[item];
        }

        /// <summary>
        /// Getting selected item.
        /// </summary>
        /// <returns>Selected item as SpaceVIL.Core.IBaseItem</returns>
        public IBaseItem GetSelectedItem()
        {
            return _area.GetSelectedItem();
        }

        /// <summary>
        /// Setting style of the WrapGrid. 
        /// <para/> Inner styles: "area", "vscrollbar", "hscrollbar".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);

            Style innerStyle = style.GetInnerStyle("vscrollbar");
            if (innerStyle != null && VScrollBar != null)
            {
                VScrollBar.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("hscrollbar");
            if (innerStyle != null && HScrollBar != null)
            {
                HScrollBar.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("area");
            if (innerStyle != null)
            {
                _area.SetStyle(innerStyle);
            }
        }
    }
}
