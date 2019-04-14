using System;
using System.Linq;
using System.Drawing;
using System.Collections.Generic;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using SpaceVIL.Common;

namespace SpaceVIL
{
    public class ListBox : Prototype
    {
        static int count = 0;

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
        /// Set selected item of the ListBox by index
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
        public void SetSelectionVisible(bool value)
        {
            _area.SetSelectionVisible(value);
        }
        public bool IsSelectionVisible()
        {
            return _area.IsSelectionVisible();
        }

        public BlankItem Menu = new BlankItem();
        private bool _is_menu_disabled = false;

        /// <summary>
        /// Is ListBox menu disabled
        /// </summary>
        public void DisableMenu(bool value)
        {
            _is_menu_disabled = value;
        }

        private Grid _grid = new Grid(2, 2);
        private ListArea _area = new ListArea();

        /// <returns> ListArea </returns>
        public ListArea GetArea()
        {
            return _area;
        }

        private ContextMenu _menu;
        public VerticalScrollBar VScrollBar = new VerticalScrollBar();
        public HorizontalScrollBar HScrollBar = new HorizontalScrollBar();
        private ScrollBarVisibility _v_scrollBarPolicy = ScrollBarVisibility.AsNeeded;

        /// <summary>
        /// Is vertical scroll bar visible
        /// </summary>
        public ScrollBarVisibility GetVScrollBarVisible()
        {
            return _v_scrollBarPolicy;
        }
        public void SetVScrollBarVisible(ScrollBarVisibility policy)
        {
            _v_scrollBarPolicy = policy;

            if (policy == ScrollBarVisibility.Never)
            {
                VScrollBar.SetDrawable(false);
                Menu.SetVisible(false);
            }
            else if (policy == ScrollBarVisibility.AsNeeded)
            {
                VScrollBar.SetDrawable(false);
                Menu.SetVisible(false);
            }
            else if (policy == ScrollBarVisibility.Always)
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

        private ScrollBarVisibility _h_scrollBarPolicy = ScrollBarVisibility.AsNeeded;

        /// <summary>
        /// Is horizontal scroll bar visible
        /// </summary>
        public ScrollBarVisibility GetHScrollBarVisible()
        {
            return _h_scrollBarPolicy;
        }
        public void SetHScrollBarVisible(ScrollBarVisibility policy)
        {
            _h_scrollBarPolicy = policy;

            if (policy == ScrollBarVisibility.Never)
            {
                HScrollBar.SetDrawable(false);
                Menu.SetVisible(false);
            }
            else if (policy == ScrollBarVisibility.AsNeeded)
            {
                HScrollBar.SetDrawable(false);
                Menu.SetVisible(false);
            }
            else if (policy == ScrollBarVisibility.Always)
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
        /// Constructs a ListBox
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
                        // _area.SetVScrollOffset(offset - (startY - selection_Y));
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

        private void UpdateListAreaAttributes(object sender)
        {
            UpdateVListArea();
            UpdateHListArea();
        }

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
            foreach (var item in _area.GetItems())
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
                if (GetVScrollBarVisible() == ScrollBarVisibility.AsNeeded)
                {
                    VScrollBar.SetDrawable(false);
                    Menu.SetVisible(false);
                    _grid.UpdateLayout();
                }
                return;
            }
            if (GetVScrollBarVisible() == ScrollBarVisibility.AsNeeded)
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
            foreach (var item in _area.GetItems())
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
                if (GetHScrollBarVisible() == ScrollBarVisibility.AsNeeded)
                {
                    HScrollBar.SetDrawable(false);
                    Menu.SetVisible(false);
                    _grid.UpdateLayout();
                }
                return;
            }
            if (GetHScrollBarVisible() == ScrollBarVisibility.AsNeeded)
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
        /// Set width of the ListBox
        /// </summary>
        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            UpdateHorizontalSlider();
            HScrollBar.Slider.UpdateHandler();
        }

        /// <summary>
        /// Set height of the ListBox
        /// </summary>
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            UpdateVerticalSlider();
            VScrollBar.Slider.UpdateHandler();
        }

        /// <summary>
        /// Add item to the ListBox
        /// </summary>
        public override void AddItem(IBaseItem item)
        {
            _area.AddItem(item);
            UpdateElements();
        }

        /// <summary>
        /// Insert item to the ListBox by index
        /// </summary>
        public override void InsertItem(IBaseItem item, Int32 index)
        {
            _area.InsertItem(item, index);
            UpdateElements();
        }

        /// <summary>
        /// Remove item from the ListBox
        /// </summary>
        public override bool RemoveItem(IBaseItem item)
        {
            List<IBaseItem> list = new List<IBaseItem>(GetItems());
            if (list.Contains(item))
            {
                return base.RemoveItem(item);
            }
            bool b = _area.RemoveItem(item);
            UpdateElements();
            _area.SetFocus();
            return b;
        }
        public override void Clear()
        {
            _area.Clear();
            // UpdateElements();
        }

        /// <summary>
        /// Update states of the all ListBox inner items
        /// </summary>
        public void UpdateElements()
        {
            UpdateVerticalSlider();
            VScrollBar.Slider.UpdateHandler();
            UpdateHorizontalSlider();
            HScrollBar.Slider.UpdateHandler();
        }

        /// <summary>
        /// Initialization and adding of all elements in the ListBox
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
            if (!_is_menu_disabled)
            {
                _menu = new ContextMenu(GetHandler());
                _menu.SetBackground(60, 60, 60);
                _menu.SetPassEvents(false);

                MenuItem go_up = new MenuItem("Go up");
                go_up.SetForeground(Color.FromArgb(210, 210, 210));
                go_up.EventMouseClick += ((sender, args) =>
                {
                    VScrollBar.Slider.SetCurrentValue(VScrollBar.Slider.GetMinValue());
                });

                MenuItem go_down = new MenuItem("Go down");
                go_down.SetForeground(Color.FromArgb(210, 210, 210));
                go_down.EventMouseClick += ((sender, args) =>
                {
                    VScrollBar.Slider.SetCurrentValue(VScrollBar.Slider.GetMaxValue());
                });

                MenuItem go_up_left = new MenuItem("Go up and left");
                go_up_left.SetForeground(Color.FromArgb(210, 210, 210));
                go_up_left.EventMouseClick += ((sender, args) =>
                {
                    VScrollBar.Slider.SetCurrentValue(VScrollBar.Slider.GetMinValue());
                    HScrollBar.Slider.SetCurrentValue(HScrollBar.Slider.GetMinValue());
                });

                MenuItem go_down_right = new MenuItem("Go down and right");
                go_down_right.SetForeground(Color.FromArgb(210, 210, 210));
                go_down_right.EventMouseClick += ((sender, args) =>
                {
                    VScrollBar.Slider.SetCurrentValue(VScrollBar.Slider.GetMaxValue());
                    HScrollBar.Slider.SetCurrentValue(HScrollBar.Slider.GetMaxValue());
                });
                _menu.AddItems(go_up_left, go_down_right, go_up, go_down);
                Menu.EventMouseClick += (sender, args) =>
                {
                    if (!_is_menu_disabled)
                        _menu.Show(sender, args);
                };
                _menu.ActiveButton = MouseButton.ButtonLeft;
                _menu.SetShadow(10, 0, 0, Color.Black);
            }
        }

        /// <returns> list of all ListBox items </returns>
        public List<IBaseItem> GetListContent()
        {
            List<IBaseItem> result = new List<IBaseItem>();
            List<IBaseItem> list = new List<IBaseItem>(_area.GetItems());

            foreach (IBaseItem item in list)
            {
                SelectionItem tmp = item as SelectionItem;
                // if (tmp != null)
                result.Add(tmp.GetContent());
                // Console.WriteLine(item.GetItemName() + " " + GetParent().GetItemName() + " " + GetHandler().GetWindowName());
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

        public SelectionItem GetWrapper(IBaseItem item)
        {
            return GetArea()._mapContent[item];
        }

        /// <returns> selection item </returns>
        public IBaseItem GetSelectedItem()
        {
            return _area.GetSelectedItem();
        }

        /// <summary>
        /// Set style of the ListBox
        /// </summary>
        //style
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);

            Style inner_style = style.GetInnerStyle("vscrollbar");
            if (inner_style != null)
            {
                VScrollBar.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("hscrollbar");
            if (inner_style != null)
            {
                HScrollBar.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("menu");
            if (inner_style != null)
            {
                Menu.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("area");
            if (inner_style != null)
            {
                _area.SetStyle(inner_style);
            }
        }
    }
}
