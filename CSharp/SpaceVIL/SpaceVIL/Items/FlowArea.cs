using System;
using System.Collections.Generic;
using System.Linq;
using System.Drawing;

namespace SpaceVIL
{
    public class FlowArea : VisualItem, IFlow, IDraggable
    {
        static int count = 0;
        Dictionary<BaseItem, int[]> _stored_crd;
        public ContextMenu _dropdownmenu = new ContextMenu();
        public FlowArea()
        {
            SetItemName("FlowArea_" + count);
            count++;
            EventMouseClick += OnMouseRelease;
            EventMousePressed += OnMousePress;
            EventMouseDrag += OnDragging;
            _stored_crd = new Dictionary<BaseItem, int[]>();
            PrepElements();
        }

        protected virtual void OnMousePress(object sender, MouseArgs args)
        {
            if (!_dropdownmenu.Handler.IsClosed)
            {
                foreach (var item in _dropdownmenu.GetListContent())
                {
                    (item as MenuItem)._dropdownmenu?.Close();
                }
                _dropdownmenu.Close();
            }
        }
        protected virtual void OnMouseRelease(object sender, MouseArgs args)
        {
            //PrintArgs.MouseArgs(args);
            if (args.Button == MouseButton.ButtonRight)
                ShowDropDownList(args);
        }

        protected virtual void OnDragging(object sender, MouseArgs args)
        {
            _xOffset -= _mouse_ptr.PrevX - _mouse_ptr.X;
            _yOffset -= _mouse_ptr.PrevY - _mouse_ptr.Y;
            UpdateLayout();
        }

        private Int64 _yOffset = 0;
        private Int64 _xOffset = 0;
        public Int64 GetVScrollOffset()
        {
            return _yOffset;
        }
        public void SetVScrollOffset(Int64 value)
        {
            _yOffset = value;
            UpdateLayout();
        }
        public Int64 GetHScrollOffset()
        {
            return _xOffset;
        }
        public void SetHScrollOffset(Int64 value)
        {
            _xOffset = value;
            UpdateLayout();
        }

        //overrides
        public override void AddItem(BaseItem item)
        {
            base.AddItem(item);
            _stored_crd.Add(item, new int[] { item.GetX(), item.GetY() });
            UpdateLayout();
        }
        public override void RemoveItem(BaseItem item)
        {
            base.AddItem(item);
            _stored_crd.Remove(item);
            UpdateLayout();
        }
        public void UpdateLayout()
        {
            foreach (var child in GetItems())
            {
                child.SetX((int)_xOffset + GetX() + GetPadding().Left + _stored_crd[child][0] + child.GetMargin().Left);
                child.SetY((int)_yOffset + GetY() + GetPadding().Top + +_stored_crd[child][1] + child.GetMargin().Top);
            }
        }

        //ContexMenu
        public void PrepElements()
        {
            Font font = new Font(new FontFamily("Courier New"), 16, FontStyle.Regular);

            _dropdownmenu.Handler.SetWidth(110);
            _dropdownmenu.Handler.SetHeight(105);

            ContextMenu menu = new ContextMenu();
            menu.Handler.SetWidth(110);
            menu.Handler.SetHeight(55);

            MenuItem x_minus = new MenuItem("X - 10px");
            x_minus.SetFont(font);
            x_minus.SetHeight(25);
            x_minus.SetPadding(6);
            x_minus.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(255, 180, 180, 180)
            });
            x_minus.EventMouseClick += (SetHandler, args) =>
            {
                _xOffset -= 10;
                _dropdownmenu?.Close();
                UpdateLayout();
            };
            MenuItem y_minus = new MenuItem("Y - 10px");
            y_minus.SetFont(font);
            y_minus.SetHeight(25);
            y_minus.SetPadding(6);
            y_minus.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(255, 180, 180, 180)
            });
            y_minus.EventMouseClick += (SetHandler, args) =>
            {
                _yOffset -= 10;
                _dropdownmenu?.Close();
                UpdateLayout();
            };
            menu.Add(x_minus);
            menu.Add(y_minus);

            MenuItem restore = new MenuItem("Restore");
            restore.SetFont(font);
            restore.SetHeight(25);
            restore.SetPadding(6);
            restore.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(255, 180, 180, 180)
            });
            restore.EventMouseClick += (SetHandler, args) =>
            {
                _xOffset = 0;
                _yOffset = 0;
                UpdateLayout();
            };
            restore.EventMouseHover += (sender, args) => menu.Close();
            AddToList(restore);
            MenuItem x_plus = new MenuItem("X + 10px");
            x_plus.SetFont(font);
            x_plus.SetHeight(25);
            x_plus.SetPadding(6);
            x_plus.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(255, 180, 180, 180)
            });
            x_plus.EventMouseClick += (SetHandler, args) =>
            {
                _xOffset += 10;
                UpdateLayout();
            };
            x_plus.EventMouseHover += (sender, args) => menu.Close();
            AddToList(x_plus);
            MenuItem y_plus = new MenuItem("Y + 10px");
            y_plus.SetFont(font);
            y_plus.SetHeight(25);
            y_plus.SetPadding(6);
            y_plus.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(255, 180, 180, 180)
            });
            y_plus.EventMouseClick += (SetHandler, args) =>
            {
                _yOffset += 10;
                UpdateLayout();
            };
            y_plus.EventMouseHover += (sender, args) => menu.Close();
            AddToList(y_plus);

            MenuItem addidion_menu = new MenuItem("Addition");
            addidion_menu.IsActionItem = true;
            addidion_menu.SetFont(font);
            addidion_menu.SetHeight(25);
            addidion_menu.SetPadding(6);
            addidion_menu.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(255, 180, 180, 180)
            });
            addidion_menu._dropdownmenu = menu;

            addidion_menu.EventMouseHover += (sender, args) =>
            {
                if (menu.Handler.IsClosed)
                {
                    menu.Handler.SetX(
                        _dropdownmenu.Handler.GetWidth() +
                        _dropdownmenu.Handler.GetX()
                        );
                    menu.Handler.SetY(
                        addidion_menu.GetY() +
                        _dropdownmenu.Handler.GetY()
                        );
                    menu.Show();
                }
            };
            AddToList(addidion_menu);
            CustomShape _arrow = new CustomShape();
            _arrow.SetTriangles(GraphicsMathService.GetTriangle(a: 90));
            _arrow.SetBackground(50, 50, 50);
            _arrow.SetSize(6, 10);
            _arrow.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            _arrow.SetAlignment(ItemAlignment.Right | ItemAlignment.VCenter);
            _arrow.SetMargin(0, 0, 5, 0);
            addidion_menu.AddItem(_arrow);
        }

        private void ShowDropDownList(MouseArgs args)
        {
            //dropdownarea
            _dropdownmenu.Handler.SetX(GetHandler().GetX() + args.Position.X);
            _dropdownmenu.Handler.SetY(GetHandler().GetY() + args.Position.Y);
            _dropdownmenu.Show();
        }

        public void AddToList(BaseItem item)
        {
            _dropdownmenu.Add(item);
        }
        public void RemoveFromList(BaseItem item)
        {
            _dropdownmenu.Remove(item);
        }
        public void SetCurrentIndex(int index)
        {
            _dropdownmenu.SetCurrentIndex(index);
        }
    }
}
