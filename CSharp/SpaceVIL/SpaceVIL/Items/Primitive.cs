using System.Collections.Generic;
using System.Drawing;

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
            if (style.Shape != null)
                SetTriangles(style.Shape);
            IsVisible = style.IsVisible;
        }
    }
}
