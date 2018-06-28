using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.IO;
using System.Text;

using Glfw3;
using System.Threading;
using System.Drawing;

namespace SpaceVIL
{
    internal class DrawEngine : GL.WGL.OpenWGL
    //where TLayout : VisualItem
    {
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
        //Glfw.WindowFocusFunc windowFocusCallback;
        ///////////////////////////////////////////////

        public bool borderHidden;
        public bool appearInCenter;
        public bool focusable;
        public bool focused;
        public bool alwaysOnTop;
        public Pointer window_position = new Pointer();
        private VisualItem HoveredItem;
        private VisualItem FocusedItem;
        private Pointer ptrPress = new Pointer();
        private Pointer ptrRelease = new Pointer();

        public WindowLayout wnd;

        Glfw.Window window;
        private uint[] gVAO = new uint[1];
        private uint ProgramPrimitives;
        private uint VertexPrimitiveShader, FragmentPrimitiveShader;

        private uint ProgramTexture;
        private uint VertexTextureShader, FragmentTextureShader;

        public DrawEngine(WindowLayout handler)
        {
            wnd = handler;
            window_position.X = 0;
            window_position.Y = 0;
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
            CreateWindow(wnd.GetWindowTitle(), 4, wnd.GetWidth(), wnd.GetHeight());
            SetEventsCallbacks();
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
            Glfw.SetWindowSizeLimits(window, wnd.GetMinWidth(), wnd.GetMinHeight(), wnd.GetMaxWidth(), wnd.GetMaxHeight());
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
            //windowFocusCallback = Focus;
            //Glfw.SetWindowFocusCallback(window, windowFocusCallback);
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
                EngineEvent.SetEvent(EventType.MouseScroll);
            }
        }

        private void KeyPress(Glfw.Window glfwwnd, KeyCode key, int scancode, InputState action, KeyMods mods)
        {
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
            FocusedItem?.InvokeInputTextEvents(codepoint, mods);
        }
        private void Focus(Glfw.Window glfwwnd, bool value)
        {
            if (focusable)
            {
                focused = value;
                Glfw.FocusWindow(window);
            }
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
            if (width <= wnd.GetMinWidth())
            {
                width = wnd.GetMinWidth();
            }
            wnd.SetWidth(width);
            if (height <= wnd.GetMinHeight())
            {
                height = wnd.GetMinHeight();
            }
            wnd.SetHeight(height);

            glViewport(0, 0, wnd.GetWidth(), wnd.GetHeight());
            Render();
        }

        public void SetWindowSize()
        {
            Glfw.SetWindowSize(window, wnd.GetWidth(), wnd.GetHeight());
        }

        //OpenGL input interaction function
        private VisualItem GetHoverVisualItem(int xpos, int ypos)
        {
            int index = -1;

            foreach (var item in ItemsLayoutBox.GetLayoutItems(wnd.Id))
            {
                if (item is VisualItem)
                {
                    if (!(item as VisualItem).IsVisible)
                        continue;
                    if ((item as VisualItem).GetHoverVerification(xpos, ypos))
                    {
                        index = ItemsLayoutBox.GetLayoutItems(wnd.Id).ToList().IndexOf(item);
                    }
                }
            }

            if (index != -1)
                return (VisualItem)ItemsLayoutBox.GetLayoutItems(wnd.Id).ElementAt(index);
            else
                return null;
        }
        protected void MouseMove(Glfw.Window wnd, double xpos, double ypos)
        {
            //logic of hovers
            ptrRelease.X = (int)xpos;
            ptrRelease.Y = (int)ypos;

            if (EngineEvent.LastEvent() == EventType.MousePressed && HoveredItem is IDraggable)
            {
                HoveredItem._mouse_ptr.SetPosition((float)xpos, (float)ypos);
                HoveredItem.InvokePoolEvents();

                //Focus get
                if (FocusedItem != null)
                    FocusedItem.IsFocused = false;

                FocusedItem = HoveredItem;
                FocusedItem.IsFocused = true;

            }
            else
            {
                HoveredItem = GetHoverVisualItem(ptrRelease.X, ptrRelease.Y);
                ptrPress.X = ptrRelease.X;
                ptrPress.Y = ptrRelease.Y;
                EngineEvent.SetEvent(EventType.MouseMove);
            }
        }

        protected void MouseClick(Glfw.Window window, MouseButton button, InputState state, KeyMods mods)
        {
            switch (state)
            {
                case InputState.Release:
                    if (HoveredItem != null)
                    {
                        HoveredItem.InvokePoolEvents();

                        //Focus get
                        if (FocusedItem != null)
                            FocusedItem.IsFocused = false;

                        FocusedItem = HoveredItem;
                        FocusedItem.IsFocused = true;
                    }
                    EngineEvent.SetEvent(EventType.MouseRelease);
                    break;
                case InputState.Press:
                    EngineEvent.SetEvent(EventType.MousePressed);
                    break;
                case InputState.Repeat:
                    break;
                default:
                    break;
            }
        }

        public void Run()
        {
            glGenVertexArrays(1, gVAO);
            glBindVertexArray(gVAO[0]);
            glUseProgram(ProgramPrimitives);
            //pre render
            Render();

            while (!Glfw.WindowShouldClose(window))
            {
                Render();
                Glfw.WaitEvents();
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
            DrawItems(wnd.GetWindow());
            Glfw.SwapBuffers(window);
        }

        //Common Draw function
        private void DrawItems(BaseItem root)
        {
            if (!root.IsVisible)
                return;

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
            else if (root is ITextContainer)
            {
                glDisable(GL_MULTISAMPLE);
                DrawText((root as ITextContainer).GetText());
                glEnable(GL_MULTISAMPLE);
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
            glEnable(GL_STENCIL_TEST);
            uint[] buffers = new uint[2];
            glGenBuffers(2, buffers);

            //Vertex
            List<float[]> crd_array;
            crd_array = GraphicsMathService.ToGL(GraphicsMathService.GetRectangle(w, h, x, y), wnd);

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
                if (shell.GetParent().GetX() + shell.GetParent().GetWidth() > shell.GetX()
                    && shell.GetParent().GetX() + shell.GetParent().GetWidth() < shell.GetX() + shell.GetWidth())
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
                            SetStencilMask(side.Value[1], shell.GetHeight(), side.Value[0], shell.GetY());
                    }
                    //set stencil mask
                    glStencilFunc(GL_NOTEQUAL, 2, 255);
                }
            }
            return true;
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
            //Console.WriteLine(item.GetItemText());
            uint[] buffers = new uint[2];
            glGenBuffers(2, buffers);
            float[] data = item.Shape();
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
            float i_x0 = ((float)image.GetX() / (float)wnd.GetWidth() * 2.0f) - 1.0f;
            float i_y0 = ((float)image.GetY() / (float)wnd.GetHeight() * 2.0f - 1.0f) * (-1.0f);
            float i_x1 = (((float)image.GetX() + (float)image.GetWidth()) / (float)wnd.GetWidth() * 2.0f) - 1.0f;
            float i_y1 = (((float)image.GetY() + (float)image.GetHeight()) / (float)wnd.GetHeight() * 2.0f - 1.0f) * (-1.0f);

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
