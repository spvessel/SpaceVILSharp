using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;

namespace SpaceVIL
{
    abstract internal class TextItem : Primitive
    {
        private List<float> _alphas;
        private List<float> _interCoords;
        private float[] _coordinates;
        private float[] _colors;
        private String _itemText = "";

        private Font _font = DefaultsService.GetDefaultFont();

        static int count = 0;
        private int queueCapacity = 512;

        internal TextItem()
        {
            SetItemName("TextItem_" + count);
            SetBackground(Color.Transparent);
            SetWidthPolicy(SizePolicy.Expand);
            SetHeightPolicy(SizePolicy.Expand);
            count++;
            undoQueue = new LinkedList<string>();
            redoQueue = new LinkedList<string>();
        }

        internal TextItem(String text, Font font) : this()
        {
            _itemText = text;
            _font = font;
        }

        internal TextItem(String text, Font font, String name) : this(text, font)
        {
            SetItemName(name);
        }

        protected void SetRealCoords(List<float> realCoords)
        {
            _coordinates = ToGL(realCoords);
        }

        protected void SetAlphas(List<float> alphas)
        {
            _alphas = alphas;
            //SetColor(alphas);
        }

        internal String GetItemText()
        {
            return _itemText;
        }

        internal void SetItemText(String itemText)
        {
            if (!_itemText.Equals(itemText))
            {
                if (isUndo)
                {
                    if (redoQueue.Count > queueCapacity)
                        redoQueue.RemoveLast();

                    redoQueue.AddFirst(_itemText);
                    isUndo = false;
                }
                else
                {
                    if (undoQueue.Count > queueCapacity)
                        undoQueue.RemoveLast();

                    undoQueue.AddFirst(_itemText);
                }
                _itemText = itemText;
                UpdateData();
            }
        }

        private bool isUndo = false;
        private LinkedList<string> undoQueue;
        internal void Undo() {
            if (undoQueue.Count > 0)
            {
                string tmpText = undoQueue.First.Value;
                undoQueue.RemoveFirst();
                isUndo = true;
                SetItemText(tmpText);
            }
        }

        private LinkedList<string> redoQueue;
        internal void Redo() {
            if (redoQueue.Count > 0) {
                string tmpText = redoQueue.First.Value;            
                redoQueue.RemoveFirst();
                SetItemText(tmpText);
            }
        }

        internal Font GetFont()
        {
            // if(_font == null)
            // _font = DefaultsService.GetDefaultFont();
            return _font;
        }
        internal void SetFont(Font font)
        {
            // if (!_font.Equals(font))
            if (_font != font)
            {
                _font = font;
                UpdateData();
            }
        }
        internal void SetFontSize(int size)
        {
            if (_font.Size != size)
            {
                _font = new Font(_font.FontFamily, size, _font.Style);
                UpdateData();
            }
        }
        internal void SetFontStyle(FontStyle style)
        {
            if (_font.Style != style)
            {
                _font = new Font(_font.FontFamily, _font.Size, style);
                UpdateData();
            }
        }
        internal void SetFontFamily(FontFamily font_family)
        {
            if (_font.FontFamily != font_family)
            {
                _font = new Font(font_family, _font.Size, _font.Style);
                UpdateData();
            }
        }

        public abstract void UpdateData();
        //protected abstract void UpdateCoords();

        internal float[] GetCoordinates()
        {
            return _coordinates;
        }

        /*
        internal float[] GetColors()
        {
            return _colors;
        }
        */

        private float[] ToGL(List<float> coord)
        {
            float[] outCoord = new float[coord.Count];
            float f;
            float x0 = GetX();
            float y0 = GetY();
            float windowH = GetHandler().GetHeight() / 2f;
            float windowW = GetHandler().GetWidth() / 2f;

            for (int i = 0; i < coord.Count; i += 3)
            {
                f = coord[i];
                f += x0;
                f = f / windowW - 1.0f;
                outCoord[i] = f;

                f = coord[i + 1];
                f += y0;
                f = -(f / windowH - 1.0f);
                outCoord[i + 1] = f;

                f = coord[i + 2];
                outCoord[i + 2] = f;
            }

            return outCoord;
        }

        private Color _foreground = Color.Black; //default
        public Color GetForeground()
        {
            return _foreground;
        }
        public void SetForeground(Color foreground)
        {
            if (!_foreground.Equals(foreground))
            {
                _foreground = foreground;
                //SetColor(_alphas); //_colorFlag = true;
            }
        }
        public void SetForeground(int r, int g, int b)
        {
            if (r < 0) r = Math.Abs(r); if (r > 255) r = 255;
            if (g < 0) g = Math.Abs(g); if (g > 255) g = 255;
            if (b < 0) b = Math.Abs(b); if (b > 255) b = 255;
            SetForeground(Color.FromArgb(255, r, g, b));
        }
        public void SetForeground(int r, int g, int b, int a)
        {
            if (r < 0) r = Math.Abs(r); if (r > 255) r = 255;
            if (g < 0) g = Math.Abs(g); if (g > 255) g = 255;
            if (b < 0) b = Math.Abs(b); if (b > 255) b = 255;
            SetForeground(Color.FromArgb(a, r, g, b));
        }
        public void SetForeground(float r, float g, float b)
        {
            if (r < 0) r = Math.Abs(r); if (r > 1.0f) r = 1.0f;
            if (g < 0) g = Math.Abs(g); if (g > 1.0f) g = 1.0f;
            if (b < 0) b = Math.Abs(b); if (b > 1.0f) b = 1.0f;
            SetForeground(Color.FromArgb(255, (int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f)));
        }
        public void SetForeground(float r, float g, float b, float a)
        {
            if (r < 0) r = Math.Abs(r); if (r > 1.0f) r = 1.0f;
            if (g < 0) g = Math.Abs(g); if (g > 1.0f) g = 1.0f;
            if (b < 0) b = Math.Abs(b); if (b > 1.0f) b = 1.0f;
            SetForeground(Color.FromArgb((int)(a * 255.0f), (int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f)));
        }

        // private void SetColor(List<float> alphas)
        // {
        //     if (alphas == null)
        //         return;
        //     _colors = new float[alphas.Count * 4];

        //     Color col = GetForeground();
        //     float r = col.R * 1f / 255f;
        //     float g = col.G * 1f / 255f;
        //     float b = col.B * 1f / 255f;
        //     //Console.WriteLine(r + " " + g + " " + b);
        //     int inc = 0;
        //     foreach (float f in alphas)
        //     {
        //         //Console.WriteLine(f);
        //         float tmp = f;
        //         float one = r;
        //         float two = g;
        //         float three = b;
        //         /*
        //         if (f > 100) {
        //             float t1, t2, t3;
        //             if (tmp >= 200) //in
        //             {
        //                 tmp -= 200;
        //                 tmp /= 100f;
        //                 if (tmp >= 2f / 3f)
        //                 {
        //                     t1 = 1;
        //                     t2 = 1;
        //                     t3 = (tmp - 2f / 3f) * 1f / 3f;
        //                 }
        //                 else if (tmp >= 1f / 3f)
        //                 {
        //                     t1 = 1;
        //                     t2 = (tmp - 1f / 3f) * 1f / 3f;
        //                     t3 = 0;
        //                 }
        //                 else
        //                 {
        //                     t1 = tmp * 1f / 3f;
        //                     t2 = 0;
        //                     t3 = 0;
        //                 }

        //                 one = t1 / 3f + t2 * 2 / 9f + t3 / 9f;
        //                 two = t1 * 2 / 9f + t2 / 3f + t3 * 2 / 9f + 1 / 9f;
        //                 three = t1 / 9f + t2 * 2 / 9f + t3 / 3f + 2 / 9f + 1 / 9f;
        //             }
        //             else //out
        //             {
        //                 tmp -= 100;
        //                 tmp /= 100f;
        //                 if (tmp > 2f / 3f)
        //                 {
        //                     t1 = 0;
        //                     t2 = 0;
        //                     t3 = (tmp - 2f / 3f) * 1f / 3f;
        //                 }
        //                 else if (tmp > 1f / 3f)
        //                 {
        //                     t1 = 0;
        //                     t2 = (tmp - 1f / 3f) * 1f / 3f;
        //                     t3 = 1;
        //                 }
        //                 else
        //                 {
        //                     t1 = tmp * 1f / 3f;
        //                     t2 = 1;
        //                     t3 = 1;
        //                 }

        //                 one = 1 / 9f + 2 / 9f + t1 / 3f + t2 * 2 / 9f + t3 / 9f;
        //                 two = 1 / 9f + t1 * 2 / 9f + t2 / 3f + t3 * 2 / 9f;
        //                 three = t1 / 9f + t2 * 2 / 9f + t3 / 3f;

        //             }
        //             //tmp /= 5f;
        //             tmp = 1 / 4f + tmp / 2f;
        //             //if (one * r > one) Console.WriteLine("one " + r);
        //             //if (two * g > two) Console.WriteLine("two " + g);
        //             //if (three * b > three) Console.WriteLine("three " + b);
        //             //one *= r;
        //             //two *= g;
        //             //three *= b;
        //             //Console.WriteLine(one + " " + two + " " + three); 
        //         }
        //         */
        //         if (tmp > 1) tmp = 1;
        //         _colors[inc] = one; inc++;
        //         _colors[inc] = two; inc++;
        //         _colors[inc] = three; inc++;
        //         _colors[inc] = tmp; inc++;
        //     }
        // }

        private ItemAlignment _textAlignment;
        public ItemAlignment GetTextAlignment()
        {
            return _textAlignment;
        }
        public void SetTextAlignment(ItemAlignment value)
        {
            if (!_textAlignment.Equals(value))
            {
                _textAlignment = value;
                //UpdateCoords(); //_coordsFlag = true;
            }
        }

        public virtual float[] Shape()
        {
            return GetCoordinates();
        }

    }
}
