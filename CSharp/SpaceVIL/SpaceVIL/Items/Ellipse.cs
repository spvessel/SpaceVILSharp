using System;
using System.Collections.Generic;

namespace SpaceVIL
{
    public class Ellipse : Primitive
    {
        static int count = 0;
        public int Quality = 16;

        /// <summary>
        /// Constructs an Ellipse
        /// </summary>
        public Ellipse()
            : base(name: "Ellipse_" + count)
        {
            count++;
        }

        /// <summary>
        /// Constructs an Ellipse
        /// </summary>
        /// <param name="quality"> Ellipse quality (points count) </param>
        public Ellipse(int quality) : this() {
            this.Quality = quality;
        }

        /// <summary>
        /// Make shape with triangles and convert to GL coordinates
        /// </summary>
        public override void MakeShape()
        {
            SetTriangles(GraphicsMathService.GetEllipse(GetWidth(), GetHeight(), 0, 0, Quality));
        }
    }
}
