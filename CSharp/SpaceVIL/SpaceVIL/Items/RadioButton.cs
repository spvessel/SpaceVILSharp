using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    class RadioButton : VisualItem
    {
        static int count = 0;
        private Label _text;
        private Indicator _indicator;
        public Indicator GetIndicator()
        {
            return _indicator;
        }

        public RadioButton()
        {
            SetItemName("RadioButton_" + count);
            SetBackground(Color.FromArgb(0, 0, 0, 0));
            SetSpacing(5, 0);
            EventKeyPress += OnKeyPress;
            count++;

            //text
            _text = new Label();
            _text.SetItemName(GetItemName() + "_text");
            _text.SetBackground(255, 255, 255, 15);
            _text.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            _text.SetAlignment(ItemAlignment.VCenter);
            _text.SetTextAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
            _text.SetPadding(10);

            //indicator
            _indicator = new Indicator();
            _indicator.SetAlignment(ItemAlignment.VCenter | ItemAlignment.Left);
        }

        protected virtual void OnKeyPress(object sender, int key, KeyMods mods)
        {
            if (key == 0x1C)
                EventMouseClick?.Invoke(this);
        }
        
        public override void InitElements()
        {
            //adding
            AddItem(_indicator);
            AddItem(_text);

            //border radius
            _indicator.Border.Radius = _indicator.GetWidth() / 2;
            _indicator.GetIndicatorMarker().Border.Radius = _indicator.GetIndicatorMarker().GetWidth() / 2;
            _text.Border.Radius = _indicator.Border.Radius;

            //connect events
            _indicator.GetIndicatorMarker().EventToggle = (sender) => _indicator.GetIndicatorMarker().IsToggled = true;
            _indicator.GetIndicatorMarker().EventToggle += UncheckOthers;
            EventMouseClick += _indicator.GetIndicatorMarker().EventToggle.Invoke;

            EventMouseClick += _indicator.GetIndicatorMarker().EventToggle;//???? узнать почему работает и не плохо ли это
            _text.EventMouseClick += EventMouseClick.Invoke;

        }

        private void UncheckOthers(object sender)
        {
            List<BaseItem> items = GetParent().GetItems();
            foreach (var item in items)
            {
                if (item is RadioButton && !item.GetItemName().Equals(this.GetItemName()))
                {
                    (item as RadioButton).GetIndicator().GetIndicatorMarker().IsToggled = false;
                }
            }
        }

        public override bool IsVisible
        {
            get => base.IsVisible;
            set
            {
                base.IsVisible = value;
                foreach (var child in GetItems())
                    child.IsVisible = value;
            }
        }
        public override void InvokePoolEvents()
        {
            if (EventMouseClick != null) EventMouseClick.Invoke(this);
        }

        //Layout rules
        public new void AddItem(BaseItem item)
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

        protected virtual void UpdateLayout()
        {
            int offset = 0;
            int startX = GetX() + GetPadding().Left;

            foreach (var child in GetItems())
            {
                child.SetX(startX + offset);
                if(child.GetWidthPolicy() == SizePolicy.Expand)
                {
                    child.SetWidth(GetWidth() - offset);
                }
                offset += child.GetWidth() + GetSpacing().Horizontal;
            }
        }

        //text init
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _text.SetTextAlignment(alignment);
        }
        public void SetTextMargin(Margin margin)
        {
            _text.SetMargin(margin);
        }
        public void SetFont(Font font)
        {
            _text.SetFont(font);
        }
        public Font GetFont()
        {
            return _text.GetFont();
        }
        public void SetText(String text)
        {
            _text.SetText(text);
        }
        public String GetText()
        {
            return _text.GetText();
        }
        public void SetForeground(Color color)
        {
            _text.SetForeground(color);
        }
        public Color GetForeground()
        {
            return _text.GetForeground();
        }

        //style
        public override void SetStyle(Style style)
        {
            base.SetStyle(style);
            SetForeground(style.Foreground);
            SetFont(style.Font);
        }
    }
}
