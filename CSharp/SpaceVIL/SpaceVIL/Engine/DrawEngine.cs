using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.IO;
using System.Text;

using Glfw3;
using System.Threading;

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
        //Glfw.WindowFocusFunc windowFocusCallback;
        Glfw.KeyFunc keyPressCallback;
        ///////////////////////////////////////////////

        public bool borderHidden;
        public bool appearInCenter;
        public bool focusable;
        public bool focused;
        public bool alwaysOnTop;
        public Pointer window_position = new Pointer();
        private VisualItem HoveredItem;
        private Pointer ptrPress = new Pointer();
        private Pointer ptrRelease = new Pointer();

        public WindowLayout wnd;

        Glfw.Window window;
        private uint[] gVAO = new uint[1];
        private uint ProgramPrimitives;
        uint vShader, fShader;

        public DrawEngine (WindowLayout handler) 
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
            glEnable(GL_MULTISAMPLE);
            glEnable(GL_BLEND);
            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glDisable(GL_DEPTH_TEST);
            glEnable(GL_ALPHA_TEST);

            CreateShaderProgram(
            Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Shaders.vs_fill.glsl"),
            Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Shaders.fs_fill.glsl")
            );
            if (ProgramPrimitives == 0)
                Console.WriteLine("Could not create the fill shaders");

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

            Glfw.WindowHint(Glfw.Hint.Samples, 4);
            //Glfw.WindowHint(Glfw.Hint.OpenglForwardCompat, true);
            //Glfw.WindowHint(Glfw.Hint.OpenglDebugContext, true);
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

        private void CreateShaderProgram(Stream vertex_shader, Stream fragment_shader)
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

            vShader = glCreateShader(GL_VERTEX_SHADER);
            glShaderSource(vShader, 1, new[] { v_code.ToString() }, new[] { v_code.ToString().Length });
            glCompileShader(vShader);

            fShader = glCreateShader(GL_FRAGMENT_SHADER);
            glShaderSource(fShader, 1, new[] { f_code.ToString() }, new[] { f_code.ToString().Length });
            glCompileShader(fShader);

            ProgramPrimitives = glCreateProgram();
            glAttachShader(ProgramPrimitives, vShader);
            glAttachShader(ProgramPrimitives, fShader);
            glLinkProgram(ProgramPrimitives);
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

        private void KeyPress(Glfw.Window glfwwnd, Glfw.KeyCode key, int scancode, Glfw.InputState action, Glfw.KeyMods mods)
        {
            if (key == Glfw.KeyCode.Space && action == Glfw.InputState.Release)
            {

            }
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
            }
            else
            {
                HoveredItem = GetHoverVisualItem(ptrRelease.X, ptrRelease.Y);
                ptrPress.X = ptrRelease.X;
                ptrPress.Y = ptrRelease.Y;
                EngineEvent.SetEvent(EventType.MouseMove);
            }
        }

        protected void MouseClick(Glfw.Window window, Glfw.MouseButton button, Glfw.InputState state, Glfw.KeyMods mods)
        {
            switch (state)
            {
                case Glfw.InputState.Release:
                    if (HoveredItem != null)
                    {
                        HoveredItem.InvokePoolEvents();
                    }
                    EngineEvent.SetEvent(EventType.MouseRelease);
                    break;
                case Glfw.InputState.Press:
                    EngineEvent.SetEvent(EventType.MousePressed);
                    break;
                case Glfw.InputState.Repeat:
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
            Render();

            while (!Glfw.WindowShouldClose(window))
            {
                //Glfw.PollEvents();
                //if (EngineEvent.IsEvent())
                {
                    Render();
                }
                Glfw.WaitEvents();
                //ev.WaitOne();
                //Thread.Sleep(1000 / 60);
            }

            glDetachShader(ProgramPrimitives, vShader);
            glDetachShader(ProgramPrimitives, fShader);
            glDeleteShader(vShader);
            glDeleteShader(fShader);

            glDeleteVertexArrays(1, gVAO);
            glDeleteProgram(ProgramPrimitives);

            ClearEventsCallbacks();
            Glfw.DestroyWindow(window);
        }

        private void Render()
        {
            glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
            DrawItems(wnd.Window);
            Glfw.SwapBuffers(window);
        }

        //Common Draw function
        private void DrawItems(BaseItem root)
        {
            if (!root.IsVisible)
                return;

            if (root is ITextContainer)
            {
                glDisable(GL_MULTISAMPLE);
                DrawText((root as ITextContainer).GetText());
                glEnable(GL_MULTISAMPLE);
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

        private void DrawShell(BaseItem shell)
        {
            //проверка: полностью ли влезает объект в свой контейнер
            //refactor
            if (shell.GetParent() != null && _isStencilSet == null)
            {
                int y = 0, h = 0;

                //bottom
                if (shell.GetParent().GetY() + shell.GetParent().GetHeight() > shell.GetY()
                    && shell.GetParent().GetY() + shell.GetParent().GetHeight() < shell.GetY() + shell.GetHeight())
                {
                    //match
                    _isStencilSet = shell;
                    y = shell.GetParent().GetY() + shell.GetParent().GetHeight() - shell.GetParent().GetPadding().Bottom;
                    h = shell.GetHeight();
                }
                //top
                else if (shell.GetParent().GetY() + shell.GetParent().GetPadding().Top > shell.GetY())
                {
                    //match
                    _isStencilSet = shell;
                    y = shell.GetY();
                    h = shell.GetParent().GetY() + shell.GetParent().GetPadding().Top - shell.GetY();
                }
                if (_isStencilSet != null)
                {
                    //stencil
                    glClearStencil(1);
                    glStencilMask(0xFF);
                    glStencilFunc(GL_NEVER, 2, 0);
                    glStencilOp(GL_REPLACE, GL_KEEP, GL_KEEP);
                    //draw mask
                    SetStencilMask(shell.GetWidth() + 2, h, shell.GetX() - 1, y);
                    //set stencil mask
                    glStencilFunc(GL_NOTEQUAL, 2, 255);
                }
            }

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

        void DrawText(ItemText item)
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

        void DrawImage(Image image)
        {
            
        }
    }
}