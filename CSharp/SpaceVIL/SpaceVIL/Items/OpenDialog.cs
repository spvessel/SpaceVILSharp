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
        private TitleBar titleBar;

        public OpenDialog()
        {
            SetItemName("OpenDialog_" + count);
            count++;
            titleBar = new TitleBar();
        }

        public override void InitElements()
        {
            // important!
            base.InitElements();

            Window.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            Window.SetMargin(50, 20, 50, 20);

            titleBar.GetMaximizeButton().SetVisible(false);

            // adding toolbar
            Window.AddItems(titleBar);

            titleBar.GetCloseButton().EventMouseClick = null;
            titleBar.GetCloseButton().EventMouseClick += (sender, args) =>
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

        public override void SetStyle(Style style)
        {

        }
    }
}