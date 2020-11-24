package com.spvessel.spacevil;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.nio.*;

import com.spvessel.spacevil.internal.Wrapper.*;

import com.spvessel.spacevil.Core.IDraggable;
import com.spvessel.spacevil.Core.IMovable;
import com.spvessel.spacevil.Core.IWindowAnchor;
import com.spvessel.spacevil.Flags.InputEventType;
import com.spvessel.spacevil.Flags.InputState;
import com.spvessel.spacevil.Flags.KeyMods;
import com.spvessel.spacevil.Flags.MouseButton;

final class MouseClickProcessor {

    private GlfwWrapper glfw = null;

    private CommonProcessor _commonProcessor;
    private long _doubleClickStartTime = 0;
    private boolean _firstClickOfDoubleClick = false;
    private Prototype _doubleClickItem = null;
    private final int _clickInterval = 500;
    private final float _accountingLength = 10.0f;

    MouseClickProcessor(CommonProcessor processor) {
        glfw = GlfwWrapper.get();
        _commonProcessor = processor;
    }

    private boolean isDoubleClick(Prototype item) {
        if (_firstClickOfDoubleClick) {
            if (_doubleClickItem != null && _doubleClickItem.equals(item)
                    && (System.nanoTime() - _doubleClickStartTime) / 1000000 < _clickInterval) {
                _firstClickOfDoubleClick = false;
                _doubleClickStartTime = 0;
                return true;
            } else {
                _doubleClickItem = item;
                _doubleClickStartTime = System.nanoTime();
            }
        } else {
            _doubleClickItem = item;
            _firstClickOfDoubleClick = true;
            _doubleClickStartTime = System.nanoTime();
        }
        return false;
    }

    void process(long wnd, int button, int action, int mods) {

        _commonProcessor.margs.button = MouseButton.getEnum(button);
        _commonProcessor.margs.state = InputState.getEnum(action);
        _commonProcessor.margs.mods = KeyMods.getEnums(mods);

        InputEventType mouseState;
        if (action == InputState.Press.getValue())
            mouseState = InputEventType.MousePress;
        else
            mouseState = InputEventType.MouseRelease;

        Prototype lastHovered = _commonProcessor.hoveredItem;
        if (lastHovered == null) {

            double[] pos = glfw.GetCursorPos(_commonProcessor.handler.getWindowId());
            int x = (int)pos[0];
            int y = (int)pos[1];

            _commonProcessor.getHoverPrototype((float)pos[0], (float)pos[1], mouseState);
            lastHovered = _commonProcessor.hoveredItem;
            _commonProcessor.margs.position.setPosition(x, y);
            _commonProcessor.ptrRelease.setPosition(x, y);
            _commonProcessor.ptrPress.setPosition(x, y);
            _commonProcessor.ptrClick.setPosition(x, y);
        }
        if (!_commonProcessor.getHoverPrototype(_commonProcessor.ptrRelease.getX(), _commonProcessor.ptrRelease.getY(),
                mouseState)) {
            lastHovered.setMousePressed(false);
            _commonProcessor.events.resetAllEvents();
            _commonProcessor.events.setEvent(InputEventType.MouseRelease);
            if (lastHovered instanceof IDraggable)
                lastHovered.eventMouseDrop.execute(lastHovered, _commonProcessor.margs);
            return;
        }

        if (action == InputState.Press.getValue() && _commonProcessor.handler.getCoreWindow().getLayout().getContainer()
                .getSides(_commonProcessor.ptrRelease.getX(), _commonProcessor.ptrRelease.getY()).size() != 0) {
            _commonProcessor.handler.getCoreWindow().getLayout().getContainer()
                    .saveLastFocus(_commonProcessor.focusedItem);
        }
        InputState state = InputState.getEnum(action);
        switch (state) {
            case Release:
                release(wnd, button, action, mods);
                break;

            case Press:
                press(wnd, button, action, mods);
                break;

            case Repeat:
                break;

            default:
                break;
        }
    }

    private void press(long wnd, int button, int action, int mods) {
        int[] framebufferSize = glfw.GetFramebufferSize(_commonProcessor.handler.getWindowId());
        _commonProcessor.wGlobal = framebufferSize[0];
        _commonProcessor.hGlobal = framebufferSize[1];

        int[] windowPos = glfw.GetWindowPos(_commonProcessor.handler.getWindowId());
        _commonProcessor.xGlobal = windowPos[0];
        _commonProcessor.yGlobal = windowPos[1];

        double[] cursorPos = glfw.GetCursorPos(_commonProcessor.handler.getWindowId());
        _commonProcessor.ptrClick.setX((int) cursorPos[0]);
        _commonProcessor.ptrClick.setY((int) cursorPos[1]);
        _commonProcessor.ptrPress.setX((int) cursorPos[0]);
        _commonProcessor.ptrPress.setY((int) cursorPos[1]);

        if (_commonProcessor.hoveredItem != null) {
            _commonProcessor.hoveredItem.setMousePressed(true);
            _commonProcessor.manager.assignActionsForHoveredItem(InputEventType.MousePress, _commonProcessor.margs,
                    _commonProcessor.hoveredItem, _commonProcessor.underHoveredItems, false);

            if (_commonProcessor.hoveredItem.isFocusable) {
                if (_commonProcessor.focusedItem == null) {
                    _commonProcessor.focusedItem = _commonProcessor.hoveredItem;
                    _commonProcessor.focusedItem.setFocused(true);
                } else if (!_commonProcessor.focusedItem.equals(_commonProcessor.hoveredItem)) {
                    _commonProcessor.focusedItem.setFocused(false);
                    _commonProcessor.focusedItem = _commonProcessor.hoveredItem;
                    _commonProcessor.focusedItem.setFocused(true);
                }
                _commonProcessor.underFocusedItems = new LinkedList<Prototype>(_commonProcessor.underHoveredItems);
                _commonProcessor.underFocusedItems.remove(_commonProcessor.focusedItem);
            } else {
                Deque<Prototype> underHoveredQueue = new ArrayDeque<>(_commonProcessor.underHoveredItems);
                while (!underHoveredQueue.isEmpty()) {
                    Prototype f = underHoveredQueue.pollLast();
                    if (f.equals(_commonProcessor.hoveredItem) && _commonProcessor.hoveredItem.isDisabled())
                        continue;
                    if (f.isFocusable) {
                        if (f instanceof IWindowAnchor)
                            _commonProcessor.handler.getCoreWindow().getLayout().getContainer()
                                    .saveLastFocus(_commonProcessor.focusedItem);
                        else {
                            _commonProcessor.focusedItem = f;
                            _commonProcessor.focusedItem.setFocused(true);
                            _commonProcessor.findUnderFocusedItems(_commonProcessor.focusedItem);
                        }
                        break;
                    }
                }
            }
        }
        _commonProcessor.events.resetAllEvents();
        _commonProcessor.events.setEvent(InputEventType.MousePress);
    }

    private void release(long wnd, int button, int action, int mods) {
        _commonProcessor.focusedItem.setMousePressed(false);

        _commonProcessor.rootContainer.restoreFocus();

        boolean isDoubleClick = isDoubleClick(_commonProcessor.hoveredItem);
        Deque<Prototype> underHoveredQueue = new ArrayDeque<>(_commonProcessor.underHoveredItems);

        while (!underHoveredQueue.isEmpty()) {
            Prototype item = underHoveredQueue.pollLast();
            if (item.isDisabled())
                continue;
            item.setMousePressed(false);
        }

        if (_commonProcessor.events.lastEvent().contains(InputEventType.WindowResize)
                || _commonProcessor.events.lastEvent().contains(InputEventType.WindowMove)) {
            _commonProcessor.events.resetAllEvents();
            _commonProcessor.events.setEvent(InputEventType.MouseRelease);
            return;
        }

        if (_commonProcessor.events.lastEvent().contains(InputEventType.MouseMove)) {

            if (_commonProcessor.draggableItem != null) {
                _commonProcessor.draggableItem.eventMouseDrop.execute(_commonProcessor.draggableItem,
                        _commonProcessor.margs);
            }

            if (_commonProcessor.hoveredItem instanceof IMovable) {
                _commonProcessor.hoveredItem.eventMouseClick.execute(_commonProcessor.hoveredItem,
                        _commonProcessor.margs);
            }

            if (!_commonProcessor.events.lastEvent().contains(InputEventType.MouseDrag)) {
                float len = getLengthBetweenTwoPixelPoints(_commonProcessor.ptrClick.getX(),
                        _commonProcessor.ptrClick.getY(), _commonProcessor.ptrRelease.getX(),
                        _commonProcessor.ptrRelease.getY());
                if (len > _accountingLength) {
                    _commonProcessor.events.resetAllEvents();
                    _commonProcessor.events.setEvent(InputEventType.MouseRelease);
                    return;
                }
            } else if (_commonProcessor.draggableItem != _commonProcessor.hoveredItem) {
                Prototype lastFocused = _commonProcessor.focusedItem;
                _commonProcessor.focusedItem = _commonProcessor.draggableItem;
                _commonProcessor.findUnderFocusedItems(_commonProcessor.draggableItem);
                _commonProcessor.focusedItem = lastFocused;

                _commonProcessor.manager.assignActionsForSender(InputEventType.MouseRelease, _commonProcessor.margs,
                        _commonProcessor.draggableItem, _commonProcessor.underFocusedItems, true);
                _commonProcessor.events.resetAllEvents();
                _commonProcessor.events.setEvent(InputEventType.MouseRelease);
                return;
            }
        }

        if (_commonProcessor.hoveredItem != null) {
            _commonProcessor.hoveredItem.setMousePressed(false);

            if (isDoubleClick) {
                _commonProcessor.manager.assignActionsForHoveredItem(InputEventType.MouseDoubleClick,
                        _commonProcessor.margs, _commonProcessor.hoveredItem, _commonProcessor.underHoveredItems,
                        false);
            } else {
                if (!_commonProcessor.events.lastEvent().contains(InputEventType.MouseDrag)) {
                    if (_commonProcessor.hoveredItem instanceof IMovable) {
                        _commonProcessor.hoveredItem.eventMouseClick.execute(_commonProcessor.hoveredItem,
                                _commonProcessor.margs);
                    } else {
                        _commonProcessor.manager.assignActionsForHoveredItem(InputEventType.MouseRelease,
                                _commonProcessor.margs, _commonProcessor.hoveredItem,
                                _commonProcessor.underHoveredItems, false);
                    }
                }
            }
        }

        _commonProcessor.events.resetAllEvents();
        _commonProcessor.events.setEvent(InputEventType.MouseRelease);
    }

    private float getLengthBetweenTwoPixelPoints(int x0, int y0, int x1, int y1) {
        return (float) Math.sqrt(Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2));
    }
}