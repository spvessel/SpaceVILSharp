using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using System.Threading;

namespace SpaceVIL
{
    public class TextEdit : Prototype, ITextEditable, ITextShortcuts, IDraggable
    {
        static int count = 0;
        private TextLine _text_object;
        private Rectangle _cursor;
        private int _cursor_position = 0;
        private Rectangle _selectedArea;
        private bool _isEditable = true;

        private int _cursorXMax = int.MaxValue;

        public Rectangle GetSelectionArea()
        {
            return _selectedArea;
        }

        private int _selectFrom = -1;
        private int _selectTo = -1;
        private bool _isSelect = false;
        private bool _justSelected = false;

        // private const int BackspaceCode = 14;
        // private const int DeleteCode = 339;
        // private const int LeftArrowCode = 331;
        // private const int RightArrowCode = 333;
        // private const int EndCode = 335;
        // private const int HomeCode = 327;
        // private const int LeftShiftCode = 42;
        // private const int RightShiftCode = 54;
        // private const int ACode = 30;
        // private const int LeftCtrlCode = 29;
        // private const int RightCtrlCode = 285;
        // private const int EscCode = 1;
        // private const int CapsCode = 58;

        private List<KeyCode> ShiftValCodes;
        private List<KeyCode> InsteadKeyMods;

        private Object textInputLock = new Object();

        private int scrollStep = 15;

        public TextEdit()
        {
            _text_object = new TextLine();
            _cursor = new Rectangle();
            _selectedArea = new Rectangle();

            SetItemName("TextEdit_" + count);
            count++;

            EventMousePressed += OnMousePressed;
            EventMouseDrag += OnDragging;
            EventKeyPress += OnKeyPress;
            EventKeyRelease += OnKeyRelease;
            EventTextInput += OnTextInput;
            EventScrollUp += OnScrollUp;
            EventScrollDown += OnScrollDown;

            ShiftValCodes = new List<KeyCode>() { KeyCode.Left, KeyCode.Right, KeyCode.End, KeyCode.Home };
            InsteadKeyMods = new List<KeyCode>() {KeyCode.LeftShift, KeyCode.RightShift, KeyCode.LeftControl, 
                KeyCode.RightControl, KeyCode.LeftAlt, KeyCode.RightAlt, KeyCode.LeftSuper, KeyCode.RightSuper};
            
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.TextEdit)));
        }

        protected virtual void OnMousePressed(object sender, MouseArgs args)
        {
            ReplaceCursorAccordingCoord(args.Position.GetX());
            if (_isSelect)
                UnselectText();
        }

        protected virtual void OnDragging(object sender, MouseArgs args)
        {
            ReplaceCursorAccordingCoord(args.Position.GetX());

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

        protected virtual void OnScrollUp(object sender, MouseArgs args)
        {
            int w = GetTextWidth();
            
            if (w < _cursorXMax) return;
            int sh = GetLineXShift();
            if (sh >= 0) return;

            int curPos = _cursor.GetX();
            int curCoord = curPos - sh;

            sh += scrollStep;
            if (sh > 0) sh = 0;

            _text_object.SetLineXShift(sh);
            _cursor.SetX(curCoord + sh);

            curPos = _cursor.GetX() - curPos;
            _selectedArea.SetX(_selectedArea.GetX() + curPos);
        }

        protected virtual void OnScrollDown(object sender, MouseArgs args)
        {
            int w = GetTextWidth();
            
            if (w < _cursorXMax) return;
            int sh = GetLineXShift();
            if (w + sh <= _cursorXMax) return;

            int curPos = _cursor.GetX();
            int curCoord = curPos - sh;

            sh -= scrollStep;
            if (w + sh < _cursorXMax)
                sh = _cursorXMax - w;
            
            _text_object.SetLineXShift(sh);
            _cursor.SetX(curCoord + sh);

            curPos = _cursor.GetX() - curPos;
            _selectedArea.SetX(_selectedArea.GetX() + curPos);
        }

        public void InvokeScrollUp(MouseArgs args)
        {
            EventScrollUp?.Invoke(this, args);
        }

        public void InvokeScrollDown(MouseArgs args)
        {
            EventScrollDown?.Invoke(this, args);
        }


        private void ReplaceCursorAccordingCoord(int realPos)
        {
            int w = GetTextWidth();
            if (_text_object.GetTextAlignment().HasFlag(ItemAlignment.Right) && (w < _cursorXMax))
                realPos -= GetX() + (GetWidth() - w) - GetPadding().Right - _text_object.GetMargin().Right;
            else
                realPos -= GetX() + GetPadding().Left + _text_object.GetMargin().Left;

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
                if (lineLetPos[i] + GetLineXShift() <= coordX + 3)
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
            Monitor.Enter(textInputLock);
            try {
            //Console.WriteLine(args.Scancode);
            if (!_isEditable)
            {
                if (args.Mods.Equals(KeyMods.Control) && (args.Key == KeyCode.A || args.Key == KeyCode.a))
                {
                    _selectFrom = 0;
                    _cursor_position = GetText().Length;
                    _selectTo = _cursor_position;
                    ReplaceCursor();
                    _isSelect = true;
                    MakeSelectedArea(_selectFrom, _selectTo);
                }
                return;
            }

            if (!_isSelect && _justSelected)
            {
                _selectFrom = -1;// 0;
                _selectTo = -1;// 0;
                _justSelected = false;
            }

            if (args.Mods != 0)
            {
                //Выделение не сбрасывается, проверяются сочетания
                switch (args.Mods)
                {
                    case KeyMods.Shift:
                        if (ShiftValCodes.Contains(args.Key))
                        {
                            if (!_isSelect)
                            {
                                _isSelect = true;
                                _selectFrom = _cursor_position;
                            }
                        }

                        break;

                    case KeyMods.Control:
                        if (args.Key == KeyCode.A || args.Key == KeyCode.a)
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
                if (args.Key == KeyCode.Backspace || args.Key == KeyCode.Delete)
                {
                    if (_isSelect)
                        PrivCutText();
                    else
                    {
                        if (args.Key == KeyCode.Backspace && _cursor_position > 0)//backspace
                        {
                            SetText(GetText().Remove(_cursor_position - 1, 1));
                            _cursor_position--;
                            ReplaceCursor();
                        }
                        if (args.Key == KeyCode.Delete && _cursor_position < GetText().Length)//delete
                        {
                            SetText(GetText().Remove(_cursor_position, 1));
                            ReplaceCursor();
                        }
                    }
                }
                else
                    if (_isSelect && !InsteadKeyMods.Contains(args.Key))
                        UnselectText();
            }

            if (args.Key == KeyCode.Left && _cursor_position > 0)//arrow left
            {
                if (!_justSelected) {
                    _cursor_position--;
                    ReplaceCursor();
                }
            }
            if (args.Key == KeyCode.Right && _cursor_position < GetText().Length)//arrow right
            {
                if (!_justSelected) {
                    _cursor_position++;
                    ReplaceCursor();
                }
            }
            if (args.Key == KeyCode.End)//end
            {
                _cursor_position = GetText().Length;
                ReplaceCursor();
            }
            if (args.Key == KeyCode.Home)//home
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
            finally
            {
                Monitor.Exit(textInputLock);
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

            if (GetLineXShift() + coord < 0)
            {
                _text_object.SetLineXShift(-coord);
            }
            if (GetLineXShift() + coord > _cursorXMax)
                _text_object.SetLineXShift(_cursorXMax - coord);

            return GetLineXShift() + coord;
        }

        private void ReplaceCursor()
        {
            int pos = CursorPosToCoord(_cursor_position);
            //Console.WriteLine(pos);

            int w = GetTextWidth();
            if (_text_object.GetTextAlignment().HasFlag(ItemAlignment.Right) && (w < _cursorXMax))
            {
                _cursor.SetX(GetX() + GetWidth() - w + pos - _cursor.GetWidth() - GetPadding().Right
                    - _text_object.GetMargin().Right); // - GetPadding().Right);
            }
            else
                _cursor.SetX(GetX() + GetPadding().Left + pos + _text_object.GetMargin().Left);
        }

        protected virtual void OnTextInput(object sender, TextInputArgs args)
        {
            if (!_isEditable) return;
            Monitor.Enter(textInputLock);
            try {
            byte[] input = BitConverter.GetBytes(args.Character);
            string str = Encoding.UTF32.GetString(input);
            if (_isSelect) {
                UnselectText();
                PrivCutText();
            }
            if (_justSelected) _justSelected = false;
            SetText(GetText().Insert(_cursor_position, str));
            _cursor_position++;
            ReplaceCursor();
            //Console.WriteLine("input in TextEdit " + _cursor_position);
            }
            finally
            {
                Monitor.Exit(textInputLock);
            }
        }

        public override void SetFocused(bool value)
        {
            base.SetFocused(value);
            if (IsFocused() && _isEditable)
                _cursor.SetVisible(true);
            else
                _cursor.SetVisible(false);
        }

        public void SetTextAlignment(ItemAlignment alignment)
        {
            ItemAlignment ial;
            if (alignment.HasFlag(ItemAlignment.Right))
                ial = ItemAlignment.Right | ItemAlignment.VCenter;
            else
                ial = ItemAlignment.Left | ItemAlignment.VCenter;
            _text_object.SetTextAlignment(ial);
        }
        public void SetTextMargin(Indents margin)
        {
            _text_object.SetMargin(margin);
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
            Monitor.Enter(textInputLock);
            try {
            _text_object.SetItemText(text);
            _text_object.CheckXShift(_cursorXMax);
            //_text_object.UpdateData(UpdateType.Critical); //Doing in _text_object
            }
            finally
            {
                Monitor.Exit(textInputLock);
            }
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
        public virtual bool IsEditable
        {
            get { return _isEditable; }
            set
            {
                if (_isEditable == value)
                    return;
                _isEditable = value;

                if (_isEditable)
                    _cursor.SetVisible(true);
                else
                    _cursor.SetVisible(false);
            }
        }

        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            _cursorXMax = GetWidth() - _cursor.GetWidth() - GetPadding().Left - GetPadding().Right
                    - _text_object.GetMargin().Left - _text_object.GetMargin().Right; // _cursorXMin;// ;
            _text_object.SetAllowWidth(_cursorXMax);
            _text_object.CheckXShift(_cursorXMax); //_text_object.SetLineXShift();
            ReplaceCursor();
        }

        public override void InitElements()
        {
            AddItems(_selectedArea, _text_object, _cursor);
            _cursorXMax = GetWidth() - _cursor.GetWidth() - GetPadding().Left - GetPadding().Right
                    - _text_object.GetMargin().Left - _text_object.GetMargin().Right; // _cursorXMin;// ;
            _text_object.SetAllowWidth(_cursorXMax);
            _text_object.SetLineXShift();

            int scctp = _text_object.GetFontDims()[0];
            if (scctp > scrollStep) scrollStep = scctp;
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
            //Console.WriteLine("from " + fromPt + " to " + toPt);
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

            int w = GetTextWidth();
            if (_text_object.GetTextAlignment().HasFlag(ItemAlignment.Right) && (w < _cursorXMax))
                _selectedArea.SetX(GetX() + GetWidth() - w + fromReal - GetPadding().Right - 
                    _text_object.GetMargin().Right);
            else
                _selectedArea.SetX(GetX() + GetPadding().Left + fromReal + _text_object.GetMargin().Left);
            _selectedArea.SetWidth(width);
        }

        private string PrivGetSelectedText() {
            if (_selectFrom == _selectTo) return "";
            string text = GetText();
            int fromReal = Math.Min(_selectFrom, _selectTo);
            int toReal = Math.Max(_selectFrom, _selectTo);
            if (fromReal < 0)
                return "";
            string selectedText = text.Substring(fromReal, toReal - fromReal);
            return selectedText;
        }

        public string GetSelectedText()
        {
            return GetSelectedText();
        }

        private void PrivPasteText(string pasteStr) {
            if (!_isEditable) return;
            Monitor.Enter(textInputLock);
            try
            {
                if (_isSelect) PrivCutText();
                string text = GetText();
                string newText = text.Substring(0, _cursor_position) + pasteStr + text.Substring(_cursor_position);
                SetText(newText);
                _cursor_position += pasteStr.Length;
                ReplaceCursor();
            }
            finally
            {
                Monitor.Exit(textInputLock);
            }
        }

        public void PasteText(string pasteStr)
        {
            PrivPasteText(pasteStr);
        }

        private string PrivCutText() {
            if (!_isEditable) return "";
            Monitor.Enter(textInputLock);
            try
            {
                string str = PrivGetSelectedText();
                if (_selectFrom == _selectTo) return str;
                int fromReal = Math.Min(_selectFrom, _selectTo);
                int toReal = Math.Max(_selectFrom, _selectTo);
                SetText(GetText().Remove(fromReal, toReal - fromReal));
                _cursor_position = fromReal;
                ReplaceCursor();
                if (_isSelect)
                    UnselectText();
                _justSelected = false;
                return str;
            }
            finally
            {
                Monitor.Exit(textInputLock);
            }
        }

        public string CutText()
        {
            return PrivCutText();
        }

        private void UnselectText()
        {
            _isSelect = false;
            _justSelected = true;
            MakeSelectedArea(_cursor_position, _cursor_position);
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

        public void Clear()
        {
            SetText("");
        }

        //style
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
            SetForeground(style.Foreground);
            SetFont(style.Font);
            SetTextAlignment(style.TextAlignment);

            Style inner_style = style.GetInnerStyle("selection");
            if (inner_style != null)
            {
                _selectedArea.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("cursor");
            if (inner_style != null)
            {
                _cursor.SetStyle(inner_style);
            }
        }

        private int GetLineXShift() {
            return _text_object.GetLineXShift();
        }
    }
}