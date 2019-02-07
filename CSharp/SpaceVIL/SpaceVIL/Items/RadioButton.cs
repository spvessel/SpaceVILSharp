using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

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

        /// <returns> RadioButton's indicator </returns>
        public Indicator GetIndicator()
        {
            return _indicator;
        }

        /// <summary>
        /// Constructs a RadioButton
        /// </summary>
        public RadioButton()
        {
            SetItemName("RadioButton_" + count);
            SetBackground(255, 255, 255, 20);
            SetSpacing(5, 0);
            EventKeyPress += OnKeyPress;
            count++;

            //text
            _text_object = new Label();
            _text_object.IsFocusable = false;
            _text_object.SetItemName(GetItemName() + "_text_object");

            //indicator
            _indicator = new CustomIndicator();
            _indicator.IsFocusable = false;

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.RadioButton)));
        }

        void OnKeyPress(object sender, KeyArgs args)
        {
            if (args.Key == KeyCode.Enter || args.Key == KeyCode.Space)
                EventMouseClick?.Invoke(this, new MouseArgs());
        }

        /// <summary>
        /// Set is mouse hover on the RadioButton
        /// </summary>
        public override void SetMouseHover(bool value)
        {
            base.SetMouseHover(value);
            _indicator.GetIndicatorMarker().SetMouseHover(IsMouseHover());
            UpdateState();
        }

        /// <summary>
        /// Initialization and adding of all elements in the RadioButton
        /// </summary>
        public override void InitElements()
        {
            //connect events
            _indicator.GetIndicatorMarker().EventToggle = null;
            EventMouseClick += (sender, args) =>
            {
                _indicator.GetIndicatorMarker().SetToggled(!_indicator.GetIndicatorMarker().IsToggled());
                if (_indicator.GetIndicatorMarker().IsToggled())
                    UncheckOthers(sender);
            };

            //adding
            AddItem(_indicator);
            AddItem(_text_object);
        }

        /// <summary>
        /// Is RadioButton checked (boolean)
        /// </summary>
        public bool IsChecked()
        {
            return _indicator.GetIndicatorMarker().IsToggled();
        }
        public void SetChecked(bool value)
        {
            _indicator.GetIndicatorMarker().SetToggled(value);
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
        /// <summary>
        /// Add item to the RadioButton
        /// </summary>
        public new void AddItem(IBaseItem item)
        {
            base.AddItem(item);
            UpdateLayout();
        }

        /// <summary>
        /// Set width of the RadioButton
        /// </summary>
        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            UpdateLayout();
        }

        /// <summary>
        /// Set X position of the RadioButton
        /// </summary>
        public override void SetX(int _x)
        {
            base.SetX(_x);
            UpdateLayout();
        }

        /// <summary>
        /// Update RadioButton's states (size and position)
        /// </summary>
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
        /// <summary>
        /// Text alignment in the RadioButton
        /// </summary>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _text_object.SetTextAlignment(alignment);
        }

        /// <summary>
        /// Text margin in the RadioButton
        /// </summary>
        public void SetTextMargin(Indents margin)
        {
            _text_object.SetMargin(margin);
        }

        /// <summary>
        /// Text font parameters in the RadioButton
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
        /// Text in the RadioButton
        /// </summary>
        public void SetText(String text)
        {
            _text_object.SetText(text);
        }
        public String GetText()
        {
            return _text_object.GetText();
        }

        /// <summary>
        /// Text color in the RadioButton
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

        /// <summary>
        /// Set style of the RadioButton
        /// </summary>
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
