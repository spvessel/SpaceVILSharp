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
        private Rectangle _selectedArea;
        private int _selectFrom = 0;
        private int _selectTo = 0;
        private bool _isSelect = false;

        private const int BackspaceCode = 14;
        private const int DeleteCode = 339;
        private const int LeftArrowCode = 331;
        private const int RightArrowCode = 333;
        private const int EndCode = 335;
        private const int HomeCode = 327;
        private const int LeftShiftCode = 42;
        private const int RightShiftCode = 54;
        private const int ACode = 30;

        private List<int> ShiftValCodes;

        public TextEdit()
        {
            _text_object = new TextLine();
            _cursor = new Rectangle();
            _selectedArea = new Rectangle();
            _selectedArea.SetBackground(Color.FromArgb(255, 0, 191, 255));

            SetItemName("TextEdit_" + count);
            SetBackground(180, 180, 180);
            SetForeground(Color.Black);
            SetPadding(5, 0, 5, 0);
            count++;

            EventMouseClick += EmptyEvent;
            EventKeyPress += OnKeyPress;
            EventTextInput += OnTextInput;

            ShiftValCodes = new List<int>() {LeftArrowCode, RightArrowCode, EndCode,
                HomeCode, LeftShiftCode, RightShiftCode};
        }

        protected virtual void OnKeyPress(object sender, int scancode, KeyMods mods)
        {
            //Console.WriteLine(scancode);
            if (mods == KeyMods.Shift && ShiftValCodes.Contains(scancode))
            {
                if (!_isSelect)
                {
                    _isSelect = true;
                    _selectFrom = _cursor_position;// UpdateCursorCoord();
                }
            }
            else
            {
                if (_isSelect)
                {
                    if (!ShiftValCodes.Contains(scancode) && (_selectFrom != _selectTo))
                    {
                        //Console.WriteLine(_selectFrom + " " + _selectTo);
                        int minPos = Math.Min(_selectFrom, _selectTo);
                        int maxPos = Math.Max(_selectFrom, _selectTo);
                        SetText(GetText().Remove(minPos, (maxPos - minPos)));
                        _cursor_position = minPos;
                        ReplaceCursor();
                    }
                    _isSelect = false;
                    MakeSelectedArea(0, 0);
                }
                else { 
                if (scancode == BackspaceCode && _cursor_position > 0)//backspace
                {
                    SetText(GetText().Remove(_cursor_position - 1, 1));
                    _cursor_position--;
                    ReplaceCursor();
                }
                if (scancode == DeleteCode && _cursor_position < GetText().Length)//delete
                {
                    SetText(GetText().Remove(_cursor_position, 1));
                }
                }

            }


            if (scancode == LeftArrowCode && _cursor_position > 0)//arrow left
            {
                _cursor_position--;
                ReplaceCursor();
            }
            if (scancode == RightArrowCode && _cursor_position < GetText().Length)//arrow right
            {
                _cursor_position++;
                ReplaceCursor();
            }
            if (scancode == EndCode)//end
            {
                _cursor_position = GetText().Length;
                ReplaceCursor();
            }
            if (scancode == HomeCode)//home
            {
                _cursor_position = 0;
                ReplaceCursor();
            }
            
            if (mods == KeyMods.Control && scancode == ACode)
            { //Нужно отключить выделение
                _selectFrom = 0;
                _cursor_position = GetText().Length;
                ReplaceCursor();
                
                _isSelect = true;
            }
            
            if (_isSelect) {
                _selectTo = _cursor_position;// UpdateCursorCoord();
                MakeSelectedArea(CursorPosToCoord(_selectFrom), CursorPosToCoord(_selectTo));
            }
        }

        private int CursorPosToCoord(int cPos) {
            int coord = 0;
            int letCount = _text_object.GetLetPosArray().Count;
            //_cursor_position = (_cursor_position < 0) ? 0 : _cursor_position;
            //_cursor_position = (_cursor_position > letCount) ? letCount : _cursor_position;
            if (cPos > 0)
                coord = _text_object.GetLetPosArray()[cPos - 1];
            return coord;
        }

        private void ReplaceCursor() {
            int pos = CursorPosToCoord(_cursor_position);
            _cursor.SetX(GetX() + GetPadding().Left + pos);// 8 * _cursor_position);
        }

        protected virtual void OnTextInput(object sender, uint codepoint, KeyMods mods)
        {
            byte[] input = BitConverter.GetBytes(codepoint);
            string str = Encoding.UTF32.GetString(input);
            SetText(GetText().Insert(_cursor_position, str));
            _cursor_position++;
            _text_object.UpdateData(UpdateType.Critical); //Console.WriteLine(_cursor_position + " " + _text_object.GetLetPosArray().Count);
            ReplaceCursor();
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
            //selectedArea
            _selectedArea.SetMargin(0, 5, 0, 5);
            _selectedArea.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
            //adding
            AddItems(_selectedArea, _text_object, _cursor);

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

        private void MakeSelectedArea(int from, int to) {
            //Console.WriteLine("from " + from + " to " + to);
            if (from == to) {
                _selectedArea.SetWidth(0);
                return;
            }
            int fromReal = Math.Min(from, to);
            int toReal = Math.Max(from, to);
            int width = toReal - fromReal + 1;
            _selectedArea.SetX(GetX() + GetPadding().Left + fromReal);
            _selectedArea.SetWidth(width);
        }

        //public void SelectText
    }
}