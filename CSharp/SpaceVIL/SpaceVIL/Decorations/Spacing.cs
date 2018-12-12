using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Decorations
{
    /// <summary>
    /// A class that store vertical and horizontal spacing values of the object
    /// </summary>
    public struct Spacing
    {
        /// <summary>
        /// Constructs a Spacing with strict horizontal and vertical spacing values
        /// (default Spacing values is zeros)
        /// </summary>
        public Spacing(int horizontal = 0, int vertical = 0)
        {
            Horizontal = horizontal;
            Vertical = vertical;
        }
        public int Horizontal;
        public int Vertical;
    }
}
