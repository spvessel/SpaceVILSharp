using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    public class HorizontalScrollBar : HorizontalStack, IScrollable
    {
        private static int count = 0;

        public ButtonCore UpArrow = new ButtonCore();
        public ButtonCore DownArrow = new ButtonCore();
        public HorizontalSlider Slider = new HorizontalSlider();

        public HorizontalScrollBar()
        {
            SetItemName("HorizontalScrollBar_" + count);
            count++;

            SetBackground(Color.FromArgb(255, 50, 50, 50));
            SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            SetHeight(16);

            //Slider
            Slider.SetHeightPolicy(SizePolicy.Expand);
            Slider.Handler.SetMinWidth(0);
            Slider.Handler.SetHeight(10);
            Slider.Handler.SetHeightPolicy(SizePolicy.Fixed);
            Slider.Handler.SetAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
            Slider.Handler.SetBackground(Color.FromArgb(50, 255, 255, 255));
            Slider.Handler.Orientation = Orientation.Horizontal;
            Slider.Track.SetBackground(Color.Transparent);

            //Arrows
            UpArrow.SetBackground(Color.FromArgb(50, 255, 255, 255));
            UpArrow.SetWidth(16);
            UpArrow.SetHeight(16);
            UpArrow.SetHeightPolicy(SizePolicy.Fixed);
            UpArrow.SetWidthPolicy(SizePolicy.Fixed);
            UpArrow.SetAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
            UpArrow.IsCustom = new CustomFigure(true, GraphicsMathService.GetTriangle(10, 8, 3, 4, -90));
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
            DownArrow.SetWidth(16);
            DownArrow.SetHeight(16);
            DownArrow.SetHeightPolicy(SizePolicy.Fixed);
            DownArrow.SetWidthPolicy(SizePolicy.Fixed);
            DownArrow.SetAlignment(ItemAlignment.Right | ItemAlignment.VCenter);
            DownArrow.IsCustom = new CustomFigure(true, GraphicsMathService.GetTriangle(10, 8, 3, 4, 90));
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
