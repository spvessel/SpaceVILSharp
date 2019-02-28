using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public abstract class OpenDialog : DialogItem
    {
        static int count = 0;
        private TitleBar _titleBar;

        public void SetTitle(String title)
        {
            _titleBar.SetText(title);
        }

        public String GetTitle()
        {
            return _titleBar.GetText();
        }

        public OpenDialog()
        {
            SetItemName("OpenDialog_" + count);
            count++;
            _titleBar = new TitleBar();
        }

        public override void InitElements()
        {
            // important!
            base.InitElements();

            _titleBar.GetMaximizeButton().SetVisible(false);

            // adding toolbar
            Window.AddItems(_titleBar);

            _titleBar.GetCloseButton().EventMouseClick = null;
            _titleBar.GetCloseButton().EventMouseClick += (sender, args) =>
            {
                Close();
            };
        }

        public override void Show(WindowLayout handler)
        {
            base.Show(handler);
        }

        public override void Close()
        {
            if (OnCloseDialog != null)
                OnCloseDialog.Invoke();

            base.Close();
        }

        // public override void SetStyle(Style style)
        // {
        //     if (style == null)
        //         return;
        //     Window.SetStyle(style);
        // }
    }
}