namespace SpaceVIL.Decorations
{
    /// <summary>
    /// A class that store radius values for each corner of the rectangle object
    /// </summary>
    public class CornerRadius
    {
        public float LeftTop;
        public float RightTop;
        public float LeftBottom;
        public float RightBottom;

        /// <summary>
        /// Constructs a CornerRadius with the same radius values for each corner of the rectangle object
        /// </summary>
        public CornerRadius(CornerRadius radius)
        {
            LeftTop = radius.LeftTop;
            RightTop = radius.RightTop;
            LeftBottom = radius.LeftBottom;
            RightBottom = radius.RightBottom;
        }

        /// <summary>
        /// Constructs a CornerRadius with the radius values from other CornerRadius object
        /// (default radius = 0)
        /// </summary>
        public CornerRadius(float radius = 0)
        {
            LeftTop = radius;
            RightTop = radius;
            LeftBottom = radius;
            RightBottom = radius;
        }

        /// <summary>
        /// Constructs a CornerRadius with the radius values for each corner of the rectangle object
        /// </summary>
        public CornerRadius(float lt, float rt, float lb, float rb)
        {
            LeftTop = lt;
            RightTop = rt;
            LeftBottom = lb;
            RightBottom = rb;
        }
    }
}