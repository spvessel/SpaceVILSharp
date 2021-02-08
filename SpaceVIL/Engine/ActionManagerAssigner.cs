using System;
using System.Collections.Generic;
using SpaceVIL.Core;

namespace SpaceVIL
{
    internal class ActionManagerAssigner
    {
        WindowLayout _layout;

        internal ActionManagerAssigner(WindowLayout layout)
        {
            _layout = layout;
        }

        internal void AssignActionsForHoveredItem(InputEventType action, IInputEventArgs args, Prototype hoveredItem,
                List<Prototype> itemPyramid, bool isOnlyHovered)
        {
            if (isOnlyHovered)
            {
                if (hoveredItem.IsDisabled())
                    return;

                _layout.SetEventTask(new EventTask()
                {
                    Item = hoveredItem,
                    Action = action,
                    Args = args
                });
            }
            else
            {
                GoThroughItemPyramid(itemPyramid, action, args);
            }
            _layout.ExecutePollActions();
        }

        internal void AssignActionsForSender(InputEventType action, IInputEventArgs args, Prototype sender,
                List<Prototype> itemPyramid, bool isPassUnder)
        {
            if (!sender.IsDisabled())
            {
                _layout.SetEventTask(new EventTask()
                {
                    Item = sender,
                    Action = action,
                    Args = args
                });
            }

            if (isPassUnder && sender.IsPassEvents(action))
            {
                if (itemPyramid != null)
                {
                    GoThroughItemPyramid(itemPyramid, action, args);
                }
            }
            _layout.ExecutePollActions();
        }

        internal void AssignActionsForItemPyramid(InputEventType action, IInputEventArgs args, Prototype sender,
                List<Prototype> itemPyramid)
        {
            if (sender.IsPassEvents(action))
            {
                if (itemPyramid != null)
                {
                    GoThroughItemPyramid(itemPyramid, action, args);
                }
            }
            _layout.ExecutePollActions();
        }

        private void GoThroughItemPyramid(List<Prototype> itemsList, InputEventType action, IInputEventArgs args)
        {
            Stack<Prototype> tmp = new Stack<Prototype>(itemsList);
            while (tmp.Count != 0)
            {
                Prototype item = tmp.Pop();

                if (item.IsDisabled())
                    continue;

                _layout.SetEventTask(new EventTask()
                {
                    Item = item,
                    Action = action,
                    Args = args
                });
                
                if (!item.IsPassEvents(action))
                    break;
            }
        }
    }
}