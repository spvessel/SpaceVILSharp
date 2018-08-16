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
        private Label _text;
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

            _text = new Label();
            _rect = new Rectangle();
        }

        public override void InitElements()
        {
            //text
            _text.SetItemName(GetItemName() + "_text");
            _text.SetBackground(255, 255, 255, 20);
            _text.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            _text.SetAlignment(ItemAlignment.VCenter);
            _text.SetTextAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);
            _text.SetPadding(10);
            _text.SetText("0%");
            _text.SetFont(new Font(new FontFamily("Courier New"), 14, FontStyle.Regular));

            //rectangle
            _rect.SetBackground(Color.FromArgb(255, 0, 191, 255)); //Перегрузить метод
            _rect.SetAlignment(ItemAlignment.Left);
            _rect.SetHeightPolicy(SizePolicy.Expand);
            _rect.SetWidthPolicy(SizePolicy.Fixed);
            _rect.SetWidth(0);
            AddItems(_rect, _text);
        }

        public void SetMaxValue(int maxVelue) { _maxValue = maxVelue; }

        public void SetMinValue(int minValue) { _minValue = minValue; }

        public void SetCurrentValue(int currentValue)
        {
            _currentValue = currentValue;
            UpdateProgressBar();
        }

        private void UpdateProgressBar()
        {
            float AllLength = _maxValue - _minValue;
            float DonePercent;
            _currentValue = (_currentValue > _maxValue) ? _maxValue : _currentValue;
            _currentValue = (_currentValue < _minValue) ? _minValue : _currentValue;
            DonePercent = (_currentValue - _minValue) / AllLength;

            _text.SetText(Math.Round(DonePercent * 100f, 1).ToString() + "%");
            _rect.SetWidth((int)Math.Round(GetWidth() * DonePercent));

            GetHandler().UpdateScene();
        }
    }
}
