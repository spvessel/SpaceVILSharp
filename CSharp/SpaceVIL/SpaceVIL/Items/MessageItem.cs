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
        private TitleBar _titleBar;
        private VerticalStack _layout;
        private Label _msg;
        private ButtonCore _ok;

        /// <summary>
        /// Constructs a MessageItem
        /// </summary>
        public MessageItem()
        {
            SetItemName("MessageItem_" + count);
            count++;

            _titleBar = new TitleBar();
            _layout = new VerticalStack();
            _msg = new Label();
            _ok = new ButtonCore("OK");

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.MessageItem)));
        }

        /// <summary>
        /// Constructs a MessageBox with message and title
        /// </summary>
        public MessageItem(String message, String title) : this()
        {
            _message = message;
            _titleBar.SetText(title);
            _msg.SetText(_message);
        }

        public void SetMessageText(String text)
        {
            _message = text;
        }

        public String GetMessageText()
        {
            return _message;
        }

        public void SetTitle(String title)
        {
            _titleBar.SetText(title);
        }
        public String GetTitle()
        {
            return _titleBar.GetText();
        }

        public override void InitElements()
        {
            //important!
            base.InitElements();
            //adding toolbar
            _titleBar.GetMaximizeButton().SetVisible(false);
            Window.AddItems(_titleBar, _layout);

            //ok
            _ok.SetItemName("OK");
            _ok.EventMouseClick += (sender, args) =>
            {
                _result = true;
                Close();
            };
            _layout.AddItems(_msg, _ok);

            _titleBar.GetCloseButton().EventMouseClick = null;
            _titleBar.GetCloseButton().EventMouseClick += (sender, args) =>
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
            if (style == null)
                return;
            base.SetStyle(style);

            Style inner_style = style.GetInnerStyle("okbutton");
            if (inner_style != null)
            {
                _ok.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("message");
            if (inner_style != null)
            {
                _msg.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("layout");
            if (inner_style != null)
            {
                _layout.SetStyle(inner_style);
            }
        }
    }
}
