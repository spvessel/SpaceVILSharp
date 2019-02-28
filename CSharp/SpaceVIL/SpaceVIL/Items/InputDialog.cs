using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class InputDialog : DialogItem
    {
        private String _inputResult = null;

        public String GetResult()
        {
            return _inputResult;
        }

        private ButtonCore _add;
        private TextEdit _input;
        private TitleBar _title;
        private Frame _layout;

        public InputDialog(String title, String actionName, String defaultText)
        {
            SetItemName("InputDialog_");
            _layout = new Frame();
            _title = new TitleBar(title);
            _add = new ButtonCore(actionName);
            _input = new TextEdit();
            _input.SetText(defaultText);

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.InputDialog)));
        }

        public InputDialog(String title, String actionName) : this(title, actionName, String.Empty) { }

        public override void InitElements()
        {
            base.InitElements();

            // title
            _title.GetMinimizeButton().SetVisible(false);
            _title.GetMaximizeButton().SetVisible(false);

            // add
            _add.SetShadow(5, 0, 4, Color.FromArgb(120, 0, 0, 0));

            // adding
            Window.AddItems(_title, _layout);
            _layout.AddItems(_input, _add);

            _title.GetCloseButton().EventMouseClick = null;
            _title.GetCloseButton().EventMouseClick += (sender, args) =>
            {
                Close();
            };

            _add.EventMouseClick += (sender, args) =>
            {
                _inputResult = _input.GetText();
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
                    Close();
                }
            };
        }


        public override void Show(WindowLayout handler)
        {
            base.Show(handler);
            _input.SetFocus();
        }

        public override void Close()
        {
            if (OnCloseDialog != null)
                OnCloseDialog.Invoke();

            base.Close();
        }

        public void SelectAll()
        {
            _input.SelectAll();
        }

        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);

            Style inner_style = style.GetInnerStyle("button");
            if (inner_style != null)
            {
                _add.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("textedit");
            if (inner_style != null)
            {
                _input.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("layout");
            if (inner_style != null)
            {
                _layout.SetStyle(inner_style);
            }
        }
    }
}
