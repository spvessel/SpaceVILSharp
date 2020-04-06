namespace SpaceVIL.Core
{
    /// <summary>
    /// Area class represents a rectangular region with a specified position.
    /// </summary>
    public class Area
    {
        private int _x, _y, _w, _h;
        /// <summary>
        /// Default constructor. All values are zero.
        /// </summary>
        public Area()
        {
            _x = _y = _w = _h = 0;
        }
        /// <summary>
        /// Constructs a Area with specified position and size.
        /// </summary>
        /// <param name="x">X position.</param>
        /// <param name="y">Y position.</param>
        /// <param name="w">Area width.</param>
        /// <param name="h">Area height.</param>
        public Area(int x, int y, int w, int h)
        {
            _x = x;
            _y = y;
            _w = w;
            _h = h;
        }
        /// <summary>
        /// Setting X position.
        /// </summary>
        /// <param name="value">X position.</param>
        public void SetX(int value)
        {
            _x = value;
        }
        /// <summary>
        /// Setting Y position.
        /// </summary>
        /// <param name="value">Y position.</param>
        public void SetY(int value)
        {
            _y = value;
        }
        /// <summary>
        /// Setting area width.
        /// </summary>
        /// <param name="value">An area width.</param>
        public void SetWidth(int value)
        {
            _w = value;
        }
        /// <summary>
        /// Setting area height.
        /// </summary>
        /// <param name="value">An area height.</param>
        public void SetHeight(int value)
        {
            _h = value;
        }
        /// <summary>
        /// Getting X position.
        /// </summary>
        /// <returns>Current X position value.</returns>
        public int GetX()
        {
            return _x;
        }
        /// <summary>
        /// Getting Y position.
        /// </summary>
        /// <returns>Current Y position value.</returns>
        public int GetY()
        {
            return _y;
        }
        /// <summary>
        /// Getting area width.
        /// </summary>
        /// <returns>Current area width.</returns>
        public int GetWidth()
        {
            return _w;
        }
        /// <summary>
        /// Getting area height.
        /// </summary>
        /// <returns>Current area height.</returns>
        public int GetHeight()
        {
            return _h;
        }
        /// <summary>
        /// Setting all area attributes.
        /// </summary>
        /// <param name="x">X position.</param>
        /// <param name="y">Y position.</param>
        /// <param name="w">Area width.</param>
        /// <param name="h">Area height.</param>
        public void SetAttr(int x, int y, int w, int h)
        {
            _x = x;
            _y = y;
            _w = w;
            _h = h;
        }

        public override string ToString()
        {
            return _x + " " + _y + " " + _w + " " + _h;
        }
    }
}