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
        bool _stoped;
        //int _interval = 1000 / 60;
        public ActionManager(WindowLayout wnd)
        {
            _handler = wnd;
        }
        public void StartManager()
        {
            _stoped = false;
            while (!_stoped)
            {
                Execute.WaitOne();
                ExecuteActions();
                Execute.Set();
                //Execute.Reset();
            }
        }

        public void StopManager()
        {
            _stoped = true;
        }

        internal void ExecuteActions()
        {
            if(StackEvents.Count == 0)
                return;

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
                case InputEventType.MousePressed:
                    InvokeMousePressedEvent(task.Item);
                    break;
                default:
                    break;
            }
        }

        //common events
        private void InvokeFocusGetEvent(VisualItem sender)
        {
            sender.EventFocusGet?.Invoke(sender);
        }
        private void InvokeFocusLostEvent(VisualItem sender)
        {
            sender.EventFocusLost?.Invoke(sender);
        }
        private void InvokeResizedEvent(VisualItem sender)
        {
            sender.EventResized?.Invoke(sender);
        }
        private void InvokeDestroyedEvent(VisualItem sender)
        {
            sender.EventDestroyed?.Invoke(sender);
        }
        //mouse input
        private void InvokeMouseClickEvent(VisualItem sender)
        {
            sender.EventMouseClick?.Invoke(sender);
        }
        private void InvokeMouseHoverEvent(VisualItem sender)
        {
            sender.EventMouseHover?.Invoke(sender);
        }
        private void InvokeMousePressedEvent(VisualItem sender)
        {
           
            sender.EventMousePressed?.Invoke(sender);
        }
        private void InvokeMouseReleaseEvent(VisualItem sender)
        {
            sender.EventMouseRelease?.Invoke(sender);
        }
        private void InvokeMouseDragEvent(VisualItem sender)
        {
            sender.EventMouseDrop.Invoke(sender);
        }
        private void InvokeMouseDropEvent(VisualItem sender)
        {
            sender.EventMouseDrag.Invoke(sender);
        }
        private void InvokeMouseScrollUpEvent(VisualItem sender)
        {
            sender.EventScrollUp.Invoke(sender);
        }
        private void InvokeMouseScrollDownEvent(VisualItem sender)
        {
            sender.EventScrollDown.Invoke(sender);
        }
        //keyboard input
        private void InvokeKeyPressEvent(VisualItem sender)
        {

        }
        private void InvokeKeyReleaseEvent(VisualItem sender)
        {

        }
        private void InvokeTextInputEvent(VisualItem sender)
        {

        }
    }
}