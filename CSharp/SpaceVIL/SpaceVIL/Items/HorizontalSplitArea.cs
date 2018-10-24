using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    public class HorizontalSplitArea : VisualItem, IVLayout
    {
        private static int count = 0;
        private BaseItem _topBlock;
        private BaseItem _bottomBlock;
        private SplitHolder _splitHolder = new SplitHolder(Orientation.Horizontal);
        private int _topHeight = 0;
        private int _diff = 0;
        private int _tMin = 0;
        private int _bMin = 0;

        public void SetSplitHolderPosition(int position)
        {
            if (position < _tMin || position > GetHeight() - _splitHolder.GetHolderSize() - _bMin)
                return;
            _topHeight = position;
            _splitHolder.SetY(position + GetY());
            UpdateLayout();
        }

        public HorizontalSplitArea()
        {
            SetItemName("HSplitArea_" + count);
            count++;
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.HorizontalSplitArea)));

            _splitHolder.EventMouseDrag += OnDragging;
            _splitHolder.EventMousePressed += OnMousePress;
        }

        protected virtual void OnMousePress(object sender, MouseArgs args)
        {
            _diff = args.Position.GetY() - _splitHolder.GetY();
        }
        protected virtual void OnDragging(object sender, MouseArgs args)
        {
            int offset = args.Position.GetY() - GetY() - _diff;
            SetSplitHolderPosition(offset);
        }

        public override void InitElements()
        {
            SetSplitHolderPosition((GetHeight() - _splitHolder.GetHolderSize()) / 2);

            //Adding
            AddItem(_splitHolder);
            UpdateLayout();
        }

        public void AssignTopItem(BaseItem item)
        {
            AddItem(item);
            _topBlock = item;
            _tMin = _topBlock.GetMinHeight();
            UpdateLayout();
        }

        public void AssignBottomItem(BaseItem item)
        {
            AddItem(item);
            _bottomBlock = item;
            _bMin = _bottomBlock.GetMinHeight();
            UpdateLayout();
        }

        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            CheckMins();
            UpdateLayout();
        }
        private void CheckMins()
        {
            int totalSize = GetHeight() - _splitHolder.GetHolderSize();
            if (totalSize < _tMin)
            {
                SetSplitHolderPosition(totalSize);
            }
            else if (totalSize <= _tMin + _bMin)
            {
                SetSplitHolderPosition(_tMin);
            }
            else
            {
                if (totalSize - _topHeight < _bMin)
                {
                    SetSplitHolderPosition(totalSize - _bMin);
                }
            }
        }
        public override void SetY(int _y)
        {
            base.SetY(_y);
            SetSplitHolderPosition(_topHeight);
            UpdateLayout();
        }

        public void UpdateLayout()
        {
            _splitHolder.SetWidth(GetWidth());

            int tmpHeight = _topHeight;

            if (_topBlock != null)
            {
                _topBlock.SetY(GetY() + GetPadding().Top);
                if (tmpHeight >= 0)
                    _topBlock.SetHeight(tmpHeight);
                else
                    _topBlock.SetHeight(0);
            }

            tmpHeight = GetHeight() - tmpHeight - _splitHolder.GetHolderSize();

            if (_bottomBlock != null)
            {
                _bottomBlock.SetY(_topHeight + GetY() + _splitHolder.GetHolderSize());
                if (tmpHeight >= 0)
                    _bottomBlock.SetHeight(tmpHeight);
                else
                    _bottomBlock.SetHeight(0);
            }

            foreach (var item in GetItems())
                item.SetConfines();
        }

        public void SetSpacerHeight(int spHeight)
        {
            _splitHolder.SetSpacerSize(spHeight);
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
