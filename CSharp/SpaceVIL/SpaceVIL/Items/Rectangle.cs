using System.Collections.Generic;
using System.Drawing;
using System;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class Rectangle : Primitive
    {
        static int count = 0;
        private CornerRadius _border_radius = new CornerRadius();
        public void SetBorderRadius(int radius)
        {
            _border_radius.LeftTop = radius;
            _border_radius.RightTop = radius;
            _border_radius.LeftBottom = radius;
            _border_radius.RightBottom = radius;
        }

        public void SetBorderRadius(CornerRadius radius)
        {
            _border_radius = radius;
        }
        
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
            SetTriangles(GraphicsMathService.GetRoundSquare(_border_radius, GetWidth(), GetHeight(), GetX(), GetY()));
            return GraphicsMathService.ToGL(this, GetHandler());
        }
    }
}
