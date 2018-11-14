using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Core
{
    public class Pointer
    {
        private bool _is_set = false;
        public bool IsSet()
        {
            return _is_set;
        }
        private int X = -1;

        public int GetX()
        {
            return X;
        }

        public void SetX(int x)
        {
            X = x;
        }

        private int Y = -1;

        public int GetY()
        {
            return Y;
        }

        public void SetY(int y)
        {
            Y = y;
        }
        private int PrevX = -1;
        private int PrevY = -1;
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
