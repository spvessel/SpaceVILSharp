using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL.Decorations
{
    /// <summary>
    /// A class that describes state of the item and its changes according to state value.
    /// </summary>
    public class ItemState
    {
        public bool Value = true;

        /// <summary>
        /// Getting backgroud color of the item of current state as System.Drawing.Color.
        /// </summary>
        public Color Background; // = Color.LightGray;

        // public Color Foreground = Color.Black;
        // public string Text = null;
        // public string ImageUri = null;
        // public bool IsVisible = true;

        /// <summary>
        /// Getting border of the item of current state as SpaceVIL.Decorations.Border.
        /// </summary>
        public Border Border = new Border();

        /// <summary>
        /// Getting shape of the item of current state as SpaceVIL.Decorations.Figure.
        /// </summary>
        public Figure Shape = null;

        /// <summary>
        /// Constructs an empty ItemState
        /// </summary>
        public ItemState() { }

        /// <summary>
        /// Constructs an ItemState with the specified background color.
        /// </summary>
        /// <param name="background">A color of item as System.Drawing.Color.</param>
        public ItemState(Color background)
        {
            this.Background = background;
        }
    }
}
