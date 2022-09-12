using System.Drawing;
using SpaceVIL;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using View.Decorations;

namespace View.Components
{
    public class BlockNote : ResizableItem {

        private ButtonCore paletteBtn;
        private ButtonToggle lockToggableBtn;
        private TextArea textArea;
        private Label note;
        private ButtonCore closeBtn;
        private ContextMenu colorMenu;

        public BlockNote()
        {
            SetPassEvents(false);
            SetBackground(45, 45, 45);
            SetPadding(4, 4, 4, 4);
            SetBorderRadius(4);
            Effects().Add(new Shadow(10, new Position(3, 3), Palette.BlackShadow));

            paletteBtn = new ButtonCore();
            paletteBtn.SetPassEvents(false);
            lockToggableBtn = new ButtonToggle();
            textArea = new TextArea();
            note = new Label();
        }

        public override void InitElements()
        {
            paletteBtn.SetAlignment(ItemAlignment.Right, ItemAlignment.Top);
            paletteBtn.SetMargin(0, 40, 0, 0);
            paletteBtn.SetSize(20, 15);
            paletteBtn.SetBackground(255, 128, 128);
            paletteBtn.SetBorderRadius(0);
            paletteBtn.SetBorderRadius(3);
            CustomShape arrow = new CustomShape();
            arrow.SetTriangles(GraphicsMathService.GetTriangle(30, 30, 0, 0, 180));
            arrow.SetBackground(50, 50, 50);
            arrow.SetSize(14, 6);
            arrow.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            arrow.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);

            lockToggableBtn.SetAlignment(ItemAlignment.Left, ItemAlignment.Top);
            lockToggableBtn.SetSize(15, 15);
            lockToggableBtn.SetBorderRadius(8);
            lockToggableBtn.EventToggle += (sender, args) => {
                IsLocked = !IsLocked;
                textArea.SetEditable(!textArea.IsEditable());
            };

            VerticalScrollBar vs = textArea.VScrollBar;
            vs.Slider.Handler.RemoveAllItemStates();
            vs.SetArrowsVisible(false);
            vs.SetBackground(151, 203, 255);
            vs.SetPadding(0, 2, 0, 2);
            vs.Slider.Track.SetBackground(0, 0, 0, 0);
            vs.Slider.Handler.SetBorderRadius(3);
            vs.Slider.Handler.SetBackground(80, 80, 80, 255);
            vs.Slider.Handler.SetMargin(new Indents(5, 0, 5, 0));

            textArea.SetBorderRadius(new CornerRadius(3));
            textArea.SetWrapText(true);
            textArea.SetHScrollBarPolicy(VisibilityPolicy.Never);
            textArea.SetHeight(25);
            textArea.SetAlignment(ItemAlignment.Left, ItemAlignment.Bottom);
            textArea.SetBackground(151, 203, 255);
            textArea.SetMargin(0, 60, 0, 0);

            note.SetForeground(180, 180, 180);
            note.SetHeight(25);
            note.SetAlignment(ItemAlignment.Left, ItemAlignment.Top);
            note.SetTextAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
            note.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            note.SetText("Add a Note:");
            note.SetMargin(0, 30, 0, 0);

            closeBtn = new ButtonCore();
            closeBtn.SetBackground(100, 100, 100);
            closeBtn.SetItemName("Close_" + GetItemName());
            closeBtn.SetSize(10, 10);
            closeBtn.SetMargin(0, 0, 0, 0);
            closeBtn.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            closeBtn.SetAlignment(ItemAlignment.Top, ItemAlignment.Right);
            ItemState hovered = new ItemState(Color.FromArgb(80, 255, 255, 255));
            closeBtn.AddItemState(ItemStateType.Hovered, hovered);
            closeBtn.SetCustomFigure(new Figure(false, GraphicsMathService.GetCross(10, 10, 3, 45)));
            closeBtn.EventMouseClick += (sender, args) => Dispose();

            AddItems(lockToggableBtn, note, textArea, paletteBtn, closeBtn);

            paletteBtn.AddItem(arrow);

            colorMenu = new ContextMenu(GetHandler());
            colorMenu.SetBackground(60, 60, 60);
            MenuItem red = new MenuItem("Red");
            red.SetForeground(210, 210, 210);
            red.EventMouseClick += (sender, args) => {
                textArea.SetBackground(255, 196, 196);
                textArea.VScrollBar.SetBackground(textArea.GetBackground());
            };
            MenuItem green = new MenuItem("Green");
            green.SetForeground(210, 210, 210);
            green.EventMouseClick += (sender, args) => {
                textArea.SetBackground(138, 255, 180);
                textArea.VScrollBar.SetBackground(textArea.GetBackground());
            };
            MenuItem blue = new MenuItem("Blue");
            blue.SetForeground(210, 210, 210);
            blue.EventMouseClick += (sender, args) => {
                textArea.SetBackground(151, 203, 255);
                textArea.VScrollBar.SetBackground(textArea.GetBackground());
            };
            MenuItem yellow = new MenuItem("Yellow");
            yellow.SetForeground(210, 210, 210);
            yellow.EventMouseClick += (sender, args) => {
                textArea.SetBackground(234, 232, 162);
                textArea.VScrollBar.SetBackground(textArea.GetBackground());
            };
            colorMenu.AddItems(red, green, blue, yellow);
            paletteBtn.EventMouseClick += (sender, args) => colorMenu.Show(sender, args);
            colorMenu.ActiveButton = MouseButton.ButtonLeft;
        }

        public void Dispose()
        {
            GetParent().RemoveItem(this);
        }
    }
}