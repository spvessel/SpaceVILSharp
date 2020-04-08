package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceInputEventArgs;
import com.spvessel.spacevil.Flags.InputEventType;

final class EventTask {
    InputEventType action = InputEventType.EMPTY;
    Prototype item = null;
    InterfaceInputEventArgs args = null;
}