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

namespace SpaceVIL
{
    internal class DrawEngine : GL.WGL.OpenWGL
    {


        private ToolTip _tooltip = new ToolTip();
        private BaseItem _isStencilSet = null;
        public InputDeviceEvent EngineEvent = new InputDeviceEvent();

        private List<VisualItem> HoveredItems;
        private VisualItem HoveredItem;
        private VisualItem FocusedItem;
        private Pointer ptrPress = new Pointer();
        private Pointer ptrRelease = new Pointer();

        internal GLWHandler _handler;
        private Shader _primitive;
        private Shader _texture;

        public DrawEngine(WindowLayout handler)
        {
            HoveredItems = new List<VisualItem>();
            _handler = new GLWHandler(handler);

            _tooltip.SetHandler(handler);
            _tooltip.GetTextLine().SetHandler(handler);
            _tooltip.GetTextLine().SetParent(_tooltip);
            _tooltip.InitElements();
        }

        public void Dispose()
        {
            //полностью аннигилирует библиотеку GLFW, что приводит к закрытию всех окон и уничтожает все что использует библиотеку GLFW
            //должно вызываться только при закрытии приложения или если необходимо - уничтожении всех окон
            //статический метод Glfw.Terminate() является общим для всех экземпляров классов, что создают окна с помощью GLFW
            Glfw.Terminate();
        }
        public void Close()
        {
            _handler.SetToClose();
        }

        public void Init()
        {
            _handler.CreateWindow();
            SetWindowPos();
            SetEventsCallbacks();
            //устанавливаем параметры отрисовки
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_MULTISAMPLE);
            glEnable(GL_BLEND);
            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            //glEnable(GL_DEPTH_TEST);
            glEnable(GL_ALPHA_TEST);
            ////////////////////////////////////////////////
            _primitive = new Shader(
                Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Shaders.vs_fill.glsl"),
                Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Shaders.fs_fill.glsl"));
            _primitive.Compile();
            if (_primitive.GetProgramID() == 0)
                Console.WriteLine("Could not create primitive shaders");
            ///////////////////////////////////////////////
            _texture = new Shader(
                Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Shaders.vs_texture.glsl"),
                Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Shaders.fs_texture.glsl"));
            _texture.Compile();
            if (_texture.GetProgramID() == 0)
                Console.WriteLine("Could not create textured shaders");

            glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

            Run();
        }

        private void SetEventsCallbacks()
        {
            _handler.SetCallbackMouseMove(MouseMove);
            _handler.SetCallbackMouseClick(MouseClick);
            _handler.SetCallbackMouseScroll(MouseScroll);
            _handler.SetCallbackKeyPress(KeyPress);
            _handler.SetCallbackTextInput(TextInput);
            _handler.SetCallbackClose(Close);
            _handler.SetCallbackPosition(Position);
            _handler.SetCallbackFocus(Focus);
            _handler.SetCallbackResize(Resize);
        }

        private void MouseScroll(Glfw.Window glfwwnd, double xoffset, double yoffset)
        {
            _tooltip.InitTimer(false);
            BaseItem root = HoveredItem;
            while (root != null) //down event
            {
                if (root is IScrollable)
                    break;
                root = root.GetParent();
            }
            if (root != null)
            {
                if (yoffset > 0 || xoffset < 0)
                    (root as IScrollable).InvokeScrollUp();
                if (yoffset < 0 || xoffset > 0)
                    (root as IScrollable).InvokeScrollDown();
                EngineEvent.SetEvent(InputEventType.MouseScroll);
            }
        }

        private void KeyPress(Glfw.Window glfwwnd, KeyCode key, int scancode, InputState action, KeyMods mods)
        {
            if (!_handler.Focusable)
                return;

            _tooltip.InitTimer(false);
            if (FocusedItem is TextEdit && ((mods == KeyMods.Control && key == KeyCode.V) ||
                (mods == KeyMods.Shift && key == KeyCode.Insert)) && action == InputState.Press)
            {
                string paste_str = Glfw.GetClipboardString(_handler.GetWindow());
                (FocusedItem as TextEdit).PasteText(paste_str);
            }
            else if (FocusedItem is TextEdit && mods == KeyMods.Control && key == KeyCode.C && action == InputState.Press)
            {
                string copy_str = (FocusedItem as TextEdit).GetSelectedText();
                Glfw.SetClipboardString(_handler.GetWindow(), copy_str);
            }
            else if (FocusedItem is TextEdit && mods == KeyMods.Control && key == KeyCode.X && action == InputState.Press)
            {
                string cut_str = (FocusedItem as TextEdit).CutText();
                Glfw.SetClipboardString(_handler.GetWindow(), cut_str);
            }
            else
                FocusedItem?.InvokeKeyboardInputEvents(scancode, action, mods);
        }
        private void TextInput(Glfw.Window glfwwnd, uint codepoint, KeyMods mods)
        {
            if (!_handler.Focusable)
                return;

            _tooltip.InitTimer(false);
            FocusedItem?.InvokeInputTextEvents(codepoint, mods);
        }
        internal void Focus(Glfw.Window glfwwnd, bool value)
        {
            EngineEvent.ResetAllEvents();
            _tooltip.InitTimer(false);

            if (value)
            {
                if (_handler.Focusable)
                {
                    WindowLayoutBox.SetCurrentFocusedWindow(_handler.GetLayout());
                    _handler.Focused = value;
                }
            }
            else
            {
                if (_handler.GetLayout().IsDialog)
                {
                    _handler.Focused = true;
                }
                else
                {
                    _handler.Focused = value;
                }
            }
        }

        internal void SetWindowPos()
        {
            EngineEvent.SetEvent(InputEventType.WindowMove);
            _handler.GetLayout().SetX(_handler.WPosition.X);
            _handler.GetLayout().SetY(_handler.WPosition.Y);
            Glfw.SetWindowPos(_handler.GetWindow(), _handler.WPosition.X, _handler.WPosition.Y);
        }
        private void Position(Glfw.Window glfwwnd, int xpos, int ypos)
        {
            _handler.WPosition.X = xpos;
            _handler.WPosition.Y = ypos;
        }
        private void Close(Glfw.Window glfwwnd)
        {
            Glfw.SetWindowShouldClose(glfwwnd, true);
        }
        private void Resize(Glfw.Window glfwwnd, int width, int height)
        {
            _tooltip.InitTimer(false);
            _handler.GetLayout().SetWidth(width);
            _handler.GetLayout().SetHeight(height);
            if (!_handler.GetLayout().IsBorderHidden)
            {
                glViewport(0, 0, _handler.GetLayout().GetWidth(), _handler.GetLayout().GetHeight());
                Render();
            }
        }

        public void SetWindowSize()
        {
            EngineEvent.SetEvent(InputEventType.WindowResize);
            Glfw.SetWindowSize(_handler.GetWindow(), _handler.GetLayout().GetWidth(), _handler.GetLayout().GetHeight());
            if (_handler.GetLayout().IsBorderHidden)
            {
                glViewport(0, 0, _handler.GetLayout().GetWidth(), _handler.GetLayout().GetHeight());
                Render();
            }
        }

        public void MinimizeWindow()
        {
            EngineEvent.SetEvent(InputEventType.WindowMinimize);
            Glfw.IconifyWindow(_handler.GetWindow());
        }
        //OpenGL input interaction function
        private VisualItem IsInListHoveredItems<T>()
        {
            foreach (var item in HoveredItems)
            {
                //Console.WriteLine(item.GetItemName());
                if (item is T)
                {
                    return item;
                }
            }
            return null;
        }

        private bool GetHoverVisualItem(float xpos, float ypos)
        {
            HoveredItems.Clear();

            foreach (var item in ItemsLayoutBox.GetLayoutItems(_handler.GetLayout().Id))
            {
                if (item is VisualItem)
                {
                    if (!(item as VisualItem).IsVisible)
                        continue;

                    if ((item as VisualItem).GetHoverVerification(xpos, ypos))
                    {
                        HoveredItems.Add(item as VisualItem);
                    }
                }
            }

            if (HoveredItems.Count > 0)
            {
                HoveredItem = HoveredItems.Last();
                return true;
            }
            else
                return false;
        }
        protected void MouseMove(Glfw.Window wnd, double xpos, double ypos)
        {
            if (!_handler.Focusable)
                return;

            _tooltip.InitTimer(false);
            EngineEvent.SetEvent(InputEventType.MouseMove);
            //logic of hovers
            ptrRelease.X = (int)xpos;
            ptrRelease.Y = (int)ypos;

            if (EngineEvent.LastEvent().HasFlag(InputEventType.MousePressed)) // жость какая-то ХЕРОТАААА!!!
            {
                if (_handler.GetLayout().IsBorderHidden)
                {
                    int w = _handler.GetLayout().GetWidth();
                    int h = _handler.GetLayout().GetHeight();

                    if (_handler.GetLayout().GetWindow()._sides.HasFlag(ItemAlignment.Left))
                    {
                        if (!(_handler.GetLayout().GetMinWidth() == _handler.GetLayout().GetWidth() && (ptrRelease.X - ptrPress.X) >= 0))
                        {
                            _handler.WPosition.X += (ptrRelease.X - ptrPress.X);
                            w -= (ptrRelease.X - ptrPress.X);
                        }
                    }
                    if (_handler.GetLayout().GetWindow()._sides.HasFlag(ItemAlignment.Right))
                    {
                        if (!(ptrRelease.X < _handler.GetLayout().GetMinWidth() && _handler.GetLayout().GetWidth() == _handler.GetLayout().GetMinWidth()))
                        {
                            w += (ptrRelease.X - ptrPress.X);
                        }
                        ptrPress.X = ptrRelease.X;
                    }
                    if (_handler.GetLayout().GetWindow()._sides.HasFlag(ItemAlignment.Top))
                    {
                        if (!(_handler.GetLayout().GetMinHeight() == _handler.GetLayout().GetHeight() && (ptrRelease.Y - ptrPress.Y) >= 0))
                        {
                            _handler.WPosition.Y += (ptrRelease.Y - ptrPress.Y);
                            h -= (ptrRelease.Y - ptrPress.Y);
                        }
                    }
                    if (_handler.GetLayout().GetWindow()._sides.HasFlag(ItemAlignment.Bottom))
                    {
                        if (!(ptrRelease.Y < _handler.GetLayout().GetMinHeight() && _handler.GetLayout().GetHeight() == _handler.GetLayout().GetMinHeight()))
                        {
                            h += (ptrRelease.Y - ptrPress.Y);
                        }
                        ptrPress.Y = ptrRelease.Y;
                    }

                    if (_handler.GetLayout().GetWindow()._sides != 0)
                    {
                        _handler.GetLayout().SetWidth(w);
                        _handler.GetLayout().SetHeight(h);
                        if (_handler.GetLayout().GetWindow()._sides.HasFlag(ItemAlignment.Left) || _handler.GetLayout().GetWindow()._sides.HasFlag(ItemAlignment.Top))
                            SetWindowPos();
                        SetWindowSize();
                    }
                }
                if (_handler.GetLayout().GetWindow()._sides == 0)
                {
                    VisualItem draggable = IsInListHoveredItems<IDraggable>();
                    VisualItem anchor = IsInListHoveredItems<IWindowAnchor>();
                    if (draggable != null)
                    {
                        draggable._mouse_ptr.SetPosition((float)xpos, (float)ypos);
                        draggable.InvokePoolEvents();
                        //(HoveredItem as ScrollHandler).EventMouseDrag.Invoke(HoveredItem);

                        //Focus get
                        if (FocusedItem != null)
                            FocusedItem.IsFocused = false;

                        FocusedItem = HoveredItem;
                        FocusedItem.IsFocused = true;
                    }
                    else if (anchor != null && !(HoveredItem is ButtonCore))
                    {
                        if ((ptrRelease.X - ptrPress.X) != 0 || (ptrRelease.Y - ptrPress.Y) != 0)
                        {
                            _handler.WPosition.X += (ptrRelease.X - ptrPress.X);
                            _handler.WPosition.Y += (ptrRelease.Y - ptrPress.Y);
                            SetWindowPos();
                        }
                    }
                }
            }
            else
            {
                ptrPress.X = ptrRelease.X;
                ptrPress.Y = ptrRelease.Y;

                //check tooltip
                if (GetHoverVisualItem(ptrRelease.X, ptrRelease.Y))
                {
                    if (HoveredItem.GetToolTip() != String.Empty)
                    {
                        _tooltip.InitTimer(true);
                        _tooltip.SetText(HoveredItem.GetToolTip());
                    }
                    _handler.SetCursorType(Glfw.CursorType.Arrow);
                    if (_handler.GetLayout().IsBorderHidden)
                    {
                        //resize
                        if ((xpos < _handler.GetLayout().GetWindow().GetWidth() - 5)
                            && (xpos > 5)
                            && (ypos < _handler.GetLayout().GetWindow().GetHeight() - 5)
                            && ypos > 5)
                        {
                            if (HoveredItem is ITextEditable)
                                _handler.SetCursorType(Glfw.CursorType.Beam);
                        }
                        else //refactor!!
                        {
                            if ((xpos > _handler.GetLayout().GetWindow().GetWidth() - 5 && ypos < 5)
                             || (xpos > _handler.GetLayout().GetWindow().GetWidth() - 5 && ypos > _handler.GetLayout().GetWindow().GetHeight() - 5)
                             || (ypos > _handler.GetLayout().GetWindow().GetHeight() - 5 && xpos < 5)
                             || (ypos > _handler.GetLayout().GetWindow().GetHeight() - 5 && xpos > _handler.GetLayout().GetWindow().GetWidth() - 5)
                             || (xpos < 5 && ypos < 5))
                            {
                                _handler.SetCursorType(Glfw.CursorType.Crosshair);
                            }
                            else
                            {
                                if (xpos > _handler.GetLayout().GetWindow().GetWidth() - 5 || xpos < 5)
                                    _handler.SetCursorType(Glfw.CursorType.ResizeX);

                                if (ypos > _handler.GetLayout().GetWindow().GetHeight() - 5 || ypos < 5)
                                    _handler.SetCursorType(Glfw.CursorType.ResizeY);
                            }
                        }
                    }
                    else
                    {
                        if (HoveredItem is ITextEditable)
                        {
                            _handler.SetCursorType(Glfw.CursorType.Beam);
                        }
                    }
                    VisualItem popup = IsInListHoveredItems<IPopUp>();
                    if (popup != null)
                    {
                        (popup as PopUpMessage).HoldSelf(true);
                    }
                }
            }
        }

        protected void MouseClick(Glfw.Window window, MouseButton button, InputState state, KeyMods mods)
        {
            if (!_handler.Focusable)
                return;

            _tooltip.InitTimer(false);
            if (!GetHoverVisualItem(ptrRelease.X, ptrRelease.Y))
            {
                EngineEvent.ResetAllEvents();
                EngineEvent.SetEvent(InputEventType.MouseRelease);
                return;
            }
            _handler.GetLayout().GetWindow().GetSides(ptrRelease.X, ptrRelease.Y);
            switch (state)
            {
                case InputState.Release:
                    _handler.GetLayout().GetWindow()._sides = 0;
                    if (EngineEvent.LastEvent().HasFlag(InputEventType.WindowResize) || EngineEvent.LastEvent().HasFlag(InputEventType.WindowMove))
                    {
                        EngineEvent.ResetAllEvents();
                        EngineEvent.SetEvent(InputEventType.MouseRelease);
                        return;
                    }

                    if (HoveredItem != null)
                    {
                        foreach (var item in HoveredItems)
                        {
                            item._mouse_ptr.X = ptrRelease.X;
                            item._mouse_ptr.Y = ptrRelease.Y;
                            //item.EventMouseClick?.Invoke(HoveredItem);
                            _handler.GetLayout().SetEventTask(new EventTask()
                            {
                                Item = item,
                                Action = InputEventType.MouseRelease
                            });
                        }
                        _handler.GetLayout().ExecutePollActions();

                        //Focus get
                        if (FocusedItem != null)
                            FocusedItem.IsFocused = false;

                        FocusedItem = HoveredItem;
                        FocusedItem.IsFocused = true;
                    }
                    EngineEvent.ResetAllEvents();
                    EngineEvent.SetEvent(InputEventType.MouseRelease);
                    break;
                case InputState.Press:
                    if (HoveredItem is IWindow)
                    {
                        (HoveredItem as WContainer)._resizing = true;
                    }
                    EngineEvent.ResetAllEvents();
                    EngineEvent.SetEvent(InputEventType.MousePressed);
                    break;
                case InputState.Repeat:
                    break;
                default:
                    break;
            }
        }

        internal void Update()
        {
            Glfw.PostEmptyEvent();
            SetWindowSize();
        }
        public void Run()
        {
            glGenVertexArrays(1, _handler.GVAO);
            glBindVertexArray(_handler.GVAO[0]);

            _primitive.UseShader();
            Focus(_handler.GetWindow(), true);

            //starting animation
            float _opacity_anim = 0.0f;
            while (_opacity_anim < 1.0f)
            {
                _opacity_anim += 0.2f;
                _handler.SetOpacity(_opacity_anim);
                Render();
                Thread.Sleep(1000 / 90);
                Glfw.PollEvents();
            }
            _handler.SetOpacity(1.0f);


            //core rendering
            while (!_handler.IsClosing())
            {
                lock (CommonService.engine_locker)
                {
                    if (_handler.Focused)
                    {
                        Render();
                    }
                }

                if (EngineEvent.LastEvent().HasFlag(InputEventType.WindowResize))
                    Glfw.PollEvents();
                else
                    Glfw.WaitEvents();
            }

            _primitive.DeleteShader();
            _texture.DeleteShader();

            glDeleteVertexArrays(1, _handler.GVAO);

            _handler.ClearEventsCallbacks();
            _handler.Destroy();
        }

        internal void Render()
        {
            glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
            DrawItems(_handler.GetLayout().GetWindow());
            //draw tooltip if needed
            DrawToolTip();
            if (!_handler.Focusable)
            {
                Rectangle dark_fill = new Rectangle();
                dark_fill.SetSize(_handler.GetLayout().GetWidth(), _handler.GetLayout().GetHeight());
                dark_fill.SetBackground(0, 0, 0, 150);
                dark_fill.SetParent(_handler.GetLayout().GetWindow());
                dark_fill.SetHandler(_handler.GetLayout());
                DrawShell(dark_fill);
            }
            _handler.Swap();
        }
        private void DrawToolTip()//refactor
        {
            if (!_tooltip.IsVisible)
                return;

            _tooltip.GetTextLine().UpdateData(UpdateType.Critical);
            _tooltip.SetWidth(
                _tooltip.GetPadding().Left +
                _tooltip.GetPadding().Right +
                _tooltip.GetTextWidth()
                );

            //проверка сверху
            if (ptrRelease.Y > _tooltip.GetHeight())
            {
                _tooltip.SetY(ptrRelease.Y - _tooltip.GetHeight() - 2);
            }
            else
            {
                _tooltip.SetY(ptrRelease.Y + _tooltip.GetHeight() + 2);
            }
            //проверка справа
            if (ptrRelease.X - 10 + _tooltip.GetWidth() > _handler.GetLayout().GetWidth())
            {
                _tooltip.SetX(_handler.GetLayout().GetWidth() - _tooltip.GetWidth() - 10);
            }
            else
            {
                _tooltip.SetX(ptrRelease.X - 10);
            }
            DrawShell(_tooltip);
            glDisable(GL_MULTISAMPLE);
            _tooltip.GetTextLine().UpdateGeometry();
            DrawText(_tooltip.GetTextLine());
            glEnable(GL_MULTISAMPLE);
            if (_isStencilSet == _tooltip.GetTextLine())
            {
                glDisable(GL_STENCIL_TEST);
                _isStencilSet = null;
            }
        }
        //Common Draw function
        private void DrawItems(BaseItem root)
        {
            if (!root.IsVisible)
                return;

            //refactor paths
            if (root is IPixelDrawable)
            {
                DrawPixels((root as IPixelDrawable));
                foreach (var child in (root as VisualItem).GetItems())
                {
                    DrawItems(child);
                }
            }
            if (root is TextItem)
            {
                DrawText(root as TextItem);
                if (_isStencilSet == root)
                {
                    glDisable(GL_STENCIL_TEST);
                    _isStencilSet = null;
                }
            }
            if (root is IImageItem)
            {
                DrawShell(root);
                _texture.UseShader();
                DrawImage(root as ImageItem);
                _primitive.UseShader();
            }
            else
            {
                DrawShell(root);
                if (root is VisualItem)
                {
                    foreach (var child in (root as VisualItem).GetItems())
                    {
                        DrawItems(child);
                    }
                }
                if (_isStencilSet == root)
                {
                    glDisable(GL_STENCIL_TEST);
                    _isStencilSet = null;
                }
            }
        }
        private void SetStencilMask(int w, int h, int x, int y)
        {
            /*Console.WriteLine(
                w + " " +
                h + " " +
                x + " " +
                y
                );*/
            glEnable(GL_STENCIL_TEST);
            uint[] buffers = new uint[2];
            glGenBuffers(2, buffers);

            //Vertex
            List<float[]> crd_array;
            crd_array = GraphicsMathService.ToGL(GraphicsMathService.GetRectangle(w, h, x, y), _handler.GetLayout());

            float[] vertexData = new float[crd_array.Count * 3];

            for (int i = 0; i < vertexData.Length / 3; i++)
            {
                vertexData[i * 3 + 0] = crd_array.ElementAt(i)[0];
                vertexData[i * 3 + 1] = crd_array.ElementAt(i)[1];
                vertexData[i * 3 + 2] = crd_array.ElementAt(i)[2];
            }

            glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);
            glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, IntPtr.Zero);
            glEnableVertexAttribArray(0);

            //Color
            float[] argb = { 1.0f, 1.0f, 1.0f, 0.5f };
            float[] colorData = new float[crd_array.Count * 4];
            for (int i = 0; i < colorData.Length / 4; i++)
            {
                colorData[i * 4 + 0] = argb[0];
                colorData[i * 4 + 1] = argb[1];
                colorData[i * 4 + 2] = argb[2];
                colorData[i * 4 + 3] = argb[3];
            }
            glBindBuffer(GL_ARRAY_BUFFER, buffers[1]);
            glBufferData(GL_ARRAY_BUFFER, colorData, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, IntPtr.Zero);
            glEnableVertexAttribArray(1);

            // draw
            glDrawArrays(GL_TRIANGLES, 0, crd_array.Count);

            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);

            // Clear VBO and shader
            glDeleteBuffers(2, buffers);

            //clear array
            crd_array.Clear();
        }

        private bool CheckOutsideBorders(BaseItem shell)
        {
            var outside = new Dictionary<ItemAlignment, Int32[]>();

            if (shell.GetParent() != null && _isStencilSet == null)
            {
                //bottom
                if (shell.GetParent().GetY() + shell.GetParent().GetHeight() > shell.GetY()
                    && shell.GetParent().GetY() + shell.GetParent().GetHeight() < shell.GetY() + shell.GetHeight())
                {
                    //match
                    int y = shell.GetParent().GetY() + shell.GetParent().GetHeight() - shell.GetParent().GetPadding().Bottom;
                    int h = shell.GetHeight();
                    outside.Add(ItemAlignment.Bottom, new int[] { y, h });
                }
                //top
                if (shell.GetParent().GetY() + shell.GetParent().GetPadding().Top > shell.GetY())
                {
                    //match
                    int y = shell.GetY();
                    int h = shell.GetParent().GetY() + shell.GetParent().GetPadding().Top - shell.GetY();
                    outside.Add(ItemAlignment.Top, new int[] { y, h });
                }
                //right
                if (shell.GetParent().GetX() + shell.GetParent().GetWidth() - shell.GetParent().GetPadding().Right <
                    shell.GetX() + shell.GetWidth())
                {
                    //match
                    int x = shell.GetParent().GetX() + shell.GetParent().GetWidth() - shell.GetParent().GetPadding().Right;
                    int w = shell.GetWidth();
                    outside.Add(ItemAlignment.Right, new int[] { x, w });
                }
                //left
                if (shell.GetParent().GetX() + shell.GetParent().GetPadding().Left > shell.GetX())
                {
                    //match
                    int x = shell.GetX();
                    int w = shell.GetParent().GetX() + shell.GetParent().GetPadding().Left - shell.GetX();
                    outside.Add(ItemAlignment.Left, new int[] { x, w });
                }

                if (outside.Count > 0)
                {
                    _isStencilSet = shell;
                    //stencil
                    glClearStencil(1);
                    glStencilMask(0xFF);
                    glStencilFunc(GL_NEVER, 2, 0);
                    glStencilOp(GL_REPLACE, GL_KEEP, GL_KEEP);
                    foreach (var side in outside)
                    {
                        //draw mask
                        if (side.Key.HasFlag(ItemAlignment.Bottom) || side.Key.HasFlag(ItemAlignment.Top))
                            SetStencilMask(shell.GetWidth() + 2, side.Value[1], shell.GetX() - 1, side.Value[0]);
                        else
                            SetStencilMask(side.Value[1], shell.GetParent().GetHeight(), side.Value[0], shell.GetY());
                    }
                    //set stencil mask
                    glStencilFunc(GL_NOTEQUAL, 2, 255);
                    return true;
                }
            }
            return false;
        }
        private void DrawShell(BaseItem shell)
        {
            if (shell.GetBackground().A == 0)
                return;

            //проверка: полностью ли влезает объект в свой контейнер
            CheckOutsideBorders(shell);

            uint[] buffers = new uint[2];
            glGenBuffers(2, buffers);

            //Vertex
            List<float[]> crd_array;
            crd_array = shell.MakeShape();

            float[] vertexData = new float[crd_array.Count * 3];

            for (int i = 0; i < vertexData.Length / 3; i++)
            {
                vertexData[i * 3 + 0] = crd_array.ElementAt(i)[0];
                vertexData[i * 3 + 1] = crd_array.ElementAt(i)[1];
                vertexData[i * 3 + 2] = crd_array.ElementAt(i)[2];
            }

            glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);
            glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, IntPtr.Zero);
            glEnableVertexAttribArray(0);

            //Color
            float[] argb = {
                (float)shell.GetBackground().R / 255.0f,
                (float)shell.GetBackground().G / 255.0f,
                (float)shell.GetBackground().B / 255.0f,
                (float)shell.GetBackground().A / 255.0f};

            float[] colorData = new float[crd_array.Count * 4];
            for (int i = 0; i < colorData.Length / 4; i++)
            {
                colorData[i * 4 + 0] = argb[0];
                colorData[i * 4 + 1] = argb[1];
                colorData[i * 4 + 2] = argb[2];
                colorData[i * 4 + 3] = argb[3];
            }
            glBindBuffer(GL_ARRAY_BUFFER, buffers[1]);
            glBufferData(GL_ARRAY_BUFFER, colorData, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, IntPtr.Zero);
            glEnableVertexAttribArray(1);

            // draw
            glDrawArrays(GL_TRIANGLES, 0, crd_array.Count);

            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);

            // Clear VBO and shader
            glDeleteBuffers(2, buffers);

            //clear array
            crd_array.Clear();
        }

        void DrawText(TextItem item)
        {
            glDisable(GL_MULTISAMPLE);

            uint[] buffers = new uint[2];
            glGenBuffers(2, buffers);
            float[] data = item.Shape();
            float[] colorData = item.GetColors();

            bool ok = CheckOutsideBorders(item as BaseItem);

            glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);
            glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, IntPtr.Zero);
            glEnableVertexAttribArray(0);

            glBindBuffer(GL_ARRAY_BUFFER, buffers[1]);
            glBufferData(GL_ARRAY_BUFFER, colorData, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, IntPtr.Zero);
            glEnableVertexAttribArray(1);

            // draw
            glDrawArrays(GL_POINTS, 0, data.Length / 3);

            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);

            // Clear VBO and shader
            glDeleteBuffers(2, buffers);

            glEnable(GL_MULTISAMPLE);
        }

        void DrawPixels(IPixelDrawable item)
        {
            glDisable(GL_MULTISAMPLE);

            //Console.WriteLine(item.GetItemText());
            uint[] buffers = new uint[2];
            glGenBuffers(2, buffers);
            float[] data = item.GetCoords();
            float[] colorData = item.GetColors();

            glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);
            glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, IntPtr.Zero);
            glEnableVertexAttribArray(0);

            glBindBuffer(GL_ARRAY_BUFFER, buffers[1]);
            glBufferData(GL_ARRAY_BUFFER, colorData, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, IntPtr.Zero);
            glEnableVertexAttribArray(1);

            // draw
            glDrawArrays(GL_POINTS, 0, data.Length / 3);

            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);

            // Clear VBO and shader
            glDeleteBuffers(2, buffers);

            glEnable(GL_MULTISAMPLE);
        }

        void DrawImage(ImageItem image)
        {
            byte[] bitmap = image.GetPixMapImage();

            if (bitmap == null)
                return;

            //проверка: полностью ли влезает объект в свой контейнер
            CheckOutsideBorders(image as BaseItem);

            float i_x0 = ((float)image.GetX() / (float)_handler.GetLayout().GetWidth() * 2.0f) - 1.0f;
            float i_y0 = ((float)image.GetY() / (float)_handler.GetLayout().GetHeight() * 2.0f - 1.0f) * (-1.0f);
            float i_x1 = (((float)image.GetX() + (float)image.GetWidth()) / (float)_handler.GetLayout().GetWidth() * 2.0f) - 1.0f;
            float i_y1 = (((float)image.GetY() + (float)image.GetHeight()) / (float)_handler.GetLayout().GetHeight() * 2.0f - 1.0f) * (-1.0f);

            //VBO
            float[] vertexData = new float[]
            {
                //X    Y      Z         //U     V
                i_x0,  i_y0,  0.0f,     0.0f, 0.0f, //x0
                i_x0,  i_y1,  0.0f,     1.0f, 0.0f, //x1
                i_x1,  i_y1,  0.0f,     1.0f, 1.0f, //x2
                i_x1,  i_y0,  0.0f,     0.0f, 1.0f, //x3
            };
            int[] ibo = new int[]
            {
                0, 1, 2, //first triangle
                2, 3, 0, // second triangle
            };

            uint[] buffers = new uint[2];
            glGenBuffers(2, buffers);

            glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);
            glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffers[1]);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, ibo, GL_STATIC_DRAW);

            //Position attribute
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * sizeof(float), IntPtr.Zero);
            glEnableVertexAttribArray(0);
            //TexCoord attribute
            glVertexAttribPointer(1, 2, GL_FLOAT, true, 5 * sizeof(float), IntPtr.Zero + (3 * sizeof(float)));
            glEnableVertexAttribArray(1);

            //texture
            int w = image.GetImageWidth(), h = image.GetImageHeight();

            uint[] texture = new uint[1];
            glGenTextures(1, texture);

            glBindTexture(GL_TEXTURE_2D, texture[0]);

            glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, h, w);
            glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, h, w, GL_RGBA, GL_UNSIGNED_BYTE, bitmap);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

            //glGenerateMipmap(GL_TEXTURE_2D);
            glActiveTexture(GL_TEXTURE0);

            int location = glGetUniformLocation(_texture.GetProgramID(), "tex".ToCharArray());
            if (location >= 0)
                glUniform1i(location, 0);

            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, IntPtr.Zero);

            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);

            glDeleteBuffers(2, buffers);
            glDeleteTextures(1, texture);
        }
    }
}
