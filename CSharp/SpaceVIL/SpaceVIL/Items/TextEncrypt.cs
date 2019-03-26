using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using System.Threading;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    internal class TextEncrypt : Prototype, ITextEditable, IDraggable
    {
        static int count = 0;

        private string _pwd = String.Empty;
        private string _hide_sign;
        private TextLine _text_object;
        private TextLine _substrate_text;

        private Rectangle _cursor;
        private int _cursor_position = 0;
        
        private Rectangle _selectedArea;
        private bool _isEditable = true;

        private int _cursorXMax = int.MaxValue;

        private int _selectFrom = -1;
        private int _selectTo = -1;
        private bool _isSelect = false;
        private bool _justSelected = false;

        private List<KeyCode> ShiftValCodes;
        private List<KeyCode> InsteadKeyMods;

        private Object textInputLock = new Object();

        internal TextEncrypt()
        {
            SetItemName("TextEncrypt_" + count);

            // _hide_sign = Encoding.UTF32.GetString(BitConverter.GetBytes(0x23fa)); //big point
            _hide_sign = Encoding.UTF32.GetString(BitConverter.GetBytes(0x25CF)); //big point
            _text_object = new TextLine();
            _text_object.SetRecountable(true);
            _substrate_text = new TextLine();

            _cursor = new Rectangle();
            _selectedArea = new Rectangle();
            count++;

            EventKeyPress += OnKeyPress;
            EventTextInput += OnTextInput;
            EventMousePress += OnMousePressed;
            EventMouseDrag += OnDragging;
            EventMouseDoubleClick += OnMouseDoubleClick;

            ShiftValCodes = new List<KeyCode>() { KeyCode.Left, KeyCode.Right, KeyCode.End, KeyCode.Home };
            InsteadKeyMods = new List<KeyCode>() {KeyCode.LeftShift, KeyCode.RightShift, KeyCode.LeftControl,
                KeyCode.RightControl, KeyCode.LeftAlt, KeyCode.RightAlt, KeyCode.LeftSuper, KeyCode.RightSuper};

            //SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.TextEncrypt)));
            SetCursor(EmbeddedCursor.Beam);
        }

        private void OnMouseDoubleClick(object sender, MouseArgs args) {
            Monitor.Enter(textInputLock);
            try
            {
                _selectFrom = 0;
                _cursor_position = GetText().Length;
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

        private void OnMousePressed(object sender, MouseArgs args)
        {
            Monitor.Enter(textInputLock);
            try
            {
                ReplaceCursorAccordingCoord(args.Position.GetX());
                if (_isSelect)
                {
                    UnselectText();
                    CancelJustSelected();
                }
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

        private void ReplaceCursorAccordingCoord(int realPos)
        {
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
        
        private void OnKeyPress(object sender, KeyArgs args)
        {
            if (!_isEditable) return;

            Monitor.Enter(textInputLock);
            try
            {

                if (!_isSelect && _justSelected)
                {
                    //_selectFrom = -1;// 0;
                    //_selectTo = -1;// 0;
                    //_justSelected = false;
                    CancelJustSelected();
                }

                if (args.Mods != 0)
                {
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
                            CutText();
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
                    if (!_justSelected)
                    {
                        _cursor_position--;
                        ReplaceCursor();
                    }
                }
                if (args.Key == KeyCode.Right && _cursor_position < GetText().Length)//arrow right
                {
                    if (!_justSelected)
                    {
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
            //int letCount = _text_object.GetLetPosArray().Count;

            if (cPos > 0)
                coord = _text_object.GetLetPosArray()[cPos - 1] + _cursor.GetWidth();

            if (GetLineXShift() + coord < 0)
                _text_object.SetLineXShift(-coord);
            if (GetLineXShift() + coord > _cursorXMax)
                _text_object.SetLineXShift(_cursorXMax - coord);

            return GetLineXShift() + coord;
        }

        private void ReplaceCursor()
        {
            int len = GetText().Length;

            if (_cursor_position > len)
            {
                _cursor_position = len;
                //replaceCursor();
            }
            int pos = CursorPosToCoord(_cursor_position);
            _cursor.SetX(GetX() + GetPadding().Left + pos + _text_object.GetMargin().Left);
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
                    CutText();
                }
                if (_justSelected) CancelJustSelected(); //_justSelected = false;

                SetText(GetText().Insert(_cursor_position, str));

                _cursor_position++;
                ReplaceCursor();
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

        internal void SetTextAlignment(ItemAlignment alignment)
        {
            //Ignore all changes
            _text_object.SetTextAlignment(alignment);
            _substrate_text.SetTextAlignment(alignment);
        }
        public void SetTextAlignment(params ItemAlignment[] alignment)
        {
            ItemAlignment common = alignment.ElementAt(0);
            if (alignment.Length > 1)
            {
                for (int i = 1; i < alignment.Length; i++)
                {
                    common |= alignment.ElementAt(i);
                }
            }
            SetTextAlignment(common);
        }

        internal void SetTextMargin(Indents margin)
        {
            _text_object.SetMargin(margin);
            _substrate_text.SetMargin(margin);
        }
        internal void SetFont(Font font)
        {
            _text_object.SetFont(font);
            _substrate_text.SetFont(new Font(font.FontFamily, _substrate_text.GetFont().Size, _substrate_text.GetFont().Style));
        }

        internal void SetFontSize(int size)
        {
            _text_object.SetFontSize(size);
        }

        internal void SetFontStyle(FontStyle style)
        {
            _text_object.SetFontStyle(style);
        }

        internal void SetFontFamily(FontFamily font_family)
        {
            _text_object.SetFontFamily(font_family);
            _substrate_text.SetFontFamily(font_family);
        }

        internal Font GetFont()
        {
            return _text_object.GetFont();
        }

        private bool _needShow = false;

        internal void ShowPassword(bool needShow)
        {
            this._needShow = needShow;
            SetText(_pwd);
            ReplaceCursor();
            GetHandler().SetFocusedItem(this);
        }

        private void SetText(String text)
        {
            Monitor.Enter(textInputLock);
            try
            {
                if (_substrate_text.IsVisible())
                    _substrate_text.SetVisible(false);
                if (text == String.Empty)
                    _substrate_text.SetVisible(true);

                _pwd = text;
                if (_needShow)
                {
                    _text_object.SetItemText(text);
                }
                else
                {
                    StringBuilder txt = new StringBuilder();
                    foreach (var item in text)
                    {
                        txt.Append(_hide_sign);
                    }
                    _text_object.SetItemText(txt.ToString());
                }

                _text_object.CheckXShift(_cursorXMax);
            }
            finally
            {
                Monitor.Exit(textInputLock);
            }
        }
        private String GetText()
        {
            return _pwd;
        }
        internal String GetPassword()
        {
            return _pwd;
        }
        internal void SetForeground(Color color)
        {
            _text_object.SetForeground(color);
        }
        internal void SetForeground(int r, int g, int b)
        {
            _text_object.SetForeground(r, g, b);
        }
        internal void SetForeground(int r, int g, int b, int a)
        {
            _text_object.SetForeground(r, g, b, a);
        }
        internal void SetForeground(float r, float g, float b)
        {
            _text_object.SetForeground(r, g, b);
        }
        internal void SetForeground(float r, float g, float b, float a)
        {
            _text_object.SetForeground(r, g, b, a);
        }
        internal Color GetForeground()
        {
            return _text_object.GetForeground();
        }
        internal bool IsEditable()
        {
            return _isEditable;
        }
        internal void SetEditable(bool value)
        { 
            if (_isEditable == value)
                return;
            _isEditable = value;

            if (_isEditable)
                _cursor.SetVisible(true);
            else
                _cursor.SetVisible(false);
        }

        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            _cursorXMax = GetWidth() - _cursor.GetWidth() - GetPadding().Left - GetPadding().Right -
                _text_object.GetMargin().Left - _text_object.GetMargin().Right;
            _text_object.SetAllowWidth(_cursorXMax);
            _text_object.CheckXShift(_cursorXMax); //_text_object.SetLineXShift();

            _substrate_text.SetAllowWidth(_cursorXMax);
            _substrate_text.CheckXShift(_cursorXMax);
        }

        public override void InitElements()
        {
            // _cursor.IsVisible = false;
            //adding

            AddItems(_substrate_text, _selectedArea, _text_object, _cursor);
            // GetHandler().SetFocusedItem(this);

            // _cursorXMax = GetWidth() - _cursor.GetWidth() - GetPadding().Left - GetPadding().Right -
            //     _text_object.GetMargin().Left - _text_object.GetMargin().Right; //_cursorXMin;// ;
            // _text_object.SetAllowWidth(_cursorXMax);
            // _text_object.SetLineXShift();

            _text_object.SetCursorWidth(_cursor.GetWidth());
            _substrate_text.SetCursorWidth(_cursor.GetWidth());
        }

        internal int GetTextWidth()
        {
            return _text_object.GetWidth();
        }

        internal int GetTextHeight()
        {
            return _text_object.GetHeight();
        }

        private void MakeSelectedArea(int fromPt, int toPt)
        {
            if (fromPt == -1)
                fromPt = 0;
            if (toPt == -1)
                toPt = 0;
            fromPt = CursorPosToCoord(fromPt);
            toPt = CursorPosToCoord(toPt);

            if (fromPt == toPt)
            {
                _selectedArea.SetWidth(0);
                return;
            }
            int fromReal = Math.Min(fromPt, toPt);
            int toReal = Math.Max(fromPt, toPt);

            if (fromReal < 0)
                fromReal = 0;
            if (toReal > _cursorXMax)
                toReal = _cursorXMax;

            int width = toReal - fromReal + 1;
            _selectedArea.SetX(GetX() + GetPadding().Left + fromReal + _text_object.GetMargin().Left);
            _selectedArea.SetWidth(width);
        }

        private void UnselectText()
        {
            _isSelect = false;
            _justSelected = true;
            MakeSelectedArea(0, 0);
        }

        private void CancelJustSelected()
        {
            _selectFrom = -1;// 0;
            _selectTo = -1;// 0;
            _justSelected = false;
        }

        //        private int NearestPosToCursor(double xPos)
        //        {
        //            List<int> endPos = _text_object.GetLetPosArray();
        //            int pos = endPos.OrderBy(x => Math.Abs(x - xPos)).First();
        //            return pos;
        //        }

        //internal void SetCursorPosition(double newPos)
        //{
        //    _cursor_position = NearestPosToCursor(newPos);
        //}

        private string CutText()
        {
            if (!_isEditable) return "";

            Monitor.Enter(textInputLock);
            try
            {
                if (_selectFrom == -1)
                    _selectFrom = 0;
                if (_selectTo == -1)
                    _selectTo = 0;
                string str = GetSelectedText();
                if (_selectFrom == _selectTo) return str;
                int fromReal = Math.Min(_selectFrom, _selectTo);
                int toReal = Math.Max(_selectFrom, _selectTo);
                SetText(GetText().Remove(fromReal, toReal - fromReal));
                _cursor_position = fromReal;
                ReplaceCursor();
                if (_isSelect)
                    UnselectText();
                CancelJustSelected(); // _justSelected = false;
                return str;
            }
            finally
            {
                Monitor.Exit(textInputLock);
            }
        }

        private string GetSelectedText() 
        {
            Monitor.Enter(textInputLock);
            try
            {
                if (_selectFrom == -1)
                    _selectFrom = 0;
                if (_selectTo == -1)
                    _selectTo = 0;
                if (_selectFrom == _selectTo) return "";
                string text = GetText();
                int fromReal = Math.Min(_selectFrom, _selectTo);
                int toReal = Math.Max(_selectFrom, _selectTo);
                string selectedText = text.Substring(fromReal, toReal - fromReal);
                return selectedText;
            }
            finally
            {
                Monitor.Exit(textInputLock);
            }
        }

        public override void Clear()
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

        internal void setSubstrateText(String substrateText)
        {
            _substrate_text.SetItemText(substrateText);
        }

        internal void SetSubstrateFontSize(int size)
        {
            _substrate_text.SetFontSize(size);
        }

        internal void SetSubstrateFontStyle(FontStyle style)
        {
            _substrate_text.SetFontStyle(style);
        }

        internal void SetSubstrateForeground(Color foreground)
        {
            _substrate_text.SetForeground(foreground);
        }

        internal void SetSubstrateForeground(int r, int g, int b)
        {
            _substrate_text.SetForeground(r, g, b);
        }

        internal void SeSubstratetForeground(int r, int g, int b, int a)
        {
            _substrate_text.SetForeground(r, g, b, a);
        }

        internal void SetSubstrateForeground(float r, float g, float b)
        {
            _substrate_text.SetForeground(r, g, b);
        }

        internal void SetSubstrateForeground(float r, float g, float b, float a)
        {
            _substrate_text.SetForeground(r, g, b, a);
        }

        internal Color GetSubstrateForeground()
        {
            return _substrate_text.GetForeground();
        }

        internal String GetSubstrateText()
        {
            return _substrate_text.GetItemText();
        }
    }
}