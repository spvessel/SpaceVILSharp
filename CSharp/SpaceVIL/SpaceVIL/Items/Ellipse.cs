namespace SpaceVIL
{
    /// <summary>
    /// Ellipse is a subclass that extends from SpaceVIL.Primitive 
    /// for rendering an ellipse shape.
    /// </summary>
    public class Ellipse : Primitive
    {
        static int count = 0;
        /// <summary>
        /// Property to specify number of edges in an ellipse shape.
        /// <para/> Default: 16.
        /// </summary>
        public int Quality = 16;

        /// <summary>
        /// Default Ellipse constructor.
        /// </summary>
        public Ellipse() : base("Ellipse_" + count)
        {
            count++;
        }

        /// <summary>
        /// Constructs an Ellipse with specified number of edges in an ellipse shape.
        /// </summary>
        /// <param name="quality">Number of edges.</param>
        public Ellipse(int quality) : this()
        {
            this.Quality = quality;
        }

        /// <summary>
        /// Overridden method for stretching the ellipse shape relative to the current size. 
        /// Use in conjunction with GetTriangles() and SetTriangles() methods.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void MakeShape()
        {
            SetTriangles(GraphicsMathService.GetEllipse(GetWidth(), GetHeight(), 0, 0, Quality));
        }
    }
}
