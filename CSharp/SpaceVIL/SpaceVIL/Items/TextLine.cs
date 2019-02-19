using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    internal class TextLine : TextItem, ITextContainer
    {
        private static int count = 0;
        private TextPrinter textPrt = new TextPrinter();
        private Object textLock = new Object();
        private bool flagBB = false;

        //private int _minLineSpacer;
        //private int _minFontY;
        //private int _maxFontY;

        private List<int> _letEndPos;
        private int _lineYShift = 0;
        private int _lineXShift = 0;
        private int _parentAllowWidth = int.MaxValue;
        private int _parentAllowHeight = int.MaxValue;
        private int _bigWidth = 0;
        private float _screenScale = 1;

        private List<FontEngine.ModifyLetter> _letters = new List<FontEngine.ModifyLetter>();
        private List<FontEngine.ModifyLetter> _bigLetters = new List<FontEngine.ModifyLetter>();

        internal TextLine()
        {
            count++;
        }

        internal TextLine(string text, Font font)
            : base(text, font, "TextLine_" + count)
        {
            count++;
            //GetFontDims();
            UpdateData();
        }

        private void CreateText()
        {
            Monitor.Enter(textLock);
            try
            {
                int _lineWidth = 0; //private float[] _lineWidth;
                String text = GetItemText();
                Font font = GetFont();

                _letters = FontEngine.GetModifyLetters(text, font);
                _letEndPos = new List<int>();
                _lineWidth = 0;
                if (_letters.Count > 0)
                    _lineWidth = _letters[_letters.Count - 1].xShift + _letters[_letters.Count - 1].width +
                        _letters[_letters.Count - 1].xBeg; //xBeg не обязательно, т.к. везде 0, но вдруг

                int[] fontDims = GetFontDims();
                base.SetWidth(_lineWidth);
                base.SetHeight(fontDims[2]);

                foreach (FontEngine.ModifyLetter modL in _letters)
                {
                    _letEndPos.Add(modL.xBeg + modL.xShift + modL.width);
                }

                WindowLayout wLayout = GetHandler();
                if (wLayout == null || wLayout.GetDpiScale() == null)
                    _screenScale = 0;
                else
                {
                    _screenScale = wLayout.GetDpiScale()[0];
                    if (_screenScale != 1) {
                        MakeBigArr();
                    }
                }
            }
            finally
            {
                Monitor.Exit(textLock);
            }
        }

        private bool isBigExist = false;

        private void MakeBigArr()
        {
            if (GetFont() == null) {
                return;
            }
            Font fontBig = new Font(GetFont().FontFamily, (int)(GetFont().Size * _screenScale), GetFont().Style);

            _bigLetters = FontEngine.GetModifyLetters(GetItemText(), fontBig);
            
            _bigWidth = 0;
            if (_bigLetters.Count > 0)
            {
                _bigWidth = _bigLetters[_bigLetters.Count - 1].xShift + _bigLetters[_bigLetters.Count - 1].width
                        + _bigLetters[_bigLetters.Count - 1].xBeg; // xBeg не обязательно, т.к. везде 0, но вдруг
                base.SetWidth((int)((float)_bigWidth / _screenScale));
            }

            // _bigMinY = output[1];

            if (_screenScale != 0)
            {
                _letEndPos = new List<int>();
                foreach (FontEngine.ModifyLetter modL in _bigLetters)
                {
                    _letEndPos.Add((int)((float)(modL.xBeg + modL.xShift + modL.width) / _screenScale));
                }
            }

            isBigExist = true;
        }

        public TextPrinter GetLetTextures()
        {
            Monitor.Enter(textLock);
            try
            {
                WindowLayout wLayout = GetHandler();
                if (wLayout != null && wLayout.GetDpiScale() != null)
                {
                    float scl = wLayout.GetDpiScale()[0];
                    if (scl != _screenScale && !isBigExist)
                    { //Это при допущении, что скейл меняется только один раз!
                        _screenScale = scl;
                        MakeBigArr();
                    }
                }

                int[] fontDims = GetFontDims();
                int height = fontDims[2];
                if (GetHeight() != height)
                {
                    base.SetHeight(height);
                }

                if (_lineYShift - fontDims[1] + height < 0 || _lineYShift - fontDims[1] > _parentAllowHeight)
                        return null;
                if (_letters.Count() == 0)
                    return new TextPrinter(); //null;
                if (flagBB)
                {
                    int bb_h = GetHeight();
                    int bb_w = GetWidth();

                    if (_parentAllowWidth > 0)
                        bb_w = bb_w > _parentAllowWidth ? _parentAllowWidth : bb_w;

                    byte[] cacheBB = new byte[bb_h * bb_w * 4];

                    int xFirstBeg; // = _letters[0].xBeg + _letters[0].xShift;

                    if (_screenScale != 0 && _screenScale != 1)
                    {
                        Font fontBig = new Font(GetFont().FontFamily, (int)(GetFont().Size * _screenScale), GetFont().Style);
                        int[] output = FontEngine.GetSpacerDims(fontBig);
                        bb_h = output[2];
                        bb_w = _bigWidth > (int)(_parentAllowWidth * _screenScale) ? (int)(_parentAllowWidth * _screenScale) : _bigWidth;

                        int bigMinY = output[1];

                        cacheBB = MakeSomeBig(bb_h, bb_w, bigMinY, 0, _letters.Count - 1);
                    }
                    else
                    {
                        foreach (FontEngine.ModifyLetter modL in _letters)
                        {

                        int widthFrom = 0;
                        int widthTo = modL.width;

                        if (modL.xBeg + modL.xShift + modL.width + _lineXShift < 0)
                        { //До разрешенной области
                            continue;
                        }
                        if (modL.xBeg + modL.xShift + _lineXShift <= 0)
                        {
                            widthFrom = Math.Abs(modL.xBeg + modL.xShift + _lineXShift);
                        }

                        xFirstBeg = -_lineXShift; // modL.xBeg + modL.xShift + widthFrom; //modL.xBeg + modL.xShift;

                        if (modL.xBeg + modL.xShift - xFirstBeg > _parentAllowWidth)
                        { //После разрешенной области + _lineXShift
                            break;
                        }
                        if (modL.xBeg + modL.xShift + modL.width - xFirstBeg >= _parentAllowWidth)
                        { // + _lineXShift
                            widthTo = _parentAllowWidth - (modL.xBeg + modL.xShift + widthFrom - xFirstBeg); // + _lineXShift
                        }

                        byte[] bitmap = modL.getArr();
                        if (bitmap == null)
                        {
                            continue;
                        }

                        int offset = (modL.yBeg - fontDims[1]) * bb_w * 4 + (modL.xBeg + modL.xShift + widthFrom - xFirstBeg) * 4;

                        for (int j = 0; j < modL.height; j++)
                        {
                            for (int i = widthFrom; i < widthTo; i++)
                            {
                                int b1 = bitmap[3 + j * 4 + i * (modL.height * 4)];
                                int b2 = cacheBB[3 + offset + (i - widthFrom) * 4 + j * (bb_w * 4)];
                                if (b1 < b2)
                                    continue;

                                cacheBB[0 + offset + (i - widthFrom) * 4 + j * (bb_w * 4)] = bitmap[0 + j * 4 + i * (modL.height * 4)];
                                cacheBB[1 + offset + (i - widthFrom) * 4 + j * (bb_w * 4)] = bitmap[1 + j * 4 + i * (modL.height * 4)];
                                cacheBB[2 + offset + (i - widthFrom) * 4 + j * (bb_w * 4)] = bitmap[2 + j * 4 + i * (modL.height * 4)];
                                cacheBB[3 + offset + (i - widthFrom) * 4 + j * (bb_w * 4)] = bitmap[3 + j * 4 + i * (modL.height * 4)];
                            }
                        }

                        // TextPrinter tp = new TextPrinter(modL.getArr());
                        // tp.xshift = x;
                        // tp.yshift = y;
                        // tp.letWidth = modL.width;
                        // tp.letHeight = modL.height;
                        // tp.yWinShift = y - (modL.yBeg - _minFontY);
                        // letTexturesList.add(tp);
                        }
                    }
                    flagBB = false;
                    textPrt = new TextPrinter(cacheBB);
                    textPrt.WidthTexture = bb_w;
                    textPrt.HeightTexture = bb_h;

                    // if (letTexturesList.size() > 0)
                    // createAtlas(letTexturesList);
                }

                UpdateCoords();
                return textPrt;
            }
            finally
            {
                Monitor.Exit(textLock);
            }
        }

        private int[] FindFirstLast(List<FontEngine.ModifyLetter> letList, int winWidth)
        { //, float someShift) {
            int firstInd = 0, lastInd = 0;
            int someShift = (int)(_lineXShift * _screenScale);
            bool isFirstFound = false;

            for (int ii = 0; ii < letList.Count; ii++)
            {
                FontEngine.ModifyLetter modL = letList[ii];
                if (modL.xBeg + modL.xShift + modL.width + someShift < 0)
                { // До разрешенной области
                    continue;
                }
                
                if (!isFirstFound)
                {
                    firstInd = ii;
                    isFirstFound = true;
                }

                if (modL.xBeg + modL.xShift + modL.width + someShift >= winWidth)
                {
                    lastInd = ii;
                    break;
                }
            }

            if (lastInd == 0) lastInd = letList.Count - 1;

            FontEngine.ModifyLetter letFirst = letList[firstInd];
            FontEngine.ModifyLetter letLast = letList[lastInd];

            int visWidth = letLast.xShift + letLast.width + letLast.xBeg - letFirst.xBeg - letFirst.xShift;
            // int outShift = (int) ((someShift + (letFirst.xBeg + letFirst.xShift) * 1.0) / _screenScale);

            return new int[] { firstInd, lastInd, visWidth }; //, outShift};
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

                if (bigLet.xBeg + bigLet.xShift + bigLet.width + someShift < 0)
                { // До разрешенной области

                    continue;
                }
                if (bigLet.xBeg + bigLet.xShift + someShift <= 0)
                {
                    // continue;
                    widthFrom = Math.Abs(bigLet.xBeg + bigLet.xShift + someShift);
                }

                xFirstBeg = -someShift;

                if (bigLet.xBeg + bigLet.xShift - xFirstBeg > parWidth)
                { // После разрешенной области + _lineXShift
                    break;
                }
                if (bigLet.xBeg + bigLet.xShift + bigLet.width - xFirstBeg >= parWidth)
                {
                    // break;
                    widthTo = parWidth - (bigLet.xBeg + bigLet.xShift + widthFrom - xFirstBeg);
                }


                byte[] bitmap = bigLet.getArr();
                if (bitmap == null)
                {
                    continue;
                }

                int offset = (bigLet.yBeg - bigMinY) * 4 * wdt + (bigLet.xBeg + bigLet.xShift + widthFrom - xFirstBeg) * 4;
                // System.out.println(outCache.capacity() + " " + offset);
                for (int j = 0; j < bigLet.height; j++)
                {
                    for (int i = widthFrom; i < widthTo; i++)
                    {
                        int b1 = bitmap[3 + j * 4 + i * (bigLet.height * 4)];
                        int b2 = outCache[3 + offset + (i - widthFrom) * 4 + j * (wdt * 4)];
                        if (b1 < b2)
                            continue;

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
            //base.SetWidth(width);
            SetAllowWidth(width);
        }

        public override void SetHeight(int height)
        {
            //base.SetWidth(width);
            SetAllowHeight(height);
        }

        /*
        private void AddAllShifts()
        {
            if (GetParent() == null)
                return;

            if (_coordArray == null)
                return;

            List<float> outRealCoords = new List<float>();
            List<float> alphas = new List<float>();

            ItemAlignment alignments = GetTextAlignment();
            float alignShiftX = 1;
            float alignShiftY = 0;

            float height = Math.Abs(_maxFontY - _minFontY);
            
            if (_lineYShift - _minFontY + height < 0 || _lineYShift - _minFontY > _parentAllowHeight)
            {
                SetAlphas(alphas);
                SetRealCoords(outRealCoords);
                return;
            }

            //Horizontal
            if (alignments.HasFlag(ItemAlignment.Right) && (_lineWidth < _parentAllowWidth))
                alignShiftX = GetParent().GetWidth() - _lineWidth - GetParent().GetPadding().Right; //[i];

            else if (alignments.HasFlag(ItemAlignment.HCenter) && (_lineWidth < _parentAllowWidth))
                alignShiftX = (GetParent().GetWidth() - _lineWidth) / 2f; //[i]) / 2f;

            //Vertical
            if (alignments.HasFlag(ItemAlignment.Bottom))
                alignShiftY = GetParent().GetHeight() - height - GetParent().GetPadding().Bottom;

            else if (alignments.HasFlag(ItemAlignment.VCenter))
                alignShiftY = (GetParent().GetHeight() - height) / 2f;

            List<float> tmpList;
            float xCoord, yCoord;

            foreach (FontEngine.ModifyLetter modL in _letters) {
                tmpList = modL.GetPix();
                xCoord = alignShiftX + modL.xBeg + modL.xShift + _lineXShift;
                yCoord = alignShiftY + _lineYShift - _minFontY + modL.yBeg;

                if (xCoord + modL.width < 0) continue;
                if (xCoord > _parentAllowWidth) break;


                alphas.AddRange(modL.GetCol());
                //_letEndPos.Add(modL.xBeg + modL.xShift + modL.width);

                for (int j = 0; j < tmpList.Count / 3; j++)
                {
                    outRealCoords.Add(tmpList[j * 3] + xCoord);
                    outRealCoords.Add(tmpList[j * 3 + 1] + yCoord);
                    outRealCoords.Add(tmpList[j * 3 + 2]);
                }
            }
            
            SetAlphas(alphas);
            SetRealCoords(outRealCoords);
        }
        */

        //private int _lineSpacer;
        //void SetLineSpacer(int lineSpacer)
        //{
        //    if ((lineSpacer != this._lineSpacer) && (lineSpacer >= _minLineSpacer))
        //    {
        //        SetCoordsFlag(true);
        //        this._lineSpacer = lineSpacer;
        //    }
        //}

        //public int GetLineSpacer()
        //{
        //    return _lineSpacer;
        //}

        public override void UpdateData() //UpdateType updateType)
        {
            //switch (updateType)
            //{
            //    case UpdateType.Critical:
            Monitor.Enter(textLock);
            try
            {
                if (GetFont() == null)
                    return;
                //int[] output = FontEngine.GetSpacerDims(GetFont());
                //_minLineSpacer = output[0];
                //_minFontY = output[1];
                //_maxFontY = output[2];
                CreateText();
                flagBB = true;
            }
            finally
            {
                Monitor.Exit(textLock);
            }
        }

        private void UpdateCoords()
        {
            //AddAllShifts();
            if (_letters.Count() == 0)
                return;
            int[] fontDims = GetFontDims();
            int height = fontDims[2];
            
            ItemAlignment alignments = GetTextAlignment();
            float alignShiftX = 1;
            float alignShiftY = 0;
            int xFirstBeg = 0;

            int _lineWidth = GetWidth();
            // Horizontal
            if (alignments.HasFlag(ItemAlignment.Left) || (_lineWidth >= _parentAllowWidth))
            {
                alignShiftX = GetParent().GetPadding().Left + GetMargin().Left + cursorWidth;
            }
            else if (alignments.HasFlag(ItemAlignment.Right) && (_lineWidth < _parentAllowWidth))
                alignShiftX = GetParent().GetWidth() - _lineWidth - GetParent().GetPadding().Right - GetMargin().Right - cursorWidth;

            else if (alignments.HasFlag(ItemAlignment.HCenter) && (_lineWidth < _parentAllowWidth))
                // alignShiftX = ((GetParent().GetWidth() - GetParent().GetPadding().Left - GetParent().GetPadding().Right
                //         + GetMargin().Left - GetMargin().Right) - _lineWidth) / 2f;
                alignShiftX = (_parentAllowWidth - _lineWidth) / 2f + GetParent().GetPadding().Left + GetMargin().Left + cursorWidth; //(GetParent().GetWidth() - _lineWidth) / 2f + GetParent().GetPadding().Left;

            // Vertical
            if (alignments.HasFlag(ItemAlignment.Top))
            {
                // System.out.println(getMargin().top);
                alignShiftY = GetParent().GetPadding().Top + GetMargin().Top;
            }
            else if (alignments.HasFlag(ItemAlignment.Bottom))
                alignShiftY = GetParent().GetHeight() - height - GetParent().GetPadding().Bottom - GetMargin().Bottom;

            else if (alignments.HasFlag(ItemAlignment.VCenter))
                // alignShiftY = ((GetParent().GetHeight() - GetParent().GetPadding().Bottom - GetParent().GetPadding().Top)
                //         - height) / 2f - GetMargin().Bottom + GetMargin().Top;
                alignShiftY = (GetParent().GetHeight() - height) / 2f + GetParent().GetPadding().Top;

            xFirstBeg = _letters[0].xBeg + _letters[0].xShift;
            
            textPrt.XTextureShift = (int)alignShiftX + GetParent().GetX() + xFirstBeg; // + _lineXShift
            textPrt.YTextureShift = (int)alignShiftY + _lineYShift + GetParent().GetY();
            
        }

        internal string GetText()
        {
            return GetItemText();
        }

        public override float[] Shape()
        {
            //AddAllShifts();
            return base.Shape();
        }

        internal List<int> GetLetPosArray()
        {
            return _letEndPos;
        }

        internal int GetLetWidth(int count)
        {
            if (_letters == null) return 0;
            if ((count < 0) || (count >= _letters.Count)) return 0;

            return _letters[count].width;
        }

        internal void SetLineYShift(int sp)
        {
            _lineYShift = sp;
            //UpdateCoords(); //SetCoordsFlag(true);
            flagBB = true;
        }

        internal int GetLineYShift()
        {
            return _lineYShift;
        }

        internal void SetLineXShift(int sp)
        {
            //if (_lineXShift == sp) return;
            _lineXShift = sp;
            //UpdateCoords();
            flagBB = true;
        }

        internal int GetLineXShift()
        {
            return _lineXShift;
        }

        internal float GetLineTopCoord()
        {
            float lineTopCoord = 0;
            ItemAlignment alignments = GetTextAlignment();
            int[] fontDims = GetFontDims();
            float height = fontDims[2];
            if (alignments.HasFlag(ItemAlignment.Bottom))
                lineTopCoord = GetParent().GetHeight() - height;

            else if (alignments.HasFlag(ItemAlignment.VCenter))
                lineTopCoord = (GetParent().GetHeight() - height) / 2f;

            lineTopCoord += _lineYShift - fontDims[1];

            return lineTopCoord;
        }

        internal int[] GetFontDims()
        {
            int[] output = FontEngine.GetSpacerDims(GetFont());
            //_minLineSpacer = output[0];
            //_minFontY = output[1];
            //_maxFontY = output[2];
            return output;
        }

        public override void SetStyle(Style style)
        {
            SetAlignment(style.Alignment);
            SetTextAlignment(style.TextAlignment);
            SetMargin(style.Margin);
            SetSizePolicy(style.WidthPolicy, style.HeightPolicy);
        }

        internal void SetLineXShift()
        {
            SetLineXShift(_lineXShift);
        }

        internal void CheckXShift(int _cursorXMax)
        {
            if (GetLetPosArray() == null || GetLetPosArray().Count == 0)
                return;
            int s = GetLetPosArray().Last() - _cursorXMax;
            if (s <= 0) SetLineXShift(0);
            else if ((s > 0) && (s + _lineXShift < 0))
            {
                SetLineXShift(-s);
            }
        }

        internal void SetLineYShift()
        {
            SetLineYShift(_lineYShift);
        }

        internal void SetAllowWidth(int allowWidth)
        {
            if (_parentAllowWidth != allowWidth)
                flagBB = true;
            _parentAllowWidth = allowWidth;
        }

        internal void SetAllowHeight(int allowHeight)
        {
            if (_parentAllowHeight != allowHeight)
                flagBB = true;
            _parentAllowHeight = allowHeight;
        }

        private int cursorWidth = 0;
        internal void SetCursorWidth(int cwidth)
        {
            if (cursorWidth != cwidth)
                flagBB = true;
            cursorWidth = cwidth;
        }
    }

}