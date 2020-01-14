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
        private SpaceVIL.Core.Point _cursorPosition = new SpaceVIL.Core.Point(0, 0);
        private CustomSelector _selectedArea;

        private TextureStorage _textureStorage;

        private SpaceVIL.Core.Point _selectFrom = new SpaceVIL.Core.Point(-1, 0);
        private SpaceVIL.Core.Point _selectTo = new SpaceVIL.Core.Point(-1, 0);
        private bool _isSelect = false;

        public TextView()
        {
            SetItemName("TextBlock_" + count);
            count++;

            _textureStorage = new TextureStorage();

            _selectedArea = new CustomSelector();
            _selectedArea.SetBackground(150, 150, 150);

            EventMousePress += OnMousePressed;
            EventMouseClick += OnMouseClick;
            // EventMouseDoubleClick += OnDoubleClick;
            EventMouseDrag += OnDragging;
            EventKeyPress += OnKeyPress;
            EventKeyRelease += OnKeyRelease;

            // style
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.TextView)));
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

                            if (_isDoubleClick) // && _startTime.ElapsedMilliseconds < 500) //Select line on triple click
                            {
                                _isSelect = true;
                                _selectFrom = new SpaceVIL.Core.Point(0, _cursorPosition.Y);
                                _selectTo = new SpaceVIL.Core.Point(GetLettersCountInLine(_cursorPosition.Y), _cursorPosition.Y);
                                _cursorPosition = new SpaceVIL.Core.Point(_selectTo.X, _selectTo.Y);
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
                                    MakeSelectedArea();
                                }

                                // _startTime.Restart();
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
        //             }
        //             int[] wordBounds = _textureStorage.FindWordBounds(_cursorPosition);

        //             if (wordBounds[0] != wordBounds[1])
        //             {
        //                 _isSelect = true;
        //                 _selectFrom = new SpaceVIL.Core.Point(wordBounds[0], _cursorPosition.Y);
        //                 _selectTo = new SpaceVIL.Core.Point(wordBounds[1], _cursorPosition.Y);
        //                 _cursorPosition = new SpaceVIL.Core.Point(_selectTo.X, _selectTo.Y);
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

        private void ReplaceCursorAccordingCoord(SpaceVIL.Core.Point realPos)
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
            MakeSelectedArea(new SpaceVIL.Core.Point(_cursorPosition.X, _cursorPosition.Y), new SpaceVIL.Core.Point(_cursorPosition.X, _cursorPosition.Y));
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

            UpdateBlockWidth(width);
        }

        private void UpdateBlockWidth(int width)
        {
            SpaceVIL.Core.Point tmpCursor; // = new SpaceVIL.Core.Point(_cursorPosition.X, _cursorPosition.Y);
            SpaceVIL.Core.Point fromTmp = new SpaceVIL.Core.Point(_selectFrom); //.X, _selectFrom.Y);
            SpaceVIL.Core.Point toTmp = new SpaceVIL.Core.Point(_selectTo); //.X, _selectTo.Y);

            tmpCursor = _textureStorage.WrapCursorPosToReal(_cursorPosition);
            if (_isSelect)
            {
                fromTmp = _textureStorage.WrapCursorPosToReal(_selectFrom);
                toTmp = _textureStorage.WrapCursorPosToReal(_selectTo);
            }

            base.SetWidth(width);
            _textureStorage.UpdateBlockWidth(2); //_cursor.GetWidth());
            ReorganizeText();

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
            UpdateBlockHeight(height);
        }

        private void UpdateBlockHeight(int height)
        {
            base.SetHeight(height);
            _textureStorage.UpdateBlockHeight();
        }

        private void ChangeHeightAccordingToText()
        {
            if (GetHeightPolicy() == SizePolicy.Expand)
                return;
            int textHeight = GetTextHeight();
            SetHeight(textHeight);
        }

        //Wrap Text Stuff---------------------------------------------------------------------------------------------------

        public bool IsWrapText()
        {
            return true;
        }

        //Something changed (text is always wrapped)
        private void ReorganizeText()
        {

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
                return;

            base.SetStyle(style);
            SetForeground(style.Foreground);
            SetFont(style.Font);
            _textureStorage.SetLineContainerAlignment(style.TextAlignment);

            Style innerStyle = style.GetInnerStyle("selection");
            if (innerStyle != null)
            {
                _selectedArea.SetStyle(innerStyle);
            }
        }

        //Shortcut methods disable------------------------------------------------------------------------------------------
        public void PasteText(String pasteStr)
        {

        }

        public String CutText()
        {
            return "";
        }

        public void Undo()
        {

        }

        public void Redo()
        {

        }
    }
}