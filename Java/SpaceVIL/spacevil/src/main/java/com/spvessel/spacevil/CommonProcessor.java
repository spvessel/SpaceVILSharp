package com.spvessel.spacevil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.spvessel.spacevil.internal.Wrapper.GlfwWrapper;

import com.spvessel.spacevil.Common.CommonService;
import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Core.IFloating;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Flags.EmbeddedCursor;
import com.spvessel.spacevil.Flags.InputEventType;

final class CommonProcessor {
    WindowProcessor wndProcessor;
    ToolTipItem toolTip;
    ActionManagerAssigner manager;
    GLWHandler handler;
    CoreWindow window;
    WindowLayout layout;
    WContainer rootContainer;
    UUID guid;
    MouseArgs margs;
    List<Prototype> underHoveredItems;
    List<Prototype> underFocusedItems;
    Prototype draggableItem = null;
    Prototype hoveredItem = null;
    Prototype focusedItem = null;
    int wGlobal = 0;
    int hGlobal = 0;
    int xGlobal = 0;
    int yGlobal = 0;
    boolean inputLocker = false;
    Position ptrPress = new Position();
    Position ptrRelease = new Position();
    Position ptrClick = new Position();
    InputDeviceEvent events;

    GlfwWrapper glfw = null;

    CommonProcessor() {
        glfw = GlfwWrapper.get();
        this.events = new InputDeviceEvent();
        margs = new MouseArgs();
        margs.clear();
        underFocusedItems = new LinkedList<>();
        underHoveredItems = new LinkedList<>();
        wndProcessor = new WindowProcessor(this);
    }

    void initProcessor(GLWHandler handler, ToolTipItem toolTip) {
        this.handler = handler;
        this.toolTip = toolTip;
        window = handler.getCoreWindow();
        layout = window.getLayout();
        rootContainer = layout.getContainer();
        guid = window.getWindowGuid();
        this.manager = new ActionManagerAssigner(layout);
    }

    boolean getHoverPrototype(float xpos, float ypos, InputEventType action) {
        inputLocker = true;
        underHoveredItems.clear();

        List<IBaseItem> layout_box_of_items = new LinkedList<IBaseItem>();
        layout_box_of_items.add(rootContainer);
        layout_box_of_items.addAll(getInnerItems(rootContainer, xpos, ypos));

        for (IBaseItem item : ItemsLayoutBox.getLayoutFloatItems(guid)) {
            if (!item.isVisible() || !item.isDrawable())
                continue;
            layout_box_of_items.add(item);

            if (item instanceof Prototype)
                layout_box_of_items.addAll(getInnerItems((Prototype) item, xpos, ypos));
        }
        inputLocker = false;

        List<Prototype> queue = new LinkedList<>();

        for (IBaseItem item : layout_box_of_items) {
            if (item instanceof Prototype) {
                Prototype prototype = (Prototype) item;
                if (prototype.getHoverVerification(xpos, ypos)) {
                    queue.add(prototype);
                } else {
                    prototype.setMouseHover(false);
                    if (item instanceof IFloating && action == InputEventType.MousePress) {
                        IFloating floatItem = (IFloating) item;
                        if (floatItem.isOutsideClickClosable()) {
                            if (item instanceof ContextMenu) {
                                ContextMenu cmToClose = (ContextMenu) item;
                                if (cmToClose.closeDependencies(margs)) {
                                    floatItem.hide();
                                }
                            } else {
                                floatItem.hide(margs);
                            }
                        }
                    }
                }
            }
        }

        if (queue.size() > 0) {
            if (hoveredItem != null && hoveredItem != queue.get(queue.size() - 1))
                manager.assignActionsForSender(InputEventType.MouseLeave, margs, hoveredItem, underFocusedItems,
                        false);

            hoveredItem = queue.get(queue.size() - 1);
            hoveredItem.setMouseHover(true);
            CommonService.currentCursor = hoveredItem.getCursor();
            glfw.SetCursor(handler.getWindowId(), CommonService.currentCursor.getCursor());

            if (window.isBorderHidden && window.isResizable && !window.isMaximized) {
                int handlerContainerWidth = rootContainer.getWidth();
                int handlerContainerHeight = rootContainer.getHeight();

                boolean cursorNearLeftTop = (xpos <= SpaceVILConstants.borderCursorTolerance
                        && ypos <= SpaceVILConstants.borderCursorTolerance);
                boolean cursorNearLeftBottom = (ypos >= handlerContainerHeight - SpaceVILConstants.borderCursorTolerance
                        && xpos <= SpaceVILConstants.borderCursorTolerance);
                boolean cursorNearRightTop = (xpos >= handlerContainerWidth - SpaceVILConstants.borderCursorTolerance
                        && ypos <= SpaceVILConstants.borderCursorTolerance);
                boolean cursorNearRightBottom = (xpos >= handlerContainerWidth - SpaceVILConstants.borderCursorTolerance
                        && ypos >= handlerContainerHeight - SpaceVILConstants.borderCursorTolerance);

                if (cursorNearRightTop || cursorNearRightBottom || cursorNearLeftBottom || cursorNearLeftTop) {
                    handler.setCursorType(EmbeddedCursor.Crosshair.getValue());
                } else {
                    if (xpos > handlerContainerWidth - SpaceVILConstants.borderCursorTolerance
                            || xpos <= SpaceVILConstants.borderCursorTolerance) {
                        handler.setCursorType(EmbeddedCursor.ResizeX.getValue());
                    }

                    if (ypos > handlerContainerHeight - SpaceVILConstants.borderCursorTolerance
                            || ypos <= SpaceVILConstants.borderCursorTolerance) {
                        handler.setCursorType(EmbeddedCursor.ResizeY.getValue());
                    }
                }
            }

            underHoveredItems = queue;
            Deque<Prototype> tmp = new ArrayDeque<>(underHoveredItems);
            while (!tmp.isEmpty()) {
                Prototype item = tmp.pollLast();
                if (item.equals(hoveredItem) && hoveredItem.isDisabled())
                    continue;
                item.setMouseHover(true);
                if (!item.isPassEvents(InputEventType.MouseHover))
                    break;
            }
            manager.assignActionsForHoveredItem(InputEventType.MouseHover, margs, hoveredItem, underHoveredItems,
                    false);
            return true;
        } else
            return false;
    }

    private List<IBaseItem> getInnerItems(Prototype root) {
        List<IBaseItem> list = new LinkedList<IBaseItem>();
        List<IBaseItem> root_items = root.getItems();

        for (IBaseItem item : root_items) {
            if (!item.isVisible() || !item.isDrawable())
                continue;
            if (item instanceof Prototype) {
                Prototype leaf = (Prototype) item;
                if (leaf.isDisabled())
                    continue;
                list.add(item);
                list.addAll(getInnerItems(leaf));
            }
        }
        return list;
    }

    private List<IBaseItem> getInnerItems(Prototype root, float xpos, float ypos) {
        List<IBaseItem> list = new LinkedList<IBaseItem>();
        List<IBaseItem> rootItems = root.getItems();

        for (IBaseItem item : rootItems) {
            if (!item.isVisible() || !item.isDrawable())
                continue;
            if (item instanceof Prototype) {
                Prototype leaf = (Prototype) item;
                if (leaf.isDisabled())
                    continue;
                if (leaf.getHoverVerification(xpos, ypos))
                    list.add(item);
                list.addAll(getInnerItems(leaf, xpos, ypos));
            }
        }
        return list;
    }

    <T> Prototype isInListHoveredItems(Class<T> type) {
        Prototype wanted = null;
        List<Prototype> list = new LinkedList<Prototype>(underHoveredItems);
        for (Prototype item : list) {
            try {
                boolean found = type.isInstance(item);
                if (found) {
                    wanted = item;
                    if (wanted instanceof IFloating)
                        return wanted;
                }
            } catch (ClassCastException cce) {
                continue;
            }
        }
        return wanted;
    }

    void findUnderFocusedItems(Prototype item) {
        Deque<Prototype> queue = new ArrayDeque<>();
        if (item == rootContainer) {
            underFocusedItems = null;
            return;
        }
        Prototype parent = item.getParent();
        while (parent != null) {
            queue.addFirst(parent);
            parent = parent.getParent();
        }
        underFocusedItems = new LinkedList<Prototype>(queue);
        underFocusedItems.remove(focusedItem);
    }

    void setFocusedItem(Prototype item) {
        if (item == null) {
            focusedItem = null;
            return;
        }
        if (focusedItem != null) {
            if (focusedItem.equals(item))
                return;
            focusedItem.setFocused(false);
        }
        focusedItem = item;
        focusedItem.setFocused(true);
        findUnderFocusedItems(item);
    }

    void resetFocus() {
        if (focusedItem != null)
            focusedItem.setFocused(false);
        focusedItem = rootContainer;
        focusedItem.setFocused(true);
        if (underFocusedItems != null)
            underFocusedItems.clear();
    }

    void resetItems() {
        resetFocus();
        if (hoveredItem != null)
            hoveredItem.setMouseHover(false);
        hoveredItem = null;
        underHoveredItems.clear();
    }

    static String getResourceString(String resource) {
        StringBuilder result = new StringBuilder();
        InputStream inputStream = CommonProcessor.class.getResourceAsStream(resource);
        try (BufferedReader scanner = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = scanner.readLine()) != null) {
                result.append(line).append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}