using System;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class MessageItem : DialogItem
    {
        static int count = 0;

        private bool _result = false;
        public bool GetResult()
        {
            return _result;
        }
        private String _message = String.Empty;
        private String _title = String.Empty;
        private TitleBar titleBar = new TitleBar();
        private Label msg = new Label();

        /// <summary>
        /// Constructs a MessageItem
        /// </summary>
        public MessageItem() : base()
        {
            SetItemName("MessageItem_" + count);
            count++;
        }

        /// <summary>
        /// Constructs a MessageBox with message and title
        /// </summary>
        public MessageItem(String m = "", String t = "") : this()
        {
            _message = m;
            _title = t;

            titleBar.SetText(_title);
            msg.SetText(_message);
        }

        public override void InitElements()
        {
            //important!
            base.InitElements();

            VerticalStack layout = new VerticalStack();
            layout.SetAlignment(ItemAlignment.Top | ItemAlignment.HCenter);
            layout.SetMargin(0, 30, 0, 0);
            layout.SetPadding(6, 6, 6, 6);
            layout.SetSpacing(vertical: 10);
            layout.SetBackground(255, 255, 255, 20);

            //adding toolbar
            titleBar.GetMaximizeButton().SetVisible(false);
            Window.AddItems(titleBar, layout);


            //message
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

            titleBar.GetCloseButton().EventMouseClick = null;
            titleBar.GetCloseButton().EventMouseClick += (sender, args) =>
            {
                Close();
            };
        }

        WindowLayout _handler = null;
        /// <summary>
        /// Show MessageBox
        /// </summary>
        public override void Show(WindowLayout handler)
        {
            _result = false;
            base.Show(handler);
        }

        public override void Close()
        {
            if (OnCloseDialog != null)
                OnCloseDialog.Invoke();

            base.Close();
        }

        public override void SetStyle(Style style)
        {

        }
    }
}
