using System.Drawing;
using SpaceVIL;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using View.Decorations;

namespace View.Components
{
    public static class ComponentsFactory
    {
        public static Prototype MakeTitleBar(string title, Bitmap image)
        {
            TitleBar titleBar = new TitleBar(title);
            titleBar.Effects().Add(new Shadow(8));
            titleBar.SetIcon(image, 24, 24);
            return titleBar;
        }

        public static Prototype MakeVerticalStack(int spacing)
        {
            VerticalStack stack = new VerticalStack();
            stack.SetBackground(55, 55, 55);
            stack.SetSpacing(0, spacing);
            return stack;
        }

        public static Prototype MakeFrame()
        {
            return new Frame();
        }

        public static VerticalSplitArea MakeSplitArea(int position)
        {
            VerticalSplitArea splitArea = new VerticalSplitArea();
            splitArea.SetSplitPosition(300);
            splitArea.SetSplitColor(Palette.Gray);
            splitArea.SetSplitThickness(4);
            return splitArea;
        }

        public static ListBox MakeList()
        {
            ListBox list = new ListBox();
            list.SetBackground(50, 50, 50);
            list.SetSelectionVisible(false);
            list.GetArea().SetPadding(10, 30, 10, 5);
            list.GetArea().SetSpacing(0, 16);
            list.SetMinWidth(100);
            return list;
        }

        public static ButtonCore MakeActionButton(string text, EventCommonMethod action)
        {
            return MakeActionButton(text, Palette.Orange, action);
        }

        public static ButtonCore MakeActionButton(string text, Color color, EventCommonMethod action)
        {
            ButtonCore button = new ButtonCore(text);
            button.SetBackground(color);
            button.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            button.SetMaxWidth(220);
            button.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            button.Effects().Add(new Shadow(5, new Position(3, 3), Palette.BlackShadow));
            button.EventMouseClick += (sender, args) => {
                action.Invoke();
            };
            return button;
        }

        public static Label MakeHeaderLabel(string text)
        {
            Label label = new Label(text);
            label.SetFontSize(24);
            label.SetMargin(2, 30, 2, 0);
            label.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            label.SetHeight(label.GetTextHeight() + 20);
            label.SetTextAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            return label;
        }
        
        public static Prototype MakeFixedLabel(string text, int width, int height)
        {
            Label label = new Label(text);
            label.SetFontSize(18);
            label.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            label.SetSize(width, height);
            label.SetBackground(Palette.GreenLight);
            label.SetForeground(Palette.Black);
            label.SetTextAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            label.Effects().Add(new Shadow(5, new Position(3, 3), Palette.BlackShadow));
            return label;
        }
        
        public static Prototype MakeExpandedLabel(string text)
        {
            Label label = new Label(text);
            label.SetFontSize(18);
            label.SetBackground(Palette.GreenLight);
            label.SetForeground(Palette.Black);
            label.SetTextAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            label.Effects().Add(new Shadow(5, new Position(3, 3), Palette.BlackShadow));
            return label;
        }

        public static MenuItem MakeMenuItem(string text, EventCommonMethod action)
        {
            MenuItem mi = new MenuItem(text);
            mi.EventMouseClick += (sender, args) => {
                action.Invoke();
            };
            return mi;
        }

        public static ContextMenu MakeContextMenu(CoreWindow handler, params MenuItem[] items)
        {
            ContextMenu contextMenu = new ContextMenu(handler, items);
            contextMenu.Effects().Add(new Shadow(5, new Position(3, 3), Palette.BlackShadow));
            return contextMenu;
        }

        public static MenuItem MakeImagedMenuItem(string name, Bitmap bitmap)
        {
            MenuItem menuItem = new MenuItem(name);
            menuItem.SetStyle(StyleFactory.GetMenuItemStyle());
            menuItem.SetTextMargin(new Indents(25, 0, 0, 0));

            // Optionally: set an event on click
            menuItem.EventMouseClick += (sender, args) => {
                PopUpMessage popUpInfo = new PopUpMessage("You choosed a function:\n" + menuItem.GetText());
                popUpInfo.SetStyle(StyleFactory.GetBluePopUpStyle());
                popUpInfo.SetTimeOut(2000);
                popUpInfo.Show(menuItem.GetHandler());
            };

            // Optionally: add an image into MenuItem
            ImageItem img = new ImageItem(bitmap, false);
            img.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            img.SetSize(20, 20);
            img.SetAlignment(ItemAlignment.Left, ItemAlignment.VCenter);
            img.KeepAspectRatio(true);
            menuItem.AddItem(img);

            // Optionally: add a button into MenuItem
            ButtonCore infoBtn = new ButtonCore("?");
            infoBtn.SetBackground(40, 40, 40);
            infoBtn.SetWidth(20);
            infoBtn.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
            infoBtn.SetFontStyle(FontStyle.Bold);
            infoBtn.SetForeground(210, 210, 210);
            infoBtn.SetAlignment(ItemAlignment.VCenter, ItemAlignment.Right);
            infoBtn.SetMargin(0, 0, 10, 0);
            infoBtn.SetBorderRadius(3);
            infoBtn.AddItemState(ItemStateType.Hovered, new ItemState(Color.FromArgb(10, 140, 210)));
            infoBtn.SetPassEvents(false, InputEventType.MousePress, InputEventType.MouseRelease,
                    InputEventType.MouseDoubleClick);
            infoBtn.IsFocusable = false; // prevent focus this button
            infoBtn.EventMouseClick += (sender, args) => {
                PopUpMessage popUpInfo = new PopUpMessage("This is decorated MenuItem:\n" + menuItem.GetText());
                popUpInfo.SetStyle(StyleFactory.GetDarkPopUpStyle());
                popUpInfo.SetTimeOut(2000);
                popUpInfo.Show(infoBtn.GetHandler());
            };
            menuItem.AddItem(infoBtn);

            return menuItem;
        }

        public static Ellipse MakeDot()
        {
            Ellipse ellipse = new Ellipse(12);
            ellipse.SetSize(8, 8);
            ellipse.SetAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
            ellipse.SetMargin(10, 0, 0, 0);
            return ellipse;
        }

        public static Prototype MakeVSpacer(int thickness)
        {
            BlankItem spacer = new BlankItem();
            spacer.SetHeightPolicy(SizePolicy.Expand);
            spacer.SetWidth(thickness);
            spacer.SetBackground(Palette.WhiteGlass);
            return spacer;
        }

        public static Prototype MakeHSpacer(int thickness)
        {
            BlankItem spacer = new BlankItem();
            spacer.SetWidthPolicy(SizePolicy.Expand);
            spacer.SetHeight(thickness);
            spacer.SetBackground(Palette.WhiteGlass);
            return spacer;
        }
    }
}
