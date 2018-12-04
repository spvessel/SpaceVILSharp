using System;
using System.Drawing;
using System.Collections.Generic;
using System.Linq;

namespace SpaceVIL
{
    public class CustomShape : Primitive
    {
        static int count = 0;
        /// <summary>
        /// Constructs a CustomShape
        /// </summary>
        public CustomShape()
            : base(name: "CustomShape_" + count)
        {
            count++;
        }

        /// <summary>
        /// Constructs a CustomShape with trinagles list
        /// </summary>
        public CustomShape(List<float[]> shape) : this()
        {
            SetTriangles(shape);
        }

        /// <summary>
        /// Make shape with triangles and convert to GL coordinates
        /// </summary>
        public override List<float[]> MakeShape()
        {
            if (GetTriangles() == null || GetTriangles().Count == 0)
                return null;

            return GraphicsMathService.ToGL(UpdateShape(), GetHandler());
        }
    }
}
