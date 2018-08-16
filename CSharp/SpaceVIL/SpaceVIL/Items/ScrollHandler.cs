using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    public class ScrollHandler : VisualItem, IDraggable, IScrollable
    {
        static int count = 0;
        public Orientation Orientation;
        private int _offset = 0;
        private int _diff = 0;

        public ScrollHandler()
        {
            SetItemName("ScrollHandler_" + count);
            EventMousePressed += OnMousePress;
            EventMouseDrag += OnDragging;
            count++;
        }

        protected virtual void OnMousePress(object sender, MouseArgs args)
        {
            if (Orientation == Orientation.Horizontal)
                _diff = args.Position.X - GetX();
            else
                _diff = args.Position.Y - GetY();
        }

        protected virtual void OnDragging(object sender, MouseArgs args)
        {
            int offset;

            if (Orientation == Orientation.Horizontal)
                offset = args.Position.X - GetParent().GetX() - _diff;
            else
                offset = args.Position.Y - GetParent().GetY() - _diff;

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

        public void InvokeScrollUp(MouseArgs args)
        {
            (GetParent() as IScrollable)?.InvokeScrollUp(args);
        }

        public void InvokeScrollDown(MouseArgs args)
        {
            (GetParent() as IScrollable)?.InvokeScrollDown(args);
        }
    }
}
