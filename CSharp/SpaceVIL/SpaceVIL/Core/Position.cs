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
    }
}
