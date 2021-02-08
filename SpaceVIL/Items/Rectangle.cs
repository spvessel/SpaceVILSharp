using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// Rectangle is a subclass that extends from SpaceVIL.Primitive 
    /// for rendering a rectangle shape.
    /// </summary>
    public class Rectangle : Primitive
    {
        static int count = 0;
        private CornerRadius _borderКadius = new CornerRadius();

        /// <summary>
        /// Setting the same radius values for each corner of the rectangle object.
        /// </summary>
        /// <param name="radius">Radii of the corners as SpaceVIL.Decorations.CornerRadius.</param>
        public void SetBorderRadius(int radius)
        {
            _borderКadius.LeftTop = radius;
            _borderКadius.RightTop = radius;
            _borderКadius.LeftBottom = radius;
            _borderКadius.RightBottom = radius;
        }

        /// <summary>
        ///  Setting the radii of corners.
        /// </summary>
        /// <param name="radius">Radii of the corners as SpaceVIL.Decorations.CornerRadius.</param>
        public void SetBorderRadius(CornerRadius radius)
        {
            _borderКadius = radius;
        }

        /// <summary>
        /// Default Rectangle constructor. Radii of the corners are 0.
        /// </summary>
        public Rectangle()
            : base(name: "Rectangle_" + count)
        {
            count++;
        }

        /// <summary>
        /// Constructs an Rectangle with specified corner radii.
        /// </summary>
        /// <param name="radius">Radii of the corners as SpaceVIL.Decorations.CornerRadius.</param>
        public Rectangle(CornerRadius radius) : this()
        {
            SetBorderRadius(radius);
        }

        /// <summary>
        /// Overridden method for stretching the rectangle shape relative to the current size. 
        /// Use in conjunction with GetTriangles() and SetTriangles() methods.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void MakeShape()
        {
            SetTriangles(GraphicsMathService.GetRoundSquare(_borderКadius, GetWidth(), GetHeight(), 0, 0));
        }
    }
}
