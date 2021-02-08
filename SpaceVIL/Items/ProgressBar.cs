using System;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// LoadingScreen is designed to show progress 
    /// the execution of any long time task.
    /// <para/> Contains image and text.
    /// <para/> Supports all events except drag and drop.
    /// <para/> By default ProgressBar cannot get focus.
    /// </summary>
    public class ProgressBar : Prototype
    {
        static int count = 0;
        private TextLine _textObject;
        /// <summary>
        /// Setting the text that represents the progress of the unfinished task, 
        /// visible or invisible.
        /// </summary>
        /// <param name="value">True: if text should be visible. 
        /// False: if text should be invisible.</param>
        public void SetValueVisible(bool value)
        {
            _textObject.SetVisible(value);
        }
        /// <summary>
        /// Returns True if text that represents the progress of 
        /// the unfinished task is visible, otherwise returns False.
        /// </summary>
        /// <returns>True: if text is visible. 
        /// False: if text is invisible.</returns>
        public bool IsValueVisible()
        {
            return _textObject.IsVisible();
        }

        private Rectangle _rect;
        private int _maxValue = 100;
        private int _minValue = 0;
        private int _currentValue = 0;

        /// <summary>
        /// Default ProgressBar constructor.
        /// </summary>
        public ProgressBar()
        {
            SetItemName("ProgressBar_" + count);
            count++;

            _textObject = new TextLine();
            _textObject.SetItemName(GetItemName() + "_textObject");
            _rect = new Rectangle();

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.ProgressBar)));
            IsFocusable = false;
        }

        /// <summary>
        /// Initializing all elements in the ProgressBar. 
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            //text
            _textObject.SetItemText("0%");
            AddItems(_rect, _textObject);
        }

        /// <summary>
        /// Setting the maximum progress value of the unfinished task limit. 
        /// Progress value cannot be greater than this limit.
        /// </summary>
        /// <param name="value">Maximum progress value of the unfinished task limit.</param>
        public void SetMaxValue(int value)
        {
            _maxValue = value;
        }
        /// <summary>
        /// Getting the current maximum progress value of the unfinished task limit.
        /// </summary>
        /// <returns>Maximum progress value of the unfinished task limit.</returns>
        public int GetMaxValue()
        {
            return _maxValue;
        }
        /// <summary>
        /// Setting the minimum progress value of the unfinished task limit. 
        /// Progress value cannot be less than this limit.
        /// </summary>
        /// <param name="value">Minimum progress value of the unfinished task limit.</param>
        public void SetMinValue(int value)
        {
            _minValue = value;
        }
        /// <summary>
        /// Getting the current minimum sprogress value of the unfinished task limit.
        /// </summary>
        /// <returns>Minimum progress value of the unfinished task limit.</returns>
        public int GetMinValue()
        {
            return _minValue;
        }

        /// <summary>
        /// Setting the current progress value of the unfinished task. 
        /// If the value is greater/less than the maximum/minimum 
        /// progress value, then the progress value becomes equal 
        /// to the maximum/minimum value.
        /// </summary>
        /// <param name="value">Progress value of of the unfinished task.</param>
        public void SetCurrentValue(int value)
        {
            _currentValue = value;
            UpdateProgressBar();
        }
        /// <summary>
        /// Getting the progress value of the unfinished task.
        /// </summary>
        /// <returns>Progress value of the unfinished task.</returns>
        public int GetCurrentValue()
        {
            return _currentValue;
        }

        private void UpdateProgressBar()
        {
            float AllLength = _maxValue - _minValue;
            float DonePercent;
            _currentValue = (_currentValue > _maxValue) ? _maxValue : _currentValue;
            _currentValue = (_currentValue < _minValue) ? _minValue : _currentValue;
            DonePercent = (_currentValue - _minValue) / AllLength;
            string text = Math.Round(DonePercent * 100f, 1).ToString() + "%";
            _textObject.SetItemText(text);
            _rect.SetWidth((int)Math.Round(GetWidth() * DonePercent));
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
        /// Setting indents for the text to offset text relative to this ProgressBar.
        /// </summary>
        /// <param name="margin">Indents as SpaceVIL.Decorations.Indents.</param>
        public void SetTextMargin(Indents margin)
        {
            _textObject.SetMargin(margin);
        }
        /// <summary>
        /// Setting indents for the text to offset text relative to ProgressBar.
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
        /// Getting the text width (useful when you need resize ProgressBar by text width).
        /// </summary>
        /// <returns>Text width.</returns>
        public int GetTextWidth()
        {
            return _textObject.GetWidth();
        }
        /// <summary>
        /// Getting the text height (useful when you need resize ProgressBar by text height).
        /// </summary>
        /// <returns>Text height.</returns>
        public int GetTextHeight()
        {
            return _textObject.GetHeight();
        }
        /// <summary>
        /// Setting text color of a ProgressBar.
        /// </summary>
        /// <param name="color">Text color as System.Drawing.Color.</param>
        public void SetForeground(Color color)
        {
            _textObject.SetForeground(color);
        }
        /// <summary>
        /// Setting text color of a ProgressBar in byte RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b)
        {
            _textObject.SetForeground(r, g, b);
        }
        /// <summary>
        /// Setting text color of a ProgressBar in byte RGBA format.
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
        /// Setting text color of a ProgressBar in float RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b)
        {
            _textObject.SetForeground(r, g, b);
        }
        /// <summary>
        /// Setting text color of a ProgressBar in float RGBA format.
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

        protected internal override bool GetHoverVerification(float xpos, float ypos)
        {
            return false;
        }

        /// <summary>
        /// Adding item into the container (this).
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        public override void AddItem(IBaseItem item)
        {
            base.AddItem(item);
            UpdateLayout();
        }

        /// <summary>
        /// Setting item width. If the value is greater/less than the maximum/minimum 
        /// value of the width, then the width becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="width"> Width of the item. </param>
        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            UpdateLayout();
        }

        /// <summary>
        /// Setting X coordinate of the left-top corner of a shape.
        /// </summary>
        /// <param name="x">X position of the left-top corner.</param>
        public override void SetX(int x)
        {
            base.SetX(x);
            UpdateLayout();
        }

        internal void UpdateLayout()
        {
            UpdateProgressBar();
        }

        /// <summary>
        /// Setting style of the ContextMenu.
        /// <para/> Inner styles: "progressbar".
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

            Style innerStyle = style.GetInnerStyle("progressbar");
            if (innerStyle != null)
            {
                _rect.SetStyle(innerStyle);
            }
        }
    }
}
