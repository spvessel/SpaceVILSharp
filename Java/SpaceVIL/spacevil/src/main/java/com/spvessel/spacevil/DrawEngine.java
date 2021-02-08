package com.spvessel.spacevil;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.*;
import com.spvessel.spacevil.Common.*;
import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Flags.*;
import com.spvessel.spacevil.internal.Wrapper.*;
import com.spvessel.spacevil.internal.Wrapper.OpenGLWrapper;
import static com.spvessel.spacevil.internal.Wrapper.OpenGLWrapper.*;

final class DrawEngine {

    void restoreCommonGLSettings() {
        gl.Enable(GL_TEXTURE_2D);
        gl.Enable(GL_BLEND);
        gl.Enable(GL_CULL_FACE);
        gl.CullFace(GL_BACK);
        gl.Enable(GL_ALPHA_TEST);
        gl.BlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_DST_ALPHA);
        gl.Enable(GL_DEPTH_TEST);
    }

    void restoreView() {
        gl.Viewport(0, 0, _framebufferWidth, _framebufferHeight);
    }

    void setGLLayerViewport(IOpenGLLayer layer) {
        if (!(layer instanceof IBaseItem))
            return;
        IBaseItem oglLayer = (IBaseItem) layer;
        setViewPort(oglLayer);
    }

    private void setViewPort(IBaseItem item) {
        int x = item.getX();
        int y = _commonProcessor.window.getHeight() - (item.getY() + item.getHeight());
        int w = item.getWidth();
        int h = item.getHeight();
        x = (int) ((float) x * _scale.getXScale());
        y = (int) ((float) y * _scale.getYScale());
        w = (int) ((float) w * _scale.getXScale());
        h = (int) ((float) h * _scale.getYScale());
        gl.Viewport(x, y, w, h);
    }

    <T> void freeVRAMResource(T resource) {
        if (_renderProcessor == null)
            return;
        _renderProcessor.freeResource(resource);
    }

    private OpenGLWrapper gl = null;
    private GlfwWrapper glfw = null;

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
    private Shader _shapeShader;
    private Shader _textureShader;
    private Shader _textShader;
    private Shader _blurShader;

    private BufferedImage _iconSmall;
    private BufferedImage _iconBig;

    void setBigIcon(BufferedImage icon) {
        _iconBig = icon;
    }

    void setSmallIcon(BufferedImage icon) {
        _iconSmall = icon;
    }

    DrawEngine(CoreWindow handler) {
        gl = OpenGLWrapper.get();
        glfw = GlfwWrapper.get();
        glwHandler = new GLWHandler(handler);
        _commonProcessor = new CommonProcessor();
        _tooltip.setHandler(handler);
    }

    void dispose() {
        glfw.Terminate();
    }

    boolean init() {
        if (!initWindow()) {
            return false;
        }

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
        try {
            glwHandler.createWindow();
            _scale.setScale(glwHandler.getCoreWindow().getDpiScale().getXScale(),
                    glwHandler.getCoreWindow().getDpiScale().getYScale());
            setEventsCallbacks();
            if (WindowManager.getVSyncValue() != 1)
                glfw.SwapInterval(WindowManager.getVSyncValue());
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            glwHandler.clearEventsCallbacks();
            if (glwHandler.getWindowId() == 0)
                glwHandler.destroy();
            glwHandler.getCoreWindow().close();
            return false;
        }
    }

    private void initGL() {
        gl.Enable(GL_TEXTURE_2D);
        gl.Enable(GL_BLEND);
        gl.Enable(GL_CULL_FACE);
        gl.CullFace(GL_BACK);
        gl.Enable(GL_ALPHA_TEST);
        gl.BlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_DST_ALPHA);
        gl.Enable(GL_DEPTH_TEST);
    }

    private void initShaders() {
        _shapeShader = ShaderFactory.getShader(ShaderFactory.PRIMITIVE);
        _textureShader = ShaderFactory.getShader(ShaderFactory.TEXTURE);
        _textShader = ShaderFactory.getShader(ShaderFactory.SYMBOL);
        _blurShader = ShaderFactory.getShader(ShaderFactory.BLUR);
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
            public void invoke(long window, int count, String[] paths) {
                drop(window, count, paths);
            }
        });

        glwHandler.setCallbackContentScale(new GLFWWindowContentScaleCallback() {
            @Override
            public void invoke(long window, float xscale, float yscale) {
                contentScale(window, xscale, yscale);
            }
        });

        glwHandler.setCallbackIconify(new GLFWWindowIconifyCallback() {

            @Override
            public void invoke(long window, boolean value) {
                iconify(window, value);
            }
        });
    }

    private void contentScale(long window, float x, float y) {
        _commonProcessor.window.setWindowScale(x, y);
        _scale.setScale(x, y);
        DisplayService.setDisplayScale(x, y);

        int[] size = glfw.GetWindowSize(_commonProcessor.handler.getWindowId());

        glwHandler.getCoreWindow().setWidthDirect((int) (size[0] / _scale.getXScale()));
        glwHandler.getCoreWindow().setHeightDirect((int) (size[1] / _scale.getYScale()));

        // подписать на обновление при смене фактора масштабирования
        // (текст в фиксированных по ширине элементов не обновляется - оно и понятно)
    }

    private void iconify(long window, boolean value) {
        _commonProcessor.window.setMinimized(value);
    }

    private void drop(long window, int count, String[] paths) {
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
        gl.Viewport(0, 0, _framebufferWidth, _framebufferHeight);

        _renderProcessor.screenSquare.clear();
        _renderProcessor.screenSquare
                .genBuffers(RenderProcessor.getFullWindowRectangle(_framebufferWidth, _framebufferHeight));
        _renderProcessor.screenSquare.unbind();
    }

    void updateWindowSize() {
        if (CommonService.getOSType() == OSType.Mac) {
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
        glfw.SetWindowShouldClose(glwHandler.getWindowId(), 0);
        _commonProcessor.window.eventClose.execute();
    }

    void focus(long wnd, boolean value) {
        _commonProcessor.wndProcessor.focus(wnd, value);
    }

    void focusWindow() {
        glfw.FocusWindow(glwHandler.getWindowId());
    }

    private void resize(long wnd, int width, int height) {
        _tooltip.initTimer(false);

        if (CommonService.getOSType() != OSType.Mac) {
            _commonProcessor.window.setWidthDirect((int) (width / _scale.getXScale()));
            _commonProcessor.window.setHeightDirect((int) (height / _scale.getYScale()));
        } else {
            _commonProcessor.window.setWidthDirect(width);
            _commonProcessor.window.setHeightDirect(height);
        }

        if (!glwHandler.getCoreWindow().isBorderHidden) {
            List<IBaseItem> list = ItemsLayoutBox.getLayoutFloatItems(_commonProcessor.window.getWindowGuid());
            for (IBaseItem item : list) {
                if (item instanceof IFloating) {
                    IFloating floatItem = (IFloating) item;
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
        _commonProcessor.events.setEvent(InputEventType.MouseMove);
        _tooltip.initTimer(false);

        if (CommonService.getOSType() != OSType.Mac) {
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
        _commonProcessor.events.setEvent(InputEventType.MouseScroll);
    }

    private void keyPress(long wnd, int key, int scancode, int action, int mods) {
        if (_commonProcessor.inputLocker || !glwHandler.focusable)
            return;
        _tooltip.initTimer(false);
        if (key == KeyCode.Unknown.getValue()) {
            key = DefaultsService.getKeyCodeByScancode(scancode).getValue();
        }
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
            _commonProcessor.events.resetEvent(InputEventType.WindowResize);
            updateSizeRequest = false;
        }
        if (updatePositionRequest) {
            updateWindowPosition();
            updatePositionRequest = false;
        }
        if (!_commonProcessor.events.lastEvent().contains(InputEventType.WindowResize)) {
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
        glwHandler.gVAO = gl.GenVertexArray();
        gl.BindVertexArray(glwHandler.gVAO);
        focus(glwHandler.getWindowId(), true);
        int[] size = glfw.GetFramebufferSize(glwHandler.getWindowId());
        _framebufferWidth = size[0];
        _framebufferHeight = size[1];
        gl.Viewport(0, 0, _framebufferWidth, _framebufferHeight);
        _fboVertex.genFBO();
        _fboVertex.unbind();
        _fboBlur.genFBO();
        _fboBlur.unbind();

        gl.ClearColor(0, 0, 0, 0);
        _renderProcessor.screenSquare
                .genBuffers(RenderProcessor.getFullWindowRectangle(_framebufferWidth, _framebufferHeight));
        _renderProcessor.screenSquare.unbind();
    }

    List<IOpenGLLayer> oglLine = new LinkedList<>();

    void freeOnClose() {
        if (_stop != null) {
            _stop.stop();
            _stop = null;
        }
        _shapeShader.deleteShader();
        _textureShader.deleteShader();
        _textShader.deleteShader();
        _blurShader.deleteShader();
        _fboVertex.clear();
        _fboBlur.clear();
        _renderProcessor.clearResources();
        gl.DeleteVertexArray(glwHandler.gVAO);

        // free ogl
        while (!oglLine.isEmpty()) {
            oglLine.get(0).free();
            oglLine.remove(0);
        }

        glwHandler.clearEventsCallbacks();
        glwHandler.destroy();
    }

    private javax.swing.Timer _stop = null;
    private boolean _updateFramerate = false;
    private int _framerate = 0;
    private int _framerateRecord = 0;
    private double _frametime = 0;
    private double _frametimeRecord = 0;
    private Label _benchmarkLabel = null;
    private boolean _isBenchmarkEnabled = false;

    void render() {
        if (RenderService.getMonitoringIndicators() != null) {
            _isBenchmarkEnabled = true;
            if (_benchmarkLabel == null) {
                _benchmarkLabel = new Label();
                _benchmarkLabel.setHandler(_commonProcessor.window);
                _benchmarkLabel.setBackground(0, 0, 0, 100);
                _benchmarkLabel.setForeground(255, 255, 255);
                _benchmarkLabel.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
                _benchmarkLabel.setPosition(20, 20);
                _benchmarkLabel.setPadding(10, 0, 10, 0);
                _benchmarkLabel.initElements();
            }
        } else if (_isBenchmarkEnabled) {
            _isBenchmarkEnabled = false;
        }

        if (_isBenchmarkEnabled) {
            _frametime = System.nanoTime() / 1000000.0;
            if (_stop == null) {
                java.awt.event.ActionListener taskPerformer = new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        _updateFramerate = true;
                        _framerateRecord = _framerate;
                        _framerate = 0;
                        _stop.stop();
                        _stop = null;
                    }
                };
                _stop = new javax.swing.Timer(1000, taskPerformer);
                _stop.start();
            }
        }

        gl.Clear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        drawStaticItems();
        drawFloatItems();
        drawToolTip();
        drawShadePillow();

        if (_isBenchmarkEnabled) {
            _frametimeRecord = System.nanoTime() / 1000000.0 - _frametime;
            _framerate++;
            drawBenchmark();
        }
    }

    private void drawBenchmark() {
        if (_updateFramerate) {

            Set<BenchmarkIndicator> indicators = RenderService.getMonitoringIndicators();
            String indicatorsString = "";
            int height = indicators.size() * 24;
            if (indicators.contains(BenchmarkIndicator.Framerate)) {
                indicatorsString = "Framerate: " + _framerateRecord;
            }
            if (indicators.size() > 1) {
                indicatorsString += "\n";
            }
            if (indicators.contains(BenchmarkIndicator.Frametime)) {
                indicatorsString += "Frametime: " + String.format("%.2f", _frametimeRecord) + " ms";
            }
            _benchmarkLabel.setText(indicatorsString);
            int width = 150;
            if (width < _benchmarkLabel.getTextWidth()) {
                width = _benchmarkLabel.getTextWidth() + _benchmarkLabel.getPadding().right
                        + _benchmarkLabel.getPadding().left;
            }
            _benchmarkLabel.setSize(width, height);
            _updateFramerate = false;
        }

        _benchmarkLabel.makeShape();

        _renderProcessor.drawDirectVertex(_shapeShader, _benchmarkLabel.getTriangles(), getItemPyramidLevel(),
                _benchmarkLabel.getX(), _benchmarkLabel.getY(), _commonProcessor.window.getWidth(),
                _commonProcessor.window.getHeight(), _benchmarkLabel.getBackground(), GL_TRIANGLES);

        gl.Disable(GL_SCISSOR_TEST);
        drawItems(_benchmarkLabel);
        gl.Disable(GL_SCISSOR_TEST);
    }

    private void drawStaticItems() {
        drawItems(_commonProcessor.rootContainer);
    }

    private void drawFloatItems() {
        List<IBaseItem> floatItems = new LinkedList<>(
                ItemsLayoutBox.getLayout(_commonProcessor.window.getWindowGuid()).getFloatItems());
        floatItems.remove(_tooltip);
        if (floatItems != null) {
            for (IBaseItem item : floatItems) {
                drawItems(item);
            }
        }
    }

    private boolean checkOutsideBorders(IBaseItem shell) {
        return _stencilProcessor.process(shell, _scale);
    }

    private void drawItems(IBaseItem root) {
        if (!root.isVisible() || !root.isDrawable())
            return;

        if (root instanceof ILines) {
            drawLines((ILines) root);
            return;
        }
        if (root instanceof IPoints) {
            drawPoints((IPoints) root);
            return;
        }
        if (root instanceof IImageItem) {
            drawShell(root);
            gl.Disable(GL_SCISSOR_TEST);
            drawImage((IImageItem) root);
            gl.Disable(GL_SCISSOR_TEST);
            drawCommonContent(root);
            return;
        }

        if (root instanceof ITextContainer) {
            drawText((ITextContainer) root);
            gl.Disable(GL_SCISSOR_TEST);
        }

        if (root instanceof IOpenGLLayer) {
            IOpenGLLayer ogl = (IOpenGLLayer) root;
            if (!oglLine.contains(ogl)) {
                oglLine.add(ogl);
            }
            drawShell(root);
            gl.Disable(GL_SCISSOR_TEST);

            drawOpenGLLayer(ogl);

            gl.Clear(GL_DEPTH_BUFFER_BIT);
            gl.Disable(GL_DEPTH_TEST);

            drawCommonContent(root);

            gl.Enable(GL_DEPTH_TEST);
            return;
        }

        drawShell(root);
        gl.Disable(GL_SCISSOR_TEST);
        drawCommonContent(root);
    }

    private void drawCommonContent(IBaseItem root) {
        if (root instanceof Prototype) {
            List<IBaseItem> list = ((Prototype) root).getItems();
            for (IBaseItem child : list) {
                drawItems(child);
            }
        }
    }

    private void drawOpenGLLayer(IOpenGLLayer ogllRoot) {
        if (!ogllRoot.isInitialized())
            ogllRoot.initialize();

        if (!(ogllRoot instanceof IBaseItem))
            return;

        IBaseItem oglItem = (IBaseItem) ogllRoot;
        boolean stencil = checkOutsideBorders(oglItem);

        setViewPort(oglItem);
        ogllRoot.draw();
        restoreView();

        gl.Disable(GL_SCISSOR_TEST);
    }

    private void drawShell(IBaseItem shell) {
        boolean stencil = checkOutsideBorders(shell);

        if (shell.getBackground().getAlpha() == 0) {
            if (shell instanceof Prototype)
                drawBorder((Prototype) shell);
            return;
        }

        List<IEffect> shadows = shell.effects().get(EffectType.Shadow);

        boolean preEffect = drawPreprocessingEffects(shell);
        if (ItemsRefreshManager.isRefreshShape(shell)) {
            shell.makeShape();

            for (IEffect shadow : shadows) {
                drawShadow(shell, (IShadow) shadow, stencil);
            }

            ItemsRefreshManager.removeShape(shell);

            _renderProcessor.drawFreshVertex(_shapeShader, shell, getItemPyramidLevel(), shell.getX(), shell.getY(),
                    _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight(), shell.getBackground(),
                    GL_TRIANGLES);
        } else {
            for (IEffect shadow : shadows) {
                drawShadow(shell, (IShadow) shadow, stencil);
            }
            _renderProcessor.drawStoredVertex(_shapeShader, shell, getItemPyramidLevel(), shell.getX(), shell.getY(),
                    _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight(), shell.getBackground(),
                    GL_TRIANGLES);
        }

        if (shell instanceof Prototype)
            drawBorder((Prototype) shell);

        if (preEffect)
            gl.Disable(GL_STENCIL_TEST);
    }

    private void drawBorder(Prototype vi) {
        if (vi.getBorderThickness() > 0) {
            List<float[]> vertex = BaseItemStatics
                    .updateShape(GraphicsMathService.getRoundSquareBorder(vi.getBorderRadius(), vi.getWidth(),
                            vi.getHeight(), vi.getBorderThickness(), 0, 0), vi.getWidth(), vi.getHeight());

            _renderProcessor.drawDirectVertex(_shapeShader, vertex, getItemPyramidLevel(), vi.getX(), vi.getY(),
                    _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight(), vi.getBorderFill(),
                    GL_TRIANGLES);
        }
    }

    float[] _weights;

    private void drawShadow(IBaseItem shell, IShadow shadow, boolean stencil) {

        if (!shadow.isApplied()) {
            return;
        }

        Size extension = shadow.getExtension();
        int xAddidion = extension.getWidth() / 2;
        int yAddidion = extension.getHeight() / 2;
        int res = shadow.getRadius();

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

        int fboWidth = shell.getWidth() + extension.getWidth() + 2 * res;
        int fboHeight = shell.getHeight() + extension.getHeight() + 2 * res;
        if (ItemsRefreshManager.isRefreshShape(shell) || _renderProcessor.shadowStorage.getResources(shell) == null) {
            if (stencil)
                gl.Disable(GL_SCISSOR_TEST);

            gl.Viewport(0, 0, fboWidth, fboHeight);
            _fboVertex.genFBO();
            _fboVertex.genFboTexture(fboWidth, fboHeight);
            gl.Clear(GL_COLOR_BUFFER_BIT);

            List<float[]> vertex = BaseItemStatics.updateShape(shell.getTriangles(),
                    shell.getWidth() + extension.getWidth(), shell.getHeight() + extension.getHeight());

            _renderProcessor.drawDirectVertex(_shapeShader, vertex, 0.0f, res, res, fboWidth, fboHeight,
                    shadow.getColor(), GL_TRIANGLES);

            _fboVertex.unbindTexture();
            _fboVertex.unbind();
            _fboBlur.bind();
            _fboBlur.genFboTexture(fboWidth, fboHeight);

            gl.ClearColor(shadow.getColor().getRed() / 255.0f, shadow.getColor().getGreen() / 255.0f,
                    shadow.getColor().getBlue() / 255.0f, 0.0f);

            gl.Clear(GL_COLOR_BUFFER_BIT);

            VramTexture store = _renderProcessor.drawDirectShadow(_blurShader, 0.0f, _weights, res, _fboVertex.texture,
                    0, 0, shell.getWidth() + extension.getWidth(), shell.getHeight() + extension.getHeight(), fboWidth,
                    fboHeight);
            store.clear();

            _fboBlur.unbindTexture();
            _fboBlur.unbind();

            if (stencil)
                gl.Enable(GL_SCISSOR_TEST);
            gl.Viewport(0, 0, _framebufferWidth, _framebufferHeight);

            _renderProcessor.drawFreshShadow(_textureShader, shell, shadow, getItemPyramidLevel(), _fboBlur.texture,
                    shell.getX() + shadow.getOffset().getX() - xAddidion - res,
                    shell.getY() + shadow.getOffset().getY() - yAddidion - res, fboWidth, fboHeight,
                    _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight());

            _fboVertex.clear();
            _fboBlur.clear();

            gl.ClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        } else {
            _renderProcessor.drawStoredShadow(_textureShader, shell, shadow, getItemPyramidLevel(),
                    shell.getX() + shadow.getOffset().getX() - xAddidion - res,
                    shell.getY() + shadow.getOffset().getY() - yAddidion - res, _commonProcessor.window.getWidth(),
                    _commonProcessor.window.getHeight());
        }
    }

    private float gauss(float x, float sigma) {
        double ans;
        ans = Math.exp(-(x * x) / (2f * sigma * sigma)) / Math.sqrt(2 * Math.PI * sigma * sigma);
        return (float) ans;
    }

    private void drawText(ITextContainer text) {

        ITextImage textImage = text.getTexture();

        if (textImage == null)
            return;

        if (textImage.isEmpty())
            return;

        checkOutsideBorders((IBaseItem) text);

        float[] argb = { (float) text.getForeground().getRed() / 255.0f,
                (float) text.getForeground().getGreen() / 255.0f, (float) text.getForeground().getBlue() / 255.0f,
                (float) text.getForeground().getAlpha() / 255.0f };

        if (ItemsRefreshManager.isRefreshText(text)) {
            ItemsRefreshManager.removeText(text);
            _renderProcessor.drawFreshText(_textShader, text, textImage, _scale, _commonProcessor.window.getWidth(),
                    _commonProcessor.window.getHeight(), getItemPyramidLevel(), argb);
        } else {
            _renderProcessor.drawStoredText(_textShader, text, textImage, _commonProcessor.window.getWidth(),
                    _commonProcessor.window.getHeight(), getItemPyramidLevel(), argb);
        }
    }

    private void drawPoints(IPoints item) {
        if (item.getPointColor().getAlpha() == 0)
            return;

        if (!(item instanceof IBaseItem))
            return;
        IBaseItem shell = (IBaseItem) item;
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
            _renderProcessor.drawFreshVertex(_shapeShader, shell, result, level, shell.getX(), shell.getY(),
                    _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight(), item.getPointColor(),
                    GL_TRIANGLES);
        } else {
            _renderProcessor.drawStoredVertex(_shapeShader, shell, level, shell.getX(), shell.getY(),
                    _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight(), item.getPointColor(),
                    GL_TRIANGLES);
        }
    }

    private void drawLines(ILines item) {
        if (item.getLineColor().getAlpha() == 0)
            return;

        if (!(item instanceof IBaseItem))
            return;
        IBaseItem shell = (IBaseItem) item;// BAD!
        checkOutsideBorders(shell);

        if (ItemsRefreshManager.isRefreshShape(shell)) {
            ItemsRefreshManager.removeShape(shell);
            shell.makeShape();

            _renderProcessor.drawFreshVertex(_shapeShader, shell, getItemPyramidLevel(), item.getX(), item.getY(),
                    _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight(), item.getLineColor(),
                    GL_LINE_STRIP);
        } else {
            _renderProcessor.drawStoredVertex(_shapeShader, shell, getItemPyramidLevel(), shell.getX(), shell.getY(),
                    _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight(), item.getLineColor(),
                    GL_LINE_STRIP);
        }
    }

    private void drawImage(IImageItem image) {

        checkOutsideBorders((IBaseItem) image);

        int w = image.getImageWidth(), h = image.getImageHeight();
        Area area = image.getAreaBounds();

        if (ItemsRefreshManager.isRefreshImage(image)) {
            _renderProcessor.drawFreshTexture(image, _textureShader, area.getX(), area.getY(), area.getWidth(),
                    area.getHeight(), w, h, _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight(),
                    getItemPyramidLevel());
        } else {
            _renderProcessor.drawStoredTexture(image, _textureShader, area.getX(), area.getY(),
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

        List<IEffect> shadows = _tooltip.effects().get(EffectType.Shadow);
        for (IEffect shadow : shadows) {
            drawToolTipShadow((IShadow) shadow);
        }

        _renderProcessor.drawDirectVertex(_shapeShader, _tooltip.getTriangles(), getItemPyramidLevel(), _tooltip.getX(),
                _tooltip.getY(), _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight(),
                _tooltip.getBackground(), GL_TRIANGLES);

        drawBorder(_tooltip);

        gl.Disable(GL_SCISSOR_TEST);

        drawItems(_tooltip);

        gl.Disable(GL_SCISSOR_TEST);
    }

    private void drawToolTipShadow(IShadow shadow) {
        if (!shadow.isApplied()) {
            return;
        }

        Size extension = shadow.getExtension();
        int xAddidion = extension.getWidth() / 2;
        int yAddidion = extension.getHeight() / 2;
        int res = shadow.getRadius();

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

        int fboWidth = _tooltip.getWidth() + extension.getWidth() + 2 * res;
        int fboHeight = _tooltip.getHeight() + extension.getHeight() + 2 * res;

        gl.Viewport(0, 0, fboWidth, fboHeight);
        _fboVertex.genFBO();
        _fboVertex.genFboTexture(fboWidth, fboHeight);
        gl.Clear(GL_COLOR_BUFFER_BIT);

        List<float[]> vertex = BaseItemStatics.updateShape(_tooltip.getTriangles(),
                _tooltip.getWidth() + extension.getWidth(), _tooltip.getHeight() + extension.getHeight());

        _renderProcessor.drawDirectVertex(_shapeShader, vertex, 0.0f, res, res, fboWidth, fboHeight, shadow.getColor(),
                GL_TRIANGLES);

        _fboVertex.unbindTexture();
        _fboVertex.unbind();
        _fboBlur.bind();
        _fboBlur.genFboTexture(fboWidth, fboHeight);
        gl.Clear(GL_COLOR_BUFFER_BIT);

        VramTexture store = _renderProcessor.drawDirectShadow(_blurShader, 0.0f, _weights, res, _fboVertex.texture, 0,
                0, _tooltip.getWidth() + extension.getWidth(), _tooltip.getHeight() + extension.getHeight(), fboWidth,
                fboHeight);
        store.clear();

        _fboBlur.unbindTexture();
        _fboBlur.unbind();

        gl.Viewport(0, 0, _framebufferWidth, _framebufferHeight);

        _renderProcessor.drawRawShadow(_textureShader, getItemPyramidLevel(), _fboBlur.texture,
                _tooltip.getX() + shadow.getOffset().getX() - xAddidion - res,
                _tooltip.getY() + shadow.getOffset().getY() - yAddidion - res, fboWidth, fboHeight,
                _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight());

        _fboVertex.clear();
        _fboBlur.clear();
    }

    private void drawShadePillow() {
        if (glwHandler.focusable)
            return;

        _renderProcessor.drawScreenRectangle(_shapeShader, getItemPyramidLevel(), 0, 0,
                _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight(),
                glwHandler.getCoreWindow().getShadeColor(), GL_TRIANGLES);
    }

    private boolean drawPreprocessingEffects(IBaseItem item) {
        List<IEffect> effects = item.effects().get(EffectType.Subtract);
        if (effects.isEmpty()) {
            return false;
        }

        gl.Enable(GL_STENCIL_TEST);
        gl.Clear(GL_STENCIL_BUFFER_BIT);
        gl.ClearStencil(1);
        gl.StencilMask(0xFF);
        gl.StencilFunc(GL_NEVER, 2, 0);
        gl.StencilOp(GL_REPLACE, GL_KEEP, GL_KEEP);
        for (IEffect effect : effects) {
            if (!effect.isApplied()) {
                continue;
            }
            if (effect instanceof ISubtractFigure) {
                ISubtractFigure subtractFigure = (ISubtractFigure) effect;
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

                _renderProcessor.drawDirectVertex(_shapeShader, vertex, 0, item.getX(), item.getY(),
                        _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight(), Color.white,
                        GL_TRIANGLES);
            }
        }
        gl.StencilFunc(GL_NOTEQUAL, 2, 255);
        return true;
    }
}
