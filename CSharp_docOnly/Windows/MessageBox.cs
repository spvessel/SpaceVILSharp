using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class MessageBox : DialogWindow
    {
        bool _result = false;

        /// <summary>
        /// Get MessageBox boolean result
        /// </summary>
        public bool Result()
        {
            return _result;
        }

        String _message = String.Empty;

        /// <summary>
        /// MessageBox text
        /// </summary>
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

        /// <summary>
        /// Constructs a MessageBox with message and title
        /// </summary>
        public MessageBox(String m = "", String t = "")
        {

            _message = m;
            DialogTitle = t;

            //ref
            //DragAnchor
            titleBar.SetText(DialogTitle);
            titleBar.SetPadding(0, 0, 10, 0);
            msg.SetText(MessageBoxText);
        }

        /// <summary>
        /// Initialize MessageBox window
        /// </summary>
        public override void InitWindow()
        {
            WindowLayout Handler = new WindowLayout("MessageBox_" + GetCount(), "MessageBox_" + GetCount());
            SetHandler(Handler);
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
            // msg.SetFont(new Font(new FontFamily("Courier New"), 14, FontStyle.Regular));
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
            ok.SetBorderRadius(new CornerRadius(6));
            ok.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            ok.EventMouseClick += (sender, args) =>
            {
                _result = true;
                Handler.Close();
            };
            layout.AddItems(msg, ok);
        }

        /// <summary>
        /// Show MessageBox
        /// </summary>
        public override void Show()
        {
            _result = false;
            base.Show();
        }
    }
}