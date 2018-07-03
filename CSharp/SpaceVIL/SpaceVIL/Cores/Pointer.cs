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
        public int PrevX = -1;
        public int PrevY = -1;
        public void SetPosition(float x, float y)
        {
            if (PrevX == -1 || PrevY == -1)
            {
                PrevX = (int)x;
                PrevY = (int)y;
            }
            else
            {
                PrevX = X;
                PrevY = Y;
            }
            X = (int)x;
            Y = (int)y;
            _is_set = true;
        }
        public void SetPosition(int x, int y)
        {
            if (PrevX == -1 || PrevY == -1)
            {
                PrevX = x;
                PrevY = y;
            }
            else
            {
                PrevX = X;
                PrevY = Y;
            }
            X = x;
            Y = y;
            _is_set = true;
        }
        public void Clear()
        {
            PrevX = -1;
            PrevY = -1;
            X = -1;
            Y = -1;
            _is_set = false;
        }
    }
}
