using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using System.Threading;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class TextEdit : Prototype, ITextEditable, ITextShortcuts, IDraggable
    {
        static int count = 0;
        private TextLine _text_object;
        private TextLine _substrate_text;

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

        private List<KeyCode> ShiftValCodes;
        private List<KeyCode> InsteadKeyMods;

        private readonly Object textInputLock = new Object();

        private int scrollStep = 15;

        public TextEdit()
        {
            _text_object = new TextLine();
            _substrate_text = new TextLine();

            _cursor = new Rectangle();
            _selectedArea = new Rectangle();

            SetItemName("TextEdit_" + count);
            count++;

            EventMousePress += OnMousePressed;
            EventMouseDrag += OnDragging;
            EventKeyPress += OnKeyPress;
            EventKeyRelease += OnKeyRelease;
            EventTextInput += OnTextInput;
            EventScrollUp += OnScrollUp;
            EventScrollDown += OnScrollDown;
            EventMouseDoubleClick += OnMouseDoubleClick;

            ShiftValCodes = new List<KeyCode>() { KeyCode.Left, KeyCode.Right, KeyCode.End, KeyCode.Home };
            InsteadKeyMods = new List<KeyCode>() {KeyCode.LeftShift, KeyCode.RightShift, KeyCode.LeftControl,
                KeyCode.RightControl, KeyCode.LeftAlt, KeyCode.RightAlt, KeyCode.LeftSuper, KeyCode.RightSuper};

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.TextEdit)));
        }
        public TextEdit(String text) : this()
        {            
            SetText(text);
        }

        private void OnMouseDoubleClick(object sender, MouseArgs args)
        {
            SelectAll();
        }

        private void OnMousePressed(object sender, MouseArgs args)
        {
            Monitor.Enter(textInputLock);
            try
            {
                ReplaceCursorAccordingCoord(args.Position.GetX());
                if (_isSelect)
                    UnselectText();
            }
            finally
            {
                Monitor.Exit(textInputLock);
            }
        }

        private void OnDragging(object sender, MouseArgs args)
        {
            Monitor.Enter(textInputLock);
            try
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
            finally
            {
                Monitor.Exit(textInputLock);
            }
        }

        private void OnScrollUp(object sender, MouseArgs args)
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

        private void OnScrollDown(object sender, MouseArgs args)
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

        private void ReplaceCursorAccordingCoord(int realPos)
        {
            int w = GetTextWidth();
            if (_text_object.GetTextAlignment().HasFlag(ItemAlignment.Right) && (w < _cursorXMax))
                realPos -= GetX() + (GetWidth() - w) - GetPadding().Right - _text_object.GetMargin().Right - _cursor.GetWidth();
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

        private void OnKeyRelease(object sender, KeyArgs args)
        {

        }
        private void OnKeyPress(object sender, KeyArgs args)
        {
            Monitor.Enter(textInputLock);
            try
            {
                //Console.WriteLine(args.Scancode);
                if (!_isEditable)
                {
                    if (args.Mods.Equals(KeyMods.Control) && (args.Key == KeyCode.A || args.Key == KeyCode.a))
                    {
                        SelectAll();
                    }
                    return;
                }

                if (!_isSelect && _justSelected)
                {
                    //_selectFrom = -1;// 0;
                    //_selectTo = -1;// 0;
                    //_justSelected = false;
                    CancelJustSelected();
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
                                _cursor_position = PrivGetText().Length;
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
                                _cursor_position--;
                                PrivSetText(PrivGetText().Remove(_cursor_position, 1));
                                //ReplaceCursor();
                            }
                            if (args.Key == KeyCode.Delete && _cursor_position < PrivGetText().Length)//delete
                            {
                                PrivSetText(PrivGetText().Remove(_cursor_position, 1));
                                //ReplaceCursor();
                            }
                        }
                    }
                    else
                        if (_isSelect && !InsteadKeyMods.Contains(args.Key))
                        UnselectText();
                }

                if (args.Key == KeyCode.Left && _cursor_position > 0)//arrow left
                {
                    if (!_justSelected)
                    {
                        _cursor_position--;
                        ReplaceCursor();
                    }
                }
                if (args.Key == KeyCode.Right && _cursor_position < PrivGetText().Length)//arrow right
                {
                    if (!_justSelected)
                    {
                        _cursor_position++;
                        ReplaceCursor();
                    }
                }
                if (args.Key == KeyCode.End)//end
                {
                    _cursor_position = PrivGetText().Length;
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

        private int CursorPosToCoord(int cPos, bool isx)
        {
            int coord = 0;
            if (_text_object.GetLetPosArray() == null) return coord;
            int letCount = _text_object.GetLetPosArray().Count;
            //_cursor_position = (_cursor_position < 0) ? 0 : _cursor_position;
            //_cursor_position = (_cursor_position > letCount) ? letCount : _cursor_position;
            //Console.WriteLine(cPos + " " + letCount);
            if (cPos > 0)
            {
                coord = _text_object.GetLetPosArray()[cPos - 1];
                if ((GetTextWidth() >= _cursorXMax) || !_text_object.GetTextAlignment().HasFlag(ItemAlignment.Right))
                {
                    coord += _cursor.GetWidth();
                }
            }

            if (isx)
            {
                if (GetLineXShift() + coord < 0)
                {
                    _text_object.SetLineXShift(-coord);
                }
                if (GetLineXShift() + coord > _cursorXMax)
                    _text_object.SetLineXShift(_cursorXMax - coord);
            }

            return GetLineXShift() + coord;
        }

        private void ReplaceCursor()
        {
            int len = PrivGetText().Length;

            if (_cursor_position > len)
            {
                _cursor_position = len;
                //ReplaceCursor();
            }
            int pos = CursorPosToCoord(_cursor_position, true);

            int w = GetTextWidth();

            if (_text_object.GetTextAlignment().HasFlag(ItemAlignment.Right) && (w < _cursorXMax))
            {
                int xcp = GetX() + GetWidth() - w + pos - GetPadding().Right // - _cursor.GetWidth()
                    - _text_object.GetMargin().Right - _cursor.GetWidth();
                if (_cursor_position == 0)
                    xcp -= _cursor.GetWidth();
                _cursor.SetX(xcp);
            }
            else
            {
                int cnt = GetX() + GetPadding().Left + pos + _text_object.GetMargin().Left;
                _cursor.SetX(cnt);
            }
        }

        private void OnTextInput(object sender, TextInputArgs args)
        {
            if (!_isEditable) return;
            Monitor.Enter(textInputLock);
            try
            {
                byte[] input = BitConverter.GetBytes(args.Character);
                string str = Encoding.UTF32.GetString(input);
                if (_isSelect || _justSelected)
                {
                    UnselectText();
                    PrivCutText();
                }
                if (_justSelected) CancelJustSelected(); //_justSelected = false;

                _cursor_position++;
                PrivSetText(PrivGetText().Insert(_cursor_position - 1, str));
                //ReplaceCursor();
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
            _substrate_text.SetTextAlignment(ial);
        }
        public void SetTextMargin(Indents margin)
        {
            _text_object.SetMargin(margin);
            _substrate_text.SetMargin(margin);
        }
        public void SetFont(Font font)
        {
            _text_object.SetFont(font);
            _substrate_text.SetFont(new Font(font.FontFamily, _substrate_text.GetFont().Size, _substrate_text.GetFont().Style));
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
            _substrate_text.SetFontFamily(font_family);
        }
        public Font GetFont()
        {
            return _text_object.GetFont();
        }

        private void PrivSetText(String text)
        {
            Monitor.Enter(textInputLock);
            try
            {
                if (_substrate_text.IsVisible())
                    _substrate_text.SetVisible(false);
                if (text == String.Empty)
                    _substrate_text.SetVisible(true);

                _text_object.SetItemText(text);
                _text_object.CheckXShift(_cursorXMax);
                ReplaceCursor();
            }
            finally
            {
                Monitor.Exit(textInputLock);
            }
        }

        public void SetText(String text)
        {
            if (_isSelect || _justSelected)
            {
                UnselectText();
                CancelJustSelected();
            }
            PrivSetText(text);
        }

        private String PrivGetText()
        {
            return _text_object.GetItemText();
        }
        public String GetText()
        {
            return PrivGetText();
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

            _substrate_text.SetAllowWidth(_cursorXMax);
            _substrate_text.CheckXShift(_cursorXMax);

            ReplaceCursor();
        }

        public override void InitElements()
        {
            AddItems(_substrate_text, _selectedArea, _text_object, _cursor);

            // _cursorXMax = GetWidth() - _cursor.GetWidth() - GetPadding().Left - GetPadding().Right
            //         - _text_object.GetMargin().Left - _text_object.GetMargin().Right; // _cursorXMin;// ;
            // _text_object.SetAllowWidth(_cursorXMax);
            // _text_object.SetLineXShift();

            int scctp = _text_object.GetFontDims()[0];
            if (scctp > scrollStep) scrollStep = scctp;

            _text_object.SetCursorWidth(_cursor.GetWidth());
            _substrate_text.SetCursorWidth(_cursor.GetWidth());
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
            if (fromPt == -1)
                fromPt = 0;
            if (toPt == -1)
                toPt = 0;
            fromPt = CursorPosToCoord(fromPt, false);
            toPt = CursorPosToCoord(toPt, false);

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
                    _text_object.GetMargin().Right - _cursor.GetWidth());
            else
                _selectedArea.SetX(GetX() + GetPadding().Left + fromReal + _text_object.GetMargin().Left);
            _selectedArea.SetWidth(width);
        }

        private string PrivGetSelectedText()
        {
            Monitor.Enter(textInputLock);
            try
            {
                if (_selectFrom == -1)
                    _selectFrom = 0;
                if (_selectTo == -1)
                    _selectTo = 0;
                if (_selectFrom == _selectTo) return "";
                string text = PrivGetText();
                int fromReal = Math.Min(_selectFrom, _selectTo);
                int toReal = Math.Max(_selectFrom, _selectTo);
                if (fromReal < 0)
                    return "";
                string selectedText = text.Substring(fromReal, toReal - fromReal);
                return selectedText;
            }
            finally
            {
                Monitor.Exit(textInputLock);
            }
        }

        public string GetSelectedText()
        {
            return GetSelectedText();
        }

        private void PrivPasteText(string pasteStr)
        {
            if (!_isEditable) return;
            Monitor.Enter(textInputLock);
            try
            {
                if (_isSelect) PrivCutText();
                string text = PrivGetText();
                string newText = text.Substring(0, _cursor_position) + pasteStr + text.Substring(_cursor_position);
                _cursor_position += pasteStr.Length;
                PrivSetText(newText);
                //ReplaceCursor();
            }
            finally
            {
                Monitor.Exit(textInputLock);
            }
        }

        public void PasteText(string pasteStr)
        {
            if (pasteStr != null)
                PrivPasteText(pasteStr);
        }

        private string PrivCutText()
        {
            if (!_isEditable) return "";
            Monitor.Enter(textInputLock);
            try
            {
                if (_selectFrom == -1)
                    _selectFrom = 0;
                if (_selectTo == -1)
                    _selectTo = 0;

                string str = PrivGetSelectedText();
                if (_selectFrom == _selectTo) return str;
                int fromReal = Math.Min(_selectFrom, _selectTo);
                int toReal = Math.Max(_selectFrom, _selectTo);
                _cursor_position = fromReal;
                PrivSetText(PrivGetText().Remove(fromReal, toReal - fromReal));
                ReplaceCursor();
                if (_isSelect)
                    UnselectText();
                CancelJustSelected(); //_justSelected = false;
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

        private void CancelJustSelected()
        {
            _selectFrom = -1;// 0;
            _selectTo = -1;// 0;
            _justSelected = false;
        }

        /*
        internal void ShowCursor(bool isShow) {
            if (isShow)
                _cursor.SetWidth(2);
            else
                _cursor.SetWidth(0);
        }
        */
        // private int NearestPosToCursor(double xPos)
        // {
        //     List<int> endPos = _text_object.GetLetPosArray();
        //     int pos = endPos.OrderBy(x => Math.Abs(x - xPos)).First();
        //     return pos;
        // }
        /*
                internal void SetCursorPosition(double newPos)
                {
                    _cursor_position = NearestPosToCursor(newPos);
                }
        */
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
            inner_style = style.GetInnerStyle("substrate");
            if (inner_style != null)
            {
                _substrate_text.SetFont(inner_style.Font);
                _substrate_text.SetForeground(inner_style.Foreground);
            }
        }

        private int GetLineXShift()
        {
            return _text_object.GetLineXShift();
        }

        internal bool IsBeginning()
        {
            return (_cursor_position == 0);
        }

        public void SelectAll()
        {
            Monitor.Enter(textInputLock);
            try
            {
                _selectFrom = 0;
                _cursor_position = PrivGetText().Length;
                _selectTo = _cursor_position;
                ReplaceCursor();
                _isSelect = true;
                MakeSelectedArea(_selectFrom, _selectTo);
            }
            finally
            {
                Monitor.Exit(textInputLock);
            }
        }

        public void Undo()
        {
            _text_object.Undo();
            ReplaceCursor();
        }

        public void Redo()
        {
            _text_object.Redo();
            ReplaceCursor();
        }

        public void SetSubstrateText(String substrateText)
        {
            _substrate_text.SetItemText(substrateText);
        }

        public void SetSubstrateFontSize(int size)
        {
            _substrate_text.SetFontSize(size);
        }

        public void SetSubstrateFontStyle(FontStyle style)
        {
            _substrate_text.SetFontStyle(style);
        }

        public void SetSubstrateForeground(Color foreground)
        {
            _substrate_text.SetForeground(foreground);
        }

        public void SetSubstrateForeground(int r, int g, int b)
        {
            _substrate_text.SetForeground(r, g, b);
        }

        public void SeSubstratetForeground(int r, int g, int b, int a)
        {
            _substrate_text.SetForeground(r, g, b, a);
        }

        public void SetSubstrateForeground(float r, float g, float b)
        {
            _substrate_text.SetForeground(r, g, b);
        }

        public void SetSubstrateForeground(float r, float g, float b, float a)
        {
            _substrate_text.SetForeground(r, g, b, a);
        }

        public Color GetSubstrateForeground()
        {
            return _substrate_text.GetForeground();
        }

        public String GetSubstrateText()
        {
            return _substrate_text.GetItemText();
        }

        public void AppendText(String text)
        {
            UnselectText();
            CancelJustSelected();
            _cursor_position = PrivGetText().Length;
            PasteText(text);
        }
    }
}