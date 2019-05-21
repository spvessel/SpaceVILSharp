using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;

namespace SpaceVIL
{
    public delegate void EventWindowDropMethod(IItem sender, DropArgs args);

    internal sealed class WContainer : Prototype//, IWindow
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
            setEvents();

            // EventDrop += (sender, args) =>
            // {
            //     foreach (String path in args.Paths)
            //     {
            //         Console.WriteLine(path);
            //     }
            // };
        }

        void setEvents()
        {
            EventFocusGet += (sender) =>
            {
                GetHandler().EventFocusGet?.Invoke(sender);
            };
            EventFocusLost += (sender) =>
            {
                GetHandler().EventFocusLost?.Invoke(sender);
            };
            EventResize += (sender) =>
            {
                GetHandler().EventResize?.Invoke(sender);
            };
            EventDestroy += (sender) =>
            {
                GetHandler().EventDestroy?.Invoke(sender);
            };
            EventMouseHover += (sender, args) =>
            {
                GetHandler().EventMouseHover?.Invoke(sender, args);
            };
            EventMouseLeave += (sender, args) =>
            {
                GetHandler().EventMouseLeave?.Invoke(sender, args);
            };
            EventMouseClick += (sender, args) =>
            {
                GetHandler().EventMouseClick?.Invoke(sender, args);
            };
            EventMouseDoubleClick += (sender, args) =>
            {
                GetHandler().EventMouseDoubleClick?.Invoke(sender, args);
            };
            EventMousePress += (sender, args) =>
            {
                GetHandler().EventMousePress?.Invoke(sender, args);
            };
            EventMouseDrag += (sender, args) =>
            {
                GetHandler().EventMouseDrag?.Invoke(sender, args);
            };
            EventMouseDrop += (sender, args) =>
            {
                GetHandler().EventMouseDrop?.Invoke(sender, args);
            };
            EventScrollUp += (sender, args) =>
            {
                GetHandler().EventScrollUp?.Invoke(sender, args);
            };
            EventScrollDown += (sender, args) =>
            {
                GetHandler().EventScrollDown?.Invoke(sender, args);
            };
            EventKeyPress += (sender, args) =>
            {
                GetHandler().EventKeyPress?.Invoke(sender, args);
            };
            EventKeyRelease += (sender, args) =>
            {
                GetHandler().EventKeyRelease?.Invoke(sender, args);
            };
            EventTextInput += (sender, args) =>
            {
                GetHandler().EventTextInput?.Invoke(sender, args);
            };
            EventDrop += (sender, args) =>
            {
                GetHandler().EventDrop?.Invoke(sender, args);
            };
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
            if (xpos <= SpaceVILConstants.BorderCursorTolerance)
            {
                _sides |= Side.Left;
            }
            if (xpos >= GetWidth() - SpaceVILConstants.BorderCursorTolerance)
            {
                _sides |= Side.Right;
            }

            if (ypos <= SpaceVILConstants.BorderCursorTolerance)
            {
                _sides |= Side.Top;
            }
            if (ypos >= GetHeight() - SpaceVILConstants.BorderCursorTolerance)
            {
                _sides |= Side.Bottom;
            }

            return _sides;
        }
    }
}