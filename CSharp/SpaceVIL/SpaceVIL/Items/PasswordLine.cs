using System;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// PaasswordLine is designed to hide the input of text information. 
    /// <para/> Contains text field, unhide button.
    /// <para/> Supports all events except drag and drop.
    /// </summary>
    public class PasswordLine : HorizontalStack
    {
        static int count = 0;
        private BlankItem _showPwdBtn;
        private TextEncrypt _textEncrypt;

        /// <summary>
        /// Default PasswordLine constructor
        /// </summary>
        public PasswordLine()
        {
            SetItemName("PasswordLine_" + count);
            _showPwdBtn = new BlankItem();
            _showPwdBtn.SetItemName(GetItemName() + "_marker");
            _textEncrypt = new TextEncrypt();
            count++;

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.PasswordLine)));
        }

        private void ShowPassword(bool value)
        {
            _textEncrypt.ShowPassword(value);
        }

        /// <summary>
        /// Setting focus on this PasswordLine if it is focusable.
        /// </summary>
        public override void SetFocus()
        {
            _textEncrypt.SetFocus();
        }

        /// <summary>
        /// Setting alignment of a the text. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Text alignment as SpaceVIL.Core.ItemAlignment.</param>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _textEncrypt.SetTextAlignment(alignment);
        }
        /// <summary>
        /// Setting alignment of the text. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Text alignment as sequence of SpaceVIL.Core.ItemAlignment.</param>
        public void SetTextAlignment(params ItemAlignment[] alignment)
        {
            _textEncrypt.SetTextAlignment(alignment);
        }
        /// <summary>
        /// Setting indents for the text to offset text relative to this PasswordLine.
        /// </summary>
        /// <param name="margin">Indents as SpaceVIL.Decorations.Indents.</param>
        public void SetTextMargin(Indents margin)
        {
            _textEncrypt.SetMargin(margin);
        }
        /// <summary>
        /// Setting indents for the text to offset text relative to PasswordLine.
        /// </summary>
        /// <param name="left">Indent on the left.</param>
        /// <param name="top">Indent on the top.</param>
        /// <param name="right">Indent on the right.</param>
        /// <param name="bottom">Indent on the bottom.</param>
        public void SetTextMargin(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            _textEncrypt.SetMargin(left, top, right, bottom);
        }
        /// <summary>
        /// Getting indents of the text.
        /// </summary>
        /// <returns>Indents as SpaceVIL.Decorations.Indents.</returns>
        public Indents GetTextMargin()
        {
            return _textEncrypt.GetTextMargin();
        }
        /// <summary>
        /// Setting font of the text.
        /// </summary>
        /// <param name="font">Font as System.Drawing.Font.</param>
        public void SetFont(Font font)
        {
            _textEncrypt.SetFont(font);
        }
        /// <summary>
        /// Setting font size of the text.
        /// </summary>
        /// <param name="size">New size of the font.</param>
        public void SetFontSize(int size)
        {
            _textEncrypt.SetFontSize(size);
        }
        /// <summary>
        /// Setting font style of the text.
        /// </summary>
        /// <param name="style">New font style as System.Drawing.FontStyle.</param>
        public void SetFontStyle(FontStyle style)
        {
            _textEncrypt.SetFontStyle(style);
        }
        /// <summary>
        /// Setting new font family of the text.
        /// </summary>
        /// <param name="fontFamily">New font family as System.Drawing.FontFamily.</param>
        public void SetFontFamily(FontFamily font_family)
        {
            _textEncrypt.SetFontFamily(font_family);
        }
        /// <summary>
        /// Getting the current font of the text.
        /// </summary>
        /// <returns>Font as System.Drawing.Font.</returns>
        public Font GetFont()
        {
            return _textEncrypt.GetFont();
        }

        /// <summary>
        /// Getting entered hidden text data.
        /// </summary>
        /// <returns>Text data.</returns>
        public String GetPassword()
        {
            return _textEncrypt.GetPassword();
        }

        /// <summary>
        /// Setting text color of a PasswordLine.
        /// </summary>
        /// <param name="color">Color as System.Drawing.Color.</param>
        public void SetForeground(Color color)
        {
            _textEncrypt.SetForeground(color);
        }
        /// <summary>
        /// Setting text color of a PasswordLine in byte RGB format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0 - 255)</param>
        /// <param name="g">Green bits of a color. Range: (0 - 255)</param>
        /// <param name="b">Blue bits of a color. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b)
        {
            _textEncrypt.SetForeground(r, g, b);
        }
        /// <summary>
        /// Setting text color of a PasswordLine in byte RGBA format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0 - 255)</param>
        /// <param name="g">Green bits of a color. Range: (0 - 255)</param>
        /// <param name="b">Blue bits of a color. Range: (0 - 255)</param>
        /// <param name="a">Alpha bits of a color. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b, int a)
        {
            _textEncrypt.SetForeground(r, g, b, a);
        }
        /// <summary>
        /// Setting text color of a PasswordLine in float RGB format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue bits of a color. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b)
        {
            _textEncrypt.SetForeground(r, g, b);
        }
        /// <summary>
        /// Setting text color of a PasswordLine in float RGBA format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="a">Alpha bits of a color. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b, float a)
        {
            _textEncrypt.SetForeground(r, g, b, a);
        }
        /// <summary>
        /// Getting current text color.
        /// </summary>
        /// <returns>Text color as System.Drawing.Color.</returns>
        public Color GetForeground()
        {
            return _textEncrypt.GetForeground();
        }

        /// <summary>
        /// Returns True if PasswordLine is editable otherwise returns False.
        /// </summary>
        /// <returns>True: if PasswordLine is editable.
        /// True: if PasswordLine is not editable.</returns>
        public bool IsEditable()
        {
            return _textEncrypt.IsEditable();
        }

        /// <summary>
        /// Setting PasswordLine text field be editable or be non-editable.
        /// </summary>
        /// <param name="value">True: if you want PasswordLine be editable.
        /// True: if you want PasswordLine be non-editable.</param>
        public void SetEditable(bool value)
        {
            _textEncrypt.SetEditable(value);
        }

        /// <summary>
        /// Initializing indicator and text in the PasswordLine. 
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            AddItems(_textEncrypt, _showPwdBtn);

            ImageItem eye = new ImageItem(DefaultsService.GetDefaultImage(EmbeddedImage.Eye, EmbeddedImageSize.Size32x32));
            eye.KeepAspectRatio(true);
            Color eyeBtnShadeColor = Color.FromArgb(80, 80, 80);
            eye.SetColorOverlay(eyeBtnShadeColor);
            _showPwdBtn.AddItem(eye);

            _showPwdBtn.SetPassEvents(false);
            _showPwdBtn.EventMousePress += (sender, args) =>
            {
                ShowPassword(true);
                eye.SetColorOverlay(Color.FromArgb(30, 30, 30));
            };
            _showPwdBtn.EventMouseClick += (sender, args) =>
            {
                ShowPassword(false);
                eye.SetColorOverlay(eyeBtnShadeColor);
            };
            _showPwdBtn.EventMouseLeave += (sender, args) =>
            {
                ShowPassword(false);
                eye.SetColorOverlay(eyeBtnShadeColor);
            };
        }

        /// <summary>
        /// Getting the text width (includes visible and invisible parts of the text).
        /// </summary>
        /// <returns>Text width.</returns>
        public int GetTextWidth()
        {
            return _textEncrypt.GetWidth();
        }

        /// <summary>
        /// Getting the text height (useful when you need resize PasswordLine by text height).
        /// </summary>
        /// <returns>Text height.</returns>
        public int GetTextHeight()
        {
            return _textEncrypt.GetHeight();
        }

        /// <summary>
        /// Remove all text from the PasswordLine.
        /// </summary>
        public override void Clear()
        {
            _textEncrypt.Clear();
        }

        /// <summary>
        /// Setting style of the PasswordLine.
        /// <para/> Inner styles: "showmarker", "textedit".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);

            Style inner_style = style.GetInnerStyle("showmarker");
            if (inner_style != null)
            {
                _showPwdBtn.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("textedit");
            if (inner_style != null)
            {
                _textEncrypt.SetStyle(inner_style);
            }
        }
        /// <summary>
        /// Setting the substrate text 
        /// (hint under main text, when you start typing substrate becomes invisible).
        /// </summary>
        /// <param name="substrateText">Substrate text.</param>
        public void SetSubstrateText(String substrateText)
        {
            _textEncrypt.SetSubstrateText(substrateText);
        }
        /// <summary>
        /// Setting font size of the substrate text 
        /// (hint under main text, when you start typing substrate becomes invisible).
        /// <para/> Font family of substrate text is the same as main font.
        /// </summary>
        /// <param name="size">New size of the font.</param>
        public void SetSubstrateFontSize(int size)
        {
            _textEncrypt.SetSubstrateFontSize(size);
        }
        /// <summary>
        /// Setting font style of the substrate text 
        /// (hint under main text, when you start typing substrate becomes invisible).
        /// <para/> Font family of substrate text is the same as main font.
        /// </summary>
        /// <param name="style">New font style as System.Drawing.FontStyle.</param>
        public void SetSubstrateFontStyle(FontStyle style)
        {
            _textEncrypt.SetSubstrateFontStyle(style);
        }
        /// <summary>
        /// Setting substrate text (hint under main text, when you start 
        /// typing substrate becomes invisible) color of a PasswordLine.
        /// </summary>
        /// <param name="color">Color as System.Drawing.Color.</param>
        public void SetSubstrateForeground(Color foreground)
        {
            _textEncrypt.SetSubstrateForeground(foreground);
        }
        /// <summary>
        /// Setting substrate text (hint under main text, when you start 
        /// typing substrate becomes invisible) color of a PasswordLine in byte RGB format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0 - 255)</param>
        /// <param name="g">Green bits of a color. Range: (0 - 255)</param>
        /// <param name="b">Blue bits of a color. Range: (0 - 255)</param>
        public void SetSubstrateForeground(int r, int g, int b)
        {
            _textEncrypt.SetSubstrateForeground(r, g, b);
        }
        /// <summary>
        /// Setting substrate text (hint under main text, when you start 
        /// typing substrate becomes invisible) color of a PasswordLine in byte RGBA format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0 - 255)</param>
        /// <param name="g">Green bits of a color. Range: (0 - 255)</param>
        /// <param name="b">Blue bits of a color. Range: (0 - 255)</param>
        /// <param name="a">Alpha bits of a color. Range: (0 - 255)</param>
        public void SeSubstratetForeground(int r, int g, int b, int a)
        {
            _textEncrypt.SetSubstrateForeground(r, g, b, a);
        }
        /// <summary>
        /// Setting substrate text (hint under main text, when you start 
        /// typing substrate becomes invisible)color of a PasswordLine in float RGB format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue bits of a color. Range: (0.0f - 1.0f)</param>
        public void SetSubstrateForeground(float r, float g, float b)
        {
            _textEncrypt.SetSubstrateForeground(r, g, b);
        }
        /// <summary>
        /// Setting substrate text (hint under main text, when you start 
        /// typing substrate becomes invisible)color of a PasswordLine in float RGBA format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="a">Alpha bits of a color. Range: (0.0f - 1.0f)</param>
        public void SetSubstrateForeground(float r, float g, float b, float a)
        {
            _textEncrypt.SetSubstrateForeground(r, g, b, a);
        }
        /// <summary>
        /// Getting current substrate text (hint under main text, when you start 
        /// typing substrate becomes invisible) color.
        /// </summary>
        /// <returns>Text color as System.Drawing.Color.</returns>
        public Color getSubstrateForeground()
        {
            return _textEncrypt.GetSubstrateForeground();
        }
        /// <summary>
        /// Getting substrate text 
        /// (hint under main text, when you start typing substrate becomes invisible).
        /// </summary>
        /// <returns>Substrate text.</returns>
        public String getSubstrateText()
        {
            return _textEncrypt.GetSubstrateText();
        }
    }
}