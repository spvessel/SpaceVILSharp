using System;
using System.Drawing;

namespace SpaceVIL
{
    public class ResizableItem : VisualItem, IDraggable
    {
        internal ItemAlignment _sides = 0;

        public EventCommonMethod PositionChanged;
        public EventCommonMethod SizeChanged;

        public bool IsResizable = true;
        public bool IsFloating = true;

        private bool _is_moved;

        static int count = 0;
        private int _pressed_x = 0;
        private int _pressed_y = 0;
        private int _width = 0;
        private int _height = 0;
        private int _diff_x = 0;
        private int _diff_y = 0;

        public ResizableItem()
        {
            SetItemName("ResizableItem_" + count);
            SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);

            EventMousePressed += OnMousePress;
            EventMouseDrag += OnDragging;
            count++;
        }

        protected virtual void OnMousePress(object sender, MouseArgs args)
        {
            _pressed_x = args.Position.X;
            _pressed_y = args.Position.Y;

            _diff_x = args.Position.X - GetX();
            _diff_y = args.Position.Y - GetY();

            GetSides(_diff_x, _diff_y);

            if (_sides == 0)
            {
                _is_moved = true;
            }
            else
            {
                _width = GetWidth();
                _height = GetHeight();
                _is_moved = false;
            }
        }

        protected virtual void OnDragging(object sender, MouseArgs args)
        {
            int offset_x;
            int offset_y;

            switch (_is_moved)
            {
                case true:
                    if (!IsFloating)
                        break;

                    offset_x = args.Position.X - _diff_x;
                    offset_y = args.Position.Y - _diff_y;
                    SetX(offset_x);
                    SetY(offset_y);
                    PositionChanged?.Invoke();
                    SetConfines();
                    break;

                case false:
                    if (!IsResizable)
                        break;

                    int x = GetX();
                    int y = GetY();
                    int w = GetWidth();
                    int h = GetHeight();

                    if (_sides.HasFlag(ItemAlignment.Left))
                    {
                        if (!(GetMinWidth() == GetWidth() && (args.Position.X - _pressed_x) >= 0))
                        {
                            x += (args.Position.X - _pressed_x);
                            w -= (args.Position.X - _pressed_x);
                        }
                        _pressed_x = args.Position.X;
                    }
                    if (_sides.HasFlag(ItemAlignment.Right))
                    {
                        if (!(args.Position.X < GetMinWidth() && GetWidth() == GetMinWidth()))
                        {
                            w += (args.Position.X - _pressed_x);
                        }
                        _pressed_x = args.Position.X;
                    }
                    if (_sides.HasFlag(ItemAlignment.Top))
                    {
                        if (!(GetMinHeight() == GetHeight() && (args.Position.Y - _pressed_y) >= 0))
                        {
                            y += (args.Position.Y - _pressed_y);
                            h -= (args.Position.Y - _pressed_y);
                        }
                        _pressed_y = args.Position.Y;
                    }
                    if (_sides.HasFlag(ItemAlignment.Bottom))
                    {
                        if (!(args.Position.Y < GetMinHeight() && GetHeight() == GetMinHeight()))
                        {
                            h += (args.Position.Y - _pressed_y);
                        }
                        _pressed_y = args.Position.Y;
                    }

                    if (_sides != 0)
                    {
                        if (_sides.HasFlag(ItemAlignment.Left) || _sides.HasFlag(ItemAlignment.Top))
                        {
                            SetX(x);
                            SetY(y);
                            PositionChanged?.Invoke();
                        }
                        SetWidth(w);
                        SetHeight(h);
                        SizeChanged?.Invoke();
                    }
                    SetConfines();
                    break;
            }
        }

        public void GetSides(float xpos, float ypos)
        {
            _sides = 0;
            if (xpos < 5)
            {
                _sides |= ItemAlignment.Left;
            }
            if (xpos > GetWidth() - 5)
            {
                _sides |= ItemAlignment.Right;
            }

            if (ypos < 5)
            {
                _sides |= ItemAlignment.Top;
            }
            if (ypos > GetHeight() - 5)
            {
                _sides |= ItemAlignment.Bottom;
            }
        }
    }
}