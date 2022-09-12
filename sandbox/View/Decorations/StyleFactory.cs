using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace View.Decorations
{
    public static class StyleFactory
    {
        public static Style GetMenuItemStyle()
        {
            // get current style of an item and change it
            Style style = Style.GetMenuItemStyle();
            style.SetBackground(255, 255, 255, 7);
            style.Foreground = Color.FromArgb(210, 210, 210);
            style.Border.SetRadius(new CornerRadius(3));
            style.AddItemState(ItemStateType.Hovered, new ItemState(Palette.WhiteGlass));
            return style;
        }

        public static Style GetComboBoxDropDownStyle()
        {
            // get current style of an item and change it
            Style style = Style.GetComboBoxDropDownStyle();
            style.SetBackground(50, 50, 50);
            style.SetBorder(new Border(Color.FromArgb(100, 100, 100), new CornerRadius(0, 0, 5, 5), 1));
            style.SetShadow(new Shadow(10, new Position(3, 3), Palette.BlackShadow));
            return style;
        }

        public static Style GetComboBoxStyle()
        {
            // get current style of an item and change it
            Style style = Style.GetComboBoxStyle();
            style.SetBackground(45, 45, 45);
            style.SetForeground(210, 210, 210);
            style.SetMaxSize(400, 40);
            style.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            style.SetBorder(new Border(Color.FromArgb(255, 181, 111), new CornerRadius(10, 0, 0, 10), 2));
            style.SetShadow(new Shadow(10, new Position(3, 3), Palette.BlackShadow));

            // Note: every complex item has a few inner styles for its children
            // for example ComboBox has drop down area, selection item, drob down button
            // (with arrow)
            // Replace inner style
            style.RemoveInnerStyle("dropdownarea");
            Style dropDownAreaStyle = GetComboBoxDropDownStyle(); // get our own style
            style.AddInnerStyle("dropdownarea", dropDownAreaStyle);

            // Change inner style
            Style selectionStyle = style.GetInnerStyle("selection");
            if (selectionStyle != null)
            {
                selectionStyle.Border.SetRadius(new CornerRadius(10, 0, 0, 10));
                selectionStyle.SetBackground(0, 0, 0, 0);
                selectionStyle.SetPadding(25, 0, 0, 0);
                selectionStyle.AddItemState(ItemStateType.Hovered, new ItemState(Palette.WhiteGlass));
            }

            // Change inner style
            Style dropDownButtonStyle = style.GetInnerStyle("dropdownbutton");
            if (dropDownButtonStyle != null)
                dropDownButtonStyle.Border.SetRadius(new CornerRadius(0, 0, 0, 10));

            return style;
        }

        public static Style GetBluePopUpStyle()
        {
            // get current style of an item and change it
            Style style = Style.GetPopUpMessageStyle();
            style.SetBackground(10, 162, 232);
            style.SetForeground(0, 0, 0);
            style.Height = 60;
            style.Border.SetRadius(new CornerRadius(12));
            style.SetAlignment(ItemAlignment.Bottom, ItemAlignment.Right);
            style.SetMargin(0, 0, 50, 50);
            style.SetShadow(new Shadow(5, new Position(3, 3), Palette.DarkGlass));

            // Change inner style
            Style closeButtonStyle = style.GetInnerStyle("closebutton");
            if (closeButtonStyle != null)
            {
                closeButtonStyle.SetBackground(10, 10, 10, 255);
                closeButtonStyle.AddItemState(ItemStateType.Hovered, new ItemState(Palette.White));
            }

            return style;
        }

        public static Style GetDarkPopUpStyle()
        {
            // get current style of an item and change it
            Style style = Style.GetPopUpMessageStyle();
            style.Height = 60;
            style.SetAlignment(ItemAlignment.Bottom, ItemAlignment.Right);
            style.SetMargin(0, 0, 50, 50);
            style.SetShadow(new Shadow(5, new Position(3, 3), Palette.DarkGlass));
            return style;
        }
    }
}