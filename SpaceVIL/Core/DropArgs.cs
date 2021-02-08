using System;
using System.Collections.Generic;
using System.Text;

namespace SpaceVIL.Core
{
    /// <summary>
    /// A class that describe mouse "drag and drop" input (file system).
    /// </summary>
    public sealed class DropArgs : IInputEventArgs
    {
        /// <summary>
        /// An item on which the drop event occurs.
        /// </summary>
        public Prototype Item = null;

        /// <summary>
        /// Number of selected paths.
        /// </summary>
        public int Count = -1;

        /// <summary>
        /// List of selected paths.
        /// </summary>
        public List<String> Paths = null;
        
        /// <summary>
        /// Clearing DropArgs.
        /// </summary>
        public void Clear()
        {
            Count = -1;
            Paths = null;
            Item = null;
        }

        public override String ToString()
        {
            StringBuilder sb = new StringBuilder();
            sb.AppendLine(Item.GetItemName());
            sb.AppendLine(Count + " ");
            if (Paths != null)
            {
                foreach (var p in Paths)
                {
                    sb.AppendLine(p);
                }
            }

            return sb.ToString();
        }
    }
}