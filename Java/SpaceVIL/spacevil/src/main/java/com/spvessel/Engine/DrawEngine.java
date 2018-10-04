package com.spvessel.Engine;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.*;
import java.io.*;
import java.nio.*;

import com.spvessel.Flags.*;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.*;

import com.spvessel.Common.*;
import com.spvessel.Items.*;
import com.spvessel.Windows.*;
import com.spvessel.Layouts.*;
import com.spvessel.Cores.*;

public class DrawEngine {

    public void resetItems() {
        if (focusedItem != null)
            focusedItem.setFocused(false);
        focusedItem = null;
        if (hoveredItem != null)
            hoveredItem.setMouseHover(false);
        ;
        hoveredItem = null;

        hoveredItems.clear();
    }

    private ToolTip _tooltip = new ToolTip();
    private BaseItem _isStencilSet = null;
    public InputDeviceEvent engineEvent = new InputDeviceEvent();
    private MouseArgs _margs = new MouseArgs();
    private KeyArgs _kargs = new KeyArgs();
    private TextInputArgs _tiargs = new TextInputArgs();

    private List<VisualItem> hoveredItems;
    private VisualItem hoveredItem = null;
    private VisualItem focusedItem = null;

    public void setFocusedItem(VisualItem item) {
        if (item == null) {
            focusedItem = null;
            return;
        }
        if (focusedItem != null)
            focusedItem.setFocused(false);
        focusedItem = item;
        focusedItem.setFocused(true);
    }

    private com.spvessel.Cores.Pointer ptrPress = new com.spvessel.Cores.Pointer();
    private com.spvessel.Cores.Pointer ptrRelease = new com.spvessel.Cores.Pointer();
    private com.spvessel.Cores.Pointer ptrClick = new com.spvessel.Cores.Pointer();

    public GLWHandler _handler;
    private Shader _primitive;
    private Shader _texture;

    public DrawEngine(WindowLayout handler) {
        hoveredItems = new LinkedList<VisualItem>();
        _handler = new GLWHandler(handler);

        _tooltip.setHandler(handler);
        _tooltip.getTextLine().setHandler(handler);
        _tooltip.getTextLine().setParent(_tooltip);
        _tooltip.initElements();
    }

    public void dispose() {
        glfwTerminate();
    }

    public void close() {
        _handler.setToClose();
    }

    private ByteBuffer[] _icon = new ByteBuffer[2];

    public void setBigIcon(ByteBuffer icon) {
        _icon[1] = icon;
    }

    public void setSmallIcon(ByteBuffer icon) {
        _icon[0] = icon;
    }

    public void applyIcon() {
        // Display.setIcon(_icon);
    }

    private String getResourceString(String resource) {

        StringBuilder result = new StringBuilder();

        // System.out.println( "URI: " + DrawEngine.class.getResource(resource));
        InputStream inputStream = DrawEngine.class.getResourceAsStream(resource);

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

    public void init() {
        synchronized (CommonService.GlobalLocker) {
            // InitWindow
            _handler.initGlfw();
            _handler.createWindow();
        }
        // _handler.switchContext();
        setWindowPos();
        // Focus(_handler.getWindowId(), true);

        // устанавливаем параметры отрисовки
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_ALPHA_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        // glEnable(GL_DEPTH_TEST);
        // glEnable(GL_STENCIL_TEST);
        ////////////////////////////////////////////////
        _primitive = new Shader(getResourceString("/shaders/vs_fill.glsl"), getResourceString("/shaders/fs_fill.glsl"));
        _primitive.compile();
        // System.out.println(_primitive.getCode(ShaderType.FRAGMENT));
        // System.out.println(_primitive.getCode(ShaderType.VERTEX));
        if (_primitive.getProgramID() == 0)
            System.out.println("Could not create primitive shaders");
        ///////////////////////////////////////////////
        _texture = new Shader(getResourceString("/shaders/vs_texture.glsl"),
                getResourceString("/shaders/fs_texture.glsl"));
        _texture.compile();
        if (_texture.getProgramID() == 0)
            System.out.println("Could not create textured shaders");

        if (_icon[0] != null && _icon[1] != null) {
            applyIcon();
        }
        setEventsCallbacks();

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // EventManager.IsLocked = false;
        run();
    }

    private void setEventsCallbacks() {
        _handler.setCallbackMouseMove(new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                mouseMove(window, xpos, ypos);
            }
        });
        _handler.setCallbackMouseClick(new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                mouseClick(window, button, action, mods);
            }
        });
        _handler.setCallbackMouseScroll(new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double dx, double dy) {
                mouseScroll(window, dx, dy);
            }
        });
        _handler.setCallbackKeyPress(new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                keyPress(window, key, scancode, action, mods);
            }
        });
        _handler.setCallbackTextInput(new GLFWCharModsCallback() {
            @Override
            public void invoke(long window, int codepoint, int mods) {
                textInput(window, codepoint, mods);
            }
        });
        _handler.setCallbackClose(new GLFWWindowCloseCallback() {
            @Override
            public void invoke(long window) {
                closeWindow(window);
            }
        });
        _handler.setCallbackFocus(new GLFWWindowFocusCallback() {
            @Override
            public void invoke(long window, boolean value) {
                focus(window, value);
            }
        });
        _handler.setCallbackResize(new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                resize(window, width, height);
            }
        });
        _handler.setCallbackPosition(new GLFWWindowPosCallback() {
            @Override
            public void invoke(long window, int xpos, int ypos) {
                position(window, xpos, ypos);
            }
        });
    }

    public void minimizeWindow() {
        engineEvent.setEvent(InputEventType.WINDOW_MINIMIZE);
        glfwIconifyWindow(_handler.getWindowId());
    }

    public void maximizeWindow() {
        // _handler.SwitchContext();
        try (MemoryStack stack = MemoryStack.stackPush()) {
            if (_handler.getLayout().isMaximized) {
                glfwRestoreWindow(_handler.getWindowId());
                _handler.getLayout().isMaximized = false;
                IntBuffer w = stack.mallocInt(1); // int*
                IntBuffer h = stack.mallocInt(1); // int*
                glfwGetWindowSize(_handler.getWindowId(), w, h);
                _handler.getLayout().setWidth(w.get(0));
                _handler.getLayout().setHeight(h.get(0));
            } else {
                glfwMaximizeWindow(_handler.getWindowId());
                _handler.getLayout().isMaximized = true;
                IntBuffer w = stack.mallocInt(1); // int*
                IntBuffer h = stack.mallocInt(1); // int*
                glfwGetWindowSize(_handler.getWindowId(), w, h);
                _handler.getLayout().setWidth(w.get(0));
                _handler.getLayout().setHeight(h.get(0));
            }
            // _handler.SwitchContext();
        }
    }

    private void closeWindow(long wnd) {
        _handler.getLayout().close();
    }

    public void focus(long wnd, Boolean value) {
        engineEvent.resetAllEvents();
        _tooltip.initTimer(false);

        if (value) {
            if (_handler.focusable) {
                synchronized (CommonService.GlobalLocker) {
                    WindowLayoutBox.setCurrentFocusedWindow(_handler.getLayout());
                }
                _handler.focused = value;
            }
        } else {
            if (_handler.getLayout().isDialog) {
                _handler.focused = true;
            } else {
                _handler.focused = value;
                if (_handler.getLayout().isOutsideClickClosable) {
                    resetItems();
                    _handler.getLayout().close();
                }
            }
        }
    }

    private void resize(long wnd, int width, int height) {
        _tooltip.initTimer(false);
        _handler.getLayout().setWidth(width);
        _handler.getLayout().setHeight(height);
        if (!_handler.getLayout().isBorderHidden) {
            glViewport(0, 0, _handler.getLayout().getWidth(), _handler.getLayout().getHeight());
        }
        update();
    }

    public void setWindowSize() {
        engineEvent.setEvent(InputEventType.WINDOW_RESIZE);
        glfwSetWindowSize(_handler.getWindowId(), _handler.getLayout().getWidth(), _handler.getLayout().getHeight());
        if (_handler.getLayout().isBorderHidden) {
            glViewport(0, 0, _handler.getLayout().getWidth(), _handler.getLayout().getHeight());
        }
    }

    private void position(long wnd, int xpos, int ypos) {
        _handler.wPosition.X = xpos;
        _handler.wPosition.Y = ypos;
    }

    public void setWindowPos() {
        engineEvent.setEvent(InputEventType.WINDOW_MOVE);
        _handler.getLayout().setX(_handler.wPosition.X);
        _handler.getLayout().setY(_handler.wPosition.Y);
        glfwSetWindowPos(_handler.getWindowId(), _handler.wPosition.X, _handler.wPosition.Y);
    }

    private void mouseMove(long wnd, double xpos, double ypos) {
        if (!_handler.focusable)
            return;

        engineEvent.setEvent(InputEventType.MOUSE_MOVE);
        _tooltip.initTimer(false);

        // logic of hovers
        ptrRelease.X = (int) xpos;
        ptrRelease.Y = (int) ypos;

        _margs.position.setPosition((float) xpos, (float) ypos);

        // assignActions(InputEventType.MOUSE_MOVE, _margs,
        // _handler.getLayout().getWindow());

        if (engineEvent.lastEvent().contains(InputEventType.MOUSE_PRESS)) {
            if (_handler.getLayout().isBorderHidden && _handler.getLayout().isResizable) {
                int w = _handler.getLayout().getWidth();
                int h = _handler.getLayout().getHeight();

                if (_handler.getLayout().getWindow()._sides.contains(ItemAlignment.LEFT)) {
                    if (!(_handler.getLayout().getMinWidth() == _handler.getLayout().getWidth()
                            && (ptrRelease.X - ptrPress.X) >= 0)) {
                        _handler.wPosition.X += (ptrRelease.X - ptrPress.X);
                        w -= (ptrRelease.X - ptrPress.X);
                    }
                    // ptrPress.X = ptrRelease.X;
                }
                if (_handler.getLayout().getWindow()._sides.contains(ItemAlignment.RIGHT)) {
                    if (!(ptrRelease.X < _handler.getLayout().getMinWidth()
                            && _handler.getLayout().getWidth() == _handler.getLayout().getMinWidth())) {
                        w += (ptrRelease.X - ptrPress.X);
                    }
                    ptrPress.X = ptrRelease.X;
                }
                if (_handler.getLayout().getWindow()._sides.contains(ItemAlignment.TOP)) {
                    if (!(_handler.getLayout().getMinHeight() == _handler.getLayout().getHeight()
                            && (ptrRelease.Y - ptrPress.Y) >= 0)) {
                        _handler.wPosition.Y += (ptrRelease.Y - ptrPress.Y);
                        h -= (ptrRelease.Y - ptrPress.Y);
                    }
                }
                if (_handler.getLayout().getWindow()._sides.contains(ItemAlignment.BOTTOM)) {
                    if (!(ptrRelease.Y < _handler.getLayout().getMinHeight()
                            && _handler.getLayout().getHeight() == _handler.getLayout().getMinHeight())) {
                        h += (ptrRelease.Y - ptrPress.Y);
                    }
                    ptrPress.Y = ptrRelease.Y;
                }

                if (_handler.getLayout().getWindow()._sides.size() != 0 && !_handler.getLayout().isMaximized) {
                    _handler.getLayout().setWidth(w);
                    _handler.getLayout().setHeight(h);
                    if (_handler.getLayout().getWindow()._sides.contains(ItemAlignment.LEFT)
                            || _handler.getLayout().getWindow()._sides.contains(ItemAlignment.TOP))
                        setWindowPos();
                    setWindowSize();
                    // update();
                }
            }
            if (_handler.getLayout().getWindow()._sides.size() == 0) {
                VisualItem draggable = isInListHoveredItems(InterfaceDraggable.class);
                VisualItem anchor = isInListHoveredItems(InterfaceWindowAnchor.class);
                if (draggable != null) {
                    draggable._mouse_ptr.setPosition((float) xpos, (float) ypos);
                    draggable.eventMouseDrag.execute(hoveredItem, _margs);
                    // assignActions(InputEventType.MOUSE_DRAG, _margs, draggable);

                    // Focus get
                    if (focusedItem != null)
                        focusedItem.setFocused(false);

                    focusedItem = hoveredItem;
                    focusedItem.setFocused(true);
                } else if (anchor != null && !(hoveredItem instanceof ButtonCore)
                        && !_handler.getLayout().isMaximized) {
                    if ((ptrRelease.X - ptrPress.X) != 0 || (ptrRelease.Y - ptrPress.Y) != 0) {
                        _handler.wPosition.X += (ptrRelease.X - ptrPress.X);
                        _handler.wPosition.Y += (ptrRelease.Y - ptrPress.Y);
                        setWindowPos();
                    }
                }
            }
        } else {
            ptrPress.X = ptrRelease.X;
            ptrPress.Y = ptrRelease.Y;

            // check tooltip
            if (getHoverVisualItem(ptrRelease.X, ptrRelease.Y, InputEventType.MOUSE_MOVE)) {
                if (hoveredItem.getToolTip() != "") {
                    _tooltip.initTimer(true);
                }

                _handler.setCursorType(GLFW_ARROW_CURSOR);
                if (_handler.getLayout().isBorderHidden && _handler.getLayout().isResizable
                        && !_handler.getLayout().isMaximized) {
                    // resize
                    if ((xpos < _handler.getLayout().getWindow().getWidth() - 5) && (xpos > 5)
                            && (ypos < _handler.getLayout().getWindow().getHeight() - 5) && ypos > 5) {
                        if (hoveredItem instanceof InterfaceTextEditable)
                            _handler.setCursorType(GLFW_IBEAM_CURSOR);
                        if (hoveredItem instanceof SplitHolder) {
                            if (((SplitHolder) hoveredItem).getOrientation().equals(Orientation.HORIZONTAL))
                                _handler.setCursorType(GLFW_VRESIZE_CURSOR);
                            else
                                _handler.setCursorType(GLFW_HRESIZE_CURSOR);
                        }
                    } else // refactor!!
                    {
                        if ((xpos > _handler.getLayout().getWindow().getWidth() - 5 && ypos < 5)
                                || (xpos > _handler.getLayout().getWindow().getWidth() - 5
                                        && ypos > _handler.getLayout().getWindow().getHeight() - 5)
                                || (ypos > _handler.getLayout().getWindow().getHeight() - 5 && xpos < 5)
                                || (ypos > _handler.getLayout().getWindow().getHeight() - 5
                                        && xpos > _handler.getLayout().getWindow().getWidth() - 5)
                                || (xpos < 5 && ypos < 5)) {
                            _handler.setCursorType(GLFW_CROSSHAIR_CURSOR);
                        } else {
                            if (xpos > _handler.getLayout().getWindow().getWidth() - 5 || xpos < 5)
                                _handler.setCursorType(GLFW_HRESIZE_CURSOR);

                            if (ypos > _handler.getLayout().getWindow().getHeight() - 5 || ypos < 5)
                                _handler.setCursorType(GLFW_VRESIZE_CURSOR);
                        }
                    }
                } else {
                    if (hoveredItem instanceof InterfaceTextEditable) {
                        _handler.setCursorType(GLFW_IBEAM_CURSOR);
                    }
                    if (hoveredItem instanceof SplitHolder) {
                        if (((SplitHolder) hoveredItem).getOrientation().equals(Orientation.HORIZONTAL))
                            _handler.setCursorType(GLFW_VRESIZE_CURSOR);
                        else
                            _handler.setCursorType(GLFW_HRESIZE_CURSOR);
                    }
                }
                VisualItem popup = isInListHoveredItems(PopUpMessage.class);
                if (popup != null) {
                    ((PopUpMessage) popup).holdSelf(true);
                }
            }
        }
    }

    private void mouseClick(long wnd, int button, int action, int mods) {
        if (!_handler.focusable)
            return;

        _tooltip.initTimer(false);
        _margs.button = MouseButton.getEnum(button);
        _margs.state = InputState.getEnum(action);
        _margs.mods = KeyMods.getEnum(mods);

        InputEventType m_state;
        if (action == InputState.PRESS.getValue())
            m_state = InputEventType.MOUSE_PRESS;
        else
            m_state = InputEventType.MOUSE_RELEASE;

        if (!getHoverVisualItem(ptrRelease.X, ptrRelease.Y, m_state)) {
            engineEvent.resetAllEvents();
            engineEvent.setEvent(InputEventType.MOUSE_RELEASE);
            return;
        }
        _handler.getLayout().getWindow().getSides(ptrRelease.X, ptrRelease.Y);

        switch (action) {
        case GLFW_RELEASE:
            _handler.getLayout().getWindow()._sides.clear();

            if (engineEvent.lastEvent().contains(InputEventType.WINDOW_RESIZE)
                    || engineEvent.lastEvent().contains(InputEventType.WINDOW_MOVE)) {
                engineEvent.setEvent(InputEventType.MOUSE_RELEASE);
                engineEvent.resetAllEvents();
                return;
            }
            if (engineEvent.lastEvent().contains(InputEventType.MOUSE_MOVE)) {
                float len = (float) Math
                        .sqrt(Math.pow(ptrRelease.X - ptrClick.X, 2) + Math.pow(ptrRelease.Y - ptrClick.Y, 2));
                if (len > 3.0f) {
                    engineEvent.resetAllEvents();
                    engineEvent.setEvent(InputEventType.MOUSE_RELEASE);
                    return;
                }
            }

            if (hoveredItem != null) {
                assignActions(InputEventType.MOUSE_RELEASE, _margs, false);
                hoveredItem.setMousePressed(false);
                // Focus get
                if (focusedItem != null)
                    focusedItem.setFocused(false);

                focusedItem = hoveredItem;
                focusedItem.setFocused(true);
            }
            engineEvent.resetAllEvents();
            engineEvent.setEvent(InputEventType.MOUSE_RELEASE);
            break;

        case GLFW_PRESS:
            DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
            glfwGetCursorPos(_handler.getWindowId(), xpos, ypos);
            ptrClick.X = (int) xpos.get(0);
            ptrClick.Y = (int) ypos.get(0);
            if (hoveredItem != null) {
                hoveredItem.setMousePressed(true);
                assignActions(InputEventType.MOUSE_PRESS, _margs, false);
            }

            if (hoveredItem instanceof WContainer) {
                ((WContainer) hoveredItem)._resizing = true;
            }
            engineEvent.resetAllEvents();
            engineEvent.setEvent(InputEventType.MOUSE_PRESS);
            break;
        case GLFW_REPEAT:
            break;
        default:
            break;
        }
    }

    private boolean getHoverVisualItem(float xpos, float ypos, InputEventType action) {
        List<VisualItem> queue = new LinkedList<VisualItem>();
        hoveredItems.clear();

        List<BaseItem> layout_box_of_items = new LinkedList<BaseItem>();
        synchronized (_handler.getLayout().engine_locker) {
            layout_box_of_items.add(_handler.getLayout().getWindow());
            layout_box_of_items.addAll(getInnerItems(_handler.getLayout().getWindow()));

            for (BaseItem item : ItemsLayoutBox.getLayoutFloatItems(_handler.getLayout().getId())) {
                if (!item.getVisible() || !item.isDrawable)
                    continue;
                layout_box_of_items.add(item);

                // System.out.println(item.getItemName() + " " +
                // layout_box_of_items.contains(item));
                if (item instanceof VisualItem)
                    layout_box_of_items.addAll(getInnerItems((VisualItem) item));
            }
        }

        for (BaseItem item : layout_box_of_items) {
            if (item instanceof VisualItem) {
                VisualItem tmp = (VisualItem) item;
                if (!tmp.getVisible() || !tmp.isDrawable)
                    continue;
                tmp.setMouseHover(false);
                // if (item instanceof ContextMenu)
                // System.out.println(item.getItemName() + " " + tmp.getHoverVerification(xpos,
                // ypos));
                if (tmp.getHoverVerification(xpos, ypos)) {
                    queue.add(tmp);
                } else {
                    if (item instanceof InterfaceFloating && action == InputEventType.MOUSE_PRESS) {
                        // System.out.println("1");
                        InterfaceFloating float_item = (InterfaceFloating) item;
                        if (float_item.getOutsideClickClosable()) {
                            // System.out.println("2");
                            if (item instanceof ContextMenu) {
                                ContextMenu to_close = (ContextMenu) item;
                                if (to_close.closeDependencies(_margs)) {
                                    // System.out.println("3");
                                    float_item.hide();
                                }
                            }
                        }
                    }
                }
                assignActions(InputEventType.MOUSE_MOVE, _margs, false);
            }
        }

        if (queue.size() > 0) {
            hoveredItem = queue.get(queue.size() - 1);
            hoveredItem.setMouseHover(true);

            hoveredItems = queue;
            Collections.reverse(hoveredItems);
            for (VisualItem item : hoveredItems) {
                if (item.equals(hoveredItem) && hoveredItem.getDisabled())
                    continue;// пропустить
                item.setMouseHover(true);
                if (!item.getPassEvents())
                    break;
            }
            Collections.reverse(hoveredItems);
            return true;
        } else
            return false;
    }

    private List<BaseItem> getInnerItems(VisualItem root) {
        List<BaseItem> list = new LinkedList<BaseItem>();

        for (BaseItem item : root.getItems()) {
            if (!item.getVisible() || !item.isDrawable)
                continue;
            list.add(item);
            if (item instanceof VisualItem)
                list.addAll(getInnerItems((VisualItem) item));
        }
        return list;
    }

    private <T> VisualItem isInListHoveredItems(Class<T> type) {
        VisualItem wanted = null;
        List<VisualItem> list = new LinkedList<VisualItem>();
        synchronized (_handler.getLayout().engine_locker) {
            for (VisualItem item : hoveredItems) {
                list.add(item);
            }
        }
        for (VisualItem item : list) {
            try {
                boolean found = type.isInstance(item);
                if (found) {
                    wanted = item;
                    if (wanted instanceof InterfaceFloating)
                        return wanted;
                }
            } catch (ClassCastException cce) {
                continue;
            }
        }
        return wanted;
    }

    private void mouseScroll(long wnd, double dx, double dy) {
        _tooltip.initTimer(false);
        if (hoveredItems.size() == 0)
            return;
        Collections.reverse(hoveredItems);
        for (VisualItem item : hoveredItems) {
            if (!item.getPassEvents())
                continue;
            if (dy > 0 || dx < 0)
                item.eventScrollUp.execute(item, _margs);
            if (dy < 0 || dx > 0)
                item.eventScrollDown.execute(item, _margs);
            engineEvent.setEvent(InputEventType.MOUSE_SCROLL);
        }
        Collections.reverse(hoveredItems);
    }

    private void keyPress(long wnd, int key, int scancode, int action, int mods) {
        if (!_handler.focusable)
            return;

        _tooltip.initTimer(false);
        _kargs.key = KeyCode.getEnum(key);
        // System.out.println(_kargs.key);
        _kargs.scancode = scancode;
        _kargs.state = InputState.getEnum(action);
        _kargs.mods = KeyMods.getEnum(mods);

        if ((focusedItem instanceof InterfaceTextShortcuts) && action == InputState.PRESS.getValue()) {
            if ((mods == KeyMods.CONTROL.getValue() && key == KeyCode.V.getValue())
                    || (mods == KeyMods.SHIFT.getValue() && key == KeyCode.INSERT.getValue())) {
                String paste_str = glfwGetClipboardString(_handler.getWindowId());
                ((InterfaceTextShortcuts) focusedItem).pasteText(paste_str);// !!!!!!!!!!!
            } else if (mods == KeyMods.CONTROL.getValue() && key == KeyCode.C.getValue()) {
                String copy_str = ((InterfaceTextShortcuts) focusedItem).getSelectedText();
                glfwSetClipboardString(_handler.getWindowId(), copy_str);
            } else if (mods == KeyMods.CONTROL.getValue() && key == KeyCode.X.getValue()) {
                String cut_str = ((InterfaceTextShortcuts) focusedItem).cutText();
                glfwSetClipboardString(_handler.getWindowId(), cut_str);
            } else {
                if (action == InputState.PRESS.getValue())
                    assignActions(InputEventType.KEY_PRESS, _kargs, focusedItem);
                if (action == InputState.REPEAT.getValue())
                    assignActions(InputEventType.KEY_PRESS, _kargs, focusedItem);
                if (action == InputState.RELEASE.getValue())
                    assignActions(InputEventType.KEY_RELEASE, _kargs, focusedItem);
            }
        } else {
            if (action == InputState.PRESS.getValue())
                assignActions(InputEventType.KEY_PRESS, _kargs, focusedItem);
            if (action == InputState.REPEAT.getValue())
                assignActions(InputEventType.KEY_PRESS, _kargs, focusedItem);
            if (action == InputState.RELEASE.getValue())
                assignActions(InputEventType.KEY_RELEASE, _kargs, focusedItem);
        }
    }

    private void textInput(long wnd, int character, int mods) {
        if (!_handler.focusable)
            return;

        _tooltip.initTimer(false);
        _tiargs.character = character;
        _tiargs.mods = KeyMods.getEnum(mods);
        if (focusedItem != null) {
            if (focusedItem.eventTextInput != null)
                focusedItem.eventTextInput.execute(focusedItem, _tiargs);
        }
    }

    private void assignActions(InputEventType action, InputEventArgs args, Boolean only_last) {
        if (only_last && !hoveredItem.getDisabled()) {
            EventTask task = new EventTask();
            task.item = hoveredItem;
            task.action = action;
            task.args = args;

            _handler.getLayout().setEventTask(task);
        } else {
            Collections.reverse(hoveredItems);
            for (VisualItem item : hoveredItems) {
                if (item.equals(hoveredItem) && hoveredItem.getDisabled())
                    continue;// пропустить

                item._mouse_ptr.X = ptrRelease.X;
                item._mouse_ptr.Y = ptrRelease.Y;
                EventTask task = new EventTask();
                task.item = item;
                task.action = action;
                task.args = args;
                _handler.getLayout().setEventTask(task);
                if (!item.getPassEvents())
                    break;
            }
            Collections.reverse(hoveredItems);
        }
        _handler.getLayout().executePollActions();
    }

    private void assignActions(InputEventType action, InputEventArgs args, VisualItem sender) {
        if (sender.getDisabled())
            return;

        EventTask task = new EventTask();
        task.item = sender;
        task.action = action;
        task.args = args;

        _handler.getLayout().setEventTask(task);
        _handler.getLayout().executePollActions();
    }

    public float _interval = 1.0f / 30.0f;// 1000 / 60;
    // internal float _interval = 1.0f / 60.0f;//1000 / 60;
    // internal int _interval = 11;//1000 / 90;
    // internal int _interval = 08;//1000 / 120;

    public void run() {
        focus(_handler.getWindowId(), true);
        _handler.gVAO = glGenVertexArrays();
        glBindVertexArray(_handler.gVAO);

        _primitive.useShader();

        while (!_handler.isClosing()) {
            // glfwPollEvents();
            // glfwWaitEvents();
            glfwWaitEventsTimeout(_interval);
            update();
            // System.out.println("loop");
        }
        _primitive.deleteShader();
        _texture.deleteShader();

        glDeleteVertexArrays(_handler.gVAO);

        _handler.clearEventsCallbacks();
        _handler.destroy();
    }

    public void update() {
        synchronized (_handler.getLayout().engine_locker) {
            if (_handler.getLayout().isBorderHidden)
                glViewport(0, 0, _handler.getLayout().getWidth(), _handler.getLayout().getHeight());
        }
        render();
    }

    private void render() {
        if (_handler.focused) {
            glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
            
            // draw static
            drawItems(_handler.getLayout().getWindow());
            
            // draw float
            for (BaseItem item : ItemsLayoutBox.getLayout(_handler.getLayout().getId()).getFloatItems())
                drawItems((BaseItem) item);

            // draw tooltip if needed
            drawToolTip();

            if (!_handler.focusable)
                drawShadePillow();
        }
        _handler.swap();
    }

    private void setStencilMask(List<float[]> crd_array) {

        int vertexbuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);
        int length = crd_array.size() * 3;
        FloatBuffer vertexData = BufferUtils.createFloatBuffer(length);
        for (int i = 0; i < length / 3; i++) {
            vertexData.put(i * 3 + 0, crd_array.get(i)[0]);
            vertexData.put(i * 3 + 1, crd_array.get(i)[1]);
            vertexData.put(i * 3 + 2, crd_array.get(i)[2]);
        }
        vertexData.rewind();
        glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        // Color
        float[] argb = { 0.0f, 0.0f, 0.0f, 0.0f };
        int colorbuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, colorbuffer);

        length = crd_array.size() * 4;
        FloatBuffer colorData = BufferUtils.createFloatBuffer(length);
        for (int i = 0; i < length / 4; i++) {
            colorData.put(i * 4 + 0, argb[0]);
            colorData.put(i * 4 + 1, argb[1]);
            colorData.put(i * 4 + 2, argb[2]);
            colorData.put(i * 4 + 3, argb[3]);
        }
        colorData.rewind();

        glBufferData(GL15.GL_ARRAY_BUFFER, colorData, GL15.GL_STATIC_DRAW);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);

        // draw
        glDrawArrays(GL_TRIANGLES, 0, crd_array.size());

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        // Clear VBO and shader
        glDeleteBuffers(vertexbuffer);
        glDeleteBuffers(colorbuffer);
    }

    private void checkOutsideBorders(BaseItem shell) {
        // if (shell.CutBehaviour == StencilBehaviour.Strict)
        // if(shell.getParent() != null)
        // _isStencilSet = shell;
        // StrictStencil(shell);
        // else

        lazyStencil(shell);
    }

    private void strictStencil(BaseItem shell) {
        glEnable(GL_STENCIL_TEST);

        glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
        glClear(GL_STENCIL_BUFFER_BIT);
        glStencilMask(0x00);

        glStencilFunc(GL_ALWAYS, 1, 0xFF);
        glStencilMask(0xFF);
        setStencilMask(shell.getParent().makeShape());

        glStencilFunc(GL_EQUAL, 1, 0xFF);

        shell.getParent()._confines_x_0 = shell.getParent().getX() + shell.getParent().getPadding().left;
        shell.getParent()._confines_x_1 = shell.getParent().getX() + shell.getParent().getWidth()
                - shell.getParent().getPadding().right;
        shell.getParent()._confines_y_0 = shell.getParent().getY() + shell.getParent().getPadding().top;
        shell.getParent()._confines_y_1 = shell.getParent().getY() + shell.getParent().getHeight()
                - shell.getParent().getPadding().bottom;
        setConfines(shell);
    }

    private void setConfines(BaseItem shell) {
        shell._confines_x_0 = shell.getParent()._confines_x_0;
        shell._confines_x_1 = shell.getParent()._confines_x_1;
        shell._confines_y_0 = shell.getParent()._confines_y_0;
        shell._confines_y_1 = shell.getParent()._confines_y_1;

        if (shell instanceof VisualItem) {
            VisualItem root = (VisualItem) shell;
            for (BaseItem item : root.getItems()) {
                setConfines(item);
            }
        }
    }

    private Boolean lazyStencil(BaseItem shell) {
        Map<ItemAlignment, int[]> outside = new HashMap<ItemAlignment, int[]>();

        if (shell.getParent() != null && _isStencilSet == null) {
            // System.out.println(shell.getParent().getItemName() + " " +
            // shell.getParent().getWidth() + " " + shell.getItemName() + " " +
            // shell.getWidth());
            // bottom
            if (shell.getParent().getY() + shell.getParent().getHeight() > shell.getY()
                    && shell.getParent().getY() + shell.getParent().getHeight() < shell.getY() + shell.getHeight()) {
                // match
                int y = shell.getParent().getY() + shell.getParent().getHeight()
                        - shell.getParent().getPadding().bottom;
                int h = shell.getHeight();
                outside.put(ItemAlignment.BOTTOM, new int[] { y, h });
            }
            // top
            if (shell.getParent().getY() + shell.getParent().getPadding().top > shell.getY()) {
                // match
                int y = shell.getY();
                int h = shell.getParent().getY() + shell.getParent().getPadding().top - shell.getY();
                outside.put(ItemAlignment.TOP, new int[] { y, h });
            }
            // right
            if (shell.getParent().getX() + shell.getParent().getWidth()
                    - shell.getParent().getPadding().right < shell.getX() + shell.getWidth()) {
                // match
                int x = shell.getParent().getX() + shell.getParent().getWidth() - shell.getParent().getPadding().right;
                int w = shell.getWidth();
                outside.put(ItemAlignment.RIGHT, new int[] { x, w });
                // System.out.println(x + " " + w + " " + (shell.getX() + shell.getWidth()));
            }
            // left
            if (shell.getParent().getX() + shell.getParent().getPadding().left > shell.getX()) {
                // match
                int x = shell.getX();
                int w = shell.getParent().getX() + shell.getParent().getPadding().left - shell.getX();
                outside.put(ItemAlignment.LEFT, new int[] { x, w });
            }

            // if (shell instanceof VerticalSlider) {
            // System.out.println(
            // shell.getX() + " " +
            // shell.getParent().getX() + " " +
            // shell.getY() + " " +
            // shell.getParent().getY() + " " +
            // outside.toString()
            // );
            // }

            if (outside.size() > 0 || shell.getParent() instanceof TextBlock) {
                _isStencilSet = shell;
                strictStencil(shell);
                return true;
            }
        }
        return false;
    }

    private void drawItems(BaseItem root) {
        if (!root.getVisible() || !root.isDrawable)
            return;

        if (root instanceof InterfaceLine) {
            drawLines((InterfaceLine) root);
        }
        if (root instanceof InterfacePoints) {
            drawPoints((InterfacePoints) root);
        }
        if (root instanceof TextItem) {
            glDisable(GL_MULTISAMPLE);
            drawText((TextItem) root);
            glEnable(GL_MULTISAMPLE);
            if (_isStencilSet == root) {
                glDisable(GL_STENCIL_TEST);
                _isStencilSet = null;
            }
        }
        if (root instanceof ImageItem) {
            drawShell(root);
            _texture.useShader();
            drawImage((ImageItem) root);
            _primitive.useShader();
            if (_isStencilSet == root) {
                glDisable(GL_STENCIL_TEST);
                _isStencilSet = null;
            }
        } else {
            drawShell(root);

            if (root instanceof VisualItem) {
                List<BaseItem> list;// = ((VisualItem)root).getItems();
                synchronized (CommonService.GlobalLocker) {
                    list = new LinkedList<>(((VisualItem) root).getItems());
                }
                for (BaseItem child : list) {
                    drawItems(child);
                }
            }
            if (_isStencilSet == root) {
                glDisable(GL_STENCIL_TEST);
                _isStencilSet = null;
            }
        }
    }

    private void drawShell(BaseItem shell) {
        checkOutsideBorders(shell);
        if (shell.getBackground().getAlpha() == 0)
            return;
        //
        // Vertex
        List<float[]> crd_array = shell.makeShape();
        if (crd_array == null)
            return;

        int vertexbuffer = glGenBuffers();
        // System.out.println(shell.getItemName() + " start");
        glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);

        int length = crd_array.size() * 3;
        FloatBuffer vertexData = BufferUtils.createFloatBuffer(length);
        for (int i = 0; i < length / 3; i++) {
            vertexData.put(i * 3 + 0, crd_array.get(i)[0]);
            vertexData.put(i * 3 + 1, crd_array.get(i)[1]);
            vertexData.put(i * 3 + 2, crd_array.get(i)[2]);
        }
        vertexData.rewind();
        glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        // Color
        float[] argb = { (float) shell.getBackground().getRed() / 255.0f,
                (float) shell.getBackground().getGreen() / 255.0f, (float) shell.getBackground().getBlue() / 255.0f,
                (float) shell.getBackground().getAlpha() / 255.0f };

        int colorbuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, colorbuffer);

        length = crd_array.size() * 4;
        FloatBuffer colorData = BufferUtils.createFloatBuffer(length);
        for (int i = 0; i < length / 4; i++) {
            colorData.put(i * 4 + 0, argb[0]);
            colorData.put(i * 4 + 1, argb[1]);
            colorData.put(i * 4 + 2, argb[2]);
            colorData.put(i * 4 + 3, argb[3]);
        }
        colorData.rewind();

        glBufferData(GL15.GL_ARRAY_BUFFER, colorData, GL15.GL_STATIC_DRAW);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);

        // draw
        glDrawArrays(GL_TRIANGLES, 0, crd_array.size());

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        // Clear VBO and shader
        glDeleteBuffers(vertexbuffer);
        glDeleteBuffers(colorbuffer);
        // System.out.println(shell.getItemName() + " end");

        // clear array
        crd_array.clear();
    }

    private void drawText(TextItem item) {
        float[] data = item.shape();
        if (data == null) {
            // System.out.println("null");
            return;
        }

        float[] color = item.getColors();
        if (color == null)
            return;

        checkOutsideBorders((BaseItem) item);

        int vertexbuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        int colorbuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, colorbuffer);
        glBufferData(GL_ARRAY_BUFFER, color, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);

        // draw
        glDrawArrays(GL_POINTS, 0, data.length / 3);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        // Clear VBO and shader
        glDeleteBuffers(vertexbuffer);
        glDeleteBuffers(colorbuffer);
    }

    private void drawPoints(InterfacePoints item) {
        // glPointSize(1.0f);

        if (item.getPointColor().getAlpha() == 0)
            return;

        List<float[]> crd_array = item.makeShape();
        if (crd_array == null)
            return;
        // long startTime = System.nanoTime();
        float[] result = new float[item.getShapePointer().size() * crd_array.size() * 3];
        int skew = 0;
        for (float[] shape : crd_array) {

            List<float[]> fig = GraphicsMathService.toGL(GraphicsMathService.moveShape(item.getShapePointer(),
                    shape[0] - item.getPointThickness() / 2.0f, shape[1] - item.getPointThickness() / 2.0f),
                    _handler.getLayout());

            for (int i = 0; i < fig.size(); i++) {
                result[skew + i * 3 + 0] = fig.get(i)[0];
                result[skew + i * 3 + 1] = fig.get(i)[1];
                result[skew + i * 3 + 2] = fig.get(i)[2];
            }
            skew += fig.size() * 3;
        }
        // long estimatedTime = (System.nanoTime() - startTime) / 1000000;
        // System.out.println(estimatedTime);

        int vertexbuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);
        int length = result.length;

        // FloatBuffer vertexData = BufferUtils.createFloatBuffer(length);
        // vertexData.put(result);
        // vertexData.rewind();

        glBufferData(GL_ARRAY_BUFFER, result, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        // Color
        float[] argb = { (float) item.getPointColor().getRed() / 255.0f,
                (float) item.getPointColor().getGreen() / 255.0f, (float) item.getPointColor().getBlue() / 255.0f,
                (float) item.getPointColor().getAlpha() / 255.0f };

        int colorbuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, colorbuffer);
        length = result.length / 3 * 4;
        FloatBuffer colorData = BufferUtils.createFloatBuffer(length);
        for (int i = 0; i < length / 4; i++) {
            colorData.put(i * 4 + 0, argb[0]);
            colorData.put(i * 4 + 1, argb[1]);
            colorData.put(i * 4 + 2, argb[2]);
            colorData.put(i * 4 + 3, argb[3]);
        }
        colorData.rewind();

        glBufferData(GL_ARRAY_BUFFER, colorData, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);

        checkOutsideBorders((BaseItem) item);

        // draw
        glDrawArrays(GL_TRIANGLES, 0, result.length / 3);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        // Clear VBO and shader
        glDeleteBuffers(vertexbuffer);
        glDeleteBuffers(colorbuffer);

    }

    private void drawLines(InterfaceLine item) {
        // Console.WriteLine();
        if (item.getLineColor().getAlpha() == 0)
            return;

        List<float[]> crd_array = GraphicsMathService.toGL(item.makeShape(), _handler.getLayout());

        if (crd_array == null)
            return;

        int vertexbuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);

        int length = crd_array.size() * 3;
        FloatBuffer vertexData = BufferUtils.createFloatBuffer(length);
        for (int i = 0; i < length / 3; i++) {
            vertexData.put(i * 3 + 0, crd_array.get(i)[0]);
            vertexData.put(i * 3 + 1, crd_array.get(i)[1]);
            vertexData.put(i * 3 + 2, crd_array.get(i)[2]);
        }
        vertexData.rewind();
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        // Color
        float[] argb = { (float) item.getLineColor().getRed() / 255.0f, (float) item.getLineColor().getGreen() / 255.0f,
                (float) item.getLineColor().getBlue() / 255.0f, (float) item.getLineColor().getAlpha() / 255.0f };
        int colorbuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, colorbuffer);
        length = crd_array.size() * 4;
        FloatBuffer colorData = BufferUtils.createFloatBuffer(length);
        for (int i = 0; i < length / 4; i++) {
            colorData.put(i * 4 + 0, argb[0]);
            colorData.put(i * 4 + 1, argb[1]);
            colorData.put(i * 4 + 2, argb[2]);
            colorData.put(i * 4 + 3, argb[3]);
        }
        colorData.rewind();

        checkOutsideBorders((BaseItem) item);

        glBufferData(GL_ARRAY_BUFFER, colorData, GL_STATIC_DRAW);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);

        // draw
        glDrawArrays(GL_LINE_STRIP, 0, crd_array.size());

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        // Clear VBO and shader
        glDeleteBuffers(vertexbuffer);
        glDeleteBuffers(colorbuffer);

        // crd_array.clear();
    }

    private void drawImage(ImageItem image) {
        byte[] bitmap = image.getPixMapImage();

        if (bitmap == null)
            return;

        ByteBuffer bb = BufferUtils.createByteBuffer(bitmap.length);
        bb.put(bitmap);
        bb.rewind();
        checkOutsideBorders((BaseItem) image);

        float i_x0 = ((float) image.getX() / (float) _handler.getLayout().getWidth() * 2.0f) - 1.0f;
        float i_y0 = ((float) image.getY() / (float) _handler.getLayout().getHeight() * 2.0f - 1.0f) * (-1.0f);
        float i_x1 = (((float) image.getX() + (float) image.getWidth()) / (float) _handler.getLayout().getWidth()
                * 2.0f) - 1.0f;
        float i_y1 = (((float) image.getY() + (float) image.getHeight()) / (float) _handler.getLayout().getHeight()
                * 2.0f - 1.0f) * (-1.0f);

        // VBO
        float[] vertex = new float[] {
                // X Y Z //U V
                i_x0, i_y0, 0.0f, 0.0f, 0.0f, // x0
                i_x0, i_y1, 0.0f, 1.0f, 0.0f, // x1
                i_x1, i_y1, 0.0f, 1.0f, 1.0f, // x2
                i_x1, i_y0, 0.0f, 0.0f, 1.0f, // x3
        };
        FloatBuffer vertexData = BufferUtils.createFloatBuffer(vertex.length);
        vertexData.put(vertex);
        vertexData.rewind();

        int[] ibo = new int[] { 0, 1, 2, // first triangle
                2, 3, 0, // second triangle
        };
        IntBuffer iboData = BufferUtils.createIntBuffer(ibo.length);
        iboData.put(ibo);
        iboData.rewind();

        int vertexbuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);

        glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);

        int elementbuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementbuffer);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, iboData, GL_STATIC_DRAW);

        // Position attribute
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * 4, 0);
        glEnableVertexAttribArray(0);
        // TexCoord attribute
        glVertexAttribPointer(1, 2, GL_FLOAT, true, 5 * 4, (3 * 4));
        glEnableVertexAttribArray(1);

        // texture
        int w = image.getImageWidth(), h = image.getImageHeight();

        int texture = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, texture);

        GL42.glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, h, w);
        GL11.glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, h, w, GL_RGBA, GL_UNSIGNED_BYTE, bb);
        GL30.glGenerateMipmap(GL_TEXTURE_2D);
        // glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, w, h);
        // glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, w, h, GL_RGBA, GL_UNSIGNED_BYTE,
        // bitmap);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        // glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_BASE_LEVEL, 256);

        // glActiveTexture(GL_TEXTURE0);

        int location = glGetUniformLocation((int) _texture.getProgramID(), "tex");
        if (location >= 0) {
            try {
                glUniform1i(location, 0);
            } catch (Exception ex) {
                System.out.println(
                        ex.getMessage() + " " + image.getItemName() + " " + _handler.getLayout().getWindowName());
                return;
            }
        }

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        // Clear VBO and shader
        glDeleteBuffers(vertexbuffer);
        glDeleteBuffers(elementbuffer);
        glDeleteTextures(texture);
    }

    private void drawToolTip() {
        if (!_tooltip.getVisible())
            return;

        _tooltip.setText(hoveredItem.getToolTip());
        _tooltip.setWidth(_tooltip.getPadding().left + _tooltip.getPadding().right + _tooltip.getTextWidth());

        // проверка сверху
        if (ptrRelease.Y > _tooltip.getHeight()) {
            _tooltip.setY(ptrRelease.Y - _tooltip.getHeight() - 2);
        } else {
            _tooltip.setY(ptrRelease.Y + _tooltip.getHeight() + 2);
        }
        // проверка справа
        if (ptrRelease.X - 10 + _tooltip.getWidth() > _handler.getLayout().getWidth()) {
            _tooltip.setX(_handler.getLayout().getWidth() - _tooltip.getWidth() - 10);
        } else {
            _tooltip.setX(ptrRelease.X - 10);
        }

        drawShell(_tooltip);

        _tooltip.getTextLine().updateGeometry();
        glDisable(GL_MULTISAMPLE);
        drawText(_tooltip.getTextLine());
        glEnable(GL_MULTISAMPLE);
        if (_isStencilSet == _tooltip.getTextLine()) {
            glDisable(GL_STENCIL_TEST);
            _isStencilSet = null;
        }
    }

    private void drawShadePillow() {

        // Vertex
        float[] vertex = new float[] {
                // X Y Z
                -1.0f, 1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f,

                1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, };
        int vertexbuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);
        int length = vertex.length;
        FloatBuffer vertexData = BufferUtils.createFloatBuffer(length);
        vertexData.put(vertex);
        vertexData.rewind();
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        // Color
        int colorbuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, colorbuffer);
        float[] argb = { 0.0f, 0.0f, 0.0f, (float) 150 / 255.0f };

        float[] color = new float[6 * 4];
        for (int i = 0; i < color.length / 4; i++) {
            color[i * 4 + 0] = argb[0];
            color[i * 4 + 1] = argb[1];
            color[i * 4 + 2] = argb[2];
            color[i * 4 + 3] = argb[3];
        }
        FloatBuffer colorData = BufferUtils.createFloatBuffer(color.length);
        colorData.put(color);
        colorData.rewind();
        glBufferData(GL_ARRAY_BUFFER, colorData, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);

        // draw
        glDrawArrays(GL_TRIANGLES, 0, 6);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        // Clear VBO and shader
        glDeleteBuffers(vertexbuffer);
        glDeleteBuffers(colorbuffer);
    }
}