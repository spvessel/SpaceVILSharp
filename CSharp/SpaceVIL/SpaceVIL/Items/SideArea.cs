using System;
using System.Drawing;

using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{

    public class SideArea : Prototype
    {
        static int count = 0;
        private ButtonCore _close;
        public ResizableItem window;

        private Side _attachSide = Side.Left;

        public Side getAttachSide()
        {
            return _attachSide;
        }

        public void SetAttachSide(Side side)
        {
            if (_attachSide == side)
                return;

            _attachSide = side;
            ApplyAttach();
        }

        private void ApplyAttach()
        {
            window.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            window.SetAlignment(ItemAlignment.Top, ItemAlignment.Left);
            window.IsWResizable = false;
            window.IsHResizable = false;
            window.ClearExcludedSides();

            switch (_attachSide)
            {
                case Side.Left:
                    window.IsWResizable = true;
                    window.SetWidthPolicy(SizePolicy.Fixed);
                    window.SetWidth(_size);
                    window.ExcludeSides(Side.Left, Side.Bottom, Side.Top);
                    window.SetAlignment(ItemAlignment.Left);
                    window.SetShadow(5, 3, 0, Color.FromArgb(150, 0, 0, 0));
                    break;

                case Side.Top:
                    window.IsHResizable = true;
                    window.SetHeightPolicy(SizePolicy.Fixed);
                    window.SetHeight(_size);
                    window.ExcludeSides(Side.Left, Side.Right, Side.Top);
                    window.SetAlignment(ItemAlignment.Top);
                    window.SetShadow(5, 0, 3, Color.FromArgb(150, 0, 0, 0));
                    break;

                case Side.Right:
                    window.IsWResizable = true;
                    window.SetWidthPolicy(SizePolicy.Fixed);
                    window.SetWidth(_size);
                    window.ExcludeSides(Side.Right, Side.Bottom, Side.Top);
                    window.SetShadow(5, -3, 0, Color.FromArgb(150, 0, 0, 0));
                    window.SetAlignment(ItemAlignment.Right);
                    break;

                case Side.Bottom:
                    window.IsHResizable = true;
                    window.SetHeightPolicy(SizePolicy.Fixed);
                    window.SetHeight(_size);
                    window.ExcludeSides(Side.Left, Side.Right, Side.Bottom);
                    window.SetShadow(5, 0, -3, Color.FromArgb(150, 0, 0, 0));
                    window.SetAlignment(ItemAlignment.Bottom);
                    break;

                default:
                    window.SetWidth(_size);
                    window.SetAlignment(ItemAlignment.Left);
                    window.SetShadow(5, 3, 0, Color.FromArgb(150, 0, 0, 0));
                    break;
            }
        }

        private int _size = 300;

        public int getAreaSize()
        {
            return _size;
        }

        public void SetAreaSize(int size)
        {
            _size = size;
        }

        public SideArea(Side attachSide)
        {
            SetItemName("SideArea_" + count++);
            SetPassEvents(false);
            _close = new ButtonCore();
            window = new ResizableItem();
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.SideArea)));
            _attachSide = attachSide;
            ApplyAttach();
            EventMouseClick += (sender, args) =>
            {
                close();
            };
        }

        public override void InitElements()
        {
            base.AddItem(window);
            window.AddItem(_close);
            window.SetPassEvents(false);
            window.IsXFloating = false;
            window.IsYFloating = false;
            _close.EventMouseClick += (sender, args) =>
            {
                close();
            };
        }

        public override void AddItem(IBaseItem item)
        {
            window.AddItem(item);
        }

        public override void InsertItem(IBaseItem item, int index)
        {
            window.InsertItem(item, index);
        }

        public override void RemoveItem(IBaseItem item)
        {
            window.RemoveItem(item);
        }

        WindowLayout _handler = null;

        public void Show(WindowLayout handler)
        {
            _handler = handler;
            _handler.AddItem(this);
            _handler.SetFocusedItem(this);
        }

        public void close()
        {
            _handler.GetWindow().RemoveItem(this);
        }

        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
            Style inner_style = style.GetInnerStyle("window");
            if (inner_style != null)
            {
                window.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("closebutton");
            if (inner_style != null)
            {
                _close.SetStyle(inner_style);
            }
        }
    }
}