﻿using System;
using System.Drawing;

namespace SpaceVIL
{
    class ButtonCore : VisualItem
    {
        private static int count = 0;
        private TextLine _text_object;

        public ButtonCore()
        {
            SetItemName("ButtonCore_" + count);
            EventMouseClick += EmptyEvent;
            EventMouseHover += (sender) => IsMouseHover = !IsMouseHover;
            count++;

            _text_object = new TextLine();
            EventKeyPress += OnKeyPress;

        }
        public ButtonCore(String text = "") : this()
        {
            SetText(text);
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

        //text init
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _text_object.SetTextAlignment(alignment);
        }
        public void SetTextMargin(Margin margin)
        {
            _text_object.SetMargin(margin);
        }
        public void SetFont(Font font)
        {
            _text_object.SetFont(font);
        }
        internal void SetFontSize(int size)
        {
            _text_object.SetFontSize(size);
        }
        internal void SetFontStyle(FontStyle style)
        {
            _text_object.SetFontStyle(style);
        }
        internal void SetFontFamily(FontFamily font_family)
        {
            _text_object.SetFontFamily(font_family);
        }
        public Font GetFont()
        {
            return _text_object.GetFont();
        }
        public void SetText(String text)
        {
            _text_object.SetItemText(text);
        }
        public String GetText()
        {
            return _text_object.GetItemText();
        }
        public void SetForeground(Color color)
        {
            _text_object.SetForeground(color);
        }
        public Color GetForeground()
        {
            return _text_object.GetForeground();
        }

        public override void InitElements()
        {
            //text
            _text_object.SetAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);

            //aligment
            SetTextAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);

            //adding
            AddItem(_text_object);

            //update text data
            _text_object.UpdateData(UpdateType.Critical);
        }

        //style
        public override void SetStyle(Style style)
        {
            base.SetStyle(style);
            SetForeground(style.Foreground);
            SetFont(style.Font);
        }
    }
}
