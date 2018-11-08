using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    public class ProgressBar : Prototype
    {
        static int count = 0;
        private TextLine _text_object;
        private Rectangle _rect;
        private int _maxValue = 100;
        private int _minValue = 0;
        private int _currentValue = 0;

        public ProgressBar()
        {
            SetItemName("ProgressBar_" + count);
            count++;

            _text_object = new TextLine();
            _text_object.SetItemName(GetItemName() + "_text_object");
            _rect = new Rectangle();

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.ProgressBar)));
        }

        public override void InitElements()
        {
            //text
            SetText("0%");
            AddItems(_rect, _text_object);
        }

        public void SetMaxValue(int value) { _maxValue = value; }
        public int GetMaxValue() { return _maxValue; }

        public void SetMinValue(int value) { _minValue = value; }
        public int GetMinValue() { return _minValue; }

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
        public void SetText(String text)
        {
            _text_object.SetItemText(text);
        }
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _text_object.SetTextAlignment(alignment);
        }
        public void SetTextMargin(Indents margin)
        {
            _text_object.SetMargin(margin);
        }
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

        internal override bool GetHoverVerification(float xpos, float ypos)
        {
            return false;
        }
        public override void AddItem(IBaseItem item)
        {
            base.AddItem(item);
            UpdateLayout();
        }
        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            UpdateLayout();
        }
        public override void SetX(int _x)
        {
            base.SetX(_x);
            UpdateLayout();
        }

        public void UpdateLayout()
        {
            UpdateProgressBar();
        }

        //style
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
