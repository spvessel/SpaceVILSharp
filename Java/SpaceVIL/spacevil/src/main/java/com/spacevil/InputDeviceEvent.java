package com.spacevil;

import java.util.LinkedList;
import java.util.List;

final class InputDeviceEvent {
    Boolean isHappen = false;
    private List<com.spacevil.Flags.InputEventType> eType = new LinkedList<>();

    void setEvent(com.spacevil.Flags.InputEventType type) {
        if (!eType.contains(type))
            eType.add(type);

        isHappen = true;
    }

    Boolean isEvent() {
        Boolean tmp = isHappen;
        isHappen = false;
        return tmp;
    }

    List<com.spacevil.Flags.InputEventType> lastEvent() {
        return eType;
    }

    void resetEvent(com.spacevil.Flags.InputEventType type) {
        if (eType.contains(type))
            eType.remove(type);
    }

    void resetAllEvents() {
        eType.clear();
    }
}