package com.spvessel.spacevil;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.nio.*;

import com.spvessel.spacevil.internal.Wrapper.*;

import com.spvessel.spacevil.Common.CommonService;
import com.spvessel.spacevil.Core.IDraggable;
import com.spvessel.spacevil.Core.IMovable;
import com.spvessel.spacevil.Core.IWindowAnchor;
import com.spvessel.spacevil.Core.Scale;
import com.spvessel.spacevil.Flags.InputEventType;
import com.spvessel.spacevil.Flags.OSType;
import com.spvessel.spacevil.Flags.Side;

final class MouseMoveProcessor {

    private GlfwWrapper glfw = null;

    private CommonProcessor _commonProcessor;

    MouseMoveProcessor(CommonProcessor processor) {
        glfw = GlfwWrapper.get();
        _commonProcessor = processor;
    }

    void process(long wnd, double xpos, double ypos, Scale scale) {
        _commonProcessor.ptrRelease.setX((int) xpos);
        _commonProcessor.ptrRelease.setY((int) ypos);
        _commonProcessor.margs.position.setPosition((int) xpos, (int) ypos);

        if (_commonProcessor.events.lastEvent().contains(InputEventType.MousePress)) {
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
                if (handlerContainerSides.contains(Side.Left)) {
                    if (!(_commonProcessor.window.getMinWidth() == _commonProcessor.window.getWidth()
                            && (xRelease - xPress) >= 0)) {
                        int x5 = xHandler - _commonProcessor.xGlobal + (int) xpos
                                - SpaceVILConstants.borderCursorTolerance;
                        xHandler = _commonProcessor.xGlobal + x5;
                        w = _commonProcessor.wGlobal - x5;
                    }
                }
                if (handlerContainerSides.contains(Side.Right)) {
                    if (!(xRelease < _commonProcessor.window.getMinWidth()
                            && _commonProcessor.window.getWidth() == _commonProcessor.window.getMinWidth())) {
                        w = xRelease;
                    }
                    _commonProcessor.ptrPress.setX(xRelease);
                }
                if (handlerContainerSides.contains(Side.Top)) {
                    if (!(_commonProcessor.window.getMinHeight() == _commonProcessor.window.getHeight()
                            && (yRelease - yPress) >= 0)) {

                        if (CommonService.getOSType() == OSType.Mac) {
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
                if (handlerContainerSides.contains(Side.Bottom)) {
                    if (!(yRelease < _commonProcessor.window.getMinHeight()
                            && _commonProcessor.window.getHeight() == _commonProcessor.window.getMinHeight())) {

                        if (CommonService.getOSType() == OSType.Mac)
                            yHandler = _commonProcessor.yGlobal;
                        h = yRelease;
                        _commonProcessor.ptrPress.setY(yRelease);
                    }
                }
                if (handlerContainerSides.size() != 0 && !_commonProcessor.window.isMaximized) {
                    if (CommonService.getOSType() == OSType.Mac) {
                        _commonProcessor.wndProcessor.setWindowSize(w, h, new Scale());
                        if (handlerContainerSides.contains(Side.Left) && handlerContainerSides.contains(Side.Top)) {
                            _commonProcessor.wndProcessor.setWindowPos(xHandler,
                                    (_commonProcessor.hGlobal - h) + _commonProcessor.yGlobal);
                        } else if (handlerContainerSides.contains(Side.Left)
                                || handlerContainerSides.contains(Side.Bottom)
                                || handlerContainerSides.contains(Side.Top)) {
                            _commonProcessor.wndProcessor.setWindowPos(xHandler, yHandler);
                            _commonProcessor.handler.getPointer().setY(yHandler);
                        }
                    } else {
                        boolean flagLT = false;
                        if (handlerContainerSides.contains(Side.Left)) {
                            flagLT = true;
                            w = (int) (w / scale.getXScale());
                        }
                        if (handlerContainerSides.contains(Side.Top)) {
                            flagLT = true;
                            h = (int) (h / scale.getYScale());
                        }
                        if (flagLT) {
                            _commonProcessor.wndProcessor.setWindowPos(xHandler, yHandler);
                        }
                        _commonProcessor.wndProcessor.setWindowSize(w, h, scale);
                    }
                }
            }

            if (_commonProcessor.rootContainer.getSides().size() == 0) {
                int xClick = _commonProcessor.ptrClick.getX();
                int yClick = _commonProcessor.ptrClick.getY();
                _commonProcessor.draggableItem = _commonProcessor.isInListHoveredItems(IDraggable.class);
                Prototype anchor = _commonProcessor.isInListHoveredItems(IWindowAnchor.class);
                if (_commonProcessor.draggableItem != null
                        && _commonProcessor.draggableItem.equals(_commonProcessor.hoveredItem)) {
                    _commonProcessor.events.setEvent(InputEventType.MouseDrag);
                    _commonProcessor.draggableItem.eventMouseDrag.execute(_commonProcessor.draggableItem,
                            _commonProcessor.margs);
                } else if (anchor != null && !(_commonProcessor.hoveredItem instanceof ButtonCore)
                        && !_commonProcessor.window.isMaximized) {
                    double[] pos = glfw.GetCursorPos(_commonProcessor.handler.getWindowId());
                    int delta_x = (int)pos[0] - xClick;
                    int delta_y = (int)pos[1] - yClick;

                    int[] windowPos = glfw.GetWindowPos(_commonProcessor.handler.getWindowId());
                    _commonProcessor.wndProcessor.setWindowPos(windowPos[0] + delta_x, windowPos[1] + delta_y);
                }
            }
            if (_commonProcessor.hoveredItem != null
                    && !_commonProcessor.hoveredItem.getHoverVerification((float) xpos, (float) ypos)) {
                _commonProcessor.hoveredItem.setMouseHover(false);
                _commonProcessor.manager.assignActionsForSender(InputEventType.MouseLeave, _commonProcessor.margs,
                        _commonProcessor.hoveredItem, _commonProcessor.underFocusedItems, false);
            }
        } else {
            _commonProcessor.ptrPress.setX(_commonProcessor.ptrRelease.getX());
            _commonProcessor.ptrPress.setY(_commonProcessor.ptrRelease.getY());

            Prototype lastHovered = _commonProcessor.hoveredItem;
            List<Prototype> tmpList = new LinkedList<>(_commonProcessor.underHoveredItems);

            if (_commonProcessor.getHoverPrototype(_commonProcessor.ptrRelease.getX(),
                    _commonProcessor.ptrRelease.getY(), InputEventType.MouseMove)) {
                if (_commonProcessor.hoveredItem != null && !("".equals(_commonProcessor.hoveredItem.getToolTip()))) {
                    _commonProcessor.toolTip.initTimer(true);
                }
                Prototype popup = _commonProcessor.isInListHoveredItems(PopUpMessage.class);
                if (popup != null) {
                    ((PopUpMessage) popup).holdSelf(true);
                }

                if (lastHovered != null) {
                    if (!_commonProcessor.underHoveredItems.contains(lastHovered))
                        lastHovered.setMouseHover(false);
                    List<Prototype> uniqueList = getUniqueList(tmpList, _commonProcessor.underHoveredItems);
                    for (Prototype item : uniqueList) {
                        item.setMouseHover(false);
                    }
                }
            }
        }

        if (_commonProcessor.hoveredItem instanceof IMovable) {
            _commonProcessor.events.setEvent(InputEventType.MouseDrag);
            _commonProcessor.hoveredItem.eventMouseMove.execute(_commonProcessor.hoveredItem, _commonProcessor.margs);
        }
    }

    private <T> List<T> getUniqueList(List<T> firstList, List<T> secondList) {
        Set<T> hashset = new HashSet<>(secondList);
        List<T> result = new LinkedList<>();
        for (T item : firstList) {
            if (!hashset.contains(item))
                result.add(item);
        }
        return result;
    }
}