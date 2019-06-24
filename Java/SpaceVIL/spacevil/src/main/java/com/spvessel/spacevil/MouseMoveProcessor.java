package com.spvessel.spacevil;

import java.util.List;
import java.nio.*;

import org.lwjgl.*;
import static org.lwjgl.glfw.GLFW.*;

import com.spvessel.spacevil.Common.CommonService;
import com.spvessel.spacevil.Core.InterfaceDraggable;
import com.spvessel.spacevil.Core.InterfaceWindowAnchor;
import com.spvessel.spacevil.Flags.InputEventType;
import com.spvessel.spacevil.Flags.OSType;
import com.spvessel.spacevil.Flags.Side;

final class MouseMoveProcessor {

    private CommonProcessor _commonProcessor;

    MouseMoveProcessor(CommonProcessor processor) {
        _commonProcessor = processor;
    }

    void process(long wnd, double xpos, double ypos) {
        _commonProcessor.ptrRelease.setX((int) xpos);
        _commonProcessor.ptrRelease.setY((int) ypos);
        _commonProcessor.margs.position.setPosition((float) xpos, (float) ypos);
        if (_commonProcessor.events.lastEvent().contains(InputEventType.MOUSE_PRESS)) {
            if (_commonProcessor.window.isBorderHidden && _commonProcessor.window.isResizable) {
                int w = _commonProcessor.window.getWidth();
                int h = _commonProcessor.window.getHeight();
                int xHandler = _commonProcessor.handler.getPointer().getX();
                int yHandler = _commonProcessor.handler.getPointer().getY();
                int xRelease = _commonProcessor.ptrRelease.getX();
                int yRelease = _commonProcessor.ptrRelease.getY();
                int xPress = _commonProcessor.ptrPress.getX();
                int yPress = _commonProcessor.ptrPress.getY();
                List<Side> handlerContainerSides = _commonProcessor.rootContainer.getSides();
                if (handlerContainerSides.contains(Side.LEFT)) {
                    if (!(_commonProcessor.window.getMinWidth() == _commonProcessor.window.getWidth()
                            && (xRelease - xPress) >= 0)) {
                        int x5 = xHandler - _commonProcessor.xGlobal + (int) xpos
                                - SpaceVILConstants.borderCursorTolerance;
                        xHandler = _commonProcessor.xGlobal + x5;
                        w = _commonProcessor.wGlobal - x5;
                    }
                }
                if (handlerContainerSides.contains(Side.RIGHT)) {
                    if (!(xRelease < _commonProcessor.window.getMinWidth()
                            && _commonProcessor.window.getWidth() == _commonProcessor.window.getMinWidth())) {
                        w = xRelease;
                    }
                    _commonProcessor.ptrPress.setX(xRelease);
                }
                if (handlerContainerSides.contains(Side.TOP)) {
                    if (!(_commonProcessor.window.getMinHeight() == _commonProcessor.window.getHeight()
                            && (yRelease - yPress) >= 0)) {

                        if (CommonService.getOSType() == OSType.MAC) {
                            h -= yRelease - yPress;
                            yHandler = (_commonProcessor.hGlobal - h) + _commonProcessor.yGlobal;
                        } else {
                            int y5 = yHandler - _commonProcessor.yGlobal + (int) ypos
                                    - SpaceVILConstants.borderCursorTolerance;
                            yHandler = _commonProcessor.yGlobal + y5;
                            h = _commonProcessor.hGlobal - y5;
                        }
                    }
                }
                if (handlerContainerSides.contains(Side.BOTTOM)) {
                    if (!(yRelease < _commonProcessor.window.getMinHeight()
                            && _commonProcessor.window.getHeight() == _commonProcessor.window.getMinHeight())) {

                        if (CommonService.getOSType() == OSType.MAC)
                            yHandler = _commonProcessor.yGlobal;
                        h = yRelease;
                        _commonProcessor.ptrPress.setY(yRelease);
                    }
                }
                if (handlerContainerSides.size() != 0 && !_commonProcessor.window.isMaximized) {
                    if (CommonService.getOSType() == OSType.MAC) {
                        _commonProcessor.wndProcessor.setWindowSize(w, h);
                        if (handlerContainerSides.contains(Side.LEFT) && handlerContainerSides.contains(Side.TOP)) {
                            _commonProcessor.wndProcessor.setWindowPos(xHandler,
                                    (_commonProcessor.hGlobal - h) + _commonProcessor.yGlobal);
                        } else if (handlerContainerSides.contains(Side.LEFT)
                                || handlerContainerSides.contains(Side.BOTTOM)
                                || handlerContainerSides.contains(Side.TOP)) {
                            _commonProcessor.wndProcessor.setWindowPos(xHandler, yHandler);
                            _commonProcessor.handler.getPointer().setY(yHandler);
                        }
                    } else {
                        if (handlerContainerSides.contains(Side.LEFT) || handlerContainerSides.contains(Side.TOP))
                            _commonProcessor.wndProcessor.setWindowPos(xHandler, yHandler);
                        _commonProcessor.wndProcessor.setWindowSize(w, h);
                    }
                }
            }

            if (_commonProcessor.rootContainer.getSides().size() == 0) {
                int xClick = _commonProcessor.ptrClick.getX();
                int yClick = _commonProcessor.ptrClick.getY();
                _commonProcessor.draggableItem = _commonProcessor.isInListHoveredItems(InterfaceDraggable.class);
                Prototype anchor = _commonProcessor.isInListHoveredItems(InterfaceWindowAnchor.class);
                if (_commonProcessor.draggableItem != null
                        && _commonProcessor.draggableItem.equals(_commonProcessor.hoveredItem)) {
                    _commonProcessor.events.setEvent(InputEventType.MOUSE_DRAG);
                    _commonProcessor.draggableItem.eventMouseDrag.execute(_commonProcessor.draggableItem,
                            _commonProcessor.margs);
                } else if (anchor != null && !(_commonProcessor.hoveredItem instanceof ButtonCore)
                        && !_commonProcessor.window.isMaximized) {
                    DoubleBuffer x_pos = BufferUtils.createDoubleBuffer(1);
                    DoubleBuffer y_pos = BufferUtils.createDoubleBuffer(1);
                    glfwGetCursorPos(_commonProcessor.handler.getWindowId(), x_pos, y_pos);
                    int delta_x = (int) x_pos.get(0) - xClick;
                    int delta_y = (int) y_pos.get(0) - yClick;
                    IntBuffer x = BufferUtils.createIntBuffer(1);
                    IntBuffer y = BufferUtils.createIntBuffer(1);
                    glfwGetWindowPos(_commonProcessor.handler.getWindowId(), x, y);
                    _commonProcessor.wndProcessor.setWindowPos(x.get(0) + delta_x, y.get() + delta_y);
                }
            }
            if (_commonProcessor.hoveredItem != null
                    && !_commonProcessor.hoveredItem.getHoverVerification((float) xpos, (float) ypos)) {
                _commonProcessor.hoveredItem.setMouseHover(false);
                _commonProcessor.manager.assignActionsForSender(InputEventType.MOUSE_LEAVE, _commonProcessor.margs,
                        _commonProcessor.hoveredItem, _commonProcessor.underFocusedItems, false);
            }
        } else {
            _commonProcessor.ptrPress.setX(_commonProcessor.ptrRelease.getX());
            _commonProcessor.ptrPress.setY(_commonProcessor.ptrRelease.getY());

            if (_commonProcessor.getHoverPrototype(_commonProcessor.ptrRelease.getX(),
                    _commonProcessor.ptrRelease.getY(), InputEventType.MOUSE_MOVE)) {
                if (_commonProcessor.hoveredItem != null && !("".equals(_commonProcessor.hoveredItem.getToolTip()))) {
                    _commonProcessor.toolTip.initTimer(true);
                }
                Prototype popup = _commonProcessor.isInListHoveredItems(PopUpMessage.class);
                if (popup != null) {
                    ((PopUpMessage) popup).holdSelf(true);
                }
            }
        }

        // try {
        //     Thread.sleep(5);
        // } catch (Exception e) {
        // }
    }
}