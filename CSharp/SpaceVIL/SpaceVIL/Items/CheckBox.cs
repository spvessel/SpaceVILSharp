using System;
using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// CheckBox is the basic implementation of a user interface check box with 
    /// the ability to be checked or be unchecked. 
    /// <para/> Contains text and indicator. 
    /// <para/> Supports all events except drag and drop.
    /// </summary>
    public class CheckBox : Prototype
    {
        internal class CustomIndicator : Indicator
        {
            protected internal override bool GetHoverVerification(float xpos, float ypos)
            {
                return false;
            }
        }

        static int count = 0;
        private TextLine _textObject;
        //private bool _toggled = false;
        private CustomIndicator _indicator;

        /// <summary>
        /// Getting indicator item of the CheckBox.
        /// </summary>
        /// <returns>Indicator as SpaceVIL.Indicator.</returns>
        public Indicator GetIndicator()
        {
            return _indicator;
        }

        /// <summary>
        /// Default CheckBox constructor. Text is empty.
        /// </summary>
        public CheckBox()
        {
            SetItemName("CheckBox_" + count);
            count++;
            EventKeyPress += OnKeyPress;

            //text
            _textObject = new TextLine();
            _textObject.SetItemName(GetItemName() + "_textObject");

            //indicator
            _indicator = new CustomIndicator();
            _indicator.IsFocusable = false;

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.CheckBox)));
        }
        /// <summary>
        /// Constructs a CheckBox with the specified text.
        /// </summary>
        /// <param name="text">CheckBox text as System.String.</param>
        public CheckBox(String text) : this()
        {
            SetText(text);
        }

        private void OnKeyPress(object sender, KeyArgs args)
        {
            if (args.Key == KeyCode.Enter || args.Key == KeyCode.Space)
                EventMouseClick?.Invoke(this, new MouseArgs());
        }

        /// <summary>
        /// Overrided Prototype.SetMouseHover(bool) method.
        /// <para/> Setting this item hovered (mouse cursor located within item's shape).
        /// </summary>
        /// <param name="value">True: if you want this item be hovered. 
        /// False: if you want this item be not hovered.</param>
        public override void SetMouseHover(bool value)
        {
            base.SetMouseHover(value);
            _indicator.GetIndicatorMarker().SetMouseHover(IsMouseHover());
            UpdateState();
        }

        /// <summary>
        /// Initializing indicator and text in the CheckBox. 
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            _indicator.GetIndicatorMarker().EventToggle = null;
            EventMouseClick += (sender, args) => _indicator.GetIndicatorMarker().SetToggled(!_indicator.GetIndicatorMarker().IsToggled());
            AddItems(_indicator, _textObject);
        }

        /// <summary>
        /// Returns True if CheckBox is checked otherwise returns False.
        /// </summary>
        /// <returns>True: CheckBox is checked. False: CheckBox is unchecked.</returns>
        public bool IsChecked()
        {
            return _indicator.GetIndicatorMarker().IsToggled();
        }
        /// <summary>
        /// Setting CheckBox checked or unchecked.
        /// </summary>
        /// <param name="value">True: if you want CheckBox to be checked. 
        /// False: if you want CheckBox to be unchecked.</param>
        public void SetChecked(bool value)
        {
            _indicator.GetIndicatorMarker().SetToggled(value);
        }

        /// <summary>
        /// Setting alignment of the text. 
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
            _textObject.SetTextAlignment(alignment);
        }
        /// <summary>
        /// Setting indents for the text to offset text relative to this CheckBox.
        /// </summary>
        /// <param name="margin">Indents as SpaceVIL.Decorations.Indents.</param>
        public void SetTextMargin(Indents margin)
        {
            _textObject.SetMargin(margin);
        }
        /// <summary>
        /// Setting indents for the text to offset text relative to CheckBox.
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
        /// Getting the current text of the CheckBox.
        /// </summary>
        /// <returns>Text as System.String.</returns>
        public virtual String GetText()
        {
            return _textObject.GetItemText();
        }
        /// <summary>
        /// Getting the text width (useful when you need resize CheckBox by text width).
        /// </summary>
        /// <returns>Text width.</returns>
        public int GetTextWidth()
        {
            return _textObject.GetWidth();
        }
        /// <summary>
        /// Getting the text height (useful when you need resize CheckBox by text height).
        /// </summary>
        /// <returns>Text height.</returns>
        public int GetTextHeight()
        {
            return _textObject.GetHeight();
        }
        /// <summary>
        /// Setting text color of a CheckBox.
        /// </summary>
        /// <param name="color">Text color as System.Drawing.Color.</param>
        public void SetForeground(Color color)
        {
            _textObject.SetForeground(color);
        }
        /// <summary>
        /// Setting text color of a CheckBox in byte RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b)
        {
            _textObject.SetForeground(r, g, b);
        }
        /// <summary>
        /// Setting text color of a CheckBox in byte RGBA format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        /// <param name="a">Alpha color component. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b, int a)
        {
            _textObject.SetForeground(r, g, b, a);
        }
        /// <summary>
        /// Setting text color of a CheckBox in float RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b)
        {
            _textObject.SetForeground(r, g, b);
        }
        /// <summary>
        /// Setting text color of a CheckBox in float RGBA format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        /// <param name="a">Alpha color component. Range: (0.0f - 1.0f)</param>
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
        /// Setting style of the CheckBox.
        /// <para/> Inner styles: "indicator", "text".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);

            SetForeground(style.Foreground);
            SetFont(style.Font);

            Style innerStyle = style.GetInnerStyle("indicator");
            if (innerStyle != null)
            {
                _indicator.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("text");
            if (innerStyle != null)
            {
                _textObject.SetStyle(innerStyle);
            }
        }
    }
}
