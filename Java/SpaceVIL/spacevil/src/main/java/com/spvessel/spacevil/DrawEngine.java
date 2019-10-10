package com.spvessel.spacevil;

import java.awt.image.BufferedImage;
import java.util.*;
import java.nio.*;

import com.spvessel.spacevil.Common.*;
import com.spvessel.spacevil.Core.*;
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
    private CommonProcessor _commonProcessor;
    private TextInputProcessor _textInputProcessor;
    private KeyInputProcessor _keyInputProcessor;
    private MouseScrollProcessor _mouseScrollProcessor;
    private MouseClickProcessor _mouseClickProcessor;
    private MouseMoveProcessor _mouseMoveProcessor;
    private RenderProcessor _renderProcessor;
    private StencilProcessor _stencilProcessor;

    boolean fullScreenRequest = false;
    boolean maximizeRequest = false;
    boolean minimizeRequest = false;
    boolean updateSizeRequest = false;
    boolean updatePositionRequest = false;

    private float _itemPyramidLevel = 1.0f;

    private float getItemPyramidLevel() {
        incItemPyramidLevel();
        return _itemPyramidLevel;
    }

    private void restoreItemPyramidLevel() {
        _itemPyramidLevel = 1.0f;
    }

    private void incItemPyramidLevel() {
        _itemPyramidLevel -= 0.001f;
    }

    private ToolTip _tooltip = new ToolTip();

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
        _tooltip.getTextLine().setHandler(handler);
        _tooltip.getTextLine().setParent(_tooltip);
        _tooltip.initElements();
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
        return true;
    }

    private boolean initWindow() {
        CommonService.GlobalLocker.lock();
        try {
            glwHandler.createWindow();
            setEventsCallbacks();
            GL.createCapabilities();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.toString());
            glwHandler.clearEventsCallbacks();
            if (glwHandler.getWindowId() == NULL)
                glwHandler.destroy();
            glwHandler.getCoreWindow().getLayout().close();
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
        glfwMakeContextCurrent(_commonProcessor.window.getGLWID());
        glViewport(0, 0, _framebufferWidth, _framebufferHeight);
        _fbo.bindFBO();
        _fbo.clearTexture();
        _fbo.genFBOTexture(_framebufferWidth, _framebufferHeight);
        _fbo.unbindFBO();
    }

    void updateWindowSize() {
        _commonProcessor.wndProcessor.setWindowSize(_commonProcessor.window.getWidth(),
                _commonProcessor.window.getHeight());
    }

    void updateWindowPosition() {
        _commonProcessor.wndProcessor.setWindowPos(_commonProcessor.window.getX(), _commonProcessor.window.getY());
    }

    void minimizeWindow() {
        _commonProcessor.wndProcessor.minimizeWindow();
    }

    void maximizeWindow() {
        _commonProcessor.wndProcessor.maximizeWindow();
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

    void setWindowFocused() {
        glfwFocusWindow(glwHandler.getWindowId());
    }

    private void resize(long wnd, int width, int height) {
        _tooltip.initTimer(false);
        _commonProcessor.window.setWidthDirect(width);
        _commonProcessor.window.setHeightDirect(height);
    }

    void setWindowSize(int width, int height) {
        _commonProcessor.wndProcessor.setWindowSize(width, height);
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
        if (!flagMove || _commonProcessor.inputLocker)
            return;
        flagMove = false;
        _commonProcessor.events.setEvent(InputEventType.MOUSE_MOVE);
        _tooltip.initTimer(false);
        if (!glwHandler.focusable)
            return;
        _mouseMoveProcessor.process(wnd, xpos, ypos);
    }

    private void mouseClick(long wnd, int button, int action, int mods) {
        if (_commonProcessor.inputLocker || !glwHandler.focusable)
            return;
        _tooltip.initTimer(false);
        _commonProcessor.rootContainer.clearSides();
        _mouseClickProcessor.process(wnd, button, action, mods);
    }

    private void mouseScroll(long wnd, double dx, double dy) {
        if (_commonProcessor.inputLocker)
            return;
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

    private VRAMFramebuffer _fbo = new VRAMFramebuffer();

    void drawScene() {
        if (fullScreenRequest) {
            fullScreen();
            fullScreenRequest = false;
        }
        else if (maximizeRequest) {
            maximizeWindow();
            maximizeRequest = false;
        }
        else if (minimizeRequest) {
            minimizeWindow();
            minimizeRequest = false;
        }
        if (updateSizeRequest) {
            updateWindowSize();
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
        VRAMStorage.flush();
        render();
        _stencilProcessor.clearBounds();
        restoreItemPyramidLevel();
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
        _fbo.genFBO();
        _fbo.genFBOTexture(_framebufferWidth, _framebufferHeight);
        _fbo.unbindFBO();
        glClearColor(0, 0, 0, 0);
    }

    void freeOnClose() {
        _primitive.deleteShader();
        _texture.deleteShader();
        _char.deleteShader();
        _blur.deleteShader();
        _fbo.clearFBO();
        VRAMStorage.clear();
        glDeleteVertexArrays(glwHandler.gVAO);
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
        List<InterfaceBaseItem> float_items = new LinkedList<>(
                ItemsLayoutBox.getLayout(_commonProcessor.window.getWindowGuid()).getFloatItems());
        if (float_items != null) {
            for (InterfaceBaseItem item : float_items) {
                // if (item.getHeightPolicy() == SizePolicy.EXPAND) {
                // int[] confines = item.getConfines();
                // item.setConfines(confines[0], confines[1], 0,
                // _commonProcessor.rootContainer.getHeight());
                // item.setY(0);
                // item.setHeight(_commonProcessor.rootContainer.getHeight());
                // }
                // if (item.getWidthPolicy() == SizePolicy.EXPAND) {
                // int[] confines = item.getConfines();
                // item.setConfines(0, _commonProcessor.rootContainer.getWidth(), confines[2],
                // confines[3]);
                // item.setX(0);
                // item.setWidth(_commonProcessor.rootContainer.getWidth());
                // }
                drawItems(item);
            }
        }
    }

    private boolean checkOutsideBorders(InterfaceBaseItem shell) {
        return _stencilProcessor.process(shell);
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
                List<InterfaceBaseItem> list = ((Prototype) root).getItems();
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

        List<float[]> crdArray = shell.makeShape();
        if (crdArray == null)
            return;

        if (shell.isShadowDrop()) {
            drawShadow(shell);
        }

        _renderProcessor.drawVertex(_primitive, crdArray, getItemPyramidLevel(), shell.getBackground(), GL_TRIANGLES);
        crdArray.clear();

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
        int[] extension = shell.getShadowExtension();
        int xAddidion = extension[0] / 2;
        int yAddidion = extension[1] / 2;

        CustomShape shadow = new CustomShape();
        shadow.setBackground(shell.getShadowColor());
        shadow.setSize(shell.getWidth() + extension[0], shell.getHeight() + extension[1]);
        shadow.setPosition(shell.getX() + shell.getShadowPos().getX() - xAddidion,
                shell.getY() + shell.getShadowPos().getY() - yAddidion);
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

    private float gauss(float x, float sigma) {
        double ans;
        ans = Math.exp(-(x * x) / (2f * sigma * sigma)) / Math.sqrt(2 * Math.PI * sigma * sigma);
        return (float) ans;
    }

    private void drawShadowPart(float[] weights, int res, int fboTexture, float[] xy, float[] wh) {
        _renderProcessor.drawShadow(_blur, getItemPyramidLevel(), weights, res, fboTexture, xy, wh,
                _commonProcessor.window.getWidth(), _commonProcessor.window.getHeight());
    }

    private void drawText(InterfaceTextContainer text) {
        TextPrinter textPrt = text.getLetTextures();
//        textPrt = null;

        if (textPrt == null)
            return;
        ByteBuffer byteBuffer = textPrt.texture;
        if (byteBuffer == null || byteBuffer.limit() == 0)
            return;

        checkOutsideBorders((InterfaceBaseItem) text);

        int byteBufferHeight = textPrt.heightTexture, byteBufferWidth = textPrt.widthTexture;
        float x0 = ((float) textPrt.xTextureShift / (float) _commonProcessor.window.getWidth() * 2.0f) - 1.0f;
        float y0 = ((float) textPrt.yTextureShift / (float) _commonProcessor.window.getHeight() * 2.0f - 1.0f)
                * (-1.0f);
        float x1 = (((float) textPrt.xTextureShift + (float) byteBufferWidth / _commonProcessor.window.getDpiScale()[0])
                / (float) _commonProcessor.window.getWidth() * 2.0f) - 1.0f;
        float y1 = (((float) textPrt.yTextureShift
                + (float) byteBufferHeight / _commonProcessor.window.getDpiScale()[0])
                / (float) _commonProcessor.window.getHeight() * 2.0f - 1.0f) * (-1.0f);
        float[] argb = { (float) text.getForeground().getRed() / 255.0f,
                (float) text.getForeground().getGreen() / 255.0f, (float) text.getForeground().getBlue() / 255.0f,
                (float) text.getForeground().getAlpha() / 255.0f };

        _renderProcessor.drawText(_char, x0, x1, y0, y1, byteBufferWidth, byteBufferHeight, byteBuffer,
                getItemPyramidLevel(), argb);
//        Runtime.getRuntime().gc();
    }

    private void drawPoints(InterfacePoints item) {
        if (item.getPointColor().getAlpha() == 0)
            return;

        List<float[]> crd_array = item.makeShape();
        if (crd_array == null)
            return;
        checkOutsideBorders((InterfaceBaseItem) item);
        List<float[]> point = item.getShapePointer();
        float centerOffset = item.getPointThickness() / 2.0f;
        float[] result = new float[point.size() * crd_array.size() * 3];
        int skew = 0;
        float level = getItemPyramidLevel();
        for (float[] shape : crd_array) {
            List<float[]> fig = GraphicsMathService.toGL(
                    GraphicsMathService.moveShape(point, shape[0] - centerOffset, shape[1] - centerOffset),
                    _commonProcessor.window);

            for (int i = 0; i < fig.size(); i++) {
                result[skew + i * 3 + 0] = fig.get(i)[0];
                result[skew + i * 3 + 1] = fig.get(i)[1];
                result[skew + i * 3 + 2] = level;
            }
            skew += fig.size() * 3;
        }
        _renderProcessor.drawVertex(_primitive, result, item.getPointColor(), GL_TRIANGLES);
    }

    private void drawLines(InterfaceLine item) {
        if (item.getLineColor().getAlpha() == 0)
            return;

        List<float[]> crd_array = GraphicsMathService.toGL(item.makeShape(), _commonProcessor.window);
        if (crd_array == null)
            return;
        checkOutsideBorders((InterfaceBaseItem) item);
        _renderProcessor.drawVertex(_primitive, crd_array, getItemPyramidLevel(), item.getLineColor(), GL_LINE_STRIP);
    }

    private void drawImage(InterfaceImageItem image) {
        checkOutsideBorders((InterfaceBaseItem) image);

        byte[] bitmap = image.getPixMapImage();
        if (bitmap == null)
            return;

        int w = image.getImageWidth(), h = image.getImageHeight();
        RectangleBounds area = image.getRectangleBounds();

        float x0 = ((float) area.getX() / (float) _commonProcessor.window.getWidth() * 2.0f) - 1.0f;
        float y0 = ((float) area.getY() / (float) _commonProcessor.window.getHeight() * 2.0f - 1.0f) * (-1.0f);
        float x1 = (((float) area.getX() + (float) area.getWidth()) / (float) _commonProcessor.window.getWidth() * 2.0f)
                - 1.0f;
        float y1 = (((float) area.getY() + (float) area.getHeight()) / (float) _commonProcessor.window.getHeight()
                * 2.0f - 1.0f) * (-1.0f);

        if (image instanceof ImageItem) {
            ImageItem tmp = (ImageItem) image;
            if (tmp.isNew()) {
                _renderProcessor.drawFreshTexture(tmp, _texture, x0, x1, y0, y1, w, h, bitmap, getItemPyramidLevel());
            } else {
                _renderProcessor.drawStoredTexture(tmp, _texture, x0, x1, y0, y1, getItemPyramidLevel());
            }
            return;
        }

        if (image.isColorOverlay())
            _renderProcessor.drawTextureWithColorOverlay(_texture, x0, x1, y0, y1, w, h, bitmap, getItemPyramidLevel(),
                    image.getRotationAngle(), image.getColorOverlay());
        else
            _renderProcessor.drawTextureAsIs(_texture, x0, x1, y0, y1, w, h, bitmap, getItemPyramidLevel(),
                    image.getRotationAngle());
    }

    private Pointer tooltipBorderIndent = new Pointer(10, 2);

    private void drawToolTip() {
        if (!_tooltip.isVisible())
            return;

        _tooltip.setText(_commonProcessor.hoveredItem.getToolTip());
        _tooltip.setWidth(_tooltip.getPadding().left + _tooltip.getPadding().right + _tooltip.getTextWidth());

        // проверка сверху
        if (_commonProcessor.ptrRelease.getY() > _tooltip.getHeight()) {
            _tooltip.setY(_commonProcessor.ptrRelease.getY() - _tooltip.getHeight() - tooltipBorderIndent.getY());
        } else {
            _tooltip.setY(_commonProcessor.ptrRelease.getY() + _tooltip.getHeight() + tooltipBorderIndent.getY());
        }
        // проверка справа
        if (_commonProcessor.ptrRelease.getX() - tooltipBorderIndent.getX() + _tooltip.getWidth() > glwHandler
                .getCoreWindow().getWidth()) {
            _tooltip.setX(_commonProcessor.window.getWidth() - _tooltip.getWidth() - tooltipBorderIndent.getX());
        } else {
            _tooltip.setX(_commonProcessor.ptrRelease.getX() - tooltipBorderIndent.getX());
        }
        drawShell(_tooltip);
        glDisable(GL_SCISSOR_TEST);
        _tooltip.getTextLine().updateGeometry();
        drawText(_tooltip.getTextLine());
        glDisable(GL_SCISSOR_TEST);
    }

    private void drawShadePillow() {
        if (glwHandler.focusable)
            return;

        List<float[]> vertex = new LinkedList<>(RenderProcessor.getFullWindowRectangle());
        _renderProcessor.drawVertex(_primitive, vertex, getItemPyramidLevel(),
                glwHandler.getCoreWindow().getShadeColor(), GL_TRIANGLES);
    }
}
