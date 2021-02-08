using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using System;
using System.Drawing;

namespace SpaceVIL
{

    /// <summary>
    /// SpinItem is designed as a user interface element that 
    /// can increase and decrease the value by a specific step.
    /// <para/> Contains increment value button, decrement value button, text field.
    /// <para/> Supports all events except drag and drop.
    /// </summary>
    public class SpinItem : Prototype
    {
        static int count = 0;
        private HorizontalStack _horzStack; // = new HorizontalStack();
        private VerticalStack _vertStack; // = new VerticalStack();
        private TextEditRestricted _textInput; // = new TextEditRestricted();

        /// <summary>
        /// Increment value button.
        /// </summary>
        public ButtonCore UpButton; // = new ButtonCore();

        /// <summary>
        /// Decrement value button.
        /// </summary>
        public ButtonCore DownButton; // = new ButtonCore();

        private CustomShape _upArrow;
        private CustomShape _downArrow;

        /// <summary>
        /// Default SpinItem constructor.
        /// </summary>
        public SpinItem()
        {
            SetItemName("SpinItem_" + count);
            count++;

            _horzStack = new HorizontalStack();
            _vertStack = new VerticalStack();
            _textInput = new TextEditRestricted();
            UpButton = new ButtonCore();
            DownButton = new ButtonCore();
            _upArrow = new CustomShape();
            _downArrow = new CustomShape();

            _horzStack.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            _textInput.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);

            UpButton.IsFocusable = false;
            UpButton.EventMouseClick += OnUpClick;

            DownButton.IsFocusable = false;
            DownButton.EventMouseClick += OnDownClick;

            EventScrollUp = OnUpClick;
            EventScrollDown = OnDownClick;

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.SpinItem)));
        }

        /// <summary>
        /// Setting focus on this item if it is focusable.
        /// </summary>
        public override void SetFocus()
        {
            _textInput.SetFocus();
        }

        private void OnUpClick(Object sender, MouseArgs args)
        {
            _textInput.IncreaseValue();
        }

        private void OnDownClick(Object sender, MouseArgs args)
        {
            _textInput.DecreaseValue();
        }

        /// <summary>
        /// Getting current value of SpinItem.
        /// </summary>
        /// <returns>Current value of SpinItem.</returns>
        public double GetValue()
        {
            return _textInput.GetValue();
        }

        /// <summary>
        /// Setting integer value of SpinItem.
        /// </summary>
        /// <param name="value">Integer value.</param>
        public void SetValue(int value)
        {
            _textInput.SetValue(value);
        }

        /// <summary>
        /// Setting double floating piont value of SpinItem.
        /// </summary>
        /// <param name="value">Double floating point value.</param>
        public void SetValue(double value)
        {
            _textInput.SetValue(value);
        }

        /// <summary>
        /// Setting integer parameters of SpinItem.
        /// </summary>
        /// <param name="currentValue"> SpinItem current value. </param>
        /// <param name="minValue"> Minimum value limit. </param>
        /// <param name="maxValue"> Maximum value limit</param>
        /// <param name="step"> Step of increment and decrement. </param>
        public void SetParameters(int currentValue, int minValue, int maxValue, int step)
        {
            _textInput.SetParameters(currentValue, minValue, maxValue, step);
        }

        /// <summary>
        /// Setting double floating piont parameters of SpinItem.
        /// </summary>
        /// <param name="currentValue"> SpinItem current value. </param>
        /// <param name="minValue"> Minimum value limit. </param>
        /// <param name="maxValue"> Maximum value limit</param>
        /// <param name="step"> Step of increment and decrement. </param>
        public void SetParameters(double currentValue, double minValue, double maxValue, double step)
        {
            _textInput.SetParameters(currentValue, minValue, maxValue, step);
        }

        /// <summary>
        /// Setting accuracy (decimal places) of SpinItem.
        /// </summary>
        /// <param name="accuracy">Accuracy value.</param>
        public void SetAccuracy(int accuracy)
        {
            _textInput.SetAccuracy(accuracy);
        }

        /// <summary>
        /// Initializing all elements in the SpinItem.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            AddItem(_horzStack);
            _horzStack.AddItems(_textInput, _vertStack);
            _vertStack.AddItems(UpButton, DownButton);
            UpButton.AddItem(_upArrow);
            DownButton.AddItem(_downArrow);
        }

        /// <summary>
        /// Seting style of the SpinItem.
        /// <para/> Inner styles: "buttonsarea", "uparrow", "downarrow", "textedit".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public override void SetStyle(Style style)
        {
            if (style == null)
            {
                return;
            }
            base.SetStyle(style);
            Style innerStyle = style.GetInnerStyle("buttonsarea");
            if (innerStyle != null)
            {
                _vertStack.SetStyle(innerStyle);
            }

            innerStyle = style.GetInnerStyle("uparrowbutton");
            if (innerStyle != null)
            {
                UpButton.SetStyle(innerStyle);
            }

            innerStyle = style.GetInnerStyle("uparrow");
            if (innerStyle != null)
            {
                _upArrow.SetStyle(innerStyle);
            }

            innerStyle = style.GetInnerStyle("downarrowbutton");
            if (innerStyle != null)
            {
                DownButton.SetStyle(innerStyle);
            }

            innerStyle = style.GetInnerStyle("downarrow");
            if (innerStyle != null)
            {
                _downArrow.SetStyle(innerStyle);
            }

            innerStyle = style.GetInnerStyle("textedit");
            if (innerStyle != null)
            {
                _textInput.SetStyle(innerStyle);
            }
        }

        /// <summary>
        /// Setting alignment of a the text. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Text alignment as SpaceVIL.Core.ItemAlignment.</param>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _textInput.SetTextAlignment(alignment);
        }

        /// <summary>
        /// Setting alignment of the text. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Text alignment as sequence of SpaceVIL.Core.ItemAlignment.</param>
        public void SetTextAlignment(params ItemAlignment[] alignment)
        {
            // ItemAlignment common = alignment[0];
            // foreach (var a in alignment)
            // {
            //     common |= a;
            // }
            // _textInput.SetTextAlignment(common);
            _textInput.SetTextAlignment(BaseItemStatics.ComposeFlags(alignment));
        }

        /// <summary>
        /// Getting alignment of a SpinItem text. 
        /// </summary>
        /// <returns>Text alignment as SpaceVIL.Core.ItemAlignment.</returns>
        public ItemAlignment GetTextAlignment()
        {
            return _textInput.GetTextAlignment();
        }

        /// <summary>
        /// Setting indents for the text to offset text relative to this SpinItem.
        /// </summary>
        /// <param name="margin">Indents as SpaceVIL.Decorations.Indents.</param>
        public void SetTextMargin(Indents margin)
        {
            _textInput.SetTextMargin(margin);
        }

        /// <summary>
        /// Setting indents for the text to offset text relative to SpinItem.
        /// </summary>
        /// <param name="left">Indent on the left.</param>
        /// <param name="top">Indent on the top.</param>
        /// <param name="right">Indent on the right.</param>
        /// <param name="bottom">Indent on the bottom.</param>
        public void SetTextMargin(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            // _textInput.SetTextMargin(left, top, right, bottom);
            SetTextMargin(new Indents(left, top, right, bottom));
        }

        /// <summary>
        /// Getting indents of the text.
        /// </summary>
        /// <returns>Indents as SpaceVIL.Decorations.Indents.</returns>
        public Indents GetTextMargin()
        {
            return _textInput.GetTextMargin();
        }

        /// <summary>
        /// Setting background color of an item's shape.
        /// </summary>
        /// <param name="color">Background color as System.Drawing.Color.</param>
        public override void SetBackground(Color color)
        {
            _textInput.SetBackground(color);
        }

        /// <summary>
        /// Setting background color of an item's shape in byte RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        public override void SetBackground(int r, int g, int b)
        {
            SetBackground(GraphicsMathService.ColorTransform(r, g, b));
        }

        /// <summary>
        /// Setting background color of an item in byte RGBA format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        /// <param name="a">Alpha color component. Range: (0 - 255)</param>
        public override void SetBackground(int r, int g, int b, int a)
        {
            SetBackground(GraphicsMathService.ColorTransform(r, g, b, a));
        }

        /// <summary>
        /// Setting background color of an item in float RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        public override void SetBackground(float r, float g, float b)
        {
            SetBackground(GraphicsMathService.ColorTransform(r, g, b));
        }

        /// <summary>
        /// Setting background color of an item in float RGBA format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        /// <param name="a">Alpha color component. Range: (0.0f - 1.0f)</param>
        public override void SetBackground(float r, float g, float b, float a)
        {
            SetBackground(GraphicsMathService.ColorTransform(r, g, b, a));
        }

        /// <summary>
        /// Getting background color of an item.
        /// </summary>
        /// <returns>Background color as System.Drawing.Color.</returns>
        public override Color GetBackground()
        {
            return _textInput.GetBackground();
        }

        /// <summary>
        /// Setting font of the text.
        /// </summary>
        /// <param name="font">Font as System.Drawing.Font.</param>
        public void SetFont(Font font)
        {
            _textInput.SetFont(font);
        }

        /// <summary>
        /// Setting font size of the text.
        /// </summary>
        /// <param name="size">New size of the font.</param>
        public void SetFontSize(int size)
        {
            _textInput.SetFontSize(size);
        }

        /// <summary>
        /// Setting font style of the text.
        /// </summary>
        /// <param name="style">New font style as System.Drawing.FontStyle.</param>
        public void SetFontStyle(FontStyle style)
        {
            _textInput.SetFontStyle(style);
        }

        /// <summary>
        /// Setting new font family of the text.
        /// </summary>
        /// <param name="fontFamily">New font family as System.Drawing.FontFamily.</param>
        public void SetFontFamily(FontFamily fontFamily)
        {
            _textInput.SetFontFamily(fontFamily);
        }

        /// <summary>
        /// Getting the current font of the text.
        /// </summary>
        /// <returns>Font as System.Drawing.Font.</returns>
        public Font GetFont()
        {
            return _textInput.GetFont();
        }

        /// <summary>
        /// Setting text color of a SpinItem.
        /// </summary>
        /// <param name="color">Text color as System.Drawing.Color.</param>
        public void SetForeground(Color color)
        {
            _textInput.SetForeground(color);
        }

        /// <summary>
        /// Setting text color of a SpinItem in byte RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b)
        {
            SetForeground(GraphicsMathService.ColorTransform(r, g, b));
        }

        /// <summary>
        /// Setting text color of a SpinItem in byte RGBA format.
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
        /// Setting text color of a SpinItem in float RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b)
        {
            SetForeground(GraphicsMathService.ColorTransform(r, g, b));
        }

        /// <summary>
        /// Setting text color of a SpinItem in float RGBA format.
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
            return _textInput.GetForeground();
        }
    }
}
