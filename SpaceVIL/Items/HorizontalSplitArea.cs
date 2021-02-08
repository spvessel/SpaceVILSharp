using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

// Each area is SpaceVIL.Frame and works according to the same rules. 
// Each area groups items based on items alignment, margins, paddings, sizes and size policies.
namespace SpaceVIL
{
    /// <summary>
    /// HorizontalSplitArea is a container with two divided areas (on top and on bottom). 
    /// HorizontalSplitArea implements SpaceVIL.Core.IVLayout.
    /// <para/> Contains SpaceVIL.SplitHolder.
    /// <para/> By default ability to get focus is disabled.
    /// <para/> Supports all events except drag and drop.
    /// </summary>
    public class HorizontalSplitArea : Prototype, IVLayout
    {
        private static int count = 0;
        private IBaseItem _topBlock;
        private IBaseItem _bottomBlock;
        private SplitHolder _splitHolder = new SplitHolder(Orientation.Horizontal);
        private int _topHeight = -1;
        private int _diff = 0;
        private int _tMin = 0;
        private int _bMin = 0;

        /// <summary>
        /// Setting position of the split holder.
        /// </summary>
        /// <param name="position">Position of the split holder.</param>
        public void SetSplitPosition(int position)
        {
            if (_init)
            {
                if (position < _tMin || position > GetHeight() - _splitHolder.GetDividerSize() - _bMin)
                    return;
                _topHeight = position;
                _splitHolder.SetY(position + GetY());
                UpdateLayout();
            }
            else
                _topHeight = position;
        }
        /// <summary>
        /// Setting split holder color.
        /// </summary>
        /// <param name="color">Split holder color as System.Drawing.Color.</param>
        public void SetSplitColor(Color color)
        {
            _splitHolder.SetBackground(color);
        }

        /// <summary>
        /// Defaults HorizontalSplitArea constructor.
        /// </summary>
        public HorizontalSplitArea()
        {
            SetItemName("HSplitArea_" + count);
            count++;
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.HorizontalSplitArea)));
            IsFocusable = false;
            _splitHolder.EventMouseDrag += OnDragging;
            _splitHolder.EventMousePress += OnMousePress;
        }

        private void OnMousePress(IItem sender, MouseArgs args)
        {
            _diff = args.Position.GetY() - _splitHolder.GetY();
        }
        private void OnDragging(IItem sender, MouseArgs args)
        {
            int offset = args.Position.GetY() - GetY() - _diff;
            SetSplitPosition(offset);
        }
        private bool _init = false;
        /// <summary>
        /// Initializing all elements in the HorizontalSplitArea. 
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            //Adding
            AddItem(_splitHolder);
            _init = true;
            if (_topHeight < 0)
                SetSplitPosition((GetHeight() - _splitHolder.GetDividerSize()) / 2);
            else
                SetSplitPosition(_topHeight);
        }

        /// <summary>
        /// Assign item on the top area of the HorizontalSplitArea.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        public void SetTopItem(IBaseItem item)
        {
            AddItem(item);
            _topBlock = item;
            _tMin = _topBlock.GetMinHeight();
            UpdateLayout();
        }

        /// <summary>
        /// Assign item on the bottom area of the HorizontalSplitArea.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        public void SetBottomItem(IBaseItem item)
        {
            AddItem(item);
            _bottomBlock = item;
            _bMin = _bottomBlock.GetMinHeight();
            UpdateLayout();
        }

        /// <summary>
        /// Setting HorizontalSplitArea height. If the value is greater/less than the maximum/minimum 
        /// value of the height, then the height becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="height">Height of the HorizontalSplitArea.</param>
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            CheckMins();
            UpdateLayout();
        }
        private void CheckMins()
        {
            int totalSize = GetHeight() - _splitHolder.GetDividerSize();
            if (totalSize < _tMin)
            {
                SetSplitPosition(totalSize);
            }
            else if (totalSize <= _tMin + _bMin)
            {
                SetSplitPosition(_tMin);
            }
            else
            {
                if (totalSize - _topHeight < _bMin)
                {
                    SetSplitPosition(totalSize - _bMin);
                }
            }
        }

        /// <summary>
        /// Setting Y coordinate of the left-top corner of the HorizontalSplitArea.
        /// </summary>
        /// <param name="y">Y position of the left-top corner.</param>
        public override void SetY(int y)
        {
            base.SetY(y);
            SetSplitPosition(_topHeight);
            UpdateLayout();
        }

        /// <summary>
        /// Updating all children positions (implementation of SpaceVIL.Core.IVLayout).
        /// </summary>
        public void UpdateLayout()
        {
            int tmpHeight = _topHeight;

            if (_topBlock != null)
            {
                _topBlock.SetY(GetY() + GetPadding().Top);
                if (tmpHeight >= 0)
                    _topBlock.SetHeight(tmpHeight);
                else
                    _topBlock.SetHeight(0);
            }

            tmpHeight = GetHeight() - tmpHeight - _splitHolder.GetDividerSize();

            if (_bottomBlock != null)
            {
                _bottomBlock.SetY(_topHeight + GetY() + _splitHolder.GetDividerSize());
                if (tmpHeight >= 0)
                    _bottomBlock.SetHeight(tmpHeight);
                else
                    _bottomBlock.SetHeight(0);
            }

            foreach (var item in GetItems())
                item.SetConfines();
        }

        /// <summary>
        /// Setting thickness of SplitHolder divider.
        /// </summary>
        /// <param name="thickness">Thickness of SplitHolder divider.</param>
        public void SetSplitThickness(int thickness)
        {
            _splitHolder.SetDividerSize(thickness);
        }

        /// <summary>
        /// Seting style of the HorizontalSplitArea.
        /// <para/> Inner styles: "splitholder".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);

            Style inner_style = style.GetInnerStyle("splitholder");
            if (inner_style != null)
            {
                _splitHolder.SetStyle(inner_style);
            }
        }
    }
}
