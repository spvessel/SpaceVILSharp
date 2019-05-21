using System.Drawing;
using SpaceVIL.Common;

namespace SpaceVIL
{
    public class Frame : Prototype
    {
        static int count = 0;

        /// <summary>
        /// Constructs a Frame
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
