using System;
using System.Collections.Generic;
using SpaceVIL.Core;
using SpaceVIL.Common;

namespace SpaceVIL
{
    /// <summary>
    /// ResisableItem is a special container that can move and resize 
    /// by mouse input events while inside another container. 
    /// <para/> Te get full functionality try to use ResizableItem with 
    /// SpaceVIL.FreeArea container.
    /// <para/> Supports all events including drag and drop.
    /// </summary>
    public class ResizableItem : Prototype, IDraggable
    {
        private List<Side> _sidesExclude = new List<Side>();

        /// <summary>
        /// Specify which sides will be excluded, and these sides 
        /// can no longer be dragged to resize the ResizableItem.
        /// </summary>
        /// <param name="sides">Sides for exclusion as sequence of SpaceVIL.Core.Side.</param>
        public void ExcludeSides(params Side[] sides)
        {
            foreach (Side side in sides)
            {
                if (!_sidesExclude.Contains(side))
                    _sidesExclude.Add(side);
            }
        }

        /// <summary>
        /// Getting exclused sides. These sides cannot be dragged 
        /// to resize the ResizableItem.
        /// </summary>
        /// <returns>Sides for exclusion as list of SpaceVIL.Core.Side.</returns>
        public List<Side> GetExcludedSides()
        {
            return _sidesExclude;
        }

        /// <summary>
        /// Removing all exclused sides. After that all sides 
        /// can be dragged to resize the ResizableItem.
        /// </summary>
        public void ClearExcludedSides()
        {
            _sidesExclude.Clear();
        }

        internal Side _sides = 0;

        /// <summary>
        /// Event that is invoked when ResizableItem moves.
        /// </summary>
        public EventCommonMethod PositionChanged;

        /// <summary>
        /// Event that is invoked when ResizableItem resizes.
        /// </summary>
        public EventCommonMethod SizeChanged;

        /// <summary>
        ///  Disposing all resources if the item was removed.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void Release()
        {
            PositionChanged = null;
            SizeChanged = null;
        }

        /// <summary>
        /// Property to lock ResizableItem movement and resizing.
        /// <para/> True: to lock. False: to unlock.
        /// <para/> Default: False.
        /// </summary>
        public bool IsLocked = false;

        /// <summary>
        /// Property to lock ResizableItem resizing by X axis.
        /// <para/> True: to unlock. False: to lock.
        /// <para/> Default: True.
        /// </summary>
        public bool IsXResizable = true;

        /// <summary>
        /// Property to lock ResizableItem resizing by Y axis.
        /// <para/> True: to unlock. False: to lock.
        /// <para/> Default: True.
        /// </summary>
        public bool IsYResizable = true;

        /// <summary>
        /// Property to lock ResizableItem movement by X axis.
        /// <para/> True: to unlock. False: to lock.
        /// <para/> Default: True.
        /// </summary>
        public bool IsXFloating = true;

        /// <summary>
        /// Property to lock ResizableItem movement by Y axis.
        /// <para/> True: to unlock. False: to lock.
        /// <para/> Default: True.
        /// </summary>
        public bool IsYFloating = true;
        private bool _isMoved;
        static int count = 0;
        private int _pressedX = 0;
        private int _pressedY = 0;
        private int _globalX = 0;
        private int _globalY = 0;
        private int _width = 0;
        private int _height = 0;
        private int _diffX = 0;
        private int _diffY = 0;

        /// <summary>
        /// Default ResizableItem constructor.
        /// </summary>
        public ResizableItem()
        {
            SetItemName("ResizableItem_" + count++);
            SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);

            EventMousePress += OnMousePress;
            EventMouseDrag += OnDragging;
            EventMouseHover += OnHover;
        }

        protected virtual void OnHover(IItem sender, MouseArgs args)
        {
            if (IsLocked)
            {
                SetCursor(DefaultsService.GetDefaultCursor());
                return;
            }

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
            {
                SetCursor(DefaultsService.GetDefaultCursor());
            }
        }

        protected virtual void OnMousePress(IItem sender, MouseArgs args)
        {
            if (IsLocked)
                return;

            _globalX = GetX();
            _globalY = GetY();
            _pressedX = args.Position.GetX();
            _pressedY = args.Position.GetY();
            _diffX = args.Position.GetX() - GetX();
            _diffY = args.Position.GetY() - GetY();
            _width = GetWidth();
            _height = GetHeight();

            GetSides(_diffX, _diffY);

            if (_sides == 0)
            {
                _isMoved = true;
            }
            else
            {
                _isMoved = false;
            }
        }

        protected virtual void OnDragging(IItem sender, MouseArgs args)
        {
            if (IsLocked)
                return;

            int offset_x;
            int offset_y;

            switch (_isMoved)
            {
                case true:
                    if (IsXFloating)
                    {
                        offset_x = args.Position.GetX() - _diffX;
                        SetX(offset_x);
                    }
                    if (IsYFloating)
                    {
                        offset_y = args.Position.GetY() - _diffY;
                        SetY(offset_y);
                    }
                    SetConfines();
                    PositionChanged?.Invoke();
                    break;

                case false:
                    if (!IsXResizable && !IsYResizable)
                        break;

                    int x_handler = GetX();
                    int y_handler = GetY();
                    int x_release = args.Position.GetX();
                    int y_release = args.Position.GetY();
                    int w = GetWidth();
                    int h = GetHeight();

                    if (_sides.HasFlag(Side.Left))
                    {
                        if (!(GetMinWidth() == GetWidth() && (args.Position.GetX() - _pressedX) >= 0))
                        {
                            int diff = _globalX - x_release;
                            x_handler = _globalX - diff;
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
                        if (!(GetMinHeight() == GetHeight() && (args.Position.GetY() - _pressedY) >= 0))
                        {
                            int diff = _globalY - y_release;
                            y_handler = _globalY - diff;
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
                        if (IsXResizable && w != GetWidth())
                        {
                            SetWidth(w);
                            flag = true;
                        }
                        if (IsYResizable && h != GetHeight())
                            SetHeight(h);
                        if (flag)
                            SizeChanged?.Invoke();
                    }
                    SetConfines();
                    break;
            }
        }

        /// <summary>
        /// Setting ResizableItem width. If the value is greater/less than the maximum/minimum 
        /// value of the width, then the width becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="width">Width of the ResizableItem.</param>
        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            SizeChanged?.Invoke();
        }

        /// <summary>
        /// Setting ResizableItem height. If the value is greater/less than the maximum/minimum 
        /// value of the height, then the height becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="height">Height of the ResizableItem.</param>
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            SizeChanged?.Invoke();
        }

        internal void GetSides(float xpos, float ypos)
        {
            _sides = 0;
            if (xpos <= SpaceVILConstants.BorderCursorTolerance && !_sidesExclude.Contains(Side.Left))
            {
                _sides |= Side.Left;
            }
            if (xpos >= GetWidth() - SpaceVILConstants.BorderCursorTolerance && !_sidesExclude.Contains(Side.Right))
            {
                _sides |= Side.Right;
            }

            if (ypos <= SpaceVILConstants.BorderCursorTolerance && !_sidesExclude.Contains(Side.Top))
            {
                _sides |= Side.Top;
            }
            if (ypos >= GetHeight() - SpaceVILConstants.BorderCursorTolerance && !_sidesExclude.Contains(Side.Bottom))
            {
                _sides |= Side.Bottom;
            }
        }
    }
}