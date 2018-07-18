using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    class TextArea : VisualItem, ITextEditable
    {
        static int count = 0;
        private TextBlock _editLines;
        //private Rectangle _cursor;
        private int _cursor_Xposition = 0;
        private int _cursor_Yposition = 0;
        private int _symbol_position = 0;

        private const int BackspaceCode = 14;
        private const int DeleteCode = 339;
        private const int LeftArrowCode = 331;
        private const int RightArrowCode = 333;
        private const int EndCode = 335;
        private const int HomeCode = 327;
        //private const int LeftShiftCode = 42;
        //private const int RightShiftCode = 54;
        private const int ACode = 30;
        //private const int LeftCtrlCode = 29;
        //private const int RightCtrlCode = 285;
        //private const int EscCode = 1;
        //private const int CapsCode = 58;
        private const int EnterCode = 28;

        private List<int> ShiftValCodes;

        public TextArea()
        {
            _editLines = new TextBlock();
            //_cursor = new Rectangle();
            
            SetItemName("TextArea_" + count);
            SetBackground(180, 180, 180);
            SetForeground(Color.Black);
            SetPadding(5, 0, 5, 0);
            count++;

            EventMouseClick += EmptyEvent;
            EventKeyPress += OnKeyPress;
            EventKeyRelease += OnKeyRelease;
            EventTextInput += OnTextInput;

            ShiftValCodes = new List<int>() {LeftArrowCode, RightArrowCode, EndCode,
                HomeCode};
        }

        protected virtual void OnKeyRelease(object sender, int scancode, KeyMods mods)
        {
        }
        protected virtual void OnKeyPress(object sender, int scancode, KeyMods mods)
        {
            //Console.WriteLine(scancode);

            if (scancode == EnterCode) {
                SetText(GetText().Insert(_cursor_Xposition, "\n"));
                _cursor_Xposition++;
            }

        }
        
        protected virtual void OnTextInput(object sender, uint codepoint, KeyMods mods)
        {
            byte[] input = BitConverter.GetBytes(codepoint);
            string str = Encoding.UTF32.GetString(input);
            //if (_isSelect) CutText();

            //SetText(GetText().Insert(_cursor_Xposition, str));
            //_cursor_Xposition++;
            //Console.WriteLine("input in TextArea");
            _editLines.OnTextInput(sender, codepoint, mods);
            //ReplaceCursor();
        }
        /*
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
        */
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _editLines.SetTextAlignment(alignment);
        }
        public void SetFont(Font font)
        {
            _editLines.SetFont(font);
        }
        public Font GetFont()
        {
            return _editLines.GetFont();
        }
        public void SetText(String text)
        {
            _editLines.SetText(text);
        }
        public String GetText()
        {
            return _editLines.GetWholeText();
        }
        public void SetForeground(Color color)
        {
            _editLines.SetForeground(color);
        }
        public Color GetForeground()
        {
            return _editLines.GetForeground();
        }

        public override void InitElements()
        {
            //text
            SetAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
            SetTextAlignment(ItemAlignment.Left | ItemAlignment.Top);
            //cursor
            //_cursor.IsVisible = false;
            //_cursor.SetBackground(0, 0, 0);
            //_cursor.SetMargin(0, 5, 0, 5);
            //_cursor.SetWidth(2);
            //_cursor.SetHeight(15);// _text_object.GetSize()[1]);
            //_cursor.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            //selectedArea
            //_selectedArea.SetMargin(0, 5, 0, 5);
            //_selectedArea.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);

            _editLines.SetBackground(Color.Transparent);
            _editLines.SetAlignment(ItemAlignment.Top);
            _editLines.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            _editLines.SetSpacing(0, 5);

            //adding

            AddItems(_editLines); //, _cursor); //_selectedArea, 

            //update text data
            //_editLines.UpdateData(UpdateType.Critical);
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
            return _editLines.GetTextWidth();
        }

        public int GetTextHeight()
        {
            return _editLines.GetTextHeight();
        }
    }
}
