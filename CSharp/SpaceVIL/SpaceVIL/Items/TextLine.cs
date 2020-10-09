using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    internal class TextLine : TextItem, ITextContainer
    {
        private static int count = 0;
        private TextPrinter textPrt = new TextPrinter();
        private readonly Object textLock = new Object();
        private bool _isUpdateNeed = false;

        private List<int> _letEndPos;
        private int _lineYShift = 0;
        private int _lineXShift = 0;
        private int _parentAllowWidth = SpaceVILConstants.SizeMaxValue;
        private int _parentAllowHeight = SpaceVILConstants.SizeMaxValue;
        private int _bigWidth = 0;
        private float _screenScale = 1;

        private List<FontEngine.ModifyLetter> _letters = new List<FontEngine.ModifyLetter>();
        private List<FontEngine.ModifyLetter> _bigLetters = new List<FontEngine.ModifyLetter>();

        private bool _isRecountable = false;

        internal TextLine() : base()
        {
            count++;
            UpdateData();
        }

        internal TextLine(string text, Font font)
            : base(text, font, "TextLine_" + count)
        {
            count++;
            UpdateData();
        }

        private bool afterCreate = false;

        private void CreateText()
        {
            Monitor.Enter(textLock);
            try
            {
                afterCreate = true;
                int _lineWidth = 0;
                String text = GetItemText();
                Font font = GetFont();

                _letters = FontEngine.GetModifyLetters(text, font);
                _letEndPos = new List<int>();

                if (_letters.Count > 0)
                    _lineWidth = _letters[_letters.Count - 1].xShift + _letters[_letters.Count - 1].width +
                        _letters[_letters.Count - 1].xBeg; //xBeg не обязательно, т.к. везде 0, но вдруг

                FontEngine.FontDimensions fontDims = GetFontDims();
                base.SetWidth(_lineWidth);
                base.SetHeight(fontDims.height);

                foreach (FontEngine.ModifyLetter modL in _letters)
                {
                    _letEndPos.Add(modL.xBeg + modL.xShift + modL.width);
                }

                CoreWindow wLayout = (GetParent() == null) ? null : GetParent().GetHandler();
                if (wLayout == null) // || Common.DisplayService.GetDpiScale() == null)
                {
                    _screenScale = 1; //0;
                }
                else
                {
                    _screenScale = DisplayService.GetDisplayDpiScale().GetXScale();
                    if (_screenScale != 1)
                    {
                        MakeBigArr();
                    }
                }
            }
            finally
            {
                Monitor.Exit(textLock);
            }
        }

        // private bool isBigExist = false;

        private void MakeBigArr()
        {
            if (GetFont() == null)
            {
                return;
            }
            // Font fontBig = new Font(GetFont().FontFamily, (int)(GetFont().Size * _screenScale), GetFont().Style);
            // Console.WriteLine("font " + GetFont().Size + " " + cnt++);
            Font fontBig = FontService.ChangeFontSize((int)(GetFont().Size * _screenScale), GetFont());

            _bigLetters = FontEngine.GetModifyLetters(GetItemText(), fontBig);

            _bigWidth = 0;
            if (_bigLetters.Count > 0)
            {
                _bigWidth = _bigLetters[_bigLetters.Count - 1].xShift + _bigLetters[_bigLetters.Count - 1].width
                        + _bigLetters[_bigLetters.Count - 1].xBeg; // xBeg не обязательно, т.к. везде 0, но вдруг
                base.SetWidth((int)((float)_bigWidth / _screenScale));
            }

            // if (_screenScale != 0)
            {
                _letEndPos = new List<int>();
                foreach (FontEngine.ModifyLetter modL in _bigLetters)
                {
                    _letEndPos.Add((int)((float)(modL.xBeg + modL.xShift + modL.width) / _screenScale));
                }
            }

            // isBigExist = true;
        }

        public ITextImage GetTexture()
        {
            Monitor.Enter(textLock);
            try
            {
                FontEngine.FontDimensions fontDims = GetFontDims();
                int height = fontDims.height;
                if (GetHeight() != height)
                {
                    base.SetHeight(height);
                }

                Prototype parent = GetParent();
                if (parent == null) //Вроде не очень-то и нужно
                    return null;

                CoreWindow wLayout = parent.GetHandler();
                if (wLayout != null) // && wLayout.GetDpiScale() != null)
                {
                    float scl = DisplayService.GetDisplayDpiScale().GetXScale();
                    if (scl != _screenScale) // && !isBigExist)
                    { //Это при допущении, что скейл меняется только один раз!                    
                        if (scl != 1) //_screenScale != 0 || 
                        {
                            //Возможно может возникнуть проблема при переходе от большего к меньшему
                            _screenScale = scl;
                            MakeBigArr();
                        }
                    }
                }

                if (_isRecountable)
                {
                    if (_lineYShift - fontDims.minY + height < 0 || _lineYShift - fontDims.minY > _parentAllowHeight) //(_lineYShift - fontDims[1] + height < 0 || _lineYShift - fontDims[1] > _parentAllowHeight)
                    {
                        return null;
                    }
                }
                if (_letters.Count() == 0)
                {
                    return new TextPrinter(); //null;
                }
                if (_isUpdateNeed && (_isRecountable || afterCreate))
                {
                    afterCreate = false;
                    int bb_h = GetHeight();
                    int bb_w = GetWidth();

                    if (_parentAllowWidth > 0 && _isRecountable)
                    {
                        bb_w = bb_w > _parentAllowWidth ? _parentAllowWidth : bb_w;
                    }

                    byte[] cacheBB = new byte[bb_h * bb_w * 4];

                    int xFirstBeg = _letters[0].xBeg + _letters[0].xShift;

                    if (_screenScale != 1) //_screenScale != 0 && 
                    {
                        // Font fontBig = new Font(GetFont().FontFamily, (int)(GetFont().Size * _screenScale), GetFont().Style);
                        Font fontBig = FontService.ChangeFontSize((int)(GetFont().Size * _screenScale), GetFont());

                        FontEngine.FontDimensions bigFontDims = FontEngine.GetFontDims(fontBig);
                        bb_h = bigFontDims.height;
                        bb_w = _bigWidth;
                        if (_isRecountable)
                        {
                            bb_w = _bigWidth > (int)(_parentAllowWidth * _screenScale)
                                     ? (int)(_parentAllowWidth * _screenScale)
                                     : _bigWidth;
                        }

                        int bigMinY = bigFontDims.minY;
                        cacheBB = MakeSomeBig(bb_h, bb_w, bigMinY, 0, _letters.Count - 1);
                    }
                    else
                    {
                        foreach (FontEngine.ModifyLetter modL in _letters)
                        {
                            int widthFrom = 0;
                            int widthTo = modL.width;

                            if (_isRecountable)
                            {
                                if (modL.xBeg + modL.xShift + modL.width + _lineXShift < 0)
                                { //До разрешенной области
                                    continue;
                                }
                                if (modL.xBeg + modL.xShift + _lineXShift <= 0)
                                {
                                    widthFrom = Math.Abs(modL.xBeg + modL.xShift + _lineXShift);
                                }

                                xFirstBeg = -_lineXShift;

                                if (modL.xBeg + modL.xShift - xFirstBeg > _parentAllowWidth)
                                { //После разрешенной области + _lineXShift
                                    break;
                                }
                                if (modL.xBeg + modL.xShift + modL.width - xFirstBeg >= _parentAllowWidth) // + _lineXShift
                                {
                                    widthTo = _parentAllowWidth - (modL.xBeg + modL.xShift + widthFrom - xFirstBeg); // + _lineXShift
                                }
                            }
                            byte[] bitmap = modL.GetArr();
                            if (bitmap == null)
                            {
                                continue;
                            }

                            int offset = (modL.yBeg - fontDims.minY) * bb_w * 4
                                 + (modL.xBeg + modL.xShift + widthFrom - xFirstBeg) * 4;

                            for (int j = 0; j < modL.height; j++)
                            {
                                for (int i = widthFrom; i < widthTo; i++)
                                {
                                    int b1 = bitmap[3 + j * 4 + i * (modL.height * 4)];
                                    int b2 = cacheBB[3 + offset + (i - widthFrom) * 4 + j * (bb_w * 4)];
                                    if (b1 < b2)
                                    {
                                        continue;
                                    }

                                    cacheBB[0 + offset + (i - widthFrom) * 4 + j * (bb_w * 4)] = bitmap[0 + j * 4 + i * (modL.height * 4)];
                                    cacheBB[1 + offset + (i - widthFrom) * 4 + j * (bb_w * 4)] = bitmap[1 + j * 4 + i * (modL.height * 4)];
                                    cacheBB[2 + offset + (i - widthFrom) * 4 + j * (bb_w * 4)] = bitmap[2 + j * 4 + i * (modL.height * 4)];
                                    cacheBB[3 + offset + (i - widthFrom) * 4 + j * (bb_w * 4)] = bitmap[3 + j * 4 + i * (modL.height * 4)];
                                }
                            }
                        }
                    }

                    _isUpdateNeed = false;
                    textPrt = new TextPrinter(cacheBB);
                    textPrt.SetSize(bb_w, bb_h);
                    ItemsRefreshManager.SetRefreshText(this);
                }
                UpdateCoords(parent);
                return textPrt;
            }
            finally
            {
                Monitor.Exit(textLock);
            }
        }

        private byte[] MakeSomeBig(int hgt, int wdt, int bigMinY, int firstInd, int lastInd)
        {
            byte[] outCache = new byte[hgt * wdt * 4];
            int someShift = (int)(_lineXShift * _screenScale);
            int parWidth = (int)(_parentAllowWidth * _screenScale);

            int xFirstBeg = _bigLetters[firstInd].xBeg + _bigLetters[firstInd].xShift;

            for (int ii = firstInd; ii <= lastInd; ii++)
            {
                FontEngine.ModifyLetter bigLet = _bigLetters[ii];

                //ignore at first
                int widthFrom = 0;
                int widthTo = bigLet.width;

                if (_isRecountable)
                {
                    if (bigLet.xBeg + bigLet.xShift + bigLet.width + someShift < 0)
                    { // До разрешенной области
                        continue;
                    }
                    if (bigLet.xBeg + bigLet.xShift + someShift <= 0)
                    {
                        widthFrom = Math.Abs(bigLet.xBeg + bigLet.xShift + someShift);
                    }

                    xFirstBeg = -someShift;

                    if (bigLet.xBeg + bigLet.xShift - xFirstBeg > parWidth)
                    { // После разрешенной области + _lineXShift
                        break;
                    }
                    if (bigLet.xBeg + bigLet.xShift + bigLet.width - xFirstBeg >= parWidth)
                    {
                        widthTo = parWidth - (bigLet.xBeg + bigLet.xShift + widthFrom - xFirstBeg);
                    }
                }

                byte[] bitmap = bigLet.GetArr();
                if (bitmap == null)
                {
                    continue;
                }

                int offset = (bigLet.yBeg - bigMinY) * 4 * wdt + (bigLet.xBeg + bigLet.xShift + widthFrom - xFirstBeg) * 4;

                for (int j = 0; j < bigLet.height; j++)
                {
                    for (int i = widthFrom; i < widthTo; i++)
                    {
                        int b1 = bitmap[3 + j * 4 + i * (bigLet.height * 4)];
                        int b2 = outCache[3 + offset + (i - widthFrom) * 4 + j * (wdt * 4)];
                        if (b1 < b2)
                        {
                            continue;
                        }

                        outCache[0 + offset + (i - widthFrom) * 4 + j * (wdt * 4)] =
                                bitmap[0 + j * 4 + i * (bigLet.height * 4)];
                        outCache[1 + offset + (i - widthFrom) * 4 + j * (wdt * 4)] =
                                bitmap[1 + j * 4 + i * (bigLet.height * 4)];
                        outCache[2 + offset + (i - widthFrom) * 4 + j * (wdt * 4)] =
                                bitmap[2 + j * 4 + i * (bigLet.height * 4)];
                        outCache[3 + offset + (i - widthFrom) * 4 + j * (wdt * 4)] =
                                bitmap[3 + j * 4 + i * (bigLet.height * 4)];
                    }
                }
            }

            return outCache;
        }

        public override void SetWidth(int width)
        {
            SetAllowWidth(width);
        }

        public override void SetHeight(int height)
        {
            SetAllowHeight(height);
        }

        public override void UpdateData()
        {
            Monitor.Enter(textLock);
            try
            {
                if (GetFont() == null)
                {
                    return;
                }
                CreateText();
                _isUpdateNeed = true;
            }
            finally
            {
                Monitor.Exit(textLock);
            }
        }

        private void UpdateCoords(Prototype parent)
        {
            //AddAllShifts();
            if (_letters.Count() == 0)
            {
                return;
            }
            FontEngine.FontDimensions fontDims = GetFontDims();
            int height = fontDims.height;

            ItemAlignment alignments = GetTextAlignment();
            float alignShiftX = 1;
            float alignShiftY = 0;
            int xFirstBeg = 0;

            int _lineWidth = GetWidth();
            // Horizontal
            if (alignments.HasFlag(ItemAlignment.Left) || (_lineWidth >= _parentAllowWidth))
            {
                alignShiftX = parent.GetPadding().Left + GetMargin().Left + cursorWidth;
            }
            else if (alignments.HasFlag(ItemAlignment.Right) && (_lineWidth < _parentAllowWidth))
            {
                alignShiftX = parent.GetWidth() - _lineWidth - parent.GetPadding().Right - GetMargin().Right - cursorWidth;
            }
            else if (alignments.HasFlag(ItemAlignment.HCenter) && (_lineWidth < _parentAllowWidth))
            {
                // alignShiftX = ((parent.GetWidth() - parent.GetPadding().Left - parent.GetPadding().Right
                //         + GetMargin().Left - GetMargin().Right) - _lineWidth) / 2f;
                alignShiftX = (_parentAllowWidth - _lineWidth) / 2f + parent.GetPadding().Left + GetMargin().Left + cursorWidth; //(parent.GetWidth() - _lineWidth) / 2f + parent.GetPadding().Left;
            }
            // Vertical
            if (alignments.HasFlag(ItemAlignment.Top))
            {
                alignShiftY = parent.GetPadding().Top + GetMargin().Top;
            }
            else if (alignments.HasFlag(ItemAlignment.Bottom))
            {
                alignShiftY = parent.GetHeight() - height - parent.GetPadding().Bottom - GetMargin().Bottom;
            }
            else if (alignments.HasFlag(ItemAlignment.VCenter))
            {
                // alignShiftY = ((parent.GetHeight() - parent.GetPadding().Bottom - parent.GetPadding().Top)
                //         - height) / 2f - GetMargin().Bottom + GetMargin().Top;
                alignShiftY = (parent.GetHeight() - height) / 2f + parent.GetPadding().Top;
            }

            xFirstBeg = _letters[0].xBeg + _letters[0].xShift;
            textPrt.SetPosition((int)alignShiftX + parent.GetX() + xFirstBeg,
                            (int)alignShiftY + _lineYShift + parent.GetY());
            
            if (!_isRecountable)
            {
                textPrt.SetXOffset(textPrt.GetXOffset() + _lineXShift);
            }
        }

        internal string GetText()
        {
            return GetItemText();
        }

        internal List<int> GetLetPosArray()
        {
            if (_isUpdateNeed)
            {
                UpdateData();
            }
            return _letEndPos;
        }

        internal int GetLetWidth(int count)
        {
            if (_letters == null)
            {
                return 0;
            }
            if ((count < 0) || (count >= _letters.Count))
            {
                return 0;
            }

            return _letters[count].width;
        }

        internal void SetLineYShift(int sp)
        {
            _lineYShift = sp;
            _isUpdateNeed = true;
        }

        internal int GetLineYShift()
        {
            return _lineYShift;
        }

        internal void SetLineXShift(int sp)
        {
            //if (_lineXShift == sp) return;
            _lineXShift = sp;
            _isUpdateNeed = true;
        }

        internal int GetLineXShift()
        {
            return _lineXShift;
        }

        internal FontEngine.FontDimensions GetFontDims()
        {
            return FontEngine.GetFontDims(GetFont());
        }

        public override void SetStyle(Style style)
        {
            SetAlignment(style.Alignment);
            SetTextAlignment(style.TextAlignment);
            SetMargin(style.Margin);
            SetSizePolicy(style.WidthPolicy, style.HeightPolicy);
        }

        internal void CheckXShift(int _cursorXMax)
        {
            if (GetLetPosArray() == null || GetLetPosArray().Count == 0)
                return;
            int s = GetLetPosArray().Last() - _cursorXMax;
            if (s <= 0) SetLineXShift(0);
            else if (s + _lineXShift < 0) //(s > 0) && 
            {
                SetLineXShift(-s);
            }
        }

        internal void SetAllowWidth(int allowWidth)
        {
            if (_parentAllowWidth != allowWidth)
            {
                _isUpdateNeed = true;
            }
            _parentAllowWidth = allowWidth;
        }

        internal void SetAllowHeight(int allowHeight)
        {
            if (_parentAllowHeight != allowHeight)
            {
                _isUpdateNeed = true;
            }
            _parentAllowHeight = allowHeight;
        }

        private int cursorWidth = 0;

        internal void SetCursorWidth(int cwidth)
        {
            if (cursorWidth != cwidth)
            {
                _isUpdateNeed = true;
            }
            cursorWidth = cwidth;
        }

        internal void SetRecountable(bool isRecountable)
        {
            _isRecountable = isRecountable;
        }
    }
}