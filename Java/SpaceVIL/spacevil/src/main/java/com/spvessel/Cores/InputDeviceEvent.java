package com.spvessel.Cores;
import com.spvessel.Flags.*;

public class InputDeviceEvent
    {
        protected Boolean isHappen = false;
        private int eType = 0;
        public void setEvent(InputEventType type)
        {
            eType |= type.getValue();
            isHappen = true;
        }
        public Boolean isEvent()
        {
            Boolean tmp = isHappen;
            isHappen = false;
            return tmp;
        }
        public int lastEvent()
        {
            return eType;
        }
        public void resetEvent(InputEventType type)
        {
            eType &= ~type.getValue();
        }
        public void resetAllEvents()
        {
            eType = 0;
        }
    }