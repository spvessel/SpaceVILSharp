using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    class SplitArea : VisualItem, IDraggable
    {
        private static int count = 0;
        private Orientation _orientation = Orientation.Vertical;
        private Frame _frame1;
        private Frame _frame2;
        private Grid _handlerGrid;

        public SplitArea()
        {
            SetItemName("SplitArea_" + count);
            SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            count++;

            _frame1 = new Frame();
            _frame2 = new Frame();
            _handlerGrid = new Grid();

            EventMouseDrag += OnDragging;
            EventMousePressed += OnMousePressed;
            
        }

        public void SetOrientation(Orientation or) {
            if (!or.Equals(_orientation)) { 
                _orientation = or;
                InitSplitArea();
            }
        }

        private void InitSplitArea() {
            switch (_orientation)
            {
                case Orientation.Horizontal:
                    _handlerGrid.SetRowCount(2);
                    _handlerGrid.SetColumnCount(1);
                    _handlerGrid.InitCells();
                    _handlerGrid.InsertItem(_frame1, 0, 0);
                    _handlerGrid.InsertItem(_frame2, 1, 0);
                    break;

                case Orientation.Vertical:
                    _handlerGrid.SetRowCount(1);
                    _handlerGrid.SetColumnCount(2);
                    _handlerGrid.InitCells();
                    _handlerGrid.InsertItem(_frame1, 0, 0);
                    _handlerGrid.InsertItem(_frame2, 0, 1);
                    break;
            }
        }

        public Orientation GetOrientation() {
            return _orientation;
        }

        private bool _isSpacerDragging = false;
        private Point _dragFrom = new Point();
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

        public override void InitElements()
        {
            _handlerGrid.SetSpacing(6, 6);
            _handlerGrid.SetMargin(10, 10, 10, 10);

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
            _button1.SetBackground(Color.FromArgb(255, 255, 151, 153));
            _button1.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            _button1.SetSize(600, 30);
             
            _button2.SetToolTip("Show LayoutTest window.");
            _button2.SetStyle(style);
            _button2.SetItemName("Layout");
            _button2.SetBackground(Color.FromArgb(255, 255, 181, 111));
            
            
            _frame1.SetBackground(Color.FromArgb(255, 71, 71, 71));
            _frame1.SetWidthPolicy(SizePolicy.Expand);
            _frame1.SetHeightPolicy(SizePolicy.Expand);
            

            _frame2.SetBackground(Color.FromArgb(255, 71, 71, 71));
            _frame2.SetWidthPolicy(SizePolicy.Expand);
            _frame2.SetHeightPolicy(SizePolicy.Expand);
            

            AddItem(_handlerGrid);
            InitSplitArea();
            _frame1.AddItem(_button1);
            _frame2.AddItem(_button2);
            //_handlerGrid.AddItems(_frame1, _frame2);
            //_handlerGrid.InsertItem(_frame1, 0, 0);
            //_handlerGrid.InsertItem(_frame2, 0, 1);

        }

    }
}
