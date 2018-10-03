using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    class TextLine : TextItem, ITextContainer
    {
        private static int count = 0;

        private int _minLineSpacer;
        private int _minFontY;
        private int _maxFontY;
        //private List<float> _coordArray; //private List<List<float>> _coordArray;
        private int _lineWidth; //private float[] _lineWidth;
        private List<int> _letEndPos;
        private int _lineYShift = 0;
        private int _lineXShift = 0;
        private int _parentAllowWidth = int.MaxValue;
        private int _parentAllowHeight = int.MaxValue;

        private List<FontEngine.ModifyLetter> _letters;

        public TextLine()
        {
            count++;
        }

        public TextLine(string text, Font font)
            : base(text, font, "TextLine_" + count)
        {
            count++;
            GetFontDims();
            /*
            int[] output = FontEngine.GetSpacerDims(font);
            _minLineSpacer = output[0];
            _minFontY = output[1];
            _maxFontY = output[2];
            */
            //_lineSpacer = _minLineSpacer;
            UpdateData();
        }

        public void CreateText()
        {
            String text = GetItemText();
            Font font = GetFont();
            //String[] line = new String[1] { text };//.Split('\n');
            //PixMapData obj;
            //_coordArray = new List<float>(); //_coordArray = new List<List<float>>();
            //_lineWidth = new float[line.Length];
            //List<float> alphas = new List<float>();

            //int inc = 0;
            //foreach (String textLine in line)
            //{

            /*
            obj = FontEngine.GetPixMap(text, font); //obj = FontEngine.GetPixMap(textLine, font);

            _coordArray.AddRange(obj.GetPixels()); //_coordArray.Add(obj.GetPixels());
            alphas.AddRange(obj.GetColors());
            _lineWidth = obj.GetAlpha();

            //inc++;
            _letEndPos = obj.GetEndPositions();
            */

            //}
            //LogService.Log().LogText("Text " + text + ", creation begin");
            _letters = FontEngine.GetPixMap(text, font);
            _letEndPos = new List<int>();
            //LogService.Log().LogText("Text " + text + ", creation end");
            //LogService.Log().LogOne(_letters.Count, "letters count");

            if (_letters.Count > 0)
                _lineWidth = _letters[_letters.Count - 1].xShift + _letters[_letters.Count - 1].width +
                    _letters[_letters.Count - 1].xBeg; //xBeg не обязательно, т.к. везде 0, но вдруг

            base.SetWidth(_lineWidth);
            base.SetHeight(Math.Abs(_maxFontY - _minFontY));

            foreach (FontEngine.ModifyLetter modL in _letters)
            {
                //alphas.AddRange(modL.GetCol());
                _letEndPos.Add(modL.xBeg + modL.xShift + modL.width);
            }

            //SetAlphas(alphas);
            

            AddAllShifts();

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
        
        private void AddAllShifts()
        {
            if (GetParent() == null)
                return;

            if (_letters == null)
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

            if (GetFont() == null)
                return;

            int[] output = FontEngine.GetSpacerDims(GetFont());
            _minLineSpacer = output[0];
            _minFontY = output[1];
            _maxFontY = output[2];
            //_lineSpacer = _minLineSpacer;
            CreateText();
            //        break;
            //    case UpdateType.CoordsOnly:
            //        AddAllShifts();
            //        break;
            //}
        }

        protected override void UpdateCoords()
        {
            AddAllShifts();
        }

        public TextItem GetText()
        {
            return this;
        }

        public override float[] Shape()
        {
            AddAllShifts();
            return base.Shape();
        }

        internal List<int> GetLetPosArray()
        {
            return _letEndPos;
        }

        internal int GetLetWidth(int count) {
            if (_letters == null) return 0;
            if ((count < 0) || (count >= _letters.Count)) return 0;
            
            return _letters[count].width;
        }

        internal void SetLineYShift(int sp)
        {
            //if (_lineYShift == sp) return;
            _lineYShift = sp;
            UpdateCoords();
        }

        internal int GetLineYShift()
        {
            return _lineYShift;
        }

        internal void SetLineXShift(int sp)
        {
            //if (_lineXShift == sp) return;
            _lineXShift = sp;
            UpdateCoords();
        }

        internal int GetLineXShift()
        {
            return _lineXShift;
        }

        internal float GetLineTopCoord()
        {
            float lineTopCoord = 0;
            ItemAlignment alignments = GetTextAlignment();
            float height = Math.Abs(_maxFontY - _minFontY);
            if (alignments.HasFlag(ItemAlignment.Bottom))
                lineTopCoord = GetParent().GetHeight() - height;

            else if (alignments.HasFlag(ItemAlignment.VCenter))
                lineTopCoord = (GetParent().GetHeight() - height) / 2f;

            lineTopCoord += _lineYShift - _minFontY;

            return lineTopCoord;
        }

        internal int[] GetFontDims()
        {
            int[] output = FontEngine.GetSpacerDims(GetFont());
            _minLineSpacer = output[0];
            _minFontY = output[1];
            _maxFontY = output[2];
            return output;
        }

        public override void SetStyle(Style style)
        {
            SetAlignment(style.Alignment);
            SetTextAlignment(style.TextAlignment);
            SetMargin(style.Margin);
            SetSizePolicy(style.WidthPolicy, style.HeightPolicy);
        }
        /*
        internal void UpdXLineShift(int xls)
        {
            _lineXShift = xls;
            SetLineXShift(_lineXShift);
        }
        */
        internal void SetLineXShift() {
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

        internal void SetLineYShift() {
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
