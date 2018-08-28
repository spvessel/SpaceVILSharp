using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    public class VerticalScrollBar : VerticalStack, IScrollable
    {
        private static int count = 0;

        public ButtonCore UpArrow = new ButtonCore();
        public ButtonCore DownArrow = new ButtonCore();
        public VerticalSlider Slider = new VerticalSlider();

        public VerticalScrollBar()
        {
            SetItemName("VerticalScrollBar_" + count);
            count++;

            SetBackground(Color.FromArgb(255, 50, 50, 50));
            SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
            SetWidth(16);

            //Slider
            Slider.SetWidthPolicy(SizePolicy.Expand);
            Slider.Handler.SetWidth(10);
            Slider.Handler.SetWidthPolicy(SizePolicy.Fixed);
            Slider.Handler.SetAlignment(ItemAlignment.Top | ItemAlignment.HCenter);
            Slider.Handler.SetBackground(Color.FromArgb(50, 255, 255, 255));
            Slider.Handler.Orientation = Orientation.Vertical;
            Slider.Track.SetBackground(Color.Transparent);

            //Arrows
            UpArrow.SetBackground(Color.FromArgb(50, 255, 255, 255));
            UpArrow.SetHeight(16);
            UpArrow.SetWidth(16);
            UpArrow.SetWidthPolicy(SizePolicy.Fixed);
            UpArrow.SetHeightPolicy(SizePolicy.Fixed);
            UpArrow.SetAlignment(ItemAlignment.Top | ItemAlignment.HCenter);
            UpArrow.IsCustom = new CustomFigure(true, GraphicsMathService.GetTriangle(10, 8, 3, 4, 0));
            UpArrow.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            UpArrow.EventMouseClick += (sender, args) =>
            {
                float value = Slider.GetCurrentValue();
                value -= Slider.GetStep();
                if (value < Slider.GetMinValue())
                    value = Slider.GetMinValue();
                Slider.SetCurrentValue(value);
            };

            DownArrow.SetBackground(Color.FromArgb(50, 255, 255, 255));
            DownArrow.SetHeight(16);
            DownArrow.SetWidth(16);
            DownArrow.SetWidthPolicy(SizePolicy.Fixed);
            DownArrow.SetHeightPolicy(SizePolicy.Fixed);
            DownArrow.SetAlignment(ItemAlignment.Bottom | ItemAlignment.HCenter);
            DownArrow.IsCustom = new CustomFigure(true, GraphicsMathService.GetTriangle(10, 8, 3, 4, 180));
            DownArrow.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            DownArrow.EventMouseClick += (sender, args) =>
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

        public void InvokeScrollUp(MouseArgs args)
        {
            if (EventScrollUp != null) EventScrollUp.Invoke(this, args);
        }

        public void InvokeScrollDown(MouseArgs args)
        {
            if (EventScrollDown != null) EventScrollDown.Invoke(this, args);
        }
    }
}
