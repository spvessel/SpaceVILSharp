using System.Collections.Generic;
using System.Drawing;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    abstract public class Primitive : BaseItem
    {
        public Primitive(
            int xpos = 0,
            int ypos = 0,
            int width = 0,
            int height = 0,
            string name = "Primitive_")
        {
            SetItemName(name);
        }

        public override List<float[]> MakeShape()
        {
            return GetTriangles();
        }

        public void SetPosition(int _x, int _y)
        {
            this.SetX(_x);
            this.SetY(_y);
        }

        //style
        internal bool _is_style_set = false;
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;

            _is_style_set = true;
            SetBackground(style.Background);
            SetSizePolicy(style.WidthPolicy, style.HeightPolicy);
            SetSize(style.Width, style.Height);
            SetMinSize(style.MinWidth, style.MinHeight);
            SetMaxSize(style.MaxWidth, style.MaxHeight);
            SetAlignment(style.Alignment);
            SetPosition(style.X, style.Y);
            SetMargin(style.Margin);
            SetVisible(style.IsVisible);
            if (style.Shape != null)
                SetTriangles(style.Shape);
        }
        public override Style GetCoreStyle()
        {
            Style style = new Style();
            style.SetSize(GetWidth(), GetHeight());
            style.SetSizePolicy(GetWidthPolicy(), GetHeightPolicy());
            style.Background = GetBackground();
            style.MinWidth = GetMinWidth();
            style.MinHeight = GetMinHeight();
            style.MaxWidth = GetMaxWidth();
            style.MaxHeight = GetMaxHeight();
            style.X = GetX();
            style.Y = GetY();
            style.Margin = new Indents(GetMargin().Left, GetMargin().Top, GetMargin().Right, GetMargin().Bottom);
            style.Alignment = GetAlignment();
            style.IsVisible = IsVisible();

            return style;
        }
        
        public override void InitElements() { }
    }
}
