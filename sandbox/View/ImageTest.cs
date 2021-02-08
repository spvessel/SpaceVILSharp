using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using SpaceVIL.Common;

namespace View
{
    class ImageTest : ActiveWindow
    {
        override public void InitWindow()
        {
            SetParameters(name: nameof(ImageTest), title: nameof(ImageTest));
            this.SetWidth(500);
            this.SetMinWidth(500);
            this.SetHeight(500);
            // this.SetMinHeight(500);
            this.SetWindowTitle(nameof(ImageTest));
            this.SetBackground(Color.FromArgb(255, 45, 45, 45));
            this.SetPadding(2, 2, 2, 2);
            // this.IsBorderHidden = true;

            //DragAnchor
            // TitleBar title = new TitleBar(nameof(ImageTest));
            TitleBar title = new TitleBar("VCENTER Alignment");
            this.AddItem(title);

            //Frame
            VerticalStack frame = new VerticalStack();
            frame.SetBackground(Color.FromArgb(255, 60, 60, 60));
            frame.SetItemName("Container");
            frame.SetMargin(0, 30, 0, 0);
            frame.SetWidthPolicy(SizePolicy.Expand);
            frame.SetHeightPolicy(SizePolicy.Expand);
            frame.SetSpacing(0, 10);
            frame.SetPadding(30, 10, 30, 10);
            frame.SetContentAlignment(ItemAlignment.VCenter);
            this.AddItem(frame);

            frame.AddItems(new RadioButton("text1"), new RadioButton("text2"));
            frame.AddItems(new CheckBox("text1"), new CheckBox("text2"));

            ButtonCore btn_action = new ButtonCore();
            btn_action.SetBackground(100, 255, 150);
            btn_action.SetText("Columnar");
            btn_action.SetTextMargin(new Indents(0, 0, 0, 3));
            btn_action.SetForeground(Color.Black);
            btn_action.SetItemName("Action");
            btn_action.SetWidth(128);
            btn_action.SetHeight(64);
            // btn_action.SetWidthPolicy(SizePolicy.Expand);
            // btn_action.SetHeightPolicy(SizePolicy.Expand);
            btn_action.SetAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);
            btn_action.SetTextAlignment(ItemAlignment.HCenter | ItemAlignment.Bottom);
            btn_action.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            btn_action.SetBorderRadius(new CornerRadius(10));
            frame.AddItem(btn_action);

            //Image img1 = Image.FromFile("D:\\icon.png");
            Bitmap img1 = new Bitmap("D:\\columnar.png");
            //Image img1 = Image.FromFile("D:\\Source\\GitHub\\spacevil_logo.png");
            //Image img1 = Image.FromFile("D:\\sample.png");
            //Image img1 = Image.FromFile("D:\\icon.jpg");

            ImageItem image = new ImageItem(img1, false);
            image.SetItemName("MissedImage");
            image.SetBackground(Color.Transparent);
            image.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            image.SetAlignment(ItemAlignment.VCenter | ItemAlignment.HCenter);
            image.KeepAspectRatio(true);
            btn_action.AddItem(image);

            btn_action.EventMouseHover += (sender, args) =>
            {
                image.SetColorOverlay(Color.FromArgb(50, 50, 150));
            };
            btn_action.EventMouseLeave += (sender, args) =>
            {
                image.SetColorOverlay(Color.FromArgb(0, 0, 0));
            };
            btn_action.SetToolTip("Refresh image");

            HorizontalSlider slider = new HorizontalSlider();
            slider.SetMaxValue(3);
            slider.SetStep(1);
            slider.SetIgnoreStep(false);
            frame.AddItem(slider);

            // for (int i = 0; i < 4; i++)
            // {
            //     ButtonToggle btn = new ButtonToggle("Button #" + i);
            //     // CheckBox btn = new CheckBox("Button #" + i);
            //     btn.SetWidth(128);
            //     btn.SetHeight(30);
            //     btn.SetBorderRadius(new CornerRadius(10));
            //     btn.SetAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);
            //     // if (i == 3)
            //     //     btn.SetMargin(5, 5, 5, 55);
            //     frame.AddItem(btn);
            // }
            MenuItem filterItem = GetMenuItem("Open Filter Function Menu", DefaultsService.GetDefaultImage(EmbeddedImage.Filter, EmbeddedImageSize.Size32x32));
            MenuItem recycleItem = GetMenuItem("Open Recycle Bin", DefaultsService.GetDefaultImage(EmbeddedImage.RecycleBin, EmbeddedImageSize.Size32x32));
            MenuItem refreshItem = GetMenuItem("Refresh UI", DefaultsService.GetDefaultImage(EmbeddedImage.Refresh, EmbeddedImageSize.Size32x32));
            MenuItem addMenuItemItem = GetMenuItem("Add New Function...", DefaultsService.GetDefaultImage(EmbeddedImage.Add, EmbeddedImageSize.Size32x32));
            MyMenuItem mymi = new MyMenuItem();
            mymi.SetText("MYMI");


            Style style = Style.GetComboBoxStyle();
            style.SetSize(125, 25);
            style.Font = DefaultsService.GetDefaultFont(25);
            style.WidthPolicy = SizePolicy.Fixed;
            Style selectionStyle = style.GetInnerStyle("selection");
            if (selectionStyle != null)
                selectionStyle.SetPadding(30, 0, 0, 0);

            ComboBox combo = new ComboBox(
            filterItem,
            recycleItem,
            refreshItem,
            addMenuItemItem,
            mymi
            );
            combo.SetAlignment(ItemAlignment.VCenter, ItemAlignment.HCenter);
            combo.SetText("Done");
            // combo.SetStyle(style);

            addMenuItemItem.EventMouseClick = (sender, args) =>
            {
                InputDialog inDialog = new InputDialog("Add new function...", "Add", "NewFunction");
                inDialog.OnCloseDialog += () =>
                {
                    if (inDialog.GetResult() != String.Empty)
                        combo.AddItem(GetMenuItem(inDialog.GetResult(), DefaultsService.GetDefaultImage(EmbeddedImage.Import, EmbeddedImageSize.Size32x32)));
                };
                inDialog.Show(addMenuItemItem.GetHandler());
            };

            frame.AddItem(combo);
            // combo.SetStyle(GetComboBoxStyle());
            combo.AddItem(GetDot());
            combo.SelectionChanged += () =>
            {
                Console.WriteLine(combo.GetCurrentIndex());
            };
            // combo.SetCurrentIndex(0);

            // ImageItem img = new ImageItem(DefaultsService.GetDefaultImage(EmbeddedImage.Home, EmbeddedImageSize.Size32x32));
            // img.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            // img.SetSize(20, 20);
            // img.SetAlignment(ItemAlignment.Left, ItemAlignment.VCenter);
            // img.KeepAspectRatio(true);
            // img.SetMargin(5, 0, 0, 0);
            // combo.AddItem(img);

            ToolTip.SetShadow(this, new Shadow(10, new Position(3, 3), Color.Black));
            ToolTip.SetFont(this, DefaultsService.GetDefaultFont(FontStyle.Italic, 18));
            ToolTip.SetBorder(this, new Border(Color.FromArgb(10, 162, 232), new CornerRadius(10), 2));
            ToolTip.SetTimeOut(this, 0);
            EventKeyRelease += (sender, args) =>
            {
                Console.WriteLine(args.Key);
            };
        }

        private MenuItem GetMenuItem(String name, Bitmap bitmap)
        {
            MenuItem menuItem = new MenuItem(name);
            // menuItem.SetStyle(GetMenuItemStyle());
            // menuItem.SetTextMargin(new Indents(25, 0, 0, 0));
            menuItem.EventMouseClick += (sender, args) =>
            {
                Console.WriteLine(menuItem.GetText() + " was clicked!");
            };

            ImageItem img = new ImageItem(bitmap, false);
            img.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            img.SetSize(20, 20);
            img.SetAlignment(ItemAlignment.Left, ItemAlignment.VCenter);
            img.KeepAspectRatio(true);
            menuItem.AddItem(img);

            ButtonCore infoBtn = new ButtonCore("?");
            infoBtn.SetBackground(Color.FromArgb(255, 40, 40, 40));
            infoBtn.SetWidth(20);
            infoBtn.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
            infoBtn.SetFontStyle(FontStyle.Bold);
            infoBtn.SetForeground(210, 210, 210);
            infoBtn.SetAlignment(ItemAlignment.VCenter, ItemAlignment.Right);
            infoBtn.SetMargin(0, 0, 10, 0);
            infoBtn.SetBorderRadius(3);
            infoBtn.AddItemState(ItemStateType.Hovered, new ItemState(Color.FromArgb(0, 140, 210)));
            infoBtn.SetPassEvents(false, InputEventType.MousePress, InputEventType.MouseRelease, InputEventType.MouseDoubleClick);

            infoBtn.EventMouseClick += (sender, args) =>
            {
                PopUpMessage popUpInfo = new PopUpMessage("This is decorated MenuItem:\n" + menuItem.GetText());
                popUpInfo.SetTimeOut(3000);
                popUpInfo.SetHeight(60);
                popUpInfo.SetAlignment(ItemAlignment.Top, ItemAlignment.HCenter);
                popUpInfo.SetMargin(0, 50, 0, 0);
                popUpInfo.Show(infoBtn.GetHandler());
            };

            menuItem.AddItem(infoBtn);

            return menuItem;
        }

        internal static Style GetMenuItemStyle()
        {
            Style style = Style.GetMenuItemStyle();
            style.SetBackground(255, 255, 255, 7);
            style.Foreground = Color.FromArgb(210, 210, 210);
            style.Border.SetRadius(new CornerRadius(3));
            style.AddItemState(ItemStateType.Hovered, new ItemState(Color.FromArgb(45, 255, 255, 255)));
            return style;
        }

        internal static Style GetComboBoxDropDownStyle()
        {
            Style style = Style.GetComboBoxDropDownStyle();
            style.SetBackground(50, 50, 50);
            style.SetBorder(new Border(Color.FromArgb(255, 100, 100, 100), new CornerRadius(0, 0, 5, 5), 1));
            style.SetShadow(new Shadow(10, new Position(3, 3), Color.FromArgb(150, 0, 0, 0)));
            return style;
        }

        internal static Style GetComboBoxStyle()
        {
            Style style = Style.GetComboBoxStyle();
            style.SetBackground(45, 45, 45);
            style.SetForeground(210, 210, 210);
            style.SetBorder(new Border(Color.FromArgb(255, 255, 181, 111), new CornerRadius(10, 0, 0, 10), 2));
            style.SetShadow(new Shadow(10, new Position(3, 3), Color.FromArgb(150, 0, 0, 0)));

            style.RemoveInnerStyle("dropdownarea");
            Style dropDownAreaStyle = GetComboBoxDropDownStyle();
            style.AddInnerStyle("dropdownarea", dropDownAreaStyle);

            Style selectionStyle = style.GetInnerStyle("selection");
            if (selectionStyle != null)
            {
                selectionStyle.Border.SetRadius(new CornerRadius(10, 0, 0, 10));
                selectionStyle.SetBackground(0, 0, 0, 0);
                selectionStyle.SetPadding(25, 0, 0, 0);
                selectionStyle.AddItemState(ItemStateType.Hovered, new ItemState(Color.FromArgb(20, 255, 255, 255)));
            }

            Style dropDownButtonStyle = style.GetInnerStyle("dropdownbutton");
            if (dropDownButtonStyle != null)
            {
                dropDownButtonStyle.Border.SetRadius(new CornerRadius(0, 0, 0, 10));
            }

            return style;
        }

        internal static Ellipse GetDot()
        {
            Ellipse ellipse = new Ellipse(12);
            ellipse.SetSize(8, 8);
            ellipse.SetAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
            ellipse.SetMargin(10, 0, 0, 0);
            return ellipse;
        }
    }
}