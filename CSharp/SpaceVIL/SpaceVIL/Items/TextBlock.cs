using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    class TextBlock : VisualItem, ITextEditable
    {
        private static int count = 0;
        private string _wholeText = "";
        
        private List<TextEdit> _linesList;

        private int _minLineSpacer;
        private int _minFontY;
        private int _maxFontY;
        private int _lineHeight;

        private Font _elementFont;

        public TextBlock()
        {
            count++;
            SetItemName("TextBlock_" + count);
            
            _linesList = new List<TextEdit>();
            TextEdit te = new TextEdit();
            _linesList.Add(te);
            
        }
        /*
        public TextBlock(string text, Font font)
        {
            count++;
            SetItemName("TextBlock_" + count);
            _elementFont = font;
            int[] output = FontEngine.GetSpacerDims(font);
            _minLineSpacer = output[0];
            _minFontY = output[1];
            _maxFontY = output[2];
            _lineHeight = Math.Abs(_maxFontY - _minFontY);
            _lineSpacer = _minLineSpacer;
            
            SplitAndMakeLines(text);
        }
        */
        private int _lineSpacer;
        void SetLineSpacer(int lineSpacer)
        {
            if ((lineSpacer != _lineSpacer) && (lineSpacer >= _minLineSpacer))
            {
                _lineSpacer = lineSpacer;
                if (_linesList == null) return;
                foreach (TextEdit te in _linesList)
                {
                    te.SetLineYShift(_lineHeight + _lineSpacer);
                }
                
            }
        }

        public int GetLineSpacer()
        {
            return _lineSpacer;
        }

        internal string GetWholeText() {
            StringBuilder sb = new StringBuilder();
            if (_linesList == null) return "";
            if (_linesList.Count == 1) {
                sb.Append(_linesList[0].GetText());
            }
            else
            { 
                foreach (TextEdit te in _linesList)
                {
                    sb.Append(te.GetText());
                    sb.Append("\n");
                }
                sb.Remove(sb.Length - 3, 2);
            }
            _wholeText = sb.ToString();
            return _wholeText;
        }

        public void SetTextAlignment(ItemAlignment alignment)
        {
            if (_linesList == null) return;
            foreach (TextEdit te in _linesList)
                te.SetTextAlignment(alignment);
        }
        public void SetFont(Font font)
        {
            if (!font.Equals(_elementFont))
            { 
                _elementFont = font;

                int[] output = FontEngine.GetSpacerDims(font);
                _minLineSpacer = output[0];
                _minFontY = output[1];
                _maxFontY = output[2];
                _lineHeight = Math.Abs(_maxFontY - _minFontY);
                if (_lineSpacer < _minLineSpacer)
                    _lineSpacer = _minLineSpacer;

                if (_linesList == null) return;
                foreach (TextEdit te in _linesList)
                    te.SetFont(font);
            }
        }
        public Font GetFont()
        {
            return _elementFont;
        }
        public void SetText(String text)
        {
            if (!text.Equals(GetWholeText()))
            {
                SplitAndMakeLines(text);
            }
        }

        public int GetTextWidth()
        {
            int w = 0, w0;
            if (_linesList == null) return w;
            foreach (TextEdit te in _linesList)
            {
                w0 = te.GetWidth();
                w = (w < w0) ? w0 : w;
            }
            return w;
        }

        public int GetTextHeight()
        {
            return _lineHeight*_linesList.Count + _lineSpacer*(_linesList.Count - 1);
        }

        private void SplitAndMakeLines(String text) {
            _linesList = new List<TextEdit>();
            RemoveAllLines();
            _wholeText = text;
            //Console.WriteLine(text + " " + _elementFont.Name);
            string[] line = text.Split('\n');
            int inc = 0;
            //if (_linesList == null) return;
            foreach (String textPart in line)
            {
                TextEdit te = new TextEdit();
                //te.SetHeight(30);
                //te.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
                te.SetFont(_elementFont);
                AddItem(te);
                te.SetText(textPart);
                te.SetLineYShift((_lineHeight + _lineSpacer) * inc);
                _linesList.Add(te);
                te.IsFocused = false;
                inc++;
            }
            _linesList[_linesList.Count - 1].IsFocused = true;
            _linesList[_linesList.Count - 1].SetCursorPosition(_linesList[_linesList.Count - 1].GetTextWidth());
            //AddAllLines();
        }
        /*
        internal void UpdateData(UpdateType updateType) {
            //foreach (TextEdit te in _linesList)
                //te.Up
        }
        */
        public void SetForeground(Color color)
        {
            if (_linesList != null && !color.Equals(GetForeground())) { 
                foreach (TextEdit te in _linesList)
                    te.SetForeground(color);
            }
        }
        public Color GetForeground()
        {
            if (_linesList == null) return Color.White; //?????
            return _linesList[0].GetForeground();
        }

        private void RemoveAllLines() {
            if (_linesList == null) return;
            foreach (TextEdit te in _linesList)
                RemoveItem(te);
        }

        private void AddAllLines() {
            if (_linesList == null) return;
            foreach (TextEdit te in _linesList)
                AddItem(te);
        }

        public override void InitElements()
        {
            AddAllLines();
        }

        internal void OnTextInput(object sender, uint codepoint, KeyMods mods)
        {
            _linesList[0]?.InvokeInputTextEvents(codepoint, mods);
        }
    }

}

