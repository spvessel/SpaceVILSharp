namespace SpaceVIL.Core
{
    /// <summary>
    /// Size is a class that describes the width and height of an object.
    /// </summary>
    public class Size
    {
        int _w = 0;
        int _h = 0;

        /// <summary>
        /// Default Size constructor with width and height equal to 0.
        /// </summary>
        public Size() { }

        /// <summary>
        /// Constructs Size with specified width and height.
        /// </summary>
        /// <param name="w">Width of the object.</param>
        /// <param name="h">Height of the object.</param>
        public Size(int w, int h)
        {
            _w = w;
            _h = h;
        }

        /// <summary>
        /// Getting the width of the object.
        /// </summary>
        /// <returns>Width of the object.</returns>
        public int GetWidth()
        {
            return _w;
        }

        /// <summary>
        /// Setting the width of the object.
        /// </summary>
        /// <param name="value">Width of the object.</param>
        public void SetWidth(int value)
        {
            _w = value;
        }

        /// <summary>
        /// Getting the height of the object.
        /// </summary>
        /// <returns>Height of the object.</returns>
        public int GetHeight()
        {
            return _h;
        }

        /// <summary>
        /// Setting the height of the object.
        /// </summary>
        /// <param name="value">Height of the object.</param>
        public void SetHeight(int value)
        {
            _h = value;
        }

        /// <summary>
        /// Setting the size of the object.
        /// </summary>
        /// <param name="w">Width of the object.</param>
        /// <param name="h">Height of the object.</param>
        public void SetSize(int w, int h)
        {
            SetWidth(w);
            SetHeight(h);
        }
    }
}