using System;
using System.Drawing;
using SpaceVIL.Core;

namespace SpaceVIL
{
    public class ResizableItem : Prototype, IDraggable
    {
        internal ItemAlignment _sides = 0;

        public EventCommonMethod PositionChanged;
        public EventCommonMethod SizeChanged;

        public bool IsLocked = false;
        public bool IsResizable = true;
        public bool IsHFloating = true;
        public bool IsVFloating = true;

        private bool _is_moved;

        static int count = 0;
        private int _pressed_x = 0;
        private int _pressed_y = 0;
        private int _x_global = 0;
        private int _y_global = 0;
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
            if (IsLocked)
                return;

            _x_global = GetX();
            _y_global = GetY();
            _pressed_x = args.Position.GetX();
            _pressed_y = args.Position.GetY();
            _diff_x = args.Position.GetX() - GetX();
            _diff_y = args.Position.GetY() - GetY();
            _width = GetWidth();
            _height = GetHeight();

            GetSides(_diff_x, _diff_y);

            if (_sides == 0)
            {
                _is_moved = true;
            }
            else
            {
                _is_moved = false;
            }
        }

        protected virtual void OnDragging(object sender, MouseArgs args)
        {
            if (IsLocked)
                return;

            int offset_x;
            int offset_y;

            switch (_is_moved)
            {
                case true:
                    if (IsHFloating)
                    {
                        offset_x = args.Position.GetX() - _diff_x;
                        SetX(offset_x);
                    }
                    if (IsVFloating)
                    {
                        offset_y = args.Position.GetY() - _diff_y;
                        SetY(offset_y);
                    }
                    SetConfines();
                    PositionChanged?.Invoke();
                    break;

                case false:
                    if (!IsResizable)
                        break;

                    int x_handler = GetX();
                    int y_handler = GetY();
                    int x_release = args.Position.GetX();
                    int y_release = args.Position.GetY();
                    int w = GetWidth();
                    int h = GetHeight();

                    if (_sides.HasFlag(ItemAlignment.Left))
                    {
                        if (!(GetMinWidth() == GetWidth() && (args.Position.GetX() - _pressed_x) >= 0))
                        {
                            int diff = _x_global - x_release;
                            x_handler = _x_global - diff;
                            w = _width + diff;
                        }
                    }
                    if (_sides.HasFlag(ItemAlignment.Right))
                    {
                        if (!(args.Position.GetX() < GetMinWidth() && GetWidth() == GetMinWidth()))
                            w = x_release - x_handler;
                    }
                    if (_sides.HasFlag(ItemAlignment.Top))
                    {
                        if (!(GetMinHeight() == GetHeight() && (args.Position.GetY() - _pressed_y) >= 0))
                        {
                            int diff = _y_global - y_release;
                            y_handler = _y_global - diff;
                            h = _height + diff;
                        }
                    }
                    if (_sides.HasFlag(ItemAlignment.Bottom))
                    {
                        if (!(args.Position.GetY() < GetMinHeight() && GetHeight() == GetMinHeight()))
                            h = y_release - y_handler;
                    }

                    if (_sides != 0)
                    {
                        if (_sides.HasFlag(ItemAlignment.Left) || _sides.HasFlag(ItemAlignment.Top))
                        {
                            SetX(x_handler);
                            SetY(y_handler);
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

        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            SizeChanged?.Invoke();
        }
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            SizeChanged?.Invoke();
        }

        // public override void SetX(int _x)
        // {
        //     base.SetX(_x);
        //     PositionChanged?.Invoke();
        // }
        // public override void SetY(int _y)
        // {
        //     base.SetY(_y);
        //     PositionChanged?.Invoke();
        // }

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