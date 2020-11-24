package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.IInputEventArgs;
import com.spvessel.spacevil.Flags.InputEventType;

final class EventTask {
    InputEventType action = InputEventType.Empty;
    Prototype item = null;
    IInputEventArgs args = null;
}