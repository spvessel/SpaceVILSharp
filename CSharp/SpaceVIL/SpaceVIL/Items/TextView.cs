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
    public class TextView : Prototype, IDraggable, ITextShortcuts, ITextWrap
    {
        private static int count = 0;
        private Point _cursorPosition = new Point(0, 0);
        private CustomSelector _selectedArea;

        private TextureStorage _textureStorage;

        private Point _selectFrom = new Point(-1, 0);
        private Point _selectTo = new Point(-1, 0);
        private bool _isSelect = false;

        public TextView()
        {
            SetItemName("TextBlock_" + count);
            count++;

            _textureStorage = new TextureStorage();
            
            _selectedArea = new CustomSelector();

            EventMousePress += OnMousePressed;
            EventMouseDrag += OnDragging;
            EventKeyPress += OnKeyPress;
            EventKeyRelease += OnKeyRelease;
            EventMouseDoubleClick += OnDoubleClick;
        }

        private Stopwatch _startTime = new Stopwatch();
        private bool _isDoubleClick = false;

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
                    }
                    int[] wordBounds = _textureStorage.FindWordBounds(_cursorPosition);

                    if (wordBounds[0] != wordBounds[1])
                    {
                        _isSelect = true;
                        _selectFrom = new Point(wordBounds[0], _cursorPosition.Y);
                        _selectTo = new Point(wordBounds[1], _cursorPosition.Y);
                        _cursorPosition = new Point(_selectTo.X, _selectTo.Y);
                        MakeSelectedArea();
                    }

                    _startTime.Restart();
                    _isDoubleClick = true;
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
                    }

                    if (_isDoubleClick && _startTime.ElapsedMilliseconds < 500) //Select line on triple click
                    {
                        _isSelect = true;
                        _selectFrom = new Point(0, _cursorPosition.Y);
                        _selectTo = new Point(GetLettersCountInLine(_cursorPosition.Y), _cursorPosition.Y);
                        _cursorPosition = new Point(_selectTo.X, _selectTo.Y);
                        MakeSelectedArea();
                    }
                }
                _isDoubleClick = false;
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
                        _selectFrom = new Point(_cursorPosition.X, _cursorPosition.Y);
                    }
                    else
                    {
                        _selectTo = new Point(_cursorPosition.X, _cursorPosition.Y);
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

        private void ReplaceCursorAccordingCoord(Point realPos)
        {
            _cursorPosition = _textureStorage.ReplaceCursorAccordingCoord(realPos);
        }

        private void OnKeyRelease(object sender, KeyArgs args)
        { }

        private void OnKeyPress(object sender, KeyArgs args)
        {
            TextShortcutProcessor.ProcessShortcut(this, args); //ctrl + c & ctrl + a processor only
            if (args.Key == KeyCode.Escape)
            {
                UnselectText();
            }
        }

        public string GetText()
        {
            return _textureStorage.GetWholeText();
        }

        public void SetText(String text)
        {
            Monitor.Enter(_textureStorage.textInputLock);
            try
            {
                _cursorPosition = _textureStorage.SetText(text);
                ChangeHeightAccordingToText();
            }
            finally
            {
                Monitor.Exit(_textureStorage.textInputLock);
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

        public override void InitElements()
        {
            AddItems(_selectedArea, _textureStorage);
            _textureStorage.InitLines(2); //_cursor.GetWidth());
            if (IsWrapText())
            {
                ReorganizeText();
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

        private void MakeSelectedArea(Point from, Point to)
        {
            if (from.X == to.X && from.Y == to.Y)
            {
                _selectedArea.SetRectangles(null);
                return;
            }

            List<Point> selectionRectangles;

            Point fromReal, toReal;
            List<Point> listPt = RealFromTo(from, to);
            fromReal = listPt[0];
            toReal = listPt[1];
            
            selectionRectangles = _textureStorage.SelectedArrays(fromReal, toReal);

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

                _textureStorage.GetSelectedText(sb, fromReal, toReal);

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

        private void UnselectText()
        {
            _isSelect = false;
            _selectFrom.X = -1;
            _selectFrom.Y = 0;
            _selectTo.X = -1;
            _selectTo.Y = 0;
            MakeSelectedArea(new Point(_cursorPosition.X, _cursorPosition.Y), new Point(_cursorPosition.X, _cursorPosition.Y));
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
                _selectTo = new Point(_cursorPosition.X, _cursorPosition.Y);
                _isSelect = true;
                MakeSelectedArea();
            }
            finally
            {
                Monitor.Exit(_textureStorage.textInputLock);
            }
        }

        public override void SetWidth(int width)
        {
            if (GetWidth() == width)
            {
                return;
            }
            Point tmpCursor = new Point(_cursorPosition.X, _cursorPosition.Y);
            Point fromTmp = new Point(_selectFrom.X, _selectFrom.Y);
            Point toTmp = new Point(_selectTo.X, _selectTo.Y);
            
            tmpCursor = _textureStorage.WrapCursorPosToReal(_cursorPosition);
            if (_isSelect)
            {
                fromTmp = _textureStorage.WrapCursorPosToReal(_selectFrom);
                toTmp = _textureStorage.WrapCursorPosToReal(_selectTo);
            }

            base.SetWidth(width);
            ReorganizeText();
            _textureStorage.UpdateBlockWidth(2); //_cursor.GetWidth());
            
            _cursorPosition = _textureStorage.RealCursorPosToWrap(tmpCursor);
            if (_isSelect)
            {
                fromTmp = _textureStorage.RealCursorPosToWrap(fromTmp);
                toTmp = _textureStorage.RealCursorPosToWrap(toTmp);
                _selectFrom = fromTmp;
                _selectTo = toTmp;
                MakeSelectedArea();
            }
            
            ChangeHeightAccordingToText();
        }
        public override void SetHeight(int height)
        {
            if (GetHeight() == height)
            {
                return;
            }
            base.SetHeight(height);
            _textureStorage.UpdateBlockHeight();
        }

        private void ChangeHeightAccordingToText()
        {
            int textHeight = GetTextHeight();
            SetHeight(textHeight);
        }

        //Wrap Text Stuff---------------------------------------------------------------------------------------------------

        public bool IsWrapText()
        {
            return true;
        }

        //Something changed (text is always wrapped)
        private void ReorganizeText() {

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

        //Decorations-------------------------------------------------------------------------------------------------------

        public void SetLineSpacer(int lineSpacer)
        {
            _textureStorage.SetLineSpacer(lineSpacer);
        }

        public int GetLineSpacer()
        {
            return _textureStorage.GetLineSpacer();
        }

        
        public void SetTextAlignment(ItemAlignment alignment)
        {
            //Ignore all changes for yet
        }

        public void SetTextMargin(Indents margin)
        {
            _textureStorage.SetTextMargin(margin);
        }

        public Indents GetTextMargin()
        {
            return _textureStorage.GetTextMargin();
        }

        public void SetFont(Font font)
        {
            _textureStorage.SetFont(font);
        }

        public Font GetFont()
        {
            return _textureStorage.GetFont();
        }

        public void SetForeground(Color color)
        {
            _textureStorage.SetForeground(color);
        }

        public Color GetForeground()
        {
            return _textureStorage.GetForeground();
        }

        
        //Style
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
        }

        //Shortcut methods disable------------------------------------------------------------------------------------------
        public void PasteText(String pasteStr) {

        }

        public String CutText() {
            return null;
        }

        public void Undo() {

        }

        public void Redo() {

        }
    }
}