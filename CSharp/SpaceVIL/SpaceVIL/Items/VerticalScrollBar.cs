using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// VerticalScrollBar is the basic implementation of a user interface scroll bar 
    /// (vertical version). 
    /// <para/> Contains arrow buttons, slider.
    /// <para/> By default ability to get focus is disabled.
    /// <para/> Supports all events except drag and drop.
    /// </summary>
    public class VerticalScrollBar : VerticalStack
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
        public VerticalSlider Slider = new VerticalSlider();

        /// <summary>
        /// Default VerticalScrollBar constructor.
        /// </summary>
        public VerticalScrollBar()
        {
            IsFocusable = false;
            SetItemName("VerticalScrollBar_" + count);
            count++;

            //Slider
            Slider.Handler.Orientation = Orientation.Vertical;

            //Arrows
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

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.VerticalScrollBar)));
        }

        /// <summary>
        /// Initializing all elements in the VerticalScrollBar.
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

            //connections
            EventScrollUp += UpArrow.EventMouseClick.Invoke;
            EventScrollDown += DownArrow.EventMouseClick.Invoke;
        }

        /// <summary>
        /// Setting Up and Down arrow buttons visibility of the VerticalScrollBar.
        /// </summary>
        /// <param name="value">True: if you want buttons visible. 
        /// False: if you want buttons invisible.</param>
        public void SetArrowsVisible(bool value)
        {
            UpArrow.SetVisible(value);
            DownArrow.SetVisible(value);
        }

        /// <summary>
        /// Seting style of the VerticalScrollBar.
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
