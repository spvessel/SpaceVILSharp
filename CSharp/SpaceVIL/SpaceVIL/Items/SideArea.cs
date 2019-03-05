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
        public ResizableItem Window;

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
            Window.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            Window.SetAlignment(ItemAlignment.Top, ItemAlignment.Left);
            Window.IsWResizable = false;
            Window.IsHResizable = false;
            Window.ClearExcludedSides();

            switch (_attachSide)
            {
                case Side.Left:
                    Window.IsWResizable = true;
                    Window.SetWidthPolicy(SizePolicy.Fixed);
                    Window.SetWidth(_size);
                    Window.ExcludeSides(Side.Left, Side.Bottom, Side.Top);
                    Window.SetAlignment(ItemAlignment.Left);
                    Window.SetShadow(5, 3, 0, Color.FromArgb(150, 0, 0, 0));
                    break;

                case Side.Top:
                    Window.IsHResizable = true;
                    Window.SetHeightPolicy(SizePolicy.Fixed);
                    Window.SetHeight(_size);
                    Window.ExcludeSides(Side.Left, Side.Right, Side.Top);
                    Window.SetAlignment(ItemAlignment.Top);
                    Window.SetShadow(5, 0, 3, Color.FromArgb(150, 0, 0, 0));
                    break;

                case Side.Right:
                    Window.IsWResizable = true;
                    Window.SetWidthPolicy(SizePolicy.Fixed);
                    Window.SetWidth(_size);
                    Window.ExcludeSides(Side.Right, Side.Bottom, Side.Top);
                    Window.SetShadow(5, -3, 0, Color.FromArgb(150, 0, 0, 0));
                    Window.SetAlignment(ItemAlignment.Right);
                    break;

                case Side.Bottom:
                    Window.IsHResizable = true;
                    Window.SetHeightPolicy(SizePolicy.Fixed);
                    Window.SetHeight(_size);
                    Window.ExcludeSides(Side.Left, Side.Right, Side.Bottom);
                    Window.SetShadow(5, 0, -3, Color.FromArgb(150, 0, 0, 0));
                    Window.SetAlignment(ItemAlignment.Bottom);
                    break;

                default:
                    Window.SetWidth(_size);
                    Window.SetAlignment(ItemAlignment.Left);
                    Window.SetShadow(5, 3, 0, Color.FromArgb(150, 0, 0, 0));
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
            Window = new ResizableItem();
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
            base.AddItem(Window);
            Window.AddItem(_close);
            Window.SetPassEvents(false);
            Window.IsXFloating = false;
            Window.IsYFloating = false;
            _close.EventMouseClick += (sender, args) =>
            {
                close();
            };
        }

        public override void AddItem(IBaseItem item)
        {
            Window.AddItem(item);
        }

        public override void InsertItem(IBaseItem item, int index)
        {
            Window.InsertItem(item, index);
        }

        public override void RemoveItem(IBaseItem item)
        {
            Window.RemoveItem(item);
        }

        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            if (width < Window.GetWidth())
                Window.SetWidth(width);
        }

        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            if (height < Window.GetHeight())
                Window.SetHeight(height);
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
                Window.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("closebutton");
            if (inner_style != null)
            {
                _close.SetStyle(inner_style);
            }
        }
    }
}