using System;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class DialogItem : Prototype
    {
        static int count = 0;
        public Frame Window = new Frame();

        /// <summary>
        /// Constructs a MessageItem
        /// </summary>
        public DialogItem()
        {
            SetItemName("DialogItem_" + count);
            count++;
            //SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.DialogItem)));

            //view
            SetAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);
            SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            SetBackground(0, 0, 0, 150);
            SetPassEvents(false);
        }

        public override void InitElements()
        {
            //simple attr
            Window.SetSize(300, 150);
            Window.SetMinSize(300, 150);
            Window.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            Window.SetAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);
            Window.SetPadding(2, 2, 2, 2);
            Window.SetBackground(Color.FromArgb(255, 45, 45, 45));
            Window.SetShadow(5, 3, 3, Color.FromArgb(180, 0, 0, 0));

            //adding
            AddItem(Window);
        }

        WindowLayout _handler = null;
        /// <summary>
        /// Show MessageBox
        /// </summary>
        public virtual void Show(WindowLayout handler)
        {
            _handler = handler;
            _handler.AddItem(this);
            _handler.SetFocusedItem(this);
        }

        public virtual void Close()
        {
            // _handler.ResetItems();
            // _handler.SetFocusedItem(_handler.GetWindow());
            _handler.GetWindow().RemoveItem(this);
        }

        public EventCommonMethod OnCloseDialog = null;

        public override void SetStyle(Style style)
        {

        }
    }
}
