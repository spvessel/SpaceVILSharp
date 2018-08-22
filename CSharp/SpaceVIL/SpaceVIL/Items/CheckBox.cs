using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Threading.Tasks;

namespace SpaceVIL
{
    public class CheckBox : VisualItem
    {
        static int count = 0;
        private Label _text_object;
        private Indicator _indicator;
        public Indicator GetIndicator()
        {
            return _indicator;
        }

        public CheckBox()
        {
            SetItemName("CheckBox_" + count);
            SetBackground(Color.Transparent);
            SetSpacing(5, 0);
            EventKeyPress += OnKeyPress;
            count++;

            //text
            _text_object = new Label();
            _text_object.SetItemName(GetItemName() + "_text_object");
            _text_object.SetBackground(255, 255, 255, 20);
            _text_object.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            _text_object.SetAlignment(ItemAlignment.VCenter);
            _text_object.SetTextAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
            _text_object.SetPadding(10);

            //indicator
            _indicator = new Indicator();
            _indicator.SetAlignment(ItemAlignment.VCenter | ItemAlignment.Left);
        }

        protected virtual void OnKeyPress(object sender, KeyArgs args)
        {
            if (args.Scancode == 0x1C)
                EventMouseClick?.Invoke(this, new MouseArgs());
        }

        public override void InitElements()
        {
            //events
            EventMouseClick += _indicator.GetIndicatorMarker().EventToggle;
            _text_object.EventMouseHover += (sender, args) => _indicator.GetIndicatorMarker().IsMouseHover = _text_object.IsMouseHover;
            
            //adding
            AddItem(_indicator);
            AddItem(_text_object);
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
            _text_object.SetTextAlignment(alignment);
        }
        public void SetTextMargin(Margin margin)
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
        public void SetText(String text)
        {
            _text_object.SetText(text);
        }
        public String GetText()
        {
            return _text_object.GetText();
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

        //style
        public override void SetStyle(Style style)
        {
            base.SetStyle(style);
            SetForeground(style.Foreground);
            SetFont(style.Font);
        }
    }
}
