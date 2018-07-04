using System.Drawing;

namespace SpaceVIL
{
    public class FlowArea : VisualItem
    {
        static int count = 0;
        public FlowArea()
        {
            SetItemName("FlowArea_" + count);
            count++;
        }

        public override void InvokePoolEvents()
        {

        }
    }
}
