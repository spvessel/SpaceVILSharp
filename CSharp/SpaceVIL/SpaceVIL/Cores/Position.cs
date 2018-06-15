using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    internal class Position : IPosition
    {
        private int _x = 0;
        private int _y = 0;
        public void SetX(int x)
        {
            _x = x;
        }
        public int GetX()
        {
            return _x;
        }
        public void SetY(int y)
        {
            _y = y;
        }
        public int GetY()
        {
            return _y;
        }
    }
}
