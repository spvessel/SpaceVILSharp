using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class ComboBox : Prototype
    {
        static int count = 0;
        public ButtonCore Selection = new ButtonCore();
        public ButtonCore DropDown = new ButtonCore();
        public CustomShape Arrow;
        public ComboBoxDropDown DropDownArea;
        public EventCommonMethod SelectionChanged;

        private List<MenuItem> preItemList;

        /// <summary>
        /// Constructs a ComboBox
        /// </summary>
        public ComboBox()
        {
            SetItemName("ComboBox_" + count);
            count++;

            EventKeyPress += OnKeyPress;
            EventMousePress += (sender, args) => ShowDropDownList();

            Arrow = new CustomShape();
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.ComboBox)));
        }

        public ComboBox(params MenuItem[] items) : this()
        {
            preItemList = new List<MenuItem>(items);
        }

        void OnKeyPress(object sender, KeyArgs args)
        {
            if (args.Key == KeyCode.Enter)
                EventMouseClick?.Invoke(this, new MouseArgs());
        }

        //text init
        /// <summary>
        /// Text alignment in the ComboBox
        /// </summary>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            Selection.SetTextAlignment(alignment);
        }

        /// <summary>
        /// Text margin in the ComboBox
        /// </summary>
        public void SetTextMargin(Indents margin)
        {
            Selection.SetMargin(margin);
        }

        /// <summary>
        /// Text font parameters in the ComboBox
        /// </summary>
        public void SetFont(Font font)
        {
            Selection.SetFont(font);
        }
        public void SetFontSize(int size)
        {
            Selection.SetFontSize(size);
        }
        public void SetFontStyle(FontStyle style)
        {
            Selection.SetFontStyle(style);
        }
        public void SetFontFamily(FontFamily font_family)
        {
            Selection.SetFontFamily(font_family);
        }
        public Font GetFont()
        {
            return Selection.GetFont();
        }

        /// <summary>
        /// Set text in the ComboBox
        /// </summary>
        public void SetText(String text)
        {
            Selection.SetText(text);
        }
        public String GetText()
        {
            return Selection.GetText();
        }

        /// <summary>
        /// Text color in the ComboBox
        /// </summary>
        public void SetForeground(Color color)
        {
            Selection.SetForeground(color);
        }
        public void SetForeground(int r, int g, int b)
        {
            Selection.SetForeground(r, g, b);
        }
        public void SetForeground(int r, int g, int b, int a)
        {
            Selection.SetForeground(r, g, b, a);
        }
        public void SetForeground(float r, float g, float b)
        {
            Selection.SetForeground(r, g, b);
        }
        public void SetForeground(float r, float g, float b, float a)
        {
            Selection.SetForeground(r, g, b, a);
        }
        public Color GetForeground()
        {
            return Selection.GetForeground();
        }

        /// <summary>
        /// Initialization and adding of all elements in the ComboBox
        /// </summary>
        public override void InitElements()
        {
            //adding
            base.AddItem(Selection);
            base.AddItem(DropDown);
            DropDown.AddItem(Arrow);

            //DropDownArea
            DropDownArea = new ComboBoxDropDown(GetHandler());
            DropDownArea.SetOutsideClickClosable(true);
            DropDownArea.SelectionChanged += OnSelectionChanged;
            if (preItemList != null)
            {
                foreach (MenuItem item in preItemList)
                    DropDownArea.AddItem(item);
                preItemList = null;
            }
        }

        private void ShowDropDownList()
        {
            //DropDownArea
            DropDownArea.SetPosition(GetX(), GetY() + GetHeight());
            DropDownArea.SetWidth(Selection.GetWidth());
            MouseArgs args = new MouseArgs();
            args.Button = MouseButton.ButtonLeft;
            DropDownArea.Show(this, args);
        }

        /// <summary>
        /// Add item to ComboBox list
        /// </summary>
        public override void AddItem(IBaseItem item)
        {
            DropDownArea.AddItem(item);
        }

        /// <summary>
        /// Remove item from the ComboBox list
        /// </summary>
        public override void RemoveItem(IBaseItem item)
        {
            DropDownArea.RemoveItem(item);
        }

        /// <summary>
        /// Current element in the ComboBox by index
        /// </summary>
        public void SetCurrentIndex(int index)
        {
            DropDownArea.SetCurrentIndex(index);
            Selection.SetText(DropDownArea.GetText());
            SelectionChanged?.Invoke();
        }
        public int GetCurrentIndex()
        {
            return DropDownArea.GetCurrentIndex();
        }
        private void OnSelectionChanged()
        {
            Selection.SetText(DropDownArea.GetText());
            SelectionChanged?.Invoke();
        }

        /// <summary>
        /// Set style of the ComboBox
        /// </summary>
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
                Selection.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("dropdownbutton");
            if (inner_style != null)
            {
                DropDown.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("arrow");
            if (inner_style != null)
            {
                Arrow.SetStyle(inner_style);
            }
        }
    }
}
