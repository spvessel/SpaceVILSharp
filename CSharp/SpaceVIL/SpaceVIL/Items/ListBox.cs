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

        public Rectangle GetSelectionShape()
        {
            return _area.GetSubstrate();
        }
        public Rectangle GetHoverShape()
        {
            return _area.GetHoverSubstrate();
        }
        public int GetSelection()
        {
            return _area.GetSelection();
        }
        public void SetSelection(int index)
        {
            _area.SetSelection(index);
        }
        public void Unselect()
        {
            _area.Unselect();
        }
        public void SetSelectionVisibility(bool visibility)
        {
            _area.SetSelectionVisibility(visibility);
        }
        public bool GetSelectionVisibility()
        {
            return _area.GetSelectionVisibility();
        }
        public void SetHoverVisibility(bool visibility)
        {
            _area.SetHoverVisibility(visibility);
        }
        public bool GetHoverVisibility()
        {
            return _area.GetHoverVisibility();
        }

        public BlankItem Menu = new BlankItem();
        private bool _is_menu_disabled = false;

        public void DisableMenu(bool value)
        {
            _is_menu_disabled = value;
        }

        private ContextMenu _menu;

        private Grid _grid = new Grid(2, 2);
        private ListArea _area = new ListArea();
        public ListArea GetArea()
        {
            return _area;
        }
        public VerticalScrollBar VScrollBar = new VerticalScrollBar();
        public HorizontalScrollBar HScrollBar = new HorizontalScrollBar();
        private ScrollBarVisibility _v_scrollBarPolicy = ScrollBarVisibility.Always;
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
        private ScrollBarVisibility _h_scrollBarPolicy = ScrollBarVisibility.Always;
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

            //Area
            _area.SetItemName(GetItemName() + "_" + _area.GetItemName());
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
            int v_offset = (int)((float)(v_size * v_value) / 100.0f);
            _area.SetVScrollOffset(v_offset);
        }
        private void UpdateHListArea()
        {
            //horizontal slider
            float h_value = HScrollBar.Slider.GetCurrentValue();
            int h_offset = (int)((float)(h_size * h_value) / 100.0f);
            _area.SetHScrollOffset(h_offset);
        }

        private void UpdateVerticalSlider()//vertical slider
        {
            int total_invisible_size = 0;
            int visible_area = _area.GetHeight() - _area.GetPadding().Top - _area.GetPadding().Bottom;
            foreach (var item in _area.GetItems())
            {
                if (item.Equals(_area.GetSubstrate()) || item.Equals(_area.GetHoverSubstrate()) || !item.IsVisible())
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
            foreach (var item in _area.GetItems())
            {
                if (item.Equals(_area.GetSubstrate()) || item.Equals(_area.GetHoverSubstrate()) || !item.IsVisible())
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

        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            UpdateHorizontalSlider();
            HScrollBar.Slider.UpdateHandler();
        }
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            UpdateVerticalSlider();
            VScrollBar.Slider.UpdateHandler();
        }
        public override void AddItem(IBaseItem item)
        {
            _area.AddItem(item);
            UpdateElements();
        }
        public override void InsertItem(IBaseItem item, Int32 index)
        {
            _area.InsertItem(item, index);
            UpdateElements();
        }
        public override void RemoveItem(IBaseItem item)
        {
            _area.RemoveItem(item);
            UpdateElements();
        }
        public void UpdateElements()
        {
            UpdateVerticalSlider();
            VScrollBar.Slider.UpdateHandler();
            UpdateHorizontalSlider();
            HScrollBar.Slider.UpdateHandler();
        }

        public override void InitElements()
        {
            //Adding
            base.AddItem(_grid);
            _grid.InsertItem(_area, 0, 0);
            _grid.InsertItem(VScrollBar, 0, 1);
            _grid.InsertItem(HScrollBar, 1, 0);
            _grid.InsertItem(Menu, 1, 1);

            //Events Connections
            EventScrollUp += VScrollBar.EventScrollUp.Invoke;
            EventScrollDown += VScrollBar.EventScrollDown.Invoke;
            _area.ItemListChanged += () => { UpdateElements(); };

            VScrollBar.Slider.EventValueChanged += (sender) => { UpdateVListArea(); };
            HScrollBar.Slider.EventValueChanged += (sender) => { UpdateHListArea(); };

            // create menu
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

        public List<IBaseItem> GetListContent()
        {
            List<IBaseItem> result = new List<IBaseItem>();
            foreach (var item in _area.GetItems())
            {
                if (item.Equals(_area.GetSubstrate()) || item.Equals(_area.GetHoverSubstrate()))
                    continue;
                result.Add(item);
            }
            return result;
        }
        public void SetListContent(List<IBaseItem> content)
        {
            content.Insert(0, _area.GetSubstrate());
            content.Insert(1, _area.GetHoverSubstrate());
            _area.SetContent(content);
        }
        public IBaseItem GetSelectionItem()
        {
            List<IBaseItem> result = new List<IBaseItem>();
            return _area.GetSelectionItem();
        }

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
        }
    }
}
