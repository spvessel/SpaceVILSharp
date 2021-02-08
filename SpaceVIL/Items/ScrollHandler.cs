using SpaceVIL.Core;

namespace SpaceVIL
{
    /// <summary>
    /// ScrollHandler is part of SpaceVIL.HorizontalSlider and
    /// SpaceVIL.VerticalSlider. ScrollHandler is responsible for 
    /// handler dragging.
    /// <para/> Supports all events including drag and drop.
    /// </summary>
    public class ScrollHandler : Prototype, IDraggable
    {
        static int count = 0;

        /// <summary>
        /// Specify orientation of ScrollHandler.
        /// <para/> Can be SpaceVIL.Core.Orientation.Vertical or
        /// SpaceVIL.Core.Orientation.Horizontal.
        /// </summary>
        public Orientation Orientation;
        private int _offset = 0;
        private int _diff = 0;

        /// <summary>
        /// Default ScrollHandler constructor.
        /// </summary>
        public ScrollHandler()
        {
            SetItemName("ScrollHandler_" + count);
            EventMousePress += OnMousePress;
            EventMouseDrag += OnDragging;
            count++;
            IsFocusable = false;
        }

        private void OnMousePress(IItem sender, MouseArgs args)
        {
            if (Orientation == Orientation.Horizontal)
                _diff = args.Position.GetX() - GetX();
            else
                _diff = args.Position.GetY() - GetY();
        }

        private void OnDragging(IItem sender, MouseArgs args)
        {
            int offset;
            Prototype parent = GetParent();
            if (Orientation == Orientation.Horizontal)
                offset = args.Position.GetX() - parent.GetX() - _diff;
            else
                offset = args.Position.GetY() - parent.GetY() - _diff;

            SetOffset(offset);
        }

        /// <summary>
        /// Setting offset of the ScrollHandler by X axis or Y axis 
        /// depending on Orientation property.
        /// </summary>
        /// <param name="offset">Offset of the ScrollHandler.</param>
        public void SetOffset(int offset)
        {
            Prototype parent = GetParent();
            if (parent == null)
                return;

            _offset = offset;

            if (Orientation == Orientation.Horizontal)
                SetX(_offset + parent.GetX());
            else
                SetY(_offset + parent.GetY());
        }
    }
}
