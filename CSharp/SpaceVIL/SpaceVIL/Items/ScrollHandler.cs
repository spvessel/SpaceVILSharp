using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    class ScrollHandler : VisualItem, IDraggable, IScrollable
    {
        static int count = 0;
        public Orientation Orientation;
        private int _offset = 0;

        public ScrollHandler()
        {
            SetItemName("ScrollHandler" + count);
            EventMouseClick += EmptyEvent;
            EventMouseDrag += OnDragging;
            EventMouseHover += (sender) => IsMouseHover = !IsMouseHover;
            count++;
        }

        public override void InvokePoolEvents()
        {
            //if (EventMouseClick != null) EventMouseClick.Invoke(this);
            if (EventMouseDrag != null) EventMouseDrag.Invoke(this);
            if (EventMouseDrop != null) EventMouseDrop.Invoke(this);
        }

        public void OnDragging(object sender)
        {
            int parent_crd, parent_size, item_size, offset;

            if (Orientation == Orientation.Horizontal)
            {
                offset = _mouse_ptr.X - _mouse_ptr.PrevX + _offset;
                parent_crd = GetParent().GetX();
                parent_size = GetParent().GetWidth();
                item_size = GetWidth();
            }
            else
            {
                offset = _mouse_ptr.Y - _mouse_ptr.PrevY + _offset;
                parent_crd = GetParent().GetY();
                parent_size = GetParent().GetHeight();
                item_size = GetHeight();
            }
            if (offset + parent_crd < parent_crd)
                offset = 0;

            if (offset + parent_crd > parent_crd + parent_size - item_size)
                offset = parent_size - item_size;

            SetOffset(offset);
        }

        public void SetOffset(int offset)
        {
            if (GetParent() == null)
                return;

            _offset = offset;

            if (Orientation == Orientation.Horizontal)
                SetX(_offset + GetParent().GetX());
            else
                SetY(_offset + GetParent().GetY());
        }

        public void InvokeScrollUp()
        {
            (GetParent() as IScrollable)?.InvokeScrollUp();
        }

        public void InvokeScrollDown()
        {
            (GetParent() as IScrollable)?.InvokeScrollDown();
        }
    }
}
