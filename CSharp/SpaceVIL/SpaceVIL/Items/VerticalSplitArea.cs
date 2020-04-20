using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;
// Each area is SpaceVIL.Frame and works according to the same rules. 
// Each area groups items based on items alignment, margins, paddings, sizes and size policies.
namespace SpaceVIL
{
    /// <summary>
    /// VerticalSplitArea is a container with two divided areas (on top and on bottom). 
    /// VerticalSplitArea implements SpaceVIL.Core.IHLayout.
    /// <para/> Contains SpaceVIL.SplitHolder.
    /// <para/> By default ability to get focus is disabled.
    /// <para/> Supports all events except drag and drop.
    /// </summary>
    public class VerticalSplitArea : Prototype, IHLayout
    {
        private static int count = 0;
        private IBaseItem _leftBlock;
        private IBaseItem _rightBlock;
        private SplitHolder _splitHolder = new SplitHolder(Orientation.Vertical);
        private int _leftWidth = -1;
        private int _diff = 0;
        private int _lMin = 0;
        private int _rMin = 0;

        /// <summary>
        /// Setting position of the split holder.
        /// </summary>
        /// <param name="position">Position of the split holder.</param>
        public void SetSplitPosition(int position)
        {
            if (_init)
            {
                if (position < _lMin || position > GetWidth() - _splitHolder.GetDividerSize() - _rMin)
                    return;
                _leftWidth = position;
                _splitHolder.SetX(position + GetX());
                UpdateLayout();
            }
            else
                _leftWidth = position;
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
        /// Defaults VerticalSplitArea constructor.
        /// </summary>
        public VerticalSplitArea()
        {
            SetItemName("VSplitArea_" + count);
            count++;
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.VerticalSplitArea)));
            IsFocusable = false;
            _splitHolder.EventMouseDrag += OnDragging;
            _splitHolder.EventMousePress += OnMousePress;
        }

        private void OnMousePress(IItem sender, MouseArgs args)
        {
            _diff = args.Position.GetX() - _splitHolder.GetX();
        }
        private void OnDragging(IItem sender, MouseArgs args)
        {
            int offset = args.Position.GetX() - GetX() - _diff;
            SetSplitPosition(offset);
        }

        private bool _init = false;
        
        /// <summary>
        /// Initializing all elements in the VerticalSplitArea. 
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            //Adding
            AddItem(_splitHolder);
            _init = true;

            if (_leftWidth < 0)
                SetSplitPosition((GetWidth() - _splitHolder.GetDividerSize()) / 2);
            else
                SetSplitPosition(_leftWidth);
        }

        /// <summary>
        /// Assign item on the left area of the VerticalSplitArea.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        public void AssignLeftItem(IBaseItem item)
        {
            AddItem(item);
            _leftBlock = item;
            _lMin = _leftBlock.GetMinWidth();
            UpdateLayout();
        }

        /// <summary>
        /// Assign item on the right area of the VerticalSplitArea.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        public void AssignRightItem(IBaseItem item)
        {
            AddItem(item);
            _rightBlock = item;
            _rMin = _rightBlock.GetMinWidth();
            UpdateLayout();
        }

        /// <summary>
        /// Setting VerticalSplitArea width. If the value is greater/less than the maximum/minimum 
        /// value of the width, then the width becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="width">Width of the VerticalSplitArea.</param>
        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            CheckMins();
            UpdateLayout();
        }
        private void CheckMins()
        {
            int totalSize = GetWidth() - _splitHolder.GetDividerSize();
            if (totalSize < _lMin)
            {
                SetSplitPosition(totalSize);
            }
            else if (totalSize <= _lMin + _rMin)
            {
                SetSplitPosition(_lMin);
            }
            else
            {
                if (totalSize - _leftWidth < _rMin)
                {
                    SetSplitPosition(totalSize - _rMin);
                }
            }
        }

        /// <summary>
        /// Setting X coordinate of the left-top corner of the VerticalSplitArea.
        /// </summary>
        /// <param name="x">X position of the left-top corner.</param>
        public override void SetX(int x)
        {
            base.SetX(x);
            SetSplitPosition(_leftWidth);
            UpdateLayout();
        }

        /// <summary>
        /// Updating all children positions (implementation of SpaceVIL.Core.IHLayout).
        /// </summary>
        public void UpdateLayout()
        {
            int tmpWidth = _leftWidth - GetPadding().Left;

            if (_leftBlock != null)
            {
                _leftBlock.SetX(GetX() + GetPadding().Left);
                if (tmpWidth > 0)
                    _leftBlock.SetWidth(tmpWidth);
                else
                    _leftBlock.SetWidth(0);
            }

            tmpWidth = GetWidth() - tmpWidth - _splitHolder.GetDividerSize();

            if (_rightBlock != null)
            {
                _rightBlock.SetX(_leftWidth + GetX() + _splitHolder.GetDividerSize());
                if (tmpWidth > 0)
                {
                    _rightBlock.SetWidth(tmpWidth);
                }
                else
                    _rightBlock.SetWidth(0);
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
        /// Seting style of the VerticalSplitArea.
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
