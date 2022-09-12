using SpaceVIL;
using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using View.Decorations;

namespace View.Components
{
    class VisualContact : Prototype
    {
        private Label contactName = null;
        public VisualContact(string name)
        {
            SetBackground(80, 80, 80);
            SetSize(300, 60);
            SetAlignment(ItemAlignment.Top, ItemAlignment.Left);
            SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            SetHeight(80);
            SetBorderRadius(10);
            ItemState hover = new ItemState();
            hover.Background = Color.FromArgb(15, 255, 255, 255);
            AddItemState(ItemStateType.Hovered, hover);
            SetPadding(10, 5, 5, 5);
            SetMargin(1, 1, 1, 1);
            Effects().Add(new Shadow(10, new Position(3, 3), Palette.BlackShadow));

            contactName = new Label(name);
        }

        public override void InitElements()
        {
            Ellipse contactPhoto = new Ellipse();
            contactPhoto.SetBackground(Palette.GreenLight);
            contactPhoto.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            contactPhoto.SetSize(70, 70);
            contactPhoto.SetAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
            contactPhoto.Quality = 32;
            contactPhoto.Effects().Add(new Shadow(10, new Position(3, 3), Palette.BlackShadow));

            // contact name
            contactName.SetTextAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
            contactName.SetFontSize(16);
            contactName.SetFontStyle(FontStyle.Bold);
            contactName.SetBackground(255, 255, 255, 32);
            contactName.SetForeground(210, 210, 210);
            contactName.SetHeight(40);
            contactName.SetHeightPolicy(SizePolicy.Fixed);
            contactName.SetWidthPolicy(SizePolicy.Expand);
            contactName.SetPadding(20, 0, 0, 0);
            contactName.SetMargin(80, 0, 15, 10);
            contactName.SetAlignment(ItemAlignment.Bottom, ItemAlignment.Left);
            contactName.SetBorderRadius(20);

            // contact close
            ButtonCore removeContactBtn = new ButtonCore();
            removeContactBtn.SetBackground(40, 40, 40);
            removeContactBtn.SetWidth(14);
            removeContactBtn.SetWidthPolicy(SizePolicy.Fixed);
            removeContactBtn.SetHeight(14);
            removeContactBtn.SetHeightPolicy(SizePolicy.Fixed);
            removeContactBtn.SetAlignment(ItemAlignment.Top, ItemAlignment.Right);
            removeContactBtn.SetMargin(0, 5, 0, 0);
            removeContactBtn.SetCustomFigure(new Figure(false, GraphicsMathService.GetCross(14, 14, 4, 45)));
            removeContactBtn.AddItemState(ItemStateType.Hovered, new ItemState(Palette.WhiteGlass));
            removeContactBtn.EventMouseClick += (sender, args) => DisposeSelf();

            AddItems(contactPhoto, contactName, removeContactBtn);
        }

        public void DisposeSelf()
        {
            GetParent().RemoveItem(this);
        }
    }
}
