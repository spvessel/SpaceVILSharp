package com.spvessel;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.*;
import java.io.*;
import java.nio.*;

import com.spvessel.Common.CommonService;
import com.spvessel.Core.*;
import com.spvessel.Flags.*;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public final class DrawEngine {

    protected void resetItems() {
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
    private InterfaceBaseItem _isStencilSet = null;
    private InputDeviceEvent engineEvent = new InputDeviceEvent();
    private MouseArgs _margs = new MouseArgs();
    private KeyArgs _kargs = new KeyArgs();
    private TextInputArgs _tiargs = new TextInputArgs();

    private List<Prototype> hoveredItems;
    private Prototype hoveredItem = null;
    private Prototype focusedItem = null;

    protected void setFocusedItem(Prototype item) {
        if (item == null) {
            focusedItem = null;
            return;
        }
        if (focusedItem != null)
            focusedItem.setFocused(false);
        focusedItem = item;
        focusedItem.setFocused(true);
    }

    private Pointer ptrPress = new Pointer();
    private Pointer ptrRelease = new Pointer();
    private Pointer ptrClick = new Pointer();

    protected GLWHandler _handler;
    private Shader _primitive;
    private Shader _texture;
    private Shader _char;
    private Shader _fxaa;
    private Shader _blur;

    protected DrawEngine(WindowLayout handler) {
        hoveredItems = new LinkedList<Prototype>();
        _handler = new GLWHandler(handler);

        _tooltip.setHandler(handler);
        _tooltip.getTextLine().setHandler(handler);
        _tooltip.getTextLine().setParent(_tooltip);
        _tooltip.initElements();
    }

    protected void dispose() {
        glfwTerminate();
    }

    protected void close() {
        _handler.setToClose();
    }

    private GLFWImage _iconSmall;
    private GLFWImage _iconBig;

    protected void setBigIcon(BufferedImage icon) {
        _iconBig = GLFWImage.create();
        _iconBig.width(icon.getWidth());
        _iconBig.height(icon.getHeight());
        _iconBig.pixels(createByteImage(icon));
    }

    protected void setSmallIcon(BufferedImage icon) {
        _iconSmall = GLFWImage.create();
        _iconSmall.width(icon.getWidth());
        _iconSmall.height(icon.getHeight());
        _iconSmall.pixels(createByteImage(icon));
    }

    private ByteBuffer createByteImage(BufferedImage image) {
        List<Byte> bmp = new LinkedList<Byte>();
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                byte[] bytes = ByteBuffer.allocate(4).putInt(image.getRGB(i, j)).array();
                bmp.add(bytes[1]);
                bmp.add(bytes[2]);
                bmp.add(bytes[3]);
                bmp.add(bytes[0]);
            }
        }
        ByteBuffer result = BufferUtils.createByteBuffer(bmp.size());
        int index = 0;
        for (byte var : bmp) {
            result.put(index, var);
            index++;
        }
        result.rewind();
        return result;
    }

    protected void applyIcon() {
        // Display.setIcon(_icon);
        GLFWImage.Buffer gb = GLFWImage.create(2);
        gb.put(0, _iconSmall);
        gb.put(1, _iconBig);
        glfwSetWindowIcon(_handler.getWindowId(), gb);
    }

    private String getResourceString(String resource) {
        StringBuilder result = new StringBuilder();
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

    protected void init() {
        CommonService.GlobalLocker.lock();
        try {
            // InitWindow
            _handler.initGlfw();
            _handler.createWindow();
            setEventsCallbacks();
            if (_iconSmall != null && _iconBig != null) {
                applyIcon();
            }
            GL.createCapabilities();
        } catch (Exception ex) {
            System.out.println(ex.toString());
            _handler.clearEventsCallbacks();
            if (_handler.getWindowId() == NULL)
                _handler.destroy();
            _handler.getLayout().close();
        } finally {
            CommonService.GlobalLocker.unlock();
        }
        // устанавливаем параметры отрисовки
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_ALPHA_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        // glDisable(GL_MULTISAMPLE);
        glEnable(GL_MULTISAMPLE);
        // glEnable(GL_DEPTH_TEST);
        // glEnable(GL_STENCIL_TEST);

        ////////////////////////////////////////////////
        _primitive = new Shader(getResourceString("/shaders/vs_fill.glsl"), getResourceString("/shaders/fs_fill.glsl"));
        _primitive.compile();
        if (_primitive.getProgramID() == 0)
            System.out.println("Could not create primitive shaders");
        ///////////////////////////////////////////////

        _texture = new Shader(getResourceString("/shaders/vs_texture.glsl"),
                getResourceString("/shaders/fs_texture.glsl"));
        _texture.compile();
        if (_texture.getProgramID() == 0)
            System.out.println("Could not create textured shaders");
        ///////////////////////////////////////////////

        _char = new Shader(getResourceString("/shaders/vs_char.glsl"), getResourceString("/shaders/fs_char.glsl"));
        _char.compile();
        if (_char.getProgramID() == 0)
            System.out.println("Could not create character shaders");
        ///////////////////////////////////////////////

        _fxaa = new Shader(getResourceString("/shaders/vs_fxaa.glsl"), getResourceString("/shaders/fs_fxaa.glsl"));
        _fxaa.compile();
        if (_fxaa.getProgramID() == 0)
            System.out.println("Could not create fxaa shaders");

        _blur = new Shader(getResourceString("/shaders/vs_blur.glsl"), getResourceString("/shaders/fs_blur.glsl"));
        _blur.compile();
        if (_blur.getProgramID() == 0)
            System.out.println("Could not create blur shaders");

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

    protected void minimizeWindow() {
        engineEvent.setEvent(InputEventType.WINDOW_MINIMIZE);
        glfwIconifyWindow(_handler.getWindowId());
    }

    protected void maximizeWindow() {
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
        }
    }

    private void closeWindow(long wnd) {
        _handler.getLayout().close();
    }

    protected void focus(long wnd, boolean value) {
        engineEvent.resetAllEvents();
        _tooltip.initTimer(false);
        _handler.getLayout().isFocused = value;
        if (value) {
            if (_handler.focusable) {
                WindowLayoutBox.setCurrentFocusedWindow(_handler.getLayout());
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
        // if (!flag_resize)
        // return;
        // flag_resize = false;

        _tooltip.initTimer(false);
        _handler.getLayout().setWidth(width);
        _handler.getLayout().setHeight(height);

        _fbo.bindFBO();
        _fbo.clearTexture();
        _fbo.genFBOTexture(_handler.getLayout().getWidth(), _handler.getLayout().getHeight());
        _fbo.unbindFBO();
    }

    protected void setWindowSize(int w, int h) {
        glfwSetWindowSize(_handler.getWindowId(), w, h);
        engineEvent.setEvent(InputEventType.WINDOW_RESIZE);
    }

    private void position(long wnd, int xpos, int ypos) {
        // if (!flag_pos)
        // return;
        // flag_pos = false;

        _handler.getPointer().setX(xpos);
        _handler.getPointer().setY(ypos);
    }

    protected void setWindowPos(int x, int y) {
        glfwSetWindowPos(_handler.getWindowId(), x, y);

        engineEvent.setEvent(InputEventType.WINDOW_MOVE);
        _handler.getLayout().setX(x);
        _handler.getLayout().setY(y);
    }

    boolean flag_move = false;
    // boolean flag_text_input = false;
    // boolean flag_click = false;
    // boolean flag_pos = false;
    // boolean flag_resize = false;

    private void mouseMove(long wnd, double xpos, double ypos) {
        engineEvent.setEvent(InputEventType.MOUSE_MOVE);
        _tooltip.initTimer(false);
        if (!flag_move)
            return;
        flag_move = false;

        if (!_handler.focusable)
            return;

        // logic of hovers
        ptrRelease.setX((int) xpos);
        ptrRelease.setY((int) ypos);

        _margs.position.setPosition((float) xpos, (float) ypos);

        if (engineEvent.lastEvent().contains(InputEventType.MOUSE_PRESS)) {
            if (_handler.getLayout().isBorderHidden && _handler.getLayout().isResizable) {
                int w = _handler.getLayout().getWidth();
                int h = _handler.getLayout().getHeight();

                int x_handler = _handler.getPointer().getX();
                int y_handler = _handler.getPointer().getY();
                int x_release = ptrRelease.getX();
                int y_release = ptrRelease.getY();
                int x_press = ptrPress.getX();
                int y_press = ptrPress.getY();

                if (_handler.getLayout().getWindow()._sides.contains(ItemAlignment.LEFT)) {
                    if (!(_handler.getLayout().getMinWidth() == _handler.getLayout().getWidth()
                            && (x_release - x_press) >= 0)) {
                        int x5 = x_handler - x_global + (int) xpos - 5;
                        x_handler = x_global + x5;
                        w = w_global - x5;
                    }
                }
                if (_handler.getLayout().getWindow()._sides.contains(ItemAlignment.RIGHT)) {
                    if (!(x_release < _handler.getLayout().getMinWidth()
                            && _handler.getLayout().getWidth() == _handler.getLayout().getMinWidth())) {
                        w = x_release;
                    }
                    ptrPress.setX(x_release);
                }
                if (_handler.getLayout().getWindow()._sides.contains(ItemAlignment.TOP)) {
                    if (!(_handler.getLayout().getMinHeight() == _handler.getLayout().getHeight()
                            && (y_release - y_press) >= 0)) {
                        int y5 = y_handler - y_global + (int) ypos - 5;
                        y_handler = y_global + y5;
                        h = h_global - y5;
                    }
                }
                if (_handler.getLayout().getWindow()._sides.contains(ItemAlignment.BOTTOM)) {
                    if (!(y_release < _handler.getLayout().getMinHeight()
                            && _handler.getLayout().getHeight() == _handler.getLayout().getMinHeight())) {
                        h = y_release;
                    }
                    ptrPress.setY(y_release);
                }

                if (_handler.getLayout().getWindow()._sides.size() != 0 && !_handler.getLayout().isMaximized) {
                    if (_handler.getLayout().getWindow()._sides.contains(ItemAlignment.LEFT)
                            || _handler.getLayout().getWindow()._sides.contains(ItemAlignment.TOP))
                        setWindowPos(x_handler, y_handler);
                    setWindowSize(w, h);
                }
            }

            if (_handler.getLayout().getWindow()._sides.size() == 0) {
                int x_click = ptrClick.getX();
                int y_click = ptrClick.getY();
                Prototype draggable = isInListHoveredItems(InterfaceDraggable.class);
                Prototype anchor = isInListHoveredItems(InterfaceWindowAnchor.class);

                if (draggable != null) {
                    draggable.eventMouseDrag.execute(draggable, _margs);
                } else if (anchor != null && !(hoveredItem instanceof ButtonCore)
                        && !_handler.getLayout().isMaximized) {

                    DoubleBuffer x_pos = BufferUtils.createDoubleBuffer(1);
                    DoubleBuffer y_pos = BufferUtils.createDoubleBuffer(1);
                    glfwGetCursorPos(_handler.getWindowId(), x_pos, y_pos);
                    int delta_x = (int) x_pos.get(0) - x_click;
                    int delta_y = (int) y_pos.get(0) - y_click;
                    IntBuffer x = BufferUtils.createIntBuffer(1);
                    IntBuffer y = BufferUtils.createIntBuffer(1);
                    glfwGetWindowPos(_handler.getWindowId(), x, y);
                    setWindowPos(x.get(0) + delta_x, y.get() + delta_y);
                }
            }
        } else {
            ptrPress.setX(ptrRelease.getX());
            ptrPress.setY(ptrRelease.getY());

            if (getHoverPrototype(ptrRelease.getX(), ptrRelease.getY(), InputEventType.MOUSE_MOVE)) {
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
                Prototype popup = isInListHoveredItems(PopUpMessage.class);
                if (popup != null) {
                    ((PopUpMessage) popup).holdSelf(true);
                }
            }
        }
    }

    private void mouseClick(long wnd, int button, int action, int mods) {
        _handler.getLayout().getWindow()._sides.clear();
        if (!_handler.focusable)
            return;
        // if (!flag_click)
        // return;
        // flag_click = false;

        _tooltip.initTimer(false);
        _margs.button = MouseButton.getEnum(button);
        _margs.state = InputState.getEnum(action);
        _margs.mods = KeyMods.getEnum(mods);

        InputEventType m_state;
        if (action == InputState.PRESS.getValue())
            m_state = InputEventType.MOUSE_PRESS;
        else
            m_state = InputEventType.MOUSE_RELEASE;

        Deque<Prototype> tmp = new ArrayDeque<>(hoveredItems);

        if (!getHoverPrototype(ptrRelease.getX(), ptrRelease.getY(), m_state)) {
            engineEvent.resetAllEvents();
            engineEvent.setEvent(InputEventType.MOUSE_RELEASE);
            return;
        }
        _handler.getLayout().getWindow().getSides(ptrRelease.getX(), ptrRelease.getY());

        switch (action) {
        case GLFW_RELEASE:

            while (!tmp.isEmpty()) {
                Prototype item = tmp.pollLast();
                if (item.isDisabled())
                    continue;// пропустить
                item.setMousePressed(false);
            }

            if (engineEvent.lastEvent().contains(InputEventType.WINDOW_RESIZE)
                    || engineEvent.lastEvent().contains(InputEventType.WINDOW_MOVE)) {
                engineEvent.resetAllEvents();
                engineEvent.setEvent(InputEventType.MOUSE_RELEASE);
                return;
            }
            if (engineEvent.lastEvent().contains(InputEventType.MOUSE_MOVE)) {
                float len = (float) Math.sqrt(Math.pow(ptrRelease.getX() - ptrClick.getX(), 2)
                        + Math.pow(ptrRelease.getY() - ptrClick.getY(), 2));
                if (len > 10.0f) {
                    engineEvent.resetAllEvents();
                    engineEvent.setEvent(InputEventType.MOUSE_RELEASE);
                    return;
                }
            }

            if (hoveredItem != null) {
                assignActions(InputEventType.MOUSE_RELEASE, _margs, false);
                hoveredItem.setMousePressed(false);
            }
            engineEvent.resetAllEvents();
            engineEvent.setEvent(InputEventType.MOUSE_RELEASE);
            break;

        case GLFW_PRESS:

            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer width = stack.mallocInt(1);
                IntBuffer height = stack.mallocInt(1);
                glfwGetFramebufferSize(_handler.getWindowId(), width, height);
                w_global = width.get(0);
                h_global = height.get(0);

                IntBuffer x = stack.mallocInt(1);
                IntBuffer y = stack.mallocInt(1);
                glfwGetWindowPos(_handler.getWindowId(), x, y);
                x_global = x.get(0);
                y_global = y.get(0);
            }

            DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
            glfwGetCursorPos(_handler.getWindowId(), xpos, ypos);
            ptrClick.setX((int) xpos.get(0));
            ptrClick.setY((int) ypos.get(0));
            ptrPress.setX((int) xpos.get(0));
            ptrPress.setY((int) ypos.get(0));
            if (hoveredItem != null) {
                hoveredItem.setMousePressed(true);
                assignActions(InputEventType.MOUSE_PRESS, _margs, false);
                if (hoveredItem.isFocusable) {
                    // Focus get
                    if (focusedItem != null)
                        focusedItem.setFocused(false);
                    focusedItem = hoveredItem;
                    focusedItem.setFocused(true);
                }
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

    private int w_global = 0;
    private int h_global = 0;
    private int x_global = 0;
    private int y_global = 0;

    private boolean getHoverPrototype(float xpos, float ypos, InputEventType action) {
        List<Prototype> queue = new LinkedList<Prototype>();
        hoveredItems.clear();

        List<InterfaceBaseItem> layout_box_of_items = new LinkedList<InterfaceBaseItem>();
        layout_box_of_items.add(_handler.getLayout().getWindow());
        layout_box_of_items.addAll(getInnerItems(_handler.getLayout().getWindow()));

        for (InterfaceBaseItem item : ItemsLayoutBox.getLayoutFloatItems(_handler.getLayout().getId())) {
            if (!item.isVisible() || !item.isDrawable())
                continue;
            layout_box_of_items.add(item);

            if (item instanceof Prototype)
                layout_box_of_items.addAll(getInnerItems((Prototype) item));
        }

        for (InterfaceBaseItem item : layout_box_of_items) {
            if (item instanceof Prototype) {
                Prototype tmp = (Prototype) item;
                if (!tmp.isVisible() || !tmp.isDrawable())
                    continue;
                tmp.setMouseHover(false);
                if (tmp.getHoverVerification(xpos, ypos)) {
                    queue.add(tmp);
                } else {
                    if (item instanceof InterfaceFloating && action == InputEventType.MOUSE_PRESS) {
                        InterfaceFloating float_item = (InterfaceFloating) item;
                        if (float_item.getOutsideClickClosable()) {
                            if (item instanceof ContextMenu) {
                                ContextMenu to_close = (ContextMenu) item;
                                if (to_close.closeDependencies(_margs)) {
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

            Deque<Prototype> tmp;
            hoveredItems = queue;
            tmp = new ArrayDeque<>(hoveredItems);
            while (!tmp.isEmpty()) {
                Prototype item = tmp.pollLast();
                if (item.equals(hoveredItem) && hoveredItem.isDisabled())
                    continue;// пропустить
                item.setMouseHover(true);
                if (!item.isPassEvents())
                    break;
            }
            return true;
        } else
            return false;
    }

    private List<InterfaceBaseItem> getInnerItems(Prototype root) {
        List<InterfaceBaseItem> list = new LinkedList<InterfaceBaseItem>();

        for (InterfaceBaseItem item : root.getItems()) {
            if (!item.isVisible() || !item.isDrawable())
                continue;
            list.add(item);
            if (item instanceof Prototype)
                list.addAll(getInnerItems((Prototype) item));
        }
        return list;
    }

    private <T> Prototype isInListHoveredItems(Class<T> type) {
        Prototype wanted = null;
        List<Prototype> list = new LinkedList<Prototype>(hoveredItems);
        for (Prototype item : list) {
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
        List<Prototype> tmp = new LinkedList<Prototype>(hoveredItems);
        Collections.reverse(tmp);
        for (Prototype item : tmp) {
            if (!item.isPassEvents())
                continue;
            if (dy > 0 || dx < 0)
                item.eventScrollUp.execute(item, _margs);
            if (dy < 0 || dx > 0)
                item.eventScrollDown.execute(item, _margs);
            engineEvent.setEvent(InputEventType.MOUSE_SCROLL);
        }
    }

    private void keyPress(long wnd, int key, int scancode, int action, int mods) {
        if (!_handler.focusable)
            return;
        _tooltip.initTimer(false);
        _kargs.key = KeyCode.getEnum(key);
        _kargs.scancode = scancode;
        _kargs.state = InputState.getEnum(action);
        _kargs.mods = KeyMods.getEnum(mods);

        if ((focusedItem instanceof InterfaceTextShortcuts) && action == InputState.PRESS.getValue()) {
            if ((mods == KeyMods.CONTROL.getValue() && key == KeyCode.V.getValue())
                    || (mods == KeyMods.SHIFT.getValue() && key == KeyCode.INSERT.getValue())) {
                String paste_str = "";
                paste_str = glfwGetClipboardString(_handler.getWindowId());
                ((InterfaceTextShortcuts) focusedItem).pasteText(paste_str);
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

        // if (!flag_text_input)
        // return;
        // flag_text_input = false;

        _tooltip.initTimer(false);
        _tiargs.character = character;
        _tiargs.mods = KeyMods.getEnum(mods);
        if (focusedItem != null) {
            if (focusedItem.eventTextInput != null)
                focusedItem.eventTextInput.execute(focusedItem, _tiargs);
        }
    }

    private void assignActions(InputEventType action, InputEventArgs args, Boolean only_last) {
        if (only_last && !hoveredItem.isDisabled()) {
            EventTask task = new EventTask();
            task.item = hoveredItem;
            task.action = action;
            task.args = args;
            _handler.getLayout().setEventTask(task);
        } else {
            Deque<Prototype> tmp;
            tmp = new ArrayDeque<>(hoveredItems);
            while (!tmp.isEmpty()) {
                Prototype item = tmp.pollLast();
                if (item.equals(hoveredItem) && hoveredItem.isDisabled())
                    continue;// пропустить

                EventTask task = new EventTask();
                task.item = item;
                task.action = action;
                task.args = args;
                _handler.getLayout().setEventTask(task);
                if (!item.isPassEvents())
                    break;
            }
        }
        _handler.getLayout().executePollActions();
    }

    private void assignActions(InputEventType action, InputEventArgs args, Prototype sender) {
        if (sender.isDisabled())
            return;

        EventTask task = new EventTask();
        task.item = sender;
        task.action = action;
        task.args = args;

        _handler.getLayout().setEventTask(task);
        _handler.getLayout().executePollActions();
    }

    protected float _interval = 1.0f / 30.0f;// 1000 / 60;
    // internal float _interval = 1.0f / 60.0f;//1000 / 60;
    // internal int _interval = 11;//1000 / 90;
    // internal int _interval = 08;//1000 / 120;

    // private int gVAO = 0;

    VRAMFramebuffer _fbo = new VRAMFramebuffer();

    protected void run() {
        _handler.gVAO = glGenVertexArrays();
        glBindVertexArray(_handler.gVAO);
        focus(_handler.getWindowId(), true);

        _fbo.genFBO();
        _fbo.genFBOTexture(_handler.getLayout().getWidth(), _handler.getLayout().getHeight());
        _fbo.unbindFBO();

        while (!_handler.isClosing()) {
            glfwWaitEventsTimeout(_interval);
            // glfwWaitEvents();
            // glfwPollEvents();

            // glClearColor((float) _handler.getLayout().getBackground().getRed() / 255.0f,
            // (float) _handler.getLayout().getBackground().getGreen() / 255.0f,
            // (float) _handler.getLayout().getBackground().getBlue() / 255.0f, 1.0f);
            glClearColor(0, 0, 0, 0);
            _primitive.useShader();
            update();
            _handler.swap();

            flag_move = true;
        }
        _primitive.deleteShader();
        _texture.deleteShader();
        _char.deleteShader();
        _fxaa.deleteShader();
        _blur.deleteShader();

        _fbo.clearFBO();

        glDeleteVertexArrays(_handler.gVAO);

        _handler.clearEventsCallbacks();
        _handler.destroy();
    }

    int _textlinecount = 0;

    protected void update() {
        glViewport(0, 0, _handler.getLayout().getWidth(), _handler.getLayout().getHeight());
        render();
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

        // draw static
        drawItems(_handler.getLayout().getWindow());
        // draw float
        for (InterfaceBaseItem item : ItemsLayoutBox.getLayout(_handler.getLayout().getId()).getFloatItems())
            drawItems((InterfaceBaseItem) item);
        // draw tooltip if needed
        drawToolTip();
        if (!_handler.focusable) {
            drawShadePillow();
        }
    }

    void drawFBO(int fbo_texture, Shader shader) {
        float i_x0 = -1.0f;
        float i_y0 = 1.0f;
        float i_x1 = 1.0f;
        float i_y1 = -1.0f;
        float[] vertexData = new float[] {
                // X Y Z //U V
                i_x0, i_y0, 0.0f, 0.0f, 1.0f, // x0
                i_x0, i_y1, 0.0f, 0.0f, 0.0f, // x1
                i_x1, i_y1, 0.0f, 1.0f, 0.0f, // x2
                i_x1, i_y0, 0.0f, 1.0f, 1.0f, // x3
        };
        int[] ibo = new int[] { 0, 1, 2, // first triangle
                2, 3, 0, // second triangle
        };

        int vertexbuffer = glGenBuffers();
        int elementbuffer = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_DYNAMIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementbuffer);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ibo, GL_DYNAMIC_DRAW);

        // Position attribute
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * 4, 0);
        glEnableVertexAttribArray(0);
        // TexCoord attribute
        glVertexAttribPointer(1, 2, GL_FLOAT, true, 5 * 4, 0 + (3 * 4));
        glEnableVertexAttribArray(1);

        int location = glGetUniformLocation((int) shader.getProgramID(), "tex");
        if (location >= 0) {
            glUniform1i(location, 0);
        }

        int location_size = glGetUniformLocation((int) shader.getProgramID(), "frame");
        if (location_size >= 0) {
            glUniform2f(location_size, (float) _handler.getLayout().getWidth(),
                    (float) _handler.getLayout().getHeight());
        }

        int location_enable = glGetUniformLocation((int) shader.getProgramID(), "is_enable");
        if (location_enable >= 0) {
            glUniform1i(location_enable, 0);
        }

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glDeleteBuffers(vertexbuffer);
        glDeleteBuffers(elementbuffer);
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
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_DYNAMIC_DRAW);

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

        glBufferData(GL_ARRAY_BUFFER, colorData, GL_DYNAMIC_DRAW);
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

    private boolean checkOutsideBorders(InterfaceBaseItem shell) {
        // if (shell.CutBehaviour == StencilBehaviour.Strict)
        // if(shell.getParent() != null)
        // _isStencilSet = shell;
        // StrictStencil(shell);
        // else
        // System.out.println(shell.getItemName());
        return lazyStencil(shell);
    }

    private void strictStencil(InterfaceBaseItem shell) {
        glEnable(GL_STENCIL_TEST);

        glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
        glClear(GL_STENCIL_BUFFER_BIT);
        glStencilMask(0x00);

        glStencilFunc(GL_ALWAYS, 1, 0xFF);
        glStencilMask(0xFF);

        _primitive.useShader();
        setStencilMask(shell.getParent().makeShape());

        glStencilFunc(GL_EQUAL, 1, 0xFF);

        shell.getParent().setConfines(shell.getParent().getX() + shell.getParent().getPadding().left,
                shell.getParent().getX() + shell.getParent().getWidth() - shell.getParent().getPadding().right,
                shell.getParent().getY() + shell.getParent().getPadding().top,
                shell.getParent().getY() + shell.getParent().getHeight() - shell.getParent().getPadding().bottom);
        setConfines(shell);
    }

    private void setConfines(InterfaceBaseItem shell) {
        int[] confines = shell.getParent().getConfines();
        shell.setConfines(confines[0], confines[1], confines[2], confines[3]);

        if (shell instanceof Prototype) {
            Prototype root = (Prototype) shell;
            for (InterfaceBaseItem item : root.getItems()) {
                setConfines(item);
            }
        }
    }

    private Boolean lazyStencil(InterfaceBaseItem shell) {
        Map<ItemAlignment, int[]> outside = new HashMap<ItemAlignment, int[]>();

        if (shell.getParent() != null && _isStencilSet == null) {
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
            }
            // left
            if (shell.getParent().getX() + shell.getParent().getPadding().left > shell.getX()) {
                // match
                int x = shell.getX();
                int w = shell.getParent().getX() + shell.getParent().getPadding().left - shell.getX();
                outside.put(ItemAlignment.LEFT, new int[] { x, w });
            }

            if (outside.size() > 0 || shell.getParent() instanceof TextBlock) {
                _isStencilSet = shell;
                strictStencil(shell);
                return true;
            }
        }
        return false;
    }

    private void drawItems(InterfaceBaseItem root) {
        if (!root.isVisible() || !root.isDrawable())
            return;

        if (root instanceof InterfaceLine) {
            drawLines((InterfaceLine) root);
        }
        if (root instanceof InterfacePoints) {
            drawPoints((InterfacePoints) root);
        }
        if (root instanceof InterfaceTextContainer) {
            _char.useShader();
            drawText((InterfaceTextContainer) root);
            _textlinecount++;
            _primitive.useShader();
            if (_isStencilSet == root) {
                glDisable(GL_STENCIL_TEST);
                _isStencilSet = null;
            }
        }
        if (root instanceof ImageItem) {
            _primitive.useShader();
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

            if (root instanceof Prototype) {
                List<InterfaceBaseItem> list = new LinkedList<>(((Prototype) root).getItems());
                for (InterfaceBaseItem child : list) {
                    drawItems(child);
                }
            }
            if (_isStencilSet == root) {
                glDisable(GL_STENCIL_TEST);
                _isStencilSet = null;
            }
        }
    }

    private void drawShell(InterfaceBaseItem shell) {
        drawShell(shell, false);
    }

    private void drawShell(InterfaceBaseItem shell, boolean ignore_borders) {
        if (!ignore_borders)
            checkOutsideBorders(shell);

        if (shell.getBackground().getAlpha() == 0) {
            if (shell instanceof Prototype)
                drawBorder((Prototype) shell);
            return;
        }
        // Vertex
        List<float[]> crd_array = shell.makeShape();
        if (crd_array == null)
            return;

        // shadow draw
        if (shell.isShadowDrop()) {
            drawShadow(shell);
            _primitive.useShader();
        }

        VRAMVertex store = new VRAMVertex();
        store.genBuffers(crd_array, shell.getBackground());
        store.draw(GL_TRIANGLES);
        store.clear();

        // clear array
        crd_array.clear();

        if (shell instanceof Prototype)
            drawBorder((Prototype) shell);
    }

    void drawBorder(Prototype vi) {
        if (vi.getBorderThickness() > 0) {
            CustomShape border = new CustomShape();
            border.setBackground(vi.getBorderFill());
            border.setSize(vi.getWidth(), vi.getHeight());
            border.setPosition(vi.getX(), vi.getY());
            border.setParent(vi);
            border.setHandler(vi.getHandler());

            border.setTriangles(GraphicsMathService.getRoundSquareBorder(vi.getBorderRadius(), vi.getWidth(),
                    vi.getHeight(), vi.getBorderThickness(), 0, 0));
            drawShell(border, true);
        }
    }

    void drawShadow(InterfaceBaseItem shell) {
        CustomShape shadow = new CustomShape();
        shadow.setBackground(shell.getShadowColor());
        shadow.setSize(shell.getWidth(), shell.getHeight());
        shadow.setPosition(shell.getX() + shell.getShadowPos().getX(), shell.getY() + shell.getShadowPos().getY());
        shadow.setParent(shell.getParent());
        shadow.setHandler(shell.getHandler());
        shadow.setTriangles(shell.getTriangles());

        _fbo.bindFBO();
        glClear(GL_COLOR_BUFFER_BIT);
        drawShell(shadow, true);

        int res = shell.getShadowRadius();
        float[] weights = new float[11];
        float sum, sigma2 = 4.0f;
        weights[0] = gauss(0, sigma2);
        sum = weights[0];
        for (int i = 1; i < 11; i++) {
            weights[i] = gauss(i, sigma2);
            sum += 2 * weights[i];
        }
        for (int i = 0; i < 11; i++)
            weights[i] /= sum;

        _fbo.unbindFBO();
        drawShadowPart(weights, res, _fbo.texture,
                new float[] { shell.getX() + shell.getShadowPos().getX(), shell.getY() + shell.getShadowPos().getY() },
                new float[] { shell.getWidth(), shell.getHeight() });

    }

    private void drawShadowPartTmp(float[] weights, int res, int[] xy, int[] wh) {
        float x0 = xy[0];
        float x1 = xy[0] + wh[0];
        float y0 = xy[1];
        float y1 = xy[1] + wh[1];
        float[][] image = new float[wh[0] + 2 * res][wh[1] + 2 * res];
        for (int i = res; i < wh[0] + res; i++) {
            for (int j = res; j < wh[1] + res; j++) {
                image[i][j] = 1f;
            }
        }

        int h = image.length;
        int w = image[0].length;
        float[][] outarr = new float[h][w];
        double tmp, ctmp;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                tmp = image[i][j] * weights[0];
                for (int k = -res; k <= res; k++) {
                    if (i + k < 0 || i + k >= h)
                        ctmp = image[i][j];
                    else ctmp = image[i + k][j];

                    tmp += (weights[Math.abs(k)]) * ctmp;

                }
                outarr[i][j] = (float)tmp;
            }
        }
        shapePart(outarr, res, weights);
    }

    private float[][] shapePart(float[][] image, int res, float[] weights) {
        int h = image.length;
        int w = image[0].length;
        float [][] outarr = new float[h][w];
        double tmp, ctmp;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                tmp = 0;
                for (int l = -res; l <= res; l++) {
                    if (j + l < 0 || j + l >= w)
                        ctmp = image[i][j];
                    else ctmp = image[i][j + l];

                    tmp += (weights[Math.abs(l)]) * ctmp;
                }

                outarr[i][j] = (float)tmp;
            }
        }
        return outarr;
    }

    private void drawShadowPart(float[] weights, int res, int fbo_texture, float[] xy, float[] wh) {
        _blur.useShader();
        float i_x0 = -1.0f;
        float i_y0 = 1.0f;
        float i_x1 = 1.0f;
        float i_y1 = -1.0f;
        VRAMTexture store = new VRAMTexture();
        store.genBuffers(i_x0, i_x1, i_y0, i_y1);
        store.bind(fbo_texture);
        store.sendUniformSample2D(_blur);
        store.sendUniform1fv(_blur, "weights", 11, weights);
        store.sendUniform2fv(_blur, "frame",
                new float[] { _handler.getLayout().getWidth(), _handler.getLayout().getHeight() });
        store.sendUniform1f(_blur, "res", (res * 1f / 10));
        store.sendUniform2fv(_blur, "xy", xy);
        store.sendUniform2fv(_blur, "wh", wh);
        store.draw();
        store.clear();
    }

    float gauss(float x, float sigma) {
        double ans;
        ans = Math.exp(-(x * x) / (2f * sigma * sigma)) / Math.sqrt(2 * Math.PI * sigma * sigma);
        return (float) ans;
    }

    void drawText(InterfaceTextContainer text) {
        TextPrinter textPrt = text.getLetTextures();
        if (textPrt == null)
            return;
        ByteBuffer bb = textPrt.texture;
        if (bb == null || bb.limit() == 0)
            return;

        if (checkOutsideBorders((InterfaceBaseItem) text))
            _char.useShader();

        int bb_h = textPrt.heightTexture, bb_w = textPrt.widthTexture;
        float i_x0 = ((float) textPrt.xTextureShift / (float) _handler.getLayout().getWidth() * 2.0f) - 1.0f;
        float i_y0 = ((float) textPrt.yTextureShift / (float) _handler.getLayout().getHeight() * 2.0f - 1.0f) * (-1.0f);
        float i_x1 = (((float) textPrt.xTextureShift + (float) bb_w) / (float) _handler.getLayout().getWidth() * 2.0f)
                - 1.0f;
        float i_y1 = (((float) textPrt.yTextureShift + (float) bb_h) / (float) _handler.getLayout().getHeight() * 2.0f
                - 1.0f) * (-1.0f);
        float[] argb = { (float) text.getForeground().getRed() / 255.0f,
                (float) text.getForeground().getGreen() / 255.0f, (float) text.getForeground().getBlue() / 255.0f,
                (float) text.getForeground().getAlpha() / 255.0f };

        VRAMTexture store = new VRAMTexture();
        store.genBuffers(i_x0, i_x1, i_y0, i_y1, true);
        store.genTexture(bb_w, bb_h, bb);
        store.sendUniformSample2D(_char);
        store.sendUniform4f(_char, "rgb", argb);
        store.draw();
        store.clear();
    }

    private void drawPoints(InterfacePoints item) {
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

        VRAMVertex store = new VRAMVertex();
        store.genBuffers(result, item.getPointColor());
        store.draw(GL_TRIANGLES);
        store.clear();
    }

    private void drawLines(InterfaceLine item) {
        if (item.getLineColor().getAlpha() == 0)
            return;

        List<float[]> crd_array = GraphicsMathService.toGL(item.makeShape(), _handler.getLayout());
        if (crd_array == null)
            return;
        checkOutsideBorders((InterfaceBaseItem) item);

        VRAMVertex store = new VRAMVertex();
        store.genBuffers(crd_array, item.getLineColor());
        store.draw(GL_LINE_STRIP);
        store.clear();
    }

    private void drawImage(ImageItem image) {
        if (checkOutsideBorders((InterfaceBaseItem) image))
            _texture.useShader();

        byte[] bitmap = image.getPixMapImage();
        if (bitmap == null)
            return;

        int w = image.getImageWidth(), h = image.getImageHeight();
        float i_x0 = ((float) image.getX() / (float) _handler.getLayout().getWidth() * 2.0f) - 1.0f;
        float i_y0 = ((float) image.getY() / (float) _handler.getLayout().getHeight() * 2.0f - 1.0f) * (-1.0f);
        float i_x1 = (((float) image.getX() + (float) image.getWidth()) / (float) _handler.getLayout().getWidth()
                * 2.0f) - 1.0f;
        float i_y1 = (((float) image.getY() + (float) image.getHeight()) / (float) _handler.getLayout().getHeight()
                * 2.0f - 1.0f) * (-1.0f);

        VRAMTexture store = new VRAMTexture();
        store.genBuffers(i_x0, i_x1, i_y0, i_y1);
        store.genTexture(w, h, bitmap);
        store.sendUniformSample2D(_texture);
        store.draw();
        store.clear();
    }

    private void drawToolTip() {
        if (!_tooltip.isVisible())
            return;

        _tooltip.setText(hoveredItem.getToolTip());
        _tooltip.setWidth(_tooltip.getPadding().left + _tooltip.getPadding().right + _tooltip.getTextWidth());

        // проверка сверху
        if (ptrRelease.getY() > _tooltip.getHeight()) {
            _tooltip.setY(ptrRelease.getY() - _tooltip.getHeight() - 2);
        } else {
            _tooltip.setY(ptrRelease.getY() + _tooltip.getHeight() + 2);
        }
        // проверка справа
        if (ptrRelease.getX() - 10 + _tooltip.getWidth() > _handler.getLayout().getWidth()) {
            _tooltip.setX(_handler.getLayout().getWidth() - _tooltip.getWidth() - 10);
        } else {
            _tooltip.setX(ptrRelease.getX() - 10);
        }

        drawShell(_tooltip);

        _tooltip.getTextLine().updateGeometry();
        _char.useShader();
        drawText(_tooltip.getTextLine());
        _primitive.useShader();
        if (_isStencilSet == _tooltip.getTextLine()) {
            glDisable(GL_STENCIL_TEST);
            _isStencilSet = null;
        }
    }

    private void drawShadePillow() {
        // //Vertex
        List<float[]> vertex = new LinkedList<float[]>();
        vertex.add(new float[] { -1.0f, 1.0f, 0.0f });
        vertex.add(new float[] { -1.0f, -1.0f, 0.0f });
        vertex.add(new float[] { 1.0f, -1.0f, 0.0f });
        vertex.add(new float[] { 1.0f, -1.0f, 0.0f });
        vertex.add(new float[] { 1.0f, 1.0f, 0.0f });
        vertex.add(new float[] { -1.0f, 1.0f, 0.0f });
        VRAMVertex store = new VRAMVertex();
        store.genBuffers(vertex, new Color(0, 0, 0, 150));
        store.draw(GL_TRIANGLES);
        store.clear();
    }
}
