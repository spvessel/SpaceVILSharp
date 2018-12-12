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

        /// <summary>
        /// Has pointer real position (true) or default (false)
        /// </summary>
        public bool IsSet()
        {
            return _is_set;
        }
        private int X = -1;

        /// <returns> X position of the pointer </returns>
        public int GetX()
        {
            return X;
        }

        /// <param name="x"> X position of the pointer </param>
        public void SetX(int x)
        {
            X = x;
        }

        private int Y = -1;

        /// <returns> Y position of the pointer </returns>
        public int GetY()
        {
            return Y;
        }

        /// <param name="y"> Y position of the pointer </param>
        public void SetY(int y)
        {
            Y = y;
        }
        private int PrevX = -1;
        private int PrevY = -1;

        /// <summary>
        /// Set pointer position
        /// </summary>
        /// <param name="x"> X position of the pointer </param>
        /// <param name="y"> Y position of the pointer </param>
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

        /// <summary>
        /// Set pointer position
        /// </summary>
        /// <param name="x"> X position of the pointer </param>
        /// <param name="y"> Y position of the pointer </param>
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

        /// <summary>
        /// Set all pointer positions default
        /// </summary>
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
