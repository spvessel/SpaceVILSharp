using System;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class FileSystemEntry : Prototype
    {
        private static int count = 0;

        TextLine _text_object;
        ImageItem _icon;
        private FileSystemEntryType _type;

        public FileSystemEntryType GetEntryType()
        {
            return _type;
        }

        public ImageItem GetIcon()
        {
            return _icon;
        }
        public void SetIcon(Bitmap icon, int width, int height)
        {
            _icon.SetSize(width, height);
            _icon.SetImage(icon);
            _icon.SetVisible(true);
        }

        /// <summary>
        /// Constructs a FileSystemEntry
        /// </summary>
        public FileSystemEntry(FileSystemEntryType type, String text)
        {
            SetItemName("FileSystemEntry_" + count);
            count++;
            _type = type;
            _icon = new ImageItem();
            _icon.SetVisible(false);
            _text_object = new TextLine();
            SetText(text);
            
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.FileSystemEntry)));
        }

        //text init
        /// <summary>
        /// Text alignment in the FileSystemEntry
        /// </summary>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _text_object.SetTextAlignment(alignment);
        }
        public void SetTextAlignment(params ItemAlignment[] alignment)
        {
            _text_object.SetTextAlignment(alignment);
        }

        /// <summary>
        /// Text margin in the FileSystemEntry
        /// </summary>
        public void SetTextMargin(Indents margin)
        {
            _text_object.SetMargin(margin);
        }

        /// <summary>
        /// Text font parameters in the FileSystemEntry
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
        /// Set text in the FileSystemEntry
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
        /// Text color in the FileSystemEntry
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
        /// Initialization and adding of all elements in the FileSystemEntry
        /// </summary>
        public override void InitElements()
        {
            AddItems(_icon, _text_object);
        }

        //style
        /// <summary>
        /// Set style of the FileSystemEntry
        /// </summary>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
            SetForeground(style.Foreground);
            SetFont(style.Font);
            SetTextAlignment(style.TextAlignment);

            Style innerStyle = style.GetInnerStyle("icon");
            if (innerStyle != null)
                _icon.SetStyle(innerStyle);
            innerStyle = style.GetInnerStyle("text");
            if (innerStyle != null)
                _text_object.SetMargin(innerStyle.Margin);
        }
    }
}
