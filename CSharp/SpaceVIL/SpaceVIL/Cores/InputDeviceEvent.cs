using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    public enum EventType
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
        private EventType EType = EventType.Empty;
        public void SetEvent(EventType type)
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
        public EventType LastEvent()
        {
            return EType;
        }
    }
}
