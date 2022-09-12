using System;
using System.Drawing;
using System.Collections.Generic;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using SpaceVIL.Common;

namespace SpaceVIL
{
    /// <summary>
    /// ListBox is a container for SpaceVIL.ListArea 
    /// (scrollable container for other elements with ability of selection)
    /// and scroll bars. ListBox controls scrolling, resizing and other actions of SpaceVIL.ListArea.
    /// <para/> Contains list area, scroll bars, menu button, navigation context menu.
    /// <para/> Supports all events except drag and drop.
    /// </summary>
    public class ListBox : Prototype
    {
        static int count = 0;

        /// <summary>
        /// Setting scroll movement step.
        /// </summary>
        /// <param name="value">Scroll step.</param>
        public void SetScrollStep(int value)
        {
            _area.SetStep(value);
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
        /// Enable or disable selection ability of ListArea.
        /// </summary>
        /// <param name="value">True: if you want selection ability of ListArea to be enabled. 
        /// False: if you want selection ability of ListArea to be disabled.</param>
        public void SetSelectionVisible(bool value)
        {
            _area.SetSelectionVisible(value);
        }
        /// <summary>
        /// Returns True if selection ability of ListArea is enabled otherwise returns False.
        /// </summary>
        /// <returns>True: selection ability of ListArea is enabled. 
        /// False: selection ability of ListArea is disabled.</returns>
        public bool IsSelectionVisible()
        {
            return _area.IsSelectionVisible();
        }
        /// <summary>
        /// Interactive item to show the navigation context menu.
        /// </summary>
        public BlankItem Menu = new BlankItem();
        private bool _isMenuDisabled = false;

        /// <summary>
        /// Setting the navigation context menu to disable or enable.
        /// </summary>
        /// <param name="value">True: if you want to disable navigation context menu. 
        /// False: if you want to enable navigation context menu. </param>
        public void SetMenuDisabled(bool value)
        {
            _isMenuDisabled = value;
        }

        private Grid _grid = new Grid(2, 2);
        private ListArea _area = new ListArea();

        /// <summary>
        /// Getting list area of ListBox.
        /// </summary>
        /// <returns>List area as SpaceVIL.ListArea.</returns>
        public ListArea GetArea()
        {
            return _area;
        }

        private ContextMenu _menu;
        /// <summary>
        /// Vertical scroll bar of ListBox.
        /// </summary>
        public VerticalScrollBar VScrollBar = new VerticalScrollBar();
        /// <summary>
        /// Horizontal scroll bar of ListBox.
        /// </summary>
        public HorizontalScrollBar HScrollBar = new HorizontalScrollBar();
        private VisibilityPolicy _vScrollBarPolicy = VisibilityPolicy.AsNeeded;
        private VisibilityPolicy _hScrollBarPolicy = VisibilityPolicy.AsNeeded;

        /// <summary>
        /// Getting vertical scroll bar visibility policy.
        /// </summary>
        /// <returns>Visibility policy as SpaceVIL.Core.VisibilityPolicy.</returns>
        public VisibilityPolicy GetVScrollBarPolicy()
        {
            return _vScrollBarPolicy;
        }
        /// <summary>
        /// Setting vertical scroll bar visibility policy.
        /// <para/> Default: SpaceVIL.Core.VisibilityPolicy.AsNeeded.
        /// </summary>
        /// <param name="policy">Visibility policy as SpaceVIL.Core.VisibilityPolicy.</param>
        public void SetVScrollBarPolicy(VisibilityPolicy policy)
        {
            _vScrollBarPolicy = policy;

            if (policy == VisibilityPolicy.Never)
            {
                VScrollBar.SetDrawable(false);
                Menu.SetVisible(false);
            }
            else if (policy == VisibilityPolicy.AsNeeded)
            {
                VScrollBar.SetDrawable(false);
                Menu.SetVisible(false);
            }
            else if (policy == VisibilityPolicy.Always)
            {
                VScrollBar.SetDrawable(true);
                if (!HScrollBar.IsDrawable())
                    Menu.SetVisible(false);
                else
                    Menu.SetVisible(true);
            }

            _grid.UpdateLayout();
            UpdateHorizontalSlider();
            HScrollBar.Slider.UpdateHandler();
        }

        /// <summary>
        /// Getting horizontal scroll bar visibility policy.
        /// </summary>
        /// <returns>Visibility policy as SpaceVIL.Core.VisibilityPolicy.</returns>
        public VisibilityPolicy GetHScrollBarPolicy()
        {
            return _hScrollBarPolicy;
        }
        /// <summary>
        /// Setting horizontal scroll bar visibility policy.
        /// <para/> Default: SpaceVIL.Core.VisibilityPolicy.AsNeeded.
        /// </summary>
        /// <param name="policy">Visibility policy as SpaceVIL.Core.VisibilityPolicy.</param>
        public void SetHScrollBarPolicy(VisibilityPolicy policy)
        {
            _hScrollBarPolicy = policy;

            if (policy == VisibilityPolicy.Never)
            {
                HScrollBar.SetDrawable(false);
                Menu.SetVisible(false);
            }
            else if (policy == VisibilityPolicy.AsNeeded)
            {
                HScrollBar.SetDrawable(false);
                Menu.SetVisible(false);
            }
            else if (policy == VisibilityPolicy.Always)
            {
                HScrollBar.SetDrawable(true);
                if (!VScrollBar.IsDrawable())
                    Menu.SetVisible(false);
                else
                    Menu.SetVisible(true);
            }

            _grid.UpdateLayout();
            UpdateVerticalSlider();
            VScrollBar.Slider.UpdateHandler();
        }

        /// <summary>
        /// Default ListBox constructor.
        /// </summary>
        public ListBox()
        {
            SetItemName("ListBox_" + count);
            count++;
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.ListBox)));

            //VBar
            VScrollBar.SetDrawable(true);
            VScrollBar.SetItemName(GetItemName() + "_" + VScrollBar.GetItemName());

            //HBar
            HScrollBar.SetDrawable(true);
            HScrollBar.SetItemName(GetItemName() + "_" + HScrollBar.GetItemName());

            //events
            EventMouseClick += (sender, args) =>
            {
                // зачем нужен этот if???   
                if (_area.GetSelection() >= 0)
                    _area.SetSelection(_area.GetSelection());
                _area.SetFocus();
            };
            EventKeyPress += OnKeyPress;
        }

        private bool IsOutsideArea(long y, int h, Indents margin)
        {
            int startY = _area.GetY() + GetPadding().Top;
            int endY = _area.GetY() + GetHeight() - GetPadding().Bottom;
            if (y + h < startY || y - margin.Top > endY)
                return true;
            return false;
        }

        private void OnKeyPress(IItem sender, KeyArgs args)
        {
            SelectionItem selection = _area.GetTrueSelection();
            if (selection == null)
                return;
            long offset = _area.GetVScrollOffset();
            int startY = _area.GetY() + GetPadding().Top;
            long selection_Y = selection.GetY() + selection.GetMargin().Top;

            switch (args.Key)
            {
                case KeyCode.Up:
                    if (IsOutsideArea(selection_Y, selection.GetHeight(), selection.GetMargin()))
                    {
                        _area.SetVScrollOffset(offset - (startY - selection_Y));
                        UpdateVerticalSlider();
                        break;
                    }
                    if (selection_Y < startY)
                    {
                        _area.SetVScrollOffset(offset - (startY - selection_Y));
                        UpdateVerticalSlider();
                    }
                    break;
                case KeyCode.Down:
                    if (IsOutsideArea(selection_Y, selection.GetHeight(), selection.GetMargin()))
                    {
                        _area.SetVScrollOffset(offset + selection.GetHeight() + (selection.GetY() - (_area.GetY() + _area.GetHeight())));
                        UpdateVerticalSlider();
                        break;
                    }
                    if (selection_Y + selection.GetHeight() + selection.GetMargin().Bottom > GetY() + GetHeight() - GetPadding().Bottom)
                    {
                        _area.SetVScrollOffset(offset + ((selection_Y + selection.GetHeight() + selection.GetMargin().Bottom
                                + _area.GetSpacing().Vertical) - (GetY() + GetHeight() - GetPadding().Bottom)));
                        UpdateVerticalSlider();
                    }
                    break;
                default:
                    break;
            }
        }

        // private void UpdateListAreaAttributes(object sender)
        // {
        //     UpdateVListArea();
        //     UpdateHListArea();
        // }

        private Int64 v_size = 0;
        private Int64 h_size = 0;

        private void UpdateVListArea()
        {
            //vertical slider
            float v_value = VScrollBar.Slider.GetCurrentValue();
            int v_offset = (int)Math.Round((float)(v_size * v_value) / 100.0f);
            _area.SetVScrollOffset(v_offset);
        }
        private void UpdateHListArea()
        {
            //horizontal slider
            float h_value = HScrollBar.Slider.GetCurrentValue();
            int h_offset = (int)Math.Round((float)(h_size * h_value) / 100.0f);
            _area.SetHScrollOffset(h_offset);
        }

        private void UpdateVerticalSlider()//vertical slider
        {
            int total_invisible_size = 0;
            int visible_area = _area.GetHeight() - _area.GetPadding().Top - _area.GetPadding().Bottom;
            if (visible_area < 0)
                visible_area = 0;

            List<IBaseItem> list = _area.GetItems();

            foreach (var item in list)
            {
                if (!item.IsVisible())
                    continue;
                total_invisible_size += (item.GetHeight() + _area.GetSpacing().Vertical);
            }
            int total = total_invisible_size - _area.GetSpacing().Vertical;
            if (total_invisible_size <= visible_area)
            {
                VScrollBar.Slider.Handler.SetDrawable(false);
                VScrollBar.Slider.SetStep(VScrollBar.Slider.GetMaxValue());
                v_size = 0;
                VScrollBar.Slider.SetCurrentValue(0);
                if (GetVScrollBarPolicy() == VisibilityPolicy.AsNeeded)
                {
                    VScrollBar.SetDrawable(false);
                    Menu.SetVisible(false);
                    _grid.UpdateLayout();
                }
                return;
            }
            if (GetVScrollBarPolicy() == VisibilityPolicy.AsNeeded)
            {
                VScrollBar.SetDrawable(true);
                if (!HScrollBar.IsDrawable())
                    Menu.SetVisible(false);
                else
                    Menu.SetVisible(true);
                _grid.UpdateLayout();
            }
            VScrollBar.Slider.Handler.SetDrawable(true);
            total_invisible_size -= visible_area;
            v_size = total_invisible_size;

            if (total_invisible_size > 0)
            {
                float size_handler = (float)(visible_area)
                    / (float)total * 100.0f;
                size_handler = (float)VScrollBar.Slider.GetHeight() / 100.0f * size_handler;
                //size of handler
                VScrollBar.Slider.Handler.SetHeight((int)size_handler);
            }
            //step of slider
            float step_count = total_invisible_size / _area.GetStep();
            VScrollBar.Slider.SetStep((VScrollBar.Slider.GetMaxValue() - VScrollBar.Slider.GetMinValue()) / step_count);
            VScrollBar.Slider.SetCurrentValue((100.0f / total_invisible_size) * _area.GetVScrollOffset());
        }
        private void UpdateHorizontalSlider()//horizontal slider
        {
            int max_size = 0;
            int visible_area = _area.GetWidth() - _area.GetPadding().Left - _area.GetPadding().Right;
            if (visible_area < 0)
                visible_area = 0;

            // List<IBaseItem> list = _area.GetItems();
            List<IBaseItem> list = GetListContent();

            foreach (var item in list)
            {
                if (!item.IsVisible())
                    continue;
                if (max_size < item.GetWidth() + item.GetMargin().Left + item.GetMargin().Right)
                    max_size = item.GetWidth() + item.GetMargin().Left + item.GetMargin().Right;
            }
            if (max_size <= visible_area)
            {
                HScrollBar.Slider.Handler.SetDrawable(false);
                HScrollBar.Slider.SetStep(HScrollBar.Slider.GetMaxValue());
                h_size = 0;
                HScrollBar.Slider.SetCurrentValue(0);
                if (GetHScrollBarPolicy() == VisibilityPolicy.AsNeeded)
                {
                    HScrollBar.SetDrawable(false);
                    Menu.SetVisible(false);
                    _grid.UpdateLayout();
                }
                return;
            }
            if (GetHScrollBarPolicy() == VisibilityPolicy.AsNeeded)
            {
                HScrollBar.SetDrawable(true);
                if (!VScrollBar.IsDrawable())
                    Menu.SetVisible(false);
                else
                    Menu.SetVisible(true);
                _grid.UpdateLayout();
            }
            HScrollBar.Slider.Handler.SetDrawable(true);
            int total_invisible_size = max_size - visible_area;
            h_size = total_invisible_size;

            if (total_invisible_size > 0)
            {
                float size_handler = (float)(visible_area) / (float)max_size * 100.0f;
                size_handler = (float)HScrollBar.Slider.GetWidth() / 100.0f * size_handler;
                //size of handler
                HScrollBar.Slider.Handler.SetWidth((int)size_handler);
            }
            //step of slider
            int step_count = (int)((float)total_invisible_size / (float)_area.GetStep());
            if (step_count == 0) HScrollBar.Slider.SetStep(HScrollBar.Slider.GetMaxValue());
            else
                HScrollBar.Slider.SetStep((HScrollBar.Slider.GetMaxValue() - HScrollBar.Slider.GetMinValue()) / step_count);

            float f = (100.0f / (float)total_invisible_size) * (float)_area.GetHScrollOffset();
            HScrollBar.Slider.SetCurrentValue(f);
        }


        /// <summary>
        /// Setting item width. If the value is greater/less than the maximum/minimum 
        /// value of the width, then the width becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="width"> Width of the item. </param>
        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            UpdateHorizontalSlider();
            HScrollBar.Slider.UpdateHandler();
        }

        /// <summary>
        /// Setting item height. If the value is greater/less than the maximum/minimum 
        /// value of the height, then the height becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="height"> Height of the item. </param>
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            UpdateVerticalSlider();
            VScrollBar.Slider.UpdateHandler();
        }

        /// <summary>
        /// Adding item to the list area of ListBox.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        public override void AddItem(IBaseItem item)
        {
            _area.AddItem(item);
            UpdateElements();
        }
        /// <summary>
        /// Insert item into the list area of ListBox by index.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        /// <param name="index">Index of insertion.</param>
        public override void InsertItem(IBaseItem item, Int32 index)
        {
            _area.InsertItem(item, index);
            UpdateElements();
        }
        /// <summary>
        /// Removing the specified item from the list area of ListBox.
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
            bool result = _area.RemoveItem(item);
            UpdateElements();
            _area.SetFocus();
            return result;
        }
        /// <summary>
        /// Removing all items from the list area of ListBox.
        /// </summary>
        public override void Clear()
        {
            _area.Clear();
        }

        /// <summary>
        /// Updating all ListBox inner items.
        /// </summary>
        public virtual void UpdateElements()
        {
            UpdateVerticalSlider();
            VScrollBar.Slider.UpdateHandler();
            UpdateHorizontalSlider();
            HScrollBar.Slider.UpdateHandler();
        }

        /// <summary>
        /// Initializing all elements in the ListBox. 
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            //Adding
            base.AddItem(_grid);

            _grid.InsertItem(_area, 0, 0);
            _grid.InsertItem(VScrollBar, 0, 1);
            _grid.InsertItem(HScrollBar, 1, 0);
            _grid.InsertItem(Menu, 1, 1);

            //Events Connections
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
            _area.ItemListChanged += () => { UpdateElements(); };

            VScrollBar.Slider.EventValueChanged += (sender) => { UpdateVListArea(); };
            HScrollBar.Slider.EventValueChanged += (sender) => { UpdateHListArea(); };

            // create menu
            if (!_isMenuDisabled)
            {
                _menu = new ContextMenu(GetHandler());
                _menu.SetBackground(60, 60, 60);
                _menu.SetPassEvents(false);

                Color menuItemForeground = Color.FromArgb(210, 210, 210);

                MenuItem go_up = new MenuItem("Go up");
                go_up.SetForeground(menuItemForeground);
                go_up.EventMouseClick += ((sender, args) =>
                {
                    VScrollBar.Slider.SetCurrentValue(VScrollBar.Slider.GetMinValue());
                });

                MenuItem go_down = new MenuItem("Go down");
                go_down.SetForeground(menuItemForeground);
                go_down.EventMouseClick += ((sender, args) =>
                {
                    VScrollBar.Slider.SetCurrentValue(VScrollBar.Slider.GetMaxValue());
                });

                MenuItem go_up_left = new MenuItem("Go up and left");
                go_up_left.SetForeground(menuItemForeground);
                go_up_left.EventMouseClick += ((sender, args) =>
                {
                    VScrollBar.Slider.SetCurrentValue(VScrollBar.Slider.GetMinValue());
                    HScrollBar.Slider.SetCurrentValue(HScrollBar.Slider.GetMinValue());
                });

                MenuItem go_down_right = new MenuItem("Go down and right");
                go_down_right.SetForeground(menuItemForeground);
                go_down_right.EventMouseClick += ((sender, args) =>
                {
                    VScrollBar.Slider.SetCurrentValue(VScrollBar.Slider.GetMaxValue());
                    HScrollBar.Slider.SetCurrentValue(HScrollBar.Slider.GetMaxValue());
                });
                _menu.AddItems(go_up_left, go_down_right, go_up, go_down);
                Menu.EventMouseClick += (sender, args) =>
                {
                    if (!_isMenuDisabled)
                        _menu.Show(sender, args);
                };
                _menu.ActiveButton = MouseButton.ButtonLeft;
                _menu.Effects().Add(new Shadow(10));
            }
        }

        /// <summary>
        /// Getting content of the list area of ListBox.
        /// </summary>
        /// <returns>Content of the list area as 
        /// List&lt;SpaceVIL.Core.IBaseItem&gt;</returns>
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
        /// Adding all elements in the list area of ListBox from the given list.
        /// </summary>
        /// <param name="content">List of items as 
        /// IEnumerable&lt;SpaceVIL.Core.IBaseItem&gt;</param>
        public virtual void SetListContent(IEnumerable<IBaseItem> content)
        {
            _area.SetListContent(content);
            UpdateElements();
        }
        /// <summary>
        /// Getting wrapper of item.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        /// <returns>Wrapper of given item as SpaceVIL.SelectionItem.</returns>
        public SelectionItem GetWrapper(IBaseItem item)
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
        /// Setting style of the ListBox. 
        /// <para/> Inner styles: "area", "vscrollbar", "hscrollbar", "menu".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        //style
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);

            Style innerStyle = style.GetInnerStyle("vscrollbar");
            if (innerStyle != null)
            {
                VScrollBar.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("hscrollbar");
            if (innerStyle != null)
            {
                HScrollBar.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("menu");
            if (innerStyle != null)
            {
                Menu.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("area");
            if (innerStyle != null)
            {
                _area.SetStyle(innerStyle);
            }
        }
    }
}
