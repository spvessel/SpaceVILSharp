using SpaceVIL;
using SpaceVIL.Core;
using View.Decorations;

namespace View.Components
{
    public class CustomElementsView: ListBox
    {
        public override void InitElements()
        {
            base.InitElements();
            SetBackground(Palette.Transparent);
            SetSelectionVisible(false);
            ListArea layout = GetArea();
            layout.SetPadding(30, 2, 30, 2);
            layout.SetSpacing(0, 10);

            HorizontalStack blockNotes = new HorizontalStack();
            blockNotes.SetBackground(Palette.Glass);
            blockNotes.SetHeightPolicy(SizePolicy.Fixed);
            blockNotes.SetHeight(350);
            blockNotes.SetContentAlignment(ItemAlignment.HCenter);
            blockNotes.SetSpacing(20, 0);
            blockNotes.SetPadding(20, 20, 20, 20);

            BlockNote bn1 = new BlockNote();
            bn1.IsLocked = true;
            bn1.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
            bn1.SetWidth(300);
            BlockNote bn2 = new BlockNote();
            bn2.IsLocked = true;
            bn2.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
            bn2.SetWidth(300);

            AddItems(ComponentsFactory.MakeHeaderLabel("Custom element - BlockNote:"), blockNotes,
                    ComponentsFactory.MakeHeaderLabel("Custom element - VisualContact:"), new VisualContact("Jonh Smith"),
                    new VisualContact("Rebecca Starling"), new VisualContact("Tom the Cat"));
            blockNotes.AddItems(bn1, bn2);
        }
    }
}