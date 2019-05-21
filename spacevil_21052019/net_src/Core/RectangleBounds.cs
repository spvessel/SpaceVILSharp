using SpaceVIL.Core;

namespace SpaceVIL
{
    public class RectangleBounds : Geometry, IPosition
    {
        private int _x, _y;
        public void SetX(int x)
        {
            _x = x;
        }
        public void SetY(int y)
        {
            _y = y;
        }
        public int GetX()
        {
            return _x;
        }
        public int GetY()
        {
            return _y;
        }
    }
}