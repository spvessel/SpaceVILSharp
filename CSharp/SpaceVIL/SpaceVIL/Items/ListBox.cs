using System;
using System.Linq;
using System.Drawing;

namespace SpaceVIL
{
    class ListBox : VisualItem, IScrollable
    {
        static int count = 0;

        public ListArea Area = new ListArea();
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
            EventScrollUp += EmptyEvent;
            EventScrollDown += EmptyEvent;
            SetItemName("ListBox" + count);
            count++;

            //Basic Attributes
            SetWidthPolicy(SizePolicy.Expand);
            SetHeightPolicy(SizePolicy.Expand);

            //VBar
            VScrollBar.SetAlignment(ItemAlignment.Right);
            VScrollBar.IsVisible = true;
            VScrollBar.Slider.SetStep(5);
            VScrollBar.SetItemName(GetItemName() + "_" + VScrollBar.GetItemName());
            VScrollBar.SetMargin(0, 0, 0, 10);

            //HBar
            HScrollBar.SetAlignment(ItemAlignment.Bottom);
            HScrollBar.IsVisible = true;
            HScrollBar.Slider.SetStep(5);
            HScrollBar.SetItemName(GetItemName() + "_" + HScrollBar.GetItemName());
            HScrollBar.SetMargin(0, 0, 10, 0);

            //Area
            Area.SetItemName(GetItemName() + "_" + Area.GetItemName());
            Area.SetBackground(Color.Transparent);
            Area.SetAlignment(ItemAlignment.Bottom);
            Area.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            Area.SetMargin(0, 0, 10, 10);
            Area.SetPadding(5, 5, 5, 5);
            Area.SetSpacing(0, 5);
        }

        private void UpdateListAreaAttributes(object sender)
        {
            float current_value = VScrollBar.Slider.GetCurrentValue();
            Int64 global_offset = ((Int64)((float)total_size / 100.0f * (float)current_value)) * (-1);
            Area.SetScrollOffset(global_offset);
        }

        private Int64 total_size = 0;
        public void UpdateSliderAttributes()
        {
            if (Area.AreaPosition == ListPosition.No)
            {
                VScrollBar.Slider.Handler.SetHeight(VScrollBar.Slider.GetHeight());
                VScrollBar.Slider.SetStep(VScrollBar.Slider.GetMaxValue());
            }
            else
            {
                //size
                Int64 total_invisible_size = 0;
                foreach (var item in Area.GetItems())
                {
                    total_invisible_size += (item.GetHeight() + Area.GetSpacing().Vertical);
                }

                Int64 total = total_invisible_size - Area.GetSpacing().Vertical;
                total_invisible_size -= (Area.GetHeight() - Area.GetPadding().Top - Area.GetPadding().Bottom - Area.GetSpacing().Vertical);
                total_size = total_invisible_size;

                if (total_invisible_size > 0)
                {
                    int size_slider = VScrollBar.Slider.GetHeight();
                    float size_handler = (float)(Area.GetHeight() - Area.GetPadding().Top - Area.GetPadding().Bottom)
                        / (float)total * 100.0f;
                    size_handler = VScrollBar.Slider.GetHeight() / 100.0f * size_handler;
                    //size of handler
                    VScrollBar.Slider.Handler.SetHeight((int)size_handler);
                }
                //step of slider
                float step_count = total_invisible_size / Area.GetStep();
                VScrollBar.Slider.SetStep((VScrollBar.Slider.GetMaxValue() - VScrollBar.Slider.GetMinValue()) / step_count);
            }
        }
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            UpdateSliderAttributes();
            VScrollBar.Slider.UpdateHandler();
        }

        public new void AddItem(BaseItem item)
        {
            Area.AddItem(item);
            UpdateSliderAttributes();
        }
        public override void InitElements()
        {
            //Adding
            AddItems(Area, VScrollBar, HScrollBar);

            //Events Connections
            EventScrollUp += VScrollBar.EventScrollUp.Invoke;
            EventScrollDown += VScrollBar.EventScrollDown.Invoke;
            VScrollBar.Slider.EventValueChanged += UpdateListAreaAttributes;
        }

        public EventMouseMethodState EventScrollUp;
        public EventMouseMethodState EventScrollDown;

        public void InvokeScrollUp()
        {
            if (Area.AreaPosition.HasFlag(ListPosition.Top))
                Area.SetScrollOffset(Area.GetScrollOffset() + Area.GetStep());
            if (EventScrollUp != null) EventScrollUp.Invoke(this);

            /*Console.WriteLine(VScrollBar.Slider.GetStep()
                + " " + VScrollBar.Slider.GetCurrentValue()
                + " " + VScrollBar.Slider.GetMinValue()
                + " " + VScrollBar.Slider.GetMaxValue());*/
        }

        public void InvokeScrollDown()
        {
            if (Area.AreaPosition.HasFlag(ListPosition.Bottom))
                Area.SetScrollOffset(Area.GetScrollOffset() - Area.GetStep());
            if (EventScrollDown != null) EventScrollDown.Invoke(this);

            /*Console.WriteLine(VScrollBar.Slider.GetStep()
                + " " + VScrollBar.Slider.GetCurrentValue()
                + " " + VScrollBar.Slider.GetMinValue()
                + " " + VScrollBar.Slider.GetMaxValue());*/
        }
    }
}
