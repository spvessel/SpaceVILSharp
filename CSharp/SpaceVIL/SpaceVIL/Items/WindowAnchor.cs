using System.Drawing;

namespace SpaceVIL
{
    public class WindowAnchor : VisualItem, IWindowAnchor
    {
        static int count = 0;
        public WindowAnchor()
        {
            SetItemName("WindowAnchor_" + count);
            count++;
            
            //events
            // EventMouseClick += EmptyEvent;
            EventMouseHover += (sender, args) => IsMouseHover = !IsMouseHover;
        }
    }
}
