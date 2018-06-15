using System.Collections.Generic;
using System.Drawing;
using System.Linq;

namespace SpaceVIL
{
    class Triangle : Primitive
    {
        static int count = 0;
        public Triangle()
            : base(name: "Triangle" + count)
        {
            count++;
        }

        public int RotationAngle = 0;
        public override List<float[]> MakeShape()
        {
            SetTriangles(GraphicsMathService.GetTriangle(GetWidth(), GetHeight(), GetX(), GetY(), RotationAngle));
            return GraphicsMathService.ToGL(UpdateShape(), GetHandler());
        }
    }
}
