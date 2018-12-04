using System.Collections.Generic;
using System.Drawing;
using System;

namespace SpaceVIL
{
    public class Rectangle : Primitive
    {
        static int count = 0;
        /// <summary>
        /// Constructs a Rectangle
        /// </summary>
        public Rectangle()
            : base(name: "Rectangle_" + count)
        {
            count++;
        }

        /// <summary>
        /// Make rectangle with width, height, X, Y
        /// </summary>
        public override List<float[]> MakeShape()
        {
            SetTriangles(GraphicsMathService.GetRectangle(GetWidth(), GetHeight(), GetX(), GetY()));
            return GraphicsMathService.ToGL(this, GetHandler());
        }
    }
}
