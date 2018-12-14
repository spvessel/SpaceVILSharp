using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using System.Threading;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class PasswordLine : HorizontalStack
    {
        static int count = 0;

        private ButtonToggle _show_pwd_btn;
        private TextEncrypt _textEncrypt;

        /// <summary>
        /// Constructs a PasswordLine
        /// </summary>
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

        /// <summary>
        /// Set PasswordLine focused/unfocused
        /// </summary>
        public override void SetFocused(bool value)
        {
            base.SetFocused(value);
            _textEncrypt.SetFocused(value);
        }

        /// <summary>
        /// Text alignment in the PasswordLine
        /// </summary>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _textEncrypt.SetAlignment(alignment);
        }

        /// <summary>
        /// Text margin in the PasswordLine
        /// </summary>
        public void SetTextMargin(Indents margin)
        {
            _textEncrypt.SetMargin(margin);
        }

        /// <summary>
        /// Text font in the PasswordLine
        /// </summary>
        public void SetFont(Font font)
        {
            _textEncrypt.SetFont(font);
        }
        public Font GetFont()
        {
            return _textEncrypt.GetFont();
        }

        /// <returns> password string </returns>
        public String GetPassword()
        {
            return _textEncrypt.GetPassword();
        }

        /// <summary>
        /// Text color in the PasswordLine
        /// </summary>
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

        /// <summary>
        /// Returns if PasswordLine editable or not
        /// </summary>
        public bool IsEditable()
        {
            return _textEncrypt.IsEditable();
        }

        /// <summary>
        /// Set PasswordLine editable true or false
        /// </summary>
        public void SetEditable(bool value)
        {
            _textEncrypt.SetEditable(value);            
        }

        /// <summary>
        /// Initialization and adding of all elements in the PasswordLine
        /// </summary>
        public override void InitElements()
        {
            _show_pwd_btn.SetPassEvents(false);
            _show_pwd_btn.EventToggle += (sender, args) => ShowPassword(sender);
            AddItems(_textEncrypt, _show_pwd_btn);
        }

        /// <summary>
        /// Returns width of the whole text in the PasswordLine
        /// (includes visible and invisible parts of the text)
        /// </summary>
        public int GetTextWidth()
        {
            return _textEncrypt.GetWidth();
        }

        /// <summary>
        /// Returns height of the whole text in the PasswordLine
        /// (includes visible and invisible parts of the text)
        /// </summary>
        public int GetTextHeight()
        {
            return _textEncrypt.GetHeight();
        }

        /// <summary>
        /// Remove all text from the PasswordLine
        /// </summary>
        public void Clear()
        {
            _textEncrypt.Clear();
        }

        //style
        /// <summary>
        /// Set style of the PasswordLine
        /// </summary>
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
            inner_style = style.GetInnerStyle("textedit");
            if (inner_style != null)
            {
                _textEncrypt.SetStyle(inner_style);
            }
        }
    }
}