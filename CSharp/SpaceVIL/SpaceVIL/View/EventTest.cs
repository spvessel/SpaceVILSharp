using System;
using System.Drawing;
using SpaceVIL;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace View
{
    public class EventTest : ActiveWindow
    {

        private BlankItem GetBlankItem(String name)
        {
            BlankItem blank = new BlankItem();
            blank.SetItemName(name);
            blank.SetStyle(Style.GetFrameStyle());
            blank.SetMargin(50, 50, 50, 50);
            blank.SetBorder(new Border(Color.FromArgb(0, 162, 232), new CornerRadius(10), 3));
            blank.SetBackground(255, 255, 255, 50);
            blank.EventMouseClick += (sender, args) =>
            {
                Console.WriteLine(blank.GetItemName() + " EventMouseClick");
            };
            blank.EventMousePress += (sender, args) =>
            {
                Console.WriteLine(blank.GetItemName() + " EventMousePress");
            };
            // blank.eventMouseDoubleClick += (sender, args) => {
            //     Console.WriteLine(blank.GetItemName() + " EventMouseDoubleClick");
            // });
            blank.EventKeyPress += (sender, args) =>
            {
                Console.WriteLine(blank.GetItemName() + " EventKeyPress");
            };
            blank.EventKeyRelease += (sender, args) =>
            {
                Console.WriteLine(blank.GetItemName() + " EventKeyRelease");
            };
            // blank.eventMouseHover += (sender, args) => {
            //     Console.WriteLine(blank.GetItemName() + " EventMouseHover");
            // });
            return blank;
        }

        public override void InitWindow()
        {

            IsBorderHidden = true;
            SetSize(400, 400);
            SetWindowName("EventTest");
            SetWindowTitle("EventTest");
            SetMinSize(400, 400);
            IsCentered = true;
            SetAntiAliasingQuality(MSAA.MSAA8x);


            TitleBar title = new TitleBar("EventTest");
            title.SetShadow(5, 0, 3, Color.FromArgb(150, 0, 0, 0));
            AddItem(title);

            Frame cc = new Frame();
            cc.SetMargin(0, title.GetHeight() + 10, 0, 0);
            cc.SetBackground(50, 50, 50);
            AddItem(cc);

            BlankItem b1 = GetBlankItem("Blank1");
            BlankItem b2 = GetBlankItem("Blank2");
            BlankItem b3 = GetBlankItem("Blank3");

            cc.AddItem(b1);
            b1.AddItem(b2);
            b2.AddItem(b3);

            b3.SetFocus();

            foreach (var i in b3.GetBlockedEvents())
            {
                Console.WriteLine(i);
            }
        }
    }
}