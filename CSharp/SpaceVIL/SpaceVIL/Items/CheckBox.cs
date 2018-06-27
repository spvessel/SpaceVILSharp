using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Threading.Tasks;

namespace SpaceVIL
{
    class CheckBox : VisualItem
    {
        static int count = 0;
        private Label _text;
        private Indicator _indicator;
        public Indicator GetIndicator()
        {
            return _indicator;
        }

        public CheckBox()
        {
            SetItemName("CheckBox" + count);
            SetBackground(Color.Transparent);
            SetSpacing(5, 0);
            EventKeyPress += OnKeyPress;
            count++;

            //text
            _text = new Label();
            _text.SetItemName(GetItemName() + "_text");
            _text.SetBackground(255,255,255,20);
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

            //connect events
            _indicator.GetIndicatorMarker().EventToggle += EventMouseClick.Invoke;//????
            EventMouseClick += _indicator.GetIndicatorMarker().EventToggle;//???? узнать почему работает и не плохо ли это
            _text.EventMouseClick += EventMouseClick.Invoke;
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
                if (child.GetWidthPolicy() == SizePolicy.Expand)
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
