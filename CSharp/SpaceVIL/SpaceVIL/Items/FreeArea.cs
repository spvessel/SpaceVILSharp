using System;
using System.Collections.Generic;
using System.Linq;
using System.Drawing;

namespace SpaceVIL
{
    public class FlowArea : VisualItem, IGrid, IDraggable
    {
        static int count = 0;
        private int _x_press = 0;
        private int _y_press = 0;
        private int _diff_x = 0;
        private int _diff_y = 0;
        Dictionary<BaseItem, int[]> _stored_crd;
        // public ContextMenu _dropdownmenu = new ContextMenu();
        public FlowArea()
        {
            SetItemName("FlowArea_" + count);
            count++;
            EventMouseClick += OnMouseRelease;
            EventMousePressed += OnMousePress;
            EventMouseDrag += OnDragging;
            _stored_crd = new Dictionary<BaseItem, int[]>();

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.FlowArea)));
        }

        public void AddContextMenu(ContextMenu context_menu)
        {
            EventMouseClick += (sender, args) => context_menu.Show(sender, args);
        }
        protected virtual void OnMousePress(IItem sender, MouseArgs args)
        {
            _x_press = args.Position.GetX();
            _y_press = args.Position.GetY();
            _diff_x = (int)_xOffset;
            _diff_y = (int)_yOffset;
        }

        protected virtual void OnMouseRelease(IItem sender, MouseArgs args)
        {
            //PrintArgs.MouseArgs(args);
            // if (args.Button == MouseButton.ButtonRight)
            //     ShowDropDownList(args);
        }

        protected virtual void OnDragging(IItem sender, MouseArgs args)
        {
            _xOffset = _diff_x - _x_press + args.Position.GetX();
            _yOffset = _diff_y + args.Position.GetY() - _y_press;
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
            ResizableItem wanted = item as ResizableItem;
            if (wanted != null)
            {
                wanted.PositionChanged += () => CorrectPosition(wanted);
            }
            UpdateLayout();
        }
        public override void RemoveItem(BaseItem item)
        {
            // Console.WriteLine("flow remove");
            base.RemoveItem(item);
            _stored_crd.Remove(item);
            UpdateLayout();
        }
        public void UpdateLayout()
        {
            lock (this)
            {
                foreach (var child in GetItems())
                {
                    child.SetX((int)_xOffset + GetX() + GetPadding().Left + _stored_crd[child][0] + child.GetMargin().Left);
                    child.SetY((int)_yOffset + GetY() + GetPadding().Top + _stored_crd[child][1] + child.GetMargin().Top);
                }
            }
        }

        //ContexMenu
        private void CorrectPosition(ResizableItem item)
        {
            int actual_x = item.GetX();
            int stored_x = _stored_crd[item][0];
            int actual_y = item.GetY();
            int stored_y = _stored_crd[item][1];

            _stored_crd[item] = new int[]
            {
                actual_x - (int)_xOffset - GetX() - GetPadding().Left - item.GetMargin().Left,
                actual_y - (int)_yOffset - GetY() - GetPadding().Top - item.GetMargin().Top
            };
        }
    }
}
