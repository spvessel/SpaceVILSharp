using System;
using System.Collections.Generic;
using System.Diagnostics;
using Glfw3;
using SpaceVIL.Core;

namespace SpaceVIL
{
    internal class MouseClickProcessor
    {
        CommonProcessor _commonProcessor;
        private readonly float _accountingLength = 10.0f;
        private Stopwatch _doubleClickTimer = new Stopwatch();
        private bool _doubleClickHappen = false;
        private Prototype _doubleClickItem = null;
        private readonly int _clickInterval = 500;

        internal MouseClickProcessor(CommonProcessor commonProcessor)
        {
            _commonProcessor = commonProcessor;
        }

        private bool IsDoubleClick(Prototype item)
        {
            if (_doubleClickTimer.IsRunning)
            {
                _doubleClickTimer.Stop();
                if (_doubleClickItem != null && _doubleClickItem.Equals(item) && _doubleClickTimer.ElapsedMilliseconds < _clickInterval)
                {
                    _doubleClickHappen = true;
                    return true;
                }
                else
                {
                    _doubleClickItem = item;
                    _doubleClickHappen = false;
                    _doubleClickTimer.Restart();
                }
            }
            else
            {
                _doubleClickItem = item;
                _doubleClickHappen = false;
                _doubleClickTimer.Restart();
            }
            return false;
        }

        internal void Process(Int64 window, MouseButton button, InputState state, KeyMods mods)
        {
            _commonProcessor.Margs.Button = button;
            _commonProcessor.Margs.State = state;
            _commonProcessor.Margs.Mods = mods;

            InputEventType mouseState;
            if (state == InputState.Press)
                mouseState = InputEventType.MousePress;
            else
                mouseState = InputEventType.MouseRelease;

            Prototype lastHovered = _commonProcessor.HoveredItem;

            if (lastHovered == null)
            {
                double x, y;
                Glfw.GetCursorPos(_commonProcessor.Handler.GetWindowId(), out x, out y);
                _commonProcessor.GetHoverPrototype((int)x, (int)y, mouseState);
                lastHovered = _commonProcessor.HoveredItem;
                _commonProcessor.Margs.Position.SetPosition((int)x, (int)y);
                _commonProcessor.PtrRelease.SetPosition((int)x, (int)y);
                _commonProcessor.PtrPress.SetPosition((int)x, (int)y);
                _commonProcessor.PtrClick.SetPosition((int)x, (int)y);
            }

            if (!_commonProcessor.GetHoverPrototype(_commonProcessor.PtrRelease.GetX(),
                    _commonProcessor.PtrRelease.GetY(), mouseState))
            {
                lastHovered.SetMousePressed(false);
                _commonProcessor.Events.ResetAllEvents();
                _commonProcessor.Events.SetEvent(InputEventType.MouseRelease);
                if (lastHovered is IDraggable)
                    lastHovered.EventMouseDrop?.Invoke(lastHovered, _commonProcessor.Margs);
                return;
            }

            if (state == InputState.Press && _commonProcessor.RootContainer.GetSides(
                    _commonProcessor.PtrRelease.GetX(), _commonProcessor.PtrRelease.GetY()) != 0)
            {
                _commonProcessor.RootContainer.SaveLastFocus(_commonProcessor.FocusedItem);
            }

            switch (state)
            {
                case InputState.Press:
                    Press(window, button, state, mods);
                    break;
                case InputState.Release:
                    Release(window, button, state, mods);
                    break;
                case InputState.Repeat:
                    break;
                default:
                    break;
            }
        }

        internal void Press(Int64 window, MouseButton button, InputState state, KeyMods mods)
        {
            Glfw.GetFramebufferSize(_commonProcessor.Handler.GetWindowId(), out _commonProcessor.WGlobal, out _commonProcessor.HGlobal);
            _commonProcessor.XGlobal = _commonProcessor.Handler.GetPointer().GetX();
            _commonProcessor.YGlobal = _commonProcessor.Handler.GetPointer().GetY();

            double xpos, ypos;
            Glfw.GetCursorPos(_commonProcessor.Handler.GetWindowId(), out xpos, out ypos);
            _commonProcessor.PtrClick.SetX((int)xpos);
            _commonProcessor.PtrClick.SetY((int)ypos);
            _commonProcessor.PtrPress.SetX((int)xpos);
            _commonProcessor.PtrPress.SetY((int)ypos);

            if (_commonProcessor.HoveredItem != null)
            {
                _commonProcessor.HoveredItem.SetMousePressed(true);
                _commonProcessor.Manager.AssignActionsForHoveredItem(InputEventType.MousePress, _commonProcessor.Margs,
                   _commonProcessor.HoveredItem, _commonProcessor.UnderHoveredItems, false);
                if (_commonProcessor.HoveredItem.IsFocusable)
                {
                    if (_commonProcessor.FocusedItem == null)
                    {
                        _commonProcessor.FocusedItem = _commonProcessor.HoveredItem;
                        _commonProcessor.FocusedItem.SetFocused(true);
                    }
                    else if (!_commonProcessor.FocusedItem.Equals(_commonProcessor.HoveredItem))
                    {
                        _commonProcessor.FocusedItem.SetFocused(false);
                        _commonProcessor.FocusedItem = _commonProcessor.HoveredItem;
                        _commonProcessor.FocusedItem.SetFocused(true);
                    }
                    _commonProcessor.UnderFocusedItems = new List<Prototype>(_commonProcessor.UnderHoveredItems);
                    _commonProcessor.UnderFocusedItems.Remove(_commonProcessor.FocusedItem);
                }
                else
                {
                    Stack<Prototype> underHoveredStack = new Stack<Prototype>(_commonProcessor.UnderHoveredItems);
                    while (underHoveredStack.Count > 0)
                    {
                        Prototype f = underHoveredStack.Pop();
                        if (f.Equals(_commonProcessor.HoveredItem) && _commonProcessor.HoveredItem.IsDisabled())
                            continue;

                        if (f.IsFocusable)
                        {
                            if (f is IWindowAnchor)
                                _commonProcessor.RootContainer.SaveLastFocus(_commonProcessor.FocusedItem);
                            else
                            {
                                _commonProcessor.FocusedItem = f;
                                _commonProcessor.FocusedItem.SetFocused(true);
                                _commonProcessor.FindUnderFocusedItems(_commonProcessor.FocusedItem);
                            }
                            break;
                        }
                    }
                }
            }
            _commonProcessor.Events.ResetAllEvents();
            _commonProcessor.Events.SetEvent(InputEventType.MousePress);
        }

        internal void Release(Int64 window, MouseButton button, InputState state, KeyMods mods)
        {
            _commonProcessor.FocusedItem.SetMousePressed(false);

            _commonProcessor.RootContainer.RestoreFocus();
            bool isDoubleClick = IsDoubleClick(_commonProcessor.HoveredItem);
            Queue<Prototype> underHoveredList = new Queue<Prototype>(_commonProcessor.UnderHoveredItems);

            while (underHoveredList.Count > 0)
            {
                Prototype item = underHoveredList.Dequeue();
                if (item.IsDisabled())
                    continue;
                item.SetMousePressed(false);
            }

            if (_commonProcessor.Events.LastEvent().HasFlag(InputEventType.WindowResize)
                || _commonProcessor.Events.LastEvent().HasFlag(InputEventType.WindowMove))
            {
                _commonProcessor.Events.ResetAllEvents();
                _commonProcessor.Events.SetEvent(InputEventType.MouseRelease);
                return;
            }

            if (_commonProcessor.Events.LastEvent().HasFlag(InputEventType.MouseMove))
            {
                if (_commonProcessor.DraggableItem != null)
                {
                    _commonProcessor.DraggableItem.EventMouseDrop?.Invoke(_commonProcessor.DraggableItem, _commonProcessor.Margs);
                }

                if (_commonProcessor.HoveredItem is IMovable)
                {
                    _commonProcessor.HoveredItem.EventMouseClick.Invoke(_commonProcessor.HoveredItem, _commonProcessor.Margs);
                }

                if (!_commonProcessor.Events.LastEvent().HasFlag(InputEventType.MouseDrag))
                {
                    float len = GetLengthBetweenTwoPixelPoints(_commonProcessor.PtrClick.GetX(),
                            _commonProcessor.PtrClick.GetY(), _commonProcessor.PtrRelease.GetX(),
                            _commonProcessor.PtrRelease.GetY());
                    if (len > _accountingLength)
                    {
                        _commonProcessor.Events.ResetAllEvents();
                        _commonProcessor.Events.SetEvent(InputEventType.MouseRelease);

                        return;
                    }
                }
                else if (_commonProcessor.DraggableItem != _commonProcessor.HoveredItem)
                {
                    Prototype lastFocused = _commonProcessor.FocusedItem;
                    _commonProcessor.FocusedItem = _commonProcessor.DraggableItem;
                    _commonProcessor.FindUnderFocusedItems(_commonProcessor.DraggableItem);
                    _commonProcessor.FocusedItem = lastFocused;
                    _commonProcessor.Manager.AssignActionsForSender(InputEventType.MouseRelease, _commonProcessor.Margs,
                       _commonProcessor.DraggableItem, _commonProcessor.UnderFocusedItems, true);
                    _commonProcessor.Events.ResetAllEvents();
                    _commonProcessor.Events.SetEvent(InputEventType.MouseRelease);

                    return;
                }
            }

            if (_commonProcessor.HoveredItem != null)
            {
                _commonProcessor.HoveredItem.SetMousePressed(false);
                if (isDoubleClick)
                {
                    _commonProcessor.Manager.AssignActionsForHoveredItem(InputEventType.MouseDoubleClick,
                       _commonProcessor.Margs, _commonProcessor.HoveredItem, _commonProcessor.UnderHoveredItems, false);
                }
                else
                {
                    if (!_commonProcessor.Events.LastEvent().HasFlag(InputEventType.MouseDrag))
                    {
                        if (_commonProcessor.HoveredItem is IMovable)
                        {
                            _commonProcessor.HoveredItem.EventMouseClick.Invoke(_commonProcessor.HoveredItem, _commonProcessor.Margs);
                        }
                        else
                        {
                            _commonProcessor.Manager.AssignActionsForHoveredItem(InputEventType.MouseRelease,
                                _commonProcessor.Margs, _commonProcessor.HoveredItem, _commonProcessor.UnderHoveredItems, false);
                        }
                    }
                }
            }

            _commonProcessor.Events.ResetAllEvents();
            _commonProcessor.Events.SetEvent(InputEventType.MouseRelease);
        }

        private float GetLengthBetweenTwoPixelPoints(int x0, int y0, int x1, int y1)
        {
            return (float)Math.Sqrt(Math.Pow(x1 - x0, 2) + Math.Pow(y1 - y0, 2));
        }
    }
}
