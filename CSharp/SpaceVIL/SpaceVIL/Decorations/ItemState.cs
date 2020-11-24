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
        public Color Background;

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
