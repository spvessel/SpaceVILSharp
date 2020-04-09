using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// SplitHolder is part of SpaceVIL.HorizontalSplitArea and
    /// SpaceVIL.VerticalSplitArea. SplitHolder is responsible for 
    /// handler dragging.
    /// <para/> Supports all events including drag and drop.
    /// </summary>
    public class SplitHolder : Prototype, IDraggable
    {
        private static int count = 0;
        private Orientation _orientation;
        private int _spacerSize = 6;

        /// <summary>
        /// Constructs a SplitHolder with the specified orientation.
        /// <para/> Orientation can be Orientation.Horizontal 
        /// or Orientation.Vertical.
        /// </summary>
        /// <param name="orientation">Orientation of SplitHolder.</param>
        public SplitHolder(Orientation orientation)
        {
            _orientation = orientation;
            SetItemName("SplitHolder_" + count);
            count++;
            IsFocusable = false;
            MakeHolderShape();
        }

        /// <summary>
        /// Setting thickness of SplitHolder divider.
        /// </summary>
        /// <param name="thickness">Thickness of SplitHolder divider.</param>
        public void SetDividerSize(int thickness)
        {
            if (_spacerSize != thickness)
            {
                _spacerSize = thickness;
                MakeHolderShape();
            }
        }

        /// <summary>
        /// Getting thickness of SplitHolder divider.
        /// </summary>
        /// <returns>Thickness of SplitHolder divider.</returns>
        public int GetDividerSize()
        {
            return _spacerSize;
        }

        private void MakeHolderShape()
        {
            switch (_orientation)
            {
                case Orientation.Vertical:
                    SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
                    // SetMinWidth(_spacerSize);
                    SetWidth(_spacerSize);
                    SetCursor(EmbeddedCursor.ResizeX);
                    break;

                case Orientation.Horizontal:
                    SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
                    // SetMinHeight(_spacerSize);
                    SetHeight(_spacerSize);
                    SetCursor(EmbeddedCursor.ResizeY);
                    break;
            }
        }

        /// <summary>
        /// Getting SplitHolder orientation.
        /// <para/> Orientation can be Orientation.Horizontal 
        /// or Orientation.Vertical.
        /// </summary>
        /// <returns>Current SplitHolder orientation.</returns>
        public Orientation GetOrientation()
        {
            return _orientation;
        }

        /// <summary>
        /// Setting style of the SplitHolder.
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public override void SetStyle(Style style)
        {
            if (style == null)
            {
                return;
            }
            SetBackground(style.Background);
        }
    }
}
