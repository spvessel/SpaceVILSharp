using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    abstract public class DropDown : DialogWindow
    {
        /// <summary>
        /// Constructs a DropDown
        /// </summary>
        public DropDown() { }

        public override void InitWindow()
        {
            WindowLayout Handler = new WindowLayout(this, "DropDown_" + GetCount(), "DropDown_" + GetCount());
            SetHandler(Handler);
            Handler.SetWindowTitle(DialogTitle);
            Handler.IsBorderHidden = true;
            Handler.IsAlwaysOnTop = true;
            Handler.IsCentered = false;
            Handler.IsResizeble = false;
        }
    }
}