using System;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;

namespace SpaceVIL
{
    public delegate void EventWindowDropMethod(IItem sender, DropArgs args);

    public class WContainer : Prototype//, IWindow
    {
        public EventWindowDropMethod EventDrop;

        public override void Release()
        {
            EventDrop = null;
        }

        static int count = 0;
        internal Side _sides = 0;
        internal bool _is_fixed = false;
        private Prototype _focus = null;

        public WContainer()
        {
            SetItemName("WContainer_" + count);
            count++;
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.WContainer)));

            // EventDrop += (sender, args) =>
            // {
            //     foreach (String path in args.Paths)
            //     {
            //         Console.WriteLine(path);
            //     }
            // };
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

        protected internal override bool GetHoverVerification(float xpos, float ypos)
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

        internal Side GetSides(float xpos, float ypos) //проблемы с глобальным курсором
        {
            if (xpos <= 5)
            {
                _sides |= Side.Left;
            }
            if (xpos >= GetWidth() - 5)
            {
                _sides |= Side.Right;
            }

            if (ypos <= 5)
            {
                _sides |= Side.Top;
            }
            if (ypos >= GetHeight() - 5)
            {
                _sides |= Side.Bottom;
            }

            return _sides;
        }
    }
}