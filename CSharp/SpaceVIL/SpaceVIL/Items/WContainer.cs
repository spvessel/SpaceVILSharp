using System.Drawing;

namespace SpaceVIL
{
    internal class WContainer : VisualItem, IWindow
    {
        static int count = 0;
        internal int _resizeBorderThickness = 5;
        internal ItemAlignment _sides = 0;
        internal bool _is_fixed = false;
        internal bool _resizing = false;

        public WContainer()
        {
            SetItemName("WContainer_" + count);
            count++;

            //Events
            EventMouseClick += EmptyEvent;
        }

        protected internal override bool GetHoverVerification(float xpos, float ypos)
        {
            if (_is_fixed)
                return false;

            if (xpos > 0
                && xpos <= GetHandler().GetWidth()
                && ypos > 0
                && ypos <= GetHandler().GetHeight())
            {
                IsMouseHover = true;
                _mouse_ptr.SetPosition(xpos, ypos);
            }
            else
            {
                IsMouseHover = false;
                _mouse_ptr.Clear();
            }
            return IsMouseHover;
        }

        public ItemAlignment GetSides(float xpos, float ypos)
        {
            if (xpos > GetWidth() - _resizeBorderThickness || _sides.HasFlag(ItemAlignment.Right))
            {
                _sides |= ItemAlignment.Right;
                if (xpos < GetMinWidth() && GetWidth() == GetMinWidth())
                    _sides &= ~ItemAlignment.Right;
            }

            if (ypos > GetHeight() - _resizeBorderThickness || _sides.HasFlag(ItemAlignment.Bottom))
            {
                _sides |= ItemAlignment.Bottom;
                if (ypos < GetMinHeight() && GetHeight() == GetMinHeight())
                    _sides &= ~ItemAlignment.Bottom;
            }
            return _sides;
        }
    }
}