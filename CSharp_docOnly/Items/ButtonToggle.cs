using System;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class ButtonToggle : Prototype
    {
        private static int count = 0;
        private TextLine _text_object;

        /// <summary>
        /// Constructs a ButtonToggle
        /// </summary>
        public ButtonToggle()
        {
            SetItemName("ButtonToggle_" + count);
            SetToggled(false);
            EventKeyPress += OnKeyPress;
            EventMouseClick += (sender, args) => EventToggle?.Invoke(sender, args); //remember
            EventToggle += (sender, args) => SetToggled(!_toggled);
            count++;
            _text_object = new TextLine();

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.ButtonToggle)));
        }

        /// <summary>
        /// Constructs a ButtonToggle with text
        /// </summary>
        public ButtonToggle(String text = "") : this()
        {
            SetText(text);
        }
        
        void OnKeyPress(object sender, KeyArgs args)
        {
            if (args.Key == KeyCode.Enter)
                EventMouseClick?.Invoke(this, new MouseArgs());
        }

        //private for class
        private bool _toggled;

        /// <summary>
        /// Is ButtonToggle toggled (boolean)
        /// </summary>
        public bool IsToggled()
        {
            return _toggled;
        }
        public void SetToggled(bool value)
        {
            _toggled = value;
            if (value == true)
                SetState(ItemStateType.Toggled);
            else
                SetState(ItemStateType.Base);
        }

        public EventMouseMethodState EventToggle;

        //text init
        /// <summary>
        /// Text alignment in the ButtonToggle
        /// </summary>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _text_object.SetTextAlignment(alignment);
        }

        /// <summary>
        /// Text margin in the ButtonToggle
        /// </summary>
        public void SetTextMargin(Indents margin)
        {
            _text_object.SetMargin(margin);
        }

        /// <summary>
        /// Text font parameters in the ButtonToggle
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
        /// Set text in the ButtonToggle
        /// </summary>
        public void SetText(String text)
        {
            _text_object.SetItemText(text);
        }
        public String GetText()
        {
            return _text_object.GetItemText();
        }

        /// <summary>
        /// Text color in the ButtonToggle
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
        /// Initialization and adding of all elements in the ButtonToggle
        /// </summary>
        public override void InitElements()
        {
            //text
            _text_object.SetAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);

            //aligment
            SetTextAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);

            //adding
            AddItem(_text_object);

            //update text data
            //_text_object.UpdateData(UpdateType.Critical);
        }

        //style
        /// <summary>
        /// Set style of the ButtonToggle
        /// </summary>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
            SetForeground(style.Foreground);
            SetFont(style.Font);
            SetTextAlignment(style.TextAlignment);
        }
    }
}
