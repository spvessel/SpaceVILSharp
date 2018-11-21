using System.Drawing;
using SpaceVIL.Core;

namespace SpaceVIL
{
    public class WindowAnchor : Prototype, IWindowAnchor
    {
        static int count = 0;
        public WindowAnchor()
        {
            SetItemName("WindowAnchor_" + count);
            count++;
            IsFocusable = false;
        }
    }
}
