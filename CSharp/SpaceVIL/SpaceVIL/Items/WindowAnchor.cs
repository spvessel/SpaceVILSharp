using System.Drawing;
using SpaceVIL.Core;

namespace SpaceVIL
{
    /// <summary>
    /// WindowAnchor is class representing the draggable window type of an item. 
    /// Supports all events except drag and drop despite that this class is draggable type.
    /// </summary>
    public class WindowAnchor : Prototype, IWindowAnchor
    {
        static int count = 0;
        /// <summary>
        /// Default WindowAnchor constructor.
        /// </summary>
        public WindowAnchor()
        {
            SetItemName("WindowAnchor_" + count);
            count++;
        }
    }
}
