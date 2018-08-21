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
        public InputEventArgs Args = null;
    }

    internal class ActionManager
    {
        internal ConcurrentQueue<EventTask> StackEvents = new ConcurrentQueue<EventTask>();

        internal ManualResetEventSlim Execute = new ManualResetEventSlim(false);
        WindowLayout _handler;
        bool _stoped;

        public ActionManager(WindowLayout wnd)
        {
            _handler = wnd;
        }
        public void StartManager()
        {
            _stoped = false;
            while (!_stoped)
            {
                Execute.Wait();
                ExecuteActions();
                Execute.Reset();
                //_handler.Execute.Set();
            }
        }

        public void StopManager()
        {
            _stoped = true;
        }

        internal void ExecuteActions()
        {
            if (StackEvents.Count == 0)
                return;

            while (StackEvents.Count > 0)
            {
                EventTask tmp = null;
                if (StackEvents.TryDequeue(out tmp))
                {
                    ExecuteAction(tmp);
                }
            }
        }
        private void ExecuteAction(EventTask task)
        {
            switch (task.Action)
            {
                case InputEventType.MouseRelease:
                    InvokeMouseClickEvent(task.Item, task.Args as MouseArgs);
                    break;
                case InputEventType.MousePressed:
                    InvokeMousePressedEvent(task.Item, task.Args as MouseArgs);
                    break;
                case InputEventType.MouseHover:
                    InvokeMouseHoverEvent(task.Item, task.Args as MouseArgs);
                    break;
                case InputEventType.MouseDrag:
                    InvokeMouseDragEvent(task.Item, task.Args as MouseArgs);
                    break;
                case InputEventType.FocusGet:
                    InvokeFocusGetEvent(task.Item);
                    break;
                case InputEventType.KeyPress:
                    InvokeKeyPressEvent(task.Item, task.Args as KeyArgs);
                    break;
                case InputEventType.KeyRelease:
                    InvokeKeyReleaseEvent(task.Item, task.Args as KeyArgs);
                    break;
                case InputEventType.TextInput:
                    InvokeTextInputEvent(task.Item, task.Args as TextInputArgs);
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
        private void InvokeMouseClickEvent(VisualItem sender, MouseArgs args)
        {
            sender.EventMouseClick?.Invoke(sender, args);
        }
        private void InvokeMouseHoverEvent(VisualItem sender, MouseArgs args)
        {
            sender.EventMouseHover?.Invoke(sender, args);
        }
        private void InvokeMousePressedEvent(VisualItem sender, MouseArgs args)
        {
            sender.EventMousePressed?.Invoke(sender, args);
        }
        private void InvokeMouseReleaseEvent(VisualItem sender, MouseArgs args)
        {
            sender.EventMouseRelease?.Invoke(sender, args);
        }
        private void InvokeMouseDragEvent(VisualItem sender, MouseArgs args)
        {
            sender.EventMouseDrag?.Invoke(sender, args);
        }
        private void InvokeMouseDropEvent(VisualItem sender, MouseArgs args)
        {
            sender.EventMouseDrag?.Invoke(sender, args);
        }
        private void InvokeMouseScrollUpEvent(VisualItem sender, MouseArgs args)
        {
            sender.EventScrollUp?.Invoke(sender, args);
        }
        private void InvokeMouseScrollDownEvent(VisualItem sender, MouseArgs args)
        {
            sender.EventScrollDown?.Invoke(sender, args);
        }

        //keyboard input
        private void InvokeKeyPressEvent(VisualItem sender, KeyArgs args)
        {
            sender.EventKeyPress?.Invoke(sender, args);
        }
        private void InvokeKeyReleaseEvent(VisualItem sender, KeyArgs args)
        {
            sender.EventKeyRelease?.Invoke(sender, args);
        }
        private void InvokeTextInputEvent(VisualItem sender, TextInputArgs args)
        {
            sender.EventTextInput?.Invoke(sender, args);
        }
    }
}