using System;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// ButtonCore is the basic implementation of a user interface button. 
    /// <para/> Contains text. 
    /// <para/> Supports all events except drag and drop.
    /// </summary>
    public class ButtonCore : Prototype
    {
        private static int count = 0;
        private TextLine _textObject;

        /// <summary>
        /// Default ButtonCore constructor. Text is empty.
        /// </summary>
        public ButtonCore()
        {
            SetItemName("ButtonCore_" + count);
            count++;

            _textObject = new TextLine();
            EventKeyPress += OnKeyPress;

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.ButtonCore)));
        }

        /// <summary>
        /// Constructs a ButtonCore with the specified text.
        /// </summary>
        /// <param name="text">Button text as System.Text.</param>
        public ButtonCore(String text = "") : this()
        {
            SetText(text);
        }

        private void OnKeyPress(IItem sender, KeyArgs args)
        {
            if (args.Key == KeyCode.Enter)
                EventMouseClick?.Invoke(this, new MouseArgs());
        }

        /// <summary>
        /// Setting alignment of a button's text. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Text alignment as SpaceVIL.Core.ItemAlignment.</param>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _textObject.SetTextAlignment(alignment);
        }
        /// <summary>
        /// Setting alignment of a button's text. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Text alignment as sequence of SpaceVIL.Core.ItemAlignment.</param>
        public void SetTextAlignment(params ItemAlignment[] alignment)
        {
            _textObject.SetTextAlignment(alignment);
        }

        /// <summary>
        /// Setting indents for the text to offset text relative to ButtonCore.
        /// </summary>
        /// <param name="margin">Indents as SpaceVIL.Decorations.Indents.</param>
        public void SetTextMargin(Indents margin)
        {
            _textObject.SetMargin(margin);
        }
        /// <summary>
        /// Setting indents for the text to offset text relative to ButtonCore.
        /// </summary>
        /// <param name="left">Indent on the left.</param>
        /// <param name="top">Indent on the top.</param>
        /// <param name="right">Indent on the right.</param>
        /// <param name="bottom">Indent on the bottom.</param>
        public void SetTextMargin(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            _textObject.SetMargin(left, top, right, bottom);
        }
        /// <summary>
        /// Getting indents of the text.
        /// </summary>
        /// <returns>Indents as SpaceVIL.Decorations.Indents.</returns>
        public Indents GetTextMargin()
        {
            return _textObject.GetMargin();
        }

        /// <summary>
        /// Setting font of the text.
        /// </summary>
        /// <param name="font">Font as System.Drawing.Font.</param>
        public void SetFont(Font font)
        {
            _textObject.SetFont(font);
        }
        /// <summary>
        /// Setting font size of the text.
        /// </summary>
        /// <param name="size">New size of the font.</param>
        public void SetFontSize(int size)
        {
            _textObject.SetFontSize(size);
        }
        /// <summary>
        /// Setting font style of the text.
        /// </summary>
        /// <param name="style">New font style as System.Drawing.FontStyle.</param>
        public void SetFontStyle(FontStyle style)
        {
            _textObject.SetFontStyle(style);
        }
        /// <summary>
        /// Setting new font family of the text.
        /// </summary>
        /// <param name="fontFamily">New font family as System.Drawing.FontFamily.</param>
        public void SetFontFamily(FontFamily fontFamily)
        {
            _textObject.SetFontFamily(fontFamily);
        }
        /// <summary>
        /// Getting the current font of the text.
        /// </summary>
        /// <returns>Font as System.Drawing.Font.</returns>
        public Font GetFont()
        {
            return _textObject.GetFont();
        }

        /// <summary>
        /// Setting the text.
        /// </summary>
        /// <param name="text">Text as System.String.</param>
        public virtual void SetText(String text)
        {
            _textObject.SetItemText(text);
        }
        /// <summary>
        /// Getting the current text of the ButtonCore.
        /// </summary>
        /// <returns>Text as System.String.</returns>
        public virtual String GetText()
        {
            return _textObject.GetItemText();
        }
        /// <summary>
        /// Getting the text width (useful when you need resize ButtonCore by text width).
        /// </summary>
        /// <returns>Text width.</returns>
        public int GetTextWidth()
        {
            return _textObject.GetWidth();
        }
        /// <summary>
        /// Getting the text height (useful when you need resize ButtonCore by text height).
        /// </summary>
        /// <returns>Text height.</returns>
        public int GetTextHeight()
        {
            return _textObject.GetHeight();
        }
        /// <summary>
        /// Setting text color of a ButtonCore.
        /// </summary>
        /// <param name="color">Color as System.Drawing.Color.</param>
        public void SetForeground(Color color)
        {
            _textObject.SetForeground(color);
        }
        /// <summary>
        /// Setting text color of a ButtonCore in byte RGB format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0 - 255)</param>
        /// <param name="g">Green bits of a color. Range: (0 - 255)</param>
        /// <param name="b">Blue bits of a color. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b)
        {
            _textObject.SetForeground(r, g, b);
        }
        /// <summary>
        /// Setting text color of a ButtonCore in byte RGBA format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0 - 255)</param>
        /// <param name="g">Green bits of a color. Range: (0 - 255)</param>
        /// <param name="b">Blue bits of a color. Range: (0 - 255)</param>
        /// <param name="a">Alpha bits of a color. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b, int a)
        {
            _textObject.SetForeground(r, g, b, a);
        }
        /// <summary>
        /// Setting text color of a ButtonCore in float RGB format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue bits of a color. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b)
        {
            _textObject.SetForeground(r, g, b);
        }
        /// <summary>
        /// Setting text color of a ButtonCore in float RGBA format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="a">Alpha bits of a color. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b, float a)
        {
            _textObject.SetForeground(r, g, b, a);
        }
        /// <summary>
        /// Getting current text color.
        /// </summary>
        /// <returns>Text color as System.Drawing.Color.</returns>
        public Color GetForeground()
        {
            return _textObject.GetForeground();
        }

        /// <summary>
        /// Initializing text in the ButtonCore.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            AddItem(_textObject);
        }

        /// <summary>
        /// Setting style of the ButtonCore.
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
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
