using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    public class DropDownList : DialogWindow
    {
        public DropDownList(String m, String t) : base(m, t) { }
        public ListBox ItemList = new ListBox();
        public ButtonCore Selection;
        public override void InitWindow()
        {
            Handler = new WindowLayout(this, "DropDownList_" + GetCount());
            Handler.SetWindowTitle(DialogTitle);
            Handler.SetPadding(2, 2, 2, 2);
            Handler.SetBackground(255, 255, 255);
            Handler.IsBorderHidden = true;
            Handler.IsAlwaysOnTop = true;
            Handler.IsOutsideClickClosable = true;
            Handler.IsCentered = false;
            Handler.IsResizeble = false;

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
                Close();
            }
        }
        public void SetCurrentIndex(int index)
        {
            ItemList.SetSelection(index);
        }
    }
}