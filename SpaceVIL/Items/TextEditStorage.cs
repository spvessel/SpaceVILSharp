using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Drawing;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    internal class TextEditStorage : Prototype, ITextEditable, ITextShortcuts, IDraggable
    {
        static int count = 0;
        private TextLine _textObject;
        private TextLine _substrateText;

        private Rectangle _cursor;
        private int _cursorPosition = 0;
        private Rectangle _selectedArea;
        private bool _isEditable = true;

        private int _cursorXMax = SpaceVILConstants.SizeMaxValue;

        internal Rectangle GetSelectionArea()
        {
            return _selectedArea;
        }

        private int _selectFrom = -1;
        private int _selectTo = -1;
        private bool _isSelect = false;
        private bool _justSelected = false;

        private HashSet<KeyCode> _cursorControlKeys;
        // private HashSet<KeyCode> InsteadKeyMods;
        private HashSet<KeyCode> _serviceEditKeys;

        private readonly Object textInputLock = new Object();

        private int scrollStep = 15;

        internal TextEditStorage()
        {
            _textObject = new TextLine();
            _textObject.SetRecountable(true);
            _substrateText = new TextLine();

            _cursor = new Rectangle();
            _selectedArea = new Rectangle();

            SetItemName("TextEditStorage_" + count);
            count++;

            EventMousePress += OnMousePressed;
            EventMouseClick += OnMouseClick;
            EventMouseDrag += OnDragging;
            EventKeyPress += OnKeyPress;
            EventKeyRelease += OnKeyRelease;
            EventTextInput += OnTextInput;
            EventScrollUp += OnScrollUp;
            EventScrollDown += OnScrollDown;
            // EventMouseDoubleClick += OnMouseDoubleClick;

            _cursorControlKeys = new HashSet<KeyCode>() { KeyCode.Left, KeyCode.Right, KeyCode.End, KeyCode.Home };
            // InsteadKeyMods = new HashSet<KeyCode>() {KeyCode.LeftShift, KeyCode.RightShift, KeyCode.LeftControl,
            //     KeyCode.RightControl, KeyCode.LeftAlt, KeyCode.RightAlt, KeyCode.LeftSuper, KeyCode.RightSuper};
            _serviceEditKeys = new HashSet<KeyCode>() {KeyCode.Backspace, KeyCode.Delete, KeyCode.Tab};

            undoQueue = new LinkedList<TextEditState>();
            redoQueue = new LinkedList<TextEditState>();
            undoQueue.AddFirst(new TextEditState("", 0, 0, 0, 0));

            SetCursor(EmbeddedCursor.IBeam);
            _startTime.Start();
        }

        protected internal override void SetFocused(bool value)
        {
            base.SetFocused(value);
            if (IsFocused() && _isEditable)
            {
                _cursor.SetVisible(true);
            }
            else
            {
                _cursor.SetVisible(false);
            }
        }
        
        private Stopwatch _startTime = new Stopwatch();
        private bool _isDoubleClick = false;
        private int _previousClickPos = 0;

        private void OnMouseClick(object sender, MouseArgs args)
        {
            Monitor.Enter(textInputLock);
            try
            {
                if (args.Button == MouseButton.ButtonLeft)
                {
                    int savePos = _cursorPosition;
                    if (IsPosSame())
                    {
                        if (_startTime.ElapsedMilliseconds < 500)
                        {
                            if (_isDoubleClick) //triple click here
                            {
                                SelectAll();

                                _isDoubleClick = false;
                            }
                            else //if double click
                            {
                                int[] wordBounds = FindWordBounds();

                                if (wordBounds[0] != wordBounds[1])
                                {
                                    _isSelect = true;
                                    _selectFrom = wordBounds[0];
                                    _selectTo = wordBounds[1];
                                    _cursorPosition = _selectTo;
                                    ReplaceCursor();
                                    MakeSelectedArea();
                                }

                                _isDoubleClick = true;
                            }
                        }
                        else
                        {
                            _isDoubleClick = false;
                        }
                    }
                    else
                    {
                        _isDoubleClick = false;
                    }

                    _previousClickPos = savePos;
                    _startTime.Restart();
                }
                else
                {
                    _isDoubleClick = false;
                }
            }
            finally
            {
                Monitor.Exit(textInputLock);
            }
        }

        private bool IsPosSame()
        {
            int tol = 5;
            return ((_cursorPosition - tol <= _previousClickPos) && 
                (_previousClickPos <= _cursorPosition + tol));
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
                        MakeSelectedArea();
                    }
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

            if (w < _cursorXMax)
            {
                return;
            }
            int sh = GetLineXShift();
            if (sh >= 0)
            {
                return;
            }

            int curPos = _cursor.GetX();
            int curCoord = curPos - sh;

            sh += scrollStep;
            if (sh > 0) sh = 0;

            SetLineXShift(sh);
            _cursor.SetX(curCoord + sh);

            if (_justSelected)
            {
                CancelJustSelected();
            }
            MakeSelectedArea();
        }

        private void OnScrollDown(object sender, MouseArgs args)
        {
            int w = GetTextWidth();

            if (w < _cursorXMax)
            {
                return;
            }
            int sh = GetLineXShift();
            if (w + sh <= _cursorXMax)
            {
                return;
            }

            int curPos = _cursor.GetX();
            int curCoord = curPos - sh;

            sh -= scrollStep;
            if (w + sh < _cursorXMax)
            {
                sh = _cursorXMax - w;
            }

            SetLineXShift(sh);
            _cursor.SetX(curCoord + sh);

            if (_justSelected)
            {
                CancelJustSelected();
            }
            MakeSelectedArea();
        }

        private void ReplaceCursorAccordingCoord(int realPos)
        {
            int w = GetTextWidth();
            if (_textObject.GetTextAlignment().HasFlag(ItemAlignment.Right) && (w < _cursorXMax))
            {
                realPos -= GetX() + (GetWidth() - w) - GetPadding().Right - _textObject.GetMargin().Right - _cursor.GetWidth();
            }
            else
            {
                realPos -= GetX() + GetPadding().Left + _textObject.GetMargin().Left;
            }

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
                {
                    pos = i + 1;
                }
                else
                {
                    break;
                }
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
                TextShortcutProcessor.ProcessShortcut(this, args);

                if (!_isEditable)
                {
                    return;
                }

                if (!_isSelect && _justSelected)
                {
                    CancelJustSelected();
                }

                bool isCursorControlKey = _cursorControlKeys.Contains(args.Key);
                bool hasShift = args.Mods.HasFlag(KeyMods.Shift);
                bool hasControl = args.Mods.HasFlag(CommonService.GetOsControlMod());

                if (args.Mods != 0)
                {
                    //Выделение не сбрасывается, проверяются сочетания
                    if (isCursorControlKey)
                    {
                        if (!_isSelect)
                        {
                            if (hasShift)
                            {
                                if ((args.Mods == KeyMods.Shift) || (args.Mods == (CommonService.GetOsControlMod() | KeyMods.Shift)))
                                {
                                    _isSelect = true;
                                    _selectFrom = _cursorPosition;
                                }
                            }
                        }
                        else //_isSelect
                        {
                            if (args.Mods == CommonService.GetOsControlMod())
                            {
                                UnselectText();
                                CancelJustSelected();
                            }
                        }
                    }

                    // control + delete/backspace
                    if (args.Mods == CommonService.GetOsControlMod())
                    {
                        if (!_isSelect)
                        {
                            if (args.Key == KeyCode.Backspace) //remove to left
                            {
                                int[] wordBounds = FindWordBounds();

                                if (wordBounds[0] != wordBounds[1] && _cursorPosition != wordBounds[0])
                                {
                                    _selectFrom = _cursorPosition;
                                    // _cursorPosition = wordBounds[0];
                                    // ReplaceCursor();
                                    _selectTo = wordBounds[0]; //_cursorPosition;
                                    CutText();
                                }
                                else
                                {
                                    OnBackSpaceInput();
                                }
                            }
                            else if (args.Key == KeyCode.Delete) //remove to right
                            {
                                int[] wordBounds = FindWordBounds();

                                if (wordBounds[0] != wordBounds[1] && _cursorPosition != wordBounds[1])
                                {
                                    _selectFrom = _cursorPosition;
                                    // _cursorPosition = wordBounds[1];
                                    // ReplaceCursor();
                                    _selectTo = wordBounds[1]; //_cursorPosition;
                                    CutText();
                                }
                                else
                                {
                                    OnDeleteInput();
                                }
                            }
                        }
                        else if (_isSelect && ((args.Key == KeyCode.Backspace) || (args.Key == KeyCode.Delete)))
                        {
                            CutText();
                        }
                    }

                    //alt, super ?
                }
                else
                {
                    if (_serviceEditKeys.Contains(args.Key)) //args.Key == KeyCode.Backspace || args.Key == KeyCode.Delete)
                    {
                        if (_isSelect)
                        {
                            PrivCutText();
                        }
                        else
                        {
                            if (args.Key == KeyCode.Backspace) // backspace
                            {
                                OnBackSpaceInput();
                            }
                            if (args.Key == KeyCode.Delete) // delete
                            {
                                OnDeleteInput();
                            }
                        }

                        if (args.Key == KeyCode.Tab)
                        {
                            PasteText("    ");
                        }
                    }
                    else if (_isSelect) //??? && !InsteadKeyMods.Contains(args.Key))
                    {
                        UnselectText();
                    }
                }

                if (isCursorControlKey)
                {
                    if (!args.Mods.HasFlag(KeyMods.Alt) && !args.Mods.HasFlag(KeyMods.Super))
                    {

                        if (args.Key == KeyCode.Left && _cursorPosition > 0) // arrow left
                        {
                            _cursorPosition = CheckLineFits(_cursorPosition);  //NECESSARY!

                            bool doUsual = true;

                            if (hasControl)
                            {
                                int[] wordBounds = FindWordBounds();

                                if (wordBounds[0] != wordBounds[1] && _cursorPosition != wordBounds[0])
                                {
                                    _cursorPosition = wordBounds[0];
                                    ReplaceCursor();
                                    doUsual = false;
                                }
                            }

                            if (!_justSelected && doUsual)
                            {
                                _cursorPosition--;
                                ReplaceCursor();
                            }
                        }

                        if (args.Key == KeyCode.Right && _cursorPosition < GetLettersCount()) // arrow right
                        {
                            bool doUsual = true;

                            if (hasControl)
                            {
                                int[] wordBounds = FindWordBounds();

                                if (wordBounds[0] != wordBounds[1] && _cursorPosition != wordBounds[1])
                                {
                                    _cursorPosition = wordBounds[1];
                                    ReplaceCursor();
                                    doUsual = false;
                                }
                            }

                            if (!_justSelected && doUsual)
                            {
                                _cursorPosition++;
                                ReplaceCursor();
                            }
                        }

                        if (args.Key == KeyCode.End) // end
                        {
                            _cursorPosition = GetLettersCount();
                            ReplaceCursor();
                        }

                        if (args.Key == KeyCode.Home) // home
                        {
                            _cursorPosition = 0;
                            ReplaceCursor();
                        }

                    }
                }

                if (_isSelect)
                {
                    if (_selectTo != _cursorPosition)
                    {
                        _selectTo = _cursorPosition;
                        MakeSelectedArea();
                    }
                }
            }
            finally
            {
                Monitor.Exit(textInputLock);
            }
        }

        private void OnBackSpaceInput()
        {
            if (_cursorPosition > 0) // backspace
            {
                TextEditState tes = CreateTextEditState();

                _cursorPosition--;
                PrivSetText(PrivGetText().Remove(_cursorPosition, 1), tes);
                //ReplaceCursor();
            }
        }

        private void OnDeleteInput()
        {
            if (_cursorPosition < PrivGetText().Length) // delete
            {
                TextEditState tes = CreateTextEditState();

                PrivSetText(PrivGetText().Remove(_cursorPosition, 1), tes);
                //ReplaceCursor();
            }
        }

        private int CheckLineFits(int checingPos)
        {
            if (checingPos < 0)
            {
                checingPos = 0;
            }

            int lineLength = GetLettersCount();
            if (checingPos > lineLength)
            {
                checingPos = lineLength;
            }

            return checingPos;
        }

        private int GetLettersCount()
        {
            return PrivGetText().Length;
        }

        private int CursorPosToCoord(int cPos, bool isx)
        {
            int coord = 0;
            if (_textObject.GetLetPosArray() == null) return coord;
            int letCount = _textObject.GetLetPosArray().Count;
            
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
                    SetLineXShift(-coord);
                }
                if (GetLineXShift() + coord > _cursorXMax)
                    SetLineXShift(_cursorXMax - coord);
            }

            return GetLineXShift() + coord;
        }

        private void ReplaceCursor()
        {
            int len = GetLettersCount();

            if (_cursorPosition > len)
            {
                _cursorPosition = len;
                //ReplaceCursor();
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
                TextEditState tes = CreateTextEditState();

                byte[] input = BitConverter.GetBytes(args.Character);
                string str = Encoding.UTF32.GetString(input);

                if (_isSelect || _justSelected)
                {
                    UnselectText();
                    PrivCutText();
                }
                if (_justSelected)
                {
                    CancelJustSelected();
                }

                _cursorPosition++;
                PrivSetText(PrivGetText().Insert(_cursorPosition - 1, str), tes);
                //ReplaceCursor();
            }
            finally
            {
                Monitor.Exit(textInputLock);
            }
        }

        private void PrivSetText(String text, TextEditState tes)
        {
            Monitor.Enter(textInputLock);
            try
            {
                if (_substrateText.IsVisible())
                {
                    _substrateText.SetVisible(false);
                }
                if ((text == null) || text.Equals(String.Empty))
                {
                    _substrateText.SetVisible(true);
                }

                _textObject.SetItemText(text);
                _textObject.CheckXShift(_cursorXMax);

                // _cursorPosition = PrivGetText().Length;
                AddToUndoAndReplaceCursor(tes);
            }
            finally
            {
                Monitor.Exit(textInputLock);
            }
        }

        private void AddToUndoAndReplaceCursor(TextEditState tes)
        {
                ReplaceCursor();

                if (!nothingFlag)
                {
                    redoQueue = new LinkedList<TextEditState>();
                }
                else
                {
                    nothingFlag = false;
                }
                if (undoQueue.Count > queueCapacity)
                {
                    undoQueue.RemoveLast();
                }

                tes.cursorStateAfter = _cursorPosition;
                tes.lineXShiftAfter = GetLineXShift();

                undoQueue.AddFirst(tes);
        }

        internal void SetText(String text)
        {
            if (_isSelect || _justSelected)
            {
                UnselectText();
                CancelJustSelected();
            }

            TextEditState tes = CreateTextEditState();
            tes.selectFromState = 0;
            tes.selectToState = GetLettersCount();

            PrivSetText(text, tes);
            _cursorPosition = GetLettersCount();
            ReplaceCursor();
        }

        internal String GetText()
        {
            return PrivGetText();
        }

        private String PrivGetText()
        {
            return _textObject.GetItemText();
        }

        internal virtual bool IsEditable
        {
            get { return _isEditable; }
            set
            {
                if (_isEditable == value)
                {
                    return;
                }
                _isEditable = value;

                if (_isEditable)
                {
                    _cursor.SetVisible(true);
                }
                else
                {
                    _cursor.SetVisible(false);
                }
            }
        }

        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            _cursorXMax = GetWidth() - _cursor.GetWidth() - GetPadding().Left - GetPadding().Right
                    - _textObject.GetMargin().Left - _textObject.GetMargin().Right; // _cursorXMin;// ;
            _textObject.SetAllowWidth(_cursorXMax);
            _textObject.CheckXShift(_cursorXMax);

            _substrateText.SetAllowWidth(_cursorXMax);
            _substrateText.CheckXShift(_cursorXMax);

            ReplaceCursor();
            if (_textObject.GetTextAlignment().HasFlag(ItemAlignment.Right))
            {
                //TODO WTF???
                MakeSelectedArea();
            }
        }

        public override void InitElements()
        {
            AddItems(_substrateText, _selectedArea, _textObject, _cursor);

            int scctp = _textObject.GetFontDims().lineSpacer; //[0];
            if (scctp > scrollStep)
            {
                scrollStep = scctp;
            }

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
            {
                fromPt = 0;
            }
            if (toPt == -1)
            {
                toPt = 0;
            }
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
            {
                fromReal = 0;
            }
            if (toReal > _cursorXMax)
            {
                toReal = _cursorXMax;
            }

            int width = toReal - fromReal + 1;

            int w = GetTextWidth();
            if (_textObject.GetTextAlignment().HasFlag(ItemAlignment.Right) && (w < _cursorXMax))
            {
                _selectedArea.SetX(GetX() + GetWidth() - w + fromReal - GetPadding().Right -
                    _textObject.GetMargin().Right - _cursor.GetWidth());
            }
            else
            {
                _selectedArea.SetX(GetX() + GetPadding().Left + fromReal + _textObject.GetMargin().Left);
            }
            _selectedArea.SetWidth(width);
        }

        private string PrivGetSelectedText()
        {
            Monitor.Enter(textInputLock);
            try
            {
                if (_selectFrom == -1)
                {
                    _selectFrom = 0;
                }
                if (_selectTo == -1)
                {
                    _selectTo = 0;
                }
                if (_selectFrom == _selectTo)
                {
                    return "";
                }
                string text = PrivGetText();
                int fromReal = Math.Min(_selectFrom, _selectTo);
                int toReal = Math.Max(_selectFrom, _selectTo);
                if (fromReal < 0)
                {
                    return "";
                }
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
            return PrivGetSelectedText();
        }

        private void PrivPasteText(string pasteStr)
        {
            if (!_isEditable)
            {
                return;
            }
            Monitor.Enter(textInputLock);
            try
            {
                TextEditState tes = CreateTextEditState();

                if (_isSelect)
                {
                    PrivCutText();
                }

                if ((pasteStr == null) || pasteStr.Equals(""))
                {
                    return;
                }

                string text = PrivGetText();
                string newText = text.Substring(0, _cursorPosition) + pasteStr + text.Substring(_cursorPosition);
                _cursorPosition += pasteStr.Length;
                PrivSetText(newText, tes);
                //ReplaceCursor();
            }
            finally
            {
                Monitor.Exit(textInputLock);
            }
        }

        public virtual void PasteText(string pasteStr)
        {   
            PrivPasteText(pasteStr);
        }

        private string PrivCutText()
        {
            if (!_isEditable)
            {
                return "";
            }
            Monitor.Enter(textInputLock);
            try
            {
                if (_selectFrom == -1)
                {
                    _selectFrom = 0;
                }
                if (_selectTo == -1)
                {
                    _selectTo = 0;
                }

                TextEditState tes = CreateTextEditState();

                string str = PrivGetSelectedText();
                if (_selectFrom == _selectTo)
                {
                    return str;
                }
                int fromReal = Math.Min(_selectFrom, _selectTo);
                int toReal = Math.Max(_selectFrom, _selectTo);
                _cursorPosition = fromReal;
                PrivSetText(PrivGetText().Remove(fromReal, toReal - fromReal), tes);
                ReplaceCursor();
                if (_isSelect)
                {
                    UnselectText();
                }
                CancelJustSelected();
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
            MakeSelectedArea(_cursorPosition, _cursorPosition);
        }

        private void CancelJustSelected()
        {
            _selectFrom = -1;
            _selectTo = -1;
            _justSelected = false;
        }

        public override void Clear()
        {
            SetText("");
        }

        private int GetLineXShift()
        {
            return _textObject.GetLineXShift();
        }

        private void SetLineXShift(int lineXShift)
        {
            _textObject.SetLineXShift(lineXShift);
        }

        internal bool IsBeginning()
        {
            return (_cursorPosition == 0);
        }

        public void SelectAll()
        {
            Monitor.Enter(textInputLock);
            try
            {
                _selectFrom = 0;
                _cursorPosition = GetLettersCount();
                _selectTo = _cursorPosition;
                ReplaceCursor();
                _isSelect = true;
                MakeSelectedArea();
            }
            finally
            {
                Monitor.Exit(textInputLock);
            }
        }

        private int queueCapacity = SpaceVILConstants.TextUndoCapacity;
        private bool nothingFlag = false;
        private LinkedList<TextEditState> undoQueue;
        private LinkedList<TextEditState> redoQueue;

        public void Redo()
        {
            if (redoQueue.Count == 0)
            {
                return;
            }

            TextEditState tmpText = redoQueue.First.Value;
            if (tmpText != null)
            {
                redoQueue.RemoveFirst();
                nothingFlag = true;
                TextEditState selectState = CreateTextEditState();
                SetText(tmpText.textState); //PrivSetText(tmpText.textState);

                //due to the SetText
                undoQueue.First.Value.selectFromState = selectState.selectFromState;
                undoQueue.First.Value.selectToState = selectState.selectToState;
                undoQueue.First.Value.cursorState = selectState.cursorState;
                undoQueue.First.Value.lineXShift = selectState.lineXShift;

                _cursorPosition = tmpText.cursorState;

                SetLineXShift(tmpText.lineXShift);

                if (tmpText.selectFromState != tmpText.selectToState)
                {
                    _selectFrom = tmpText.selectFromState;
                    _selectTo = tmpText.selectToState;
                    _cursorPosition = tmpText.selectToState;
                    _isSelect = true;
                    MakeSelectedArea();
                }

                undoQueue.First.Value.cursorStateAfter = _cursorPosition;
                undoQueue.First.Value.lineXShiftAfter = GetLineXShift();

                ReplaceCursor();
            }
        }

        public void Undo()
        {
            if (undoQueue.Count == 1)
            {
                return;
            }

            TextEditState tmpText = undoQueue.First.Value;
            if (tmpText != null)
            {
                undoQueue.RemoveFirst();
                if (redoQueue.Count > queueCapacity)
                {
                    redoQueue.RemoveLast();
                }
                redoQueue.AddFirst(CreateTextEditState()); //new TextEditState(tmpText.textState, tmpText.cursorState));
                redoQueue.First.Value.cursorState = tmpText.cursorStateAfter;
                redoQueue.First.Value.lineXShift = tmpText.lineXShiftAfter;

                // tmpText = undoQueue.First.Value;
                // if (tmpText != null)
                // {
                    // undoQueue.RemoveFirst();
                    nothingFlag = true;
                    SetText(tmpText.textState); //PrivSetText(tmpText.textState);

                    //due to the SetText
                    undoQueue.RemoveFirst();

                    _cursorPosition = tmpText.cursorState;

                    SetLineXShift(tmpText.lineXShift);

                    if (tmpText.selectFromState != tmpText.selectToState)
                    {
                        _selectFrom = tmpText.selectFromState;
                        _selectTo = tmpText.selectToState;
                        _cursorPosition = tmpText.selectToState;
                        _isSelect = true;
                        MakeSelectedArea();
                    }

                    // undoQueue.First.Value.cursorState = _cursorPosition;
                    ReplaceCursor();
                // }
            }
        }

        private TextEditState CreateTextEditState()
        {
            int selectFromState = 0;
            int selectToState = 0;
            if (_isSelect)
            {
                selectFromState = _selectFrom;
                selectToState = _selectTo;
            }

            TextEditState tes = new TextEditState(GetText(), _cursorPosition, GetLineXShift(),
                        selectFromState, selectToState);
            
            return tes;
        }

        internal void SetSubstrateText(String substrateText)
        {
            _substrateText.SetItemText(substrateText);
        }

        internal String GetSubstrateText()
        {
            return _substrateText.GetItemText();
        }

        internal void AppendText(String text)
        {
            UnselectText();
            CancelJustSelected();
            _cursorPosition = GetLettersCount();
            PasteText(text);
        }

        private int[] FindWordBounds()
        {
            Regex patternWordBounds = new Regex(@"\W|_");
            //С положением курсора должно быть все в порядке, не нужно проверять вроде бы
            String lineText = PrivGetText();
            int index = _cursorPosition;

            String testString = lineText.Substring(index);
            MatchCollection matcher = patternWordBounds.Matches(testString);

            int begPt = 0;
            int endPt = GetLettersCount();

            if (matcher.Count > 0)
            {
                foreach (Match match in matcher)
                {
                    endPt = index + match.Index;
                    break;
                }
            }

            testString = lineText.Substring(0, index);
            matcher = patternWordBounds.Matches(testString);

            if (matcher.Count > 0)
            {
                foreach (Match match in matcher)
                {
                    begPt = match.Index + 1;
                }
            }

            return new int[] { begPt, endPt };
        }

        //----------------------------------------------------------------------
        internal class TextEditState
        {
            internal String textState;
            internal int cursorState;
            internal int cursorStateAfter;
            internal int lineXShift;
            internal int lineXShiftAfter;
            internal int selectFromState;
            internal int selectToState;

            internal TextEditState(String textState, int cursorState, int lineXShift, int selectFromState, int selectToState)
            {
                this.textState = textState;
                this.cursorState = cursorState;
                this.lineXShift = lineXShift;
                this.selectFromState = selectFromState;
                this.selectToState = selectToState;
                
                this.cursorStateAfter = 0;
                this.lineXShiftAfter = 0;
            }
        }

        //decorations-------------------------------------------------------------------------------------------------------
        internal void SetTextAlignment(ItemAlignment alignment)
        {
            ItemAlignment ial;
            if (alignment.HasFlag(ItemAlignment.Right))
            {
                ial = ItemAlignment.Right | ItemAlignment.VCenter;
            }
            else
            {
                ial = ItemAlignment.Left | ItemAlignment.VCenter;
            }
            _textObject.SetTextAlignment(ial);
            _substrateText.SetTextAlignment(ial);
        }

        internal ItemAlignment GetTextAlignment()
        {
            return _textObject.GetTextAlignment();
        }

        internal void SetTextMargin(Indents margin)
        {
            _textObject.SetMargin(margin);
            _substrateText.SetMargin(margin);
        }

        internal Indents GetTextMargin() {
            return _textObject.GetMargin();
        }

        internal void SetFont(Font font)
        {
            _textObject.SetFont(font);
            _substrateText.SetFont(FontService.ChangeFontFamily(font.FontFamily, _substrateText.GetFont())); //new Font(font.FontFamily, _substrate_text.GetFont().Size, _substrate_text.GetFont().Style));
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

        internal void SetForeground(Color color)
        {
            _textObject.SetForeground(color);
        }
        internal Color GetForeground()
        {
            return _textObject.GetForeground();
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

        internal Color GetSubstrateForeground()
        {
            return _substrateText.GetForeground();
        }
        
        //style
        public override void SetStyle(Style style)
        {
            if (style == null)
            {
                return;
            }
            base.SetStyle(style);
            SetForeground(style.Foreground);
            SetFont(style.Font);
            SetTextAlignment(style.TextAlignment);

            Style innerStyle = style.GetInnerStyle("selection");
            if (innerStyle != null)
            {
                _selectedArea.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("cursor");
            if (innerStyle != null)
            {
                _cursor.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("substrate");
            if (innerStyle != null)
            {
                _substrateText.SetFont(innerStyle.Font);
                _substrateText.SetForeground(innerStyle.Foreground);
            }
        }
    }
}