// #define LINUX 

using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.IO;
using System.Text;

using Glfw3;
using System.Threading;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL.Core;
using Pointer = SpaceVIL.Core.Pointer;
using SpaceVIL.Common;
using System.Diagnostics;

#if MAC
using static GL.LGL.OpenLGL;
#elif WINDOWS
using static GL.WGL.OpenWGL;
#elif LINUX
using static GL.LGL.OpenLGL;
#else
using static GL.WGL.OpenWGL;
#endif

namespace SpaceVIL
{
    internal sealed class DrawEngine
    {
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
            _itemPyramidLevel -= 0.001f;
        }

        private ToolTip _tooltip = new ToolTip();

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

        internal void SetIcons(Image ibig, Image ismall)
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
            _tooltip.GetTextLine().SetHandler(handler);
            _tooltip.GetTextLine().SetParent(_tooltip);
            _tooltip.InitElements();
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
            return true;
        }

        private bool InitWindow()
        {
            Monitor.Enter(CommonService.GlobalLocker);
            try
            {
                GLWHandler.CreateWindow();
                SetEventsCallbacks();
                return true;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                GLWHandler.ClearEventsCallbacks();
                if (GLWHandler.GetWindowId())
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

        private void Drop(Glfw.Window window, int count, string[] paths)
        {
            _commonProcessor.WndProcessor.Drop(window, count, paths);
        }

        private void Refresh(Glfw.Window window)
        {
            Update();
            GLWHandler.Swap();
        }

        void Framebuffer(Glfw.Window window, int w, int h)
        {
            _framebufferWidth = w;
            _framebufferHeight = h;
            Glfw.MakeContextCurrent(_commonProcessor.Window.GetGLWID());
            glViewport(0, 0, _framebufferWidth, _framebufferHeight);
            _fbo.BindFBO();
            _fbo.ClearTexture();
            _fbo.GenFBOTexture(_framebufferWidth, _framebufferHeight);
            _fbo.UnbindFBO();
        }

        internal void MinimizeWindow()
        {
            _commonProcessor.WndProcessor.MinimizeWindow();
        }

        internal bool MaximizeRequest = false;

        internal void MaximizeWindow()
        {
            _commonProcessor.WndProcessor.MaximizeWindow();
        }

        private void CloseWindow(Glfw.Window window)
        {
            Glfw.SetWindowShouldClose(GLWHandler.GetWindowId(), false);
            _commonProcessor.Window.EventClose.Invoke();
        }

        internal void Focus(Glfw.Window window, bool value)
        {
            _commonProcessor.WndProcessor.Focus(window, value);
        }

        internal void SetWindowFocused()
        {
            Glfw.FocusWindow(GLWHandler.GetWindowId());
        }

        private void Resize(Glfw.Window window, int width, int height)
        {
            _tooltip.InitTimer(false);
            GLWHandler.GetCoreWindow().SetWidth(width);
            GLWHandler.GetCoreWindow().SetHeight(height);
        }

        internal void SetWindowSize(int width, int height)
        {
            _commonProcessor.WndProcessor.SetWindowSize(width, height);
        }

        private void Position(Glfw.Window window, int xpos, int ypos)
        {
            GLWHandler.GetPointer().SetX(xpos);
            GLWHandler.GetPointer().SetY(ypos);
            _commonProcessor.Window.SetX(xpos);
            _commonProcessor.Window.SetY(ypos);
        }

        internal void SetWindowPos(int x, int y)
        {
            _commonProcessor.WndProcessor.SetWindowPos(x, y);
        }

        private bool flag_move = false;

        private void MouseMove(Glfw.Window wnd, double xpos, double ypos)
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

        private void MouseClick(Glfw.Window window, MouseButton button, InputState state, KeyMods mods)
        {
            if (_commonProcessor.InputLocker || !GLWHandler.Focusable)
                return;
            _tooltip.InitTimer(false);
            _commonProcessor.RootContainer.ClearSides();
            _mouseClickProcessor.Process(window, button, state, mods);
        }

        private void MouseScroll(Glfw.Window window, double dx, double dy)
        {
            if (_commonProcessor.InputLocker)
                return;
            _tooltip.InitTimer(false);
            _mouseScrollProcessor.Process(window, dx, dy);
            _commonProcessor.Events.SetEvent(InputEventType.MouseScroll);
        }

        private void KeyPress(Glfw.Window window, KeyCode key, int scancode, InputState action, KeyMods mods)
        {
            if (_commonProcessor.InputLocker || !GLWHandler.Focusable)
                return;
            _tooltip.InitTimer(false);
            _keyInputProcessor.Process(window, key, scancode, action, mods);
        }

        private void TextInput(Glfw.Window window, uint character, KeyMods mods)
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

        VRAMFramebuffer _fbo = new VRAMFramebuffer();

        internal void DrawScene()
        {
            if (MaximizeRequest)
            {
                MaximizeWindow();
                MaximizeRequest = false;
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
            VRAMStorage.Flush();
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
            _fbo.GenFBO();
            _fbo.GenFBOTexture(_framebufferWidth, _framebufferHeight);
            _fbo.UnbindFBO();
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        }

        internal void FreeOnClose()
        {
            _primitive.DeleteShader();
            _texture.DeleteShader();
            _char.DeleteShader();
            _blur.DeleteShader();
            _fbo.ClearFBO();
            VRAMStorage.Clear();
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
            List<IBaseItem> float_items = new List<IBaseItem>(ItemsLayoutBox.GetLayout(_commonProcessor.Window.GetWindowGuid()).FloatItems);
            if (float_items != null)
                foreach (var item in float_items)
                    DrawItems(item);
        }

        private bool CheckOutsideBorders(IBaseItem shell)
        {
            return _stencilProcessor.Process(shell);
        }

        private void DrawItems(IBaseItem root)
        {
            if (root == null || !root.IsVisible() || !root.IsDrawable())
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

                if (root is Prototype)
                {
                    List<IBaseItem> list = ((Prototype)root).GetItems();
                    foreach (var child in list)
                    {
                        DrawItems(child);
                    }
                }
            }
        }

        private void DrawShell(IBaseItem shell, bool ignore_borders = false)
        {
            if (!ignore_borders)
                CheckOutsideBorders(shell);

            if (shell.GetBackground().A == 0)
            {
                Prototype pr = shell as Prototype;
                if (pr != null)
                    DrawBorder(pr);
                return;
            }

            List<float[]> crdArray = shell.MakeShape();
            if (crdArray == null)
                return;

            if (shell.IsShadowDrop())
                DrawShadow(shell);

            _renderProcessor.DrawVertex(_primitive, crdArray, GetItemPyramidLevel(), shell.GetBackground(), GL_TRIANGLES); ;
            crdArray.Clear();

            Prototype vi = shell as Prototype;
            if (vi != null)
                DrawBorder(vi);
        }

        void DrawBorder(Prototype vi)
        {
            if (vi.GetBorderThickness() > 0)
            {
                CustomShape border = new CustomShape();
                border.SetBackground(vi.GetBorderFill());
                border.SetSize(vi.GetWidth(), vi.GetHeight());
                border.SetPosition(vi.GetX(), vi.GetY());
                border.SetParent(vi);
                border.SetHandler(vi.GetHandler());
                border.SetTriangles(GraphicsMathService.GetRoundSquareBorder(
                    vi.GetBorderRadius(),
                    vi.GetWidth(),
                    vi.GetHeight(),
                    vi.GetBorderThickness(),
                    vi.GetX(),
                    vi.GetY()
                    ));
                DrawShell(border, true);
            }
        }

        void DrawShadow(IBaseItem shell)
        {
            int[] extension = shell.GetShadowExtension();
            int xAddidion = extension[0] / 2;
            int yAddidion = extension[1] / 2;

            CustomShape shadow = new CustomShape();
            shadow.SetBackground(shell.GetShadowColor());
            shadow.SetSize(shell.GetWidth() + extension[0], shell.GetHeight() + extension[1]);
            shadow.SetPosition(shell.GetX() + shell.GetShadowPos().GetX() - xAddidion,
                shell.GetY() + shell.GetShadowPos().GetY() - yAddidion);
            shadow.SetParent(shell.GetParent());
            shadow.SetHandler(shell.GetHandler());
            shadow.SetTriangles(shell.GetTriangles());

            _fbo.BindFBO();
            glClear(GL_COLOR_BUFFER_BIT);
            DrawShell(shadow, true);

            int res = shell.GetShadowRadius();
            float[] weights = new float[11];
            float sum, sigma2 = 4.0f;
            weights[0] = Gauss(0, sigma2);
            sum = weights[0];
            for (int i = 1; i < 11; i++)
            {
                weights[i] = Gauss(i, sigma2);
                sum += 2 * weights[i];
            }
            for (int i = 0; i < 11; i++)
                weights[i] /= sum;

            _fbo.UnbindFBO();
            DrawShadowPart(weights, res, _fbo.Texture,
            new float[] { shell.GetX() + shell.GetShadowPos().GetX(), shell.GetY() + shell.GetShadowPos().GetY() },
                new float[] { shell.GetWidth(), shell.GetHeight() });
        }

        float Gauss(float x, float sigma)
        {
            double ans;
            ans = Math.Exp(-(x * x) / (2f * sigma * sigma)) / Math.Sqrt(2 * Math.PI * sigma * sigma);
            return (float)ans;
        }

        private void DrawShadowPart(float[] weights, int res, uint[] fboTexture, float[] xy, float[] wh)
        {
            _renderProcessor.DrawShadow(_blur, GetItemPyramidLevel(), weights, res, fboTexture, xy, wh,
                _commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight());
        }


        void DrawText(ITextContainer text)
        {
            TextPrinter textPrt = text.GetLetTextures();
            if (textPrt == null)
                return;
            byte[] byteBuffer = textPrt.Texture;
            if (byteBuffer == null || byteBuffer.Length == 0)
                return;

            CheckOutsideBorders(text as IBaseItem);

            int byteBufferHeight = textPrt.HeightTexture, byteBufferWidth = textPrt.WidthTexture;
            float x0 = ((float)textPrt.XTextureShift / (float)GLWHandler.GetCoreWindow().GetWidth() * 2.0f) - 1.0f;
            float y0 = ((float)textPrt.YTextureShift / (float)GLWHandler.GetCoreWindow().GetHeight() * 2.0f - 1.0f) * (-1.0f);
            float x1 = (((float)textPrt.XTextureShift + (float)byteBufferWidth / GLWHandler.GetCoreWindow().GetDpiScale()[0])
                    / (float)GLWHandler.GetCoreWindow().GetWidth() * 2.0f) - 1.0f;
            float y1 = (((float)textPrt.YTextureShift + (float)byteBufferHeight / GLWHandler.GetCoreWindow().GetDpiScale()[0])
                    / (float)GLWHandler.GetCoreWindow().GetHeight() * 2.0f - 1.0f) * (-1.0f);
            float[] argb = {
                (float) text.GetForeground().R / 255.0f,
                (float) text.GetForeground().G / 255.0f,
                (float) text.GetForeground().B / 255.0f,
                (float) text.GetForeground().A / 255.0f };

            _renderProcessor.DrawText(_char, x0, x1, y0, y1, byteBufferWidth, byteBufferHeight, byteBuffer,
                GetItemPyramidLevel(), argb);
        }

        void DrawPoints(IPoints item)
        {
            if (item.GetPointColor().A == 0)
                return;

            List<float[]> crd_array = item.MakeShape();
            if (crd_array == null)
                return;
            CheckOutsideBorders(item as IBaseItem);
            List<float[]> point = item.GetShapePointer();
            float centerOffset = item.GetPointThickness() / 2.0f;
            float[] result = new float[point.Count * crd_array.Count * 3];
            int skew = 0;
            float level = GetItemPyramidLevel();
            foreach (float[] shape in crd_array)
            {
                List<float[]> fig = GraphicsMathService.ToGL(GraphicsMathService.MoveShape(point, shape[0] - centerOffset, shape[1] - centerOffset),
                    GLWHandler.GetCoreWindow());
                for (int i = 0; i < fig.Count; i++)
                {
                    result[skew + i * 3 + 0] = fig[i][0];
                    result[skew + i * 3 + 1] = fig[i][1];
                    result[skew + i * 3 + 2] = level;
                }
                skew += fig.Count * 3;
            }
            _renderProcessor.DrawVertex(_primitive, result, item.GetPointColor(), GL_TRIANGLES);

            ////////////////////////////////////////
            // _clone.UseShader();
            // List<float[]> point = GraphicsMathService.ToGL(item.GetShapePointer().Distinct().ToList(), GLWHandler.GetLayout());//item.GetShapePointer().Distinct().ToList(); //
            // float[] result = new float[point.Count * 2];
            // for (int i = 0; i < result.Length / 2; i++)
            // {
            //     result[i * 2 + 0] = point.ElementAt(i)[0];
            //     result[i * 2 + 1] = point.ElementAt(i)[1];
            //     // Console.WriteLine(result[i * 2 + 0] + " " + result[i * 2 + 1]);
            // }

            // VRAMVertex store = new VRAMVertex();
            // store.SendColor(_clone, item.GetPointColor());
            // store.GenBuffers(GraphicsMathService.ToGL(crd_array, GLWHandler.GetLayout()));
            // store.SendUniform1i(_clone, "size", point.Count);
            // store.SendUniform2fv(_clone, "shape", result);
            // store.Draw(GL_POINTS);
            // store.Clear();
            // crd_array.Clear();
            // _primitive.UseShader();
            ////////////////////////////////////////
        }

        void DrawLines(ILine item)
        {
            if (item.GetLineColor().A == 0)
                return;

            List<float[]> crd_array = GraphicsMathService.ToGL(item.MakeShape(), GLWHandler.GetCoreWindow());
            if (crd_array == null)
                return;
            CheckOutsideBorders(item as IBaseItem);
            _renderProcessor.DrawVertex(_primitive, crd_array, GetItemPyramidLevel(), item.GetLineColor(), GL_LINE_STRIP);
        }

        void DrawImage(IImageItem image)
        {
            CheckOutsideBorders(image as IBaseItem);

            byte[] bitmap = image.GetPixMapImage();
            if (bitmap == null)
                return;

            int w = image.GetImageWidth(), h = image.GetImageHeight();
            RectangleBounds Area = image.GetRectangleBounds();

            float x0 = ((float)Area.GetX() / (float)GLWHandler.GetCoreWindow().GetWidth() * 2.0f) - 1.0f;
            float y0 = ((float)Area.GetY() / (float)GLWHandler.GetCoreWindow().GetHeight() * 2.0f - 1.0f) * (-1.0f);
            float x1 = (((float)Area.GetX() + (float)Area.GetWidth()) / (float)GLWHandler.GetCoreWindow().GetWidth() * 2.0f) - 1.0f;
            float y1 = (((float)Area.GetY() + (float)Area.GetHeight()) / (float)GLWHandler.GetCoreWindow().GetHeight() * 2.0f - 1.0f) * (-1.0f);

            ImageItem tmp = image as ImageItem;
            if (tmp != null)
            {
                if (tmp.IsNew())
                {
                    _renderProcessor.DrawFreshTexture(tmp, _texture, x0, x1, y0, y1, w, h, bitmap, GetItemPyramidLevel());
                }
                else
                {
                    _renderProcessor.DrawStoredTexture(tmp, _texture, x0, x1, y0, y1, GetItemPyramidLevel());
                }
                return;
            }

            if (image.IsColorOverlay())
                _renderProcessor.DrawTextureWithColorOverlay(_texture, x0, x1, y0, y1, w, h, bitmap, GetItemPyramidLevel(),
                        image.GetRotationAngle(), image.GetColorOverlay());
            else
                _renderProcessor.DrawTextureAsIs(_texture, x0, x1, y0, y1, w, h, bitmap, GetItemPyramidLevel(),
                        image.GetRotationAngle());
        }

        private Pointer tooltipBorderIndent = new Pointer(10, 2);

        private void DrawToolTip()
        {
            if (!_tooltip.IsVisible())
                return;

            _tooltip.SetText(_commonProcessor.HoveredItem.GetToolTip());
            _tooltip.SetWidth(_tooltip.GetPadding().Left + _tooltip.GetPadding().Right + _tooltip.GetTextWidth());

            //проверка сверху
            if (_commonProcessor.PtrRelease.GetY() > _tooltip.GetHeight())
                _tooltip.SetY(_commonProcessor.PtrRelease.GetY() - _tooltip.GetHeight() - tooltipBorderIndent.GetY());
            else
                _tooltip.SetY(_commonProcessor.PtrRelease.GetY() + _tooltip.GetHeight() + tooltipBorderIndent.GetY());
            //проверка справа
            if (_commonProcessor.PtrRelease.GetX() - tooltipBorderIndent.GetX() + _tooltip.GetWidth() > GLWHandler.GetCoreWindow().GetWidth())
                _tooltip.SetX(GLWHandler.GetCoreWindow().GetWidth() - _tooltip.GetWidth() - tooltipBorderIndent.GetX());
            else
                _tooltip.SetX(_commonProcessor.PtrRelease.GetX() - tooltipBorderIndent.GetX());

            DrawShell(_tooltip);
            glDisable(GL_SCISSOR_TEST);
            _tooltip.GetTextLine().UpdateGeometry();
            DrawText(_tooltip.GetTextLine());
            glDisable(GL_SCISSOR_TEST);
        }

        private void DrawShadePillow()
        {
            if (GLWHandler.Focusable)
                return;

            List<float[]> vertex = new List<float[]>(RenderProcessor.GetFullWindowRectangle());
            _renderProcessor.DrawVertex(_primitive, vertex, GetItemPyramidLevel(), Color.FromArgb(200, 0, 0, 0), GL_TRIANGLES);
        }
    }
}

