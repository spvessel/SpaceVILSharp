using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Core
{
    internal sealed class EventManager
    {
        //internal static bool IsLocked = true;
        internal Dictionary<GeometryEventType, List<IEventUpdate>> listeners = new Dictionary<GeometryEventType, List<IEventUpdate>>();

        internal void Subscribe(GeometryEventType type, IEventUpdate listener)
        {
            if (!listeners.ContainsKey(type))
                listeners.Add(type, new List<IEventUpdate>());

            if (!listeners[type].Contains(listener))
                listeners[type].Add(listener);
        }

        internal void Unsubscribe(GeometryEventType type, IEventUpdate listener)
        {
            if (listeners.ContainsKey(type))
                if (listeners[type].Contains(listener))
                    listeners[type].Remove(listener);
        }

        internal void NotifyListeners(GeometryEventType type, int value)
        {
            if (listeners.ContainsKey(type))
                foreach (var _ in listeners[type])
                    _.Update(type, value);
        }
    }
}
