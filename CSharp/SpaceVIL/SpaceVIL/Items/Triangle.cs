using System.Collections.Generic;
using System.Drawing;
using System.Linq;

namespace SpaceVIL
{
    public class Triangle : Primitive
    {
        static int count = 0;
        public Triangle()
            : base(name: "Triangle_" + count)
        {
            count++;
        }

        public Triangle(int angle) : this()
        {
            RotationAngle = angle;
        }

        public int RotationAngle = 0;
        public override void MakeShape()
        {
            SetTriangles(GraphicsMathService.GetTriangle(GetWidth(), GetHeight(), 0, 0, RotationAngle));
        }
    }
}
