using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Decorations
{
    /// <summary>
    /// A class that store vertical and horizontal spacing values of the item.
    /// </summary>
    public struct Spacing
    {
        /// <summary>
        /// Constructs a Spacing with strict horizontal and vertical spacing values.
        /// <para/> Default Spacing values is zeros.
        /// </summary>
        /// <param name="horizontal">Horizontal indent between items.</param>
        /// <param name="vertical">Vertical indent between items.</param>
        public Spacing(int horizontal = 0, int vertical = 0)
        {
            Horizontal = horizontal;
            Vertical = vertical;
        }
        /// <summary>
        /// Horizontal indent between items.
        /// </summary>
        public int Horizontal;
        /// <summary>
        /// Vertical indent between items.
        /// </summary>
        public int Vertical;
    }
}
