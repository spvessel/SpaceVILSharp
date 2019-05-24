using System;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    abstract public class DialogItem : Prototype, IDialogItem
    {
        static int count = 0;
        public ResizableItem Window = new ResizableItem();

        /// <summary>
        /// Constructs a DialogItem
        /// </summary>
        public DialogItem()
        {
            SetItemName("DialogItem_" + count);
            count++;
            SetPassEvents(false);
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.DialogItem)));
        }

        public override void InitElements()
        {
            AddItem(Window);
        }

        CoreWindow _handler = null;
        /// <summary>
        /// Show DialogItem
        /// </summary>
        public virtual void Show(CoreWindow handler)
        {
            _handler = handler;
            _handler.AddItem(this);
            this.SetFocus();
        }

        public virtual void Close()
        {
            _handler.RemoveItem(this);
        }

        public EventCommonMethod OnCloseDialog;
        public override void Release()
        {
            OnCloseDialog = null;
        }

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
