using System;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// DialogItem is an abstract class for imitation of modal window.
    /// <para/>DialogItem extends Prototype class, IDialogItem interface. 
    /// </summary>
    abstract public class DialogItem : Prototype, IDialogItem
    {
        static int count = 0;
        /// <summary>
        /// Window area of DialogItem.
        /// </summary>
        public ResizableItem Window = new ResizableItem();

        /// <summary>
        /// Constructs a DialogItem.
        /// </summary>
        public DialogItem()
        {
            SetItemName("DialogItem_" + count);
            count++;
            SetPassEvents(false);
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.DialogItem)));
        }
        /// <summary>
        /// Initializing all elements in the DialogItem.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            AddItem(Window);
        }

        private CoreWindow _handler = null;
        /// <summary>
        /// Shows DialogItem and attaches it to the specified window 
        /// (see SpaceVIL.CoreWindow, SpaceVIL.ActiveWindow, SpaceVIL.DialogWindow).
        /// </summary>
        /// <param name="handler">Window for attaching InputDialog.</param>
        public virtual void Show(CoreWindow handler)
        {
            _handler = handler;
            _handler.AddItem(this);
            this.SetFocus();
        }
        /// <summary>
        /// Close the DialogItem.
        /// </summary>
        public virtual void Close()
        {
            _handler.RemoveItem(this);
        }
        /// <summary>
        /// An event to describe the actions that must be performed after the dialog is closed.
        /// <para/> Event type: SpaceVIL.EventCommonMethod.
        /// <para/> Function arguments: none.
        /// </summary>
        public EventCommonMethod OnCloseDialog;

        /// <summary>
        /// Disposing DialogItem resources if the it was removed.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void Release()
        {
            OnCloseDialog = null;
        }

        /// <summary>
        /// Setting style of the DialogItem.
        /// <para/> Inner styles: "window".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
            Style inner_style = style.GetInnerStyle("window");
            if (inner_style != null)
            {
                Window.SetStyle(inner_style);
            }
        }
    }
}
