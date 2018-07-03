using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
namespace SpaceVIL
{
    class PasswordLine : VisualItem, ITextEditable
    {
        static int count = 0;

        private ButtonToggle _show_pwd_btn;
        private string _pwd = String.Empty;
        private string _hide_sign;
        private TextLine _text_object;

        public PasswordLine()
        {
            _hide_sign = Encoding.UTF32.GetString(BitConverter.GetBytes(0x23fa)); //big point
            _text_object = new TextLine();
            _show_pwd_btn = new ButtonToggle();

            SetItemName("PasswordLine" + count);
            SetBackground(180, 180, 180);
            SetForeground(Color.Black);
            SetPadding(5, 0, 5, 0);
            count++;
            EventMouseClick += EmptyEvent;
            EventKeyPress += OnKeyPress;
            EventTextInput += OnTextInput;
        }

        private void ShowPassword(IItem sender)
        {
            if (_show_pwd_btn.IsToggled)
                _text_object.SetItemText(_pwd);
            else
            {
                SetText(String.Empty);
                string txt = String.Empty;
                foreach (var item in _pwd)
                {
                    txt += _hide_sign;
                }
                _text_object.SetItemText(txt);
            }
            //_text_object.UpdateData(UpdateType.Critical);
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
            if (_show_pwd_btn.IsToggled)
                SetText(GetText() + str);
            else
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
            //_show_pwd_btn
            _show_pwd_btn = new ButtonToggle();
            _show_pwd_btn.SetItemName(GetItemName() + "_marker");
            _show_pwd_btn.SetBackground(Color.FromArgb(255, 120, 120, 120));
            _show_pwd_btn.SetWidth(16);
            _show_pwd_btn.SetHeight(16);
            _show_pwd_btn.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            _show_pwd_btn.SetAlignment(ItemAlignment.VCenter | ItemAlignment.Right);
            _show_pwd_btn.AddItemState(true, ItemStateType.Toggled, new ItemState()
            {
                Background = Color.FromArgb(255, 80, 80, 80)
            });
            _show_pwd_btn.Border.Radius = 4;
            _show_pwd_btn.EventToggle += ShowPassword;
            //text
            _text_object.SetAlignment(ItemAlignment.Left | ItemAlignment.VCenter);

            //adding
            AddItem(_text_object);
            AddItem(_show_pwd_btn);

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