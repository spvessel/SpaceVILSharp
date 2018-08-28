using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
namespace SpaceVIL
{
    public class TextEdit : VisualItem, ITextEditable, ITextShortcuts, IDraggable
    {
        static int count = 0;
        private TextLine _text_object;
        private Rectangle _cursor;
        private int _cursor_position = 0;
        private Rectangle _selectedArea;
        public Rectangle GetSelectionArea()
        {
            return _selectedArea;
        }
        private int _selectFrom = 0;
        private int _selectTo = 0;
        private bool _isSelect = false;
        private bool _justSelected = false;

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

        private List<int> ShiftValCodes;
        //private List<int> CtrlValCodes;

        public TextEdit()
        {
            _text_object = new TextLine();
            _cursor = new Rectangle();
            _selectedArea = new Rectangle();
            _selectedArea.SetBackground(Color.FromArgb(50, 0, 0, 0));

            SetItemName("TextEdit_" + count);
            SetBackground(180, 180, 180);
            SetForeground(Color.Black);
            SetPadding(5, 0, 5, 0);
            count++;

            EventMousePressed += OnMousePressed;
            EventMouseDrag += OnDragging;
            EventKeyPress += OnKeyPress;
            EventKeyRelease += OnKeyRelease;
            EventTextInput += OnTextInput;

            ShiftValCodes = new List<int>() {LeftArrowCode, RightArrowCode, EndCode,
                HomeCode};//, LeftShiftCode, RightShiftCode , LeftCtrlCode, RightCtrlCode};
            //CtrlValCodes = new List<int>() {LeftCtrlCode, RightCtrlCode, ACode};
        }

        protected virtual void OnMousePressed(object sender, MouseArgs args)
        {
            ReplaceCursorAccordingCoord(_mouse_ptr.X);
            if (_isSelect)
                UnselectText();
        }

        protected virtual void OnDragging(object sender, MouseArgs args)
        {
            ReplaceCursorAccordingCoord(_mouse_ptr.X);

            if (!_isSelect)
            {
                _isSelect = true;
                _selectFrom = _cursor_position;
            }
            else
            {
                _selectTo = _cursor_position;
                MakeSelectedArea(_selectFrom, _selectTo);
            }
        }

        private void ReplaceCursorAccordingCoord(int realPos)
        {
            realPos -= GetX() + GetPadding().Left;

            _cursor_position = CoordXToPos(realPos);
            ReplaceCursor();
        }

        private int CoordXToPos(int coordX)
        {
            int pos = 0;

            List<int> lineLetPos = _text_object.GetLetPosArray();
            if (lineLetPos == null) return pos;

            for (int i = 0; i < lineLetPos.Count; i++)
            {
                if (lineLetPos[i] <= coordX + 3)
                    pos = i + 1;
                else break;
            }

            return pos;
        }

        protected virtual void OnKeyRelease(object sender, KeyArgs args)
        {

        }
        protected virtual void OnKeyPress(object sender, KeyArgs args)
        {
            //Console.WriteLine(scancode);

            if (!_isSelect && _justSelected)
            {
                _selectFrom = 0;
                _selectTo = 0;
                _justSelected = false;
            }

            if (args.Mods != 0)
            {
                //Выделение не сбрасывается, проверяются сочетания
                switch (args.Mods)
                {
                    case KeyMods.Shift:
                        if (ShiftValCodes.Contains(args.Scancode))
                        {
                            if (!_isSelect)
                            {
                                _isSelect = true;
                                _selectFrom = _cursor_position;
                            }
                        }

                        break;

                    case KeyMods.Control:
                        if (args.Scancode == ACode)
                        {
                            _selectFrom = 0;
                            _cursor_position = GetText().Length;
                            ReplaceCursor();

                            _isSelect = true;
                        }
                        break;

                        //alt, super ?
                }
            }
            else
            {
                if (args.Scancode == BackspaceCode || args.Scancode == DeleteCode)
                {
                    if (_isSelect)
                        CutText();
                    else
                    {
                        if (args.Scancode == BackspaceCode && _cursor_position > 0)//backspace
                        {
                            SetText(GetText().Remove(_cursor_position - 1, 1));
                            _cursor_position--;
                            ReplaceCursor();
                        }
                        if (args.Scancode == DeleteCode && _cursor_position < GetText().Length)//delete
                        {
                            SetText(GetText().Remove(_cursor_position, 1));
                        }
                    }
                }
                else
                    if (_isSelect)
                    UnselectText();
            }

            if (args.Scancode == LeftArrowCode && _cursor_position > 0)//arrow left
            {
                _cursor_position--;
                ReplaceCursor();
            }
            if (args.Scancode == RightArrowCode && _cursor_position < GetText().Length)//arrow right
            {
                _cursor_position++;
                ReplaceCursor();
            }
            if (args.Scancode == EndCode)//end
            {
                _cursor_position = GetText().Length;
                ReplaceCursor();
            }
            if (args.Scancode == HomeCode)//home
            {
                _cursor_position = 0;
                ReplaceCursor();
            }

            if (_isSelect)
            {
                if (_selectTo != _cursor_position)
                {
                    _selectTo = _cursor_position;
                    MakeSelectedArea(_selectFrom, _selectTo);
                }
            }
        }

        private int CursorPosToCoord(int cPos)
        {
            int coord = 0;
            if (_text_object.GetLetPosArray() == null) return coord;
            int letCount = _text_object.GetLetPosArray().Count;
            //_cursor_position = (_cursor_position < 0) ? 0 : _cursor_position;
            //_cursor_position = (_cursor_position > letCount) ? letCount : _cursor_position;
            //Console.WriteLine(cPos + " " + letCount);
            if (cPos > 0)
                coord = _text_object.GetLetPosArray()[cPos - 1];
            return coord;
        }

        private void ReplaceCursor()
        {
            int pos = CursorPosToCoord(_cursor_position);
            _cursor.SetX(GetX() + GetPadding().Left + pos);// 8 * _cursor_position);
        }

        protected virtual void OnTextInput(object sender, TextInputArgs args)
        {
            byte[] input = BitConverter.GetBytes(args.Character);
            string str = Encoding.UTF32.GetString(input);
            //if (_isSelect) CutText();
            if (_justSelected) CutText();
            SetText(GetText().Insert(_cursor_position, str));
            _cursor_position++;
            ReplaceCursor();
            //Console.WriteLine("input in TextEdit " + _cursor_position);
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
        public void SetFontSize(int size)
        {
            _text_object.SetFontSize(size);
        }
        public void SetFontStyle(FontStyle style)
        {
            _text_object.SetFontStyle(style);
        }
        public void SetFontFamily(FontFamily font_family)
        {
            _text_object.SetFontFamily(font_family);
        }
        public Font GetFont()
        {
            return _text_object.GetFont();
        }
        public void SetText(String text)
        {
            _text_object.SetItemText(text);
            //_text_object.UpdateData(UpdateType.Critical); //Doing in _text_object
        }
        public String GetText()
        {
            return _text_object.GetItemText();
        }
        public void SetForeground(Color color)
        {
            _text_object.SetForeground(color);
        }
        public void SetForeground(int r, int g, int b)
        {
            _text_object.SetForeground(r, g, b);
        }
        public void SetForeground(int r, int g, int b, int a)
        {
            _text_object.SetForeground(r, g, b, a);
        }
        public void SetForeground(float r, float g, float b)
        {
            _text_object.SetForeground(r, g, b);
        }
        public void SetForeground(float r, float g, float b, float a)
        {
            _text_object.SetForeground(r, g, b, a);
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
            //_cursor.SetMargin(0, 5, 0, 5);
            _cursor.SetWidth(2);
            //int[] otp = _text_object.GetFontDims();
            //int _minLineSpacer = otp[0];
            //int _minFontY = otp[1];
            //int _maxFontY = otp[2];
            //Console.WriteLine(_minLineSpacer);
            //_cursor.SetHeight(_maxFontY - _minFontY + _minLineSpacer);
            _cursor.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
            //selectedArea
            _selectedArea.SetMargin(0, 5, 0, 5);
            _selectedArea.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
            //adding
            AddItems(_selectedArea, _text_object, _cursor);

            //update text data
            //_text_object.UpdateData(UpdateType.Critical);
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

        private void MakeSelectedArea(int fromPt, int toPt)
        {
            //Console.WriteLine("from " + from + " to " + to);
            fromPt = CursorPosToCoord(fromPt);
            toPt = CursorPosToCoord(toPt);

            if (fromPt == toPt)
            {
                _selectedArea.SetWidth(0);
                return;
            }
            int fromReal = Math.Min(fromPt, toPt);
            int toReal = Math.Max(fromPt, toPt);
            int width = toReal - fromReal + 1;
            _selectedArea.SetX(GetX() + GetPadding().Left + fromReal);
            _selectedArea.SetWidth(width);
        }

        public string GetSelectedText()
        {
            if (_selectFrom == _selectTo) return "";
            string text = GetText();
            int fromReal = Math.Min(_selectFrom, _selectTo);
            int toReal = Math.Max(_selectFrom, _selectTo);
            string selectedText = text.Substring(fromReal, toReal - fromReal);
            return selectedText;
        }

        public void PasteText(string pasteStr)
        {
            if (_isSelect) CutText();
            string text = GetText();
            string newText = text.Substring(0, _cursor_position) + pasteStr + text.Substring(_cursor_position);
            SetText(newText);
            _cursor_position += pasteStr.Length;
            ReplaceCursor();
        }

        public string CutText()
        {
            string str = GetSelectedText();
            if (_selectFrom == _selectTo) return str;
            int fromReal = Math.Min(_selectFrom, _selectTo);
            int toReal = Math.Max(_selectFrom, _selectTo);
            SetText(GetText().Remove(fromReal, toReal - fromReal));
            _cursor_position = fromReal;
            ReplaceCursor();
            UnselectText();
            return str;
        }

        private void UnselectText()
        {
            _isSelect = false;
            _justSelected = true;
            MakeSelectedArea(0, 0);
            //_selectFrom = 0;
            //_selectTo = 0;
        }
        /*
        internal void ShowCursor(bool isShow) {
            if (isShow)
                _cursor.SetWidth(2);
            else
                _cursor.SetWidth(0);
        }
        */
        private int NearestPosToCursor(double xPos)
        {
            List<int> endPos = _text_object.GetLetPosArray();
            int pos = endPos.OrderBy(x => Math.Abs(x - xPos)).First();
            return pos;
        }

        internal void SetCursorPosition(double newPos)
        {
            _cursor_position = NearestPosToCursor(newPos);
        }

    }
}