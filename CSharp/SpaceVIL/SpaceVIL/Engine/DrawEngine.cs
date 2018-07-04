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
    //where TLayout : VisualItem
    {
        //cursors 
        Glfw.Cursor _arrow;
        Glfw.Cursor _input;
        Glfw.Cursor _hand;
        Glfw.Cursor _resize_h;
        Glfw.Cursor _resize_v;
        Glfw.Cursor _resize_all;

        private ToolTip _tooltip = new ToolTip();
        private BaseItem _isStencilSet = null;
        public InputDeviceEvent EngineEvent = new InputDeviceEvent();
        public readonly object engine_locker = new object();

        ///////////////////////////////////////////////
        Glfw.WindowSizeFunc resizeCallback;
        Glfw.CursorPosFunc mouseMoveCallback;
        Glfw.MouseButtonFunc mouseClickCallback;
        Glfw.ScrollFunc mouseScrollCallback;
        Glfw.WindowCloseFunc windowCloseCallback;
        Glfw.WindowPosFunc windowPosCallback;
        Glfw.KeyFunc keyPressCallback;
        Glfw.CharModsFunc keyInputText;
        Glfw.WindowFocusFunc windowFocusCallback;
        ///////////////////////////////////////////////

        public bool borderHidden;
        public bool appearInCenter;
        public bool focusable;
        public bool focused = true;
        public bool alwaysOnTop;
        public Pointer window_position = new Pointer();
        private VisualItem HoveredItem;
        private VisualItem FocusedItem;
        private Pointer ptrPress = new Pointer();
        private Pointer ptrRelease = new Pointer();

        public WindowLayout wnd_handler;

        Glfw.Window window;
        private uint[] gVAO = new uint[1];
        private uint ProgramPrimitives;
        private uint VertexPrimitiveShader, FragmentPrimitiveShader;

        private uint ProgramTexture;
        private uint VertexTextureShader, FragmentTextureShader;

        public DrawEngine(WindowLayout handler)
        {
            wnd_handler = handler;
            window_position.X = 0;
            window_position.Y = 0;

            _tooltip.SetHandler(wnd_handler);
            _tooltip.GetTextLine().SetHandler(wnd_handler);
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

        public void Init()
        {
            CreateWindow(wnd_handler.GetWindowTitle(), 4, wnd_handler.GetWidth(), wnd_handler.GetHeight());
            SetEventsCallbacks();
            //cursors
            _arrow = Glfw.CreateStandardCursor(Glfw.CursorType.Arrow);
            _input = Glfw.CreateStandardCursor(Glfw.CursorType.Beam);
            _hand = Glfw.CreateStandardCursor(Glfw.CursorType.Hand);
            _resize_h = Glfw.CreateStandardCursor(Glfw.CursorType.ResizeX);
            _resize_v = Glfw.CreateStandardCursor(Glfw.CursorType.ResizeY);
            _resize_all = Glfw.CreateStandardCursor(Glfw.CursorType.Crosshair);

            glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

            //устанавливаем параметры отрисовки
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_MULTISAMPLE);
            glEnable(GL_BLEND);
            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glDisable(GL_DEPTH_TEST);
            glEnable(GL_ALPHA_TEST);

            ProgramPrimitives = CreateShaderProgram(
            Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Shaders.vs_fill.glsl"),
            Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Shaders.fs_fill.glsl"),
            ref VertexPrimitiveShader,
            ref FragmentPrimitiveShader
            );
            if (ProgramPrimitives == 0)
                Console.WriteLine("Could not create primitive shaders");

            ProgramTexture = CreateShaderProgram(
            Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Shaders.vs_texture.glsl"),
            Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Shaders.fs_texture.glsl"),
            ref VertexTextureShader,
            ref FragmentTextureShader
            );
            if (ProgramTexture == 0)
                Console.WriteLine("Could not create textured shaders");
            Run();
        }

        private void CreateWindow(string title, int msaa, int w, int h)
        {
            //path to c++ glfw3.dll (x32 or x64)
            //Glfw.ConfigureNativesDirectory(AppDomain.CurrentDomain.BaseDirectory);
            if (!Glfw.Init())
            {
                Environment.Exit(-1);
            }

            //Glfw.WindowHint(Glfw.Hint.OpenglDebugContext, true);
            Glfw.WindowHint(Glfw.Hint.Samples, 4);
            Glfw.WindowHint(Glfw.Hint.OpenglForwardCompat, true);
            Glfw.WindowHint(Glfw.Hint.ContextVersionMajor, 4);
            Glfw.WindowHint(Glfw.Hint.ContextVersionMinor, 3);
            Glfw.WindowHint(Glfw.Hint.Resizable, true);
            Glfw.WindowHint(Glfw.Hint.Decorated, !borderHidden);//make borderless window
            Glfw.WindowHint(Glfw.Hint.Focused, focusable);
            Glfw.WindowHint(Glfw.Hint.Floating, alwaysOnTop);

            window = Glfw.CreateWindow(w, h, title);
            if (!window)
            {
                Glfw.Terminate();
                Environment.Exit(-1);
            }

            if (appearInCenter)
            {
                window_position.X = (Glfw.GetVideoMode(Glfw.GetPrimaryMonitor()).Width - w) / 2;
                window_position.Y = (Glfw.GetVideoMode(Glfw.GetPrimaryMonitor()).Height - h) / 2;
                MoveWindowPos();
            }
            else
            {
                MoveWindowPos();
            }

            Glfw.MakeContextCurrent(window);
            Glfw.SetWindowSizeLimits(window, wnd_handler.GetMinWidth(), wnd_handler.GetMinHeight(), wnd_handler.GetMaxWidth(), wnd_handler.GetMaxHeight());
        }

        private uint CreateShaderProgram(Stream vertex_shader, Stream fragment_shader, ref uint vertex, ref uint fragment)
        {
            StringBuilder v_code = new StringBuilder();
            StringBuilder f_code = new StringBuilder();
            try
            {
                using (StreamReader sr = new StreamReader(vertex_shader))
                {
                    while (!sr.EndOfStream)
                        v_code.Append(sr.ReadLine() + "\n");
                }

                using (StreamReader sr = new StreamReader(fragment_shader))
                {
                    while (!sr.EndOfStream)
                        f_code.Append(sr.ReadLine() + "\n");
                }
            }
            catch (Exception e)
            {
                Console.WriteLine("The file could not be read:");
                Console.WriteLine(e.Message);
            }
            //Console.WriteLine(v_code);
            //Console.WriteLine(f_code);
            vertex = glCreateShader(GL_VERTEX_SHADER);
            glShaderSource(vertex, 1, new[] { v_code.ToString() }, new[] { v_code.ToString().Length });
            glCompileShader(vertex);

            fragment = glCreateShader(GL_FRAGMENT_SHADER);
            glShaderSource(fragment, 1, new[] { f_code.ToString() }, new[] { f_code.ToString().Length });
            glCompileShader(fragment);

            uint shader = glCreateProgram();
            glAttachShader(shader, vertex);
            glAttachShader(shader, fragment);
            glLinkProgram(shader);

            return shader;
        }

        private void SetEventsCallbacks()
        {
            mouseMoveCallback = MouseMove;
            Glfw.SetCursorPosCallback(window, mouseMoveCallback);
            mouseClickCallback = MouseClick;
            Glfw.SetMouseButtonCallback(window, mouseClickCallback);
            resizeCallback = Resize;
            Glfw.SetWindowSizeCallback(window, resizeCallback);
            windowCloseCallback = Close;
            Glfw.SetWindowCloseCallback(window, windowCloseCallback);
            windowPosCallback = Position;
            Glfw.SetWindowPosCallback(window, windowPosCallback);
            mouseScrollCallback = MouseScroll;
            Glfw.SetScrollCallback(window, mouseScrollCallback);
            windowFocusCallback = Focus;
            Glfw.SetWindowFocusCallback(window, windowFocusCallback);
            keyPressCallback = KeyPress;
            Glfw.SetKeyCallback(window, keyPressCallback);
            keyInputText = TextInput;
            Glfw.SetCharModsCallback(window, keyInputText);
        }
        private void ClearEventsCallbacks()
        {
            mouseMoveCallback -= MouseMove;
            mouseClickCallback -= MouseClick;
            resizeCallback -= Resize;
            windowCloseCallback -= Close;
            windowPosCallback -= Position;
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
            _tooltip.InitTimer(false);
            if (FocusedItem is TextEdit && mods == KeyMods.Control && key == KeyCode.V)
            {
                string paste_str = Glfw.GetClipboardString(window);
                (FocusedItem as TextEdit).SetText(paste_str);
            }
            else
                FocusedItem?.InvokeKeyboardInputEvents(scancode, action, mods);
        }
        private void TextInput(Glfw.Window glfwwnd, uint codepoint, KeyMods mods)
        {
            _tooltip.InitTimer(false);
            FocusedItem?.InvokeInputTextEvents(codepoint, mods);
        }
        private void Focus(Glfw.Window glfwwnd, bool value)
        {
            _tooltip.InitTimer(false);
            focused = value;
        }

        internal void MoveWindowPos()
        {
            Glfw.SetWindowPos(window, window_position.X, window_position.Y);
        }
        private void Position(Glfw.Window glfwwnd, int xpos, int ypos)
        {
            window_position.X = xpos;
            window_position.Y = ypos;
        }
        private void Close(Glfw.Window glfwwnd)
        {
            Glfw.SetWindowShouldClose(glfwwnd, true);
        }
        private void Resize(Glfw.Window glfwwnd, int width, int height)
        {
            _tooltip.InitTimer(false);
            wnd_handler.SetWidth(width);
            wnd_handler.SetHeight(height);
            if (!wnd_handler.IsBorderHidden)
            {
                glViewport(0, 0, wnd_handler.GetWidth(), wnd_handler.GetHeight());
                Render();
            }
        }

        public void SetWindowSize()
        {
            if (wnd_handler.IsBorderHidden)
            {
                Glfw.SetWindowSize(window, wnd_handler.GetWidth(), wnd_handler.GetHeight());
                glViewport(0, 0, wnd_handler.GetWidth(), wnd_handler.GetHeight());
                Render();
            }
        }

        public void MinimizeWindow()
        {
            Glfw.IconifyWindow(window);
        }
        //OpenGL input interaction function
        private VisualItem GetHoverVisualItem(float xpos, float ypos)
        {
            int index = -1;

            foreach (var item in ItemsLayoutBox.GetLayoutItems(wnd_handler.Id))
            {
                if (item is VisualItem)
                {
                    if (!(item as VisualItem).IsVisible)
                        continue;

                    if ((item as VisualItem).GetHoverVerification(xpos, ypos))
                    {
                        index = ItemsLayoutBox.GetLayoutItems(wnd_handler.Id).ToList().IndexOf(item);
                    }
                }
            }

            if (index != -1)
                return (VisualItem)ItemsLayoutBox.GetLayoutItems(wnd_handler.Id).ElementAt(index);
            else
                return null;
        }
        protected void MouseMove(Glfw.Window wnd, double xpos, double ypos)
        {
            _tooltip.InitTimer(false);
            //logic of hovers
            ptrRelease.X = (int)xpos;
            ptrRelease.Y = (int)ypos;

            if (EngineEvent.LastEvent() == InputEventType.MousePressed && HoveredItem is IDraggable)
            {
                HoveredItem._mouse_ptr.SetPosition((float)xpos, (float)ypos);
                HoveredItem.InvokePoolEvents();
                //(HoveredItem as ScrollHandler).EventMouseDrag.Invoke(HoveredItem);

                //Focus get
                if (FocusedItem != null)
                    FocusedItem.IsFocused = false;

                FocusedItem = HoveredItem;
                FocusedItem.IsFocused = true;
            }
            else if (EngineEvent.LastEvent() == InputEventType.MousePressed && HoveredItem is IWindowAnchor)
            {
                window_position.X += (ptrRelease.X - ptrPress.X);
                window_position.Y += (ptrRelease.Y - ptrPress.Y);
                MoveWindowPos();
            }
            else if (EngineEvent.LastEvent() == InputEventType.MousePressed && HoveredItem is IWindow)//for refactor
            {
                if (wnd_handler.IsBorderHidden)
                {
                    int w = wnd_handler.GetWidth();
                    int h = wnd_handler.GetHeight();

                    ItemAlignment sides = (HoveredItem as IWindow).GetSides((float)xpos, (float)ypos);

                    if (sides.HasFlag(ItemAlignment.Right))
                    {
                        w += (ptrRelease.X - ptrPress.X);
                    }
                    if (sides.HasFlag(ItemAlignment.Bottom))
                    {
                        h += (ptrRelease.Y - ptrPress.Y);
                    }

                    Resize(window, w, h);
                    SetWindowSize();

                    ptrPress.X = ptrRelease.X;
                    ptrPress.Y = ptrRelease.Y;
                }
            }
            else
            {
                HoveredItem = GetHoverVisualItem(ptrRelease.X, ptrRelease.Y);
                ptrPress.X = ptrRelease.X;
                ptrPress.Y = ptrRelease.Y;

                //check tooltip
                if (HoveredItem != null)
                {
                    if (HoveredItem.GetToolTip() != String.Empty)
                    {
                        _tooltip.InitTimer(true);
                        _tooltip.SetText(HoveredItem.GetToolTip());
                    }

                    if (HoveredItem is ITextEditable)
                    {
                        Glfw.SetCursor(window, _input);
                    }
                    else if (HoveredItem is IWindow)//for refactor
                    {
                        if (!wnd_handler.IsBorderHidden)
                            return;
                        if (xpos > HoveredItem.GetWidth() - 5 && ypos > HoveredItem.GetHeight() - 5)
                            Glfw.SetCursor(window, _resize_all);
                        else
                        {
                            if (xpos > HoveredItem.GetWidth() - 5)
                                Glfw.SetCursor(window, _resize_h);

                            if (ypos > HoveredItem.GetHeight() - 5)
                                Glfw.SetCursor(window, _resize_v);

                            if ((xpos < HoveredItem.GetWidth() - 5) && (ypos < HoveredItem.GetHeight() - 5))
                                Glfw.SetCursor(window, _arrow);
                        }
                    }
                    else
                    {
                        Glfw.SetCursor(window, _arrow);
                    }
                }

                EngineEvent.SetEvent(InputEventType.MouseMove);
            }
        }

        protected void MouseClick(Glfw.Window window, MouseButton button, InputState state, KeyMods mods)
        {
            _tooltip.InitTimer(false);
            switch (state)
            {
                case InputState.Release:
                    if (HoveredItem != null)
                    {
                        Console.WriteLine(HoveredItem.GetItemName());
                        if (HoveredItem is IWindow)
                        {
                            (HoveredItem as WContainer)._sides = 0;
                            (HoveredItem as WContainer)._resizing = false;
                        }
                        HoveredItem.EventMouseClick.Invoke(HoveredItem);
                        //HoveredItem.InvokePoolEvents();

                        //Focus get
                        if (FocusedItem != null)
                            FocusedItem.IsFocused = false;

                        FocusedItem = HoveredItem;
                        FocusedItem.IsFocused = true;
                    }
                    EngineEvent.SetEvent(InputEventType.MouseRelease);
                    break;
                case InputState.Press:
                    if (HoveredItem is IWindow)
                    {
                        (HoveredItem as WContainer)._resizing = true;
                    }
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
        }
        public void Run()
        {
            glGenVertexArrays(1, gVAO);
            glBindVertexArray(gVAO[0]);
            glUseProgram(ProgramPrimitives);

            while (!Glfw.WindowShouldClose(window))
            {
                Glfw.WaitEvents();
                //Glfw.PollEvents();
                if (focused)
                    Render();
            }

            glDetachShader(ProgramPrimitives, VertexPrimitiveShader);
            glDetachShader(ProgramPrimitives, FragmentPrimitiveShader);
            glDeleteShader(VertexPrimitiveShader);
            glDeleteShader(FragmentPrimitiveShader);

            glDetachShader(ProgramTexture, VertexTextureShader);
            glDetachShader(ProgramTexture, FragmentTextureShader);
            glDeleteShader(VertexTextureShader);
            glDeleteShader(FragmentTextureShader);

            glDeleteVertexArrays(1, gVAO);
            glDeleteProgram(ProgramPrimitives);
            glDeleteProgram(ProgramTexture);

            ClearEventsCallbacks();
            Glfw.DestroyWindow(window);
        }

        internal void Render()
        {
            glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
            DrawItems(wnd_handler.GetWindow());
            //draw tooltip if needed
            DrawToolTip();
            Glfw.SwapBuffers(window);
        }
        private void DrawToolTip()//refactor
        {
            if (!_tooltip.IsVisible)
                return;

            _tooltip.GetTextLine().UpdateData(UpdateType.Critical);
            _tooltip.SetX(ptrRelease.X - 10);
            _tooltip.SetY(ptrRelease.Y - _tooltip.GetHeight() - 2);
            _tooltip.SetWidth(
                _tooltip.GetPadding().Left +
                _tooltip.GetPadding().Right +
                _tooltip.GetTextWidth()
                );
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
                glDisable(GL_MULTISAMPLE);
                DrawPixels((root as IPixelDrawable));
                foreach (var child in (root as VisualItem).GetItems())
                {
                    DrawItems(child);
                }
                glEnable(GL_MULTISAMPLE);
            }
            if (root is TextItem)
            {
                glDisable(GL_MULTISAMPLE);
                DrawText(root as TextItem);
                glEnable(GL_MULTISAMPLE);
                if (_isStencilSet == root)
                {
                    glDisable(GL_STENCIL_TEST);
                    _isStencilSet = null;
                }
            }
            if (root is IImageItem)
            {
                DrawShell(root);
                glUseProgram(ProgramTexture);
                DrawImage(root as ImageItem);
                glUseProgram(ProgramPrimitives);
            }
            else
            {
                if (root is PopUpMessage)
                {
                    (root as PopUpMessage).InitTimer();
                }
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
            crd_array = GraphicsMathService.ToGL(GraphicsMathService.GetRectangle(w, h, x, y), wnd_handler);

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
        }

        void DrawPixels(IPixelDrawable item)
        {
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
        }

        void DrawImage(ImageItem image)
        {
            //проверка: полностью ли влезает объект в свой контейнер
            CheckOutsideBorders(image as BaseItem);

            float i_x0 = ((float)image.GetX() / (float)wnd_handler.GetWidth() * 2.0f) - 1.0f;
            float i_y0 = ((float)image.GetY() / (float)wnd_handler.GetHeight() * 2.0f - 1.0f) * (-1.0f);
            float i_x1 = (((float)image.GetX() + (float)image.GetWidth()) / (float)wnd_handler.GetWidth() * 2.0f) - 1.0f;
            float i_y1 = (((float)image.GetY() + (float)image.GetHeight()) / (float)wnd_handler.GetHeight() * 2.0f - 1.0f) * (-1.0f);

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
            byte[] bitmap = image.GetPixMapImage();
            int w = image.GetImageWidth(), h = image.GetImageHeight();

            uint[] texture = new uint[1];
            glGenTextures(1, texture);

            glBindTexture(GL_TEXTURE_2D, texture[0]);

            glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, h, w);
            glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, h, w, GL_RGBA, GL_UNSIGNED_BYTE, bitmap);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

            //glGenerateMipmap(GL_TEXTURE_2D);
            glActiveTexture(GL_TEXTURE0);

            int location = glGetUniformLocation(ProgramTexture, "tex".ToCharArray());
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
