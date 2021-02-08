using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Core
{
    internal class InputDeviceEvent
    {
        internal bool isHappen = false;
        private InputEventType EType = 0;
        internal void SetEvent(InputEventType type)
        {
            EType |= type;
            isHappen = true;
        }
        internal bool IsEvent()
        {
            bool tmp = isHappen;
            isHappen = false;
            return tmp;
        }
        internal InputEventType LastEvent()
        {
            return EType;
        }
        internal void ResetEvent(InputEventType type)
        {
            EType &= ~type;
        }
        internal void ResetAllEvents()
        {
            EType = 0;
        }
    }
}
