using System;
using System.Drawing;

namespace SpaceVIL
{
    public class FloatItem : VisualItem, IFloating, IDraggable //create abstract!!!!
    {
        private int _stored_offset = 0;
        private bool IsFloating = true;
        private bool _init = false;
        static int count = 0;
        private int _diff_x = 0;
        private int _diff_y = 0;

        private bool _ouside = true;
        public bool IsOutsideClickClosable()
        {
            return _ouside;
        }
        public void SetOutsideClickClosable(bool value)
        {
            _ouside = value;
        }

        public FloatItem(WindowLayout handler)
        {
            IsVisible = false;
            SetHandler(handler);
            SetItemName("FloatItem_" + count);
            SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            SetBackground(51, 51, 51);
            Border.Radius = 6;
            EventMouseHover += OnMousePress;
            EventMousePressed += OnMousePress;
            EventMouseDrag += OnDragging;
            count++;

            ItemsLayoutBox.AddItem(GetHandler(), this, LayoutType.Floating);
        }
        public override void SetConfines()
        {
            _confines_x_0 = GetX();
            _confines_x_1 = GetX() + GetWidth();
            _confines_y_0 = GetY();
            _confines_y_1 = GetY() + GetHeight();
        }

        private ButtonCore _btn_close;
        public override void InitElements()
        {
            //fake tests
            SetConfines();
            //close_btn
            _btn_close = new ButtonCore();
            _btn_close.SetBackground(Color.FromArgb(255, 100, 100, 100));
            _btn_close.SetItemName("Close_" + GetItemName());
            _btn_close.SetSize(10, 10);
            _btn_close.SetMargin(0, 10, 10, 0);
            _btn_close.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            _btn_close.SetAlignment(ItemAlignment.Top | ItemAlignment.Right);
            _btn_close.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });
            _btn_close.IsCustom = new CustomFigure(false, GraphicsMathService.GetCross(10, 10, 3, 45));
            _btn_close.EventMouseClick += (sender, args) =>
            {
                Hide();
            };

            RadioButton rb_1 = new RadioButton();
            rb_1.SetText("First radio.");
            rb_1.SetFont(new Font(new FontFamily("Courier New"), 12, FontStyle.Regular));
            rb_1.SetForeground(Color.White);
            rb_1.SetAlignment(ItemAlignment.Top | ItemAlignment.Left);
            rb_1.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            rb_1.SetHeight(20);
            rb_1.SetMargin(10, 25, 40, 0);
            // rb_1.EventMouseClick += (sender, arg) => Console.WriteLine("rb_1");

            RadioButton rb_2 = new RadioButton();
            rb_2.SetText("Second radio.");
            rb_2.SetFont(new Font(new FontFamily("Courier New"), 12, FontStyle.Regular));
            rb_2.SetForeground(Color.White);
            rb_2.SetAlignment(ItemAlignment.Bottom | ItemAlignment.Left);
            rb_2.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            rb_2.SetHeight(20);
            rb_2.SetMargin(10, 0, 40, 25);

            AddItems(_btn_close, rb_1, rb_2);

            _init = true;
        }
        public void Show(IItem sender, MouseArgs args)
        {
            if (!_init)
                InitElements();
            if (GetX() == -GetWidth()) //refactor?
                SetX(_stored_offset);
            IsVisible = true;
        }
        public void Hide()
        {
            _stored_offset = GetX();
            SetX(-GetWidth());
            IsVisible = false;
        }

        protected virtual void OnMousePress(object sender, MouseArgs args)
        {
            _diff_x = args.Position.X - GetX();
            _diff_y = args.Position.Y - GetY();
        }

        protected virtual void OnDragging(object sender, MouseArgs args)
        {
            if (!IsFloating)
                return;

            int offset_x;
            int offset_y;

            offset_x = args.Position.X - _diff_x;
            offset_y = args.Position.Y - _diff_y;

            SetX(offset_x);
            SetY(offset_y);
            SetConfines();
        }
    }
}