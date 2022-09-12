using SpaceVIL;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using View.Decorations;

namespace View.Components
{
    public class ClickablesAndTogglesView: ListBox
    {
        public override void InitElements()
        {
            base.InitElements();

            SetBackground(Palette.Transparent);
            SetSelectionVisible(false);

            ListArea layout = GetArea();
            layout.SetPadding(30, 2, 30, 2);
            layout.SetSpacing(0, 20);

            // Clickables
            ButtonCore button = new ButtonCore("ButtonCore");
            button.EventMouseClick += (sender, args) => {
                System.Console.WriteLine("ButtonCore click!");
            };
            button.SetSize(150, 70);
            button.SetBackground(Palette.Green);
            button.SetBorder(new Border(Palette.White, new CornerRadius(35), 2));
            button.Effects().Add(new Shadow(5, new Position(5, 5), Palette.BlackShadow));

            BlankItem blankItem = new BlankItem();
            blankItem.EventMouseClick += (sender, args) => {
                System.Console.WriteLine("BlankItem click!");
            };
            blankItem.SetSize(150, 70);
            blankItem.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            blankItem.SetBackground(Palette.OrangeLight);
            blankItem.SetBorder(new Border(Palette.Pink, new CornerRadius(35), 4));
            blankItem.Effects().Add(new Shadow(5, new Position(5, 5), Palette.BlackShadow));

            HorizontalStack clickables = new HorizontalStack();
            clickables.SetHeightPolicy(SizePolicy.Fixed);
            clickables.SetHeight(100);
            clickables.SetSpacing(30, 0);
            clickables.SetContentAlignment(ItemAlignment.HCenter);

            // Toggles
            ButtonToggle toggle = new ButtonToggle("ButtonToggle");
            toggle.EventMouseClick += (sender, args) => {
                System.Console.WriteLine("ButtonToggle is toggled: " + toggle.IsToggled());
            };
            toggle.SetSize(150, 70);
            toggle.SetBackground(Palette.Blue);
            toggle.SetBorder(new Border(Palette.White, new CornerRadius(35), 2));
            toggle.Effects().Add(new Shadow(5, new Position(5, 5), Palette.BlackShadow));
            toggle.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);

            HorizontalStack checkables = new HorizontalStack();
            checkables.SetHeightPolicy(SizePolicy.Fixed);
            checkables.SetHeight(150);
            checkables.SetSpacing(30, 0);
            checkables.SetContentAlignment(ItemAlignment.HCenter);

            VerticalStack checkBoxes = new VerticalStack();
            checkBoxes.SetSpacing(0, 10);

            VerticalStack radioButtons = new VerticalStack();
            radioButtons.SetSpacing(0, 10);

            AddItems(ComponentsFactory.MakeHeaderLabel("Examples of clickables:"), clickables,
                    ComponentsFactory.MakeHeaderLabel("Examples of toggles:"), toggle, checkables);
            clickables.AddItems(button, blankItem);
            checkables.AddItems(checkBoxes, radioButtons);
            checkBoxes.AddItems(
                new CheckBox("OpenGL API"),
                new CheckBox("Vulkan API"),
                new CheckBox("DirectX API"),
                new CheckBox("Metal API")
            );
            radioButtons.AddItems(
                new RadioButton("Java"),
                new RadioButton("Kotlin"),
                new RadioButton("C#"),
                new RadioButton("C/C++")
            );
        }
    }
}