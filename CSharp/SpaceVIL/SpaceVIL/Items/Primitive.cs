using System.Collections.Generic;
using System.Drawing;

namespace SpaceVIL
{
    abstract public class Primitive : BaseItem
    {
        public Primitive(
            int xpos = 0,
            int ypos = 0,
            int width = 0,
            int height = 0,
            string name = "Primitive_")
        {
            SetItemName(name);
        }

        public override List<float[]> MakeShape()
        {
            return GetTriangles();
        }
    }
}
