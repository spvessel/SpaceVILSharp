using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    public class ComboBoxDropDown : DialogWindow, IDisposable
    {
        public ListBox ItemList; // = new ListBox();
        public ButtonCore Selection;
        public EventCommonMethod SelectionChanged;

        public ComboBoxDropDown()
        {
        }

        public override void InitWindow()
        {
            ItemList = new ListBox();
            Handler = new WindowLayout(this, "DropDown_" + GetCount(), "DropDown_" + GetCount());
            Handler.IsOutsideClickClosable = true;
            Handler.IsBorderHidden = true;
            Handler.IsAlwaysOnTop = true;
            Handler.IsCentered = false;
            Handler.IsResizeble = false;

            ItemList.SetVScrollBarVisible(ScrollBarVisibility.Never);
            ItemList.SetHScrollBarVisible(ScrollBarVisibility.Never);
            ItemList.GetArea().SelectionChanged += OnSelectionChanged;

            Handler.AddItem(ItemList);

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.ComboBoxDropDown)));
        }

        public void Add(IBaseItem item)
        {
            ItemList.AddItem(item);
        }
        public void Remove(IBaseItem item)
        {
            ItemList.RemoveItem(item);
        }

        private void OnSelectionChanged()
        {
            MenuItem l = ItemList.GetListContent().ElementAt(ItemList.GetSelection()) as MenuItem;
            if (l != null)
            {
                Selection.SetText(l.GetText());
                Handler.ResetItems();
                Close();
                SelectionChanged?.Invoke();
            }
        }
        public void SetCurrentIndex(int index)
        {
            ItemList.SetSelection(index);
        }
        public int GetCurrentIndex()
        {
            return ItemList.GetSelection();
        }

        public override void Show()
        {
            base.Show();
            //WindowLayoutBox.SetFocusedWindow(this);
        }

        // public override void Close()
        // {
        //     Handler.SetHidden(true);
        // }

        // public void Unhide()
        // {
        //     Handler.SetHidden(false);
        // }

        public void Dispose()
        {
            Handler.Close();
        }

        public void SetStyle(Style style)
        {
            if (style == null)
                return;
                
            Handler.SetBackground(style.Background);
            Handler.SetPadding(style.Padding);

            Style itemlist_style = style.GetInnerStyle("itemlist");
            if(itemlist_style != null)
            {
                ItemList.SetBackground(itemlist_style.Background);
                ItemList.SetAlignment(itemlist_style.Alignment);
            }
        }
    }
}