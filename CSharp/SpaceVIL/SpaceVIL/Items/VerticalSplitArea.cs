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
        private BaseItem _leftBlock;
        private BaseItem _rightBlock;
        public SplitHolder _splitHolder = new SplitHolder(Orientation.Vertical);
        private int _leftWidth = 0;
        private int _lMin = 0;
        private int _rMin = 0;

        public void SetSplitHolderPosition(int pos) {
            
            _leftWidth = pos;
            _splitHolder.SetX(pos + GetX());
            
        }

        public VerticalSplitArea()
        {
            SetItemName("HSplitArea_" + count);
            SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            count++;

            _splitHolder.EventMouseDrag += OnDragging;
            //EventMousePressed += OnMousePressed;
        }

        public virtual void OnDragging(object sender, MouseArgs args)
        {
            //Console.WriteLine(args.Position.X + " " + args.Position.PrevX);

            
            if (args.Position.X + _splitHolder.GetSpacerSize() <= GetWidth() + GetX() &&
                (args.Position.X >= GetX())) { 
                int dif = args.Position.X - args.Position.PrevX;
                /*
                if (_leftBlock != null)
                    _leftBlock.SetWidth(_leftBlock.GetWidth() + dif);
                */

                
                int totalSize = GetWidth() - _splitHolder.GetSpacerSize();
                if ((_leftWidth + dif >= _lMin) && 
                    (totalSize - _leftWidth - dif) >= _rMin)
                    SetSplitHolderPosition(_leftWidth + dif);
                UpdateLayout();
            }
            
        }

        public override void InitElements()
        {
            _splitHolder.SetBackground(Color.FromArgb(255, 71, 71, 71));
            SetSplitHolderPosition((GetWidth() - _splitHolder.GetSpacerSize()) / 2);
            
            //Adding
            AddItem(_splitHolder);
            UpdateLayout();
        }

        public void AssignLeftItem(BaseItem item) {
            item.SetWidthPolicy(SizePolicy.Ignored);
            AddItem(item);
            _leftBlock = item;
            _lMin = _leftBlock.GetMinWidth();
            //Console.Write("Left " + _leftBlock.GetWidth());
            //if (_leftBlock.GetWidth() == 0)
                //_leftBlock.SetWidth((GetWidth() - _splitHolder.GetSpacerSize()) / 2);
            
            UpdateLayout();
        }

        public void AssignRightItem(BaseItem item) {
            item.SetWidthPolicy(SizePolicy.Ignored);
            AddItem(item);
            _rightBlock = item;
            _rMin = _rightBlock.GetMinWidth();
            //Console.Write(" Right " + _rightBlock.GetWidth());
            //if (_rightBlock.GetWidth() == 0)
                //_rightBlock.SetWidth(GetWidth() - _splitHolder.GetSpacerSize() - (GetWidth() - _splitHolder.GetSpacerSize()) / 2);
            
            
            UpdateLayout();
        }

        public override void SetWidth(int width)
        {
            //Поставить ограничитель на ширину холдэра
            base.SetWidth(width);
            /*
            if (_leftWidth >= width)
            {
                SetSplitHolderPosition(width - _splitHolder.GetSpacerSize());
            }
            */
            CheckMins();
            /*
            if (_leftBlock != null && _rightBlock != null)
            { 
                int sz = GetWidth() - _splitHolder.GetSpacerSize() - _leftBlock.GetWidth();
                if (sz >= 0)
                    _rightBlock.SetWidth(sz);
                else
                    _leftBlock.SetWidth(GetWidth() + sz);
            }
            */
            UpdateLayout();
        }
        public override void SetX(int _x)
        {
            base.SetX(_x);
            SetSplitHolderPosition(_leftWidth);
            UpdateLayout();
        }
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            UpdateLayout();
        }
        public override void SetY(int _y)
        {
            base.SetY(_y);
            UpdateLayout();
        }

        private void CheckMins() {
            int totalSize = GetWidth() - _splitHolder.GetSpacerSize();
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

        public void UpdateLayout()
        {
            _splitHolder.SetY(GetY());
            _splitHolder.SetHeight(GetHeight());
            //Console.WriteLine(_leftWidth + " " + GetWidth() + " " + _splitHolder.GetX() + " " + test);
            int tmpWidth = _leftWidth;// _splitHolder.GetX() - GetX();

            if (_leftBlock != null)
            {
                _leftBlock.SetY(GetY());
                _leftBlock.SetHeight(GetHeight());
                _leftBlock.SetX(GetX());
                if (tmpWidth >= 0) _leftBlock.SetWidth(tmpWidth);
                else _leftBlock.SetWidth(0);
            }

            tmpWidth = GetWidth() - tmpWidth - _splitHolder.GetSpacerSize();
            if (_rightBlock != null)
            {
                _rightBlock.SetHeight(GetHeight());
                _rightBlock.SetY(GetY());
                _rightBlock.SetX(_leftWidth + GetX() + _splitHolder.GetSpacerSize());
                if (tmpWidth >= 0) _rightBlock.SetWidth(tmpWidth);
                else _rightBlock.SetWidth(0);
            }

            foreach (var item in GetItems())
                item.SetConfines();
        }

        public void SetSpacerWidth(int spWidth)
        {
            _splitHolder.SetSpacerSize(spWidth);
        }


        
    }
}
