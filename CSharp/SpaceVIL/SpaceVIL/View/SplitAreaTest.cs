using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL;

namespace View
{
    class SplitAreaTest : ActiveWindow
    {
        override public void InitWindow() {
            Handler = new WindowLayout(this, name: nameof(SplitAreaTest));
            Handler.SetWidth(500);
            Handler.SetMinWidth(500);
            Handler.SetHeight(500);
            Handler.SetMinHeight(500);
            Handler.SetWindowTitle(nameof(SplitAreaTest));
            Handler.SetPadding(2, 2, 2, 2);
            Handler.SetBackground(Color.FromArgb(255, 45, 45, 45));
            Handler.IsBorderHidden = true;

            //DragAnchor
            TitleBar title = new TitleBar(nameof(SplitAreaTest));
            Handler.AddItem(title);

            //SplitArea
            SplitArea splitArea = new SplitArea();
            splitArea.SetMargin(0,30,0,0);
            Handler.AddItem(splitArea);
        }
    }
}
