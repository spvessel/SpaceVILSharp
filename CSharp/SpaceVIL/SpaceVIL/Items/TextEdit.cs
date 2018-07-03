using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
namespace SpaceVIL
{
    class TextEdit : VisualItem, ITextEditable
    {
        static int count = 0;
        private TextLine _text_object;
        private Rectangle _cursor;
        private int _cursor_position = 0;
        public TextEdit()
        {
            _text_object = new TextLine();
            _cursor = new Rectangle();

            SetItemName("TextEdit_" + count);
            SetBackground(180, 180, 180);
            SetForeground(Color.Black);
            SetPadding(5, 0, 5, 0);
            count++;

            EventMouseClick += EmptyEvent;
            EventKeyPress += OnKeyPress;
            EventTextInput += OnTextInput;
        }

        protected virtual void OnKeyPress(object sender, int scancode, KeyMods mods)
        {
            //Console.WriteLine(scancode);
            if (scancode == 14 && _cursor_position > 0)//backspace
            {
                SetText(GetText().Remove(_cursor_position - 1, 1));
                _cursor_position--;
                _cursor.SetX(GetX() + GetPadding().Left + 8 * _cursor_position);
            }
            if (scancode == 339 && _cursor_position < GetText().Length)//delete
            {
                SetText(GetText().Remove(_cursor_position, 1));
            }
            if (scancode == 331 && _cursor_position > 0)//arrow left
            {
                _cursor_position--;
                _cursor.SetX(GetX() + GetPadding().Left + 8 * _cursor_position);
            }
            if (scancode == 333 && _cursor_position < GetText().Length)//arrow right
            {
                _cursor_position++;
                _cursor.SetX(GetX() + GetPadding().Left + 8 * _cursor_position);
            }
            if (scancode == 335)//home
            {
                _cursor_position = GetText().Length;
                _cursor.SetX(GetX() + GetPadding().Left + 8 * _cursor_position);
            }
            if (scancode == 327)//end
            {
                _cursor_position = 0;
                _cursor.SetX(GetX() + GetPadding().Left + 8 * _cursor_position);
            }
        }

        protected virtual void OnTextInput(object sender, uint codepoint, KeyMods mods)
        {
            byte[] input = BitConverter.GetBytes(codepoint);
            string str = Encoding.UTF32.GetString(input);
            SetText(GetText().Insert(_cursor_position, str));
            _cursor_position++;
            _cursor.SetX(GetX() + GetPadding().Left + 8 * _cursor_position);
        }

        public override bool IsFocused
        {
            get
            {
                return base.IsFocused;
            }
            set
            {
                base.IsFocused = value;
                if (IsFocused)
                    _cursor.IsVisible = true;
                else
                    _cursor.IsVisible = false;
            }
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
            //cursor
            _cursor.IsVisible = false;
            _cursor.SetBackground(0, 0, 0);
            _cursor.SetMargin(0, 5, 0, 5);
            _cursor.SetWidth(2);
            _cursor.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);

            //adding
            AddItems(_text_object, _cursor);

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