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
        private HorizontalStack _horzStack = new HorizontalStack();
        private VerticalStack _vertStack = new VerticalStack();
        private TextEditRestricted textInput = new TextEditRestricted();

        /// <summary>
        /// Increment value button.
        /// </summary>
        public ButtonCore UpButton = new ButtonCore();

        /// <summary>
        /// Decrement value button.
        /// </summary>
        public ButtonCore DownButton = new ButtonCore();

        /// <summary>
        /// Default SpinItem constructor.
        /// </summary>
        public SpinItem()
        {
            SetItemName("SpinItem_" + count);
            count++;
            _horzStack.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            textInput.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.SpinItem)));
            UpButton.IsFocusable = false;
            DownButton.IsFocusable = false;
            UpButton.EventMouseClick += OnUpClick;
            DownButton.EventMouseClick += OnDownClick;
            EventScrollUp = OnUpClick;
            EventScrollDown = OnDownClick;
        }

        private void OnUpClick(Object sender, MouseArgs args)
        {
            textInput.IncreaseValue();
        }

        private void OnDownClick(Object sender, MouseArgs args)
        {
            textInput.DecreaseValue();
        }

        /// <summary>
        /// Getting current value of SpinItem.
        /// </summary>
        /// <returns>Current value of SpinItem.</returns>
        public double GetValue()
        {
            return textInput.GetValue();
        }

        /// <summary>
        /// Setting integer value of SpinItem.
        /// </summary>
        /// <param name="value">Integer value.</param>
        public void SetValue(int value)
        {
            textInput.SetValue(value);
        }

        /// <summary>
        /// Setting double floating piont value of SpinItem.
        /// </summary>
        /// <param name="value">Double floating point value.</param>
        public void SetValue(double value)
        {
            textInput.SetValue(value);
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
            textInput.SetParameters(currentValue, minValue, maxValue, step);
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
            textInput.SetParameters(currentValue, minValue, maxValue, step);
        }

        /// <summary>
        /// Setting accuracy (decimal places) of SpinItem.
        /// </summary>
        /// <param name="accuracy">Accuracy value.</param>
        public void SetAccuracy(int accuracy)
        {
            textInput.SetAccuracy(accuracy);
        }

        /// <summary>
        /// Initializing all elements in the SpinItem.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            AddItem(_horzStack);
            _horzStack.AddItems(textInput, _vertStack);
            _vertStack.AddItems(UpButton, DownButton);
        }

        /// <summary>
        /// Seting style of the SpinItem.
        /// <para/> Inner styles: "buttonsarea", "uparrow", "downarrow", "textedit".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
            Style innerStyle = style.GetInnerStyle("buttonsarea");
            if (innerStyle != null)
            {
                _vertStack.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("uparrow");
            if (innerStyle != null)
            {
                UpButton.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("downarrow");
            if (innerStyle != null)
            {
                DownButton.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("textedit");
            if (innerStyle != null)
            {
                textInput.SetStyle(innerStyle);
            }
        }

        /// <summary>
        /// Setting alignment of a the text. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Text alignment as SpaceVIL.Core.ItemAlignment.</param>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            textInput.SetTextAlignment(alignment);
        }

        /// <summary>
        /// Setting alignment of the text. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Text alignment as sequence of SpaceVIL.Core.ItemAlignment.</param>
        public void SetTextAlignment(params ItemAlignment[] alignment)
        {
            ItemAlignment common = alignment[0];
            foreach (var a in alignment)
            {
                common |= a;
            }
            textInput.SetTextAlignment(common);
        }

        /// <summary>
        /// Setting indents for the text to offset text relative to this SpinItem.
        /// </summary>
        /// <param name="margin">Indents as SpaceVIL.Decorations.Indents.</param>
        public void SetTextMargin(Indents margin)
        {
            textInput.SetMargin(margin);
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
            textInput.SetMargin(left, top, right, bottom);
        }

        /// <summary>
        /// Setting background color of an item's shape.
        /// </summary>
        /// <param name="color">Background color as System.Drawing.Color.</param>
        public override void SetBackground(Color color)
        {
            textInput.SetBackground(color);
        }

        /// <summary>
        /// Setting background color of an item's shape in byte RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        public override void SetBackground(int r, int g, int b)
        {
            textInput.SetBackground(r, g, b);
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
            textInput.SetBackground(r, g, b, a);
        }

        /// <summary>
        /// Setting background color of an item in float RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        public override void SetBackground(float r, float g, float b)
        {
            textInput.SetBackground(r, g, b);
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
            textInput.SetBackground(r, g, b, a);
        }

        /// <summary>
        /// Getting background color of an item.
        /// </summary>
        /// <returns>Background color as System.Drawing.Color.</returns>
        public override Color GetBackground()
        {
            return textInput.GetBackground();
        }

        /// <summary>
        /// Setting font of the text.
        /// </summary>
        /// <param name="font">Font as System.Drawing.Font.</param>
        public void SetFont(Font font)
        {
            textInput.SetFont(font);
        }

        /// <summary>
        /// Setting font size of the text.
        /// </summary>
        /// <param name="size">New size of the font.</param>
        public void SetFontSize(int size)
        {
            textInput.SetFontSize(size);
        }

        /// <summary>
        /// Setting font style of the text.
        /// </summary>
        /// <param name="style">New font style as System.Drawing.FontStyle.</param>
        public void SetFontStyle(FontStyle style)
        {
            textInput.SetFontStyle(style);
        }

        /// <summary>
        /// Setting new font family of the text.
        /// </summary>
        /// <param name="fontFamily">New font family as System.Drawing.FontFamily.</param>
        public void SetFontFamily(FontFamily font_family)
        {
            textInput.SetFontFamily(font_family);
        }

        /// <summary>
        /// Getting the current font of the text.
        /// </summary>
        /// <returns>Font as System.Drawing.Font.</returns>
        public Font GetFont()
        {
            return textInput.GetFont();
        }

        /// <summary>
        /// Setting text color of a SpinItem.
        /// </summary>
        /// <param name="color">Text color as System.Drawing.Color.</param>
        public void SetForeground(Color color)
        {
            textInput.SetForeground(color);
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
            return textInput.GetForeground();
        }
    }
}
