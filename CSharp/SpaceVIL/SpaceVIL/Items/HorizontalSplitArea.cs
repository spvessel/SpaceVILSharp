using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    class HorizontalSplitArea : VisualItem, IGrid
    { 
        private static int count = 0;
        private BaseItem _leftBlock;
        private BaseItem _rightBlock;
        public SplitHolder _splitHolder = new SplitHolder(Orientation.Horizontal);
        private int _leftWidth = 0;
        private bool test = false;

        public void SetSplitHolderPosition(int pos) {
            //Console.Write(pos + " getX " + GetX());
            _leftWidth = pos;
            _splitHolder.SetX(pos + GetX());
            //Console.WriteLine(" spX " + _splitHolder.GetX() + " " + test);
        }

        public HorizontalSplitArea()
        {
            SetItemName("SplitArea_" + count);
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
                SetSplitHolderPosition(_leftWidth + dif);
                UpdateLayout();
            }
            
        }

        public override void InitElements()
        {
            _splitHolder.SetBackground(Color.FromArgb(255, 71, 71, 71));
            SetSplitHolderPosition((GetWidth() - _splitHolder.GetSpacerSize()) / 2);
            //Console.WriteLine("Init elements " + _splitHolder.GetX());
            //Adding
            AddItem(_splitHolder);
            test = true;
            UpdateLayout();
        }

        public void AssignLeftItem(BaseItem item) {
            item.SetWidthPolicy(SizePolicy.Ignored);
            AddItem(item);
            _leftBlock = item;
            
            //Console.Write("Left " + _leftBlock.GetWidth());
            //if (_leftBlock.GetWidth() == 0)
                //_leftBlock.SetWidth((GetWidth() - _splitHolder.GetSpacerSize()) / 2);
            
            UpdateLayout();
        }

        public void AssignRightItem(BaseItem item) {
            item.SetWidthPolicy(SizePolicy.Ignored);
            AddItem(item);
            _rightBlock = item;
            
            //Console.Write(" Right " + _rightBlock.GetWidth());
            //if (_rightBlock.GetWidth() == 0)
                //_rightBlock.SetWidth(GetWidth() - _splitHolder.GetSpacerSize() - (GetWidth() - _splitHolder.GetSpacerSize()) / 2);
            
            
            UpdateLayout();
        }

        public override void SetWidth(int width)
        {
            //Поставить ограничитель на ширину холдэра
            base.SetWidth(width);
            if (_leftWidth >= width)
            {
                SetSplitHolderPosition(width - _splitHolder.GetSpacerSize());
            }

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

        /*
        protected virtual void OnMousePressed(object sender) {
            Int32[] arr;
            int spacerStart = 0, spacerEnd = 0;
            switch (_orientation)
            {
                case Orientation.Horizontal:
                    arr = _handlerGrid.GetRowHeight();
                    spacerStart = _handlerGrid.GetY() + arr[0];
                    spacerEnd = spacerStart + _handlerGrid.GetSpacing().Horizontal;
                    //Console.WriteLine(spacerStart + " " + _mouse_ptr.Y + " " + spacerEnd);
                    if (_mouse_ptr.Y >= spacerStart && _mouse_ptr.Y <= spacerEnd)
                    {
                        _isSpacerDragging = true;
                        _dragFrom.Y = _mouse_ptr.Y;
                    }
                    else _isSpacerDragging = false;
                    break;

                case Orientation.Vertical:
                    arr = _handlerGrid.GetColWidth();
                    spacerStart = _handlerGrid.GetX() + arr[0];
                    spacerEnd = spacerStart + _handlerGrid.GetSpacing().Vertical;
                    //Console.WriteLine(spacerStart + " " + _mouse_ptr.X + " " + spacerEnd);

                    if (_mouse_ptr.X >= spacerStart && _mouse_ptr.X <= spacerEnd)
                    {
                        _isSpacerDragging = true;
                        _dragFrom.X = _mouse_ptr.X;
                    }
                    else _isSpacerDragging = false;
                    break;
            }
        }

        protected virtual void OnDragging(object sender, MouseArgs args)
        {
            //Console.WriteLine("Mouse " + _mouse_ptr.X + " " + _mouse_ptr.Y);
            if (_isSpacerDragging)
            {
                //Int32[] arr;
                switch (_orientation)
                {
                    case Orientation.Horizontal:
                        //arr = _handlerGrid.GetRowHeight();
                        _frame1.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
                        _frame1.SetHeight(_frame1.GetHeight() + (_mouse_ptr.Y - _dragFrom.Y));
                        _dragFrom.Y = _mouse_ptr.Y;
                        break;

                    case Orientation.Vertical:
                        //arr = _handlerGrid.GetColWidth();
                        _frame1.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
                        _frame1.SetWidth(_frame1.GetWidth() + (_mouse_ptr.X - _dragFrom.X));
                        _dragFrom.X = _mouse_ptr.X;
                        break;
                }
            }
        }
        */

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
                //_leftBlock.SetConfines();
            }

            tmpWidth = GetWidth() - tmpWidth - _splitHolder.GetSpacerSize();
            if (_rightBlock != null)
            {
                _rightBlock.SetHeight(GetHeight());
                _rightBlock.SetY(GetY());
                _rightBlock.SetX(_leftWidth + GetX() + _splitHolder.GetSpacerSize());
                if (tmpWidth >= 0) _rightBlock.SetWidth(tmpWidth);
                else _rightBlock.SetWidth(0);
                //_rightBlock.SetConfines();
            }

            //_splitHolder?.SetConfines();
        }

        public void SetSpacerWidth(int spWidth)
        {
            _splitHolder.SetSpacerSize(spWidth);
        }


        
    }
}
