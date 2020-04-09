using SpaceVIL.Common;

namespace SpaceVIL
{
    /// <summary>
    /// Frame is the basic container. 
    /// It groups items based on items alignment, margins, paddings, sizes and size policies.
    /// <para/> Frame cannot receive any events, so Frame is always in the SpaceVIL.Core.ItemStateType.Base state.
    /// </summary>
    public class Frame : Prototype
    {
        static int count = 0;

        /// <summary>
        /// Default Frame constructor. Frame cannot get focus.
        /// </summary>
        public Frame()
        {
            SetItemName("Frame_" + count);
            count++;

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.Frame)));
            IsFocusable = false;
        }

        protected internal override bool GetHoverVerification(float xpos, float ypos)
        {
            return false;
        }
    }
}
