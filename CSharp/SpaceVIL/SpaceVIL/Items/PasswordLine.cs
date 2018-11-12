using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using System.Threading;

namespace SpaceVIL
{
    public class PasswordLine : HorizontalStack
    {
        static int count = 0;

        private ButtonToggle _show_pwd_btn;
        private TextEncrypt _textEncrypt;

        public PasswordLine()
        {
            SetItemName("PasswordLine_" + count);
            _show_pwd_btn = new ButtonToggle();
            _show_pwd_btn.SetItemName(GetItemName() + "_marker");
            _textEncrypt = new TextEncrypt();
            count++;

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.PasswordLine)));
        }

        private void ShowPassword(IItem sender)
        {
            _textEncrypt.ShowPassword(_show_pwd_btn.IsToggled());
        }
        
        public override void SetFocused(bool value)
        {
            base.SetFocused(value);
            _textEncrypt.SetFocused(value);
        }

        public void SetTextAlignment(ItemAlignment alignment)
        {
            _textEncrypt.SetAlignment(alignment);
        }
        public void SetTextMargin(Indents margin)
        {
            _textEncrypt.SetMargin(margin);
        }
        public void SetFont(Font font)
        {
            _textEncrypt.SetFont(font);
        }
        public Font GetFont()
        {
            return _textEncrypt.GetFont();
        }
        
        public String GetPassword()
        {
            return _textEncrypt.GetPassword();
        }
        public void SetForeground(Color color)
        {
            _textEncrypt.SetForeground(color);
        }
        public void SetForeground(int r, int g, int b)
        {
            _textEncrypt.SetForeground(r, g, b);
        }
        public void SetForeground(int r, int g, int b, int a)
        {
            _textEncrypt.SetForeground(r, g, b, a);
        }
        public void SetForeground(float r, float g, float b)
        {
            _textEncrypt.SetForeground(r, g, b);
        }
        public void SetForeground(float r, float g, float b, float a)
        {
            _textEncrypt.SetForeground(r, g, b, a);
        }
        public Color GetForeground()
        {
            return _textEncrypt.GetForeground();
        }
        public virtual bool IsEditable
        {
            get { return _textEncrypt.IsEditable; }
            set
            {
                _textEncrypt.IsEditable = value;
            }
        }

        public override void InitElements()
        {
            _show_pwd_btn.SetPassEvents(false);
            _show_pwd_btn.EventToggle += (sender, args) => ShowPassword(sender);
            AddItems(_textEncrypt, _show_pwd_btn);
        }

        public int GetTextWidth()
        {
            return _textEncrypt.GetWidth();
        }

        public int GetTextHeight()
        {
            return _textEncrypt.GetHeight();
        }

        public void Clear()
        {
            _textEncrypt.Clear();
        }

        //style
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);

            Style inner_style = style.GetInnerStyle("showmarker");
            if (inner_style != null)
            {
                _show_pwd_btn.SetStyle(inner_style);
            }

            SetSpacing(5, 0);
        }
    }
}