using System;
using System.Collections.Generic;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// ComboBoxDropDown is drop-down list implementation for ComboBox (see SpaceVIL.ComboBox). 
    /// ComboBox do not contains ComboBoxDropDown in usual way (ComboBox.GetItems() does not 
    /// return ComboBoxDropDown), they just connected with each other. Used for selecting 
    /// option from the list. ComboBoxDropDown is a floating item (see SpaceVIL.Core.IFloating 
    /// and  enum SpaceVIL.Core.LayoutType) and closes when mouse click outside the 
    /// ComboBoxDropDown area.
    /// <para/> Contains ListBox. 
    /// <para/> Supports all events except drag and drop.
    /// <para/> Notice: All floating items render above all others items.
    /// </summary>
    public class ComboBoxDropDown : Prototype, IFloating
    {
        internal ComboBox Parent = null;

        /// <summary>
        /// Event that is invoked when one of the options is selected.
        /// </summary>
        public EventCommonMethod SelectionChanged;

        /// <summary>
        /// Disposing ComboBoxDropDown resources if it was removed.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void Release()
        {
            SelectionChanged = null;
        }

        /// <summary>
        /// ListBox for storing a list of options (SpaceVIL.MenuItem).
        /// </summary>
        public ListBox ItemList = new ListBox();
        private String _textSelection = String.Empty;

        /// <summary>
        /// Getting the text of selected option.
        /// </summary>
        /// <returns>Text of selected option.</returns>
        public String GetText()
        {
            return _textSelection;
        }

        /// <summary>
        /// Getting index of the current selected option in the list.
        /// </summary>
        /// <returns>Index of the current selected option</returns>
        public int GetCurrentIndex()
        {
            return _selectionIndexStore;
        }

        /// <summary>
        /// Getting current selected item in ItemList.
        /// </summary>
        /// <returns>Current selected item as SpaceVIL.Core.IBaseItem.</returns>
        public IBaseItem GetSelectedItem()
        {
            return ItemList.GetSelectedItem();
        }

        /// <summary>
        /// Selecting an option from the list at the specified index.
        /// </summary>
        /// <param name="index">Index of option in the list.</param>
        public void SetCurrentIndex(int index)
        {
            InitElements();

            ItemList.SetSelection(index);
            _selectionIndexStore = index;
            MenuItem selection = ItemList.GetSelectedItem() as MenuItem;
            if (selection != null)
            {
                _textSelection = selection.GetText();
            }
        }
        private List<IBaseItem> _queue = new List<IBaseItem>();

        private static int count = 0;

        /// <summary>
        /// You can specify mouse button (see SpaceVIL.Core.MouseButton) 
        /// that is used to open ComboBoxDropDown.
        /// <para/> Default: SpaceVIL.Core.MouseButton.ButtonLeft.
        /// </summary>
        public MouseButton ActiveButton = MouseButton.ButtonLeft;

        private bool _init = false;
        private bool _ouside = true;

        /// <summary>
        /// Returns True if ComboBoxDropDown (see SpaceVIL.Core.IFloating)
        /// should closes when mouse click outside the area of ComboBoxDropDown otherwise returns False.
        /// </summary>
        /// <returns>True: if ComboBoxDropDown closes when mouse click outside the area.
        /// False: if ComboBoxDropDown stays opened when mouse click outside the area.</returns>
        public bool IsOutsideClickClosable()
        {
            return _ouside;
        }

        /// <summary>
        /// Setting boolean value of item's behavior when mouse click occurs outside the ComboBoxDropDown.
        /// </summary>
        /// <param name="value">True: ComboBoxDropDown should become invisible if mouse click occurs outside the item.
        /// False: an item should stay visible if mouse click occurs outside the item.</param>
        public void SetOutsideClickClosable(bool value)
        {
            _ouside = value;
        }

        /// <summary>
        /// Default ComboBoxDropDown constructor. 
        /// ComboBoxDropDown does not pass any input events and invisible by default.
        /// </summary>
        public ComboBoxDropDown()
        {
            SetItemName("ComboBoxDropDown_" + count++);
            SetPassEvents(false);
            SetVisible(false);
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.ComboBoxDropDown)));
        }

        private EventMouseMethodState linkEventScrollUp = null;
        private EventMouseMethodState linkEventScrollDown = null;
        private EventMouseMethodState linkEventMouseClick = null;
        private EventKeyMethodState linkEventKeyPress = null;

        private void DisableAdditionalControls()
        {
            ItemList.SetVScrollBarPolicy(VisibilityPolicy.Never);
            ItemList.SetHScrollBarPolicy(VisibilityPolicy.Never);
            ItemList.EventScrollUp = null;
            ItemList.EventScrollDown = null;
            ItemList.EventMouseClick = null;
            ItemList.EventKeyPress = null;
        }

        private void EnableAdditionalControls()
        {
            ItemList.SetVScrollBarPolicy(VisibilityPolicy.AsNeeded);
            ItemList.SetHScrollBarPolicy(VisibilityPolicy.AsNeeded);
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

        private int _selectionIndexStore = -1;

        /// <summary>
        /// Initializing all elements in the ComboBoxDropDown. 
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            if (!_init)
            {
                ItemList.SetMenuDisabled(true);
                base.AddItem(ItemList);
                SaveAdditionalControls();
                DisableAdditionalControls();
                ItemList.GetArea().EventMouseClick += (sender, args) =>
                {
                    if (ItemList.GetSelection() != _selectionIndexStore)
                    {
                        _selectionIndexStore = ItemList.GetSelection();
                        OnSelectionChanged();
                    }
                };
                ItemList.GetArea().EventKeyPress += (sender, args) =>
                {
                    if (args.Key == KeyCode.Escape)
                    {
                        Hide();
                    }
                    else if (args.Key == KeyCode.Enter && args.Mods == 0)
                    {
                        OnSelectionChanged();
                    }
                };
                foreach (var item in _queue)
                {
                    ItemList.AddItem(item);
                }
                _queue = null;
                _init = true;
            }
            UpdateSize();
        }

        private void OnSelectionChanged()
        {
            MenuItem selection = ItemList.GetSelectedItem() as MenuItem;
            if (selection != null)
            {
                _textSelection = selection.GetText();
            }
            Hide();
            SelectionChanged?.Invoke();
        }

        /// <summary>
        /// Getting number of options in the list.
        /// </summary>
        /// <returns>Number of options in the list.</returns>
        public int GetListCount()
        {
            return ItemList.GetListContent().Count;
        }

        /// <summary>
        /// Getting all existing options (list of SpaceVIL.IBaseItem objects).
        /// </summary>
        /// <returns>Options as List&lt;SpaceVIL.IBaseItem&gt;</returns>
        public List<IBaseItem> GetListContent()
        {
            return ItemList.GetListContent();
        }

        /// <summary>
        /// Adding option (or any SpaceVIL.Core.IBaseItem implementation) to the ComboBoxDropDown.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        public override void AddItem(IBaseItem item)
        {
            if (_init)
            {
                ItemList.AddItem(item);
            }
            else
            {
                _queue.Add(item);
            }
        }

        /// <summary>
        /// Removing option (or any SpaceVIL.Core.IBaseItem implementation) from the ComboBoxDropDown.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        /// <returns>True: if the removal was successful. 
        /// False: if the removal was unsuccessful.</returns>
        public override bool RemoveItem(IBaseItem item)
        {
            return ItemList.RemoveItem(item);
        }

        private void UpdateSize()
        {
            int height = ItemList.GetPadding().Top + ItemList.GetPadding().Bottom;
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
                {
                    tmp = tmp + item.GetWidth() + item.GetMargin().Left + item.GetMargin().Right;
                }

                if (width < tmp)
                {
                    width = tmp;
                }
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
        /// Shows the ComboBoxDropDown at the proper position.
        /// </summary>
        /// <param name="sender"> The item from which the show request is sent. </param>
        /// <param name="args"> Mouse click arguments (cursor position, mouse button,
        /// mouse button press/release, etc.). </param>
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
        /// <summary>
        /// Shows the ComboBoxDropDown at the position (0, 0).
        /// </summary>
        public void Show()
        {
            MouseArgs args = new MouseArgs();
            args.Button = ActiveButton;
            Show(this, args);
        }

        /// <summary>
        /// Hide the ComboBoxDropDown without destroying.
        /// </summary>
        public void Hide()
        {
            SetVisible(false);
            ItemList.Unselect();

            if (Parent.ReturnFocus != null)
            {
                Parent.SetFocus();
            }
            else
            {
                GetHandler().ResetFocus();
            }
        }
        /// <summary>
        /// Hide the ComboBoxDropDown without destroying with using specified mouse arguments.
        /// </summary>
        /// <param name="args">Arguments as SpaceVIL.Core.MouseArgs.</param>
        public void Hide(MouseArgs args)
        {
            if (!IsVisible())
            {
                return;
            }
            Hide();
            Parent.IsDropDownAreaOutsideClicked(args);
        }


        /// <summary>
        /// Overridden method for setting confines according 
        /// to position and size of the ComboBoxDropDown (see Prototype.SetConfines()).
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

        /// <summary>
        /// Setting style of the ComboBoxDropDown.
        /// <para/> Inner styles: "itemlist".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public override void SetStyle(Style style)
        {
            if (style == null)
            {
                return;
            }
            base.SetStyle(style);
            Style innerStyle = style.GetInnerStyle("itemlist");
            if (innerStyle != null)
            {
                ItemList.SetStyle(innerStyle);
            }
        }
    }
}