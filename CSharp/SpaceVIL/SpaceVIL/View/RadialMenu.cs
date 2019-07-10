using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL;
using SpaceVIL.Decorations;
using SpaceVIL.Core;

namespace RadialMenu
{
    public class RadialMenuItem : Prototype, IDraggable, IFloating, IFreeLayout
    {
        private bool _init = false;
        public Int32 Radius = 150;
        public Int32 ItemRadius = 30;
        private List<ButtonCore> _menuItems = new List<ButtonCore>();

        public ButtonCore RadialMenuButton = new ButtonCore("Close");

        public RadialMenuItem(CoreWindow wnd)
        {
            //IFloating
            ItemsLayoutBox.AddItem(wnd, this, LayoutType.Floating);

            //Parameters
            SetItemName("Radial");
            SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            SetPosition(0, 0);
            SetBackground(0, 0, 0, 120);
            SetVisible(false);
            SetPassEvents(false);
        }

        double alphaStep = Math.PI;
        double distance = 20;
        public override void InitElements()
        {
            // alphaStep = 2 * Math.PI / 12;

            alphaStep = Math.Acos(1 - Math.Pow(2 * ItemRadius + distance, 2) / (2 * Math.Pow(Radius - ItemRadius * Math.Sqrt(2), 2)));

            RadialMenuButton.SetFontStyle(FontStyle.Bold);
            RadialMenuButton.SetBackground(121, 223, 152);
            RadialMenuButton.SetSize(80, 80);
            RadialMenuButton.SetBorderRadius(40);
            RadialMenuButton.SetShadow(5, 2, 2, Color.FromArgb(150, 0, 0, 0));

            //Adding
            base.AddItem(RadialMenuButton);

            //Events
            RadialMenuButton.EventMouseClick += (sender, args) =>
            {
                Hide();
                GetHandler().SetFocus();
            };
            EventMouseDrag += OnMouseDrag;
            EventMousePress += OnMousePress;
            _init = true;
        }

        Pointer _beginPos = new Pointer();
        private void OnMousePress(IItem sender, MouseArgs args)
        {
            _beginPos.SetPosition(args.Position.GetX(), args.Position.GetY());
            int xCenter = GetWidth() / 2 + GetX();
            int yCenter = GetHeight() / 2 + GetY();
            indent = Math.Atan2(_beginPos.GetY() - yCenter, _beginPos.GetX() - xCenter);
            if (_beginPos.GetY() - yCenter < 0)
                indent += Math.PI;
        }

        Pointer _dragPos = new Pointer();
        private void OnMouseDrag(IItem sender, MouseArgs args)
        {
            _dragPos.SetPosition(args.Position.GetX(), args.Position.GetY());
            int xCenter = GetWidth() / 2 + GetX();
            int yCenter = GetHeight() / 2 + GetY();
            double indentTmp = Math.Atan2(_dragPos.GetY() - yCenter, _dragPos.GetX() - xCenter);
            if (_dragPos.GetY() - yCenter < 0)
                indentTmp += Math.PI;

            diff = indentTmp - indent;
            // RearrangeButtons();
        }
        double alpha = Math.PI;
        double indent = 0;
        double diff = 0;
        private void RearrangeButtons()
        {
            int xCenter = GetWidth() / 2 + GetX();
            int yCenter = GetHeight() / 2 + GetY();

            alpha += diff;

            foreach (var item in _menuItems)
            {
                int x = (int)(Radius * Math.Cos(alpha)) + xCenter - ItemRadius;
                int y = (int)(Radius * Math.Sin(alpha)) + yCenter - ItemRadius;
                item.SetPosition(x, y);
                alpha += alphaStep;
                item.SetConfines();
            }
        }

        public override void AddItem(IBaseItem item)
        {
            ButtonCore btn = item as ButtonCore;
            if (btn != null)
            {
                btn.SetSize(ItemRadius * 2, ItemRadius * 2);
                btn.SetBorderRadius(ItemRadius);
                btn.SetShadow(8, 2, 3, Color.FromArgb(150, 0, 0, 0));
                btn.EventMouseClick += (sender, args) =>
                {
                    Console.WriteLine(btn.GetItemName());
                };
                _menuItems.Add(btn);
            }
            UpdateLayout();
        }

        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            UpdateLayout();
        }

        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            UpdateLayout();
        }

        int count = 0;
        public void UpdateLayout()
        {
            // Console.WriteLine("Update " + count++);
            RadialMenuButton.SetX(GetWidth() / 2 + GetX() - RadialMenuButton.GetWidth() / 2);
            RadialMenuButton.SetY(GetHeight() / 2 + GetY() - RadialMenuButton.GetHeight() / 2);
            RearrangeButtons();
        }

        public void Hide()
        {
            SetVisible(false);
        }
        public void Show()
        {
            if (!_init)
                InitElements();
            Console.WriteLine(GetWidth() + " " + GetHeight());
            SetVisible(true);
            SetFocus();
        }

        public void Show(IItem sender, MouseArgs args)
        {
            Show();
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