using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// Indicator is the basic implementation of a user interface indicator which 
    /// can be in enabled state or can be disabled state. 
    /// Used in SpaceVIL.CheckBox and SpaceVIL.RadioButton.
    /// <para/> Supports all events except drag and drop.
    /// </summary>
    public class Indicator : Prototype
    {
        internal class CustomToggle : ButtonToggle
        {
            protected internal override bool GetHoverVerification(float xpos, float ypos)
            {
                return false;
            }
        }

        private static int count = 0;

        private CustomToggle _marker;

        internal ButtonToggle GetIndicatorMarker()
        {
            return _marker;
        }

        /// <summary>
        /// Default Indicator constructor.
        /// </summary>
        public Indicator()
        {
            SetItemName("Indicator_" + count);
            count++;

            _marker = new CustomToggle();

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.Indicator)));
        }

        /// <summary>
        /// Initializing all elements in the Indicator. 
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            //marker
            _marker.SetItemName(GetItemName() + "_marker");
            AddItem(_marker);
        }

        /// <summary>
        /// Setting style of the Indicator.
        /// <para/> Inner styles: "marker".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
            Style marker_style = style.GetInnerStyle("marker");
            if(marker_style != null)
            {
                _marker.SetStyle(marker_style);
            }
        }
    }
}
