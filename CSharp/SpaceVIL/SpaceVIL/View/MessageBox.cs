using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    class MessageBox : DialogWindow
    {
<<<<<<< HEAD
        public MessageBox(String m, String t) : base(m, t) {

=======
        private static int count = 0;
        WindowLayout wnd_handler;
        bool _result = false;
        public MessageBox(String message, String title)
        {
            InitWindow(message, title);
>>>>>>> 4361347998b9a2e2441593dbbfa22c805c2fc637
        }

        override internal void InitWindow()
        {
           Handler = new WindowLayout();
            //window's attr
<<<<<<< HEAD
           Handler.SetWindowName("MessageBox_" + GetCount());
           Handler.SetWindowTitle(DialogTitle);
           Handler.SetWidth(400);
           Handler.SetMinWidth(400);
           Handler.SetHeight(250);
           Handler.SetMinHeight(250);
           Handler.SetPadding(2, 2, 2, 2);
           Handler.SetBackground(Color.FromArgb(255, 45, 45, 45));
           Handler.IsBorderHidden = true;
           Handler.IsAlwaysOnTop = true;
=======
            wnd_handler.SetWindowName("MessageBox_" + count);
            count++;
            wnd_handler.SetWindowTitle(title);
            wnd_handler.SetWidth(400);
            wnd_handler.SetMinWidth(400);
            wnd_handler.SetHeight(250);
            wnd_handler.SetMinHeight(250);
            wnd_handler.SetPadding(2, 2, 2, 2);
            wnd_handler.SetBackground(Color.FromArgb(255, 45, 45, 45));
            wnd_handler.IsBorderHidden = true;
            wnd_handler.IsAlwaysOnTop = true;
>>>>>>> 4361347998b9a2e2441593dbbfa22c805c2fc637

            //DragAnchor
            TitleBar titleBar = new TitleBar(DialogTitle);
            Handler.AddItem(titleBar);

            VerticalStack layout = new VerticalStack();
            layout.SetAlignment(ItemAlignment.Top | ItemAlignment.HCenter);
            layout.SetMargin(0, 30, 0, 0);
            layout.SetPadding(6, 6, 6, 6);
            layout.SetSpacing(vertical: 10);
            layout.SetBackground(255, 255, 255, 20);

            //adding toolbar
            Handler.AddItem(layout);

            //message
            Label msg = new Label(DialogMessage);
            msg.SetFont(new Font(new FontFamily("Courier New"), 14, FontStyle.Regular));
            msg.SetForeground(Color.FromArgb(255, 210, 210, 210));
            msg.SetAlignment(ItemAlignment.VCenter | ItemAlignment.HCenter);
            msg.SetTextAlignment(ItemAlignment.VCenter | ItemAlignment.HCenter);
            msg.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);

            //ok
            ButtonCore ok = new ButtonCore("OK");
            ok.SetBackground(100, 255, 150);
            ok.SetForeground(Color.Black);
            ok.SetItemName("OK");
            ok.SetSize(100,30);
            ok.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            ok.SetAlignment(ItemAlignment.HCenter | ItemAlignment.Bottom);
            ok.Border.Radius = 6;
            ok.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            ok.EventMouseClick += (sender) =>
            {
                DialogResult = true;
                Handler.Close();
            };
            layout.AddItems(msg, ok);

            //show
            Handler.IsDialog = true;
            Handler.Show();
        }
    }
}