using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace SpaceVIL
{
    class TextLine : TextItem, ITextContainer
    {
        private static int count = 0;
        private TextPrinter textPrt = new TextPrinter();
        private Object textLock = new Object();
        private bool flagBB = false;

        //private int _minLineSpacer;
        //private int _minFontY;
        //private int _maxFontY;

        private int _lineWidth = 0; //private float[] _lineWidth;
        private List<int> _letEndPos;
        private int _lineYShift = 0;
        private int _lineXShift = 0;
        private int _parentAllowWidth = int.MaxValue;
        private int _parentAllowHeight = int.MaxValue;

        private List<FontEngine.ModifyLetter> _letters = new List<FontEngine.ModifyLetter>();

        public TextLine()
        {
            count++;
        }

        public TextLine(string text, Font font)
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
                String text = GetItemText();
                Font font = GetFont();

                _letters = FontEngine.GetModifyLetters(text, font);
                _letEndPos = new List<int>();

                if (_letters.Count > 0)
                    _lineWidth = _letters[_letters.Count - 1].xShift + _letters[_letters.Count - 1].width +
                        _letters[_letters.Count - 1].xBeg; //xBeg не обязательно, т.к. везде 0, но вдруг

                int[] fontDims = GetFontDims();
                base.SetWidth(_lineWidth);
                base.SetHeight(fontDims[2]); //(Math.Abs(_maxFontY - _minFontY));

                foreach (FontEngine.ModifyLetter modL in _letters)
                {
                    _letEndPos.Add(modL.xBeg + modL.xShift + modL.width);
                }

                //AddAllShifts();
            }
            finally
            {
                Monitor.Exit(textLock);
            }
        }

        public TextPrinter GetLetTextures()
        {
            Monitor.Enter(textLock);
            try
            {
                if (flagBB)
                {
                    int[] fontDims = GetFontDims();
                    int height = fontDims[2];
                    if (GetHeight() != height)
                        base.SetHeight(height);

                    if (_letters.Count() == 0)
                        return null;
                    if (_lineYShift - fontDims[1] + height < 0 || _lineYShift - fontDims[1] > _parentAllowHeight)
                        return null;

                    //textPrt.WidthTexture = ;
                    //textPrt.HeightTexture = fontDims[3];
                    int bb_h = GetHeight(); //fontDims[3];
                    int bb_w = GetWidth(); //_letters[_letters.Count - 1].xShift + _letters[_letters.Count - 1].width +
                                           //_letters[_letters.Count - 1].xBeg;

                    byte[] cacheBB = new byte[bb_h * bb_w * 4];

                    int xFirstBeg = _letters[0].xBeg + _letters[0].xShift;

                    foreach (FontEngine.ModifyLetter modL in _letters)
                    {

                        byte[] bitmap = modL.getArr();
                        if (bitmap == null)
                        {
                            continue;
                        }

                        int offset = (modL.yBeg - fontDims[1]) * bb_w * 4 + (modL.xBeg + modL.xShift - xFirstBeg) * 4;

                        for (int j = 0; j < modL.height; j++)
                        {
                            for (int i = 0; i < modL.width; i++)
                            {
                                if (bitmap[3 + j * 4 + i * (modL.height * 4)] < cacheBB[3 + offset + i * 4 + j * (bb_w * 4)])
                                    continue;
                                cacheBB[0 + offset + i * 4 + j * (bb_w * 4)] = bitmap[0 + j * 4 + i * (modL.height * 4)];
                                cacheBB[1 + offset + i * 4 + j * (bb_w * 4)] = bitmap[1 + j * 4 + i * (modL.height * 4)];
                                cacheBB[2 + offset + i * 4 + j * (bb_w * 4)] = bitmap[2 + j * 4 + i * (modL.height * 4)];
                                cacheBB[3 + offset + i * 4 + j * (bb_w * 4)] = bitmap[3 + j * 4 + i * (modL.height * 4)];
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

        protected void UpdateCoords()
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

            // Horizontal
            if (alignments.HasFlag(ItemAlignment.Left))
            {
                alignShiftX = GetParent().GetPadding().Left + GetMargin().Left;
            }
            else if (alignments.HasFlag(ItemAlignment.Right) && (_lineWidth < _parentAllowWidth))
                alignShiftX = GetParent().GetWidth() - _lineWidth - GetParent().GetPadding().Right - GetMargin().Right;

            else if (alignments.HasFlag(ItemAlignment.HCenter) && (_lineWidth < _parentAllowWidth))
                alignShiftX = ((GetParent().GetWidth() - GetParent().GetPadding().Left - GetParent().GetPadding().Right
                        + GetMargin().Left - GetMargin().Right) - _lineWidth) / 2f;

            // Vertical
            if (alignments.HasFlag(ItemAlignment.Top))
            {
                // System.out.println(getMargin().top);
                alignShiftY = GetParent().GetPadding().Top + GetMargin().Top;
            }
            else if (alignments.HasFlag(ItemAlignment.Bottom))
                alignShiftY = GetParent().GetHeight() - height - GetParent().GetPadding().Bottom - GetMargin().Bottom;

            else if (alignments.HasFlag(ItemAlignment.VCenter))
                alignShiftY = ((GetParent().GetHeight() - GetParent().GetPadding().Bottom - GetParent().GetPadding().Top)
                        - height) / 2f - GetMargin().Bottom + GetMargin().Top;

            xFirstBeg = _letters[0].xBeg + _letters[0].xShift;
            
            textPrt.XTextureShift = (int)alignShiftX + _lineXShift + GetParent().GetX() + xFirstBeg;
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
            _parentAllowWidth = allowWidth;
        }

        internal void SetAllowHeight(int allowHeight)
        {
            _parentAllowHeight = allowHeight;
        }
    }

}