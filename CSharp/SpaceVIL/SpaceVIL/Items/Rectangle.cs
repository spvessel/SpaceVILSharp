using System.Collections.Generic;
using System.Drawing;
using System;

namespace SpaceVIL
{
    public class Rectangle : Primitive
    {
        static int count = 0;
        public Rectangle()
            : base(name: "Rectangle_" + count)
        {
            count++;
        }

        public override List<float[]> MakeShape()
        {
            SetTriangles(GraphicsMathService.GetRectangle(GetWidth(), GetHeight(), GetX(), GetY()));
            return GraphicsMathService.ToGL(this as BaseItem, GetHandler());
        }
    }
}
