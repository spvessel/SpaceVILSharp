using System;
using System.Linq;
using System.Drawing;
using System.Collections.Generic;

namespace SpaceVIL
{
    public class TextArea : VisualItem
    {
        static int count = 0;
        private Grid _grid = new Grid(2, 2);
        private TextBlock _area = new TextBlock();
        public VisualItem GetTextBlock()
        {
            return _area;
        }
        public bool IsEditable
        {
            get
            {
                return _area.IsEditable;
            }
            set
            {
                _area.IsEditable = value;
            }
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
                VScrollBar.IsVisible = false;
            else
                VScrollBar.IsVisible = true;

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
                HScrollBar.IsVisible = false;
            else
                HScrollBar.IsVisible = true;

            _grid.UpdateLayout();
            UpdateVerticalSlider();
            VScrollBar.Slider.UpdateHandler();
        }

        public TextArea()
        {
            SetItemName("TextArea_" + count);
            count++;
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.TextArea)));

            //VBar
            VScrollBar.IsVisible = true;
            VScrollBar.SetItemName(GetItemName() + "_" + VScrollBar.GetItemName());

            //HBar
            HScrollBar.IsVisible = true;
            HScrollBar.SetItemName(GetItemName() + "_" + HScrollBar.GetItemName());

            //Area
            _area.SetItemName(GetItemName() + "_" + _area.GetItemName());
            _area.SetSpacing(0, 5);
        }

        private Int64 v_size = 0;
        private Int64 h_size = 0;
        private void UpdateVListArea()
        {
            //vertical slider
            float v_value = VScrollBar.Slider.GetCurrentValue();
            int v_offset = (int)((float)(v_size * v_value) / 100.0f);
            _area.SetScrollYOffset(-v_offset);
        }
        private void UpdateHListArea()
        {
            //horizontal slider
            float h_value = HScrollBar.Slider.GetCurrentValue();
            int h_offset = (int)((float)(h_size * h_value) / 100.0f);
            _area.SetScrollXOffset(-h_offset);
        }

        private void UpdateVerticalSlider()//vertical slider
        {
            //1. собрать всю высоту всех элементов
            //2. собрать видимую высоту
            //3. связать видимую и всю высоту с скроллом
            //4. выставить размеры и позицию в %

            int visible_area = _area.GetHeight() - _area.GetPadding().Top - _area.GetPadding().Bottom;
            int total = _area.GetTextHeight();

            int total_invisible_size = total - visible_area;
            if (total <= visible_area)
            {
                VScrollBar.Slider.Handler.SetHeight(0);
                VScrollBar.Slider.SetStep(VScrollBar.Slider.GetMaxValue());
                v_size = 0;
                VScrollBar.Slider.SetCurrentValue(0);
                return;
            }
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
            float step_count = (float)total_invisible_size / (float)_area.GetScrollYStep();
            VScrollBar.Slider.SetStep((VScrollBar.Slider.GetMaxValue() - VScrollBar.Slider.GetMinValue()) / step_count);
            VScrollBar.Slider.SetCurrentValue((100.0f / total_invisible_size) * Math.Abs(_area.GetScrollYOffset()));
        }
        private void UpdateHorizontalSlider()//horizontal slider
        {
            //1. найти самый широкий из всех элементов
            //2. определить видимую ширину
            //3. связать видимую и всю ширину с скроллом
            //4. выставить размеры и позицию в %

            int visible_area = _area.GetWidth() - _area.GetPadding().Left - _area.GetPadding().Right;
            int total = _area.GetTextWidth();

            int total_invisible_size = total - visible_area;
            if (total <= visible_area)
            {
                HScrollBar.Slider.Handler.SetWidth(0);
                HScrollBar.Slider.SetStep(HScrollBar.Slider.GetMaxValue());
                h_size = 0;
                HScrollBar.Slider.SetCurrentValue(0);
                return;
            }
            h_size = total_invisible_size;

            if (total_invisible_size > 0)
            {
                float size_handler = (float)(visible_area)
                    / (float)total * 100.0f;
                size_handler = (float)HScrollBar.Slider.GetWidth() / 100.0f * size_handler;
                //size of handler
                HScrollBar.Slider.Handler.SetWidth((int)size_handler);
            }
            //step of slider
            float step_count = (float)total_invisible_size / (float)_area.GetScrollXStep();
            HScrollBar.Slider.SetStep((HScrollBar.Slider.GetMaxValue() - HScrollBar.Slider.GetMinValue()) / step_count);
            HScrollBar.Slider.SetCurrentValue((100.0f / total_invisible_size) * Math.Abs(_area.GetScrollXOffset()));
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

            //Events Connections
            EventScrollUp += VScrollBar.EventScrollUp.Invoke;
            EventScrollDown += VScrollBar.EventScrollDown.Invoke;
            _area.TextChanged += UpdateElements;

            VScrollBar.Slider.EventValueChanged += (sender) => { UpdateVListArea(); };
            HScrollBar.Slider.EventValueChanged += (sender) => { UpdateHListArea(); };
            UpdateElements();
        }

        public void SetText(String text)
        {
            _area.SetText(text);
        }

        public String GetText()
        {
            return _area.GetText();
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
        }
    }
}