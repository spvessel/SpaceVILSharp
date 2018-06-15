using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    public class Pointer
    {
        private bool _is_set = false;
        public bool IsSet()
        {
            return _is_set;
        }
        public int X = -1;
        public int Y = -1;
        public void SetPosition(float x, float y)
        {
            X = (int)x;
            Y = (int)y;
            _is_set = true;
        }
        public void SetPosition(int x, int y)
        {
            X = x;
            Y = y;
            _is_set = true;
        }
        public void Clear()
        {
            X = -1;
            Y = -1;
            _is_set = false;
        }
    }
}
