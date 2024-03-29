using System;
using System.Collections.Generic;
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
        /// Setting alignment of a PasswordLine text. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Text alignment as SpaceVIL.Core.ItemAlignment.</param>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _textEncrypt.SetTextAlignment(alignment);
        }

        /// <summary>
        /// Setting alignment of a PasswordLine text. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Text alignment as sequence of SpaceVIL.Core.ItemAlignment.</param>
        public void SetTextAlignment(params ItemAlignment[] alignment)
        {
            _textEncrypt.SetTextAlignment(alignment);
        }

        /// <summary>
        /// Getting alignment of a PasswordLine text. 
        /// </summary>
        /// <returns>Text alignment as SpaceVIL.Core.ItemAlignment.</returns>
        public ItemAlignment GetTextAlignment()
        {
            return _textEncrypt.GetTextAlignment();
        }

        /// <summary>
        /// Setting indents for the text to offset text relative to this PasswordLine.
        /// </summary>
        /// <param name="margin">Indents as SpaceVIL.Decorations.Indents.</param>
        public void SetTextMargin(Indents margin)
        {
            _textEncrypt.SetTextMargin(margin);
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
            SetTextMargin(new Indents(left, top, right, bottom));
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
        public void SetFontFamily(FontFamily fontFamily)
        {
            _textEncrypt.SetFontFamily(fontFamily);
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
        /// <param name="color">Text color as System.Drawing.Color.</param>
        public void SetForeground(Color color)
        {
            _textEncrypt.SetForeground(color);
        }

        /// <summary>
        /// Setting text color of a PasswordLine in byte RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b)
        {
            SetForeground(GraphicsMathService.ColorTransform(r, g, b));
        }

        /// <summary>
        /// Setting text color of a PasswordLine in byte RGBA format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        /// <param name="a">Alpha color component. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b, int a)
        {
            SetForeground(GraphicsMathService.ColorTransform(r, g, b, a));
        }

        /// <summary>
        /// Setting text color of a PasswordLine in float RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b)
        {
            SetForeground(GraphicsMathService.ColorTransform(r, g, b));
        }

        /// <summary>
        /// Setting text color of a PasswordLine in float RGBA format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        /// <param name="a">Alpha color component. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b, float a)
        {
            SetForeground(GraphicsMathService.ColorTransform(r, g, b, a));
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

            ImageItem eye = new ImageItem(DefaultsService.GetDefaultImage(EmbeddedImage.Eye, EmbeddedImageSize.Size64x64));
            eye.KeepAspectRatio(true);
            Color eyeBtnShadeColor = Color.FromArgb(80, 80, 80);
            eye.SetColorOverlay(eyeBtnShadeColor);
            _showPwdBtn.AddItem(eye);

            _textEncrypt.SetPassEvents(false);
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
        /// Getting boolean value to know if this item can pass further 
        /// any input events (mouse, keyboard and etc.).
        /// <para/> Tip: Need for filtering input events.
        /// </summary>
        /// <returns>True: if this item pass on any input events.
        /// False: If this item do not pass any input events.</returns>
        override public bool IsPassEvents()
        {
            return _textEncrypt.IsPassEvents();
        }

        /// <summary>
        /// Getting boolean value to know if this item can pass further 
        /// the specified type of input events (mouse, keyboard and etc.).
        /// </summary>
        /// <param name="e">Type of input events as SpaceVIL.Core.InputEventType.</param>
        /// <returns>True: if this item pass on the specified type of input events.
        /// False: If this item do not pass the specified type of input events.</returns>
        override public bool IsPassEvents(InputEventType e)
        {
            return _textEncrypt.IsPassEvents(e);
        }
        /// <summary>
        /// Getting all allowed input events.
        /// </summary>
        /// <returns>Allowed input events as List&lt;SpaceVIL.Core.InputEventType&gt;</returns>
        override public List<InputEventType> GetPassEvents()
        {
            return _textEncrypt.GetPassEvents();
        }
        /// <summary>
        /// Getting all blocked input events.
        /// </summary>
        /// <returns>Blocked input events as List&lt;SpaceVIL.Core.InputEventType&gt;</returns>
        override public List<InputEventType> GetBlockedEvents()
        {
            return _textEncrypt.GetBlockedEvents();
        }

        /// <summary>
        /// Setting on or off so that this item can pass further 
        /// any input events (mouse, keyboard and etc.).
        /// </summary>
        /// <param name="value">True: if you want that this item may to pass on any input events.
        /// False: if you want that this item cannot to pass on any input events.</param>
        override public void SetPassEvents(bool value)
        {
            _textEncrypt.SetPassEvents(value);
        }

        /// <summary>
        /// Setting on or off so that this item can pass further 
        /// the specified type of input events (mouse, keyboard and etc.).
        /// </summary>
        /// <param name="value">True: if you want this item can pass further the specified type of input events.
        /// False: if you want this item connot pass further the specified type of input events.</param>
        /// <param name="e">Type of input events as SpaceVIL.Core.InputEventType.</param>
        override public void SetPassEvents(bool value, InputEventType e)
        {
            _textEncrypt.SetPassEvents(value, e);
        }
        /// <summary>
        /// Setting on or off so that this item can pass further 
        /// the specified types of input events (mouse, keyboard and etc.).
        /// </summary>
        /// <param name="value">True: if you want this item can pass further the specified types of input events.
        /// False: if you want this item connot pass further the specified types of input events.</param>
        /// <param name="events">Sequence of input event types as SpaceVIL.Core.InputEventType.</param>
        override public void SetPassEvents(bool value, params InputEventType[] events)
        {
            _textEncrypt.SetPassEvents(value, events);
        }

        /// <summary>
        /// Getting the text width (useful when you need resize PasswordLine by text width).
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
        /// <param name="color">Substrate text color as System.Drawing.Color.</param>
        public void SetSubstrateForeground(Color color)
        {
            _textEncrypt.SetSubstrateForeground(color);
        }

        /// <summary>
        /// Setting substrate text (hint under main text, when you start 
        /// typing substrate becomes invisible) color of a PasswordLine in byte RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        public void SetSubstrateForeground(int r, int g, int b)
        {
            SetSubstrateForeground(GraphicsMathService.ColorTransform(r, g, b));
        }

        /// <summary>
        /// Setting substrate text (hint under main text, when you start 
        /// typing substrate becomes invisible) color of a PasswordLine in byte RGBA format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        /// <param name="a">Alpha color component. Range: (0 - 255)</param>
        public void SeSubstratetForeground(int r, int g, int b, int a)
        {
            SetSubstrateForeground(GraphicsMathService.ColorTransform(r, g, b, a));
        }

        /// <summary>
        /// Setting substrate text (hint under main text, when you start 
        /// typing substrate becomes invisible)color of a PasswordLine in float RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        public void SetSubstrateForeground(float r, float g, float b)
        {
            SetSubstrateForeground(GraphicsMathService.ColorTransform(r, g, b));
        }

        /// <summary>
        /// Setting substrate text (hint under main text, when you start 
        /// typing substrate becomes invisible)color of a PasswordLine in float RGBA format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        /// <param name="a">Alpha color component. Range: (0.0f - 1.0f)</param>
        public void SetSubstrateForeground(float r, float g, float b, float a)
        {
            SetSubstrateForeground(GraphicsMathService.ColorTransform(r, g, b, a));
        }

        /// <summary>
        /// Getting current substrate text (hint under main text, when you start 
        /// typing substrate becomes invisible) color.
        /// </summary>
        /// <returns>Text color as System.Drawing.Color.</returns>
        public Color GetSubstrateForeground()
        {
            return _textEncrypt.GetSubstrateForeground();
        }

        /// <summary>
        /// Getting substrate text 
        /// (hint under main text, when you start typing substrate becomes invisible).
        /// </summary>
        /// <returns>Substrate text.</returns>
        public String GetSubstrateText()
        {
            return _textEncrypt.GetSubstrateText();
        }
    }
}