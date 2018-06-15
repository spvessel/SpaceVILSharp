using System;
using System.Drawing;

namespace SpaceVIL
{
    class ButtonCore : VisualItem
    {
        static int count = 0;
        private TextLine _text_object;

        public ButtonCore()
        {
            SetItemName("ButtonCore" + count);
            EventMouseClick += EmptyEvent;
            EventMouseHover += (sender) => IsMouseHover = !IsMouseHover;
            count++;

            _text_object = new TextLine();
        }
        public ButtonCore(String text = "") : this()
        {
            SetText(text);
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
    }
}
