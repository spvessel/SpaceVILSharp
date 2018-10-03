using System;
using System.Drawing;
using System.Collections.Generic;
using System.Linq;

namespace SpaceVIL
{
    public class CustomShape : Primitive
    {
        static int count = 0;
        public CustomShape()
            : base(name: "CustomShape_" + count)
        {
            count++;
        }
        public CustomShape(List<float[]> shape) : this()
        {
            SetTriangles(shape);
        }

        public override List<float[]> MakeShape()
        {
            if (GetTriangles() == null || GetTriangles().Count == 0)
                return null;

            return GraphicsMathService.ToGL(UpdateShape(), GetHandler());
        }
    }
}
