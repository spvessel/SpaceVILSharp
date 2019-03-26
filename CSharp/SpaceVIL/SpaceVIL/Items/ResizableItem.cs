using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Common;

namespace SpaceVIL
{
    public class ResizableItem : Prototype, IDraggable
    {
        public List<Side> _sidesExclude = new List<Side>();

        public void ExcludeSides(params Side[] sides)
        {
            foreach (Side side in sides)
            {
                if (!_sidesExclude.Contains(side))
                    _sidesExclude.Add(side);
            }
        }

        public List<Side> GetExcludedSides()
        {
            return _sidesExclude;
        }
        public void ClearExcludedSides()
        {
            _sidesExclude.Clear();
        }

        internal Side _sides = 0;

        public EventCommonMethod PositionChanged;
        public EventCommonMethod SizeChanged;

        public override void Release()
        {
            PositionChanged = null;
            SizeChanged = null;
        }

        public bool IsLocked = false;
        public bool IsWResizable = true;
        public bool IsHResizable = true;
        public bool IsXFloating = true;
        public bool IsYFloating = true;

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

        /// <summary>
        /// Constructs a ResizableItem
        /// </summary>
        public ResizableItem()
        {
            SetItemName("ResizableItem_" + count);
            SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);

            EventMousePress += OnMousePress;
            // EventMouseClick+= OnMouseRelease;
            EventMouseDrag += OnDragging;
            EventMouseHover += OnHover;
            count++;
        }
        // private RedrawFrequency _renderFreq;
        protected virtual void OnHover(IItem sender, MouseArgs args)
        {
            if (IsLocked)
                return;

            GetSides(args.Position.GetX() - GetX(), args.Position.GetY() - GetY());

            if (_sides.HasFlag(Side.Left) || _sides.HasFlag(Side.Right))
            {
                if (_sides.HasFlag(Side.Top) || _sides.HasFlag(Side.Bottom))
                    SetCursor(EmbeddedCursor.Crosshair);
                else
                    SetCursor(EmbeddedCursor.ResizeX);
            }
            else if (_sides.HasFlag(Side.Top) || _sides.HasFlag(Side.Bottom))
            {
                if (_sides.HasFlag(Side.Left) || _sides.HasFlag(Side.Right))
                    SetCursor(EmbeddedCursor.Crosshair);
                else
                    SetCursor(EmbeddedCursor.ResizeY);
            }
            else
                SetCursor(DefaultsService.GetDefaultCursor());
        }
        protected virtual void OnMousePress(IItem sender, MouseArgs args)
        {
            if (IsLocked)
                return;
            // _renderFreq = GetHandler().GetRenderFrequency();
            // GetHandler().SetRenderFrequency(RedrawFrequency.Ultra);
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

        protected virtual void OnMouseRelease(IItem sender, MouseArgs args)
        {
            // GetHandler().SetRenderFrequency(_renderFreq);
        }

        protected virtual void OnDragging(IItem sender, MouseArgs args)
        {
            if (IsLocked)
                return;

            int offset_x;
            int offset_y;

            switch (_is_moved)
            {
                case true:
                    if (IsXFloating)
                    {
                        offset_x = args.Position.GetX() - _diff_x;
                        SetX(offset_x);
                    }
                    if (IsYFloating)
                    {
                        offset_y = args.Position.GetY() - _diff_y;
                        SetY(offset_y);
                    }
                    SetConfines();
                    PositionChanged?.Invoke();
                    break;

                case false:
                    if (!IsWResizable && !IsHResizable)
                        break;

                    int x_handler = GetX();
                    int y_handler = GetY();
                    int x_release = args.Position.GetX();
                    int y_release = args.Position.GetY();
                    int w = GetWidth();
                    int h = GetHeight();

                    if (_sides.HasFlag(Side.Left))
                    {
                        if (!(GetMinWidth() == GetWidth() && (args.Position.GetX() - _pressed_x) >= 0))
                        {
                            int diff = _x_global - x_release;
                            x_handler = _x_global - diff;
                            w = _width + diff;
                        }
                    }
                    if (_sides.HasFlag(Side.Right))
                    {
                        if (!(args.Position.GetX() < GetMinWidth() && GetWidth() == GetMinWidth()))
                            w = x_release - x_handler;
                    }
                    if (_sides.HasFlag(Side.Top))
                    {
                        if (!(GetMinHeight() == GetHeight() && (args.Position.GetY() - _pressed_y) >= 0))
                        {
                            int diff = _y_global - y_release;
                            y_handler = _y_global - diff;
                            h = _height + diff;
                        }
                    }
                    if (_sides.HasFlag(Side.Bottom))
                    {
                        if (!(args.Position.GetY() < GetMinHeight() && GetHeight() == GetMinHeight()))
                            h = y_release - y_handler;
                    }

                    if (_sides != 0)
                    {
                        if (_sides.HasFlag(Side.Left) || _sides.HasFlag(Side.Top))
                        {
                            SetX(x_handler);
                            SetY(y_handler);
                            PositionChanged?.Invoke();
                        }
                        bool flag = false;
                        if (IsWResizable && w != GetWidth())
                        {
                            SetWidth(w);
                            flag = true;
                        }
                        if (IsHResizable && h != GetHeight())
                            SetHeight(h);
                        if (flag)
                            SizeChanged?.Invoke();
                    }
                    SetConfines();
                    break;
            }
        }

        /// <summary>
        /// Set width of the ResizableItem
        /// </summary>
        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            SizeChanged?.Invoke();
        }

        /// <summary>
        /// Set height of the ResizableItem
        /// </summary>
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            SizeChanged?.Invoke();
        }

        internal void GetSides(float xpos, float ypos)
        {
            _sides = 0;
            if (xpos <= 10 && !_sidesExclude.Contains(Side.Left))
            {
                _sides |= Side.Left;
            }
            if (xpos >= GetWidth() - 10 && !_sidesExclude.Contains(Side.Right))
            {
                _sides |= Side.Right;
            }

            if (ypos <= 10 && !_sidesExclude.Contains(Side.Top))
            {
                _sides |= Side.Top;
            }
            if (ypos >= GetHeight() - 10 && !_sidesExclude.Contains(Side.Bottom))
            {
                _sides |= Side.Bottom;
            }
        }
    }
}