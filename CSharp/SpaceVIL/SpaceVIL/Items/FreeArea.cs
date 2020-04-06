using System;
using System.Collections.Generic;
using System.Linq;
using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Common;

namespace SpaceVIL
{
    /// <summary>
    /// FreeArea is class representing an unbounded area with free location of inner items.
    /// FreeArea implements SpaceVIL.Core.IFreeLayout and SpaceVIL.Core.IDraggable.
    /// FreeArea is supposed to be used with SpaceVIL.ResizableItem.
    /// <para/> Supports all events including drag and drop.
    /// </summary>
    public class FreeArea : Prototype, IFreeLayout, IDraggable
    {
        static int count = 0;
        private int _xPress = 0;
        private int _yPress = 0;
        private int _diffX = 0;
        private int _diffY = 0;
        private Dictionary<IBaseItem, int[]> _storedItemsCoords;

        /// <summary>
        /// Default FreeArea constructor.
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

        private void OnMousePress(IItem sender, MouseArgs args)
        {
            _xPress = args.Position.GetX();
            _yPress = args.Position.GetY();
            _diffX = (int)_xOffset;
            _diffY = (int)_yOffset;
        }

        private void OnDragging(IItem sender, MouseArgs args)
        {
            _xOffset = _diffX - _xPress + args.Position.GetX();
            _yOffset = _diffY + args.Position.GetY() - _yPress;
            UpdateLayout();
        }

        private Int64 _yOffset = 0;
        private Int64 _xOffset = 0;
        /// <summary>
        /// Getting Y axis offset of an unbounded area of FreArea.
        /// <para/> Default: 0.
        /// </summary>
        /// <returns>Y axis offset of an unbounded area.</returns>
        public Int64 GetVScrollOffset()
        {
            return _yOffset;
        }
        /// <summary>
        /// Setting Y axis offset of an unbounded area of FreArea.
        /// </summary>
        /// <param name="value">Y axis offset of an unbounded area.</param>
        public void SetVScrollOffset(Int64 value)
        {
            _yOffset = value;
            UpdateLayout();
        }
        /// <summary>
        /// Getting X axis offset of an unbounded area of FreArea.
        /// <para/> Default: 0.
        /// </summary>
        /// <returns>X axis offset of an unbounded area.</returns>
        public Int64 GetHScrollOffset()
        {
            return _xOffset;
        }
        /// <summary>
        /// Setting X axis offset of an unbounded area of FreArea.
        /// </summary>
        /// <param name="value">X axis offset of an unbounded area.</param>
        public void SetHScrollOffset(Int64 value)
        {
            _xOffset = value;
            UpdateLayout();
        }

        /// <summary>
        /// Adding item to the FreeArea. 
        /// <para/> Notice: Make sure the item is in the correct position to be visible.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
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
        /// Remove item from the FreeArea.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        /// <returns>True: if the removal was successful. 
        /// False: if the removal was unsuccessful.</returns>
        public override bool RemoveItem(IBaseItem item)
        {
            bool b = base.RemoveItem(item);
            _storedItemsCoords.Remove(item);
            UpdateLayout();
            return b;
        }

        /// <summary>
        /// Updating all children positions (implementation of SpaceVIL.Core.IFreeLayout).
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
