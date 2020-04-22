using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    /// <summary>
    /// DialogWindow is an abstract class for modal window instances.
    /// <para/>DialogWindow extends CoreWindow class. 
    /// CoreWindow is an abstract class containing an implementation of common functionality for a window.
    /// </summary>
    public abstract class DialogWindow : CoreWindow
    {
        /// <summary>
        /// Constructs a DialogWindow
        /// </summary>
        public DialogWindow() : base()
        {
            IsDialog = true;
            IsCentered = true;
            IsAlwaysOnTop = true;
        }

        /// <summary>
        /// Show the DialogWindow.
        /// </summary>
        public override void Show()
        {
            InitWindow();
            base.Show();
        }
    }
}
