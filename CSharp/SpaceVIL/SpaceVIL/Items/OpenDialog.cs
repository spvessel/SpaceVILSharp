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
            _titleBar.EventMouseDoubleClick += (sender, args) =>
            {
                UpdateWindow();
            };
            EventKeyPress += (sender, args) =>
            {
                if (args.Key == KeyCode.R && args.Mods == KeyMods.Control)
                    UpdateWindow();
                else if (args.Key == KeyCode.Escape)
                    Close();
            };
        }
        private void UpdateWindow()
        {
            Window.Update(GeometryEventType.ResizeHeight, 0);
            Window.Update(GeometryEventType.ResizeWidth, 0);
        }
        public override void Show(CoreWindow handler)
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