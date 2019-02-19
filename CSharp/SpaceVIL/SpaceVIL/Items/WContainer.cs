using System;
using System.Drawing;
using SpaceVIL.Core;

namespace SpaceVIL
{
    public class WContainer : Prototype//, IWindow
    {
        static int count = 0;
        internal ItemAlignment _sides = 0;
        internal bool _is_fixed = false;
        private Prototype _focus = null;

        public WContainer()
        {
            SetItemName("WContainer_" + count);
            count++;
        }

        internal void SaveLastFocus(Prototype focused)
        {
            _focus = focused;
        }
        internal void RestoreFocus()
        {
            if (_focus != null)
                _focus.SetFocus();
            _focus = null;
        }

        internal override bool GetHoverVerification(float xpos, float ypos)
        {
            if (_is_fixed)
                return false;

            if (xpos > 0
                && xpos <= GetHandler().GetWidth()
                && ypos > 0
                && ypos <= GetHandler().GetHeight())
            {
                SetMouseHover(true);
            }
            else
            {
                SetMouseHover(false);
            }
            return IsMouseHover();
        }

        internal ItemAlignment GetSides(float xpos, float ypos) //проблемы с глобальным курсором
        {
            if (xpos <= 5)
            {
                _sides |= ItemAlignment.Left;
            }
            if (xpos >= GetWidth() - 5)
            {
                _sides |= ItemAlignment.Right;
            }

            if (ypos <= 5)
            {
                _sides |= ItemAlignment.Top;
            }
            if (ypos >= GetHeight() - 5)
            {
                _sides |= ItemAlignment.Bottom;
            }

            return _sides;
        }
    }
}