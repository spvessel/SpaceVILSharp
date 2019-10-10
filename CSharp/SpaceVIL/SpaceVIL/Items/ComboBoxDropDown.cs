using System;
using System.Drawing;
using System.Collections.Generic;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class ComboBoxDropDown : Prototype, IFloating
    {
        internal ComboBox Parent = null;
        public EventCommonMethod SelectionChanged;
        public override void Release()
        {
            SelectionChanged = null;
        }

        public Prototype _returnFocus = null;
        public void SetReturnFocus(Prototype item)
        {
            _returnFocus = item;
        }
        public Prototype GetReturnFocusItem()
        {
            return _returnFocus;
        }

        public ListBox ItemList = new ListBox();
        private String _textSelection = String.Empty;
        public String GetText()
        {
            return _textSelection;
        }
        public int GetCurrentIndex()
        {
            return ItemList.GetSelection();
        }
        public void SetCurrentIndex(int index)
        {
            InitElements();

            ItemList.SetSelection(index);
            MenuItem selection = ItemList.GetSelectedItem() as MenuItem;
            if (selection != null)
                _textSelection = selection.GetText();
        }
        private List<IBaseItem> _queue = new List<IBaseItem>();

        private static int count = 0;
        public MouseButton ActiveButton = MouseButton.ButtonLeft;

        private bool _init = false;
        private bool _ouside = true;

        /// <summary>
        /// Close the ComboBoxDropDown it mouse click is outside (true or false)
        /// </summary>
        public bool IsOutsideClickClosable()
        {
            return _ouside;
        }
        public void SetOutsideClickClosable(bool value)
        {
            _ouside = value;
        }

        /// <summary>
        /// Constructs a ComboBoxDropDown
        /// </summary>
        /// <param name="handler"> parent window for the ComboBoxDropDown </param>
        internal ComboBoxDropDown()
        {
            SetItemName("ComboBoxDropDown_" + count++);
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.ComboBoxDropDown)));
            SetPassEvents(false);
            SetVisible(false);
        }

        private EventMouseMethodState linkEventScrollUp = null;
        private EventMouseMethodState linkEventScrollDown = null;
        private EventMouseMethodState linkEventMouseClick = null;
        private EventKeyMethodState linkEventKeyPress = null;

        private void DisableAdditionalControls()
        {
            ItemList.SetVScrollBarVisible(ScrollBarVisibility.Never);
            ItemList.SetHScrollBarVisible(ScrollBarVisibility.Never);
            ItemList.EventScrollUp = null;
            ItemList.EventScrollDown = null;
            ItemList.EventMouseClick = null;
            ItemList.EventKeyPress = null;
        }

        private void EnableAdditionalControls()
        {
            ItemList.SetVScrollBarVisible(ScrollBarVisibility.AsNeeded);
            ItemList.SetHScrollBarVisible(ScrollBarVisibility.AsNeeded);
            ItemList.EventScrollUp = linkEventScrollUp;
            ItemList.EventScrollDown = linkEventScrollDown;
            ItemList.EventMouseClick = linkEventMouseClick;
            ItemList.EventKeyPress = linkEventKeyPress;
        }

        private void SaveAdditionalControls()
        {
            linkEventScrollUp = ItemList.EventScrollUp;
            linkEventScrollDown = ItemList.EventScrollDown;
            linkEventMouseClick = ItemList.EventMouseClick;
            linkEventKeyPress = ItemList.EventKeyPress;
        }

        /// <summary>
        /// Initialization and adding of all elements in the ComboBoxDropDown
        /// </summary>
        public override void InitElements()
        {
            if (!_init)
            {
                ItemList.DisableMenu(true);
                base.AddItem(ItemList);
                SaveAdditionalControls();
                DisableAdditionalControls();
                ItemList.GetArea().SelectionChanged += OnSelectionChanged;
                ItemList.GetArea().EventKeyPress += (sender, args) =>
                {
                    if (args.Key == KeyCode.Escape)
                        Hide();
                };
                foreach (var item in _queue)
                    ItemList.AddItem(item);
                _queue = null;
                _init = true;
            }
            UpdateSize();
        }

        private void OnSelectionChanged()
        {
            MenuItem selection = ItemList.GetSelectedItem() as MenuItem;
            if (selection != null)
                _textSelection = selection.GetText();
            Hide();
            SelectionChanged?.Invoke();
        }

        /// <summary>
        /// Returns count of the ComboBoxDropDown lines
        /// </summary>
        public int GetListCount()
        {
            return ItemList.GetListContent().Count;
        }

        /// <summary>
        /// Returns ComboBoxDropDown items list
        /// </summary>
        public List<IBaseItem> GetListContent()
        {
            return ItemList.GetListContent();
        }

        /// <summary>
        /// Add item to the ComboBoxDropDown
        /// </summary>
        public override void AddItem(IBaseItem item)
        {
            if (_init)
                ItemList.AddItem(item);
            else
                _queue.Add(item);
        }

        /// <summary>
        /// Remove item from the ComboBoxDropDown
        /// </summary>
        public override bool RemoveItem(IBaseItem item)
        {
            return ItemList.RemoveItem(item);
        }

        void UpdateSize()
        {
            int height = 0;// ItemList.GetPadding().Top + ItemList.GetPadding().Bottom;
            int width = GetWidth();
            List<IBaseItem> list = ItemList.GetListContent();
            foreach (var item in list)
            {
                IBaseItem wrapper = ItemList.GetWrapper(item);
                height += (wrapper.GetHeight() + ItemList.GetArea().GetSpacing().Vertical);

                int tmp = GetPadding().Left + GetPadding().Right + item.GetMargin().Left + item.GetMargin().Right;

                MenuItem m = item as MenuItem;
                if (m != null)
                {
                    tmp += m.GetTextWidth() + m.GetMargin().Left + m.GetMargin().Right + m.GetPadding().Left + m.GetPadding().Right;
                }
                else
                    tmp = tmp + item.GetWidth() + item.GetMargin().Left + item.GetMargin().Right;

                if (width < tmp)
                    width = tmp;
            }
            if ((GetY() + height) > GetHandler().GetHeight())
            {
                EnableAdditionalControls();
                SetHeight(GetHandler().GetHeight() - GetY() - 10);
            }
            else
            {
                DisableAdditionalControls();
                SetHeight(height);
                ItemList.VScrollBar.Slider.SetCurrentValue(ItemList.VScrollBar.Slider.GetMinValue());
            }
            SetWidth(width);
        }

        /// <summary>
        /// Show the ComboBoxDropDown
        /// </summary>
        /// <param name="sender"> the item from which the show request is sent </param>
        /// <param name="args"> mouse click arguments (cursor position, mouse button,
        /// mouse button press/release, etc.) </param>
        public void Show(IItem sender, MouseArgs args)
        {
            if (args.Button == ActiveButton)
            {
                InitElements();
                SetVisible(true);
                SetConfines();
                ItemList.GetArea().SetFocus();
            }
        }
        public void Show()
        {
            MouseArgs args = new MouseArgs();
            args.Button = ActiveButton;
            Show(this, args);
        }

        /// <summary>
        /// Hide the ComboBoxDropDown without destroying
        /// </summary>
        public void Hide()
        {
            SetVisible(false);
            ItemList.Unselect();
            _returnFocus?.SetFocus();
        }

        public void Hide(MouseArgs args)
        {
            if (!IsVisible())
                return;

            Hide();
            Parent.IsDropDownAreaOutsideClicked(args);
        }


        /// <summary>
        /// Set confines according to position and size of the ComboBoxDropDown
        /// </summary>
        public override void SetConfines()
        {
            base.SetConfines(
                GetX(),
                GetX() + GetWidth(),
                GetY(),
                GetY() + GetHeight()
            );
        }

        //style
        /// <summary>
        /// Set style of the ComboBoxDropDown
        /// </summary>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
            Style inner_style = style.GetInnerStyle("itemlist");
            if (inner_style != null)
            {
                ItemList.SetStyle(inner_style);
            }
        }
    }
}
