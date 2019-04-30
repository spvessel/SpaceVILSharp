using System;
using System.Collections.Generic;
using System.Drawing;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    sealed internal class TextureStorage : Primitive, ITextContainer
    {
        private List<TextLine> _linesList;
        private ItemAlignment _blockAlignment = ItemAlignment.Left | ItemAlignment.Top;
        private Font _elementFont;
        private int _minLineSpacer;
        private int _lineSpacer;
        private int _lineHeight;

        private int globalXShift = 0;
        private int globalYShift = 0;
        private int _cursorXMax = int.MaxValue;
        private int _cursorYMax = int.MaxValue;

        internal Object textInputLock = new Object();

        private static int count = 0;
        internal TextureStorage() : base(name: "TextureStorage_" + count)
        {
            _linesList = new List<TextLine>();
            TextLine te = new TextLine();
            te.SetRecountable(true);
            if (GetForeground() != null)
                te.SetForeground(GetForeground());
            te.SetTextAlignment(_blockAlignment);
            if (_elementFont != null)
                te.SetFont(_elementFont);
            _linesList.Add(te);
            GetDims();

        }

        internal int ScrollBlockUp(int cursorY)
        {
            int h = GetTextHeight();
            int curCoord = cursorY;
            if (h < _cursorYMax && globalYShift >= 0) return curCoord;

            curCoord -= globalYShift;

            globalYShift += GetLineY(1);
            if (globalYShift > 0) globalYShift = 0;

            UpdLinesYShift();
            return (curCoord + globalYShift);
        }

        internal int ScrollBlockDown(int cursorY)
        {
            int h = GetTextHeight();
            int curCoord = cursorY;
            if (h < _cursorYMax && h + globalYShift <= _cursorYMax) return curCoord;

            curCoord -= globalYShift;

            globalYShift -= GetLineY(1);
            if (h + globalYShift < _cursorYMax)
                globalYShift = _cursorYMax - h;

            UpdLinesYShift();
            return (curCoord + globalYShift);
        }

        internal int GetCursorHeight()
        {
            return (_lineHeight + _lineSpacer);
        }

        internal void SetBlockWidth(int width, int curWidth)
        {
            cursorWidth = curWidth;
            Prototype parent = GetParent();
            if (parent == null)
                return;
            Indents textMargin = GetTextMargin();
            _cursorXMax = parent.GetWidth() - cursorWidth - parent.GetPadding().Left -
                parent.GetPadding().Right - textMargin.Left - textMargin.Right;
            SetAllowWidth();
            UpdLinesXShift();
        }

        internal void SetBlockHeight(int height)
        {
            Prototype parent = GetParent();
            if (parent == null)
            {
                _cursorYMax = 0;
            }
            else
            {
                Indents textMargin = GetTextMargin();
                _cursorYMax = parent.GetHeight() - parent.GetPadding().Top - parent.GetPadding().Bottom
                     - textMargin.Top - textMargin.Bottom;
            }
            SetAllowHeight();
            UpdLinesYShift();
        }

        private int cursorWidth = 0;
        internal void InitLines(int curWidth)
        {
            cursorWidth = curWidth;
            Indents textMargin = GetTextMargin();
            Prototype parent = GetParent();
            _cursorXMax = parent.GetWidth() - cursorWidth - parent.GetPadding().Left
                 - parent.GetPadding().Right - textMargin.Left - textMargin.Right;

            _cursorYMax = parent.GetHeight() - parent.GetPadding().Top - parent.GetPadding().Bottom
                 - textMargin.Top - textMargin.Bottom;

            AddAllLines();
            SetAllowWidth();
            SetAllowHeight();
            UpdLinesXShift();
            UpdLinesYShift();
        }

        internal void SetLineContainerAlignment(ItemAlignment alignment)
        {
            foreach (TextLine tl in _linesList)
                tl.SetAlignment(alignment);
        }

        private void AddAllLines()
        {
            //RemoveItem(_cursor);
            foreach (TextLine tl in _linesList)
            {
                tl.SetParent(GetParent());
            }
            //AddItem(_cursor);
        }

        internal int GetLineLetCount(int lineNum)
        {
            if (lineNum >= _linesList.Count)
                return 0;
            else
            {
                return _linesList[lineNum].GetItemText().Length;
            }
        }

        internal int CheckLineWidth(int xpt, Point checkPoint)
        {
            int outPtX = xpt;
            int letCount = GetLineLetCount(checkPoint.Y);
            if (checkPoint.X > letCount)
                outPtX = letCount;
            return outPtX;
        }

        private int GetLetPosInLine(int cPosY, int cPosX)
        {
            return _linesList[cPosY].GetLetPosArray()[cPosX];
        }

        private void AddNewLine(string text, int lineNum)
        {
            //GetParent().RemoveItem(_cursor);
            TextLine te = new TextLine();
            te.SetForeground(GetForeground());
            te.SetTextAlignment(_blockAlignment);
            te.SetMargin(GetTextMargin());
            if (_elementFont != null)
                te.SetFont(_elementFont);

            //GetParent().AddItem(te);
            te.SetParent(GetParent());

            //text.TrimEnd('\r');
            te.SetItemText(text);
            te.SetRecountable(true);
            _linesList.Insert(lineNum, te);

            UpdLinesYShift(); //Пока обновляются все, но в принципе, нужно только под lineNum
            //GetParent().AddItem(_cursor);

            CheckWidth();
        }

        internal void BreakLine(Point _cursorPosition)
        {
            string newText;
            if (_cursorPosition.X >= GetLineLetCount(_cursorPosition.Y))
                newText = "";
            else
            {
                TextLine tl = _linesList[_cursorPosition.Y];
                string text = tl.GetItemText();
                tl.SetItemText(text.Substring(0, _cursorPosition.X));
                newText = text.Substring(_cursorPosition.X);
            }
            AddNewLine(newText, _cursorPosition.Y + 1);

            //CheckWidth(); //Есть в addNewLine
        }

        internal void Clear()
        {
            _linesList[0].SetItemText("");
            RemoveLines(1, _linesList.Count - 1);

            CheckWidth();
        }

        private int CoordXToPos(int coordX, int lineNumb)
        {
            int pos = 0;

            List<int> lineLetPos = _linesList[lineNumb].GetLetPosArray();
            if (lineLetPos == null) return pos;

            for (int i = 0; i < lineLetPos.Count; i++)
            {
                if (lineLetPos[i] + globalXShift <= coordX + 3)
                    pos = i + 1;
                else break;
            }

            return pos;
        }

        internal Point CheckLineFits(Point checkPoint)
        {
            Point outPt = new Point();
            //??? check line count
            outPt.Y = checkPoint.Y;
            if (outPt.Y == -1) outPt.Y = 0;
            outPt.X = checkPoint.X;
            if (outPt.X == -1) outPt.X = 0;

            outPt.X = CheckLineWidth(outPt.X, checkPoint);

            return outPt;
        }

        private Point CursorPosToCoord(Point cPos0)
        {
            Point cPos = CheckLineFits(cPos0);
            Point coord = new Point(0, 0);
            coord.Y = GetLineY(cPos.Y);

            int letCount = GetLineLetCount(cPos.Y);
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
                //if (!(cPos.X == 0 && cPos.Y == 0))
                //{
                coord.X = GetLetPosInLine(cPos.Y, cPos.X - 1) + cursorWidth;
                //}
            }
            return coord;
        }

        internal Point ReplaceCursorAccordingCoord(Point realPos)
        {
            Prototype parent = GetParent();
            realPos.Y -= parent.GetY() + parent.GetPadding().Top + globalYShift + GetTextMargin().Top;
            int lineNumb = realPos.Y / GetLineY(1);
            if (lineNumb >= GetCount())
                lineNumb = GetCount() - 1;
            if (lineNumb < 0) lineNumb = 0;
            //return lineNumb;

            realPos.X -= parent.GetX() + parent.GetPadding().Left + GetTextMargin().Left;

            Point outPt = new Point(0, 0);
            outPt.Y = lineNumb;
            outPt.X = CoordXToPos(realPos.X, lineNumb);
            return outPt;
        }

        internal void CombineLines(int topLineY)
        {
            string text = _linesList[topLineY].GetItemText();
            text += _linesList[topLineY + 1].GetItemText();
            _linesList[topLineY].SetItemText(text);

            RemoveLines(topLineY + 1, topLineY + 1);

            CheckWidth();
        }

        private void RemoveLines(int fromLine, int toLine)
        {
            int inc = fromLine;
            while (inc <= toLine)
            {
                //GetParent().RemoveItem(_linesList[fromLine]);
                _linesList[fromLine].SetParent(null);

                _linesList.RemoveAt(fromLine);
                inc++;
            }

            UpdLinesYShift(); //Пока обновляются все, но в принципе, нужно только под fromLine
        }

        private void UpdLinesYShift()
        {
            int inc = 0;
            foreach (TextLine line in _linesList)
            {
                line.SetLineYShift(GetLineY(inc) + globalYShift);
                inc++;
            }
        }

        private void UpdLinesXShift()
        {
            foreach (TextLine line in _linesList)
            {
                line.SetLineXShift(globalXShift);
            }
        }

        private void SetAllowHeight()
        {
            foreach (TextLine line in _linesList)
            {
                line.SetAllowHeight(_cursorYMax);
            }
        }

        private void SetAllowWidth()
        {
            foreach (TextLine line in _linesList)
            {
                line.SetAllowWidth(_cursorXMax);
            }
        }

        private int GetLineY(int num)
        {
            return (_lineHeight + _lineSpacer) * num;//_lineSpacer / 2 + 
        }

        internal int GetCount()
        {
            return _linesList.Count;
        }

        internal void SetTextMargin(Indents margin)
        {
            //_text_margin = margin;
            foreach (TextLine var in _linesList)
            {
                var.SetMargin(margin);
            }
        }

        internal Indents GetTextMargin()
        {
            return _linesList[0].GetMargin();
        }

        private int[] GetDims()
        {
            int[] output = _linesList[0].GetFontDims();
            _minLineSpacer = output[0];
            _lineHeight = output[2];

            // if (_lineSpacer < _minLineSpacer)
            //     _lineSpacer = _minLineSpacer;
            SetLineSpacer(_minLineSpacer);

            return output;
        }

        internal void SetFont(Font font)
        {
            if (font == null)
                return;
            if (!font.Equals(_elementFont))
            {
                _elementFont = font;

                if (_linesList == null) return;
                foreach (TextLine te in _linesList)
                    te.SetFont(font);

                GetDims();
            }
        }

        internal Font GetFont()
        {
            return _elementFont;
        }

        internal void SetTextInLine(string text, int lineY)
        {
            _linesList[lineY].SetItemText(text);
            CheckWidth();
        }

        internal string GetTextInLine(int lineNum)
        {
            return _linesList[lineNum].GetItemText();
        }

        internal int GetTextHeight()
        {
            return GetLineY(_linesList.Count);
        }

        private string _wholeText = "";

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
                for (int i = 0; i < _linesList.Count - 1; i++)
                {
                    sb.Append(_linesList[i].GetText());
                    sb.Append("\n");
                }
                sb.Append(_linesList[_linesList.Count - 1].GetText());
            }
            _wholeText = sb.ToString();
            return _wholeText;
        }

        internal Point SetText(string text, Point curPos)
        {
            if (text == null || text.Equals("")) Clear();
            else if (!text.Equals(GetWholeText()))
            {
                curPos = SplitAndMakeLines(text, curPos);
            }
            return curPos;
        }

        internal void SetLineSpacer(int lsp)
        {
            if (lsp < _minLineSpacer)
                lsp = _minLineSpacer;

            if (lsp != _lineSpacer)
            {
                _lineSpacer = lsp;

                if (_linesList == null) return;

                UpdLinesYShift();
            }
        }

        internal int GetLineSpacer()
        {
            return _lineSpacer;
        }

        private Point SplitAndMakeLines(string text, Point curPos)
        {
            Clear();

            _wholeText = text;

            string[] line = text.Split('\n');
            int inc = 0;
            string s;

            s = line[0].TrimEnd('\r');
            _linesList[0].SetItemText(s);

            for (int i = 1; i < line.Length; i++)
            {
                inc++;
                s = line[i].TrimEnd('\r');
                AddNewLine(s, inc);
            }

            curPos.Y = line.Length - 1;
            curPos.X = GetLineLetCount(curPos.Y);

            CheckWidth();
            return curPos;
        }

        internal void CutText(Point fromReal, Point toReal)
        {
            if (fromReal.Y == toReal.Y)
                _linesList[toReal.Y].SetItemText(_linesList[toReal.Y].GetItemText().Remove(fromReal.X, toReal.X - fromReal.X));
            else
            {
                RemoveLines(fromReal.Y + 1, toReal.Y - 1);

                _linesList[fromReal.Y].SetItemText(_linesList[fromReal.Y].GetItemText().Substring(0, fromReal.X));
                _linesList[fromReal.Y + 1].SetItemText(_linesList[fromReal.Y + 1].GetItemText().Substring(toReal.X));
                CombineLines(fromReal.Y);
            }

            CheckWidth();
        }

        internal Point PasteText(string pasteStr, Point _cursor_position)
        {
            string textBeg = _linesList[_cursor_position.Y].GetItemText().Substring(0, _cursor_position.X);
            string textEnd = "";
            if (_cursor_position.X < GetLineLetCount(_cursor_position.Y))
                textEnd = _linesList[_cursor_position.Y].GetItemText().Substring(_cursor_position.X);

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
            }

            CheckWidth();
            return _cursor_position;
        }

        internal Point AddXYShifts(int xShift, int yShift, Point point, bool isx)
        {
            Point outPoint = CursorPosToCoord(point);
            Prototype parent = GetParent();
            if (parent == null)
                return new Point(0, 0);
            //Point outPoint = CursorPosToCoord(point);
            int offset = _cursorXMax / 3;
            if (isx)
            {
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

                    if (outPoint.X < GetWidth())
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
            }

            outPoint.X += parent.GetX() + parent.GetPadding().Left + globalXShift + GetTextMargin().Left;
            outPoint.Y += parent.GetY() + parent.GetPadding().Top + globalYShift + GetTextMargin().Top;

            //outPoint.X += GetX() + GetPadding().Left + _linesList[0].GetMargin().Left + xShift;
            //outPoint.Y += GetY() + GetPadding().Top + _linesList[0].GetMargin().Top + yShift;

            return outPoint;
        }

        private bool CheckPoints(Point point)
        {
            return (!(point.Y >= _linesList.Count));
        }

        internal List<Point> SelectedArrays(Point fromPt, Point toPt)
        {
            if (!CheckPoints(fromPt))
                return null;
            if (!CheckPoints(toPt))
                return null;

            int cursorHeight = GetCursorHeight();
            List<Point> selectionRectangles = new List<Point>();
            Prototype parent = GetParent();
            int xAdder = parent.GetX() + parent.GetPadding().Left + GetTextMargin().Left;
            int yAdder = parent.GetY() + parent.GetPadding().Top + GetTextMargin().Top;

            Point tmp = new Point();
            Point tmp0 = new Point();
            int x1, y1;
            int x2, y2;
            int lsp = GetLineSpacer();
            if (fromPt.Y == toPt.Y)
            {
                if (_linesList[fromPt.Y].GetLetTextures() == null)
                    return null;

                tmp0 = CursorPosToCoord(fromPt);
                x1 = tmp0.X + globalXShift; y1 = tmp0.Y + globalYShift;
                tmp0 = CursorPosToCoord(toPt);
                x2 = tmp0.X + globalXShift; y2 = tmp0.Y + globalYShift;

                if (x2 < 0 || x1 > _cursorXMax)
                    return null;

                if (x1 < 0)
                    x1 = 0;

                if (x2 > _cursorXMax)
                    x2 = _cursorXMax;

                x1 += xAdder; y1 += yAdder;
                x2 += xAdder; y2 += yAdder;

                selectionRectangles.Add(new Point(x1, y1 - cursorHeight - lsp / 2 + 1));
                selectionRectangles.Add(new Point(x2, y2 - lsp / 2 + 1));

                return selectionRectangles;
            }

            tmp0 = CursorPosToCoord(fromPt);
            x1 = tmp0.X + globalXShift; y1 = tmp0.Y + globalYShift;

            tmp.X = GetLineLetCount(fromPt.Y);
            tmp.Y = fromPt.Y;
            tmp0 = CursorPosToCoord(tmp);
            x2 = tmp0.X + globalXShift; y2 = tmp0.Y + globalYShift;

            if (_linesList[fromPt.Y].GetLetTextures() != null)
            {
                if (x2 >= 0 && x1 <= _cursorXMax)
                {
                    if (x1 < 0)
                        x1 = 0;

                    if (x2 > _cursorXMax)
                        x2 = _cursorXMax;

                    x1 += xAdder; y1 += yAdder;
                    x2 += xAdder; y2 += yAdder;
                    selectionRectangles.Add(new Point(x1, y1 - cursorHeight - lsp / 2 + 1));
                    selectionRectangles.Add(new Point(x2, y2 - lsp / 2 + 1));
                }
            }

            tmp.X = 0;
            tmp.Y = toPt.Y;
            tmp0 = CursorPosToCoord(tmp);
            x1 = tmp0.X + globalXShift; y1 = tmp0.Y + globalYShift;
            tmp0 = CursorPosToCoord(toPt);
            x2 = tmp0.X + globalXShift; y2 = tmp0.Y + globalYShift;

            if (_linesList[toPt.Y].GetLetTextures() != null)
            {
                if (x2 >= 0 && x1 <= _cursorXMax)
                {
                    if (x1 < 0)
                        x1 = 0;

                    if (x2 > _cursorXMax)
                        x2 = _cursorXMax;

                    x1 += xAdder; y1 += yAdder;
                    x2 += xAdder; y2 += yAdder;


                    selectionRectangles.Add(new Point(x1, y1 - cursorHeight - lsp / 2 + 1));
                    selectionRectangles.Add(new Point(x2, y2 - lsp / 2 + 1));
                }
            }

            for (int i = fromPt.Y + 1; i < toPt.Y; i++)
            {
                tmp.X = 0;
                tmp.Y = i;
                tmp0 = CursorPosToCoord(tmp);
                x1 = tmp0.X + globalXShift; y1 = tmp0.Y + globalYShift;
                tmp.X = GetLineLetCount(i);
                tmp.Y = i;
                tmp0 = CursorPosToCoord(tmp);
                x2 = tmp0.X + globalXShift; y2 = tmp0.Y + globalYShift;

                if (_linesList[i].GetLetTextures() != null)
                {
                    if (x2 >= 0 && x1 <= _cursorXMax)
                    {
                        if (x1 < 0)
                            x1 = 0;

                        if (x2 > _cursorXMax)
                            x2 = _cursorXMax;

                        x1 += xAdder; y1 += yAdder;
                        x2 += xAdder; y2 += yAdder;
                        selectionRectangles.Add(new Point(x1, y1 - cursorHeight - lsp / 2 + 1));
                        selectionRectangles.Add(new Point(x2, y2 - lsp / 2 + 1));
                    }
                }
            }

            return selectionRectangles;
        }

        private Color _foreground = Color.Black;
        internal void SetForeground(Color foreground)
        {
            if (_linesList == null || foreground == null)
                return;
            if (!foreground.Equals(GetForeground()))
            {
                _foreground = foreground;
                foreach (TextLine te in _linesList)
                    te.SetForeground(foreground); //Вроде бы это больше не нужно
            }
        }
        public Color GetForeground()
        {
            return _foreground;
        }

        public TextPrinter GetLetTextures()
        {
            Monitor.Enter(textInputLock);
            try
            {
                float _screenScale = 1;
                CoreWindow wLayout = GetHandler();
                if (wLayout == null || wLayout.GetDpiScale() == null)
                    _screenScale = 1;
                else
                {
                    _screenScale = wLayout.GetDpiScale()[0];
                    if (_screenScale == 0)
                        _screenScale = 1;
                }

                List<TextPrinter> tpLines = new List<TextPrinter>();
                int w = 0, h = 0, bigWidth = 0;
                int lineHeigh = (int)(GetLineY(1) * _screenScale);
                int visibleHeight = 0;
                int startNumb = -1;
                int inc = -1;
                foreach (TextLine tl in _linesList)
                {
                    inc++;
                    TextPrinter tmp = tl.GetLetTextures();
                    tpLines.Add(tmp);
                    h += lineHeigh;//tmp.HeightTexture;
                    w = (w > tl.GetWidth()) ? w : tl.GetWidth();
                    if (tmp == null) continue;
                    //if (_screenScale != 1)
                    //{
                    //    int bw = 0;
                    //    if (tmp != null)
                    int bw = tmp.WidthTexture;
                    bigWidth = (bigWidth > bw) ? bigWidth : bw;
                    //}
                    //w = (w > tmp.WidthTexture) ? w : tmp.WidthTexture;
                    visibleHeight += lineHeigh;
                    if (startNumb == -1)
                        startNumb = inc;
                }
                //w += _cursorXMax / 3;
                SetWidth(w);
                SetHeight((int)((float)h / _screenScale));
                //if (_screenScale != 1)
                //{
                w = bigWidth;
                //}

                byte[] bigByte = new byte[visibleHeight * w * 4]; //h
                int bigOff = 0;

                foreach (TextPrinter tptmp in tpLines)
                {
                    if (tptmp == null)
                    {
                        continue;
                    }
                    if (tptmp.Texture == null)
                    {
                        bigOff += lineHeigh * w * 4;
                        continue;
                    }

                    for (int p = 0; p < 4; p++)
                    {
                        for (int j = 0; j < tptmp.HeightTexture; j++)
                        {
                            for (int i = 0; i < tptmp.WidthTexture; i++)
                            {
                                bigByte[bigOff + p + i * 4 + j * (w * 4)] = tptmp.Texture[p + i * 4 + j * (tptmp.WidthTexture * 4)];
                            }

                            for (int i = tptmp.WidthTexture; i < w; i++)
                            {
                                bigByte[bigOff + p + i * 4 + j * (w * 4)] = 0;
                            }
                        }

                        for (int j = tptmp.HeightTexture; j < lineHeigh; j++)
                        {
                            for (int i = 0; i < w; i++)
                            {
                                bigByte[bigOff + p + i * 4 + j * (w * 4)] = 0;
                            }
                        }
                    }
                    bigOff += lineHeigh * w * 4;
                }
                TextPrinter tpout = new TextPrinter(bigByte);
                tpout.WidthTexture = w;
                tpout.HeightTexture = visibleHeight; // h;
                Prototype parent = GetParent();
                tpout.XTextureShift = parent.GetPadding().Left + GetTextMargin().Left + parent.GetX() + cursorWidth;
                tpout.YTextureShift = parent.GetPadding().Top + GetTextMargin().Top + parent.GetY();

                //                if (tpLines.Count == 0 || tpLines[0] == null)
                //                {
                //                    tpout.XTextureShift = parent.GetPadding().Left + GetTextMargin().Left + parent.GetX();
                //                    tpout.YTextureShift = parent.GetPadding().Top + GetTextMargin().Top + parent.GetY();
                //
                //                    tpout.XTextureShift += 0; // _linesList[0].GetLineXShift();
                //                    tpout.YTextureShift += 0; // _linesList[0].GetLineYShift();
                //                }
                //                else
                //                {
                //                    tpout.XTextureShift = parent.GetPadding().Left + GetTextMargin().Left + parent.GetX(); //tpLines[0].XTextureShift;
                //                    tpout.YTextureShift = parent.GetPadding().Top + GetTextMargin().Top + parent.GetY(); //tpLines[0].YTextureShift;
                //               }

                if (startNumb > -1)
                    tpout.YTextureShift += _linesList[startNumb].GetLineYShift();

                return tpout;
            }
            finally
            {
                Monitor.Exit(textInputLock);
            }
        }

        internal int GetScrollStep()
        {
            return GetLineY(1);
        }
        internal int GetScrollYOffset()
        {
            return globalYShift;
        }
        internal void SetScrollYOffset(int offset)
        {
            globalYShift = offset;
            UpdLinesYShift();
        }
        internal int GetScrollXOffset()
        {
            return globalXShift;
        }
        internal void SetScrollXOffset(int offset)
        {
            globalXShift = offset;
            UpdLinesXShift();
        }

        private void CheckWidth()
        {
            int w = 0, h = 0;
            int lineHeigh = GetLineY(1);

            foreach (TextLine tl in _linesList)
            {
                h += lineHeigh;
                w = (w > tl.GetWidth()) ? w : tl.GetWidth();
            }

            SetWidth(w);
            SetHeight(h);
        }

        private Regex patternWordBounds = new Regex(@"\W|_");
        internal int[] FindWordBounds(Point cursorPosition)
        {
            //С положение курсора должно быть все в порядке, не нужно проверять вроде бы
            String lineText = GetTextInLine(cursorPosition.Y);
            int index = cursorPosition.X;

            String testString = lineText.Substring(index);
            MatchCollection matcher = patternWordBounds.Matches(testString);

            int begPt = 0;
            int endPt = GetLineLetCount(cursorPosition.Y);

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
                    begPt = match.Index + 1;
            }

            return new int[] { begPt, endPt };
        }
    }
}