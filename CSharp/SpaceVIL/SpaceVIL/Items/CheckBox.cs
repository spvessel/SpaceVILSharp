using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Threading.Tasks;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class CheckBox : Prototype
    {
        internal class CustomIndicator : Indicator
        {
            internal override bool GetHoverVerification(float xpos, float ypos)
            {
                return false;
            }
        }

        static int count = 0;
        private TextLine _text_object;
        //private bool _toggled = false;
        private CustomIndicator _indicator;

        /// <returns> indicator from the CheckBox for styling </returns>
        public Indicator GetIndicator()
        {
            return _indicator;
        }

        /// <summary>
        /// Constructs a CheckBox
        /// </summary>
        public CheckBox()
        {
            SetItemName("CheckBox_" + count);
            count++;
            EventKeyPress += OnKeyPress;

            //text
            _text_object = new TextLine();
            _text_object.SetItemName(GetItemName() + "_text_object");

            //indicator
            _indicator = new CustomIndicator();
            _indicator.IsFocusable = false;

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.CheckBox)));
        }

        void OnKeyPress(object sender, KeyArgs args)
        {
            if (args.Key == KeyCode.Enter || args.Key == KeyCode.Space)
                EventMouseClick?.Invoke(this, new MouseArgs());
        }

        /// <summary>
        /// Is mouse hovered on the CheckBox
        /// </summary>
        public override void SetMouseHover(bool value)
        {
            base.SetMouseHover(value);
            _indicator.GetIndicatorMarker().SetMouseHover(IsMouseHover());
            UpdateState();
        }

        /// <summary>
        /// Initialization and adding of all elements in the CheckBox
        /// </summary>
        public override void InitElements()
        {
            //events
            _indicator.GetIndicatorMarker().EventToggle = null;
            EventMouseClick += (sender, args) => _indicator.GetIndicatorMarker().SetToggled(!_indicator.GetIndicatorMarker().IsToggled());

            //adding
            AddItems(_indicator, _text_object);
        }

        /// <summary>
        /// Is CheckBox checked (boolean)
        /// </summary>
        public bool IsChecked()
        {
            return _indicator.GetIndicatorMarker().IsToggled();
        }
        public void SetChecked(bool value)
        {
            _indicator.GetIndicatorMarker().SetToggled(value);
        }

        //text init
        /// <summary>
        /// Text alignment in the CheckBox
        /// </summary>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _text_object.SetTextAlignment(alignment);
        }

        /// <summary>
        /// Text margin in the CheckBox
        /// </summary>
        public void SetTextMargin(Indents margin)
        {
            _text_object.SetMargin(margin);
        }

        /// <summary>
        /// Text font parameters in the CheckBox
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
        /// Set text in the CheckBox
        /// </summary>
        public void SetText(String text)
        {
            _text_object.SetItemText(text);
        }
        public String GetText()
        {
            return _text_object.GetText();
        }

        /// <summary>
        /// Text color in the CheckBox
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

        //style
        /// <summary>
        /// Set style of the CheckBox
        /// </summary>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
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
