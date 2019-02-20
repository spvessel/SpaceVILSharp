using System;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class SelectionItem : Prototype
    {
        private static int count = 0;

        ImageItem _image_icon;
        TextLine _text_object;

        /// <summary>
        /// Constructs a SelectionItem
        /// </summary>
        public SelectionItem()
        {
            SetItemName("SelectionItem_" + count);
            count++;

            _image_icon = new ImageItem();
            _text_object = new TextLine();

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.SelectionItem)));
        }

        /// <summary>
        /// Constructs a SelectionItem with text
        /// </summary>
        public SelectionItem(Image icon, String text) : this()
        {
            SetText(text);
            _image_icon.SetImage(icon);
        }

        public void SetIcon(Image icon)
        {
            _image_icon.SetImage(icon);
        }
        //text init
        /// <summary>
        /// Text alignment in the SelectionItem
        /// </summary>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _text_object.SetTextAlignment(alignment);
        }

        /// <summary>
        /// Text margin in the SelectionItem
        /// </summary>
        public void SetTextMargin(Indents margin)
        {
            _text_object.SetMargin(margin);
        }

        /// <summary>
        /// Text font parameters in the SelectionItem
        /// </summary>
        public void SetFont(Font font)
        {
            _text_object.SetFont(font);
        }
        public void SetFontSize(int size)
        {
            _text_object.SetFontSize(size);
        }
        public void SetFontStyle(FontStyle style)
        {
            _text_object.SetFontStyle(style);
        }
        public void SetFontFamily(FontFamily font_family)
        {
            _text_object.SetFontFamily(font_family);
        }
        public Font GetFont()
        {
            return _text_object.GetFont();
        }

        /// <summary>
        /// Set text in the SelectionItem
        /// </summary>
        public void SetText(String text)
        {
            _text_object.SetItemText(text);
        }
        public String GetText()
        {
            return _text_object.GetItemText();
        }

        /// <summary>
        /// Text color in the SelectionItem
        /// </summary>
        public void SetForeground(Color color)
        {
            _text_object.SetForeground(color);
        }
        public void SetForeground(int r, int g, int b)
        {
            _text_object.SetForeground(r, g, b);
        }
        public void SetForeground(int r, int g, int b, int a)
        {
            _text_object.SetForeground(r, g, b, a);
        }
        public void SetForeground(float r, float g, float b)
        {
            _text_object.SetForeground(r, g, b);
        }
        public void SetForeground(float r, float g, float b, float a)
        {
            _text_object.SetForeground(r, g, b, a);
        }
        public Color GetForeground()
        {
            return _text_object.GetForeground();
        }

        /// <summary>
        /// Initialization and adding of all elements in the SelectionItem
        /// </summary>
        public override void InitElements()
        {
            AddItems(_image_icon, _text_object);
        }

        //style
        /// <summary>
        /// Set style of the SelectionItem
        /// </summary>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
            SetForeground(style.Foreground);
            SetFont(style.Font);
            SetTextAlignment(style.TextAlignment);
        }
    }
}
