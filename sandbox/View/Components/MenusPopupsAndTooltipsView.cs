using SpaceVIL;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using View.Decorations;

namespace View.Components
{
    public class MenusPopupsAndTooltipsView: ListBox
    {
        public override void InitElements()
        {
            base.InitElements();

            SetBackground(Palette.Transparent);
            SetSelectionVisible(false);

            ListArea layout = GetArea();
            layout.SetPadding(30, 2, 30, 2);
            layout.SetSpacing(0, 10);

            // ContextMenu
            MenuItem showSubmenu = ComponentsFactory.MakeMenuItem("Show Submenu", () => {
                System.Console.WriteLine("Selected: Submenu");
            });
            ContextMenu contextMenu = ComponentsFactory.MakeContextMenu(GetHandler(), ComponentsFactory.MakeMenuItem("MenuItem1", () => {
                System.Console.WriteLine("Selected: MenuItem1");
            }), ComponentsFactory.MakeMenuItem("MenuItem2", () => {
                System.Console.WriteLine("Selected: MenuItem2");
            }), ComponentsFactory.MakeMenuItem("MenuItem3", () => {
                System.Console.WriteLine("Selected: MenuItem3");
            }), ComponentsFactory.MakeMenuItem("MenuItem4", () => {
                System.Console.WriteLine("Selected: MenuItem4");
            }), showSubmenu);

            ContextMenu subMenu = ComponentsFactory.MakeContextMenu(GetHandler(), ComponentsFactory.MakeMenuItem("SubMenuItem1", () => {
                System.Console.WriteLine("Selected: SubMenuItem1");
            }), ComponentsFactory.MakeMenuItem("SubMenuItem2", () => {
                System.Console.WriteLine("Selected: SubMenuItem2");
            }), ComponentsFactory.MakeMenuItem("SubMenuItem3", () => {
                System.Console.WriteLine("Selected: SubMenuItem3");
            }), ComponentsFactory.MakeMenuItem("SubMenuItem4", () => {
                System.Console.WriteLine("Selected: SubMenuItem4");
            }));

            showSubmenu.AssignContextMenu(subMenu);

            HorizontalStack contextMenuContainer = new HorizontalStack();
            contextMenuContainer.SetHeightPolicy(SizePolicy.Fixed);
            contextMenuContainer.SetHeight(100);
            contextMenuContainer.SetSpacing(30, 0);
            contextMenuContainer.SetContentAlignment(ItemAlignment.HCenter);

            Prototype openContextMenuL = ComponentsFactory.MakeActionButton("LeftClick", Palette.GreenLight, () => {
                contextMenu.ActiveButton = MouseButton.ButtonLeft;
            });
            openContextMenuL.SetBackground(Palette.OrangeLight);
            openContextMenuL.SetToolTip("Left click to open the ContextMenu.");
            openContextMenuL.EventMouseClick += (sender, args) => {
                contextMenu.Show(sender, args);
            };
            Prototype openContextMenuR = ComponentsFactory.MakeActionButton("RightClick", Palette.GreenLight, () => {
                contextMenu.ActiveButton = MouseButton.ButtonRight;
            });
            openContextMenuR.SetBackground(Palette.Purple);
            openContextMenuR.SetToolTip("Right click to open the ContextMenu.");
            openContextMenuR.EventMouseClick += (sender, args) => {
                contextMenu.Show(sender, args);
            };
            Prototype openContextMenuPositioned = ComponentsFactory.MakeActionButton("Positioned", Palette.GreenLight, () => {
                contextMenu.ActiveButton = MouseButton.ButtonLeft;
            });
            openContextMenuPositioned.SetToolTip("Left click to open the ContextMenu\nat the start/bottom of the button. ");
            openContextMenuPositioned.SetBackground(Palette.Pink);
            openContextMenuPositioned.EventMouseClick += (sender, args) => {
                args.Position.SetPosition(openContextMenuPositioned.GetX(),
                        openContextMenuPositioned.GetY() + openContextMenuPositioned.GetHeight());
                contextMenu.Show(sender, args);
            };

            // ComboBox
            MenuItem filterItem = ComponentsFactory.MakeImagedMenuItem("Open Filter Function Menu",
                    DefaultsService.GetDefaultImage(EmbeddedImage.Filter, EmbeddedImageSize.Size32x32));

            MenuItem recycleItem = ComponentsFactory.MakeImagedMenuItem("Open Recycle Bin",
                    DefaultsService.GetDefaultImage(EmbeddedImage.RecycleBin, EmbeddedImageSize.Size32x32));

            MenuItem refreshItem = ComponentsFactory.MakeImagedMenuItem("Refresh UI",
                    DefaultsService.GetDefaultImage(EmbeddedImage.Refresh, EmbeddedImageSize.Size32x32));

            MenuItem addMenuItemItem = ComponentsFactory.MakeImagedMenuItem("Add New Function...",
                    DefaultsService.GetDefaultImage(EmbeddedImage.Add, EmbeddedImageSize.Size32x32));

            // ComboBox
            ComboBox comboBox = new ComboBox(filterItem, recycleItem, refreshItem, addMenuItemItem);
            comboBox.SetAlignment(ItemAlignment.VCenter, ItemAlignment.HCenter);
            comboBox.SetText("Operations");
            comboBox.SetStyle(StyleFactory.GetComboBoxStyle());
            comboBox.SetMargin(0, 35, 0, 35);

            // Change event for "addMenuItemItem" - add a new MenuItem into ComboBox
            addMenuItemItem.EventMouseClick = null;
            addMenuItemItem.EventMouseClick += (sender, args) => {
                InputDialog inDialog = new InputDialog("Add new function...", "Add", "NewFunction");
                inDialog.OnCloseDialog += () => {
                    if (inDialog.GetResult() != "")
                    {
                        comboBox.AddItem(ComponentsFactory.MakeImagedMenuItem(inDialog.GetResult(),
                                DefaultsService.GetDefaultImage(EmbeddedImage.Import, EmbeddedImageSize.Size32x32)));
                    }
                };
                inDialog.Show(addMenuItemItem.GetHandler());
            };

            // Popups
            HorizontalStack popupsContainer = new HorizontalStack();
            popupsContainer.SetHeightPolicy(SizePolicy.Fixed);
            popupsContainer.SetHeight(100);
            popupsContainer.SetSpacing(30, 0);
            popupsContainer.SetContentAlignment(ItemAlignment.HCenter);

            Prototype openPopup = ComponentsFactory.MakeActionButton("Popup", Palette.BlueLight, () => {
                PopUpMessage popUpMessage = new PopUpMessage("Message: This is PopUpMesage item.");
                popUpMessage.SetTimeOut(1500);
                popUpMessage.Show(GetHandler());
            });

            SideArea sideArea = new SideArea(GetHandler(), Side.Right);
            sideArea.SetAreaSize(350);
            Prototype openSideArea = ComponentsFactory.MakeActionButton("SideArea", Palette.GreenLight, () => {
                sideArea.SetAttachSide(Side.Right);
                sideArea.Show();
            });

            FloatItem floatItem = new FloatItem(GetHandler());
            floatItem.SetPassEvents(false);
            floatItem.SetSize(300, 250);
            floatItem.SetBackground(Palette.Green);
            floatItem.Effects().Add(new Shadow(5, new Position(3, 3), Palette.BlackShadow));
            Prototype openFloatItem = ComponentsFactory.MakeActionButton("FloatItem", Palette.Orange, () => {
                floatItem.Show();
            });
            Prototype hideFloatItem = ComponentsFactory.MakeActionButton("Close", Palette.Gray, () => {
                floatItem.Hide();
            });

            AddItems(ComponentsFactory.MakeHeaderLabel("Context menu implementation:"), contextMenuContainer,
                    ComponentsFactory.MakeHeaderLabel("ComboBox implementation:"), comboBox,
                    ComponentsFactory.MakeHeaderLabel("Popup implementations:"), popupsContainer);
            contextMenuContainer.AddItems(openContextMenuL, openContextMenuPositioned, openContextMenuR);
            popupsContainer.AddItems(openPopup, openSideArea, openFloatItem);

            // Decorate our ComboBox with a white dot
            comboBox.AddItem(ComponentsFactory.MakeDot());

            // Optionally: set start index (this method should only be called if ComboBox
            // has already been added)
            comboBox.SetCurrentIndex(0);

            floatItem.AddItem(hideFloatItem);
        }
    }
}