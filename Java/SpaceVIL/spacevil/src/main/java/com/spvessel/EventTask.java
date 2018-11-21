package com.spvessel;

import com.spvessel.Core.InputEventArgs;
import com.spvessel.Flags.InputEventType;

final class EventTask {
    public InputEventType action = InputEventType.EMPTY;
    public Prototype item = null;
    public InputEventArgs args = null;
}