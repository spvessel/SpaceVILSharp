using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL.Core;

namespace SpaceVIL
{
    /// <summary>
    /// A storage-class that provides an access to existing window layouts by name and Guid
    /// </summary>
    public static class WindowLayoutBox
    {
        static internal Dictionary<string, WindowLayout> windows_name = new Dictionary<string, WindowLayout>();
        static internal Dictionary<Guid, WindowLayout> windows_guid = new Dictionary<Guid, WindowLayout>();
        static internal List<WindowPair> current_calling_pair = new List<WindowPair>();
        static internal WindowLayout LastFocusedWindow;
        //static Object locker = new Object();

        static internal void InitWindow(WindowLayout _layout)
        {
            windows_name.Add(_layout.GetWindowName(), _layout);
            windows_guid.Add(_layout.Id, _layout);

            ItemsLayoutBox.InitLayout(_layout.Id);

            //add filling frame
            //ALL ATTRIBUTES STRICTLY NEEDED!!!
            _layout.SetWindow(new WContainer());
            _layout.GetWindow().SetHandler(_layout);
            _layout.GetWindow().SetItemName(_layout.GetWindowName());
            _layout.GetWindow().SetWidth(_layout.GetWidth());
            _layout.GetWindow().SetMinWidth(_layout.GetMinWidth());
            _layout.GetWindow().SetMaxWidth(_layout.GetMaxWidth());
            _layout.GetWindow().SetHeight(_layout.GetHeight());
            _layout.GetWindow().SetMinHeight(_layout.GetMinHeight());
            _layout.GetWindow().SetMaxHeight(_layout.GetMaxHeight());
            _layout.GetWindow().SetWidthPolicy(SizePolicy.Expand);
            _layout.GetWindow().SetHeightPolicy(SizePolicy.Expand);

            ItemsLayoutBox.AddItem(_layout, _layout.GetWindow(), LayoutType.Static);
        }
        static internal void RemoveWindow(WindowLayout _layout)
        {
            windows_name.Remove(_layout.GetWindowName());
            windows_guid.Remove(_layout.Id);
        }
        static public bool TryShow(Guid guid)
        {
            WindowLayout wnd = WindowLayoutBox.GetWindowInstance(guid);
            if (wnd != null)
            {
                wnd.Show();
                return true;
            }
            return false;
        }

        static public bool TryShow(String name)
        {
            WindowLayout wnd = WindowLayoutBox.GetWindowInstance(name);
            if (wnd != null)
            {
                wnd.Show();
                return true;
            }
            return false;
        }

        static public WindowLayout GetWindowInstance(string name)
        {
            if (windows_name.ContainsKey(name))
                return windows_name[name];
            else return null;
        }
        static public WindowLayout GetWindowInstance(Guid guid)
        {
            if (windows_guid.ContainsKey(guid))
                return windows_guid[guid];
            else return null;
        }

        static internal void AddToWindowDispatcher(WindowLayout sender_wnd)
        {
            WindowPair pair = new WindowPair();
            pair.WINDOW = sender_wnd;
            if (LastFocusedWindow == null)
            {
                pair.GUID = sender_wnd.Id;//root
                LastFocusedWindow = sender_wnd;
            }
            else
                pair.GUID = LastFocusedWindow.Id;
            current_calling_pair.Add(pair);
        }
        static internal void SetCurrentFocusedWindow(WindowLayout wnd)
        {
            LastFocusedWindow = wnd;
        }
        static internal void SetFocusedWindow(CoreWindow window)
        {
            window.GetHandler().SetFocus(true);
        }
        static internal void RemoveFromWindowDispatcher(WindowLayout sender_wnd)
        {
            List<WindowPair> pairs_to_delete = new List<WindowPair>();
            foreach (var windows_pair in current_calling_pair)
            {
                if (windows_pair.WINDOW.Equals(sender_wnd))
                {
                    pairs_to_delete.Add(windows_pair);
                }
            }

            foreach (var pairs in pairs_to_delete)
            {
                current_calling_pair.Remove(pairs);
            }

            pairs_to_delete = null;
        }
        static public string[] GetListOfWindows() => windows_name.Keys.ToArray();
        static public void PrintStoredWindows()
        {
            foreach (var item in windows_name.Keys.ToArray())
            {
                Console.WriteLine(item);
            }
        }
    }
}
