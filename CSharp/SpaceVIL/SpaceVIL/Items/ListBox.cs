using System;
using System.Linq;
using System.Drawing;

namespace SpaceVIL
{
    class ListBox : VisualItem, IScrollable
    {
        static int count = 0;

        private ListArea _area = new ListArea();
        public VerticalScrollBar VScrollBar = new VerticalScrollBar();
        public HorizontalScrollBar HScrollBar = new HorizontalScrollBar();
        private ScrollBarVisibility _v_scrollBarPolicy = ScrollBarVisibility.Always;
        public ScrollBarVisibility GetVScrollBarPolicy()
        {
            return _v_scrollBarPolicy;
        }
        public void SetVScrollBarPolicy(ScrollBarVisibility policy)
        {
            _v_scrollBarPolicy = policy;

            if (policy == ScrollBarVisibility.Never)
                VScrollBar.IsVisible = false;
            else
                VScrollBar.IsVisible = true;
        }
        private ScrollBarVisibility _h_scrollBarPolicy = ScrollBarVisibility.Always;
        public ScrollBarVisibility GetHScrollBarPolicy()
        {
            return _h_scrollBarPolicy;
        }
        public void SetHScrollBarPolicy(ScrollBarVisibility policy)
        {
            _h_scrollBarPolicy = policy;

            if (policy == ScrollBarVisibility.Never)
                HScrollBar.IsVisible = false;
            else
                HScrollBar.IsVisible = true;
        }

        public ListBox()
        {
            EventMouseClick += EmptyEvent;
            EventScrollUp += EmptyEvent;
            EventScrollDown += EmptyEvent;
            SetItemName("ListBox_" + count);
            count++;

            //Basic Attributes
            SetWidthPolicy(SizePolicy.Expand);
            SetHeightPolicy(SizePolicy.Expand);

            //VBar
            VScrollBar.SetAlignment(ItemAlignment.Right);
            VScrollBar.IsVisible = true;
            VScrollBar.SetItemName(GetItemName() + "_" + VScrollBar.GetItemName());
            VScrollBar.SetMargin(0, 0, 0, 10);

            //HBar
            HScrollBar.SetAlignment(ItemAlignment.Bottom);
            HScrollBar.IsVisible = true;
            HScrollBar.SetItemName(GetItemName() + "_" + HScrollBar.GetItemName());
            HScrollBar.SetMargin(0, 0, 10, 0);

            //Area
            _area.SetItemName(GetItemName() + "_" + _area.GetItemName());
            _area.SetBackground(Color.Transparent);
            _area.SetAlignment(ItemAlignment.Bottom);
            _area.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            _area.SetMargin(0, 0, 10, 10);//упростить доступ к элементам области
            _area.SetPadding(5, 5, 5, 5);
            _area.SetSpacing(0, 5);
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
            Int64 v_offset = ((Int64)((float)v_size / 100.0f * (float)v_value));
            _area.SetVScrollOffset(v_offset);
        }
        private void UpdateHListArea()
        {
            /*Console.Write(
                _area.GetHScrollOffset() + " "
            );*/

            //horizontal slider
            //h_value = 100.0f / h_size * h_offset);
            float h_value = HScrollBar.Slider.GetCurrentValue();
            int h_offset = (int)((float)(h_size * h_value) / 100.0f);
            _area.SetHScrollOffset(h_offset);

            /*Console.WriteLine(
                h_value + " " +
                _area.GetHScrollOffset()
            );*/
        }

        private void UpdateVerticalSlider()
        {
            if (_area.AreaPosition == ListPosition.No)
            {
                VScrollBar.Slider.Handler.SetHeight(VScrollBar.Slider.GetHeight());
                VScrollBar.Slider.SetStep(VScrollBar.Slider.GetMaxValue());
            }
            else
            {
                //size
                Int64 total_invisible_size = 0;
                foreach (var item in _area.GetItems())
                {
                    total_invisible_size += (item.GetHeight() + _area.GetSpacing().Vertical);
                }

                Int64 total = total_invisible_size - _area.GetSpacing().Vertical;
                total_invisible_size -= (_area.GetHeight() - _area.GetPadding().Top - _area.GetPadding().Bottom - _area.GetSpacing().Vertical);
                v_size = total_invisible_size;

                if (total_invisible_size > 0)
                {
                    float size_handler = (float)(_area.GetHeight() - _area.GetPadding().Top - _area.GetPadding().Bottom)
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
        }
        private void UpdateHorizontalSlider()
        {
            int max_size = 0;
            int visible_area = _area.GetWidth() - _area.GetPadding().Left - _area.GetPadding().Right;
            foreach (var item in _area.GetItems())
            {
                if (max_size < item.GetWidth() + item.GetMargin().Left + item.GetMargin().Right)
                    max_size = item.GetWidth() + item.GetMargin().Left + item.GetMargin().Right;
            }
            if (max_size <= visible_area)
            {
                HScrollBar.Slider.Handler.SetWidth(HScrollBar.Slider.GetWidth());
                HScrollBar.Slider.SetStep(HScrollBar.Slider.GetMaxValue());
                return;
            }
            Int64 total_invisible_size = max_size - visible_area;
            h_size = total_invisible_size;

            if (total_invisible_size > 0)
            {
                float size_handler = (float)(visible_area) / (float)max_size * 100.0f;
                size_handler = (float)HScrollBar.Slider.GetWidth() / 100.0f * size_handler;
                //size of handler
                HScrollBar.Slider.Handler.SetWidth((int)size_handler);
            }
            //step of slider
            float step_count = (float)total_invisible_size / (float)_area.GetStep();
            HScrollBar.Slider.SetStep((HScrollBar.Slider.GetMaxValue() - HScrollBar.Slider.GetMinValue()) / step_count);
            HScrollBar.Slider.SetCurrentValue((100.0f / (float)total_invisible_size) * (float)_area.GetHScrollOffset());
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
        public override void AddItem(BaseItem item)
        {
            _area.AddItem(item);
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
            base.AddItem(_area);
            base.AddItem(VScrollBar);
            base.AddItem(HScrollBar);

            //Events Connections
            EventScrollUp += VScrollBar.EventScrollUp.Invoke;
            EventScrollDown += VScrollBar.EventScrollDown.Invoke;
            VScrollBar.Slider.EventValueChanged += (sender) => { UpdateVListArea(); };
            HScrollBar.Slider.EventValueChanged += (sender) => { UpdateHListArea(); };
        }

        public EventMouseMethodState EventScrollUp;
        public EventMouseMethodState EventScrollDown;

        public void InvokeScrollUp()
        {
            if (_area.AreaPosition.HasFlag(ListPosition.Top))
                _area.SetVScrollOffset(_area.GetVScrollOffset() + _area.GetStep());

            if (EventScrollUp != null) EventScrollUp.Invoke(this);
        }

        public void InvokeScrollDown()
        {
            if (_area.AreaPosition.HasFlag(ListPosition.Bottom))
                _area.SetVScrollOffset(_area.GetVScrollOffset() - _area.GetStep());

            if (EventScrollDown != null) EventScrollDown.Invoke(this);
        }
    }
}
