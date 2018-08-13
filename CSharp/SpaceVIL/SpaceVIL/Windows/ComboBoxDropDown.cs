using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    public class ComboBoxDropDown : DropDown
    {
        public ListBox ItemList = new ListBox();
        public ButtonCore Selection;
        
        public ComboBoxDropDown() { }
        public override void InitWindow()
        {
            base.InitWindow();
            Handler.SetPadding(2, 2, 2, 2);
            Handler.SetBackground(255, 255, 255);
            Handler.SetWindowName("ComboBoxDropDown_" + GetCount());

            ItemList.GetArea().SetSpacing(0, 0);
            ItemList.GetArea().SetPadding(0);
            ItemList.SetBackground(Color.Transparent);
            ItemList.SetAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);
            ItemList.SetVScrollBarVisible(ScrollBarVisibility.Never);
            ItemList.SetHScrollBarVisible(ScrollBarVisibility.Never);
            ItemList.GetArea().SelectionChanged += OnSelectionChanged;

            Handler.AddItem(ItemList);
        }

        public void Add(BaseItem item)
        {
            ItemList.AddItem(item);
        }
        public void Remove(BaseItem item)
        {
            ItemList.RemoveItem(item);
        }

        private void OnSelectionChanged()
        {
            Label l = ItemList.GetListContent().ElementAt(ItemList.GetSelection()) as Label;
            if (l != null)
            {
                Selection.SetText(l.GetText());
                Handler.ResetItems();
                Close();
            }
        }
        public void SetCurrentIndex(int index)
        {
            ItemList.SetSelection(index);
        }
    }
}