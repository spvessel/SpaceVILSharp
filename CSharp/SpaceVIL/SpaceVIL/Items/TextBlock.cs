using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    internal class TextBlock : Prototype, ITextEditable, IDraggable, ITextShortcuts, IGrid//, ITextContainer
    {
        public EventCommonMethod TextChanged;
        public EventCommonMethod CursorChanged;

        public override void Release()
        {
            TextChanged = null;
        }

        private static int count = 0;
        private Rectangle _cursor;
        private Point _cursor_position = new Point(0, 0);
        private CustomSelector _selectedArea;

        private TextureStorage _textureStorage;

        private bool _isEditable = true;

        private Point _selectFrom = new Point(-1, 0);
        private Point _selectTo = new Point(-1, 0);
        private bool _isSelect = false;
        private bool _justSelected = false;

        //private Color _blockForeground;

        private List<KeyCode> ShiftValCodes;
        private List<KeyCode> InsteadKeyMods;

        private int scrollXStep = 15;

        public TextBlock()
        {
            SetItemName("TextBlock_" + count);
            count++;

            _textureStorage = new TextureStorage();
            _cursor = new Rectangle();
            _selectedArea = new CustomSelector();

            // SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.TextBlock)));

            EventMousePress += OnMousePressed;
            EventMouseDrag += OnDragging;
            EventKeyPress += OnKeyPress;
            EventKeyRelease += OnKeyRelease;
            EventTextInput += OnTextInput;
            EventScrollUp += OnScrollUp;
            EventScrollDown += OnScrollDown;
            EventMouseDoubleClick += OnDoubleClick;

            ShiftValCodes = new List<KeyCode>() {KeyCode.Left, KeyCode.Right, KeyCode.End,
                KeyCode.Home, KeyCode.Up, KeyCode.Down};
            InsteadKeyMods = new List<KeyCode>() {KeyCode.LeftShift, KeyCode.RightShift, KeyCode.LeftControl,
                KeyCode.RightControl, KeyCode.LeftAlt, KeyCode.RightAlt, KeyCode.LeftSuper, KeyCode.RightSuper};

            //int[] output = _textureStorage.GetDims(); //te.GetFontDims();// FontEngine.GetSpacerDims(te.GetFont());
            //_minLineSpacer = output[0];
            //_minFontY = output[1];
            //_maxFontY = output[2];
            //_lineHeight = output[2]; //Math.Abs(_maxFontY - _minFontY);
            //if (_lineSpacer < _minLineSpacer)
            //    _lineSpacer = _minLineSpacer;

            _cursor.SetHeight(_textureStorage.GetCursorHeight());

            undoQueue = new LinkedList<TextBlockState>();
            redoQueue = new LinkedList<TextBlockState>();
            undoQueue.AddFirst(new TextBlockState(GetText(), _cursor_position.X, _cursor_position.Y));

            SetCursor(EmbeddedCursor.IBeam);
        }

        private void OnDoubleClick(object sender, MouseArgs args)
        {
            Monitor.Enter(_textureStorage.textInputLock);
            try
            {
                if (args.Button == MouseButton.ButtonLeft)
                {
                    ReplaceCursorAccordingCoord(new Point(args.Position.GetX(), args.Position.GetY()));
                    if (_isSelect)
                    {
                        UnselectText();
                        CancelJustSelected();
                    }
                    int[] wordBounds = _textureStorage.FindWordBounds(_cursor_position);

                    if (wordBounds[0] != wordBounds[1])
                    {
                        _isSelect = true;
                        _selectFrom = new Point(wordBounds[0], _cursor_position.Y);
                        _selectTo = new Point(wordBounds[1], _cursor_position.Y);
                        MakeSelectedArea(_selectFrom, _selectTo);
                    }
                }
            }
            finally
            {
                Monitor.Exit(_textureStorage.textInputLock);
            }
        }

        private void OnMousePressed(object sender, MouseArgs args)
        {
            Monitor.Enter(_textureStorage.textInputLock);
            try
            {
                if (args.Button == MouseButton.ButtonLeft)
                {
                    ReplaceCursorAccordingCoord(new Point(args.Position.GetX(), args.Position.GetY()));
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

        private void OnDragging(object sender, MouseArgs args)
        {
            Monitor.Enter(_textureStorage.textInputLock);
            try
            {
                if (args.Button == MouseButton.ButtonLeft)
                {
                    ReplaceCursorAccordingCoord(new Point(args.Position.GetX(), args.Position.GetY()));
                    if (!_isSelect)
                    {
                        _isSelect = true;
                        _selectFrom = new Point(_cursor_position.X, _cursor_position.Y);
                    }
                    else
                    {
                        _selectTo = new Point(_cursor_position.X, _cursor_position.Y);
                        MakeSelectedArea(_selectFrom, _selectTo);
                    }
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
            // _selectedArea.ShiftAreaY(diff);
            if (_justSelected)
                CancelJustSelected();
            MakeSelectedArea(_selectFrom, _selectTo);
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
            // _selectedArea.ShiftAreaX(diff);
            if (_justSelected)
                CancelJustSelected();
            MakeSelectedArea(_selectFrom, _selectTo);
            _cursor.SetX(_cursor.GetX() + diff);
        }

        private void OnScrollUp(object sender, MouseArgs args)
        {
            int curPos = _cursor.GetY();
            _cursor.SetY(_textureStorage.ScrollBlockUp(curPos));
            curPos = _cursor.GetY() - curPos;

            if (_justSelected)
                CancelJustSelected();
            MakeSelectedArea(_selectFrom, _selectTo);
            // _selectedArea.ShiftAreaY(curPos);
            //ReplaceCursor();
        }

        private void OnScrollDown(object sender, MouseArgs args)
        {
            int curPos = _cursor.GetY();
            _cursor.SetY(_textureStorage.ScrollBlockDown(_cursor.GetY()));
            curPos = _cursor.GetY() - curPos;

            if (_justSelected)
                CancelJustSelected();
            MakeSelectedArea(_selectFrom, _selectTo);
            // _selectedArea.ShiftAreaY(curPos);
            //ReplaceCursor();
        }

        // public void InvokeScrollUp(MouseArgs args)
        // {
        //     EventScrollUp?.Invoke(this, args);
        // }

        // public void InvokeScrollDown(MouseArgs args)
        // {
        //     EventScrollDown?.Invoke(this, args);
        // }

        private void ReplaceCursorAccordingCoord(Point realPos)
        {
            _cursor_position = _textureStorage.ReplaceCursorAccordingCoord(realPos);
            ReplaceCursor();
        }

        private void OnKeyRelease(object sender, KeyArgs args)
        {
            //if (args.Scancode == 0x2F && args.Mods == KeyMods.Control)
            //    PasteText(CommonService.ClipboardTextStorage);
        }
        private void OnKeyPress(object sender, KeyArgs args)
        {
            Monitor.Enter(_textureStorage.textInputLock);
            try
            {
                if (!_isEditable)
                {
                    if (args.Mods == KeyMods.Control && (args.Key == KeyCode.A || args.Key == KeyCode.a))
                    {
                        _selectFrom.X = 0;
                        _selectFrom.Y = 0;
                        _cursor_position.Y = _textureStorage.GetCount() - 1;
                        _cursor_position.X = GetLineLetCount(_cursor_position.Y);
                        _selectTo = new Point(_cursor_position.X, _cursor_position.Y);
                        ReplaceCursor();
                        _isSelect = true;
                        MakeSelectedArea(_selectFrom, _selectTo);
                    }
                    return;
                }

                if (!_isSelect && _justSelected)
                {
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
                                    _selectFrom = new Point(_cursor_position.X, _cursor_position.Y);
                                }
                            }

                            break;

                        case KeyMods.Control:
                            if (args.Key == KeyCode.A || args.Key == KeyCode.a)
                            {
                                _selectFrom.X = 0;
                                _selectFrom.Y = 0;
                                _cursor_position.Y = _textureStorage.GetCount() - 1;
                                _cursor_position.X = GetLineLetCount(_cursor_position.Y);
                                ReplaceCursor();

                                _isSelect = true;
                            }
                            break;

                            //alt, super ?
                    }
                }
                else
                {
                    if (args.Key == KeyCode.Backspace || args.Key == KeyCode.Delete || args.Key == KeyCode.Enter || args.Key == KeyCode.Tab)
                    {
                        if (_isSelect)
                            CutText();
                        else
                        {
                            _cursor_position = _textureStorage.CheckLineFits(_cursor_position);
                            if (args.Key == KeyCode.Backspace)//backspace
                            {
                                if (_cursor_position.X > 0)
                                {
                                    SetTextInLine(_textureStorage.GetTextInLine(_cursor_position.Y).Remove(_cursor_position.X - 1, 1));
                                    _cursor_position.X--;
                                }
                                else if (_cursor_position.Y > 0)
                                {
                                    _cursor_position.Y--;
                                    _cursor_position.X = GetLineLetCount(_cursor_position.Y);
                                    _textureStorage.CombineLines(_cursor_position.Y);
                                    UndoStuff();
                                }
                                ReplaceCursor();
                            }
                            if (args.Key == KeyCode.Delete)//delete
                            {
                                if (_cursor_position.X < GetLineLetCount(_cursor_position.Y))
                                {
                                    SetTextInLine(_textureStorage.GetTextInLine(_cursor_position.Y).Remove(_cursor_position.X, 1));
                                }
                                else if (_cursor_position.Y < _textureStorage.GetCount() - 1)
                                {
                                    _textureStorage.CombineLines(_cursor_position.Y);
                                    UndoStuff();
                                }
                            }
                        }
                    }
                    else
                        if (_isSelect && !InsteadKeyMods.Contains(args.Key))
                        UnselectText();
                }

                if (args.Key == KeyCode.Left) //arrow left
                {
                    _cursor_position = _textureStorage.CheckLineFits(_cursor_position);
                    if (!_justSelected)
                    {
                        if (_cursor_position.X > 0)
                            _cursor_position.X--;
                        else if (_cursor_position.Y > 0)
                        {
                            _cursor_position.Y--;
                            _cursor_position.X = GetLineLetCount(_cursor_position.Y);
                        }
                        ReplaceCursor();
                    }
                }
                if (args.Key == KeyCode.Right) //arrow right
                {
                    if (!_justSelected)
                    {
                        if (_cursor_position.X < GetLineLetCount(_cursor_position.Y))
                            _cursor_position.X++;
                        else if (_cursor_position.Y < _textureStorage.GetCount() - 1)
                        {
                            _cursor_position.Y++;
                            _cursor_position.X = 0;
                        }
                        ReplaceCursor();
                    }

                }
                if (args.Key == KeyCode.Up) //arrow up
                {
                    if (!_justSelected)
                    {
                        if (_cursor_position.Y > 0)
                            _cursor_position.Y--;
                        //?????
                        ReplaceCursor();
                    }

                }
                if (args.Key == KeyCode.Down) //arrow down
                {
                    if (!_justSelected)
                    {
                        if (_cursor_position.Y < _textureStorage.GetCount() - 1)
                            _cursor_position.Y++;
                        //?????
                        ReplaceCursor();
                    }

                }

                if (args.Key == KeyCode.End) //end
                {
                    _cursor_position.X = GetLineLetCount(_cursor_position.Y);
                    ReplaceCursor();
                }
                if (args.Key == KeyCode.Home) //home
                {
                    _cursor_position.X = 0;
                    ReplaceCursor();
                }

                if (args.Key == KeyCode.Enter || args.Key == KeyCode.NumpadEnter) //enter
                {
                    _textureStorage.BreakLine(_cursor_position);
                    _cursor_position.Y++;
                    _cursor_position.X = 0;

                    ReplaceCursor();
                    //TextChanged?.Invoke();
                    UndoStuff();
                }

                if (args.Key == KeyCode.Tab)
                {
                    PrivPasteText("    ");
                }

                if (_isSelect)
                {
                    if (!_selectTo.Equals(_cursor_position))
                    {
                        _selectTo = _cursor_position;
                        MakeSelectedArea(_selectFrom, _selectTo);
                    }
                }
            }
            finally
            {
                Monitor.Exit(_textureStorage.textInputLock);
            }
        }

        private void OnTextInput(object sender, TextInputArgs args)
        {
            if (!_isEditable) return;
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
                if (_justSelected) CancelJustSelected(); //_justSelected = false;
                _cursor_position = _textureStorage.CheckLineFits(_cursor_position);
                SetTextInLine(_textureStorage.GetTextInLine(_cursor_position.Y).Insert(_cursor_position.X, str));
                _cursor_position.X++;
                ReplaceCursor();
                //TextChanged?.Invoke();

                UndoStuff();
            }
            finally
            {
                Monitor.Exit(_textureStorage.textInputLock);
            }
        }

        private void ReplaceCursor()
        {
            Point pos = AddXYShifts(0, 0, _cursor_position);
            _cursor.SetX(pos.X);
            _cursor.SetY(pos.Y - GetLineSpacer() / 2 + 1);// - 3);
            CursorChanged?.Invoke(); //???????
            //UpdateLayout();
            //Console.WriteLine(pos.X + " cursor pos " + _cursor.GetX());
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
            //Ignore all changes
            /*
            _blockAlignment = alignment;
            if (_linesList == null) return;
            foreach (TextLine te in _linesList)
                te.SetTextAlignment(alignment);
            */
        }

        //private Indents _text_margin = new Indents();
        internal void SetTextMargin(Indents margin)
        {
            _textureStorage.SetTextMargin(margin);
        }

        internal Indents GetTextMargin()
        {
            return _textureStorage.GetTextMargin();
        }

        internal void SetFont(Font font)
        {
            _textureStorage.SetFont(font);
            _cursor.SetHeight(_textureStorage.GetCursorHeight());
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
                    UnselectText();
                if (_justSelected)
                    CancelJustSelected();

                _cursor_position = _textureStorage.SetText(text, _cursor_position);
                ReplaceCursor();
                //TextChanged?.Invoke();

                UndoStuff();
            }
            finally
            {
                Monitor.Exit(_textureStorage.textInputLock);
            }
        }

        private void SetTextInLine(String text)
        {
            _textureStorage.SetTextInLine(text, _cursor_position.Y);

            if (!ignoreSetInLine)
                UndoStuff();
            else ignoreSetInLine = false;
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
                    return;
                _isEditable = value;

                if (_isEditable)
                    _cursor.SetVisible(true);
                else
                    _cursor.SetVisible(false);
            }
        }

        public override void InitElements()
        {        
            _cursor.SetHeight(_textureStorage.GetCursorHeight());
            AddItems(_selectedArea, _textureStorage, _cursor);

            _textureStorage.InitLines(_cursor.GetWidth());
        }

        protected internal override void SetFocused(bool value)
        {
            base.SetFocused(value);
            if (IsFocused() && _isEditable)
                _cursor.SetVisible(true);
            else
                _cursor.SetVisible(false);
        }

        private void MakeSelectedArea(Point from, Point to)
        {
            if (from.X == to.X && from.Y == to.Y)
            {
                _selectedArea.SetRectangles(null);
                return;
            }

            List<Point> selectionRectangles; // = new List<Point>();

            Point fromReal, toReal;
            List<Point> listPt = RealFromTo(from, to);
            fromReal = listPt[0];
            toReal = listPt[1];
            
            selectionRectangles = _textureStorage.SelectedArrays(fromReal, toReal);

            /*
            Point tmp = new Point();
            int lsp = GetLineSpacer();
            if (from.Y == to.Y)
            {
                selectionRectangles.Add(AddXYShifts(0, -_cursor.GetHeight() - lsp / 2 + 1, fromReal, false));
                selectionRectangles.Add(AddXYShifts(0, -lsp / 2 + 1, toReal, false));
                _selectedArea.SetRectangles(selectionRectangles);
                return;
            }

            selectionRectangles.Add(AddXYShifts(0, -_cursor.GetHeight() - lsp / 2 + 1, fromReal, false));
            tmp.X = GetLineLetCount(fromReal.Y);
            tmp.Y = fromReal.Y;
            selectionRectangles.Add(AddXYShifts(0, -lsp / 2 + 1, tmp, false));
            tmp.X = 0;
            tmp.Y = toReal.Y;
            selectionRectangles.Add(AddXYShifts(0, -_cursor.GetHeight() - lsp / 2 + 1, tmp, false));
            selectionRectangles.Add(AddXYShifts(0, -lsp / 2 + 1, toReal, false));

            for (int i = fromReal.Y + 1; i < toReal.Y; i++)
            {
                tmp.X = 0;
                tmp.Y = i;
                selectionRectangles.Add(AddXYShifts(0, -_cursor.GetHeight() - lsp / 2 + 1, tmp, false));
                tmp.X = GetLineLetCount(i);
                tmp.Y = i;
                selectionRectangles.Add(AddXYShifts(0, -lsp / 2 + 1, tmp, false));
            }
            */
            _selectedArea.SetRectangles(selectionRectangles);
            //UpdateLayout();
        }

        private List<Point> RealFromTo(Point from, Point to)
        {
            List<Point> ans = new List<Point>();
            Point fromReal, toReal;
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

        private Point AddXYShifts(int xShift, int yShift, Point point, bool isx = true)
        {
            Point outPoint = _textureStorage.AddXYShifts(xShift, yShift, point, isx);

            //Indents textMargin = _textureStorage.TextMargin();
            outPoint.X += /* GetX() + GetPadding().Left + textMargin.Left + */ xShift;
            outPoint.Y += /* GetY() + GetPadding().Top + textMargin.Top + */ yShift;

            return outPoint;
        }

        private string PrivGetSelectedText()
        {
            Monitor.Enter(_textureStorage.textInputLock);
            try
            {
                if (_selectFrom.X == -1 || _selectTo.X == -1)
                    return "";
                _selectFrom = _textureStorage.CheckLineFits(_selectFrom);
                _selectTo = _textureStorage.CheckLineFits(_selectTo);
                if (_selectFrom.X == _selectTo.X && _selectFrom.Y == _selectTo.Y) return "";
                StringBuilder sb = new StringBuilder();
                List<Point> listPt = RealFromTo(_selectFrom, _selectTo);
                Point fromReal = listPt[0];
                Point toReal = listPt[1];

                string stmp;
                if (fromReal.Y == toReal.Y)
                {
                    stmp = _textureStorage.GetTextInLine(fromReal.Y);
                    sb.Append(stmp.Substring(fromReal.X, toReal.X - fromReal.X));
                    return sb.ToString();
                }

                if (fromReal.X >= GetLineLetCount(fromReal.Y))
                    sb.Append("\n");
                else
                {
                    stmp = _textureStorage.GetTextInLine(fromReal.Y);
                    sb.Append(stmp.Substring(fromReal.X) + "\n");
                }
                for (int i = fromReal.Y + 1; i < toReal.Y; i++)
                {
                    stmp = _textureStorage.GetTextInLine(i);
                    sb.Append(stmp + "\n");
                }

                stmp = _textureStorage.GetTextInLine(toReal.Y);
                sb.Append(stmp.Substring(0, toReal.X));

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
            if (!_isEditable) return;
            Monitor.Enter(_textureStorage.textInputLock);
            try
            {
                if (_isSelect) PrivCutText();
                if (pasteStr == null || pasteStr.Equals("")) return;

                _cursor_position = _textureStorage.CheckLineFits(_cursor_position);
                _cursor_position = _textureStorage.PasteText(pasteStr, _cursor_position);

                ReplaceCursor();

                UndoStuff();
            }
            finally
            {
                Monitor.Exit(_textureStorage.textInputLock);
            }
        }

        public void PasteText(string text)
        {
            if (text != null)
                PrivPasteText(text);
        }

        private string PrivCutText()
        {
            if (!_isEditable) return "";
            Monitor.Enter(_textureStorage.textInputLock);
            try
            {
                if (_selectFrom.X == -1 || _selectTo.X == -1)
                    return "";
                string str = PrivGetSelectedText();
                _selectFrom = _textureStorage.CheckLineFits(_selectFrom);
                _selectTo = _textureStorage.CheckLineFits(_selectTo);
                if (_selectFrom.X == _selectTo.X && _selectFrom.Y == _selectTo.Y) return "";
                List<Point> listPt = RealFromTo(_selectFrom, _selectTo);
                Point fromReal = listPt[0];
                Point toReal = listPt[1];

                _textureStorage.CutText(fromReal, toReal);

                _cursor_position = new Point(fromReal.X, fromReal.Y);
                ReplaceCursor();
                if (_isSelect)
                    UnselectText();
                CancelJustSelected(); //_justSelected = false;
                return str;
            }
            finally
            {
                Monitor.Exit(_textureStorage.textInputLock);
            }
        }

        public string CutText()
        {
            String ans = PrivCutText();
            UndoStuff();
            return ans;
        }

        private void UnselectText()
        {
            _isSelect = false;
            _justSelected = true;
            MakeSelectedArea(new Point(0, 0), new Point(0, 0));
        }

        private void CancelJustSelected()
        {
            _selectFrom.X = -1;// 0;
            _selectFrom.Y = 0;
            _selectTo.X = -1;// 0;
            _selectTo.Y = 0;
            _justSelected = false;
        }

        private int GetLineLetCount(int lineNum)
        {
            return _textureStorage.GetLineLetCount(lineNum);
        }

        public override void Clear()
        {
            ClearText();
        }
        
        internal void ClearText()
        {
            _textureStorage.Clear();
            _cursor_position.X = 0;
            _cursor_position.Y = 0;
            if (_isSelect)
                UnselectText();
            if (_justSelected)
                CancelJustSelected();

            UndoStuff();
        }

        //style
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
            SetForeground(style.Foreground);
            SetFont(style.Font);
            // SetTextAlignment(style.TextAlignment);
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
                    _cursor.SetHeight(_textureStorage.GetCursorHeight());
            }
        }

        /*
        public override List<IBaseItem> GetItems()
        {
            List<IBaseItem> list = base.GetItems();
            return new List<IBaseItem>() { list[0], list[1], list[2] };
        }

        public override bool RemoveItem(IBaseItem item)
        {
            if (item.Equals(_cursor))
            {
                while (base.GetItems().Count > 0)
                {
                    base.RemoveItem(base.GetItems().First());
                }
                return true;
            }
            return base.RemoveItem(item);
        }
        */

        private LinkedList<TextBlockState> undoQueue;
        private LinkedList<TextBlockState> redoQueue;
        private bool nothingFlag = false;
        private int queueCapacity = 100;
        private bool ignoreSetInLine = false;

        public void Redo()
        {
            if (redoQueue.Count == 0)
                return;

            TextBlockState tmpText = redoQueue.First.Value;
            if (tmpText != null)
            {
                redoQueue.RemoveFirst();
                nothingFlag = true;

                SetText(tmpText.textState);
                _cursor_position = new Point(tmpText.cursorStateX, tmpText.cursorStateY);
                undoQueue.First.Value.cursorStateX = _cursor_position.X;
                undoQueue.First.Value.cursorStateY = _cursor_position.Y;

                ReplaceCursor();
                //_textureStorage.Redo();
            }
        }

        public void Undo()
        {
            if (undoQueue.Count == 1)
                return;

            // undoStuff();

            TextBlockState tmpText = undoQueue.First.Value;
            if (tmpText != null)
            {
                undoQueue.RemoveFirst();
                if (redoQueue.Count > queueCapacity)
                    redoQueue.RemoveLast();
                redoQueue.AddFirst(new TextBlockState(tmpText.textState, tmpText.cursorStateX, tmpText.cursorStateY));

                tmpText = undoQueue.First.Value;
                if (tmpText != null)
                {
                    undoQueue.RemoveFirst();
                    nothingFlag = true;

                    SetText(tmpText.textState);
                    _cursor_position = new Point(tmpText.cursorStateX, tmpText.cursorStateY);
                    undoQueue.First.Value.cursorStateX = _cursor_position.X;
                    undoQueue.First.Value.cursorStateY = _cursor_position.Y;
                    ReplaceCursor();
                    //_textureStorage.Undo();
                }
            }
        }

        private void UndoStuff()
        {
            if (!nothingFlag)
            {
                // TextBlockState tbs = new TextBlockState(getText(), _cursor_position);
                // redoQueue.addFirst(tbs);
                redoQueue = new LinkedList<TextBlockState>();
            }
            else
            {
                nothingFlag = false;
            }

            if (undoQueue.Count > queueCapacity)
                undoQueue.RemoveLast();
            TextBlockState tbs = new TextBlockState(GetText(), _cursor_position.X, _cursor_position.Y);
            // if (_isSelect) {
            //     tbs.fromSelectState = new Point(_selectFrom);
            //     tbs.toSelectState = new Point(_selectTo);
            // }
            undoQueue.AddFirst(tbs);
            TextChanged?.Invoke();
        }

        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            _textureStorage.SetBlockWidth(width, _cursor.GetWidth());
            // UpdateLayout();
        }
        public override void SetX(int _x)
        {
            if (GetX() != _x)
            {
                base.SetX(_x);
                UpdateLayout();
            }
        }
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            _textureStorage.SetBlockHeight(height);
            // UpdateLayout();
        }
        public override void SetY(int _y)
        {
            if (GetY() != _y)
            {
                base.SetY(_y);
                UpdateLayout();
            }
        }

        public void UpdateLayout()
        {
            // Console.Write("upd");
            // int xSh = _selectedArea.GetX();
            // int ySh = _selectedArea.GetY();
            // if (xSh > 0)
            //     xSh = GetX() + GetPadding().Left - xSh;
            // if (ySh > 0)
            //     ySh = GetY() + GetPadding().Top - ySh;
            // _selectedArea.ShiftAreaX(GetX() + GetPadding().Left - xSh);
            // _selectedArea.ShiftAreaY(GetY() + GetPadding().Top - ySh);
            // _cursor.SetX(_cursor.GetX() + GetX() + GetPadding().Left);
            // _cursor.SetY(_cursor.GetY() + GetY() + GetPadding().Top);
            // Console.WriteLine(" " + _selectedArea.GetX() + " " + _selectedArea.GetY());
            if (_textureStorage.GetParent() == null) return;
            //ReplaceCursor();
            Point pos = AddXYShifts(0, 0, _cursor_position);
            _cursor.SetX(pos.X);
            _cursor.SetY(pos.Y - GetLineSpacer() / 2 + 1);// - 3);
            MakeSelectedArea(_selectFrom, _selectTo);
        }

        // private class TextCursor : Rectangle {
        //     Point _cursor_position = new Point(0, 0);
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
            int lineNum = _textureStorage.GetCount() - 1;
            _cursor_position = new Point(GetLineLetCount(lineNum), lineNum);
            PasteText(text);
        }

        internal class TextBlockState
        {
            internal String textState;
            internal int cursorStateX;
            internal int cursorStateY;
            // internal Point fromSelectState;
            // internal Point toSelectState;
            internal TextBlockState()
            {
            }
            internal TextBlockState(String textState, int cursorStateX, int cursorStateY)
            {
                this.textState = textState;
                this.cursorStateX = cursorStateX;
                this.cursorStateY = cursorStateY;
                // fromSelectState = new Point(0, 0);
                // toSelectState = new Point(0, 0);
            }
        }

        internal void RewindText()
        {
            _cursor_position = new Point(0, 0);
            ReplaceCursor();
        }
    }
}