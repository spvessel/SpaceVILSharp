using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    public class RadioButton : Prototype, IHLayout
    {
        internal class CustomIndicator : Indicator
        {
            internal override bool GetHoverVerification(float xpos, float ypos)
            {
                return false;
            }
        }

        static int count = 0;
        private Label _text_object;
        private CustomIndicator _indicator;
        public Indicator GetIndicator()
        {
            return _indicator;
        }

        public RadioButton()
        {
            SetItemName("RadioButton_" + count);
            SetBackground(255, 255, 255, 20);
            SetSpacing(5, 0);
            EventKeyPress += OnKeyPress;
            count++;

            //text
            _text_object = new Label();
            _text_object.SetItemName(GetItemName() + "_text_object");
            _text_object.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            _text_object.SetAlignment(ItemAlignment.VCenter);
            _text_object.SetTextAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
            _text_object.SetMargin(10);

            //indicator
            _indicator = new CustomIndicator();
            // _indicator.SetAlignment(ItemAlignment.VCenter | ItemAlignment.Left);

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.RadioButton)));
        }

        protected virtual void OnKeyPress(object sender, KeyArgs args)
        {
            if (args.Scancode == 0x1C)
                EventMouseClick?.Invoke(this, new MouseArgs());
        }

        public override void SetMouseHover(bool value)
        {
            base.SetMouseHover(value);
            _indicator.GetIndicatorMarker().SetMouseHover(IsMouseHover());
            UpdateState();
        }
        public override void InitElements()
        {

            //border radius
            // _indicator.Border.Radius = _indicator.GetWidth() / 2;
            // _indicator.GetIndicatorMarker().Border.Radius = _indicator.GetIndicatorMarker().GetWidth() / 2;
            // _text_object.Border.Radius = _indicator.Border.Radius;

            //connect events
            _indicator.GetIndicatorMarker().EventToggle = null;
            EventMouseClick += (sender, args) =>
            {
                _indicator.GetIndicatorMarker().SetToggled(!_indicator.GetIndicatorMarker().IsToggled());
                if (_indicator.GetIndicatorMarker().IsToggled())
                    UncheckOthers(sender);
            };
            // _indicator.GetIndicatorMarker().EventToggle += (sender, args) => UncheckOthers(sender);

            //adding
            AddItem(_indicator);
            AddItem(_text_object);
        }

        private void UncheckOthers(object sender)
        {
            List<IBaseItem> items = GetParent().GetItems();
            foreach (var item in items)
            {
                if (item is RadioButton && !item.Equals(this))
                {
                    (item as RadioButton).GetIndicator().GetIndicatorMarker().SetToggled(false);
                }
            }
        }

        // public override bool IsVisible
        // {
        //     get => base.IsVisible;
        //     set
        //     {
        //         base.IsVisible = value;
        //         foreach (var child in GetItems())
        //             child.IsVisible = value;
        //     }
        // }

        //Layout rules
        public new void AddItem(IBaseItem item)
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

        public virtual void UpdateLayout()
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
            if (style == null)
                return;
            base.SetStyle(style);
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
            SetForeground(style.Foreground);
            SetFont(style.Font);
        }
    }
}
