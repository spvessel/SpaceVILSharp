namespace SpaceVIL.Core
{
    public class Size
    {
        int _w = 0;
        int _h = 0;

        public Size(int w, int h)
        {
            _w = w;
            _h = h;
        }

        public int GetWidth()
        {
            return _w;
        }

        public void SetWidth(int value)
        {
            _w = value;
        }

        public int GetHeight()
        {
            return _h;
        }

        public void SetHeight(int value)
        {
            _h = value;
        }

        public void SetSize(int w, int h)
        {
            SetWidth(w);
            SetHeight(h);
        }
    }
}