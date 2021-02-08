using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// ComboBox is an item allowing to select one of the many options from the list. 
    /// <para/> Contains text, drop-down button, drop-down list. 
    /// <para/> Supports all events except drag and drop.
    /// </summary>
    public class ComboBox : Prototype
    {
        static int count = 0;
        internal ButtonCore Selection;
        internal ButtonCore DropDown;
        internal CustomShape Arrow;
        internal ComboBoxDropDown DropDownArea;

        /// <summary>
        /// Event that is invoked when one of the options is selected.
        /// </summary>
        public EventCommonMethod SelectionChanged;
        
        /// <summary>
        /// Property that allows to specify what item will be focused after drop-down list is closed.
        /// </summary>
        public Prototype ReturnFocus = null;
        
        /// <summary>
        /// Disposing ComboBox resources if it was removed.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void Release()
        {
            SelectionChanged = null;
        }

        private List<MenuItem> preItemList;

        /// <summary>
        /// Default ComboBox constructor. Options list is empty.
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
        /// <summary>
        /// Constructs ComboBox with spesified sequence of options (as SpaceVIL.MenuItem).
        /// </summary>
        /// <param name="items">Sequence of options as SpaceVIL.MenuItem.</param>
        public ComboBox(params MenuItem[] items) : this()
        {
            preItemList = new List<MenuItem>(items);
        }

        private void OnKeyPress(object sender, KeyArgs args)
        {
            if (args.Key == KeyCode.Enter)
            {
                EventMouseClick?.Invoke(this, new MouseArgs());
            }
        }

        /// <summary>
        /// Setting alignment of an ComboBox text of selected option. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Text alignment as SpaceVIL.Core.ItemAlignment.</param>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            Selection.SetTextAlignment(alignment);
        }

        /// <summary>
        /// Setting alignment of an ComboBox text of selected option. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Text alignment as sequence of SpaceVIL.Core.ItemAlignment.</param>
        public void SetTextAlignment(params ItemAlignment[] alignment)
        {
            Selection.SetTextAlignment(alignment);
        }

        /// <summary>
        /// Setting indents for the text to offset text relative to ComboBox.
        /// </summary>
        /// <param name="margin">Indents as SpaceVIL.Decorations.Indents.</param>
        public void SetTextMargin(Indents margin)
        {
            Selection.SetTextMargin(margin);
        }

        /// <summary>
        /// Setting indents for the text to offset text relative to ComboBox.
        /// </summary>
        /// <param name="left">Indent on the left.</param>
        /// <param name="top">Indent on the top.</param>
        /// <param name="right">Indent on the right.</param>
        /// <param name="bottom">Indent on the bottom.</param>
        public void SetTextMargin(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            Selection.SetTextMargin(new Indents(left, top, right, bottom));
        }

        /// <summary>
        /// Setting font of the text of selected option. 
        /// </summary>
        /// <param name="font">Font as System.Drawing.Font.</param>
        public void SetFont(Font font)
        {
            Selection.SetFont(font);
        }

        /// <summary>
        /// Setting font size of the text of selected option.
        /// </summary>
        /// <param name="size">New size of the font.</param>
        public void SetFontSize(int size)
        {
            Selection.SetFontSize(size);
        }

        /// <summary>
        /// Setting font style of the text of selected option.
        /// </summary>
        /// <param name="style">New font style as System.Drawing.FontStyle.</param>
        public void SetFontStyle(FontStyle style)
        {
            Selection.SetFontStyle(style);
        }

        /// <summary>
        /// Setting new font family of the text of selected option.
        /// </summary>
        /// <param name="fontFamily">New font family as System.Drawing.FontFamily.</param>
        public void SetFontFamily(FontFamily fontFamily)
        {
            Selection.SetFontFamily(fontFamily);
        }

        /// <summary>
        /// Getting the current font of the text of selected option.
        /// </summary>
        /// <returns>Font as System.Drawing.Font.</returns>
        public Font GetFont()
        {
            return Selection.GetFont();
        }

        /// <summary>
        /// Setting the text of selected option.
        /// </summary>
        /// <param name="text">Text as System.String.</param>
        public virtual void SetText(String text)
        {
            Selection.SetText(text);
        }

        /// <summary>
        /// Setting the text.
        /// </summary>
        /// <param name="text">Text as System.Object.</param>
        public virtual void SetText(object text)
        {
            SetText(text.ToString());
        }

        /// <summary>
        /// Getting the current text  of selected option.
        /// </summary>
        /// <returns>Text as System.String.</returns>
        public virtual String GetText()
        {
            return Selection.GetText();
        }

        /// <summary>
        /// Getting the text width (useful when you need resize ComboBox by text width).
        /// </summary>
        /// <returns>Text width.</returns>
        public int GetTextWidth()
        {
            return Selection.GetTextWidth();
        }

        /// <summary>
        /// Getting the text height (useful when you need resize ComboBox by text height).
        /// </summary>
        /// <returns>Text height.</returns>
        public int GetTextHeight()
        {
            return Selection.GetTextHeight();
        }

        /// <summary>
        /// Setting text color of selected option.
        /// </summary>
        /// <param name="color">Text color as System.Drawing.Color.</param>
        public void SetForeground(Color color)
        {
            Selection.SetForeground(color);
        }

        /// <summary>
        /// Setting text color of selected option in byte RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b)
        {
            Selection.SetForeground(r, g, b);
        }

        /// <summary>
        /// Setting text color of selected option in byte RGBA format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        /// <param name="a">Alpha color component. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b, int a)
        {
            Selection.SetForeground(r, g, b, a);
        }

        /// <summary>
        /// Setting text color of selected option in float RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b)
        {
            Selection.SetForeground(r, g, b);
        }

        /// <summary>
        /// Setting text color of selected option in float RGBA format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        /// <param name="a">Alpha color component. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b, float a)
        {
            Selection.SetForeground(r, g, b, a);
        }

        /// <summary>
        /// Getting current text color of selected option.
        /// </summary>
        /// <returns>Text color as System.Drawing.Color.</returns>
        public Color GetForeground()
        {
            return Selection.GetForeground();
        }

        /// <summary>
        /// Initializing and adding of all elements in the ComboBox 
        /// (drop-down list, drop bown button, selection, options and etc.).
        /// </summary>
        public override void InitElements()
        {
            //adding
            base.AddItem(Selection);
            base.AddItem(DropDown);
            DropDown.AddItem(Arrow);

            //DropDownArea
            ItemsLayoutBox.AddItem(GetHandler(), DropDownArea, LayoutType.Floating);
            DropDownArea.Parent = this;
            DropDownArea.SelectionChanged += OnSelectionChanged;
            if (preItemList != null)
            {
                foreach (MenuItem item in preItemList)
                {
                    DropDownArea.AddItem(item);
                }
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
        /// <summary>
        /// Opens drop-down list.
        /// </summary>
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
        /// Adding item to ComboBox. 
        /// If item is SpaceVIL.MenuItem then it is added to the drop-down list as an option.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        public override void AddItem(IBaseItem item)
        {
            if (item is MenuItem)
            {
                DropDownArea.AddItem(item);
            }
            else
            {
                base.AddItem(item);
            }
        }

        /// <summary>
        /// Removing item from ComboBox. 
        /// If item is SpaceVIL.MenuItem then it is removed from the drop-down list.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        /// <returns>True: if the removal was successful. 
        /// False: if the removal was unsuccessful.</returns>
        public override bool RemoveItem(IBaseItem item)
        {
            return DropDownArea.RemoveItem(item);
        }

        /// <summary>
        /// Selecting option by its index in the drop-down list.
        /// </summary>
        /// <param name="index">Index in the drop-down list.</param>
        public void SetCurrentIndex(int index)
        {
            Prototype currentFocus = GetHandler().GetFocusedItem();
            DropDownArea.SetCurrentIndex(index);
            currentFocus.SetFocus();
            Selection.SetText(DropDownArea.GetText());
            SelectionChanged?.Invoke();
        }

        /// <summary>
        /// Getting index of selected option in the drop-down list.
        /// </summary>
        /// <returns>Index of selected option.</returns>
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
        /// Getting current selected item in DropDown list.
        /// </summary>
        /// <returns>Current selected item as SpaceVIL.Core.IBaseItem.</returns>
        public IBaseItem GetSelectedItem()
        {
            return DropDownArea.GetSelectedItem();
        }

        /// <summary>
        /// Getting all existing options (list of SpaceVIL.IBaseItem objects).
        /// </summary>
        /// <returns>Options as List&lt;SpaceVIL.IBaseItem&gt;</returns>
        public List<IBaseItem> GetListContent()
        {
            return DropDownArea.GetListContent();
        }

        /// <summary>
        /// Setting style of the ComboBox.
        /// <para/> Inner styles: "selection", "dropdownbutton", "arrow", "dropdownarea".
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        /// </summary>
        public override void SetStyle(Style style)
        {
            if (style == null)
            {
                return;
            }

            base.SetStyle(style);

            Style innerStyle = style.GetInnerStyle("selection");
            if (innerStyle != null)
            {
                Selection.SetStyle(innerStyle);
            }

            innerStyle = style.GetInnerStyle("dropdownbutton");
            if (innerStyle != null)
            {
                DropDown.SetStyle(innerStyle);
            }

            innerStyle = style.GetInnerStyle("arrow");
            if (innerStyle != null)
            {
                Arrow.SetStyle(innerStyle);
            }

            innerStyle = style.GetInnerStyle("dropdownarea");
            if (innerStyle != null)
            {
                DropDownArea.SetStyle(innerStyle);
            }

            SetForeground(style.Foreground);
            SetFont(style.Font);
        }
    }
}
