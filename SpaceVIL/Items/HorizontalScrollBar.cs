using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// HorizontalScrollBar is the basic implementation of a user interface scroll bar 
    /// (horizontal version). 
    /// <para/> Contains arrow buttons, slider.
    /// <para/> By default ability to get focus is disabled.
    /// <para/> Supports all events except drag and drop.
    /// </summary>
    public class HorizontalScrollBar : HorizontalStack
    {
        private static int count = 0;
        /// <summary>
        /// Button to scroll up.
        /// </summary>
        public ButtonCore UpArrow = new ButtonCore();
        /// <summary>
        /// Button to scroll down.
        /// </summary>
        public ButtonCore DownArrow = new ButtonCore();
        /// <summary>
        /// Slider for scrolling with mouse drag and drop ivents or mouse wheel.
        /// </summary>
        public HorizontalSlider Slider = new HorizontalSlider();

        /// <summary>
        /// Default HorizontalScrollBar constructor.
        /// </summary>
        public HorizontalScrollBar()
        {
            IsFocusable = false;
            SetItemName("HorizontalScrollBar_" + count);
            count++;

            Slider.Handler.Orientation = Orientation.Horizontal;

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.HorizontalScrollBar)));
        }

        /// <summary>
        /// Initializing all elements in the HorizontalScrollBar.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            UpArrow.IsFocusable = false;
            DownArrow.IsFocusable = false;
            Slider.IsFocusable = false;
            Slider.Handler.IsFocusable = false;
            //Adding
            AddItems(UpArrow, Slider, DownArrow);

            EventMouseMethodState upScroll = (sender, args) =>
            {
                float value = Slider.GetCurrentValue();
                value -= Slider.GetStep() * (float)args.ScrollValue.DX;
                if (value < Slider.GetMinValue())
                    value = Slider.GetMinValue();
                Slider.SetCurrentValue(value);
            };

            UpArrow.EventMouseClick += upScroll;
            EventScrollUp += upScroll;

            EventMouseMethodState downScroll = (sender, args) =>
            {
                float value = Slider.GetCurrentValue();
                value += Slider.GetStep() * (float)args.ScrollValue.DX;
                if (value > Slider.GetMaxValue())
                    value = Slider.GetMaxValue();
                Slider.SetCurrentValue(value);
            };

            DownArrow.EventMouseClick += downScroll;
            EventScrollDown += downScroll;
        }

        /// <summary>
        /// Setting Up and Down arrow buttons visibility of the HorizontalScrollBar.
        /// </summary>
        /// <param name="value">True: if you want buttons visible. 
        /// False: if you want buttons invisible.</param>
        public void SetArrowsVisible(bool value)
        {
            UpArrow.SetVisible(value);
            DownArrow.SetVisible(value);
        }

        /// <summary>
        /// Seting style of the HorizontalScrollBar.
        /// <para/> Inner styles: "uparrow", "downarrow", "slider".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);

            Style innerStyle = style.GetInnerStyle("uparrow");
            if (innerStyle != null)
            {
                UpArrow.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("downarrow");
            if (innerStyle != null)
            {
                DownArrow.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("slider");
            if (innerStyle != null)
            {
                Slider.SetStyle(innerStyle);
            }
        }
    }
}
