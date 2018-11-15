using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class HorizontalScrollBar : HorizontalStack
    {
        private static int count = 0;

        public ButtonCore UpArrow = new ButtonCore();
        public ButtonCore DownArrow = new ButtonCore();
        public HorizontalSlider Slider = new HorizontalSlider();

        public HorizontalScrollBar()
        {
            SetItemName("HorizontalScrollBar_" + count);
            count++;

            Slider.Handler.Orientation = Orientation.Horizontal;

            UpArrow.EventMouseClick += (sender, args) =>
            {
                float value = Slider.GetCurrentValue();
                value -= Slider.GetStep();
                if (value < Slider.GetMinValue())
                    value = Slider.GetMinValue();
                Slider.SetCurrentValue(value);
            };

            DownArrow.EventMouseClick += (sender, args) =>
            {
                float value = Slider.GetCurrentValue();
                value += Slider.GetStep();
                if (value > Slider.GetMaxValue())
                    value = Slider.GetMaxValue();
                Slider.SetCurrentValue(value);
            };

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.HorizontalScrollBar)));
        }

        public override void InitElements()
        {
            UpArrow.IsFocusable = false;
            DownArrow.IsFocusable = false;
            Slider.IsFocusable = false;
            Slider.Handler.IsFocusable = false;
            //Adding
            AddItems(UpArrow, Slider, DownArrow);

            //connections
            EventScrollUp += UpArrow.EventMouseClick.Invoke;
            EventScrollDown += DownArrow.EventMouseClick.Invoke;
        }

        public void SetArrowsVisible(bool value)
        {
            UpArrow.SetVisible(value);
            DownArrow.SetVisible(value);
        }

        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);

            Style inner_style = style.GetInnerStyle("uparrow");
            if (inner_style != null)
            {
                UpArrow.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("downarrow");
            if (inner_style != null)
            {
                DownArrow.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("slider");
            if (inner_style != null)
            {
                Slider.SetStyle(inner_style);
            }
        }
    }
}
