using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    class HorizontalSplitArea : VisualItem, IDraggable, IHLayout
    { 
        private static int count = 0;
        private SimpleCell _leftAnchor = new SimpleCell();
        private SimpleCell _rightAnchor = new SimpleCell();
        public SplitHolder _splitHolder = new SplitHolder(Orientation.Horizontal);

            

        public HorizontalSplitArea()
        {
            SetItemName("SplitArea_" + count);
            SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            count++;

            //EventMouseDrag += OnDragging;
            //EventMousePressed += OnMousePressed;
        }

        public void OnDragging(object sender)
        {

        }

        public override void InitElements()
        {
            _splitHolder.SetBackground(Color.FromArgb(255, 71, 71, 71));
            _splitHolder.EventMouseDrag += OnDragging;

            //Adding
            AddItem(_splitHolder);
            UpdateLayout();
        }

        public void SetLeftAnchor(BaseItem item) {
            AddItem(item);
            _leftAnchor.SetItem(item);
            //_leftAnchor.SetWidth(item.GetWidth());
            //_leftAnchor.SetHeight(item.GetHeight());
            UpdateLayout();
        }

        public void SetRightAnchor(BaseItem item) {
            AddItem(item);
            _rightAnchor.SetItem(item);
            //_rightAnchor.SetWidth(item.GetWidth());
            //_rightAnchor.SetHeight(item.GetHeight());
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

        private bool _isSpacerDragging = false;
        private Point _dragFrom = new Point();
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
                    if (_mouse_ptr.Y >= spacerStart && _mouse_ptr.Y <= spacerEnd) { 
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

        protected virtual void OnDragging(object sender)
        {
            //Console.WriteLine("Mouse " + _mouse_ptr.X + " " + _mouse_ptr.Y);
            if (_isSpacerDragging) {
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
        /*
        public override void InitElements()
        {
            
            Style style = new Style();
            style.Background = Color.FromArgb(255, 13, 176, 255);
            style.Foreground = Color.Black;
            style.BorderRadius = 6;
            style.Font = new Font(new FontFamily("Courier New"), 20, FontStyle.Regular);
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Expand;
            style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
            ItemState brighter = new ItemState();
            brighter.Background = Color.FromArgb(125, 255, 255, 255);
            style.ItemStates.Add(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(60, 255, 255, 255)
            });

            ButtonCore _button1 = new ButtonCore("btn1");
            ButtonCore _button2 = new ButtonCore("btn2");
            
            _button1.SetToolTip("Show LayoutTest window.");
            _button1.SetStyle(style);
            _button1.SetItemName("Layout");
            _button1.SetBackground(Color.FromArgb(255, 255, 181, 111));

            _button2.SetToolTip("Show LayoutTest window.");
            _button2.SetStyle(style);
            _button2.SetItemName("Layout");
            _button2.SetBackground(Color.FromArgb(255, 255, 181, 111));

            
            _leftAnchor.SetBackground(Color.FromArgb(255, 71, 71, 71));
            _leftAnchor.SetWidthPolicy(SizePolicy.Expand);
            _leftAnchor.SetHeightPolicy(SizePolicy.Expand);


            _rightAnchor.SetBackground(Color.FromArgb(255, 71, 71, 71));
            _rightAnchor.SetWidthPolicy(SizePolicy.Expand);
            _rightAnchor.SetHeightPolicy(SizePolicy.Expand);
            
            _splitHolder.SetBackground(Color.FromArgb(255, 71, 71, 71));

            //AddItems(_leftAnchor, _splitHolder, _rightAnchor);

            //InitSplitArea();

            SetLeftAnchor(_button1);
            SetRightAnchor(_button2);
            //_handlerGrid.AddItems(_frame1, _frame2);
            //_handlerGrid.InsertItem(_frame1, 0, 0);
            //_handlerGrid.InsertItem(_frame2, 0, 1);
            UpdateLayout();
        }
        */

        public void UpdateLayout()
        {
            //_frame1.SetWidthPolicy(SizePolicy.Fixed);
            //_frame2.SetWidthPolicy(SizePolicy.Fixed);
            int total_space = GetWidth() - _splitHolder.GetSpacerSize();
            Console.Write(total_space + " ");

            _leftAnchor.SetHeight(GetHeight());
            _rightAnchor.SetHeight(GetHeight());

            BaseItem item;

            int startX = GetX();
            _leftAnchor.SetX(startX);
            _leftAnchor.SetWidth(total_space / 2);

            item = _leftAnchor.GetItem();

            if (item != null)
            {
                item.SetX(startX);
            item.SetWidth(_leftAnchor.GetWidth());

            }


            startX += total_space / 2;
            total_space -= total_space / 2;
            Console.Write(total_space + " \n");
            _rightAnchor.SetWidth(total_space);

            _splitHolder.SetX(startX);

            startX += _splitHolder.GetSpacerSize();
            _rightAnchor.SetX(startX);

            item = _rightAnchor.GetItem();
            if (item != null)
            {
                item.SetX(startX);
            item.SetWidth(_rightAnchor.GetWidth());

            }

        }

        public void SetSpacerWidth(int spWidth)
        {
            _splitHolder.SetSpacerSize(spWidth);
        }


        
    }
}
