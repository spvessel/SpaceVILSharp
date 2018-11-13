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

#if OS_LINUX
using static GL.LGL.OpenLGL;
#elif OS_WNDOWS
using static GL.WGL.OpenWGL;
#else
using static GL.WGL.OpenWGL;
#endif

namespace SpaceVIL
{
    internal sealed class DrawEngine
    {
        public void ResetItems()
        {
            if (FocusedItem != null)
                FocusedItem.SetFocused(false);
            FocusedItem = null;
            if (HoveredItem != null)
                HoveredItem.SetMouseHover(false);
            HoveredItem = null;
            HoveredItems.Clear();
        }

        private ToolTip _tooltip = new ToolTip();
        private IBaseItem _isStencilSet = null;
        public InputDeviceEvent EngineEvent = new InputDeviceEvent();
        private MouseArgs _margs = new MouseArgs();
        private KeyArgs _kargs = new KeyArgs();
        private TextInputArgs _tiargs = new TextInputArgs();

        private List<Prototype> HoveredItems;
        private Prototype HoveredItem = null;
        private Prototype FocusedItem = null;
        public void SetFocusedItem(Prototype item)
        {
            if (item == null)
            {
                FocusedItem = null;
                return;
            }
            if (FocusedItem != null)
                FocusedItem.SetFocused(false);
            FocusedItem = item;
            FocusedItem.SetFocused(true);
        }
        private Pointer ptrPress = new Pointer();
        private Pointer ptrRelease = new Pointer();
        private Pointer ptrClick = new Pointer();

        internal GLWHandler _handler;
        private Shader _primitive;
        private Shader _texture;
        private Shader _char;
        private Shader _fxaa;
        private Shader _blur;

        public DrawEngine(WindowLayout handler)
        {
            HoveredItems = new List<Prototype>();
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
            Monitor.Enter(CommonService.GlobalLocker);
            try
            {
                //InitWindow
                _handler.InitGlfw();
                _handler.CreateWindow();
                SetEventsCallbacks();
                if (_icon_big.Pixels != null && _icon_small.Pixels != null)
                {
                    Glfw.Image[] images = new Glfw.Image[2];
                    images[0] = _icon_big;
                    images[1] = _icon_small;
                    Glfw.SetWindowIcon(_handler.GetWindowId(), images);
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                _handler.ClearEventsCallbacks();
                if (_handler.GetWindowId())
                    _handler.Destroy();
                _handler.GetLayout().Close();
            }
            finally
            {
                Monitor.Exit(CommonService.GlobalLocker);
            }

            //устанавливаем параметры отрисовки
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_BLEND);
            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
            glEnable(GL_ALPHA_TEST);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glEnable(GL_MULTISAMPLE);
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
            ///////////////////////////////////////////////
            _char = new Shader(
            Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Shaders.vs_char.glsl"),
            Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Shaders.fs_char.glsl"));
            _char.Compile();
            if (_char.GetProgramID() == 0)
                Console.WriteLine("Could not create char shaders");
            ///////////////////////////////////////////////
            _fxaa = new Shader(
            Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Shaders.vs_fxaa.glsl"),
            Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Shaders.fs_fxaa.glsl"));
            _fxaa.Compile();
            if (_fxaa.GetProgramID() == 0)
                Console.WriteLine("Could not create fxaa shaders");
            ///////////////////////////////////////////////
            _blur = new Shader(
            Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Shaders.vs_blur.glsl"),
            Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Shaders.fs_blur.glsl"));
            _blur.Compile();
            if (_blur.GetProgramID() == 0)
                Console.WriteLine("Could not create blur shaders");

            Run();
        }

        private void SetEventsCallbacks()
        {
            _handler.SetCallbackMouseMove(MouseMove);
            _handler.SetCallbackMouseClick(MouseClick);
            _handler.SetCallbackMouseScroll(MouseScroll);
            _handler.SetCallbackKeyPress(KeyPress);
            _handler.SetCallbackTextInput(TextInput);
            _handler.SetCallbackClose(CloseWindow);
            _handler.SetCallbackPosition(Position);
            _handler.SetCallbackFocus(Focus);
            _handler.SetCallbackResize(Resize);
        }

        public void MinimizeWindow()
        {
            EngineEvent.SetEvent(InputEventType.WindowMinimize);
            Glfw.IconifyWindow(_handler.GetWindowId());
        }

        public void MaximizeWindow()
        {
            if (_handler.GetLayout().IsMaximized)
            {
                Glfw.RestoreWindow(_handler.GetWindowId());
                _handler.GetLayout().IsMaximized = false;
                int w, h;
                Glfw.GetFramebufferSize(_handler.GetWindowId(), out w, out h);
                _handler.GetLayout().SetWidth(w);
                _handler.GetLayout().SetHeight(h);
            }
            else
            {
                Glfw.MaximizeWindow(_handler.GetWindowId());
                _handler.GetLayout().IsMaximized = true;
                int w, h;
                Glfw.GetFramebufferSize(_handler.GetWindowId(), out w, out h);
                _handler.GetLayout().SetWidth(w);
                _handler.GetLayout().SetHeight(h);
            }
        }

        private void CloseWindow(Glfw.Window glfwwnd)
        {
            _handler.GetLayout().Close();
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
                    _handler.Focused = true;
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

        private void Resize(Glfw.Window glfwwnd, int width, int height)
        {
            _tooltip.InitTimer(false);
            _handler.GetLayout().SetWidth(width);
            _handler.GetLayout().SetHeight(height);
        }

        public void SetWindowSize(int w, int h)
        {
            Glfw.SetWindowSize(_handler.GetWindowId(), w, h);
            EngineEvent.SetEvent(InputEventType.WindowResize);
        }

        private void Position(Glfw.Window glfwwnd, int xpos, int ypos)
        {
            _handler.GetPointer().SetX(xpos);
            _handler.GetPointer().SetY(ypos);

            _handler.GetLayout().SetX(xpos);
            _handler.GetLayout().SetY(ypos);
        }

        internal void SetWindowPos(int x, int y)
        {
            Glfw.SetWindowPos(_handler.GetWindowId(), x, y);
            EngineEvent.SetEvent(InputEventType.WindowMove);
        }

        private bool flag_move = false;
        private void MouseMove(Glfw.Window wnd, double xpos, double ypos)
        {
            EngineEvent.SetEvent(InputEventType.MouseMove);
            _tooltip.InitTimer(false);
            if (!flag_move)
                return;
            flag_move = false;

            if (!_handler.Focusable)
                return;

            //logic of hovers
            ptrRelease.SetX((int)xpos);
            ptrRelease.SetY((int)ypos);

            _margs.Position.SetPosition((float)xpos, (float)ypos);

            if (EngineEvent.LastEvent().HasFlag(InputEventType.MousePressed)) // жость какая-то ХЕРОТАААА!!!
            {
                if (_handler.GetLayout().IsBorderHidden && _handler.GetLayout().IsResizeble)
                {
                    int w = _handler.GetLayout().GetWidth();
                    int h = _handler.GetLayout().GetHeight();
                    int x_handler = _handler.GetPointer().GetX();
                    int y_handler = _handler.GetPointer().GetY();
                    int x_release = ptrRelease.GetX();
                    int y_release = ptrRelease.GetY();
                    int x_press = ptrPress.GetX();
                    int y_press = ptrPress.GetY();

                    if (_handler.GetLayout().GetWindow()._sides.HasFlag(ItemAlignment.Left))
                    {
                        if (!(_handler.GetLayout().GetMinWidth() == _handler.GetLayout().GetWidth() && (ptrRelease.GetX() - ptrPress.GetX()) >= 0))
                        {
                            int x5 = x_handler - x_global + (int)xpos - 5;
                            x_handler = x_global + x5;
                            w = w_global - x5;
                        }
                    }
                    if (_handler.GetLayout().GetWindow()._sides.HasFlag(ItemAlignment.Right))
                    {
                        if (!(ptrRelease.GetX() < _handler.GetLayout().GetMinWidth() && _handler.GetLayout().GetWidth() == _handler.GetLayout().GetMinWidth()))
                        {
                            w = x_release;
                        }
                        ptrPress.SetX(x_release);
                    }
                    if (_handler.GetLayout().GetWindow()._sides.HasFlag(ItemAlignment.Top))
                    {
                        if (!(_handler.GetLayout().GetMinHeight() == _handler.GetLayout().GetHeight() && (ptrRelease.GetY() - ptrPress.GetY()) >= 0))
                        {
                            int y5 = y_handler - y_global + (int)ypos - 5;
                            y_handler = y_global + y5;
                            h = h_global - y5;
                        }
                    }
                    if (_handler.GetLayout().GetWindow()._sides.HasFlag(ItemAlignment.Bottom))
                    {
                        if (!(ptrRelease.GetY() < _handler.GetLayout().GetMinHeight() && _handler.GetLayout().GetHeight() == _handler.GetLayout().GetMinHeight()))
                        {
                            h = y_release;
                        }
                        ptrPress.SetY(y_release); ;
                    }

                    if (_handler.GetLayout().GetWindow()._sides != 0 && !_handler.GetLayout().IsMaximized)
                    {
                        if (_handler.GetLayout().GetWindow()._sides.HasFlag(ItemAlignment.Left) || _handler.GetLayout().GetWindow()._sides.HasFlag(ItemAlignment.Top))
                            SetWindowPos(x_handler, y_handler);
                        SetWindowSize(w, h);
                    }
                }
                if (_handler.GetLayout().GetWindow()._sides == 0)
                {
                    int x_click = ptrClick.GetX();
                    int y_click = ptrClick.GetY();
                    Prototype draggable = IsInListHoveredItems<IDraggable>();
                    Prototype anchor = IsInListHoveredItems<WindowAnchor>();
                    if (draggable != null)
                    {
                        draggable.EventMouseDrag?.Invoke(HoveredItem, _margs);
                    }
                    else if (anchor != null && !(HoveredItem is ButtonCore) && !_handler.GetLayout().IsMaximized)
                    {
                        double x_pos, y_pos;
                        Glfw.GetCursorPos(_handler.GetWindowId(), out x_pos, out y_pos);
                        int delta_x = (int)x_pos - x_click;
                        int delta_y = (int)y_pos - y_click;
                        int x, y;
                        Glfw.GetWindowPos(_handler.GetWindowId(), out x, out y);
                        SetWindowPos(x + delta_x, y + delta_y);
                    }
                }
            }
            else
            {
                ptrPress.SetX(ptrRelease.GetX());
                ptrPress.SetY(ptrRelease.GetY());

                //check tooltip
                if (GetHoverPrototype(ptrRelease.GetX(), ptrRelease.GetY(), InputEventType.MouseMove))
                {
                    if (HoveredItem.GetToolTip() != String.Empty)
                    {
                        _tooltip.InitTimer(true);
                    }

                    _handler.SetCursorType(Glfw.CursorType.Arrow);
                    if (_handler.GetLayout().IsBorderHidden && _handler.GetLayout().IsResizeble && !_handler.GetLayout().IsMaximized)
                    {
                        //resize
                        if ((xpos < _handler.GetLayout().GetWindow().GetWidth() - 5) && (xpos > 5)
                            && (ypos < _handler.GetLayout().GetWindow().GetHeight() - 5) && ypos > 5)
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
                    Prototype popup = IsInListHoveredItems<PopUpMessage>();
                    if (popup != null)
                    {
                        (popup as PopUpMessage).HoldSelf(true);
                    }
                }
            }
        }

        private void MouseClick(Glfw.Window window, MouseButton button, InputState state, KeyMods mods)
        {
            _handler.GetLayout().GetWindow()._sides = 0;

            if (!_handler.Focusable)
                return;

            _tooltip.InitTimer(false);
            _margs.Button = button;
            _margs.State = state;
            _margs.Mods = mods;

            InputEventType m_state;
            if (state == InputState.Press)
                m_state = InputEventType.MousePressed;
            else
                m_state = InputEventType.MouseRelease;

            Queue<Prototype> tmp = new Queue<Prototype>(HoveredItems);

            if (!GetHoverPrototype(ptrRelease.GetX(), ptrRelease.GetY(), m_state))
            {
                EngineEvent.ResetAllEvents();
                EngineEvent.SetEvent(InputEventType.MouseRelease);
                return;
            }
            _handler.GetLayout().GetWindow().GetSides(ptrRelease.GetX(), ptrRelease.GetY());

            switch (state)
            {
                case InputState.Release:

                    while (tmp.Count > 0)
                    {
                        Prototype item = tmp.Dequeue();
                        if (item.IsDisabled())
                            continue;// пропустить
                        item.SetMousePressed(false);
                    }
                    if (EngineEvent.LastEvent().HasFlag(InputEventType.WindowResize)
                        || EngineEvent.LastEvent().HasFlag(InputEventType.WindowMove))
                    {
                        EngineEvent.ResetAllEvents();
                        EngineEvent.SetEvent(InputEventType.MouseRelease);
                        return;
                    }
                    if (EngineEvent.LastEvent().HasFlag(InputEventType.MouseMove))
                    {
                        float len = (float)Math.Sqrt(Math.Pow(ptrRelease.GetX() - ptrClick.GetX(), 2) + Math.Pow(ptrRelease.GetY() - ptrClick.GetY(), 2));
                        if (len > 10.0f)
                        {
                            EngineEvent.ResetAllEvents();
                            EngineEvent.SetEvent(InputEventType.MouseRelease);
                            return;
                        }
                    }

                    if (HoveredItem != null)
                    {
                        AssignActions(InputEventType.MouseRelease, _margs, false);
                        HoveredItem.SetMousePressed(false);
                    }
                    EngineEvent.ResetAllEvents();
                    EngineEvent.SetEvent(InputEventType.MouseRelease);
                    break;

                case InputState.Press:
                    Glfw.GetFramebufferSize(_handler.GetWindowId(), out w_global, out h_global);
                    x_global = _handler.GetPointer().GetX();
                    y_global = _handler.GetPointer().GetY();

                    double xpos, ypos;
                    Glfw.GetCursorPos(_handler.GetWindowId(), out xpos, out ypos);
                    ptrClick.SetX((int)xpos);
                    ptrClick.SetY((int)ypos);
                    if (HoveredItem != null)
                    {
                        HoveredItem.SetMousePressed(true);
                        AssignActions(InputEventType.MousePressed, _margs, false);

                        //Focus get
                        if (HoveredItem.IsFocusable)
                        {
                            if (FocusedItem != null)
                                FocusedItem.SetFocused(false);
                            FocusedItem = HoveredItem;
                            FocusedItem.SetFocused(true);
                        }
                    }

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

        private int w_global = 0;
        private int h_global = 0;
        private int x_global = 0;
        private int y_global = 0;

        private bool GetHoverPrototype(float xpos, float ypos, InputEventType action)
        {
            List<Prototype> queue = new List<Prototype>();
            HoveredItems.Clear();

            List<IBaseItem> layout_box_of_items = new List<IBaseItem>();
            layout_box_of_items.Add(_handler.GetLayout().GetWindow());
            layout_box_of_items.AddRange(GetInnerItems(_handler.GetLayout().GetWindow()));

            foreach (var item in ItemsLayoutBox.GetLayoutFloatItems(_handler.GetLayout().Id))
            {
                if (!item.IsVisible() || !item.IsDrawable())
                    continue;
                layout_box_of_items.Add(item);
                Prototype leaf = item as Prototype;
                if (leaf != null)
                    layout_box_of_items.AddRange(GetInnerItems(leaf));
            }

            foreach (var item in layout_box_of_items)
            {
                Prototype tmp = item as Prototype;
                if (tmp != null)
                {
                    if (!tmp.IsVisible() || !tmp.IsDrawable())
                        continue;
                    tmp.SetMouseHover(false);
                    if (tmp.GetHoverVerification(xpos, ypos))
                    {
                        queue.Add(tmp);
                    }
                    else
                    {
                        IFloating float_item = item as IFloating;
                        if (float_item != null && action == InputEventType.MousePressed)
                        {
                            if (float_item.IsOutsideClickClosable())
                            {
                                ContextMenu to_close = (item as ContextMenu);
                                if (to_close != null && to_close.CloseDependencies(_margs))
                                    float_item.Hide();
                            }
                        }
                    }
                    AssignActions(InputEventType.MouseMove, _margs, false);
                }
            }

            if (queue.Count > 0)
            {
                HoveredItem = queue.Last();
                HoveredItem.SetMouseHover(true);

                HoveredItems = queue;
                List<Prototype> tmp = new List<Prototype>(HoveredItems);
                while (tmp.Count != 0)
                {
                    Prototype item = tmp.Last();
                    if (item.Equals(HoveredItem) && HoveredItem.IsDisabled())
                        continue;//пропустить
                    item.SetMouseHover(true);
                    if (!item.GetPassEvents())
                        break;//остановить передачу событий последующим элементам
                    tmp.Remove(item);
                }
                return true;
            }
            else
                return false;
        }

        private List<IBaseItem> GetInnerItems(Prototype root)
        {
            List<IBaseItem> list = new List<IBaseItem>();

            foreach (var item in root.GetItems())
            {
                if (!item.IsVisible() || !item.IsDrawable())
                    continue;
                list.Add(item);
                Prototype leaf = item as Prototype;
                if (leaf != null)
                    list.AddRange(GetInnerItems(leaf));
            }
            return list;
        }

        private Prototype IsInListHoveredItems<T>()//idraggable adaptations
        {
            Prototype wanted = null;
            foreach (var item in HoveredItems)
            {
                if (item is T)
                {
                    wanted = item;
                    if (wanted is IFloating)
                        return wanted;
                }
            }
            return wanted;
        }

        private void MouseScroll(Glfw.Window glfwwnd, double dx, double dy)
        {
            _tooltip.InitTimer(false);

            List<Prototype> tmp = new List<Prototype>(HoveredItems);
            tmp.Reverse();
            foreach (var item in tmp)
            {
                if (!item.GetPassEvents())
                    continue;
                if (dy > 0 || dx < 0)
                    item.EventScrollUp?.Invoke(item, _margs);
                if (dy < 0 || dx > 0)
                    item.EventScrollDown?.Invoke(item, _margs);

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
                    string paste_str = Glfw.GetClipboardString(_handler.GetWindowId());
                    (FocusedItem as ITextShortcuts).PasteText(paste_str);//!!!!!!!!!!!
                }
                else if (mods == KeyMods.Control && key == KeyCode.C)
                {
                    string copy_str = (FocusedItem as ITextShortcuts).GetSelectedText();
                    Glfw.SetClipboardString(_handler.GetWindowId(), copy_str);
                }
                else if (mods == KeyMods.Control && key == KeyCode.X)
                {
                    string cut_str = (FocusedItem as ITextShortcuts).CutText();
                    Glfw.SetClipboardString(_handler.GetWindowId(), cut_str);
                }
                else
                {
                    if (action == InputState.Press)
                        AssignActions(InputEventType.KeyPress, _kargs, FocusedItem);
                    if (action == InputState.Repeat)
                        AssignActions(InputEventType.KeyPress, _kargs, FocusedItem);
                    if (action == InputState.Release)
                        AssignActions(InputEventType.KeyRelease, _kargs, FocusedItem);
                }
            } //Нехорошо это все
            else
            {
                if (action == InputState.Press)
                    AssignActions(InputEventType.KeyPress, _kargs, FocusedItem);
                if (action == InputState.Repeat)
                    AssignActions(InputEventType.KeyPress, _kargs, FocusedItem);
                if (action == InputState.Release)
                    AssignActions(InputEventType.KeyRelease, _kargs, FocusedItem);
            }
        }

        private void TextInput(Glfw.Window glfwwnd, uint codepoint, KeyMods mods)
        {
            if (!_handler.Focusable)
                return;
            _tooltip.InitTimer(false);
            _tiargs.Character = codepoint;
            _tiargs.Mods = mods;
            FocusedItem?.EventTextInput?.Invoke(FocusedItem, _tiargs);
        }

        private void AssignActions(InputEventType action, InputEventArgs args, bool only_last)
        {
            if (only_last && !HoveredItem.IsDisabled())
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
                List<Prototype> tmp = new List<Prototype>(HoveredItems);
                while (tmp.Count != 0)
                {
                    Prototype item = tmp.Last();
                    if (item.Equals(HoveredItem) && HoveredItem.IsDisabled())
                        continue;//пропустить

                    _handler.GetLayout().SetEventTask(new EventTask()
                    {
                        Item = item,
                        Action = action,
                        Args = args
                    });
                    if (!item.GetPassEvents())
                        break;//остановить передачу событий последующим элементам
                    tmp.Remove(item);
                }
            }
            _handler.GetLayout().ExecutePollActions();
        }
        private void AssignActions(InputEventType action, InputEventArgs args, Prototype sender)
        {
            if (sender.IsDisabled())
                return;

            _handler.GetLayout().SetEventTask(new EventTask()
            {
                Item = sender,
                Action = action,
                Args = args
            });
            _handler.GetLayout().ExecutePollActions();
        }

        internal float _interval = 1.0f / 60.0f;//1000 / 60;
        // internal float _interval = 1.0f / 60.0f;//1000 / 60;
        // internal int _interval = 11;//1000 / 90;
        // internal int _interval = 08;//1000 / 120;

        public void Run()
        {
            glGenVertexArrays(1, _handler.GVAO);
            glBindVertexArray(_handler.GVAO[0]);
            Focus(_handler.GetWindowId(), true);

            while (!_handler.IsClosing())
            {
                // Glfw.PollEvents();
                // Glfw.WaitEvents();
                glClearColor(
                    (float)_handler.GetLayout().GetBackground().R / 255.0f,
                    (float)_handler.GetLayout().GetBackground().G / 255.0f,
                    (float)_handler.GetLayout().GetBackground().B / 255.0f, 1.0f);

                Glfw.WaitEventsTimeout(_interval);

                _primitive.UseShader();
                Update();
                _handler.Swap();
                flag_move = true;
            }
            _primitive.DeleteShader();
            _texture.DeleteShader();
            _char.DeleteShader();
            _fxaa.DeleteShader();
            _blur.DeleteShader();

            glDeleteVertexArrays(1, _handler.GVAO);

            _handler.ClearEventsCallbacks();
            _handler.Destroy();
        }

        internal void Update()
        {
            glViewport(0, 0, _handler.GetLayout().GetWidth(), _handler.GetLayout().GetHeight());
            Render();
        }

        internal void Render()
        {
            glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
            //draw static
            DrawItems(_handler.GetLayout().GetWindow());
            //draw float
            foreach (var item in ItemsLayoutBox.GetLayout(_handler.GetLayout().Id).FloatItems)
                DrawItems(item as IBaseItem);
            //draw tooltip if needed
            DrawToolTip();
            if (!_handler.Focusable)
            {
                DrawShadePillow();
            }
        }

        private void DrawFBO() { }

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
        }

        private bool CheckOutsideBorders(IBaseItem shell)
        {
            return LazyStencil(shell);
        }

        private void StrictStencil(IBaseItem shell)
        {
            glEnable(GL_STENCIL_TEST);
            glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
            glClear(GL_STENCIL_BUFFER_BIT);
            glStencilMask(0x00);

            glStencilFunc(GL_ALWAYS, 1, 0xFF);
            glStencilMask(0xFF);

            _primitive.UseShader();
            SetStencilMask(shell.GetParent().MakeShape());

            glStencilFunc(GL_EQUAL, 1, 0xFF);

            shell.GetParent().SetConfines(
                shell.GetParent().GetX() + shell.GetParent().GetPadding().Left,
                shell.GetParent().GetX() + shell.GetParent().GetWidth() - shell.GetParent().GetPadding().Right,
                shell.GetParent().GetY() + shell.GetParent().GetPadding().Top,
                shell.GetParent().GetY() + shell.GetParent().GetHeight() - shell.GetParent().GetPadding().Bottom
            );
            SetConfines(shell);
        }

        private void SetConfines(IBaseItem shell)
        {
            int[] confines = shell.GetParent().GetConfines();
            shell.SetConfines(
                confines[0],
                confines[1],
                confines[2],
                confines[3]
            );

            Prototype root = shell as Prototype;
            if (root != null)
            {
                foreach (var item in root.GetItems())
                    SetConfines(item);
            }
        }

        private bool LazyStencil(IBaseItem shell)
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
                    return true;
                }
            }
            return false;
        }

        //Common Draw function
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
                _char.UseShader();
                DrawText(root as ITextContainer);
                _primitive.UseShader();
                if (_isStencilSet == root)
                {
                    glDisable(GL_STENCIL_TEST);
                    _isStencilSet = null;
                }
            }
            if (root is IImageItem)
            {
                _primitive.UseShader();
                DrawShell(root);
                _texture.UseShader();
                DrawImage(root as ImageItem);
                _primitive.UseShader();
                if (_isStencilSet == root)
                {
                    glDisable(GL_STENCIL_TEST);
                    _isStencilSet = null;
                }
            }
            else
            {
                DrawShell(root);

                if (root is Prototype)
                {
                    List<IBaseItem> list = new List<IBaseItem>(((Prototype)root).GetItems());
                    foreach (var child in list)
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

        private void DrawShell(IBaseItem shell, bool ignore_borders = false)
        {
            //проверка: полностью ли влезает объект в свой контейнер
            if (!ignore_borders)
                CheckOutsideBorders(shell);

            if (shell.GetBackground().A == 0)
            {
                Prototype pr = shell as Prototype;
                if (pr != null)
                    DrawBorder(pr);
                return;
            }

            //Vertex
            List<float[]> crd_array;
            crd_array = shell.MakeShape();
            if (crd_array == null)
                return;

            //shadow draw
            if (shell.IsShadowDrop())
            {
                DrawShadow(shell);
                _primitive.UseShader();
            }

            VRAMVertex store = new VRAMVertex();
            store.GenBuffers(crd_array, shell.GetBackground());
            store.Draw(GL_TRIANGLES);
            store.Clear();

            //clear array
            crd_array.Clear();

            //border draw
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
            CustomShape shadow = new CustomShape();
            shadow.SetBackground(shell.GetShadowColor());
            shadow.SetSize(shell.GetWidth(), shell.GetHeight());
            shadow.SetPosition(shell.GetX() + shell.GetShadowPos().GetX(), shell.GetY() + shell.GetShadowPos().GetY());
            shadow.SetParent(shell.GetParent());
            shadow.SetHandler(shell.GetHandler());
            shadow.SetTriangles(shell.GetTriangles());

            VRAMFramebuffer store = new VRAMFramebuffer();
            store.GenFBOTexture(_handler.GetLayout().GetWidth(), _handler.GetLayout().GetHeight());
            store.GenFBO();

            DrawShell(shadow, true);

            int res = (int)shell.GetShadowRadius();
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

            store.UnbindFBO();
            store.ClearFBO();
            DrawShadowPart(weights, res, store.Texture, new float[2] { shell.GetX() + shell.GetShadowPos().GetX(), shell.GetY() + shell.GetShadowPos().GetY() },
                new float[2] { shell.GetWidth(), shell.GetHeight() });
        }

        private void DrawShadowPart(float[] weights, int res, uint[] fbo_texture, float[] xy, float[] wh)
        {
            _blur.UseShader();
            float i_x0 = -1.0f;
            float i_y0 = 1.0f;
            float i_x1 = 1.0f;
            float i_y1 = -1.0f;

            VRAMTexture store = new VRAMTexture();
            store.GenBuffers(i_x0, i_x1, i_y0, i_y1);
            store.Bind(fbo_texture);
            store.SendUniformSample2D(_blur);
            store.SendUniform1fv(_blur, "weights", 11, weights);
            store.SendUniform2fv(_blur, "frame", new float[2] { _handler.GetLayout().GetWidth(), _handler.GetLayout().GetHeight() });
            store.SendUniform1f(_blur, "res", (res * 1f / 10));
            store.SendUniform2fv(_blur, "xy", xy);
            store.SendUniform2fv(_blur, "wh", wh);
            store.Draw();
            store.Clear();

            //Console.WriteLine(xy[0] + " xy " + xy[1]);
            //Console.WriteLine(wh[0] + " wh " + wh[1]);
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

            byte[] bb = textPrt.Texture;
            if (bb == null || bb.Length == 0)
                return;

            //проверка: полностью ли влезает объект в свой контейнер
            if (CheckOutsideBorders(text as IBaseItem))
                _char.UseShader();

            int bb_h = textPrt.HeightTexture, bb_w = textPrt.WidthTexture;
            float i_x0 = ((float)textPrt.XTextureShift / (float)_handler.GetLayout().GetWidth() * 2.0f) - 1.0f;
            float i_y0 = ((float)textPrt.YTextureShift / (float)_handler.GetLayout().GetHeight() * 2.0f - 1.0f) * (-1.0f);
            float i_x1 = (((float)textPrt.XTextureShift + (float)bb_w/* * 0.9f*/) / (float)_handler.GetLayout().GetWidth() * 2.0f) - 1.0f;
            float i_y1 = (((float)textPrt.YTextureShift + (float)bb_h) / (float)_handler.GetLayout().GetHeight() * 2.0f - 1.0f) * (-1.0f);
            float[] argb = {
                (float) text.GetForeground().R / 255.0f,
                (float) text.GetForeground().G / 255.0f,
                (float) text.GetForeground().B / 255.0f,
                (float) text.GetForeground().A / 255.0f };

            VRAMTexture store = new VRAMTexture();
            store.GenBuffers(i_x0, i_x1, i_y0, i_y1, true);
            store.GenTexture(bb_w, bb_h, bb);
            store.SendUniformSample2D(_char);
            store.SendUniform4f(_char, "rgb", argb);
            store.Draw();
            store.Clear();
        }

        void DrawPoints(IPoints item)
        {
            if (item.GetPointColor().A == 0)
                return;

            uint[] buffers = new uint[2];
            glGenBuffers(2, buffers);

            List<float[]> crd_array = item.MakeShape();
            if (crd_array == null)
                return;
            CheckOutsideBorders(item as IBaseItem);

            List<float[]> result = new List<float[]>();
            foreach (var shape in crd_array)
            {
                result.AddRange(GraphicsMathService.MoveShape(
                    item.GetShapePointer(),
                    shape[0] - item.GetPointThickness() / 2.0f,
                    shape[1] - item.GetPointThickness() / 2.0f
                    ));
            }
            result = GraphicsMathService.ToGL(result, _handler.GetLayout());

            VRAMVertex store = new VRAMVertex();
            store.GenBuffers(result, item.GetPointColor());
            store.Draw(GL_TRIANGLES);
            store.Clear();
        }

        void DrawLines(ILine item)
        {
            if (item.GetLineColor().A == 0)
                return;

            List<float[]> crd_array = GraphicsMathService.ToGL(item.MakeShape(), _handler.GetLayout());
            if (crd_array == null)
                return;
            CheckOutsideBorders(item as IBaseItem);

            VRAMVertex store = new VRAMVertex();
            store.GenBuffers(crd_array, item.GetLineColor());
            store.Draw(GL_LINE_STRIP);
            store.Clear();
        }

        void DrawImage(ImageItem image)
        {
            //проверка: полностью ли влезает объект в свой контейнер
            if (CheckOutsideBorders(image as IBaseItem))
                _texture.UseShader();

            byte[] bitmap = image.GetPixMapImage();
            if (bitmap == null)
                return;

            int w = image.GetImageWidth(), h = image.GetImageHeight();
            float i_x0 = ((float)image.GetX() / (float)_handler.GetLayout().GetWidth() * 2.0f) - 1.0f;
            float i_y0 = ((float)image.GetY() / (float)_handler.GetLayout().GetHeight() * 2.0f - 1.0f) * (-1.0f);
            float i_x1 = (((float)image.GetX() + (float)image.GetWidth()) / (float)_handler.GetLayout().GetWidth() * 2.0f) - 1.0f;
            float i_y1 = (((float)image.GetY() + (float)image.GetHeight()) / (float)_handler.GetLayout().GetHeight() * 2.0f - 1.0f) * (-1.0f);
            VRAMTexture store = new VRAMTexture();
            store.GenBuffers(i_x0, i_x1, i_y0, i_y1);
            store.GenTexture(w, h, bitmap);
            store.SendUniformSample2D(_texture);
            store.Draw();
            store.Clear();
        }

        private void DrawToolTip() //refactor
        {
            if (!_tooltip.IsVisible())
                return;

            _tooltip.SetText(HoveredItem.GetToolTip());
            _tooltip.SetWidth(
                _tooltip.GetPadding().Left +
                _tooltip.GetPadding().Right +
                _tooltip.GetTextWidth()
                );

            //проверка сверху
            if (ptrRelease.GetY() > _tooltip.GetHeight())
                _tooltip.SetY(ptrRelease.GetY() - _tooltip.GetHeight() - 2);
            else
                _tooltip.SetY(ptrRelease.GetY() + _tooltip.GetHeight() + 2);
            //проверка справа
            if (ptrRelease.GetX() - 10 + _tooltip.GetWidth() > _handler.GetLayout().GetWidth())
                _tooltip.SetX(_handler.GetLayout().GetWidth() - _tooltip.GetWidth() - 10);
            else
                _tooltip.SetX(ptrRelease.GetX() - 10);

            DrawShell(_tooltip);
            _tooltip.GetTextLine().UpdateGeometry();
            _char.UseShader();
            DrawText(_tooltip.GetTextLine());
            _primitive.UseShader();
            if (_isStencilSet == _tooltip.GetTextLine())
            {
                glDisable(GL_STENCIL_TEST);
                _isStencilSet = null;
            }
        }

        private void DrawShadePillow()
        {
            // //Vertex
            List<float[]> vertex = new List<float[]>
            {
                //X     Y		Z
                new float[] {-1.0f,  1.0f,   0.0f},
                new float[] {-1.0f,  -1.0f,  0.0f},
                new float[] {1.0f,   -1.0f,  0.0f},

                new float[] {1.0f,   -1.0f,  0.0f},
                new float[] {1.0f,   1.0f,   0.0f},
                new float[] {-1.0f,  1.0f,   0.0f}
            };
            VRAMVertex store = new VRAMVertex();
            store.GenBuffers(vertex, Color.FromArgb(150, 0, 0, 0));
            store.Draw(GL_TRIANGLES);
            store.Clear();
        }
    }
}

