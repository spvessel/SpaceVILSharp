package com.spvessel.Engine;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.*;
import java.io.*;
import java.nio.*;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import com.spvessel.Common.*;
import com.spvessel.Items.*;
import com.spvessel.Windows.*;
import com.spvessel.Layouts.*;
import com.spvessel.Cores.*;
import com.spvessel.Flags.InputEventType;

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

    public void SetfocusedItem(VisualItem item) {
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

    public DrawEngine(WindowLayout handler) {
        hoveredItems = new LinkedList<VisualItem>();
        _handler = new GLWHandler(handler);

        _tooltip.setHandler(handler);
        _tooltip.getTextLine().setHandler(handler);
        _tooltip.getTextLine().setParent(_tooltip);
        // _tooltip.InitElements();
    }

    public void dispose() {
        // полностью аннигилирует библиотеку GLFW, что приводит к закрытию всех окон и
        // уничтожает все что использует библиотеку GLFW
        // должно вызываться только при закрытии приложения или если необходимо -
        // уничтожении всех окон
        // статический метод Glfw.Terminate() является общим для всех экземпляров
        // классов, что создают окна с помощью GLFW
        // LogService.Log().EndLogging();
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
        Display.setIcon(_icon);
    }

    private String getResourceString(String resource) {

        StringBuilder result = new StringBuilder("");

        File file = new File(this.getClass().getResource(resource).getFile());

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    public void init() {
        synchronized (CommonService.GlobalLocker) {
            // InitWindow
            _handler.InitGlfw();
            _handler.CreateWindow();
        }
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
        _primitive = new Shader(getResourceString("/shaders/vs_fill.glsl"),
                getResourceString("/shaders/vfs_fill.glsl"));
        _primitive.compile();
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

        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

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

    private void focus(long wnd, Boolean value) {
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

    }

    private void mouseClick(long wnd, int button, int action, int mods) {

    }

    private void mouseScroll(long wnd, double dx, double dy) {
        _tooltip.initTimer(false);
        BaseItem root = hoveredItem;
        while (root != null) {
            if (root instanceof InterfaceScrollable)
                break;
            root = root.getParent();
        }
        if (root != null) {
            InterfaceScrollable tmp = (InterfaceScrollable) root;
            if (dy > 0 || dx < 0)
                tmp.invokeScrollUp(_margs);
            if (dy < 0 || dx > 0)
                tmp.invokeScrollDown(_margs);
            engineEvent.setEvent(InputEventType.MOUSE_SCROLL);
        }
    }

    private void keyPress(long wnd, int key, int scancode, int action, int mods) {

    }

    private void textInput(long wnd, int codepoint, int mods) {

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

        /*
         * foreach (var item in _handler.GetLayout().GetWindow().GetItems()) { ComboBox
         * combo = item as ComboBox; if (combo != null) {
         * AssignActions(InputEventType.MousePressed, _margs, combo); //
         * combo._dropdownarea.Handler.Show(); } }
         */

        while (!_handler.isClosing()) {
            // glfwPollEvents();
            // glfwWaitEvents();
            glfwWaitEventsTimeout(_interval);
            update();
        }

        _primitive.deleteShader();
        _texture.deleteShader();

        glDeleteVertexArrays(_handler.gVAO);

        _handler.clearEventsCallbacks();
        _handler.destroy();
    }

    private void update() {
        synchronized (_handler.getLayout().engine_locker) {
            if (_handler.getLayout().isBorderHidden)
                glViewport(0, 0, _handler.getLayout().getWidth(), _handler.getLayout().getHeight());
            render();
        }
    }

    private void render() {
        if (_handler.focused) {
            glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
            // draw static
            drawItems(_handler.getLayout().getWindow());
            // draw float
            for (BaseItem item : ItemsLayoutBox.getLayout(_handler.getLayout().getId()).getItems())
                drawItems((BaseItem) item);
            // draw tooltip if needed
            drawToolTip();
            if (!_handler.focusable)
                drawShadePillow();
        }
        _handler.swap();
    }

    private void drawItems(BaseItem item) {
        drawShell(item);
    }

    private void drawShell(BaseItem shell) {
        if (shell.getBackground().getAlpha() == 0)
            return;

        // Vertex
        List<float[]> crd_array = shell.makeShape();

        int vertexbuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);
        int length = crd_array.size() * 3;
        FloatBuffer vertexData = BufferUtils.createFloatBuffer(length);
        for (int i = 0; i < length / 3; i++) {
            vertexData.put(i * 3 + 0, crd_array.get(i)[0]);
            vertexData.put(i * 3 + 1, crd_array.get(i)[1]);
            vertexData.put(i * 3 + 2, crd_array.get(i)[2]);
        }
        vertexData.flip();

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
        colorData.flip();

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

        // clear array
        crd_array.clear();
    }

    private void drawToolTip() {

    }

    private void drawShadePillow() {

    }
}