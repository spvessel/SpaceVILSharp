using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
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
