using System;
using System.Linq;
using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// TextEdit is a basic implementation of a user interface 
    /// editable text field that contains only one line. 
    /// <para/> Supports all events except drag and drop.
    /// </summary>
    public class TextEdit : Prototype
    {
        static int count = 0;
        private TextEditStorage _textObject;

        /// <summary>
        /// Default TextEdit constructor.
        /// </summary>
        public TextEdit()
        {
            _textObject = new TextEditStorage();

            SetItemName("TextEdit_" + count);
            count++;
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.TextEdit)));
        }

        /// <summary>
        /// Constructs TextEdit eith the given text.
        /// </summary>
        /// <param name="text">Text for TextEdit.</param>
        public TextEdit(String text) : this()
        {
            SetText(text);
        }

        public override void SetFocus() {
            _textObject.SetFocus();
        }

        /// <summary>
        /// Setting alignment of a the text. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Text alignment as SpaceVIL.Core.ItemAlignment.</param>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _textObject.SetTextAlignment(alignment);
        }

        /// <summary>
        /// Setting alignment of the text. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Text alignment as sequence of SpaceVIL.Core.ItemAlignment.</param>
        public void SetTextAlignment(params ItemAlignment[] alignment)
        {
            // ItemAlignment common = alignment.ElementAt(0);
            // if (alignment.Length > 1)
            // {
            //     for (int i = 1; i < alignment.Length; i++)
            //     {
            //         common |= alignment.ElementAt(i);
            //     }
            // }
            // SetTextAlignment(common);
            SetTextAlignment(BaseItemStatics.ComposeFlags(alignment));
        }
        
        /// <summary>
        /// Getting alignment of a TextEdit text. 
        /// </summary>
        /// <returns>Text alignment as SpaceVIL.Core.ItemAlignment.</returns>
        public ItemAlignment GetTextAlignment()
        {
            return _textObject.GetTextAlignment();
        }

        /// <summary>
        /// Setting indents for the text to offset text relative to TextEdit.
        /// </summary>
        /// <param name="margin">Indents as SpaceVIL.Decorations.Indents.</param>
        public void SetTextMargin(Indents margin)
        {
            _textObject.SetTextMargin(margin);
        }

        /// <summary>
        /// Setting indents for the text to offset text relative to TextEdit.
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
            return _textObject.GetTextMargin();
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
            // return _text_object.GetFont();
            return _textObject.GetFont();
        }

        /// <summary>
        /// Setting the text.
        /// </summary>
        /// <param name="text">Text as System.String.</param>
        public void SetText(String text)
        {
            _textObject.SetText(text);
        }

        /// <summary>
        /// Setting the text.
        /// </summary>
        /// <param name="text">Text as System.Object.</param>
        public virtual void SetText(object text)
        {
            SetText(text.ToString());
        }

        /// <summary>
        /// Getting the current text of the TextEdit.
        /// </summary>
        /// <returns>Text as System.String.</returns>
        public String GetText()
        {
            // return PrivGetText();
            return _textObject.GetText();
        }

        /// <summary>
        /// Setting text color of a TextEdit.
        /// </summary>
        /// <param name="color">Text color as System.Drawing.Color.</param>
        public void SetForeground(Color color)
        {
            _textObject.SetForeground(color);
        }

        /// <summary>
        /// Setting text color of a TextEdit in byte RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b)
        {
            SetForeground(GraphicsMathService.ColorTransform(r, g, b));
        }

        /// <summary>
        /// Setting text color of a TextEdit in byte RGBA format.
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
        /// Setting text color of a TextEdit in float RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b)
        {
            // _text_object.SetForeground(r, g, b);
            SetForeground(GraphicsMathService.ColorTransform(r, g, b));
        }

        /// <summary>
        /// Setting text color of a TextEdit in float RGBA format.
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
            return _textObject.GetForeground();
        }

        /// <summary>
        /// Returns True if TextEdit is editable otherwise returns False.
        /// </summary>
        /// <returns>True: if TextEdit is editable.
        /// True: if TextEdit is non-editable.</returns>
        public bool IsEditable()
        {
            return _textObject.IsEditable;
        }

        /// <summary>
        /// Setting TextEdit text field be editable or be non-editable.
        /// </summary>
        /// <param name="value">True: if you want TextEdit be editable.
        /// True: if you want TextEdit be non-editable.</param>
        public void SetEditable(bool value)
        {
            _textObject.IsEditable = value;
        }

        /// <summary>
        /// Initializing all elements in the TextEdit.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            AddItem(_textObject);
        }

        /// <summary>
        /// Getting the text width.
        /// </summary>
        /// <returns>Text width.</returns>
        public int GetTextWidth()
        {
            return _textObject.GetWidth();
        }

        /// <summary>
        /// Getting the text height.
        /// </summary>
        /// <returns>Text height.</returns>
        public int GetTextHeight()
        {
            return _textObject.GetHeight();
        }

        /// <summary>
        /// Getting the current selected text.
        /// </summary>
        /// <returns>Current selected text.</returns>
        public string GetSelectedText()
        {
            return _textObject.GetSelectedText();
        }

        /// <summary>
        /// Paste the specified text at the current position of the text cursor 
        /// (or replace the specified text at the current starting position of 
        /// the selected text).
        /// </summary>
        /// <param name="pasteStr">Text to insert.</param>
        public void PasteText(string pasteStr)
        {
            _textObject.PasteText(pasteStr);
        }

        /// <summary>
        /// Cuts and returns the current selected text.
        /// </summary>
        /// <returns>Selected text.</returns>
        public string CutText()
        {
            return _textObject.CutText();
        }

        /// <summary>
        /// Deletes all text in the TextEdit.
        /// </summary>
        public override void Clear()
        {
            _textObject.Clear();
        }

        /// <summary>
        /// Setting style of the TextEdit.
        /// <para/> Inner styles: "text".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public override void SetStyle(Style style)
        {
            base.SetStyle(style);
            Style innerStyle = style.GetInnerStyle("text");
            if (innerStyle != null)
            {
                _textObject.SetStyle(innerStyle);
            }
        }

        // internal bool IsBeginning()
        // {
        //     return _textObject.IsBeginning();
        // }

        /// <summary>
        /// Selecting entire text of the TextEdit.
        /// </summary>
        public void SelectAll()
        {
            _textObject.SelectAll();
        }

        /// <summary>
        /// Method for undo last change.
        /// </summary>
        public void Undo()
        {
            _textObject.Undo();
        }

        /// <summary>
        /// Method for redo last undo action.
        /// </summary>
        public void Redo()
        {
            _textObject.Redo();
        }

        /// <summary>
        /// Setting the substrate text 
        /// (hint under main text, when you start typing substrate becomes invisible).
        /// </summary>
        /// <param name="substrateText">Substrate text.</param>
        public void SetSubstrateText(String substrateText)
        {
            _textObject.SetSubstrateText(substrateText);
        }

        /// <summary>
        /// Setting font size of the substrate text 
        /// (hint under main text, when you start typing substrate becomes invisible).
        /// <para/> Font family of substrate text is the same as main font.
        /// </summary>
        /// <param name="size">New size of the font.</param>
        public void SetSubstrateFontSize(int size)
        {
            _textObject.SetSubstrateFontSize(size);
        }

        /// <summary>
        /// Setting font style of the substrate text 
        /// (hint under main text, when you start typing substrate becomes invisible).
        /// <para/> Font family of substrate text is the same as main font.
        /// </summary>
        /// <param name="style">New font style as System.Drawing.FontStyle.</param>
        public void SetSubstrateFontStyle(FontStyle style)
        {
            _textObject.SetSubstrateFontStyle(style);
        }

        /// <summary>
        /// Setting substrate text (hint under main text, when you start 
        /// typing substrate becomes invisible) color of a TextEdit.
        /// </summary>
        /// <param name="color">Substrate text color as System.Drawing.Color.</param>
        public void SetSubstrateForeground(Color color)
        {
            _textObject.SetSubstrateForeground(color);
        }

        /// <summary>
        /// Setting substrate text (hint under main text, when you start 
        /// typing substrate becomes invisible) color of a TextEdit in byte RGB format.
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
        /// typing substrate becomes invisible) color of a TextEdit in byte RGBA format.
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
        /// typing substrate becomes invisible)color of a TextEdit in float RGB format.
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
        /// typing substrate becomes invisible)color of a TextEdit in float RGBA format.
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
            return _textObject.GetSubstrateForeground();
        }

        /// <summary>
        /// Getting substrate text 
        /// (hint under main text, when you start typing substrate becomes invisible).
        /// </summary>
        /// <returns>Substrate text.</returns>
        public String GetSubstrateText()
        {
            return _textObject.GetSubstrateText();
        }

        /// <summary>
        /// Adding the specified text to the end of the existing text.
        /// </summary>
        /// <param name="text">Text for adding.</param>
        public void AppendText(String text)
        {
            _textObject.AppendText(text);
        }
    }
}