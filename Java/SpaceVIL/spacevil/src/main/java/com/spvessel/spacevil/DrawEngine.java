package com.spvessel.spacevil;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.io.BufferedReader;
import java.io.*;
import java.nio.*;

import com.spvessel.spacevil.Common.*;
import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Flags.*;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;

final class DrawEngine {
    private Map<InterfaceBaseItem, int[]> _bounds = new HashMap<>();
    Prototype _draggable = null;
    // private ToolTip _tooltip = ToolTip.getInstance();
    private ToolTip _tooltip = new ToolTip();
    // private InterfaceBaseItem _isStencilSet = null;
    private InputDeviceEvent engineEvent = new InputDeviceEvent();
    private MouseArgs _margs = new MouseArgs();
    private KeyArgs _kargs = new KeyArgs();
    private TextInputArgs _tiargs = new TextInputArgs();

    private List<Prototype> hoveredItems;
    private List<Prototype> underFocusedItem;
    private Prototype hoveredItem = null;
    private Prototype focusedItem = null;

    private int _framebufferWidth = 0;
    private int _framebufferHeight = 0;

    Prototype getFocusedItem() {
        return focusedItem;
    }

    void setFocusedItem(Prototype item) {
        if (item == null) {
            focusedItem = null;
            return;
        }
        if (focusedItem != null && focusedItem.equals(item))
            return;

        if (focusedItem != null)
            focusedItem.setFocused(false);

        focusedItem = item;
        focusedItem.setFocused(true);
        findUnderFocusedItems(item);
    }

    private void findUnderFocusedItems(Prototype item) {
        Deque<Prototype> queue = new ArrayDeque<>();

        if (item == _handler.getCoreWindow().getLayout().getContainer()) {
            underFocusedItem = null;
            return;
        }

        Prototype parent = item.getParent();

        while (parent != null) {
            queue.addFirst(parent);
            parent = parent.getParent();
        }
        underFocusedItem = new LinkedList<Prototype>(queue);
        underFocusedItem.remove(focusedItem);
    }

    void resetFocus() {
        if (focusedItem != null)
            focusedItem.setFocused(false);
        // set focus to WContainer
        focusedItem = _handler.getCoreWindow().getLayout().getContainer();
        focusedItem.setFocused(true);
        if (underFocusedItem != null)
            underFocusedItem.clear();
    }

    void resetItems() {
        resetFocus();

        if (hoveredItem != null)
            hoveredItem.setMouseHover(false);
        hoveredItem = null;
        hoveredItems.clear();
    }

    private Pointer ptrPress = new Pointer();
    private Pointer ptrRelease = new Pointer();
    private Pointer ptrClick = new Pointer();

    GLWHandler _handler;
    private Shader _primitive;
    private Shader _texture;
    private Shader _char;
    // private Shader _fxaa;
    private Shader _blur;

    DrawEngine(CoreWindow handler) {
        hoveredItems = new LinkedList<Prototype>();
        _handler = new GLWHandler(handler);

        _tooltip.setHandler(handler);
        _tooltip.getTextLine().setHandler(handler);
        _tooltip.getTextLine().setParent(_tooltip);
        _tooltip.initElements();
    }

    void dispose() {
        glfwTerminate();
    }

    private BufferedImage _iconSmall;
    private BufferedImage _iconBig;

    void setBigIcon(BufferedImage icon) {
        _iconBig = icon;
    }

    void setSmallIcon(BufferedImage icon) {
        _iconSmall = icon;
    }

    private ByteBuffer createByteImage(BufferedImage image) {
        List<Byte> bmp = new LinkedList<Byte>();
        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
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

    void applyIcon() {
        GLFWImage.Buffer gb = GLFWImage.malloc(2);
        GLFWImage s = GLFWImage.malloc();
        s.set(_iconSmall.getWidth(), _iconSmall.getHeight(), createByteImage(_iconSmall));
        GLFWImage b = GLFWImage.malloc();
        b.set(_iconBig.getWidth(), _iconBig.getHeight(), createByteImage(_iconBig));
        gb.put(0, s);
        gb.put(1, b);

        glfwSetWindowIcon(_handler.getWindowId(), gb);

        gb.free();
        s.free();
        b.free();
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

    void init() {
        CommonService.GlobalLocker.lock();
        try {
            // InitWindow
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
            _handler.getCoreWindow().getLayout().close();
            return;
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

        ////////////////////////////////////////////////
        _primitive = new Shader(getResourceString("/shaders/vs_primitive.glsl"),
                getResourceString("/shaders/fs_primitive.glsl"));
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

        // _fxaa = new Shader(getResourceString("/shaders/vs_fxaa.glsl"),
        // getResourceString("/shaders/fs_fxaa.glsl"));
        // _fxaa.compile();
        // if (_fxaa.getProgramID() == 0)
        // System.out.println("Could not create fxaa shaders");

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
        _handler.setCallbackFramebuffer(new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                framebuffer(window, w, h);
            }
        });
        _handler.setCallbackRefresh(new GLFWWindowRefreshCallback() {
            @Override
            public void invoke(long window) {
                refresh(window);
            }
        });
        _handler.setCallbackDrop(new GLFWDropCallback() {
            @Override
            public void invoke(long window, int count, long paths) {
                drop(window, count, paths);
            }
        });
    }

    private void drop(long window, int count, long paths) {
        DropArgs dargs = new DropArgs();
        dargs.count = count;
        dargs.paths = new LinkedList<>();
        dargs.item = hoveredItem;

        for (int i = 0; i < count; i++) {
            String str = GLFWDropCallback.getName(paths, i);
            dargs.paths.add(str);
        }
        assignActions(InputEventType.WINDOW_DROP, dargs, _handler.getCoreWindow().getLayout().getContainer(), false);
    }

    // public static void printBytes(byte[] array, String name) {
    // for (int k = 0; k < array.length; k++) {
    // System.out.println(name + "[" + k + "] = " + "0x" + byteToHex(array[k]));
    // }
    // }

    // static public String byteToHex(byte b) {
    // // Returns hex String representation of byte b
    // char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
    // 'b', 'c', 'd', 'e', 'f' };
    // char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
    // return new String(array);
    // }

    private void refresh(long window) {
        update();
        _handler.swap();
    }

    private void framebuffer(long window, int w, int h) {
        _framebufferWidth = w;
        _framebufferHeight = h;
        // _framebufferWidth = w * 2;
        // _framebufferHeight = h * 2;

        glViewport(0, 0, _framebufferWidth, _framebufferHeight);

        _fbo.bindFBO();
        _fbo.clearTexture();
        _fbo.genFBOTexture(_framebufferWidth, _framebufferHeight);
        _fbo.unbindFBO();
    }

    private boolean _inputLocker = false;

    void minimizeWindow() {
        _inputLocker = true;
        engineEvent.setEvent(InputEventType.WINDOW_MINIMIZE);
        glfwIconifyWindow(_handler.getWindowId());
        _inputLocker = false;
    }

    boolean maximizeRequest = false;

    void maximizeWindow() {
        _inputLocker = true;
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        engineEvent.setEvent(InputEventType.WINDOW_RESTORE);

        if (_handler.getCoreWindow().isMaximized) {
            glfwRestoreWindow(_handler.getWindowId());
            _handler.getCoreWindow().isMaximized = false;
            glfwGetWindowSize(_handler.getWindowId(), w, h);
            _handler.getCoreWindow().setWidth(w.get(0));
            _handler.getCoreWindow().setHeight(h.get(0));
        } else {
            glfwMaximizeWindow(_handler.getWindowId());
            _handler.getCoreWindow().isMaximized = true;
            glfwGetWindowSize(_handler.getWindowId(), w, h);
            _handler.getCoreWindow().setWidth(w.get(0));
            _handler.getCoreWindow().setHeight(h.get(0));
        }
        _inputLocker = false;
    }

    private void closeWindow(long wnd) {
        glfwSetWindowShouldClose(_handler.getWindowId(), false);
        _handler.getCoreWindow().eventClose.execute();
        // _handler.getCoreWindow().close();
    }

    void focus(long wnd, boolean value) {
        engineEvent.resetAllEvents();
        _tooltip.initTimer(false);
        _handler.getCoreWindow().isFocused = value;
        if (value) {
            if (_handler.focusable) {
                WindowsBox.setCurrentFocusedWindow(_handler.getCoreWindow());
                _handler.focused = value;
            }
        } else {
            if (_handler.getCoreWindow().isDialog) {
                _handler.focused = true;
            } else {
                _handler.focused = value;
                if (_handler.getCoreWindow().isOutsideClickClosable) {
                    resetItems();
                    _handler.getCoreWindow().close();
                }
            }
        }
    }

    private void resize(long wnd, int width, int height) {
        // if (!flag_resize)
        // return;
        // flag_resize = false;

        _tooltip.initTimer(false);
        _handler.getCoreWindow().setWidth(width);
        _handler.getCoreWindow().setHeight(height);

        // if (engineEvent.lastEvent().contains(InputEventType.WINDOW_RESTORE))
        // return;

        // if (!_handler.getCoreWindow().isBorderHidden) {
        // // glClearColor(0, 0, 0, 0);
        // update();
        // _handler.swap();
        // }
    }

    void setWindowSize(int w, int h) {
        if (_handler.getCoreWindow().isKeepAspectRatio) {
            float currentW = w;
            float currentH = h;

            float ratioW = _handler.getCoreWindow().ratioW;
            float ratioH = _handler.getCoreWindow().ratioH;

            float xScale = (currentW / ratioW);
            float yScale = (currentH / ratioH);

            float scale = 0;

            if ((_handler.getCoreWindow().getLayout().getContainer()._sides.contains(Side.RIGHT)
                    && _handler.getCoreWindow().getLayout().getContainer()._sides.contains(Side.TOP))
                    || (_handler.getCoreWindow().getLayout().getContainer()._sides.contains(Side.RIGHT)
                            && _handler.getCoreWindow().getLayout().getContainer()._sides.contains(Side.BOTTOM))
                    || (_handler.getCoreWindow().getLayout().getContainer()._sides.contains(Side.LEFT)
                            && _handler.getCoreWindow().getLayout().getContainer()._sides.contains(Side.TOP))
                    || (_handler.getCoreWindow().getLayout().getContainer()._sides.contains(Side.LEFT)
                            && _handler.getCoreWindow().getLayout().getContainer()._sides.contains(Side.BOTTOM))
                    || _handler.getCoreWindow().getLayout().getContainer()._sides.contains(Side.LEFT)
                    || _handler.getCoreWindow().getLayout().getContainer()._sides.contains(Side.RIGHT))
                scale = xScale;
            else
                scale = yScale;

            w = (int) (ratioW * scale);
            h = (int) (ratioH * scale);
        }
        glfwSetWindowSize(_handler.getWindowId(), w, h);
        engineEvent.setEvent(InputEventType.WINDOW_RESIZE);
    }

    private void position(long wnd, int xpos, int ypos) {
        // if (!flag_pos)
        // return;
        // flag_pos = false;

        _handler.getPointer().setX(xpos);
        _handler.getPointer().setY(ypos);

        _handler.getCoreWindow().setX(xpos);
        _handler.getCoreWindow().setY(ypos);
    }

    void setWindowPos(int x, int y) {
        glfwSetWindowPos(_handler.getWindowId(), x, y);
        engineEvent.setEvent(InputEventType.WINDOW_MOVE);
    }

    boolean flag_move = false;
    // boolean flag_text_input = false;
    // boolean flag_click = false;
    // boolean flag_pos = false;
    // boolean flag_resize = false;

    private void mouseMove(long wnd, double xpos, double ypos) {
        if (_inputLocker)
            return;
        engineEvent.setEvent(InputEventType.MOUSE_MOVE);
        if (!flag_move)
            return;
        flag_move = false;

        _tooltip.initTimer(false);

        if (!_handler.focusable)
            return;

        // logic of hovers
        ptrRelease.setX((int) xpos);
        ptrRelease.setY((int) ypos);

        _margs.position.setPosition((float) xpos, (float) ypos);

        if (engineEvent.lastEvent().contains(InputEventType.MOUSE_PRESS)) {
            if (_handler.getCoreWindow().isBorderHidden && _handler.getCoreWindow().isResizable) {
                int w = _handler.getCoreWindow().getWidth();
                int h = _handler.getCoreWindow().getHeight();

                int x_handler = _handler.getPointer().getX();
                int y_handler = _handler.getPointer().getY();
                int x_release = ptrRelease.getX();
                int y_release = ptrRelease.getY();
                int x_press = ptrPress.getX();
                int y_press = ptrPress.getY();

                if (_handler.getCoreWindow().getLayout().getContainer()._sides.contains(Side.LEFT)) {
                    if (!(_handler.getCoreWindow().getMinWidth() == _handler.getCoreWindow().getWidth()
                            && (x_release - x_press) >= 0)) {
                        int x5 = x_handler - x_global + (int) xpos - 5;
                        x_handler = x_global + x5;
                        w = w_global - x5;
                    }
                }
                if (_handler.getCoreWindow().getLayout().getContainer()._sides.contains(Side.RIGHT)) {
                    if (!(x_release < _handler.getCoreWindow().getMinWidth()
                            && _handler.getCoreWindow().getWidth() == _handler.getCoreWindow().getMinWidth())) {
                        w = x_release;
                    }
                    ptrPress.setX(x_release);
                }
                if (_handler.getCoreWindow().getLayout().getContainer()._sides.contains(Side.TOP)) {
                    if (!(_handler.getCoreWindow().getMinHeight() == _handler.getCoreWindow().getHeight()
                            && (y_release - y_press) >= 0)) {

                        if (CommonService.getOSType() == OSType.MAC) {
                            h -= y_release - y_press;
                            y_handler = (h_global - h) + y_global;
                        } else {
                            int y5 = y_handler - y_global + (int) ypos - 5;
                            y_handler = y_global + y5;
                            h = h_global - y5;
                        }
                    }
                }
                if (_handler.getCoreWindow().getLayout().getContainer()._sides.contains(Side.BOTTOM)) {
                    if (!(y_release < _handler.getCoreWindow().getMinHeight()
                            && _handler.getCoreWindow().getHeight() == _handler.getCoreWindow().getMinHeight())) {

                        if (CommonService.getOSType() == OSType.MAC)
                            y_handler = y_global;
                        h = y_release;
                        ptrPress.setY(y_release);
                    }
                }

                if (_handler.getCoreWindow().getLayout().getContainer()._sides.size() != 0 && !_handler.getCoreWindow().isMaximized) {

                    if (CommonService.getOSType() == OSType.MAC) {
                        setWindowSize(w, h);
                        if (_handler.getCoreWindow().getLayout().getContainer()._sides.contains(Side.LEFT)
                                && _handler.getCoreWindow().getLayout().getContainer()._sides.contains(Side.TOP)) {
                            setWindowPos(x_handler, (h_global - h) + y_global);
                        } else if (_handler.getCoreWindow().getLayout().getContainer()._sides.contains(Side.LEFT)
                                || _handler.getCoreWindow().getLayout().getContainer()._sides.contains(Side.BOTTOM)
                                || _handler.getCoreWindow().getLayout().getContainer()._sides.contains(Side.TOP)) {
                            setWindowPos(x_handler, y_handler);
                            _handler.getPointer().setY(y_handler);
                        }
                    } else {
                        if (_handler.getCoreWindow().getLayout().getContainer()._sides.contains(Side.LEFT)
                                || _handler.getCoreWindow().getLayout().getContainer()._sides.contains(Side.TOP))
                            setWindowPos(x_handler, y_handler);
                        setWindowSize(w, h);
                    }
                }
            }

            if (_handler.getCoreWindow().getLayout().getContainer()._sides.size() == 0) {
                int x_click = ptrClick.getX();
                int y_click = ptrClick.getY();
                _draggable = isInListHoveredItems(InterfaceDraggable.class);
                Prototype anchor = isInListHoveredItems(InterfaceWindowAnchor.class);

                if (_draggable != null && _draggable.equals(hoveredItem)) {
                    engineEvent.setEvent(InputEventType.MOUSE_DRAG);
                    _draggable.eventMouseDrag.execute(_draggable, _margs);
                } else if (anchor != null && !(hoveredItem instanceof ButtonCore)
                        && !_handler.getCoreWindow().isMaximized) {

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
            if (!hoveredItem.getHoverVerification((float) xpos, (float) ypos)) {
                hoveredItem.setMouseHover(false);
                assignActions(InputEventType.MOUSE_LEAVE, _margs, hoveredItem, false);
            }
        } else {
            ptrPress.setX(ptrRelease.getX());
            ptrPress.setY(ptrRelease.getY());

            if (getHoverPrototype(ptrRelease.getX(), ptrRelease.getY(), InputEventType.MOUSE_MOVE)) {
                if (hoveredItem != null && !("".equals(hoveredItem.getToolTip()))) {
                    _tooltip.initTimer(true);
                }

                Prototype popup = isInListHoveredItems(PopUpMessage.class);
                if (popup != null) {
                    ((PopUpMessage) popup).holdSelf(true);
                }
            }
        }

        try {
            Thread.sleep(10);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private long _start_time = 0; // System.nanoTime();
    // long _estimated_ime = 0; // System.nanoTime() - startTime;
    private boolean _first_click = false;
    private Prototype _dcItem = null;
    // boolean _second_click = false;

    private boolean isDoubleClick(Prototype item) {
        if (_first_click) {
            if (_dcItem != null && _dcItem.equals(item) && (System.nanoTime() - _start_time) / 1000000 < 500) {
                _first_click = false;
                _start_time = 0;
                return true;
            } else {
                _dcItem = item;
                _start_time = System.nanoTime();
            }
        } else {
            _dcItem = item;
            _first_click = true;
            _start_time = System.nanoTime();
        }
        return false;
    }

    private void mouseClick(long wnd, int button, int action, int mods) {
        if (_inputLocker)
            return;

        _handler.getCoreWindow().getLayout().getContainer()._sides.clear();

        if (!_handler.focusable)
            return;

        // if (!flag_click)
        // return;
        // flag_click = false;

        _tooltip.initTimer(false);

        _margs.button = MouseButton.getEnum(button);
        _margs.state = InputState.getEnum(action);
        _margs.mods = KeyMods.getEnums(mods);

        InputEventType m_state;
        if (action == InputState.PRESS.getValue())
            m_state = InputEventType.MOUSE_PRESS;
        else
            m_state = InputEventType.MOUSE_RELEASE;

        Deque<Prototype> tmp = new ArrayDeque<>(hoveredItems);

        Prototype lastHovered = hoveredItem;
        if (lastHovered == null) {
            DoubleBuffer x_pos = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer y_pos = BufferUtils.createDoubleBuffer(1);
            glfwGetCursorPos(_handler.getWindowId(), x_pos, y_pos);
            int x = (int) x_pos.get(0);
            int y = (int) y_pos.get(0);
            getHoverPrototype(x, y, m_state);
            lastHovered = hoveredItem;
            _margs.position.setPosition(x, y);
            ptrRelease.setPosition(x, y);
            ptrPress.setPosition(x, y);
            ptrClick.setPosition(x, y);
        }
        if (!getHoverPrototype(ptrRelease.getX(), ptrRelease.getY(), m_state)) {
            lastHovered.setMousePressed(false);
            engineEvent.resetAllEvents();
            engineEvent.setEvent(InputEventType.MOUSE_RELEASE);
            return;
        }

        if (action == InputState.PRESS.getValue()
                && _handler.getCoreWindow().getLayout().getContainer().getSides(ptrRelease.getX(), ptrRelease.getY()).size() != 0) {
            _handler.getCoreWindow().getLayout().getContainer().saveLastFocus(focusedItem);
        }

        switch (action) {
        case GLFW_RELEASE:
            _handler.getCoreWindow().getLayout().getContainer().restoreFocus();
            boolean is_double_click = isDoubleClick(hoveredItem);

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

                if (!engineEvent.lastEvent().contains(InputEventType.MOUSE_DRAG)) {
                    float len = (float) Math.sqrt(Math.pow(ptrRelease.getX() - ptrClick.getX(), 2)
                            + Math.pow(ptrRelease.getY() - ptrClick.getY(), 2));
                    if (len > 10.0f) {
                        engineEvent.resetAllEvents();
                        engineEvent.setEvent(InputEventType.MOUSE_RELEASE);
                        return;
                    }
                } else if (_draggable != hoveredItem) {

                    Prototype lastFocused = focusedItem;
                    focusedItem = _draggable;
                    findUnderFocusedItems(_draggable);
                    focusedItem = lastFocused;

                    assignActions(InputEventType.MOUSE_RELEASE, _margs, _draggable, true);
                    engineEvent.resetAllEvents();
                    engineEvent.setEvent(InputEventType.MOUSE_RELEASE);
                    return;
                }
            }

            if (hoveredItem != null) {
                hoveredItem.setMousePressed(false);

                if (is_double_click)
                    assignActions(InputEventType.MOUSE_DOUBLE_CLICK, _margs, false);
                else
                    assignActions(InputEventType.MOUSE_RELEASE, _margs, false);
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
                    if (focusedItem == null) {
                        focusedItem = hoveredItem;
                        focusedItem.setFocused(true);
                    } else if (!focusedItem.equals(hoveredItem)) {
                        focusedItem.setFocused(false);
                        focusedItem = hoveredItem;
                        focusedItem.setFocused(true);
                    }
                } else {
                    Deque<Prototype> focused_list = new ArrayDeque<>(hoveredItems);
                    while (!focused_list.isEmpty()) {
                        Prototype f = focused_list.pollLast();
                        if (f.equals(hoveredItem) && hoveredItem.isDisabled())
                            continue;// пропустить
                        if (f.isFocusable) {
                            if (f instanceof WindowAnchor)
                                _handler.getCoreWindow().getLayout().getContainer().saveLastFocus(focusedItem);
                            else {
                                focusedItem = f;
                                focusedItem.setFocused(true);
                            }
                            break;// остановить передачу событий последующим элементам
                        }
                    }
                }
                underFocusedItem = new LinkedList<Prototype>(hoveredItems);
                underFocusedItem.remove(focusedItem);
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
        List<Prototype> queue = new LinkedList<>();
        hoveredItems.clear();

        List<InterfaceBaseItem> layout_box_of_items = new LinkedList<InterfaceBaseItem>();
        layout_box_of_items.add(_handler.getCoreWindow().getLayout().getContainer());
        layout_box_of_items.addAll(getInnerItems(_handler.getCoreWindow().getLayout().getContainer()));

        for (InterfaceBaseItem item : ItemsLayoutBox.getLayoutFloatItems(_handler.getCoreWindow().getWindowGuid())) {
            if (!item.isVisible() || !item.isDrawable())
                continue;
            layout_box_of_items.add(item);

            if (item instanceof Prototype)
                layout_box_of_items.addAll(getInnerItems((Prototype) item));
        }

        // for (InterfaceBaseItem item :
        // ItemsLayoutBox.getLayoutDialogItems(_handler.getCoreWindow().getId())) {
        // if (!item.isVisible() || !item.isDrawable())
        // continue;
        // layout_box_of_items.add(item);

        // if (item instanceof Prototype)
        // layout_box_of_items.addAll(getInnerItems((Prototype) item));
        // }

        for (InterfaceBaseItem item : layout_box_of_items) {
            if (item instanceof Prototype) {
                Prototype tmp = (Prototype) item;
                if (!tmp.isVisible() || !tmp.isDrawable())
                    continue;
                if (tmp.getHoverVerification(xpos, ypos)) {
                    queue.add(tmp);
                } else {
                    tmp.setMouseHover(false);
                    if (item instanceof InterfaceFloating && action == InputEventType.MOUSE_PRESS) {
                        InterfaceFloating float_item = (InterfaceFloating) item;
                        if (float_item.isOutsideClickClosable()) {
                            if (item instanceof ContextMenu) {
                                ContextMenu to_close = (ContextMenu) item;
                                if (to_close.closeDependencies(_margs)) {
                                    float_item.hide();
                                }
                            } else {
                                float_item.hide();
                            }
                        }
                    }
                }
            }
        }

        if (queue.size() > 0) {
            if (hoveredItem != null && hoveredItem != queue.get(queue.size() - 1))
                assignActions(InputEventType.MOUSE_LEAVE, _margs, hoveredItem, false);

            hoveredItem = queue.get(queue.size() - 1);
            hoveredItem.setMouseHover(true);
            glfwSetCursor(_handler.getWindowId(), hoveredItem.getCursor().getCursor());

            if ((xpos >= _handler.getCoreWindow().getLayout().getContainer().getWidth() - 5 && ypos <= 5)
                    || (xpos >= _handler.getCoreWindow().getLayout().getContainer().getWidth() - 5
                            && ypos >= _handler.getCoreWindow().getLayout().getContainer().getHeight() - 5)
                    || (ypos >= _handler.getCoreWindow().getLayout().getContainer().getHeight() - 5 && xpos <= 5)
                    || (ypos >= _handler.getCoreWindow().getLayout().getContainer().getHeight() - 5
                            && xpos >= _handler.getCoreWindow().getLayout().getContainer().getWidth() - 5)
                    || (xpos <= 5 && ypos <= 5)) {
                _handler.setCursorType(GLFW_CROSSHAIR_CURSOR);
            } else {
                if (xpos > _handler.getCoreWindow().getLayout().getContainer().getWidth() - 5 || xpos <= 5)
                    _handler.setCursorType(GLFW_HRESIZE_CURSOR);

                if (ypos > _handler.getCoreWindow().getLayout().getContainer().getHeight() - 5 || ypos <= 5)
                    _handler.setCursorType(GLFW_VRESIZE_CURSOR);
            }

            hoveredItems = queue;
            Deque<Prototype> tmp = new ArrayDeque<>(hoveredItems);
            while (!tmp.isEmpty()) {
                Prototype item = tmp.pollLast();
                if (item.equals(hoveredItem) && hoveredItem.isDisabled())
                    continue;// пропустить
                item.setMouseHover(true);
                if (!item.isPassEvents(InputEventType.MOUSE_HOVER))
                    break;
            }
            assignActions(InputEventType.MOUSE_HOVER, _margs, false);
            return true;
        } else
            return false;
    }

    private List<InterfaceBaseItem> getInnerItems(Prototype root) {
        List<InterfaceBaseItem> list = new LinkedList<InterfaceBaseItem>();
        List<InterfaceBaseItem> root_items = new LinkedList<InterfaceBaseItem>(root.getItems());

        for (InterfaceBaseItem item : root_items) {
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
        if (_inputLocker)
            return;
        _tooltip.initTimer(false);
        if (hoveredItems.size() == 0)
            return;
        Deque<Prototype> tmp = new ArrayDeque<>(hoveredItems);
        while (!tmp.isEmpty()) {
            Prototype item = tmp.pollLast();
            if (dy > 0 || dx < 0)
                item.eventScrollUp.execute(item, _margs);
            if (dy < 0 || dx > 0)
                item.eventScrollDown.execute(item, _margs);
            if (!item.isPassEvents(InputEventType.MOUSE_SCROLL))
                break;
        }
        engineEvent.setEvent(InputEventType.MOUSE_SCROLL);
    }

    private void keyPress(long wnd, int key, int scancode, int action, int mods) {
        if (_inputLocker)
            return;
        // System.out.println("keypress");
        // switch (action) {
        // case 1 :
        // System.out.println("key press");
        // break;
        // case 0 :
        // System.out.println("key release");
        // break;
        // case 2 :
        // System.out.println("key repeat");
        // break;
        // }
        // System.out.println(mods);
        if (!_handler.focusable)
            return;
        _tooltip.initTimer(false);
        _kargs.key = KeyCode.getEnum(key);
        _kargs.scancode = scancode;
        _kargs.state = InputState.getEnum(action);
        _kargs.mods = KeyMods.getEnums(mods);
        _margs.mods = KeyMods.getEnums(mods);

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
            } else if (mods == KeyMods.CONTROL.getValue() && key == KeyCode.Z.getValue()) {
                ((InterfaceTextShortcuts) focusedItem).undo();
            } else if (mods == KeyMods.CONTROL.getValue() && key == KeyCode.Y.getValue()) {
                ((InterfaceTextShortcuts) focusedItem).redo();
            } else {
                if (action == InputState.PRESS.getValue()) {
                    focusedItem.eventKeyPress.execute(focusedItem, _kargs);
                    assignActions(InputEventType.KEY_PRESS, _kargs, focusedItem);
                }

                if (action == InputState.REPEAT.getValue()) {
                    focusedItem.eventKeyPress.execute(focusedItem, _kargs);
                    assignActions(InputEventType.KEY_PRESS, _kargs, focusedItem);
                }

                if (action == InputState.RELEASE.getValue()) {
                    focusedItem.eventKeyRelease.execute(focusedItem, _kargs);
                    assignActions(InputEventType.KEY_RELEASE, _kargs, focusedItem);
                }

            }
        } else {
            if (action == InputState.PRESS.getValue())
                assignActions(InputEventType.KEY_PRESS, _kargs, focusedItem, true);
            else if (action == InputState.REPEAT.getValue())
                assignActions(InputEventType.KEY_PRESS, _kargs, focusedItem, true);
            else if (action == InputState.RELEASE.getValue())
                assignActions(InputEventType.KEY_RELEASE, _kargs, focusedItem, true);
        }
    }

    private void textInput(long wnd, int character, int mods) {
        if (_inputLocker)
            return;
        // System.out.println("textinput");
        if (!_handler.focusable)
            return;

        // if (!flag_text_input)
        // return;
        // flag_text_input = false;

        _tooltip.initTimer(false);
        _tiargs.character = character;
        _tiargs.mods = KeyMods.getEnums(mods);
        if (focusedItem != null) {
            if (focusedItem.eventTextInput != null)
                focusedItem.eventTextInput.execute(focusedItem, _tiargs);
        }
    }

    // hovered
    private void assignActions(InputEventType action, InputEventArgs args, Boolean only_last) {

        if (only_last && !hoveredItem.isDisabled()) {
            EventTask task = new EventTask();
            task.item = hoveredItem;
            task.action = action;
            task.args = args;
            _handler.getCoreWindow().getLayout().setEventTask(task);
        } else {
            Deque<Prototype> tmp = new ArrayDeque<>(hoveredItems);
            while (!tmp.isEmpty()) {
                Prototype item = tmp.pollLast();
                if (item.equals(hoveredItem) && hoveredItem.isDisabled())
                    continue;// пропустить

                EventTask task = new EventTask();
                task.item = item;
                task.action = action;
                task.args = args;
                _handler.getCoreWindow().getLayout().setEventTask(task);
                if (!item.isPassEvents(action))
                    break;
            }
        }
        _handler.getCoreWindow().getLayout().executePollActions();
    }

    // focused
    private void assignActions(InputEventType action, InputEventArgs args, Prototype sender, boolean is_pass_under) {
        if (sender.isDisabled())
            return;

        EventTask task = new EventTask();
        task.item = sender;
        task.action = action;
        task.args = args;
        _handler.getCoreWindow().getLayout().setEventTask(task);

        if (is_pass_under && sender.isPassEvents(action)) {
            if (underFocusedItem != null) {
                Deque<Prototype> tmp = new ArrayDeque<Prototype>(underFocusedItem);
                while (tmp.size() != 0) {
                    Prototype item = tmp.pollLast();

                    if (item.equals(focusedItem) && focusedItem.isDisabled())
                        continue;// пропустить

                    EventTask t = new EventTask();
                    t.item = item;
                    t.action = action;
                    t.args = args;
                    _handler.getCoreWindow().getLayout().setEventTask(t);

                    if (!item.isPassEvents(action))
                        break;// остановить передачу событий последующим элементам
                }
            }
        }
        _handler.getCoreWindow().getLayout().executePollActions();
    }

    private void assignActions(InputEventType action, InputEventArgs args, Prototype sender) {
        if (sender.isDisabled())
            return;

        if (sender.isPassEvents(action)) {
            if (underFocusedItem != null) {
                Deque<Prototype> tmp = new ArrayDeque<Prototype>(underFocusedItem);
                while (tmp.size() != 0) {
                    Prototype item = tmp.pollLast();

                    if (item.equals(focusedItem) && focusedItem.isDisabled())
                        continue;// пропустить

                    EventTask t = new EventTask();
                    t.item = item;
                    t.action = action;
                    t.args = args;
                    _handler.getCoreWindow().getLayout().setEventTask(t);

                    if (!item.isPassEvents(action))
                        break;// остановить передачу событий последующим элементам
                }
            }
        }
        _handler.getCoreWindow().getLayout().executePollActions();
    }

    private float _intervalLow = 1.0f / 10.0f;
    private float _intervalMedium = 1.0f / 30.0f;
    private float _intervalHigh = 1.0f / 60.0f;
    private float _intervalUltra = 1.0f / 120.0f;
    private float _intervalAssigned = 1.0f / 15.0f;

    private RedrawFrequency _frequency = RedrawFrequency.LOW;

    private Lock _locker = new ReentrantLock();

    void setFrequency(RedrawFrequency value) {
        _locker.lock();
        try {
            if (value == RedrawFrequency.LOW) {
                _intervalAssigned = _intervalLow;
            } else if (value == RedrawFrequency.MEDIUM) {
                _intervalAssigned = _intervalMedium;
            } else if (value == RedrawFrequency.HIGH) {
                _intervalAssigned = _intervalHigh;
            } else if (value == RedrawFrequency.ULTRA) {
                _intervalAssigned = _intervalUltra;
            }
        } catch (Exception ex) {
            System.out.println("Method - SetFrequency");
            ex.printStackTrace();
        } finally {
            _locker.unlock();
        }
    }

    private float getFrequency() {
        _locker.lock();
        try {
            return _intervalAssigned;
        } catch (Exception ex) {
            System.out.println("Method - SetFrequency");
            ex.printStackTrace();
            return _intervalLow;
        } finally {
            _locker.unlock();
        }
    }

    RedrawFrequency getRedrawFrequency() {
        _locker.lock();
        try {
            return _frequency;
        } catch (Exception ex) {
            System.out.println("Method - SetFrequency");
            ex.printStackTrace();
            _frequency = RedrawFrequency.LOW;
            return _frequency;
        } finally {
            _locker.unlock();
        }
    }

    private VRAMFramebuffer _fbo = new VRAMFramebuffer();

    private void run() {
        _handler.gVAO = glGenVertexArrays();
        glBindVertexArray(_handler.gVAO);
        focus(_handler.getWindowId(), true);

        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        glfwGetFramebufferSize(_handler.getWindowId(), w, h);

        _framebufferWidth = w.get(0);
        _framebufferHeight = h.get(0);
        // _framebufferWidth = w.get(0) * 2;
        // _framebufferHeight = h.get(0) * 2;

        glViewport(0, 0, _framebufferWidth, _framebufferHeight);

        _fbo.genFBO();
        _fbo.genFBOTexture(_framebufferWidth, _framebufferHeight);
        _fbo.unbindFBO();
        // glfwSwapInterval(0);
        while (!_handler.isClosing()) {
            glfwWaitEventsTimeout(getFrequency());
            // glfwWaitEvents();
            // glfwPollEvents();
            // synchronized(this)
            // {
            // try {
            // this.wait(30);
            // } catch (Exception e) {
            // //TODO: handle exception
            // }
            // }

            // glClearColor(0, 0, 0, 0);
            if (maximizeRequest) {
                maximizeWindow();
                maximizeRequest = false;
            }
            if (!engineEvent.lastEvent().contains(InputEventType.WINDOW_RESIZE)) {
                update();
                _handler.swap();
            }
            flag_move = true;
            // try {
            // Thread.sleep(1000 / 60);
            // } catch (InterruptedException e) {
            // e.printStackTrace();
            // }
        }
        _primitive.deleteShader();
        _texture.deleteShader();
        _char.deleteShader();
        // _fxaa.deleteShader();
        _blur.deleteShader();

        _fbo.clearFBO();
        VRAMStorage.clear();

        glDeleteVertexArrays(_handler.gVAO);

        _handler.clearEventsCallbacks();
        _handler.destroy();
    }

    void update() {
        VRAMStorage.flush();
        render();
        _bounds.clear();
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

        // draw static
        drawItems(_handler.getCoreWindow().getLayout().getContainer());
        // draw float
        List<InterfaceBaseItem> float_items = new LinkedList<>(
                ItemsLayoutBox.getLayout(_handler.getCoreWindow().getWindowGuid()).getFloatItems());
        if (float_items != null) {
            for (InterfaceBaseItem item : float_items) {
                if (item.getHeightPolicy() == SizePolicy.EXPAND) {
                    int[] confines = item.getConfines();
                    item.setConfines(confines[0], confines[1], 0, _handler.getCoreWindow().getLayout().getContainer().getHeight());
                    item.setY(0);
                    item.setHeight(_handler.getCoreWindow().getLayout().getContainer().getHeight());
                }
                if (item.getWidthPolicy() == SizePolicy.EXPAND) {
                    int[] confines = item.getConfines();
                    item.setConfines(0, _handler.getCoreWindow().getLayout().getContainer().getWidth(), confines[2], confines[3]);
                    item.setX(0);
                    item.setWidth(_handler.getCoreWindow().getLayout().getContainer().getWidth());
                }
                drawItems(item);
            }
        }

        // List<InterfaceBaseItem> dialog_items = new LinkedList<>(
        // ItemsLayoutBox.getLayout(_handler.getCoreWindow().getId()).getDialogItems());
        // if (dialog_items != null) {
        // for (InterfaceBaseItem item : dialog_items) {
        // if (item.getHeightPolicy() == SizePolicy.EXPAND) {
        // int[] confines = item.getConfines();
        // item.setConfines(confines[0], confines[1], 0,
        // _handler.getCoreWindow().getWindow().getHeight());
        // item.setY(0);
        // item.setHeight(_handler.getCoreWindow().getWindow().getHeight());
        // }
        // if (item.getWidthPolicy() == SizePolicy.EXPAND) {
        // int[] confines = item.getConfines();
        // item.setConfines(0, _handler.getCoreWindow().getWindow().getWidth(), confines[2],
        // confines[3]);
        // item.setX(0);
        // item.setWidth(_handler.getCoreWindow().getWindow().getWidth());
        // }
        // drawItems(item);
        // }
        // }

        // draw tooltip if needed
        drawToolTip();
        if (!_handler.focusable) {
            drawShadePillow();
        }
    }

    private boolean checkOutsideBorders(InterfaceBaseItem shell) {
        if (shell.getParent() == null)
            return false;

        if (_bounds.containsKey(shell.getParent())) {
            int[] shape = _bounds.get(shell.getParent());
            if (shape == null)/////////////////////////////////////
                return false;
            glEnable(GL_SCISSOR_TEST);
            glScissor(shape[0], shape[1], shape[2], shape[3]);

            if (!_bounds.containsKey(shell)) {
                int x = shell.getX();
                int y = _handler.getCoreWindow().getHeight() - (shell.getY() + shell.getHeight());
                int w = shell.getWidth();
                int h = shell.getHeight();

                int x1 = x + w;
                int y1 = y + h;

                if (x < shape[0]) {
                    x = shape[0];
                    w = x1 - x;
                }

                if (y < shape[1]) {
                    y = shape[1];
                    h = y1 - y;
                }

                if (x + w > shape[0] + shape[2]) {
                    w = shape[0] + shape[2] - x;
                }

                if (y + h > shape[1] + shape[3])
                    h = shape[1] + shape[3] - y;

                // if (shell.getParent() instanceof ListArea)
                // System.out.println(shape[1] + " " + shape[3] + " " + shell.getItemName());

                _bounds.put(shell, new int[] { x, y, w, h });
            }
            return true;
        }
        return lazyStencil(shell);
    }

    // private void setStencilMask(List<float[]> crd_array) {
    // if (crd_array == null)
    // return;
    // _primitive.useShader();
    // VRAMVertex stencil = new VRAMVertex();
    // stencil.genBuffers(crd_array);
    // stencil.sendColor(_primitive, new Color(0, 0, 0, 0));
    // stencil.draw(GL_TRIANGLES);
    // stencil.clear();
    // }

    // private void strictStencil(InterfaceBaseItem shell) {
    // glEnable(GL_STENCIL_TEST);

    // glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
    // glClear(GL_STENCIL_BUFFER_BIT);
    // glStencilMask(0x00);

    // glStencilFunc(GL_ALWAYS, 1, 0xFF);
    // glStencilMask(0xFF);

    // setStencilMask(shell.getParent().makeShape());

    // glStencilFunc(GL_EQUAL, 1, 0xFF);

    // shell.getParent().setConfines(shell.getParent().getX() +
    // shell.getParent().getPadding().left,
    // shell.getParent().getX() + shell.getParent().getWidth() -
    // shell.getParent().getPadding().right,
    // shell.getParent().getY() + shell.getParent().getPadding().top,
    // shell.getParent().getY() + shell.getParent().getHeight() -
    // shell.getParent().getPadding().bottom);
    // setConfines(shell);
    // }

    private void setConfines(InterfaceBaseItem shell) {
        int[] confines = shell.getParent().getConfines();
        shell.setConfines(confines[0], confines[1], confines[2], confines[3]);

        if (shell instanceof Prototype) {
            Prototype root = (Prototype) shell;
            List<InterfaceBaseItem> root_items = new LinkedList<>(root.getItems());
            for (InterfaceBaseItem item : root_items) {
                setConfines(item);
            }
        }
    }

    private void setScissorRectangle(InterfaceBaseItem rect) {

        int x = rect.getParent().getX();
        int y = _handler.getCoreWindow().getHeight() - (rect.getParent().getY() + rect.getParent().getHeight());
        int w = rect.getParent().getWidth();
        int h = rect.getParent().getHeight();

        float scale = _handler.getCoreWindow().getDpiScale()[0];
        x *= scale;
        y *= scale;
        w *= scale;
        h *= scale;

        glEnable(GL_SCISSOR_TEST);
        glScissor(x, y, w, h);

        if (!_bounds.containsKey(rect))
            _bounds.put(rect, new int[] { x, y, w, h });

        rect.getParent().setConfines(rect.getParent().getX() + rect.getParent().getPadding().left,
                rect.getParent().getX() + rect.getParent().getWidth() - rect.getParent().getPadding().right,
                rect.getParent().getY() + rect.getParent().getPadding().top,
                rect.getParent().getY() + rect.getParent().getHeight() - rect.getParent().getPadding().bottom);
        setConfines(rect);
    }

    private Boolean lazyStencil(InterfaceBaseItem shell) {
        Map<ItemAlignment, int[]> outside = new HashMap<ItemAlignment, int[]>();

        if (shell.getParent() != null) {
            // bottom
            if (shell.getParent().getY() + shell.getParent().getHeight() < shell.getY() + shell.getHeight()) {
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

            if (outside.size() > 0) {
                // _isStencilSet = shell;
                // strictStencil(shell);
                setScissorRectangle(shell);
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
            drawText((InterfaceTextContainer) root);
            glDisable(GL_SCISSOR_TEST);
        }
        if (root instanceof InterfaceImageItem) {
            drawShell(root);
            glDisable(GL_SCISSOR_TEST);
            drawImage((InterfaceImageItem) root);
            glDisable(GL_SCISSOR_TEST);
        } else {
            drawShell(root);
            glDisable(GL_SCISSOR_TEST);
            if (root instanceof Prototype) {
                List<InterfaceBaseItem> list = new LinkedList<>(((Prototype) root).getItems());
                for (InterfaceBaseItem child : list) {
                    drawItems(child);
                }
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
        }

        _primitive.useShader();
        VRAMVertex store = new VRAMVertex();
        store.genBuffers(crd_array);
        store.sendColor(_primitive, shell.getBackground());
        store.draw(GL_TRIANGLES);
        store.clear();

        // clear array
        crd_array.clear();

        if (shell instanceof Prototype)
            drawBorder((Prototype) shell);
    }

    private void drawBorder(Prototype vi) {
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

    private void drawShadow(InterfaceBaseItem shell) {
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

    // private void drawShadowPartTmp(float[] weights, int res, int[] xy, int[] wh)
    // {
    // float x0 = xy[0];
    // float x1 = xy[0] + wh[0];
    // float y0 = xy[1];
    // float y1 = xy[1] + wh[1];
    // float[][] image = new float[wh[0] + 2 * res][wh[1] + 2 * res];
    // for (int i = res; i < wh[0] + res; i++) {
    // for (int j = res; j < wh[1] + res; j++) {
    // image[i][j] = 1f;
    // }
    // }

    // int h = image.length;
    // int w = image[0].length;
    // float[][] outarr = new float[h][w];
    // double tmp, ctmp;
    // for (int i = 0; i < h; i++) {
    // for (int j = 0; j < w; j++) {
    // tmp = image[i][j] * weights[0];
    // for (int k = -res; k <= res; k++) {
    // if (i + k < 0 || i + k >= h)
    // ctmp = image[i][j];
    // else
    // ctmp = image[i + k][j];

    // tmp += (weights[Math.abs(k)]) * ctmp;

    // }
    // outarr[i][j] = (float) tmp;
    // }
    // }
    // shapePart(outarr, res, weights);
    // }

    // private float[][] shapePart(float[][] image, int res, float[] weights) {
    // int h = image.length;
    // int w = image[0].length;
    // float[][] outarr = new float[h][w];
    // double tmp, ctmp;
    // for (int i = 0; i < h; i++) {
    // for (int j = 0; j < w; j++) {
    // tmp = 0;
    // for (int l = -res; l <= res; l++) {
    // if (j + l < 0 || j + l >= w)
    // ctmp = image[i][j];
    // else
    // ctmp = image[i][j + l];

    // tmp += (weights[Math.abs(l)]) * ctmp;
    // }

    // outarr[i][j] = (float) tmp;
    // }
    // }
    // return outarr;
    // }

    private void drawShadowPart(float[] weights, int res, int fbo_texture, float[] xy, float[] wh) {
        float i_x0 = -1.0f;
        float i_y0 = 1.0f;
        float i_x1 = 1.0f;
        float i_y1 = -1.0f;
        _blur.useShader();
        VRAMTexture store = new VRAMTexture();
        store.genBuffers(i_x0, i_x1, i_y0, i_y1);
        store.bind(fbo_texture);
        store.sendUniformSample2D(_blur);
        store.sendUniform1fv(_blur, "weights", 11, weights);
        // store.sendUniform2fv(_blur, "frame", new float[] { _framebufferWidth,
        // _framebufferHeight });
        store.sendUniform2fv(_blur, "frame",
                new float[] { _handler.getCoreWindow().getWidth(), _handler.getCoreWindow().getHeight() });
        store.sendUniform1f(_blur, "res", (res * 1f / 10));
        store.sendUniform2fv(_blur, "point", xy);
        store.sendUniform2fv(_blur, "size", wh);
        store.draw();
        store.clear();
    }

    private float gauss(float x, float sigma) {
        double ans;
        ans = Math.exp(-(x * x) / (2f * sigma * sigma)) / Math.sqrt(2 * Math.PI * sigma * sigma);
        return (float) ans;
    }

    private void drawText(InterfaceTextContainer text) {
        TextPrinter textPrt = text.getLetTextures();
        if (textPrt == null)
            return;
        ByteBuffer bb = textPrt.texture;
        if (bb == null || bb.limit() == 0)
            return;

        checkOutsideBorders((InterfaceBaseItem) text);

        int bb_h = textPrt.heightTexture, bb_w = textPrt.widthTexture;
        float i_x0 = ((float) textPrt.xTextureShift / (float) _handler.getCoreWindow().getWidth() * 2.0f) - 1.0f;
        float i_y0 = ((float) textPrt.yTextureShift / (float) _handler.getCoreWindow().getHeight() * 2.0f - 1.0f) * (-1.0f);
        float i_x1 = (((float) textPrt.xTextureShift + (float) bb_w / _handler.getCoreWindow().getDpiScale()[0])
                / (float) _handler.getCoreWindow().getWidth() * 2.0f) - 1.0f;
        float i_y1 = (((float) textPrt.yTextureShift + (float) bb_h / _handler.getCoreWindow().getDpiScale()[0])
                / (float) _handler.getCoreWindow().getHeight() * 2.0f - 1.0f) * (-1.0f);
        float[] argb = { (float) text.getForeground().getRed() / 255.0f,
                (float) text.getForeground().getGreen() / 255.0f, (float) text.getForeground().getBlue() / 255.0f,
                (float) text.getForeground().getAlpha() / 255.0f };

        _char.useShader();
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
        checkOutsideBorders((InterfaceBaseItem) item);
        List<float[]> point = item.getShapePointer();
        float center_offset = item.getPointThickness() / 2.0f;
        float[] result = new float[point.size() * crd_array.size() * 3];
        int skew = 0;
        for (float[] shape : crd_array) {
            List<float[]> fig = GraphicsMathService.toGL(
                    GraphicsMathService.moveShape(point, shape[0] - center_offset, shape[1] - center_offset),
                    _handler.getCoreWindow());

            for (int i = 0; i < fig.size(); i++) {
                result[skew + i * 3 + 0] = fig.get(i)[0];
                result[skew + i * 3 + 1] = fig.get(i)[1];
                result[skew + i * 3 + 2] = fig.get(i)[2];
            }
            skew += fig.size() * 3;
        }
        _primitive.useShader();
        VRAMVertex store = new VRAMVertex();
        store.genBuffers(result);
        store.sendColor(_primitive, item.getPointColor());
        store.draw(GL_TRIANGLES);
        store.clear();
    }

    private void drawLines(InterfaceLine item) {
        if (item.getLineColor().getAlpha() == 0)
            return;

        List<float[]> crd_array = GraphicsMathService.toGL(item.makeShape(), _handler.getCoreWindow());
        if (crd_array == null)
            return;
        checkOutsideBorders((InterfaceBaseItem) item);
        _primitive.useShader();
        VRAMVertex store = new VRAMVertex();
        store.genBuffers(crd_array);
        store.sendColor(_primitive, item.getLineColor());
        store.draw(GL_LINE_STRIP);
        store.clear();
    }

    private void drawImage(InterfaceImageItem image) {
        // проверка: полностью ли влезает объект в свой контейнер
        checkOutsideBorders((InterfaceBaseItem) image);

        byte[] bitmap = image.getPixMapImage();
        if (bitmap == null)
            return;

        int w = image.getImageWidth(), h = image.getImageHeight();
        RectangleBounds area = image.getRectangleBounds();

        float i_x0 = ((float) area.getX() / (float) _handler.getCoreWindow().getWidth() * 2.0f) - 1.0f;
        float i_y0 = ((float) area.getY() / (float) _handler.getCoreWindow().getHeight() * 2.0f - 1.0f) * (-1.0f);
        float i_x1 = (((float) area.getX() + (float) area.getWidth()) / (float) _handler.getCoreWindow().getWidth() * 2.0f)
                - 1.0f;
        float i_y1 = (((float) area.getY() + (float) area.getHeight()) / (float) _handler.getCoreWindow().getHeight() * 2.0f
                - 1.0f) * (-1.0f);

        _texture.useShader();

        if (image instanceof ImageItem) {
            ImageItem tmp = (ImageItem) image;
            if (tmp.isNew()) {
                VRAMStorage.storageLocker.lock();
                try {
                    VRAMStorage.deleteTexture(image);
                    // create and store
                    VRAMTexture tex = new VRAMTexture();
                    tex.genBuffers(i_x0, i_x1, i_y0, i_y1);
                    tex.genTexture(w, h, bitmap);
                    VRAMStorage.addTexture(image, tex);
                    tmp.setNew(false);

                    tex.sendUniformSample2D(_texture);
                    if (image.isColorOverlay()) {
                        float[] argb = { (float) image.getColorOverlay().getRed() / 255.0f,
                                (float) image.getColorOverlay().getGreen() / 255.0f,
                                (float) image.getColorOverlay().getBlue() / 255.0f,
                                (float) image.getColorOverlay().getAlpha() / 255.0f };
                        tex.sendUniform1i(_texture, "overlay", 1);
                        tex.sendUniform4f(_texture, "rgb", argb);
                    } else
                        tex.sendUniform1i(_texture, "overlay", 0);// VEEEEEEEERY interesting!!!

                    tex.sendUniform1f(_texture, "alpha", image.getRotationAngle());
                    tex.draw();
                    tex.deleteIBOBuffer();
                    tex.deleteVBOBuffer();
                    tex.unbind();
                } finally {
                    VRAMStorage.storageLocker.unlock();
                }
            } else {
                VRAMStorage.storageLocker.lock();
                try {
                    // draw
                    VRAMTexture tex = VRAMStorage.getTexture(image);
                    if (tex == null) {
                        tmp.setNew(true);
                        return;
                    }

                    tex.bind();
                    tex.genBuffers(i_x0, i_x1, i_y0, i_y1);
                    tex.sendUniformSample2D(_texture);
                    if (image.isColorOverlay()) {
                        float[] argb = { (float) image.getColorOverlay().getRed() / 255.0f,
                                (float) image.getColorOverlay().getGreen() / 255.0f,
                                (float) image.getColorOverlay().getBlue() / 255.0f,
                                (float) image.getColorOverlay().getAlpha() / 255.0f };
                        tex.sendUniform1i(_texture, "overlay", 1);
                        tex.sendUniform4f(_texture, "rgb", argb);
                    } else
                        tex.sendUniform1i(_texture, "overlay", 0);// VEEEEEEEERY interesting!!!

                    tex.sendUniform1f(_texture, "alpha", image.getRotationAngle());
                    tex.draw();
                    tex.deleteIBOBuffer();
                    tex.deleteVBOBuffer();
                    tex.unbind();
                } finally {
                    VRAMStorage.storageLocker.unlock();
                }
            }
            return;
        }

        VRAMTexture store = new VRAMTexture();
        store.genBuffers(i_x0, i_x1, i_y0, i_y1);
        store.genTexture(w, h, bitmap);
        store.sendUniformSample2D(_texture);
        if (image.isColorOverlay()) {
            float[] argb = { (float) image.getColorOverlay().getRed() / 255.0f,
                    (float) image.getColorOverlay().getGreen() / 255.0f,
                    (float) image.getColorOverlay().getBlue() / 255.0f,
                    (float) image.getColorOverlay().getAlpha() / 255.0f };
            store.sendUniform1i(_texture, "overlay", 1);
            store.sendUniform4f(_texture, "rgb", argb);
        } else
            store.sendUniform1i(_texture, "overlay", 0);// VEEEEEEEERY interesting!!!

        store.sendUniform1f(_texture, "alpha", image.getRotationAngle());

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
        if (ptrRelease.getX() - 10 + _tooltip.getWidth() > _handler.getCoreWindow().getWidth()) {
            _tooltip.setX(_handler.getCoreWindow().getWidth() - _tooltip.getWidth() - 10);
        } else {
            _tooltip.setX(ptrRelease.getX() - 10);
        }
        // System.out.println(_tooltip.getY());
        drawShell(_tooltip);
        glDisable(GL_SCISSOR_TEST);
        _tooltip.getTextLine().updateGeometry();
        drawText(_tooltip.getTextLine());
        // System.out.println(_tooltip.getTextLine().getY());
        glDisable(GL_SCISSOR_TEST);
    }

    private void drawShadePillow() {
        // //Vertex
        List<float[]> vertex = new LinkedList<>();
        vertex.add(new float[] { -1.0f, 1.0f, 0.0f });
        vertex.add(new float[] { -1.0f, -1.0f, 0.0f });
        vertex.add(new float[] { 1.0f, -1.0f, 0.0f });
        vertex.add(new float[] { 1.0f, -1.0f, 0.0f });
        vertex.add(new float[] { 1.0f, 1.0f, 0.0f });
        vertex.add(new float[] { -1.0f, 1.0f, 0.0f });
        _primitive.useShader();
        VRAMVertex store = new VRAMVertex();
        store.genBuffers(vertex);
        store.sendColor(_primitive, new Color(0, 0, 0, 150));
        store.draw(GL_TRIANGLES);
        store.clear();
    }
}
