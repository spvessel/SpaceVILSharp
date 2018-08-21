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
        override public void InitWindow()
        {
            Handler = new WindowLayout(this, nameof(SplitAreaTest), nameof(SplitAreaTest), 500, 500, true);
            Handler.SetMinWidth(50);
            Handler.SetMinHeight(50);
            Handler.SetPadding(2, 2, 2, 2);
            Handler.SetBackground(Color.FromArgb(255, 45, 45, 45));

            //DragAnchor
            TitleBar title = new TitleBar(nameof(SplitAreaTest));
            Handler.AddItem(title);

            //SplitArea
            VerticalSplitArea splitArea = new VerticalSplitArea();
            splitArea.SetMargin(0, 30, 0, 0);
            Handler.AddItem(splitArea);
        }
    }
}
