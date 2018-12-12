namespace SpaceVIL.Decorations
{
    /// <summary>
    /// A class that store indents of the object
    /// </summary>
    public struct Indents
    {
        /// <summary>
        /// Constructs a Indents with strict values for each side
        /// (default values is zeros)
        /// </summary>
        public Indents(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            Left = left;
            Top = top;
            Right = right;
            Bottom = bottom;
        }
        public int Left;
        public int Top;
        public int Right;
        public int Bottom;
    }
}
