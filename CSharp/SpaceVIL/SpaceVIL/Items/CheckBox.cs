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
        internal class CustomIndicator : Indicator
        {
            protected internal override bool GetHoverVerification(float xpos, float ypos)
            {
                return false;
            }
        }

        static int count = 0;
        private TextLine _text_object;
        //private bool _toggled = false;
        private CustomIndicator _indicator;
        public Indicator GetIndicator()
        {
            return _indicator;
        }

        public CheckBox()
        {
            SetItemName("CheckBox_" + count);
            // SetBackground(255, 255, 255, 20);
            // SetSpacing(5, 0);
            EventKeyPress += OnKeyPress;
            count++;

            //text
            _text_object = new TextLine();
            _text_object.SetItemName(GetItemName() + "_text_object");
            // _text_object.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            // _text_object.SetAlignment(ItemAlignment.VCenter);
            // _text_object.SetTextAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
            // _text_object.SetMargin(10);

            //indicator
            _indicator = new CustomIndicator();
            //_indicator.SetAlignment(ItemAlignment.VCenter | ItemAlignment.Left);

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.CheckBox)));
        }

        protected virtual void OnKeyPress(object sender, KeyArgs args)
        {
            if (args.Scancode == 0x1C)
                EventMouseClick?.Invoke(this, new MouseArgs());
        }

        public override bool IsMouseHover
        {
            get { return base.IsMouseHover; }
            set
            {
                base.IsMouseHover = value;
                _indicator.GetIndicatorMarker().IsMouseHover = IsMouseHover;
                UpdateState();
            }
        }
        public override void InitElements()
        {
            //events
            _indicator.GetIndicatorMarker().EventToggle = null;
            EventMouseClick += (sender, args) => _indicator.GetIndicatorMarker().IsToggled = !_indicator.GetIndicatorMarker().IsToggled;
            // EventMouseHover += (sender, args) =>
            // {
            //     //if (_indicator.GetIndicatorMarker().IsMouseHover != IsMouseHover)
            //         _indicator.GetIndicatorMarker().IsMouseHover = IsMouseHover;
            // };

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
                child.SetX(startX + offset + child.GetMargin().Left);
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
        public void SetText(String text)
        {
            _text_object.SetItemText(text);
        }
        public String GetText()
        {
            return _text_object.GetItemText();
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

            Style inner_style = style.GetInnerStyle("indicator");
            if (inner_style != null)
            {
                _indicator.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("textline");
            if (inner_style != null)
            {
                _text_object.SetStyle(inner_style);
            }
        }
    }
}
