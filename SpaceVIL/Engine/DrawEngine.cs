using System;
using System.Collections.Generic;

using Glfw3;
using System.Diagnostics;
using System.Drawing;
using SpaceVIL.Core;
using Position = SpaceVIL.Core.Position;
using SpaceVIL.Common;
using static OpenGL.OpenGLConstants;
using static OpenGL.OpenGLWrapper;

namespace SpaceVIL
{
    internal sealed class DrawEngine
    {
        internal void RestoreCommonGLSettings()
        {
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_BLEND);
            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
            glEnable(GL_ALPHA_TEST);
            glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_DST_ALPHA);
            glEnable(GL_DEPTH_TEST);
        }

        internal void RestoreView()
        {
            glViewport(0, 0, _framebufferWidth, _framebufferHeight);
        }

        internal void SetGLLayerViewport(IOpenGLLayer layer)
        {
            IBaseItem oglLayer = layer as IBaseItem;
            if (oglLayer == null)
                return;
            SetViewPort(oglLayer);
        }

        private void SetViewPort(IBaseItem item)
        {
            int x = item.GetX();
            int y = _commonProcessor.Window.GetHeight() - (item.GetY() + item.GetHeight());
            int w = item.GetWidth();
            int h = item.GetHeight();
            x = (int)((float)x * _scale.GetXScale());
            y = (int)((float)y * _scale.GetYScale());
            w = (int)((float)w * _scale.GetXScale());
            h = (int)((float)h * _scale.GetYScale());
            glViewport(x, y, w, h);
        }

        internal void FreeVRAMResource<T>(T resource)
        {

            if (_renderProcessor == null)
                return;
            _renderProcessor.FreeResource(resource);
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

        internal bool FullScreenRequest = false;
        internal bool MaximizeRequest = false;
        internal bool MinimizeRequest = false;
        internal bool UpdateSizeRequest = false;
        internal bool UpdatePositionRequest = false;
        internal bool FocusRequest = false;

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
        private Shader _shapeShader;
        private Shader _textureShader;
        private Shader _textShader;
        private Shader _blurShader;

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

            if (GLWHandler.Maximized)
            {
                _commonProcessor.Window.IsMaximized = false;
                _commonProcessor.Window.Maximize();
            }

            DrawScene();

            return true;
        }

        private bool InitWindow()
        {
            try
            {
                GLWHandler.CreateWindow();
                _scale.SetScale(GLWHandler.GetCoreWindow().GetDpiScale().GetXScale(),
                    GLWHandler.GetCoreWindow().GetDpiScale().GetYScale());
                SetEventsCallbacks();
                if (WindowManager.GetVSyncValue() != 1)
                    Glfw.SwapInterval(WindowManager.GetVSyncValue());
                return true;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                GLWHandler.ClearEventsCallbacks();
                if (GLWHandler.GetWindowId() == 0)
                    GLWHandler.Destroy();
                GLWHandler.GetCoreWindow().Close();
                return false;
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
            _shapeShader = ShaderFactory.GetShader(ShaderFactory.Primitive);
            _textureShader = ShaderFactory.GetShader(ShaderFactory.Texture);
            _textShader = ShaderFactory.GetShader(ShaderFactory.Symbol);
            _blurShader = ShaderFactory.GetShader(ShaderFactory.Blur);
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
            GLWHandler.SetCallbackFocus(Focus);
            GLWHandler.SetCallbackResize(Resize);
            GLWHandler.SetCallbackPosition(Position);
            GLWHandler.SetCallbackFramebuffer(Framebuffer);
            GLWHandler.SetCallbackRefresh(Refresh);
            GLWHandler.SetCallbackDrop(Drop);
            GLWHandler.SetCallbackContentScale(ContentScale);
            GLWHandler.SetCallbackIconify(Iconify);
        }

        private void Iconify(long window, bool value)
        {
            _commonProcessor.Window.SetMinimized(value);
        }

        private void ContentScale(Int64 wnd, float x, float y)
        {
            _commonProcessor.Window.SetWindowScale(x, y);
            _scale.SetScale(x, y);
            DisplayService.SetDisplayScale(x, y);

            int widthWnd, heightWnd;
            Glfw.GetWindowSize(wnd, out widthWnd, out heightWnd);

            GLWHandler.GetCoreWindow().SetWidthDirect((int)(widthWnd / _scale.GetXScale()));
            GLWHandler.GetCoreWindow().SetHeightDirect((int)(heightWnd / _scale.GetYScale()));

            // подписать на обновление при смене фактора масштабирования 
            // (текст в фиксированных по ширине элементов не обновляется - оно и понятно)
        }

        private void Drop(Int64 wnd, int count, string[] paths)
        {
            _commonProcessor.WndProcessor.Drop(wnd, count, paths);
        }

        private void Refresh(Int64 wnd)
        {
            Update();
            GLWHandler.Swap();
        }

        private void Framebuffer(Int64 wnd, int w, int h)
        {
            _framebufferWidth = w;
            _framebufferHeight = h;
            WindowManager.SetContextCurrent(_commonProcessor.Window);
            glViewport(0, 0, _framebufferWidth, _framebufferHeight);

            _renderProcessor.ScreenSquare.Clear();
            _renderProcessor.ScreenSquare.GenBuffers(
                RenderProcessor.GetFullWindowRectangle(_framebufferWidth, _framebufferHeight));
            _renderProcessor.ScreenSquare.Unbind();
        }

        internal void UpdateWindowSize()
        {
            if (CommonService.GetOSType() == OSType.Mac)
            {
                _commonProcessor.WndProcessor.SetWindowSize(_commonProcessor.Window.GetWidth(),
                        _commonProcessor.Window.GetHeight(), new Scale());
            }
            else
            {
                _commonProcessor.WndProcessor.SetWindowSize(_commonProcessor.Window.GetWidth(),
                        _commonProcessor.Window.GetHeight(), _scale);
            }
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
            _commonProcessor.WndProcessor.MaximizeWindow(_scale);
        }

        internal void FullScreen()
        {
            _commonProcessor.WndProcessor.FullScreenWindow();
        }

        private void CloseWindow(Int64 wnd)
        {
            Glfw.SetWindowShouldClose(GLWHandler.GetWindowId(), false);
            _commonProcessor.Window.EventClose.Invoke();
        }

        internal void Focus(Int64 wnd, bool value)
        {
            _commonProcessor.WndProcessor.Focus(wnd, value);
        }

        internal void FocusWindow()
        {
            Glfw.FocusWindow(GLWHandler.GetWindowId());
        }

        private void Resize(Int64 wnd, int width, int height)
        {
            _tooltip.InitTimer(false);

            if (CommonService.GetOSType() != OSType.Mac)
            {
                GLWHandler.GetCoreWindow().SetWidthDirect((int)(width / _scale.GetXScale()));
                GLWHandler.GetCoreWindow().SetHeightDirect((int)(height / _scale.GetYScale()));
            }
            else
            {
                _commonProcessor.Window.SetWidthDirect(width);
                _commonProcessor.Window.SetHeightDirect(height);
            }

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
                // Render();
                // GLWHandler.Swap();
            }
        }

        private void Position(Int64 wnd, int xpos, int ypos)
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
            if (CommonService.GetOSType() != OSType.Mac)
            {
                _mouseMoveProcessor.Process(wnd, xpos / _scale.GetXScale(), ypos / _scale.GetYScale(), _scale);
            }
            else
            {
                _mouseMoveProcessor.Process(wnd, xpos, ypos, new Scale());
            }
        }

        private void MouseClick(Int64 wnd, MouseButton button, InputState state, KeyMods mods)
        {
            if (_commonProcessor.InputLocker || !GLWHandler.Focusable)
                return;
            _tooltip.InitTimer(false);

            _commonProcessor.RootContainer.ClearSides();
            _mouseClickProcessor.Process(wnd, button, state, mods);
        }

        private void MouseScroll(Int64 wnd, double dx, double dy)
        {
            // if (_commonProcessor.InputLocker)
            //     return;
            _tooltip.InitTimer(false);
            _mouseScrollProcessor.Process(wnd, dx, dy);
            _commonProcessor.Events.SetEvent(InputEventType.MouseScroll);
        }

        private KeyCode TryGetKeyByScancode(int scancode)
        {
            foreach (KeyCode key in Enum.GetValues(typeof(KeyCode)))
            {
                int code = Glfw.GetKeyScancode(key);
                System.Console.WriteLine("{ " + code + ", KeyCode." + key + " },");
            }
            return KeyCode.Unknown;
        }

        private void KeyPress(Int64 wnd, KeyCode key, int scancode, InputState action, KeyMods mods)
        {
            // if (action == InputState.Release) {
            //     TryGetKeyByScancode(scancode);
            // }

            if (_commonProcessor.InputLocker || !GLWHandler.Focusable)
                return;
            _tooltip.InitTimer(false);
            if (key == KeyCode.Unknown)
            {
                key = DefaultsService.GetKeyCodeByScancode(scancode);
            }
            _keyInputProcessor.Process(wnd, key, scancode, action, mods);
        }

        private void TextInput(Int64 wnd, uint character, KeyMods mods)
        {
            if (_commonProcessor.InputLocker || !GLWHandler.Focusable)
                return;
            _tooltip.InitTimer(false);
            _textInputProcessor.Process(wnd, character, mods);
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
            if (FocusRequest)
            {
                FocusWindow();
                FocusRequest = false;
            }
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

        IList<IOpenGLLayer> oglLine = new List<IOpenGLLayer>();
        internal void FreeOnClose()
        {
            if (_stop != null)
            {
                _stop.Stop();
                _stop = null;
            }
            _shapeShader.DeleteShader();
            _textureShader.DeleteShader();
            _textShader.DeleteShader();
            _blurShader.DeleteShader();
            _fboVertex.Clear();
            _fboBlur.Clear();
            _renderProcessor.ClearResources();
            glDeleteVertexArrays(1, GLWHandler.GVAO);

            while (!(oglLine.Count == 0))
            {
                oglLine[0].Free();
                oglLine.RemoveAt(0);
            }

            GLWHandler.ClearEventsCallbacks();
            GLWHandler.Destroy();
        }

        private System.Timers.Timer _stop = null;
        private bool _updateFramerate = false;
        private int _framerate = 0;
        private int _framerateRecord = 0;
        private double _frametime = 0;
        private double _frametimeRecord = 0;
        private Label _benchmarkLabel = null;
        private bool _isBenchmarkEnabled = false;

        internal void Render()
        {
            if (RenderService.GetMonitoringIndicators() != 0)
            {
                _isBenchmarkEnabled = true;
                if (_benchmarkLabel == null)
                {
                    _benchmarkLabel = new Label();
                    _benchmarkLabel.SetHandler(_commonProcessor.Window);
                    _benchmarkLabel.SetBackground(0, 0, 0, 100);
                    _benchmarkLabel.SetForeground(255, 255, 255);
                    _benchmarkLabel.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
                    _benchmarkLabel.SetPosition(20, 20);
                    _benchmarkLabel.SetPadding(10, 0, 10, 0);
                    _benchmarkLabel.InitElements();
                }
            }
            else if (_isBenchmarkEnabled)
            {
                _isBenchmarkEnabled = false;
            }

            if (_isBenchmarkEnabled)
            {
                _frametime = NanoTime() / 1000000.0;
                if (_stop == null)
                {
                    _stop = new System.Timers.Timer(1000);
                    _stop.Elapsed += (sender, e) =>
                    {
                        _updateFramerate = true;
                        _framerateRecord = _framerate;
                        _framerate = 0;
                        _stop.Stop();
                        _stop = null;
                    };
                    _stop.Start();
                }
            }

            glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            DrawStaticItems();
            DrawFloatItems();
            DrawToolTip();
            DrawShadePillow();

            if (_isBenchmarkEnabled)
            {
                _frametimeRecord = NanoTime() / 1000000.0 - _frametime;
                _framerate++;
                DrawBenchmark();
            }
        }

        private void DrawBenchmark()
        {
            if (_updateFramerate)
            {

                BenchmarkIndicator indicators = RenderService.GetMonitoringIndicators();
                String indicatorsString = "";
                int size = 0;
                foreach (BenchmarkIndicator indicator in Enum.GetValues(typeof(BenchmarkIndicator)))
                {
                    if (indicators.HasFlag(indicator))
                    {
                        size++;
                    }
                }
                int height = size * 24;
                if (indicators.HasFlag(BenchmarkIndicator.Framerate))
                {
                    indicatorsString = "Framerate: " + _framerateRecord;
                }
                if (size > 1)
                {
                    indicatorsString += "\n";
                }
                if (indicators.HasFlag(BenchmarkIndicator.Frametime))
                {
                    indicatorsString += "Frametime: " + _frametimeRecord.ToString("0.00") + " ms";
                }
                _benchmarkLabel.SetText(indicatorsString);
                int width = 150;
                if (width < _benchmarkLabel.GetTextWidth())
                {
                    width = _benchmarkLabel.GetTextWidth() + _benchmarkLabel.GetPadding().Right + _benchmarkLabel.GetPadding().Left;
                }
                _benchmarkLabel.SetSize(width, height);
                _updateFramerate = false;
            }

            _benchmarkLabel.MakeShape();

            _renderProcessor.DrawDirectVertex(_shapeShader, _benchmarkLabel.GetTriangles(), GetItemPyramidLevel(),
                    _benchmarkLabel.GetX(), _benchmarkLabel.GetY(), _commonProcessor.Window.GetWidth(),
                    _commonProcessor.Window.GetHeight(), _benchmarkLabel.GetBackground(), GL_TRIANGLES);

            glDisable(GL_SCISSOR_TEST);
            DrawItems(_benchmarkLabel);
            glDisable(GL_SCISSOR_TEST);
        }

        private long NanoTime()
        {
            long nano = 10000L * Stopwatch.GetTimestamp();
            nano /= TimeSpan.TicksPerMillisecond;
            nano *= 100L;
            return nano;
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
            return _stencilProcessor.Process(shell, _scale);
        }

        private void DrawItems(IBaseItem root) // Переписать
        {
            if (!root.IsVisible() || !root.IsDrawable())
                return;

            ILines linesRoot = root as ILines;
            IPoints pointsRoot = root as IPoints;
            ITextContainer textRoot = root as ITextContainer;
            IImageItem imageRoot = root as IImageItem;
            IOpenGLLayer openGLLayerRoot = root as IOpenGLLayer;

            // unique items without content meaning
            if (linesRoot != null)
            {
                DrawLines(linesRoot);
                return;
            }
            if (pointsRoot != null)
            {
                DrawPoints(pointsRoot);
                return;
            }
            if (imageRoot != null) // протестировать на вложенные элементы
            {
                DrawShell(root);
                glDisable(GL_SCISSOR_TEST);
                DrawImage(imageRoot);
                glDisable(GL_SCISSOR_TEST);
                DrawCommonContent(root);
                return;
            }

            // unique items with posible content
            if (textRoot != null)
            {
                DrawText(textRoot);
                glDisable(GL_SCISSOR_TEST);
            }
            if (openGLLayerRoot != null)
            {
                if (!oglLine.Contains(openGLLayerRoot))
                {
                    oglLine.Add(openGLLayerRoot);
                }
                DrawShell(root);
                glDisable(GL_SCISSOR_TEST);

                DrawOpenGLLayer(openGLLayerRoot);

                glClear(GL_DEPTH_BUFFER_BIT);
                glDisable(GL_DEPTH_TEST);

                DrawCommonContent(root);

                glEnable(GL_DEPTH_TEST);
                return;
            }

            // common item
            DrawShell(root);
            glDisable(GL_SCISSOR_TEST);
            DrawCommonContent(root);
        }

        private void DrawCommonContent(IBaseItem root)
        {
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

        private void DrawOpenGLLayer(IOpenGLLayer ogllRoot)
        {
            if (!ogllRoot.IsInitialized())
                ogllRoot.Initialize();

            IBaseItem oglItem = ogllRoot as IBaseItem;
            if (oglItem == null)
                return;

            bool stencil = CheckOutsideBorders(oglItem);

            SetViewPort(oglItem);

            ogllRoot.Draw();
            RestoreView();
            glDisable(GL_SCISSOR_TEST);
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

            var shadows = shell.Effects().Get(EffectType.Shadow);

            bool preEffect = DrawPreprocessingEffects(shell);
            if (ItemsRefreshManager.IsRefreshShape(shell))
            {
                shell.MakeShape();
                foreach (var shadow in shadows)
                {
                    DrawShadow(shell, shadow as IShadow, stencil);
                }

                ItemsRefreshManager.RemoveShape(shell);

                _renderProcessor.DrawFreshVertex(
                    _shapeShader, shell, GetItemPyramidLevel(), shell.GetX(), shell.GetY(),
                    _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(),
                    shell.GetBackground(), GL_TRIANGLES);
            }
            else
            {
                foreach (var shadow in shadows)
                {
                    DrawShadow(shell, shadow as IShadow, stencil);
                }
                _renderProcessor.DrawStoredVertex(
                    _shapeShader, shell, GetItemPyramidLevel(), shell.GetX(), shell.GetY(),
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
                    _shapeShader, vertex, GetItemPyramidLevel(), vi.GetX(), vi.GetY(),
                    _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(),
                    vi.GetBorderFill(), GL_TRIANGLES);
            }
        }

        float[] _weights;

        void DrawShadow(IBaseItem shell, IShadow shadow, bool stencil)
        {
            if (!shadow.IsApplied())
            {
                return;
            }

            Core.Size extension = shadow.GetExtension();
            int xAddidion = extension.GetWidth() / 2;
            int yAddidion = extension.GetHeight() / 2;
            int res = shadow.GetRadius();

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

            int fboWidth = shell.GetWidth() + extension.GetWidth() + 2 * res;
            int fboHeight = shell.GetHeight() + extension.GetHeight() + 2 * res;
            if (ItemsRefreshManager.IsRefreshShape(shell) || _renderProcessor.ShadowStorage.GetResources(shell) == null)
            {
                if (stencil)
                    glDisable(GL_SCISSOR_TEST);

                glViewport(0, 0, fboWidth, fboHeight);
                _fboVertex.GenFBO();
                _fboVertex.GenFboTexture(fboWidth, fboHeight);
                glClear(GL_COLOR_BUFFER_BIT);

                List<float[]> vertex = BaseItemStatics.UpdateShape(
                    shell.GetTriangles(), shell.GetWidth() + extension.GetWidth(),
                    shell.GetHeight() + extension.GetHeight());

                _renderProcessor.DrawDirectVertex(_shapeShader, vertex, 0.0f, res, res,
                    fboWidth, fboHeight, shadow.GetColor(), GL_TRIANGLES);

                _fboVertex.UnbindTexture();
                _fboVertex.Unbind();
                _fboBlur.Bind();
                _fboBlur.GenFboTexture(fboWidth, fboHeight);

                glClearColor(
                    shadow.GetColor().R / 255.0f,
                    shadow.GetColor().G / 255.0f,
                    shadow.GetColor().B / 255.0f,
                    0.0f);

                glClear(GL_COLOR_BUFFER_BIT);

                VramTexture store = _renderProcessor.DrawDirectShadow(
                    _blurShader, 0.0f, _weights, res, _fboVertex.Texture, 0, 0,
                    shell.GetWidth() + extension.GetWidth(), shell.GetHeight() + extension.GetHeight(),
                    fboWidth, fboHeight);
                store.Clear();

                _fboBlur.UnbindTexture();
                _fboBlur.Unbind();

                if (stencil)
                    glEnable(GL_SCISSOR_TEST);
                glViewport(0, 0, _framebufferWidth, _framebufferHeight);

                _renderProcessor.DrawFreshShadow(_textureShader, shell, shadow, GetItemPyramidLevel(), _fboBlur.Texture,
                    shell.GetX() + shadow.GetOffset().GetX() - xAddidion - res,
                    shell.GetY() + shadow.GetOffset().GetY() - yAddidion - res,
                    fboWidth, fboHeight, _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight());

                _fboVertex.Clear();
                _fboBlur.Clear();

                glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            }
            else
            {
                _renderProcessor.DrawStoredShadow(_textureShader, shell, shadow, GetItemPyramidLevel(),
                    shell.GetX() + shadow.GetOffset().GetX() - xAddidion - res,
                    shell.GetY() + shadow.GetOffset().GetY() - yAddidion - res,
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
            ITextImage textImage = text.GetTexture();
            if (textImage == null)
                return;

            if (textImage.IsEmpty())
                return;

            CheckOutsideBorders(text as IBaseItem);

            float[] argb = {
                (float) text.GetForeground().R / 255.0f,
                (float) text.GetForeground().G / 255.0f,
                (float) text.GetForeground().B / 255.0f,
                (float) text.GetForeground().A / 255.0f };

            if (ItemsRefreshManager.IsRefreshText(text))
            {
                ItemsRefreshManager.RemoveText(text);
                _renderProcessor.DrawFreshText(_textShader, text, textImage,
                    _scale,
                    _commonProcessor.Window.GetWidth(),
                    _commonProcessor.Window.GetHeight(),
                    GetItemPyramidLevel(), argb);
            }
            else
            {
                _renderProcessor.DrawStoredText(_textShader, text, textImage,
                    _commonProcessor.Window.GetWidth(),
                    _commonProcessor.Window.GetHeight(),
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

            if (ItemsRefreshManager.IsRefreshShape(shell))
            {
                ItemsRefreshManager.RemoveShape(shell);
                shell.MakeShape();

                List<float[]> points = item.GetPoints();
                if (points == null)
                    return;

                List<float[]> shape = item.GetPointShape();
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
                    _shapeShader, shell, result, level, shell.GetX(), shell.GetY(),
                    _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(),
                    item.GetPointColor(), GL_TRIANGLES);
            }
            else
            {
                _renderProcessor.DrawStoredVertex(
                    _shapeShader, shell, level, shell.GetX(), shell.GetY(),
                    _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(),
                    item.GetPointColor(), GL_TRIANGLES);
            }
        }

        void DrawLines(ILines item)
        {
            if (item.GetLineColor().A == 0)
                return;

            IBaseItem shell = item as IBaseItem;
            if (shell == null)
                return;

            CheckOutsideBorders(shell);

            if (ItemsRefreshManager.IsRefreshShape(shell))
            {
                ItemsRefreshManager.RemoveShape(shell);
                shell.MakeShape();

                _renderProcessor.DrawFreshVertex(
                    _shapeShader, shell, GetItemPyramidLevel(), item.GetX(), item.GetY(),
                    _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(),
                    item.GetLineColor(), GL_LINE_STRIP);
            }
            else
            {
                _renderProcessor.DrawStoredVertex(
                    _shapeShader, shell, GetItemPyramidLevel(), item.GetX(), item.GetY(),
                    _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(),
                    item.GetLineColor(), GL_LINE_STRIP);
            }
        }

        void DrawImage(IImageItem image)
        {
            CheckOutsideBorders(image as IBaseItem);

            int w = image.GetImageWidth(), h = image.GetImageHeight();
            Area area = image.GetAreaBounds();

            if (ItemsRefreshManager.IsRefreshImage(image))
            {
                _renderProcessor.DrawFreshTexture(
                    image, _textureShader,
                    area.GetX(), area.GetY(), area.GetWidth(), area.GetHeight(),
                    w, h,
                    _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(),
                    GetItemPyramidLevel());
            }
            else
            {
                _renderProcessor.DrawStoredTexture(
                    image, _textureShader, area.GetX(), area.GetY(),
                    _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(),
                    GetItemPyramidLevel());
            }
        }

        private Position _tooltipBorderIndent = new Position(10, 2);

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
                _tooltip.SetY(_commonProcessor.PtrRelease.GetY() - _tooltip.GetHeight() - _tooltipBorderIndent.GetY());
            else
                _tooltip.SetY(_commonProcessor.PtrRelease.GetY() + CommonService.CurrentCursor.GetCursorHeight() + _tooltipBorderIndent.GetY());
            //проверка справа
            if (_commonProcessor.PtrRelease.GetX() - _tooltipBorderIndent.GetX() + _tooltip.GetWidth()
                > GLWHandler.GetCoreWindow().GetWidth() - _tooltipBorderIndent.GetX())
                _tooltip.SetX(GLWHandler.GetCoreWindow().GetWidth() - _tooltip.GetWidth() - _tooltipBorderIndent.GetX());
            else
                _tooltip.SetX(_commonProcessor.PtrRelease.GetX() - _tooltipBorderIndent.GetX());

            _tooltip.MakeShape();

            var shadows = _tooltip.Effects().Get(EffectType.Shadow);

            foreach (var shadow in shadows)
            {
                DrawToolTipShadow(shadow as IShadow);
            }

            _renderProcessor.DrawDirectVertex(
                _shapeShader, _tooltip.GetTriangles(), GetItemPyramidLevel(), _tooltip.GetX(), _tooltip.GetY(),
                _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(), _tooltip.GetBackground(),
                GL_TRIANGLES);

            DrawBorder(_tooltip);

            glDisable(GL_SCISSOR_TEST);

            DrawItems(_tooltip);

            glDisable(GL_SCISSOR_TEST);
        }

        private void DrawToolTipShadow(IShadow shadow)
        {
            if (!shadow.IsApplied())
            {
                return;
            }
            Core.Size extension = shadow.GetExtension();
            int xAddidion = extension.GetWidth() / 2;
            int yAddidion = extension.GetHeight() / 2;
            int res = shadow.GetRadius();

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

            int fboWidth = _tooltip.GetWidth() + extension.GetWidth() + 2 * res;
            int fboHeight = _tooltip.GetHeight() + extension.GetHeight() + 2 * res;

            glViewport(0, 0, fboWidth, fboHeight);
            _fboVertex.GenFBO();
            _fboVertex.GenFboTexture(fboWidth, fboHeight);
            glClear(GL_COLOR_BUFFER_BIT);

            List<float[]> vertex = BaseItemStatics.UpdateShape(
                _tooltip.GetTriangles(), _tooltip.GetWidth() + extension.GetWidth(),
                _tooltip.GetHeight() + extension.GetHeight());

            _renderProcessor.DrawDirectVertex(_shapeShader, vertex, 0.0f, res, res,
                fboWidth, fboHeight, shadow.GetColor(), GL_TRIANGLES);

            _fboVertex.UnbindTexture();
            _fboVertex.Unbind();
            _fboBlur.Bind();
            _fboBlur.GenFboTexture(fboWidth, fboHeight);
            glClear(GL_COLOR_BUFFER_BIT);

            VramTexture store = _renderProcessor.DrawDirectShadow(
                _blurShader, 0.0f, _weights, res, _fboVertex.Texture, 0, 0,
                _tooltip.GetWidth() + extension.GetWidth(), _tooltip.GetHeight() + extension.GetHeight(),
                fboWidth, fboHeight);
            store.Clear();

            _fboBlur.UnbindTexture();
            _fboBlur.Unbind();

            glViewport(0, 0, _framebufferWidth, _framebufferHeight);

            _renderProcessor.DrawRawShadow(_textureShader, GetItemPyramidLevel(), _fboBlur.Texture,
                _tooltip.GetX() + shadow.GetOffset().GetX() - xAddidion - res,
                _tooltip.GetY() + shadow.GetOffset().GetY() - yAddidion - res,
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
                _shapeShader, GetItemPyramidLevel(), 0, 0,
                _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(),
                GLWHandler.GetCoreWindow().GetShadeColor(), GL_TRIANGLES);
        }

        private bool DrawPreprocessingEffects(IBaseItem item)
        {
            List<IEffect> effects = item.Effects().Get(EffectType.Subtract);
            if (effects.Count == 0)
            {
                return false;
            }

            glEnable(GL_STENCIL_TEST);
            glClear(GL_STENCIL_BUFFER_BIT);
            glClearStencil(1);
            glStencilMask(0xFF);
            glStencilFunc(GL_NEVER, 2, 0);
            glStencilOp(GL_REPLACE, GL_KEEP, GL_KEEP);
            foreach (IEffect effect in effects)
            {
                if (!effect.IsApplied())
                {
                    continue;
                }
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
                                        (int)(item.GetHeight() * subtractFigure.GetHeightScale())
                                        //, new Area(0, 0, item.GetWidth(), item.GetHeight()), subtractFigure.GetAlignment()
                                        ),
                                subtractFigure.GetXOffset(), subtractFigure.GetYOffset());
                    }

                    _renderProcessor.DrawDirectVertex(_shapeShader, vertex, 0, item.GetX(), item.GetY(),
                            _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight(), Color.White,
                            GL_TRIANGLES);

                }
            }
            glStencilFunc(GL_NOTEQUAL, 2, 255);
            return true;
        }
    }
}
