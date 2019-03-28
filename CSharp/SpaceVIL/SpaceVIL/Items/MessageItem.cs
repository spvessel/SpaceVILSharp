using System;
using System.Collections.Generic;
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

        /**
         * Get MessageItem boolean result
         */
        public bool GetResult()
        {
            return _result;
        }

        Dictionary<ButtonCore, Int32> _userMap = new Dictionary<ButtonCore, Int32>();
        ButtonCore _userButtonResult = null;

        public int GetUserButtonResult()
        {
            if (_userMap.ContainsKey(_userButtonResult))
                return _userMap[_userButtonResult];
            return -1;
        }

        private TitleBar _titleBar;
        private Frame _msg_layout;
        private Label _msg;
        private ButtonCore _ok;

        public ButtonCore GetOkButton()
        {
            return _ok;
        }

        private ButtonCore _cancel;

        public ButtonCore GetCancelButton()
        {
            return _cancel;
        }

        private HorizontalStack _toolbar;
        private HorizontalStack _userbar;

        /**
         * Constructs a MessageItem
         */
        public MessageItem()
        {
            SetItemName("MessageItem_" + count);
            count++;

            _titleBar = new TitleBar();
            _msg_layout = new Frame();
            _msg = new Label();
            _ok = GetButton("OK");
            _cancel = GetButton("Cancel");

            _toolbar = new HorizontalStack();
            _userbar = new HorizontalStack();

            Window.IsLocked = true;

            EventKeyPress += (sender, args) =>
            {
                if (args.Key == KeyCode.Escape)
                    Close();
            };

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.MessageItem)));
        }

        /**
         * Constructs a MessageItem with message and title
         */
        public MessageItem(String message, String title) : this()
        {
            _titleBar.SetText(title);
            _msg.SetText(message);
        }

        /**
         * MessageItem text
         */
        public void SetMessageText(String text)
        {
            _msg.SetText(text);
        }

        public String GetMessageText()
        {
            return _msg.GetText();
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
            // important!
            base.InitElements();

            // Adding toolbar
            _titleBar.GetMaximizeButton().SetVisible(false);

            int w_global = 0;

            // toolbar size
            int w = _ok.GetWidth() + _ok.GetMargin().Left + _ok.GetMargin().Right;
            if (_cancel.IsVisible())
                w = w * 2 + 10;
            _toolbar.SetSize(w, _ok.GetHeight() + _ok.GetMargin().Top + _ok.GetMargin().Bottom);
            w_global += w;

            bool isEmpty = true;
            w = _userbar.GetPadding().Left + _userbar.GetPadding().Right;
            if (_queue.Count > 0)
            {
                isEmpty = false;
                foreach (ButtonCore btn in _queue)
                {
                    // btn.SetStyle(_btnStyle);
                    w += btn.GetWidth() + _userbar.GetSpacing().Horizontal;
                }
                w -= _userbar.GetSpacing().Horizontal;
            }
            _userbar.SetSize(w, _toolbar.GetHeight());
            w_global += (w + _toolbar.GetMargin().Left + _toolbar.GetMargin().Right + 50);
            w_global += (_userbar.GetMargin().Left + _userbar.GetMargin().Right);

            Window.SetMinWidth(w_global);
            if (Window.GetWidth() < w_global)
                Window.SetWidth(w_global);
            int w_text = _msg.GetTextWidth() + _msg_layout.GetMargin().Left + _msg_layout.GetMargin().Right
            + _msg_layout.GetPadding().Left + _msg_layout.GetPadding().Right + _msg.GetMargin().Left
            + _msg.GetMargin().Right + _msg.GetPadding().Left + _msg.GetPadding().Right + 10;
            if (Window.GetWidth() < w_text)
                Window.SetWidth(w_text);
            Window.AddItems(_titleBar, _msg_layout);
            Window.Update(GeometryEventType.ResizeWidth, 0);

            if (!isEmpty)
            {
                _toolbar.SetAlignment(ItemAlignment.Right, ItemAlignment.Bottom);
                int right = _toolbar.GetWidth() + _toolbar.GetMargin().Left + _toolbar.GetMargin().Right + 10;
                _userbar.SetMargin(0, 0, right / 2, 0);
                _msg_layout.AddItems(_msg, _userbar, _toolbar);
            }
            else
            {
                _msg_layout.AddItems(_msg, _toolbar);
            }

            // queue
            if (!isEmpty)
            {
                foreach (ButtonCore btn in _queue)
                {
                    _userbar.AddItem(btn);
                }
            }
            _toolbar.AddItems(_ok, _cancel);
            _userMap.Add(_ok, 1);
            _userMap.Add(_titleBar.GetCloseButton(), 0);
            _userMap.Add(_cancel, -1);
            // buttons
            _ok.SetItemName("OK");
            _ok.EventMouseClick += (sender, args) =>
            {
                _userButtonResult = _ok;
                _result = true;
                Close();
            };
            _cancel.SetItemName("Cancel");
            _cancel.EventMouseClick += (sender, args) =>
            {
                _userButtonResult = _cancel;
                Close();
            };

            _titleBar.GetCloseButton().EventMouseClick = null;
            _titleBar.GetCloseButton().EventMouseClick += (sender, args) =>
            {
                _userButtonResult = _titleBar.GetCloseButton();
                Close();
            };
        }


        public override void Show(WindowLayout handler)
        {
            _result = false;
            base.Show(handler);
        }


        public override void Close()
        {
            OnCloseDialog?.Invoke();
            base.Close();
        }

        private List<ButtonCore> _queue = new List<ButtonCore>();

        public void AddUserButton(ButtonCore button, int id)
        {
            if (id == -1 || id == 0 || id == 1)
                return;

            button.SetStyle(_btnStyle);
            _userMap.Add(button, id);
            _queue.Add(button);
            button.EventMouseClick += (sender, args) =>
            {
                _userButtonResult = button;
                Close();
            };
        }

        private ButtonCore GetButton(String name)
        {
            ButtonCore btn = new ButtonCore(name);
            return btn;
        }

        private Style _btnStyle = null;

        public Style GetDialogButtonStyle()
        {
            return _btnStyle;
        }

        public void SetDialogButtonStyle(Style style)
        {
            _btnStyle = style;
        }

        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);

            Style inner_style = style.GetInnerStyle("message");
            if (inner_style != null)
            {
                _msg.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("layout");
            if (inner_style != null)
            {
                _msg_layout.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("toolbar");
            if (inner_style != null)
            {
                _toolbar.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("userbar");
            if (inner_style != null)
            {
                _userbar.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("button");
            if (inner_style != null)
            {
                _btnStyle = inner_style.Clone();
                _ok.SetStyle(inner_style);
                _cancel.SetStyle(inner_style);
            }
        }
    }
}
