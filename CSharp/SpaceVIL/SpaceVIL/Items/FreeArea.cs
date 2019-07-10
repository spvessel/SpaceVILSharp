using System;
using System.Collections.Generic;
using System.Linq;
using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Common;

namespace SpaceVIL
{
    /// <summary>
    /// Unbounded area with free location of inner items
    /// </summary>
    public class FreeArea : Prototype, IFreeLayout, IDraggable
    {
        static int count = 0;
        private int _xPress = 0;
        private int _yPress = 0;
        private int _diffX = 0;
        private int _diffY = 0;
        Dictionary<IBaseItem, int[]> _storedItemsCoords;

        /// <summary>
        /// Constructs a FreeArea
        /// </summary>
        public FreeArea()
        {
            SetItemName("FreeArea_" + count);
            count++;
            EventMousePress += OnMousePress;
            EventMouseDrag += OnDragging;
            _storedItemsCoords = new Dictionary<IBaseItem, int[]>();

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.FreeArea)));
        }

        void OnMousePress(IItem sender, MouseArgs args)
        {
            _xPress = args.Position.GetX();
            _yPress = args.Position.GetY();
            _diffX = (int)_xOffset;
            _diffY = (int)_yOffset;
        }

        void OnDragging(IItem sender, MouseArgs args)
        {
            _xOffset = _diffX - _xPress + args.Position.GetX();
            _yOffset = _diffY + args.Position.GetY() - _yPress;
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

        /// <summary>
        /// Add item to the FreeArea
        /// </summary>
        public override void AddItem(IBaseItem item)
        {
            _storedItemsCoords.Add(item, new int[]
            {
                item.GetX() ,
                item.GetY()
            });
            ResizableItem wanted = item as ResizableItem;
            if (wanted != null)
            {
                wanted.PositionChanged += () => CorrectPosition(wanted);
            }
            base.AddItem(item);
            UpdateLayout();
        }

        /// <summary>
        /// Remove item from the FreeArea
        /// </summary>
        public override bool RemoveItem(IBaseItem item)
        {
            bool b = base.RemoveItem(item);
            _storedItemsCoords.Remove(item);
            UpdateLayout();
            return b;
        }

        /// <summary>
        /// Update all children elements positions
        /// </summary>
        public void UpdateLayout()
        {
            foreach (var child in GetItems())
            {
                child.SetX((int)_xOffset + GetX() + GetPadding().Left + _storedItemsCoords[child][0] + child.GetMargin().Left);
                child.SetY((int)_yOffset + GetY() + GetPadding().Top + _storedItemsCoords[child][1] + child.GetMargin().Top);
            }
        }

        private void CorrectPosition(ResizableItem item)
        {
            int actual_x = item.GetX();
            int actual_y = item.GetY();
            int[] crd = new int[]
            {
                actual_x - (int)_xOffset - GetX() - GetPadding().Left - item.GetMargin().Left,
                actual_y - (int)_yOffset - GetY() - GetPadding().Top - item.GetMargin().Top
            };
            _storedItemsCoords.Remove(item);
            _storedItemsCoords.Add(item, crd);
        }
    }
}
