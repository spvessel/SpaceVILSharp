using System;
using System.Collections.Generic;
using System.Drawing;

namespace SpaceVIL
{
    public class ComboBox : VisualItem
    {
        // Queue<BaseItem> _queue = new Queue<BaseItem>();

        static int count = 0;
        public ButtonCore _selected = new ButtonCore();
        public ButtonCore _dropdown = new ButtonCore();
        public CustomShape _arrow = new CustomShape();
        public ComboBoxDropDown _dropdownarea = new ComboBoxDropDown();
        public EventCommonMethod SelectionChanged;

        public ComboBox()
        {
            SetBackground(Color.Transparent);
            SetItemName("ComboBox_" + count);
            count++;

            EventKeyPress += OnKeyPress;
            EventMousePressed += (sender, args) => ShowDropDownList();

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.ComboBox)));
        }

        protected virtual void OnKeyPress(object sender, KeyArgs args)
        {
            if (args.Scancode == 0x1C)
                EventMouseClick?.Invoke(this, new MouseArgs());
        }

        //text init
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _selected.SetTextAlignment(alignment);
        }
        public void SetTextMargin(Indents margin)
        {
            _selected.SetMargin(margin);
        }
        public void SetFont(Font font)
        {
            _selected.SetFont(font);
        }
        public void SetFontSize(int size)
        {
            _selected.SetFontSize(size);
        }
        public void SetFontStyle(FontStyle style)
        {
            _selected.SetFontStyle(style);
        }
        public void SetFontFamily(FontFamily font_family)
        {
            _selected.SetFontFamily(font_family);
        }
        public Font GetFont()
        {
            return _selected.GetFont();
        }
        public void SetText(String text)
        {
            _selected.SetText(text);
        }
        public String GetText()
        {
            return _selected.GetText();
        }
        public void SetForeground(Color color)
        {
            _selected.SetForeground(color);
        }
        public void SetForeground(int r, int g, int b)
        {
            _selected.SetForeground(r, g, b);
        }
        public void SetForeground(int r, int g, int b, int a)
        {
            _selected.SetForeground(r, g, b, a);
        }
        public void SetForeground(float r, float g, float b)
        {
            _selected.SetForeground(r, g, b);
        }
        public void SetForeground(float r, float g, float b, float a)
        {
            _selected.SetForeground(r, g, b, a);
        }
        public Color GetForeground()
        {
            return _selected.GetForeground();
        }

        public override void InitElements()
        {
            //adding
            AddItems(_selected, _dropdown);

            _dropdown.AddItem(_arrow);
            // _selected.SetTextAlignment(ItemAlignment.Left | ItemAlignment.VCenter);

            //dropdownarea
            _dropdownarea.Selection = _selected;
            _dropdownarea.SelectionChanged += OnSelectionChanged;
        }

        private void ShowDropDownList()
        {
            //dropdownarea
            _dropdownarea.Handler.SetWidth(_selected.GetWidth());
            _dropdownarea.Handler.SetHeight(100);

            _dropdownarea.Handler.SetX(GetHandler().GetX() + _selected.GetX());
            _dropdownarea.Handler.SetY(GetHandler().GetY() + _selected.GetY() + _selected.GetHeight());

            Console.WriteLine(_dropdownarea.Handler.GetX() + " " + _dropdownarea.Handler.GetY());

            _dropdownarea.Show();
        }

        public void AddToList(BaseItem item)
        {
            _dropdownarea.Add(item);
            // _queue.Enqueue(item);
        }
        public void RemoveFromLst(BaseItem item)
        {
            _dropdownarea.Remove(item);
        }
        public void SetCurrentIndex(int index)
        {
            _dropdownarea.SetCurrentIndex(index);
        }
        public int GetCurrentIndex()
        {
            return _dropdownarea.GetCurrentIndex();
        }
        private void OnSelectionChanged()
        {
            SelectionChanged?.Invoke();
        }

        public override void SetStyle(Style style)
        {
            if (style == null)
                return;

            base.SetStyle(style);
            SetForeground(style.Foreground);
            SetFont(style.Font);

            Style inner_style = style.GetInnerStyle("selection");
            if (inner_style != null)
            {
                _selected.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("dropdownbutton");
            if (inner_style != null)
            {
                _dropdown.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("arrow");
            if (inner_style != null)
            {
                _arrow.SetStyle(inner_style);
            }
        }
    }
}