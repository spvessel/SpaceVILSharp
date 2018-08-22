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
        public void ResetItems()
        {
            if (FocusedItem != null)
                FocusedItem.IsFocused = false;
            FocusedItem = null;
            if (HoveredItem != null)
                HoveredItem.IsMouseHover = false;
            HoveredItem = null;

            HoveredItems.Clear();
        }
        private ToolTip _tooltip = new ToolTip();
        private BaseItem _isStencilSet = null;
        public InputDeviceEvent EngineEvent = new InputDeviceEvent();
        private MouseArgs _margs = new MouseArgs();
        private KeyArgs _kargs = new KeyArgs();
        private TextInputArgs _tiargs = new TextInputArgs();

        private List<VisualItem> HoveredItems;
        private VisualItem HoveredItem = null;
        private VisualItem FocusedItem = null;
        public void SetFocusedItem(VisualItem item)
        {
            if (item == null)
            {
                FocusedItem = null;
                return;
            }
            if (FocusedItem != null)
                FocusedItem.IsFocused = false;
            FocusedItem = item;
            FocusedItem.IsFocused = true;
        }
        private Pointer ptrPress = new Pointer();
        private Pointer ptrRelease = new Pointer();
        private Pointer ptrClick = new Pointer();

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
            //LogService.Log().EndLogging();
            Glfw.Terminate();
        }
        public void Close()
        {
            _handler.SetToClose();
        }

        private Glfw.Image _icon_big;
        private Glfw.Image _icon_small;

        public void SetBigIcon(Image icon)
        {
            if (_icon_big.Pixels == null)
            {
                if (icon == null)
                    return;

                List<byte> _map = new List<byte>();
                Bitmap bmp = new Bitmap(icon);
                _icon_big.Width = icon.Width;
                _icon_big.Height = icon.Height;
                for (int i = 0; i < icon.Width; i++)
                {
                    for (int j = 0; j < icon.Height; j++)
                    {
                        Color pixel = bmp.GetPixel(i, j);
                        _map.Add(pixel.R);
                        _map.Add(pixel.G);
                        _map.Add(pixel.B);
                        _map.Add(pixel.A);
                    }
                }
                _icon_big.Pixels = _map.ToArray();
            }
        }
        public void SetSmallIcon(Image icon)
        {
            if (_icon_small.Pixels == null)
            {
                if (icon == null)
                    return;

                List<byte> _map = new List<byte>();
                Bitmap bmp = new Bitmap(icon);
                _icon_small.Width = icon.Width;
                _icon_small.Height = icon.Height;
                for (int i = 0; i < icon.Width; i++)
                {
                    for (int j = 0; j < icon.Height; j++)
                    {
                        Color pixel = bmp.GetPixel(i, j);
                        _map.Add(pixel.R);
                        _map.Add(pixel.G);
                        _map.Add(pixel.B);
                        _map.Add(pixel.A);
                    }
                }
                _icon_small.Pixels = _map.ToArray();
            }
        }

        public void Init()
        {
            lock (CommonService.GlobalLocker)
            {
                //InitWindow
                _handler.InitGlfw();
                _handler.CreateWindow();

                //устанавливаем параметры отрисовки
                glEnable(GL_TEXTURE_2D);
                glEnable(GL_MULTISAMPLE);
                glEnable(GL_BLEND);
                glEnable(GL_CULL_FACE);
                glCullFace(GL_BACK);
                glEnable(GL_ALPHA_TEST);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                //glEnable(GL_DEPTH_TEST);
                //glEnable(GL_STENCIL_TEST);
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

                if (_icon_big.Pixels != null && _icon_small.Pixels != null)
                {
                    Glfw.Image[] images = new Glfw.Image[2];
                    images[0] = _icon_big;
                    images[1] = _icon_small;
                    Glfw.SetWindowIcon(_handler.GetWindow(), images);
                }
                SetWindowPos();
                SetEventsCallbacks();

                glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            }

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
                    (root as IScrollable).InvokeScrollUp(_margs);
                if (yoffset < 0 || xoffset > 0)
                    (root as IScrollable).InvokeScrollDown(_margs);
                EngineEvent.SetEvent(InputEventType.MouseScroll);
            }
        }

        private void KeyPress(Glfw.Window glfwwnd, KeyCode key, int scancode, InputState action, KeyMods mods)
        {
            if (!_handler.Focusable)
                return;

            _tooltip.InitTimer(false);

            _kargs.Key = key;
            _kargs.Scancode = scancode;
            _kargs.State = action;
            _kargs.Mods = mods;

            if ((FocusedItem is ITextShortcuts) && action == InputState.Press)
            {
                if ((mods == KeyMods.Control && key == KeyCode.V) || (mods == KeyMods.Shift && key == KeyCode.Insert))
                {
                    CommonService.ClipboardTextStorage = Glfw.GetClipboardString(_handler.GetWindow());
                    //AssignActions(InputEventType.KeyRelease, _kargs, FocusedItem);
                    // string paste_str = Glfw.GetClipboardString(_handler.GetWindow());
                    //(FocusedItem as ITextShortcuts).PasteText(paste_str);//!!!!!!!!!!!
                }
                else if (mods == KeyMods.Control && key == KeyCode.C)
                {
                    string copy_str = (FocusedItem as ITextShortcuts).GetSelectedText();
                    Glfw.SetClipboardString(_handler.GetWindow(), copy_str);
                }
                else if (mods == KeyMods.Control && key == KeyCode.X)
                {
                    string cut_str = (FocusedItem as ITextShortcuts).CutText();
                    Glfw.SetClipboardString(_handler.GetWindow(), cut_str);
                }
                else
                {
                    //FocusedItem?.InvokeKeyboardInputEvents(key, scancode, action, mods);
                    if (action == InputState.Press)
                    {
                        // FocusedItem.EventKeyPress?.Invoke(FocusedItem, _kargs);
                        AssignActions(InputEventType.KeyPress, _kargs, FocusedItem);
                    }
                    if (action == InputState.Repeat)
                    {
                        // FocusedItem.EventKeyPress?.Invoke(FocusedItem, _kargs);
                        AssignActions(InputEventType.KeyPress, _kargs, FocusedItem);
                    }
                    if (action == InputState.Release)
                    {
                        // FocusedItem.EventKeyRelease?.Invoke(FocusedItem, _kargs);
                        AssignActions(InputEventType.KeyRelease, _kargs, FocusedItem);
                    }
                }
            } //Нехорошо это все
            else
            {
                //FocusedItem?.InvokeKeyboardInputEvents(key, scancode, action, mods);
                if (action == InputState.Press)
                {
                    // FocusedItem.EventKeyPress?.Invoke(FocusedItem, _kargs);
                    AssignActions(InputEventType.KeyPress, _kargs, FocusedItem);
                }
                if (action == InputState.Repeat)
                {
                    AssignActions(InputEventType.KeyPress, _kargs, FocusedItem);
                    // FocusedItem.EventKeyPress?.Invoke(FocusedItem, _kargs);
                }
                if (action == InputState.Release)
                {
                    // FocusedItem.EventKeyRelease?.Invoke(FocusedItem, _kargs);
                    AssignActions(InputEventType.KeyRelease, _kargs, FocusedItem);
                }
            }
        }
        private void TextInput(Glfw.Window glfwwnd, uint codepoint, KeyMods mods)
        {
            if (!_handler.Focusable)
                return;

            _tooltip.InitTimer(false);
            _tiargs.Character = codepoint;
            _tiargs.Mods = mods;
            // FocusedItem?.InvokeInputTextEvents(codepoint, mods);
            AssignActions(InputEventType.TextInput, _tiargs, FocusedItem);
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
                    if (_handler.GetLayout().IsOutsideClickClosable)
                    {
                        ResetItems();
                        _handler.GetLayout().Close();
                    }
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
            _handler.GetLayout().Close();
        }
        private void Resize(Glfw.Window glfwwnd, int width, int height)
        {
            _tooltip.InitTimer(false);
            _handler.GetLayout().SetWidth(width);
            _handler.GetLayout().SetHeight(height);
            if (!_handler.GetLayout().IsBorderHidden)
            {
                glViewport(0, 0, _handler.GetLayout().GetWidth(), _handler.GetLayout().GetHeight());
            }
        }

        public void SetWindowSize()
        {
            EngineEvent.SetEvent(InputEventType.WindowResize);
            Glfw.SetWindowSize(_handler.GetWindow(), _handler.GetLayout().GetWidth(), _handler.GetLayout().GetHeight());
            if (_handler.GetLayout().IsBorderHidden)
            {
                glViewport(0, 0, _handler.GetLayout().GetWidth(), _handler.GetLayout().GetHeight());
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

            List<BaseItem> layout_box_of_items = new List<BaseItem>();
            lock (_handler.GetLayout().engine_locker)
            {
                foreach (var item in ItemsLayoutBox.GetLayoutFloatItems(_handler.GetLayout().Id))
                    layout_box_of_items.Add(item);
                foreach (var item in ItemsLayoutBox.GetLayoutItems(_handler.GetLayout().Id))
                    layout_box_of_items.Add(item);
            }

            foreach (var item in layout_box_of_items)
            {
                if (item is VisualItem)
                {
                    if (!(item as VisualItem).IsVisible)
                        continue;
                    if ((item as VisualItem).GetHoverVerification(xpos, ypos))
                    {
                        HoveredItems.Add(item as VisualItem);
                        AssignActions(InputEventType.MouseHover, _margs, item as VisualItem);
                    }
                    AssignActions(InputEventType.MouseMove, _margs, false);
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

            EngineEvent.SetEvent(InputEventType.MouseMove);
            _tooltip.InitTimer(false);

            //logic of hovers
            ptrRelease.X = (int)xpos;
            ptrRelease.Y = (int)ypos;

            _margs.Position.SetPosition((float)xpos, (float)ypos);

            AssignActions(InputEventType.MouseMove, _margs, _handler.GetLayout().GetWindow());

            if (EngineEvent.LastEvent().HasFlag(InputEventType.MousePressed)) // жость какая-то ХЕРОТАААА!!!
            {
                if (_handler.GetLayout().IsBorderHidden && _handler.GetLayout().IsResizeble)
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
                        Update();
                    }
                }
                if (_handler.GetLayout().GetWindow()._sides == 0)
                {
                    VisualItem draggable = IsInListHoveredItems<IDraggable>();
                    VisualItem anchor = IsInListHoveredItems<IWindowAnchor>();
                    if (draggable != null)
                    {
                        draggable._mouse_ptr.SetPosition((float)xpos, (float)ypos);
                        // draggable.EventMouseDrag?.Invoke(HoveredItem, _margs);
                        AssignActions(InputEventType.MouseDrag, _margs, draggable);

                        //Focus get
                        if (FocusedItem != null)
                            FocusedItem.IsFocused = false;

                        FocusedItem = HoveredItem;
                        FocusedItem.IsFocused = true;

                        //Update();
                    }
                    else if (anchor != null && !(HoveredItem is ButtonCore))
                    {
                        if ((ptrRelease.X - ptrPress.X) != 0 || (ptrRelease.Y - ptrPress.Y) != 0)
                        {
                            _handler.WPosition.X += (ptrRelease.X - ptrPress.X);
                            _handler.WPosition.Y += (ptrRelease.Y - ptrPress.Y);
                            SetWindowPos();
                            //Update();
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
                        _tooltip.SetText(HoveredItem.GetToolTip());
                        _tooltip.InitTimer(true);
                    }

                    _handler.SetCursorType(Glfw.CursorType.Arrow);
                    if (_handler.GetLayout().IsBorderHidden && _handler.GetLayout().IsResizeble)
                    {
                        //resize
                        if ((xpos < _handler.GetLayout().GetWindow().GetWidth() - 5)
                            && (xpos > 5)
                            && (ypos < _handler.GetLayout().GetWindow().GetHeight() - 5)
                            && ypos > 5)
                        {
                            if (HoveredItem is ITextEditable)
                                _handler.SetCursorType(Glfw.CursorType.Beam);
                            if (HoveredItem is SplitHolder)
                            {
                                if (((SplitHolder)HoveredItem).GetOrientation().Equals(Orientation.Horizontal))
                                    _handler.SetCursorType(Glfw.CursorType.ResizeY);
                                else _handler.SetCursorType(Glfw.CursorType.ResizeX);
                            }
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
                        if (HoveredItem is SplitHolder)
                        {
                            if (((SplitHolder)HoveredItem).GetOrientation().Equals(Orientation.Horizontal))
                                _handler.SetCursorType(Glfw.CursorType.ResizeY);
                            else _handler.SetCursorType(Glfw.CursorType.ResizeX);
                        }
                    }
                    VisualItem popup = IsInListHoveredItems<PopUpMessage>();
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

            _margs.Button = button;
            _margs.State = state;
            _margs.Mods = mods;

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
                    EngineEvent.ResetAllEvents();
                    EngineEvent.SetEvent(InputEventType.MouseRelease);

                    if (EngineEvent.LastEvent().HasFlag(InputEventType.WindowResize)
                        || EngineEvent.LastEvent().HasFlag(InputEventType.WindowMove))
                    {
                        // EngineEvent.SetEvent(InputEventType.MouseRelease);
                        // EngineEvent.ResetAllEvents();
                        return;
                    }
                    if (EngineEvent.LastEvent().HasFlag(InputEventType.MouseMove))
                    {
                        float len = (float)Math.Sqrt(Math.Pow(ptrRelease.X - ptrClick.X, 2) + Math.Pow(ptrRelease.Y - ptrClick.Y, 2));
                        if (len > 3.0f)
                        {
                            // EngineEvent.ResetAllEvents();
                            // EngineEvent.SetEvent(InputEventType.MouseRelease);
                            return;
                        }
                    }

                    if (HoveredItem != null)
                    {
                        AssignActions(InputEventType.MouseRelease, _margs, false);

                        //Focus get
                        if (FocusedItem != null)
                            FocusedItem.IsFocused = false;

                        FocusedItem = HoveredItem;
                        FocusedItem.IsFocused = true;
                    }
                    break;

                case InputState.Press:
                    Glfw.GetCursorPos(_handler.GetWindow(), out double xpos, out double ypos);
                    ptrClick.X = (int)xpos;
                    ptrClick.Y = (int)ypos;

                    AssignActions(InputEventType.MousePressed, _margs, false);

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

        private void AssignActions(InputEventType action, InputEventArgs args, bool only_last)
        {
            if (only_last)
            {
                _handler.GetLayout().SetEventTask(new EventTask()
                {
                    Item = HoveredItem,
                    Action = action,
                    Args = args
                });
            }
            else
            {
                foreach (var item in HoveredItems)
                {
                    item._mouse_ptr.X = ptrRelease.X;
                    item._mouse_ptr.Y = ptrRelease.Y;
                    _handler.GetLayout().SetEventTask(new EventTask()
                    {
                        Item = item,
                        Action = action,
                        Args = args
                    });
                }
            }
            _handler.GetLayout().ExecutePollActions();
        }
        private void AssignActions(InputEventType action, InputEventArgs args, VisualItem sender)
        {
            _handler.GetLayout().SetEventTask(new EventTask()
            {
                Item = sender,
                Action = action,
                Args = args
            });
            _handler.GetLayout().ExecutePollActions();
        }

        internal void UpdateGL()
        {
            Glfw.PostEmptyEvent();
        }

        internal float _interval = 1.0f / 30.0f;//1000 / 60;
        // internal float _interval = 1.0f / 60.0f;//1000 / 60;
        // internal int _interval = 11;//1000 / 90;
        // internal int _interval = 08;//1000 / 120;

        internal void Update()
        {
            lock (_handler.GetLayout().engine_locker)
                Render();
        }

        public void Run()
        {
            glGenVertexArrays(1, _handler.GVAO);
            glBindVertexArray(_handler.GVAO[0]);

            _primitive.UseShader();
            Focus(_handler.GetWindow(), true);

            while (!_handler.IsClosing())
            {
                //Glfw.WaitEvents();
                Glfw.WaitEventsTimeout(_interval);
                Update();
            }

            _primitive.DeleteShader();
            _texture.DeleteShader();

            glDeleteVertexArrays(1, _handler.GVAO);

            _handler.ClearEventsCallbacks();
            _handler.Destroy();
        }

        internal void Render()
        {
            if (_handler.Focused)
            {
                glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
                DrawItems(_handler.GetLayout().GetWindow());
                foreach (var item in ItemsLayoutBox.GetLayout(_handler.GetLayout().Id).FloatItems)
                {
                    DrawItems(item as BaseItem);
                }
                //draw tooltip if needed
                DrawToolTip();
                // if (!_handler.Focusable)
                // {
                //     DrawShadePillow();
                // }
                _handler.Swap();
            }
            //Thread.Sleep(1000/60);
        }
        private void DrawShadePillow()
        {
            uint[] buffers = new uint[2];
            glGenBuffers(2, buffers);

            //Vertex
            float[] vertexData = new float[6 * 3]
            {
		        //X     Y		Z
		        -1.0f,  1.0f,   0.0f,
                -1.0f,  -1.0f,  0.0f,
                1.0f,   -1.0f,  0.0f,

                1.0f,   -1.0f,  0.0f,
                1.0f,   1.0f,   0.0f,
                -1.0f,  1.0f,   0.0f,
            };

            glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);
            glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, IntPtr.Zero);
            glEnableVertexAttribArray(0);

            //Color
            float[] argb = {
                0.0f,
                0.0f,
                0.0f,
                (float)150 / 255.0f};

            float[] colorData = new float[6 * 4];
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
            glDrawArrays(GL_TRIANGLES, 0, 6);

            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);

            // Clear VBO and shader
            glDeleteBuffers(2, buffers);
        }

        private void DrawToolTip() //refactor
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

            /*Console.WriteLine(_tooltip.GetText() + " " +
                _tooltip.GetWidth() + " " +
                _tooltip.GetHeight() + " " +
                _tooltip.GetX() + " " +
                _tooltip.GetY());*/

            DrawShell(_tooltip);

            //glDisable(GL_MULTISAMPLE);
            _tooltip.GetTextLine().UpdateGeometry();
            //DrawText_new(_tooltip.GetTextLine());
            DrawText_deprecated(_tooltip.GetTextLine());
            //glEnable(GL_MULTISAMPLE);
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
                // DrawText_new(root as TextItem);
                try
                {
                    DrawText_deprecated(root as TextItem);
                }
                catch (Exception ex)
                {
                    Console.WriteLine(ex);
                }
                if (_isStencilSet == root)
                {
                    glDisable(GL_STENCIL_TEST);
                    _isStencilSet = null;
                }
                //_primitive.UseShader();
            }
            if (root is IImageItem)
            {
                DrawShell(root);
                DrawImage(root as ImageItem);
                _primitive.UseShader();
            }
            else
            {
                try
                {
                    DrawShell(root);
                }
                catch (Exception ex)
                {
                    Console.WriteLine(ex);
                }

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
        private void SetStencilMask(List<float[]> crd_array)
        {
            uint[] buffers = new uint[2];
            glGenBuffers(2, buffers);

            //Vertex
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
            float[] argb = { 0.0f, 0.0f, 0.0f, 0.0f };
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
            //crd_array.Clear();
        }

        private void CheckOutsideBorders(BaseItem shell)
        {
            //if (shell.CutBehaviour == StencilBehaviour.Strict)
            //if(shell.GetParent() != null)
            // _isStencilSet = shell;
            //StrictStencil(shell);
            //else
            LazyStencil(shell);
        }

        private void StrictStencil(BaseItem shell)
        {
            glEnable(GL_STENCIL_TEST);
            /*glClearStencil(1);
            glStencilMask(0xFF);
            glStencilFunc(GL_NEVER, 2, 0);
            glStencilOp(GL_REPLACE, GL_KEEP, GL_KEEP);
            SetStencilMask(shell.GetParent().MakeShape());
            glStencilFunc(GL_NOTEQUAL, 1, 0xFF);*/
            glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
            glClear(GL_STENCIL_BUFFER_BIT);
            glStencilMask(0x00);

            glStencilFunc(GL_ALWAYS, 1, 0xFF);
            glStencilMask(0xFF);
            SetStencilMask(shell.GetParent().MakeShape());

            glStencilFunc(GL_EQUAL, 1, 0xFF);

            shell.GetParent()._confines_x_0 = shell.GetParent().GetX() + shell.GetParent().GetPadding().Left;
            shell.GetParent()._confines_x_1 = shell.GetParent().GetX() + shell.GetParent().GetWidth() - shell.GetParent().GetPadding().Right;
            shell.GetParent()._confines_y_0 = shell.GetParent().GetY() + shell.GetParent().GetPadding().Top;
            shell.GetParent()._confines_y_1 = shell.GetParent().GetY() + shell.GetParent().GetHeight() - shell.GetParent().GetPadding().Bottom;
            SetConfines(shell);
        }

        private void SetConfines(BaseItem shell)
        {
            shell._confines_x_0 = shell.GetParent()._confines_x_0;
            shell._confines_x_1 = shell.GetParent()._confines_x_1;
            shell._confines_y_0 = shell.GetParent()._confines_y_0;
            shell._confines_y_1 = shell.GetParent()._confines_y_1;

            VisualItem root = shell as VisualItem;
            if (root != null)
            {
                foreach (var item in root.GetItems())
                {
                    SetConfines(item);
                }
            }
        }

        private bool LazyStencil(BaseItem shell)
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


                if (outside.Count > 0 || shell.GetParent() is TextBlock)
                {
                    _isStencilSet = shell;
                    StrictStencil(shell);
                    // Console.WriteLine(shell.GetItemName());

                    //stencil
                    /*glClearStencil(1);
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
                    glStencilFunc(GL_NOTEQUAL, 2, 255);*/
                    return true;
                }
            }
            return false;
        }
        private void DrawShell(BaseItem shell)
        {
            //проверка: полностью ли влезает объект в свой контейнер
            CheckOutsideBorders(shell);

            if (shell.GetBackground().A == 0)
            {
                return;
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
            //glClearStencil(1);
        }

        void DrawText_new(TextItem image)
        {
            byte[] bitmap = GraphicsMathService.PointsToTexture(image);

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
            int w = image.GetWidth();
            int h = image.GetHeight();

            uint[] texture = new uint[1];
            glGenTextures(1, texture);

            glBindTexture(GL_TEXTURE_2D, texture[0]);

            glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, h, w);
            glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, h, w, GL_RGBA, GL_UNSIGNED_BYTE, bitmap);
            // glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, w, h);
            // glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, w, h, GL_RGBA, GL_UNSIGNED_BYTE, bitmap);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

            //glGenerateMipmap(GL_TEXTURE_2D);
            glActiveTexture(GL_TEXTURE0);

            _texture.UseShader();
            int location = glGetUniformLocation(_texture.GetProgramID(), "tex".ToCharArray());
            if (location >= 0)
            {
                try
                {
                    glUniform1i(location, 0);
                }
                catch (Exception ex)
                {
                    Console.WriteLine(ex.GetType() + " " + image.GetItemName() + " " + _handler.GetLayout().GetWindowName());
                    return;
                    // Unhandled Exception: System.Exception: Extension function glUniform1i not supported
                    // at GL.WGL.OpenWGL.InvokeWGL[T](String name)
                    // at GL.WGL.OpenWGL.glUniform1i(Int32 location, Int32 v0)
                    // at SpaceVIL.DrawEngine.DrawImage(ImageItem image)
                }
            }

            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, IntPtr.Zero);

            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);

            glDeleteBuffers(2, buffers);
            glDeleteTextures(1, texture);

            _primitive.UseShader();
        }

        void DrawText_deprecated(TextItem item)
        {
            glDisable(GL_MULTISAMPLE);

            uint[] buffers = new uint[2];
            glGenBuffers(2, buffers);

            float[] data = item.Shape();
            float[] colorData = item.GetColors();

            //bool ok = CheckOutsideBorders(item as BaseItem); //deprecated
            CheckOutsideBorders(item as BaseItem);

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
            glDisable(GL_MULTISAMPLE);

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
            glGenerateMipmap(GL_TEXTURE_2D);
            // glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, w, h);
            // glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, w, h, GL_RGBA, GL_UNSIGNED_BYTE, bitmap);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_BASE_LEVEL, 256);

            // glActiveTexture(GL_TEXTURE0);

            _texture.UseShader();
            int location = glGetUniformLocation(_texture.GetProgramID(), "tex".ToCharArray());
            if (location >= 0)
            {
                try
                {
                    glUniform1i(location, 0);
                }
                catch (Exception ex)
                {
                    Console.WriteLine(ex.GetType() + " " + image.GetItemName() + " " + _handler.GetLayout().GetWindowName());
                    return;
                    // Unhandled Exception: System.Exception: Extension function glUniform1i not supported
                    // at GL.WGL.OpenWGL.InvokeWGL[T](String name)
                    // at GL.WGL.OpenWGL.glUniform1i(Int32 location, Int32 v0)
                    // at SpaceVIL.DrawEngine.DrawImage(ImageItem image)
                }
            }

            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, IntPtr.Zero);

            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);

            glDeleteBuffers(2, buffers);
            glDeleteTextures(1, texture);
        }
    }
}
