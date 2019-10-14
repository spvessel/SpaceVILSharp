using System;
using System.Collections.Generic;

using Glfw3;
using System.Threading;
using System.Drawing;
using SpaceVIL.Core;
using Pointer = SpaceVIL.Core.Pointer;
using SpaceVIL.Common;
using static OpenGL.OpenGLConstants;
using static OpenGL.OpenGLWrapper;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    internal sealed class DrawEngine
    {
        internal void FreeVRAMResource<T>(T resource)
        {

            if (_renderProcessor == null)
                return;
            _renderProcessor.FreeResource(resource);
        }

        internal bool FullScreenRequest = false;
        internal bool MaximizeRequest = false;
        internal bool MinimizeRequest = false;
        internal bool UpdateSizeRequest = false;
        internal bool UpdatePositionRequest = false;

        private CommonProcessor _commonProcessor;
        private TextInputProcessor _textInputProcessor;
        private KeyInputProcessor _keyInputProcessor;
        private MouseScrollProcessor _mouseScrollProcessor;
        private MouseClickProcessor _mouseClickProcessor;
        private MouseMoveProcessor _mouseMoveProcessor;
        private RenderProcessor _renderProcessor;
        private StencilProcessor _stencilProcessor;

        private float _itemPyramidLevel = 1.0f;

        private float GetItemPyramidLevel()
        {
            IncItemPyramidLevel();
            return _itemPyramidLevel;
        }

        private void RestoreItemPyramidLevel()
        {
            _itemPyramidLevel = 1.0f;
        }

        private void IncItemPyramidLevel()
        {
            _itemPyramidLevel -= 0.000001f;
        }

        private ToolTipItem _tooltip = new ToolTipItem();
        internal ToolTipItem GetToolTip()
        {
            return _tooltip;
        }

        private int _framebufferWidth = 0;
        private int _framebufferHeight = 0;

        internal Prototype GetFocusedItem()
        {
            return _commonProcessor.FocusedItem;
        }

        internal void SetFocusedItem(Prototype item)
        {
            _commonProcessor.SetFocusedItem(item);
        }

        internal void ResetFocus()
        {
            _commonProcessor.ResetFocus();
        }

        internal void ResetItems()
        {
            _commonProcessor.ResetItems();
        }

        internal void SetIcons(Bitmap ibig, Bitmap ismall)
        {
            _commonProcessor.WndProcessor.SetIcons(ibig, ismall);
        }

        internal GLWHandler GLWHandler;
        private Shader _primitive;
        private Shader _texture;
        private Shader _char;
        private Shader _blur;
        private Shader _clone;

        internal DrawEngine(CoreWindow handler)
        {
            GLWHandler = new GLWHandler(handler);
            _commonProcessor = new CommonProcessor();
            _tooltip.SetHandler(handler);
        }

        internal void Dispose()
        {
            Glfw.Terminate();
        }

        internal bool Init()
        {
            if (!InitWindow())
                return false;
            InitGL();
            InitShaders();
            InitProcessors();
            _commonProcessor.WndProcessor.ApplyIcon();
            PrepareCanvas();
            _tooltip.InitElements();
            return true;
        }

        private bool InitWindow()
        {
            Monitor.Enter(CommonService.GlobalLocker);
            try
            {
                GLWHandler.CreateWindow();
                SetEventsCallbacks();
                if (WindowManager.GetVSyncValue() != 1)
                    Glfw.SwapInterval(WindowManager.GetVSyncValue());
                return true;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                GLWHandler.ClearEventsCallbacks();
                if (GLWHandler.GetWindowId() == 0)
                    GLWHandler.Destroy();
                GLWHandler.GetCoreWindow().Close();
                return false;
            }
            finally
            {
                Monitor.Exit(CommonService.GlobalLocker);
            }
        }
        private void InitGL()
        {
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_BLEND);
            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
            glEnable(GL_ALPHA_TEST);
            glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_DST_ALPHA);
            glEnable(GL_DEPTH_TEST);
        }

        private void InitShaders()
        {
            _primitive = ShaderFactory.GetShader(ShaderFactory.Primitive);
            _texture = ShaderFactory.GetShader(ShaderFactory.Texture);
            _char = ShaderFactory.GetShader(ShaderFactory.Symbol);
            _blur = ShaderFactory.GetShader(ShaderFactory.Blur);
        }

        private void InitProcessors()
        {
            _commonProcessor.InitProcessor(GLWHandler, _tooltip);
            _mouseMoveProcessor = new MouseMoveProcessor(_commonProcessor);
            _mouseClickProcessor = new MouseClickProcessor(_commonProcessor);
            _mouseScrollProcessor = new MouseScrollProcessor(_commonProcessor);
            _keyInputProcessor = new KeyInputProcessor(_commonProcessor);
            _textInputProcessor = new TextInputProcessor(_commonProcessor);
            _stencilProcessor = new StencilProcessor(_commonProcessor);
            _renderProcessor = new RenderProcessor();
        }

        private void SetEventsCallbacks()
        {
            GLWHandler.SetCallbackMouseMove(MouseMove);
            GLWHandler.SetCallbackMouseClick(MouseClick);
            GLWHandler.SetCallbackMouseScroll(MouseScroll);
            GLWHandler.SetCallbackKeyPress(KeyPress);
            GLWHandler.SetCallbackTextInput(TextInput);
            GLWHandler.SetCallbackClose(CloseWindow);
            GLWHandler.SetCallbackPosition(Position);
            GLWHandler.SetCallbackFocus(Focus);
            GLWHandler.SetCallbackResize(Resize);
            GLWHandler.SetCallbackFramebuffer(Framebuffer);
            GLWHandler.SetCallbackRefresh(Refresh);
            GLWHandler.SetCallbackDrop(Drop);
        }

        private void Drop(Int64 window, int count, string[] paths)
        {
            _commonProcessor.WndProcessor.Drop(window, count, paths);
        }

        private void Refresh(Int64 window)
        {
            Update();
            GLWHandler.Swap();
        }

        private void Framebuffer(Int64 window, int w, int h)
        {
            _framebufferWidth = w;
            _framebufferHeight = h;
            Glfw.MakeContextCurrent(_commonProcessor.Window.GetGLWID());
            glViewport(0, 0, _framebufferWidth, _framebufferHeight);
            _renderProcessor.ScreenSquare.GenBuffers(
                RenderProcessor.GetFullWindowRectangle(_framebufferWidth, _framebufferHeight));
            _renderProcessor.ScreenSquare.Unbind();
        }

        internal void UpdateWindowSize()
        {
            _commonProcessor.WndProcessor.SetWindowSize(_commonProcessor.Window.GetWidth(),
                    _commonProcessor.Window.GetHeight());
        }

        internal void UpdateWindowPosition()
        {
            _commonProcessor.WndProcessor.SetWindowPos(_commonProcessor.Window.GetX(),
                    _commonProcessor.Window.GetY());
        }

        internal void MinimizeWindow()
        {
            _commonProcessor.WndProcessor.MinimizeWindow();
        }

        internal void MaximizeWindow()
        {
            _commonProcessor.WndProcessor.MaximizeWindow();
        }

        internal void FullScreen()
        {
            _commonProcessor.WndProcessor.FullScreenWindow();
        }

        private void CloseWindow(Int64 window)
        {
            Glfw.SetWindowShouldClose(GLWHandler.GetWindowId(), false);
            _commonProcessor.Window.EventClose.Invoke();
        }

        internal void Focus(Int64 window, bool value)
        {
            _commonProcessor.WndProcessor.Focus(window, value);
        }

        internal void SetWindowFocused()
        {
            Glfw.FocusWindow(GLWHandler.GetWindowId());
        }

        private void Resize(Int64 window, int width, int height)
        {
            _tooltip.InitTimer(false);
            GLWHandler.GetCoreWindow().SetWidthDirect(width);
            GLWHandler.GetCoreWindow().SetHeightDirect(height);

            if (!GLWHandler.GetCoreWindow().IsBorderHidden)
            {
                List<IBaseItem> list = ItemsLayoutBox.GetLayoutFloatItems(_commonProcessor.Window.GetWindowGuid());
                foreach (var item in list)
                {
                    IFloating floatItem = item as IFloating;
                    if (floatItem != null && floatItem.IsOutsideClickClosable())
                    {
                        ContextMenu toClose = item as ContextMenu;
                        if (toClose != null)
                        {
                            if (toClose.CloseDependencies(new MouseArgs()))
                                floatItem.Hide();
                        }
                        else
                        {
                            floatItem.Hide();
                        }
                    }
                }
            }
        }

        internal void SetWindowSize(int width, int height)
        {
            _commonProcessor.WndProcessor.SetWindowSize(width, height);
        }

        private void Position(Int64 window, int xpos, int ypos)
        {
            GLWHandler.GetPointer().SetX(xpos);
            GLWHandler.GetPointer().SetY(ypos);
            _commonProcessor.Window.SetXDirect(xpos);
            _commonProcessor.Window.SetYDirect(ypos);
        }

        internal void SetWindowPos(int x, int y)
        {
            _commonProcessor.WndProcessor.SetWindowPos(x, y);
        }

        private bool flag_move = false;

        private void MouseMove(Int64 wnd, double xpos, double ypos)
        {
            if (!flag_move || _commonProcessor.InputLocker)
                return;
            flag_move = false;
            _commonProcessor.Events.SetEvent(InputEventType.MouseMove);
            _tooltip.InitTimer(false);
            if (!GLWHandler.Focusable)
                return;
            _mouseMoveProcessor.Process(wnd, xpos, ypos);
        }

        private void MouseClick(Int64 window, MouseButton button, InputState state, KeyMods mods)
        {
            if (_commonProcessor.InputLocker || !GLWHandler.Focusable)
                return;
            _tooltip.InitTimer(false);
            _commonProcessor.RootContainer.ClearSides();
            _mouseClickProcessor.Process(window, button, state, mods);
        }

        private void MouseScroll(Int64 window, double dx, double dy)
        {
            // if (_commonProcessor.InputLocker)
            //     return;
            _tooltip.InitTimer(false);
            _mouseScrollProcessor.Process(window, dx, dy);
            _commonProcessor.Events.SetEvent(InputEventType.MouseScroll);
        }

        private void KeyPress(Int64 window, KeyCode key, int scancode, InputState action, KeyMods mods)
        {
            if (_commonProcessor.InputLocker || !GLWHandler.Focusable)
                return;
            _tooltip.InitTimer(false);
            _keyInputProcessor.Process(window, key, scancode, action, mods);
        }

        private void TextInput(Int64 window, uint character, KeyMods mods)
        {
            if (_commonProcessor.InputLocker || !GLWHandler.Focusable)
                return;
            _tooltip.InitTimer(false);
            _textInputProcessor.Process(window, character, mods);
        }

        internal void SetFrequency(RedrawFrequency value)
        {
            _renderProcessor.SetFrequency(value);
        }

        internal RedrawFrequency GetRedrawFrequency()
        {
            return _renderProcessor.GetRedrawFrequency();
        }

        VramFramebuffer _fboVertex = new VramFramebuffer();
        VramFramebuffer _fboBlur = new VramFramebuffer();

        internal void DrawScene()
        {
            if (FullScreenRequest)
            {
                FullScreen();
                FullScreenRequest = false;
            }
            else if (MaximizeRequest)
            {
                MaximizeWindow();
                MaximizeRequest = false;
            }
            else if (MinimizeRequest)
            {
                MinimizeWindow();
                MinimizeRequest = false;
            }
            if (UpdateSizeRequest)
            {
                UpdateWindowSize();
                _commonProcessor.Events.ResetEvent(InputEventType.WindowResize);
                UpdateSizeRequest = false;
            }
            if (UpdatePositionRequest)
            {
                UpdateWindowPosition();
                UpdatePositionRequest = false;
            }
            if (!_commonProcessor.Events.LastEvent().HasFlag(InputEventType.WindowResize))
            {
                Update();
                GLWHandler.Swap();
            }
            flag_move = true;
        }

        internal void Update()
        {
            _renderProcessor.FlushResources();
            Render();
            _stencilProcessor.ClearBounds();
            RestoreItemPyramidLevel();
        }

        private void PrepareCanvas()
        {
            glGenVertexArrays(1, GLWHandler.GVAO);
            glBindVertexArray(GLWHandler.GVAO[0]);
            Focus(GLWHandler.GetWindowId(), true);
            int w, h;
            Glfw.GetFramebufferSize(GLWHandler.GetWindowId(), out w, out h);
            _framebufferWidth = w;
            _framebufferHeight = h;
            glViewport(0, 0, _framebufferWidth, _framebufferHeight);
            _fboVertex.GenFBO();
            _fboVertex.Unbind();
            _fboBlur.GenFBO();
            _fboBlur.Unbind();

            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            _renderProcessor.ScreenSquare.GenBuffers(
                RenderProcessor.GetFullWindowRectangle(_framebufferWidth, _framebufferHeight));
            _renderProcessor.ScreenSquare.Unbind();
        }

        internal void FreeOnClose()
        {
            _primitive.DeleteShader();
            _texture.DeleteShader();
            _char.DeleteShader();
            _blur.DeleteShader();
            _fboVertex.Clear();
            _fboBlur.Clear();
            _renderProcessor.ClearResources();
            glDeleteVertexArrays(1, GLWHandler.GVAO);
            GLWHandler.ClearEventsCallbacks();
            GLWHandler.Destroy();
        }

        internal void Render()
        {
            glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            DrawStaticItems();
            DrawFloatItems();
            DrawToolTip();
            DrawShadePillow();
        }

        private void DrawStaticItems()
        {
            DrawItems(_commonProcessor.RootContainer);
        }

        private void DrawFloatItems()
        {
            List<IBaseItem> floatItems = new List<IBaseItem>(ItemsLayoutBox.GetLayout(_commonProcessor.Window.GetWindowGuid()).FloatItems);
            floatItems.Remove(_tooltip);
            if (floatItems != null)
                foreach (var item in floatItems)
                    DrawItems(item);
        }

        private bool CheckOutsideBorders(IBaseItem shell)
        {
            return _stencilProcessor.Process(shell);
        }

        private void DrawItems(IBaseItem root)
        {
            if (!root.IsVisible() || !root.IsDrawable())
                return;

            if (root is ILine)
            {
                DrawLines((root as ILine));
            }
            if (root is IPoints)
            {
                DrawPoints((root as IPoints));
            }
            if (root is ITextContainer)
            {
                DrawText(root as ITextContainer);
                glDisable(GL_SCISSOR_TEST);
            }
            if (root is IImageItem)
            {
                DrawShell(root);
                glDisable(GL_SCISSOR_TEST);
                DrawImage(root as IImageItem);
                glDisable(GL_SCISSOR_TEST);
            }
            else
            {
                DrawShell(root);
                glDisable(GL_SCISSOR_TEST);
                Prototype rProto = root as Prototype;
                if (rProto != null)
                {
                    List<IBaseItem> list = rProto.GetItems();
                    foreach (var child in list)
                    {
                        DrawItems(child);
                    }
                }
            }
        }

        private void DrawShell(IBaseItem shell)
        {
            bool stencil = CheckOutsideBorders(shell);

            if (shell.GetBackground().A == 0)
            {
                Prototype pr = shell as Prototype;
                if (pr != null)
                    DrawBorder(pr);
                return;
            }

            bool preEffect = DrawPreEffect(shell);

            if (shell.IsRemakeRequest())
            {
                shell.MakeShape();
                if (shell.IsShadowDrop())
                    DrawShadow(shell, stencil);
                shell.SetRemakeRequest(false);
                _renderProcessor.DrawFreshVertex(
                    _primitive, shell, GetItemPyramidLevel(), shell.GetX(), shell.GetY(),
                    _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(),
                    shell.GetBackground(), GL_TRIANGLES);
            }
            else
            {
                if (shell.IsShadowDrop())
                    DrawShadow(shell, stencil);
                _renderProcessor.DrawStoredVertex(
                    _primitive, shell, GetItemPyramidLevel(), shell.GetX(), shell.GetY(),
                    _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(),
                    shell.GetBackground(), GL_TRIANGLES);
            }

            Prototype vi = shell as Prototype;
            if (vi != null)
                DrawBorder(vi);

            if (preEffect)
                glDisable(GL_STENCIL_TEST);
        }

        void DrawBorder(Prototype vi)
        {
            if (vi.GetBorderThickness() > 0)
            {
                List<float[]> vertex = BaseItemStatics.UpdateShape(
                    GraphicsMathService.GetRoundSquareBorder(
                        vi.GetBorderRadius(),
                        vi.GetWidth(),
                        vi.GetHeight(),
                        vi.GetBorderThickness(),
                        0,
                        0),
                        vi.GetWidth(),
                        vi.GetHeight());
                _renderProcessor.DrawDirectVertex(
                    _primitive, vertex, GetItemPyramidLevel(), vi.GetX(), vi.GetY(),
                    _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(),
                    vi.GetBorderFill(), GL_TRIANGLES);
            }
        }

        float[] _weights;

        void DrawShadow(IBaseItem shell, bool stencil)
        {
            int[] extension = shell.GetShadowExtension();
            int xAddidion = extension[0] / 2;
            int yAddidion = extension[1] / 2;
            int res = shell.GetShadowRadius();

            if (_weights == null)
            {
                _weights = new float[11];
                float sum, sigma2 = 4.0f;
                _weights[0] = Gauss(0, sigma2);
                sum = _weights[0];
                for (int i = 1; i < 11; i++)
                {
                    _weights[i] = Gauss(i, sigma2);
                    sum += 2 * _weights[i];
                }
                for (int i = 0; i < 11; i++)
                    _weights[i] /= sum;
            }

            int fboWidth = shell.GetWidth() + extension[0] + 2 * res;
            int fboHeight = shell.GetHeight() + extension[1] + 2 * res;
            if (shell.IsRemakeRequest())
            {
                if (stencil)
                    glDisable(GL_SCISSOR_TEST);

                glViewport(0, 0, fboWidth, fboHeight);
                _fboVertex.GenFBO();
                _fboVertex.GenFboTexture(fboWidth, fboHeight);
                glClear(GL_COLOR_BUFFER_BIT);

                List<float[]> vertex = BaseItemStatics.UpdateShape(
                    shell.GetTriangles(), shell.GetWidth() + extension[0], shell.GetHeight() + extension[1]);

                _renderProcessor.DrawDirectVertex(_primitive, vertex, 0.0f, res, res,
                    fboWidth, fboHeight, shell.GetShadowColor(), GL_TRIANGLES);

                _fboVertex.UnbindTexture();
                _fboVertex.Unbind();
                _fboBlur.Bind();
                _fboBlur.GenFboTexture(fboWidth, fboHeight);
                glClear(GL_COLOR_BUFFER_BIT);

                VramTexture store = _renderProcessor.DrawDirectShadow(
                    _blur, 0.0f, _weights, res, _fboVertex.Texture, 0, 0,
                    shell.GetWidth() + extension[0], shell.GetHeight() + extension[1],
                    fboWidth, fboHeight);
                store.Clear();

                _fboBlur.UnbindTexture();
                _fboBlur.Unbind();

                if (stencil)
                    glEnable(GL_SCISSOR_TEST);
                glViewport(0, 0, _framebufferWidth, _framebufferHeight);

                _renderProcessor.DrawFreshShadow(_texture, shell, GetItemPyramidLevel(), _fboBlur.Texture,
                    shell.GetX() + shell.GetShadowPos().GetX() - xAddidion - res,
                    shell.GetY() + shell.GetShadowPos().GetY() - yAddidion - res,
                    fboWidth, fboHeight, _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight());

                _fboVertex.Clear();
                _fboBlur.Clear();
            }
            else
            {
                _renderProcessor.DrawStoredShadow(_texture, shell, GetItemPyramidLevel(),
                    shell.GetX() + shell.GetShadowPos().GetX() - xAddidion - res,
                    shell.GetY() + shell.GetShadowPos().GetY() - yAddidion - res,
                    _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight());
            }
        }

        float Gauss(float x, float sigma)
        {
            double ans;
            ans = Math.Exp(-(x * x) / (2f * sigma * sigma)) / Math.Sqrt(2 * Math.PI * sigma * sigma);
            return (float)ans;
        }

        void DrawText(ITextContainer text)
        {
            TextPrinter textPrt = text.GetLetTextures();
            if (textPrt == null)
                return;

            byte[] byteBuffer = textPrt.Texture;
            if ((byteBuffer == null) || (byteBuffer.Length == 0))
                return;

            CheckOutsideBorders(text as IBaseItem);

            float[] argb = {
                (float) text.GetForeground().R / 255.0f,
                (float) text.GetForeground().G / 255.0f,
                (float) text.GetForeground().B / 255.0f,
                (float) text.GetForeground().A / 255.0f };

            if (text.IsRemakeText())
            {
                text.SetRemakeText(false);
                _renderProcessor.DrawFreshText(_char, text, textPrt,
                    _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(),
                    GetItemPyramidLevel(), argb);
            }
            else
            {
                _renderProcessor.DrawStoredText(_char, text, textPrt,
                    _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(),
                    GetItemPyramidLevel(), argb);
            }
        }

        void DrawPoints(IPoints item)
        {
            if (item.GetPointColor().A == 0)
                return;

            IBaseItem shell = item as IBaseItem;
            if (shell == null)
                return;
            CheckOutsideBorders(shell);

            float level = GetItemPyramidLevel();

            if (shell.IsRemakeRequest())
            {
                shell.SetRemakeRequest(false);
                shell.MakeShape();

                List<float[]> points = item.GetPoints();
                if (points == null)
                    return;

                List<float[]> shape = item.GetShapePointer();
                float centerOffset = item.GetPointThickness() / 2.0f;
                float[] result = new float[shape.Count * points.Count * 2];
                int skew = 0;
                foreach (float[] p in points)
                {
                    List<float[]> fig = GraphicsMathService.MoveShape(shape, p[0] - centerOffset, p[1] - centerOffset);
                    for (int i = 0; i < fig.Count; i++)
                    {
                        result[skew + i * 2 + 0] = fig[i][0];
                        result[skew + i * 2 + 1] = fig[i][1];
                    }
                    skew += fig.Count * 2;
                }
                _renderProcessor.DrawFreshVertex(
                    _primitive, shell, result, level, shell.GetX(), shell.GetY(),
                    _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(),
                    item.GetPointColor(), GL_TRIANGLES);
            }
            else
            {
                _renderProcessor.DrawStoredVertex(
                    _primitive, shell, level, shell.GetX(), shell.GetY(),
                    _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(),
                    item.GetPointColor(), GL_TRIANGLES);
            }
        }

        void DrawLines(ILine item)
        {
            if (item.GetLineColor().A == 0)
                return;

            IBaseItem shell = item as IBaseItem;
            if (shell == null)
                return;

            CheckOutsideBorders(shell);

            if (shell.IsRemakeRequest())
            {
                shell.SetRemakeRequest(false);
                shell.MakeShape();
                _renderProcessor.DrawFreshVertex(
                    _primitive, shell, GetItemPyramidLevel(), item.GetX(), item.GetY(),
                    _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(),
                    item.GetLineColor(), GL_LINE_STRIP);
            }
            else
            {
                _renderProcessor.DrawStoredVertex(
                    _primitive, shell, GetItemPyramidLevel(), item.GetX(), item.GetY(),
                    _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(),
                    item.GetLineColor(), GL_LINE_STRIP);
            }
        }

        void DrawImage(IImageItem image)
        {
            CheckOutsideBorders(image as IBaseItem);

            int w = image.GetImageWidth(), h = image.GetImageHeight();
            RectangleBounds area = image.GetRectangleBounds();

            ImageItem tmp = image as ImageItem;
            if (tmp != null)
            {
                if (tmp.IsNew())
                {
                    _renderProcessor.DrawFreshTexture(
                        tmp, _texture,
                        area.GetX(), area.GetY(), area.GetWidth(), area.GetHeight(),
                        w, h,
                        _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(),
                        GetItemPyramidLevel());
                }
                else
                {
                    _renderProcessor.DrawStoredTexture(
                        tmp, _texture, area.GetX(), area.GetY(),
                        _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(),
                        GetItemPyramidLevel());
                }
                return;
            }
            _renderProcessor.DrawTextureAsIs(
                _texture, image, area.GetX(), area.GetY(), area.GetWidth(), area.GetHeight(),
                w, h, _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(),
                GetItemPyramidLevel());
        }

        private Pointer tooltipBorderIndent = new Pointer(10, 2);

        private void DrawToolTip()
        {
            if (!_tooltip.IsVisible())
                return;

            _tooltip.SetText(_commonProcessor.HoveredItem.GetToolTip());
            int width = _tooltip.GetPadding().Left + _tooltip.GetPadding().Right + _tooltip.GetTextLabel().GetMargin().Left
                + _tooltip.GetTextLabel().GetMargin().Right + _tooltip.GetTextWidth();
            _tooltip.SetWidth(width);

            int height = _tooltip.GetPadding().Top + _tooltip.GetPadding().Bottom + _tooltip.GetTextLabel().GetMargin().Top
                + _tooltip.GetTextLabel().GetMargin().Bottom + _tooltip.GetTextHeight();
            _tooltip.SetHeight(height);

            //проверка сверху
            if (_commonProcessor.PtrRelease.GetY() > _tooltip.GetHeight())
                _tooltip.SetY(_commonProcessor.PtrRelease.GetY() - _tooltip.GetHeight() - tooltipBorderIndent.GetY());
            else
                _tooltip.SetY(_commonProcessor.PtrRelease.GetY() + CommonService.CurrentCursor.GetCursorHeight() + tooltipBorderIndent.GetY());
            //проверка справа
            if (_commonProcessor.PtrRelease.GetX() - tooltipBorderIndent.GetX() + _tooltip.GetWidth()
                > GLWHandler.GetCoreWindow().GetWidth() - tooltipBorderIndent.GetX())
                _tooltip.SetX(GLWHandler.GetCoreWindow().GetWidth() - _tooltip.GetWidth() - tooltipBorderIndent.GetX());
            else
                _tooltip.SetX(_commonProcessor.PtrRelease.GetX() - tooltipBorderIndent.GetX());

            _tooltip.MakeShape();

            if (_tooltip.IsShadowDrop())
                DrawToolTipShadow();

            _renderProcessor.DrawDirectVertex(
                _primitive, _tooltip.GetTriangles(), GetItemPyramidLevel(), _tooltip.GetX(), _tooltip.GetY(),
                _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(), _tooltip.GetBackground(),
                GL_TRIANGLES);

            DrawBorder(_tooltip);

            glDisable(GL_SCISSOR_TEST);

            DrawItems(_tooltip);

            glDisable(GL_SCISSOR_TEST);
        }

        private void DrawToolTipShadow()
        {
            int[] extension = _tooltip.GetShadowExtension();
            int xAddidion = extension[0] / 2;
            int yAddidion = extension[1] / 2;
            int res = _tooltip.GetShadowRadius();

            if (_weights == null)
            {
                _weights = new float[11];
                float sum, sigma2 = 4.0f;
                _weights[0] = Gauss(0, sigma2);
                sum = _weights[0];
                for (int i = 1; i < 11; i++)
                {
                    _weights[i] = Gauss(i, sigma2);
                    sum += 2 * _weights[i];
                }
                for (int i = 0; i < 11; i++)
                    _weights[i] /= sum;
            }

            int fboWidth = _tooltip.GetWidth() + extension[0] + 2 * res;
            int fboHeight = _tooltip.GetHeight() + extension[1] + 2 * res;

            glViewport(0, 0, fboWidth, fboHeight);
            _fboVertex.GenFBO();
            _fboVertex.GenFboTexture(fboWidth, fboHeight);
            glClear(GL_COLOR_BUFFER_BIT);

            List<float[]> vertex = BaseItemStatics.UpdateShape(
                _tooltip.GetTriangles(), _tooltip.GetWidth() + extension[0], _tooltip.GetHeight() + extension[1]);

            _renderProcessor.DrawDirectVertex(_primitive, vertex, 0.0f, res, res,
                fboWidth, fboHeight, _tooltip.GetShadowColor(), GL_TRIANGLES);

            _fboVertex.UnbindTexture();
            _fboVertex.Unbind();
            _fboBlur.Bind();
            _fboBlur.GenFboTexture(fboWidth, fboHeight);
            glClear(GL_COLOR_BUFFER_BIT);

            VramTexture store = _renderProcessor.DrawDirectShadow(
                _blur, 0.0f, _weights, res, _fboVertex.Texture, 0, 0,
                _tooltip.GetWidth() + extension[0], _tooltip.GetHeight() + extension[1],
                fboWidth, fboHeight);
            store.Clear();

            _fboBlur.UnbindTexture();
            _fboBlur.Unbind();

            glViewport(0, 0, _framebufferWidth, _framebufferHeight);

            _renderProcessor.DrawRawShadow(_texture, GetItemPyramidLevel(), _fboBlur.Texture,
                _tooltip.GetX() + _tooltip.GetShadowPos().GetX() - xAddidion - res,
                _tooltip.GetY() + _tooltip.GetShadowPos().GetY() - yAddidion - res,
                fboWidth, fboHeight,
                _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight());

            _fboVertex.Clear();
            _fboBlur.Clear();
        }

        private void DrawShadePillow()
        {
            if (GLWHandler.Focusable)
                return;

            _renderProcessor.DrawScreenRectangle(
                _primitive, GetItemPyramidLevel(), 0, 0,
                _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(),
                GLWHandler.GetCoreWindow().GetShadeColor(), GL_TRIANGLES);
        }

        private bool DrawPreEffect(IBaseItem item)
        {
            if (Effects.GetEffects(item) == null)
                return false;

            List<IEffect> effects = Effects.GetEffects(item);
            glEnable(GL_STENCIL_TEST);
            glClearStencil(1);
            glStencilMask(0xFF);
            glStencilFunc(GL_NEVER, 2, 0);
            glStencilOp(GL_REPLACE, GL_KEEP, GL_KEEP);
            foreach (IEffect effect in effects)
            {
                if (effect is ISubtractFigure)
                {
                    ISubtractFigure subtractFigure = (ISubtractFigure)effect;
                    List<float[]> vertex = null;

                    if (subtractFigure.GetSubtractFigure().IsFixed())
                    {
                        vertex = GraphicsMathService.MoveShape(
                            subtractFigure.GetSubtractFigure().GetFigure(),
                            subtractFigure.GetXOffset(),
                            subtractFigure.GetYOffset(),
                            new Area(0, 0, item.GetWidth(), item.GetHeight()),
                            subtractFigure.GetAlignment());
                    }
                    else
                    {
                        vertex = GraphicsMathService.MoveShape(
                                GraphicsMathService.UpdateShape(subtractFigure.GetSubtractFigure().GetFigure(),
                                        (int)(item.GetWidth() * subtractFigure.GetWidthScale()),
                                        (int)(item.GetHeight() * subtractFigure.GetHeightScale()),
                                        new Area(0, 0, item.GetWidth(), item.GetHeight()), subtractFigure.GetAlignment()),
                                subtractFigure.GetXOffset(), subtractFigure.GetYOffset());
                    }

                    _renderProcessor.DrawDirectVertex(_primitive, vertex, 0, item.GetX(), item.GetY(),
                            _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(), Color.White,
                            GL_TRIANGLES);

                }
            }
            glStencilFunc(GL_NOTEQUAL, 2, 255);
            return true;
        }
    }
}

