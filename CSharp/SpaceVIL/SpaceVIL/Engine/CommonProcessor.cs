using System;
using System.Collections.Generic;
using System.Linq;
using Glfw3;
using SpaceVIL.Common;
using SpaceVIL.Core;

namespace SpaceVIL
{
    internal class CommonProcessor
    {
        internal WindowProcessor WndProcessor;
        internal ToolTip Tooltip;
        internal ActionManagerAssigner Manager;
        internal GLWHandler Handler;
        internal CoreWindow Window;
        internal WindowLayout Layout;
        internal WContainer RootContainer;
        internal Guid Guid;
        internal MouseArgs Margs;
        internal List<Prototype> UnderHoveredItems;
        internal List<Prototype> UnderFocusedItems;
        internal Prototype DraggableItem = null;
        internal Prototype HoveredItem = null;
        internal Prototype FocusedItem = null;
        internal int WGlobal = 0;
        internal int HGlobal = 0;
        internal int XGlobal = 0;
        internal int YGlobal = 0;
        internal bool InputLocker = false;
        internal Pointer PtrPress = new Pointer();
        internal Pointer PtrRelease = new Pointer();
        internal Pointer PtrClick = new Pointer();
        internal InputDeviceEvent Events;

        internal CommonProcessor()
        {
            this.Events = new InputDeviceEvent();
            Margs = new MouseArgs();
            Margs.Clear();
            UnderFocusedItems = new List<Prototype>();
            UnderHoveredItems = new List<Prototype>();
            WndProcessor = new WindowProcessor(this);
        }

        internal void InitProcessor(GLWHandler handler, ToolTip toolTip)
        {
            this.Handler = handler;
            this.Tooltip = toolTip;
            Window = handler.GetCoreWindow();
            Layout = Window.GetLayout();
            RootContainer = Layout.GetContainer();
            Guid = Window.GetWindowGuid();
            this.Manager = new ActionManagerAssigner(Layout);
        }

        internal bool GetHoverPrototype(float xpos, float ypos, InputEventType action)
        {
            InputLocker = true;
            List<Prototype> queue = new List<Prototype>();
            UnderHoveredItems.Clear();

            List<IBaseItem> layout_box_of_items = new List<IBaseItem>();
            layout_box_of_items.Add(Handler.GetCoreWindow().GetLayout().GetContainer());
            layout_box_of_items.AddRange(GetInnerItems(Handler.GetCoreWindow().GetLayout().GetContainer()));

            foreach (var item in ItemsLayoutBox.GetLayoutFloatItems(Handler.GetCoreWindow().GetWindowGuid()))
            {
                if (!item.IsVisible() || !item.IsDrawable())
                    continue;
                Prototype leaf = item as Prototype;
                if (leaf != null)
                {
                    if (leaf.IsDisabled())
                        continue;
                    layout_box_of_items.Add(item);
                    layout_box_of_items.AddRange(GetInnerItems(leaf));
                }
            }
            InputLocker = false;
            foreach (var item in layout_box_of_items)
            {
                Prototype tmp = item as Prototype;
                if (tmp != null)
                {
                    if (!tmp.IsVisible() || !tmp.IsDrawable())
                        continue;
                    if (tmp.GetHoverVerification(xpos, ypos))
                    {
                        queue.Add(tmp);
                    }
                    else
                    {
                        tmp.SetMouseHover(false);
                        IFloating float_item = item as IFloating;
                        if (float_item != null && action == InputEventType.MousePress)
                        {
                            if (float_item.IsOutsideClickClosable())
                            {
                                ContextMenu to_close = (item as ContextMenu);
                                if (to_close != null)
                                {
                                    if (to_close.CloseDependencies(Margs))
                                        float_item.Hide();
                                }
                                else
                                {
                                    float_item.Hide();
                                }
                            }
                        }
                    }
                }
            }

            if (queue.Count > 0)
            {
                if (HoveredItem != null && (HoveredItem != queue.Last()))
                    Manager.AssignActionsForSender(InputEventType.MouseLeave, Margs, HoveredItem, UnderFocusedItems, false);

                HoveredItem = queue.Last();
                HoveredItem.SetMouseHover(true);
                Glfw.SetCursor(Handler.GetWindowId(), HoveredItem.GetCursor().GetCursor());

                if (Handler.GetCoreWindow().IsBorderHidden && Handler.GetCoreWindow().IsResizable && !Handler.GetCoreWindow().IsMaximized)
                {
                    int handlerContainerWidth = Handler.GetCoreWindow().GetLayout().GetContainer().GetWidth();
                    int handlerContainerHeight = Handler.GetCoreWindow().GetLayout().GetContainer().GetHeight();

                    bool cursorNearLeftTop = (xpos <= SpaceVILConstants.BorderCursorTolerance && ypos <= SpaceVILConstants.BorderCursorTolerance);
                    bool cursorNearLeftBottom = (ypos >= handlerContainerHeight - SpaceVILConstants.BorderCursorTolerance && xpos <= SpaceVILConstants.BorderCursorTolerance);
                    bool cursorNearRightTop = (xpos >= handlerContainerWidth - SpaceVILConstants.BorderCursorTolerance && ypos <= SpaceVILConstants.BorderCursorTolerance);
                    bool cursorNearRightBottom = (xpos >= handlerContainerWidth - SpaceVILConstants.BorderCursorTolerance && ypos >= handlerContainerHeight - SpaceVILConstants.BorderCursorTolerance);

                    if (cursorNearRightTop || cursorNearRightBottom || cursorNearLeftBottom || cursorNearLeftTop)
                    {
                        Handler.SetCursorType(EmbeddedCursor.Crosshair);
                    }
                    else
                    {
                        if (xpos >= handlerContainerWidth - SpaceVILConstants.BorderCursorTolerance || xpos < SpaceVILConstants.BorderCursorTolerance)
                            Handler.SetCursorType(EmbeddedCursor.ResizeX);

                        if (ypos >= handlerContainerHeight - SpaceVILConstants.BorderCursorTolerance || ypos < SpaceVILConstants.BorderCursorTolerance)
                            Handler.SetCursorType(EmbeddedCursor.ResizeY);
                    }
                }

                UnderHoveredItems = queue;
                Stack<Prototype> tmp = new Stack<Prototype>(UnderHoveredItems);
                while (tmp.Count > 0)
                {
                    Prototype item = tmp.Pop();
                    if (item.Equals(HoveredItem) && HoveredItem.IsDisabled())
                        continue;
                    item.SetMouseHover(true);
                    if (!item.IsPassEvents(InputEventType.MouseHover))
                        break;
                }

                Manager.AssignActionsForHoveredItem(InputEventType.MouseHover, Margs, HoveredItem, UnderHoveredItems, false);
                return true;
            }
            else
                return false;
        }

        private List<IBaseItem> GetInnerItems(Prototype root)
        {
            List<IBaseItem> list = new List<IBaseItem>();
            List<IBaseItem> root_items = root.GetItems();
            foreach (var item in root_items)
            {
                if (!item.IsVisible() || !item.IsDrawable())
                    continue;

                Prototype leaf = item as Prototype;
                if (leaf != null)
                {
                    if (leaf.IsDisabled())
                        continue;
                    list.Add(item);
                    list.AddRange(GetInnerItems(leaf));
                }
            }
            return list;
        }

        internal Prototype IsInListHoveredItems<T>()
        {
            Prototype wanted = null;
            foreach (var item in UnderHoveredItems)
            {
                if (item is T)
                {
                    wanted = item;
                    if (wanted is IFloating)
                        return wanted;
                }
            }
            return wanted;
        }

        internal void FindUnderFocusedItems(Prototype item)
        {
            Stack<Prototype> queue = new Stack<Prototype>();
            if (item == RootContainer)
            {
                UnderFocusedItems = null;
                return;
            }
            Prototype parent = item.GetParent();
            while (parent != null)
            {
                queue.Push(parent);
                parent = parent.GetParent();
            }
            UnderFocusedItems = new List<Prototype>(queue);
            UnderFocusedItems.Remove(FocusedItem);
        }

        internal void SetFocusedItem(Prototype item)
        {
            if (item == null)
            {
                FocusedItem = null;
                return;
            }
            if (FocusedItem != null)
            {
                if (FocusedItem.Equals(item))
                    return;
                FocusedItem.SetFocused(false);
            }
            FocusedItem = item;
            FocusedItem.SetFocused(true);
            FindUnderFocusedItems(item);
        }

        internal void ResetFocus()
        {
            if (FocusedItem != null)
                FocusedItem.SetFocused(false);
            FocusedItem = RootContainer;
            FocusedItem.SetFocused(true);
            UnderFocusedItems?.Clear();
        }

        internal void ResetItems()
        {
            ResetFocus();
            if (HoveredItem != null)
                HoveredItem.SetMouseHover(false);
            HoveredItem = null;
            UnderHoveredItems.Clear();
        }
    }
}