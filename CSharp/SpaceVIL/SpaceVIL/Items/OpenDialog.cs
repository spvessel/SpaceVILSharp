using System;
using SpaceVIL.Common;
using SpaceVIL.Core;

namespace SpaceVIL
{
    /// <summary>
    /// Abstract class containing all common methods and properties 
    /// for creating modal window for file browsing.
    /// </summary>
    public abstract class OpenDialog : DialogItem
    {
        static int count = 0;
        private TitleBar _titleBar;

        /// <summary>
        /// Setting a title text of the dialog window.
        /// </summary>
        /// <param name="title">Title text.</param>
        public void SetTitle(String title)
        {
            _titleBar.SetText(title);
        }

        /// <summary>
        /// Getting a title text of the dialog window.
        /// </summary>
        /// <returns>Title text.</returns>
        public String GetTitle()
        {
            return _titleBar.GetText();
        }

        /// <summary>
        /// Default common constructor.
        /// </summary>
        public OpenDialog()
        {
            SetItemName("OpenDialog_" + count);
            count++;
            _titleBar = new TitleBar();
        }

        /// <summary>
        /// Initializing all elements in the OpenDialog.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
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
                if (args.Key == KeyCode.R && args.Mods == CommonService.GetOsControlMod())
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

        /// <summary>
        /// Shows OpenDialog and attaches it to the specified window 
        /// (see SpaceVIL.CoreWindow, SpaceVIL.ActiveWindow, SpaceVIL.DialogWindow).
        /// </summary>
        /// <param name="handler">Window for attaching OpenDialog.</param>
        public override void Show(CoreWindow handler)
        {
            base.Show(handler);
        }

        /// <summary>
        /// Closes OpenDialog.
        /// </summary>
        public override void Close()
        {
            if (OnCloseDialog != null)
                OnCloseDialog.Invoke();

            base.Close();
        }
    }
}