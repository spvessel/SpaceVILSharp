using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    class VerticalSplitArea : VisualItem, IGrid
    {
        private static int count = 0;
        private BaseItem _topBlock;
        private BaseItem _bottomBlock;
        public SplitHolder _splitHolder = new SplitHolder(Orientation.Vertical);
        private int _topHeight = 0;
        private int _tMin = 0;
        private int _bMin = 0;

        public void SetSplitHolderPosition(int pos)
        {
            _topHeight = pos;
            _splitHolder.SetY(pos + GetY());

        }

        public VerticalSplitArea()
        {
            SetItemName("VSplitArea_" + count);
            SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            count++;

            _splitHolder.EventMouseDrag += OnDragging;
            //EventMousePressed += OnMousePressed;
        }

        public virtual void OnDragging(object sender, MouseArgs args)
        {
            //Console.WriteLine(args.Position.X + " " + args.Position.PrevX);

            if (args.Position.Y + _splitHolder.GetSpacerSize() <= GetHeight() + GetY() &&
                (args.Position.Y >= GetY()))
            {
                int dif = args.Position.Y - args.Position.PrevY;
                /*
                if (_leftBlock != null)
                    _leftBlock.SetWidth(_leftBlock.GetWidth() + dif);
                */


                int totalSize = GetHeight() - _splitHolder.GetSpacerSize();
                if ((_topHeight + dif >= _tMin) &&
                    (totalSize - _topHeight - dif) >= _bMin)
                    SetSplitHolderPosition(_topHeight + dif);
                UpdateLayout();
            }

        }

        public override void InitElements()
        {
            _splitHolder.SetBackground(Color.FromArgb(255, 71, 71, 71));
            SetSplitHolderPosition((GetHeight() - _splitHolder.GetSpacerSize()) / 2);

            //Adding
            AddItem(_splitHolder);
            UpdateLayout();
        }

        public void AssignTopItem(BaseItem item)
        {
            item.SetHeightPolicy(SizePolicy.Ignored);
            AddItem(item);
            _topBlock = item;
            _tMin = _topBlock.GetMinHeight();
            //Console.Write("Left " + _leftBlock.GetWidth());
            //if (_leftBlock.GetWidth() == 0)
            //_leftBlock.SetWidth((GetWidth() - _splitHolder.GetSpacerSize()) / 2);

            UpdateLayout();
        }

        public void AssignBottomItem(BaseItem item)
        {
            item.SetHeightPolicy(SizePolicy.Ignored);
            AddItem(item);
            _bottomBlock = item;
            _bMin = _bottomBlock.GetMinHeight();
            //Console.Write(" Right " + _rightBlock.GetWidth());
            //if (_rightBlock.GetWidth() == 0)
            //_rightBlock.SetWidth(GetWidth() - _splitHolder.GetSpacerSize() - (GetWidth() - _splitHolder.GetSpacerSize()) / 2);


            UpdateLayout();
        }

        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            UpdateLayout();
        }
        public override void SetX(int _x)
        {
            base.SetX(_x);
            UpdateLayout();
        }
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            CheckMins();
            UpdateLayout();
        }
        public override void SetY(int _y)
        {
            base.SetY(_y);
            SetSplitHolderPosition(_topHeight);
            UpdateLayout();
        }

        private void CheckMins()
        {
            int totalSize = GetHeight() - _splitHolder.GetSpacerSize();
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

        public void UpdateLayout()
        {
            _splitHolder.SetX(GetX());
            _splitHolder.SetWidth(GetWidth());
            //Console.WriteLine(_leftWidth + " " + GetWidth() + " " + _splitHolder.GetX() + " " + test);
            int tmpHeight = _topHeight;

            if (_topBlock != null)
            {
                _topBlock.SetY(GetY());
                _topBlock.SetWidth(GetWidth());
                _topBlock.SetX(GetX());
                if (tmpHeight >= 0) _topBlock.SetHeight(tmpHeight);
                else _topBlock.SetHeight(0);
            }

            tmpHeight = GetHeight() - tmpHeight - _splitHolder.GetSpacerSize();
            if (_bottomBlock != null)
            {
                _bottomBlock.SetWidth(GetWidth());
                _bottomBlock.SetX(GetX());
                _bottomBlock.SetY(_topHeight + GetY() + _splitHolder.GetSpacerSize());
                if (tmpHeight >= 0) _bottomBlock.SetHeight(tmpHeight);
                else _bottomBlock.SetHeight(0);
            }

            foreach (var item in GetItems())
                item.SetConfines();
        }

        public void SetSpacerHeight(int spHeight)
        {
            _splitHolder.SetSpacerSize(spHeight);
        }
    }
}
