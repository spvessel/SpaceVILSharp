using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using System.Threading;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using SpaceVIL.Common;

namespace SpaceVIL
{
    internal class TextEncrypt : Prototype, ITextEditable, IDraggable
    {
        static int count = 0;

        private string _pwd = String.Empty;
        private string _hideSign;
        private TextLine _textObject;
        private TextLine _substrateText;

        private Rectangle _cursor;
        private int _cursorPosition = 0;

        private Rectangle _selectedArea;
        private bool _isEditable = true;

        private int _cursorXMax = SpaceVILConstants.SizeMaxValue;

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

            // _hideSign = Encoding.UTF32.GetString(BitConverter.GetBytes(0x23fa)); //big point
            _hideSign = Encoding.UTF32.GetString(BitConverter.GetBytes(0x25CF)); //big point
            _textObject = new TextLine();
            _textObject.SetRecountable(true);
            _substrateText = new TextLine();

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
            SetCursor(EmbeddedCursor.IBeam);
        }

        private void OnMouseDoubleClick(object sender, MouseArgs args)
        {
            Monitor.Enter(textInputLock);
            try
            {
                if (args.Button == MouseButton.ButtonLeft)
                {
                    _selectFrom = 0;
                    _cursorPosition = GetText().Length;
                    _selectTo = _cursorPosition;
                    ReplaceCursor();

                    _isSelect = true;
                    MakeSelectedArea(); //_selectFrom, _selectTo);
                }
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
                if (args.Button == MouseButton.ButtonLeft)
                {
                    ReplaceCursorAccordingCoord(args.Position.GetX());
                    if (_isSelect)
                    {
                        UnselectText();
                        CancelJustSelected();
                    }
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
                if (args.Button == MouseButton.ButtonLeft)
                {
                    ReplaceCursorAccordingCoord(args.Position.GetX());

                    if (!_isSelect)
                    {
                        _isSelect = true;
                        _selectFrom = _cursorPosition;
                    }
                    else
                    {
                        _selectTo = _cursorPosition;
                        MakeSelectedArea(); //_selectFrom, _selectTo);
                    }
                }
            }
            finally
            {
                Monitor.Exit(textInputLock);
            }
        }

        private void ReplaceCursorAccordingCoord(int realPos)
        {
            int w = GetTextWidth();
            if (_textObject.GetTextAlignment().HasFlag(ItemAlignment.Right) && (w < _cursorXMax))
                realPos -= GetX() + (GetWidth() - w) - GetPadding().Right - _textObject.GetMargin().Right - _cursor.GetWidth();
            else
                realPos -= GetX() + GetPadding().Left + _textObject.GetMargin().Left;

            _cursorPosition = CoordXToPos(realPos);
            ReplaceCursor();
        }

        private int CoordXToPos(int coordX)
        {
            int pos = 0;

            List<int> lineLetPos = _textObject.GetLetPosArray();
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
                    CancelJustSelected();
                }

                if (args.Mods != 0)
                {
                    // switch (args.Mods)
                    // {
                    //     case KeyMods.Shift:
                    if (args.Mods == KeyMods.Shift)
                    {
                        if (ShiftValCodes.Contains(args.Key))
                        {
                            if (!_isSelect)
                            {
                                _isSelect = true;
                                _selectFrom = _cursorPosition;
                            }
                        }
                    }
                    //     break;
                    // case control:
                    if (args.Mods == CommonService.GetOsControlMod())
                    {
                        if (args.Key == KeyCode.A || args.Key == KeyCode.a)
                        {
                            _selectFrom = 0;
                            _cursorPosition = GetText().Length;
                            ReplaceCursor();

                            _isSelect = true;
                        }
                    }
                    // break;
                    //alt, super ?
                    // }
                }
                else
                {
                    if (args.Key == KeyCode.Backspace || args.Key == KeyCode.Delete)
                    {
                        if (_isSelect)
                            CutText();
                        else
                        {
                            if (args.Key == KeyCode.Backspace && _cursorPosition > 0)//backspace
                            {
                                SetText(GetText().Remove(_cursorPosition - 1, 1));
                                _cursorPosition--;
                                ReplaceCursor();
                            }
                            if (args.Key == KeyCode.Delete && _cursorPosition < GetText().Length)//delete
                            {
                                SetText(GetText().Remove(_cursorPosition, 1));
                                ReplaceCursor();
                            }
                        }
                    }
                    else
                        if (_isSelect && !InsteadKeyMods.Contains(args.Key))
                        UnselectText();
                }

                if (args.Key == KeyCode.Left && _cursorPosition > 0)//arrow left
                {
                    if (!_justSelected)
                    {
                        _cursorPosition--;
                        ReplaceCursor();
                    }
                }
                if (args.Key == KeyCode.Right && _cursorPosition < GetText().Length)//arrow right
                {
                    if (!_justSelected)
                    {
                        _cursorPosition++;
                        ReplaceCursor();
                    }
                }
                if (args.Key == KeyCode.End)//end
                {
                    _cursorPosition = GetText().Length;
                    ReplaceCursor();
                }
                if (args.Key == KeyCode.Home)//home
                {
                    _cursorPosition = 0;
                    ReplaceCursor();
                }

                if (_isSelect)
                {
                    if (_selectTo != _cursorPosition)
                    {
                        _selectTo = _cursorPosition;
                        MakeSelectedArea(); //_selectFrom, _selectTo);
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
            if (_textObject.GetLetPosArray() == null) return coord;

            if (cPos > 0)
            {
                coord = _textObject.GetLetPosArray()[cPos - 1];
                if ((GetTextWidth() >= _cursorXMax) || !_textObject.GetTextAlignment().HasFlag(ItemAlignment.Right))
                {
                    coord += _cursor.GetWidth();
                }
            }

            if (isx)
            {
                if (GetLineXShift() + coord < 0)
                {
                    _textObject.SetLineXShift(-coord);
                }
                if (GetLineXShift() + coord > _cursorXMax)
                    _textObject.SetLineXShift(_cursorXMax - coord);
            }

            return GetLineXShift() + coord;
        }

        private void ReplaceCursor()
        {
            int len = GetText().Length;

            if (_cursorPosition > len)
            {
                _cursorPosition = len;
                //replaceCursor();
            }
            int pos = CursorPosToCoord(_cursorPosition, true);
            int w = GetTextWidth();

            if (_textObject.GetTextAlignment().HasFlag(ItemAlignment.Right) && (w < _cursorXMax))
            {
                int xcp = GetX() + GetWidth() - w + pos - GetPadding().Right // - _cursor.GetWidth()
                    - _textObject.GetMargin().Right - _cursor.GetWidth();
                if (_cursorPosition == 0)
                    xcp -= _cursor.GetWidth();
                _cursor.SetX(xcp);
            }
            else
            {
                int cnt = GetX() + GetPadding().Left + pos + _textObject.GetMargin().Left;
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
                    CutText();
                }
                if (_justSelected) CancelJustSelected();

                SetText(GetText().Insert(_cursorPosition, str));

                _cursorPosition++;
                ReplaceCursor();
            }
            finally
            {
                Monitor.Exit(textInputLock);
            }
        }

        protected internal override void SetFocused(bool value)
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
            ItemAlignment ial;
            if (alignment.HasFlag(ItemAlignment.Right))
                ial = ItemAlignment.Right | ItemAlignment.VCenter;
            else
                ial = ItemAlignment.Left | ItemAlignment.VCenter;
            _textObject.SetTextAlignment(ial);
            _substrateText.SetTextAlignment(ial);
        }
        internal void SetTextAlignment(params ItemAlignment[] alignment)
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
            _textObject.SetMargin(margin);
            _substrateText.SetMargin(margin);
        }
        internal Indents GetTextMargin()
        {
            return _textObject.GetMargin();
        }
        internal void SetFont(Font font)
        {
            _textObject.SetFont(font);
            _substrateText.SetFont(FontService.ChangeFontFamily(font.FontFamily, _substrateText.GetFont()));
        }

        internal void SetFontSize(int size)
        {
            _textObject.SetFontSize(size);
        }

        internal void SetFontStyle(FontStyle style)
        {
            _textObject.SetFontStyle(style);
        }

        internal void SetFontFamily(FontFamily font_family)
        {
            _textObject.SetFontFamily(font_family);
            _substrateText.SetFontFamily(font_family);
        }

        internal Font GetFont()
        {
            return _textObject.GetFont();
        }

        private bool _needShow = false;

        internal void ShowPassword(bool needShow)
        {
            if (needShow == _needShow)
                return;
            this._needShow = needShow;
            SetText(_pwd);
            MakeSelectedArea(); //_selectFrom, _selectTo);
            ReplaceCursor();
            GetHandler().SetFocusedItem(this);
        }

        private void SetText(String text)
        {
            Monitor.Enter(textInputLock);
            try
            {
                if (_substrateText.IsVisible())
                    _substrateText.SetVisible(false);
                if (String.IsNullOrEmpty(text)) // text == null || text == String.Empty)
                    _substrateText.SetVisible(true);

                _pwd = text;
                if (_needShow)
                {
                    _textObject.SetItemText(text);
                }
                else
                {
                    StringBuilder txt = new StringBuilder();
                    foreach (var item in text)
                    {
                        txt.Append(_hideSign);
                    }
                    _textObject.SetItemText(txt.ToString());
                }

                _textObject.CheckXShift(_cursorXMax);
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
            _textObject.SetForeground(color);
        }
        internal void SetForeground(int r, int g, int b)
        {
            _textObject.SetForeground(r, g, b);
        }
        internal void SetForeground(int r, int g, int b, int a)
        {
            _textObject.SetForeground(r, g, b, a);
        }
        internal void SetForeground(float r, float g, float b)
        {
            _textObject.SetForeground(r, g, b);
        }
        internal void SetForeground(float r, float g, float b, float a)
        {
            _textObject.SetForeground(r, g, b, a);
        }
        internal Color GetForeground()
        {
            return _textObject.GetForeground();
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
                _textObject.GetMargin().Left - _textObject.GetMargin().Right;
            _textObject.SetAllowWidth(_cursorXMax);
            _textObject.CheckXShift(_cursorXMax); //_textObject.SetLineXShift();

            _substrateText.SetAllowWidth(_cursorXMax);
            _substrateText.CheckXShift(_cursorXMax);

            ReplaceCursor();
            if (_textObject.GetTextAlignment().HasFlag(ItemAlignment.Right))
                MakeSelectedArea(); //_selectFrom, _selectTo);
        }

        public override void InitElements()
        {
            //adding
            AddItems(_substrateText, _selectedArea, _textObject, _cursor);
            // GetHandler().SetFocusedItem(this);

            // _cursorXMax = GetWidth() - _cursor.GetWidth() - GetPadding().Left - GetPadding().Right -
            //     _textObject.GetMargin().Left - _textObject.GetMargin().Right; //_cursorXMin;// ;
            // _textObject.SetAllowWidth(_cursorXMax);
            // _textObject.SetLineXShift();

            _textObject.SetCursorWidth(_cursor.GetWidth());
            _substrateText.SetCursorWidth(_cursor.GetWidth());
        }

        internal int GetTextWidth()
        {
            return _textObject.GetWidth();
        }

        internal int GetTextHeight()
        {
            return _textObject.GetHeight();
        }

        private void MakeSelectedArea()
        {
            MakeSelectedArea(_selectFrom, _selectTo);
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

            if (fromReal < 0)
                fromReal = 0;
            if (toReal > _cursorXMax)
                toReal = _cursorXMax;

            int width = toReal - fromReal + 1;

            int w = GetTextWidth();
            if (_textObject.GetTextAlignment().HasFlag(ItemAlignment.Right) && (w < _cursorXMax))
                _selectedArea.SetX(GetX() + GetWidth() - w + fromReal - GetPadding().Right -
                    _textObject.GetMargin().Right - _cursor.GetWidth());
            else
                _selectedArea.SetX(GetX() + GetPadding().Left + fromReal + _textObject.GetMargin().Left);
            _selectedArea.SetWidth(width);
        }

        private void UnselectText()
        {
            _isSelect = false;
            _justSelected = true;
            MakeSelectedArea(_cursorPosition, _cursorPosition);
        }

        private void CancelJustSelected()
        {
            _selectFrom = -1;// 0;
            _selectTo = -1;// 0;
            _justSelected = false;
        }

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
                _cursorPosition = fromReal;
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
                _substrateText.SetFont(inner_style.Font);
                _substrateText.SetForeground(inner_style.Foreground);
            }
        }

        private int GetLineXShift()
        {
            return _textObject.GetLineXShift();
        }

        internal void SetSubstrateText(String substrateText)
        {
            _substrateText.SetItemText(substrateText);
        }

        internal void SetSubstrateFontSize(int size)
        {
            _substrateText.SetFontSize(size);
        }

        internal void SetSubstrateFontStyle(FontStyle style)
        {
            _substrateText.SetFontStyle(style);
        }

        internal void SetSubstrateForeground(Color foreground)
        {
            _substrateText.SetForeground(foreground);
        }

        internal void SetSubstrateForeground(int r, int g, int b)
        {
            _substrateText.SetForeground(r, g, b);
        }

        internal void SeSubstratetForeground(int r, int g, int b, int a)
        {
            _substrateText.SetForeground(r, g, b, a);
        }

        internal void SetSubstrateForeground(float r, float g, float b)
        {
            _substrateText.SetForeground(r, g, b);
        }

        internal void SetSubstrateForeground(float r, float g, float b, float a)
        {
            _substrateText.SetForeground(r, g, b, a);
        }

        internal Color GetSubstrateForeground()
        {
            return _substrateText.GetForeground();
        }

        internal String GetSubstrateText()
        {
            return _substrateText.GetItemText();
        }
    }
}