using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Drawing;
using System.Text;
using System.Threading;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    internal class TextBlock : Prototype, ITextEditable, IDraggable, ITextShortcuts, IFreeLayout, ITextWrap//, ITextContainer
    {
        internal EventCommonMethod TextChanged;
        internal EventCommonMethod CursorChanged;

        public override void Release()
        {
            TextChanged = null;
        }

        private static int count = 0;
        private Rectangle _cursor;
        private SpaceVIL.Core.Point _cursorPosition = new SpaceVIL.Core.Point(0, 0);
        private CustomSelector _selectedArea;

        private TextureStorage _textureStorage;

        private bool _isEditable = true;

        private SpaceVIL.Core.Point _selectFrom = new SpaceVIL.Core.Point(-1, 0);
        private SpaceVIL.Core.Point _selectTo = new SpaceVIL.Core.Point(-1, 0);
        private bool _isSelect = false;
        private bool _justSelected = false;

        private HashSet<KeyCode> _cursorControlKeys;
        // private HashSet<KeyCode> _insteadKeyMods;
        private HashSet<KeyCode> _serviceEditKeys;

        private int scrollXStep = 15;

        internal TextBlock()
        {
            SetItemName("TextBlock_" + count);
            count++;

            _textureStorage = new TextureStorage();
            _cursor = new Rectangle();
            _selectedArea = new CustomSelector();

            EventMousePress += OnMousePressed;
            EventMouseClick += OnMouseClick;
            // EventMouseDoubleClick += OnDoubleClick;
            EventMouseDrag += OnDragging;
            EventKeyPress += OnKeyPress;
            EventKeyRelease += OnKeyRelease;
            EventTextInput += OnTextInput;
            EventScrollUp += OnScrollUp;
            EventScrollDown += OnScrollDown;

            _cursorControlKeys = new HashSet<KeyCode>() {KeyCode.Left, KeyCode.Right, KeyCode.End,
                KeyCode.Home, KeyCode.Up, KeyCode.Down};
            // _insteadKeyMods = new HashSet<KeyCode>() {KeyCode.LeftShift, KeyCode.RightShift, KeyCode.LeftControl,
            //    KeyCode.RightControl, KeyCode.LeftAlt, KeyCode.RightAlt, KeyCode.LeftSuper, KeyCode.RightSuper};
            _serviceEditKeys = new HashSet<KeyCode>() {KeyCode.Backspace, KeyCode.Delete, KeyCode.Enter,
                KeyCode.NumpadEnter, KeyCode.Tab};

            _cursor.SetHeight(_textureStorage.GetCursorHeight());

            undoQueue = new LinkedList<TextBlockState>();
            redoQueue = new LinkedList<TextBlockState>();
            undoQueue.AddFirst(new TextBlockState(GetText(), _cursorPosition.X, _cursorPosition.Y));

            SetCursor(EmbeddedCursor.IBeam);

            _isWrapText = false;
        }

        private Stopwatch _startTime = new Stopwatch();
        private bool _isDoubleClick = false;
        private SpaceVIL.Core.Point _previousClickPos = new SpaceVIL.Core.Point();

        private void OnMousePressed(object sender, MouseArgs args)
        {
            Monitor.Enter(_textureStorage.textInputLock);
            try
            {
                if (args.Button == MouseButton.ButtonLeft)
                {
                    ReplaceCursorAccordingCoord(new SpaceVIL.Core.Point(args.Position.GetX(), args.Position.GetY()));
                    if (_isSelect)
                    {
                        UnselectText();
                        CancelJustSelected();
                    }
                }
            }
            finally
            {
                Monitor.Exit(_textureStorage.textInputLock);
            }
        }

        private void OnMouseClick(object sender, MouseArgs args)
        {
            Monitor.Enter(_textureStorage.textInputLock);
            try
            {
                if (args.Button == MouseButton.ButtonLeft)
                {
                    SpaceVIL.Core.Point savePos = new SpaceVIL.Core.Point(_cursorPosition);
                    if (IsPosSame())
                    {
                        if (_startTime.ElapsedMilliseconds < 500)
                        {
                            if (_isDoubleClick) // && _startTime.ElapsedMilliseconds < 500)
                            {
                                _isSelect = true;
                                _selectFrom = new SpaceVIL.Core.Point(0, _cursorPosition.Y);
                                _selectTo = new SpaceVIL.Core.Point(GetLettersCountInLine(_cursorPosition.Y), _cursorPosition.Y);
                                _cursorPosition = new SpaceVIL.Core.Point(_selectTo.X, _selectTo.Y);
                                ReplaceCursor();
                                MakeSelectedArea();

                                _isDoubleClick = false;
                            }
                            else //if double click
                            {
                                int[] wordBounds = _textureStorage.FindWordBounds(_cursorPosition);

                                if (wordBounds[0] != wordBounds[1])
                                {
                                    _isSelect = true;
                                    _selectFrom = new SpaceVIL.Core.Point(wordBounds[0], _cursorPosition.Y);
                                    _selectTo = new SpaceVIL.Core.Point(wordBounds[1], _cursorPosition.Y);
                                    _cursorPosition = new SpaceVIL.Core.Point(_selectTo.X, _selectTo.Y);
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
                Monitor.Exit(_textureStorage.textInputLock);
            }
        }

        // private void OnDoubleClick(object sender, MouseArgs args)
        // {
        //     Monitor.Enter(_textureStorage.textInputLock);
        //     try
        //     {
        //         if (args.Button == MouseButton.ButtonLeft)
        //         {
        //             ReplaceCursorAccordingCoord(new SpaceVIL.Core.Point(args.Position.GetX(), args.Position.GetY()));
        //             if (_isSelect)
        //             {
        //                 UnselectText();
        //                 CancelJustSelected();
        //             }
        //             int[] wordBounds = _textureStorage.FindWordBounds(_cursorPosition);

        //             if (wordBounds[0] != wordBounds[1])
        //             {
        //                 _isSelect = true;
        //                 _selectFrom = new SpaceVIL.Core.Point(wordBounds[0], _cursorPosition.Y);
        //                 _selectTo = new SpaceVIL.Core.Point(wordBounds[1], _cursorPosition.Y);
        //                 _cursorPosition = new SpaceVIL.Core.Point(_selectTo.X, _selectTo.Y);
        //                 ReplaceCursor();
        //                 MakeSelectedArea();
        //             }

        //             _startTime.Restart();
        //             _isDoubleClick = true;
        //         }
        //         else
        //         {
        //             _isDoubleClick = false;
        //         }
        //     }
        //     finally
        //     {
        //         Monitor.Exit(_textureStorage.textInputLock);
        //     }
        // }

        private bool IsPosSame()
        {
            SpaceVIL.Core.Point pos1 = new SpaceVIL.Core.Point(_cursorPosition);
            SpaceVIL.Core.Point pos2 = new SpaceVIL.Core.Point(_previousClickPos);
            int tol = 5;
            if (pos1.Y != pos2.Y)
            {
                return false;
            }
            return (pos1.X - tol <= pos2.X && pos2.X <= pos1.X + tol);
        }

        private void OnDragging(object sender, MouseArgs args)
        {
            Monitor.Enter(_textureStorage.textInputLock);
            try
            {
                if (args.Button == MouseButton.ButtonLeft)
                {
                    ReplaceCursorAccordingCoord(new SpaceVIL.Core.Point(args.Position.GetX(), args.Position.GetY()));
                    if (!_isSelect)
                    {
                        _isSelect = true;
                        _selectFrom = new SpaceVIL.Core.Point(_cursorPosition.X, _cursorPosition.Y);
                    }
                    else
                    {
                        _selectTo = new SpaceVIL.Core.Point(_cursorPosition.X, _cursorPosition.Y);
                        MakeSelectedArea();
                    }
                    _isDoubleClick = false;
                }
            }
            finally
            {
                Monitor.Exit(_textureStorage.textInputLock);
            }
        }

        internal int GetScrollYStep()
        {
            return _textureStorage.GetScrollStep();
        }
        internal int GetScrollXStep()
        {
            return scrollXStep;
        }
        internal int GetScrollYOffset()
        {
            return _textureStorage.GetScrollYOffset();
        }
        internal void SetScrollYOffset(int offset)
        {
            int oldOff = _textureStorage.GetScrollYOffset();
            _textureStorage.SetScrollYOffset(offset);
            int diff = offset - oldOff;
            if (_justSelected)
            {
                CancelJustSelected();
            }
            MakeSelectedArea();
            _cursor.SetY(_cursor.GetY() + diff);
        }
        internal int GetScrollXOffset()
        {
            return _textureStorage.GetScrollXOffset();
        }
        internal void SetScrollXOffset(int offset)
        {
            int oldOff = _textureStorage.GetScrollXOffset();
            _textureStorage.SetScrollXOffset(offset);
            int diff = offset - oldOff;
            if (_justSelected)
            {
                CancelJustSelected();
            }
            MakeSelectedArea();
            _cursor.SetX(_cursor.GetX() + diff);
        }

        private void OnScrollUp(object sender, MouseArgs args)
        {
            _cursor.SetY(_textureStorage.ScrollBlockUp(_cursor.GetY()));

            if (_justSelected)
            {
                CancelJustSelected();
            }
            MakeSelectedArea();
        }

        private void OnScrollDown(object sender, MouseArgs args)
        {
            _cursor.SetY(_textureStorage.ScrollBlockDown(_cursor.GetY()));

            if (_justSelected)
            {
                CancelJustSelected();
            }
            MakeSelectedArea();
        }

        private void ReplaceCursorAccordingCoord(SpaceVIL.Core.Point realPos)
        {
            _cursorPosition = _textureStorage.ReplaceCursorAccordingCoord(realPos);
            ReplaceCursor();
        }

        private void OnKeyRelease(object sender, KeyArgs args)
        {

        }
        private void OnKeyPress(object sender, KeyArgs args)
        {
            Monitor.Enter(_textureStorage.textInputLock);
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
                                    _selectFrom = new SpaceVIL.Core.Point(_cursorPosition.X, _cursorPosition.Y);
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
                                int[] wordBounds = _textureStorage.FindWordBounds(_cursorPosition);

                                if (wordBounds[0] != wordBounds[1] && _cursorPosition.X != wordBounds[0])
                                {
                                    _selectFrom = new SpaceVIL.Core.Point(_cursorPosition);
                                    _cursorPosition = new SpaceVIL.Core.Point(wordBounds[0], _cursorPosition.Y);
                                    // ReplaceCursor();
                                    _selectTo = new SpaceVIL.Core.Point(_cursorPosition);
                                    CutText();
                                }
                                else
                                {
                                    OnBackSpaceInput(args);
                                }
                            }
                            else if (args.Key == KeyCode.Delete) //remove to right
                            {
                                int[] wordBounds = _textureStorage.FindWordBounds(_cursorPosition);

                                if (wordBounds[0] != wordBounds[1] && _cursorPosition.X != wordBounds[1])
                                {
                                    _selectFrom = new SpaceVIL.Core.Point(_cursorPosition);
                                    _cursorPosition = new SpaceVIL.Core.Point(wordBounds[1], _cursorPosition.Y);
                                    // ReplaceCursor();
                                    _selectTo = new SpaceVIL.Core.Point(_cursorPosition);
                                    CutText();
                                }
                                else
                                {
                                    OnDeleteInput(args);
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
                    if (_serviceEditKeys.Contains(args.Key))
                    {
                        if (_isSelect)
                        {
                            CutText();
                        }
                        else
                        {
                            _cursorPosition = _textureStorage.CheckLineFits(_cursorPosition);
                            if (args.Key == KeyCode.Backspace) //backspace
                            {
                                OnBackSpaceInput(args);
                            }
                            if (args.Key == KeyCode.Delete) //delete
                            {
                                OnDeleteInput(args);
                            }
                        }

                        if (args.Key == KeyCode.Enter || args.Key == KeyCode.NumpadEnter) //enter
                        {
                            _textureStorage.BreakLine(_cursorPosition);
                            _cursorPosition.Y++;
                            _cursorPosition.X = 0;

                            //// ReplaceCursor();
                            AddToUndoAndReplaceCursor();
                        }

                        if (args.Key == KeyCode.Tab)
                        {
                            PasteText("    "); //PrivPasteText
                        }
                    }
                    else if (_isSelect) // && !_insteadKeyMods.Contains(args.Key)) //кажется, вторая проверка лишняя теперь
                    {
                        UnselectText();
                    }
                }

                if (isCursorControlKey)
                {
                    if (!args.Mods.HasFlag(KeyMods.Alt) && !args.Mods.HasFlag(KeyMods.Super))
                    {
                        if (args.Key == KeyCode.Left) //arrow left
                        {
                            _cursorPosition = _textureStorage.CheckLineFits(_cursorPosition);  //NECESSARY!

                            bool doUsual = true;

                            if (hasControl)
                            {
                                int[] wordBounds = _textureStorage.FindWordBounds(_cursorPosition);

                                if (wordBounds[0] != wordBounds[1] && _cursorPosition.X != wordBounds[0])
                                {
                                    _cursorPosition = new SpaceVIL.Core.Point(wordBounds[0], _cursorPosition.Y);
                                    ReplaceCursor();
                                    doUsual = false;
                                }
                            }

                            if (!_justSelected && doUsual)
                            {
                                if (_cursorPosition.X > 0)
                                {
                                    _cursorPosition.X--;
                                }
                                else if (_cursorPosition.Y > 0)
                                {
                                    _cursorPosition.Y--;
                                    _cursorPosition.X = GetLettersCountInLine(_cursorPosition.Y);
                                }
                                ReplaceCursor();
                            }
                        }

                        if (args.Key == KeyCode.Right) //arrow right
                        {
                            bool doUsual = true;

                            if (hasControl)
                            {
                                int[] wordBounds = _textureStorage.FindWordBounds(_cursorPosition);

                                if (wordBounds[0] != wordBounds[1] && _cursorPosition.X != wordBounds[1])
                                {
                                    _cursorPosition = new SpaceVIL.Core.Point(wordBounds[1], _cursorPosition.Y);
                                    ReplaceCursor();
                                    doUsual = false;
                                }
                            }

                            if (!_justSelected && doUsual)
                            {
                                if (_cursorPosition.X < GetLettersCountInLine(_cursorPosition.Y))
                                {
                                    _cursorPosition.X++;
                                }
                                else if (_cursorPosition.Y < _textureStorage.GetLinesCount() - 1)
                                {
                                    _cursorPosition.Y++;
                                    _cursorPosition.X = 0;
                                }
                                ReplaceCursor();
                            }

                        }

                        if (args.Key == KeyCode.Up) //arrow up
                        {
                            if (!_justSelected)
                            {
                                if (_cursorPosition.Y > 0)
                                {
                                    _cursorPosition.Y--;
                                }
                                //?????
                                ReplaceCursor();
                            }
                        }

                        if (args.Key == KeyCode.Down) //arrow down
                        {
                            if (!_justSelected)
                            {
                                if (_cursorPosition.Y < _textureStorage.GetLinesCount() - 1)
                                {
                                    _cursorPosition.Y++;
                                }
                                //?????
                                ReplaceCursor();
                            }
                        }

                        if (args.Key == KeyCode.End) //end
                        {
                            bool doUsual = true;

                            if (hasControl)
                            {
                                int lineNum = _textureStorage.GetLinesCount() - 1;
                                _cursorPosition = new SpaceVIL.Core.Point(GetLettersCountInLine(lineNum), lineNum);
                                ReplaceCursor();
                                doUsual = false;
                            }

                            if (doUsual)
                            {
                                _cursorPosition.X = GetLettersCountInLine(_cursorPosition.Y);
                                ReplaceCursor();
                            }
                        }

                        if (args.Key == KeyCode.Home) //home
                        {
                            bool doUsual = true;

                            if (hasControl)
                            {
                                _cursorPosition = new SpaceVIL.Core.Point(0, 0);
                                ReplaceCursor();
                                doUsual = false;
                            }

                            if (doUsual)
                            {
                                _cursorPosition.X = 0;
                                ReplaceCursor();
                            }
                        }
                    }
                }
                // ReplaceCursor();
                if (_isSelect)
                {
                    if (!_selectTo.Equals(_cursorPosition))
                    {
                        _selectTo = _cursorPosition;
                        MakeSelectedArea();
                    }
                }
            }
            finally
            {
                Monitor.Exit(_textureStorage.textInputLock);
            }
        }

        private void OnBackSpaceInput(KeyArgs args)
        {
            if (_cursorPosition.X > 0)
            {
                string sb = _textureStorage.GetTextInLine(_cursorPosition.Y);
                _cursorPosition.X--;
                SetTextInLine(sb.Remove(_cursorPosition.X, 1));
            }
            else if (_cursorPosition.Y > 0)
            {
                _cursorPosition.Y--;
                _cursorPosition.X = GetLettersCountInLine(_cursorPosition.Y);
                _textureStorage.CombineLinesOrRemoveLetter(_cursorPosition, args.Key); //_textureStorage.CombineLines(_cursorPosition); //.Y);
                AddToUndoAndReplaceCursor();
            }
            // ReplaceCursor();
        }

        private void OnDeleteInput(KeyArgs args)
        {
            if (_cursorPosition.X < GetLettersCountInLine(_cursorPosition.Y))
            {
                SetTextInLine(_textureStorage.GetTextInLine(_cursorPosition.Y).Remove(_cursorPosition.X, 1));
            }
            else if (_cursorPosition.Y < _textureStorage.GetLinesCount() - 1)
            {
                _textureStorage.CombineLinesOrRemoveLetter(_cursorPosition, args.Key); //_textureStorage.CombineLines(_cursorPosition); //.Y);
                AddToUndoAndReplaceCursor();
            }
        }

        private void OnTextInput(object sender, TextInputArgs args)
        {
            if (!_isEditable)
            {
                return;
            }
            Monitor.Enter(_textureStorage.textInputLock);
            try
            {
                ignoreSetInLine = true;
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

                _cursorPosition = _textureStorage.CheckLineFits(_cursorPosition);

                string sb = _textureStorage.GetTextInLine(_cursorPosition.Y);
                _cursorPosition.X++;
                SetTextInLine(sb.Insert(_cursorPosition.X - 1, str));

                AddToUndoAndReplaceCursor();
            }
            finally
            {
                Monitor.Exit(_textureStorage.textInputLock);
            }
        }

        private void ReplaceCursor()
        {
            Monitor.Enter(_textureStorage.textInputLock);
            try
            {
                // _cursorPosition = _textureStorage.CheckLineFits(_cursorPosition);
                SpaceVIL.Core.Point pos = AddXYShifts(_cursorPosition);
                _cursor.SetX(pos.X);
                _cursor.SetY(pos.Y - GetLineSpacer() / 2 + 1);

                //invoke cancelJustSelected
                CursorChanged?.Invoke();
            }
            finally
            {
                Monitor.Exit(_textureStorage.textInputLock);
            }
        }

        internal void SetLineSpacer(int lineSpacer)
        {
            _textureStorage.SetLineSpacer(lineSpacer);
            _cursor.SetHeight(_textureStorage.GetCursorHeight());
        }

        internal int GetLineSpacer()
        {
            return _textureStorage.GetLineSpacer();
        }

        internal string GetText()
        {
            return _textureStorage.GetWholeText();
        }

        internal void SetTextAlignment(ItemAlignment alignment)
        {
            //Ignore all changes for yet
        }

        internal void SetTextMargin(Indents margin)
        {
            _textureStorage.SetTextMargin(margin);
            _cursorPosition = _textureStorage.CheckLineFits(_cursorPosition); //???
            ReplaceCursor(); //???
        }

        internal Indents GetTextMargin()
        {
            return _textureStorage.GetTextMargin();
        }

        internal void SetFont(Font font)
        {
            _textureStorage.SetFont(font);
            _cursor.SetHeight(_textureStorage.GetCursorHeight());
            _cursorPosition = _textureStorage.CheckLineFits(_cursorPosition); //???
            ReplaceCursor();
        }

        internal Font GetFont()
        {
            return _textureStorage.GetFont();
        }

        internal void SetText(String text)
        {
            Monitor.Enter(_textureStorage.textInputLock);
            try
            {
                if (_isSelect)
                {
                    UnselectText();
                }
                if (_justSelected)
                {
                    CancelJustSelected();
                }

                _cursorPosition = _textureStorage.SetText(text); //, _cursorPosition);
                // ReplaceCursor();
                AddToUndoAndReplaceCursor();
            }
            finally
            {
                Monitor.Exit(_textureStorage.textInputLock);
            }
        }

        private void SetTextInLine(String text)
        {
            _textureStorage.SetTextInLine(text, _cursorPosition); //.Y);

            if (!ignoreSetInLine)
            {
                AddToUndoAndReplaceCursor();
            }
            else
            {
                ignoreSetInLine = false;
            }
        }

        internal int GetTextWidth()
        {
            return _textureStorage.GetWidth();
        }

        internal int GetTextHeight()
        {
            return _textureStorage.GetTextHeight();
        }
        internal void SetForeground(Color color)
        {
            _textureStorage.SetForeground(color);
        }

        internal Color GetForeground()
        {
            return _textureStorage.GetForeground();
        }
        internal bool IsEditable
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

        public override void InitElements()
        {
            _cursor.SetHeight(_textureStorage.GetCursorHeight());
            AddItems(_selectedArea, _textureStorage, _cursor);
            _textureStorage.InitLines(_cursor.GetWidth());
            if (IsWrapText())
            {
                ReorganizeText();
            }
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

        private int GetLettersCountInLine(int lineNum)
        {
            return _textureStorage.GetLettersCountInLine(lineNum);
        }

        private void MakeSelectedArea()
        {
            MakeSelectedArea(_selectFrom, _selectTo);
        }

        private void MakeSelectedArea(SpaceVIL.Core.Point from, SpaceVIL.Core.Point to)
        {
            if (from.X == to.X && from.Y == to.Y)
            {
                _selectedArea.SetRectangles(null);
                return;
            }

            List<SpaceVIL.Core.Point> selectionRectangles;

            SpaceVIL.Core.Point fromReal, toReal;
            List<SpaceVIL.Core.Point> listPt = RealFromTo(from, to);
            fromReal = listPt[0];
            toReal = listPt[1];

            selectionRectangles = _textureStorage.SelectedArrays(fromReal, toReal);

            _selectedArea.SetRectangles(selectionRectangles);
        }

        private List<SpaceVIL.Core.Point> RealFromTo(SpaceVIL.Core.Point from, SpaceVIL.Core.Point to)
        {
            List<SpaceVIL.Core.Point> ans = new List<SpaceVIL.Core.Point>();
            SpaceVIL.Core.Point fromReal, toReal;
            if (from.Y == to.Y)
            {
                if (from.X < to.X)
                {
                    fromReal = from;
                    toReal = to;
                }
                else
                {
                    fromReal = to;
                    toReal = from;
                }
            }
            else
            {
                if (from.Y < to.Y)
                {
                    fromReal = from;
                    toReal = to;
                }
                else
                {
                    fromReal = to;
                    toReal = from;
                }
            }

            ans.Add(fromReal);
            ans.Add(toReal);
            return ans;
        }

        private SpaceVIL.Core.Point AddXYShifts(SpaceVIL.Core.Point point)
        {
            return _textureStorage.AddXYShifts(point);
        }

        private string PrivGetSelectedText()
        {
            Monitor.Enter(_textureStorage.textInputLock);
            try
            {
                if (_selectFrom.X == -1 || _selectTo.X == -1)
                {
                    return "";
                }
                _selectFrom = _textureStorage.CheckLineFits(_selectFrom);
                _selectTo = _textureStorage.CheckLineFits(_selectTo);
                if (_selectFrom.X == _selectTo.X && _selectFrom.Y == _selectTo.Y)
                {
                    return "";
                }
                StringBuilder sb = new StringBuilder();
                List<SpaceVIL.Core.Point> listPt = RealFromTo(_selectFrom, _selectTo);
                SpaceVIL.Core.Point fromReal = listPt[0];
                SpaceVIL.Core.Point toReal = listPt[1];

                string stmp;
                if (fromReal.Y == toReal.Y)
                {
                    stmp = _textureStorage.GetTextInLine(fromReal.Y);
                    sb.Append(stmp.Substring(fromReal.X, toReal.X - fromReal.X));
                    return sb.ToString();
                }

                _textureStorage.GetSelectedText(sb, fromReal, toReal);

                // if (fromReal.X >= GetLettersCountInLine(fromReal.Y))
                //     sb.Append("\n");
                // else
                // {
                //     stmp = _textureStorage.GetTextInLine(fromReal.Y);
                //     sb.Append(stmp.Substring(fromReal.X) + "\n");
                // }
                // for (int i = fromReal.Y + 1; i < toReal.Y; i++)
                // {
                //     stmp = _textureStorage.GetTextInLine(i);
                //     sb.Append(stmp + "\n");
                // }

                // stmp = _textureStorage.GetTextInLine(toReal.Y);
                // sb.Append(stmp.Substring(0, toReal.X));

                return sb.ToString();
            }
            finally
            {
                Monitor.Exit(_textureStorage.textInputLock);
            }
        }

        public string GetSelectedText()
        {
            return PrivGetSelectedText();
        }

        private void PrivPasteText(string pasteStr)
        {
            Monitor.Enter(_textureStorage.textInputLock);
            try
            {
                if (_isSelect)
                {
                    PrivCutText();
                }
                if (pasteStr == null || pasteStr.Equals(""))
                {
                    return;
                }

                _cursorPosition = _textureStorage.CheckLineFits(_cursorPosition);
                _cursorPosition = _textureStorage.PasteText(pasteStr, _cursorPosition);

                // ReplaceCursor();
                AddToUndoAndReplaceCursor();
            }
            finally
            {
                Monitor.Exit(_textureStorage.textInputLock);
            }
        }

        public void PasteText(string text)
        {
            if (!_isEditable)
            {
                return;
            }
            if (text != null)
            {
                PrivPasteText(text);
            }
        }

        private string PrivCutText()
        {
            Monitor.Enter(_textureStorage.textInputLock);
            try
            {
                if (_selectFrom.X == -1 || _selectTo.X == -1)
                {
                    return "";
                }
                string str = PrivGetSelectedText();
                _selectFrom = _textureStorage.CheckLineFits(_selectFrom);
                _selectTo = _textureStorage.CheckLineFits(_selectTo);
                if (_selectFrom.X == _selectTo.X && _selectFrom.Y == _selectTo.Y)
                {
                    return "";
                }
                List<SpaceVIL.Core.Point> listPt = RealFromTo(_selectFrom, _selectTo);
                SpaceVIL.Core.Point fromReal = listPt[0];
                SpaceVIL.Core.Point toReal = listPt[1];

                _textureStorage.CutText(fromReal, toReal);

                _cursorPosition = new SpaceVIL.Core.Point(fromReal.X, fromReal.Y);
                if (_isSelect)
                {
                    UnselectText();
                }
                CancelJustSelected();
                ReplaceCursor();
                return str;
            }
            finally
            {
                Monitor.Exit(_textureStorage.textInputLock);
            }
        }

        public string CutText()
        {
            if (!_isEditable)
            {
                return "";
            }
            String ans = PrivCutText();
            AddToUndoAndReplaceCursor();
            return ans;
        }

        private void UnselectText()
        {
            _isSelect = false;
            _justSelected = true;
            MakeSelectedArea(new SpaceVIL.Core.Point(_cursorPosition.X, _cursorPosition.Y), new SpaceVIL.Core.Point(_cursorPosition.X, _cursorPosition.Y));
        }

        private void CancelJustSelected()
        {
            _selectFrom.X = -1;
            _selectFrom.Y = 0;
            _selectTo.X = -1;
            _selectTo.Y = 0;
            _justSelected = false;
        }

        public override void Clear()
        {
            ClearText();
        }

        internal void ClearText()
        {
            _textureStorage.Clear();
            _cursorPosition.X = 0;
            _cursorPosition.Y = 0;
            if (_isSelect)
            {
                UnselectText();
            }
            if (_justSelected)
            {
                CancelJustSelected();
            }

            // ReplaceCursor();
            AddToUndoAndReplaceCursor();
        }

        public void SelectAll()
        {
            Monitor.Enter(_textureStorage.textInputLock);
            try
            {
                _selectFrom.X = 0;
                _selectFrom.Y = 0;
                _cursorPosition.Y = _textureStorage.GetLinesCount() - 1;
                _cursorPosition.X = GetLettersCountInLine(_cursorPosition.Y);
                _selectTo = new SpaceVIL.Core.Point(_cursorPosition.X, _cursorPosition.Y);
                ReplaceCursor();
                _isSelect = true;
                MakeSelectedArea();
            }
            finally
            {
                Monitor.Exit(_textureStorage.textInputLock);
            }
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

            _textureStorage.SetLineContainerAlignment(style.TextAlignment);

            Style inner_style = style.GetInnerStyle("selection");
            if (inner_style != null)
            {
                _selectedArea.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("cursor");
            if (inner_style != null)
            {
                _cursor.SetStyle(inner_style);
                if (_cursor.GetHeight() == 0)
                {
                    _cursor.SetHeight(_textureStorage.GetCursorHeight());
                }
            }
        }

        private LinkedList<TextBlockState> undoQueue;
        private LinkedList<TextBlockState> redoQueue;
        private bool nothingFlag = false;
        private int queueCapacity = SpaceVILConstants.TextUndoCapacity;
        private bool ignoreSetInLine = false;

        public void Redo()
        {
            if (redoQueue.Count == 0)
            {
                return;
            }

            TextBlockState tmpText = redoQueue.First.Value;
            if (tmpText != null)
            {
                redoQueue.RemoveFirst();
                nothingFlag = true;

                SetText(tmpText.textState);
                _cursorPosition = new SpaceVIL.Core.Point(tmpText.cursorStateX, tmpText.cursorStateY);
                undoQueue.First.Value.cursorStateX = _cursorPosition.X;
                undoQueue.First.Value.cursorStateY = _cursorPosition.Y;
                //TODO here reverse
                if (IsWrapText())
                {
                    _cursorPosition = _textureStorage.RealCursorPosToWrap(_cursorPosition);
                }
                ReplaceCursor();
            }
        }

        public void Undo()
        {
            if (undoQueue.Count == 1)
            {
                return;
            }

            TextBlockState tmpText = undoQueue.First.Value;
            if (tmpText != null)
            {
                undoQueue.RemoveFirst();
                if (redoQueue.Count > queueCapacity)
                {
                    redoQueue.RemoveLast();
                }
                redoQueue.AddFirst(new TextBlockState(tmpText.textState, tmpText.cursorStateX, tmpText.cursorStateY));

                tmpText = undoQueue.First.Value;
                if (tmpText != null)
                {
                    undoQueue.RemoveFirst();
                    nothingFlag = true;

                    SetText(tmpText.textState);
                    _cursorPosition = new SpaceVIL.Core.Point(tmpText.cursorStateX, tmpText.cursorStateY);
                    undoQueue.First.Value.cursorStateX = _cursorPosition.X;
                    undoQueue.First.Value.cursorStateY = _cursorPosition.Y;
                    //TODO here reverse
                    if (IsWrapText())
                    {
                        _cursorPosition = _textureStorage.RealCursorPosToWrap(_cursorPosition);
                    }
                    ReplaceCursor();
                }
            }
        }

        private void AddToUndoAndReplaceCursor()
        {
            ReplaceCursor();
            if (!nothingFlag)
            {
                redoQueue = new LinkedList<TextBlockState>();
            }
            else
            {
                nothingFlag = false;
            }

            if (undoQueue.Count > queueCapacity)
            {
                undoQueue.RemoveLast();
            }
            //TODO here forward
            SpaceVIL.Core.Point realPos = new SpaceVIL.Core.Point(_cursorPosition.X, _cursorPosition.Y);
            if (IsWrapText())
            {
                realPos = _textureStorage.WrapCursorPosToReal(_cursorPosition);
            }
            TextBlockState tbs = new TextBlockState(GetText(), _cursorPosition.X, _cursorPosition.Y);
            // if (_isSelect) {
            //     tbs.fromSelectState = new SpaceVIL.Core.Point(_selectFrom);
            //     tbs.toSelectState = new SpaceVIL.Core.Point(_selectTo);
            // }
            undoQueue.AddFirst(tbs);
            TextChanged?.Invoke();
        }

        public override void SetWidth(int width)
        {
            if (GetWidth() == width)
            {
                return;
            }
            UpdateBlockWidth(width);
        }

        private void UpdateBlockWidth(int width)
        {
            SpaceVIL.Core.Point tmpCursor = new SpaceVIL.Core.Point(_cursorPosition.X, _cursorPosition.Y);
            SpaceVIL.Core.Point fromTmp = new SpaceVIL.Core.Point(_selectFrom.X, _selectFrom.Y);
            SpaceVIL.Core.Point toTmp = new SpaceVIL.Core.Point(_selectTo.X, _selectTo.Y);
            if (IsWrapText())
            {
                tmpCursor = _textureStorage.WrapCursorPosToReal(_cursorPosition);
                if (_isSelect)
                {
                    fromTmp = _textureStorage.WrapCursorPosToReal(_selectFrom);
                    toTmp = _textureStorage.WrapCursorPosToReal(_selectTo);
                }
            }
            base.SetWidth(width);
            _textureStorage.UpdateBlockWidth(_cursor.GetWidth());
            ReorganizeText();
            if (IsWrapText())
            {
                _cursorPosition = _textureStorage.RealCursorPosToWrap(tmpCursor);
                ReplaceCursor();
                if (_isSelect)
                {
                    fromTmp = _textureStorage.RealCursorPosToWrap(fromTmp);
                    toTmp = _textureStorage.RealCursorPosToWrap(toTmp);
                    _selectFrom = fromTmp;
                    _selectTo = toTmp;
                    MakeSelectedArea();
                }
            }
        }

        public override void SetHeight(int height)
        {
            if (GetHeight() == height)
            {
                return;
            }
            UpdateBlockHeight(height);
        }

        private void UpdateBlockHeight(int height)
        {
            base.SetHeight(height);
            _textureStorage.UpdateBlockHeight();
        }

        public override void SetX(int x)
        {
            if (GetX() == x)
            {
                return;
            }
            base.SetX(x);
            UpdateLayout();
        }
        public override void SetY(int y)
        {
            if (GetY() == y)
            {
                return;
            }
            base.SetY(y);
            UpdateLayout();
        }

        public void UpdateLayout()
        {
            if (_textureStorage.GetParent() == null)
            {
                return;
            }
            //ReplaceCursor();
            SpaceVIL.Core.Point pos = AddXYShifts(_cursorPosition);
            _cursor.SetX(pos.X);
            _cursor.SetY(pos.Y - GetLineSpacer() / 2 + 1);
            MakeSelectedArea();
        }

        // private class TextCursor : Rectangle {
        //     SpaceVIL.Core.Point _cursorPosition = new SpaceVIL.Core.Point(0, 0);
        //     TextCursor(int height) {
        //         SetItemName("TextCursor_" + count);
        //         SetHeight(height);
        //     }

        // }

        internal int GetCursorWidth()
        {
            return _cursor.GetWidth();
        }

        internal void AppendText(String text)
        {
            UnselectText();
            CancelJustSelected();
            int lineNum = _textureStorage.GetLinesCount() - 1;
            _cursorPosition = new SpaceVIL.Core.Point(GetLettersCountInLine(lineNum), lineNum);
            PrivPasteText(text); //PasteText
        }

        internal class TextBlockState
        {
            internal String textState;
            internal int cursorStateX;
            internal int cursorStateY;
            // internal SpaceVIL.Core.Point fromSelectState;
            // internal SpaceVIL.Core.Point toSelectState;

            internal TextBlockState(String textState, int cursorStateX, int cursorStateY)
            {
                this.textState = textState;
                this.cursorStateX = cursorStateX;
                this.cursorStateY = cursorStateY;
                // fromSelectState = new SpaceVIL.Core.Point(0, 0);
                // toSelectState = new SpaceVIL.Core.Point(0, 0);
            }
        }

        internal void RewindText()
        {
            _cursorPosition = new SpaceVIL.Core.Point(0, 0);
            ReplaceCursor();
        }

        //Wrap Text Stuff---------------------------------------------------------------------------------------------------

        private bool _isWrapText;

        public bool IsWrapText()
        {
            return _isWrapText;
        }

        internal void SetWrapText(bool value)
        {
            if (value == _isWrapText)
            {
                return;
            }

            Monitor.Enter(_textureStorage.textInputLock);
            try
            {
                String text = GetText();

                SpaceVIL.Core.Point cursorTmp = _cursorPosition;
                SpaceVIL.Core.Point fromTmp = new SpaceVIL.Core.Point();
                SpaceVIL.Core.Point toTmp = new SpaceVIL.Core.Point();
                if (_isWrapText)
                {
                    cursorTmp = _textureStorage.WrapCursorPosToReal(cursorTmp);
                    if (_isSelect)
                    {
                        fromTmp = _textureStorage.WrapCursorPosToReal(_selectFrom);
                        toTmp = _textureStorage.WrapCursorPosToReal(_selectTo);
                    }
                }

                _isWrapText = value;

                _textureStorage.SetText(text); //not added into redo/undo

                if (_isWrapText) //was unwrap become wrap
                {
                    cursorTmp = _textureStorage.RealCursorPosToWrap(cursorTmp);
                    if (_isSelect)
                    {
                        fromTmp = _textureStorage.RealCursorPosToWrap(_selectFrom);
                        toTmp = _textureStorage.RealCursorPosToWrap(_selectTo);
                    }
                }

                _cursorPosition = cursorTmp;
                ReplaceCursor();
                if (_isSelect)
                {
                    _selectFrom = fromTmp;
                    _selectTo = toTmp;
                    MakeSelectedArea();
                }
            }
            finally
            {
                Monitor.Exit(_textureStorage.textInputLock);
            }
        }

        // if wrapText is on && something changed
        private void ReorganizeText()
        {
            if (!_isWrapText)
            {
                return;
            }

            Monitor.Enter(_textureStorage.textInputLock);
            try
            {
                _textureStorage.RewrapText();
            }
            finally
            {
                Monitor.Exit(_textureStorage.textInputLock);
            }
        }

        internal void SetScrollStepFactor(float value)
        {
            _textureStorage.SetScrollStepFactor(value);
        }
    }
}