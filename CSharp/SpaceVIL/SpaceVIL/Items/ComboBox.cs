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
        internal ButtonCore Selection;
        internal ButtonCore DropDown;
        internal CustomShape Arrow;
        internal ComboBoxDropDown DropDownArea;
        public EventCommonMethod SelectionChanged;
        public Prototype ReturnFocus = null;

        public override void Release()
        {
            SelectionChanged = null;
        }

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

            Selection = new ButtonCore();
            DropDown = new ButtonCore();
            Arrow = new CustomShape();
            DropDownArea = new ComboBoxDropDown();

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
            Selection.SetTextMargin(margin);
        }

        public void SetTextMargin(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            Selection.SetTextMargin(new Indents(left, top, right, bottom));
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
            // DropDownArea = new ComboBoxDropDown(GetHandler());
            ItemsLayoutBox.AddItem(GetHandler(), DropDownArea, LayoutType.Floating);
            DropDownArea.Parent = this;
            DropDownArea.SelectionChanged += OnSelectionChanged;
            if (preItemList != null)
            {
                foreach (MenuItem item in preItemList)
                    DropDownArea.AddItem(item);
                preItemList = null;
            }
        }

        internal bool IsOpened = false;

        private void ShowDropDownList()
        {
            if (IsOpened)
            {
                IsOpened = false;
            }
            else
            {
                DropDownArea.SetPosition(GetX(), GetY() + GetHeight());
                DropDownArea.SetWidth(Selection.GetWidth());
                MouseArgs args = new MouseArgs();
                args.Button = MouseButton.ButtonLeft;
                DropDownArea.Show(this, args);
            }
        }

        public void Open()
        {
            ShowDropDownList();
        }

        internal void IsDropDownAreaOutsideClicked(MouseArgs args)
        {
            if (GetHoverVerification(args.Position.GetX(), args.Position.GetY()))
            {
                IsOpened = true;
            }
        }

        /// <summary>
        /// Add item to ComboBox list
        /// </summary>
        public override void AddItem(IBaseItem item)
        {
            if (item is MenuItem)
                DropDownArea.AddItem(item);
            else
                base.AddItem(item);
        }

        /// <summary>
        /// Remove item from the ComboBox list
        /// </summary>
        public override bool RemoveItem(IBaseItem item)
        {
            return DropDownArea.RemoveItem(item);
        }

        /// <summary>
        /// Current element in the ComboBox by index
        /// </summary>
        public void SetCurrentIndex(int index)
        {
            Prototype currentFocus = GetHandler().GetFocusedItem();
            DropDownArea.SetCurrentIndex(index);
            currentFocus.SetFocus();
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

            Style innerStyle = style.GetInnerStyle("selection");
            if (innerStyle != null)
                Selection.SetStyle(innerStyle);

            innerStyle = style.GetInnerStyle("dropdownbutton");
            if (innerStyle != null)
                DropDown.SetStyle(innerStyle);

            innerStyle = style.GetInnerStyle("arrow");
            if (innerStyle != null)
                Arrow.SetStyle(innerStyle);

            innerStyle = style.GetInnerStyle("dropdownarea");
            if (innerStyle != null)
                DropDownArea.SetStyle(innerStyle);

            SetForeground(style.Foreground);
            SetFont(style.Font);
        }
    }
}
