package com.spvessel.spacevil;

import com.spvessel.spacevil.Flags.InputEventType;

import java.util.LinkedList;
import java.util.List;

final class InputDeviceEvent {
    Boolean isHappen = false;
    private List<InputEventType> eType = new LinkedList<>();

    void setEvent(InputEventType type) {
        if (!eType.contains(type))
            eType.add(type);

        isHappen = true;
    }

    Boolean isEvent() {
        Boolean tmp = isHappen;
        isHappen = false;
        return tmp;
    }

    List<InputEventType> lastEvent() {
        return eType;
    }

    void resetEvent(InputEventType type) {
        if (eType.contains(type))
            eType.remove(type);
    }

    void resetAllEvents() {
        eType.clear();
    }
}