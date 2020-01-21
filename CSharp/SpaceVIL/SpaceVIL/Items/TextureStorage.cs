using System;
using System.Collections.Generic;
using System.Drawing;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using SpaceVIL.Common;

namespace SpaceVIL
{
    sealed internal class TextureStorage : Primitive, ITextContainer
    {
        private List<TextLine> _linesList;
        private ItemAlignment _blockAlignment = ItemAlignment.Left | ItemAlignment.Top;
        private List<int> _lineBreakes;
        private Font _elementFont;
        private int _minLineSpacer;
        private int _lineSpacer;
        private int _lineHeight;

        private int globalXShift = 0;
        private int globalYShift = 0;
        private int _cursorXMax = SpaceVILConstants.SizeMaxValue;
        private int _cursorYMax = SpaceVILConstants.SizeMaxValue;

        internal Object textInputLock = new Object();

        private static int count = 0;
        internal TextureStorage() : base(name: "TextureStorage_" + count)
        {
            _linesList = new List<TextLine>();
            _lineBreakes = new List<int>();
            TextLine te = new TextLine();
            te.SetRecountable(true);
            if (GetForeground() != null)
                te.SetForeground(GetForeground());
            te.SetTextAlignment(_blockAlignment);
            if (_elementFont != null)
                te.SetFont(_elementFont);
            _linesList.Add(te);
            GetDims();

            _isUpdateTextureNeed = true;
        }

        internal int ScrollBlockUp(int cursorY)
        {
            int h = GetTextHeight();
            int curCoord = cursorY;
            if (h < _cursorYMax && globalYShift >= 0)
            {
                return curCoord;
            }

            curCoord -= globalYShift;

            globalYShift += GetLineY(1);
            if (globalYShift > 0)
            {
                globalYShift = 0;
            }

            UpdLinesYShift();
            return (curCoord + globalYShift);
        }

        internal int ScrollBlockDown(int cursorY)
        {
            int h = GetTextHeight();
            int curCoord = cursorY;

            if (h < _cursorYMax && h + globalYShift <= _cursorYMax)
            {
                return curCoord;
            }

            curCoord -= globalYShift;

            globalYShift -= GetLineY(1);
            if (h + globalYShift < _cursorYMax)
            {
                globalYShift = _cursorYMax - h;
            }

            UpdLinesYShift();
            return (curCoord + globalYShift);
        }

        internal int GetCursorHeight()
        {
            return (_lineHeight + _lineSpacer);
        }

        internal void UpdateBlockWidth(int curWidth)
        {
            cursorWidth = curWidth;
            Prototype parent = GetParent();
            if (parent == null)
            {
                return;
            }
            Indents textMargin = GetTextMargin();
            Indents textPadding = parent.GetPadding();
            int cursorChanges = _cursorXMax;
            _cursorXMax = parent.GetWidth() - cursorWidth * 2 - textPadding.Left -
                textPadding.Right - textMargin.Left - textMargin.Right;
            cursorChanges -= _cursorXMax;

            SetAllowWidth((cursorChanges != 0)); // <- UpdLinesXShift();
        }

        internal void UpdateBlockHeight()
        {
            Prototype parent = GetParent();
            if (parent == null)
            {
                return;
            }
            Indents textMargin = GetTextMargin();
            Indents textPadding = parent.GetPadding();
            int cursorChanges = _cursorYMax;
            _cursorYMax = parent.GetHeight() - textPadding.Top - textPadding.Bottom
                - textMargin.Top - textMargin.Bottom;
            cursorChanges -= _cursorYMax;

            SetAllowHeight((cursorChanges != 0)); // <- UpdLinesYShift();
        }

        private int cursorWidth = 0;

        internal void InitLines(int curWidth)
        {
            Prototype parent = GetParent();
            if (parent == null)
            {
                return;
            }

            cursorWidth = curWidth;
            Indents textMargin = GetTextMargin();
            Indents textPadding = parent.GetPadding();

            _cursorXMax = parent.GetWidth() - cursorWidth - textPadding.Left
                 - textPadding.Right - textMargin.Left - textMargin.Right;

            _cursorYMax = parent.GetHeight() - textPadding.Top - textPadding.Bottom
                 - textMargin.Top - textMargin.Bottom;

            AddAllLines();
            SetAllowWidth(true); // <- UpdLinesXShift();
            SetAllowHeight(true); // <- UpdLinesYShift();
        }

        internal void SetLineContainerAlignment(ItemAlignment alignment)
        {
            foreach (TextLine tl in _linesList)
            {
                tl.SetAlignment(alignment);
            }
        }

        private void AddAllLines()
        {
            foreach (TextLine tl in _linesList)
            {
                tl.SetParent(GetParent());
            }
        }

        private TextLine GetTextLine(int lineNum)
        {
            return _linesList[lineNum];
        }

        internal int GetLettersCountInLine(int lineNum)
        {
            if (lineNum >= _linesList.Count) //возможно это плохо или очень плохо
            {
                return 0;
            }
            else
            {
                return GetTextInLine(lineNum).Length; //_linesList[lineNum].GetItemText().Length;
            }
        }

        private int CheckLineWidth(int xpt, SpaceVIL.Core.Point checkPoint)
        {
            int outPtX = xpt;
            int letCount = GetLettersCountInLine(checkPoint.Y);
            if (checkPoint.X > letCount)
            {
                outPtX = letCount;
            }
            return outPtX;
        }

        private bool CheckIsWrap()
        {
            Prototype parent = GetParent();
            if (parent != null)
            {
                return (((ITextWrap)parent).IsWrapText());
            }
            return false;
        }

        private void AddNewLine(string text, int lineNum)
        {
            AddNewLine(text, lineNum, true);
        }

        private void AddNewLine(string text, int lineNum, bool isRealLine)
        {
            TextLine te = new TextLine();
            te.SetForeground(GetForeground());
            te.SetTextAlignment(_blockAlignment);
            te.SetMargin(GetTextMargin());
            te.SetAllowWidth(_cursorXMax);
            te.SetAllowHeight(_cursorYMax);
            te.SetLineYShift(GetLineY(lineNum) + globalYShift);
            te.SetLineXShift(globalXShift);

            if (_elementFont != null)
            {
                te.SetFont(_elementFont);
            }

            te.SetParent(GetParent());

            //text.TrimEnd('\r');
            te.SetItemText(text);
            te.SetRecountable(true);
            _linesList.Insert(lineNum, te);

            if (CheckIsWrap())
            {
                int prevLineVal = 0;
                if (lineNum - 1 >= 0 && lineNum - 1 < _lineBreakes.Count)
                {
                    prevLineVal = _lineBreakes[lineNum - 1];
                }

                if (isRealLine)
                {
                    _lineBreakes.Insert(lineNum, prevLineVal + 1);
                    for (int i = lineNum + 1; i < _lineBreakes.Count; i++)
                    {
                        _lineBreakes[i] = _lineBreakes[i] + 1;
                    }
                }
                else
                {
                    _lineBreakes.Insert(lineNum, prevLineVal);
                }

                WrapLine(lineNum);
            }

            UpdLinesYShift(); //Пока обновляются все, но в принципе, нужно только под lineNum

            CheckWidth(); // <- _isUpdateTextureNeed = true;
        }

        // internal void BreakLine(SpaceVIL.Core.Point _cursorPosition)
        // {
        //     BreakLine(_cursorPosition, true);
        // }

        internal void BreakLine(SpaceVIL.Core.Point _cursorPosition) //, bool isRealBreak)
        {
            string newText = "";
            int lineNum = _cursorPosition.Y + 1;
            AddNewLine(newText, lineNum, true); //isRealBreak); // <- CheckWidth(); //Есть в addNewLine

            // if (_cursorPosition.X >= GetLettersCountInLine(_cursorPosition.Y))
            // {
            //     newText = "";
            // }
            // else
            if (_cursorPosition.X < GetLettersCountInLine(_cursorPosition.Y))
            {
                string text = GetTextInLine(_cursorPosition.Y);
                SetTextInLine(text.Substring(0, _cursorPosition.X), _cursorPosition);
                newText = text.Substring(_cursorPosition.X);
            }
            // int lineNum = _cursorPosition.Y + 1;

            // AddNewLine(newText, lineNum, isRealBreak); // <- CheckWidth(); //Есть в addNewLine
            string changedText = GetTextInLine(lineNum);
            if (!String.IsNullOrEmpty(changedText))
            {
                newText += changedText;
            }
            SetTextInLine(newText, new SpaceVIL.Core.Point(0, lineNum));
        }

        internal void Clear()
        {
            _lineBreakes = new List<int>();
            _lineBreakes.Add(0);
            SetTextInLine("", new SpaceVIL.Core.Point(0, 0)); //_linesList[0].SetItemText("");
            RemoveLines(1, _linesList.Count - 1);

            CheckWidth();
        }

        private int CoordXToPos(int coordX, int lineNumb)
        {
            int pos = 0;

            List<int> lineLetPos = GetTextLine(lineNumb).GetLetPosArray(); //_linesList[lineNumb].GetLetPosArray();
            if (lineLetPos == null)
            {
                return pos;
            }

            for (int i = 0; i < lineLetPos.Count; i++)
            {
                if (lineLetPos[i] + globalXShift <= coordX + 3)
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

        internal SpaceVIL.Core.Point CheckLineFits(SpaceVIL.Core.Point checkPoint)
        {
            SpaceVIL.Core.Point outPt = new SpaceVIL.Core.Point();
            outPt.Y = checkPoint.Y;
            //??? check line count
            if (outPt.Y >= GetLinesCount()) {
                outPt.Y = GetLinesCount() - 1;
            }

            if (outPt.Y == -1)
            {
                outPt.Y = 0;
            }
            outPt.X = checkPoint.X;
            if (outPt.X == -1)
            {
                outPt.X = 0;
            }

            outPt.X = CheckLineWidth(outPt.X, checkPoint);

            return outPt;
        }

        private SpaceVIL.Core.Point CursorPosToCoord(SpaceVIL.Core.Point cPos0)
        {
            SpaceVIL.Core.Point cPos = CheckLineFits(cPos0); //??? moved to the ReplaceCursor
            SpaceVIL.Core.Point coord = new SpaceVIL.Core.Point(0, 0);
            coord.Y = GetLineY(cPos.Y);

            int letCount = GetLettersCountInLine(cPos.Y);
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
                coord.X = GetLetPosInLine(cPos.Y, cPos.X - 1) + cursorWidth;
            }
            return coord;
        }

        private int GetLetPosInLine(int cPosY, int cPosX)
        {
            return GetTextLine(cPosY).GetLetPosArray()[cPosX]; //_linesList[cPosY].GetLetPosArray()[cPosX];
        }

        private SpaceVIL.Core.Point CursorPosToCoordAndGlobalShifts(SpaceVIL.Core.Point cursorPos)
        {
            SpaceVIL.Core.Point coord = CursorPosToCoord(cursorPos);
            return SumPoints(coord, new SpaceVIL.Core.Point(globalXShift, globalYShift));
        }

        private SpaceVIL.Core.Point SumPoints(SpaceVIL.Core.Point cursorPos, SpaceVIL.Core.Point adderPoint)
        {
            SpaceVIL.Core.Point coord = new SpaceVIL.Core.Point(cursorPos.X, cursorPos.Y);
            coord.X += adderPoint.X;
            coord.Y += adderPoint.Y;
            return coord;
        }

        internal SpaceVIL.Core.Point ReplaceCursorAccordingCoord(SpaceVIL.Core.Point realPos)
        {
            Prototype parent = GetParent();
            realPos.Y -= parent.GetY() + parent.GetPadding().Top + globalYShift + GetTextMargin().Top;
            int lineNumb = realPos.Y / GetLineY(1);
            if (lineNumb >= GetLinesCount())
            {
                lineNumb = GetLinesCount() - 1;
            }
            if (lineNumb < 0)
            {
                lineNumb = 0;
            }

            realPos.X -= parent.GetX() + parent.GetPadding().Left + GetTextMargin().Left;

            SpaceVIL.Core.Point outPt = new SpaceVIL.Core.Point(0, 0);
            outPt.Y = lineNumb;
            outPt.X = CoordXToPos(realPos.X, lineNumb);
            return outPt;
        }

        private void CombineLines(SpaceVIL.Core.Point combinePos) //int topLineY)
        {
            if (combinePos.Y >= GetLinesCount() - 1)
            {
                return;
            }
            string text = GetTextInLine(combinePos.Y); //_linesList[topLineY].GetItemText();
            text += GetTextInLine(combinePos.Y + 1); //_linesList[topLineY + 1].GetItemText();

            if (CheckIsWrap())
            {
                int lineNum = combinePos.Y + 1;
                int currentLineVal = _lineBreakes[lineNum];
                int prevLineVal = (lineNum > 0) ? _lineBreakes[lineNum - 1] : -1;
                int nextLineVal = (lineNum < _lineBreakes.Count - 1) ? _lineBreakes[lineNum + 1] : currentLineVal;
                
                if (prevLineVal != currentLineVal && currentLineVal == nextLineVal)
                {
                    for (int i = lineNum; i < _lineBreakes.Count; i++)
                    {
                        _lineBreakes[i] = _lineBreakes[i] - 1;
                    }
                }
            }

            RemoveLines(combinePos.Y + 1, combinePos.Y + 1);
            SetTextInLine(text, combinePos); //_linesList[topLineY].SetItemText(text);

            CheckWidth();
        }

        internal void CombineLinesOrRemoveLetter(SpaceVIL.Core.Point combinePos, KeyCode keyCode)
        {
            if (!CheckIsWrap())
            {
                CombineLines(combinePos);
                return;
            }
            
            //line is not last is checked before call
            int currentLineVal = _lineBreakes[combinePos.Y];
            int nextLineVal = (combinePos.Y < _lineBreakes.Count - 1) ? _lineBreakes[combinePos.Y + 1] : currentLineVal + 1;

            //???
            String currentText = GetTextInLine(combinePos.Y);
            String nextText = GetTextInLine(combinePos.Y + 1);
            if (currentLineVal != nextLineVal || currentText.Length == 0 || nextText.Length == 0)
            {
                CombineLines(combinePos);
                return;
            }

            if (keyCode == KeyCode.Backspace)
            {
                combinePos.X--;
                SetTextInLine(currentText.Substring(0, currentText.Length - 1), combinePos); //new Point(combinePos));
            }
            else if (keyCode == KeyCode.Delete)
            {
                combinePos.X = 0;
                combinePos.Y++;
                SetTextInLine(nextText.Substring(1), combinePos); //new Point(combinePos));
            }
        }

        private void RemoveLines(int fromLine, int toLine)
        {
            int inc = fromLine;
            bool isWrapped = CheckIsWrap();
            while (inc <= toLine)
            {
                GetTextLine(fromLine).SetParent(null); //_linesList[fromLine].SetParent(null);
                _linesList.RemoveAt(fromLine);

                if (isWrapped)
                {
                    RemoveLineBreakes(fromLine);
                }

                inc++;
            }

            UpdLinesYShift(); //Пока обновляются все, но в принципе, нужно только под fromLine
        }

        private void RemoveLineBreakes(int lineNum)
        {
            if (lineNum >= _lineBreakes.Count)
            {
                return;
            }

            int lineVal = _lineBreakes[lineNum];
            int prevLineVal = (lineNum > 0) ? _lineBreakes[lineNum - 1] : -1;
            int nextLineVal = (lineNum < _lineBreakes.Count - 1) ? _lineBreakes[lineNum + 1] : lineVal;

            _lineBreakes.RemoveAt(lineNum);

            if (lineVal != prevLineVal && lineVal != nextLineVal)
            {
                for (int i = lineNum; i < _lineBreakes.Count; i++)
                {
                    _lineBreakes[i] = _lineBreakes[i] - 1;
                }
            }
        }

        private void UpdLinesYShift()
        {
            int inc = 0;
            foreach (TextLine line in _linesList)
            {
                line.SetLineYShift(GetLineY(inc) + globalYShift);
                inc++;
            }
            _isUpdateTextureNeed = true;
        }

        private void UpdLinesXShift()
        {
            foreach (TextLine line in _linesList)
            {
                line.SetLineXShift(globalXShift);
            }
            _isUpdateTextureNeed = true;
        }

        private void SetAllowHeight(bool isCursorChanged)
        {
            if (isCursorChanged)
            {
                foreach (TextLine line in _linesList)
                {
                    line.SetAllowHeight(_cursorYMax);
                }
            }
            UpdLinesYShift();
        }

        private void SetAllowWidth(bool isCursorChanged)
        {
            if (isCursorChanged)
            {
                foreach (TextLine line in _linesList)
                {
                    line.SetAllowWidth(_cursorXMax);
                }
            }
            UpdLinesXShift();
        }

        private int GetLineY(int num)
        {
            return (_lineHeight + _lineSpacer) * num;
        }

        private int GetLineY(float num)
        {
            return (int)((_lineHeight + _lineSpacer) * num);
        }

        internal int GetLinesCount()
        {
            return _linesList.Count;
        }

        internal void SetTextMargin(Indents margin)
        {
            foreach (TextLine var in _linesList)
            {
                var.SetMargin(margin);
            }
            UpdateLayout();
        }

        internal Indents GetTextMargin()
        {
            return GetTextLine(0).GetMargin(); //_linesList[0].GetMargin();
        }

        private int[] GetDims()
        {
            int[] output = GetTextLine(0).GetFontDims(); //_linesList[0].GetFontDims();
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
            {
                return;
            }
            if (!font.Equals(_elementFont))
            {
                _elementFont = font;

                if (_linesList == null)
                {
                    return;
                }
                foreach (TextLine te in _linesList)
                {
                    te.SetFont(font);
                }

                GetDims(); // <- SetLineSpacer <- UpdLinesYShift
                UpdateLayout();
            }
        }

        internal Font GetFont()
        {
            return _elementFont;
        }

        internal void SetTextInLine(string text, SpaceVIL.Core.Point cursorPos) //int lineY)
        {
            GetTextLine(cursorPos.Y).SetItemText(text); //_linesList[lineY].SetItemText(text);
            if (CheckIsWrap())
            {
                WrapLine(cursorPos.Y);
                SpaceVIL.Core.Point newPos = FindNewPosition(cursorPos);
                cursorPos.X = newPos.X;
                cursorPos.Y = newPos.Y;
            }
            CheckWidth(); // <- _isUpdateTextureNeed = true;
        }

        internal string GetTextInLine(int lineNum)
        {
            return GetTextLine(lineNum).GetItemText(); //_linesList[lineNum].GetItemText();
        }

        internal int GetTextHeight()
        {
            return GetLineY(_linesList.Count);
        }

        private string _wholeText = "";

        internal string GetWholeText()
        {
            StringBuilder sb = new StringBuilder();
            if (_linesList == null)
            {
                return "";
            }
            if (_linesList.Count == 1) //Кажется, else покрывает if, проверить
            {
                sb.Append(GetTextInLine(0)); //_linesList[0].GetText());
            }
            else
            {
                if (CheckIsWrap() && (_lineBreakes.Count == _linesList.Count))
                {
                    MakeTextAccordingToBreaks(sb);
                }
                else
                {
                    MakeUnwrapText(sb);
                }
            }
            _wholeText = sb.ToString();
            return _wholeText;
        }

        internal void GetSelectedText(StringBuilder sb, SpaceVIL.Core.Point fromPt, SpaceVIL.Core.Point toPt)
        {
            if (CheckIsWrap())
            {
                MakeTextAccordingToBreaks(sb, fromPt, toPt);
            }
            else
            {
                MakeUnwrapText(sb, fromPt, toPt);
            }
        }

        private void MakeUnwrapText(StringBuilder sb)
        {
            MakeUnwrapText(sb, new SpaceVIL.Core.Point(0, 0), new SpaceVIL.Core.Point(GetLettersCountInLine(_linesList.Count - 1), _linesList.Count - 1));
        }

        private void MakeUnwrapText(StringBuilder sb, SpaceVIL.Core.Point fromPt, SpaceVIL.Core.Point toPt)
        {
            if (fromPt.X >= GetLettersCountInLine(fromPt.Y))
            {
                sb.Append("\n");
            }
            else
            {
                String textFirst = GetTextInLine(fromPt.Y);
                sb.Append(textFirst.Substring(fromPt.X));
                sb.Append("\n");
            }
            for (int i = fromPt.Y + 1; i < toPt.Y; i++)
            {
                sb.Append(GetTextInLine(i));
                sb.Append("\n");
            }

            String textLast = GetTextInLine(toPt.Y).Substring(0, toPt.X);
            sb.Append(textLast);
        }

        private void MakeTextAccordingToBreaks(StringBuilder sb)
        {
            MakeTextAccordingToBreaks(sb, new SpaceVIL.Core.Point(0, 0), new SpaceVIL.Core.Point(GetLettersCountInLine(_linesList.Count - 1), _linesList.Count - 1));
        }

        private void MakeTextAccordingToBreaks(StringBuilder sb, SpaceVIL.Core.Point fromPt, SpaceVIL.Core.Point toPt)
        {
            int currentVal = _lineBreakes[fromPt.Y];
            int nextVal = (fromPt.Y < toPt.Y) ? _lineBreakes[fromPt.Y + 1] : currentVal;
            String textFirst = "";

            if (fromPt.X < GetLettersCountInLine(fromPt.Y))
            {
                textFirst = GetTextInLine(fromPt.Y).Substring(fromPt.X);
            }

            sb.Append(textFirst);
            if (currentVal != nextVal)
            {
                sb.Append("\n");
            }
            currentVal = nextVal;

            for (int i = fromPt.Y + 1; i < toPt.Y; i++) //0; i < _linesList.size() - 1; i++) {
            {
                nextVal = _lineBreakes[i + 1];
                sb.Append(GetTextInLine(i)); //_linesList.get(i).getText());
                if (currentVal != nextVal)
                {
                    sb.Append("\n");
                }
                currentVal = nextVal;
            }
            String textLast = GetTextInLine(toPt.Y).Substring(0, toPt.X);
            sb.Append(textLast); //_linesList.get(_linesList.size() - 1).getText());
        }

        internal SpaceVIL.Core.Point SetText(string text)
        {
            SpaceVIL.Core.Point curPos = new SpaceVIL.Core.Point(0, 0);
            if (String.IsNullOrEmpty(text)) //text == null || text.Equals(""))
            {
                Clear();
            }
            else //if (!text.Equals(GetWholeText()))
            {
                curPos = SplitAndMakeLines(text); // <- CheckWidth <- _isUpdateTextureNeed = true;
            }
            return curPos;
        }

        internal void SetLineSpacer(int lsp)
        {
            if (lsp < _minLineSpacer)
            {
                lsp = _minLineSpacer;
            }

            if (lsp != _lineSpacer)
            {
                _lineSpacer = lsp;

                if (_linesList == null)
                {
                    return;
                }

                UpdLinesYShift();
            }
        }

        internal int GetLineSpacer()
        {
            return _lineSpacer;
        }

        private SpaceVIL.Core.Point SplitAndMakeLines(string text)
        {
            Clear();

            _wholeText = text;

            string[] line = text.Split('\n');
            // int inc = 0;
            string s;

            s = line[0].TrimEnd('\r');
            SetTextInLine(s, new SpaceVIL.Core.Point(line[0].Length, 0)); //_linesList[0].SetItemText(s);

            for (int i = 1; i < line.Length; i++)
            {
                s = line[i].TrimEnd('\r');
                AddNewLine(s, _linesList.Count);
            }

            SpaceVIL.Core.Point curPos = new SpaceVIL.Core.Point(0, 0);
            curPos.Y = _linesList.Count - 1; //line.Length - 1;
            curPos.X = GetLettersCountInLine(curPos.Y);

            CheckWidth();
            return curPos;
        }

        internal void CutText(SpaceVIL.Core.Point fromReal, SpaceVIL.Core.Point toReal)
        {
            if (fromReal.Y == toReal.Y)
            {
                string sb = GetTextInLine(toReal.Y); //
                // _linesList[toReal.Y].SetItemText(_linesList[toReal.Y].GetItemText().Remove(fromReal.X, toReal.X - fromReal.X));
                SetTextInLine(sb.Remove(fromReal.X, toReal.X - fromReal.X), toReal);
            }
            else
            {
                StringBuilder firstLinePartText = new StringBuilder(GetTextInLine(fromReal.Y).Substring(0, fromReal.X));
                StringBuilder lastLinePartText = new StringBuilder(GetTextInLine(toReal.Y).Substring(toReal.X));
                RemoveLines(fromReal.Y + 1, toReal.Y - 1);
                SetTextInLine(firstLinePartText.Append(lastLinePartText).ToString(), fromReal); //.y);
                // _linesList[fromReal.Y].SetItemText(_linesList[fromReal.Y].GetItemText().Substring(0, fromReal.X));
                // _linesList[fromReal.Y + 1].SetItemText(_linesList[fromReal.Y + 1].GetItemText().Substring(toReal.X));
                // CombineLines(fromReal.Y);
            }

            CheckWidth();
        }

        internal SpaceVIL.Core.Point PasteText(string pasteStr, SpaceVIL.Core.Point _cursor_position)
        {
            string textInLine = GetTextInLine(_cursor_position.Y); //_linesList[_cursor_position.Y].GetItemText()
            string textBeg = textInLine.Substring(0, _cursor_position.X);
            string textEnd = "";
            if (_cursor_position.X < GetLettersCountInLine(_cursor_position.Y))
            {
                textEnd = textInLine.Substring(_cursor_position.X);
            }

            string[] line = pasteStr.Split('\n');
            for (int i = 0; i < line.Length; i++)
            {
                line[i] = line[i].TrimEnd('\r');
            }

            if (line.Length == 1)
            {
                // _linesList[_cursor_position.Y].SetItemText(textBeg + line[0] + textEnd);
                _cursor_position.X += pasteStr.Length;
                SetTextInLine(textBeg + line[0] + textEnd, _cursor_position);
            }
            else
            {
                // _linesList[_cursor_position.Y].SetItemText(textBeg + line[0]);
                // int ind = _cursor_position.Y + 1;
                // for (int i = 1; i < line.Length - 1; i++)
                // {
                //     AddNewLine(line[i], ind);
                //     ind++;
                // }

                // AddNewLine(line[line.Length - 1] + textEnd, ind);

                // _cursor_position.X = line[line.Length - 1].Length;
                // _cursor_position.Y += line.Length - 1;


                //------------------------------------------------------------------------------
                BreakLine(_cursor_position);
                int beforeSize = _linesList.Count;
                SetTextInLine(textBeg + line[0], new SpaceVIL.Core.Point(0, _cursor_position.Y)); //_cursor_position.y); //textBeg = getTextInLine(_cursor_position.y);
                int nextLineNumb = _cursor_position.Y;
                int afterSize = _linesList.Count;
                nextLineNumb += (afterSize - beforeSize) + 1;
                for (int i = 1; i < line.Length - 1; i++)
                {
                    beforeSize = afterSize;
                    AddNewLine(line[i], nextLineNumb);
                    afterSize = _linesList.Count;
                    nextLineNumb += (afterSize - beforeSize);
                }

                _cursor_position = new SpaceVIL.Core.Point(line[line.Length - 1].Length, nextLineNumb);
                SetTextInLine(line[line.Length - 1] + textEnd, _cursor_position); //nextLineNumb);
            }

            CheckWidth();
            return _cursor_position;
        }

        private SpaceVIL.Core.Point FindNewPosition(SpaceVIL.Core.Point oldCoord)
        {
            int linesInc = oldCoord.Y;
            while (oldCoord.X >= 0 && linesInc < _linesList.Count)
            {
                int lineLength = GetTextInLine(linesInc).Length;
                if (lineLength < oldCoord.X)
                {
                    oldCoord.X -= lineLength;
                    linesInc++;
                }
                else
                {
                    return new SpaceVIL.Core.Point(oldCoord.X, linesInc); //_cursor_position.x = lastLineLength; // - 1;
                }
            }

            return new SpaceVIL.Core.Point(0, oldCoord.Y);
        }

        internal SpaceVIL.Core.Point AddXYShifts(SpaceVIL.Core.Point point)
        {
            SpaceVIL.Core.Point outPoint = CursorPosToCoord(point);
            Prototype parent = GetParent();
            if (parent == null)
                return new SpaceVIL.Core.Point(0, 0);
            int offset = _cursorXMax / 3;
            if (globalXShift + outPoint.X < 0)
            {
                globalXShift = -outPoint.X;
                globalXShift += offset;
                if (globalXShift > 0)
                {
                    globalXShift = 0;
                }
                UpdLinesXShift();
            }
            if (globalXShift + outPoint.X > _cursorXMax)
            {
                globalXShift = _cursorXMax - outPoint.X;

                if (outPoint.X < GetWidth())
                {
                    globalXShift -= offset;
                }

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

            outPoint.X += parent.GetX() + parent.GetPadding().Left + GetTextMargin().Left;
            outPoint.Y += parent.GetY() + parent.GetPadding().Top + GetTextMargin().Top;

            return outPoint;
        }

        private bool CheckPoints(SpaceVIL.Core.Point point)
        {
            return (!(point.Y >= _linesList.Count));
        }

        internal List<SpaceVIL.Core.Point> SelectedArrays(SpaceVIL.Core.Point fromPt, SpaceVIL.Core.Point toPt)
        {
            if (!CheckPoints(fromPt))
            {
                return null;
            }
            if (!CheckPoints(toPt))
            {
                return null;
            }

            int cursorHeight = GetCursorHeight();
            List<SpaceVIL.Core.Point> selectionRectangles = new List<SpaceVIL.Core.Point>();
            Prototype parent = GetParent();
            SpaceVIL.Core.Point adderPt = new SpaceVIL.Core.Point();
            adderPt.X = parent.GetX() + parent.GetPadding().Left + GetTextMargin().Left;
            adderPt.Y = parent.GetY() + parent.GetPadding().Top + GetTextMargin().Top;

            SpaceVIL.Core.Point tmp = new SpaceVIL.Core.Point();
            SpaceVIL.Core.Point xy1;
            SpaceVIL.Core.Point xy2;
            int lsp = GetLineSpacer();

            if (fromPt.Y == toPt.Y)
            {
                if (GetTextLine(fromPt.Y).GetTexture() == null) // _linesList[fromPt.Y].GetLetTextures() == null)
                {
                    return null;
                }

                xy1 = CursorPosToCoordAndGlobalShifts(fromPt);
                xy2 = CursorPosToCoordAndGlobalShifts(toPt);

                if (xy2.X < 0 || xy1.X > _cursorXMax)
                {
                    return null;
                }

                if (xy1.X < 0)
                {
                    xy1.X = 0;
                }

                if (xy2.X > _cursorXMax)
                {
                    xy2.X = _cursorXMax;
                }

                xy1 = SumPoints(xy1, adderPt);
                xy2 = SumPoints(xy2, adderPt);
                selectionRectangles.Add(new SpaceVIL.Core.Point(xy1.X, xy1.Y - cursorHeight - lsp / 2 + 1));
                selectionRectangles.Add(new SpaceVIL.Core.Point(xy2.X, xy2.Y - lsp / 2 + 1));

                return selectionRectangles;
            }

            xy1 = CursorPosToCoordAndGlobalShifts(fromPt);
            tmp.X = GetLettersCountInLine(fromPt.Y);
            tmp.Y = fromPt.Y;
            xy2 = CursorPosToCoordAndGlobalShifts(tmp);

            if (GetTextLine(fromPt.Y).GetTexture() != null) //_linesList[fromPt.Y].GetLetTextures() != null)
            {
                if (xy2.X >= 0 && xy1.X <= _cursorXMax)
                {
                    if (xy1.X < 0)
                    {
                        xy1.X = 0;
                    }

                    if (xy2.X > _cursorXMax)
                    {
                        xy2.X = _cursorXMax;
                    }

                    xy1 = SumPoints(xy1, adderPt);
                    xy2 = SumPoints(xy2, adderPt);

                    selectionRectangles.Add(new SpaceVIL.Core.Point(xy1.X, xy1.Y - cursorHeight - lsp / 2 + 1));
                    selectionRectangles.Add(new SpaceVIL.Core.Point(xy2.X, xy2.Y - lsp / 2 + 1));
                }
            }

            tmp.X = 0;
            tmp.Y = toPt.Y;
            xy1 = CursorPosToCoordAndGlobalShifts(tmp);
            xy2 = CursorPosToCoordAndGlobalShifts(toPt);

            if (GetTextLine(toPt.Y).GetTexture() != null) //_linesList[toPt.Y].GetLetTextures() != null)
            {
                if (xy2.X >= 0 && xy1.X <= _cursorXMax)
                {
                    if (xy1.X < 0)
                    {
                        xy1.X = 0;
                    }

                    if (xy2.X > _cursorXMax)
                    {
                        xy2.X = _cursorXMax;
                    }

                    xy1 = SumPoints(xy1, adderPt);
                    xy2 = SumPoints(xy2, adderPt);
                    selectionRectangles.Add(new SpaceVIL.Core.Point(xy1.X, xy1.Y - cursorHeight - lsp / 2 + 1));
                    selectionRectangles.Add(new SpaceVIL.Core.Point(xy2.X, xy2.Y - lsp / 2 + 1));
                }
            }

            for (int i = fromPt.Y + 1; i < toPt.Y; i++)
            {
                tmp.X = 0;
                tmp.Y = i;
                xy1 = CursorPosToCoordAndGlobalShifts(tmp);

                tmp.X = GetLettersCountInLine(i);
                tmp.Y = i;
                xy2 = CursorPosToCoordAndGlobalShifts(tmp);

                if (GetTextLine(i).GetTexture() != null) //_linesList[i].GetLetTextures() != null)
                {
                    if (xy2.X >= 0 && xy1.X <= _cursorXMax)
                    {
                        if (xy1.X < 0)
                        {
                            xy1.X = 0;
                        }

                        if (xy2.X > _cursorXMax)
                        {
                            xy2.X = _cursorXMax;
                        }

                        xy1 = SumPoints(xy1, adderPt);
                        xy2 = SumPoints(xy2, adderPt);
                        selectionRectangles.Add(new SpaceVIL.Core.Point(xy1.X, xy1.Y - cursorHeight - lsp / 2 + 1));
                        selectionRectangles.Add(new SpaceVIL.Core.Point(xy2.X, xy2.Y - lsp / 2 + 1));
                    }
                }
            }

            return selectionRectangles;
        }

        private Color _foreground = Color.Black;

        internal void SetForeground(Color foreground)
        {
            if (_linesList == null || foreground == null)
            {
                return;
            }
            if (!foreground.Equals(GetForeground()))
            {
                _foreground = foreground;
                foreach (TextLine te in _linesList)
                {
                    te.SetForeground(foreground); //Вроде бы это больше не нужно
                }
            }
        }

        public Color GetForeground()
        {
            return _foreground;
        }


        private bool _isUpdateTextureNeed = true;
        private TextPrinter _blockTexture = null;
        private int _firstVisibleLineNumb = -1;
        public ITextImage GetTexture()
        {
            Monitor.Enter(textInputLock);
            try
            {
                Prototype parent = GetParent();
                if (parent == null)
                {
                    return null;
                }

                if (_isUpdateTextureNeed)
                {
                    float _screenScale = 1;
                    CoreWindow wLayout = GetHandler();
                    if (wLayout == null)// || Common.DisplayService.GetDpiScale() == null) // && Common.DisplayService.GetDpiScale().Length > 0) //wLayout == null || wLayout.GetDpiScale() == null)
                    {
                        _screenScale = 1;
                    }
                    else
                    {
                        _screenScale = DisplayService.GetDisplayDpiScale().GetX(); //Common.DisplayService.GetDpiScale()[0]; //wLayout.GetDpiScale()[0];
                        if (_screenScale == 0)
                        {
                            _screenScale = 1;
                        }
                    }

                    List<ITextImage> tpLines = new List<ITextImage>();
                    int w = 0, h = 0, bigWidth = 0;
                    int lineHeigh = (int)(GetLineY(1) * _screenScale);
                    int visibleHeight = 0;
                    _firstVisibleLineNumb = -1; // int startNumb = -1;
                    int inc = -1;

                    foreach (TextLine tl in _linesList)
                    {
                        inc++;
                        ITextImage tmp = tl.GetTexture();
                        tpLines.Add(tmp);
                        h += lineHeigh;//tmp.HeightTexture;
                        w = (w > tl.GetWidth()) ? w : tl.GetWidth();
                        if (tmp == null)
                        {
                            continue;
                        }
                        //if (_screenScale != 1)
                        //{
                        //    int bw = 0;
                        //    if (tmp != null)
                        int bw = tmp.GetWidth();
                        bigWidth = (bigWidth > bw) ? bigWidth : bw;
                        //}
                        //w = (w > tmp.WidthTexture) ? w : tmp.WidthTexture;
                        visibleHeight += lineHeigh;
                        if (_firstVisibleLineNumb == -1) //(startNumb == -1)
                            _firstVisibleLineNumb = inc; //startNumb = inc;
                    }
                    //w += _cursorXMax / 3;
                    SetWidth(w);
                    SetHeight((int)((float)h / _screenScale));
                    //if (_screenScale != 1)
                    //{
                    w = bigWidth;
                    //}
                    // Console.WriteLine("texture width " + w + " " + GetWidth() + " " + _cursorXMax + "\n");
                    byte[] bigByte = new byte[visibleHeight * w * 4]; //h
                    int bigOff = 0;

                    foreach (TextPrinter tptmp in tpLines)
                    {
                        if (tptmp == null)
                        {
                            continue;
                        }
                        if (tptmp.GetBytes() == null)
                        {
                            bigOff += lineHeigh * w * 4;
                            continue;
                        }

                        for (int p = 0; p < 4; p++)
                        {
                            for (int j = 0; j < tptmp.GetHeight(); j++)
                            {
                                for (int i = 0; i < tptmp.GetWidth(); i++)
                                {
                                    bigByte[bigOff + p + i * 4 + j * (w * 4)] = tptmp.GetBytes()[p + i * 4 + j * (tptmp.GetWidth() * 4)];
                                }

                                for (int i = tptmp.GetWidth(); i < w; i++)
                                {
                                    bigByte[bigOff + p + i * 4 + j * (w * 4)] = 0;
                                }
                            }

                            for (int j = tptmp.GetHeight(); j < lineHeigh; j++)
                            {
                                for (int i = 0; i < w; i++)
                                {
                                    bigByte[bigOff + p + i * 4 + j * (w * 4)] = 0;
                                }
                            }
                        }
                        bigOff += lineHeigh * w * 4;
                    }
                    _blockTexture = new TextPrinter(bigByte); //TextPrinter tpout = new TextPrinter(bigByte);
                    // Console.WriteLine("Here texture = " + w + " " + GetWidth());
                    _blockTexture.SetSize(w, visibleHeight);
                    // _blockTexture.WidthTexture = w; //tpout.WidthTexture = w;
                    // _blockTexture.HeightTexture = visibleHeight; // h; //tpout.HeightTexture = visibleHeight; // h;
                                                                 //     tpout.XTextureShift = parent.GetPadding().Left + GetTextMargin().Left + parent.GetX() + cursorWidth;
                                                                 //     tpout.YTextureShift = parent.GetPadding().Top + GetTextMargin().Top + parent.GetY();

                    // //                if (tpLines.Count == 0 || tpLines[0] == null)
                    // //                {
                    // //                    tpout.XTextureShift = parent.GetPadding().Left + GetTextMargin().Left + parent.GetX();
                    // //                    tpout.YTextureShift = parent.GetPadding().Top + GetTextMargin().Top + parent.GetY();
                    // //
                    // //                    tpout.XTextureShift += 0; // _linesList[0].GetLineXShift();
                    // //                    tpout.YTextureShift += 0; // _linesList[0].GetLineYShift();
                    // //                }
                    // //                else
                    // //                {
                    // //                    tpout.XTextureShift = parent.GetPadding().Left + GetTextMargin().Left + parent.GetX(); //tpLines[0].XTextureShift;
                    // //                    tpout.YTextureShift = parent.GetPadding().Top + GetTextMargin().Top + parent.GetY(); //tpLines[0].YTextureShift;
                    // //               }

                    //     if (startNumb > -1)
                    //         tpout.YTextureShift += _linesList[startNumb].GetLineYShift();

                    _isUpdateTextureNeed = false;
                    ItemsRefreshManager.SetRefreshText(this);
                }
                UpdateCoords(parent);
                return _blockTexture; //tpout;
            }
            finally
            {
                Monitor.Exit(textInputLock);
            }
        }

        private void UpdateCoords(Prototype parent)
        {
            _blockTexture.SetPosition(parent.GetPadding().Left + GetTextMargin().Left + parent.GetX() + cursorWidth,
                parent.GetPadding().Top + GetTextMargin().Top + parent.GetY());
            // _blockTexture.XTextureShift = parent.GetPadding().Left + GetTextMargin().Left + parent.GetX() + cursorWidth;
            // _blockTexture.YTextureShift = parent.GetPadding().Top + GetTextMargin().Top + parent.GetY();

            if (_firstVisibleLineNumb > -1)
            {
                // _blockTexture.YTextureShift += _linesList[_firstVisibleLineNumb].GetLineYShift();
                _blockTexture.SetYOffset(_blockTexture.GetYOffset() + _linesList[_firstVisibleLineNumb].GetLineYShift());
            }
        }

        private float _stepFactor = 1.0f;

        internal void SetScrollStepFactor(float value)
        {
            _stepFactor = value;
        }

        internal int GetScrollStep()
        {
            // return GetLineY(1);
            return GetLineY(_stepFactor);
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
            _isUpdateTextureNeed = true;
        }

        private Regex patternWordBounds = new Regex(@"\W|_");
        internal int[] FindWordBounds(SpaceVIL.Core.Point cursorPosition)
        {
            //С положение курсора должно быть все в порядке, не нужно проверять вроде бы
            String lineText = GetTextInLine(cursorPosition.Y);
            int index = cursorPosition.X;

            String testString = lineText.Substring(index);
            MatchCollection matcher = patternWordBounds.Matches(testString);

            int begPt = 0;
            int endPt = GetLettersCountInLine(cursorPosition.Y);

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

        //Wrap Text Stuff---------------------------------------------------------------------------------------------------

        private void WrapLine(int lineNum)
        {
            TextLine textLine = GetTextLine(lineNum); //_linesList.get(lineNum);

            if (textLine.GetWidth() == _cursorXMax)
            {
                return;
            }

            bool isLinesChanged;

            if (lineNum > 0)
            {
                isLinesChanged = HasThisAndNextLineCombination(lineNum - 1);
                if (isLinesChanged)
                {
                    return;
                }
            }

            if (lineNum < _lineBreakes.Count - 1)
            {
                isLinesChanged = HasThisAndNextLineCombination(lineNum);
                if (isLinesChanged)
                {
                    return;
                }
            }

            // if (textLine.GetWidth() < _cursorXMax) // parentAllowWidth
            // {
            //     if (lineVal == nextLineVal)
            //     {
            //         int nextLet = GetTextLine(lineNum + 1).GetLetPosArray()[0];
            //         if (textLine.GetWidth() + nextLet < _cursorXMax)
            //         {
            //             CombineLines(new SpaceVIL.Core.Point(textInLine.Length, lineNum));
            //         }
            //     }
            //     return;
            // }

            if (textLine.GetWidth() < _cursorXMax) // parentAllowWidth
            {
                return;
            }

            int lineVal = _lineBreakes[lineNum];
            int nextLineVal = (lineNum < _lineBreakes.Count - 1) ? _lineBreakes[lineNum + 1] : lineVal + 1;
            String textInLine = textLine.GetText();

            List<int> letPosArr = textLine.GetLetPosArray();

            if (letPosArr.Count == 0)
            {
                return;
            }

            List<int> listSpace = new List<int>();
            List<int> listPos = new List<int>();

            int ind = 0;
            int pos = textInLine.IndexOf(" ", ind);
            while (pos >= 0)
            {
                listSpace.Add(letPosArr[pos]);
                listPos.Add(pos);
                ind = pos + 1;
                pos = textInLine.IndexOf(" ", ind);
            }

            SpaceVIL.Core.Point breakPos;
            int splitPos = 0;
            if (listSpace.Count == 0) //one long word
            {
                splitPos = letPosArr.Count - 1; //letPosArr.get(letPosArr.size() - 1);
            }
            else
            {
                splitPos = BinarySearch(0, listSpace.Count - 1, listSpace, _cursorXMax);

                splitPos = listPos[splitPos] + 1; //After space
                if (splitPos >= letPosArr.Count)
                {
                    splitPos = letPosArr.Count - 1;
                }
            }

            if (letPosArr[splitPos] > _cursorXMax)
            {
                //one long word
                splitPos = BinarySearch(0, letPosArr.Count - 1, letPosArr, _cursorXMax);
            }

            if (splitPos == letPosArr.Count - 1 || splitPos == 0)
            {
                return; //or it will be splitted with an empty linef
            }

            breakPos = new SpaceVIL.Core.Point(splitPos, lineNum);

            if (lineVal == nextLineVal)
            {
                String text = GetTextInLine(breakPos.Y);
                StringBuilder newText = new StringBuilder(text.Substring(breakPos.X));
                newText.Append(GetTextInLine(breakPos.Y + 1));
                textLine.SetItemText(text.Substring(0, breakPos.X)); // setTextInLine(text.substring(0, breakPos.x), new Point(breakPos.x, breakPos.y));
                SetTextInLine(newText.ToString(), new SpaceVIL.Core.Point(newText.Length, breakPos.Y + 1));
            }
            else
            {
                // BreakLine(breakPos, false);
                string newText = "";
                if (breakPos.X < letPosArr.Count)
                {
                    string text = GetTextInLine(breakPos.Y);
                    textLine.SetItemText(text.Substring(0, breakPos.X)); //SetTextInLine(text.Substring(0, breakPos.X), breakPos);
                    newText = text.Substring(breakPos.X);
                }

                AddNewLine(newText, breakPos.Y + 1, false);
            }
        }

        private bool HasThisAndNextLineCombination(int lineNum)
        {
            //checking before call
            int currentLineVal = _lineBreakes[lineNum];
            int nextLineVal = _lineBreakes[lineNum + 1];
            if (currentLineVal == nextLineVal)
            {
                TextLine currentLine = GetTextLine(lineNum );
                string currentText = currentLine.GetText();
                TextLine nextLine = GetTextLine(lineNum + 1);
                string nextText = nextLine.GetText();

                if (currentLine.GetWidth() < _cursorXMax)
                {
                    List<int> nextLineLetPosArray = nextLine.GetLetPosArray();

                if (nextLineLetPosArray.Count != 0)
                {
                    if (currentText.EndsWith(" "))
                    {
                        int firstSpaceInd = nextText.IndexOf(" ");
                        if (firstSpaceInd > 0)
                        {
                            if (currentLine.GetWidth() + nextLineLetPosArray[firstSpaceInd] < _cursorXMax)
                            {
                                CombineLines(new SpaceVIL.Core.Point(currentText.Length, lineNum));
                                return true;
                            }
                        }
                    }
                    else if (!nextText.StartsWith(" "))
                    {
                        if (nextLineLetPosArray.Count >= 3)
                        {
                            if (currentLine.GetWidth() + nextLineLetPosArray[2] < _cursorXMax)
                            {
                                CombineLines(new SpaceVIL.Core.Point(currentText.Length, lineNum));
                                return true;
                            }
                        }
                    }
                }
            }

            //Пока плохо работает из-за положения курсора
            // if (nextText.StartsWith(" ") && !currentText.EndsWith(" ")) {
            //     //move space to previous line directly, without checking
            //     currentLine.SetItemText(currentText + " ");
            //     nextLine.SetItemText(nextText.Substring(1));
            // }
        }
        return false;
    }

        private int BinarySearch(int fromInd, int toInd, List<int> searchingList, int testValue)
        {
            while (toInd > fromInd)
            {
                int midInd = (toInd + fromInd) / 2;

                if (searchingList[midInd] == testValue)
                {
                    return midInd;
                }

                if (searchingList[midInd] > testValue)
                {
                    toInd = midInd - 1;
                }
                else
                {
                    fromInd = midInd + 1;
                }
            }

            if (searchingList[fromInd] > testValue && fromInd > 0)
            {
                fromInd--;
            }

            return fromInd;
        }

        internal void RewrapText()
        {
            SetText(GetWholeText());
        }

        private void UpdateLayout()
        {
            if (!CheckIsWrap())
            {
                return;
            }
            Monitor.Enter(textInputLock);
            try
            {
                RewrapText();
            }
            finally
            {
                Monitor.Exit(textInputLock);
            }
        }

        internal SpaceVIL.Core.Point WrapCursorPosToReal(SpaceVIL.Core.Point wrapPos)
        {
            //Convert wrap cursor position to the real position
            if (_lineBreakes.Count != _linesList.Count)
            {
                return wrapPos;
            }
            SpaceVIL.Core.Point realPos = new SpaceVIL.Core.Point(0, 0);
            int lineNum = FindLineBegInBreakLines(wrapPos.Y);
            int lineRealLength = wrapPos.X;
            for (int i = lineNum; i < wrapPos.Y; i++)
            {
                lineRealLength += GetTextInLine(i).Length;
            }

            realPos.X = lineRealLength;
            realPos.Y = _lineBreakes[wrapPos.Y];

            return realPos;
        }

        internal SpaceVIL.Core.Point RealCursorPosToWrap(SpaceVIL.Core.Point realPos)
        {
            //Convert real cursor position to the wrap position
            if (_lineBreakes.Count != _linesList.Count)
            {
                return realPos;
            }
            SpaceVIL.Core.Point wrapPos = new SpaceVIL.Core.Point(0, 0);

            int lineBeg = _lineBreakes[realPos.Y];
            if (lineBeg != realPos.Y) //which means less
            {
                lineBeg = BinarySearch(lineBeg, _lineBreakes.Count - 1, _lineBreakes, realPos.Y);
            }
            lineBeg = FindLineBegInBreakLines(lineBeg);

            int lineRealLength = realPos.X;
            int linesCount = GetLinesCount();
            while (lineBeg < linesCount - 1)
            {
                int len = GetTextInLine(lineBeg).Length;
                if (len >= lineRealLength)
                {
                    break;
                }
                lineRealLength -= len;
                lineBeg++;
            }

            wrapPos.X = lineRealLength;
            wrapPos.Y = lineBeg;

            return wrapPos;
        }

        private int FindLineBegInBreakLines(int wrapLineNum)
        {
            int lineBeg = wrapLineNum;
            int lineVal = _lineBreakes[lineBeg];
            if (lineVal == 0)
            {
                return 0;
            }

            while (lineBeg > 0 && _lineBreakes[lineBeg - 1] == lineVal)
            {
                lineBeg--;
                lineVal = _lineBreakes[lineBeg];
            }

            return lineBeg;
        }
    }
}