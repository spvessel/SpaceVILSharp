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
        static internal Dictionary<Guid, CoreWindow> windowsGuid = new Dictionary<Guid, CoreWindow>();
        static internal Dictionary<CoreWindow, CoreWindow> pairs = new Dictionary<CoreWindow, CoreWindow>();
        static internal CoreWindow lastFocusedWindow;

        static internal void InitWindow(CoreWindow _layout)
        {
            if (windowsGuid.ContainsKey(_layout.GetWindowGuid()))
                return;

            windows.Add(_layout);
            windowsGuid.Add(_layout.GetWindowGuid(), _layout);

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
            windowsGuid.Remove(_layout.GetWindowGuid());
            _layout.Release();
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
            if (windowsGuid.ContainsKey(guid))
                return windowsGuid[guid];
            else return null;
        }

        static internal void CreateWindowsPair(CoreWindow wnd)
        {
            AddToWindowDispatcher(wnd);
        }

        static internal void DestroyWindowsPair(CoreWindow wnd)
        {
            RemoveFromWindowDispatcher(wnd);
        }

        static internal CoreWindow GetWindowPair(CoreWindow wnd)
        {
            if (pairs.ContainsKey(wnd))
                return pairs[wnd];
            else
                return null;
        }

        static void AddToWindowDispatcher(CoreWindow wnd)
        {
            if (!pairs.ContainsKey(wnd))
                pairs.Add(wnd, lastFocusedWindow);
        }

        static internal void SetCurrentFocusedWindow(CoreWindow wnd)
        {
            lastFocusedWindow = wnd;
        }

        public static CoreWindow GetCurrentFocusedWindow()
        {
            return lastFocusedWindow;
        }

        static internal void SetFocusedWindow(CoreWindow window)
        {
            window.SetFocus(true);
        }

        static internal void RemoveFromWindowDispatcher(CoreWindow wnd)
        {
            if (pairs.ContainsKey(wnd))
                pairs.Remove(wnd);
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
