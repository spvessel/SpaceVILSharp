using System;
using System.Threading;
using System.Collections.Generic;
using System.Collections.Concurrent;

namespace SpaceVIL.Core
{
    internal sealed class EventTask
    {
        internal InputEventType Action = 0;
        internal Prototype Item = null;
        internal IInputEventArgs Args = null;
    }
}