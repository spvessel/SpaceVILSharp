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
            if (text == null)
            {
                text = "";
            }
            _itemText = text;
            if (font != null)
            {
                _font = font;
            }
        }

        internal TextItem(String text, Font font, String name) : this(text, font)
        {
            SetItemName(name);
        }

        internal String GetItemText()
        {
            return _itemText;
        }

        internal void SetItemText(String itemText)
        {
            if (itemText == null)
            {
                itemText = "";
            }
            if (!_itemText.Equals(itemText))
            {
                _itemText = itemText;
                UpdateData();
            }
        }

        internal Font GetFont()
        {
            return _font;
        }
        internal void SetFont(Font font)
        {
            if (font == null)
            {
                return;
            }
            if (!_font.Equals(font))
            {
                _font = font;
                UpdateData();
            }
        }
        internal void SetFontSize(int size)
        {
            if (_font.Size != size)
            {
                _font = FontService.ChangeFontSize(size, _font);
                UpdateData();
            }
        }
        internal void SetFontStyle(FontStyle style)
        {
            if (_font.Style != style)
            {
                _font = FontService.ChangeFontStyle(style, _font);
                UpdateData();
            }
        }
        internal void SetFontFamily(FontFamily font_family)
        {
            if (font_family == null)
            {
                return;
            }
            if (_font.FontFamily != font_family)
            {
                _font = FontService.ChangeFontFamily(font_family, _font);
                UpdateData();
            }
        }

        public abstract void UpdateData();

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
            }
        }
        public void SetForeground(int r, int g, int b)
        {
            SetForeground(GraphicsMathService.ColorTransform(r, g, b));
        }
        public void SetForeground(int r, int g, int b, int a)
        {
            SetForeground(GraphicsMathService.ColorTransform(r, g, b, a));
        }
        public void SetForeground(float r, float g, float b)
        {
            SetForeground(GraphicsMathService.ColorTransform(r, g, b));
        }
        public void SetForeground(float r, float g, float b, float a)
        {
            SetForeground(GraphicsMathService.ColorTransform(r, g, b, a));
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
            }
        }
        public void SetTextAlignment(params ItemAlignment[] alignment)
        {
            if (alignment == null)
                return;
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
    }
}
