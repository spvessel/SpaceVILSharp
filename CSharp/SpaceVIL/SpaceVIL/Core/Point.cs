namespace SpaceVIL.Core
{
    internal class Point
    {
        internal int X { get; set; }
        internal int Y { get; set; }

        internal Point() : this(0, 0) { }

        internal Point(int x, int y)
        {
            X = x;
            Y = y;
        }

        internal Point(Point point) : this(point.X, point.Y) { }
    }
}