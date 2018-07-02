using System;
using System.Collections.Generic;
using System.Drawing;

namespace SpaceVIL
{
    class ComboBox : VisualItem //not finished
    {
        static int count = 0;

        public ComboBox()
        {
            SetItemName("ComboBox_" + count);
            EventMouseClick += EmptyEvent;
            EventMouseHover += (sender) => IsMouseHover = !IsMouseHover;
            count++;

            EventKeyPress += OnKeyPress;
        }

        protected virtual void OnKeyPress(object sender, int key, KeyMods mods)
        {
            if (key == 0x1C)
                EventMouseClick?.Invoke(this);
        }
        public override void InvokePoolEvents()
        {
            if (EventMouseClick != null) EventMouseClick.Invoke(this);
        }
    }
}