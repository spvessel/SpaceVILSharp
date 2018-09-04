using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    public class ProgressBar : VisualItem
    {
        static int count = 0;
        private Label _text_object;
        private Rectangle _rect;
        private int _maxValue = 100;
        private int _minValue = 0;
        private int _currentValue = 0;

        public ProgressBar()
        {
            SetItemName("ProgressBar_" + count);
            SetBackground(Color.Transparent);
            SetHeight(20);
            count++;

            _text_object = new Label();
            _rect = new Rectangle();
        }

        public override void InitElements()
        {
            //text
            _text_object.SetItemName(GetItemName() + "_text_object");
            _text_object.SetBackground(255, 255, 255, 20);
            _text_object.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            _text_object.SetAlignment(ItemAlignment.VCenter);
            _text_object.SetTextAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);
            _text_object.SetPadding(0, 2, 0, 0);
            _text_object.SetText("0%");
            _text_object.SetFont(new Font(new FontFamily("Courier New"), 14, FontStyle.Regular));

            //rectangle
            _rect.SetBackground(Color.FromArgb(255, 0, 191, 255)); //Перегрузить метод
            _rect.SetAlignment(ItemAlignment.Left);
            _rect.SetHeightPolicy(SizePolicy.Expand);
            _rect.SetWidthPolicy(SizePolicy.Fixed);
            _rect.SetWidth(0);
            AddItems(_rect, _text_object);
        }

        public void SetMaxValue(int value) { _maxValue = value; }
        public int GetMaxValue() { return _maxValue;  }

        public void SetMinValue(int value) { _minValue = value; }
        public int GetMinValue() { return _minValue;  }

        public void SetCurrentValue(int currentValue)
        {
            _currentValue = currentValue;
            UpdateProgressBar();
        }
        public int GetCurrentValue() { return _currentValue;  }

        private void UpdateProgressBar()
        {
            float AllLength = _maxValue - _minValue;
            float DonePercent;
            _currentValue = (_currentValue > _maxValue) ? _maxValue : _currentValue;
            _currentValue = (_currentValue < _minValue) ? _minValue : _currentValue;
            DonePercent = (_currentValue - _minValue) / AllLength;

            _text_object.SetText(Math.Round(DonePercent * 100f, 1).ToString() + "%");
            _rect.SetWidth((int)Math.Round(GetWidth() * DonePercent));
        }

        //text init
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

        protected internal override bool GetHoverVerification(float xpos, float ypos)
        {
            return false;
        }
        public override void AddItem(BaseItem item)
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
    }
}
