using System.Drawing;

namespace SpaceVIL
{
    public class WContainer : VisualItem, IWindow
    {
        static int count = 0;
        internal ItemAlignment _sides = 0;
        internal bool _is_fixed = false;
        internal bool _resizing = false;

        public WContainer()
        {
            SetItemName("WContainer_" + count);
            count++;
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
            }
            else
            {
                IsMouseHover = false;
            }
            return IsMouseHover;
        }

        public ItemAlignment GetSides(float xpos, float ypos) //проблемы с глобальным курсором
        {
            if (xpos <= 5)
            {
                _sides |= ItemAlignment.Left;
            }
            if (xpos > GetWidth() - 5)
            {
                _sides |= ItemAlignment.Right;
            }

            if (ypos <= 5)
            {
                _sides |= ItemAlignment.Top;
            }
            if (ypos > GetHeight() - 5)
            {
                _sides |= ItemAlignment.Bottom;
            }

            return _sides;
        }
    }
}