using System.Collections.Generic;
using System.Drawing;

namespace SpaceVIL
{
    abstract class Primitive : BaseItem
    {
        public Primitive(
            int xpos = 0,
            int ypos = 0,
            int width = 0,
            int height = 0,
            string name = "Primitive")
        {
            SetItemName(name);
        }

        public override List<float[]> MakeShape()
        {
            return GetTriangles();
        }

        public override void InvokePoolEvents()
        {
            //do nothing
        }
    }
}
