using System;
using System.Collections.Generic;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using SpaceVIL.Common;

namespace SpaceVIL
{
    /// <summary>
    /// MessageBox - a modal window for displaying any messages with ability to reply to them. 
    /// It supports custom toolbar to make user's reply flexible.
    /// </summary>
    public class MessageBox : DialogWindow
    {
        /// <summary>
        /// An event to describe the actions that must be performed after the window is closed.
        /// <para/> Event type: SpaceVIL.EventCommonMethod.
        /// <para/> Function arguments: none.
        /// </summary>
        public EventCommonMethod OnCloseDialog;

        static int count = 0;

        private bool _result = false;

        /// <summary>
        /// Get MessageBox result.
        /// <para/> Default: False
        /// </summary>
        /// <returns>True: OK button was clicked. False: Close button or Cancel button was clicked.</returns>
        public bool GetResult()
        {
            return _result;
        }

        private Dictionary<ButtonCore, Int32> _userMap = new Dictionary<ButtonCore, Int32>();
        private ButtonCore _userButtonResult = null;
        /// <summary>
        /// Getting result from custom toolbar (if it was created).
        /// </summary>
        /// <returns>Id of clicked button (see AddUserButton(ButtonCore button, int id)).</returns>
        public int GetUserButtonResult()
        {
            if (_userButtonResult != null && _userMap.ContainsKey(_userButtonResult))
                return _userMap[_userButtonResult];
            return -1;
        }

        private TitleBar _titleBar;
        private Frame _msg_layout;
        private Label _msg;
        private ButtonCore _ok;
        /// <summary>
        /// Getting OK button for appearance customizing or assigning new actions.
        /// </summary>
        /// <returns>MessageBox's OK button as SpaceVIL.ButtonCore.</returns>
        public ButtonCore GetOkButton()
        {
            return _ok;
        }

        private ButtonCore _cancel;
        /// <summary>
        /// Getting CANCEL button for appearance customizing or assigning new actions.
        /// </summary>
        /// <returns>MessageBox's CANCEL button as SpaceVIL.ButtonCore.</returns>
        public ButtonCore GetCancelButton()
        {
            return _cancel;
        }

        private HorizontalStack _toolbar;
        private HorizontalStack _userbar;
        /// <summary>
        /// Default MessageBox constructor.
        /// </summary>
        public MessageBox()
        {
            SetWindowName("MessageBox_" + count);
            count++;

            _titleBar = new TitleBar();
            _msg_layout = new Frame();
            _msg = new Label();
            _ok = GetButton("OK");
            _cancel = GetButton("Cancel");

            _toolbar = new HorizontalStack();
            _userbar = new HorizontalStack();

            EventKeyPress += (sender, args) =>
            {
                if (args.Key == KeyCode.Escape)
                    Close();
            };

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.MessageItem)));
        }

        /// <summary>
        /// Constructs a MessageBox with specified message and title.
        /// </summary>
        /// <param name="message">Message to a user as System.String.</param>
        /// <param name="title">Title of MessageBox as System.String.</param>
        public MessageBox(String message, String title) : this()
        {
            SetWindowTitle(title);
            _titleBar.SetText(title);
            _msg.SetText(message);
        }
        /// <summary>
        /// Setting a text of message of MessageBox.
        /// </summary>
        /// <param name="text">Text of message as System.String.</param>
        public void SetMessageText(String text)
        {
            _msg.SetText(text);
        }
        /// <summary>
        /// Getting the current text of message of MessageBox.
        /// </summary>
        /// <returns>The current text of message as System.String.</returns>
        public String GetMessageText()
        {
            return _msg.GetText();
        }
        /// <summary>
        /// Setting a text of title of MessageBox.
        /// </summary>
        /// <param name="title">Text of title as System.String.</param>
        public void SetTitle(String title)
        {
            _titleBar.SetText(title);
        }
        /// <summary>
        /// Getting the current text of title of MessageBox.
        /// </summary>
        /// <returns>The current text of title as System.String.</returns>
        public String GetTitle()
        {
            return _titleBar.GetText();
        }

        /// <summary>
        /// Initialize MessageBox window.
        /// </summary>
        public override void InitWindow()
        {
            IsBorderHidden = true;
            IsAlwaysOnTop = true;

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

            SetMinWidth(w_global);
            if (GetWidth() < w_global)
                SetWidth(w_global);
            int w_text = _msg.GetTextWidth() + _msg_layout.GetMargin().Left + _msg_layout.GetMargin().Right
            + _msg_layout.GetPadding().Left + _msg_layout.GetPadding().Right + _msg.GetMargin().Left
            + _msg.GetMargin().Right + _msg.GetPadding().Left + _msg.GetPadding().Right + 10;
            if (GetWidth() < w_text)
                SetWidth(w_text);
            AddItems(_titleBar, _msg_layout);
            GetLayout().GetContainer().Update(GeometryEventType.ResizeWidth, 0);


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

        /// <summary>
        /// Show MessageBox window.
        /// </summary>
        public override void Show()
        {
            _result = false;
            base.Show();
        }
        /// <summary>
        /// Close MessageBox window.
        /// </summary>
        public override void Close()
        {
            base.Close();
            OnCloseDialog?.Invoke();
        }

        private List<ButtonCore> _queue = new List<ButtonCore>();
        /// <summary>
        /// Adding a custom user button to toolbal with the specified ID.
        /// </summary>
        /// <param name="button">User button as SpaceVIL.ButtonCore.</param>
        /// <param name="id">Button's ID as System.Int32.</param>
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
        /// <summary>
        /// Getting the current style of a custom user button (that placed into user's toolbar).
        /// </summary>
        /// <returns>The current style of custom user button as SpaceVIL.Decorations.Style.</returns>
        public Style GetDialogButtonStyle()
        {
            return _btnStyle;
        }
        /// <summary>
        /// Setting a style for a custom user button (that placed into user's toolbar).
        /// </summary>
        /// <param name="style">A style for custom user button as SpaceVIL.Decorations.Style.</param>
        public void SetDialogButtonStyle(Style style)
        {
            _btnStyle = style;
        }
        /// <summary>
        /// Setting a style for entire MessageBox.
        /// <para/> Inner styles: "window", "message", "layout", "toolbar", "userbar" (custom toolbar), "button".
        /// </summary>
        /// <param name="style">A style for MessageBox as SpaceVIL.Decorations.Style.</param>
        public void SetStyle(Style style)
        {
            if (style == null)
                return;

            Style innerStyle = style.GetInnerStyle("window");
            if (innerStyle != null)
            {
                GetLayout().GetContainer().SetStyle(innerStyle);
                SetMinSize(innerStyle.MinWidth, innerStyle.MinHeight);
                SetSize(innerStyle.Width, innerStyle.Height);
            }

            innerStyle = style.GetInnerStyle("message");
            if (innerStyle != null)
            {
                _msg.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("layout");
            if (innerStyle != null)
            {
                _msg_layout.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("toolbar");
            if (innerStyle != null)
            {
                _toolbar.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("userbar");
            if (innerStyle != null)
            {
                _userbar.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("button");
            if (innerStyle != null)
            {
                _btnStyle = innerStyle.Clone();
                _ok.SetStyle(innerStyle);
                _cancel.SetStyle(innerStyle);
            }
        }
    }
}
