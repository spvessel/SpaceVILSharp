using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    public class EventManager
    {
        public const int Focused = 0;
        public const int Moved_X = 1;
        public const int Moved_Y = 2;
        public const int ResizeWidth = 3;
        public const int ResizeHeight = 4;

        Dictionary<int, List<IEventUpdate>> listeners = new Dictionary<int, List<IEventUpdate>>();

        public void SetListeners(params int[] events)
        {
            foreach (var s in events)
            {
                listeners.Add(s, new List<IEventUpdate>());
            }
        }

        public void Subscribe(int eventType, IEventUpdate listener)
        {
            if (!listeners.ContainsKey(eventType))
                listeners.Add(eventType, new List<IEventUpdate>());

            if (!listeners[eventType].Contains(listener))
                listeners[eventType].Add(listener);
        }

        public void Unsubscribe(int eventType, IEventUpdate listener)
        {
            if (listeners[eventType].Contains(listener))
                listeners[eventType].Remove(listener);
        }

        public void NotifyListeners(int eventType, int value)
        {
            if (listeners.ContainsKey(eventType))
            {
                foreach (var _ in listeners[eventType])
                {
                    _.Update(eventType, value);
                }
            }
        }
    }
}
