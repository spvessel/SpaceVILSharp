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
        bool _result = false;
        public bool Result()
        {
            return _result;
        }

        String _message = String.Empty;
        public String MessageBoxText
        {
            get
            {
                return _message;
            }
            set
            {
                _message = value;
            }
        }

        TitleBar titleBar = new TitleBar();
        Label msg = new Label();

        public MessageBox(String m = "", String t = "")
        {
            _message = m;
            DialogTitle = t;

            //ref
            titleBar.SetText(DialogTitle);
            msg.SetText(MessageBoxText);
        }

        public override void InitWindow()
        {
            Handler = new WindowLayout(this, "MessageBox_" + GetCount());
            //window's attr
            Handler.SetWidth(300);
            Handler.SetMinWidth(300);
            Handler.SetHeight(150);
            Handler.SetMinHeight(150);
            Handler.SetPadding(2, 2, 2, 2);
            Handler.SetBackground(Color.FromArgb(255, 45, 45, 45));
            Handler.IsBorderHidden = true;
            Handler.IsDialog = true;
            Handler.IsAlwaysOnTop = true;

            //DragAnchor
            titleBar.SetPadding(0, 0, 10, 0);
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
            ok.SetSize(100, 30);
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
                Handler.Close();
            };
            layout.AddItems(msg, ok);
        }
    }
}