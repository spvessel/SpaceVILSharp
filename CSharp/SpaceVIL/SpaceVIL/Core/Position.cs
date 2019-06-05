using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Core
{
    /// <summary>
    /// Class describes position of the item
    /// </summary>
    public class Position : IPosition
    {
        public Position()
        {

        }

        public Position(int x, int y)
        {
            _x = x;
            _y = y;
        }
        
        private int _x = 0;
        private int _y = 0;

        /// <param name="x"> X position of the item </param>
        public void SetX(int x)
        {
            _x = x;
        }

        /// <returns> X position of the item </returns>
        public int GetX()
        {
            return _x;
        }

        /// <param name="y"> Y position of the item </param>
        public void SetY(int y)
        {
            _y = y;
        }

        /// <returns> Y position of the item </returns>
        public int GetY()
        {
            return _y;
        }

        public void SetPosition(int x, int y)
        {
            SetX(x);
            SetY(y);
        }
    }
}
