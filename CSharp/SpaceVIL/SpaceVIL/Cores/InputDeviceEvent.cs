using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    public enum InputEventType
    {
        Empty,
        MouseMove,
        MousePressed,
        MouseRelease,
        MouseScroll,
        KeyPress,
        WindowResize,
        WindowMove
    }

    public class InputDeviceEvent
    {
        internal bool isHappen = false;
        private InputEventType EType = InputEventType.Empty;
        public void SetEvent(InputEventType type)
        {
            EType = type;
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
    }
}
