namespace SpaceVIL.Core
{
    internal class Point
    {
        internal int X { get; set; }
        internal int Y { get; set; }

        internal Point() { }

        internal Point(int x, int y)
        {
            X = x;
            Y = y;
        }
    }
}