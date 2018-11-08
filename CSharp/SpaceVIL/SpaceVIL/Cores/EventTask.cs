using System;
using System.Threading;
using System.Collections.Generic;
using System.Collections.Concurrent;

namespace SpaceVIL
{
    internal class EventTask
    {
        public InputEventType Action = 0;
        public Prototype Item = null;
        public InputEventArgs Args = null;
    }
}