using System;
using System.Collections.Generic;
using System.Drawing;

namespace SpaceVIL
{
    public class MenuItem : VisualItem //not finished
    {
        static int count = 0;
        private TextLine _text_object;
        public ContextMenu _dropdownmenu;
        public bool IsActionItem = false;

        public MenuItem()
        {
            SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            SetBackground(Color.Transparent);
            SetItemName("MenuItem_" + count);
            count++;
            EventKeyPress += OnKeyPress;

            _text_object = new TextLine();
        }
        public MenuItem(String text = "") : this()
        {
            SetText(text);
        }

        protected virtual void OnKeyPress(object sender, KeyArgs args)
        {
            if (args.Scancode == 0x1C)
                EventMouseClick?.Invoke(this, new MouseArgs());
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
        public void SetForeground(int r, int g, int b)
        {
            _text_object.SetForeground(r, g, b);
        }
        public void SetForeground(int r, int g, int b, int a)
        {
            _text_object.SetForeground(r, g, b, a);
        }
        public void SetForeground(float r, float g, float b)
        {
            _text_object.SetForeground(r, g, b);
        }
        public void SetForeground(float r, float g, float b, float a)
        {
            _text_object.SetForeground(r, g, b, a);
        }
        public Color GetForeground()
        {
            return _text_object.GetForeground();
        }

        public override void InitElements()
        {
            //text
            SetForeground(Color.Black);
            _text_object.SetAlignment(ItemAlignment.Left | ItemAlignment.VCenter);

            //aligment
            SetTextAlignment(ItemAlignment.Left | ItemAlignment.VCenter);

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