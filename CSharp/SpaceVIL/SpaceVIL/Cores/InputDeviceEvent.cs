using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    [Flags]
    public enum InputEventType
    {
        FocusGet = 0x01,
        FocusLost = 0x02,
        Resized = 0x04,
        Destroy = 0x08,
        ValueChanged = 0x10,
        SelectionChanged = 0x20,
        IndexChanged = 0x40,

        MouseMove = 0x80,
        MousePressed = 0x100,
        MouseRelease = 0x200,
        MouseScroll = 0x400,

        KeyPress = 0x800,
        KeyRepeat = 0x1000,
        KeyRelease = 0x2000,

        WindowResize = 0x4000,
        WindowMove = 0x8000,
        WindowMinimize = 0x10000,
        WindowRestore = 0x20000,
        WindowClose = 0x40000,
        WindowGetFocus = 0x80000,
        WindowLostFocus = 0x100000
    }

    public class InputDeviceEvent
    {
        internal bool isHappen = false;
        private InputEventType EType = 0;
        public void SetEvent(InputEventType type)
        {
            EType |= type;
            isHappen = true;
        }
        public bool IsEvent()
        {
            bool tmp = isHappen;
            isHappen = false;
            return tmp;
        }
        public InputEventType LastEvent()
        {
            return EType;
        }
        public void ResetEvent(InputEventType type)
        {
            EType &= ~type;
        }
        public void ResetAllEvents()
        {
            EType = 0;
        }
    }
}
