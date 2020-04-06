using System;
using System.Drawing;
using System.Collections.Generic;
using System.Linq;

namespace SpaceVIL
{
    /// <summary>
    /// CustomShape is a subclass that extends from SpaceVIL.Primitive and can be any type of shapes.
    /// <para/> You must provide the correct 2D vertices (triangles) of your figure to wark with this class.
    /// </summary>
    public class CustomShape : Primitive
    {
        static int count = 0;
        /// <summary>
        /// Default CustomShape constructor.
        /// </summary>
        public CustomShape() : base("CustomShape_" + count)
        {
            count++;
        }

        /// <summary>
        /// Constructs a CustomShape with the specified shape.
        /// </summary>
        /// <param name="shape">Shape as list of tringles 
        /// (points list of the shape as List of float[2] array).</param>
        public CustomShape(List<float[]> shape) : this()
        {
            SetTriangles(shape);
        }

        /// <summary>
        /// Overridden method for stretching the shape of the current item 
        /// relative to the current size. 
        /// Use in conjunction with GetTriangles() and SetTriangles() methods.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void MakeShape()
        {
            if (GetTriangles() == null || GetTriangles().Count == 0)
                return;
            SetTriangles(UpdateShape());
        }
    }
}
