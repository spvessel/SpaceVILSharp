using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    class MessageBox
    {
        private static int count = 0;
        WindowLayout wnd_handler;
        bool _result = false;
        public MessageBox(String message, String title)
        {
            InitWindow(message, title);
        }
        private void InitWindow(String message, String title)
        {
            wnd_handler = new WindowLayout(title);
            //window's attr
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

            //DragAnchor
            TitleBar titleBar = new TitleBar(title);
            wnd_handler.AddItem(titleBar);

            VerticalStack layout = new VerticalStack();
            layout.SetAlignment(ItemAlignment.Top | ItemAlignment.HCenter);
            layout.SetMargin(0, 30, 0, 0);
            layout.SetPadding(6, 6, 6, 6);
            layout.SetSpacing(vertical: 10);
            layout.SetBackground(255, 255, 255, 20);

            //adding toolbar
            wnd_handler.AddItem(layout);

            //message
            Label msg = new Label(message);
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
                _result = true;
                wnd_handler.Close();
            };
            layout.AddItems(msg, ok);

            //show
            wnd_handler.IsDialog = true;
            wnd_handler.Show();
        }
        public bool Result()
        {
            return _result;
        }
    }
}