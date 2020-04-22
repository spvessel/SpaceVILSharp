package com.spvessel.spacevil;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.*;
import java.nio.*;

import com.spvessel.spacevil.Common.*;
import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Decorations.Effects;
import com.spvessel.spacevil.Flags.*;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

final class DrawEngine {

    void restoreCommonGLSettings() {
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_ALPHA_TEST);
        glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_DST_ALPHA);
        glEnable(GL_DEPTH_TEST);
    }

    void restoreView() {
        glViewport(0, 0, _framebufferWidth, _framebufferHeight);
    }

    void setGLLayerViewport(InterfaceOpenGLLayer layer) {
        if (!(layer instanceof InterfaceBaseItem))
            return;
        InterfaceBaseItem oglLayer = (InterfaceBaseItem) layer;
        setViewPort(oglLayer);
    }

    private void setViewPort(InterfaceBaseItem item) {
        int x = item.getX();
        int y = _commonProcessor.window.getHeight() - (item.getY() + item.getHeight());
        int w = item.getWidth();
        int h = item.getHeight();
        x = (int) ((float) x * _scale.getXScale());
        y = (int) ((float) y * _scale.getYScale());
        w = (int) ((float) w * _scale.getXScale());
        h = (int) ((float) h * _scale.getYScale());
        glViewport(x, y, w, h);
    }

    <T> void freeVRAMResource(T resource) {
        if (_renderProcessor == null)
            return;
        _renderProcessor.freeResource(resource);
    }

    private CommonProcessor _commonProcessor;
    private TextInputProcessor _textInputProcessor;
    private KeyInputProcessor _keyInputProcessor;
    private MouseScrollProcessor _mouseScrollProcessor;
    private MouseClickProcessor _mouseClickProcessor;
    private MouseMoveProcessor _mouseMoveProcessor;
    private RenderProcessor _renderProcessor;
    private StencilProcessor _stencilProcessor;

    private Scale _scale = new Scale();

    boolean fullScreenRequest = false;
    boolean maximizeRequest = false;
    boolean minimizeRequest = false;
    boolean updateSizeRequest = false;
    boolean updatePositionRequest = false;
    boolean focusRequest = false;

    private float _itemPyramidLevel = 1.0f;

    private float getItemPyramidLevel() {
        incItemPyramidLevel();
        return _itemPyramidLevel;
    }

    private void restoreItemPyramidLevel() {
        _itemPyramidLevel = 1.0f;
    }

    private void incItemPyramidLevel() {
        _itemPyramidLevel -= 0.000001f;
    }

    private ToolTipItem _tooltip = new ToolTipItem();

    ToolTipItem getToolTip() {
        return _tooltip;
    }

    private int _framebufferWidth = 0;
    private int _framebufferHeight = 0;

    Prototype getFocusedItem() {
        return _commonProcessor.focusedItem;
    }

    void setFocusedItem(Prototype item) {
        _commonProcessor.setFocusedItem(item);
    }

    void resetFocus() {
        _commonProcessor.resetFocus();
    }

    void resetItems() {
        _commonProcessor.resetItems();
    }

    GLWHandler glwHandler;
    private Shader _primitive;
    private Shader _texture;
    private Shader _char;
    private Shader _blur;

    private BufferedImage _iconSmall;
    private BufferedImage _iconBig;

    void setBigIcon(BufferedImage icon) {
        _iconBig = icon;
    }

    void setSmallIcon(BufferedImage icon) {
        _iconSmall = icon;
    }

    DrawEngine(CoreWindow handler) {
        glwHandler = new GLWHandler(handler);
        _commonProcessor = new CommonProcessor();
        _tooltip.setHandler(handler);
    }

    void dispose() {
        glfwTerminate();
    }

    boolean init() {
        if (!initWindow())
            return false;

        initGL();
        initShaders();
        initProcessors();
        if (_iconSmall != null && _iconBig != null)
            _commonProcessor.wndProcessor.applyIcon(_iconBig, _iconSmall);
        prepareCanvas();
        _tooltip.initElements();

        if (glwHandler.maximized) {
            _commonProcessor.window.isMaximized = false;
            _commonProcessor.window.maximize();
        }

        drawScene();
        return true;
    }

    private boolean initWindow() {
        CommonService.GlobalLocker.lock();
        try {
            glwHandler.createWindow();
            _scale.setScale(glwHandler.getCoreWindow().getDpiScale().getXScale(),
                    glwHandler.getCoreWindow().getDpiScale().getYScale());
            setEventsCallbacks();
            GL.createCapabilities();
            if (WindowManager.getVSyncValue() != 1)
                glfwSwapInterval(WindowManager.getVSyncValue());
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            glwHandler.clearEventsCallbacks();
            if (glwHandler.getWindowId() == NULL)
                glwHandler.destroy();
            glwHandler.getCoreWindow().close();
            return false;
        } finally {
            CommonService.GlobalLocker.unlock();
        }
    }

    private void initGL() {
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_ALPHA_TEST);
        glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_DST_ALPHA);
        glEnable(GL_DEPTH_TEST);
    }

    private void initShaders() {
        _primitive = ShaderFactory.getShader(ShaderFactory.PRIMITIVE);
        _texture = ShaderFactory.getShader(ShaderFactory.TEXTURE);
        _char = ShaderFactory.getShader(ShaderFactory.SYMBOL);
        _blur = ShaderFactory.getShader(ShaderFactory.BLUR);
    }

    private void initProcessors() {
        _commonProcessor.initProcessor(glwHandler, _tooltip);
        _mouseMoveProcessor = new MouseMoveProcessor(_commonProcessor);
        _mouseClickProcessor = new MouseClickProcessor(_commonProcessor);
        _mouseScrollProcessor = new MouseScrollProcessor(_commonProcessor);
        _keyInputProcessor = new KeyInputProcessor(_commonProcessor);
        _textInputProcessor = new TextInputProcessor(_commonProcessor);
        _stencilProcessor = new StencilProcessor(_commonProcessor);
        _renderProcessor = new RenderProcessor();
    }

    private void setEventsCallbacks() {
        glwHandler.setCallbackMouseMove(new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                mouseMove(window, xpos, ypos);
            }
        });
        glwHandler.setCallbackMouseClick(new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                mouseClick(window, button, action, mods);
            }
        });
        glwHandler.setCallbackMouseScroll(new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double dx, double dy) {
                mouseScroll(window, dx, dy);
            }
        });
        glwHandler.setCallbackKeyPress(new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                keyPress(window, key, scancode, action, mods);
            }
        });
        glwHandler.setCallbackTextInput(new GLFWCharModsCallback() {
            @Override
            public void invoke(long window, int codepoint, int mods) {
                textInput(window, codepoint, mods);
            }
        });
        glwHandler.setCallbackClose(new GLFWWindowCloseCallback() {
            @Override
            public void invoke(long window) {
                closeWindow(window);
            }
        });
        glwHandler.setCallbackFocus(new GLFWWindowFocusCallback() {
            @Override
            public void invoke(long window, boolean value) {
                focus(window, value);
            }
        });
        glwHandler.setCallbackResize(new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                resize(window, width, height);
            }
        });
        glwHandler.setCallbackPosition(new GLFWWindowPosCallback() {
            @Override
            public void invoke(long window, int xpos, int ypos) {
                position(window, xpos, ypos);
            }
        });
        glwHandler.setCallbackFramebuffer(new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                framebuffer(window, w, h);
            }
        });
        glwHandler.setCallbackRefresh(new GLFWWindowRefreshCallback() {
            @Override
            public void invoke(long window) {
                refresh(window);
            }
        });
        glwHandler.setCallbackDrop(new GLFWDropCallback() {
            @Override
            public void invoke(long window, int count, long paths) {
                drop(window, count, paths);
            }
        });

        glwHandler.setCallbackContentScale(new GLFWWindowContentScaleCallback() {
            @Override
            public void invoke(long window, float xscale, float yscale) {
                contentScale(window, xscale, yscale);
            }
        });
    }

    private void contentScale(long window, float x, float y) {
        _commonProcessor.window.setWindowScale(x, y);
        _scale.setScale(x, y);
        DisplayService.setDisplayScale(x, y);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(_commonProcessor.handler.getWindowId(), width, height);

        glwHandler.getCoreWindow().setWidthDirect((int) (width.get(0) / _scale.getXScale()));
        glwHandler.getCoreWindow().setHeightDirect((int) (height.get(0) / _scale.getYScale()));

        // подписать на обновление при смене фактора масштабирования
        // (текст в фиксированных по ширине элементов не обновляется - оно и понятно)
    }

    private void drop(long window, int count, long paths) {
        _commonProcessor.wndProcessor.drop(window, count, paths);
    }

    private void refresh(long window) {
        update();
        glwHandler.swap();
    }

    private void framebuffer(long window, int w, int h) {
        _framebufferWidth = w;
        _framebufferHeight = h;
        WindowManager.setContextCurrent(_commonProcessor.window);
        glViewport(0, 0, _framebufferWidth, _framebufferHeight);

        _renderProcessor.screenSquare.clear();
        _renderProcessor.screenSquare
                .genBuffers(RenderProcessor.getFullWindowRectangle(_framebufferWidth, _framebufferHeight));
        _renderProcessor.screenSquare.unbind();
    }

    void updateWindowSize() {
        if (CommonService.getOSType() == OSType.MAC) {
            _commonProcessor.wndProcessor.setWindowSize(_commonProcessor.window.getWidth(),
                    _commonProcessor.window.getHeight(), new Scale());
        } else {
            _commonProcessor.wndProcessor.setWindowSize(_commonProcessor.window.getWidth(),
                    _commonProcessor.window.getHeight(), _scale);
        }
    }

    void updateWindowPosition() {
        _commonProcessor.wndProcessor.setWindowPos(_commonProcessor.window.getX(), _commonProcessor.window.getY());
    }

    void minimizeWindow() {
        _commonProcessor.wndProcessor.minimizeWindow();
    }

    void maximizeWindow() {
        _commonProcessor.wndProcessor.maximizeWindow(_scale);
    }

    void fullScreen() {
        _commonProcessor.wndProcessor.fullScreenWindow();
    }

    private void closeWindow(long wnd) {
        glfwSetWindowShouldClose(glwHandler.getWindowId(), false);
        _commonProcessor.window.eventClose.execute();
    }

    void focus(long wnd, boolean value) {
        _commonProcessor.wndProcessor.focus(wnd, value);
    }

    void focusWindow() {
        glfwFocusWindow(glwHandler.getWindowId());
    }

    private void resize(long wnd, int width, int height) {
        _tooltip.initTimer(false);

        if (CommonService.getOSType() != OSType.MAC) {
            _commonProcessor.window.setWidthDirect((int) (width / _scale.getXScale()));
            _commonProcessor.window.setHeightDirect((int) (height / _scale.getYScale()));
        } else {
            _commonProcessor.window.setWidthDirect(width);
            _commonProcessor.window.setHeightDirect(height);
        }

        if (!glwHandler.getCoreWindow().isBorderHidden) {
            List<InterfaceBaseItem> list = ItemsLayoutBox.getLayoutFloatItems(_commonProcessor.window.getWindowGuid());
            for (InterfaceBaseItem item : list) {
                if (item instanceof InterfaceFloating) {
                    InterfaceFloating floatItem = (InterfaceFloating) item;
                    if (floatItem.isOutsideClickClosable()) {
                        if (item instanceof ContextMenu) {
                            ContextMenu toClose = (ContextMenu) item;
                            if (toClose.closeDependencies(new MouseArgs()))
                                floatItem.hide();
                        } else {
                            floatItem.hide();
                        }
                    }
                }
            }
            // render();
            // glwHandler.swap();
        }
    }

    private void position(long wnd, int xpos, int ypos) {
        glwHandler.getPointer().setX(xpos);
        glwHandler.getPointer().setY(ypos);
        _commonProcessor.window.setXDirect(xpos);
        _commonProcessor.window.setYDirect(ypos);
    }

    void setWindowPos(int x, int y) {
        _commonProcessor.wndProcessor.setWindowPos(x, y);
    }

    boolean flagMove = false;

    private void mouseMove(long wnd, double xpos, double ypos) {
        if (!flagMove || _commonProcessor.inputLocker || !glwHandler.focusable)
            return;

        flagMove = false;
        _commonProcessor.events.setEvent(InputEventType.MOUSE_MOVE);
        _tooltip.initTimer(false);

        if (CommonService.getOSType() != OSType.MAC) {
            _mouseMoveProcessor.process(wnd, xpos / _scale.getXScale(), ypos / _scale.getXScale(), _scale);
        } else {
            _mouseMoveProcessor.process(wnd, xpos, ypos, new Scale());
        }
    }

    private void mouseClick(long wnd, int button, int action, int mods) {
        if (_commonProcessor.inputLocker || !glwHandler.focusable)
            return;
        _tooltip.initTimer(false);

        _commonProcessor.rootContainer.clearSides();
        _mouseClickProcessor.process(wnd, button, action, mods);
    }

    private void mouseScroll(long wnd, double dx, double dy) {
        // if (_commonProcessor.inputLocker)
        // return;
        _tooltip.initTimer(false);
        _mouseScrollProcessor.process(wnd, dx, dy);
        _commonProcessor.events.setEvent(InputEventType.MOUSE_SCROLL);
    }

    private void keyPress(long wnd, int key, int scancode, int action, int mods) {
        if (_commonProcessor.inputLocker || !glwHandler.focusable)
            return;
        _tooltip.initTimer(false);
        _keyInputProcessor.process(wnd, key, scancode, action, mods);
    }

    private void textInput(long wnd, int character, int mods) {
        if (_commonProcessor.inputLocker || !glwHandler.focusable)
            return;
        _tooltip.initTimer(false);
        _textInputProcessor.process(wnd, character, mods);
    }

    void setFrequency(RedrawFrequency value) {
        _renderProcessor.setFrequency(value);
    }

    RedrawFrequency getRedrawFrequency() {
        return _renderProcessor.getRedrawFrequency();
    }

    private VramFramebuffer _fboVertex = new VramFramebuffer();
    private VramFramebuffer _fboBlur = new VramFramebuffer();

    void drawScene() {
        if (focusRequest) {
            focusWindow();
            focusRequest = false;
        }
        if (fullScreenRequest) {
            fullScreen();
            fullScreenRequest = false;
        } else if (maximizeRequest) {
            maximizeWindow();
            maximizeRequest = false;
        } else if (minimizeRequest) {
            minimizeWindow();
            minimizeRequest = false;
        }
        if (updateSizeRequest) {
            updateWindowSize();
            _commonProcessor.events.resetEvent(InputEventType.WINDOW_RESIZE);
            updateSizeRequest = false;
        }
        if (updatePositionRequest) {
            updateWindowPosition();
            updatePositionRequest = false;
        }
        if (!_commonProcessor.events.lastEvent().contains(InputEventType.WINDOW_RESIZE)) {
            update();
            glwHandler.swap();
        }
        flagMove = true;
    }

    private void update() {
        _renderProcessor.flushResources();
        render();
        _stencilProcessor.clearBounds();
        restoreItemPyramidLevel();
        // Runtime.getRuntime().gc();
    }

    private void prepareCanvas() {
        glwHandler.gVAO = glGenVertexArrays();
        glBindVertexArray(glwHandler.gVAO);
        focus(glwHandler.getWindowId(), true);
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        glfwGetFramebufferSize(glwHandler.getWindowId(), w, h);
        _framebufferWidth = w.get(0);
        _framebufferHeight = h.get(0);
        glViewport(0, 0, _framebufferWidth, _framebufferHeight);
        _fboVertex.genFBO();
        _fboVertex.unbind();
        _fboBlur.genFBO();
        _fboBlur.unbind();

        glClearColor(0, 0, 0, 0);
        _renderProcessor.screenSquare
                .genBuffers(RenderProcessor.getFullWindowRectangle(_framebufferWidth, _framebufferHeight));
        _renderProcessor.screenSquare.unbind();
    }

    List<InterfaceOpenGLLayer> oglLine = new LinkedList<>();

    void freeOnClose() {
        _primitive.deleteShader();
        _texture.deleteShader();
        _char.deleteShader();
        _blur.deleteShader();
        _fboVertex.clear();
        _fboBlur.clear();
        _renderProcessor.clearResources();
        glDeleteVertexArrays(glwHandler.gVAO);

        // free ogl
        while (!oglLine.isEmpty()) {
            oglLine.get(0).free();
            oglLine.remove(0);
        }

        glwHandler.clearEventsCallbacks();
        glwHandler.destroy();
    }

    void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        drawStaticItems();
        drawFloatItems();
        drawToolTip();
        drawShadePillow();
    }

    private void drawStaticItems() {
        drawItems(_commonProcessor.rootContainer);
    }

    private void drawFloatItems() {
        List<InterfaceBaseItem> floatItems = new LinkedList<>(
                ItemsLayoutBox.getLayout(_commonProcessor.window.getWindowGuid()).getFloatItems());
        floatItems.remove(_tooltip);
        if (floatItems != null) {
            for (InterfaceBaseItem item : floatItems) {
                drawItems(item);
            }
        }
    }

    private boolean checkOutsideBorders(InterfaceBaseItem shell) {
        return _stencilProcessor.process(shell, _scale);
    }

    private void drawItems(InterfaceBaseItem root) {
        if (!root.isVisible() || !root.isDrawable())
            return;

        if (root instanceof InterfaceLines) {
            drawLines((InterfaceLines) root);
            return;
        }
        if (root instanceof InterfacePoints) {
            drawPoints((InterfacePoints) root);
            return;
        }
        if (root instanceof InterfaceImageItem) {
            drawShell(root);
            glDisable(GL_SCISSOR_TEST);
            drawImage((InterfaceImageItem) root);
            glDisable(GL_SCISSOR_TEST);
            drawCommonContent(root);
            return;
        }

        if (root instanceof InterfaceTextContainer) {
            drawText((InterfaceTextContainer) root);
            glDisable(GL_SCISSOR_TEST);
        }

        if (root instanceof InterfaceOpenGLLayer) {
            InterfaceOpenGLLayer ogl = (InterfaceOpenGLLayer) root;
            if (!oglLine.contains(ogl)) {
                oglLine.add(ogl);
            }
            drawShell(root);
            glDisable(GL_SCISSOR_TEST);

            drawOpenGLLayer(ogl);

            glClear(GL_DEPTH_BUFFER_BIT);
            glDisable(GL_DEPTH_TEST);

            drawCommonContent(root);

            glEnable(GL_DEPTH_TEST);
            return;
        }

        drawShell(root);
        glDisable(GL_SCISSOR_TEST);
        drawCommonContent(root);
    }

    private void drawCommonContent(InterfaceBaseItem root) {
        if (root instanceof Prototype) {
            List<InterfaceBaseItem> list = ((Prototype) root).getItems();
            for (InterfaceBaseItem child : list) {
                drawItems(child);
            }
        }
    }

    private void drawOpenGLLayer(InterfaceOpenGLLayer ogllRoot) {
        if (!ogllRoot.isInitialized())
            ogllRoot.initialize();

        if (!(ogllRoot instanceof InterfaceBaseItem))
            return;

        InterfaceBaseItem oglItem = (InterfaceBaseItem) ogllRoot;
        boolean stencil = checkOutsideBorders(oglItem);

        setViewPort(oglItem);
        ogllRoot.draw();
        restoreView();

        glDisable(GL_SCISSOR_TEST);
    }

    private void drawShell(InterfaceBaseItem shell) {
        boolean stencil = checkOutsideBorders(shell);

        if (shell.getBackground().getAlpha() == 0) {
            if (shell instanceof Prototype)
                drawBorder((Prototype) shell);
            return;
        }

        boolean preEffect = drawPreprocessingEffects(shell);

        if (ItemsRefreshManager.isRefreshShape(shell)) {
            shell.makeShape();

            if (shell.isShadowDrop())
                drawShadow(shell, stencil);

            ItemsRefreshManager.removeShape(shell);

            _renderProcessor.drawFreshVertex(_primitive, shell, getItemPyramidLevel(), shell.getX(), shell.getY(),
                    _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight(), shell.getBackground(),
                    GL_TRIANGLES);
        } else {
            if (shell.isShadowDrop())
                drawShadow(shell, stencil);
            _renderProcessor.drawStoredVertex(_primitive, shell, getItemPyramidLevel(), shell.getX(), shell.getY(),
                    _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight(), shell.getBackground(),
                    GL_TRIANGLES);
        }

        if (shell instanceof Prototype)
            drawBorder((Prototype) shell);

        if (preEffect)
            glDisable(GL_STENCIL_TEST);
    }

    private void drawBorder(Prototype vi) {
        if (vi.getBorderThickness() > 0) {
            List<float[]> vertex = BaseItemStatics
                    .updateShape(GraphicsMathService.getRoundSquareBorder(vi.getBorderRadius(), vi.getWidth(),
                            vi.getHeight(), vi.getBorderThickness(), 0, 0), vi.getWidth(), vi.getHeight());

            _renderProcessor.drawDirectVertex(_primitive, vertex, getItemPyramidLevel(), vi.getX(), vi.getY(),
                    _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight(), vi.getBorderFill(),
                    GL_TRIANGLES);
        }
    }

    float[] _weights;

    private void drawShadow(InterfaceBaseItem shell, boolean stencil) {
        int[] extension = shell.getShadowExtension();
        int xAddidion = extension[0] / 2;
        int yAddidion = extension[1] / 2;
        int res = shell.getShadowRadius();

        if (_weights == null) {
            _weights = new float[11];
            float sum, sigma2 = 4.0f;
            _weights[0] = gauss(0, sigma2);
            sum = _weights[0];
            for (int i = 1; i < 11; i++) {
                _weights[i] = gauss(i, sigma2);
                sum += 2 * _weights[i];
            }
            for (int i = 0; i < 11; i++)
                _weights[i] /= sum;
        }

        int fboWidth = shell.getWidth() + extension[0] + 2 * res;
        int fboHeight = shell.getHeight() + extension[1] + 2 * res;
        if (ItemsRefreshManager.isRefreshShape(shell) || _renderProcessor.shadowStorage.getResource(shell) == null) {
            if (stencil)
                glDisable(GL_SCISSOR_TEST);

            glViewport(0, 0, fboWidth, fboHeight);
            _fboVertex.genFBO();
            _fboVertex.genFboTexture(fboWidth, fboHeight);
            glClear(GL_COLOR_BUFFER_BIT);

            List<float[]> vertex = BaseItemStatics.updateShape(shell.getTriangles(), shell.getWidth() + extension[0],
                    shell.getHeight() + extension[1]);

            _renderProcessor.drawDirectVertex(_primitive, vertex, 0.0f, res, res, fboWidth, fboHeight,
                    shell.getShadowColor(), GL_TRIANGLES);

            _fboVertex.unbindTexture();
            _fboVertex.unbind();
            _fboBlur.bind();
            _fboBlur.genFboTexture(fboWidth, fboHeight);
            glClear(GL_COLOR_BUFFER_BIT);

            VramTexture store = _renderProcessor.drawDirectShadow(_blur, 0.0f, _weights, res, _fboVertex.texture, 0, 0,
                    shell.getWidth() + extension[0], shell.getHeight() + extension[1], fboWidth, fboHeight);
            store.clear();

            _fboBlur.unbindTexture();
            _fboBlur.unbind();

            if (stencil)
                glEnable(GL_SCISSOR_TEST);
            glViewport(0, 0, _framebufferWidth, _framebufferHeight);

            _renderProcessor.drawFreshShadow(_texture, shell, getItemPyramidLevel(), _fboBlur.texture,
                    shell.getX() + shell.getShadowPos().getX() - xAddidion - res,
                    shell.getY() + shell.getShadowPos().getY() - yAddidion - res, fboWidth, fboHeight,
                    _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight());

            // _fboVertex.clearTexture();
            _fboVertex.clear();
            _fboBlur.clear();
        } else {
            _renderProcessor.drawStoredShadow(_texture, shell, getItemPyramidLevel(),
                    shell.getX() + shell.getShadowPos().getX() - xAddidion - res,
                    shell.getY() + shell.getShadowPos().getY() - yAddidion - res, _commonProcessor.window.getWidth(),
                    _commonProcessor.window.getHeight());
        }
    }

    private float gauss(float x, float sigma) {
        double ans;
        ans = Math.exp(-(x * x) / (2f * sigma * sigma)) / Math.sqrt(2 * Math.PI * sigma * sigma);
        return (float) ans;
    }

    private void drawText(InterfaceTextContainer text) {

        InterfaceTextImage textImage = text.getTexture();

        if (textImage == null)
            return;

        if (textImage.isEmpty())
            return;

        checkOutsideBorders((InterfaceBaseItem) text);

        float[] argb = { (float) text.getForeground().getRed() / 255.0f,
                (float) text.getForeground().getGreen() / 255.0f, (float) text.getForeground().getBlue() / 255.0f,
                (float) text.getForeground().getAlpha() / 255.0f };

        if (ItemsRefreshManager.isRefreshText(text)) {
            ItemsRefreshManager.removeText(text);
            _renderProcessor.drawFreshText(_char, text, textImage, _scale, _commonProcessor.window.getWidth(),
                    _commonProcessor.window.getHeight(), getItemPyramidLevel(), argb);
        } else {
            _renderProcessor.drawStoredText(_char, text, textImage, _commonProcessor.window.getWidth(),
                    _commonProcessor.window.getHeight(), getItemPyramidLevel(), argb);
        }
    }

    private void drawPoints(InterfacePoints item) {
        if (item.getPointColor().getAlpha() == 0)
            return;

        if (!(item instanceof InterfaceBaseItem))
            return;
        InterfaceBaseItem shell = (InterfaceBaseItem) item;
        checkOutsideBorders(shell);

        float level = getItemPyramidLevel();

        if (ItemsRefreshManager.isRefreshShape(shell)) {
            ItemsRefreshManager.removeShape(shell);
            shell.makeShape();

            List<float[]> points = item.getPoints();
            if (points == null)
                return;

            List<float[]> shape = item.getPointShape();
            float centerOffset = item.getPointThickness() / 2.0f;
            float[] result = new float[shape.size() * points.size() * 2];
            int skew = 0;
            for (float[] p : points) {
                List<float[]> fig = GraphicsMathService.moveShape(shape, p[0] - centerOffset, p[1] - centerOffset);
                for (int i = 0; i < fig.size(); i++) {
                    result[skew + i * 2 + 0] = fig.get(i)[0];
                    result[skew + i * 2 + 1] = fig.get(i)[1];
                }
                skew += fig.size() * 2;
            }
            _renderProcessor.drawFreshVertex(_primitive, shell, result, level, shell.getX(), shell.getY(),
                    _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight(), item.getPointColor(),
                    GL_TRIANGLES);
        } else {
            _renderProcessor.drawStoredVertex(_primitive, shell, level, shell.getX(), shell.getY(),
                    _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight(), item.getPointColor(),
                    GL_TRIANGLES);
        }
    }

    private void drawLines(InterfaceLines item) {
        if (item.getLineColor().getAlpha() == 0)
            return;

        if (!(item instanceof InterfaceBaseItem))
            return;
        InterfaceBaseItem shell = (InterfaceBaseItem) item;// BAD!
        checkOutsideBorders(shell);

        if (ItemsRefreshManager.isRefreshShape(shell)) {
            ItemsRefreshManager.removeShape(shell);
            shell.makeShape();

            _renderProcessor.drawFreshVertex(_primitive, shell, getItemPyramidLevel(), item.getX(), item.getY(),
                    _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight(), item.getLineColor(),
                    GL_LINE_STRIP);
        } else {
            _renderProcessor.drawStoredVertex(_primitive, shell, getItemPyramidLevel(), shell.getX(), shell.getY(),
                    _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight(), item.getLineColor(),
                    GL_LINE_STRIP);
        }
    }

    private void drawImage(InterfaceImageItem image) {

        checkOutsideBorders((InterfaceBaseItem) image);

        int w = image.getImageWidth(), h = image.getImageHeight();
        Area area = image.getAreaBounds();

        if (ItemsRefreshManager.isRefreshImage(image)) {
            _renderProcessor.drawFreshTexture(image, _texture, area.getX(), area.getY(), area.getWidth(),
                    area.getHeight(), w, h, _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight(),
                    getItemPyramidLevel());
        } else {
            _renderProcessor.drawStoredTexture(image, _texture, area.getX(), area.getY(),
                    _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight(), getItemPyramidLevel());
        }
    }

    private Position tooltipBorderIndent = new Position(10, 2);

    private void drawToolTip() {
        if (!_tooltip.isVisible())
            return;

        _tooltip.setText(_commonProcessor.hoveredItem.getToolTip());

        int width = _tooltip.getPadding().left + _tooltip.getPadding().right + _tooltip.getTextLabel().getMargin().left
                + _tooltip.getTextLabel().getMargin().right + _tooltip.getTextWidth();
        _tooltip.setWidth(width);

        int height = _tooltip.getPadding().top + _tooltip.getPadding().bottom + _tooltip.getTextLabel().getMargin().top
                + _tooltip.getTextLabel().getMargin().bottom + _tooltip.getTextHeight();
        _tooltip.setHeight(height);

        // проверка сверху
        if (_commonProcessor.ptrRelease.getY() > _tooltip.getHeight()) {
            _tooltip.setY(_commonProcessor.ptrRelease.getY() - _tooltip.getHeight() - tooltipBorderIndent.getY());
        } else {
            _tooltip.setY(_commonProcessor.ptrRelease.getY() + CommonService.currentCursor.getCursorHeight()
                    + tooltipBorderIndent.getY());
        }
        // проверка справа
        if (_commonProcessor.ptrRelease.getX() - tooltipBorderIndent.getX()
                + _tooltip.getWidth() > glwHandler.getCoreWindow().getWidth() - tooltipBorderIndent.getX()) {
            _tooltip.setX(_commonProcessor.window.getWidth() - _tooltip.getWidth() - tooltipBorderIndent.getX());
        } else {
            _tooltip.setX(_commonProcessor.ptrRelease.getX() - tooltipBorderIndent.getX());
        }

        _tooltip.makeShape();

        if (_tooltip.isShadowDrop())
            drawToolTipShadow();

        _renderProcessor.drawDirectVertex(_primitive, _tooltip.getTriangles(), getItemPyramidLevel(), _tooltip.getX(),
                _tooltip.getY(), _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight(),
                _tooltip.getBackground(), GL_TRIANGLES);

        drawBorder(_tooltip);

        glDisable(GL_SCISSOR_TEST);

        drawItems(_tooltip);

        glDisable(GL_SCISSOR_TEST);
    }

    private void drawToolTipShadow() {
        int[] extension = _tooltip.getShadowExtension();
        int xAddidion = extension[0] / 2;
        int yAddidion = extension[1] / 2;
        int res = _tooltip.getShadowRadius();

        if (_weights == null) {
            _weights = new float[11];
            float sum, sigma2 = 4.0f;
            _weights[0] = gauss(0, sigma2);
            sum = _weights[0];
            for (int i = 1; i < 11; i++) {
                _weights[i] = gauss(i, sigma2);
                sum += 2 * _weights[i];
            }
            for (int i = 0; i < 11; i++)
                _weights[i] /= sum;
        }

        int fboWidth = _tooltip.getWidth() + extension[0] + 2 * res;
        int fboHeight = _tooltip.getHeight() + extension[1] + 2 * res;

        glViewport(0, 0, fboWidth, fboHeight);
        _fboVertex.genFBO();
        _fboVertex.genFboTexture(fboWidth, fboHeight);
        glClear(GL_COLOR_BUFFER_BIT);

        List<float[]> vertex = BaseItemStatics.updateShape(_tooltip.getTriangles(), _tooltip.getWidth() + extension[0],
                _tooltip.getHeight() + extension[1]);

        _renderProcessor.drawDirectVertex(_primitive, vertex, 0.0f, res, res, fboWidth, fboHeight,
                _tooltip.getShadowColor(), GL_TRIANGLES);

        _fboVertex.unbindTexture();
        _fboVertex.unbind();
        _fboBlur.bind();
        _fboBlur.genFboTexture(fboWidth, fboHeight);
        glClear(GL_COLOR_BUFFER_BIT);

        VramTexture store = _renderProcessor.drawDirectShadow(_blur, 0.0f, _weights, res, _fboVertex.texture, 0, 0,
                _tooltip.getWidth() + extension[0], _tooltip.getHeight() + extension[1], fboWidth, fboHeight);
        store.clear();

        _fboBlur.unbindTexture();
        _fboBlur.unbind();

        glViewport(0, 0, _framebufferWidth, _framebufferHeight);

        _renderProcessor.drawRawShadow(_texture, getItemPyramidLevel(), _fboBlur.texture,
                _tooltip.getX() + _tooltip.getShadowPos().getX() - xAddidion - res,
                _tooltip.getY() + _tooltip.getShadowPos().getY() - yAddidion - res, fboWidth, fboHeight,
                _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight());

        _fboVertex.clear();
        _fboBlur.clear();
    }

    private void drawShadePillow() {
        if (glwHandler.focusable)
            return;

        _renderProcessor.drawScreenRectangle(_primitive, getItemPyramidLevel(), 0, 0,
                _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight(),
                glwHandler.getCoreWindow().getShadeColor(), GL_TRIANGLES);
    }

    private boolean drawPreprocessingEffects(InterfaceBaseItem item) {
        if (Effects.getEffects(item) == null)
            return false;

        List<InterfaceEffect> effects = Effects.getEffects(item);
        glEnable(GL_STENCIL_TEST);
        glClear(GL_STENCIL_BUFFER_BIT);
        glClearStencil(1);
        glStencilMask(0xFF);
        glStencilFunc(GL_NEVER, 2, 0);
        glStencilOp(GL_REPLACE, GL_KEEP, GL_KEEP);
        for (InterfaceEffect effect : effects) {
            if (effect instanceof InterfaceSubtractFigure) {
                InterfaceSubtractFigure subtractFigure = (InterfaceSubtractFigure) effect;
                List<float[]> vertex = null;

                if (subtractFigure.getSubtractFigure().isFixed()) {

                    vertex = GraphicsMathService.moveShape(subtractFigure.getSubtractFigure().getFigure(),
                            subtractFigure.getXOffset(), subtractFigure.getYOffset(),
                            new Area(0, 0, item.getWidth(), item.getHeight()), subtractFigure.getAlignment());
                } else {
                    vertex = GraphicsMathService.moveShape(
                            GraphicsMathService.updateShape(subtractFigure.getSubtractFigure().getFigure(),
                                    (int) (item.getWidth() * subtractFigure.getWidthScale()),
                                    (int) (item.getHeight() * subtractFigure.getHeightScale()),
                                    new Area(0, 0, item.getWidth(), item.getHeight()), subtractFigure.getAlignment()),
                            subtractFigure.getXOffset(), subtractFigure.getYOffset());
                }

                _renderProcessor.drawDirectVertex(_primitive, vertex, 0, item.getX(), item.getY(),
                        _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight(), Color.white,
                        GL_TRIANGLES);
            }
        }
        glStencilFunc(GL_NOTEQUAL, 2, 255);
        return true;
    }
}
