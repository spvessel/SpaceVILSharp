using System;
using System.Collections.Generic;
using System.Linq;
using System.Drawing;

namespace SpaceVIL
{
    public class FlowArea : VisualItem, IFlow, IDraggable
    {
        Dictionary<BaseItem, int[]> _stored_crd;
        static int count = 0;
        public FlowArea()
        {
            SetItemName("FlowArea_" + count);
            count++;
            EventMouseClick += EmptyEvent;
            EventMouseDrop += EmptyEvent;
            EventMouseDrag += OnDragging;
            _stored_crd = new Dictionary<BaseItem, int[]>();
        }

        public override void InvokePoolEvents()
        {
            if (EventMouseDrag != null) EventMouseDrag.Invoke(this);
        }

        public void OnDragging(object sender)
        {
            _xOffset -= _mouse_ptr.PrevX - _mouse_ptr.X;
            _yOffset -= _mouse_ptr.PrevY - _mouse_ptr.Y;
            UpdateLayout();
        }

        private Int64 _yOffset = 0;
        private Int64 _xOffset = 0;
        public Int64 GetVScrollOffset()
        {
            return _yOffset;
        }
        public void SetVScrollOffset(Int64 value)
        {
            _yOffset = value;
            UpdateLayout();
        }
        public Int64 GetHScrollOffset()
        {
            return _xOffset;
        }
        public void SetHScrollOffset(Int64 value)
        {
            _xOffset = value;
            UpdateLayout();
        }

        //overrides
        public override void AddItem(BaseItem item)
        {
            base.AddItem(item);
            _stored_crd.Add(item, new int[] { item.GetX(), item.GetY() });
            UpdateLayout();
        }
        public override void RemoveItem(BaseItem item)
        {
            base.AddItem(item);
            _stored_crd.Remove(item);
            UpdateLayout();
        }
        public void UpdateLayout()
        {
            foreach (var child in GetItems())
            {
                child.SetX((int)_xOffset + GetX() + GetPadding().Left + _stored_crd[child][0] + child.GetMargin().Left);
                child.SetY((int)_yOffset + GetY() + GetPadding().Top + +_stored_crd[child][1] + child.GetMargin().Top);
            }
        }
    }
}
