package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InputEventArgs;
import com.spvessel.spacevil.Flags.InputEventType;

final class EventTask {
    InputEventType action = InputEventType.EMPTY;
    Prototype item = null;
    InputEventArgs args = null;
}