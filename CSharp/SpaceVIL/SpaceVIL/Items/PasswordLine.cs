using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
namespace SpaceVIL
{
    class PasswordLine : VisualItem
    {
        static int count = 0;

        private string _pwd = String.Empty;
        private string _hide_sign;
        private TextLine _text_object;

        public PasswordLine()
        {
            _hide_sign = Encoding.UTF32.GetString(BitConverter.GetBytes(0x23fa)); //big point
            _text_object = new TextLine();
            SetItemName("PasswordLine" + count);
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
                _pwd = _pwd.Substring(0, _pwd.Length - 1);
            }
        }

        protected virtual void OnTextInput(object sender, uint codepoint, KeyMods mods)
        {
            byte[] input = BitConverter.GetBytes(codepoint);
            string str = Encoding.UTF32.GetString(input);
            _pwd += str;
            SetText(GetText() + _hide_sign);
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
        private void SetText(String text)
        {
            _text_object.SetItemText(text);
        }
        private String GetText()
        {
            return _text_object.GetItemText();
        }
        public String GetPassword()
        {
            return _pwd;
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