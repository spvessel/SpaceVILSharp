using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using SpaceVIL.Common;
using SpaceVIL.Core;

namespace SpaceVIL
{
    abstract internal class TextItem : Primitive
    {
        // private List<float> _alphas;
        // private List<float> _interCoords;
        // private float[] _coordinates;
        // private float[] _colors;
        private String _itemText = "";

        private Font _font = DefaultsService.GetDefaultFont();

        static int count = 0;

        internal TextItem()
        {
            SetItemName("TextItem_" + count);
            SetBackground(Color.Transparent);
            SetWidthPolicy(SizePolicy.Expand);
            SetHeightPolicy(SizePolicy.Expand);
            count++;
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

        // protected void SetRealCoords(List<float> realCoords)
        // {
        //     _coordinates = ToGL(realCoords);
        // }
        // 
        // protected void SetAlphas(List<float> alphas)
        // {
        //     _alphas = alphas;
        //     //SetColor(alphas);
        // }

        internal String GetItemText()
        {
            return _itemText;
        }

        internal void SetItemText(String itemText)
        {
            if (!_itemText.Equals(itemText))
            {
                _itemText = itemText;
                UpdateData();
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

        // internal float[] GetCoordinates()
        // {
        //     return _coordinates;
        // }

        /*
        internal float[] GetColors()
        {
            return _colors;
        }
        */

        // private float[] ToGL(List<float> coord)
        // {
        //     float[] outCoord = new float[coord.Count];
        //     float f;
        //     float x0 = GetX();
        //     float y0 = GetY();
        //     float windowH = GetHandler().GetHeight() / 2f;
        //     float windowW = GetHandler().GetWidth() / 2f;
        // 
        //     for (int i = 0; i < coord.Count; i += 3)
        //     {
        //         f = coord[i];
        //         f += x0;
        //         f = f / windowW - 1.0f;
        //         outCoord[i] = f;
        // 
        //         f = coord[i + 1];
        //         f += y0;
        //         f = -(f / windowH - 1.0f);
        //         outCoord[i + 1] = f;
        // 
        //         f = coord[i + 2];
        //         outCoord[i + 2] = f;
        //     }
        // 
        //     return outCoord;
        // }

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
        public void SetTextAlignment(params ItemAlignment[] alignment)
        {
            ItemAlignment common = alignment.ElementAt(0);
            if (alignment.Length > 1)
            {
                for (int i = 1; i < alignment.Length; i++)
                {
                    common |= alignment.ElementAt(i);
                }
            }
            SetTextAlignment(common);
        }

        // public virtual float[] Shape()
        // {
        //     return GetCoordinates();
        // }

    }
}
