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
            //ALL ATTRIBUTES STRICTLY NEEDED!!!
            _layout.SetWindow(new Frame());
            _layout.GetWindow().SetHandler(_layout);
            _layout.GetWindow().SetItemName(_layout.GetWindowName());
            _layout.GetWindow().SetWidth(_layout.GetWidth());
            _layout.GetWindow().SetHeight(_layout.GetHeight());
            _layout.GetWindow().SetWidthPolicy(SizePolicy.Expand);
            _layout.GetWindow().SetHeightPolicy(SizePolicy.Expand);

            ItemsLayoutBox.AddItem(_layout, _layout.GetWindow());
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
