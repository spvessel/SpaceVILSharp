using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class ProgressBar : Prototype
    {
        static int count = 0;
        private TextLine _text_object;
        public void SetValueVisible(bool value)
        {
            _text_object.SetVisible(value);
        }
        public bool IsValueVisible()
        {
            return _text_object.IsVisible();
        }

        private Rectangle _rect;
        private int _maxValue = 100;
        private int _minValue = 0;
        private int _currentValue = 0;

        /// <summary>
        /// Constructs a ProgressBar
        /// </summary>
        public ProgressBar()
        {
            SetItemName("ProgressBar_" + count);
            count++;

            _text_object = new TextLine();
            _text_object.SetItemName(GetItemName() + "_text_object");
            _rect = new Rectangle();

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.ProgressBar)));
            IsFocusable = false;
        }

        /// <summary>
        /// Initialization and adding of all elements in the ProgressBar
        /// </summary>
        public override void InitElements()
        {
            //text
            SetText("0%");
            AddItems(_rect, _text_object);
        }

        /// <param name="value"> maximum value of the ProgressBar </param>
        public void SetMaxValue(int value) { _maxValue = value; }
        public int GetMaxValue() { return _maxValue; }

        /// <param name="value"> minimum value of the ProgressBar </param>
        public void SetMinValue(int value) { _minValue = value; }
        public int GetMinValue() { return _minValue; }

        /// <param name="currentValue"> current value of the ProgressBar </param>
        public void SetCurrentValue(int currentValue)
        {
            _currentValue = currentValue;
            UpdateProgressBar();
        }
        public int GetCurrentValue() { return _currentValue; }

        private void UpdateProgressBar()
        {
            float AllLength = _maxValue - _minValue;
            float DonePercent;
            _currentValue = (_currentValue > _maxValue) ? _maxValue : _currentValue;
            _currentValue = (_currentValue < _minValue) ? _minValue : _currentValue;
            DonePercent = (_currentValue - _minValue) / AllLength;
            string text = Math.Round(DonePercent * 100f, 1).ToString() + "%";
            _text_object.SetItemText(text);
            _rect.SetWidth((int)Math.Round(GetWidth() * DonePercent));
        }

        //text init
        /// <summary>
        /// Text in the ProgressBar
        /// </summary>
        public void SetText(String text)
        {
            _text_object.SetItemText(text);
        }

        /// <summary>
        /// Text alignment in the ProgressBar
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
        /// Text margin in the ProgressBar
        /// </summary>
        public void SetTextMargin(Indents margin)
        {
            _text_object.SetMargin(margin);
        }

        /// <summary>
        /// Text font parameters in the ProgressBar
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
        /// Text color in the ProgressBar
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

        protected internal override bool GetHoverVerification(float xpos, float ypos)
        {
            return false;
        }

        /// <summary>
        /// Add item to the ProgressBar
        /// </summary>
        public override void AddItem(IBaseItem item)
        {
            base.AddItem(item);
            UpdateLayout();
        }

        /// <summary>
        /// Set width of the ProgressBar
        /// </summary>
        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            UpdateLayout();
        }

        /// <summary>
        /// Set X position of the ProgressBar left top corner
        /// </summary>
        public override void SetX(int _x)
        {
            base.SetX(_x);
            UpdateLayout();
        }

        /// <summary>
        /// Update ProgressBar states
        /// </summary>
        public void UpdateLayout()
        {
            UpdateProgressBar();
        }

        //style
        /// <summary>
        /// Set style of the ProgressBar
        /// </summary>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);

            SetForeground(style.Foreground);
            SetFont(style.Font);
            SetTextAlignment(style.TextAlignment);

            Style inner_style = style.GetInnerStyle("progressbar");
            if (inner_style != null)
            {
                _rect.SetStyle(inner_style);
            }
        }
    }
}
