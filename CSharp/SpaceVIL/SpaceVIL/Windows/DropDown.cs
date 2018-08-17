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
        public DropDown() { }

        public override void InitWindow()
        {
            Handler = new WindowLayout(this, "DropDown_" + GetCount());
            Handler.SetWindowTitle(DialogTitle);
            Handler.IsBorderHidden = true;
            Handler.IsAlwaysOnTop = true;
            Handler.IsCentered = false;
            Handler.IsResizeble = false;
        }
    }
}