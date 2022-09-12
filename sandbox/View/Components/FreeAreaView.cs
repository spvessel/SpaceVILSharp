using System.Collections.Generic;
using SpaceVIL;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using View.Decorations;
using Color = System.Drawing.Color;

namespace View.Components
{
    public class FreeAreaView : Frame
    {
        internal List<IBaseItem> content = new List<IBaseItem>();
        internal void UnfocusContent()
        {
            if (content.Count > 0)
            {
                Prototype focusedItem = ((Prototype) content[content.Count - 1]);
                focusedItem.SetBorderThickness(0);
            }
        }

        public override void InitElements()
        {
            base.InitElements();

            FreeArea area = new FreeArea();
            area.SetMargin(2, 2, 2, 2);
            area.SetBackground(Palette.Transparent);
            area.EventMousePress += (sender, args) => {
                UnfocusContent();
            };

            Toolbar toolbar = new Toolbar(area, this);
            AddItems(area, toolbar);
        }
    }

    internal class Toolbar : BlankItem {
        FreeAreaView root;
        FreeArea view;
        ToolbarPalette palette = new ToolbarPalette();

        internal Toolbar(FreeArea view, FreeAreaView root)
        {
            this.view = view;
            this.root = root;
            SetBackground(Palette.GrayDark);
            SetBorderRadius(3);
            SetBorder(new Border(Palette.WhiteGlass, new CornerRadius(3), 1));
            Effects().Add(new Shadow(8, Palette.BlackShadow));
            SetAlignment(ItemAlignment.HCenter, ItemAlignment.Top);
            SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            SetSize(256, 46);
            SetMargin(0, 10, 0, 0);
            SetPadding(6, 4, 4, 4);
            SetPassEvents(false);
        }

        public override void InitElements()
        {
            base.InitElements();
            HorizontalStack layout = new HorizontalStack();
            layout.SetSpacing(5, 0);
            AddItem(layout);
            
            ToolbarButton btnTriangle = new ToolbarButton(new Triangle(), view);
            btnTriangle.Action = () => {
                root.UnfocusContent();
                view.AddItem(GetContainer(applyLAF(new Triangle(), palette.GetColor())));
            };
            ToolbarButton btnRectangle = new ToolbarButton(new Rectangle(), view);
            btnRectangle.Action = () => {
                root.UnfocusContent();
                view.AddItem(GetContainer(applyLAF(new Rectangle(new CornerRadius(6)), palette.GetColor())));
            };
            ToolbarButton btnEllipse = new ToolbarButton(new Ellipse(), view);
            btnEllipse.Action = () => {
                root.UnfocusContent();
                view.AddItem(GetContainer(applyLAF(new Ellipse(64), palette.GetColor())));
            };

            
            layout.AddItems(
                btnTriangle,
                btnRectangle,
                btnEllipse,
                ComponentsFactory.MakeFrame(),
                ComponentsFactory.MakeVSpacer(1),
                palette
            );
        }

        private ResizableItem GetContainer(Primitive item)
        {
            CustomResizableItem container = new CustomResizableItem(item);
            container.SetBackground(Palette.Transparent);
            container.SetSize(200, 200);
            container.SetPosition(100, 100);
            container.SetPadding(5, 5, 5, 5);
            container.SetBorder(new Border(Palette.WhiteGlass, new CornerRadius(3), 1));
            container.EventMousePress += (sender, args) => {
                root.UnfocusContent();
                root.content.Remove(container);
                root.content.Add(container);
                view.SetContent(root.content);
                container.SetBorderThickness(1);
            };
            root.content.Add(container);
            return container;
        }

        private Primitive applyLAF(Primitive primitive, Color color)
        {
            primitive.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            primitive.SetBackground(color);
            return primitive;
        }
    }


    internal class ToolbarButton : BlankItem {
        internal EventCommonMethod Action;
        internal Primitive Icon;
        
        internal ToolbarButton(Primitive icon, FreeArea view)
        {
            this.Icon = icon;
            icon.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            icon.SetBackground(Palette.GrayLight);
            SetBackground(Palette.GrayDark);
            SetAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
            SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            SetSize(32, 32);
            SetPadding(6, 6, 6, 6);
            SetBorderRadius(3);
            ItemState hovered = new ItemState();
            hovered.Background = Palette.Gray;
            hovered.Border = new Border(Palette.WhiteGlass, new CornerRadius(3), 1);
            AddItemState(ItemStateType.Hovered, hovered);
            ItemState pressed = new ItemState();
            pressed.Background = Palette.Dark;
            pressed.Border = new Border(Palette.WhiteGlass, new CornerRadius(3), 1);
            AddItemState(ItemStateType.Pressed, pressed);

            EventMouseClick += (sender, args) => {
                Action.Invoke();
            };
        }

        public override void InitElements()
        {
            base.InitElements();
            AddItem(Icon);
        }
    }

    internal class ToolbarPalette : BlankItem {
        private Primitive marker;

        internal ToolbarPalette()
        {
            SetBackground(Palette.GrayDark);
            SetAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
            SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            SetSize(32, 32);
            SetPadding(6, 6, 6, 6);
            SetBorderRadius(3);
            ItemState hovered = new ItemState();
            hovered.Background = Palette.Gray;
            hovered.Border = new Border(Palette.WhiteGlass, new CornerRadius(3), 1);
            AddItemState(ItemStateType.Hovered, hovered);
            ItemState pressed = new ItemState();
            pressed.Background = Palette.Dark;
            pressed.Border = new Border(Palette.WhiteGlass, new CornerRadius(3), 1);
            AddItemState(ItemStateType.Pressed, pressed);
            marker = new Ellipse();
            marker.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        }
        
        public override void InitElements()
        {
            base.InitElements();
            SetColor(Palette.Blue);
            AddItem(marker);

            ContextMenu menu = ComponentsFactory.MakeContextMenu(
                GetHandler(),
                ComponentsFactory.MakeMenuItem("Red", () => { SetColor(Palette.RedLight); }),
                ComponentsFactory.MakeMenuItem("Green", () => { SetColor(Palette.Green); }),
                ComponentsFactory.MakeMenuItem("Blue", () => { SetColor(Palette.Blue); }),
                ComponentsFactory.MakeMenuItem("Pink", () => { SetColor(Palette.Pink); }),
                ComponentsFactory.MakeMenuItem("Purple", () => { SetColor(Palette.Purple); }),
                ComponentsFactory.MakeMenuItem("Orange", () => { SetColor(Palette.Orange); })
            );
            menu.ActiveButton = MouseButton.ButtonLeft;
            SetToolTip("Palette");

            EventMouseClick += (sender, args) => {
                args.Position.SetPosition(GetX(), GetY() + GetHeight());
                menu.Show(sender, args);
            };
        }

        internal void SetColor(Color color)
        {
            marker.SetBackground(color);
        }

        internal Color GetColor()
        {
            return marker.GetBackground();
        }
    }

    internal class CustomResizableItem : ResizableItem {
        private IBaseItem _item = null;
        internal CustomResizableItem(IBaseItem item)
        {
            _item = item;
        }
        public override void InitElements()
        {
            base.InitElements();
            AddItem(_item);
        }
    }
}