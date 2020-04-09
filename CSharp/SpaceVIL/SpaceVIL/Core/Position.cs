using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Core
{
    /// <summary>
    /// Position is a class representing the location of a 2D coordinate integer point.
    /// </summary>
    public class Position : IPosition
    {
        /// <summary>
        /// Default Position constructor.
        /// </summary>
        public Position() { }

        /// <summary>
        /// Position constructor with specified X and Y coordinates.
        /// </summary>
        /// <param name="x">X position of the 2D point.</param>
        /// <param name="y">Y position of the 2D point.</param>
        public Position(int x, int y)
        {
            _x = x;
            _y = y;
        }

        private int _x = 0;
        private int _y = 0;

        /// <summary>
        /// Setting X position of the 2D point.
        /// </summary>
        /// <param name="x"> X position of the 2D point. </param>
        public void SetX(int x)
        {
            _x = x;
        }

        /// <summary>
        /// Getting X position of the 2D point.
        /// </summary>
        /// <returns> X position of the 2D point. </returns>
        public int GetX()
        {
            return _x;
        }

        /// <summary>
        /// Setting Y position of the 2D point.
        /// </summary>
        /// <param name="y"> Y position of the 2D point. </param>
        public void SetY(int y)
        {
            _y = y;
        }

        /// <summary>
        /// Getting Y position of the 2D point.
        /// </summary>
        /// <returns> Y position of the 2D point. </returns>
        public int GetY()
        {
            return _y;
        }
        
        /// <summary>
        /// Setting 2D point position.
        /// </summary>
        /// <param name="x"> X position of the 2D point. </param>
        /// <param name="y"> Y position of the 2D point. </param>
        public void SetPosition(int x, int y)
        {
            SetX(x);
            SetY(y);
        }
    }
}
