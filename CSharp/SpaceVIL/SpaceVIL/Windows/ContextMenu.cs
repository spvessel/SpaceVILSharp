using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    public class ContextMenu : DropDown
    {
        public ListBox ItemList = new ListBox();

        public ContextMenu() { }

        public override void InitWindow()
        {
            base.InitWindow();

            Handler.SetPadding(2, 2, 2, 2);
            Handler.SetBackground(255, 255, 255);
            Handler.SetWindowName("ContexMenu_" + GetCount());

            ItemList.SetSelectionVisibility(false);
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

        protected virtual void OnSelectionChanged()
        {
            Handler.ResetItems();
            Close();
        }
        public void SetCurrentIndex(int index)
        {
            ItemList.SetSelection(index);
        }
        public int GetCurrentIndex()
        {
            return ItemList.GetSelection();
        }
        public int GetListCount()
        {
            return ItemList.GetListContent().Count;
        }
        public List<BaseItem> GetListContent()
        {
            return ItemList.GetListContent();
        }
    }
}