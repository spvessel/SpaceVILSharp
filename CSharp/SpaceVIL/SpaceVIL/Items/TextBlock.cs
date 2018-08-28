using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    internal class TextBlock : VisualItem, ITextEditable, IDraggable, ITextShortcuts
    {
        private static int count = 0;
        private string _wholeText = "";
        private Rectangle _cursor;
        private Point _cursor_position = new Point(0, 0);
        private CustomSelector _selectedArea;
        private List<TextLine> _linesList;

        private Point _selectFrom = new Point(0, 0);
        private Point _selectTo = new Point(0, 0);
        private bool _isSelect = false;
        private bool _justSelected = false;

        private int _minLineSpacer;
        private int _minFontY;
        private int _maxFontY;
        private int _lineHeight;

        private Font _elementFont;

        private const int BackspaceCode = 14;
        private const int DeleteCode = 339;
        private const int LeftArrowCode = 331;
        private const int RightArrowCode = 333;
        private const int UpArrowCode = 328;
        private const int DownArrowCode = 336;
        private const int EndCode = 335;
        private const int HomeCode = 327;
        private const int ACode = 30;
        private const int EnterCode = 28;

        private List<int> ShiftValCodes;

        public TextBlock()
        {
            SetItemName("TextBlock_" + count);
            SetBackground(180, 180, 180);
            SetForeground(Color.Black);
            //SetPadding(5, 0, 5, 0);
            count++;

            _linesList = new List<TextLine>();
            TextLine te = new TextLine();
            _linesList.Add(te);

            _cursor = new Rectangle();
            _selectedArea = new CustomSelector();
            _selectedArea.SetBackground(111, 181, 255);

            EventMousePressed += OnMousePressed;
            EventMouseDrag += OnDragging;
            EventKeyPress += OnKeyPress;
            EventKeyRelease += OnKeyRelease;
            EventTextInput += OnTextInput;

            ShiftValCodes = new List<int>() {LeftArrowCode, RightArrowCode, EndCode,
                HomeCode, UpArrowCode, DownArrowCode};

            int[] output = te.GetFontDims();// FontEngine.GetSpacerDims(te.GetFont());
            _minLineSpacer = 3;// output[0];
            _minFontY = output[1];
            _maxFontY = output[2];
            _lineHeight = Math.Abs(_maxFontY - _minFontY);
            if (_lineSpacer < _minLineSpacer)
                _lineSpacer = _minLineSpacer;

            _cursor.SetHeight(_lineHeight + _lineSpacer); // + 6);
        }

        protected virtual void OnMousePressed(object sender, MouseArgs args)
        {
            ReplaceCursorAccordingCoord(new Point(_mouse_ptr.X, _mouse_ptr.Y));
            if (_isSelect)
                UnselectText();
        }

        protected virtual void OnDragging(object sender, MouseArgs args)
        {
            ReplaceCursorAccordingCoord(new Point(_mouse_ptr.X, _mouse_ptr.Y));
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

        private void ReplaceCursorAccordingCoord(Point realPos)
        {
            //Вроде бы и без этого норм, но пусть пока будет
            //if (_linesList != null)
            //    realPos.Y -= (int)_linesList[0].GetLineTopCoord(); //???????!!!!!!

            //Console.WriteLine(_lineSpacer);

            realPos.Y -= GetY() + GetPadding().Top;
            int lineNumb = realPos.Y / (_lineHeight + _lineSpacer);
            if (lineNumb >= _linesList.Count)
                lineNumb = _linesList.Count - 1;
            if (lineNumb < 0) lineNumb = 0;

            realPos.X -= GetX() + GetPadding().Left;

            _cursor_position.Y = lineNumb;
            _cursor_position.X = CoordXToPos(realPos.X, lineNumb);
            ReplaceCursor();
        }

        private int CoordXToPos(int coordX, int lineNumb)
        {
            int pos = 0;

            List<int> lineLetPos = _linesList[lineNumb].GetLetPosArray();
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
            if(args.Scancode == 0x2F && args.Mods == KeyMods.Control)
                PasteText(CommonService.ClipboardTextStorage);
        }
        protected virtual void OnKeyPress(object sender, KeyArgs args)
        {
            //Console.WriteLine(scancode);

            if (!_isSelect && _justSelected)
            {
                _selectFrom.X = 0;
                _selectFrom.Y = 0;
                _selectTo.X = 0;
                _selectTo.Y = 0;
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
                            _selectFrom.X = 0;
                            _selectFrom.Y = 0;
                            _cursor_position.Y = _linesList.Count() - 1;
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
                if (args.Scancode == BackspaceCode || args.Scancode == DeleteCode)
                {
                    if (_isSelect)
                        CutText();
                    else
                    {
                        _cursor_position = CheckLineFits(_cursor_position);
                        if (args.Scancode == BackspaceCode)//backspace
                        {
                            if (_cursor_position.X > 0)
                            {
                                SetTextInLine(_linesList[_cursor_position.Y].GetItemText().Remove(_cursor_position.X - 1, 1));
                                _cursor_position.X--;
                            }
                            else if (_cursor_position.Y > 0)
                            {
                                _cursor_position.Y--;
                                _cursor_position.X = GetLineLetCount(_cursor_position.Y);
                                CombineLines(_cursor_position.Y);
                            }
                            ReplaceCursor();
                        }
                        if (args.Scancode == DeleteCode)//delete
                        {
                            if (_cursor_position.X < GetLineLetCount(_cursor_position.Y))
                            {
                                SetTextInLine(_linesList[_cursor_position.Y].GetItemText().Remove(_cursor_position.X, 1));
                            }
                            else if (_cursor_position.Y < _linesList.Count - 1)
                            {
                                CombineLines(_cursor_position.Y);
                            }
                        }
                    }

                }
                else
                    if (_isSelect)
                {
                    UnselectText();
                    //_justSelected = true;
                }
            }

            if (args.Scancode == LeftArrowCode)//arrow left
            {
                _cursor_position = CheckLineFits(_cursor_position);
                if (_cursor_position.X > 0)
                    _cursor_position.X--;
                else if (_cursor_position.Y > 0)
                {
                    _cursor_position.Y--;
                    _cursor_position.X = GetLineLetCount(_cursor_position.Y);
                }
                ReplaceCursor();
            }
            if (args.Scancode == RightArrowCode)//arrow right
            {
                if (_cursor_position.X < GetLineLetCount(_cursor_position.Y))
                    _cursor_position.X++;
                else if (_cursor_position.Y < _linesList.Count - 1)
                {
                    _cursor_position.Y++;
                    _cursor_position.X = 0;
                }

                ReplaceCursor();
            }
            if (args.Scancode == UpArrowCode)//arrow up
            {
                if (_cursor_position.Y > 0)
                    _cursor_position.Y--;
                //?????

                ReplaceCursor();
            }
            if (args.Scancode == DownArrowCode)//arrow down
            {
                if (_cursor_position.Y < _linesList.Count - 1)
                    _cursor_position.Y++;
                //?????

                ReplaceCursor();
            }

            if (args.Scancode == EndCode)//end
            {
                _cursor_position.X = GetLineLetCount(_cursor_position.Y);
                ReplaceCursor();
            }
            if (args.Scancode == HomeCode)//home
            {
                _cursor_position.X = 0;
                ReplaceCursor();
            }

            if (args.Scancode == EnterCode)//enter
            {
                BreakLine();
                _cursor_position.Y++;
                _cursor_position.X = 0;

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

        public virtual void OnTextInput(object sender, TextInputArgs args)
        {
            byte[] input = BitConverter.GetBytes(args.Character);
            string str = Encoding.UTF32.GetString(input);
            //if (_isSelect) CutText();
            if (_justSelected) CutText();
            _cursor_position = CheckLineFits(_cursor_position);
            SetTextInLine(_linesList[_cursor_position.Y].GetItemText().Insert(_cursor_position.X, str));
            _cursor_position.X++;
            ReplaceCursor();
        }

        private Point CheckLineFits(Point checkPoint)
        {
            Point outPt = new Point();
            //??? check line count
            outPt.Y = checkPoint.Y;
            int letCount = GetLineLetCount(checkPoint.Y);
            outPt.X = checkPoint.X;
            if (checkPoint.X > letCount)
                outPt.X = letCount;

            return outPt;
        }

        private Point CursorPosToCoord(Point cPos0)
        {
            Point coord = new Point(0, 0);
            
            Point cPos = CheckLineFits(cPos0);
            int letCount = GetLineLetCount(cPos.Y);
            //Console.WriteLine(cPos0.X + " " + cPos0.Y + " " + _linesList[cPos.Y].GetLetPosArray());
            coord.Y = (int)_linesList[cPos.Y].GetLineYShift();
            
            if (letCount == 0)
            {
                coord.X = 0;
                return coord;
            }
            if (cPos.X == 0)
            {
                coord.X = 0;
                return coord;
            }
            else
            {
                if (!(cPos.X == 0 && cPos.Y == 0))
                {
                    coord.X = _linesList[cPos.Y].GetLetPosArray()[cPos.X - 1];
                }
            }
            return coord;
        }

        private void ReplaceCursor()
        {
            Point pos = AddXYShifts(0, 0, _cursor_position);
            _cursor.SetX(pos.X);
            _cursor.SetY(pos.Y);// - 3);
        }

        private int _lineSpacer;
        void SetLineSpacer(int lineSpacer)
        {
            if (lineSpacer < _minLineSpacer)
                lineSpacer = _minLineSpacer;

            if (lineSpacer != _lineSpacer)
            {
                _lineSpacer = lineSpacer;

                if (_linesList == null) return;

                /*????????????????????
                foreach (TextLine te in _linesList)
                {
                    te.SetLineYShift(_lineHeight + _lineSpacer);
                }
                */

                for (int i = 0; i < _linesList.Count; i++)
                {
                    _linesList[i].SetLineYShift((_lineHeight + _lineSpacer) * i);
                }
            }

            _cursor.SetHeight(_lineHeight + _lineSpacer); // + 6);
        }

        public int GetLineSpacer()
        {
            return _lineSpacer;
        }

        internal string GetWholeText()
        {
            StringBuilder sb = new StringBuilder();
            if (_linesList == null) return "";
            if (_linesList.Count == 1)
            {
                sb.Append(_linesList[0].GetText());
            }
            else
            {
                foreach (TextLine te in _linesList)
                {
                    sb.Append(te.GetText());
                    sb.Append("\n");
                }
                sb.Remove(sb.Length - 3, 2);
            }
            _wholeText = sb.ToString();
            return _wholeText;
        }

        public void SetTextAlignment(ItemAlignment alignment)
        {
            if (_linesList == null) return;
            foreach (TextLine te in _linesList)
                te.SetTextAlignment(alignment);
        }
        public void SetFont(Font font)
        {
            if (!font.Equals(_elementFont))
            {
                _elementFont = font;

                int[] output = FontEngine.GetSpacerDims(font);
                _minLineSpacer = 3; // output[0];
                _minFontY = output[1];
                _maxFontY = output[2];
                _lineHeight = Math.Abs(_maxFontY - _minFontY);
                if (_lineSpacer < _minLineSpacer)
                    _lineSpacer = _minLineSpacer;

                if (_linesList == null) return;
                foreach (TextLine te in _linesList)
                    te.SetFont(font);

                _cursor.SetHeight(_lineHeight + _lineSpacer); // + 6);
            }
        }
        public Font GetFont()
        {
            return _elementFont;
        }
        public void SetText(String text)
        {
            if (!text.Equals(GetWholeText()))
            {
                SplitAndMakeLines(text);
            }
        }

        private void SetTextInLine(String text)
        {
            _linesList[_cursor_position.Y].SetItemText(text);
            //_linesList[_cursor_position.Y].UpdateData(UpdateType.Critical); //Doing in TextItem
        }

        public int GetTextWidth()
        {
            int w = 0, w0;
            if (_linesList == null) return w;
            foreach (TextLine te in _linesList)
            {
                w0 = te.GetWidth();
                w = (w < w0) ? w0 : w;
            }
            return w;
        }

        public int GetTextHeight()
        {
            return _lineHeight * _linesList.Count + _lineSpacer * (_linesList.Count - 1);
        }

        private void SplitAndMakeLines(String text)
        {
            _linesList = new List<TextLine>();
            
            _wholeText = text;

            string[] line = text.Split('\n');
            int inc = 0;
            string s;

            foreach (string textPart in line)
            {
                s = textPart.TrimEnd('\r');
                AddNewLine(s, inc);
                inc++;
            }

            _cursor_position.Y = line.Length;
            _cursor_position.X = GetLineLetCount(_cursor_position.Y);
            ReplaceCursor();
        }
        /*
        internal void UpdateData(UpdateType updateType) {
            //foreach (TextEdit te in _linesList)
                //te.Up
        }
        */
        public void SetForeground(Color color)
        {
            if (_linesList != null && !color.Equals(GetForeground()))
            {
                foreach (TextLine te in _linesList)
                    te.SetForeground(color);
            }
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
            if (_linesList == null) return Color.White; //?????
            return _linesList[0].GetForeground();
        }

        public override void InitElements()
        {
            //text
            SetLineContainerAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
            //cursor
            _cursor.IsVisible = false;
            _cursor.SetBackground(0, 0, 0);
            //_cursor.SetMargin(0, 5, 0, 5);
            _cursor.SetWidth(2);

            _cursor.SetHeight(_lineHeight + _lineSpacer);// + 6
            _cursor.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            //selectedArea
            //_selectedArea.SetMargin(0, 5, 0, 5);
            _selectedArea.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            //adding
            AddItems(_selectedArea, _cursor);
            AddAllLines();

            //update text data
            //UpdateLinesData(UpdateType.Critical);
        }

        private void SetLineContainerAlignment(ItemAlignment alignment)
        {
            foreach (TextLine tl in _linesList)
                tl.SetAlignment(alignment);
        }
        /*
        private void UpdateLinesData(UpdateType updateType)
        {
            foreach (TextLine tl in _linesList)
                tl.UpdateData(updateType);
        }
        */
        private void AddAllLines()
        {
            RemoveItem(_cursor);
            foreach (TextLine tl in _linesList)
                AddItem(tl);
            AddItem(_cursor);
        }

        private void RemoveAllLines()
        {
            foreach (TextLine tl in _linesList)
                RemoveItem(tl);
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

        private int GetLineLetCount(int lineNum)
        {
            if (lineNum >= _linesList.Count)
                return 0;
            else
            {
                return _linesList[lineNum].GetItemText().Length;
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
            ///!!!Добавить высоту строк
            Point fromReal, toReal;
            List<Point> listPt = RealFromTo(from, to);
            fromReal = listPt[0];
            toReal = listPt[1];
            Point tmp = new Point();
            if (from.Y == to.Y)
            {
                //Console.WriteLine("Font: " + (_maxFontY - _minFontY));
                //Console.WriteLine("Cur: " + _cursor.GetHeight());
                selectionRectangles.Add(AddXYShifts(0, -_cursor.GetHeight(), fromReal));
                selectionRectangles.Add(AddXYShifts(0, 0, toReal));
                _selectedArea.SetRectangles(selectionRectangles);
                return;
            }

            selectionRectangles.Add(AddXYShifts(0, -_cursor.GetHeight(), fromReal));
            tmp.X = GetLineLetCount(fromReal.Y);
            tmp.Y = fromReal.Y;
            selectionRectangles.Add(AddXYShifts(0, 0, tmp));
            tmp.X = 0;
            tmp.Y = toReal.Y;
            selectionRectangles.Add(AddXYShifts(0, -_cursor.GetHeight(), tmp));
            selectionRectangles.Add(AddXYShifts(0, 0, toReal));

            for (int i = fromReal.Y + 1; i < toReal.Y; i++)
            {
                tmp.X = 0;
                tmp.Y = i;
                selectionRectangles.Add(AddXYShifts(0, -_cursor.GetHeight(), tmp));
                tmp.X = GetLineLetCount(i);
                tmp.Y = i;
                selectionRectangles.Add(AddXYShifts(0, 0, tmp));
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

        private Point AddXYShifts(int xShift, int yShift, Point point)
        {
            Point outPoint = CursorPosToCoord(point);
            outPoint.X += GetX() + GetPadding().Left + xShift;
            outPoint.Y += GetY() + GetPadding().Top + yShift;

            return outPoint;
        }

        public string GetSelectedText()
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
                stmp = _linesList[fromReal.Y].GetItemText();
                sb.Append(stmp.Substring(fromReal.X, toReal.X - fromReal.X));
                return sb.ToString();
            }

            if (fromReal.X >= GetLineLetCount(fromReal.Y))
                sb.Append("\n");
            else
            {
                stmp = _linesList[fromReal.Y].GetItemText();
                sb.Append(stmp.Substring(fromReal.X) + "\n");
            }
            for (int i = fromReal.Y + 1; i < toReal.Y; i++)
            {
                stmp = _linesList[i].GetItemText();
                sb.Append(stmp + "\n");
            }

            stmp = _linesList[toReal.Y].GetItemText();
            sb.Append(stmp.Substring(0, toReal.X));

            return sb.ToString();
        }

        public void PasteText(string pasteStr)
        {
            //Console.WriteLine("paste");
            if (_isSelect) CutText();
            if (pasteStr == null || pasteStr.Equals("")) return;

            _cursor_position = CheckLineFits(_cursor_position);
            string textBeg = _linesList[_cursor_position.Y].GetItemText().Substring(0, _cursor_position.X);
            string textEnd = "";
            if (_cursor_position.X < GetLineLetCount(_cursor_position.Y)) textEnd = _linesList[_cursor_position.Y].GetItemText().Substring(_cursor_position.X);
            
            string[] line = pasteStr.Split('\n');
            for (int i = 0; i < line.Length; i++)
            { 
                line[i] = line[i].TrimEnd('\r');
            }

            if (line.Length == 1)
            {
                _linesList[_cursor_position.Y].SetItemText(textBeg + line[0] + textEnd);
                _cursor_position.X += pasteStr.Length;
            }
            else
            {
                _linesList[_cursor_position.Y].SetItemText(textBeg + line[0]);
                int ind = _cursor_position.Y + 1;
                for (int i = 1; i < line.Length - 1; i++)
                {
                    AddNewLine(line[i], ind);
                    ind++;
                }

                AddNewLine(line[line.Length - 1] + textEnd, ind);

                _cursor_position.X = line[line.Length - 1].Length;
                _cursor_position.Y += line.Length - 1;
                //Console.WriteLine(line[line.Length - 1].Length + " " + (_cursor_position.Y + line.Length - 1));

            }
            //Console.WriteLine(_cursor_position.X + " " + _cursor_position.Y);

            ReplaceCursor();
        }

        public string CutText()
        {
            string str = GetSelectedText();
            _selectFrom = CheckLineFits(_selectFrom);
            _selectTo = CheckLineFits(_selectTo);
            if (_selectFrom.X == _selectTo.X && _selectFrom.Y == _selectTo.Y) return "";
            List<Point> listPt = RealFromTo(_selectFrom, _selectTo);
            Point fromReal = listPt[0];
            Point toReal = listPt[1];

            if (fromReal.Y == toReal.Y)
                _linesList[toReal.Y].SetItemText(_linesList[toReal.Y].GetItemText().Remove(fromReal.X, toReal.X - fromReal.X));
            else
            {
                RemoveLines(fromReal.Y + 1, toReal.Y - 1);

                _linesList[fromReal.Y].SetItemText(_linesList[fromReal.Y].GetItemText().Substring(0, fromReal.X));
                _linesList[fromReal.Y + 1].SetItemText(_linesList[fromReal.Y + 1].GetItemText().Substring(toReal.X));
                CombineLines(fromReal.Y);
            }

            _cursor_position = fromReal;
            ReplaceCursor();
            UnselectText();
            //Console.WriteLine(str);
            return str;
        }

        private void UnselectText()
        {
            _isSelect = false;
            _justSelected = true;
            //_selectFrom.X = 0;
            //_selectFrom.Y = 0;
            //_selectTo.X = 0;
            //_selectTo.Y = 0;
            MakeSelectedArea(new Point(0, 0), new Point(0, 0)); // _selectFrom, _selectTo);
        }

        private void AddNewLine(String text, int lineNum)
        {
            RemoveItem(_cursor);
            TextLine te = new TextLine();
            te.SetForeground(GetForeground());
            if (_elementFont != null)
                te.SetFont(_elementFont);
            AddItem(te);
            //text.TrimEnd('\r');
            te.SetItemText(text);
            te.SetLineYShift((_lineHeight + _lineSpacer) * lineNum);
            for (int i = lineNum; i < _linesList.Count; i++)
                _linesList[i].SetLineYShift((_lineHeight + _lineSpacer) * (i + 1));
            _linesList.Insert(lineNum, te);
            AddItem(_cursor);
            //te.UpdateData(UpdateType.Critical); //Doing when SetItemText
        }

        private void BreakLine()
        {
            string newText;
            if (_cursor_position.X >= GetLineLetCount(_cursor_position.Y))
                newText = "";
            else
            {
                TextLine tl = _linesList[_cursor_position.Y];
                string text = tl.GetItemText();
                tl.SetItemText(text.Substring(0, _cursor_position.X));
                newText = text.Substring(_cursor_position.X);
            }
            AddNewLine(newText, _cursor_position.Y + 1);
        }

        private void CombineLines(int topLineY)
        {
            string text = _linesList[topLineY].GetItemText();
            text += _linesList[topLineY + 1].GetItemText();
            _linesList[topLineY].SetItemText(text);

            RemoveLines(topLineY + 1, topLineY + 1);
            /*
            _linesList[topLineY + 1].SetItemText("");
            _linesList.RemoveAt(topLineY + 1);
            for (int i = topLineY + 1; i < _linesList.Count; i++) {
                _linesList[i].SetLineYShift((_lineHeight + _lineSpacer) * i);
            }
            */
        }

        private void RemoveLines(int fromLine, int toLine)
        {
            int inc = fromLine;
            while (inc <= toLine)
            {
                _linesList[fromLine].SetItemText("");
                _linesList.RemoveAt(fromLine);
                inc++;
            }

            for (int i = fromLine; i < _linesList.Count; i++)
            {
                _linesList[i].SetLineYShift((_lineHeight + _lineSpacer) * i + _lineSpacer);
            }
        }
    }

}