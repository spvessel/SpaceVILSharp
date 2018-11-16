using System;
using System.Threading;
using System.Collections.Generic;
using System.Collections.Concurrent;
using SpaceVIL.Core;

namespace SpaceVIL
{
    internal sealed class ActionManager
    {
        internal ConcurrentQueue<EventTask> StackEvents = new ConcurrentQueue<EventTask>();

        internal ManualResetEventSlim Execute = new ManualResetEventSlim(false);
        WindowLayout _handler;
        bool _stoped;
        private Object managerLock = new Object();

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
                Monitor.Enter(managerLock);
                try
                {
                    ExecuteActions();
                }
                finally
                {
                    Execute.Reset();
                    Monitor.Exit(managerLock);
                }
            }
        }

        public void AddTask(EventTask task)
        {
            Monitor.Enter(managerLock);
            try
            {
                StackEvents.Enqueue(task);
            }
            finally
            {
                Monitor.Exit(managerLock);
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
                case InputEventType.MousePress:
                    InvokeMousePressEvent(task.Item, task.Args as MouseArgs);
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
        private void InvokeFocusGetEvent(Prototype sender)
        {
            sender.EventFocusGet?.Invoke(sender);
        }
        private void InvokeFocusLostEvent(Prototype sender)
        {
            sender.EventFocusLost?.Invoke(sender);
        }
        private void InvokeResizedEvent(Prototype sender)
        {
            sender.EventResize?.Invoke(sender);
        }
        private void InvokeDestroyedEvent(Prototype sender)
        {
            sender.EventDestroy?.Invoke(sender);
        }

        //mouse input
        private void InvokeMouseClickEvent(Prototype sender, MouseArgs args)
        {
            sender.EventMouseClick?.Invoke(sender, args);
        }
        private void InvokeMouseHoverEvent(Prototype sender, MouseArgs args)
        {
            sender.EventMouseHover?.Invoke(sender, args);
        }
        private void InvokeMousePressEvent(Prototype sender, MouseArgs args)
        {
            sender.EventMousePress?.Invoke(sender, args);
        }
        private void InvokeMouseDoubleClickEvent(Prototype sender, MouseArgs args)
        {
            sender.EventMouseDoubleClick?.Invoke(sender, args);
        }
        private void InvokeMouseDragEvent(Prototype sender, MouseArgs args)
        {
            sender.EventMouseDrag?.Invoke(sender, args);
        }
        private void InvokeMouseDropEvent(Prototype sender, MouseArgs args)
        {
            sender.EventMouseDrag?.Invoke(sender, args);
        }
        private void InvokeMouseScrollUpEvent(Prototype sender, MouseArgs args)
        {
            sender.EventScrollUp?.Invoke(sender, args);
        }
        private void InvokeMouseScrollDownEvent(Prototype sender, MouseArgs args)
        {
            sender.EventScrollDown?.Invoke(sender, args);
        }

        //keyboard input
        private void InvokeKeyPressEvent(Prototype sender, KeyArgs args)
        {
            sender.EventKeyPress?.Invoke(sender, args);
        }
        private void InvokeKeyReleaseEvent(Prototype sender, KeyArgs args)
        {
            sender.EventKeyRelease?.Invoke(sender, args);
        }
        private void InvokeTextInputEvent(Prototype sender, TextInputArgs args)
        {
            sender.EventTextInput?.Invoke(sender, args);
        }
    }
}