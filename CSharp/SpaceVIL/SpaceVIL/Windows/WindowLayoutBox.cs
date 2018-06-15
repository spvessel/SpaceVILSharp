using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    static class WindowLayoutBox
    {
        static public Dictionary<string, WindowLayout> windows = new Dictionary<string, WindowLayout>();
        static public int ActiveWindow;
        static public void InitWindow(WindowLayout _layout)
        {
            windows.Add(_layout.GetWindowName(), _layout);
            ItemsLayoutBox.InitLayout(_layout.Id);

            //add filling frame
            _layout.Window = new Frame();
            _layout.Window.SetHandler(_layout);
            _layout.Window.SetItemName(_layout.GetWindowName());
            _layout.Window.SetWidth(_layout.GetWidth());
            _layout.Window.SetHeight(_layout.GetHeight());
            _layout.Window.SetX(0);
            _layout.Window.SetY(0);
            _layout.Window.SetWidthPolicy(SizePolicy.Expand);
            _layout.Window.SetHeightPolicy(SizePolicy.Expand);
            ItemsLayoutBox.AddItem(_layout, _layout.Window);
        }
        static public WindowLayout GetWindowInstance(string name)
        {
            if (windows.ContainsKey(name))
                return windows[name];
            else return null;
        }
        static public void WindowDispatcher(int sender_id)
        {
            ActiveWindow = sender_id;
            foreach (var wnd in windows)
            {
                if (wnd.Value.IsOutsideClickClosable && (wnd.Value.Id != ActiveWindow))
                    wnd.Value.Close();
            }
        }
        static public string[] GetListOfWindows() => windows.Keys.ToArray();
        static public void PrintStoredWindows()
        {
            foreach (var item in windows.Keys.ToArray())
            {
                Console.WriteLine(item);
            }
        }
    }
}
