using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    class TextBlock : TextItem, ITextContainer
    {
        private static int count = 0;

        private int _minLineSpacer;
        private int _minFontY;
        private int _maxFontY;
        private List<List<float>> _coordArray;
        private float[] _lineWidth;
        private List<int> _letEndPos;

        public TextBlock()
        {
            count++;
        }

        public TextBlock(string text, Font font)
            : base(text, font, "TextLine_" + count)
        {
            count++;
            int[] output = FontEngine.GetSpacerDims(font);
            _minLineSpacer = output[0];
            _minFontY = output[1];
            _maxFontY = output[2];
            _lineSpacer = _minLineSpacer;
        }

        public void CreateText()
        {
            String text = GetItemText();
            Font font = GetFont();
            String[] line = text.Split('\n');
            PixMapData obj;
            _coordArray = new List<List<float>>();
            _lineWidth = new float[line.Length];
            List<float> alphas = new List<float>();

            int inc = 0;
            foreach (String textLine in line)
            {
                obj = FontEngine.GetPixMap(textLine, font);
                _coordArray.Add(obj.GetPixels());
                alphas.AddRange(obj.GetColors());
                _lineWidth[inc] = obj.GetAlpha();
                inc++;
                _letEndPos = obj.GetEndPositions();
            }

            SetAlphas(alphas);

            AddAllShifts();
        }

        private void AddAllShifts()
        {
            if (_coordArray == null)
                return;

            List<float> outRealCoords = new List<float>();

            ItemAlignment alignments = GetTextAlignment();
            float alignShiftX = 1;
            float alignShiftY = 0;

            float height = Math.Abs(_maxFontY - _minFontY);
            float bigSpacer = height + _lineSpacer;
            float addSpace = -_minFontY;

            for (int i = 0; i < _coordArray.Count; i++)
            {
                //Horizontal
                if (alignments.HasFlag(ItemAlignment.Right))
                    alignShiftX = GetParent().GetWidth() - _lineWidth[i];

                else if (alignments.HasFlag(ItemAlignment.HCenter))
                    alignShiftX = (GetParent().GetWidth() - _lineWidth[i]) / 2f;

                //Vertical
                if (alignments.HasFlag(ItemAlignment.Bottom))
                    alignShiftY = GetParent().GetHeight() - height;

                else if (alignments.HasFlag(ItemAlignment.VCenter))
                    alignShiftY = (GetParent().GetHeight() - height) / 2f;

                for (int j = 0; j < _coordArray[i].Count / 3; j++)
                {
                    outRealCoords.Add(_coordArray[i][j * 3] + alignShiftX);
                    outRealCoords.Add(_coordArray[i][j * 3 + 1] + addSpace + alignShiftY);
                    outRealCoords.Add(_coordArray[i][j * 3 + 2]);
                }
                addSpace += bigSpacer;
            }

            SetRealCoords(outRealCoords);
        }


        private int _lineSpacer;
        void SetLineSpacer(int lineSpacer)
        {
            if ((lineSpacer != this._lineSpacer) && (lineSpacer >= _minLineSpacer))
            {
                SetCoordsFlag(true);
                this._lineSpacer = lineSpacer;
            }
        }

        public int GetLineSpacer()
        {
            return _lineSpacer;
        }

        public override void UpdateData(UpdateType updateType)
        {
            switch (updateType)
            {
                case UpdateType.Critical:
                    int[] output = FontEngine.GetSpacerDims(GetFont());
                    _minLineSpacer = output[0];
                    _minFontY = output[1];
                    _maxFontY = output[2];
                    _lineSpacer = _minLineSpacer;
                    CreateText();
                    break;
                case UpdateType.CoordsOnly:
                    AddAllShifts();
                    break;
            }
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
    }
}
