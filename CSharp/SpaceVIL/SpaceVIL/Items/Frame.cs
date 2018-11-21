using System.Drawing;
using SpaceVIL.Common;

namespace SpaceVIL
{
    public class Frame : Prototype
    {
        static int count = 0;
        public Frame()
        {
            SetItemName("Frame_" + count);
            count++;

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.Frame)));
            IsFocusable = false;
        }

        internal override bool GetHoverVerification(float xpos, float ypos)
        {
            return false;
        }
    }
}
