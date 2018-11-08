using System;
using System.Collections.Generic;
using System.Drawing;
using System.Text;
using System.Threading;

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

        static int count = 0;
        internal TextureStorage() : base(name: "TextureStorage_" + count)
        {
            _linesList = new List<TextLine>();
            TextLine te = new TextLine();
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
            if (GetParent() == null) return;
            Indents textMargin = TextMargin();
            _cursorXMax = GetParent().GetWidth() - curWidth - GetParent().GetPadding().Left -
                GetParent().GetPadding().Right - textMargin.Left - textMargin.Right;
            SetAllowWidth();
            UpdLinesXShift();
        }

        internal void SetBlockHeight(int height)
        {
            if (GetParent() == null) return;
            Indents textMargin = TextMargin();
            _cursorYMax = GetParent().GetHeight() - GetParent().GetPadding().Top - GetParent().GetPadding().Bottom
                 - textMargin.Top - textMargin.Bottom;
            SetAllowHeight();
            UpdLinesYShift();
        }

        internal void InitLines(int curWidth)
        {
            Indents textMargin = TextMargin();

            _cursorXMax = GetParent().GetWidth() - curWidth - GetParent().GetPadding().Left
                 - GetParent().GetPadding().Right - textMargin.Left - textMargin.Right;
            _cursorYMax = GetParent().GetHeight() - GetParent().GetPadding().Top - GetParent().GetPadding().Bottom
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
                GetParent().AddItem(tl);
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
            te.SetMargin(TextMargin());
            if (_elementFont != null)
                te.SetFont(_elementFont);

            GetParent().AddItem(te);
            //text.TrimEnd('\r');
            te.SetItemText(text);
            _linesList.Insert(lineNum, te);

            UpdLinesYShift(); //Пока обновляются все, но в принципе, нужно только под lineNum

            //GetParent().AddItem(_cursor);
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
        }

        internal void Clear()
        {
            _linesList[0].SetItemText("");
            RemoveLines(1, _linesList.Count - 1);
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

        internal Point CupsorPosToCoord(Point cPos)
        {
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
                if (!(cPos.X == 0 && cPos.Y == 0))
                {
                    coord.X = GetLetPosInLine(cPos.Y, cPos.X - 1);
                }
            }
            return coord;
        }

        internal Point ReplaceCursorAccordingCoord(Point realPos)
        {
            realPos.Y -= GetParent().GetY() + GetParent().GetPadding().Top + globalYShift + TextMargin().Top;
            int lineNumb = realPos.Y / GetLineY(1);
            if (lineNumb >= GetCount())
                lineNumb = GetCount() - 1;
            if (lineNumb < 0) lineNumb = 0;
            //return lineNumb;

            realPos.X -= GetParent().GetX() + GetParent().GetPadding().Left + TextMargin().Left;

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
        }

        private void RemoveLines(int fromLine, int toLine)
        {
            int inc = fromLine;
            while (inc <= toLine)
            {
                GetParent().RemoveItem(_linesList[fromLine]);
                _linesList.RemoveAt(fromLine);
                inc++;
            }

            UpdLinesYShift(); //Пока обновляются все, но в принципе, нужно только под fromLine
        }
        /*
                private int GetLineXShift(int n)
                {
                    if (_linesList.Count > n)
                        return _linesList[n].GetLineXShift();
                    else
                        return 0;
                }
        */
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

        private Indents TextMargin()
        {
            return _linesList[0].GetMargin();
        }

        private int[] GetDims()
        {
            int[] output = _linesList[0].GetFontDims();
            _minLineSpacer = output[0];
            _lineHeight = output[2];

            if (_lineSpacer < _minLineSpacer)
                _lineSpacer = _minLineSpacer;

            return output;
        }

        internal void SetFont(Font font)
        {
            if (!font.Equals(_elementFont))
            {
                _elementFont = font;
                if (_elementFont == null)
                    return;

                if (_linesList == null) return;
                foreach (TextLine te in _linesList)
                    te.SetFont(font);

                GetDims();
                /*
                int[] output = FontEngine.GetSpacerDims(font);
                _minLineSpacer = output[0];
                //_minFontY = output[1];
                //_maxFontY = output[2];
                _lineHeight = output[2]; //Math.Abs(_maxFontY - _minFontY);
                if (_lineSpacer < _minLineSpacer)
                    _lineSpacer = _minLineSpacer;
                _cursor.SetHeight(_lineHeight + _lineSpacer); // + 6);
                */
            }
        }

        internal Font GetFont()
        {
            return _elementFont;
        }

        internal void SetTextInLine(string text, int lineY)
        {
            _linesList[lineY].SetItemText(text);
            //_linesList[_cursor_position.Y].UpdateData(UpdateType.Critical); //Doing in TextItem
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

        internal Point SetText(string text, Point curPos)
        {
            if (text.Equals("") || text == null) Clear();
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

            _linesList[0].SetItemText(line[0]);

            for (int i = 1; i < line.Length; i++)
            {
                s = line[i].TrimEnd('\r');
                AddNewLine(s, inc);
                inc++;
            }

            curPos.Y = line.Length - 1;
            curPos.X = GetLineLetCount(curPos.Y);
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

            return _cursor_position;
        }

        internal Point AddXYShifts(int xShift, int yShift, Point outPoint, bool isx)
        {
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
            outPoint.X += GetParent().GetX() + GetParent().GetPadding().Left + globalXShift + TextMargin().Left;
            outPoint.Y += GetParent().GetY() + GetParent().GetPadding().Top + globalYShift + TextMargin().Top;

            //outPoint.X += GetX() + GetPadding().Left + _linesList[0].GetMargin().Left + xShift;
            //outPoint.Y += GetY() + GetPadding().Top + _linesList[0].GetMargin().Top + yShift;

            return outPoint;
        }

        Color _foreground = Color.Black;
        internal void SetForeground(Color foreground)
        {
            if (_linesList != null && !foreground.Equals(GetForeground()))
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

                List<TextPrinter> tpLines = new List<TextPrinter>();
                int w = 0, h = 0;
                int lineHeigh = GetLineY(1);

                //Console.Write(_linesList.Count + ": ");
                foreach (TextLine tl in _linesList)
                {
                    TextPrinter tmp = tl.GetLetTextures();
                    tpLines.Add(tmp);
                    h += lineHeigh;//tmp.HeightTexture;
                    if (tmp == null) continue;
                    w = (w > tmp.WidthTexture) ? w : tmp.WidthTexture;
                    //Console.Write(h + " ");
                }
                //Console.WriteLine();
                w += _cursorXMax / 3;
                SetWidth(w);
                SetHeight(h);

                byte[] bigByte = new byte[h * w * 4];
                int bigOff = 0;

                foreach (TextPrinter tptmp in tpLines)
                {
                    if (tptmp == null || tptmp.Texture == null)
                    {
                        for (int p = 0; p < 4; p++)
                            for (int j = 0; j < lineHeigh; j++)
                                for (int i = 0; i < w; i++)
                                    bigByte[bigOff + p + i * 4 + j * (w * 4)] = 0;

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
                tpout.HeightTexture = h;
                if (tpLines.Count == 0 || tpLines[0] == null)
                {
                    tpout.XTextureShift = GetParent().GetPadding().Left + TextMargin().Left + GetParent().GetX();
                    tpout.YTextureShift = GetParent().GetPadding().Top + TextMargin().Top + GetParent().GetY();

                    tpout.XTextureShift += _linesList[0].GetLineXShift();
                    tpout.YTextureShift += _linesList[0].GetLineYShift();
                }
                else
                {
                    tpout.XTextureShift = tpLines[0].XTextureShift;
                    tpout.YTextureShift = tpLines[0].YTextureShift;



                }
                //Console.WriteLine("BigByte length " + w + " " + h);
                return tpout;
            }
            finally
            {
                Monitor.Exit(textInputLock);
            }
        }

        internal int GetScrollStep() {
            return GetLineY(1);
        }
        internal int GetScrollYOffset() {
            return globalYShift;
        }
        internal void SetScrollYOffset(int offset)
        {
            globalYShift = offset;
            UpdLinesYShift();
        }
        internal int GetScrollXOffset() {
            return globalXShift;
        }
        internal void SetScrollXOffset(int offset)
        {
            globalXShift = offset;
            UpdLinesXShift();
        }
    }
}