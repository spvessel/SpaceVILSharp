namespace SpaceVIL.Decorations
{
    /// <summary>
    /// A structure that store indents of the item.
    /// </summary>
    public struct Indents
    {
        /// <summary>
        /// Indent from left side of the item.
        /// </summary>
        public int Left;

        /// <summary>
        /// Indent from top side of the item.
        /// </summary>
        public int Top;

        /// <summary>
        /// Indent from right side of the item.
        /// </summary>
        public int Right;

        /// <summary>
        /// Indent from bottom side of the item.
        /// </summary>
        public int Bottom;
        
        /// </summary>
        /// Constructs a Indents with strict values for each side (default values is zeros).
        /// <summary>
        /// <param name="left">Indent from left side of the item.</param>
        /// <param name="top">Indent from top side of the item.</param>
        /// <param name="right">Indent from right side of the item.</param>
        /// <param name="bottom">Indent from bottom side of the item.</param>
        public Indents(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            Left = left;
            Top = top;
            Right = right;
            Bottom = bottom;
        }
    }
}
