using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace SpaceVIL
{
    internal class TextBlock : VisualItem, ITextEditable, IDraggable, ITextShortcuts//, ITextContainer
    {
        public EventCommonMethod TextChanged;

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

        private Color _blockForeground;

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

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.TextBlock)));

            EventMousePressed += OnMousePressed;
            EventMouseDrag += OnDragging;
            EventKeyPress += OnKeyPress;
            EventKeyRelease += OnKeyRelease;
            EventTextInput += OnTextInput;
            EventScrollUp += OnScrollUp;
            EventScrollDown += OnScrollDown;

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
        }

        protected virtual void OnMousePressed(object sender, MouseArgs args)
        {
            ReplaceCursorAccordingCoord(new Point(args.Position.GetX(), args.Position.GetY()));
            if (_isSelect)
                UnselectText();
        }

        protected virtual void OnDragging(object sender, MouseArgs args)
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
            _selectedArea.ShiftAreaY(diff);
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
            _selectedArea.ShiftAreaX(diff);
            _cursor.SetX(_cursor.GetX() + diff);
        }

        protected virtual void OnScrollUp(object sender, MouseArgs args)
        {
            int curPos = _cursor.GetY();
            _cursor.SetY(_textureStorage.ScrollBlockUp(curPos));
            curPos = _cursor.GetY() - curPos;
            _selectedArea.ShiftAreaY(curPos);
            //ReplaceCursor();
        }

        protected virtual void OnScrollDown(object sender, MouseArgs args)
        {
            int curPos = _cursor.GetY();
            _cursor.SetY(_textureStorage.ScrollBlockDown(_cursor.GetY()));
            curPos = _cursor.GetY() - curPos;
            _selectedArea.ShiftAreaY(curPos);
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
            //Вроде бы и без этого норм, но пусть пока будет
            //if (_linesList != null)
            //    realPos.Y -= (int)_linesList[0].GetLineTopCoord(); //???????!!!!!!
            /*
            int lineNumb = _textureStorage.FindLineNumb(realPos.Y);

            realPos.X -= GetX() + GetPadding().Left + _textureStorage.TextMargin().Left;

            _cursor_position.Y = lineNumb;
            _cursor_position.X = CoordXToPos(realPos.X, lineNumb);
            */

            _cursor_position = _textureStorage.ReplaceCursorAccordingCoord(realPos);
            ReplaceCursor();
        }

        protected virtual void OnKeyRelease(object sender, KeyArgs args)
        {
            //if (args.Scancode == 0x2F && args.Mods == KeyMods.Control)
            //    PasteText(CommonService.ClipboardTextStorage);
        }
        protected virtual void OnKeyPress(object sender, KeyArgs args)
        {
            //Console.WriteLine(scancode);
            Monitor.Enter(_textureStorage.textInputLock);
            try
            {
                if (!_isEditable)
                {
                    if (args.Mods.Equals(KeyMods.Control) && (args.Key == KeyCode.A || args.Key == KeyCode.a))
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
                    _selectFrom.X = -1;// 0;
                    _selectFrom.Y = 0;
                    _selectTo.X = -1;// 0;
                    _selectTo.Y = 0;
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
                    if (args.Key == KeyCode.Backspace || args.Key == KeyCode.Delete || args.Key == KeyCode.Enter)
                    {
                        if (_isSelect)
                            PrivCutText();
                        else
                        {
                            _cursor_position = CheckLineFits(_cursor_position);
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
                    _cursor_position = CheckLineFits(_cursor_position);
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

        public virtual void OnTextInput(object sender, TextInputArgs args)
        {
            if (!_isEditable) return;
            Monitor.Enter(_textureStorage.textInputLock);
            try
            {
                byte[] input = BitConverter.GetBytes(args.Character);
                string str = Encoding.UTF32.GetString(input);
                if (_isSelect)
                {
                    UnselectText();
                    PrivCutText();
                }
                if (_justSelected) _justSelected = false;
                _cursor_position = CheckLineFits(_cursor_position);
                SetTextInLine(_textureStorage.GetTextInLine(_cursor_position.Y).Insert(_cursor_position.X, str));
                _cursor_position.X++;
                ReplaceCursor();
                //TextChanged?.Invoke();
            }
            finally
            {
                Monitor.Exit(_textureStorage.textInputLock);
            }
        }

        private Point CheckLineFits(Point checkPoint)
        {
            Point outPt = new Point();
            //??? check line count
            outPt.Y = checkPoint.Y;
            if (outPt.Y == -1) outPt.Y = 0;
            outPt.X = checkPoint.X;
            if (outPt.X == -1) outPt.X = 0;

            outPt.X = _textureStorage.CheckLineWidth(outPt.X, checkPoint);

            return outPt;
        }

        private Point CursorPosToCoord(Point cPos0)
        {
            Point cPos = CheckLineFits(cPos0);
            return _textureStorage.CupsorPosToCoord(cPos);
        }

        private void ReplaceCursor()
        {
            Point pos = AddXYShifts(0, 0, _cursor_position);
            _cursor.SetX(pos.X);
            _cursor.SetY(pos.Y - GetLineSpacer() / 2 + 1);// - 3);
            TextChanged?.Invoke();
        }

        void SetLineSpacer(int lineSpacer)
        {
            _textureStorage.SetLineSpacer(lineSpacer);
            _cursor.SetHeight(_textureStorage.GetCursorHeight());
        }

        public int GetLineSpacer()
        {
            return _textureStorage.GetLineSpacer();
        }

        internal string GetText()
        {
            return _textureStorage.GetWholeText();
        }



        public void SetTextAlignment(ItemAlignment alignment)
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
        public void SetTextMargin(Indents margin)
        {
            _textureStorage.SetTextMargin(margin);
        }

        public void SetFont(Font font)
        {
            /*
            if (!font.Equals(_elementFont))
            {
                _elementFont = font;
                if (_elementFont == null)
                    return;
                int[] output = FontEngine.GetSpacerDims(font);
                _minLineSpacer = output[0];
                //_minFontY = output[1];
                //_maxFontY = output[2];
                _lineHeight = output[2]; //Math.Abs(_maxFontY - _minFontY);
                if (_lineSpacer < _minLineSpacer)
                    _lineSpacer = _minLineSpacer;

                if (_linesList == null) return;
                foreach (TextLine te in _linesList)
                    te.SetFont(font);

                _cursor.SetHeight(_lineHeight + _lineSpacer); // + 6);
            }
            */
            _textureStorage.SetFont(font);
            _cursor.SetHeight(_textureStorage.GetCursorHeight());
        }
        public Font GetFont()
        {
            return _textureStorage.GetFont();
        }
        public void SetText(String text)
        {
            Monitor.Enter(_textureStorage.textInputLock);
            try
            {
                _cursor_position = _textureStorage.SetText(text, _cursor_position);
                ReplaceCursor();
                //TextChanged?.Invoke();
            }
            finally
            {
                Monitor.Exit(_textureStorage.textInputLock);
            }
        }

        private void SetTextInLine(String text)
        {
            //_linesList[_cursor_position.Y].UpdateData(UpdateType.Critical); //Doing in TextItem
            _textureStorage.SetTextInLine(text, _cursor_position.Y);
        }

        public int GetTextWidth()
        {
            /*
            int w = 0, w0;
            if (_linesList == null) return w;
            foreach (TextLine te in _linesList)
            {
                w0 = te.GetWidth();
                w = (w < w0) ? w0 : w;
            }
            return w;
            */
            return _textureStorage.GetWidth();
        }

        public int GetTextHeight()
        {
            return _textureStorage.GetTextHeight();
        }
        public void SetForeground(Color color)
        {
            _textureStorage.SetForeground(color);
        }
        public void SetForeground(int r, int g, int b)
        {
            if (r < 0) r = Math.Abs(r); if (r > 255) r = 255;
            if (g < 0) g = Math.Abs(g); if (g > 255) g = 255;
            if (b < 0) b = Math.Abs(b); if (b > 255) b = 255;
            SetForeground(Color.FromArgb(255, r, g, b));
        }
        public void SetForeground(int r, int g, int b, int a)
        {
            if (r < 0) r = Math.Abs(r); if (r > 255) r = 255;
            if (g < 0) g = Math.Abs(g); if (g > 255) g = 255;
            if (b < 0) b = Math.Abs(b); if (b > 255) b = 255;
            SetForeground(Color.FromArgb(a, r, g, b));
        }
        public void SetForeground(float r, float g, float b)
        {
            if (r < 0) r = Math.Abs(r); if (r > 1.0f) r = 1.0f;
            if (g < 0) g = Math.Abs(g); if (g > 1.0f) g = 1.0f;
            if (b < 0) b = Math.Abs(b); if (b > 1.0f) b = 1.0f;
            SetForeground(Color.FromArgb(255, (int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f)));
        }
        public void SetForeground(float r, float g, float b, float a)
        {
            if (r < 0) r = Math.Abs(r); if (r > 1.0f) r = 1.0f;
            if (g < 0) g = Math.Abs(g); if (g > 1.0f) g = 1.0f;
            if (b < 0) b = Math.Abs(b); if (b > 1.0f) b = 1.0f;
            SetForeground(Color.FromArgb((int)(a * 255.0f), (int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f)));
        }
        public Color GetForeground()
        {
            //if (_linesList == null) return Color.White; //?????
            //return _linesList[0].GetForeground();
            return _blockForeground;
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
                    _cursor.IsVisible = true;
                else
                    _cursor.IsVisible = false;
            }
        }
        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            _textureStorage.SetBlockWidth(width, _cursor.GetWidth());
        }
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            _textureStorage.SetBlockHeight(height);
        }

        public override void InitElements()
        {
            _cursor.SetHeight(_textureStorage.GetCursorHeight());
            AddItems(_selectedArea, _textureStorage, _cursor);

            _textureStorage.InitLines(_cursor.GetWidth());
        }

        /*
        private void UpdateLinesData(UpdateType updateType)
        {
            foreach (TextLine tl in _linesList)
                tl.UpdateData(updateType);
        }
        */

        /*
        private void RemoveAllLines()
        {
            foreach (TextLine tl in _linesList)
                RemoveItem(tl);
        }
        */
        public override bool IsFocused
        {
            get
            {
                return base.IsFocused;
            }
            set
            {
                base.IsFocused = value;
                if (IsFocused && _isEditable)
                    _cursor.IsVisible = true;
                else
                    _cursor.IsVisible = false;
            }
        }



        private void MakeSelectedArea(Point from, Point to)
        {
            if (from.X == to.X && from.Y == to.Y)
            {
                _selectedArea.SetRectangles(null);
                return;
            }

            List<Point> selectionRectangles = new List<Point>();
            Point fromReal, toReal;
            List<Point> listPt = RealFromTo(from, to);
            fromReal = listPt[0];
            toReal = listPt[1];
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

            _selectedArea.SetRectangles(selectionRectangles);
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
            Point outPoint = _textureStorage.AddXYShifts(xShift, yShift, CursorPosToCoord(point), isx);
            //int offset = _cursorXMax/3;
            /*
            if (globalXShift + outPoint.X < 0)
            {
                globalXShift = -outPoint.X;
                globalXShift += offset;
                if (globalXShift > 0) globalXShift = 0;
                UpdLinesXShift();
            }
            if (globalXShift + outPoint.X > _cursorXMax)
            {
                globalXShift = _cursorXMax - outPoint.X;
                globalXShift -= offset;
                UpdLinesXShift();
            }
            if (outPoint.Y + globalYShift < 0)
            {
                globalYShift = -outPoint.Y;
                UpdLinesYShift();

            }
            if (outPoint.Y + _lineHeight + globalYShift > _cursorYMax)
            {
                globalYShift = _cursorYMax - outPoint.Y - _lineHeight;
                UpdLinesYShift();
            }

            outPoint.X += globalXShift;
            outPoint.Y += globalYShift;
            */
            //Indents textMargin = _textureStorage.TextMargin();
            outPoint.X += /* GetX() + GetPadding().Left + textMargin.Left + */ xShift;
            outPoint.Y += /* GetY() + GetPadding().Top + textMargin.Top + */ yShift;

            return outPoint;
        }

        private string PrivGetSelectedText()
        {
            _selectFrom = CheckLineFits(_selectFrom);
            _selectTo = CheckLineFits(_selectTo);
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

                _cursor_position = CheckLineFits(_cursor_position);
                _cursor_position = _textureStorage.PasteText(pasteStr, _cursor_position);

                ReplaceCursor();
            }
            finally
            {
                Monitor.Exit(_textureStorage.textInputLock);
            }
        }

        public void PasteText(string text)
        {
            PrivPasteText(text);
        }

        private string PrivCutText()
        {
            if (!_isEditable) return "";
            Monitor.Enter(_textureStorage.textInputLock);
            try
            {
                string str = PrivGetSelectedText();
                _selectFrom = CheckLineFits(_selectFrom);
                _selectTo = CheckLineFits(_selectTo);
                if (_selectFrom.X == _selectTo.X && _selectFrom.Y == _selectTo.Y) return "";
                List<Point> listPt = RealFromTo(_selectFrom, _selectTo);
                Point fromReal = listPt[0];
                Point toReal = listPt[1];

                _textureStorage.CutText(fromReal, toReal);

                _cursor_position = fromReal;
                ReplaceCursor();
                if (_isSelect)
                    UnselectText();
                _justSelected = false;
                return str;
            }
            finally
            {
                Monitor.Exit(_textureStorage.textInputLock);
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
            MakeSelectedArea(new Point(0, 0), new Point(0, 0));
        }

        private int GetLineLetCount(int lineNum)
        {
            return _textureStorage.GetLineLetCount(lineNum);
        }

        public void Clear()
        {
            //SetText("");
            _textureStorage.Clear();
            _cursor_position.X = 0;
            _cursor_position.Y = 0;
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
            }
        }

        public override List<BaseItem> GetItems()
        {
            List<BaseItem> list = base.GetItems();
            return new List<BaseItem>() { list[0], list[1], list[2] };
        }

        public override void RemoveItem(BaseItem item)
        {
            if (item.Equals(_cursor))
            {
                while (base.GetItems().Count > 0)
                {
                    base.RemoveItem(base.GetItems().First());
                }
                return;
            }
            base.RemoveItem(item);
        }
    }
}