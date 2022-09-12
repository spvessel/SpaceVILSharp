using SpaceVIL;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using View.Decorations;
using FontStyle = System.Drawing.FontStyle;

namespace View.Components
{
    public class StyleExampleView : VerticalStack
    {
        public StyleExampleView()
        {
            // add style for our StyledComponent class to the current theme
            DefaultsService.GetDefaultTheme().AddDefaultCustomItemStyle(typeof(StyledComponent), GetStyledComponentStyle());
        }

        public override void InitElements()
        {
            base.InitElements();
            // adding default StyledComponent
            AddItems(new StyledComponent(), new StyledComponent());

            // changing default registered style for StyleExampleView
            // get style from current theme
            var styledComponentStyle = DefaultsService.GetDefaultTheme().GetThemeStyle(typeof(StyledComponent));
            if (styledComponentStyle != null)
            {
                styledComponentStyle.SetAlignment(ItemAlignment.Right, ItemAlignment.VCenter);
                styledComponentStyle.SetBorder(Palette.White, new CornerRadius(30, 3, 30, 3), 2);
                styledComponentStyle.Background = Palette.Blue;
                styledComponentStyle.MaxWidth = 280;
                styledComponentStyle.AddItemState(ItemStateType.Hovered, new ItemState(Palette.WhiteGlass));
                var textStyle = styledComponentStyle.GetInnerStyle("text");
                if (textStyle != null)
                {
                    textStyle.SetTextAlignment(ItemAlignment.Right, ItemAlignment.VCenter);
                    textStyle.Font = DefaultsService.GetDefaultFont(FontStyle.Italic, 24);
                }
            }

            // adding StyledComponent with changed global style
            AddItems(new StyledComponent(), new StyledComponent());

            // setting style manualy for current item
            var manuallyStyledItem = new StyledComponent();

            // style: an easier way is to get the current style from the theme and change it
            var newStyle = DefaultsService.GetDefaultStyle(typeof(StyledComponent));
            newStyle.Background = Palette.Purple;
            newStyle.SetBorder(Palette.White, new CornerRadius(30, 30, 30, 30), 2);
            //setting style
            manuallyStyledItem.SetStyle(newStyle);

            AddItem(manuallyStyledItem);
        }

        internal Style GetStyledComponentStyle()
        {
            Style style = new Style();
            style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            style.Height = 60;
            style.SetSpacing(0, 5);
            style.SetMargin(100, 10, 100, 10);
            style.SetPadding(5, 5, 5, 5);
            style.SetAlignment(ItemAlignment.Left, ItemAlignment.Top);
            style.Background = Palette.Green;
            style.SetShadow(new Shadow(5, Palette.BlackShadow));
            style.SetBorder(Palette.White, new CornerRadius(10, 20, 30, 0), 2);

            // inner styles: StyledComponent consist of Label
            // style for Label
            Style textStyle = new Style();
            textStyle.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            textStyle.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            textStyle.SetTextAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            textStyle.Height = 60;
            textStyle.Foreground = Palette.Black;
            textStyle.Background = Palette.Transparent;
            textStyle.Font = DefaultsService.GetDefaultFont(FontStyle.Bold, 24);
            style.AddInnerStyle("text", textStyle);
            
            return style;
        }
    }

    internal class StyledComponent : Prototype
    {
        private Label _text = new Label("StyledComponent");
        public StyledComponent()
        {
            SetStyle(DefaultsService.GetDefaultStyle(typeof(StyledComponent)));
        }

        public override void InitElements()
        {
            base.InitElements();
            AddItem(_text);
        }

        
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);

            var innerStyle = style.GetInnerStyle("text");
            if (innerStyle != null)
            {
                _text.SetStyle(innerStyle);
            }
        }
    }
}