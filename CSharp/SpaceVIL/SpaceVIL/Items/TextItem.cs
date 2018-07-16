using System;
using System.Collections.Generic;
using System.Drawing;

namespace SpaceVIL
{
    abstract class TextItem : Primitive
    {
        private List<float> _alphas;
        private float[] _coordinates;
        private float[] _colors;
        private String _itemText = "";

        private Font _font = new Font(new FontFamily("Courier New"), 22, FontStyle.Regular);

        private bool _criticalFlag = true;
        private bool _coordsFlag = true;
        private bool _colorFlag = true;
        static int count = 0;
        public TextItem()
        {
            SetItemName("TextItem_" + count);
            SetBackground(Color.Black);
            SetWidthPolicy(SizePolicy.Expand);
            SetHeightPolicy(SizePolicy.Expand);
            count++;
        }

        public TextItem(String text, Font font, String name) : this()
        {
            _itemText = text;
            this._font = font;
        }

        protected void SetRealCoords(List<float> realCoords)
        {
            _coordinates = ToGL(realCoords);
        }

        protected void SetAlphas(List<float> alphas)
        {
            this._alphas = alphas;
            SetColor(alphas);
        }

        internal String GetItemText()
        {
            return _itemText;
        }
        internal void SetItemText(String itemText)
        {
            if (!this._itemText.Equals(itemText))
            {
                this._itemText = itemText;
                _criticalFlag = true;
            }
        }

        internal Font GetFont()
        {
            return _font;
        }
        internal void SetFont(Font font)
        {
            if (!this._font.Equals(font))
            {
                this._font = font;
                _criticalFlag = true;
            }
        }

        public abstract void UpdateData(UpdateType updateType);

        internal float[] GetCoordinates()
        {
            if (_criticalFlag)
            {
                UpdateData(UpdateType.Critical);
                _criticalFlag = false;
                _coordsFlag = false;
            }
            else if (_coordsFlag)
            {
                UpdateData(UpdateType.CoordsOnly);
                _coordsFlag = false;
            }
            return _coordinates;
        }

        internal float[] GetColors()
        {
            if (_colorFlag)
            {
                SetAlphas(_alphas);
                _colorFlag = false;
            }
            return _colors;
        }

        private float[] ToGL(List<float> coord)
        {
            float[] outCoord = new float[coord.Count];
            float f;
            float x0 = GetX();
            float y0 = GetY();
            float windowH = GetHandler().GetHeight() / 2f;
            float windowW = GetHandler().GetWidth() / 2f;

            float xmin = Int32.MaxValue;
            float xmax = Int32.MinValue;
            float ymin = Int32.MaxValue;
            float ymax = Int32.MinValue;

            for (int i = 0; i < coord.Count; i += 3)
            {
                f = coord[i];
                xmin = (xmin > f) ? f : xmin;
                xmax = (xmax < f) ? f : xmax;
                f += x0;
                f = f / windowW - 1.0f;
                outCoord[i] = f;

                f = coord[i + 1];
                ymin = (ymin > f) ? f : ymin;
                ymax = (ymax < f) ? f : ymax;
                f += y0;
                f = -(f / windowH - 1.0f);
                outCoord[i + 1] = f;

                f = coord[i + 2];
                outCoord[i + 2] = f;
            }
            
            SetWidth((int)Math.Abs(xmax - xmin));
            SetHeight((int)Math.Abs(ymax - ymin));

            return outCoord;
        }

        private Color _foreground = Color.White; //default
        public Color GetForeground()
        {
            return _foreground;
        }
        public void SetForeground(Color foreground)
        {
            if (!this._foreground.Equals(foreground))
            {
                this._foreground = foreground;
                _colorFlag = true;
            }
        }

        private void SetColor(List<float> alphas)
        {
            _colors = new float[alphas.Count * 4];
            
            Color col = GetForeground();
            float r = col.R * 1f / 255f;
            float g = col.G * 1f / 255f;
            float b = col.B * 1f / 255f;
            //Console.WriteLine(r + " " + g + " " + b);
            int inc = 0;
            foreach (float f in alphas)
            {
                //Console.WriteLine(f);
                float tmp = f;
                float one = r;
                float two = g;
                float three = b;
                if (f > 100) {
                    float t1, t2, t3;
                    if (tmp >= 200) //in
                    {
                        tmp -= 200;
                        tmp /= 100f;
                        if (tmp >= 2f / 3f)
                        {
                            t1 = 1;
                            t2 = 1;
                            t3 = (tmp - 2f / 3f) * 1f / 3f;
                        }
                        else if (tmp >= 1f / 3f)
                        {
                            t1 = 1;
                            t2 = (tmp - 1f / 3f) * 1f / 3f;
                            t3 = 0;
                        }
                        else 
                        {
                            t1 = tmp * 1f / 3f;
                            t2 = 0;
                            t3 = 0;
                        }

                        one = t1 / 3f + t2 * 2 / 9f + t3 / 9f;
                        two = t1 * 2 / 9f + t2 / 3f + t3 * 2 / 9f + 1 / 9f;
                        three = t1 / 9f + t2 * 2 / 9f + t3 / 3f + 2 / 9f + 1 / 9f;
                    }
                    else //out
                    {
                        tmp -= 100;
                        tmp /= 100f;
                        if (tmp > 2f / 3f)
                        {
                            t1 = 0;
                            t2 = 0;
                            t3 = (tmp - 2f / 3f) * 1f / 3f;
                        }
                        else if (tmp > 1f / 3f)
                        {
                            t1 = 0;
                            t2 = (tmp - 1f / 3f) * 1f / 3f;
                            t3 = 1;
                        }
                        else
                        {
                            t1 = tmp * 1f / 3f;
                            t2 = 1;
                            t3 = 1;
                        }

                        one = 1 / 9f + 2 / 9f + t1 / 3f + t2 * 2 / 9f + t3 / 9f;
                        two = 1 / 9f + t1 * 2 / 9f + t2 / 3f + t3 * 2 / 9f;
                        three = t1 / 9f + t2 * 2 / 9f + t3 / 3f;
                        
                    }
                    //tmp /= 5f;
                    tmp = 1 / 4f + tmp / 2f;
                    //if (one * r > one) Console.WriteLine("one " + r);
                    //if (two * g > two) Console.WriteLine("two " + g);
                    //if (three * b > three) Console.WriteLine("three " + b);
                    //one *= r;
                    //two *= g;
                    //three *= b;
                    //Console.WriteLine(one + " " + two + " " + three); 
                }

                if (tmp > 1) tmp = 1;
                _colors[inc] = one; inc++;
                _colors[inc] = two; inc++;
                _colors[inc] = three; inc++;
                _colors[inc] = tmp; inc++;
            }
        }

        private ItemAlignment _textAlignment;
        public ItemAlignment GetTextAlignment()
        {
            return _textAlignment;
        }
        public void SetTextAlignment(ItemAlignment value)
        {
            //Bottomif (!_textAlignment.Equals(value))
            {
                _textAlignment = value;
                _coordsFlag = true;
            }
        }

        protected void SetCoordsFlag(bool flag)
        {
            _coordsFlag = flag;
        }

        public virtual float[] Shape()
        {
            return GetCoordinates();
        }
    }
}
