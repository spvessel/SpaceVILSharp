using System;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// InputBox - dialog window 
    /// for entering text and perform assigned actions. 
    /// <para/> Contains ACTION button, CANCEL button, titlebar. 
    /// </summary>
    public class InputBox : DialogWindow
    {
        private String _inputResult = String.Empty;

        /// <summary>
        /// Getting text input result. Default: empty.
        /// </summary>
        /// <returns>Text result as System.String.</returns>
        public String GetResult()
        {
            return _inputResult;
        }

        private ButtonCore _action;
        private ButtonCore _cancel;

        /// <summary>
        /// Getting ACTION button for appearance customizing or assigning new actions.
        /// </summary>
        /// <returns>InputBox's OK button as SpaceVIL.ButtonCore.</returns>
        public ButtonCore GetActionButton()
        {
            return _action;
        }

        /// <summary>
        /// Getting CANCEL button for appearance customizing or assigning new actions.
        /// </summary>
        /// <returns>InputBox's CANCEL button as SpaceVIL.ButtonCore.</returns>
        public ButtonCore GetCancelButton()
        {
            return _cancel;
        }

        /// <summary>
        /// Setting CANCEL button visible of invisible.
        /// </summary>
        /// <param name="value">True: if you want CANCEL button to be visible. 
        /// False: if you want CANCEL button to be invisible.</param>
        public void SetCancelVisible(bool value)
        {
            _cancel.SetVisible(value);
        }

        private TextEdit _input;
        private TitleBar _title;
        private Frame _layout;
        private HorizontalStack _stack;

        /// <summary>
        /// Constructs a InputBox with specified default text, 
        /// title and name of ACTION button.
        /// </summary>
        /// <param name="title">Title of InputBox as System.String.</param>
        /// <param name="actionName">Name of ACTION button as System.String.</param>
        /// <param name="textByDefault">Default text of text field as System.String.</param>
        public InputBox(String title, String actionName, String textByDefault)
        {
            SetWindowName("InputBox:" + title);
            _layout = new Frame();
            _stack = new HorizontalStack();
            _title = new TitleBar(title);
            _action = new ButtonCore(actionName);
            _cancel = new ButtonCore("Cancel");
            _input = new TextEdit();
            _input.SetText(textByDefault);

            EventKeyPress += (sender, args) =>
            {
                if (args.Key == KeyCode.Escape)
                {
                    _inputResult = String.Empty;
                    Close();
                }
            };

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.InputDialog)));
        }

        /// <summary>
        /// Constructs a InputBox with specified 
        /// title and name of ACTION button.
        /// </summary>
        /// <param name="title">Title of InputBox as System.String.</param>
        /// <param name="actionName">Name of ACTION button as System.String.</param>
        public InputBox(String title, String actionName)
            : this(title, actionName, String.Empty) { }


        /// <summary>
        /// Initializing all elements in the InputBox. 
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitWindow()
        {
            IsBorderHidden = true;
            IsAlwaysOnTop = true;

            // title
            _title.GetMinimizeButton().SetVisible(false);
            _title.GetMaximizeButton().SetVisible(false);

            // adding
            AddItems(_title, _layout);

            //stack size
            int w = (_action.GetWidth() + _action.GetMargin().Left + _action.GetMargin().Right);
            if (_cancel.IsVisible())
                w = w * 2 + 10;
            _stack.SetSize(w, _action.GetHeight() + _action.GetMargin().Top + _action.GetMargin().Bottom);

            _layout.AddItems(_input, _stack);
            _stack.AddItems(_action, _cancel);

            _title.GetCloseButton().EventMouseClick = null;
            _title.GetCloseButton().EventMouseClick += (sender, args) =>
            {
                _inputResult = String.Empty;
                Close();
            };

            _action.EventMouseClick += (sender, args) =>
            {
                _inputResult = _input.GetText();
                Close();
            };

            _cancel.EventMouseClick += (sender, args) =>
            {
                _inputResult = String.Empty;
                Close();
            };

            _input.EventKeyPress += (sender, args) =>
            {
                if (args.Key == KeyCode.Enter || args.Key == KeyCode.NumpadEnter)
                {
                    _inputResult = _input.GetText();
                    Close();
                }
                else if (args.Key == KeyCode.Escape)
                {
                    _inputResult = String.Empty;
                    Close();
                }
            };
        }

        /// <summary>
        /// Show InputBox window.
        /// </summary>
        public override void Show()
        {
            base.Show();

            _input.SetFocus();
            _input.SelectAll();
        }

        /// <summary>
        /// Closes InputBox window.
        /// </summary>
        public override void Close()
        {
            base.Close();

            OnCloseDialog?.Invoke();
        }

        /// <summary>
        /// Select all text in the text field.
        /// </summary>
        public void SelectAll()
        {
            _input.SelectAll();
        }

        /// <summary>
        /// Setting a style for entire InputBox.
        /// <para/> Inner styles: "window", "textedit", "layout", "toolbar", "button".
        /// </summary>
        /// <param name="style">A style for InputBox as SpaceVIL.Decorations.Style.</param>
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

            innerStyle = style.GetInnerStyle("button");
            if (innerStyle != null)
            {
                _action.SetStyle(innerStyle);
                _cancel.SetStyle(innerStyle);
            }

            innerStyle = style.GetInnerStyle("textedit");
            if (innerStyle != null)
            {
                _input.SetStyle(innerStyle);
            }

            innerStyle = style.GetInnerStyle("layout");
            if (innerStyle != null)
            {
                _layout.SetStyle(innerStyle);
            }

            innerStyle = style.GetInnerStyle("toolbar");
            if (innerStyle != null)
            {
                _stack.SetStyle(innerStyle);
            }
        }
    }
}