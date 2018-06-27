using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
namespace SpaceVIL
{
    class TextEdit : VisualItem
    {
        static int count = 0;
        private TextLine _text_object;

        public TextEdit()
        {
            _text_object = new TextLine();
            SetItemName("TextEdit" + count);
            SetBackground(180, 180, 180);
            SetForeground(Color.Black);
            SetPadding(5);
            count++;
            EventMouseClick += EmptyEvent;
            EventKeyPress += OnKeyPress;
            EventTextInput += OnTextInput;
        }

        protected virtual void OnKeyPress(object sender, int scancode, KeyMods mods)
        {
            if (scancode == 14 && !GetText().Equals(String.Empty))
            {
                SetText(GetText().Substring(0, GetText().Length - 1));
            }
        }

        protected virtual void OnTextInput(object sender, uint codepoint, KeyMods mods)
        {
            byte[] input = BitConverter.GetBytes(codepoint);
            string str = Encoding.UTF32.GetString(input);
            SetText(GetText() + str);
        }

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
            _text_object.SetAlignment(ItemAlignment.Left | ItemAlignment.VCenter);

            //aligment
            ////SetTextAlignment(ItemAlignment.Left | ItemAlignment.VCenter);

            //adding
            AddItem(_text_object);

            //update text data
            _text_object.UpdateData(UpdateType.Critical);
        }

        public override void InvokePoolEvents()
        {
            if (EventMouseClick != null) EventMouseClick.Invoke(this);
        }

        //style
        public override void SetStyle(Style style)
        {
            base.SetStyle(style);
            SetForeground(style.Foreground);
            SetFont(style.Font);
        }

        public int GetTextWidth()
        {
            return _text_object.GetWidth();
        }

        public int GetTextHeight()
        {
            return _text_object.GetHeight();
        }
    }
}