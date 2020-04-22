package com.spvessel.spacevil;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.nio.*;

import org.lwjgl.*;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.system.MemoryStack;

import com.spvessel.spacevil.Core.InterfaceDraggable;
import com.spvessel.spacevil.Core.InterfaceMovable;
import com.spvessel.spacevil.Core.InterfaceWindowAnchor;
import com.spvessel.spacevil.Flags.InputEventType;
import com.spvessel.spacevil.Flags.InputState;
import com.spvessel.spacevil.Flags.KeyMods;
import com.spvessel.spacevil.Flags.MouseButton;

final class MouseClickProcessor {

    private CommonProcessor _commonProcessor;
    private long _doubleClickStartTime = 0;
    private boolean _firstClickOfDoubleClick = false;
    private Prototype _doubleClickItem = null;
    private final int _clickInterval = 500;
    private final float _accountingLength = 10.0f;

    MouseClickProcessor(CommonProcessor processor) {
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
        if (action == InputState.PRESS.getValue())
            mouseState = InputEventType.MOUSE_PRESS;
        else
            mouseState = InputEventType.MOUSE_RELEASE;

        Prototype lastHovered = _commonProcessor.hoveredItem;
        if (lastHovered == null) {
            DoubleBuffer xPos = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer yPos = BufferUtils.createDoubleBuffer(1);
            glfwGetCursorPos(_commonProcessor.handler.getWindowId(), xPos, yPos);
            int x = (int) xPos.get(0);
            int y = (int) yPos.get(0);
            _commonProcessor.getHoverPrototype(x, y, mouseState);
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
            _commonProcessor.events.setEvent(InputEventType.MOUSE_RELEASE);
            if (lastHovered instanceof InterfaceDraggable)
                lastHovered.eventMouseDrop.execute(lastHovered, _commonProcessor.margs);
            return;
        }

        if (action == InputState.PRESS.getValue() && _commonProcessor.handler.getCoreWindow().getLayout().getContainer()
                .getSides(_commonProcessor.ptrRelease.getX(), _commonProcessor.ptrRelease.getY()).size() != 0) {
            _commonProcessor.handler.getCoreWindow().getLayout().getContainer()
                    .saveLastFocus(_commonProcessor.focusedItem);
        }

        switch (action) {
            case GLFW_RELEASE:
                release(wnd, button, action, mods);
                break;

            case GLFW_PRESS:
                press(wnd, button, action, mods);
                break;

            case GLFW_REPEAT:
                break;

            default:
                break;
        }
    }

    private void press(long wnd, int button, int action, int mods) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            glfwGetFramebufferSize(_commonProcessor.handler.getWindowId(), width, height);
            _commonProcessor.wGlobal = width.get(0);
            _commonProcessor.hGlobal = height.get(0);

            IntBuffer x = stack.mallocInt(1);
            IntBuffer y = stack.mallocInt(1);
            glfwGetWindowPos(_commonProcessor.handler.getWindowId(), x, y);
            _commonProcessor.xGlobal = x.get(0);
            _commonProcessor.yGlobal = y.get(0);
        }

        DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(_commonProcessor.handler.getWindowId(), xpos, ypos);
        _commonProcessor.ptrClick.setX((int) xpos.get(0));
        _commonProcessor.ptrClick.setY((int) ypos.get(0));
        _commonProcessor.ptrPress.setX((int) xpos.get(0));
        _commonProcessor.ptrPress.setY((int) ypos.get(0));

        if (_commonProcessor.hoveredItem != null) {
            _commonProcessor.hoveredItem.setMousePressed(true);
            _commonProcessor.manager.assignActionsForHoveredItem(InputEventType.MOUSE_PRESS, _commonProcessor.margs,
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
                        if (f instanceof InterfaceWindowAnchor)
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
        _commonProcessor.events.setEvent(InputEventType.MOUSE_PRESS);
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

        if (_commonProcessor.events.lastEvent().contains(InputEventType.WINDOW_RESIZE)
                || _commonProcessor.events.lastEvent().contains(InputEventType.WINDOW_MOVE)) {
            _commonProcessor.events.resetAllEvents();
            _commonProcessor.events.setEvent(InputEventType.MOUSE_RELEASE);
            return;
        }

        if (_commonProcessor.events.lastEvent().contains(InputEventType.MOUSE_MOVE)) {

            if (_commonProcessor.draggableItem != null) {
                _commonProcessor.draggableItem.eventMouseDrop.execute(_commonProcessor.draggableItem,
                        _commonProcessor.margs);
            }

            if (_commonProcessor.hoveredItem instanceof InterfaceMovable) {
                _commonProcessor.hoveredItem.eventMouseClick.execute(_commonProcessor.hoveredItem,
                        _commonProcessor.margs);
            }

            if (!_commonProcessor.events.lastEvent().contains(InputEventType.MOUSE_DRAG)) {
                float len = getLengthBetweenTwoPixelPoints(_commonProcessor.ptrClick.getX(),
                        _commonProcessor.ptrClick.getY(), _commonProcessor.ptrRelease.getX(),
                        _commonProcessor.ptrRelease.getY());
                if (len > _accountingLength) {
                    _commonProcessor.events.resetAllEvents();
                    _commonProcessor.events.setEvent(InputEventType.MOUSE_RELEASE);
                    return;
                }
            } else if (_commonProcessor.draggableItem != _commonProcessor.hoveredItem) {
                Prototype lastFocused = _commonProcessor.focusedItem;
                _commonProcessor.focusedItem = _commonProcessor.draggableItem;
                _commonProcessor.findUnderFocusedItems(_commonProcessor.draggableItem);
                _commonProcessor.focusedItem = lastFocused;

                _commonProcessor.manager.assignActionsForSender(InputEventType.MOUSE_RELEASE, _commonProcessor.margs,
                        _commonProcessor.draggableItem, _commonProcessor.underFocusedItems, true);
                _commonProcessor.events.resetAllEvents();
                _commonProcessor.events.setEvent(InputEventType.MOUSE_RELEASE);
                return;
            }
        }

        if (_commonProcessor.hoveredItem != null) {
            _commonProcessor.hoveredItem.setMousePressed(false);

            if (isDoubleClick) {
                _commonProcessor.manager.assignActionsForHoveredItem(InputEventType.MOUSE_DOUBLE_CLICK,
                        _commonProcessor.margs, _commonProcessor.hoveredItem, _commonProcessor.underHoveredItems,
                        false);
            } else {
                if (!_commonProcessor.events.lastEvent().contains(InputEventType.MOUSE_DRAG)) {
                    if (_commonProcessor.hoveredItem instanceof InterfaceMovable) {
                        _commonProcessor.hoveredItem.eventMouseClick.execute(_commonProcessor.hoveredItem,
                                _commonProcessor.margs);
                    } else {
                        _commonProcessor.manager.assignActionsForHoveredItem(InputEventType.MOUSE_RELEASE,
                                _commonProcessor.margs, _commonProcessor.hoveredItem,
                                _commonProcessor.underHoveredItems, false);
                    }
                }
            }
        }

        _commonProcessor.events.resetAllEvents();
        _commonProcessor.events.setEvent(InputEventType.MOUSE_RELEASE);
    }

    private float getLengthBetweenTwoPixelPoints(int x0, int y0, int x1, int y1) {
        return (float) Math.sqrt(Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2));
    }
}