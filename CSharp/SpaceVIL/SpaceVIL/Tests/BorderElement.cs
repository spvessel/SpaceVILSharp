using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    class BorderElement : VisualItem, IPixelDrawable
    {
        private static int count = 0;
        private Label _text;
        private float[] _coords;
        private float[] _colors;
        private Color _color;

        public BorderElement()
        {
            SetItemName("BorderElement" + count);
            count++;
            SetBackground(Color.Transparent);
            //text
            _text = new Label();
            _text.SetItemName(GetItemName() + "_text");
            _text.SetBackground(255, 255, 255, 20);
            _text.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            _text.SetAlignment(ItemAlignment.Top | ItemAlignment.Left);
            //_text.SetTextAlignment(ItemAlignment.Right | ItemAlignment.Top);
            //_text.SetPadding(10);
            _text.SetText("Textertwey");
            _text.SetFont(new Font(new FontFamily("Courier New"), 16, FontStyle.Regular));
            _text.SetMargin(10);
        }

        public override void InitElements()
        {
            AddItem(_text);
        }

        public float[] GetCoords()
        {
            
            _text.SetY(GetY() - _text.GetTextHeight() / 2);
            //Console.WriteLine(_text.GetX() + " " + _text.GetTextWidth()+ " " + _text.GetY());
            UpdateCoords(GraphicsMathService.GetRectBorderIgnoreTop(GetWidth(), GetHeight(), 5, _text.GetTextWidth() + 10));
            return _coords;
        }

        public float[] GetColors() {
            UpdateColor();
            return _colors;
        }

        private void UpdateCoords(List<float> coordList) { //ToGl
            _coords = new float[coordList.Count];
            float f;
            float x0 = GetX();
            float y0 = GetY();
            float windowH = GetHandler().GetHeight() / 2f;
            float windowW = GetHandler().GetWidth() / 2f;

            float xmin = Int32.MaxValue;
            float xmax = Int32.MinValue;
            float ymin = Int32.MaxValue;
            float ymax = Int32.MinValue;

            for (int i = 0; i < coordList.Count; i += 3)
            {
                f = coordList[i];
                xmin = (xmin > f) ? f : xmin;
                xmax = (xmax < f) ? f : xmax;
                f += x0;
                f = f / windowW - 1.0f;
                _coords[i] = f;

                f = coordList[i + 1];
                ymin = (ymin > f) ? f : ymin;
                ymax = (ymax < f) ? f : ymax;
                f += y0;
                f = -(f / windowH - 1.0f);
                _coords[i + 1] = f;

                f = coordList[i + 2];
                _coords[i + 2] = f;
            }
        }

        private void UpdateColor() {
            _colors = new float[(_coords.Length / 3) * 4];

            for (int i = 0; i < _coords.Length / 3; i++)
            {
                _colors[i * 4 + 0] = _color.R;
                _colors[i * 4 + 1] = _color.G;
                _colors[i * 4 + 2] = _color.B;
                _colors[i * 4 + 3] = _color.A;
            }
        }

        public override void SetBackground(Color color) {
            _color = color;
        }
    }
}
