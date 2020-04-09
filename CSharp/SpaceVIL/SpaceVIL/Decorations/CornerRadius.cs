namespace SpaceVIL.Decorations
{
    /// <summary>
    /// A class that store radius values for each corner of the rectangle shape.
    /// </summary>
    public class CornerRadius
    {
        /// <summary>
        /// Left-top corner radius
        /// </summary>
        public float LeftTop;

        /// <summary>
        /// Right-top corner radius
        /// </summary>
        public float RightTop;

        /// <summary>
        /// Left-bottom corner radius
        /// </summary>
        public float LeftBottom;

        /// <summary>
        /// Right-bottom corner radius
        /// </summary>
        public float RightBottom;

        /// <summary>
        /// Checking if all corner radiuses is 0.
        /// </summary>
        /// <returns>True: if all corner radiuses is 0. False: if one of the corner radiuses is not 0.</returns>
        public bool IsCornersZero()
        {
            if (LeftTop != 0) return false;
            if (RightTop != 0) return false;
            if (RightBottom != 0) return false;
            if (LeftBottom != 0) return false;
            return true;
        }
        /// <summary>
        /// Constructs a CornerRadius with the radius values from other CornerRadius object.
        /// </summary>
        /// <param name="radius">Radius values as SpaceVIL.Decorations.CornerRadius.</param>
        public CornerRadius(CornerRadius radius)
        {
            LeftTop = radius.LeftTop;
            RightTop = radius.RightTop;
            LeftBottom = radius.LeftBottom;
            RightBottom = radius.RightBottom;
        }

        /// <summary>
        /// Constructs a CornerRadius with the same radius values for each corner of the rectangle object. 
        /// (default radius = 0).
        /// </summary>
        /// <param name="radius">Radius of the item's corners.</param>
        public CornerRadius(float radius = 0)
        {
            LeftTop = radius;
            RightTop = radius;
            LeftBottom = radius;
            RightBottom = radius;
        }

        /// <summary>
        /// Constructs a CornerRadius with the radius values for each corner of the rectangle object.
        /// </summary>
        /// <param name="lt">Left-top corner radius.</param>
        /// <param name="rt">Right-top corner radius.</param>
        /// <param name="lb">Left-bottom corner radius.</param>
        /// <param name="rb">Right-bottom corner radius.</param>
        public CornerRadius(float lt, float rt, float lb, float rb)
        {
            LeftTop = lt;
            RightTop = rt;
            LeftBottom = lb;
            RightBottom = rb;
        }

        public override bool Equals(object obj)
        {
            if (obj == this)
                return true;
            CornerRadius raduis = (CornerRadius)obj;
            if (obj == null || raduis == null)
                return false;

            if (raduis.LeftBottom == this.LeftBottom
                && raduis.RightBottom == this.RightBottom
                && raduis.LeftTop == this.LeftTop
                && raduis.RightTop == this.RightTop)
                return true;
            else
                return false;
        }

        public override int GetHashCode()
        {
            return base.GetHashCode();
        }
    }
}