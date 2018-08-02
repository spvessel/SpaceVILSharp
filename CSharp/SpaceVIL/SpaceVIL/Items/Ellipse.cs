using System;
using System.Collections.Generic;

namespace SpaceVIL
{
    public class Ellipse : Primitive
    {
        static int count = 0;
        public Ellipse()
            : base(name: "Ellipse_" + count)
        {
            count++;
        }

        public override List<float[]> MakeShape()
        {
            SetTriangles(GraphicsMathService.GetEllipse(GetWidth(), GetHeight(), GetX(), GetY()));
            return GraphicsMathService.ToGL(this as BaseItem, GetHandler());
        }
    }
}
