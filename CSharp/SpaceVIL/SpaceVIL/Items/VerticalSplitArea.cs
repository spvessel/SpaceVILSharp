using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    class VerticalSplitArea : Prototype, IHLayout
    {
        private static int count = 0;
        private IBaseItem _leftBlock;
        private IBaseItem _rightBlock;
        private SplitHolder _splitHolder = new SplitHolder(Orientation.Vertical);
        private int _leftWidth = 0;
        private int _diff = 0;
        private int _lMin = 0;
        private int _rMin = 0;

        public void SetSplitHolderPosition(int position)
        {
            if (position < _lMin || position > GetWidth() - _splitHolder.GetHolderSize() - _rMin)
                return;
            _leftWidth = position;
            _splitHolder.SetX(position + GetX());
            UpdateLayout();
        }

        public VerticalSplitArea()
        {
            SetItemName("VSplitArea_" + count);
            count++;
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.VerticalSplitArea)));
            IsFocusable = false;
            _splitHolder.EventMouseDrag += OnDragging;
            _splitHolder.EventMousePress += OnMousePress;
        }

        void OnMousePress(object sender, MouseArgs args)
        {
            _diff = args.Position.GetX() - _splitHolder.GetX();
        }
        void OnDragging(IItem sender, MouseArgs args)
        {
            int offset = args.Position.GetX() - GetX() - _diff;
            SetSplitHolderPosition(offset);
        }

        public override void InitElements()
        {
            //Adding
            AddItem(_splitHolder);
            SetSplitHolderPosition((GetWidth() - _splitHolder.GetHolderSize()) / 2);
            // Console.WriteLine(_splitHolder.GetX() + " " + _splitHolder.GetWidth());
            // UpdateLayout();
        }

        public void AssignLeftItem(IBaseItem item)
        {
            AddItem(item);
            _leftBlock = item;
            _lMin = _leftBlock.GetMinWidth();
            UpdateLayout();
        }

        public void AssignRightItem(IBaseItem item)
        {
            AddItem(item);
            _rightBlock = item;
            _rMin = _rightBlock.GetMinWidth();
            UpdateLayout();
        }

        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            CheckMins();
            UpdateLayout();
        }
        private void CheckMins()
        {
            int totalSize = GetWidth() - _splitHolder.GetHolderSize();
            if (totalSize < _lMin)
            {
                SetSplitHolderPosition(totalSize);
            }
            else if (totalSize <= _lMin + _rMin)
            {
                SetSplitHolderPosition(_lMin);
            }
            else
            {
                if (totalSize - _leftWidth < _rMin)
                {
                    SetSplitHolderPosition(totalSize - _rMin);
                }
            }
        }
        public override void SetX(int _x)
        {
            base.SetX(_x);
            SetSplitHolderPosition(_leftWidth);
            UpdateLayout();
        }

        public void UpdateLayout()
        {
            // _splitHolder.SetHeight(GetHeight());

            int tmpWidth = _leftWidth - GetPadding().Left;

            if (_leftBlock != null)
            {
                _leftBlock.SetX(GetX() + GetPadding().Left);
                if (tmpWidth > 0)
                    _leftBlock.SetWidth(tmpWidth);
                else
                    _leftBlock.SetWidth(0);
            }

            tmpWidth = GetWidth() - tmpWidth - _splitHolder.GetHolderSize();

            if (_rightBlock != null)
            {
                _rightBlock.SetX(_leftWidth + GetX() + _splitHolder.GetHolderSize());
                if (tmpWidth > 0)
                    _rightBlock.SetWidth(tmpWidth);
                else
                    _rightBlock.SetWidth(0);
            }

            foreach (var item in GetItems())
                item.SetConfines();
        }

        public void SetSplitThickness(int thickness)
        {
            _splitHolder.SetSpacerSize(thickness);
        }

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
