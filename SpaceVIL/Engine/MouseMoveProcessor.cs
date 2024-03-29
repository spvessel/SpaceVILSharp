using System;
using System.Collections.Generic;
using System.Threading;
using Glfw3;
using SpaceVIL.Common;
using SpaceVIL.Core;

namespace SpaceVIL
{
    internal class MouseMoveProcessor
    {
        CommonProcessor _commonProcessor;
        internal MouseMoveProcessor(CommonProcessor commonProcessor)
        {
            _commonProcessor = commonProcessor;
        }

        internal void Process(Int64 wnd, double xpos, double ypos, Scale scale)
        {
            _commonProcessor.PtrRelease.SetX((int)xpos);
            _commonProcessor.PtrRelease.SetY((int)ypos);
            _commonProcessor.Margs.Position.SetPosition((int)xpos, (int)ypos);
            if (_commonProcessor.Events.LastEvent().HasFlag(InputEventType.MousePress))
            {
                if (_commonProcessor.Window.IsBorderHidden && _commonProcessor.Window.IsResizable)
                {
                    int w = _commonProcessor.Window.GetWidth();
                    int h = _commonProcessor.Window.GetHeight();
                    int xHandler = _commonProcessor.Handler.GetPointer().GetX();
                    int yHandler = _commonProcessor.Handler.GetPointer().GetY();
                    int xRelease = _commonProcessor.PtrRelease.GetX();
                    int yRelease = _commonProcessor.PtrRelease.GetY();
                    int xPress = _commonProcessor.PtrPress.GetX();
                    int yPress = _commonProcessor.PtrPress.GetY();

                    Side handlerContainerSides = _commonProcessor.RootContainer.GetSides();

                    if (handlerContainerSides.HasFlag(Side.Left))
                    {
                        if (!(_commonProcessor.Window.GetMinWidth() == _commonProcessor.Window.GetWidth()
                            && (_commonProcessor.PtrRelease.GetX() - _commonProcessor.PtrPress.GetX()) >= 0))
                        {
                            int x5 = xHandler - _commonProcessor.XGlobal + (int)xpos - SpaceVILConstants.BorderCursorTolerance;
                            xHandler = _commonProcessor.XGlobal + x5;
                            w = _commonProcessor.WGlobal - x5;
                        }
                    }
                    if (handlerContainerSides.HasFlag(Side.Right))
                    {
                        if (!(_commonProcessor.PtrRelease.GetX() < _commonProcessor.Window.GetMinWidth()
                            && _commonProcessor.Window.GetWidth() == _commonProcessor.Window.GetMinWidth()))
                        {
                            w = xRelease;
                        }
                        _commonProcessor.PtrPress.SetX(xRelease);
                    }
                    if (handlerContainerSides.HasFlag(Side.Top))
                    {
                        if (!(_commonProcessor.Window.GetMinHeight() == _commonProcessor.Window.GetHeight()
                            && (_commonProcessor.PtrRelease.GetY() - _commonProcessor.PtrPress.GetY()) >= 0))
                        {
                            if (CommonService.GetOSType() == OSType.Mac)
                            {
                                h -= yRelease - yPress;
                                yHandler = (_commonProcessor.HGlobal - h) + _commonProcessor.YGlobal;
                            }
                            else
                            {
                                int y5 = yHandler - _commonProcessor.YGlobal + (int)ypos - SpaceVILConstants.BorderCursorTolerance;
                                yHandler = _commonProcessor.YGlobal + y5;
                                h = _commonProcessor.HGlobal - y5;
                            }
                        }
                    }
                    if (handlerContainerSides.HasFlag(Side.Bottom))
                    {
                        if (!(_commonProcessor.PtrRelease.GetY() < _commonProcessor.Window.GetMinHeight()
                            && _commonProcessor.Window.GetHeight() == _commonProcessor.Window.GetMinHeight()))
                        {
                            if (CommonService.GetOSType() == OSType.Mac)
                                yHandler = _commonProcessor.YGlobal;
                            h = yRelease;
                            _commonProcessor.PtrPress.SetY(yRelease);
                        }
                    }
                    if (handlerContainerSides != 0 && !_commonProcessor.Window.IsMaximized)
                    {
                        if (CommonService.GetOSType() == OSType.Mac)
                        {
                            _commonProcessor.WndProcessor.SetWindowSize(w, h, new Scale());
                            if (handlerContainerSides.HasFlag(Side.Left) && handlerContainerSides.HasFlag(Side.Top))
                            {
                                _commonProcessor.WndProcessor.SetWindowPos(xHandler, (_commonProcessor.HGlobal - h) + _commonProcessor.YGlobal);
                            }
                            else if (handlerContainerSides.HasFlag(Side.Left) || handlerContainerSides.HasFlag(Side.Bottom)
                                || handlerContainerSides.HasFlag(Side.Top))
                            {
                                _commonProcessor.WndProcessor.SetWindowPos(xHandler, yHandler);
                                _commonProcessor.Handler.GetPointer().SetY(yHandler);//???
                            }
                        }
                        else
                        {
                            bool flagLT = false;
                            if (handlerContainerSides.HasFlag(Side.Left))
                            {
                                flagLT = true;
                                w = (int)(w / scale.GetXScale());
                            }
                            if (handlerContainerSides.HasFlag(Side.Top))
                            {
                                flagLT = true;
                                h = (int)(h / scale.GetYScale());
                            }
                            if (flagLT)
                            {
                                _commonProcessor.WndProcessor.SetWindowPos(xHandler, yHandler);
                            }
                            _commonProcessor.WndProcessor.SetWindowSize(w, h, scale);
                        }
                    }
                }
                if (_commonProcessor.RootContainer.GetSides() == 0)
                {
                    int xClick = _commonProcessor.PtrClick.GetX();
                    int yClick = _commonProcessor.PtrClick.GetY();
                    _commonProcessor.DraggableItem = _commonProcessor.IsInListHoveredItems<IDraggable>();
                    Prototype anchor = _commonProcessor.IsInListHoveredItems<IWindowAnchor>();
                    if (_commonProcessor.DraggableItem != null
                        && _commonProcessor.DraggableItem.Equals(_commonProcessor.HoveredItem)
                        )
                    {
                        _commonProcessor.Events.SetEvent(InputEventType.MouseDrag);
                        _commonProcessor.DraggableItem.EventMouseDrag?.Invoke(_commonProcessor.HoveredItem, _commonProcessor.Margs);
                    }
                    else if (anchor != null && !(_commonProcessor.HoveredItem is ButtonCore) && !_commonProcessor.Window.IsMaximized)
                    {
                        double x_pos, y_pos;
                        Glfw.GetCursorPos(_commonProcessor.Handler.GetWindowId(), out x_pos, out y_pos);
                        int deltaX = (int)x_pos - xClick;
                        int deltaY = (int)y_pos - yClick;
                        int x, y;
                        Glfw.GetWindowPos(_commonProcessor.Handler.GetWindowId(), out x, out y);
                        _commonProcessor.WndProcessor.SetWindowPos(x + deltaX, y + deltaY);
                    }
                }

                if (_commonProcessor.HoveredItem != null
                    && !_commonProcessor.HoveredItem.GetHoverVerification((float)xpos, (float)ypos))
                {
                    _commonProcessor.HoveredItem.SetMouseHover(false);
                    _commonProcessor.Manager.AssignActionsForSender(InputEventType.MouseLeave, _commonProcessor.Margs,
                        _commonProcessor.HoveredItem, _commonProcessor.UnderFocusedItems, false);
                }
            }
            else
            {
                _commonProcessor.PtrPress.SetX(_commonProcessor.PtrRelease.GetX());
                _commonProcessor.PtrPress.SetY(_commonProcessor.PtrRelease.GetY());

                Prototype lastHovered = _commonProcessor.HoveredItem;
                List<Prototype> tmpList = new List<Prototype>(_commonProcessor.UnderHoveredItems);

                if (_commonProcessor.GetHoverPrototype(_commonProcessor.PtrRelease.GetX(), _commonProcessor.PtrRelease.GetY(),
                    InputEventType.MouseMove))
                {
                    if (_commonProcessor.HoveredItem != null
                        && !(String.Empty.Equals(_commonProcessor.HoveredItem.GetToolTip())))
                    {
                        _commonProcessor.Tooltip.InitTimer(true);
                    }
                    Prototype popup = _commonProcessor.IsInListHoveredItems<PopUpMessage>();
                    if (popup != null)
                    {
                        (popup as PopUpMessage).HoldSelf(true);
                    }

                    if (lastHovered != null)
                    {
                        if (!_commonProcessor.UnderHoveredItems.Contains(lastHovered))
                            lastHovered.SetMouseHover(false);
                        List<Prototype> uniqueList = GetUniqueList(tmpList, _commonProcessor.UnderHoveredItems);
                        foreach (Prototype item in uniqueList)
                        {
                            item.SetMouseHover(false);
                        }
                    }
                }
            }

            if (_commonProcessor.HoveredItem is IMovable)
            {
                _commonProcessor.Events.SetEvent(InputEventType.MouseMove);
                _commonProcessor.HoveredItem.EventMouseMove?.Invoke(_commonProcessor.HoveredItem, _commonProcessor.Margs);
            }
        }

        private List<T> GetUniqueList<T>(List<T> firstList, List<T> secondList)
        {
            HashSet<T> hashset = new HashSet<T>(secondList);
            List<T> result = new List<T>();
            foreach (T item in firstList)
            {
                if (!hashset.Contains(item))
                    result.Add(item);
            }
            return result;
        }
    }
}