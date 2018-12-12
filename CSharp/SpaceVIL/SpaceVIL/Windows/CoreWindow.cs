using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    public abstract class CoreWindow
    {
        private static int count = 0;
        private Guid windowGuid;

        /// <summary>
        /// Constructs a CoreWindow
        /// </summary>
        public CoreWindow()
        {
            windowGuid = Guid.NewGuid();
            count++;
        }

        private WindowLayout wnd_handler;

        /// <summary>
        /// Parent item for the CoreWindow
        /// </summary>
        public WindowLayout GetHandler()
        {
            return wnd_handler;
        }

        /// <summary>
        /// Parent item for the CoreWindow
        /// </summary>
        public void SetHandler(WindowLayout value)
        {
            wnd_handler = value;
        }

        /// <summary>
        /// Show the CoreWindow
        /// </summary>
        public virtual void Show()
        {
            wnd_handler.Show();
        }

        /// <summary>
        /// Close the CoreWindow
        /// </summary>
        public virtual void Close()
        {
            wnd_handler.Close();
        }

        /// <summary>
        /// Initialize the window
        /// </summary>
        abstract public void InitWindow();

        /// <returns> count of all CoreWindows </returns>
        public int GetCount() { return count; }

        /// <returns> CoreWindow unique ID </returns>
        public Guid GetWindowGuid() { return windowGuid; }
    }
}
