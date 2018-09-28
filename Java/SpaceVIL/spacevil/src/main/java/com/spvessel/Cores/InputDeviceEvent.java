package com.spvessel.Cores;

import com.spvessel.Flags.*;
import java.util.LinkedList;
import java.util.List;

public class InputDeviceEvent {
    protected Boolean isHappen = false;
    private List<InputEventType> eType = new LinkedList<InputEventType>();

    public void setEvent(InputEventType type) {
        if (!eType.contains(type))
            eType.add(type);

        isHappen = true;
    }

    public Boolean isEvent() {
        Boolean tmp = isHappen;
        isHappen = false;
        return tmp;
    }

    public List<InputEventType> lastEvent() {
        return eType;
    }

    public void resetEvent(InputEventType type) {
        if (eType.contains(type))
            eType.remove(type);
    }

    public void resetAllEvents() {
        eType.clear();
    }
}