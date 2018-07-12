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
        MouseMove = 0x01,
        MousePressed = 0x02,
        MouseRelease = 0x04,
        MouseScroll = 0x08,
        KeyPress = 0x10,
        KeyRepeat = 0x20,
        KeyRelease = 0x40,
        WindowResize = 0x80,
        WindowMove = 0x100,
        WindowMinimize = 0x200,
        WindowRestore = 0x400,
        WindowGetFocus = 0x800,
        WindowLostFocus = 0x1000
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
