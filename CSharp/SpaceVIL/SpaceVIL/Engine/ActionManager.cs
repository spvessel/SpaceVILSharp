using System;
using System.Threading;
using System.Collections.Generic;
using System.Collections.Concurrent;

namespace SpaceVIL
{
    internal class EventTask
    {
        public InputEventType Action = 0;
        public VisualItem Item = null;
    }

    internal class ActionManager
    {
        internal ConcurrentQueue<EventTask> StackEvents = new ConcurrentQueue<EventTask>();

        internal ManualResetEvent Execute = new ManualResetEvent(false);
        WindowLayout _handler;
        bool _stoped = false;
        int _interval = 1000 / 60;

        public ActionManager(WindowLayout wnd)
        {
            _handler = wnd;
        }
        public void StartManager()
        {
            while (!_stoped)
            {
                Execute.WaitOne();
                ExecuteActions();
                Execute.Set();
            }
        }

        public void StopManager()
        {
            _stoped = true;
        }

        internal void ExecuteActions()
        {
            while (StackEvents.Count > 0)
            {
                EventTask tmp = null;
                StackEvents.TryDequeue(out tmp);
                if (tmp != null)
                    ExecuteAction(tmp);
            }
        }
        private void ExecuteAction(EventTask task)
        {
            switch (task.Action)
            {
                case InputEventType.MouseRelease:
                    InvokeMouseClickEvent(task.Item);
                    break;
                default:
                    break;
            }
        }

        //common events
        private void InvokeFocusGetEvent(VisualItem sender)
        {

        }
        private void InvokeFocusLostEvent(VisualItem sender) { }
        private void InvokeResizedEvent(VisualItem sender) { }
        private void InvokeDestroyedEvent(VisualItem sender) { }
        //mouse input
        private void InvokeMouseClickEvent(VisualItem sender)
        {
            sender.EventMouseClick.Invoke(sender);
        }
        private void InvokeMouseHoverEvent(VisualItem sender) { }
        private void InvokeMousePressedEvent(VisualItem sender) { }
        private void InvokeMouseReleaseEvent(VisualItem sender) { }
        private void InvokeMouseDragEvent(VisualItem sender)
        {
            sender.EventMouseDrop.Invoke(sender);
        }
        private void InvokeMouseDropEvent(VisualItem sender)
        {
            sender.EventMouseDrag.Invoke(sender);
        }
        private void InvokeMouseScrollUpEvent(VisualItem sender) { }
        private void InvokeMouseScrollDownEvent(VisualItem sender) { }
        //keyboard input
        private void InvokeKeyPressEvent(VisualItem sender) { }
        private void InvokeKeyReleaseEvent(VisualItem sender) { }
        private void InvokeTextInputEvent(VisualItem sender) { }
    }
}