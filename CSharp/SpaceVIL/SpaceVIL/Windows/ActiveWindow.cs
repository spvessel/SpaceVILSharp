using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    /// <summary>
    /// ActiveWindow is an abstract class for base window instances.
    /// <para/>ActiveWindow extends CoreWindow class. 
    /// CoreWindow is an abstract class containing an implementation of common functionality for a window.
    /// </summary>
    public abstract class ActiveWindow : CoreWindow
    {
        /// <summary>
        /// Constructs a ActiveWindow
        /// </summary>
        public ActiveWindow() : base()
        {
            InitWindow();
        }
    }
}
