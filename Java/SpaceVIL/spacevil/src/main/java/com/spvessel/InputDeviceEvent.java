package com.spvessel;

import com.spvessel.Flags.InputEventType;

import java.util.LinkedList;
import java.util.List;

final class InputDeviceEvent {
    protected Boolean isHappen = false;
    private List<InputEventType> eType = new LinkedList<InputEventType>();

    protected synchronized void setEvent(InputEventType type) {
        if (!eType.contains(type))
            eType.add(type);

        isHappen = true;
    }

    protected synchronized Boolean isEvent() {
        Boolean tmp = isHappen;
        isHappen = false;
        return tmp;
    }

    protected synchronized List<InputEventType> lastEvent() {
        return eType;
    }

    protected synchronized void resetEvent(InputEventType type) {
        if (eType.contains(type))
            eType.remove(type);
    }

    protected synchronized void resetAllEvents() {
        eType.clear();
    }
}