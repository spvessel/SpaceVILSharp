using System;
using System.Drawing;

using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{

    public class SideArea : Prototype, IFloating
    {
        static int count = 0;
        private bool _init = false;

        private ButtonCore _close;
        public ResizableItem Window;

        private Side _attachSide = Side.Left;

        public Side GetAttachSide()
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

            Color shadowColor = Color.FromArgb(150, 0, 0, 0);
            int shadowRadius = 5;
            int shadowIndent = 3;

            switch (_attachSide)
            {
                case Side.Left:
                    Window.IsWResizable = true;
                    Window.SetWidthPolicy(SizePolicy.Fixed);
                    Window.SetWidth(_size);
                    Window.ExcludeSides(Side.Left, Side.Bottom, Side.Top);
                    Window.SetAlignment(ItemAlignment.Left);
                    Window.SetShadow(shadowRadius, shadowIndent, 0, shadowColor);
                    break;

                case Side.Top:
                    Window.IsHResizable = true;
                    Window.SetHeightPolicy(SizePolicy.Fixed);
                    Window.SetHeight(_size);
                    Window.ExcludeSides(Side.Left, Side.Right, Side.Top);
                    Window.SetAlignment(ItemAlignment.Top);
                    Window.SetShadow(shadowRadius, 0, shadowIndent, shadowColor);
                    break;

                case Side.Right:
                    Window.IsWResizable = true;
                    Window.SetWidthPolicy(SizePolicy.Fixed);
                    Window.SetWidth(_size);
                    Window.ExcludeSides(Side.Right, Side.Bottom, Side.Top);
                    Window.SetShadow(shadowRadius, -shadowIndent, 0, shadowColor);
                    Window.SetAlignment(ItemAlignment.Right);
                    break;

                case Side.Bottom:
                    Window.IsHResizable = true;
                    Window.SetHeightPolicy(SizePolicy.Fixed);
                    Window.SetHeight(_size);
                    Window.ExcludeSides(Side.Left, Side.Right, Side.Bottom);
                    Window.SetShadow(shadowRadius, 0, -shadowIndent, shadowColor);
                    Window.SetAlignment(ItemAlignment.Bottom);
                    break;

                default:
                    Window.SetWidth(_size);
                    Window.SetAlignment(ItemAlignment.Left);
                    Window.SetShadow(shadowRadius, shadowIndent, 0, shadowColor);
                    break;
            }
        }

        private int _size = 300;

        public int GetAreaSize()
        {
            return _size;
        }

        public void SetAreaSize(int size)
        {
            if (size == _size)
                return;
            _size = size;
            ApplyAttach();
        }

        public SideArea(CoreWindow handler, Side attachSide)
        {
            ItemsLayoutBox.AddItem(handler, this, LayoutType.Floating);

            SetItemName("SideArea_" + count++);
            _close = new ButtonCore();
            Window = new ResizableItem();
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.SideArea)));
            _attachSide = attachSide;
            ApplyAttach();
            EventMouseClick += (sender, args) =>
            {
                Hide();
            };
            SetVisible(false);
            SetPassEvents(false);

            EventKeyPress += (sender, args) =>
            {
                if (args.Key == KeyCode.Escape)
                    Hide();
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
                Hide();
            };

            _init = true;
        }

        public override void AddItem(IBaseItem item)
        {
            Window.AddItem(item);
        }

        public override void InsertItem(IBaseItem item, int index)
        {
            Window.InsertItem(item, index);
        }

        public override bool RemoveItem(IBaseItem item)
        {
            return Window.RemoveItem(item);
        }

        public override void SetWidth(int width)
        {
            base.SetWidth(width);
        }

        public override void SetHeight(int height)
        {
            base.SetHeight(height);
        }

        public virtual void Show()
        {
            if (!_init)
                InitElements();
            SetVisible(true);
            SetFocus();
        }
        public virtual void Show(IItem sender, MouseArgs args)
        {
            Show();
        }

        public virtual void Hide()
        {
            SetVisible(false);
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

        private bool _ouside = false;
        public bool IsOutsideClickClosable()
        {
            return _ouside;
        }
        public void SetOutsideClickClosable(bool value)
        {
            _ouside = value;
        }
    }
}