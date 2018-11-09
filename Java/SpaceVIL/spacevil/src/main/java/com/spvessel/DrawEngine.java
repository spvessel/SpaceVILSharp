package com.spvessel;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
// import java.util.concurrent.SynchronousQueue;
import java.util.*;
import java.io.*;
// import java.lang.ref.SoftReference;
import java.nio.*;

import com.spvessel.Common.CommonService;
import com.spvessel.Core.*;
import com.spvessel.Flags.*;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
// import static org.lwjgl.opengl.GL13.*;
// import static org.lwjgl.system.MemoryUtil.*;

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
    private InterfaceBaseItem _isStencilSet = null;
    public InputDeviceEvent engineEvent = new InputDeviceEvent();
    private MouseArgs _margs = new MouseArgs();
    private KeyArgs _kargs = new KeyArgs();
    private TextInputArgs _tiargs = new TextInputArgs();

    private List<Prototype> hoveredItems;
    private Prototype hoveredItem = null;
    private Prototype focusedItem = null;

    public void setFocusedItem(Prototype item) {
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

    public GLWHandler _handler;
    private Shader _primitive;
    private Shader _texture;
    private Shader _char;
    private Shader _fxaa;
    private Shader _blur;

    public DrawEngine(WindowLayout handler) {
        hoveredItems = new LinkedList<Prototype>();
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

    private GLFWImage _iconSmall;
    private GLFWImage _iconBig;

    public void setBigIcon(BufferedImage icon) {
        _iconBig = GLFWImage.create();
        _iconBig.width(icon.getWidth());
        _iconBig.height(icon.getHeight());
        _iconBig.pixels(createByteImage(icon));
    }

    public void setSmallIcon(BufferedImage icon) {
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

    public void applyIcon() {
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

    public void init() {
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

    public void minimizeWindow() {
        engineEvent.setEvent(InputEventType.WINDOW_MINIMIZE);
        glfwIconifyWindow(_handler.getWindowId());
    }

    public void maximizeWindow() {
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

    public void focus(long wnd, boolean value) {
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
        // System.out.println("resize " + count + " " + width + " " + height);
        _tooltip.initTimer(false);
        _handler.getLayout().setWidth(width);
        _handler.getLayout().setHeight(height);
    }

    public void setWindowSize(int w, int h) {
        glfwSetWindowSize(_handler.getWindowId(), w, h);
        engineEvent.setEvent(InputEventType.WINDOW_RESIZE);
    }

    private void position(long wnd, int xpos, int ypos) {
        // if (!flag_pos)
        // return;
        // flag_pos = false;
        // System.out.println("pos " + count + " " + xpos + " " + ypos);
        _handler.getPointer().setX(xpos);
        _handler.getPointer().setY(ypos);
    }

    public void setWindowPos(int x, int y) {
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

                    // Focus get
                    // if (focusedItem != null)
                    // focusedItem.setFocused(false);

                    // focusedItem = hoveredItem;
                    // focusedItem.setFocused(true);
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

                    // Point mpos = MouseInfo.getPointerInfo().getLocation();
                    // int x5, y5;
                    // x5 = (mpos.x - x_click); // x_global - (x_click - (int)xpos);
                    // y5 = (mpos.y - y_click); // y_global - (y_click - (int)ypos);
                    // x_handler = x5;
                    // y_handler = y5;
                    // setWindowPos(x_handler, y_handler);
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

        // System.out.println("click " + count + " " + button + " " + action);

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

            // System.out.println(item.getItemName() + " " +
            // layout_box_of_items.contains(item));
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
                if (!item.getPassEvents())
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
            if (!item.getPassEvents())
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
                if (!item.getPassEvents())
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

    public float _interval = 1.0f / 30.0f;// 1000 / 60;
    // internal float _interval = 1.0f / 60.0f;//1000 / 60;
    // internal int _interval = 11;//1000 / 90;
    // internal int _interval = 08;//1000 / 120;

    // private int gVAO = 0;

    public void run() {
        _handler.gVAO = glGenVertexArrays();
        glBindVertexArray(_handler.gVAO);
        focus(_handler.getWindowId(), true);

        // int fbo_handle = glGenFramebuffersEXT();
        // int fbo_texture = glGenTextures();
        // int stencil_rb = glGenRenderbuffersEXT();
        // glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fbo_handle);

        // glBindTexture(GL_TEXTURE_2D, fbo_texture);
        // glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        // glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT,
        // GL_TEXTURE_2D, fbo_texture, 0);
        // glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, stencil_rb);
        // glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_STENCIL_ATTACHMENT_EXT,
        // GL_RENDERBUFFER_EXT, stencil_rb);
        // glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);

        while (!_handler.isClosing()) {
            glfwWaitEventsTimeout(_interval);
            // glfwWaitEvents();
            // glfwPollEvents();

            glClearColor((float) _handler.getLayout().getBackground().getRed() / 255.0f,
                    (float) _handler.getLayout().getBackground().getGreen() / 255.0f,
                    (float) _handler.getLayout().getBackground().getBlue() / 255.0f, 1.0f);
            // glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, _handler.getLayout().getWidth(),
            // _handler.getLayout().getHeight(),
            // 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
            // glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_STENCIL_INDEX,
            // _handler.getLayout().getWidth(),
            // _handler.getLayout().getHeight());

            // glBindTexture(GL_TEXTURE_2D, 0);
            // glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fbo_handle);
            _primitive.useShader();
            update();
            // glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
            // glActiveTexture(GL_TEXTURE0);
            // glBindTexture(GL_TEXTURE_2D, fbo_texture);
            // _fxaa.useShader();
            // drawFBO(fbo_texture, _fxaa);
            _handler.swap();

            flag_move = true;
        }
        _primitive.deleteShader();
        _texture.deleteShader();
        _char.deleteShader();
        _fxaa.deleteShader();
        _blur.deleteShader();

        // glDeleteFramebuffersEXT(fbo_handle);
        // glDeleteTextures(fbo_texture);
        glDeleteVertexArrays(_handler.gVAO);

        _handler.clearEventsCallbacks();
        _handler.destroy();
    }

    int _textlinecount = 0;

    public void update() {
        // if (_handler.focused) {
        glViewport(0, 0, _handler.getLayout().getWidth(), _handler.getLayout().getHeight());
        render();
        // }
    }

    private void render() {
        // fbo texture
        // int fbo_handle = glGenFramebuffersEXT();
        // int fbo_texture = glGenTextures();
        // int stencil_rb = glGenRenderbuffersEXT();
        // glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fbo_handle);

        // glBindTexture(GL_TEXTURE_2D, fbo_texture);
        // glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        // glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT,
        // GL_TEXTURE_2D, fbo_texture, 0);
        // glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, stencil_rb);
        // glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_STENCIL_ATTACHMENT_EXT,
        // GL_RENDERBUFFER_EXT, stencil_rb);
        // glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);

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
            // glBindTexture(GL_TEXTURE_2D, fbo_texture);
            // _blur.useShader();
            // drawFBO(fbo_texture, _blur);
        }
    }

    void drawFBO(int fbo_texture, Shader shader) {
        // glDisable(org.lwjgl.opengl.EXTFramebufferSRGB.GL_FRAMEBUFFER_SRGB_EXT);
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
        // glEnable(GL_STENCIL_TEST);
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

        // System.out.println(shell.getParent().getItemName());
        _primitive.useShader();
        setStencilMask(shell.getParent().makeShape());

        glStencilFunc(GL_EQUAL, 1, 0xFF);

        // System.out.println(shell.getParent()._confines_x_0 + " " +
        // shell.getParent()._confines_x_1 + " "
        // + shell.getParent()._confines_y_0 + " " + shell.getParent()._confines_y_1 + "
        // ");

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
                // if (shell instanceof TreeItem)
                // System.out.println("treeitem _ bottom");
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
                // if (shell instanceof TreeItem)
                // System.out.println("treeitem _ right");
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
                // System.out.println(shell.getItemName() + " stenciltest");
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

        if (shell.getBackground().getAlpha() == 0)
            return;
        // Vertex
        List<float[]> crd_array = shell.makeShape();
        if (crd_array == null)
            return;

        // shadow draw
        if (shell.isShadowDrop()) {
            drawShadow(shell);
            _primitive.useShader();
        }

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
        glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_DYNAMIC_DRAW);

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

        glBufferData(GL15.GL_ARRAY_BUFFER, colorData, GL15.GL_DYNAMIC_DRAW);
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

        if (shell instanceof Prototype) {
            Prototype vi = (Prototype) shell;
            if (vi.getBorderThickness() > 0) {
                CustomShape border = new CustomShape();
                border.setBackground(vi.getBorderFill());
                border.setSize(vi.getWidth(), vi.getHeight());
                border.setPosition(vi.getX(), vi.getY());
                border.setParent(vi);
                border.setHandler(vi.getHandler());

                border.setTriangles(GraphicsMathService.getRoundSquareBorder(
                    vi.getBorderRadius(), 
                    vi.getWidth(), 
                    vi.getHeight(),
                    vi.getBorderThickness(), 0, 0));

                drawShell(border);
            }
        }
    }

    void drawShadow(InterfaceBaseItem shell) {
        int fbo_handle = glGenFramebuffersEXT();
        int fbo_texture = glGenTextures();
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fbo_handle);

        // texture
        glBindTexture(GL_TEXTURE_2D, fbo_texture);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, _handler.getLayout().getWidth(), _handler.getLayout().getHeight(), 0,
                GL_BGRA, GL_UNSIGNED_BYTE, 0);
        glBindTexture(GL_TEXTURE_2D, 0);

        // fbo
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fbo_handle);
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, fbo_texture, 0);

        int draw_bufs = GL_COLOR_ATTACHMENT0_EXT;
        glDrawBuffers(draw_bufs);

        int result = glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT);
        if (result != GL_FRAMEBUFFER_COMPLETE_EXT) {
            System.out.println("Framebuffer error " + result);
            glDeleteFramebuffersEXT(fbo_handle);
            glDeleteTextures(fbo_texture);
            return;
        }

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fbo_handle);
        //////////
        CustomShape shadow = new CustomShape();
        shadow.setBackground(shell.getShadowColor());
        shadow.setSize(shell.getWidth(), shell.getHeight());
        shadow.setPosition(shell.getX() + shell.getShadowPos().getX(), shell.getY() + shell.getShadowPos().getY());
        shadow.setParent(shell.getParent());
        shadow.setHandler(shell.getHandler());
        shadow.setTriangles(shell.getTriangles());
        drawShell(shadow, true);
        //////////

        int res = (int) shell.getShadowRadius();
        float[] weights = new float[res];
        float sum, sigma2 = 4.0f;
        weights[0] = gauss(0, sigma2);
        sum = weights[0];
        for (int i = 1; i < res; i++) {
            weights[i] = gauss(i, sigma2);
            sum += 2 * weights[i];
        }
        for (int i = 0; i < res; i++)
            weights[i] /= sum;

        //drawShadowPart(weights, res, fbo_texture, 1);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
        drawShadowPart(weights, res, fbo_texture);

        glDeleteFramebuffersEXT(fbo_handle);
        glDeleteTextures(fbo_texture);
    }

    private void drawShadowPart(float[] weights, int res, int fbo_texture) {
        _blur.useShader();
        float i_x0 = -1.0f;
        float i_y0 = 1.0f;
        float i_x1 = 1.0f;
        float i_y1 = -1.0f;
        // VBO
        float[] vertex = new float[] {
                // X Y Z //U V
                i_x0, i_y0, 0.0f, 0.0f, 1.0f, // x0
                i_x0, i_y1, 0.0f, 0.0f, 0.0f, // x1
                i_x1, i_y1, 0.0f, 1.0f, 0.0f, // x2
                i_x1, i_y0, 0.0f, 1.0f, 1.0f, // x3
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
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_DYNAMIC_DRAW);

        int elementbuffer = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementbuffer);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, iboData, GL_DYNAMIC_DRAW);

        // Position attribute
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * 4, 0);
        glEnableVertexAttribArray(0);
        // TexCoord attribute
        glVertexAttribPointer(1, 2, GL_FLOAT, true, 5 * 4, (3 * 4));
        glEnableVertexAttribArray(1);

        glBindTexture(GL_TEXTURE_2D, fbo_texture);
        // glActiveTexture(GL_TEXTURE0);

        int location = glGetUniformLocation((int) _blur.getProgramID(), "tex");
        if (location >= 0)
            glUniform1i(location, 0);
        int location_frame = glGetUniformLocation((int) _blur.getProgramID(), "frame");
        if (location_frame >= 0)
            glUniform2fv(location_frame,
                    new float[] { _handler.getLayout().getWidth(), _handler.getLayout().getHeight() });

        // int location_direction = glGetUniformLocation((int) _blur.getProgramID(),
        // "direction");
        // if (location_direction >= 0)
        // glUniform2fv(location_direction, new float[] { shell.getShadowRadius(),
        // shell.getShadowRadius() });

        int location_res = glGetUniformLocation((int) _blur.getProgramID(), "res");
        if (location_res >= 0)
            glUniform1i(location_res, res);

        int location_weights = glGetUniformLocation((int) _blur.getProgramID(), "weights");
        if (location_weights >= 0)
            glUniform1fv(location_weights, weights);

//        int location_isfirst = glGetUniformLocation((int) _blur.getProgramID(), "isFirst");
//        if (location_isfirst >= 0)
//            glUniform1i(location_isfirst, isFirst);

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
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

        int bb_h = textPrt.heightTexture;
        int bb_w = textPrt.widthTexture;

        float i_x0 = ((float) textPrt.xTextureShift / (float) _handler.getLayout().getWidth() * 2.0f) - 1.0f;
        float i_y0 = ((float) textPrt.yTextureShift / (float) _handler.getLayout().getHeight() * 2.0f - 1.0f) * (-1.0f);
        float i_x1 = (((float) textPrt.xTextureShift + (float) bb_w) / (float) _handler.getLayout().getWidth() * 2.0f)
                - 1.0f;
        float i_y1 = (((float) textPrt.yTextureShift + (float) bb_h) / (float) _handler.getLayout().getHeight() * 2.0f
                - 1.0f) * (-1.0f);

        // VBO
        float[] vertex = new float[] {
                // X Y Z //U V
                i_x0, i_y0, 0.0f, 0.0f, 0.0f, // x0
                i_x0, i_y1, 0.0f, 0.0f, 1.0f, // x1
                i_x1, i_y1, 0.0f, 1.0f, 1.0f, // x2
                i_x1, i_y0, 0.0f, 1.0f, 0.0f, // x3
        };

        int[] ibo = new int[] { 0, 1, 2, // first triangle
                2, 3, 0, // second triangle
        };

        int vertexbuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);
        glBufferData(GL_ARRAY_BUFFER, vertex, GL_DYNAMIC_DRAW);

        int elementbuffer = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementbuffer);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ibo, GL_DYNAMIC_DRAW);

        // Position attribute
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * 4, 0);
        glEnableVertexAttribArray(0);
        // TexCoord attribute
        glVertexAttribPointer(1, 2, GL_FLOAT, true, 5 * 4, (3 * 4));
        glEnableVertexAttribArray(1);

        // texture
        int w = bb_w, h = bb_h;

        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        GL42.glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, w, h);
        GL11.glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, w, h, GL_RGBA, GL_UNSIGNED_BYTE, bb);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        int location = glGetUniformLocation((int) _char.getProgramID(), "tex");
        glUniform1i(location, 0);

        float[] argb = { (float) text.getForeground().getRed() / 255.0f,
                (float) text.getForeground().getGreen() / 255.0f, (float) text.getForeground().getBlue() / 255.0f,
                (float) text.getForeground().getAlpha() / 255.0f };
        int location_rgb = glGetUniformLocation((int) _char.getProgramID(), "rgb");
        glUniform4f(location_rgb, argb[0], argb[1], argb[2], argb[3]);

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        // Clear VBO and shader
        glDeleteBuffers(vertexbuffer);
        glDeleteBuffers(elementbuffer);
        glDeleteTextures(texture);
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
        // long estimatedTime = (System.nanoTime() - startTime) / 1000000;
        // System.out.println(estimatedTime);

        int vertexbuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);
        int length = result.length;

        // FloatBuffer vertexData = BufferUtils.createFloatBuffer(length);
        // vertexData.put(result);
        // vertexData.rewind();

        glBufferData(GL_ARRAY_BUFFER, result, GL_DYNAMIC_DRAW);
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

        glBufferData(GL_ARRAY_BUFFER, colorData, GL_DYNAMIC_DRAW);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);

        checkOutsideBorders((InterfaceBaseItem) item);

        // draw
        glDrawArrays(GL_TRIANGLES, 0, result.length / 3);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        // Clear VBO and shader
        glDeleteBuffers(vertexbuffer);
        glDeleteBuffers(colorbuffer);

    }

    private void drawLines(InterfaceLine item) {
        if (item.getLineColor().getAlpha() == 0)
            return;

        List<float[]> crd_array = GraphicsMathService.toGL(item.makeShape(), _handler.getLayout());

        if (crd_array == null)
            return;
        checkOutsideBorders((InterfaceBaseItem) item);

        int vertexbuffer = glGenBuffers();
        glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexbuffer);

        int length = crd_array.size() * 3;
        FloatBuffer vertexData = BufferUtils.createFloatBuffer(length);
        for (int i = 0; i < length / 3; i++) {
            vertexData.put(i * 3 + 0, crd_array.get(i)[0]);
            vertexData.put(i * 3 + 1, crd_array.get(i)[1]);
            vertexData.put(i * 3 + 2, crd_array.get(i)[2]);
        }
        vertexData.rewind();
        glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_DYNAMIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        // Color
        float[] argb = { (float) item.getLineColor().getRed() / 255.0f, (float) item.getLineColor().getGreen() / 255.0f,
                (float) item.getLineColor().getBlue() / 255.0f, (float) item.getLineColor().getAlpha() / 255.0f };
        int colorbuffer = glGenBuffers();
        glBindBuffer(GL15.GL_ARRAY_BUFFER, colorbuffer);
        int c_length = crd_array.size() * 4;
        FloatBuffer colorData = BufferUtils.createFloatBuffer(c_length);
        for (int i = 0; i < c_length / 4; i++) {
            colorData.put(i * 4 + 0, argb[0]);
            colorData.put(i * 4 + 1, argb[1]);
            colorData.put(i * 4 + 2, argb[2]);
            colorData.put(i * 4 + 3, argb[3]);
        }
        colorData.rewind();

        glBufferData(GL15.GL_ARRAY_BUFFER, colorData, GL15.GL_DYNAMIC_DRAW);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);

        // draw
        GL15.glDrawArrays(GL15.GL_LINE_STRIP, 0, length / 3);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        // Clear VBO and shader
        glDeleteBuffers(vertexbuffer);
        glDeleteBuffers(colorbuffer);

        crd_array.clear();
    }

    private void drawImage(ImageItem image) {
        byte[] bitmap = image.getPixMapImage();

        if (bitmap == null)
            return;

        ByteBuffer bb = BufferUtils.createByteBuffer(bitmap.length);
        bb.put(bitmap);
        bb.rewind();
        if (checkOutsideBorders((InterfaceBaseItem) image))
            _texture.useShader();

        float i_x0 = ((float) image.getX() / (float) _handler.getLayout().getWidth() * 2.0f) - 1.0f;
        float i_y0 = ((float) image.getY() / (float) _handler.getLayout().getHeight() * 2.0f - 1.0f) * (-1.0f);
        float i_x1 = (((float) image.getX() + (float) image.getWidth()) / (float) _handler.getLayout().getWidth()
                * 2.0f) - 1.0f;
        float i_y1 = (((float) image.getY() + (float) image.getHeight()) / (float) _handler.getLayout().getHeight()
                * 2.0f - 1.0f) * (-1.0f);

        // VBO
        float[] vertex = new float[] {
                // X Y Z //U V
                i_x0, i_y0, 0.0f, 0.0f, 1.0f, // x0
                i_x0, i_y1, 0.0f, 0.0f, 0.0f, // x1
                i_x1, i_y1, 0.0f, 1.0f, 0.0f, // x2
                i_x1, i_y0, 0.0f, 1.0f, 1.0f, // x3
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
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_DYNAMIC_DRAW);

        int elementbuffer = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementbuffer);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, iboData, GL_DYNAMIC_DRAW);

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

        GL42.glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, w, h);
        GL11.glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, w, h, GL_RGBA, GL_UNSIGNED_BYTE, bb);
        GL30.glGenerateMipmap(GL_TEXTURE_2D);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST_MIPMAP_NEAREST);
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
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_DYNAMIC_DRAW);
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
        glBufferData(GL_ARRAY_BUFFER, colorData, GL_DYNAMIC_DRAW);
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
