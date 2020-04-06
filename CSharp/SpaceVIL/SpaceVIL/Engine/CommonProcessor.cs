using System;
using System.Collections.Generic;
using Glfw3;
using SpaceVIL.Common;
using SpaceVIL.Core;

namespace SpaceVIL
{
    internal class CommonProcessor
    {
        internal WindowProcessor WndProcessor;
        internal ToolTipItem Tooltip;
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
        internal Position PtrPress = new Position();
        internal Position PtrRelease = new Position();
        internal Position PtrClick = new Position();
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

        internal void InitProcessor(GLWHandler handler, ToolTipItem toolTip)
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
            UnderHoveredItems.Clear();

            List<IBaseItem> layout_box_of_items = new List<IBaseItem>();
            layout_box_of_items.Add(RootContainer);
            layout_box_of_items.AddRange(GetInnerItems(RootContainer, xpos, ypos));

            foreach (var item in ItemsLayoutBox.GetLayoutFloatItems(Guid))
            {
                if (!item.IsVisible() || !item.IsDrawable())
                    continue;
                layout_box_of_items.Add(item);

                Prototype leaf = item as Prototype;
                if (leaf != null)
                    layout_box_of_items.AddRange(GetInnerItems(leaf, xpos, ypos));
            }
            InputLocker = false;

            List<Prototype> queue = new List<Prototype>();

            foreach (var item in layout_box_of_items)
            {
                Prototype prototype = item as Prototype;
                if (prototype != null)
                {
                    if (prototype.GetHoverVerification(xpos, ypos))
                    {
                        queue.Add(prototype);
                    }
                    else
                    {
                        prototype.SetMouseHover(false);
                        IFloating floatItem = item as IFloating;
                        if (floatItem != null && action == InputEventType.MousePress)
                        {
                            if (floatItem.IsOutsideClickClosable())
                            {
                                ContextMenu cmToClose = (item as ContextMenu);
                                if (cmToClose != null)
                                {
                                    if (cmToClose.CloseDependencies(Margs))
                                        floatItem.Hide();
                                }
                                else
                                {
                                    floatItem.Hide(Margs);
                                }
                            }
                        }
                    }
                }
            }

            if (queue.Count > 0)
            {
                if (HoveredItem != null && (HoveredItem != queue[queue.Count - 1]))
                    Manager.AssignActionsForSender(InputEventType.MouseLeave, Margs, HoveredItem, UnderFocusedItems, false);

                HoveredItem = queue[queue.Count - 1];
                HoveredItem.SetMouseHover(true);
                CommonService.CurrentCursor = HoveredItem.GetCursor();
                Glfw.SetCursor(Handler.GetWindowId(), CommonService.CurrentCursor.GetCursor());

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

        private List<IBaseItem> GetInnerItems(Prototype root, float xpos, float ypos)
        {
            List<IBaseItem> list = new List<IBaseItem>();
            List<IBaseItem> rootItems = root.GetItems();

            foreach (IBaseItem item in rootItems)
            {
                if (!item.IsVisible() || !item.IsDrawable())
                    continue;
                Prototype leaf = item as Prototype;
                if (leaf != null)
                {
                    if (leaf.IsDisabled())
                        continue;
                    if (leaf.GetHoverVerification(xpos, ypos))
                        list.Add(item);
                    list.AddRange(GetInnerItems(leaf, xpos, ypos));
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