
namespace SpaceVIL
{
    /// <summary>
    /// Triangle is a subclass that extends from SpaceVIL.Primitive 
    /// for rendering an triangle shape.
    /// </summary>
    public class Triangle : Primitive
    {
        static int count = 0;

        /// <summary>
        /// Default Triangle constructor.
        /// </summary>
        public Triangle()
            : base(name: "Triangle_" + count)
        {
            count++;
        }

        /// <summary>
        /// Constructs an Triangle with specified rotation angle of an triangle shape.
        /// </summary>
        /// <param name="angle">Rotation angle of an triangle shape.</param>
        public Triangle(int angle) : this()
        {
            RotationAngle = angle;
        }

        /// <summary>
        /// Rotation angle in degrees of an triangle shape. 
        /// <para/> Default: 0.
        /// </summary>
        public int RotationAngle = 0;

        /// <summary>
        /// Overridden method for stretching the triangle shape relative to the current size. 
        /// Use in conjunction with GetTriangles() and SetTriangles() methods.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void MakeShape()
        {
            SetTriangles(GraphicsMathService.GetTriangle(GetWidth(), GetHeight(), 0, 0, RotationAngle));
        }
    }
}
