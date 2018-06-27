using System;
using System.Drawing;

namespace SpaceVIL
{
    class ButtonToggle : VisualItem
    {
        private static int count = 0;
        private TextLine _text_object;

        public ButtonToggle()
        {
            SetItemName("ButtonToggle" + count);
            IsToggled = false;
            EventKeyPress += OnKeyPress;
            EventToggle += (sender) => IsToggled = !_toggled;
            count++;

            states.Add(ItemStateType.Toggled, new ItemState()
            {
                Value = true,
                Background = Color.Gray,
                Text = null,
                ImageUri = null
            });

            _text_object = new TextLine();
        }

        public ButtonToggle(String text = "") : this()
        {
            SetText(text);
        }

        protected virtual void OnKeyPress(object sender, int key, KeyMods mods)
        {
            if (key == 0x1C)
                EventMouseClick?.Invoke(this);
        }
        
        //private for class
        private bool _toggled;
        public bool IsToggled
        {
            get
            {
                return _toggled;
            }
            set
            {
                _toggled = value;
                if (value == true)
                    _state = ItemStateType.Toggled;
                else
                    _state = ItemStateType.Base;
                UpdateState();
            }
        }

        public EventMouseMethodState EventToggle;

        public override void InvokePoolEvents()
        {
            if (EventToggle != null)  EventToggle.Invoke(this);
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
        public Color GetForeground()
        {
            return _text_object.GetForeground();
        }

        public override void InitElements()
        {
            //text
            _text_object.SetAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);

            //aligment
            SetTextAlignment(ItemAlignment.HCenter | ItemAlignment.Bottom);

            //adding
            AddItem(_text_object);

            //update text data
            _text_object.UpdateData(UpdateType.Critical);
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
