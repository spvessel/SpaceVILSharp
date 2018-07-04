using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    class HorizontalScrollBar : HorizontalStack, IScrollable
    {
        private static int count = 0;

        public ButtonCore UpArrow = new ButtonCore();
        public ButtonCore DownArrow = new ButtonCore();
        public HorizontalSlider Slider = new HorizontalSlider();

        public HorizontalScrollBar()
        {
            SetItemName("HorizontalScrollBar_" + count);
            count++;
            EventScrollUp += EmptyEvent;
            EventScrollDown += EmptyEvent;

            SetBackground(Color.FromArgb(255, 50, 50, 50));
            SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            SetHeight(10);

            //Slider
            Slider.SetHeightPolicy(SizePolicy.Expand);
            Slider.Handler.SetMinWidth(30);
            Slider.Handler.SetBackground(Color.FromArgb(100, 255, 255, 255));
            Slider.Handler.Orientation = Orientation.Horizontal;
            Slider.Track.SetBackground(Color.Transparent);

            //Arrows
            UpArrow.SetBackground(Color.FromArgb(50, 255, 255, 255));
            UpArrow.SetWidth(10);
            UpArrow.SetHeightPolicy(SizePolicy.Expand);
            UpArrow.SetWidthPolicy(SizePolicy.Fixed);
            UpArrow.SetAlignment(ItemAlignment.Left);
            UpArrow.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            UpArrow.EventMouseClick += (sender) =>
            {
                float value = Slider.GetCurrentValue();
                value -= Slider.GetStep();
                if (value < Slider.GetMinValue())
                    value = Slider.GetMinValue();
                Slider.SetCurrentValue(value);
            };

            DownArrow.SetBackground(Color.FromArgb(50, 255, 255, 255));
            DownArrow.SetWidth(10);
            DownArrow.SetHeightPolicy(SizePolicy.Expand);
            DownArrow.SetWidthPolicy(SizePolicy.Fixed);
            DownArrow.SetAlignment(ItemAlignment.Right);
            DownArrow.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            DownArrow.EventMouseClick += (sender) =>
            {
                float value = Slider.GetCurrentValue();
                value += Slider.GetStep();
                if (value > Slider.GetMaxValue())
                    value = Slider.GetMaxValue();
                Slider.SetCurrentValue(value);
            };
        }

        public override void InitElements()
        {
            //Adding
            AddItems(UpArrow, Slider, DownArrow);

            //connections
            EventScrollUp += UpArrow.EventMouseClick.Invoke;
            EventScrollDown += DownArrow.EventMouseClick.Invoke;
        }

        public void SetArrowsVisible(bool value)
        {
            UpArrow.IsVisible = value;
            DownArrow.IsVisible = value;
        }

        public EventMouseMethodState EventScrollUp;
        public EventMouseMethodState EventScrollDown;

        public void InvokeScrollUp()
        {
            if (EventScrollUp != null) EventScrollUp.Invoke(this);
        }

        public void InvokeScrollDown()
        {
            if (EventScrollDown != null) EventScrollDown.Invoke(this);
        }
    }
}
