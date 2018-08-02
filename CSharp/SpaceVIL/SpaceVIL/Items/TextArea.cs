using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    public class TextArea : VisualItem, ITextEditable
    {
        static int count = 0;
        private TextBlock _text_object;
        private Rectangle _cursor;
        private int _cursor_position = 0;

        public TextArea()
        {
            _text_object = new TextBlock();
            _cursor = new Rectangle();
            
            SetItemName("TextArea_" + count);
            SetBackground(180, 180, 180);
            SetForeground(Color.Black);
            SetPadding(5, 0, 5, 0);
            count++;

            EventMouseClick += EmptyEvent;
            EventKeyPress += OnKeyPress;
            EventKeyRelease += OnKeyRelease;
            EventTextInput += OnTextInput;

        }

        protected virtual void OnKeyRelease(object sender, int scancode, KeyMods mods)
        {
        }
        protected virtual void OnKeyPress(object sender, int scancode, KeyMods mods)
        {
        }
        protected virtual void OnTextInput(object sender, uint codepoint, KeyMods mods)
        {
            
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
            _text_object.UpdateData(UpdateType.Critical);
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
            //selectedArea
            //_selectedArea.SetMargin(0, 5, 0, 5);
            //_selectedArea.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
            //adding
            AddItems(_text_object, _cursor); //_selectedArea, 

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
