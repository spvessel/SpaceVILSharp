using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    public class EventManager
    {
        internal static bool IsLocked = true;
        Dictionary<GeometryEventType, List<IEventUpdate>> listeners = new Dictionary<GeometryEventType, List<IEventUpdate>>();

        public void SetListeners(params GeometryEventType[] events)
        {
            foreach (var s in events)
            {
                listeners.Add(s, new List<IEventUpdate>());
            }
        }

        public void Subscribe(GeometryEventType type, IEventUpdate listener)
        {
            if (!listeners.ContainsKey(type))
                listeners.Add(type, new List<IEventUpdate>());

            if (!listeners[type].Contains(listener))
                listeners[type].Add(listener);
        }

        public void Unsubscribe(GeometryEventType type, IEventUpdate listener)
        {
            if (listeners.ContainsKey(type))
            {
                if (listeners[type].Contains(listener))
                {
                    listeners[type].Remove(listener);
                }
            }
        }

        public void NotifyListeners(GeometryEventType type, int value)
        {
            if (listeners.ContainsKey(type))
            {
                foreach (var _ in listeners[type])
                {
                    _.Update(type, value);
                }
            }
        }
    }
}
