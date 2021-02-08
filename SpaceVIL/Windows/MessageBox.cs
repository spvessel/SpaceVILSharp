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
        static int count = 0;

        private bool _result = false;

        /// <summary>
        /// Getting MessageBox result.
        /// <para/> Default: False
        /// </summary>
        /// <returns>True: OK button was clicked. False: Close button or Cancel button was clicked.</returns>
        public bool GetResult()
        {
            return _result;
        }

        private Dictionary<Int32, ButtonCore> _userMap = new Dictionary<Int32, ButtonCore>();
        private Int32 _userButtonResult = -1;

        /// <summary>
        /// Getting result from custom toolbar (if it was created).
        /// </summary>
        /// <returns>Id of clicked button (see AddUserButton(ButtonCore button, int id)).</returns>
        public int GetUserButtonResult()
        {
            return _userButtonResult;
        }

        private TitleBar _titleBar;
        private Frame _msgLayout;
        private Label _msgLabel;
        private ButtonCore _okButton;

        /// <summary>
        /// Getting OK button for appearance customizing or assigning new actions.
        /// </summary>
        /// <returns>MessageBox's OK button as SpaceVIL.ButtonCore.</returns>
        public ButtonCore GetOkButton()
        {
            return _okButton;
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
            _msgLayout = new Frame();
            _msgLabel = new Label();
            _okButton = GetButton("OK");
            _cancel = GetButton("Cancel");

            _toolbar = new HorizontalStack();
            _userbar = new HorizontalStack();

            EventKeyPress += (sender, args) =>
            {
                if (args.Key == KeyCode.Escape)
                {
                    Close();
                }
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
            _msgLabel.SetText(message);
        }

        /// <summary>
        /// Setting a text of message of MessageBox.
        /// </summary>
        /// <param name="text">Text of message as System.String.</param>
        public void SetMessageText(String text)
        {
            _msgLabel.SetText(text);
        }

        /// <summary>
        /// Getting the current text of message of MessageBox.
        /// </summary>
        /// <returns>The current text of message as System.String.</returns>
        public String GetMessageText()
        {
            return _msgLabel.GetText();
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
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitWindow()
        {
            IsBorderHidden = true;
            IsAlwaysOnTop = true;

            // Adding toolbar
            _titleBar.GetMaximizeButton().SetVisible(false);

            int wGlobal = 0;

            // toolbar size
            int w = _okButton.GetWidth() + _okButton.GetMargin().Left + _okButton.GetMargin().Right;
            if (_cancel.IsVisible())
            {
                w = w * 2 + 10;
            }
            _toolbar.SetSize(w, _okButton.GetHeight() + _okButton.GetMargin().Top + _okButton.GetMargin().Bottom);

            wGlobal += w;

            bool isEmpty = true;

            w = _userbar.GetPadding().Left + _userbar.GetPadding().Right;

            if (_queue.Count > 0)
            {
                isEmpty = false;
                foreach (ButtonCore btn in _queue)
                {
                    w += btn.GetWidth() + _userbar.GetSpacing().Horizontal;
                }
                w -= _userbar.GetSpacing().Horizontal;
            }
            _userbar.SetSize(w, _toolbar.GetHeight());

            wGlobal += (_toolbar.GetMargin().Left + _toolbar.GetMargin().Right);
            wGlobal += _userbar.GetMargin().Left + _userbar.GetMargin().Right + _userbar.GetWidth();
            wGlobal += _msgLayout.GetPadding().Left + _msgLayout.GetPadding().Right;

            if (GetWidth() < wGlobal)
            {
                SetWidth(wGlobal);
            }
            int wText = _msgLabel.GetTextWidth() + _msgLayout.GetMargin().Left + _msgLayout.GetMargin().Right
                + _msgLayout.GetPadding().Left + _msgLayout.GetPadding().Right + _msgLabel.GetMargin().Left
                + _msgLabel.GetMargin().Right + _msgLabel.GetPadding().Left + _msgLabel.GetPadding().Right + 10;

            if (GetWidth() < wText)
            {
                SetWidth(wText);
            }
            AddItems(_titleBar, _msgLayout);
            GetLayout().GetContainer().Update(GeometryEventType.ResizeWidth, 0);

            if (!isEmpty)
            {
                _toolbar.SetAlignment(ItemAlignment.Right, ItemAlignment.Bottom);
                _msgLayout.AddItems(_msgLabel, _userbar, _toolbar);
            }
            else
            {
                _msgLayout.AddItems(_msgLabel, _toolbar);
            }

            // queue
            if (!isEmpty)
            {
                foreach (ButtonCore btn in _queue)
                {
                    _userbar.AddItem(btn);
                }
            }
            _toolbar.AddItems(_okButton, _cancel);

            SetMinWidth(wGlobal);

            TryAdd(_userMap, 1, _okButton);
            TryAdd(_userMap, 0, _titleBar.GetCloseButton());
            TryAdd(_userMap, -1, _cancel);
            // buttons
            _okButton.SetItemName("OK");
            _okButton.EventMouseClick += (sender, args) =>
            {
                _userButtonResult = 1;
                _result = true;
                Close();
            };

            _titleBar.GetCloseButton().EventMouseClick = null;
            _titleBar.GetCloseButton().EventMouseClick += (sender, args) =>
            {
                _userButtonResult = 0;
                Close();
            };

            _cancel.SetItemName("Cancel");
            _cancel.EventMouseClick += (sender, args) =>
            {
                _userButtonResult = -1;
                Close();
            };
        }

        /// <summary>
        /// Show MessageBox window.
        /// </summary>
        public override void Show()
        {
            _userButtonResult = -1;
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

            if (!TryAdd(_userMap, id, button))
                return;

            _queue.Add(button);

            button.SetStyle(_btnStyle);

            button.EventMouseClick += (sender, args) =>
            {
                _userButtonResult = id;
                Close();
            };
        }

        private bool TryAdd(Dictionary<Int32, ButtonCore> map, Int32 id, ButtonCore btn)
        {
            if (map.ContainsKey(id))
                return false;

            map.Add(id, btn);
            return true;
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
            {
                return;
            }

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
                _msgLabel.SetStyle(innerStyle);
            }

            innerStyle = style.GetInnerStyle("layout");
            if (innerStyle != null)
            {
                _msgLayout.SetStyle(innerStyle);
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
                _okButton.SetStyle(innerStyle);
                _cancel.SetStyle(innerStyle);
            }
        }
    }
}
