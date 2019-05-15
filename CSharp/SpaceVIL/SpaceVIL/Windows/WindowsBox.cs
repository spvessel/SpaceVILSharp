using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL.Core;

namespace SpaceVIL
{
    internal class WindowPair
    {
        public Guid GUID;
        public CoreWindow WINDOW;
    }

    /// <summary>
    /// A storage-class that provides an access to existing window layouts by name and Guid
    /// </summary>
    public static class WindowsBox
    {
        static internal HashSet<CoreWindow> windows = new HashSet<CoreWindow>();
        static internal Dictionary<Guid, CoreWindow> windows_guid = new Dictionary<Guid, CoreWindow>();
        static internal List<WindowPair> current_calling_pair = new List<WindowPair>();
        static internal CoreWindow LastFocusedWindow;

        static internal void InitWindow(CoreWindow _layout)
        {
            if (windows_guid.ContainsKey(_layout.GetWindowGuid()))
                return;

            windows.Add(_layout);
            windows_guid.Add(_layout.GetWindowGuid(), _layout);

            ItemsLayoutBox.InitLayout(_layout.GetWindowGuid());

            //add filling frame
            //ALL ATTRIBUTES STRICTLY NEEDED!!!
            WContainer container = new WContainer();
            container.SetHandler(_layout);
            container.SetItemName(_layout.GetWindowName());
            container.SetWidth(_layout.GetWidth());
            container.SetMinWidth(_layout.GetMinWidth());
            container.SetMaxWidth(_layout.GetMaxWidth());
            container.SetHeight(_layout.GetHeight());
            container.SetMinHeight(_layout.GetMinHeight());
            container.SetMaxHeight(_layout.GetMaxHeight());
            container.SetWidthPolicy(SizePolicy.Expand);
            container.SetHeightPolicy(SizePolicy.Expand);

            _layout.SetWindow(container);

            ItemsLayoutBox.AddItem(_layout, container, LayoutType.Static);
        }
        static internal void RemoveWindow(CoreWindow _layout)
        {
            windows.Remove(_layout);
            windows_guid.Remove(_layout.GetWindowGuid());
            if (_is_main_running == _layout)
                _is_main_running = null;
            _layout.Release();
        }

        private static CoreWindow _is_main_running = null;

        internal static bool IsAnyWindowRunning()
        {
            if (_is_main_running != null)
                return true;
            return false;
        }

        internal static void SetWindowRunning(CoreWindow window)
        {
            _is_main_running = window;
        }

        static public bool TryShow(Guid guid)
        {
            CoreWindow wnd = WindowsBox.GetWindowInstance(guid);
            if (wnd != null)
            {
                wnd.Show();
                return true;
            }
            return false;
        }

        static public bool TryShow(String name)
        {
            CoreWindow wnd = WindowsBox.GetWindowInstance(name);
            if (wnd != null)
            {
                wnd.Show();
                return true;
            }
            return false;
        }

        static public CoreWindow GetWindowInstance(string name)
        {
            foreach (CoreWindow wnd in windows)
            {
                if (wnd.GetWindowName().Equals(name))
                    return wnd;
            }
            return null;
        }
        static public CoreWindow GetWindowInstance(Guid guid)
        {
            if (windows_guid.ContainsKey(guid))
                return windows_guid[guid];
            else return null;
        }

        static internal void AddToWindowDispatcher(CoreWindow sender_wnd)
        {
            WindowPair pair = new WindowPair();
            pair.WINDOW = sender_wnd;
            if (LastFocusedWindow == null)
            {
                pair.GUID = sender_wnd.GetWindowGuid();//root
                LastFocusedWindow = sender_wnd;
            }
            else
                pair.GUID = LastFocusedWindow.GetWindowGuid();
            current_calling_pair.Add(pair);
        }
        static internal void SetCurrentFocusedWindow(CoreWindow wnd)
        {
            LastFocusedWindow = wnd;
        }
        public static CoreWindow GetCurrentFocusedWindow()
        {
            return LastFocusedWindow;
        }
        static internal void SetFocusedWindow(CoreWindow window)
        {
            window.GetLayout().SetFocus(true);
        }
        static internal void RemoveFromWindowDispatcher(CoreWindow sender_wnd)
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
        static public List<String> GetWindowsList()
        {
            List<String> result = new List<String>();

            foreach (CoreWindow wl in windows)
            {
                result.Add(wl.GetWindowName());
            }

            return result;
        }
        static public void PrintStoredWindows()
        {
            foreach (var item in GetWindowsList())
            {
                Console.WriteLine(item);
            }
        }
    }
}
