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
        public String GetMessageText()
        {
            return _message;
        }
        public void SetMessageText(String text)
        {
            _message = text;
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
            msg.SetText(GetMessageText());
        }

        /// <summary>
        /// Initialize MessageBox window
        /// </summary>
        public override void InitWindow()
        {
            SetParameters("MessageBox_" + GetCount(), "MessageBox_" + GetCount());
            //window's attr
            SetWidth(300);
            SetMinWidth(300);
            SetHeight(150);
            SetMinHeight(150);
            SetPadding(2, 2, 2, 2);
            SetBackground(Color.FromArgb(255, 45, 45, 45));
            IsBorderHidden = true;
            IsDialog = true;
            IsAlwaysOnTop = true;
            IsCentered = true;
            AddItem(titleBar);

            VerticalStack layout = new VerticalStack();
            layout.SetAlignment(ItemAlignment.Top | ItemAlignment.HCenter);
            layout.SetMargin(0, 30, 0, 0);
            layout.SetPadding(6, 6, 6, 6);
            layout.SetSpacing(vertical: 10);
            layout.SetBackground(255, 255, 255, 20);

            //adding toolbar
            AddItem(layout);

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
                Close();
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