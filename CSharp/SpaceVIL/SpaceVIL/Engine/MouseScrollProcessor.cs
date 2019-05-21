using System;
using System.Collections.Generic;
using Glfw3;
using SpaceVIL.Core;

namespace SpaceVIL
{
    internal class MouseScrollProcessor
    {
        private CommonProcessor _commonProcessor;

        internal MouseScrollProcessor(CommonProcessor processor)
        {
            _commonProcessor = processor;
        }

        internal void Process(Glfw.Window glfwwnd, double dx, double dy)
        {
            if (_commonProcessor.UnderHoveredItems.Count == 0)
                return;
            Stack<Prototype> tmp = new Stack<Prototype>(_commonProcessor.UnderHoveredItems);
            while (tmp.Count > 0)
            {
                Prototype item = tmp.Pop();
                if (dy > 0 || dx < 0)
                    item.EventScrollUp?.Invoke(item, _commonProcessor.Margs);
                if (dy < 0 || dx > 0)
                    item.EventScrollDown?.Invoke(item, _commonProcessor.Margs);

                if (!item.IsPassEvents(InputEventType.MouseScroll))
                    break;
            }
            _commonProcessor.Events.SetEvent(InputEventType.MouseScroll);
        }
    }
}